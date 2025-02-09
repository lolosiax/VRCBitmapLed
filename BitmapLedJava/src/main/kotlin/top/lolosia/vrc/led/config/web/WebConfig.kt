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

package top.lolosia.vrc.led.config.web

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import top.lolosia.vrc.led.config.ParentConfig
import top.lolosia.vrc.led.config.SConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer
import top.lolosia.vrc.led.BitmapLed.applicationContext
import top.lolosia.vrc.led.util.property.PropertyCallback
import top.lolosia.vrc.led.util.property.asProperty
import top.lolosia.vrc.led.util.spring.ApplicationContextProvider
import top.lolosia.vrc.led.util.spring.ContextArgumentResolver
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo
import javax.jmdns.impl.JmDNSImpl

@Configuration
@EnableWebFlux
@EnableScheduling
class WebConfig : WebFluxConfigurer {

    val logger = LoggerFactory.getLogger(WebConfig::class.java)

    @Autowired
    lateinit var contextArgumentResolver: ContextArgumentResolver

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().maxInMemorySize(128 * 1024 * 1024)
    }

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(contextArgumentResolver)
    }

    @Bean
    fun getPasswordEncoder(): PasswordEncoder = BCryptPasswordEncoder(10)

    @Bean
    fun getJsonMapper() = JsonMapper().apply {
        registerModule(JavaTimeModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    @Bean
    fun getApplicationContextProvider(): ApplicationContextProvider {
        return ApplicationContextProvider { applicationContext }
    }

    private lateinit var jmDnsHook: PropertyCallback<ParentConfig.HostType>

    @Bean
    fun jmDns(): JmDNSHolder {
        // JmDNS创建可能会失败
        try {
            val jmDns = JmDNS.create()
            val serviceName = "vrc-led-dns-service"
            val parent = SConfig.host.serviceParent


            jmDnsHook = parent::mode.asProperty().addListener {
                val service = (jmDns as JmDNSImpl).services[serviceName]

                if (it == ParentConfig.HostType.SERVER) {
                    if (service != null) return@addListener
                    val info = ServiceInfo.create(
                        "_http._tcp.local.",
                        serviceName,
                        SConfig.server.port,
                        "hello-world"
                    )
                    jmDns.registerService(info)
                } else {
                    if (service != null) {
                        jmDns.unregisterService(service)
                    }
                }
            }

            jmDnsHook.invoke(parent.mode)

            return JmDNSHolder(jmDns)
        } catch (e: Throwable) {
            logger.error("初始化JmDNS服务时发生异常", e)
            return JmDNSHolder(e = e)
        }
    }

    data class JmDNSHolder(val jmDNS: JmDNS? = null, val e: Throwable? = null) {
        operator fun invoke(): JmDNS {
            return jmDNS ?: throw e!!
        }
    }
}