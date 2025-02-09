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

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.NullNode
import top.lolosia.vrc.led.util.bundle.bundleScope
import top.lolosia.vrc.led.util.kotlin.pass
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.ErrorResponse
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

abstract class ErrorWebFilter(order: Int) : AbstractWebFilter(order) {
    interface Ignore
    interface Warn
    companion object {
        suspend fun makeError(response: ServerHttpResponse, e: Throwable) {
            return makeError0(response, e)
        }
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return mono {
            try {
                chain.filter(exchange).awaitSingleOrNull()
            } catch (e: Throwable) {
                makeError0(exchange.response, e)
            }
        }.then()
    }
}

private val logger = LoggerFactory.getLogger(ErrorWebFilter::class.java)!!
private val mapper = ObjectMapper()
private val bufferFactory = DefaultDataBufferFactory()

private suspend fun makeError0(response: ServerHttpResponse, e: Throwable) {

    val code = when (e) {
        is HttpStatusCodeException -> e.statusCode
        is ErrorResponse -> e.statusCode
        else -> null
    }
    if (code == HttpStatus.NOT_FOUND) pass
    else if (code == HttpStatus.METHOD_NOT_ALLOWED) pass
    else if (code == HttpStatus.UNAUTHORIZED) pass
    else if (e is ErrorWebFilter.Ignore) pass
    else if (e is ErrorWebFilter.Warn) logger.warn(e::class.qualifiedName + ": " + e.message)
    else if (e::class.qualifiedName == "kotlinx.coroutines.JobCancellationException") pass
    else logger.error(e.message, e)

    val isActive = coroutineScope { isActive }

    // 如果响应体被提交，或者连接被切断，直接返回
    if (response.isCommitted || !isActive) return

    val obj = bundleScope {
        "data" set NullNode.instance
        "msg" set e.message
        "code" set (code?.value() ?: 500)

        // 所有错误列表
        val types = mutableListOf<String>()
        var e1: Throwable? = e;
        while (e1 != null) {
            types += e1::class.java.name
            e1 = e1.cause
        }

        if (types.size == 1) "type" set types[0]
        else "type" set types
    }

    val bytes = mapper.writeValueAsBytes(obj)
    response.apply {
        headers.contentType = MediaType.APPLICATION_JSON
        headers.contentLength = bytes.size.toLong()
        headers.setCacheControl(CacheControl.noCache())
        statusCode = code ?: HttpStatus.INTERNAL_SERVER_ERROR
        writeWith(Mono.just(bufferFactory.wrap(bytes))).awaitSingleOrNull()
    }
}