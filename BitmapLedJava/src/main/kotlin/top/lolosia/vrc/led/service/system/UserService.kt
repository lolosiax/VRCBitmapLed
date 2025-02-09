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
import top.lolosia.vrc.led.interceptor.ErrorWebFilter
import top.lolosia.vrc.led.manager.SessionManager
import top.lolosia.vrc.led.model.system.SysUserEntity
import top.lolosia.vrc.led.model.system.SysUserRolesEntity
import top.lolosia.vrc.led.model.system.ViewUserRoleEntity
import top.lolosia.vrc.led.model.system.query.QSysRoleEntity
import top.lolosia.vrc.led.model.system.query.QSysUserEntity
import top.lolosia.vrc.led.model.system.query.QSysUserRolesEntity
import top.lolosia.vrc.led.model.system.query.QViewUserRoleEntity
import top.lolosia.vrc.led.util.bundle.*
import top.lolosia.vrc.led.util.ebean.*
import top.lolosia.vrc.led.util.session.Context
import top.lolosia.vrc.led.util.session.IWebExchangeContext
import top.lolosia.vrc.led.util.session.SessionMap
import top.lolosia.vrc.led.util.success
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.*
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.IOException
import java.time.Duration
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

@Service
class UserService {
    init {
        Path("./work/image/avatar").createDirectories()
    }

    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var mapper: JsonMapper

    @Autowired
    lateinit var session: SessionManager

    @Autowired
    lateinit var userRoleService: UserRoleService

    @Suppress("UNCHECKED_CAST")
    fun list(ctx: Context, pageNo: Int = 1, pageSize: Int = 10, keys: String? = null): Pair<List<Bundle>, Int> {
        val arrList = bundleOf()
        if (!keys.isNullOrEmpty()) {
            // 查询User匹配信息
            ctx.query<QSysUserEntity> {
                or {
                    userName.contains(keys)
                    realName.contains(keys)
                    phone.contains(keys)
                    email.contains(keys)
                }
                select(id)
                val idList = findSingleAttributeSet<UUID>()
                if (idList.isNotEmpty()) arrList["userId"] = idList
            }
            // 查询角色匹配信息
            ctx.query<QSysRoleEntity> {
                roleName.contains(keys)
                select(id)
                val rs = findSingleAttributeSet<Int>()
                if (rs.isNotEmpty()) arrList["roleId"] = rs
            }
        }

        val offset = (pageNo - 1) * pageSize

        // 从视图中查询所有用户。
        val (userRoles, count) = ctx.query<QViewUserRoleEntity> {

            if (arrList.isNotEmpty()) {
                if (arrList.size > 1) or()
                arrList.forEach { (k, v) ->
                    when (k) {
                        "userId" -> userId.isIn(v as Set<UUID>)
                        "roleId" -> roleId.isIn(v as Set<Int>)
                    }
                }
                if (arrList.size > 1) endOr()
            }
            orderBy {
                roleId.asc()
                userName.asc()
            }
            setFirstRow(offset)
            setMaxRows(pageSize)

        }.findPagedList().run {
            list.map(mapper::toBundle) to totalCount
        }

        if (userRoles.isNotEmpty()) {
            val users = ctx.query<QSysUserEntity> {
                id.isIn(userRoles.map { it.getAs<String>("userId")!!.toUuid() })
                id.asMapKey()
            }.findMap<UUID>().mapValues { (_, v) -> mapper.toBundle(v) }
            userRoles.forEach {
                it.scope {
                    "realName" set ""
                    "roleType" set it("type")
                    users[it["userId"].toString().toUuid()]?.let { user ->
                        user.remove("password")
                        it.putAll(user)
                    } ?: let {
                        "realName" set "<账户不存在>"
                        "isUse" set false
                    }
                }
            }
        }

        return userRoles to count
    }

    /**
     * 按照文本搜索用户，默认只显示10行
     * @param keys 关键字
     */
    fun userSearching(ctx: Context, keys: String?): List<SysUserEntity> {
        return ctx.query<QSysUserEntity> {
            if (!keys.isNullOrEmpty()) or {
                userName.contains(keys)
                realName.contains(keys)
                phone.contains(keys)
            }
            setMaxRows(10)
        }.findList()
    }

    /**
     * 查找用户
     * @param idList ID列表。根据经验，元素可能为 null。
     */
    fun get(ctx: Context, idList: List<String?>): List<Bundle> {
        val list = ctx.query<QSysUserEntity> {
            id.isIn(idList.filterNotNull().map { it.toUuid() })
        }.findList()
        return mapper.toBundle(list).onEach {
            it.remove("password")
        }
    }

    fun create(ctx: Context, data: Bundle): ViewUserRoleEntity = ctx {
        val phone: String? = data("phone")
        val email: String? = data("email")

        if ((phone ?: email) == null) throw IllegalArgumentException("请输入手机号或邮箱")

        val roleId: Int = data("roleId")!!
        // 如果没有输入用户名，用户名将被设置为以手机号或邮箱
        if ("userName" !in data) data["userName"] = phone ?: email

        // 如果存在密码，则对密码进行加密
        data["password"]?.let {
            data["password"] = encoder.encode(it.toString())
        }

        val userName: String = data("userName")!!

        // 查询或创建用户
        val exists = query<QSysUserEntity> {
            or {
                phone?.let { this.phone.eq(it) }
                email?.let { this.email.eq(it) }
                this.userName.eq(userName)
            }
        }.exists()

        if (exists) {
            throw IllegalStateException("同名用户已存在")
        }
        val userId = UUID.randomUUID()
        transaction { tran ->
            mapper.toModel<SysUserEntity>(data).apply {
                applyDatabase(ctx)
                id = userId
                insert(tran)
            }

            val hasRole = query<QSysUserRolesEntity> {
                this.userId.eq(userId)
            }.exists()

            if (!hasRole) {
                createModel<SysUserRolesEntity> {
                    this.userId = userId
                    this.roleId = roleId
                    this.createdBy = ctx.userIdOrNull ?: userId
                    insert(tran)
                }
            }
        }

        return@ctx query<QViewUserRoleEntity>().userId.eq(userId).findOne()!!
    }

