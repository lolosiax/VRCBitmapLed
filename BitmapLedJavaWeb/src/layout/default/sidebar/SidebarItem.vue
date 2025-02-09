<template>
  <template v-if="!item.hidden">
    <template v-if="showSidebarItem(item.children, item)">
      <Link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <MenuIcon v-if="showIcon" :icon="onlyOneChild.meta?.icon || item.meta?.icon" />
          <template #title>{{ langTitle(onlyOneChild.meta?.title) }}</template>
        </el-menu-item>
      </Link>
    </template>
    <el-sub-menu v-else :index="resolvePath(item.path)">
      <template v-if="item.meta" #title>
        <MenuIcon v-if="showIcon" :icon="item.meta?.icon" />
        <span>{{ langTitle(item.meta.title) }}</span>
      </template>
      <SidebarItem
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
      />
    </el-sub-menu>
  </template>
</template>

<script setup lang="ts">
import { resolve } from 'path-browserify'
import Link from './Link.vue'
import MenuIcon from '@/components/MenuIcon.vue'
import type { RouteRawConfig, RouterTypes } from '~/basic'
import { isExternal } from '@/hooks/use-layout'
import { langTitle } from '@/hooks/use-common'

const props = defineProps({
  //每一个router Item
  item: {
    type: Object,
    required: true
  },
  //用于判断是不是子Item,设置响应的样式
  isNest: {
    type: Boolean,
    default: false
  },
  //基础路径，用于拼接
  basePath: {
    type: String,
    default: ''
  }
})
const showIcon = inject('sidebar:showIcon', ref(true))
//显示sidebarItem 的情况
const onlyOneChild = ref()
const showSidebarItem = (children: RouterTypes = [], parent: RouteRawConfig) => {
  if (parent.expand === false) {
    onlyOneChild.value = { ...parent, path: '', noChildren: true }
    return true
  }
  const showingChildren = children.filter((item) => {
    return !item.hidden
  })
  if (showingChildren.length === 1 && !parent?.alwaysShow) {
    onlyOneChild.value = showingChildren[0]
    return true
  }
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noChildren: true }
    return true
  }
  return false
}
const resolvePath = (routePath) => {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  return resolve(props.basePath, routePath)
}
</script>

<style scoped lang="scss">
:deep(.el-menu-item.is-active) {
  text-shadow: 1px 1px 0 #ffffff88;
}
</style>
