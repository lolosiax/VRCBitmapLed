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

import top.lolosia.vrc.led.annotation.HostForward
import top.lolosia.vrc.led.api.rootUrl
import top.lolosia.vrc.led.service.NetworkSyncService
import top.lolosia.vrc.led.util.isClient
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

abstract class HostForwardWebFilter(order: Int) : AbstractWebFilter(order) {

    val logger = LoggerFactory.getLogger(HostForwardWebFilter::class.java)

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    lateinit var requestMapping: RequestMappingHandlerMapping

    private val networkSyncService: NetworkSyncService by lazy {
        applicationContext.getBean(NetworkSyncService::class.java)
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        if (!isClient) {
            return chain.filter(exchange)
        }

        // if (networkSyncService.status != NetworkSyncService.Status.CONNECTED) {
        //     val e = HostForwardException("未连接到服务端")
        //     return Mono.error(e)
        // }
        return mono {
            val method = requestMapping.getHandler(exchange).awaitSingle()
            if (method !is HandlerMethod) {
                return@mono chain.filter(exchange).awaitSingleOrNull()
            }

            val fClass = AnnotationUtils.findAnnotation(method.beanType, HostForward::class.java)?.value
            val fMethod = AnnotationUtils.findAnnotation(method.method, HostForward::class.java)?.value

            // 根据类型判断是否使用转发器
            val mono0 = when {
                fClass == true && fMethod == true -> forward(exchange)
                fClass == true && fMethod == null -> forward(exchange)
                fClass == null && fMethod == true -> forward(exchange)
                else -> null
            }
            if (mono0 == null) {
                return@mono chain.filter(exchange).awaitSingleOrNull()
            }

            try {
                // 等待转发器
                withTimeout(14500){
                    mono0.awaitSingleOrNull()
                }
            } catch (e: TimeoutCancellationException) {
                throw HostForwardException("与教师机连接超时", e)
            }
        }.then()
    }

    private fun forward(exchange: ServerWebExchange): Mono<Void> {
        val rawUri = exchange.request.uri
        var url = rootUrl + rawUri.path
        if (!rawUri.rawQuery.isNullOrEmpty()) url += "?${rawUri.rawQuery}"
        val request = exchange.request
        return WebClient.create()
            .method(request.method) // 使用相同的请求方法
            .uri(url) // 设置目标 URL
            .headers { h ->
                h.addAll(request.headers) // 复制原请求的头部
                val hostAddress = exchange.request.remoteAddress!!.address.hostAddress
                val localAddress = exchange.request.localAddress!!.address.hostAddress
                if ("X-Real-IP" !in h) {
                    h["X-Real-IP"] = hostAddress
                }
                if ("X-Forwarded-Proto" !in h) {
                    h["X-Forwarded-Proto"] = exchange.request.uri.scheme
                }
                val history = h.getOrPut("X-Forwarded-For") {
                    listOf(hostAddress)
                }.toMutableList()
                history.add(localAddress)
            }
            .body(request.body, DataBuffer::class.java)
            .exchangeToMono { body ->
                exchange.response.statusCode = body.statusCode()
                exchange.response.headers.addAll(body.headers().asHttpHeaders())
                val flux = body.bodyToFlux<DataBuffer>()
                exchange.response.writeWith(flux).then()
            }
    }
}

private class HostForwardException(s: String, e: Throwable? = null) : IllegalStateException(s, e), ErrorWebFilter.Warn