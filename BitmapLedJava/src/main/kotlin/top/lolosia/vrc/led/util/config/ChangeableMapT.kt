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

abstract class ChangeableMapT<T : Any> {

    protected abstract val data: Bundle
    protected open val defaultValue: T? = null

    protected open operator fun setValue(target: ChangeableMapT<T>, property: KProperty<*>, value: T) {
        data[property.name] = value
    }

    @Suppress("UNCHECKED_CAST")
    protected open operator fun getValue(target: ChangeableMapT<T>, property: KProperty<*>): T {
        return convert(data[property.name], property.returnType.classifier as KClass<T>)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> convert(value: Any?, clazz: KClass<T>): T {
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
}