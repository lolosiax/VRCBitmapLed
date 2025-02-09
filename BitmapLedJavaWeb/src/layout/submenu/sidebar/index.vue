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
        <sidebar-item v-for="r in routes" :key="r.path" :item="r" :base-path="basePath(r.path)" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts" name="SubMenuSideBar">
import { computed } from 'vue'
import { storeToRefs } from 'pinia/dist/pinia'
import { useRoute } from 'vue-router'
import Logo from '@/layout/default/sidebar/Logo.vue'
import SidebarItem from '@/layout/default/sidebar/SidebarItem.vue'
import { useBasicStore } from '@/store/basic'

const { settings, sidebar } = storeToRefs(useBasicStore())
const route = useRoute()
const activeMenu = ref<any>(route.path)
import type { BootstrapIcons, RouteRawConfig, RouterTypes } from '~/basic'

const prop = defineProps<{
  parentRouter: RouteRawConfig
}>()

watch(
  () => route.path,
  (val) => {
    activeMenu.value = val
  }
)

const routes = computed(() => {
  return [
    {
      path: '/',
      meta: {
        title: '返回',
        icon: 'arrow-left-square' as BootstrapIcons
      }
    },
    ...prop.parentRouter.children!
  ] as RouterTypes
})

function basePath(path: string) {
  if (path.startsWith('/')) return `/${path}`
  else return `${prop.parentRouter.path}/${path}`
}
</script>
<style lang="scss">
//fix open the item style issue
.el-menu-vertical {
  width: var(--side-bar-width);
}
</style>
