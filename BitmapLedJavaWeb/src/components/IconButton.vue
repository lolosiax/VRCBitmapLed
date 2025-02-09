<script setup lang="ts">
import type icons from 'bootstrap-icons/font/bootstrap-icons.json'

const props = defineProps<{
  disabled?: boolean
  active?: boolean
  icon?: keyof typeof icons | string
  type?: string
}>()
</script>

<template>
  <el-button
    :text="!active"
    class="icon-button"
    v-bind="{ ...props, type: active && type === undefined ? 'primary' : type, icon: undefined, active: undefined }"
  >
    <menu-icon :icon="props.icon!" />
  </el-button>
</template>

<style scoped lang="scss">
.icon-button {
  padding: 3px !important;
  .menu-icon {
    position: unset;
    left: unset;
    top: unset;
  }
}
</style>
