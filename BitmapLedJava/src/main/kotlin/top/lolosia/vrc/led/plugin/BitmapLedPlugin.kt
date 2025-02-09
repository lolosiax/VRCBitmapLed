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

package top.lolosia.vrc.led.plugin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.lolosia.vrc.led.BitmapLed
import top.lolosia.vrc.led.util.kotlin.pass
import top.lolosia.vrc.led.util.property.DelegateProperty
import top.lolosia.vrc.led.util.property.asPropertyOrNull
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty0

/**
 * VRC点阵屏幕插件主入口点
 *
 * @author 洛洛希雅Lolosia
 * @since 2024-08-03 08:44
 */
abstract class BitmapLedPlugin : CoroutineScope {



    final override val coroutineContext get() = environment.coroutineContext

    /** 插件ID */
    abstract val id: String

    /** 插件标题 */
    open val title: String get() = this::class.java.`package`.implementationTitle ?: this::class.java.name


    /** 插件版本 */
    open val version: String get() = this::class.java.`package`.implementationVersion ?: "unknown"

    open val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 插件是否处于运行状态，
     * 若获取到的值为 false ，则 onUpdate() 不会被调用。
     */
    open val isActive get() = true

    protected lateinit var environment: EnvironmentHooker
        private set

    /** 应用程序 */
    val ledCore get() = BitmapLed

    /** 插件ID列表 */
    val pluginIds by DelegateProperty { environment.pluginIds } // late init

    /** 获取插件的私有数据目录 */
    val pluginFolder by lazy {
        Path("plugins/${id}/").createDirectories().absolutePathString()
    }

    /** 获取程序的运行目录 */
    val workFolder by lazy {
        Path("").absolutePathString()
    }

    /**
     * 初始化插件，为插件处理运行环境。
     * 反射专用方法。
     */
    @Suppress("unused")
    private fun postInit(env: EnvironmentHooker): Deferred<Unit> {
        if (this::environment.isInitialized) {
            throw IllegalStateException("postInit() call twice!")
        }
        environment = env
        return async {  }
    }

    /**
     * 获取一个插件实例
     * @param id 插件ID
     */
    fun <T : BitmapLedPlugin> getPlugin(id: String): T? {
        @Suppress("UNCHECKED_CAST")
        return environment.getPlugin(id) as T?
    }

    /**
     * 判断指定ID的插件是否已被加载
     * @param id 插件ID
     */
    fun isPluginLoaded(id: String): Boolean {
        return getPlugin<BitmapLedPlugin>(id) != null
    }

    /**
     * 判断指定ID的插件是否处于活跃状态
     * @param id 插件ID
     */
    fun isPluginActive(id: String): Boolean {
        return getPlugin<BitmapLedPlugin>(id)?.isActive ?: false
    }

    protected open val watcher = mutableSetOf<Any>()

    /**
     * 监听一个属性的更新
     * @param prop 属性
     * @param immediate 立刻更新
     * @param weak 弱引用侦听
     * @param block 侦听器
     */
    protected open fun <T : Any> watch(
        prop: KProperty0<T>,
        immediate: Boolean = false,
        weak: Boolean = false,
        block: (T) -> Unit
    ): (T) -> Unit {
        val p1 = prop.asPropertyOrNull()
        if (p1 != null) {
            if (!weak) watcher.add(block)
            p1.addListener(immediate, block)
        } else {
            prop as CallableReference
            val mode = if (prop is KMutableProperty<*>) "var" else "val"
            throw IllegalAccessException(
                "$mode ${prop.boundReceiver::class.qualifiedName}.${prop.name} 不是一个可监听的属性"
            )
        }
        return block
    }

    /**
     * 取消监听一个属性
     */
    protected open fun unwatch(callback: (Any?) -> Unit) {
        watcher -= callback
    }

    // ====================
    //      Function
    // ====================

    /**
     * 当插件初始化时调用，可以在此方法内初始化一些本地库的运行环境。
     */
    open suspend fun onCreate() = pass

    /** 当插件启动时调用 */
    open suspend fun onEnable() = pass

    /** 以125Hz的速率执行更新任务 */
    open suspend fun onUpdate(nanoTime: Long) = pass

    /** 当插件关闭时调用 */
    open suspend fun onDisable() = pass

    /**
     * 当插件被移除时调用。
     * 此时插件应当卸载所有的线程、侦听器、定时任务，并保存数据等待退出。
     */
    open suspend fun onClose() = pass

    /**
     * 当接收到其他插件传来的消息时调用
     * @param topic 消息类型
     * @param msg 消息内容
     */
    open suspend fun onMessage(topic: String, msg: ByteArray) = pass

    /**
     * 向其他插件发送消息
     * @param plugin 插件ID
     * @param topic 消息类型
     * @param msg 消息体
     * @return 若为 true，则发送成功，否则目标插件不存在
     * @throws TimeoutCancellationException 目标插件无响应
     */
    protected suspend fun postMessage(plugin: String, topic: String, msg: ByteArray): Boolean {
        return environment.postMessage(plugin, topic, msg)
    }

}