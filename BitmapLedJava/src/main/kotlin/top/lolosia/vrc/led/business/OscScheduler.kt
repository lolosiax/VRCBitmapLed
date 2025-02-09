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

package top.lolosia.vrc.led.business

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import top.lolosia.vrc.led.event.led.OscSendEvent
import top.lolosia.vrc.led.util.event.EventHandle
import top.lolosia.vrc.led.util.event.IEventHandle
import kotlin.math.max

class OscScheduler {
    // 共享数据结构
    private data class GroupData(
        var data: OscChar = OscChar(),
        var modified: Boolean = false,
        var lastUpdate: Long = 0,
        var updateCount: Int = 0
    )

    private val groups = Array(256) { GroupData() }
    private val remoteDisplay = Array(256) { OscChar() } // 模拟远程显示状态
    private val mutex = Mutex()
    private val realtimeChannel = Channel<Int>(Channel.UNLIMITED)
    private var backgroundIndex = 0
    private val heatMap = mutableMapOf<Int, Int>()
    private var lastHeatUpdate = System.currentTimeMillis()

    // 时间片配置
    private val TIME_SLOT = 200L // 0.2秒
    private val HEAT_WINDOW = 10_000L // 10秒
    private val BASE_WEIGHT = 1.0f // 基础权重

    var isActive : Boolean = true
        private set

    private val mOnOscSending = EventHandle<OscSendEvent>()
    val onOscSending: IEventHandle<OscSendEvent> get() = mOnOscSending

    init {
        CoroutineScope(Dispatchers.Default).launch {
            startScheduler()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateMemory(pointer: Int, data: OscChar) {
        check(pointer in 0..255) { "Invalid pointer" }
        CoroutineScope(Dispatchers.Default).launch {
            mutex.withLock {
                val group = groups[pointer]
                if (group.data != data) {
                    group.data = data
                    group.modified = true
                    group.lastUpdate = System.currentTimeMillis()
                    group.updateCount++

                    // 更新热点统计
                    heatMap[pointer] = heatMap.getOrDefault(pointer, 0) + 1

                    // 触发实时更新
                    if (!realtimeChannel.isClosedForSend) {
                        realtimeChannel.send(pointer)
                    }
                }
            }
        }
    }

    suspend fun getRemoteDisplay() : Array<OscChar> = mutex.withLock {
        remoteDisplay.clone()
    }

    suspend fun getLocalDisplay() : Array<OscChar> = mutex.withLock {
        Array(256) { groups[it].data }
    }

    suspend fun getWeights(): Array<Float> = mutex.withLock {
        val now = System.currentTimeMillis()
        return Array(256) { pointer ->
            val heatValue = heatMap[pointer]?.takeIf {
                now - groups[pointer].lastUpdate <= HEAT_WINDOW
            } ?: 0
            BASE_WEIGHT + heatValue * 0.5f
        }
    }

    private suspend fun startScheduler() {
        while (isActive) {
            val startTime = System.currentTimeMillis()

            // 优先处理实时更新
            val realtimePointer = realtimeChannel.tryReceive().getOrNull()
            if (realtimePointer != null) {
                sendData(realtimePointer)
                groups[realtimePointer].modified = false
            } else {
                // 处理热点更新
                val heatPointer = findHotPoint()
                if (heatPointer != null) {
                    sendData(heatPointer)
                    groups[heatPointer].modified = false
                } else {
                    // 后台刷新
                    handleBackgroundRefresh()
                }
            }

            // 维护热点统计
            if (System.currentTimeMillis() - lastHeatUpdate > 1000) {
                updateHeatMap()
            }

            // 精确时间控制
            val elapsed = System.currentTimeMillis() - startTime
            delay(max(0, TIME_SLOT - elapsed))
        }

    }

    private suspend fun sendData(pointer: Int) {
        val data = mutex.withLock {
            groups[pointer].data.also {
                remoteDisplay[pointer] = it // 更新远程显示状态
            }
        }
        sendOsc(pointer, data)
    }

    private suspend fun findHotPoint(): Int? {
        mutex.withLock {
            return heatMap.entries
                .filter { (k, _) ->
                    groups[k].modified &&
                            (System.currentTimeMillis() - groups[k].lastUpdate <= HEAT_WINDOW)
                }
                .maxByOrNull { it.value }
                ?.key
        }
    }

    private suspend fun handleBackgroundRefresh() {
        var sent = false
        mutex.withLock {
            for (i in 0 until 256) {
                val index = (backgroundIndex + i) % 256
                if (groups[index].modified) {
                    backgroundIndex = (index + 1) % 256
                    sendData(index)
                    groups[index].modified = false
                    sent = true
                    break
                }
            }
            // 全屏清空优化
            if (!sent && groups.count { it.modified } > 128) {
                sendOsc(255, OscChar(0x000004))
                groups.forEach { it.modified = false }
                sent = true
            }
        }
    }

    private suspend fun updateHeatMap() {
        val now = System.currentTimeMillis()
        mutex.withLock {
            val iterator = heatMap.iterator()
            while (iterator.hasNext()) {
                val (k, _) = iterator.next()
                if (now - groups[k].lastUpdate > HEAT_WINDOW) {
                    iterator.remove()
                }
            }
        }
        lastHeatUpdate = now
    }

    // OSC发送接口
    private fun sendOsc(pointer: Int, data: OscChar) {
        mOnOscSending fire OscSendEvent(this, pointer, data)
    }

    fun close() {
        isActive = false
    }
}