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

import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundleOf
import top.lolosia.vrc.led.util.property.MutableProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import org.springframework.web.reactive.function.client.exchangeToFlow
import java.io.File

/**
 * ResourcesApi
 * @author 洛洛希雅Lolosia
 * @since 2024-11-09 16:33
 */
object ResourcesApi {

    suspend fun checkFiles(dir: String): List<Bundle>? {
        return post(
            baseUrl,
            "/resources/checkFiles",
            body = bundleOf("dir" to dir),
            raw = true,
        )
    }

    suspend fun downloadFiles(
        file: String,
        target: File,
        byteProcessed : MutableProperty<Long> = MutableProperty(0L),
        byteCount: MutableProperty<Long> = MutableProperty(0L)
    ) {
        val response = WebClient.create(rootUrl)
            .get()
            .uri("/res/$file")
            .accept(MediaType.APPLICATION_OCTET_STREAM)
            .exchangeToFlow {
                if (it.statusCode() != HttpStatus.OK) {
                    throw IllegalStateException("文件下载失败：${it.statusCode()}")
                }
                byteCount.value = it.headers().contentLength().orElse(-1L)
                it.bodyToFlow<ByteArray>()
            }

        withContext(Dispatchers.IO) {
            try {
                target.parentFile.mkdirs()
                target.outputStream().use { fos ->
                    response.collect {
                        @Suppress("BlockingMethodInNonBlockingContext")
                        fos.write(it)
                        byteProcessed.value += it.size
                    }
                }
            } catch (e: Throwable) {
                if (target.exists()) {
                    target.delete()
                }
                throw e
            }
        }
    }
}