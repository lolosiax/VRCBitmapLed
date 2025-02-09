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

package top.lolosia.vrc.led

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import top.lolosia.vrc.led.business.toOsc
import top.lolosia.vrc.led.manager.led.OscManager

/**
 * OscSchedulerTest
 * @author 洛洛希雅Lolosia
 * @since 2025-02-09 19:27
 */
@SpringBootTest(classes = [OscManager::class])
class OscSchedulerTest {

    @Autowired
    lateinit var manager: OscManager

    @Test
    fun testScheduler() = runBlocking {
        manager.sendText("小猫咪小狐狸小狗小兔子张三李四王五赵六".toOsc(), 5)
        manager.sendRectText("服务器繁忙，请稍后再试。".toOsc(), 3 until 7, 3 until 7)
        printRect()
    }


    @OptIn(ExperimentalStdlibApi::class)
    suspend fun printRect() {
        val data = manager.scheduler.getLocalDisplay()
        for (y in 0 until 16) {
            for (x in 0 until 16) {
                val index = y * 16 + x
                print(data[index].char.code.toHexString().slice(4 until 8))
                print(' ')
            }
            print('\n')
        }
    }
}