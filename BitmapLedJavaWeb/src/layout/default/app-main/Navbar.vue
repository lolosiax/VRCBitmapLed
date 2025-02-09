<template>
  <div class="navbar reset-el-dropdown">
    <div class="navbar-left">
      <div class="nav-bar-system rowSC">
        <!--  切换sidebar按钮  -->
        <hamburger
          v-if="settings.showHamburger"
          :is-active="sidebar.opened"
          class="hamburger-container"
          @toggle-click="toggleSideBar"
        />
        <!--  面包屑导航  -->
        <breadcrumb class="breadcrumb-container" />
      </div>
      <NavBarAppTools position="left" />
    </div>
    <div class="navbar-center">
      <!--导航标题-->
      <div v-if="settings.showNavbarTitle" class="heardCenterTitle">{{ settings.title }}</div>
      <NavBarAppTools position="center" />
    </div>
    <div class="navbar-right">
      <NavBarAppTools position="right" />
      <!-- 下拉操作菜单 -->
      <debugger />
      <AvatarFrame />
    </div>
  </div>
</template>

<script setup lang="ts">
import AvatarFrame from '@/layout/default/app-main/component/AvatarFrame.vue'
import Breadcrumb from './Breadcrumb.vue'
import Hamburger from './Hamburger.vue'
import { useBasicStore } from '@/store/basic'
import Debugger from '@/layout/default/app-main/component/Debugger.vue'
import NavBarAppTools from '@/layout/default/app-main/NavBarAppTools'

const basicStore = useBasicStore()
const { settings, sidebar, setToggleSideBar } = basicStore

const toggleSideBar = () => {
  setToggleSideBar()
}
</script>

<style lang="scss" scoped>
.online-status {
  display: flex;
  align-items: center;

  .menu-icon {
    color: gold;
    margin-right: 0.5em;
  }

  span {
    color: red;
  }
}

.navbar {
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

  .nav-bar-system {
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .navbar-left {
    display: flex;
    align-items: center;

    > * {
      margin-right: 1em;
    }
  }

  .navbar-center {
    flex: 1 1;
    display: flex;
    align-items: center;
    justify-content: center;

    > * {
      margin: 0.5em;
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

  //center-title
  .heardCenterTitle {
    text-align: center;
    position: absolute;
    top: 50%;
    left: 46%;
    font-weight: 600;
    font-size: 20px;
    transform: translate(-50%, -50%);
  }
}

.drop-down {
}
</style>
