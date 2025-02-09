<template>
  <div v-if="settings.ShowDropDown" class="right-menu rowSC">
    <!--        <ScreenFull />-->
    <!--        <ScreenLock />-->
    <!--        <ThemeSelect />-->
    <!--        <SizeSelect />-->
    <!--        <LangSelect />-->
    <el-dropdown trigger="click" size="default">
      <Avatar />
      <template #dropdown>
        <el-dropdown-menu class="drop-down">
          <el-dropdown-item class="welcome-user">
            {{ time }}好，
            <b>{{ userInfo.realName || userInfo.userName || '平台用户' }}</b>
          </el-dropdown-item>
          <router-link to="/">
            <el-dropdown-item divided>{{ langTitle('Home') }}</el-dropdown-item>
          </router-link>
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click="loginOut">{{ langTitle('login out') }}</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup lang="ts">
import Avatar from '@/layout/default/app-main/component/Avatar.vue'
import { nextTick } from 'vue'
import { useRouter } from 'vue-router'
import LangSelect from './component/LangSelect.vue'
import ScreenFull from './component/ScreenFull.vue'
import SizeSelect from './component/SizeSelect.vue'
import ThemeSelect from './component/ThemeSelect.vue'
import ScreenLock from './component/ScreenLock.vue'
import { resetState } from '@/hooks/use-permission'
import { elMessage } from '@/hooks/use-element'
import { useBasicStore } from '@/store/basic'
import { langTitle } from '@/hooks/use-common'

const basicStore = useBasicStore()
const { settings, sidebar, userInfo } = basicStore

//退出登录
const router = useRouter()
const loginOut = () => {
  elMessage('退出登录成功')
  router.push(`/login?redirect=/`)
  nextTick(() => {
    resetState()
  })
}

const time = computed(() => {
  const hover = new Date().getHours()
  if (hover > 20) return '晚上'
  if (hover > 17) return '傍晚'
  if (hover > 13) return '下午'
  if (hover > 11) return '中午'
  if (hover > 8) return '上午'
  if (hover > 5) return '早上'
  return '凌晨'
})
</script>

<style scoped lang="scss">
//drop-down
.right-menu {
  cursor: pointer;
  margin-right: 16px !important;
  position: relative;
  background-color: var(--nav-bar-right-menu-background);
  .switch-platform-btn {
    margin: 0 0.5em 0 1em;
    padding: 0 0.5em;
    font-size: 1em;
  }
}
</style>
