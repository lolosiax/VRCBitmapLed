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

import top.lolosia.vrc.led.util.ebean.toUuid
import top.lolosia.vrc.led.util.session.SSEContext
import top.lolosia.vrc.led.util.spring.EventSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventBusSSEManager : CoroutineScope {

    override val coroutineContext get() = Dispatchers.Default

    // sseId -> controller
    private val mClients = mutableMapOf<UUID, Meta>()


    /**
     * 记录了所有的Socket连接信息，键：SessionId
     */
    val clients: Map<UUID, Meta> get() = mClients.toMap()

    operator fun get(sseId: UUID): Meta? {
        return mClients[sseId]
    }

    fun getUserSessions(userId: UUID?): Map<UUID, Meta> {
        synchronized(mClients) {
            return mClients.filterValues { it.ctx.userIdOrNull == userId }
        }
    }

    fun getUserSessions(userId: String?): Map<UUID, Meta> {
        return getUserSessions(userId?.toUuid())
    }

    fun set(meta: Meta) {
        synchronized(mClients) {
            mClients[meta.client.sseId] = meta
        }
    }

    operator fun set(sseId: UUID, meta: Meta) {
        synchronized(mClients) {
            mClients[sseId] = meta
        }
    }

    fun remove(sseId: UUID) {
        synchronized(mClients) {
            mClients.remove(sseId)
        }
    }

    fun remove(client: EventSource) {
        synchronized(mClients){
            mClients.remove(client.sseId)
        }
    }

    val size get() = mClients.size

    data class Meta(
        val ctx: SSEContext,
        val client: EventSource
    )
}