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

package top.lolosia.vrc.led.util.session

import top.lolosia.vrc.led.api.SystemApi
import top.lolosia.vrc.led.model.system.SysRoleEntity
import top.lolosia.vrc.led.model.system.query.QSysRoleEntity
import top.lolosia.vrc.led.model.system.query.QSysUserEntity
import top.lolosia.vrc.led.model.system.query.QSysUserRolesEntity
import top.lolosia.vrc.led.util.ebean.query
import top.lolosia.vrc.led.util.ebean.toUuid
import top.lolosia.vrc.led.util.isClient
import kotlinx.coroutines.runBlocking
import java.util.*

class UserInfo(val context: Context) {
    val id: UUID by lazy { context.userId.toUuid() }
    val userEntity by lazy {
        if (isClient) {
            return@lazy runBlocking {
                SystemApi.myUserInfo(context)
            }
        }
        context.query<QSysUserEntity>().id.eq(id).findOne() ?: throw NoSuchElementException("未找到用户 $id")
    }

    private val roleEntity: SysRoleEntity by lazy {
        if (isClient) {
            return@lazy runBlocking {
                val role = SystemApi.myRole(context)
                SysRoleEntity().apply {
                    id = role["roleId"] as Int
                    roleName = role["roleName"] as String
                    type = role["roleType"] as String
                }
            }
        }
        val ae = context.query<QSysUserRolesEntity>().userId.eq(id).findOne()
        ae ?: throw NoSuchElementException("未找到用户角色 $id")
        val role = context.query<QSysRoleEntity>().id.eq(ae.roleId).findOne()
        role ?: throw NoSuchElementException("找不到角色 ${ae.id}")
        return@lazy role
    }

    val userName get() = userEntity.userName
    val realName get() = userEntity.realName
    val phone get() = userEntity.phone
    val isUse get() = userEntity.isUse
    val roleId get() = roleEntity.id
    val roleName get() = roleEntity.roleName
    val roleType get() = roleEntity.type
    val isAdmin get() = "admin" in roleType
    val isTeacher get() = roleType == "teacher"
    val isStudent get() = roleType == "student"
}