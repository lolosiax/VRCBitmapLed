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

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import libui.ktx.Label
import libui.ktx.VBox
import libui.ktx.appWindow
import top.lolosia.installer.util.threading.staThread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun main() = runBlocking {
    Installer.main()
}

private suspend fun localWindowTest(){
    val name = getCurrentProcessFileName()
    println(name)
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