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

package top.lolosia.vrc.led.service

import top.lolosia.vrc.led.config.web.WebConfig
import top.lolosia.vrc.led.util.property.MutableProperty
import top.lolosia.vrc.led.util.property.TriggerProperty
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener

@Service
class JmDnsService : ServiceListener {

    @Autowired
    lateinit var jmDNS: WebConfig.JmDNSHolder
    private val mServices = mutableSetOf<String>()
    val services by TriggerProperty { mServices.toList() }
    final var isSupported by MutableProperty(false)
        private set

    @PostConstruct
    fun init() {
        if (jmDNS.jmDNS == null) return
        jmDNS().addServiceListener("_http._tcp.local.", this)
        isSupported = true
    }

    override fun serviceAdded(event: ServiceEvent) {}

    override fun serviceRemoved(event: ServiceEvent) {
        val ip = event.info.inet4Addresses.firstOrNull() ?: return
        val port = event.info.port
        mServices.remove("${ip.hostAddress}:$port")
        TriggerProperty.trigger(::services)
    }

    override fun serviceResolved(event: ServiceEvent) {
        if (event.name != "exp-ai-dns-service") return
        val ip = event.info.inet4Addresses.firstOrNull() ?: return
        val port = event.info.port
        mServices.add("${ip.hostAddress}:$port")
        TriggerProperty.trigger(::services)
    }
}