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

package top.lolosia.vrc.led.util

import com.fasterxml.jackson.databind.json.JsonMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponseException
import top.lolosia.vrc.led.config.ParentConfig
import top.lolosia.vrc.led.config.SConfig

private val mapper = JsonMapper()

fun success(msg: Any? = "success"): ResponseEntity<ByteArray> {
    val obj = mapper.writeValueAsBytes(msg)
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(obj)
}

fun html(html: String): ResponseEntity<String>{
    return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html)
}

/**
 * Constructor with a given message
 */
fun ErrorResponseException(status: HttpStatusCode, msg: String, e: Throwable? = null): ErrorResponseException {
    return ErrorResponseException(status, ProblemDetail.forStatusAndDetail(status, msg), e)
}

/** 获取所在包的Logger */
@Suppress("NOTHING_TO_INLINE")
inline fun packageLogger() = packageLogger("PackageKt")

/**
 * 获取所在包的Logger
 * @param className 指定一个新的类名
 */
@Suppress("NOTHING_TO_INLINE")
inline fun packageLogger(className: String): Logger {
    return LoggerFactory.getLogger(object {}::class.java.packageName + ".$className")
}

/** 获取所在类的Logger */
@JvmName("packageLoggerReified")
inline fun <reified T> packageLogger(): Logger = LoggerFactory.getLogger(T::class.java)

/**
 * 判断当前是否是客户端状态
 */
val isClient get() = SConfig.host.serviceParent.mode == ParentConfig.HostType.CLIENT