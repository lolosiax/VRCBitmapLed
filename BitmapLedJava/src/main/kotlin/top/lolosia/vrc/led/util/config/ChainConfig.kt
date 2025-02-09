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

import top.lolosia.vrc.led.util.property.AbstractProperty
import top.lolosia.vrc.led.util.property.MutableProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.*

/**
 *  代理Config
 *
 * @author 一七年夏
 * @since 2022-06-23 20:00
 */
open class ChainConfig(val parent: ChainConfig?) {

    protected open fun <T : Any?> get(key: String, type: KType): T? {
        return parent!!.get("${parentProperty.name}.${key}", type)
    }

    @JvmName("getInline")
    protected inline fun <reified T : Any?> get(key: String): T? {
        return get(key, T::class.createType())
    }

    operator fun get(key: String): Any? {
        return get(key, Any::class.createType(nullable = true))
    }

    open operator fun <T : Any?> set(key: String, data: T?) {
        parent!!["${parentProperty.name}.${key}"] = data
    }

    protected operator fun <T : ChainConfig, R> setValue(pConfig: T, property: KProperty<*>, s: R?) {
        var name = property.findAnnotation<ChainProp>()?.name
        if (name.isNullOrEmpty()) name = property.name
        set(name, s)
    }

    @Suppress("UNCHECKED_CAST")
    protected operator fun <T : ChainConfig, R> getValue(pConfig: T, property: KProperty<*>): R? {
        val ann = property.findAnnotation<ChainProp>()
        var name = ann?.name
        if (name.isNullOrEmpty()) name = property.name
        return get(name, property.returnType) ?: run {
            val ann1 = property.annotations.find {
                it.annotationClass.annotations.find { def -> def is Default } != null
            } ?: return null
            val value = (ann1::class.memberProperties.first {
                it.name == "value"
            } as KProperty1<Annotation, Any?>).get(ann1)
            value?.let {
                val def = value::class.createType(nullable = true)
                val pro = property.returnType
                require(pro.isSubtypeOf(def)) { "默认数据类型不一致，$def 不是 $pro" }
                if (ann?.writeDefault != false) {
                    setValue(pConfig, property, value)
                }
                return value as R
            } ?: return null
        }
    }

    open val KProperty<*>.path: String
        get() = getPath(this.name)

    open fun getPath(bean: String): String {
        return parent!!.getPath("${parentProperty.name}.${bean}")
    }

    @Suppress("UNCHECKED_CAST")
    protected val parentProperty: KProperty<*> by lazy {
        require(parent != null) { "调用 parentProperty 时， 父配置文件不能为null！" }
        parent::class.memberProperties.first {
            it as? KProperty1<ChainConfig, *> ?: return@first false
            val kc = it.returnType.classifier as? KClass<*> ?: return@first false
            if (kc.isSubclassOf(this::class)) {
                it.get(parent) == this
            } else return@first false
        }
    }

    protected abstract class ConfigProperty<R> : AbstractProperty<R>(), MutableProperty<R> {
        protected var cache: R? = null
        protected var hasCache = false

        override var value: R
            get() = throw NotImplementedError("not yet implemented")
            set(_) = throw NotImplementedError("not yet implemented")
    }

    protected inner class NotNull : ConfigProperty<Any?>() {

        @Suppress("UNCHECKED_CAST")
        operator fun <T : ChainConfig, R> getValue(pConfig: T, property: KProperty<*>): R {
            if (hasCache) return cache as R
            synchronized(this) {
                val obj = this@ChainConfig.getValue<T, R>(pConfig, property) as R
                cache = obj
                hasCache = true
                return obj
            }
        }

        operator fun <T : ChainConfig, R> setValue(pConfig: T, property: KProperty<*>, value: R) {
            val old = this@ChainConfig.getValue<T, R>(pConfig, property)
            synchronized(this){
                this@ChainConfig.setValue(pConfig, property, value)
                cache = value
                hasCache = true
            }
            if (old != value) afterChange(value, old)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): Any? {
            return getValue(thisRef as ChainConfig, property)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Any?) {
            @Suppress("RemoveExplicitTypeArguments")
            setValue<ChainConfig, Any?>(thisRef as ChainConfig, property, value)
        }
    }

    protected inner class DefaultValue<R>(val callback: () -> R?) : ConfigProperty<R?>() {
        operator fun <T : ChainConfig> getValue(pConfig: T, property: KProperty<*>): R? {
            if (hasCache) return cache
            synchronized(this){
                val obj = this@ChainConfig.getValue(pConfig, property) ?: callback().apply {
                    this@ChainConfig.setValue<T, R>(pConfig, property, this)
                }
                cache = obj
                hasCache = true
                return obj
            }
        }

        operator fun <T : ChainConfig> setValue(pConfig: T, property: KProperty<*>, value: R?) {
            val old = this@ChainConfig.getValue<T, R>(pConfig, property)
            synchronized(this){
                this@ChainConfig.setValue<T, R>(pConfig, property, value)
                if (old != value) afterChange(value, old)
                cache = value
                hasCache = true
            }
            if (old != value) afterChange(value, old)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): R? {
            return getValue(thisRef as ChainConfig, property)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: R?) {
            setValue(thisRef as ChainConfig, property, value)
        }
    }

    protected inner class NotNullValue<R>(val callback: () -> R) : ConfigProperty<R>() {

        operator fun <T : ChainConfig> getValue(pConfig: T, property: KProperty<*>): R {
            if (hasCache) return cache!!
            synchronized(this){
                val obj = this@ChainConfig.getValue(pConfig, property) ?: callback().apply {
                    this@ChainConfig.setValue<T, R>(pConfig, property, this)
                }
                cache = obj
                hasCache = true
                return obj
            }
        }

        operator fun <T : ChainConfig> setValue(pConfig: T, property: KProperty<*>, value: R) {
            val old = this@ChainConfig.getValue<T, R>(pConfig, property)!!
            synchronized(this){
                this@ChainConfig.setValue<T, R>(pConfig, property, value)
                cache = value
                hasCache = true
            }
            if (old != value) afterChange(value, old)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): R {
            return getValue(thisRef as ChainConfig, property)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: R) {
            @Suppress("RemoveExplicitTypeArguments")
            setValue<ChainConfig>(thisRef as ChainConfig, property, value)
        }
    }

    protected operator fun invoke() = DefaultValue { null }
    protected val default get() = this
    protected fun <T> default(callback: () -> T?) = DefaultValue(callback)
    protected fun <T> default(value: T?) = DefaultValue { value }
    protected val notnull = NotNull()
    protected fun <T> notnull(callback: () -> T) = NotNullValue(callback)
    protected fun <T> notnull(value: T) = NotNullValue { value }
}