    fun getUserInfo(ctx: Context, id: String): Bundle = ctx {
        val id1 = id.toUuid()
        val userInfo = query<QSysUserEntity> {
            this.id.eq(id1)
        }.findOne()?.let { mapper.toBundle(it) }

        userInfo ?: throw NoSuchElementException("找不到用户")

        val roleId = query<QSysUserRolesEntity> {
            userId.eq(id1)
        }.findOne()
        // 神仙也不知道这一段代码究竟干了什么。
        // if (roleId != null) {
        userInfo["roleId"] = roleId?.roleId ?: 3
        //} else {
        //    SysUserRolesEntity().apply {
        //        this.userId = id1
        //        this.roleId = 7
        //        this.createdBy = ctx.user.id
        //    }
        //    userInfo["roleId"] = 7
        // }
        return userInfo
    }

    /** 修改用户 */
    fun update(ctx: Context, data: Bundle): Any = ctx {
        // 加密或移除密码
        data["password"]?.let {
            val str = it.toString()
            if (str.isNotEmpty()) {
                data["password"] = encoder.encode(str)
            } else data.remove("password")
        }
        transaction { tran ->
            // 更新用户信息
            val model = ctx.createModel<SysUserEntity>(data, true)
            model.update(tran)

            // 更新角色信息
            data.getAs<Int>("roleId")?.let { roleId ->
                val id: UUID = data["id"]!!.toString().toUuid()
                val role = query<QSysUserRolesEntity>().userId.eq(id).findOne()
                if (role != null && role.roleId != roleId) {
                    role.roleId = roleId
                    role.save(tran)
                } else if (role == null) {
                    createModel<SysUserRolesEntity> {
                        this.roleId = roleId
                        this.userId = id
                    }.save(tran)
                }
            }
        }
        return success()
    }

    fun delete(ctx: Context, ids: List<String>): Any = ctx {
        transaction { tran ->
            query<QSysUserRolesEntity> {
                this.userId.isIn(ids.map { it.toUuid() })
            }.delete(tran)
        }
        return success()
    }

    fun updatePassword(ctx: Context, id: String, origin: String, target: String): Any {
        val user = ctx.query<QSysUserEntity>().id.eq(id.toUuid()).findOne()
        user ?: throw NoSuchElementException("用户不存在")
        if (!user.password.isNullOrEmpty() && !encoder.matches(origin, user.password)) {
            throw IllegalStateException("密码错误！")
        }
        user.password = target
        user.update()
        return success()
    }

    fun myUserInfo(ctx: Context): Bundle {
        val obj = ctx.session
        val user = ctx.query<QSysUserEntity> {
            id.eq(ctx.user.id)
            deleted.ne(true)
        }.findOne()!!
        return bundleOf().also {
            it.putAll(obj)
            it.putAll(mapper.toBundle(user))
            it.remove("password")
        }
    }

    fun myUserRole(ctx: Context): Bundle {
        return userRoleService.getRoleByUserId(ctx, bundleScope {
            "userId" set ctx.userId
        })
    }

    fun mySession(ctx: Context) : SessionMap?{
        return session.mySession(ctx)
    }

    fun avatar(ctx: Context, id: String): ResponseEntity<Flux<DataBuffer>> {
        ctx as IWebExchangeContext

        val avatar = ctx.query<QSysUserEntity> {
            this.id.eq(id.toUuid())
        }.findOne()?.avatar
        val path = Path("work/image/avatar", avatar.toString())
        if (avatar == null || !path.exists()) {
            throw object : IOException("文件 \"$avatar\" 不存在"), ErrorWebFilter.Ignore {
                override fun fillInStackTrace(): Throwable {
                    return this
                }
            }
        }

        // 文件更新检查
        val lastModified = ctx.exchange.request.headers.ifModifiedSince;
        val fileLastModified = path.toFile().lastModified()

        if (lastModified == fileLastModified) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                .lastModified(fileLastModified)
                .build()
        }

        // 返回文件
        val fileFlux: Flux<DataBuffer> = DataBufferUtils.read(path, DefaultDataBufferFactory.sharedInstance, 8192)
        val mime = MediaTypeFactory.getMediaType(avatar).orElse(MediaType.APPLICATION_OCTET_STREAM)
        return ResponseEntity.ok()
            .contentType(mime)
            .contentLength(path.toFile().length())
            .headers {
                it.setCacheControl(CacheControl.maxAge(Duration.ofMinutes(1)))
                it.lastModified = path.toFile().lastModified()
            }
            .body(fileFlux)
    }

    suspend fun avatar(ctx: Context, id: String, file: FilePart): ResponseEntity<ByteArray> {
        val ext = file.filename().split(".").last()
        val filename = "${id}.${ext}"
        val outFile = Path("work/image/avatar", filename).toFile()
        if (outFile.exists()) outFile.delete()
        file.transferTo(outFile).awaitSingleOrNull()
        val user = ctx.query<QSysUserEntity> {
            this.id.eq(id.toUuid())
        }.findOne()!!
        user.avatar = filename
        // user.applyDatabase(ctx)
        user.save()
        return success()
    }
}