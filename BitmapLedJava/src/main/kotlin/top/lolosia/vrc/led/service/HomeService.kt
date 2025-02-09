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

package top.lolosia.vrc.led.service

import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.ErrorResponseException
import top.lolosia.vrc.led.BitmapLed
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.session.IWebExchangeContext
import top.lolosia.vrc.led.util.spring.createFileResponse
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.readBytes

@Service
class HomeService {


    final val bootTime = Date()
    val classpathCaches = mutableMapOf<String, ByteArray>()
    val fileCaches = mutableMapOf<String, Pair<ByteArray, Long>>()

    /**
     * 处理平台静态文件
     */
    suspend fun handlePlatform(ctx: Context, platform: String, file: String): ResponseEntity<ByteArray> {
        ctx as IWebExchangeContext
        val paths = listOf(
            "work/web/${platform}/${file}",
            "/static/${platform}/${file}",
            "work/web/${platform}/index.html",
            "/static/${platform}/index.html",
        )

        for (path in paths) {
            val (data, lastModified) = getFileData(path) ?: continue
            val contentType = MediaTypeFactory.getMediaType(path).orElse(MediaType.APPLICATION_OCTET_STREAM)

            return ctx.createFileResponse(
                lastModified,
                CacheControl.noCache(),
                contentType,
                data.size.toLong(),
                data
            )
        }

        throw ErrorResponseException(HttpStatus.NOT_FOUND)
    }

    private fun getFileData(path: String): Pair<ByteArray, Long>? {
        if (path.endsWith("/")) return null

        if (path.startsWith("work/web")) {
            val p = Path(path)
            if (!p.exists() || p.isDirectory()) return null
            // 判断文件是否被缓存且时间戳一致
            if (fileCaches.containsKey(path) && fileCaches[path]?.second == p.toFile().lastModified()){
                return fileCaches[path]
            }

            val pair = p.readBytes() to p.toFile().lastModified()
            fileCaches[path] = pair
            return pair
        } else if (path.startsWith("/static")) {
            if (classpathCaches.containsKey(path)) {
                val data = classpathCaches[path]!!
                return Pair(data, bootTime.time)
            }

            val bytes = BitmapLed::class.java.getResourceAsStream(path)?.readBytes() ?: return null
            classpathCaches[path] = bytes
            return Pair(bytes, bootTime.time)
        }
        return null
    }
}