<template>
  <div class="avatar-wrapper" :title="(userInfo?.realName || userInfo?.userName) + ' - 在线'">
    <img :src="avatar" alt="用户头像" class="user-avatar" @error="avatar0 = userImage" />
    <div class="user-avatar-status" :style="{ backgroundColor: !online ? 'red' : null }">
      <el-popover :visible="!online" :effect="'dark'" :width="170">
        <template #reference>
          <div />
        </template>
        <div class="online-status">
          <menu-icon icon="exclamation-triangle-fill" />
          <span>与服务器连接中断</span>
        </div>
      </el-popover>
    </div>
  </div>
</template>

<script setup lang="ts">
import userImage from '@/assets/layout/user.png'
import { useBasicStore } from '@/store/basic'

const basicStore = useBasicStore()
const { userInfo } = basicStore

const avatar0 = $ref()
const avatar = computed(() => avatar0 || userInfo.avatar || userImage)

const online = toRef(basicStore, 'online')
</script>

<style scoped lang="scss">
//logo
.avatar-wrapper {
  margin-top: 5px;
  position: relative;
  cursor: pointer;

  .user-avatar {
    cursor: pointer;
    width: 40px;
    height: 40px;
    border-radius: 10px;
  }

  .user-avatar-status {
    position: absolute;
    right: -2px;
    bottom: 0;
    height: 10px;
    width: 10px;
    border-radius: 50%;
    background-color: #55e802;
    box-shadow: 0 0 3px black;
  }

  .el-icon-caret-bottom {
    cursor: pointer;
    position: absolute;
    right: -20px;
    top: 25px;
    font-size: 12px;
  }
}
</style>
