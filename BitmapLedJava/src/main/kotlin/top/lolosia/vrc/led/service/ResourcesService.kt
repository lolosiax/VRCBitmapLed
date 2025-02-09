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

package top.lolosia.vrc.led.service

import top.lolosia.vrc.led.api.ResourcesApi
import top.lolosia.vrc.led.config.SConfig
import top.lolosia.vrc.led.util.bundle.TreeElementMap
import top.lolosia.vrc.led.util.bundle.TreeElementMap.Companion.getOrNull
import top.lolosia.vrc.led.util.bundle.bundleScope
import top.lolosia.vrc.led.util.property.MutableProperty
import top.lolosia.vrc.led.util.property.Property
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.success
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.server.ServerWebExchange
import java.io.File
import java.io.OutputStream
import java.net.URI
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.name

@Service
class ResourcesService {

    final var resourcesMeta = TreeElementMap<String, FileData>()
        private set

    final var metadataChecking by MutableProperty(false)
        private set

    val logger = LoggerFactory.getLogger(ResourcesService::class.java)

    val fileCount by fileCount0
    val fileProcessed by fileProcessed0
    val byteCount by byteCount0
    val byteProcessed by byteProcessed0

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    suspend fun get(exchange: ServerWebExchange, baseDir: String, url: String): ResponseEntity<*> {
        val conf = SConfig.resources[url]
        when (conf.mode) {
            "redirect" -> {
                return ResponseEntity<Unit>(HttpHeaders().apply {
                    location = URI("${conf.redirect}${url.replace(" ", "%20")}")
                }, HttpStatus.FOUND)
            }

            "proxy" -> {
                val request = exchange.request;
                val entity = WebClient.create()
                    .method(request.method) // 使用相同的请求方法
                    .uri(url) // 设置目标 URL
                    .headers { headers ->
                        headers.addAll(request.headers) // 复制原请求的头部
                    }
                    .body(request.body, ByteArray::class.java)
                    .awaitExchange<ResponseEntity<*>> {
                        ResponseEntity.status(it.statusCode())
                            .headers { h -> h.addAll(it.headers().asHttpHeaders()) }
                            .body(it.awaitBody<ByteArray>())
                    }
                return entity
            }

            "pass" -> {
                val location = Path("${baseDir}${url}").absolute()
                val res = resourceLoader.getResource("file:$location")
                if (!res.exists()) return ResponseEntity.notFound().build<Any>()

                val mime = MediaTypeFactory.getMediaType(location.name).orElse(MediaType.APPLICATION_OCTET_STREAM)

                return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                    .contentType(mime)
                    .contentLength(res.contentLength())
                    .body(res)
            }

            else -> return ResponseEntity.notFound().build<Any>()
        }
    }

    suspend fun checkFiles(ctx: Context, path: String): ResponseEntity<ByteArray> {

        var root: TreeElementMap<String, FileData>? = resourcesMeta

        path.split("/")
            .filter { it.isNotEmpty() }
            .forEach { root = root.getOrNull(it) }

        if (root == null) return success(null)

        val rs = root!!.flatMap { k, it ->
            if (it == null) return@flatMap null
            bundleScope {
                use(it::md5)
                use(it::lastUpdate)
                use(it::size)
                "name" set k.joinToString("/")
            }
        }

        return success(rs)
    }

    @PostConstruct
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            refreshMetadata()
        }
    }

    /**
     * 更新资源信息
     */
    fun refreshMetadata() {
        synchronized(this) {
            if (metadataChecking) return
        }
        metadataChecking = true
        fileCount0.value = 0
        fileProcessed0.value = 0
        byteCount0.value = 0
        byteProcessed0.value = 0
        logger.info("开始校检资源文件完整性……")
        try {
            val map = TreeElementMap<String, FileData>()
            val resDir = File("work/resources")
            resDir.mkdirs()
            countFiles(resDir)
            logger.info("已找到 $fileCount 个文件，共 ${byteCount / 1048576L}MB。")
            refreshMetadata0(map["resources"], resDir)
            logger.info("校检资源文件完成！")
            resourcesMeta = map
        } catch (e: Throwable) {
            logger.error("校检资源文件失败: ${e.message}")
            throw e
        } finally {
            metadataChecking = false
        }
    }

    /**
     * 对指定文件夹进行同步
     * @param url 远程服务器URL路径
     * @param pos 本地 TreeMap 对应文件夹路径
     * @param dir 本地文件系统文件夹路径
     */
    suspend fun resourcesSync(
        url: String,
        pos: List<String>,
        dir: File,
    ): SyncJob {
        val serverRoot = TreeElementMap<String, FileData>()
        var localRoot = resourcesMeta
        pos.filter { it.isNotEmpty() }.forEach { localRoot = localRoot[it] }
        val files = ResourcesApi.checkFiles(url) ?: emptyList()
        if (files.isEmpty()) {
            logger.warn("远程服务器返回资源${url}为空。")
        }
        files.forEach { meta ->
            var node = serverRoot
            meta["name"]?.toString()
                ?.split("/")
                ?.filter { it.isNotEmpty() }
                ?.forEach { node = node[it] }
                ?: return@forEach
            val ele = FileData(
                (meta["lastUpdate"] as Number).toLong(),
                (meta["size"] as Number).toLong(),
                meta["md5"] as String
            )
            node.set(ele)
        }

        val jobs = localRoot.zip(serverRoot).flatMap { k, node ->
            // k 相对路径

            val file = dir.resolve(k.joinToString("/"))
            if (node == null) return@flatMap null
            val (local, remote) = node
            if (remote == null && local == null) return@flatMap null
            FileSyncJob(this, pos + k, (listOf(url) + k).joinToString("/"), file, local, remote)
        }

        return SyncJobImpl(jobs)
    }

    data class FileData(
        val lastUpdate: Long,
        val size: Long,
        val md5: String,
    )


    interface SyncJob {
        val byteCount: Property<Long>
        val fileCount: Property<Long>
        val byteProcessed: Property<Long>
        val fileProcessed: Property<Long>
        suspend fun run()
    }
}


