/*
 * Copyright (c) 2025 Lolosia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package top.lolosia.vrc.led.manager

import com.fasterxml.jackson.databind.json.JsonMapper
import top.lolosia.vrc.led.annotation.NetworkSync
import top.lolosia.vrc.led.api.SystemApi
import top.lolosia.vrc.led.model.session.SessionEntity
import top.lolosia.vrc.led.model.session.query.QSessionEntity
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundleScope
import top.lolosia.vrc.led.util.bundle.readBundle
import top.lolosia.vrc.led.util.ebean.*
import top.lolosia.vrc.led.util.isClient
import top.lolosia.vrc.led.util.network.Invoker
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.session.IWebExchangeContext
import top.lolosia.vrc.led.util.session.SessionMap
import top.lolosia.vrc.led.util.spring.ApplicationContextProvider
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import java.util.*

@Service
class SessionManager : DisposableBean, CoroutineScope {
    override val coroutineContext get() = Dispatchers.Default
    private val logger = LoggerFactory.getLogger(SessionManager::class.java)

    private val mSessions = mutableMapOf<UUID, SessionMapImpl>()

    @Autowired
    private lateinit var acp: ApplicationContextProvider

    @Autowired
    private lateinit var mapper: JsonMapper

    fun get() = get(UUID.randomUUID().toString())
    operator fun get(sessionId: String) = get(sessionId.toUuid())
    operator fun get(sessionId: UUID): SessionMap {
        var data = mSessions[sessionId] ?: findDatabase(sessionId)
        if (data.isNullOrEmpty()) {
            data = bundleScope {
                "sessionId" set sessionId.toString()
            }.toSession()
            mSessions[sessionId] = data
            acp.createModel<SessionEntity> {
                this.id = sessionId
                this.data = mapper.writeValueAsString(data)
            }.save()
        }
        data["session:lastAccess"] = Date().time
        return data
    }

    fun remove(sessionId: String) = remove(sessionId.toUuid())
    fun remove(sessionId: UUID) {
        synchronized(mSessions) {
            mSessions.remove(sessionId)
        }
        acp.query<QSessionEntity> {
            id.eq(sessionId)

        }.delete()
    }

    val size get() = mSessions.size

    fun getUserSessions(userId: UUID) = getUserSessions(userId.toString())
    fun getUserSessions(userId: String): Map<UUID, SessionMap> {
        synchronized(mSessions) {
            return mSessions.filterValues { it["userId"] == userId }
        }
    }

    fun mySession(context: Context): SessionMap? {
        return if (context is IWebExchangeContext) mySession(context.exchange)
        else mySession(context.sessionId)
    }

    fun mySession(exchange: ServerWebExchange): SessionMap? = mySession(exchange.request)

    /**
     * 获取本次请求的Session
     */
    fun mySession(req: ServerHttpRequest): SessionMap? {
        var auth = req.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return null
        if (auth.contains(' ')) auth = auth.split(' ', limit = 2)[1]

        val regex = let {
            val x = "[0-9a-f]"
            Regex("^$x{8}(-$x{4}){4}$x{8}\$")
        }

        if (!regex.matches(auth)) return null
        return mySession(auth)
    }

    private fun mySession(sessionId: String) = mySession(sessionId.toUuid())
    private fun mySession(sessionId: UUID): SessionMap? {
        return if (sessionId in this) return this[sessionId]
        else null
    }

    operator fun contains(key: String) = contains(key.toUuid())
    operator fun contains(key: UUID): Boolean {
        if (mSessions.containsKey(key)) return true
        return findDatabase(key) != null
    }

    /**
     * 从数据库查找一个session。
     * 若找到，返回它；若找不到或解析失败，返回null
     * @param sessionId SessionID
     */
    private fun findDatabase(sessionId: UUID): SessionMapImpl? {

        if (isClient) {
            return runBlocking {
                val bundle = withTimeout(5000) {
                    SystemApi.mySession(sessionId)
                }
                val session = bundle.toSession()
                synchronized(mSessions) {
                    mSessions[sessionId] = session
                }
                session
            }
        }

        // 非服务器模式，查询数据库

        val entity = acp.query<QSessionEntity> {
            id.eq(sessionId)
            deleted.ne(true)
        }.findOne() ?: return null
        return try {
            val data = SessionMapImpl(mapper.readBundle(entity.data))
            synchronized(mSessions) {
                mSessions[sessionId] = data
            }
            data
        } catch (e: Throwable) {
            logger.error("未能读取${sessionId}的Session信息", e)
            entity.delete()
            null
        }
    }

    fun save(sessionId: String) = save(sessionId.toUuid())
    fun save(sessionId: UUID) {
        val session = mSessions[sessionId] ?: return
        val obj = acp.query<QSessionEntity> {
            id.eq(sessionId)
        }.findOne() ?: acp.createModel<SessionEntity> {
            id = sessionId
        }
        obj.applyDatabase(acp)
        obj.data = mapper.writeValueAsString(session)
        obj.save()
    }

    fun saveAll() {
        if (mSessions.isEmpty()) return
        var saveCounts = 0
        val needRelease = mutableSetOf<UUID>()
        val now = Date().time
        val entities = acp.query<QSessionEntity>().run {
            synchronized(mSessions) {
                id.isIn(mSessions.keys)
                findMap<UUID>()
            }
        }
        synchronized(mSessions) {
            entities += (mSessions.keys - entities.keys).associateBy({ it }) {
                SessionEntity().apply {
                    id = it
                }
            }
            mSessions.forEach { (key, entity) ->
                entities[key]!!.data = mapper.writeValueAsString(entity)
                val lastAccess = entity["session:lastAccess"] as? Long ?: Date().time
                if (lastAccess != entity["session:lastManagerAccess"]) {
                    entity["session:lastManagerAccess"] = lastAccess
                    saveCounts++
                }
                // 查询超时的Session信息，超时时间为5分钟
                if (now - lastAccess > 5 * 60_000) needRelease.add(key)
            }
        }
        acp.database.saveAll(entities.values)
        // 仅在有session需要保存时打印日志
        if (saveCounts > 0) {
            logger.info("Auto saving $saveCounts of sessions.")
        }
        // 释放过时的Session信息，减少内存占用
        if (needRelease.isNotEmpty()) {
            synchronized(mSessions) {
                mSessions -= needRelease
            }
            postUserSessionReleaseSync(needRelease)
            logger.info("There are ${needRelease.size} sessions that have been released.")
        }
    }

    private var saveTimerJob: Job? = null

    @Scheduled(fixedDelay = 10000)
    private fun saveTimer() {
        // 客户端不需要触发Session保存
        if (isClient) return

        if (saveTimerJob != null) return
        saveTimerJob = launch(Dispatchers.IO) {
            try {
                saveAll()
            } catch (e: Throwable) {
                logger.error("自动保存Session信息时发生错误", e)
            } finally {
                saveTimerJob = null
            }
        }
    }

    override fun destroy() {
        try {
            // 正在保存数据时等待其完成后再保存一遍
            val job = saveTimerJob
            if (job != null) runBlocking {
                job.join()
            }
            // 不在此时操作。
            //saveAll()
        } catch (e: Throwable) {
            logger.error("保存Session信息时发生错误", e)
        }
    }

    //
    //  网络同步
    //

    @NetworkSync("user-session")
    private lateinit var postUserSessionSync: Invoker

    @NetworkSync("user-session")
    private fun onUserSessionSync(sessionId: UUID, key: String, value: Any?) {
        val session = mSessions[sessionId] ?: return
        session.netWorkSyncSet(key, value)
    }

    @NetworkSync("user-session-remove")
    private lateinit var postUserSessionRemoveSync: Invoker

    @NetworkSync("user-session-remove")
    private fun onUserSessionRemoveSync(sessionId: UUID, key: String, value: Any?) {
        val session = mSessions[sessionId] ?: return
        session.netWorkSyncRemove(key)
    }

    @NetworkSync("user-session-release")
    private lateinit var postUserSessionReleaseSync: Invoker

    @NetworkSync("user-session-release")
    private fun onUserSessionReleaseSync(sessionId: Set<UUID>) {
        mSessions -= sessionId
    }


    //
    // Session 实体
    //

    private fun Bundle.toSession(): SessionMapImpl {
        return SessionMapImpl(this)
    }


    inner class SessionMapImpl private constructor(
        val map: MutableMap<String, Any?>, var2: Boolean
    ) : SessionMap, Map<String, Any?> by map {
        constructor() : this(mutableMapOf())
        constructor(map: Map<String, Any?>) : this(map.toMutableMap(), true)

        val id
            get() = map["sessionId"]?.toString()?.toUuid()
                ?: throw NoSuchElementException("No SessionId found!")

        val userId
            get() = map["userId"]?.toString()?.toUuid()
                ?: throw NoSuchElementException("No UserId found!")

        fun netWorkSyncSet(key: String, value: Any?) {
            map[key] = value
        }

        fun netWorkSyncRemove(key: String) {
            map.remove(key)
        }

        override operator fun set(key: String, value: Any?) {
            map[key] = value
            postUserSessionSync(id, key, value)
        }

        override fun remove(key: String): Any? {
            val obj = map.remove(key)
            postUserSessionRemoveSync(id, key)
            return obj
        }
    }
}