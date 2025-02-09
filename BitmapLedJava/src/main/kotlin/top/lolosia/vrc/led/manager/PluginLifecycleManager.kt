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

import kotlinx.coroutines.*
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import top.lolosia.vrc.led.plugin.BitmapLedPlugin
import top.lolosia.vrc.led.util.packageLogger


/**
 * 插件生命周期管理器
 * @author 洛洛希雅Lolosia
 * @since 2024-08-11 13:45
 */
@Service
class PluginLifecycleManager : DisposableBean {

    @Autowired
    private lateinit var app: ApplicationContext
    private val pluginManager by lazy { app.getBean(PluginManager::class.java) }
    private val logger = packageLogger<PluginLifecycleManager>()

    private val registeredPlugins = mutableSetOf<String>()

    /**
     * 注册一个插件到当前管理器的已加载插件列表中。
     */
    fun registerPlugin(pluginId: String) {
        synchronized(registeredPlugins) {
            registeredPlugins.add(pluginId)
        }
    }

    /** 初始化插件生命周期管理器 */
    private suspend fun init() {
        enableAllPlugins()
    }

    /** 启动全部插件 */
    // 该方法以后可能会更改
    private suspend fun enableAllPlugins() {
        for (it in registeredPlugins.toList()) {
            val plugin = pluginManager[it] ?: continue
            try {
                withContext(plugin.coroutineContext) {
                    plugin.onEnable()
                }
            } catch (e: Throwable) {
                logger.error("未能启动插件${plugin.id}", e)
            }
        }
    }

    /**
     * 同步调用所有插件的指定方法
     */
    private fun callPlugins(block: BitmapLedPlugin.() -> Unit) {
        synchronized(registeredPlugins) {
            for (id in registeredPlugins) {
                val plugin = pluginManager[id] ?: continue
                block(plugin)
            }
        }
    }

    /**
     * 异步调用所有插件的指定方法
     */
    private suspend fun callPluginsAsync(block: suspend BitmapLedPlugin.() -> Unit) {
        for (id in registeredPlugins.toList()) {
            val plugin = pluginManager[id] ?: continue
            block(plugin)
        }
    }

    @EventListener
    private fun onApplicationStarted(event: ApplicationStartedEvent) {
        CoroutineScope(Dispatchers.Default).launch {
            init()
        }
    }

    override fun destroy() {
        runBlocking {
            val plugins = pluginManager.plugins.values.toList()
            plugins.forEach {
                try {
                    if (it.isActive) withContext(it.coroutineContext) {
                        it.onDisable()
                    }
                } catch (e: Throwable) {
                    logger.error("关闭插件${it.title}的过程中发生异常", e)
                }
            }
            plugins.forEach {
                try {
                    withContext(it.coroutineContext){
                        it.onClose()
                    }
                } catch (e: Throwable) {
                    logger.error("卸载插件${it.title}的过程中发生异常", e)
                }
            }
        }
    }
}