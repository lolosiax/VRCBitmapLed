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

package top.lolosia.vrc.led.util.ebean

import com.fasterxml.jackson.databind.ObjectMapper
import io.ebean.Database
import io.ebean.Model
import io.ebean.Transaction
import io.ebean.bean.EntityBean
import io.ebean.typequery.TQRootBean
import jakarta.persistence.Table
import top.lolosia.vrc.led.model.ModelCompanion
import top.lolosia.vrc.led.util.bundle.Bundle
import java.util.*
import kotlin.reflect.KProperty

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Model> Database.delete(id: Any): Int {
    return this.delete(T::class.java, id)
}

fun String.toUuid(): UUID {
    return UUID.fromString(this)
}

fun String?.toUuidOrNull(): UUID? {
    if (this.isNullOrBlank()) return null
    return UUID.fromString(this)
}


inline fun <T, R> TQRootBean<T, R>.or(block: () -> Unit) {
    or()
    block()
    endJunction()
}

inline fun <T, R> TQRootBean<T, R>.and(block: () -> Unit) {
    and()
    block()
    endJunction()
}

inline fun <T, R> TQRootBean<T, R>.not(block: () -> Unit) {
    not()
    block()
    endJunction()
}

//inline fun <T, R> TQRootBean<T, R>.mustNot(block: () -> Unit) {
//    mustNot()
//    block()
//    endJunction()
//}
//
//inline fun <T, R> TQRootBean<T, R>.should(block: () -> Unit) {
//    should()
//    block()
//    endJunction()
//}

inline fun <T, R> TQRootBean<T, R>.orderBy(block: () -> Unit) {
    orderBy()
    block()
}

/**
 * Execute as a delete query returning the number of rows deleted using the given transaction.
 * <p>
 * Note that if the query includes joins then the generated delete statement may not be
 * optimal depending on the database platform.
 * </p>
 *
 * @return the number of beans/rows that were deleted.
 */
fun <T, R> TQRootBean<T, R>.delete(tran: Transaction): Int {
    usingTransaction(tran)
    return delete()
    // return query().delete(tran)
}

@Suppress("DEPRECATION")
@Deprecated(message = "database must use spring bean instead.",
    replaceWith = ReplaceWith("ctx.database.transaction(block)", "ctx")
)
inline fun <R> ModelCompanion.transaction(block: (tran: Transaction) -> R): R = database.transaction(block)

inline fun <R> Database.transaction(block: (tran: Transaction) -> R): R {
    val transaction = this.createTransaction()
    var err: Throwable? = null
    try {
        return block(transaction)
    } catch (e: Throwable) {
        err = e
        throw e
    } finally {
        if (err == null) transaction.commit()
        else transaction.rollback()
    }
}

fun <T : Model> ObjectMapper.toModel(obj: Bundle, clazz: Class<T>, markProperty: Boolean = false): T {
    val model = readValue(writeValueAsString(obj), clazz)
    if (markProperty) model.mark(obj)
    return model
}

/**
 * 将一个Bundle对象转为Model
 */
inline fun <reified T : Model> ObjectMapper.toModel(obj: Bundle, markProperty: Boolean = false): T {
    return toModel(obj, T::class.java, markProperty)
}

private val modelPropCache = mutableMapOf<Class<out Model>, Set<String>>()
val Model.props: Set<String>
    get() {
        val clazz = this::class.java
        if (clazz in modelPropCache) return modelPropCache[clazz]!!
        val list = (this as EntityBean)._ebean_getPropertyNames()
        val set = Collections.unmodifiableSet(LinkedHashSet(list.toList()))
        modelPropCache[clazz] = set

        return set
    }

/**
 * Mark the property as set or 'is loaded'.
 * <p>
 * This would be used to specify a property that we did wish to include in a stateless update.
 * </p>
 *
 * ```kotlin
 *
 *   // populate an entity bean from JSON or whatever
 *   val user: User = ...;
 *
 *   // mark the email property as 'set' so that it is
 *   // included in a 'state update'
 *   user.markPropertySet("email");
 *
 *   user.update();
 * ```
 * @param propertyName the name of the property on the bean to be marked as 'set'
 */
fun Model.markPropertySet(propertyName: String) {
    (this as EntityBean)._ebean_getIntercept().setPropertyLoaded(propertyName, true)
}

/**
 * 更新时排除一些属性
 */
fun Model.unmark(vararg prop: String) {
    val prop0 = listOf(*prop, "createdAt", "updatedAt") intersect props
    prop0.forEach(this::markPropertyUnset)
}

/**
 * 更新时排除一些属性
 */
fun Model.unmark(vararg prop: KProperty<*>) {
    val prop1 = prop.map { it.name }
    val prop2 = (prop1 + listOf("createdAt", "updatedAt")) intersect props

    prop2.forEach(this::markPropertyUnset)
}

/**
 * 仅标记Bundle包含的属性，将不包含的属性排除
 */
fun Model.mark(bundle: Bundle) {
    val props = props - ((bundle.keys - listOf("createdAt", "updatedAt")) intersect props)
    unmark(*props.toTypedArray())
}

inline fun <reified T : Model> tableName(): String {
    return T::class.java.getAnnotation(Table::class.java).name
}