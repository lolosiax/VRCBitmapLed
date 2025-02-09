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

package top.lolosia.vrc.led.service.system

import com.fasterxml.jackson.databind.json.JsonMapper
import top.lolosia.vrc.led.model.system.SysUserRolesEntity
import top.lolosia.vrc.led.model.system.query.QSysRoleEntity
import top.lolosia.vrc.led.model.system.query.QSysUserRolesEntity
import top.lolosia.vrc.led.model.system.query.QViewUserRoleEntity
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundleScope
import top.lolosia.vrc.led.util.bundle.toBundle
import top.lolosia.vrc.led.util.ebean.createModel
import top.lolosia.vrc.led.util.ebean.query
import top.lolosia.vrc.led.util.ebean.toModel
import top.lolosia.vrc.led.util.ebean.toUuid
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.success
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserRoleService {

    @Autowired
    lateinit var mapper: JsonMapper

    fun create(context: Context, userRole: Bundle): Any {
        mapper.toModel<SysUserRolesEntity>(userRole).insert()
        return success()
    }

    fun getByUserId(ctx: Context, userId: String): Bundle {
        val rs = ctx.query<QViewUserRoleEntity> {
            this.userId.eq(ctx.user.id)
        }.findOne()
        return rs?.let { mapper.toBundle(it) } ?: bundleScope {
            "roleId" set 0
            "userId" set 0
        }
    }

    /**
     * 根据用户ID获取关联的角色信息
     */
    fun getRoleByUserId(ctx: Context, user: Bundle): Bundle = ctx {
        val userId = user["userId"]?.toString()!!.toUuid()
        val has = query<QSysUserRolesEntity>().userId.eq(userId).exists()
        // 找不到角色时自动创建学生角色
        if (!has) {
            val roleUser = query<QSysRoleEntity> {
                type.eq("student")
            }.findOne() ?: throw NoSuchElementException("找不到用户角色")
            createModel<SysUserRolesEntity> {
                this.userId = userId
                this.roleId = roleUser.id
            }.insert()
        }
        val rs = query<QViewUserRoleEntity> {
            this.userId.eq(userId)
        }.findOne()
        val out = rs?.let { mapper.toBundle(it) } ?: bundleScope {
            "roleId" set 0
            "userId" set 0
        }
        out["roleType"] = out["type"]
        return out
    }
}