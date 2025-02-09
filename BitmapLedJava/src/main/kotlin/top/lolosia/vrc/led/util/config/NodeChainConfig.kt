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

import com.fasterxml.jackson.annotation.JsonValue
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundleOf
import org.apache.commons.collections4.BidiMap
import org.apache.commons.collections4.bidimap.DualHashBidiMap
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.composer.Composer
import org.yaml.snakeyaml.events.Event
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.nodes.NodeId.*
import org.yaml.snakeyaml.parser.Parser
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField


open class NodeChainConfig(parent: NodeChainConfig?, node: MappingNode?) : ChainConfig(parent) {

    open val mapper: ConfigYaml get() = (parent as NodeChainConfig).mapper
    private val node0: MappingNode? = node
    open val node get() = node0 ?: throw NoSuchElementException("Node not found")

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: String, type: KType): T? {
        val node = getNode(key) ?: return null
        val kClass = type.classifier as KClass<*>

        if (node.tag == Tag.NULL) return null

        val value = when (node.nodeId!!) {
            scalar -> convert<T>((node as ScalarNode).value, kClass)
            // 不稳定。
            sequence -> mapper.construct(node, kClass.java) as T?
            mapping -> mapper.construct(node, kClass.java) as T?
            anchor -> null
        }

        return value
    }

    override fun <T> set(key: String, data: T?) {
        var value: Any? = data

        if (data is Enum<*>) value = convertEnumToString(data)

        val tValue = mapper.represent(value)

        setNode(key, tValue)
    }

    open fun save() {
        (parent as? NodeChainConfig)?.save()
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun <T> convert(value: String, clazz: KClass<*>): T? {
        val rs: Any = when (clazz) {
            Double::class -> value.toDouble()
            Float::class -> value.toFloat()
            Byte::class -> value.toByte()
            Char::class -> value[0]
            Short::class -> value.toShort()
            Int::class -> value.toInt()
            Long::class -> value.toLong()
            String::class -> value
            Boolean::class -> value.toBoolean()
            else -> Unit
        }
        if (rs != Unit) return rs as T
        if (clazz.java.isEnum) {
            return convertEnum(value, clazz as KClass<Enum<*>>) as T?
        }

        return with(mapper) {
            construct(represent(value), clazz.java) as T?
        }
    }

    private val enumCaches = mutableMapOf<KClass<*>, BidiMap<String, Enum<*>>>()
    private fun enumCache(clazz: KClass<out Enum<*>>): BidiMap<String, Enum<*>> {
        return synchronized(enumCaches) {
            enumCaches.getOrPut(clazz) {
                val map = DualHashBidiMap<String, Enum<*>>()
                // 查询反射方法
                val method: ((Enum<*>) -> String)? = clazz.memberProperties.find {
                    it.hasAnnotation<JsonValue>() || it.javaField?.getAnnotation(JsonValue::class.java) != null
                }?.let { p ->
                    @Suppress("UNCHECKED_CAST")
                    { (p as KProperty1<Enum<*>, String>).get(it) }
                } ?: clazz.java.methods.find {
                    it.getAnnotation(JsonValue::class.java) != null && it.parameters.isEmpty()
                }?.let { m ->
                    { m.invoke(it).toString() }
                }

                clazz.java.enumConstants.forEach { e ->
                    val key = method?.invoke(e) ?: e.name
                    map[key] = e
                }
                map
            }
        }
    }

    protected fun convertEnum(value: String, clazz: KClass<out Enum<*>>): Enum<*>? {
        return enumCache(clazz)[value]
    }

    protected fun convertEnumToString(value: Enum<*>): String {
        val caches = enumCache(value::class)
        return caches.getKey(value)
    }

    fun getNode(key: String): Node? {
        val tuple = synchronized(node) {
            this.node.value.find { key == (it.keyNode as? ScalarNode)?.value } ?: return null
        }
        return tuple.valueNode
    }

    fun getMappingNodeOrCreate(key: String): MappingNode {
        var node = getNode(key)
        if (node != null) return node as MappingNode
        node = mapper.represent(bundleOf())
        setNode(key, node)
        return node as MappingNode
    }

    fun setNode(key: String, node: Node) {

        synchronized(this.node) {
            val values = this.node.value
            val index = values.indexOfFirst { key == (it.keyNode as? ScalarNode)?.value }
            if (index != -1) {
                val obj = values.removeAt(index)
                val old = obj.valueNode

                if (node.blockComments.isNullOrEmpty()) node.blockComments = old.blockComments
                if (node.inLineComments.isNullOrEmpty()) node.inLineComments = old.inLineComments
                if (node.endComments.isNullOrEmpty()) node.endComments = old.endComments

                val tuple = NodeTuple(obj.keyNode, node)
                values.add(index, tuple)
            } else {
                val tKey = mapper.represent(key)
                val tuple = NodeTuple(tKey, node)
                values.add(tuple)
            }
        }

        save()
    }

    fun getBundle(key: String): Bundle? {
        val node = getNode(key) ?: return null
        @Suppress("UNCHECKED_CAST")
        return mapper.construct(node, LinkedHashMap::class.java) as Bundle
    }

    fun getBundleOrCreate(key: String): Bundle {
        var bundle = getBundle(key)
        if (bundle != null) return bundle
        bundle = bundleOf()
        set(key, bundle)
        return bundle
    }

    class ConfigYaml : Yaml(
        // Constructor(LoaderOptions().apply { isProcessComments = true }),
        // Representer(DumperOptions().apply { isProcessComments = true }),
        // DumperOptions().apply { isProcessComments = true },
        // LoaderOptions().apply { isProcessComments = true },
    ) {
        init {
            loadingConfig.isProcessComments = true
            dumperOptions.isProcessComments = true
        }

        fun <T> construct(node: Node, clazz: Class<T>): T {
            val parser = object : Parser {
                override fun checkEvent(choice: Event.ID?) = throw NotImplementedError("Not yet implemented")
                override fun peekEvent() = throw NotImplementedError("Not yet implemented")
                override fun getEvent() = throw NotImplementedError("Not yet implemented")
            }
            val composer = object : Composer(parser, resolver, loadingConfig) {
                override fun getSingleNode() = node
            }
            constructor.setComposer(composer)
            @Suppress("UNCHECKED_CAST")
            return constructor.getSingleData(clazz) as T
        }
    }
}