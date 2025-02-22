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

package top.lolosia.installer.service

import io.ktor.client.*
import io.ktor.client.engine.winhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.cinterop.pointed
import kotlinx.datetime.Clock
import kotlinx.io.buffered
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import platform.windows.*
import top.lolosia.installer.*
import top.lolosia.installer.ui.component.dispatch
import top.lolosia.installer.ui.view.EnvironmentPage
import top.lolosia.installer.util.ConcurrentTaskQueue
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt
import kotlin.system.exitProcess

/**
 * 运行环境服务
 * @author 洛洛希雅Lolosia
 * @since 2025-02-15 19:32
 */

class EnvironmentService : IService {
    val view = EnvironmentPage(this)
    private val baseDir get() = Installer.baseDir
    private val workDir get() = Installer.workDir

    suspend fun checkEnvironment() {
        while (true) {
            val failure = downloadDependencies()
            if (failure.isEmpty()) break
            val rs = suspendCoroutine { cor ->
                thread {
                    CoInitializeEx(null, COINIT_APARTMENTTHREADED)
                    val hwnd = Installer.mainWindow.window.ptr.pointed.hwnd
                    var msg = "以下依赖下载失败，要重试吗？\n"
                    msg += failure.joinToString("\n") { "${it.group}:${it.name}:${it.version}" }
                    val type = MB_ABORTRETRYIGNORE or MB_ICONWARNING or MB_DEFBUTTON2
                    val rs = MessageBoxW(hwnd, msg, "下载失败", type.toUInt())
                    if (rs == 0) {
                        cor.resumeWithException(RuntimeException("Win32 Error: ${GetLastError()}"))
                    }
                    cor.resume(rs)
                    CoUninitialize()
                }
            }
            if (rs == IDABORT) exitProcess(-1)
            else if (rs == IDTRYAGAIN) continue
            else if (rs == IDIGNORE) return
            else throw IllegalStateException("未知选项: $rs")
        }
    }

    private fun updateChildStatus(index: Int, process: Double, message: String) {
        view.dispatch {
            val (label, proc) = components[index]
            if (process >= 0) {
                val value = (process * 100).roundToInt()
                label.text = "任务${index + 1}：[$value%]${message}"
                proc.value = value
            } else {
                label.text = "任务${index + 1}：[失败]$message"
                proc.value = 0
            }
        }
    }

    private fun updateMainStatus(count: Int, sum: Int, message: String? = null) {
        view.dispatch {
            val (label, proc) = components[10]
            label.text = message ?: "正在初始化运行环境……($count/$sum)"
            proc.value = (100.0 * count / sum).roundToInt()
        }
    }

    /**
     * 下载依赖
     */
    private suspend fun downloadDependencies(): List<JarDependency> {
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
                downloadJava(client) { a, b -> updateChildStatus(i, a, b) }
            } finally {
                synchronized(lock) {
                    finished++
                    updateMainStatus(finished, count)
                }
            }
        }

        val failure = mutableListOf<JarDependency>()

        for (dep in deps) {
            queue.send { i ->
                try {
                    val rs = downloadDependency(client, repos, dep) { a, b -> updateChildStatus(i, a, b) }
                    if (!rs) failure += dep
                } finally {
                    finished++
                    updateMainStatus(finished, count)
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
        status: (Double, String) -> Unit
    ): Boolean {
        val baseDir = Path(Installer.baseDir, "library")

        val fileName = dependency.url.split('/').last()
        val path = Path(baseDir, dependency.group, dependency.name, dependency.version, fileName)
        if (SystemFileSystem.exists(path)) {
            println("Dependency \"${dependency.group}:${dependency.name}:${dependency.version}\" exists!")
            return true
        }

        var success = false
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

    private val javaZipDir by lazy { Path(workDir, "download", "jre") }
    private val jreDir by lazy { Path(baseDir, "jre") }
    private val jreDownloadUrl = "https://mirrors.tuna.tsinghua.edu.cn/Adoptium/21/jre/x64/windows/"

    private suspend fun downloadJava(client: HttpClient, status: (Double, String) -> Unit) {
        val jvmPath = Path(jreDir, "jre/bin/server/jvm.dll")
        if (SystemFileSystem.exists(jvmPath)) {
            status(1.0, "Jvm运行时环境安装完成")
            return
        }

        val javaVersion = findJavaVersion(client)
        val url = "$jreDownloadUrl$javaVersion"
        val resp = client.get(url)
        if (!resp.status.isSuccess()) {
            throw RuntimeException("java下载失败")
        }

        if (!SystemFileSystem.exists(javaZipDir)) SystemFileSystem.createDirectories(javaZipDir)

        val javaZipPath = Path(javaZipDir, javaVersion)

        try {
            resp.transferTo(javaZipPath, status)
            releaseJava()
            SystemFileSystem.delete(javaZipPath)
            status(1.0, "Jvm运行时环境安装完成")
        } catch (e: Throwable) {
            if (SystemFileSystem.exists(javaZipPath)) {
                SystemFileSystem.delete(javaZipPath)
            }
        }
    }

    private suspend fun findJavaVersion(client: HttpClient): String {
        val resp = client.get(jreDownloadUrl)
        if (!resp.status.isSuccess()) {
            throw RuntimeException("java下载失败，无法获取版本信息。")
        }
        try {
            var str = resp.bodyAsText()
            str = str.split("<tbody>")[1].split("</tbody>")[0]
            var findResult = "href=\"[^\"]+\\.zip\"".toRegex().findAll(str).firstOrNull()?.value
            findResult ?: throw RuntimeException("java下载失败，版本信息获取失败。")
            findResult = findResult.removePrefix("href=\"").removeSuffix("\"")
            return findResult
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Throwable) {
            throw RuntimeException("下载java失败，版本信息获取失败", e)
        }
    }

    fun releaseJava() {
        val file = SystemFileSystem.list(javaZipDir).find { it.name.endsWith(".zip") }
        file ?: throw FileNotFoundException("找不到可用的Java运行时安装包")
        val data = SystemFileSystem.source(file).buffered().use { it.readByteArray() }
        if (SystemFileSystem.exists(jreDir)) {
            SystemFileSystem.deleteRecursively(jreDir)
        }
        ZipCollection.open(data).use {
            it.forEach { (name, data) ->
                val name1 = name.split("/", limit = 2)[1]
                val itemFile = Path(jreDir, name1)
                val parent = itemFile.parent!!
                if (!SystemFileSystem.exists(parent)) {
                    SystemFileSystem.createDirectories(parent)
                }
                SystemFileSystem.sink(itemFile).buffered().use { fos ->
                    fos.write(data)
                }
            }
        }
    }

    private suspend fun HttpResponse.transferTo(target: Path, status: (Double, String) -> Unit) {
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
                        if (now0 - lastUpdate > 200) {
                            lastUpdate = now0
                            status((1.0 * transformed / count), url)
                        }
                    }
                    it.write(array, 0, len)
                }
            }
            status(1.0, "完成")
        } catch (e: Throwable) {
            status(-1.0, "下载失败：${url}")
        }
    }
}