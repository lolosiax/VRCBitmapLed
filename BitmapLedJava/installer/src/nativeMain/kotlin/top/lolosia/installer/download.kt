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
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.datetime.Clock
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import top.lolosia.installer.util.ConcurrentTaskQueue

/**
 * download
 * @author 洛洛希雅Lolosia
 * @since 2025-02-15 19:32
 */
typealias StatusCallback = (Int, Pair<Double, String>) -> Unit
typealias StatusCallback0 = (Pair<Double, String>) -> Unit
typealias StatusCallback1 = (now: Int, count: Int) -> Unit

/**
 * 下载依赖
 */
suspend fun downloadDependencies(status: StatusCallback, finish: StatusCallback1): List<JarDependency> {
    val baseDir = Path("library")
    if (!SystemFileSystem.exists(baseDir)) SystemFileSystem.createDirectories(baseDir)

    val client = HttpClient(WinHttp)
    var (repos, deps) = getDependencies()
    repos = repos.map { it.removeSuffix("/") }
    val queue = ConcurrentTaskQueue(10)
    queue.start()
    val count = deps.size + 1
    var finished = 0
    val lock = SynchronizedObject()

    queue.send { i ->
        try {
            downloadJava(client) { status(i, it) }
        } finally {
            synchronized(lock) {
                finished++
                finish(finished, count)
            }
        }
    }

    val failure = mutableListOf<JarDependency>()

    for (dep in deps) {
        queue.send { i ->
            try {
                val rs = downloadDependency(client, repos, dep) { status(i, it) }
                if (!rs) failure += dep
            } finally {
                finished++
                finish(finished, count)
            }
        }
    }
    queue.awaitFinish()
    return failure
}

private suspend fun downloadDependency(
    client: HttpClient,
    repos: List<String>,
    dependency: JarDependency,
    status: StatusCallback0
): Boolean {
    val baseDir = SystemFileSystem.resolve(Path("library"))
    var success = false

    val fileName = dependency.url.split('/').last()
    val path = Path(baseDir, dependency.group, dependency.name, dependency.version, fileName)

    repo@ for (repo in repos) {
        val url = "${repo}${dependency.url}"
        val resp = client.get(url)
        if (!resp.status.isSuccess()) {
            println("错误代码${resp.status}，未能下载：$url")
            continue
        }
        retry@ for (i in 1..3) {
            try {
                resp.transferTo(path, status)
                success = true
            } catch (e: Throwable) {
                println("下载失败($i/3)，${e.message}，未能下载$url")
                e.printStackTrace()
            }
            break@repo
        }
    }
    return success
}

private suspend fun downloadJava(client: HttpClient, status: StatusCallback0) {
    val url =
        "https://mirrors.tuna.tsinghua.edu.cn/Adoptium/21/jre/x64/windows/OpenJDK21U-jre_x64_windows_hotspot_21.0.6_7.zip"
    val resp = client.get(url)
    if (!resp.status.isSuccess()) {
        println("下载失败")
        return
    }
    val target = Path(".\\work\\OpenJDK21U-jre_x64_windows_hotspot_21.0.6_7.zip")
    resp.transferTo(target, status)
}

private suspend fun HttpResponse.transferTo(target: Path, status: StatusCallback0 = {}) {
    val url = request.url.toString()
    try {
        val array = ByteArray(102400)
        val channel = bodyAsChannel()
        val count = contentLength() ?: -1
        var transformed = 0
        val clock = Clock.System
        val now = { clock.now().toEpochMilliseconds() }
        var lastUpdate = 0L

        SystemFileSystem.createDirectories(target.parent!!)
        if (SystemFileSystem.exists(target)) SystemFileSystem.delete(target)
        SystemFileSystem.sink(target).buffered().use {
            while (true) {
                val len = channel.readAvailable(array, 0, 4096)
                if (len == -1) break

                transformed += len
                if (count > 0) {
                    // println("$transformed/$count")
                    val now0 = now()
                    if (now0 - lastUpdate > 200){
                        lastUpdate = now0
                        status((1.0 * transformed / count) to url)
                    }
                }
                it.write(array, 0, len)
            }
        }
        status(1.0 to "完成")
    } catch (e: Throwable) {
        status(-1.0 to "下载失败：${url}")
    }
}