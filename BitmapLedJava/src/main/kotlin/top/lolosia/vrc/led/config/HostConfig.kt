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

import org.yaml.snakeyaml.nodes.MappingNode
import top.lolosia.vrc.led.util.config.ChainProp
import top.lolosia.vrc.led.util.config.NodeChainConfig
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

class HostConfig(parent: NodeChainConfig, node: MappingNode) : NodeChainConfig(parent, node) {

    val serviceParent by lazy { ParentConfig(this, getMappingNodeOrCreate("parent")) }

    /**
     * 程序在主机上的工作目录。
     * 此目录指持有Docker的主机，若未配置，则为当前程序运行路径。
     */
    @ChainProp("host-work-dir", writeDefault = false)
    var workDir: String by notnull {
        Path("").absolutePathString().replace("\\", "/")
    }
}