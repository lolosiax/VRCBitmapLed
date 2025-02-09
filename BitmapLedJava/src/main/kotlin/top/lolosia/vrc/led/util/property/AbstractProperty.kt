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

package top.lolosia.vrc.led.util.property

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * 抽象可观察属性
 *
 * @author 洛洛希雅Lolosia
 * @since 2024-08-28 16:25
 */
abstract class AbstractProperty<T> : Property<T> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AbstractProperty::class.java)
    }

    protected open val handler = WeakHashMap<Function<*>, Unit>()

    @Suppress("UNCHECKED_CAST")
    protected open fun afterChange(newValue: T, oldValue: T) {
        synchronized(handler) {
            handler.keys.forEach {
                try {
                    if (it is Function2<*, *, *>) {
                        (it as PropertyChangeCallback<T>).invoke(oldValue, newValue)
                    } else if (it is Function1<*, *>) {
                        (it as PropertyCallback<T>).invoke(newValue)
                    }
                } catch (e: Exception) {
                    logger.error(e.message, e)
                }
            }
        }
    }

    override operator fun plusAssign(callback: PropertyCallback<T>) {
        addListener(false, callback)
    }

    override operator fun minusAssign(callback: Function<T>) {
        removeListener(callback)
    }

    override fun <F : PropertyCallback<T>> addListener(immediate: Boolean, callback: F): F {
        synchronized(handler) {
            handler.put(callback, Unit)
        }
        if (immediate) callback(value)
        return callback
    }

    override fun <F : PropertyChangeCallback<T>> addChangeListener(immediate: Boolean, callback: F): F {
        synchronized(handler) {
            handler.put(callback, Unit)
        }
        if (immediate) callback(value, value)
        return callback
    }

    override fun removeListener(callback: Function<*>) {
        synchronized(handler) {
            handler.remove(callback)
        }
    }
}