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

import com.fasterxml.jackson.databind.json.JsonMapper
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.spring.ApplicationContextProvider
import io.ebean.Database
import io.ebean.Transaction
import io.ebean.typequery.IQueryBean
import io.ebean.typequery.TQRootBean
import org.springframework.beans.factory.getBean
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType

private typealias ACP = ApplicationContextProvider

private val databaseMap = Collections.synchronizedMap(WeakHashMap<String, Database>())

private var mJsonMapper: JsonMapper? = null

private inline val ACP.jsonMapper: JsonMapper
    get() = mJsonMapper ?: run {
        mJsonMapper = applicationContext.getBean<JsonMapper>()
        mJsonMapper!!
    }

val ACP.database get() = database("db")

fun ACP.database(name: String): Database {
    return databaseMap.getOrPut(name) {
        applicationContext.getBean(name, Database::class.java)
    }
}

/** 开启一个数据库事务 */
inline fun <R> ACP.transaction(dbName: String = "db", block: (Transaction) -> R) = database(dbName).transaction(block)

fun AbstractModel.applyDatabase(ctx: ACP) {
    database = ctx.database(dbName)
}

/** 创建一个数据库模型 */
fun <T : AbstractModel> ACP.createModel(clazz: Class<T>, bundle: Bundle? = null, markProperty: Boolean = false): T {
    val model = bundle?.let { jsonMapper.toModel(bundle, clazz, markProperty) } ?: clazz.getConstructor().newInstance()
    model.database = database(model.dbName)
    return model
}

/** 创建一个数据库模型 */
inline fun <reified T : AbstractModel> ACP.createModel(bundle: Bundle? = null, markProperty: Boolean = false): T {
    return createModel(T::class.java, bundle, markProperty)
}

/** 创建一个数据库模型 */
inline fun <reified T : AbstractModel> ACP.createModel(
    bundle: Bundle? = null,
    markProperty: Boolean = false,
    block: T.() -> Unit
): T {
    return createModel<T>(bundle, markProperty).apply(block)
}

private val queryDatabaseMap = mutableMapOf<KClass<*>, String>()

/** 创建一个数据库查询 */
inline fun <reified T : IQueryBean<*, *>> ACP.query(): T = query(T::class)

/** 创建一个数据库查询 */
inline fun <reified T : IQueryBean<*, *>> ACP.query(block: T.() -> Unit): T = query(T::class).apply(block)

fun <T : IQueryBean<*, *>> ACP.query(kClass: KClass<T>): T {
    val dbName = queryDatabaseMap.getOrPut(kClass) {
        // 将KClass转为KType，用于搜索父类的模板类型
        var parentType = kClass.createType()
        // 遍历父级Class，直到找到TQRootBean，且为了避免异常，当判断到Any时，跳出循环
        while (parentType.classifier != TQRootBean::class && parentType.classifier != Any::class) {
            // 获取这个Type的父级Type，然后稍后再次开始循环
            parentType = (parentType.classifier as KClass<*>).supertypes.first()
        }
        // 如果没有找到TQRootBean，则抛出异常
        if (parentType.classifier != TQRootBean::class) throw NoSuchElementException("找不到TQRootBean")

        // 获取父级Class的模板类型，即数据库模型的类型，然后创建实例
        val modelClass = parentType.arguments.first().type!!.classifier as KClass<*>
        val obj = modelClass.createInstance() as AbstractModel
        // 返回数据库名称
        obj.dbName
    }
    val db = database(dbName)
    val constructor = kClass.java.getConstructor(Database::class.java)
    val query = constructor.newInstance(db)
    query.usingDatabase(db)
    return query
}