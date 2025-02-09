<template>
  <div class="navbar reset-el-dropdown">
    <div class="navbar-left">
      <Logo :collapse="false" />
      <MenuBar />
    </div>
    <div class="navbar-right">
      <NavBarAppTools position="left" />
      <NavBarAppTools position="center" />
      <NavBarAppTools position="right" />
      <!-- 下拉操作菜单 -->
      <debugger />
      <AvatarFrame />
    </div>
  </div>
</template>

<script setup lang="ts">
import AvatarFrame from '@/layout/default/app-main/component/AvatarFrame.vue'
import Logo from '@/layout/default/sidebar/Logo.vue'
import Debugger from '@/layout/default/app-main/component/Debugger.vue'
import NavBarAppTools from '@/layout/default/app-main/NavBarAppTools'
import MenuBar from '@/layout/vertical/MenuBar.vue'
</script>

<style lang="scss" scoped>
.navbar {
  --sidebar-logo-background: transparent;
  --sidebar-logo-title-color: var(--el-text-color);
  height: var(--nav-bar-height);
  overflow: hidden;
  background: var(--nav-bar-background);
  box-shadow: var(--nav-bar-box-shadow);
  z-index: 1;
  display: flex;
  align-items: center;
  overflow-x: auto;

  &::-webkit-scrollbar {
    display: none;
  }

  .navbar-left {
    flex: 1 1;
    display: grid;
    grid-template-columns: auto 1fr;
    align-items: center;
    gap: 3em;

    .menu-bar {
      position: relative;
      top: 2px;
    }
  }

  .navbar-right {
    display: flex;
    align-items: center;
    justify-content: flex-end;

    :deep(> *) {
      margin-left: 1.5em;
      margin-right: 0;
    }
  }
}
</style>
