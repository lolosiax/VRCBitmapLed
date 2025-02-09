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

package top.lolosia.vrc.led.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import top.lolosia.vrc.led.service.EventBusSSEService
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.session.IWebExchangeContext
import top.lolosia.vrc.led.util.session.SSEContext
import top.lolosia.vrc.led.util.spring.EventSource
import java.util.*

@RestController
@RequestMapping("/api")
class EventBusSSEController {

    @Autowired
    lateinit var service: EventBusSSEService

    @PostMapping("/sse")
    suspend fun connect(ctx: Context, source: EventSource) {
        val ctx1 = object : SSEContext(ctx), IWebExchangeContext {
            override val exchange: ServerWebExchange = (ctx as IWebExchangeContext).exchange
        }
        service.connect(ctx1, source)
    }

    data class RegisterUserFn(val sseId: UUID)

    @PostMapping("/sse/registry")
    suspend fun registerUser(ctx: Context, @RequestBody params: RegisterUserFn): ResponseEntity<ByteArray> {
        return service.registerUser(ctx, params.sseId)
    }
}