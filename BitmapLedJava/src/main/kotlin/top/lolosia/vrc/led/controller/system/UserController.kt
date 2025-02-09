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

package top.lolosia.vrc.led.controller.system

import top.lolosia.vrc.led.annotation.HostForward
import top.lolosia.vrc.led.manager.SessionManager
import top.lolosia.vrc.led.model.system.SysUserEntity
import top.lolosia.vrc.led.service.system.UserService
import top.lolosia.vrc.led.util.ErrorResponseException
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.bundle.bundleScope
import top.lolosia.vrc.led.util.bundle.invoke
import top.lolosia.vrc.led.util.bundle.scope
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.session.SessionMap
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@HostForward
@RestController
@RequestMapping("/api/user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var sessionManager: SessionManager

    @PostMapping("/myInfo")
    fun myInfo(context: Context): Bundle {
        return userService.myUserInfo(context)
    }

    @PostMapping("/myRole")
    fun myRole(context: Context): Bundle {
        return userService.myUserRole(context)
    }

    @PostMapping("/mySession")
    fun mySession(context: Context): SessionMap? {
        return userService.mySession(context)
    }

    data class ListParam(
        var pageNo: Int,
        var pageSize: Int,
        var keys: String?,
    )

    @PostMapping("/list")
    fun list(context: Context, @RequestBody param: ListParam): Bundle {
        val (a, b, c) = param
        val (rows, count) = userService.list(context, a, b, c)
        return bundleScope {
            "data" set rows
            "total" set count
        }
    }

    data class UserSearchingParam(var keys: String?)

    @PostMapping("/searching")
    fun userSearching(context: Context, params: UserSearchingParam): List<SysUserEntity> {
        return userService.userSearching(context, params.keys)
    }

    data class GetParam(var idList: List<String>)

    @PostMapping("/get")
    fun get(context: Context, @RequestBody param: GetParam): List<Bundle> {
        return userService.get(context, param.idList)
    }

    data class CreateParam(
        var phone: String,
        var userName: String?,
        var realName: String?,
        var password: String?,
        var isUse: Boolean,
        var roleId: Int
    )

    @PostMapping("/create")
    fun create(context: Context, @RequestBody param: CreateParam): Any {
        return userService.create(context, bundleScope {
            use(param::phone)
            use(param::userName) { param.phone }
            use(param::realName)
            use(param::isUse)
            use(param::roleId)
            use(param::password)
            "createdBy" set context.userId
        })
    }

    @PostMapping("/userInfo")
    fun getUserInfo(context: Context): Bundle {
        return userService.getUserInfo(context, context.userId).scope {
            // 我也不知道这句话什么意思。
            "permissions" set listOf(current["userName"])
        }
    }

    @PostMapping("/edit")
    fun edit(context: Context, @RequestBody data: Bundle): Any {
        if ("id" !in data) throw IllegalArgumentException("必须提供UserID")
        data["updatedBy"] = context.userId
        return userService.update(context, data)
    }

    data class DeleteParam(var ids: List<String>)

    @PostMapping("/delete")
    fun delete(context: Context, @RequestBody data: DeleteParam): Any {
        return userService.delete(context, data.ids)
    }

    data class UpdatePasswordParam(
        var id: String?,
        var origin: String?,
        var target: String,
        var session: String?,
    )

    /** 修改密码 */
    @PostMapping("/updatePassword")
    fun updatePassword(context: Context, @RequestBody params: UpdatePasswordParam): Any {
        if (params.session != null) {
            val session = params.session!!
            if (!sessionManager.contains(session)) {
                throw ErrorResponseException(HttpStatus.UNAUTHORIZED, "身份认证失败，请重新登陆")
            }
            val session1 = sessionManager[session]
            params.id = session1.invoke("id")
        } else if (sessionManager.mySession(context) == null) {
            throw ErrorResponseException(HttpStatus.UNAUTHORIZED, "身份认证失败，请重新登陆")
        }

        return userService.updatePassword(context, params.id!!, params.origin ?: "", params.target)
    }

    @GetMapping("/avatar")
    fun getAvatar(context: Context, @RequestParam("id") id: String): ResponseEntity<Flux<DataBuffer>> {
        return userService.avatar(context, id)
    }

    @GetMapping("/avatar/get/{userId}")
    fun getAvatarStatic(
        context: Context,
        @PathVariable("userId") userId: String
    ): ResponseEntity<Flux<DataBuffer>> {
        return userService.avatar(context, userId)
    }

    @PutMapping("/avatar")
    suspend fun setAvatar(
        context: Context,
        @RequestPart("file") filePart: Flux<FilePart>,
        @RequestParam("id") id: String
    ): ResponseEntity<ByteArray> {
        val file = filePart.awaitSingle()
        return userService.avatar(context, id, file)
    }
}