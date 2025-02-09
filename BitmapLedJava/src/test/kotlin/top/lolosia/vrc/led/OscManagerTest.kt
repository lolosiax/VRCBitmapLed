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

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import top.lolosia.vrc.led.business.background
import top.lolosia.vrc.led.business.color
import top.lolosia.vrc.led.business.toOsc
import top.lolosia.vrc.led.manager.led.OscManager
import top.lolosia.vrc.led.service.led.OscService
import java.text.SimpleDateFormat
import java.util.*


/**
 * OscManagerTest
 * @author 洛洛希雅Lolosia
 * @since 2025-02-09 21:40
 */
@SpringBootTest(classes = [OscManager::class, OscService::class])
class OscManagerTest {

    @Autowired
    lateinit var manager: OscManager

    @Test
    @OptIn(ExperimentalStdlibApi::class)
    fun drawTest() = runBlocking {
        manager.scheduler.onOscSending += {
            val char = it.value
            val pointer = it.pointer
            println("${pointer}: ${char.char}, ${char.char.code.toHexString().slice(4 until 8)}")
        }
        val job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val formatter = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss z")
                val date = Date(System.currentTimeMillis())
                val osc = formatter.format(date).toOsc() color 0xFFFF00
                manager.sendText(osc, 0)

                delay(1000)
            }
        }

        val job1 = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                var str = System.currentTimeMillis().toString()
                var osc = str.toOsc() background 0x7FFFBF
                manager.sendRectText(osc, 4 until 12, 2 until 3, overlay = true)
                str = System.currentTimeMillis().toHexString()
                osc = str.toOsc() background 0xBF7FFF
                manager.sendRectText(osc, 4 until 12, 3 until 4, overlay = true)
                delay(100)
            }
        }

        listOf(job, job1).joinAll()
        Unit
    }

    @Test
    fun drawLatter() = runBlocking {
        manager.sendText("零00一01二02三03四04五05六06".toOsc(), 0)
        manager.sendText("零一二三四五六七八九".toOsc(), 16)
        println("等待发送结束...")
        delay(10000)
    }

}