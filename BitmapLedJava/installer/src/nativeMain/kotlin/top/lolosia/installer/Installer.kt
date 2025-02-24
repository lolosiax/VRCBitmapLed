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

package top.lolosia.installer

import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import top.lolosia.installer.service.EnvironmentService
import top.lolosia.installer.ui.component.dispatch
import top.lolosia.installer.ui.view.JvmBootPage
import top.lolosia.installer.ui.window.MainWindow

/**
 * Installer
 * @author 洛洛希雅Lolosia
 * @since 2025-02-23 00:14
 */
object Installer {
    lateinit var mainWindow: MainWindow
    lateinit var environmentService: EnvironmentService

    val baseDir by lazy {
        val dir = Path(".")
        return@lazy SystemFileSystem.resolve(dir)
    }

    val workDir by lazy {
        val dir = Path("work")
        if (!SystemFileSystem.exists(dir)) SystemFileSystem.createDirectories(dir)
        return@lazy SystemFileSystem.resolve(dir)
    }

    suspend fun main() {
        mainWindow = MainWindow.create()
        environmentService = EnvironmentService()
        mainWindow.dispatch {
            activePage = environmentService.view
        }
        delay(50)
        // awaitCancellation()
        environmentService.checkEnvironment()

        val bootPage = withUI { JvmBootPage() }
        mainWindow.dispatch {
            activePage = bootPage
        }
        bootPage.await()

        runJvm()

        awaitCancellation()
    }
}