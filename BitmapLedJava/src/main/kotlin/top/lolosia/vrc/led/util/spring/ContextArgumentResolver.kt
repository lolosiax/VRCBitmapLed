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

package top.lolosia.vrc.led.util.spring

import top.lolosia.vrc.led.manager.EventSourceManager
import top.lolosia.vrc.led.util.ebean.toUuid
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.session.WebExchangeContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

@Component
class ContextArgumentResolver : HandlerMethodArgumentResolver {

    @Autowired
    lateinit var eventSourceManager: EventSourceManager

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        if (Context::class assi parameter) return true
        if (EventSource::class assi parameter) return true
        return false
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> {
        return when {
            Context::class assi parameter -> Mono.just(WebExchangeContext(exchange))
            EventSource::class assi parameter -> {
                val id = exchange.request.headers.getFirst("event-source-id")!!.toUuid()
                val eventSource = eventSourceManager.getEventSource(id)!!
                Mono.just(eventSource)
            }

            else -> throw IllegalStateException("不支持")
        }
    }

    private infix fun KClass<*>.assi(other: MethodParameter): Boolean {
        return this.java.isAssignableFrom(other.parameterType)
    }
}