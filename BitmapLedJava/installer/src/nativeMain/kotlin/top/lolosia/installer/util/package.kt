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
package top.lolosia.installer.util

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.cinterop.*
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import libui.uiQueueMain
import platform.windows.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * util
 * @author 洛洛希雅Lolosia
 * @since 2025-02-15 17:43
 */

private val actions: MutableList<StableRef<Any>> = mutableListOf()
private val actionsSync = SynchronizedObject()

fun runOnUiThread(block: () -> Unit) {
    val ref = StableRef.create(block)
    synchronized(actionsSync) {
        actions.add(ref)
        uiQueueMain(staticCFunction { it ->
            val ref0 = it!!.asStableRef<() -> Unit>()
            try {
                ref0.get().invoke()
            } finally {
                synchronized(actionsSync) {
                    actions.remove(ref0)
                }
                ref0.dispose()
            }
        }, ref.asCPointer())
    }
}

suspend fun <T> withUI(block: () -> T): T {
    return suspendCoroutine { continuation ->
        runOnUiThread {
            try {
                continuation.resume(block())
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }
    }
}

/**
 * 指示一段代码需要在UI线程执行
 * @param block 需要执行的代码块
 */
fun <T> ui(block: () -> T) = runBlocking {
    withUI { block() }
}

/**
 * 返回当前进程的完整文件名称，包含文件路径。
 */
fun getCurrentProcessFileName(): String {
    val bufferLength = 32767 // Windows 最大路径长度
    return memScoped {
        val buffer = allocArray<UShortVar>(bufferLength)
        val result = GetModuleFileNameW(
            hModule = null,
            lpFilename = buffer,
            nSize = bufferLength.toUInt()
        )
        if (result == 0u) {
            throw RuntimeException("获取模块文件名失败，错误代码: ${GetLastError()}")
        }
        buffer.toKString()
    }
}

fun FileSystem.deleteRecursively(path: Path) {
    list(path).forEach { child ->
        val meta = metadataOrNull(child)!!
        if (meta.isDirectory) deleteRecursively(child)
        else delete(child)
    }
    delete(path)
}