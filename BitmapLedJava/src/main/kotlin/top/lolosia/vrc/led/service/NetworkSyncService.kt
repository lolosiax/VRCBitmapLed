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

import top.lolosia.vrc.led.config.SConfig
import top.lolosia.vrc.led.event.system.ServerConnectedEvent
import top.lolosia.vrc.led.manager.NetworkSyncManager
import top.lolosia.vrc.led.util.isClient
import top.lolosia.vrc.led.util.property.ComputedProperty
import top.lolosia.vrc.led.util.property.MutableProperty
import top.lolosia.vrc.led.util.property.TriggerProperty
import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.CloseStatus
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.net.ConnectException
import java.net.URI
import kotlin.time.Duration.Companion.seconds

@Service
class NetworkSyncService {

    private val logger = LoggerFactory.getLogger(NetworkSyncService::class.java)!!

    @Autowired
    private lateinit var manager: NetworkSyncManager

    @Autowired
    private lateinit var publisher: ApplicationEventPublisher

    final var serverSocketA: WebSocket? by MutableProperty(null)
        private set

    final var serverSocketB: WebSocket? by MutableProperty(null)
        private set

    private var connectJob: Job? by MutableProperty(null)

    private var mSockets: MutableList<WebSocket> = mutableListOf()
    val sockets by TriggerProperty { mSockets.toList() }
    val server by SConfig.host.serviceParent::selected

    val status by ComputedProperty(::serverSocketA, ::serverSocketB, ::connectJob) {
        if (serverSocketA != null || serverSocketB != null) {
            publisher.publishEvent(ServerConnectedEvent(this))
            return@ComputedProperty Status.CONNECTED
        }
        if (connectJob != null) return@ComputedProperty Status.CONNECTING
        return@ComputedProperty Status.DISCONNECTED
    }

    private fun onConnected(ws: WebSocket) {
        logger.info("WebSocket connected")
        synchronized(mSockets) {
            mSockets.add(ws)
        }
        TriggerProperty.trigger(::sockets)
    }

    private fun onDisconnected(ws: WebSocket, status: CloseStatus) {
        logger.info("WebSocket disconnected: $status")
        synchronized(mSockets) {
            mSockets.remove(ws)
        }
        TriggerProperty.trigger(::sockets)
    }

    private fun handleMessage(ws: WebSocket, ms: WebSocketMessage) {
        var payloads: List<String>? = null
        try {
            val txt = ms.payloadAsText
            payloads = txt.split("\n")
            // 拦截 ping 消息和其他长度不足 2 的消息
            if (payloads.size < 2) return

            logger.debug("REMOTE: ${txt.replace("\n", ", ")}")
            manager.onNetworkSync(payloads[0], payloads[1], *payloads.drop(2).toTypedArray())
        } catch (e: Throwable) {
            logger.warn("网络同步过程中发生异常，同步信息为：${payloads?.joinToString(", ")}", e)
        }
    }

    fun connect(server: String? = null) {
        if (!isClient) throw IllegalStateException("只有客户端模式才可以连接服务器！")
        var selected by SConfig.host.serviceParent::selected
        var records by SConfig.host.serviceParent::records

        if (server != null) {
            selected = server
            if (server !in records) {
                records = records + server
            }
        }
        selected ?: throw IllegalStateException("未选择服务器")
        synchronized(this) {
            if (connectJob != null) return
            val job = Job()
            connectJob = job
            CoroutineScope(job + Dispatchers.IO).launch {
                while (true) {
                    try {
                        doConnect()
                    } catch (e: Error) {
                        logger.error(e.message, e)
                        disconnect()
                    } catch (e: Throwable) {
                        if (e.cause is ConnectException) {
                            logger.warn(e.message)
                        } else {
                            logger.error(e.message, e)
                        }
                    } finally {
                        serverSocketA = null
                        delay(1000)
                    }
                }
            }
        }
    }

    /**
     * 通过双 WebSocket 交错连接保证消息的连接稳定性
     */
    private suspend fun doConnect() {
        server ?: throw IllegalStateException("未选择服务器")
        val jobs = mutableListOf<Job>()
        coroutineScope {
            jobs += doConnect0(this) { serverSocketA = it }
            delay(30.seconds)
            jobs += doConnect0(this) { serverSocketB = it }
        }
        jobs.joinAll()
    }

    private fun doConnect0(scope: CoroutineScope, callback: (WebSocket) -> Unit): Job {
        return scope.launch {
            while (isActive) {
                try {
                    // 连接最大60秒
                    withTimeout(60.seconds) {
                        val client = ReactorNettyWebSocketClient()
                        client.execute(URI("ws://${server}/api/client/")) {
                            mono {
                                val (ws, job) = handleWebSocketSession(it)
                                callback(ws)
                                job.join()
                            }.then()
                        }.awaitSingleOrNull()
                    }
                } catch (_: TimeoutCancellationException) {
                }
            }
        }
    }

    fun disconnect() {
        synchronized(this) {
            connectJob?.cancel()
            connectJob = null
            serverSocketA?.session?.close()?.block()
            serverSocketA = null
            serverSocketB?.session?.close()?.block()
            serverSocketB = null
        }
    }

    suspend fun handleWebSocketSession(ws: WebSocketSession): Pair<WebSocket, Job> {
        val job = Job()
        var closed = false
        var slink: FluxSink<WebSocketMessage>? = null
        val sendFlux: Flux<WebSocketMessage> = Flux.create {
            slink = it
        }
        val handler = WebSocket(ws) { slink!! }
        onConnected(handler)

        var listUpdate = System.currentTimeMillis()
        CoroutineScope(job + Dispatchers.IO).launch {
            while (!closed) {
                delay(2.seconds)
                try {
                    if (listUpdate < System.currentTimeMillis() - 5_000) {
                        ws.close(CloseStatus.create(1000, "TIMED_OUT")).subscribe()
                        job.cancel()
                        return@launch
                    }
                    handler.ping()
                } catch (e: Exception) {
                    logger.warn("An exception occurred during the Ping test", e)
                }
            }
        }


        logger.info("WebSocket Connect ${System.currentTimeMillis()}")

        val job1 = ws.send(sendFlux)
        val job2 = ws.receive().map {
            listUpdate = System.currentTimeMillis()
            handleMessage(handler, it)
        }

        val mono = Flux.zip(job1, job2).then().doFinally {
            logger.info("WebSocket Close ${System.currentTimeMillis()}")
            closed = true
            @Suppress("CallingSubscribeInNonBlockingScope")
            ws.closeStatus().subscribe {
                onDisconnected(handler, it)
            }
        }

        CoroutineScope(job + Dispatchers.IO).launch {
            mono.awaitSingleOrNull()
        }

        return handler to job
    }

    @EventListener(ApplicationStartedEvent::class)
    private fun launch() {
        if (isClient && server != null) {
            connect()
        } else if (isClient) {
            logger.error("未找到可用服务器，请进行配置")
        }
    }

    enum class Status {
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
    }

    class WebSocket(val session: WebSocketSession, slink: () -> FluxSink<WebSocketMessage>) {
        private val slinkGetter = slink
        private var mSlink: FluxSink<WebSocketMessage>? = null

        private val slink: FluxSink<WebSocketMessage>
            get() {
                if (mSlink == null) mSlink = slinkGetter()
                return mSlink!!
            }

        fun send(string: String) {
            synchronized(slink) {
                slink.next(session.textMessage(string))
            }
        }

        fun ping() {
            synchronized(slink) {
                slink.next(
                    session.pingMessage {
                        it.wrap("ping".toByteArray())
                    }
                )
            }
        }
    }
}