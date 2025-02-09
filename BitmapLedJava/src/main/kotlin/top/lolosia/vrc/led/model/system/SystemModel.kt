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

package top.lolosia.vrc.led.model.system

import com.fasterxml.jackson.annotation.JsonFormat
import top.lolosia.vrc.led.util.ebean.AbstractModel
import top.lolosia.vrc.led.model.ModelCompanion
import io.ebean.DB
import io.ebean.annotation.DbName
import io.ebean.annotation.SoftDelete
import io.ebean.annotation.WhenCreated
import io.ebean.annotation.WhenModified
import jakarta.persistence.MappedSuperclass
import java.sql.Timestamp
import java.util.*

@DbName("db")
@MappedSuperclass
abstract class SystemModel : AbstractModel("db") {
    companion object : ModelCompanion {
        @JvmStatic
        @Deprecated("use spring bean instead.", replaceWith = ReplaceWith("ctx.database", "ctx"))
        override val database get() = DB.byName("db")
    }

    var createdBy: UUID? = null

    var updatedBy: UUID? = null

    @WhenCreated
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createdAt: Timestamp? = null

    @WhenModified
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var updatedAt: Timestamp? = null

    @SoftDelete
    var deleted = false
}