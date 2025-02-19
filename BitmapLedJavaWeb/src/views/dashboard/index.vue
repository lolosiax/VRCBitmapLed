<script setup lang="ts">
import ServerHostQR from '@/views/dashboard/components/ServerHostQR.vue'

onMounted(() => {
  useBasicStore().noMainPadding = true
})
</script>

<template>
  <div class="dashboard">
    <div class="left-panel">
      <h1>Bitmap Led Java Web</h1>
      <br />
      <div>
        VRChat 点阵屏幕控制台，功能尚未实现<br/>
        可能需要色彩控制器，文本调度器一类的功能
      </div>
    </div>
    <div class="right-panel">
      <server-host-q-r />
    </div>
  </div>
</template>

<style lang="scss">
.dashboard {
  height: 100%;
  width: 100%;
  overflow: hidden;
  display: grid;
  grid-template-columns: auto 300px;

  .left-panel {
    padding: var(--app-main-padding);
  }

  .right-panel {
    background-color: var(--el-fill-color-dark);
  }
}
</style>
