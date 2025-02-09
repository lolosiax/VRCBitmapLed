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

import com.fasterxml.jackson.annotation.JsonValue
import org.yaml.snakeyaml.nodes.MappingNode
import top.lolosia.vrc.led.util.config.NodeChainConfig

/**
 * 父级服务设置
 * @author 洛洛希雅Lolosia
 * @since 2024-10-27 15:11
 */
class ParentConfig(parent: NodeChainConfig, node: MappingNode) : NodeChainConfig(parent, node) {
    var mode by notnull(HostType.SERVER)

    /** 选中的服务器地址 */
    var selected: String? by default(null)

    var records: List<String> by notnull(listOf())

    val rootUrl: String
        get() {
            if (selected == null) throw NoSuchElementException("Parent server has not been loaded yet!")
            return "http://${selected}"
        }

    val baseUrl: String get() = "${rootUrl}/api"

    enum class HostType(@JsonValue val value: String) {
        CLIENT("client"),
        SERVER("server"),
    }
}