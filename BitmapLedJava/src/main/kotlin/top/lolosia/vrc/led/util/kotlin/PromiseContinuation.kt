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
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine

class PromiseContinuation<T>(override val context: CoroutineContext = Dispatchers.Default) : Continuation<T> {
    private val listener = mutableListOf<Continuation<T>>()
    private var result: Result<T>? = null
    var isResumed = false
        private set

    override fun resumeWith(result: Result<T>) {
        if (isResumed) throw IllegalStateException("PromiseContinuation.resumeWith is called twice")
        synchronized(listener) {
            this.isResumed = true
            this.result = result
            listener.forEach {
                it.resumeWith(result)
            }
            listener.clear()
        }
    }

    suspend fun await(): T {
        return suspendCoroutine {
            synchronized(listener) {
                if (isResumed) {
                    it.resumeWith(result!!)
                } else {
                    listener += it
                }
            }
        }
    }
}