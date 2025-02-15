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

import io.ktor.client.*
import io.ktor.client.engine.winhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import libui.ktx.ProgressBar

/**
 * download
 * @author 洛洛希雅Lolosia
 * @since 2025-02-15 19:32
 */

suspend fun download(progress: ProgressBar){
    val client = HttpClient(WinHttp)
    val resp = client.get(
        "https://mirrors.tuna.tsinghua.edu.cn/Adoptium/21/jre/x64/windows/OpenJDK21U-jre_x64_windows_hotspot_21.0.6_7.zip"
    )
    if (!resp.status.isSuccess()) {
        println("下载失败")
        return
    }
    var array = ByteArray(4096)
    val channel = resp.bodyAsChannel()
    val count = resp.contentLength() ?: -1
    var transformed = 0


    val target = Path(".\\work\\OpenJDK21U-jre_x64_windows_hotspot_21.0.6_7.zip")
    SystemFileSystem.createDirectories(target.parent!!)
    SystemFileSystem.sink(target).buffered().use {
        while (true) {
            val len = channel.readAvailable(array, 0, 4096)
            if (len == -1) break

            transformed += len
            if (count > 0) {
                println("$transformed/$count")
                runOnUiThread {
                    progress.value = (transformed / count * 100.0).toInt()
                }
            }
            it.write(array, 0, len)
        }
    }
}

suspend fun showJar(){
    val jar = getJar()
    println(jar.size)
}