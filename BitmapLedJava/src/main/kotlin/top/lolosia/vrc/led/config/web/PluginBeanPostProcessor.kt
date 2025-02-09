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

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.beans.factory.getBeanNamesForAnnotation
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import top.lolosia.vrc.led.annotation.NeedPlugin
import top.lolosia.vrc.led.manager.PluginManager
import top.lolosia.vrc.led.plugin.BitmapLedPlugin
import top.lolosia.vrc.led.plugin.EnvironmentHooker
import top.lolosia.vrc.led.util.packageLogger
import top.lolosia.vrc.led.util.property.asProperty
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.CancellationException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

/**
 * 插件初始化处理器
 * @author 洛洛希雅Lolosia
 * @since 2024-08-04 20:45
 */
@Component
class PluginBeanPostProcessor : BeanPostProcessor {

    private val logger = packageLogger<PluginBeanPostProcessor>()

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Autowired
    lateinit var pluginManager: PluginManager

    lateinit var pluginIdToBeansMap: Map<String, List<String>>


    @PostConstruct
    private fun init() {
        val ctx = applicationContext as AnnotationConfigReactiveWebServerApplicationContext
        val factory = ctx.beanFactory
        pluginIdToBeansMap = factory.getBeanNamesForAnnotation<NeedPlugin>()
            .associateBy({ it }) { factory.getBeanDefinition(it) }
            .filterValues { it is AnnotatedBeanDefinition }
            .map {
                it.key to (it.value as AnnotatedBeanDefinition).metadata
                    .getAnnotationAttributes(NeedPlugin::class.java.name)!!["id"] as String
            }
            .groupBy({ it.second }) { it.first }
    }


    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        if (bean is BitmapLedPlugin) {
            initPlugin(bean)
        }
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (bean is BitmapLedPlugin) {
            loadPlugin(bean)
        }
        return bean
    }

    /**
     * 初始化一个插件
     */
    private fun initPlugin(plugin: BitmapLedPlugin) {
        val method = BitmapLedPlugin::class.java.getDeclaredMethod("postInit", EnvironmentHooker::class.java)
        val env = FactoryEnvironmentHooker(plugin.id, plugin)
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val result = method.invoke(plugin, env) as Deferred<Unit>
        runBlocking {
            result.await()
        }
        method.isAccessible = false
        pluginManager.addPlugin(plugin.id, plugin)

        // 初始化依赖的服务
        pluginIdToBeansMap[plugin.id]?.forEach {
            try {
                applicationContext.getBean(it)
            } catch (e: Exception) {
                logger.error("加载依赖插件${plugin.id}的服务时出现异常", e)
            }
        }
    }

    /**
     * 加载一个插件
     */
    private fun loadPlugin(plugin: BitmapLedPlugin) {
        plugin.launch {
            plugin.onCreate()
        }
    }

    inner class FactoryEnvironmentHooker(
        id: String,
        val plugin: BitmapLedPlugin
    ) : EnvironmentHooker {
        val threadPool = SingleThreadExecutor(id, plugin)
        override val coroutineContext = threadPool.asCoroutineDispatcher()
        override val pluginIds get() = pluginManager::pluginIds.asProperty()

        override fun getPlugin(id: String): BitmapLedPlugin? {
            return pluginManager[id]
        }

        override suspend fun postMessage(plugin: String, topic: String, msg: ByteArray): Boolean {
            this.plugin.logger.trace("Post message to \"${plugin}\", $topic: ${msg.toString(Charsets.ISO_8859_1)} ")
            return pluginManager.postMessage(plugin, topic, msg)
        }
    }

    inner class SingleThreadExecutor(
        id: String,
        private val plugin: BitmapLedPlugin
    ) : AbstractExecutorService() {
        val queue = LinkedBlockingQueue<Runnable>()
        val thread = Thread(this::run, id)
        private var active: Boolean = true
        private var closeNow: Boolean = false

        override fun execute(command: Runnable) {
            if (!active) throw IllegalStateException("Plugin thread is not active!")
            queue.add(command)
        }

        override fun shutdown() {
            active = false
            if (queue.peek() == null) {
                thread.interrupt()
            }
        }

        override fun shutdownNow(): List<Runnable> {
            closeNow = true
            val list = mutableListOf<Runnable>()
            while (true) list.add(queue.poll() ?: return list)
        }

        override fun isShutdown(): Boolean {
            return active
        }

        override fun isTerminated(): Boolean {
            return active
        }

        override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean {
            val deadline = System.currentTimeMillis() + unit.toMillis(timeout)
            while (queue.isNotEmpty() && System.currentTimeMillis() < deadline) {
                Thread.sleep(100)
            }
            return System.currentTimeMillis() < deadline
        }

        private fun run() {
            while ((active || queue.peek() != null) && !closeNow && !thread.isInterrupted) {
                try {
                    queue.take().run()
                } catch (_: CancellationException) {
                } catch (e: Throwable) {
                    plugin.logger.error("线程调度器中发生未捕获的异常", e)
                }
            }
        }

        init {
            thread.start()
        }
    }
}