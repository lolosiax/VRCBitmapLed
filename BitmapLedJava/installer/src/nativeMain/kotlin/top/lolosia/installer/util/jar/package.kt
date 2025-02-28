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

package top.lolosia.installer.util.jar

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.memScoped
import kotlinx.io.files.FileNotFoundException
import platform.windows.FindResourceW
import platform.windows.LoadResource
import platform.windows.LockResource
import platform.windows.SizeofResource
import top.lolosia.installer.util.zip.ZipCollection

/**
 * 获取有关当前应用程序内打包的Jar文件的相关功能
 * @author 洛洛希雅Lolosia
 * @since 2025-02-21 19:11
 */

/**
 * 获取当前应用程序内嵌的核心Jar的压缩文件集合
 * @return 核心Jar压缩文件集合
 */
fun getInstallerJarCollection() = getJarCollection("IDR_JAR1")

/**
 * 获取指定名称Jar的压缩文件集合
 * @param name Windows RC资源中指定的文件ID
 * @return 压缩文件集合
 * @throws FileNotFoundException 指定ID的文件不存在
 */
fun getJarCollection(name: String): ZipCollection {
    val (data, size) = getJarResource(name)
    return ZipCollection(data, size)
}

/**
 * 获取指定名称Jar的原始数据。
 * @param name Windows RC资源中指定的文件ID
 * @return 指向资源的指针与资源长度
 * @throws FileNotFoundException 指定ID的文件不存在
 */
fun getJarResource(name: String): Pair<COpaquePointer, Int> {
    memScoped {
        val hRes = FindResourceW(null, name, "JARFILE")
        hRes ?: throw FileNotFoundException("Internal jar '$name' not found!")
        val size = SizeofResource(null, hRes)
        val hData = LoadResource(null, hRes)
        val jarData = LockResource(hData)!!
        return jarData to size.toInt()
    }
}