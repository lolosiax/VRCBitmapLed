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

package top.lolosia.vrc.led.manager

import com.fasterxml.jackson.databind.json.JsonMapper
import top.lolosia.vrc.led.annotation.NetworkSync
import top.lolosia.vrc.led.service.NetworkSyncService
import top.lolosia.vrc.led.util.network.Invoker
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.lang.reflect.Method
import java.util.*

@Service
class NetworkSyncManager {

    private val logger = LoggerFactory.getLogger(NetworkSyncManager::class.java)
    private val registry: MutableMap<String, LocalInvoker> = mutableMapOf()
    private val hashes = LinkedHashSet<String>()

    @Autowired
    lateinit var mapper: JsonMapper

    @Autowired
    lateinit var applicationContext: ApplicationContext

    val service by lazy { applicationContext.getBean<NetworkSyncService>() }

    fun register(obj: Any, method: Method) {
        val ann = method.getAnnotation(NetworkSync::class.java)
        val clazzName = obj::class.java.name
        val namespace = "$clazzName/${ann.namespace}"
        registry[namespace] = LocalInvoker(obj, method, namespace)
    }

    fun getRemoteInvoker(obj: Any, namespace: String): Invoker? {
        val clazzName = obj::class.java.name
        val namespace1 = "$clazzName/${namespace}"
        val localInvoker = registry[namespace1] ?: return null
        return RemoteInvoker(localInvoker)
    }

    fun onNetworkSync(hash: String, namespace: String, vararg message: String) {
        synchronized(hashes) {
            if (hashes.contains(hash)) return

            hashes.add(hash)
            // 移除500个记录
            if (hashes.size > 1000) {
                var count = 0
                val iter = hashes.iterator()
                while (count < 500) {
                    iter.next()
                    iter.remove()
                    count++
                }
            }
        }

        val invoker = registry[namespace] ?: return
        val types = invoker.method.parameterTypes
        if (types.size != message.size) {
            throw IllegalArgumentException("Inconsistent number of parameters, need ${types.size} got ${message.size}")
        }
        val params = message.mapIndexed { i, it ->
            mapper.readValue(it, types[i])
        }
        invoker(*params.toTypedArray())
    }

    private fun postNetworkSync(invoker: LocalInvoker, vararg args: Any?) {
        val ws = service.sockets
        val namespace = invoker.namespace
        val hash = UUID.randomUUID().toString()
        synchronized(hashes) {
            hashes.add(hash)
        }
        val args1 = arrayOf(hash, namespace) + args.map {
            mapper.writeValueAsString(it)
        }
        val str = args1.joinToString("\n")

        logger.debug("SEND TO: ${str.replace("\n", ", ")}")

        ws.forEach {
            it.send(str)
        }
    }

    private class LocalInvoker(
        val obj: Any,
        val method: Method,
        val namespace: String,
    ) : Invoker {
        init {
            method.isAccessible = true
        }

        override operator fun invoke(vararg args: Any?) {
            method.invoke(obj, *args)
        }
    }

    private inner class RemoteInvoker(val localInvoker: LocalInvoker) : Invoker {
        override fun invoke(vararg args: Any?) {
            postNetworkSync(localInvoker, *args)
        }
    }
}