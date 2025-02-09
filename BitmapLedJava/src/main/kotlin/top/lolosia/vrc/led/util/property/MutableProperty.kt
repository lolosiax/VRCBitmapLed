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

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 可修改的可监听的属性
 *
 * @author 洛洛希雅Lolosia
 * @since 2024-08-27 15:25
 */
interface MutableProperty<T> : Property<T>, ReadWriteProperty<Any?, T> {
    companion object {
        operator fun <T> invoke(value: T): MutableProperty<T> {
            return MutablePropertyImpl(value)
        }
    }

    override var value: T
}

private class MutablePropertyImpl<T>(value: T) : AbstractProperty<T>(), MutableProperty<T> {

    override var value = value
        set(value) {
            val oldValue = field
            field = value
            if (value !== oldValue) {
                afterChange(value, oldValue)
            }
        }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    /** 创建一个只读的视图  */
    fun readonlyView(): Property<T> = object : Property<T> by this {}
}