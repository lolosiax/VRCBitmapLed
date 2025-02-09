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

package top.lolosia.vrc.led.api

import top.lolosia.vrc.led.model.system.SysUserEntity
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundleOf
import top.lolosia.vrc.led.util.session.Context
import java.util.*

/**
 * SystemApi
 * @author 洛洛希雅Lolosia
 * @since 2024-10-27 15:03
 */
object SystemApi {

    suspend fun mySession(sessionId: UUID): Bundle {
        return post(baseUrl, "/user/mySession", raw = true, sessionId = sessionId)
    }

    suspend fun myRole(ctx: Context): Bundle {
        return post(baseUrl, "/user/myRole", raw = true, ctx = ctx)
    }

    suspend fun myUserInfo(ctx: Context): SysUserEntity {
        return post(baseUrl, "/user/myInfo", raw = true, ctx = ctx)
    }

    suspend fun sse(): SSEResult {
        return sse(baseUrl, "/sse")
    }

    suspend fun registerSseUser(ctx: Context, sseId: UUID) {
        return post(baseUrl, "/sse/registry", body = bundleOf("sseId" to sseId), raw = true, ctx = ctx)
    }
}