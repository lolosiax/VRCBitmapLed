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
import top.lolosia.vrc.led.util.bundle.bundleOf
import top.lolosia.vrc.led.util.config.NodeChainConfigRoot
import java.io.StringReader
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writer

object SConfig : NodeChainConfigRoot() {

    private const val CONFIG_FILE = "application.yaml"
    private var initialized = false
    private var initializeNeedSave = false

    override val node: MappingNode by lazy { init() }
    val host by lazy { HostConfig(this, node) }
    val resources by lazy { ResourcesConfig(getBundleOrCreate("resources")) }
    val server by lazy { ServerConfig(this, getMappingNodeOrCreate("server")) }
    val osc by lazy { OscConfig(this, getMappingNodeOrCreate("osc")) }

    private fun init(): MappingNode {
        if (!Path(CONFIG_FILE).exists()) {
            return mapper.represent(bundleOf()) as MappingNode
        }
        val text = Path(CONFIG_FILE).readText()
        return mapper.compose(StringReader(text)) as MappingNode
    }

    override fun save() {
        if (!initialized) {
            initializeNeedSave = true
            return
        }
        beforeSaving()
        Path(CONFIG_FILE).writer(Charsets.UTF_8).use {
            mapper.serialize(node, it)
        }
    }

    init {
        initialized = true
        if (initializeNeedSave) {
            save()
        }
    }
}