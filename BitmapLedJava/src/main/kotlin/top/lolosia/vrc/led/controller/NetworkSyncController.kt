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

package top.lolosia.vrc.led.controller

import top.lolosia.vrc.led.service.NetworkSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.coroutines.CoroutineContext


@RestController
@RequestMapping("/api/client")
class NetworkSyncController : CoroutineScope {

    private val logger = LoggerFactory.getLogger(NetworkSyncController::class.java)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    @Autowired
    lateinit var adapter: WebSocketHandlerAdapter

    @Autowired
    lateinit var service: NetworkSyncService

    @GetMapping("/")
    fun handleClient(exchange: ServerWebExchange): Mono<Void> {
        return adapter.webSocketService.handleRequest(exchange) { ws ->
            mono {
                val (_, job) = service.handleWebSocketSession(ws)
                try {
                    job.join()
                } finally {
                    logger.info("WebSocket disconnected Mono<Void>")
                }
            }.then()
        }
    }
}