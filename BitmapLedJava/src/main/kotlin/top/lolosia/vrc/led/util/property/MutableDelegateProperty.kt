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

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * 委托的可监听属性
 * @author 洛洛希雅Lolosia
 * @since 2024-08-28 23:49
 */
open class MutableDelegateProperty<T> : DelegateProperty<T>, MutableProperty<T> {

    constructor(property: MutableProperty<T>) : super(property)

    constructor(property: KMutableProperty0<T>) : super(property)

    constructor(property: () -> MutableProperty<T>) : super(property)

    override var value: T
        get() = super.value
        set(value) {
            (property as MutableProperty<T>).value = value
        }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        (this.property as MutableProperty<T>).setValue(thisRef, property, value)
    }
}