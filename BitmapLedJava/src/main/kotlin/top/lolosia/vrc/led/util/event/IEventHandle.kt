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

/**
 * 事件处理器接口
 *
 * @author 一七年夏
 * @since 2021-09-19 12:59
 */
interface IEventHandle<E : IEvent> {
    /**
     * 添加一个事件回调到侦听器
     * @param runnable 事件回调
     */
    fun addListener(runnable: IEventRunnable<E>)

    /**
     * 获取全部侦听器
     */
    val eventListeners: Iterable<IEventRunnable<E>>

    /**
     * 移除一个侦听器回调
     * @param runnable 回调
     */
    fun removeListener(runnable: IEventRunnable<E>)


    /**
     * 移除一个侦听器回调
     * @param runnable 回调
     */
    operator fun minusAssign(runnable: IEventRunnable<E>) {
        removeListener(runnable)
    }

    /**
     * 添加一个事件回调到侦听器
     * @param runnable 事件回调
     */
    operator fun plusAssign(runnable: IEventRunnable<E>) {
        addListener(runnable)
    }

    /**
     * 添加一个事件回调到侦听器
     * @param runnable 事件回调
     */
    operator fun plusAssign(runnable: (e: E) -> Unit) {
        addListener(runnable)
    }

    /**
     * 添加一个事件回调到侦听器
     * @param runnable 事件回调
     */
    fun addListener(runnable: (e: E) -> Unit) {
        addListener(KtLambdaEventRunnable<E>().apply {
            this.runnable = runnable
        })
    }

    /**
     * 移除一个侦听器回调
     * @param runnable 回调
     */
    fun removeListener(runnable: (e: E) -> Unit) {
        for (lis in this.eventListeners) {
            if (lis is KtLambdaEventRunnable) {
                if (lis == runnable) removeListener(lis as IEventRunnable)
                break
            }
        }
    }

    /**
     * 移除一个侦听器回调
     * @param runnable 回调
     */
    operator fun minusAssign(runnable: (e: E) -> Unit) {
        removeListener(runnable)
    }

}
