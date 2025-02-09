<template>
  <div :class="classObj" class="layout-wrapper">
    <!--left side-->
    <Sidebar v-if="settings.showLeftMenu" class="sidebar-container" :parent-router="prop.parentRouter" />
    <!--right container-->
    <div class="main-container">
      <Navbar v-if="settings.showTopNavbar" class="nav-bar" />
      <TagsView v-if="settings.showTagsView" />
      <AppMain class="app-main" />
    </div>
  </div>
</template>
<script setup lang="ts" name="SubMenuLayout">
import { computed } from 'vue'
import Sidebar from './sidebar/index.vue'
import AppMain from '../default/app-main/index.vue'
import Navbar from '../default/app-main/Navbar.vue'
import TagsView from '../default/app-main/TagsView.vue'
import { useBasicStore } from '@/store/basic'
import { resizeHandler } from '@/hooks/use-layout'
import type { RouteRawConfig } from '~/basic'

const prop = defineProps<{
  parentRouter: RouteRawConfig
}>()

const { sidebar, settings } = useBasicStore()
const classObj = computed(() => {
  return {
    closeSidebar: !sidebar.opened,
    hideSidebar: !settings.showLeftMenu
  }
})
resizeHandler()
</script>

<style lang="scss" scoped>
.main-container {
  transition: margin-left var(--sideBar-switch-duration);
  margin-left: var(--side-bar-width);
  position: relative;
  height: 100vh;
  display: flex;
  flex-direction: column;
  .nav-bar {
    flex-shrink: 0;
    height: 50px;
  }
  .app-main {
    flex-grow: 1;
    overflow: auto;
  }
}
.sidebar-container {
  transition: width var(--sideBar-switch-duration);
  width: var(--side-bar-width) !important;
  background-color: var(--el-menu-bg-color);
  height: 100%;
  position: fixed;
  font-size: 0;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;
  border-right: 0.5px solid var(--side-bar-border-right-color);
}
.closeSidebar {
  .sidebar-container {
    width: 54px !important;
  }
  .main-container {
    margin-left: 54px !important;
  }
}
.hideSidebar {
  .sidebar-container {
    width: 0 !important;
  }
  .main-container {
    margin-left: 0;
  }
}
</style>
