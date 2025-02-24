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
    // 协程回调等待中断，创建UI线程并等待UI线程完成主窗口创建
    val window = suspendCoroutine { continuation ->
        staThread("main") { // 启动一个STA线程
            // 在UI线程创建窗口并进入消息循环
            appWindow(title = "VRC Bitmap Led", width = 460, height = 240) {
                // 在创建玩窗口的DSL中返回窗口对象，并唤醒主协程的中断。
                continuation.resume(this)
            }
            // 消息循环结束，代表程序退出了
            println("UI Thread exited!")
        }
    }
    // 等待UI线程初始化完成
    delay(200)

    // 阻塞方法，在UI线程执行任务
    withUI {
        // 创建控件以及添加控件到窗口上
        val box = VBox()
        window.add(box)
        box.add(Label("Hello World!"))
    }

    // 主协程等待程序退出
    while (1 == 1) {
        delay(5000)
        println("wait...")
    }
}