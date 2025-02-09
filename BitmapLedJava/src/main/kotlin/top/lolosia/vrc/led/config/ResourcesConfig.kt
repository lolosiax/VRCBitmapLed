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

package top.lolosia.vrc.led.config

import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundle
import top.lolosia.vrc.led.util.bundle.bundleOf
import top.lolosia.vrc.led.util.config.ChangeableMap

class ResourcesConfig(override val data: Bundle) : ChangeableMap() {

    private val keys by lazy { data.keys.toList().sortedBy { -it.length } }

    operator fun get(key: String): Entity {
        val matched = keys.firstOrNull { key.startsWith(it) } ?: ""
        return Entity(matched)
    }

    inner class Entity(key: String) : ChangeableMap() {
        override val data = this@ResourcesConfig.data.bundle(key) ?: bundleOf()

        /**
         * 转发模式。
         *
         * pass: 直接前往资源文件夹，
         * redirect：进行302跳转，
         * proxy：通过服务器直接进行转发。
         */
        val mode by this("pass")

        /**
         * 重定向到的服务器地址
         */
        val redirect : String? by this(null)
        // val overrider : String? by this(null)
    }
}