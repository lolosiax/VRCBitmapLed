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

import top.lolosia.vrc.led.model.system.SysCacheEntity
import top.lolosia.vrc.led.model.system.query.QSysCacheEntity
import top.lolosia.vrc.led.util.ebean.*
import top.lolosia.vrc.led.util.spring.ApplicationContextProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@Service
class CacheManager {
    @Autowired
    private lateinit var acp: ApplicationContextProvider

    fun getCacheAccessor(path: String) = Accessor(path)

    operator fun get(path: String) = getCacheAccessor(path)

    inner class Accessor(val path: String) : MutableMap<String, String> {
        val acp get() = this@CacheManager.acp

        fun query() = query { }

        /** 查询缓存，默认不查询过期的缓存记录。  */
        inline fun query(block: QSysCacheEntity.() -> Unit): QSysCacheEntity {
            val query = acp.query<QSysCacheEntity>()
            query.path.eq(path)
            query.expiresAt.gt(Timestamp(System.currentTimeMillis()))
            block(query)
            return query
        }

        /** 创建一个缓存，默认的过期时间是 1 天 */
        inline fun createModel(block: SysCacheEntity.() -> Unit): SysCacheEntity {
            val entity = acp.createModel<SysCacheEntity>()
            entity.path = path
            block(entity)
            return entity
        }

        /** 创建一个缓存并存入数据库，默认的过期时间是 1 天 */
        inline fun create(block: SysCacheEntity.() -> Unit): SysCacheEntity {
            val neo = createModel(block)
            val old = query().tag.eq(neo.tag).findOne()
            if (old != null) {
                old.value = neo.value
                old.expiresAt = neo.expiresAt
                old.update()
                return old
            }
            else {
                neo.insert()
                neo.refresh()
                return neo
            }
        }

        /**
         * 添加一个缓存，默认的过期时间是 1 天
         * @param tag 标签
         * @param value 值
         * @param expiresAt 过期时间
         */
        fun put(tag: String, value: String, expiresAt: Duration): String {
            val entity = create {
                this.tag = tag
                this.value = value
                this.expiresAt = Timestamp(System.currentTimeMillis() + expiresAt.inWholeMilliseconds)
            }
            return entity.value
        }

        override val size: Int
            get() = query().findCount()

        override fun containsKey(key: String): Boolean {
            return query().tag.eq(key).exists()
        }

        override fun containsValue(value: String): Boolean {
            return query().value.eq(value).exists()
        }

        override fun get(key: String): String? {
            return query {
                tag.eq(key)
                createdAt.desc()
            }.findOne()?.value
        }

        override fun isEmpty(): Boolean {
            return query().exists()
        }

        // 只读
        override val entries: MutableSet<MutableMap.MutableEntry<String, String>>
            get() = query {
                createdAt.desc()
                tag.asMapKey()
            }.findMap<String>().mapValues { it.value.value }.toMutableMap().entries

        // 只读
        override val keys: MutableSet<String>
            get() = query {
                setDistinct(true)
                select(tag)
            }.findSingleAttributeSet()

        // 只读
        override val values: MutableCollection<String>
            get() = query { select(value) }.findSingleAttributeSet()

        override fun put(key: String, value: String): String {
            return put(key, value, 1.days)
        }

        override fun putAll(from: Map<out String, String>) {
            val entities = from.map {
                createModel {
                    tag = it.key
                    value = it.value
                    expiresAt = Timestamp(System.currentTimeMillis() + 1.days.inWholeMilliseconds)
                }
            }
            acp.database.transaction {
                acp.database.saveAll(entities, it)
            }
        }

        override fun remove(key: String): String? {
            val out = get(key)
            query().tag.eq(key).delete()
            return out
        }

        override fun clear() {
            acp.database.transaction {
                query().delete(it)
            }
        }
    }
}