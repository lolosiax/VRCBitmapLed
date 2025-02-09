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

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf

/**
 *  代理Config根
 *
 * @author 一七年夏
 * @since 2022-06-23 20:04
 */
abstract class ChainConfigRoot : ChainConfig(null) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> get(key: String, type: KType): T? {
        return concert(get0(key, type), type) as T?
    }

    override fun <T : Any?> set(key: String, data: T?) {
        set0(key, data)
    }

    override fun getPath(bean: String): String {
        return bean
    }

    abstract fun <T : Any?> get0(key: String, type: KType): T?
    abstract fun <T : Any?> set0(key: String, data: T?)

    open fun concert(obj: Any?, type: KType): Any? {
        obj ?: return null
        if (type.isSupertypeOf(obj::class.createType(nullable = true))) return obj
        val clazz = type.classifier as? KClass<*> ?: return null
        if (obj::class == clazz) return obj
        if (clazz == String::class) return obj.toString()
        if (clazz == Boolean::class) {
            if (obj is String) return obj.toBoolean()
            if (obj is Number) return (obj != 0)
        }
        if (obj is Number) {
            when (clazz) {
                Int::class -> return obj.toInt()
                Long::class -> return obj.toLong()
                Short::class -> return obj.toShort()
                Double::class -> return obj.toDouble()
                Float::class -> return obj.toFloat()
                Char::class -> return obj.toChar()
                Byte::class -> return obj.toByte()
            }
        }
        if (obj is String) {
            when (clazz) {
                Int::class -> return obj.toInt()
                Long::class -> return obj.toLong()
                Short::class -> return obj.toShort()
                Double::class -> return obj.toDouble()
                Float::class -> return obj.toFloat()
                Char::class -> return obj[0]
                Byte::class -> return obj.toByte()
            }
        }
        return obj
    }
}