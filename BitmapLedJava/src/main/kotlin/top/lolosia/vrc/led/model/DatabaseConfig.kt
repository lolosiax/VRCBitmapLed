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

package top.lolosia.vrc.led.model

import top.lolosia.vrc.led.util.isClient
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebeaninternal.api.SpiEbeanServer
import org.springframework.cglib.proxy.Enhancer
import org.springframework.cglib.proxy.MethodInterceptor
import org.springframework.cglib.proxy.MethodProxy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Method

@Configuration
class DatabaseConfig {
    @Bean("db")
    fun getDefaultDatabase() = database("db")

    private fun database(name: String): Database {
        return serverModeLoader {
            DatabaseFactory.create(name)
        }
    }

    private fun serverModeLoader(block: () -> Database): Database {
        val eh = Enhancer()
        eh.setSuperclass(SpiEbeanServer::class.java)
        eh.setCallback(ServerModeInterceptor(block))
        val db = eh.create()
        return db as Database
    }

    private class ServerModeInterceptor(val block: () -> Database) : MethodInterceptor {

        private var db: Database? = null

        @Synchronized
        fun getDb(): Database {
            if (db == null) {
                db = block()
            }
            return db!!
        }

        override fun intercept(obj: Any, method: Method, args: Array<out Any>, proxy: MethodProxy): Any? {

            if (isClient) {
                if (method.name == "shutdown") {
                    db?.let {
                        proxy.invoke(it, args)
                    }
                    return Unit
                }
                throw IllegalStateException("The application mode is not server.")
            }

            return proxy.invoke(getDb(), args)
        }
    }
}