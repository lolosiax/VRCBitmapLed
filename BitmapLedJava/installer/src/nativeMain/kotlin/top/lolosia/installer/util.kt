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

@file:OptIn(InternalAPI::class, ExperimentalForeignApi::class)

package top.lolosia.installer

import io.ktor.utils.io.*
import io.ktor.utils.io.locks.*
import kotlinx.cinterop.*
import libui.uiQueueMain
import platform.windows.FindResourceW
import platform.windows.LoadResource
import platform.windows.LockResource
import platform.windows.SizeofResource

/**
 * util
 * @author 洛洛希雅Lolosia
 * @since 2025-02-15 17:43
 */

private val actions: MutableList<StableRef<Any>> = mutableListOf()
private val actionsSync = SynchronizedObject()

@OptIn(ExperimentalForeignApi::class)
fun runOnUiThread(block: () -> Unit) {
    val ref = StableRef.create(block)
    synchronized(actionsSync) {
        actions.add(ref)
    }
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

@OptIn(ExperimentalForeignApi::class)
fun getJar(): ByteArray {
    memScoped {
        val hRes = FindResourceW(null, "IDR_JAR1", "JARFILE")
        hRes ?: throw RuntimeException("Internal jar 'IDR_JAR1' not found!")
        val size = SizeofResource(null, hRes)
        val hData = LoadResource(null, hRes)
        val jarData = LockResource(hData)
        return jarData!!.readBytes(size.toInt())
    }
}