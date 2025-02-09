<template>
  <div class="login-container columnCE">
    <!--    <div class="login-hero">-->
    <!--      <img src="@/assets/layout/login.svg" :alt="settings.title" />-->
    <!--    </div>-->

    <!-- 登录界面 -->
    <el-form v-if="mode == 0" ref="refLoginForm" class="login-form" :model="subForm" :rules="formRules">
      <div class="title-container">
        <h3 class="title text-center">{{ settings.title }}</h3>
      </div>
      <el-form-item prop="userName" :rules="formRules.isNotNull('用户名')">
        <span class="svg-container">
          <ElSvgIcon name="User" :size="14" />
        </span>
        <el-input v-model="subForm.userName" placeholder="用户名" />
        <!--占位-->
      </el-form-item>
      <el-form-item prop="password" :rules="formRules.isNotNull('密码')">
        <span class="svg-container">
          <ElSvgIcon name="Lock" :size="14" />
        </span>
        <el-input
          :key="passwordType"
          ref="refPassword"
          v-model="subForm.password"
          :type="passwordType"
          name="password"
          placeholder="密码"
          @keyup.enter="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>
      <div class="tip-message">{{ tipMessage }}</div>
      <el-button :loading="subLoading" class="login-btn" type="primary" size="default" @click.prevent="handleLogin">
        登录
      </el-button>
      <p class="change-mode">
        还没有账户？点此
        <span @click="mode = 1">注册</span>
        或
        <span @click="handleGuestLogin">访客登录</span>
      </p>
    </el-form>

    <!-- 注册界面 1 -->
    <el-form v-else-if="mode == 1" ref="refLoginForm" class="login-form" :model="subForm" :rules="formRules">
      <div class="title-container">
        <h3 class="title text-center">注册账户</h3>
      </div>
      <el-form-item prop="userName" :rules="formRules.isNotNull('用户名')">
        <span class="svg-container">
          <ElSvgIcon name="User" :size="14" />
        </span>
        <el-input v-model="subForm.userName" placeholder="用户名" />
        <!--占位-->
      </el-form-item>
      <el-form-item prop="email" :rules="formRules.email('邮箱')">
        <span class="svg-container">
          <MenuIcon icon="envelope" style="font-size: 14px; left: 0" />
        </span>
        <el-input v-model="subForm.email" placeholder="邮箱" />
        <!--占位-->
      </el-form-item>
      <el-form-item prop="captcha">
        <div flex class="captcha">
          <img :src="captchaData" alt="验证码" relative style="left: 2em" @click="reloadCaptcha" />
          <el-input v-model="captchaValue" placeholder="验证码，点击图片刷新" />
        </div>
      </el-form-item>
      <div class="tip-message">{{ tipMessage }}</div>
      <el-button :loading="subLoading" class="login-btn" type="primary" size="default" @click.prevent="verifyRobotCode">
        下一步
      </el-button>
      <p class="change-mode">
        已经有账户了？点此
        <span @click="mode = 0">登录</span>
      </p>
    </el-form>

    <!-- 注册界面 2 -->
    <el-form v-else-if="mode == 2" ref="refLoginForm" class="login-form" :model="subForm" :rules="formRules">
      <div class="title-container">
        <h3 class="title text-center">欢迎您，{{ subForm.userName }}</h3>
      </div>
      <div text-white m-b>请在您的邮箱 {{ subForm.email }} 中获取验证码，然后为账户设置一个新的密码</div>
      <el-form-item prop="emailVerify" :rules="formRules.isNotNull('邮箱验证码')">
        <span class="svg-container">
          <menu-icon icon="envelope-paper" style="font-size: 12px; left: 0" />
        </span>
        <el-input v-model="subForm.emailVerify" placeholder="邮箱验证码" />
        <!--占位-->
      </el-form-item>
      <el-form-item prop="password" :rules="formRules.isNotNull('密码')">
        <span class="svg-container">
          <ElSvgIcon name="Lock" :size="14" />
        </span>
        <el-input
          :key="passwordType"
          ref="refPassword"
          v-model="subForm.password"
          :type="passwordType"
          name="password"
          placeholder="密码"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>
      <el-form-item>
        <span class="svg-container">
          <ElSvgIcon name="Lock" :size="14" />
        </span>
        <el-input
          :key="passwordType"
          v-model="subForm.password_2"
          :type="passwordType"
          placeholder="请再次输入一遍密码"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>
      <div class="tip-message">{{ tipMessage }}</div>
      <el-button :loading="subLoading" class="login-btn" type="primary" size="default" @click.prevent="handleRegistry">
        注册
      </el-button>
      <p class="change-mode">
        已经有账户了？点此
        <span @click="mode = 0">登录</span>
      </p>
    </el-form>
    <div class="icp">
      鲁ICP备 000000000号-0
      <template v-if="buildEnv">
        <br />
        {{ buildEnv }}
      </template>
    </div>
    <div class="copyright">© 2024 一七年夏</div>
  </div>
