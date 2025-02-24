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
import libui.uiAlignEnd
import libui.uiAlignStart
import top.lolosia.installer.service.EnvironmentService
import top.lolosia.installer.ui.component.AnyComponent
import top.lolosia.installer.ui.component.BaseContainer
import top.lolosia.installer.ui.component.component
import top.lolosia.installer.ui.layout.BaseLayout
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * DownloadPage
 * @author 洛洛希雅Lolosia
 * @since 2025-02-22 20:21
 */
class EnvironmentPage(
    val service: EnvironmentService
) : BaseContainer.VMode(), IRouterView<VBox> {
    override val layout = BaseLayout::class
    val mainStatus = Label("正在准备运行环境……").component()
    val mainSpeed = Label("0.0 kb/s").component()
    val mainProgress = ProgressBar().component()
    val textArea = TextArea().component()
    val grid = GridPane().apply {
        hexpand = true
        halign = uiAlignStart
        add(mainStatus.container)
        halign = uiAlignEnd
        add(mainSpeed.container)
        row()
        xspan = 2
        add(mainProgress.container)
    }.component()

    init {
        setupLayout()
    }

    private fun setupLayout(){
        add(grid)
    }

    fun updateStatus(status: String) {
        mainStatus.container.text = status
    }

    fun updateProgress(progress: Double) {
        mainProgress.container.value = max(min(progress * 100, 100.0), 0.0).roundToInt()
    }

    fun appendText(text: String) {
        textArea.container.append(text + "\n")
    }
}