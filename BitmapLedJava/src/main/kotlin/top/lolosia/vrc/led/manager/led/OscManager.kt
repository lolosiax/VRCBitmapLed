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

package top.lolosia.vrc.led.manager.led

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import top.lolosia.vrc.led.business.OscChar
import top.lolosia.vrc.led.business.OscScheduler
import top.lolosia.vrc.led.event.led.OscSendEvent
import top.lolosia.vrc.led.service.led.OscService
import kotlin.math.min

/**
 * OscManager
 * @author 洛洛希雅Lolosia
 * @since 2025-02-09 14:54
 */
@Service
class OscManager {

    final val scheduler = OscScheduler()

    @Autowired
    lateinit var applicationContext: ApplicationContext

    private val logger = LoggerFactory.getLogger(this::class.java)

    var fastShow: Boolean = false

    private val service by lazy {
        applicationContext.getBean<OscService>()
    }

    /** 本地叠加层，实时显示 */
    private val overlay = Array(256) { OscChar() }

    @PostConstruct
    fun init() {
        scheduler.onOscSending += ::onOscSending
        CoroutineScope(Dispatchers.Default).launch {
            localSyncJob()
        }
    }

    fun send(pointer: Int, data: OscChar, overlay: Boolean = false) {
        if (overlay) this.overlay[pointer] = data
        else scheduler.updateMemory(pointer, data)
    }

    fun sendText(
        data: List<OscChar>,
        start: Int,
        maxLength: Int = 10000,
        ellipsis: Boolean = false,
        overlay: Boolean = false
    ) {
        for ((i, c) in data.withIndex()) {
            val max = min(start + maxLength, 256)
            if (start + i < max) {
                send(start + i, c, overlay)
            } else {
                send(max - 1, data[i - 1] char '…', overlay)
                break
            }
        }
    }

    fun sendRectText(
        data: List<OscChar>,
        x: IntRange,
        y: IntRange,
        ellipsis: Boolean = false,
        overlay: Boolean = false
    ) {
        val rows = y.last - y.first + 1
        val cols = x.last - x.first + 1
        val mat = Array(rows) { Array(cols) { OscChar(0) } }
        for ((i, c) in data.withIndex()) {
            val row = i / cols
            val col = i % cols
            if (row < rows) {
                mat[row][col] = c
            } else {
                if (ellipsis) mat[rows - 1][cols - 1] = mat[rows - 1][cols - 1] char '…'
                break
            }
        }
        for ((dy, line) in mat.withIndex()) {
            for ((dx, c) in line.withIndex()) {
                val x1 = x.first + dx
                val y1 = y.first + dy
                if (x1 in 0..x.last && y1 in 0..y.last) {
                    send(y1 * 16 + x1, c, overlay)
                }
            }
        }
    }

    fun onOscSending(e: OscSendEvent) {
        CoroutineScope(Dispatchers.Default).launch {
            service.sendOsc(e.pointer, e.value)
        }
    }

    suspend fun localSyncJob() {
        while (true) {
            try {
                val dp = if (fastShow) scheduler.getLocalDisplay()
                else scheduler.getRemoteDisplay()

                for ((i, c) in overlay.zip(dp).withIndex()) {
                    val (ov, rd) = c
                    if (ov.value == 0) service.sendOscInternal(i, rd)
                    else service.sendOscInternal(i, ov)
                }
            } catch (e: Exception) {
                logger.warn("本地同步过程中发生异常", e)
            }
            delay(150)
        }
    }
}