<template>
  <div id="Sidebar" class="reset-menu-style">
    <!--logo-->
    <Logo v-if="settings.sidebarLogo" :collapse="!sidebar.opened" />
    <!--router menu-->
    <el-scrollbar>
      <el-menu
        class="el-menu-vertical"
        :collapse="!sidebar.opened"
        :default-active="activeMenu"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="r in allRoutes" :key="r.path" :item="r" :base-path="r.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts" name="SideBar">
import { storeToRefs } from 'pinia/dist/pinia'
import { useRoute } from "vue-router";
import Logo from './Logo.vue'
import SidebarItem from './SidebarItem.vue'
import { useBasicStore } from '@/store/basic'
const { settings, allRoutes, sidebar } = storeToRefs(useBasicStore())
const route = useRoute()
const activeMenu = ref<any>(route.path);

watch(() => route.path, val => {
  activeMenu.value = val
})
</script>
<style lang="scss">
//fix open the item style issue
.el-menu-vertical {
  width: var(--side-bar-width);
}
</style>
