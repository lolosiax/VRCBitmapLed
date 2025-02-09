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

package top.lolosia.vrc.led.service

import top.lolosia.vrc.led.api.SystemApi
import top.lolosia.vrc.led.event.system.SystemEvent
import top.lolosia.vrc.led.manager.EventBusSSEManager
import top.lolosia.vrc.led.util.isClient
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.session.SSEContext
import top.lolosia.vrc.led.util.spring.EventSource
import top.lolosia.vrc.led.util.success
import kotlinx.coroutines.Job
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventBusSSEService {

    private val logger = LoggerFactory.getLogger(EventBusSSEService::class.java)

    @Autowired
    lateinit var manager: EventBusSSEManager

    @Autowired
    lateinit var eventBus: ApplicationEventPublisher

    /**
     * 有用户发起SSE连接
     */
    suspend fun connect(ctx: SSEContext, source: EventSource) {
        var job: Job? = null

        // TODO: SSE在客户端现在仍然是一个不可用状态。
        // if (isClient) {
        //     val result = SystemApi.sse()
        //     // 连接到服务端转发SSE消息
        //     job = CoroutineScope(Dispatchers.IO).launch {
        //         ctx.parentSse = result
        //         result.flow.collect {
        //             logger.info(it.toString())
        //             source.send(it.first, it.second)
        //         }
        //         source.close()
        //         logger.info("SSE disconnected: ${result.id}")
        //     }
        // }
        val meta = EventBusSSEManager.Meta(ctx, source)
        manager[source.sseId] = meta
        source.send("status", "connect successful")
        eventBus.publishEvent(SSEConnectedEvent(this, meta))
        // 等待 EventSource 关闭
        source.await()
        job?.cancel()
        manager.remove(source.sseId)
        eventBus.publishEvent(SSEDisconnectedEvent(this, meta))
    }

    /**
     * 用户登录后将已建立连接的SSE 链接更新用户信息
     */
    suspend fun registerUser(ctx: Context, sseId: UUID): ResponseEntity<ByteArray> {
        manager[sseId]?.let {
            it.ctx.sessionId = ctx.sessionId

            if (isClient) {
                // 服务器下发的SSE-ID并不一致
                SystemApi.registerSseUser(ctx, it.client.sseId)
            }
        }
        return success()
    }

    sealed class SSEConnectEvent(source: Any, val meta: EventBusSSEManager.Meta) : SystemEvent(source) {
        val session get() = meta.ctx
        val client get() = meta.client
    }

    class SSEConnectedEvent(source: Any, meta: EventBusSSEManager.Meta) : SSEConnectEvent(source, meta)
    class SSEDisconnectedEvent(source: Any, meta: EventBusSSEManager.Meta) : SSEConnectEvent(source, meta)
}