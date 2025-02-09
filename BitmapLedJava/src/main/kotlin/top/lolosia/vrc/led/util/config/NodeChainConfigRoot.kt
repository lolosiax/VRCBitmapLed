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

package top.lolosia.vrc.led.util.config

import top.lolosia.vrc.led.util.kotlin.pass
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.nodes.NodeId.*


abstract class NodeChainConfigRoot : NodeChainConfig(null, null) {
    override val mapper = ConfigYaml()

    abstract override val node: MappingNode
    abstract override fun save()

    protected fun beforeSaving(){
        beforeSavingNode(node)
    }

    private fun beforeSavingNode(node: Node){
        when(node.nodeId){
            mapping -> beforeSavingMap(node as MappingNode)
            sequence -> beforeSavingSeq(node as SequenceNode)
            scalar -> beforeSavingScalar(node as ScalarNode)
            else -> pass
        }
    }

    private fun beforeSavingMap(node: MappingNode){
        node.tag = Tag.MAP
        node.flowStyle = DumperOptions.FlowStyle.BLOCK
        node.value.forEach {
            beforeSavingNode(it.keyNode)
            beforeSavingNode(it.valueNode)
        }
    }
    private fun beforeSavingSeq(node: SequenceNode){
        node.tag = Tag.SEQ
        node.flowStyle = DumperOptions.FlowStyle.BLOCK
        node.value.forEach(::beforeSavingNode)
    }

    private fun beforeSavingScalar(node: ScalarNode){
        if (node.tag !in Tag.standardTags){
            node.tag = Tag.STR
        }
    }
}