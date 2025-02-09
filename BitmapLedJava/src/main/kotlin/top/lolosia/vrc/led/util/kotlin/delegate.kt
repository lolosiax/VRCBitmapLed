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

package top.lolosia.vrc.led.util.kotlin

import kotlin.reflect.KProperty

open class Delegate<T, R>(
    protected var defaultValue: R? = null
) {
    companion object {
        fun <T, R> setter(
            block: (target: T, property: KProperty<*>, value: R, setter: (R) -> Unit) -> Unit
        ): IDelegate<T, R> = LambdaSetterDelegate(block)


        fun <T, R> getter(
            block: (target: T, property: KProperty<*>, getter: () -> R) -> R
        ): IDelegate<T, R> = LambdaGetterDelegate(block)
    }

    protected val chain = mutableListOf<IDelegate<in T, R>>()
    protected val default = DefaultDelegate()

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(target: T, property: KProperty<*>): R {
        fun next(i: Int): R {
            val delegate = chain.getOrNull(i) ?: default
            delegate as IDelegate<T, R>
            return delegate.getValue(target, property) {
                next(i + 1)
            }
        }
        return next(0)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun setValue(target: T, property: KProperty<*>, value: R) {
        fun next(i: Int, arg: R) {
            val delegate = chain.getOrNull(i) ?: default
            delegate as IDelegate<T, R>
            return delegate.setValue(target, property, arg) {
                next(i + 1, it)
            }
        }
        next(0, value)
    }

    /**
     * 创建一个新的代理对象，并将两者拼合。
     * @return 新的代理对象
     */
    operator fun <O : T> plus(other: Delegate<O, R>): Delegate<O, R> {
        val out = Delegate<O, R>(other.defaultValue)
        out.chain += chain
        out.chain += other.chain
        return out
    }

    /**
     * 将另一个的所有代理元素添加到当前代理中。
     */
    operator fun plusAssign(other: Delegate<in T, R>) {
        chain += other.chain
        defaultValue = other.defaultValue
    }

    /**
     * 将一个代理元素添加到当前代理中。
     * @return 当前代理对象
     */
    operator fun plus(other: IDelegate<in T, R>): Delegate<T, R> {
        chain += other
        return this
    }

    /**
     * 将一个代理元素添加到当前代理中。
     */
    operator fun plusAssign(other: IDelegate<in T, R>) {
        plus(other)
    }

    /**
     * 设定委托默认值
     */
    infix fun default(value: R): Delegate<T, R> {
        defaultValue = value
        return this
    }

    @Suppress("UNCHECKED_CAST")
    protected inner class DefaultDelegate : IDelegate<T, R> {
        override fun getValue(target: T, property: KProperty<*>, getter: () -> R): R {
            return defaultValue as R
        }

        override fun setValue(target: T, property: KProperty<*>, value: R, setter: (R) -> Unit) {
            defaultValue = value
        }
    }
}

interface IDelegate<T, R> {

    operator fun getValue(target: T, property: KProperty<*>): R {
        return getValue(target, property) {
            throw IllegalArgumentException("未设置委托默认值")
        }
    }

    fun getValue(target: T, property: KProperty<*>, getter: () -> R): R

    operator fun setValue(target: T, property: KProperty<*>, value: R) {
        setValue(target, property, value) {
            throw IllegalArgumentException("未设置委托默认值")
        }
    }

    fun setValue(target: T, property: KProperty<*>, value: R, setter: (R) -> Unit)

    operator fun plus(other: IDelegate<in T, R>): Delegate<T, R> {
        return Delegate<T, R>() + this + other
    }

    infix fun default(value: R): Delegate<T, R> {
        return Delegate<T, R>(value) + this
    }
}

private class LambdaSetterDelegate<T, R>(
    val block: (target: T, property: KProperty<*>, value: R, setter: (R) -> Unit) -> Unit
) : IDelegate<T, R> {

    override fun getValue(target: T, property: KProperty<*>, getter: () -> R): R {
        return getter()
    }

    override fun setValue(target: T, property: KProperty<*>, value: R, setter: (R) -> Unit) {
        block(target, property, value, setter)
    }
}

private class LambdaGetterDelegate<T, R>(
    val block: (target: T, property: KProperty<*>, getter: () -> R) -> R
) : IDelegate<T, R> {

    override fun getValue(target: T, property: KProperty<*>, getter: () -> R): R {
        return block(target, property, getter)
    }

    override fun setValue(target: T, property: KProperty<*>, value: R, setter: (R) -> Unit) {
        setter(value)
    }
}