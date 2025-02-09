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

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.context.annotation.Configuration

/**
 * 通过类名称载入插件，这在测试过程中很有实用性。
 * @author 洛洛希雅Lolosia
 * @since 2024-08-20 00:01
 */
@Configuration(proxyBeanMethods = false)
class ClassPluginRegistrationConfig : BeanDefinitionRegistryPostProcessor {

    companion object : MutableList<String> by mutableListOf()

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        toSet().forEach {
            val clazz = Class.forName(it)
            val beanName = clazz.simpleName.toCharArray()
            beanName[0] = beanName[0].lowercaseChar()

            // 创建一个新的 Bean 定义
            val def = GenericBeanDefinition()
            def.setBeanClass(clazz)
            def.scope = BeanDefinition.SCOPE_SINGLETON

            registry.registerBeanDefinition(beanName.concatToString(), def)
        }
    }
}