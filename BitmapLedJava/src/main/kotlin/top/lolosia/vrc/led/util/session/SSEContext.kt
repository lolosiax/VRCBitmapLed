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

package top.lolosia.vrc.led.util.session

import top.lolosia.vrc.led.api.SSEResult
import top.lolosia.vrc.led.manager.SessionManager
import org.springframework.context.ApplicationContext
import java.util.*

open class SSEContext(override val applicationContext: ApplicationContext) : Context() {

    constructor(ctx: Context) : this(ctx.applicationContext){
        try {
            mSessionId = ctx.sessionId
        }
        catch (_: Exception){}
    }

    private val sessionManager by lazy { applicationContext.getBean(SessionManager::class.java) }
    private val mSession by lazy { sessionManager[this.sessionId] }
    private val emptySession = SSESessionMap(mutableMapOf())
    private var mSessionId: UUID? = null

    var parentSse : SSEResult? = null

    val hasSessionId get() = mSessionId === null
    override var sessionId: UUID
        get() = mSessionId ?: throw IllegalStateException("sessionId is null")
        set(value) {
            mSessionId = value
        }

    override val session: SessionMap
        get() {
            val out = mSessionId?.let { mSession } ?: emptySession
            out["session:lastAccess"] = Date().time
            return out
        }

    inner class SSESessionMap(val map : MutableMap<String, Any?>) :SessionMap, MutableMap<String, Any?> by map {
        override fun set(key: String, value: Any?) {
            map[key] = value
        }
    }
}