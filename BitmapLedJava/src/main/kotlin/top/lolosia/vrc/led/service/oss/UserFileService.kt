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

package top.lolosia.vrc.led.service.oss

import com.fasterxml.jackson.databind.json.JsonMapper
import top.lolosia.vrc.led.model.oss.UserFileEntity
import top.lolosia.vrc.led.model.oss.query.QUserFileEntity
import top.lolosia.vrc.led.util.ErrorResponseException
import top.lolosia.vrc.led.util.ebean.createModel
import top.lolosia.vrc.led.util.ebean.database
import top.lolosia.vrc.led.util.ebean.query
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.session.IWebExchangeContext
import top.lolosia.vrc.led.util.spring.createStaticFileResponse
import top.lolosia.vrc.led.util.success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.*
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.InputStream
import java.io.OutputStream
import java.net.URLEncoder
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.*
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.outputStream
import kotlin.time.Duration.Companion.days

@Service
class UserFileService {

    @Autowired
    lateinit var mapper: JsonMapper

    fun getFileMeta(ctx: Context, md5: String): UserFileEntity? {
        return ctx.query<QUserFileEntity> {
            this.md5.eq(md5)
            this.createdBy.eq(ctx.userIdOrNull)
        }.findOne()
    }

    fun getFile(ctx: Context, md5: String): Path? {
        val userId = ctx.userIdOrNull?.toString() ?: "guest"
        val dir = Path.of("work/files", userId, md5)
        if (!dir.exists()) return null
        return dir.toFile().listFiles()?.firstOrNull()?.toPath()
    }

    suspend fun fileCheck(ctx: Context, md5: List<String>): List<Boolean> {
        val userId = ctx.userIdOrNull
        val has = ctx.query<QUserFileEntity> {
            createdBy.eq(userId)
            this.md5.isIn(md5)
        }.findList().map { it.md5 }.toSet()
        return md5.map { it in has }
    }

    suspend fun get(ctx: Context, md5: String, download: Boolean = false): ResponseEntity<Flux<DataBuffer>> {
        ctx as IWebExchangeContext

        val lastModified = (ctx.session["req"] as? ServerHttpRequest)?.headers?.ifModifiedSince ?: -1
        if (!download && lastModified > -1) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).lastModified(lastModified).build()
        }

        return ctx.createStaticFileResponse(maxAge = 7.days) {

            val path = getFile(ctx, md5) ?: throw ErrorResponseException(HttpStatus.NOT_FOUND, "文件不存在")
            val file = path.toFile()
            val fileFlux: Flux<DataBuffer> = DataBufferUtils.read(path, DefaultDataBufferFactory.sharedInstance, 8192)

            var mime = MediaTypeFactory.getMediaType(file.name).orElse(MediaType.APPLICATION_OCTET_STREAM)
            if (mime.isCompatibleWith(MediaType.APPLICATION_JSON)) mime = MediaType.APPLICATION_OCTET_STREAM

            headers {
                if (download){
                    val urlFileName = URLEncoder.encode(file.name.toString(), Charsets.UTF_8);
                    it.contentDisposition = ContentDisposition.attachment().filename(urlFileName).build()
                }
                it.lastModified = file.lastModified()
                it.contentLength = file.length()
                it.contentType = mime
            }

            body(fileFlux)
        }
    }

    /**
     * 接收用户上传的文件
     * @param ctx 上下文
     * @param file 本次上传的文件
     * @param reference 本次不需要上传的文件
     * @param service 本次要上传到的服务名称
     */
    suspend fun uploadFromFileParts(ctx: Context, file: Flux<FilePart>, reference: List<String>, service: String): Any {
        val userId = ctx.userIdOrNull

        val iter = withContext(Dispatchers.IO) {
            file.toIterable()
        }

        upload<FilePart>(ctx, iter, service, { it.filename() }){ it, os ->
            it.content().asFlow().collect { dataBuffer ->
                val byteArray = dataBuffer.asInputStream().readBytes()
                DataBufferUtils.release(dataBuffer)
                withContext(Dispatchers.IO) {
                    os.write(byteArray)
                }
            }
        }

        val models = mutableListOf<UserFileEntity>()
        // 用户上传了已存在的文件，添加一个引用。
        if (reference.isNotEmpty()) {
            val files = ctx.query<QUserFileEntity> {
                this.md5.isIn(reference)
                this.createdBy.eq(userId)
                this.service.ne(service)
            }.findList().map {
                it.id = UUID.randomUUID()
                it.service = service
                it.createdAt = null
                it
            }
            models += files
        }
        ctx.database.insertAll(models)
        return success("ok")
    }

    suspend fun uploadFromInputStream(ctx: Context, inputStream: InputStream, fileName: String, service: String) : String {
        val list = upload(ctx, listOf(inputStream), service, {fileName}){ it, os ->
            it.transferTo(os)
        }
        return list[0]
    }

    /**
     * 用户上传文件
     * @param ctx 上下文
     * @param file 文件列表
     * @param service 文件服务类型
     * @param getFileName 从文件列表获取每一项的文件名
     * @param block 将文件写入到输出流
     * @return 文件md5列表
     */
    private suspend fun <T> upload(
        ctx: Context,
        file: Iterable<T>,
        service: String,
        getFileName: (T) -> String,
        block: suspend (T, OutputStream) -> Unit
    ) : List<String> {
        val userId = ctx.userIdOrNull
        val dir = Path.of("work/files/${userId ?: "guest"}")
        withContext(Dispatchers.IO) {
            Files.createDirectories(dir)
        }
        val models = mutableListOf<UserFileEntity>()
        val md5 = file.map { filePart ->
            val fileName = getFileName(filePart)
            val filePath = dir.resolve(fileName)
            val messageDigest = MessageDigest.getInstance("MD5")

            filePath.deleteIfExists()


            filePath.outputStream().use { os ->
                val os1 = object : OutputStream(){
                    override fun write(b: Int) = write(byteArrayOf(b.toByte()))
                    override fun write(b: ByteArray, off: Int, len: Int) {
                        messageDigest.update(b, off, len)
                        os.write(b, off, len)
                    }
                    override fun close() = os.close()
                    override fun flush() = os.flush()
                }
                block(filePart, os1)
            }
            // 完成MD5计算
            val md5Digest = messageDigest.digest()
            val md5Hex = md5Digest.joinToString("") { "%02x".format(it) }
            val fileTarget = dir.resolve(md5Hex)
            withContext(Dispatchers.IO) {
                Files.createDirectories(fileTarget)
                for (file1 in fileTarget.toFile().listFiles()!!) {
                    file1.delete()
                }
                Files.move(filePath, fileTarget.resolve(fileName))
            }

            models += ctx.createModel<UserFileEntity> {
                this.id = UUID.randomUUID()
                this.fileName = fileName
                this.md5 = md5Hex
                this.createdBy = userId
                this.service = service
            }

            return@map md5Hex
        }
        ctx.database.insertAll(models)

        return md5
    }
}