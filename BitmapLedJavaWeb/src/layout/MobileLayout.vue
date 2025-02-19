<script setup lang="ts">
import type { RouterTypes } from '~/basic'
import SvgIcon from '@/icons/SvgIcon.vue'

const route = useRoute()
const logo = 'sidebar-logo'

const title = computed(() => {
  let matched = route.matched.filter((item) => item.meta?.title) as any as RouterTypes
  const isHasDashboard = (matched[0]?.name as string)?.toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase()
  if (!isHasDashboard) {
    matched = [{ path: '/dashboard', meta: { title: 'Dashboard' } }].concat(matched as any) as any
  }
  const bc = matched.findLast((item) => item.meta && item.meta.title && item.meta.breadcrumb !== false)
  return bc?.meta?.title ?? ''
})
</script>

<template>
  <div class="mobile-layout">
    <div class="mobile-header">
      <div class="mobile-icon">
        <svg-icon v-if="logo" :icon-class="logo" class="sidebar-logo" />
      </div>
      <div class="mobile-title">{{ title }}</div>
      <div class="mobile-menu">
        <icon-button icon="list" />
      </div>
    </div>
    <div class="mobile-body">
      <RouterView />
    </div>
    <div class="mobile-footer"></div>
  </div>
</template>

<style scoped lang="scss">
.mobile-layout {
  width: 100dvw;
  height: 100dvh;
  overflow: auto;
  font-size: 1.5em;
  display: grid;
  grid-template-rows: auto 1fr auto;

  .mobile-header {
    display: grid;
    grid-template-columns: 2em 1fr 2em;
    align-items: center;
    gap: 1em;
    height: 2.2em;
    box-shadow: 0 0 0.5em gray;
    padding: 0 1em;

    .mobile-icon {
      fill: currentColor;
      color: var(--sidebar-logo-color);
      width: var(--sidebar-logo-width);
      height: var(--sidebar-logo-height);
      font-size: 1.3em;
    }

    .mobile-title {
      text-align: center;
      text-wrap: nowrap;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    .mobile-menu {
      text-align: right;

      &:deep(.menu-icon) {
        font-size: 2em;
      }
    }
  }

  .mobile-body {
    overflow: auto;
    padding: var(--app-main-padding);

    &.no-padding {
      padding: 0;
    }
  }
}
</style>
