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

/**
 * 计算式可监听属性
 *
 * @param dependencies 计算属性所需依赖
 * @param block 用于计算结果的回调
 * @author 洛洛希雅Lolosia
 * @since 2024-08-28 17:27
 */
class ComputedProperty<T>(
    vararg dependencies: KProperty0<*>,
    val block: () -> T
) : AbstractTriggerProperty<T>() {

    override val rawValue: T
        get() = block()

    // 此字段用于保持侦听器不被 GC。
    @Suppress("unused")
    private val hooks = dependencies.map {
        it.asProperty().addListener(false) {
            trigger()
        }
    }

    private var updating = false

    override fun trigger() {
        if (updating) throw IllegalStateException("不允许循环更新！")
        synchronized(this) {
            try {
                updating = true
                super.trigger()
            } finally {
                updating = false
            }
        }
    }
}