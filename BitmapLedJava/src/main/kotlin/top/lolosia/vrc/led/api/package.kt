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

package top.lolosia.vrc.led.api

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import top.lolosia.vrc.led.config.SConfig
import top.lolosia.vrc.led.util.ErrorResponseException
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundleOf
import top.lolosia.vrc.led.util.bundle.bundleScope
import top.lolosia.vrc.led.util.json.parameterizedTypeReference
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.spring.EventSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.http.client.reactive.ClientHttpRequest
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Flux
import java.nio.file.Path
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.jvm.optionals.getOrNull
import kotlin.math.max
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection.Companion.invariant
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


private val logger = LoggerFactory.getLogger("top.lolosia.vrc.led.api.PackageKt")

private val mapper = JsonMapper().apply {
    registerModule(JavaTimeModule())
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
}

private fun String.s200() = if (length > 200) slice(0..200) + "...(${length - 200} more)"
else this

private fun Any?.s200(): String = try {
    if (this is Flow<*>) "<flow>"
    else mapper.writeValueAsString(this).s200()
} catch (e: Throwable) {
    "<serialization failed>"
}


val rootUrl get() = SConfig.host.serviceParent.rootUrl
val baseUrl get() = SConfig.host.serviceParent.baseUrl

/**
 * 对一个外部服务发起请求
 * @param baseUrl 根URL
 * @param url 子URL
 * @param body 请求体，传入 MultiValueMap 时将以FormData发送请求
 * @param key 远程接口所需要的验证字符串
 * @param raw 是否发送原始JSON请求，传入 MultiValueMap 时该参数不生效
 * @param ctx 上下文，用户获取用户SessionID
 * @param sessionId 用户SessionID
 * @param headers 请求头
 * @param T 响应类型，传入 Flow<DataBuffer> 时将以二进制返回数据
 * @return 响应体
 */
suspend inline fun <reified T> post(
    baseUrl: String,
    url: String,
    body: Any? = bundleOf(),
    key: String? = null,
    raw: Boolean = false,
    ctx: Context? = null,
    sessionId: UUID? = null,
    timeout: Duration = 14.seconds,
    headers: HttpHeaders? = null,
): T {
    return post(baseUrl, url, body, key, raw, ctx, sessionId, headers, timeout, typeOf<T>())
}


/**
 * 对一个外部服务发起请求
 */
@OptIn(FlowPreview::class)
@Suppress("UNCHECKED_CAST")
suspend fun <T> post(
    baseUrl: String,
    url: String,
    body: Any? = bundleOf(),
    key: String? = null,
    raw: Boolean = false,
    ctx: Context? = null,
    sessionId: UUID? = null,
    headers: HttpHeaders? = null,
    timeout: Duration = 14.seconds,
    type: KType = Any::class.createType(),
): T {

    val sessionId1 = sessionId ?: ctx?.sessionId

    val body1 = if (raw) body ?: bundleOf()
    else bundleScope {
        "key" set key
        "apiBody" set body
    }

    val body2: BodyInserter<*, in ClientHttpRequest> = if (body is MultiValueMap<*, *>) {
        // FormData
        if (!raw) (body as MultiValueMap<String, Any?>)["key"] = key ?: ""
        BodyInserters.fromMultipartData(body as MultiValueMap<String, *>)
    } else {
        // JSON
        BodyInserters.fromValue(body1)
    }

    val contentType = if (body is MultiValueMap<*, *>) MediaType.MULTIPART_FORM_DATA
    else MediaType.APPLICATION_JSON


    val spec = WebClient.create(baseUrl)
        .post()
        .uri(url)
        .contentType(contentType)
        .headers { h ->
            headers?.let { h.addAll(it) }
            sessionId1?.let { h.setBearerAuth(it.toString()) }
        }
        .body(body2)
        .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM)

    val out = channelFlow {
        beforeWebRequest(body, baseUrl, url)

        spec.exchangeToMono { resp ->
            mono {
                if (resp.statusCode().isError) respHandleException(resp)
                if (type.classifier == Unit::class) {
                    resp.releaseBody().awaitSingleOrNull()
                    send(Unit)
                }

                if (resp.isJson) {
                    val (data) = resp.json<T>(type)
                    send(data)
                } else if (type.classifier == Flow::class) {
                    val elementType = type.arguments[0].type?.javaType ?: Any::class.createType().javaType
                    val flow1 = resp.bodyToFlux(ParameterizedTypeReference.forType<Any>(elementType)).asFlow()
                    flow1.collect {
                        send(it)
                    }
                } else {
                    val type1 = ParameterizedTypeReference.forType<T>(type.javaType)
                    val rs = resp.bodyToMono<T>(type1).awaitSingleOrNull()
                    send(rs)
                }

                return@mono Unit
            }
        }.awaitSingle()
    }.catch { e ->
        catchWebRequestException(e, baseUrl, url)
        throw e
    }

    if (type.classifier == Flow::class) {
        return out.timeout(14.seconds) as T
    }

    try {
        return withTimeout(timeout) {
            val rs = out.firstOrNull()
            afterWebRequest(rs, baseUrl, url)
            rs as T
        }
    } catch (e: TimeoutCancellationException) {
        catchWebRequestException(e, baseUrl, url)
        throw e
    }
}