</template>

<script setup lang="ts">
import { registrySSE } from '@/api/sse'
import { getSSEConnect } from '@/utils/bus'
import { reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useBasicStore } from '@/store/basic'
import { elMessage, useElement } from '@/hooks/use-element'
import { captcha, getMyRole, login, register, verify } from '@/api/user'
import type { FormInstance, InputInstance } from 'element-plus'
import { settings as viteSettings } from '@/settings'
import { ElMessage } from 'element-plus'
import { setRouterFromDatabase } from '@/hooks/use-permission'
import { roleRoutes } from '@/router'

const buildEnv = (() => {
  const time = import.meta.env.VITE_BUILD_TIMESTAMP
  const version = import.meta.env.VITE_PROJECT_VERSION || import.meta.env.PROJECT_VERSION
  if (time) {
    let build = import.meta.env.VITE_BUILD_DISPLAY_NAME
    let hash = (import.meta.env.VITE_GIT_COMMIT || '').slice(0, 8)
    build = build ? `构建 ${build} 于` : '构建于'
    hash = hash ? `-${hash}` : ''
    return `当前版本 ${version}${hash} ${build} ${time}`
  } else {
    return `版本 ${version}-dev`
  }
})()

/* listen router change and set the query  */
const { settings } = useBasicStore()
//element valid
const formRules = useElement().formRules
//form
const subForm = reactive({
  userName: '',
  email: '',
  emailVerify: '',
  password: '',
  password_2: ''
})
const state: any = reactive({
  otherQuery: {},
  redirect: undefined
})
const route = useRoute()

onMounted(() => {
  // reloadCaptcha()
})

let mode = $ref(0)
let captchaData = $ref('')
let captchaId = $ref(0)
const captchaValue = $ref('')
async function reloadCaptcha() {
  const { image, id } = await captcha()
  captchaData = `data:image/png;base64,${image}`
  captchaId = id
}

function verifyRobotCode() {
  refLoginForm.validate(async (valid) => {
    if (valid) {
      await verify(captchaId, captchaValue, subForm.userName, subForm.email)
      // 进入注册账户的最后一步
      mode = 2
    }
  })
}

function handleRegistry() {
  refLoginForm.validate(async (valid) => {
    if (valid) {
      if (subForm.password != subForm.password_2) {
        ElMessage.error('两次输入的密码不一致')
        return
      }
      await register(subForm.userName, subForm.email, subForm.emailVerify, subForm.password)
      ElMessage.success('注册成功！')
      // 进行登录
      mode = 0
      loginFunc()
    }
  })
}

function getOtherQuery(query) {
  return Object.keys(query).reduce((acc, cur) => {
    if (cur !== 'redirect') {
      acc[cur] = query[cur]
    }
    return acc
  }, {})
}

watch(
  () => route.query,
  (query) => {
    if (query) {
      state.redirect = query.redirect
      state.otherQuery = getOtherQuery(query)
    }
  },
  { immediate: true }
)

/*
 *  login relative
 * */
let subLoading = $ref(false)
//tip message
let tipMessage = $ref('')
//sub form
const refLoginForm: FormInstance = $ref(null) as any

function handleLogin() {
  // router.push('/category')
  // eslint-disable-next-line no-constant-condition
  // if (1 === 1) return

  refLoginForm.validate((valid) => {
    if (valid) {
      subLoading = true
      loginFunc()
    } else {
      ElMessage.error('请检查输入是否正确')
    }
  })
}

function handleGuestLogin() {
  subForm.userName = 'guest'
  subForm.password = '123456'
  subLoading = true
  loginFunc()
}

const router = useRouter()
const basicStore = useBasicStore()

