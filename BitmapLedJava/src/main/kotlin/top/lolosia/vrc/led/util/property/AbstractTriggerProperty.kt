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

package top.lolosia.vrc.led.util.property

import kotlin.reflect.KProperty

/**
 * 触发式可监听属性基类
 *
 * @author 洛洛希雅Lolosia
 * @since 2024-08-28 17:31
 */
abstract class AbstractTriggerProperty<T> : AbstractProperty<T>() {
    protected object EMPTY
    protected var value0: Any? = EMPTY
    protected abstract val rawValue: T

    override val value: T
        get() {
            if (value0 == EMPTY) {
                rawValue.let {
                    value0 = it
                    afterChange(it, it)
                }
            }
            @Suppress("UNCHECKED_CAST")
            return value0 as T
        }


    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    /** 主动更新属性值 */
    protected open fun trigger() {
        var before = value0
        val after = rawValue
        if (before == EMPTY) before = after
        value0 = after
        @Suppress("UNCHECKED_CAST")
        afterChange(after, before as T)
    }
}