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

package top.lolosia.installer.util.collection

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

/**
 * ConcurrentTaskQueue
 * @author 洛洛希雅Lolosia
 * @since 2025-02-22 00:35
 */
class ConcurrentTaskQueue(val maxConcurrency: Int) : AutoCloseable {
    private lateinit var channel: Channel<suspend (Int) -> Unit>

    private var running = false
    private var jobs = emptyList<Job>()

    fun start() {
        if (running) return
        running = true
        channel = Channel(Channel.UNLIMITED)
        jobs = List(maxConcurrency) { index ->
            CoroutineScope(Dispatchers.Default).launch {
                for (task in channel) {
                    try {
                        task(index)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    suspend fun send(task: suspend (no: Int) -> Unit) {
        channel.send(task)
    }

    suspend fun awaitFinish(){
        channel.close()
        jobs.joinAll()
        running = false
    }

    override fun close() {
        running = false
        channel.close()
        jobs.forEach { it.cancel() }
    }
}