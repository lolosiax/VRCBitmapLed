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
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "view_user_role")
class ViewUserRoleEntity : SystemModel() {
    @Id
    @DbComment("用户和角色关系id")
    var id = 0

    @Column(nullable = false)
    @DbComment("用户关联ID")
    lateinit var userId: UUID

    @Column(nullable = false)
    @DbComment("角色ID")
    var roleId = 0

    @DbComment("角色名称")
    @Column(nullable = false)
    lateinit var roleName: String

    @DbComment("用户名")
    @Column(nullable = false)
    var userName: String = ""

    @DbComment("用户昵称")
    var realName: String? = ""

    @DbComment("手机号")
    var phone: String? = null

    @DbComment("邮箱")
    var email: String? = null

    @DbComment("用户头像")
    var avatar: String? = null

    @DbComment("类型")
    @Column(nullable = false)
    lateinit var type: String
}