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

package top.lolosia.vrc.led.service.led

import com.illposed.osc.OSCMessage
import com.illposed.osc.transport.OSCPortOut
import org.springframework.stereotype.Service
import top.lolosia.vrc.led.business.OscChar
import java.net.InetAddress

/**
 * OscService
 * @author 洛洛希雅Lolosia
 * @since 2025-02-09 21:12
 */
@Service
class OscService {

    val oscClient: OSCPortOut by lazy {
        OSCPortOut(InetAddress.getByName("localhost"), 9000)
    }

    /**
     * 向屏幕的同步参数区域发送数据。
     * 两次调用的间隔不应小于0.14秒。
     */
    suspend fun sendOsc(pointer: Int, char: OscChar) {
        val (x24, x16, x8) = char
        oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/Pointer", listOf(pointer)))
        oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/DataX24", listOf(x24.toInt())))
        oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/DataX16", listOf(x16.toInt())))
        oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/Data", listOf(x8.toInt())))
    }

    /**
     * 直接向屏幕的非同步参数区域发送数据
     */
    suspend fun sendOscInternal(pointer: Int, char: OscChar) {
        val (x24, x16, x8) = char
        oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/Data${pointer + 512}", listOf(x24.toInt())))
        oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/Data${pointer * 2}", listOf(x16.toInt())))
        oscClient.send(OSCMessage("/avatar/parameters/BitmapLed/Data${pointer * 2 + 1}", listOf(x8.toInt())))
    }
}