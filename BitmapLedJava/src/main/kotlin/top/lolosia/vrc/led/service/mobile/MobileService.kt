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

package top.lolosia.vrc.led.service.mobile

import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.env.get
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import top.lolosia.vrc.led.business.OscChar
import top.lolosia.vrc.led.business.background
import top.lolosia.vrc.led.business.color
import top.lolosia.vrc.led.business.toOsc
import top.lolosia.vrc.led.controller.mobile.MobileController
import top.lolosia.vrc.led.manager.led.OscManager
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundleScope
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.success
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*

/**
 * MobileService
 * @author 洛洛希雅Lolosia
 * @since 2025-02-19 18:54
 */
@Service
class MobileService {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var oscService: OscManager

    /**
     * 获取所有可用服务地址
     */
    suspend fun getServerHost(ctx: Context): List<Bundle> {
        val port = applicationContext.environment["server.port"]
        var add: List<Pair<String, String>> = mutableListOf()
        val faces = withContext(Dispatchers.IO) {
            NetworkInterface.getNetworkInterfaces()
        }
        while (faces.hasMoreElements()) { // 遍历网络接口
            val face = faces.nextElement()
            if (face.isVirtual || !face.isUp) continue
            val address = face.inetAddresses
            val name = face.displayName
            while (address.hasMoreElements()) { // 遍历网络地址
                val addr = address.nextElement()
                (add as MutableList<Pair<String, String>>) += name to addr.hostAddress
            }
        }
        add = add
            .filter { !it.second.startsWith("fe80") }
            .map { (n, it) -> n to it.replace("%.+".toRegex(), "") }
        val ipv6 = add.filter { ":" in it.second }.map { (n, it) -> n to "[${it}]" }
        add = add.filter { ":" !in it.second } + ipv6
        val bundles = add.map { (n, it) ->
            bundleScope {
                "interfaceName" set n
                "address" set "$it:$port"
            }
        }

        return bundles
    }

    suspend fun setText(ctx: Context, text: MobileController.SetTextFn) {
        val start = text.start * 16
        val end = text.end * 16
        val color = text.color.drop(1).toInt(16)
        var osc = text.text.toOsc()
        osc = if (text.background) osc background color
        else osc color color
        oscService.sendText(osc, start, end)
    }

    private var timer: Job? = null

    suspend fun switchTimer(ctx: Context): ResponseEntity<ByteArray> {
        if (timer == null) {
            timer = CoroutineScope(Dispatchers.Default).launch {
                while (isActive) {
                    val formatter = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss z")
                    val date = Date(System.currentTimeMillis())
                    val osc = formatter.format(date).toOsc() color 0xFFFF00
                    oscService.sendText(osc, 0)

                    delay(1000)
                }
            }
            return success(true)
        }
        timer?.cancel()
        timer = null
        oscService.send(255, OscChar(4))
        return success(false)
    }
}