private val fileCount0 = MutableProperty(0L)
private val fileProcessed0 = MutableProperty(0L)
private val byteCount0 = MutableProperty(0L)
private val byteProcessed0 = MutableProperty(0L)

private fun countFiles(file: File) {
    if (file.isDirectory) {
        file.listFiles()?.forEach(::countFiles)
        return
    }
    fileCount0.value += 1
    byteCount0.value += file.length()
}


private fun refreshMetadata0(node: TreeElementMap<String, ResourcesService.FileData>, file: File) {
    if (file.isDirectory) {
        file.listFiles()?.forEach {
            refreshMetadata0(node[it.name], it)
        }
        return
    }
    val os = MD5OutputStream()
    file.inputStream().use { it.transferTo(os) }

    node.set(ResourcesService.FileData(file.lastModified(), file.length(), os.result))
    fileProcessed0.value += 1
}

private class MD5OutputStream : OutputStream() {
    private val util = MessageDigest.getInstance("MD5")
    override fun write(b: Int) {
        util.update(b.toByte())
        byteProcessed0.value += 1
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        util.update(b, off, len)
        byteProcessed0.value += len
    }

    val result: String
        get() {
            return HexFormat.of().formatHex(util.digest())
        }
}

/**
 * @param svc 资源服务
 * @param path 本地 TreeMap 所对应文件路径
 * @param url 远程服务器文件路径
 * @param file 本地文件系统文件路径
 * @param local 本地文件信息
 * @param remote 远程文件信息
 */
private class FileSyncJob(
    val svc: ResourcesService,
    val path: List<String>,
    val url: String,
    val file: File,
    val local: ResourcesService.FileData?,
    val remote: ResourcesService.FileData?,
) {
    val byteProcessed = MutableProperty(0L)
    val logger = LoggerFactory.getLogger(ResourcesService::class.java)
    var exception: Throwable? = null

    suspend fun run() {
        var retry = 0;
        exception = null
        suspend fun doRetry() {
            try {
                run0()
                exception = null
            } catch (e: Throwable) {
                exception = e
                logger.error("同步文件时发生异常", e)
            }
        }
        while (retry < 3) {
            doRetry()
            if (exception == null) return
            retry++
        }
        throw exception!!
    }

    private suspend fun run0() {
        var item = svc.resourcesMeta
        path.filter { it.isNotEmpty() }.forEach { item = item[it] }
        // 远程文件不存在时，直接删除
        if (remote == null) {
            if (file.exists()) file.delete()
            item.remove()
            return
        }
        // 本地文件不存在或版本不一致时，下载文件。
        if (local == null || local.md5 != remote.md5) {
            byteProcessed.value = 0
            ResourcesApi.downloadFiles(url, file, byteProcessed)
            item.set(remote)
        }
    }
}

private class SyncJobImpl(val jobs: List<FileSyncJob>) : ResourcesService.SyncJob, CoroutineScope {
    override val coroutineContext = Dispatchers.IO

    override val byteCount: MutableProperty<Long> = MutableProperty(
        jobs.fold(0L) { acc, it -> acc + (it.remote?.size ?: 0L) }
    )
    override val fileCount: MutableProperty<Long> = MutableProperty(jobs.size.toLong())
    override val byteProcessed: MutableProperty<Long> = MutableProperty(0L)
    override val fileProcessed: MutableProperty<Long> = MutableProperty(0L)

    val mSucceeded: MutableSet<FileSyncJob> = mutableSetOf()
    val mFailed: MutableSet<FileSyncJob> = mutableSetOf()

    override suspend fun run() {
        val kJob = mutableListOf<Job>()
        (jobs.toSet() - mSucceeded).map {
            kJob += (this as CoroutineScope).launch {
                try {
                    val listener = it.byteProcessed.addChangeListener{ o, n ->
                        if (n <= o) return@addChangeListener
                        synchronized(byteProcessed){
                            byteProcessed.value += (n - o)
                        }
                    }
                    it.run()
                    fileProcessed.value += 1
                    mSucceeded += it
                    it.byteProcessed.removeListener(listener)
                } catch (e: Throwable) {
                    synchronized(byteProcessed){
                        byteProcessed.value -= it.byteProcessed.value
                    }
                    mFailed += it
                    throw e
                }
            }
        }
        joinAll(*kJob.toTypedArray())
    }

}