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

package top.lolosia.installer.ui.view

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import libui.ktx.VBox
import libui.ktx.button
import top.lolosia.installer.ui.component.BaseContainer
import top.lolosia.installer.ui.layout.BaseLayout
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * JvmBootPage
 * @author 洛洛希雅Lolosia
 * @since 2025-02-24 21:07
 */
class JvmBootPage : BaseContainer.VMode(), IRouterView<VBox> {
    override val layout = BaseLayout::class
    private val lock = SynchronizedObject();
    private val listeners = mutableListOf<() -> Unit>()

    init {
        initLayout()
    }

    private fun initLayout() {
        container.button("启动JVM") {
            action {
                synchronized(lock) {
                    listeners.forEach { it() }
                    listeners.clear()
                }
            }
        }
    }

    suspend fun await() {
        suspendCoroutine { continuation ->
            synchronized(lock) {
                listeners.add {
                    continuation.resume(Unit)
                }
            }
        }
    }
}