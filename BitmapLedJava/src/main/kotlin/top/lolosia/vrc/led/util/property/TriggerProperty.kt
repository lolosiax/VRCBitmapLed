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

import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

/**
 * 触发式可监听属性
 *
 * @author 洛洛希雅Lolosia
 * @since 2024-08-28 16:24
 */
class TriggerProperty<T>(private val getter: () -> T) : AbstractTriggerProperty<T>() {

    companion object {

        /**
         * 触发 TriggerProperty 的更新
         */
        fun trigger(prop: KProperty0<*>): TriggerProperty<*> {
            prop.isAccessible = true
            val prop1 = prop.getDelegate() as TriggerProperty<*>
            prop1.trigger()
            return prop1
        }
    }

    override val rawValue: T
        get() = getter()

    public override fun trigger() {
        super.trigger()
    }
}