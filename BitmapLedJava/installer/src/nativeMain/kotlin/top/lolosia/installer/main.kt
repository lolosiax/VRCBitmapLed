package top.lolosia.installer

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import libui.ktx.Label
import libui.ktx.VBox
import libui.ktx.appWindow
import top.lolosia.installer.util.threading.staThread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun main() = runBlocking {
    // localWindowTest()
    val name = getCurrentProcessFileName()
    println(name)
    Installer.main()
}

private suspend fun localWindowTest(){

    val window = suspendCoroutine { continuation ->
        staThread("main") {
            appWindow(title = "VRC Bitmap Led", width = 460, height = 240) {
                continuation.resume(this)
            }
            println("UI Thread exited!")
        }
    }
    delay(200)

    withUI {
        val box = VBox()
        window.add(box)
        box.add(Label("Hello World!"))
    }

    while (1 == 1) {
        delay(5000)
        println("wait...")
    }
}