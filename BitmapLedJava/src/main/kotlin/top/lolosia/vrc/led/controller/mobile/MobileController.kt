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

package top.lolosia.vrc.led.controller.mobile

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.lolosia.vrc.led.service.mobile.MobileService
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.success

/**
 * MobileController
 * @author 洛洛希雅Lolosia
 * @since 2025-02-19 18:41
 */
@RestController
@RequestMapping("/api/mobile")
class MobileController {

    @Autowired
    lateinit var service: MobileService

    @PostMapping("/server/host")
    suspend fun getServerHost(ctx: Context): List<Bundle> {
        return service.getServerHost(ctx)
    }

    data class SetTextFn(
        val text: String, val start: Int, val end: Int,
        val color: String, val background: Boolean
    )

    @PostMapping("/setText")
    suspend fun setText(ctx: Context, @RequestBody p: SetTextFn): ResponseEntity<ByteArray> {
        service.setText(ctx, p)
        return success()
    }

    @PostMapping("/switchTimer")
    suspend fun switchTimer(ctx: Context): ResponseEntity<ByteArray> {
        return service.switchTimer(ctx)
    }
}