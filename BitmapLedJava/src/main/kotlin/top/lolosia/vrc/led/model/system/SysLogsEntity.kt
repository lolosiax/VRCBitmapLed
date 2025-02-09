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

import io.ebean.annotation.DbComment
import io.ebean.annotation.DbEnumValue
import java.util.UUID
import jakarta.persistence.*

@Entity
@Table(name = "sys_logs")
class SysLogsEntity : SystemModel() {
    @Id
    @Column(nullable = false)
    var id = 0

    @Enumerated(EnumType.STRING)
    @DbComment("日志级别，info、error")
    var level = Level.Info

    @DbComment("用户编号")
    var userId: UUID? = null

    @Enumerated(EnumType.STRING)
    @DbComment("请求方式，get、post...")
    var method: Method? = null

    @DbComment("请求的接口")
    var url = ""

    @DbComment("请求参数")
    var params: String? = ""

    @DbComment("响应的数据")
    var response: String? = ""

    enum class Level(private val value: String) {
        Info("info"),
        Error("error");

        @DbEnumValue
        override fun toString(): String {
            return value
        }
    }

    enum class Method(private val value: String) {
        GET("get"),
        POST("post"),
        HEAD("head"),
        OPTIONS("options"),
        PUT("put"),
        PATCH("patch"),
        DELETE("delete"),
        TRACE("trace"),
        CONNECT("connect");

        @DbEnumValue
        override fun toString(): String {
            return value
        }
    }
}