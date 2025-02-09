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

package top.lolosia.vrc.led.manager

import top.lolosia.vrc.led.util.spring.EventSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.seconds

@Service
class EventSourceManager {

    private val requestCache = mutableMapOf<UUID, Pair<ServerHttpRequest, ByteArray>>()
    private val eventSources = mutableMapOf<UUID, EventSource>()
    suspend fun createEventSource(request: ServerHttpRequest): UUID {
        val id = UUID.randomUUID()
        val body: ByteArray = suspendCoroutine {
            val bos = ByteArrayOutputStream()
            request.body.subscribe({
                it.asInputStream().transferTo(bos)
                DataBufferUtils.release(it)
            }, { e ->
                it.resumeWithException(e)
            }, {
                it.resume(bos.toByteArray())
            })
        }
        requestCache[id] = request to body
        CoroutineScope(Dispatchers.Default).launch {
            delay(10.seconds)
            requestCache.remove(id)
        }
        return id
    }

    fun getRequest(id: UUID): Pair<ServerHttpRequest, ByteArray>? {
        return requestCache.remove(id)
    }

    fun setEventSource(id: UUID, source: EventSource) {
        eventSources[id] = source
    }

    fun getEventSource(id: UUID): EventSource? {
        return eventSources[id]
    }

    fun removeEventSource(id: UUID) {
        eventSources.remove(id)
    }
}