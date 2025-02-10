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

import org.jboss.errai.reflections.Reflections
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.context.annotation.Configuration
import top.lolosia.vrc.led.BitmapLed
import top.lolosia.vrc.led.boot.LedClassLoader
import top.lolosia.vrc.led.util.packageLogger

/**
 * Jar类型插件加载器
 * @author 洛洛希雅Lolosia
 * @since 2024-08-12 20:28
 */
@Configuration(proxyBeanMethods = false)
class JarPluginRegistrationConfig : BeanDefinitionRegistryPostProcessor {

    val logger = packageLogger<JarPluginRegistrationConfig>()

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {

        if (!BitmapLed.mixinSupported) {
            logger.warn("The current environment does not support mixins and external plugins will not be loaded.")
            return
        }

        // 开始加载插件
        val loader = JarPluginRegistrationConfig::class.java.classLoader as LedClassLoader

        val effectJars = loader.pluginRegister

        var loadedPluginCount = 0

        effectJars.keys.forEach {
            val clazz = loader.loadClass(it)
            val beanName = clazz.simpleName.toCharArray()
            beanName[0] = beanName[0].lowercaseChar()

            // 创建一个新的 Bean 定义
            val def = GenericBeanDefinition()
            def.setBeanClass(clazz)
            def.scope = BeanDefinition.SCOPE_SINGLETON

            registry.registerBeanDefinition(beanName.concatToString(), def)
            ++loadedPluginCount
        }

        logger.info("$loadedPluginCount jars loaded")
    }

    private class JarReflections(
        configuration: org.jboss.errai.reflections.Configuration
    ) : Reflections(configuration) {
        public override fun scan() {
            super.scan()
        }
    }
}