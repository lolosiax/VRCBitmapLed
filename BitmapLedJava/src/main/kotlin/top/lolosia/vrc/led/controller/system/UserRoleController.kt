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
import top.lolosia.vrc.led.service.system.UserRoleService
import top.lolosia.vrc.led.util.bundle.Bundle
import top.lolosia.vrc.led.util.session.Context
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@HostForward
@RestController
@RequestMapping("/api/userRole")
class UserRoleController {

    @Autowired
    lateinit var service: UserRoleService

    data class GetByUserIdParam(val userId: String)

    @PostMapping("/getByUserId")
    fun getByUserId(context: Context, @RequestBody param: GetByUserIdParam): Bundle {
        return service.getByUserId(context, param.userId)
    }

    @PostMapping("/getUserRole")
    fun getUserRole(context: Context, @RequestBody param: GetByUserIdParam): Bundle {
        return service.getRoleByUserId(context, mutableMapOf("userId" to param.userId))
    }
}