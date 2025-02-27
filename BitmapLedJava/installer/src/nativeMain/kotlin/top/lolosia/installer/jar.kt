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

package top.lolosia.installer

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.memScoped
import platform.windows.FindResourceW
import platform.windows.LoadResource
import platform.windows.LockResource
import platform.windows.SizeofResource

/**
 * jar
 * @author 洛洛希雅Lolosia
 * @since 2025-02-21 19:11
 */

fun getInstallerJarCollection() = getJarCollection0("IDR_JAR1")
fun getClassloaderJarCollection() = getJarCollection0("IDR_JAR2")

fun getJarResource(name: String): Pair<COpaquePointer, Int> {
    memScoped {
        val hRes = FindResourceW(null, name, "JARFILE")
        hRes ?: throw RuntimeException("Internal jar '$name' not found!")
        val size = SizeofResource(null, hRes)
        val hData = LoadResource(null, hRes)
        val jarData = LockResource(hData)!!
        return jarData to size.toInt()
    }
}

private fun getJarCollection0(name: String): ZipCollection {
    val (data, size) = getJarResource(name)
    return ZipCollection.open(data, size)
}