async function loginFunc() {
  try {
    const data = await login(subForm)
    elMessage('登录成功')
    basicStore.setToken(data?.Authorization)
    basicStore.keepSession = true

    // const role = await getMyRole()
    // basicStore.setUserInfo({
    //   userInfo: data,
    //   roles: [role.roleType],
    //   codes: [role.roleId]
    // })
    //
    // setRouterFromDatabase(roleRoutes)
    // basicStore.getUserInfo = true

    // 注册SSE的用户信息
    const sse = getSSEConnect()
    if (sse != null) {
      await registrySSE(sse.id)
    }

    // router.push("/").catch(e => console.error(e));
    if (route.query?.redirect) {
      let url = route.query.redirect as string
      if (viteSettings.viteBasePath.endsWith('/') && url.startsWith('/')) {
        url = url.replace(/^\//, '')
      }
      // window.location.href = viteSettings.viteBasePath + url
      await router.push(url || '/index')
    } else {
      await router.push('/index')
      // window.location.href = viteSettings.viteBasePath
    }
  } catch (e: Error | any) {
    tipMessage = e?.message
  } finally {
    subLoading = false
  }
}

/*
 *  password show or hidden
 * */
const passwordType = ref('password')
const refPassword = ref<InputInstance>()

function showPwd() {
  if (passwordType.value === 'password') {
    passwordType.value = ''
  } else {
    passwordType.value = 'password'
  }
  nextTick(() => {
    refPassword.value!.focus()
  })
}
</script>
<style lang="scss" scoped>
$bg: #fbfcff;
$dark_gray: #333;
$gray: #999;
$light_gray: #eee;
.login-container {
  height: 100dvh;
  position: relative;
  overflow-y: hidden;
  width: 100%;
  background-color: $bg;
  background-image: url('./img.png');
  background-repeat: no-repeat;
  background-size: cover;
  background-position-x: 65%;
  background-position-y: 75%;

  .login-form {
    width: 360px;
    padding: 40px 30px;
    background: #0008;
    box-shadow: 0 4px 16px rgba(4, 61, 175, 0.15);
    border-radius: 8px;
    margin-right: 10vw;
    z-index: 10;
    @media screen and (min-width: 769px) and (max-width: 992px) {
      margin-right: 10vw;
    }
    @media only screen and (max-width: 768px) {
      margin-right: auto;
      margin-left: auto;
    }

    .captcha {
      img {
        cursor: pointer;
      }
    }

    .change-mode {
      margin: 0;
      text-align: center;
      color: white;
      position: relative;
      top: 20px;
      opacity: 75%;

      &:hover {
        opacity: 1;
      }

      span {
        color: var(--el-color-primary-light-3);
        text-decoration: underline;
        cursor: pointer;
      }
    }
  }

  .link {
    padding: 10px 30px;
    background: #0006;
    box-shadow: 0 4px 16px rgba(4, 61, 175, 0.15);
    border-radius: 8px;
    position: absolute;
    left: 15px;
    top: 10px;
    color: white;
    display: flex;
    align-items: center;
    div {
      font-size: 18px;
      font-weight: bold;
      display: inline-block;
      margin-right: 1em;
    }
    a {
      background-color: #ffffffaa;
      color: #1f294a;
      padding: 0.3em 0.5em;
      margin-right: 0.5em;
      border-radius: 0.3em;
    }
  }

  .title-container {
    .title {
      font-size: 18px;
      color: white;
      margin: 0 auto 25px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .icp {
    position: absolute;
    bottom: 0.5em;
    left: 2em;
    color: #fffb;
  }

  .copyright {
    position: absolute;
    bottom: 0.5em;
    right: 2em;
    color: #fffb;
  }
}

.login-hero {
  width: 40vw;
  position: absolute;
  top: 50%;
  left: 15vw;
  z-index: 0;
  transform: translateY(-50%);
  @media screen and (min-width: 769px) and (max-width: 992px) {
    width: 60vw;
    left: 5vw;
  }
  @media screen and (max-width: 768px) {
    width: 100vw;
    left: 0;
  }

  img {
    width: 100%;
  }
}

.svg-container {
  padding-left: 16px;
  color: $gray;
  text-align: center;
  width: 30px;
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
}

//错误提示信息
.tip-message {
  color: #e4393c;
  height: 30px;
  margin-top: -12px;
  font-size: 12px;
}

//登录按钮
.login-btn {
  width: 100%;
  //margin-bottom: 30px;
}

.show-pwd {
  width: 50px;
  font-size: 16px;
  color: $gray;
  cursor: pointer;
  text-align: center;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}
</style>

<style lang="scss">
//css 样式重置 增加个前缀避免全局污染
.login-container {
  .el-input__wrapper {
    background-color: transparent;
    box-shadow: none;
  }

  .el-form-item {
    border: 1px solid #414141;
    background: #000b;
    border-radius: 4px;
    color: white;

    &__content {
      position: relative;
    }
  }

  .el-input input {
    background: transparent;
    border: 0;
    -webkit-appearance: none;
    border-radius: 0;
    padding: 10px 5px 10px 35px;
    color: white;
    height: 42px; //此处调整item的高度
    caret-color: #999;
  }

  //hidden the input border
  .el-input__inner {
    box-shadow: none !important;
  }
}
</style>
