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