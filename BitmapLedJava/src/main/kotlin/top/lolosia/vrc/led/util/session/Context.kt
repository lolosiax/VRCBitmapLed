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

import top.lolosia.vrc.led.util.ErrorResponseException
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.invoke
import top.lolosia.vrc.led.util.ebean.toUuid
import top.lolosia.vrc.led.util.spring.ApplicationContextProvider
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import java.util.*
import kotlin.reflect.KProperty

abstract class Context : ApplicationContextProvider {
    abstract override val applicationContext: ApplicationContext
    abstract val session: SessionMap
    protected val proxy = proxy()
    protected fun proxy(prop: String? = null) = SessionProxy(prop)

    //
    // 变量表
    //

    open val sessionId: UUID get() = session["sessionId"]!!.toString().toUuid()
    val userId: String
        get() = session("id") ?: throw ErrorResponseException(
            HttpStatus.UNAUTHORIZED,
            "身份认证失败，请重新登录"
        )
    val userIdOrNull : UUID?
        get() = try {
            session.invoke<String>("id")?.toUuid()
        } catch (e: Exception) {
            null
        }

    val user = UserInfo(this)

    inline operator fun <R> invoke(block: Context.() -> R) : R{
        return block(this)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Context) other.sessionId == sessionId
        else super.equals(other)
    }

    override fun hashCode(): Int {
        return sessionId.hashCode()
    }

    //
    //  Session代理对象
    //

    protected class SessionProxy(private val propName: String? = null) {
        @Suppress("UNCHECKED_CAST")
        operator fun <T> getValue(target: Context, prop: KProperty<*>): T {
            return target.session[propName ?: prop.name] as T
        }

        operator fun <T> setValue(target: Context, prop: KProperty<*>, value: T) {
            target.session[propName ?: prop.name] = value
        }
    }
}