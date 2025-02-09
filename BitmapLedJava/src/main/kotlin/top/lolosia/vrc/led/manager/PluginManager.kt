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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.lolosia.vrc.led.plugin.BitmapLedPlugin
import top.lolosia.vrc.led.util.property.ComputedProperty
import top.lolosia.vrc.led.util.property.TriggerProperty
import kotlin.time.Duration.Companion.seconds

/**
 * 本机插件接口
 * @author 洛洛希雅Lolosia
 * @since 2024-08-04 20:25
 */
@Service
class PluginManager : CoroutineScope {
    override val coroutineContext = Dispatchers.Default

    @Autowired
    lateinit var lifecycleManager: PluginLifecycleManager

    private val pluginRegister = mutableMapOf<String, BitmapLedPlugin>()

    /** 插件列表 */
    val plugins by TriggerProperty { pluginRegister.toMap() }

    /** 插件ID列表 */
    val pluginIds by ComputedProperty(::plugins) {
        plugins.keys.toList()
    }

    /**
     * A插件向B插件发送消息
     * @param plugin 插件ID
     * @param topic 消息路径
     * @param message 消息
     * @return 若为 true，则发送成功，否则目标插件不存在
     * @throws TimeoutCancellationException 目标插件无响应
     */
    suspend fun postMessage(plugin: String, topic: String, message: ByteArray): Boolean {
        // 查找指定ID的插件，若插件不存在，返回 false
        val pluginBean = pluginRegister[plugin] ?: return false
        // 延时保护，若B插件阻塞超过5秒，抛出超时 Exception。
        withTimeout(5.seconds) {
            // 转到B插件所在线程执行
            withContext(pluginBean.coroutineContext) {
                pluginBean.onMessage(topic, message)
            }
        }
        // B插件成功接收了消息
        return true
    }

    /**
     * 注册一个插件到控制台中
     */
    fun addPlugin(id: String, plugin: BitmapLedPlugin) {
        if (id in pluginIds) {
            throw IllegalArgumentException("Plugin $id already exists!")
        }
        pluginRegister[id] = plugin
        lifecycleManager.registerPlugin(id)

        // 触发属性更新
        TriggerProperty.trigger(::plugins)
    }

    operator fun get(plugin: String) = pluginRegister[plugin]
}