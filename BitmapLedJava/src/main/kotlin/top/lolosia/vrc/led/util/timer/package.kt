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

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

/**
 * Timer
 *
 * @author 洛洛希雅Lolosia
 * @since 2024-08-04 13:10
 */

typealias TimerCancelHook = () -> Unit

/**
 * 创建一个Timer
 * @param interval 每两次执行的时间间隔，毫秒
 * @param delay 延时启动，毫秒
 * @param fixed 固定更新时间，若为`true`则以每次执行开始的时间计算下次执行起始时间，
 *              若为`false`则以每次执行结束的时间计算下次执行起始的时间
 * @param name 日志系统的名称
 * @param context 选择的调度器
 * @param block 定时器触发回调
 * @return 取消Timer的回调函数
 */
fun timer(
    interval: Long,
    delay: Long = 0,
    fixed: Boolean = true,
    name: Any = "top.lolosia.vrc.led.timer.PackageKt",
    context: CoroutineContext = Dispatchers.Default,
    block: suspend () -> Unit
): TimerCancelHook {
    var active = true
    // 起始时间
    val startTime = System.currentTimeMillis() + delay
    var count = 0
    val logger = when (name) {
        is String -> LoggerFactory.getLogger(name)
        is Class<*> -> LoggerFactory.getLogger(name.name)
        else -> LoggerFactory.getLogger(name::class.java.name)
    }

    suspend fun wait() {
        // 如果不是固定调用时间，直接等待并返回
        if (!fixed) return delay(interval)

        val now = System.currentTimeMillis()
        // startTime + count * interval 是 count 对应的下次调用的时间
        // count + 1 将时间轴向左平移1格，即误差时间。
        // 如果这个误差时间仍然无法命中，则将 count 向后调整1格。
        while ((startTime + (count + 1) * interval) < now) count++
        count++
        // 这个就是正儿八经的下次调用时间了。
        val next = startTime + count * interval
        // 下次更新时间和当前时间的时间差
        delay(next - now)
    }

    CoroutineScope(context).launch {
        // 如果存在等待时间
        if (delay > 0) {
            delay(startTime - System.currentTimeMillis())
        }
        // 循环任务
        while (active) {
            try {
                block()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                logger.error("An exception throws in timer.", e)
            }
            wait()
        }
    }

    return { active = false }
}