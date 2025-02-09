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

import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpMethod.*
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

abstract class CorsWebFilter(order: Int) : AbstractWebFilter(order) {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val req = exchange.request
        val resp = exchange.response

        if (req.method != OPTIONS) {
            resp.beforeCommit {
                Mono.fromCallable {
                    val rh = exchange.request.headers
                    resp.headers.apply {
                        remove("Access-Control-Allow-Origin")
                        remove("Access-Control-Allow-Headers")
                        remove("Access-Control-Allow-Methods")

                        accessControlAllowOrigin = rh.origin ?: "*"
                        accessControlAllowHeaders = rh.accessControlRequestHeaders
                        accessControlAllowMethods = listOf(rh.accessControlRequestMethod ?: exchange.request.method)
                    }
                    Unit
                }.then()
            }
            return chain.filter(exchange)
        }

        val rh = exchange.request.headers
        exchange.response.headers.apply {
            accessControlAllowOrigin = rh.origin ?: "*"
            accessControlAllowCredentials = true
            accessControlAllowMethods = listOf(GET, POST, PUT, DELETE, PATCH, TRACE, HEAD)
            accessControlMaxAge = 86400
            accessControlAllowHeaders = rh.accessControlRequestHeaders
        }
        val mono = Mono.just(DefaultDataBufferFactory.sharedInstance.wrap("ok".toByteArray()))
        return exchange.response.writeWith(mono)
    }
}