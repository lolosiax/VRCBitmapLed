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

package top.lolosia.vrc.led.util.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * 延时执行工具。
 *
 * 当在指定时间内连续执行操作时，除最后一次操作外皆被取消。
 * 当最后一次操作的指定时间段内没有新的操作，则执行这一次操作。
 */
class DelayUpdater(
    private val delay: Duration,
    context: CoroutineContext = Dispatchers.Default
) : CoroutineScope {
    override val coroutineContext = context

    private val lastAccess = AtomicInteger(0)
    operator fun invoke(block: suspend () -> Unit) {
        val acc = lastAccess.incrementAndGet()
        launch {
            delay(delay)
            if (acc == lastAccess.get()) block()
        }
    }

    fun now(block: () -> Unit) {
        lastAccess.incrementAndGet()
        block()
    }
}