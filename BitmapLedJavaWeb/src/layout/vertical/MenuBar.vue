<script setup lang="ts">
import { storeToRefs } from 'pinia/dist/pinia'
import { useRoute } from 'vue-router'
import { useBasicStore } from '@/store/basic'
import SidebarItem from '@/layout/default/sidebar/SidebarItem.vue'

const { allRoutes } = storeToRefs(useBasicStore())

const route = useRoute()
const activeMenu = ref<any>(route.path);
watch(
  () => route.path,
  (val) => {
    activeMenu.value = val
  }
)

provide('sidebar:showIcon', ref(false))
</script>

<template>
  <el-menu class="menu-bar" :default-active="activeMenu" mode="horizontal">
    <sidebar-item v-for="r in allRoutes" :key="r.path" :item="r" :base-path="r.path" />
  </el-menu>
</template>

<style scoped lang="scss">
.menu-bar {
  --el-menu-bg-color: transparent;
  --el-menu-hover-bg-color: var(--el-color-primary-light-3);
  --el-menu-text-color: var(--el-text-color);
  --el-menu-hover-text-color: white;
  --el-menu-horizontal-height: 48px;
  --el-menu-item-height: 48px;

  :deep(.el-menu-item.is-active) {
    border-bottom: 4px solid var(--el-menu-active-color);
    border-top: 4px solid transparent;
  }
}
</style>
