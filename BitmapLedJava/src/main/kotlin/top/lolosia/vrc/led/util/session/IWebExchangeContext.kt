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

package top.lolosia.vrc.led.util.session

import org.springframework.http.HttpHeaders
import org.springframework.web.server.ServerWebExchange

interface IWebExchangeContext {
    val exchange: ServerWebExchange
    val isWebSocket get() = exchange.request.headers.upgrade == "websocket"
    val request get() = exchange.request
    val response get() = exchange.response
    val headers get() = exchange.request.headers
    val clientCookie get() = exchange.request.cookies
    val serverHost get() = headers.host
    val serverHostString get() = headers[HttpHeaders.HOST]?.first()
    val clientReferer get() = headers[HttpHeaders.REFERER]
    val clientUserAgent get() = headers[HttpHeaders.USER_AGENT]
    val clientAddress get() = exchange.request.remoteAddress!!
    val clientAddressString get() = exchange.request.remoteAddress!!.hostString
}