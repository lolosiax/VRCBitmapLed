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

package top.lolosia.installer.util.threading

import platform.windows.COINIT_APARTMENTTHREADED
import platform.windows.COINIT_MULTITHREADED
import platform.windows.CoInitializeEx
import platform.windows.CoUninitialize
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun thread(name: String? = null, block: () -> Unit): Thread {
    return Thread(name, block).apply(Thread::start)
}

fun staThread(name: String? = null, block: () -> Unit) = thread(name) {
    try {
        CoInitializeEx(null, COINIT_APARTMENTTHREADED)
        block()
    } finally {
        CoUninitialize()
    }
}

fun mtaThread(name: String? = null, block: () -> Unit) = thread(name) {
    try {
        CoInitializeEx(null, COINIT_MULTITHREADED)
        block()
    } finally {
        CoUninitialize()
    }
}

suspend fun <T> withStaThread(block: () -> T): T {
    return suspendCoroutine {
        staThread {
            try {
                it.resume(block())
            } catch (e: Throwable) {
                it.resumeWithException(e)
            }
        }
    }
}