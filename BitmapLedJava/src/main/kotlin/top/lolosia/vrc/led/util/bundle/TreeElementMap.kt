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

package top.lolosia.vrc.led.util.bundle

/**
 * 树状表
 */
open class TreeElementMap<K, V> private constructor(
    private val map: MutableMap<K, TreeElementMap<K, V>>
) : Map<K, TreeElementMap<K, V>> by map {

    companion object {
        fun <T : TreeElementMap<K, V>, K, V> T?.getOrNull(key: K): T? {
            if (this == null) return null
            @Suppress("UNCHECKED_CAST")
            return if (this.containsKey(key)) this[key] as T else null
        }
    }

    constructor() : this(mutableMapOf())

    private var hasRoot: Boolean = false
    private var root: V? = null

    open fun get(): V? {
        return root
    }

    override fun get(key: K): TreeElementMap<K, V> {
        return map.getOrPut(key) { TreeElementMap() }
    }

    open fun getOrPut(block: () -> V?): V? {
        if (!hasRoot) {
            root = block()
            hasRoot = true
        }
        return root
    }

    open fun set(value: V?) {
        hasRoot = true
        root = value
    }

    operator fun set(key: K, value: TreeElementMap<K, V>) {
        map[key] = value
    }

    open fun remove(): V? {
        val obj = root
        root = null
        hasRoot = false
        return obj
    }

    open fun remove(key: K): TreeElementMap<K, V>? {
        return map.remove(key)
    }

    /**
     * 遍历并处理树状表中的所有元素，包括所有子级。
     * @param function 遍历方法
     * @return 经扁平化处理的元素列表。
     */
    open fun <R> flatMap(function: (k: List<K>, it: V?) -> R?): List<R> {
        return flatMap0(emptyList(), function)
    }

    private fun <R> flatMap0(keys: List<K>, function: (k: List<K>, it: V?) -> R?): List<R> {
        val r = mutableListOf<R>()
        function(keys, get())?.let {
            r += it
        }
        forEach { (k, c) ->
            r += c.flatMap0(keys + k, function)
        }
        return r.toList()
    }

    open fun forEach(function: (k: List<K>, it: V?) -> Unit) {
        flatMap0(emptyList(), function)
    }

    /**
     * 将树中的所有元素由A转换为B
     * @param function 转换方法
     * @return 经转换的新的树
     */
    open fun <R> map(function: (k: List<K>, it: V?) -> R?): TreeElementMap<K, R> {
        return map0(emptyList(), function)
    }

    private fun <R> map0(keys: List<K>, function: (k: List<K>, it: V?) -> R?): TreeElementMap<K, R> {
        val r = TreeElementMap<K, R>()
        function(keys, get())?.let {
            r.set(it)
        }
        forEach { (k, c) ->
            r[k] = c.map0(keys + k, function)
        }
        return r
    }

    /**
     * 将当前的树与另一个树按照键键一致方式进行合并
     */
    fun <R> zip(other: TreeElementMap<K, R>) : TreeElementMap<K, Pair<V?, R?>>{
        val root = TreeElementMap<K, Pair<V?, R?>>()
        root.set(get() to other.get())

        val keys = this.keys + other.keys

        keys.forEach { key ->
            root[key] = this[key].zip(other[key])
        }

        return root
    }
}