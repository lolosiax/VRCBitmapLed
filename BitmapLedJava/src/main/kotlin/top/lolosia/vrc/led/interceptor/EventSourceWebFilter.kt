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

package top.lolosia.vrc.led.interceptor

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import top.lolosia.vrc.led.manager.EventSourceManager
import top.lolosia.vrc.led.util.bundle.bundleScope
import top.lolosia.vrc.led.util.ebean.toUuid
import top.lolosia.vrc.led.util.event.Event
import top.lolosia.vrc.led.util.event.EventHandle
import top.lolosia.vrc.led.util.spring.EventSource
import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.asMono
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.apache.commons.logging.LogFactory
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.seconds

abstract class EventSourceWebFilter(order: Int) : AbstractWebFilter(order) {

    val logger = LogFactory.getLog(EventSourceWebFilter::class.java)

    @Autowired
    lateinit var manager: EventSourceManager

    var mapper: JsonMapper = JsonMapper().apply {
        registerModule(JavaTimeModule())
    }

    val wrapper get() = DefaultDataBufferFactory.sharedInstance

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val req = exchange.request
        if (req.headers.getFirst("x-upgrade") == "event-source") {
            return mono {
                val id = manager.createEventSource(exchange.request)
                val data = bundleScope {
                    "data" set id.toString()
                    "code" set 200
                }
                val bytes = mapper.writeValueAsBytes(data)
                val buffer = wrapper.wrap(bytes)
                exchange.response.headers.apply {
                    contentType = MediaType.APPLICATION_JSON
                    contentLength = bytes.size.toLong()
                }
                exchange.response.writeWith(Mono.just(buffer)).awaitSingleOrNull()
            }.then()
        } else if ("eventSourceId" in req.queryParams) {
            val id = req.queryParams.getFirst("eventSourceId")!!.toUuid()
            return if ("last-event-id" in req.headers) {
                val resp = Response(exchange.response, id)
                resp.writerStatus.asMono(Dispatchers.Default).then()
            } else {
                val (req1, body) = manager.getRequest(id)
                    ?: throw NoSuchElementException("no event source $id found!")
                val exchange1 = exchange.mutate()
                    .request(Request(req1, body, id))
                    .response(Response(exchange.response, id, true))
                    .build()
                chain.filter(exchange1).doOnError { e ->
                    manager.getEventSource(id)?.let {
                        it.send("error", e.message.toString())
                        it.close()
                    }
                }
            }
        } else return chain.filter(exchange)
    }

    inner class Request(
        delegate: ServerHttpRequest,
        private val data: ByteArray,
        id: UUID,
    ) : ServerHttpRequestDecorator(delegate) {
        private val mHeaders = HttpHeaders.writableHttpHeaders(delegate.headers)

        init {
            mHeaders["event-source-id"] = id.toString()
        }

        override fun getHeaders(): HttpHeaders {
            return mHeaders
        }

        override fun getBody(): Flux<DataBuffer> {
            return Mono.just<DataBuffer>(wrapper.wrap(data)).flux()
        }
    }

    inner class Response(
        delegate: ServerHttpResponse,
        id: UUID,
        create: Boolean = false
    ) : ServerHttpResponseDecorator(delegate) {
        init {
            var eventSource = (manager.getEventSource(id) as? InnerEventSource)
            if (eventSource != null) {
                eventSource.slink?.complete()
                eventSource.init(this)
            } else {
                eventSource = InnerEventSource(id)
                eventSource.init(this)
                if (create) {
                    manager.setEventSource(id, eventSource)
                } else {
                    eventSource.close()
                }
            }
        }

        lateinit var writerStatus: Job
        val dHeaders get() = delegate.headers

        override fun getHeaders(): HttpHeaders {
            // 后续所有的Headers修改，皆不在影响前面的Headers的内容。
            val headers = HttpHeaders()
            headers.putAll(dHeaders)
            return headers
        }

        fun writeWith0(body: Flux<out DataBuffer>): Mono<Void> {
            var result: Result<Unit>? = null
            var con: Continuation<Unit>? = null
            writerStatus = CoroutineScope(Dispatchers.Default).launch {
                result?.let {
                    if (it.isFailure) throw it.exceptionOrNull()!!
                } ?: suspendCoroutine {
                    con = it
                }
            }
            val flux = body.doOnComplete {
                result = Result.success(Unit)
                con?.resumeWith(result!!)
            }.doOnError {
                result = Result.failure(it)
                con?.resumeWith(result!!)
            }
            return super.writeWith(flux)
        }

        override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
            return writerStatus.asMono(Dispatchers.Default).then()
        }
    }

    private val mInnerEventSourceId = AtomicInteger(0);

    /**
     * 消息源，向客户端发送信息的实现。
     */
    private inner class InnerEventSource(
        override val sseId: UUID
    ) : EventSource {
        private lateinit var flux: Flux<DataBuffer>
        var slink: FluxSink<DataBuffer>? = null
        private var mClosed = false
        override val isClosed: Boolean get() = mClosed

        override val onClose = EventHandle<Event>()

        fun init(response: Response) {
            if (isClosed) throw IllegalStateException("EventSource has been closed!")

            flux = Flux.create {
                // 这个回调在 response.writeWith0().subscribe() 被调用时才会执行。
                slink = it
            }
            response.dHeaders.let {
                it.contentType = MediaType.parseMediaType("text/event-stream")
                it.cacheControl = "no-cache"
                it.connection = listOf("keep-alive")
            }

            // 发生错误后自动关闭请求
            response.writeWith0(flux).doOnError { e ->
                slink = null
                logger.warn("EventSource连接发生异常, SSE-ID: $sseId")
                logger.warn("${e::class.qualifiedName}: ${e.message}")
                CoroutineScope(Dispatchers.Default).launch {
                    delay(60.seconds)
                    if (slink == null) {
                        mClosed = true
                        onClose fire Event(this)
                        manager.removeEventSource(sseId)
                    }
                }
            }.subscribe()

            // 自动Ping消息
            CoroutineScope(Dispatchers.Default).launch {
                while (!mClosed) {
                    slink?.next(wrapper.wrap(":ping ${System.currentTimeMillis()}\n\n".toByteArray()))
                    delay(5.seconds)
                }
            }
        }

        override fun send(event: String, data: Any?, retry: Int) {
            if (mClosed) throw IllegalStateException("EventSource has been closed!")
            // log.info("$event / $data")
            val id = mInnerEventSourceId.incrementAndGet()
            val dataBytes = mapper.writeValueAsString(data)
            val text = "id: ${id}\nevent: ${event}\ndata: ${dataBytes}\nretry: ${retry}\n\n"
            slink?.next(wrapper.wrap(text.toByteArray()))
        }

        override fun close() {
            if (mClosed) return
            send("close")
            slink?.complete()
            slink = null
            onClose fire Event(this)
            manager.removeEventSource(sseId)
            mClosed = true
        }
    }
}