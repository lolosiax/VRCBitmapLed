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

import libui.ktx.*
import top.lolosia.installer.service.EnvironmentService
import top.lolosia.installer.ui.component.BaseContainer
import top.lolosia.installer.ui.layout.BaseLayout

/**
 * DownloadPage
 * @author 洛洛希雅Lolosia
 * @since 2025-02-22 20:21
 */
class EnvironmentPage(
    val service: EnvironmentService
) : BaseContainer.VMode(), IRouterView<VBox> {
    override val layout = BaseLayout::class
    val components = mutableListOf<Pair<Label, ProgressBar>>()

    override val container = VBox().apply {
        val mainStatus = label("正在准备运行环境……")
        val mainProgress = progressbar()
        hbox {
            vbox {
                components += List(5) { i ->
                    label("任务${i + 1}") to progressbar()
                }
            }
            vbox {
                components += List(5) { i ->
                    label("任务${i + 6}") to progressbar()
                }
            }
            components += mainStatus to mainProgress
        }
    }
}