suspend fun streamingRequest(
    event: EventSource,
    baseUrl: String,
    url: String,
    body: Bundle = bundleOf(),
    key: String? = null
): String {
    // 响应结果，稍后要存入数据库
    var out = ""

    val body1 = bundleScope {
        "key" set key
        "apiBody" set body
    }

    beforeWebRequest(body, baseUrl, url)

    val response = WebClient.create(baseUrl)
        .post()
        .uri(url)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(body1))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()

    val map = try {
        response.awaitBody<Bundle>()
    } catch (e: Throwable) {
        catchWebRequestException(e, baseUrl, url)
        throw e
    }

    afterWebRequest(map, baseUrl, url)

    out += map["data"] as String
    event.send("data", out)

    // .accept(MediaType.TEXT_PLAIN)
    // .retrieve()
    // .bodyToFlow<DataBuffer>()
    // .collect {
    //     val text = it.toString(Charsets.UTF_8)
    //     DataBufferUtils.release(it)
    //     response += text
    //     event.send("data", text)
    // }

    return out
}

suspend fun streamingFile(file: Path, baseUrl: String, url: String) {
    val builder = MultipartBodyBuilder()
    val fileFlux: Flux<DataBuffer> = DataBufferUtils.read(file, DefaultDataBufferFactory.sharedInstance, 8192)
    builder.asyncPart("file", fileFlux, DataBuffer::class.java)
        .filename(file.fileName.toString())
        .contentType(MediaType.APPLICATION_PDF)
    val bodyMap = builder.build()

    val body = WebClient.create(baseUrl)
        .post()
        .uri(url)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(bodyMap))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .awaitBody<Map<String, Any>>()
    if (body["code"] != 200) {
        throw RuntimeException("文件上传失败(${body["msg"]})")
    }
}

suspend fun sse(
    baseUrl: String,
    url: String,
    body: Any? = bundleOf(),
    raw: Boolean = false,
    ctx: Context? = null,
    sessionId: UUID? = null,
): SSEResult {
    var id: UUID? = null
    if (!raw) {
        val headers = HttpHeaders()
        headers.add("X-Upgrade", "event-source")
        id = post<UUID>(
            baseUrl,
            url,
            body,
            raw = true,
            ctx = ctx,
            sessionId = sessionId,
            headers = headers
        )
    }


    val spec = WebClient.create(baseUrl)
        .get()
        .apply {
            if (raw) uri(url)
            else uri("$url?eventSourceId=$id")
        }
        .accept(MediaType.TEXT_EVENT_STREAM)

    val flow = channelFlow {
        val sseBaseUrl = baseUrl.replace("^https?".toRegex(), "sse")
        beforeWebRequest(body, sseBaseUrl, url)

        spec.exchangeToMono { resp ->
            mono {
                if (resp.statusCode().isError) respHandleException(resp)
                if (resp.isJson) {
                    val template = "The result returned by the remote server is not an SSE connection.\n"
                    val ent = resp.awaitBody<String>()
                    try {
                        val (data, code, msg) = mapper.readValue<ApiResponseEntity<Any?>>(ent)
                        val ex = IllegalStateException("${template}data: $data, code: $code, msg: $msg")
                        catchWebRequestException(ex, sseBaseUrl, url)
                        throw ex
                    } catch (_: Throwable) {
                        val ex = IllegalStateException("${template}${ent}")
                        catchWebRequestException(ex, sseBaseUrl, url)
                        throw ex
                    }
                }
                val flow = resp.bodyToFlow<ByteArray>()
                try {
                    var queue = ByteArray(0)
                    flow.transform {
                        queue += it
                        var lastIndex = -1
                        for (i in queue.indices) {
                            if (lastIndex >= i) continue
                            if (queue[i] == 10.toByte() && queue.getOrNull(i + 1) == 10.toByte()) {
                                emit(queue.copyOfRange(max(lastIndex, 0), i))
                                lastIndex = i + 2
                            }
                        }
                        if (lastIndex != -1) {
                            if (lastIndex >= queue.size) {
                                queue = ByteArray(0)
                            } else {
                                queue = queue.copyOfRange(lastIndex, queue.size)
                            }
                        }
                    }.collect { ev ->
                        // 以冒号开头的数据将被忽略
                        if (ev.isEmpty() || ev[0] == 58.toByte()) return@collect

                        var event = "message"
                        var data = "null"
                        val msg = ev.decodeToString()
                        logger.info(msg)
                        msg.split("\n").forEach {
                            if (it.startsWith("event: ")) event = it.substringAfter(' ')
                            if (it.startsWith("data: ")) data = it.substringAfter(' ')
                        }

                        val data1 = mapper.readValue<Any?>(data)

                        if (event == "close") throw CancellationException(data)

                        send(event to data1)
                    }
                } catch (e: CancellationException) {
                    afterWebRequest(e.message, sseBaseUrl, url)
                } catch (e: Throwable) {
                    catchWebRequestException(e, sseBaseUrl, url)
                    throw e
                }
            }
        }.awaitSingle()
    }.catch { e ->
        catchWebRequestException(e, baseUrl, url)
        throw e
    }

    val job = coroutineScope {
        coroutineContext[Job]!!
    }

    return SSEResult(flow, id, job)
}

