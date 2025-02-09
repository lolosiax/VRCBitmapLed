<template>
  <!-- 始终显示 elSvgIcon-->
  <el-icon class="el-svg-icon menu-icon">
    <i :class="`bi bi-${icon}`"/>
  </el-icon>
</template>

<script setup lang="ts">

defineProps({
  icon: {type: String, default: null}
})
</script>

<style scoped lang="scss">
.el-svg-icon {
  width: 1em;
  height: 1em;
  position: relative;
  right: 3px;
  font-size: var(--sidebar-el-icon-size);
  text-align: left !important;
}
</style>
