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

import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.jvmName


typealias Bundle = MutableMap<String, Any?>

fun bundleOf() = mutableMapOf<String, Any?>()
fun bundleOf(vararg pairs: Pair<String, Any?>) = mutableMapOf(*pairs)
fun bundleList() = mutableListOf<Bundle>()
fun bundleList(bundles: Bundle) = mutableListOf(bundles)

@Suppress("UNCHECKED_CAST")
fun Bundle.bundle(key: String): Bundle? = this[key] as? Bundle

@Suppress("UNCHECKED_CAST")
fun Bundle.bundleList(key: String): MutableList<Bundle>? = this[key] as? MutableList<Bundle>

@Suppress("UNCHECKED_CAST")
fun <T> Bundle.getList(key: String): MutableList<T>? = this[key] as? MutableList<T>

inline fun <reified T : Any> Bundle.getAs(key: String): T? =
    getAs0(this, key, T::class)

inline fun <reified T : Any> Bundle.getAs(key: String, default: () -> T): T {
    return getAs0(this, key, T::class) ?: default()
}

inline operator fun <reified T : Any> Bundle.invoke(key: String): T? =
    getAs0(this, key, T::class)

inline operator fun <reified T : Any> Bundle.invoke(key: String, default: () -> T): T {
    return getAs0(this, key, T::class) ?: default()
}

inline operator fun <reified T : Any> Bundle.invoke(key: KMutableProperty0<T?>) {
    key.set(getAs0(this, key.name, T::class))
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> getAs0(bundle: Bundle, key: String, clazz: KClass<T>): T? {
    if (!bundle.containsKey(key)) return null
    val value = bundle[key] ?: return null

    val number: () -> Number = number@{
        when (value) {
            is String -> return@number value.toDouble()
            is Number -> return@number value
            else -> throw ClassCastException("class ${value::class.jvmName} cannot be cast to class java.lang.Number")
        }
    }

    return when (clazz) {
        Double::class -> number().toDouble() as T
        Float::class -> number().toFloat() as T
        Byte::class -> number().toByte() as T
        Char::class -> number().toInt().toChar() as T
        Short::class -> number().toShort() as T
        Int::class -> number().toInt() as T
        Long::class -> number().toLong() as T
        String::class -> value.toString() as T
        else -> value as T
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> Bundle.getNotNull(key: String): T = this[key] as T

/**
 * 打开一个BundleDSL
 */
inline fun Bundle.scope(block: BundleScope.() -> Unit): Bundle {
    block(BundleScope(this))
    return this
}

inline fun bundleScope(block: BundleScope.() -> Unit): Bundle {
    val bundle = bundleOf()
    block(BundleScope(bundle))
    return bundle
}

class BundleScope(val current: Bundle) {

    /**
     * 向Bundle中设置一个属性
     * @param key 属性
     * @param default 若属性为null时的默认值回调
     */
    fun use(key: KProperty0<*>, default: (() -> Any)? = null) {
        current[key.name] = key.get() ?: default?.invoke()
    }

    /**
     * 从Bundle中读取一个属性，并写入到属性值
     * @param key 属性
     */
    inline fun <reified T> read(key: KMutableProperty0<T?>) {
        key.set(current(key.name))
    }

    /**
     * 从Bundle中读取一个属性，并写入到属性值。若ignoreNull为true，则跳过这次设置
     * @param key 属性
     * @param ignoreNull 忽略空值
     */
    inline fun <reified T> read(key: KMutableProperty0<T>, ignoreNull: Boolean) {
        key.set(current(key.name) ?: run {
            if (ignoreNull) return
            else throw NullPointerException("无法于Bundle中向非空属性${key.name}中写入空值")
        })
    }

    /**
     * 从Bundle中读取一个属性，并写入到属性值
     * @param key 属性
     * @param default 默认值
     */
    inline fun <reified T> read(key: KMutableProperty0<T>, default: () -> T) {
        key.set(current(key.name) ?: default())
    }

    /**
     * 向Bundle中设置一个值
     */
    fun bundle(key: String, value: Any?) {
        current[key] = value
    }

    /**
     * 向Bundle中设置一个新的Bundle
     */
    @Suppress("UNCHECKED_CAST")
    fun bundle(key: String, ignoreNull: Boolean = false, block: BundleScope.() -> Unit) {
        if (ignoreNull && current[key] == null) return
        val bundle = current.getOrPut(key) { bundleOf() }
        block(BundleScope(bundle as Bundle))
    }

    /**
     * 向Bundle中设置一个新的Bundle列表
     */
    @Suppress("UNCHECKED_CAST")
    fun list(key: String, block: BundleListScope.() -> Unit) {
        val list = current.getOrPut(key) { bundleList() }
        current[key] = list
        block(BundleListScope(list as MutableList<Bundle>))
    }

    infix fun String.set(value: Any?) {
        bundle(this, value)
    }
}


class BundleListScope(val current: MutableList<Bundle>) {
    fun bundle(block: BundleScope.() -> Unit) {
        val bundle = bundleOf()
        current.add(bundle)
        block(BundleScope(bundle))
    }
}

@Suppress("UNCHECKED_CAST")
fun ObjectMapper.readBundle(string: String): Bundle {
    return this.readValue(string, LinkedHashMap::class.java) as Bundle
}

fun ObjectMapper.toBundle(obj: Iterable<Any>): List<Bundle> {
    return obj.map { toBundle(it) }
}

fun ObjectMapper.toBundle(obj: Any): Bundle {
    return readBundle(writeValueAsString(obj))
}
