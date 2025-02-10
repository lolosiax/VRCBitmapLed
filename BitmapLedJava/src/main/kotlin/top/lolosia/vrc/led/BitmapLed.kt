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

package top.lolosia.vrc.led

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContext
import top.lolosia.vrc.led.boot.LedClassLoader
import top.lolosia.vrc.led.manager.PluginManager
import top.lolosia.vrc.led.plugin.BitmapLedPlugin

/**
 * VRC Bitmap Led 应用程序核心服务主入口点。
 * 请注意该入口点必须使用 LedClassLoader 启动。
 * @author 洛洛希雅Lolosia
 * @since 2024-08-02 21:20
 */
object BitmapLed {
    @SpringBootApplication
    class Application

    private val logger = LoggerFactory.getLogger(BitmapLed::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val classLoader = BitmapLed::class.java.classLoader as? LedClassLoader

        val builder = SpringApplicationBuilder(Application::class.java)
        builder.headless(false)
        val app = builder.application()
        app.addInitializers({
            classLoader?.let { _ ->
                it.setClassLoader(classLoader)
            }
        })
        applicationContext = app.run(*args)
    }

    /** SpringBoot 应用程序上下文 */
    lateinit var applicationContext: ApplicationContext
        private set

    /** 判断当前环境是否支持 Mixin */
    val mixinSupported get() = BitmapLed::class.java.classLoader is LedClassLoader

    /**
     * 获取一个插件
     * @param plugin 插件的ID
     */
    fun <T : BitmapLedPlugin> getPlugin(plugin: String): T? {
        @Suppress("UNCHECKED_CAST")
        return applicationContext.getBean(PluginManager::class.java)[plugin] as T?
    }
}