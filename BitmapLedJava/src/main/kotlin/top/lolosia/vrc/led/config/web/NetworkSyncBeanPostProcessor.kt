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

package top.lolosia.vrc.led.config.web

import top.lolosia.vrc.led.annotation.NetworkSync
import top.lolosia.vrc.led.manager.NetworkSyncManager
import top.lolosia.vrc.led.util.network.Invoker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible

@Component
class NetworkSyncBeanPostProcessor : BeanPostProcessor {

    @Autowired
    lateinit var applicationContext: ApplicationContext

    val manager: NetworkSyncManager by lazy {
        applicationContext.getBean(NetworkSyncManager::class.java)
    }

    // 这两个属性将会在程序启动后被设置为null。
    var methodRecords: MutableList<() -> Unit>? = mutableListOf()
    var invokerRecords: MutableList<() -> Unit>? = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (!bean::class.java.name.startsWith("top.lolosia.vrc.led")) return bean

        // 远程方法注册阶段
        var records = bean::class.java.declaredMethods
            .filter { !Modifier.isStatic(it.modifiers) && it.getAnnotation(NetworkSync::class.java) != null }
            .map { method ->
                { manager.register(bean, method) }
            }
        methodRecords?.let {
            it += records
        } ?: records.forEach { it() }

        // 字段的Invoker对象注入阶段
        records = bean::class.declaredMemberProperties
            .filterIsInstance<KMutableProperty1<Any, *>>()
            .filter {
                if (!it.hasAnnotation<NetworkSync>()) return@filter false
                return@filter (it.returnType.classifier as KClass<*>).isSubclassOf(Invoker::class)
            }
            .map { prop ->
                {
                    val ann = prop.findAnnotation<NetworkSync>()!!
                    manager.getRemoteInvoker(bean, ann.namespace)?.let {
                        prop.isAccessible = true
                        (prop as KMutableProperty1<Any, Invoker>).set(bean, it)
                    }
                    Unit
                }
            }

        invokerRecords?.let {
            it += records
        } ?: records.forEach { it() }

        return bean
    }


    @EventListener(ApplicationStartedEvent::class)
    fun doRegister() {
        methodRecords?.forEach { it() }
        invokerRecords?.forEach { it() }
        methodRecords = null
        invokerRecords = null
    }
}