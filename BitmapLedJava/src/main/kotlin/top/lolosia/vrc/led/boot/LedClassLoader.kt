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

package top.lolosia.vrc.led.boot

import org.jboss.errai.reflections.Reflections
import org.jboss.errai.reflections.scanners.SubTypesScanner
import org.jboss.errai.reflections.scanners.TypeAnnotationsScanner
import org.jboss.errai.reflections.util.ConfigurationBuilder
import org.slf4j.LoggerFactory
import top.lolosia.vrc.led.annotation.Plugin
import top.lolosia.vrc.led.boot.wrapper.LaunchClassLoader
import java.io.InputStream
import java.net.URL
import kotlin.io.path.*

/**
 * LedClassLoader
 * @author 洛洛希雅Lolosia
 * @since 2024-09-01 21:32
 */
class LedClassLoader(parent: ClassLoader?) : LaunchClassLoader(emptyArray(), parent) {

    val logger = LoggerFactory.getLogger(LedClassLoader::class.java)

    val pluginRegister by lazy { findPluginsAndAddIt() }

    /**
     * 白名单，这些类必须被放行。
     */
    private val whiteList = setOf(
        "top.lolosia.vrc.led.boot.Boot",
        "top.lolosia.vrc.led.boot.LedClassLoader",
        "top.lolosia.vrc.led.plugin.NativePluginInterface",
        "top.lolosia.vrc.led.logging.ConsoleListenerAppender",
        "top.lolosia.vrc.led.logging.CoreLogging",
        "top.lolosia.vrc.led.util.ebean.AbstractModel",
    )

    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        if (name.startsWith("top.lolosia.vrc.led.")){
            // 部分白名单需要放行，不然会发生冲突。
            if (name in whiteList){
                return parent.loadClass(name)
            }
            val clazz = findClass(name)
            if (resolve) resolveClass(clazz)
            return clazz
        }
        return super.loadClass(name, resolve)
    }

    override fun getParentClassBytes(name: String): InputStream? {
        return parent.getResourceAsStream(name.replace('.', '/') + ".class")
    }

    private fun findPluginsAndAddIt(): Map<String, URL> {
        val map = findPlugins().toMap()
        map.values.forEach(::addURL)
        return map
    }

    fun findPlugins(): MutableMap<String, URL> {
        // 列出plugins下面所有的 jar
        val paths = mutableListOf("plugins")
        // plugins 目录需要默认被创建
        Path(paths[0]).createDirectories()
        // 插件目录可以在系统环境变量如外指定
        System.getenv("UBE_PLUGIN_PATHS")?.let { paths.addAll(0, it.split(";")) }
        val files = paths.map { Path(it) }
            .filter { it.exists() }
            .flatMap { it.listDirectoryEntries("*.jar") }
            .map { it.absolute() }

        val effectJars = mutableMapOf<String, URL>()
        for (file in files) {
            val url = file.toUri().toURL()

            // 搜索插件所在类
            val config = ConfigurationBuilder().apply {
                addUrls(url)
                setScanners(TypeAnnotationsScanner(), SubTypesScanner())
            }
            val ref = JarReflections(config)
            ref.scan()
            val classes = ref.store.getTypesAnnotatedWith(Plugin::class.java.name).toList()

            // 不加载不正确的插件
            if (classes.isEmpty()) {
                logger.error("文件 $file 没有一个可加载的插件！请在插件主类上声明 @top.lolosia.vrc.led.annotate.Plugin")
                continue
            } else if (classes.size > 1) {
                logger.error(
                    """
                    文件 $file 重复声明了多个插件！这将不会有任何插件被加载！
                    ${classes.joinToString("\n > ", "> ")}
                    """.trimIndent()
                )
                continue
            }
            val clazz = classes[0]
            if (effectJars.containsKey(clazz)) {
                logger.error(
                    """
                    插件类名发生冲突，不允许加载同名类！$clazz
                    1 -> ${effectJars[clazz]!!}
                    2 -> $url
                """.trimIndent()
                )
                throw IncompatibleClassChangeError("插件类名发生冲突，不允许加载同名类！$clazz")
            }

            effectJars += clazz to url
        }
        return effectJars
    }

    private class JarReflections(
        configuration: org.jboss.errai.reflections.Configuration
    ) : Reflections(configuration) {
        public override fun scan() {
            super.scan()
        }
    }
}