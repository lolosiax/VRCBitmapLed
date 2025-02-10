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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import top.lolosia.vrc.led.service.HomeService
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.session.IWebExchangeContext
import java.net.URI
import kotlin.io.path.Path
import kotlin.io.path.exists

@RestController
class HomeController {

    @Autowired
    lateinit var service: HomeService

    @GetMapping("/")
    fun home(resp: ServerHttpResponse): Mono<Void> {
        resp.statusCode = HttpStatus.FOUND
        // dev-jump
        if (Path("static/build.gradle.kts").exists()) {
            resp.headers.location = URI.create("http://localhost:5005/bitmapLed/")
        } else {
            resp.headers.location = URI.create("/bitmapLed/")
        }
        return resp.writeWith(Mono.empty())
    }

    private val Context.sub: String
        get() {
            this@sub as IWebExchangeContext
            return request.path.toString().split("/", limit = 3)[2]
        }

    @GetMapping("/bitmapLed/**")
    suspend fun home(ctx: Context): ResponseEntity<ByteArray> {
        return service.handlePlatform(ctx, "bitmapLed", ctx.sub)
    }
}