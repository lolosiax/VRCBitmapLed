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
package top.lolosia.vrc.led.util.event

import top.lolosia.vrc.led.util.packageLogger

/**
 * 事件控制器
 *
 * @author 一七年夏
 * @since 2021-09-19 13:15
 */
open class EventHandle<E : IEvent> : PublisableEventHandle<E> {

    companion object{
        val logger = packageLogger<EventHandle<*>>()
    }

    private var callbacks = mutableSetOf<IEventRunnable<E>>()
    private var queue = ArrayDeque<() -> Unit>()
    private var running = false
    override fun addListener(runnable: IEventRunnable<E>) {
        if (running) queue.add { callbacks.add(runnable) }
        else callbacks.add(runnable)
    }

    override fun removeListener(runnable: IEventRunnable<E>) {
        if (running) queue.add { callbacks.remove(runnable) }
        else callbacks.remove(runnable)
    }

    override fun publish(event: E) {
        running = true
        try {
            for (runnable in callbacks) {
                try {
                    runnable.onEvent(event)
                } catch (e: Throwable) {
                    logger.warn("EventHandle执行中发生异常", e)
                }
            }
            while (queue.isNotEmpty()) queue.removeFirst().invoke()
        } catch (e: Throwable) {
            throw e
        } finally {
            running = false
        }
    }

    override val eventListeners: Iterable<IEventRunnable<E>> get() = callbacks.toList()
}