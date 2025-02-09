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

import io.ebean.Database
import io.ebean.Model
import jakarta.persistence.MappedSuperclass
import java.util.function.Function

@MappedSuperclass
abstract class AbstractModel(dbName: String?) : Model(dbName) {
    companion object {
        /**
         * 给 TQRootBean 反射用的回调。
         * @see io.ebean.typequery.TQRootBean.database
         */
        @JvmStatic
        @Suppress("unused")
        private val databaseCallback = Function<Array<Any>, Unit> { t ->
            (t[0] as? AbstractModel)?.let {
                it.database = t[1] as Database
            }
        }
    }

    @JvmField
    @Transient
    val dbName: String = dbName ?: "db"

    @JvmField
    @Transient
    var database: Database? = null

    override fun db(): Database {
        return database ?: throw IllegalStateException("database is null, try to use Context.createModel<T>()")
    }
}