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

package top.lolosia.vrc.led.util.kotlin

import kotlinx.coroutines.Dispatchers
import org.apache.commons.collections4.BidiMap
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

private val logger by lazy {
    LoggerFactory.getLogger("top.lolosia.vrc.led.util.kotlin.PackageKt")
}

val pass = Unit

fun Instant.toDate(): Date {
    return Date(toEpochMilli())
}

/**
 * 保留n位小数
 * @param fractionDigits n
 */
fun Double.fixed(fractionDigits: Int): String {
    return String.format("%.${fractionDigits}f", this)
}

/**
 * 保留n位小数
 * @param fractionDigits n
 */
fun Float.fixed(fractionDigits: Int): String {
    return String.format("%.${fractionDigits}f", this)
}


fun <T> createContinuation(): Continuation<T> = createContinuation { _, e ->
    if (e != null) {
        logger.error("An exception occurs in Continuation", e)
    }
}

fun <T> createContinuation(block: (result: T?, error: Throwable?) -> Unit?): Continuation<T> {
    return object : Continuation<T> {
        override val context: CoroutineContext
            get() = Dispatchers.Default

        override fun resumeWith(result: Result<T>) {
            result.fold({
                block(it, null)
            }) {
                block(null, it)
            }
        }
    }
}

fun <K, V> Map<K, V>.toBidiMap(): BidiMap<K, V> {
    return DualLinkedHashBidiMap(this)
}