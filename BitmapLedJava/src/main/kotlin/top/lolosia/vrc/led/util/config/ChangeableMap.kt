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

package top.lolosia.vrc.led.util.config

import top.lolosia.vrc.led.util.bundle.Bundle
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class ChangeableMap {

    protected abstract val data: Bundle
    protected open operator fun <T> setValue(target: ChangeableMap, property: KProperty<*>, value: T) {
        data[property.name] = value as Any
    }

    protected open operator fun <T> getValue(target: ChangeableMap, property: KProperty<*>): T {
        return convert(data[property.name], property.returnType.classifier as KClass<*>)
    }

    protected operator fun <T> invoke(default: T): Proxy<T> = InternalProxy { default }
    protected operator fun <T> invoke(default: () -> T): Proxy<T> = InternalProxy(default)

    @Suppress("UNCHECKED_CAST")
    protected open fun <T : Any> convert(value: Any?, clazz: KClass<*>): T {
        return when (clazz) {
            Double::class -> (value as Number).toDouble() as T
            Float::class -> (value as Number).toFloat() as T
            Byte::class -> (value as Number).toByte() as T
            Char::class -> (value as Number).toInt().toChar() as T
            Short::class -> (value as Number).toShort() as T
            Int::class -> (value as Number).toInt() as T
            Long::class -> (value as Number).toLong() as T
            else -> value as T
        }
    }

    /**
     * 默认值代理对象，当配置文件中的此项为null时，返回默认值
     */
    interface Proxy<T> {
        operator fun setValue(target: ChangeableMap, property: KProperty<*>, value: T)
        operator fun getValue(target: ChangeableMap, property: KProperty<*>): T
    }

    private inner class InternalProxy<T>(val default: () -> T) : Proxy<T> {
        override operator fun setValue(target: ChangeableMap, property: KProperty<*>, value: T) {
            this@ChangeableMap.setValue(target, property, value)
        }

        override operator fun getValue(target: ChangeableMap, property: KProperty<*>): T {
            return convert(data[property.name] ?: default(), property.returnType.classifier as KClass<*>)
        }
    }
}