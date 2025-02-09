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

package top.lolosia.vrc.led.util.spring

import top.lolosia.vrc.led.util.session.IWebExchangeContext
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import kotlin.time.Duration
import kotlin.time.toJavaDuration

private typealias Builder<T> = suspend ResponseEntity.BodyBuilder.() -> ResponseEntity<T>

/**
 * 创建文件响应，若文件未修改，则直接返回304
 * @param lastModified 文件最后修改时间
 * @param cacheControl 缓存控制
 * @param contentType 文件类型
 * @param contentLength 文件长度
 * @param data 响应体数据
 * @param block 响应体创建回调
 */
suspend fun <T> IWebExchangeContext.createFileResponse(
    lastModified: Long,
    cacheControl: CacheControl? = null,
    contentType: MediaType = MediaType.APPLICATION_OCTET_STREAM,
    contentLength: Long? = null,
    data: T? = null,
    block: Builder<T>? =  null
): ResponseEntity<T> {
    return if (headers.ifModifiedSince == lastModified) {
        ResponseEntity.status(HttpStatus.NOT_MODIFIED).lastModified(lastModified).build()
    } else {
        val builder = ResponseEntity.ok().lastModified(lastModified)
        cacheControl?.let { builder.cacheControl(it) }
        contentLength?.let { builder.contentLength(it) }
        builder.contentType(contentType)
        block?.invoke(builder) ?: builder.body(data)
    }
}

/**
 * 创建文件响应，若文件未修改，则直接返回304
 * @param lastModified 文件最后修改时间
 * @param maxAge 缓存时间
 * @param contentType 文件类型
 * @param contentLength 文件长度
 * @param data 响应体数据
 * @param block 响应体创建回调
 */
suspend fun <T> IWebExchangeContext.createFileResponse(
    lastModified: Long,
    maxAge: Duration,
    contentType: MediaType = MediaType.APPLICATION_OCTET_STREAM,
    contentLength: Long? = null,
    data: T? = null,
    block: Builder<T>? =  null
) = createFileResponse(
    lastModified,
    CacheControl.maxAge(maxAge.toJavaDuration()),
    contentType,
    contentLength,
    data,
    block
)

/**
 * 创建静态文件响应，这一类文件默认为从不更新
 * @param contentType 文件类型
 * @param contentLength 文件长度
 * @param maxAge 缓存时间
 * @param lastModified 文件最后修改时间
 * @param data 响应体数据
 * @param block 响应体创建回调
 */
suspend fun <T> IWebExchangeContext.createStaticFileResponse(
    contentType: MediaType = MediaType.APPLICATION_OCTET_STREAM,
    contentLength: Long? = null,
    maxAge: Duration? = null,
    lastModified: Long? = null,
    data: T? = null,
    block: Builder<T>? =  null,
): ResponseEntity<T> {
    return if (headers.ifModifiedSince > 0) {
        ResponseEntity.status(HttpStatus.NOT_MODIFIED).lastModified(headers.ifModifiedSince).build()
    } else {
        val builder = ResponseEntity.ok().lastModified(lastModified ?: System.currentTimeMillis())
        maxAge?.let { builder.cacheControl(CacheControl.maxAge(it.toJavaDuration())) }
        contentLength?.let { builder.contentLength(it) }
        builder.contentType(contentType)
        block?.invoke(builder) ?: builder.body(data)
    }
}