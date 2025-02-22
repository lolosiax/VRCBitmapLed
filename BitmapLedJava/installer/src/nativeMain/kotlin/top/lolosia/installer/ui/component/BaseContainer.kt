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

package top.lolosia.installer.ui.component

import libui.ktx.Box
import libui.ktx.HBox
import libui.ktx.VBox

/**
 * BaseContainer
 * @author 洛洛希雅Lolosia
 * @since 2025-02-22 20:14
 */
abstract class BaseContainer<T> : IContainer<T> where T : Box {
    private val mChildren = mutableListOf<IComponent<*>>()
    override val children: List<IComponent<*>> = mChildren

    override fun add(component: IComponent<*>) {
        component.parent = this
        container.add(component.container)
        mChildren.add(component)
    }

    override fun remove(component: IComponent<*>) {
        val i = mChildren.indexOf(component)
        container.delete(i)
        mChildren.remove(component)
        component.parent = null
    }

    override fun clear() {
        for (i in mChildren.size - 1 downTo 0) {
            container.delete(i)
            mChildren.removeAt(i).parent = null
        }
    }

    open class HMode : BaseContainer<HBox>() {
        override val container = HBox()
    }

    open class VMode : BaseContainer<VBox>() {
        override val container = VBox()
    }
}