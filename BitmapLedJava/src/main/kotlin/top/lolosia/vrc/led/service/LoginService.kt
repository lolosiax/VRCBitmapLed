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

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import top.lolosia.vrc.led.event.system.UserLoginEvent
import top.lolosia.vrc.led.event.system.UserLogoutEvent
import top.lolosia.vrc.led.manager.SessionManager
import top.lolosia.vrc.led.model.system.query.QSysUserEntity
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundleOf
import top.lolosia.vrc.led.util.bundle.bundleScope
import top.lolosia.vrc.led.util.bundle.toBundle
import top.lolosia.vrc.led.util.ebean.or
import top.lolosia.vrc.led.util.ebean.query
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.success

@Service
class LoginService {

    companion object {
        private val encoder = BCryptPasswordEncoder(10)
        private val mapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
        }
    }

    @Autowired
    lateinit var session: SessionManager

    @Autowired
    lateinit var publisher: ApplicationEventPublisher

    suspend fun login(ctx: Context, userName: String, password: String?): Bundle {
        val user = ctx.query<QSysUserEntity> {
            or {
                this.userName.eq(userName)
                this.phone.eq(userName)
            }
            deleted.ne(true)
        }.findOne() ?: throw NoSuchElementException("用户不存在！")
        var firstLogin = false
        if (user.password.isNullOrEmpty() && password.isNullOrEmpty()) {
            firstLogin = true
        }
        // 判断密码是否正确
        else if (user.password.isNullOrEmpty() || !encoder.matches(password, user.password)) {
            throw IllegalAccessException("密码不正确")
        }
        val obj = session.get()
        obj["id"] = user.id.toString()

        session.save(obj["sessionId"]!!.toString())
        val out = bundleScope {
            current.putAll(mapper.toBundle(user))
            current.putAll(obj)
            "Authorization" set obj["sessionId"]
            "firstLogin" set firstLogin
            "password" set null
        }

        publisher.publishEvent(UserLoginEvent(this, out, obj("sessionId")!!))
        return out
    }

    @Suppress("NAME_SHADOWING")
    suspend fun logout(ctx: Context, sessionId: String? = null): Any {
        val sessionId = try {
            sessionId ?: ctx.sessionId.toString()
        } catch (_: Throwable) {
            return success("SessionId哪去了？")
        }
        val out = if (session.contains(sessionId)) session[sessionId]
        else bundleOf()
        session.remove(sessionId)
        publisher.publishEvent(UserLogoutEvent(this, out, sessionId))
        return success()
    }
}