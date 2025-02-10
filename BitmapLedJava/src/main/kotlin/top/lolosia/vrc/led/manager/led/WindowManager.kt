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

package top.lolosia.vrc.led.manager.led

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import top.lolosia.vrc.led.config.SConfig
import java.awt.Desktop
import java.awt.Toolkit
import java.net.URI
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

/**
 * WindowManager
 * @author 洛洛希雅Lolosia
 * @since 2025-02-10 19:25
 */
@Service
class WindowManager {

    @PostConstruct
    fun init() {
        showMainWindow()
    }

    fun showMainWindow() {
        val url = "http://127.0.0.1:${SConfig.server.port}/"
        val edge = """C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe"""

        if (!Path(edge).exists()) {
            return Desktop.getDesktop().browse(URI.create(url))
        }

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val w = 1000
        val h = 600
        val x = (screenSize.width - w) / 2
        val y = (screenSize.height - h) / 2

        val userDataDir = Path("./work/webview")
        if (!userDataDir.exists()) userDataDir.createDirectories()

        Runtime.getRuntime().exec(
            """
            "$edge"
            --disable-extensions
            --window-size=$w,$h
            --window-position=$x,$y
            --window-name="Bitmap Led - VRC点阵屏幕"
            --user-data-dir="${userDataDir.absolutePathString()}"
            --disable-features=msEdgeMouseGestureDefaultEnabled,msEdgeMouseGestureSupported
            --app="$url"
            """.trimIndent().split("\n").toTypedArray()
        )
    }
}