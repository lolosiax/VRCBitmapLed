package top.lolosia.installer

import kotlinx.coroutines.runBlocking
import top.lolosia.installer.ui.window.MainWindow


fun main() {
    val mainWindow = runBlocking {
        MainWindow.create()
    }

    return
}