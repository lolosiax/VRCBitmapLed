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
import top.lolosia.vrc.led.model.system.SysRoleEntity
import top.lolosia.vrc.led.model.system.query.QSysRoleEntity
import top.lolosia.vrc.led.model.system.query.QSysUserRolesEntity
import top.lolosia.vrc.led.util.bundle.*
import top.lolosia.vrc.led.util.ebean.*
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.success
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleService {

    @Autowired
    lateinit var mapper: JsonMapper

    fun list(ctx: Context): MutableList<SysRoleEntity> {
        return ctx.query<QSysRoleEntity>().findList()
    }

    fun queryRoleByPage(ctx: Context, pageNo: Int, pageSize: Int, roleName: String?): Bundle {
        val roles = ctx.query<QSysRoleEntity> {
            if (!roleName.isNullOrEmpty()) {
                or {
                    this.roleName.contains(roleName)
                    this.type.contains(roleName)
                }
            }
            setMaxRows(pageSize)
            setFirstRow((pageNo - 1) * pageSize)
        }.findPagedList()

        // 查询菜单的相关代码已被弃用，此处仅保留用于兼容的代码。
        val data = roles.list.map {
            mapper.toBundle(it).scope {
                "menuId" set emptyList<Int>()
                "menuTitle" set emptyList<String>()
            }
        }

        return bundleScope {
            "total" set roles.totalCount
            "rows" set data
        }
    }

    fun create(ctx: Context, bundle: Bundle): SysRoleEntity {
        return ctx.createModel<SysRoleEntity> {
            roleName = bundle("roleName") ?: throw IllegalArgumentException("必须填写roleName")
            type = bundle("type") ?: throw IllegalArgumentException("必须填写type")
            createdBy = ctx.user.id
            insert()
            refresh()
        }
    }

    fun update(ctx: Context, bundle: Bundle): Any {
        mapper.toModel<SysRoleEntity>(bundle).apply {
            applyDatabase(ctx)
            updatedBy = ctx.user.id
            update()
        }
        return success()
    }

    fun destroy(ctx: Context, id: Int): Any {
        val role = ctx.query<QSysRoleEntity>{
            this.id.eq(id)
        }.findOne() ?: return success()
        if (role.type == "super_admin") throw IllegalStateException("不能删除超级管理员角色")
        ctx.query<QSysUserRolesEntity> {
            this.roleId.eq(id)
            if (exists()) throw IllegalStateException("该角色正在使用")
        }
        role.delete()
        return success()
    }
}