data class ApiResponseEntity<T>(
    var data: T? = null,
    var code: Int = 200,
    @JsonProperty("msg")
    @JsonAlias("detail")
    var msg: String = ""
)

data class SSEResult(val flow: Flow<Pair<String, Any?>>, val id: UUID?, val job: Job)

private inline fun <reified T> responseType() = typeOf<T>().responseType()
private fun KType.responseType() = ApiResponseEntity::class.createType(listOf(invariant(this)))

private val ClientResponse.isJson
    get() = headers().contentType().getOrNull()?.isCompatibleWith(MediaType.APPLICATION_JSON) == true

/** 从 json 数据获取响应体 */
private suspend fun ClientResponse.json() = json<Any?>(Any::class.createType(nullable = true))

/**
 * 从 json 数据获取响应体
 * @param type 模板类型 T
 */
private suspend fun <T> ClientResponse.json(type: KType): ApiResponseEntity<T> {
    val mono = bodyToMono(type.responseType().parameterizedTypeReference<ApiResponseEntity<T>>())
    val out = mono.awaitSingle()
    if (out.code != 200) {
        throw ErrorResponseException(HttpStatus.BAD_GATEWAY, out.msg)
    }
    return out
}


private fun beforeWebRequest(body: Any?, baseUrl: String, url: String) {
    if (body is MultiValueMap<*, *>) {
        logger.info("发起请求: $baseUrl$url - <form-data>")
    } else {
        logger.info("发起请求: $baseUrl$url - ${body.s200()}")
    }
}

private fun afterWebRequest(map: Any?, baseUrl: String, url: String) {
    logger.info("响应成功: $baseUrl$url - 200 - ${map.s200()}")
}

private suspend fun respHandleException(resp: ClientResponse): Nothing {
    if (resp.isJson) {
        val ent = resp.awaitBody<String>()
        try {
            val obj = mapper.readValue<ApiResponseEntity<Any?>>(ent)
            throw ErrorResponseException(HttpStatus.BAD_GATEWAY, obj.msg)
        } catch (_: Throwable) {
            throw ErrorResponseException(HttpStatus.BAD_GATEWAY, ent)
        }
    }

    val msg = try {
        HttpStatus.valueOf(resp.statusCode().value()).reasonPhrase
    } catch (e: Throwable) {
        HttpStatus.BAD_GATEWAY.reasonPhrase
    }
    throw ErrorResponseException(resp.statusCode(), msg)
}

private fun catchWebRequestException(e: Throwable, baseUrl: String, url: String) {
    if (e is WebClientResponseException) {
        logger.warn("响应失败: $baseUrl$url - ${e.statusCode} - ${e.responseBodyAsString.s200()}")
        if (e.headers.contentType?.isCompatibleWith(MediaType.APPLICATION_JSON) == true) {
            val bundle = mapper.readValue<Bundle>(e.responseBodyAsString)
            throw ErrorResponseException(
                HttpStatus.BAD_GATEWAY,
                bundle["msg"]?.toString() ?: e.message ?: e.statusText,
                e
            )
        }
        return
    }
    logger.warn("响应失败: $baseUrl$url - ${e::class.qualifiedName}: ${e.message}")
}