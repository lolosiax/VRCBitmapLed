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

import com.illposed.osc.OSCMessage
import com.illposed.osc.transport.OSCPortOut
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import top.lolosia.vrc.led.business.OscChar
import java.net.InetAddress

/**
 * RawOscTest
 * @author 洛洛希雅Lolosia
 * @since 2025-02-09 23:17
 */
class RawOscTest {

    @Test
    fun drawRaw() = runBlocking {
        val oscClient = OSCPortOut(InetAddress.getByName("localhost"), 9000)
        fun sendOsc(pointer: Int, char: Char) {
            val code = char.code
            val h = code shr 8 and 0xFF
            val l = code and 0xFF
            oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/Pointer", listOf(pointer)))
            // oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/DataX24", listOf(x24.toInt())))
            oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/DataX16", listOf(h)))
            oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/Data", listOf(l)))
        }

        sendOsc(255, 4.toChar())

        while (true){
            val list = "一二三四五六七八九十"
            for(i in list){
                sendOsc(list.indexOf(i), i)
                delay(200)
            }
        }
    }


    @Test
    fun drawOsc() = runBlocking {
        val oscClient = OSCPortOut(InetAddress.getByName("localhost"), 9000)
        fun sendOsc(pointer: Int, char: OscChar) {
            val (x24, x16, x8) = char
            oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/Pointer", listOf(pointer)))
            oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/DataX24", listOf(x24.toInt())))
            oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/DataX16", listOf(x16.toInt())))
            oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/Data", listOf(x8.toInt())))
        }

        sendOsc(255, OscChar(4))

        while (true){
            val list = "一二三四五六七八九十"
            for(i in list){
                sendOsc(list.indexOf(i), OscChar(i) color 0x00FFFF)
                delay(200)
            }
        }
    }
}