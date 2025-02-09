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

import top.lolosia.vrc.led.manager.SessionManager
import top.lolosia.vrc.led.util.ErrorResponseException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import org.springframework.web.util.pattern.PathPatternParser
import reactor.core.publisher.Mono

abstract class JwtWebFilter(order: Int) : AbstractWebFilter(order) {
    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(JwtWebFilter::class.java)!!
        private val pathPatternParser = PathPatternParser()
        private val apiPattern = pathPatternParser.parse("/api/**")

        // 全路径匹配
        private val ignore = listOf(
            "/api/sse",
            "/api/login",
            "/api/captcha",
            "/api/verify",
            "/api/register",
            "/api/user/updatePassword",
            "/api/user/avatar",
            "/api/client/",
            "/api/resources/checkFiles"
        )

        // 起始匹配
        private val ignoreMatches = listOf(
            "/api/oss/show/",
            "/api/oss/get/",
            "/api/user/avatar/get/",
        )
    }

    @Autowired
    lateinit var sessionManager: SessionManager;

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val path = exchange.request.path.pathWithinApplication();
        var pathStr = path.value();
        if (pathStr.contains('?')) pathStr = pathStr.split('?', limit = 2)[0]
        if (apiPattern.matches(path)) {
            if (ignore.contains(pathStr)) {
                return chain.filter(exchange)
            } else if (ignoreMatches.any { pathStr.startsWith(it) }) {
                return chain.filter(exchange)
            } else {
                val session = sessionManager.mySession(exchange)
                if (session == null) {
                    val e = ErrorResponseException(HttpStatus.UNAUTHORIZED, "身份认证失败，请重新登录")
                    return Mono.error(e)
                }
                return chain.filter(exchange)
            }
        } else {
            return chain.filter(exchange)
        }
    }
}