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

import top.lolosia.vrc.led.manager.SessionManager
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.ebean.toUuid
import org.springframework.web.server.ServerWebExchange
import java.util.*

class WebExchangeContext(override val exchange: ServerWebExchange) :
    SessionBasedContext(exchange.applicationContext!!, UUID.randomUUID()),
    IWebExchangeContext {

    private val sessionManager by lazy { applicationContext.getBean(SessionManager::class.java) }

    private val mSession: SessionMap by lazy {
        sessionManager.mySession(exchange) ?: throw IllegalStateException("请重新登录")
    }

    override val sessionId: UUID by lazy { session["sessionId"]!!.toString().toUuid() }

    override val session: SessionMap
        get() {
            mSession["session:lastAccess"] = Date().time
            return mSession
        }
}