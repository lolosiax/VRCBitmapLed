<template>
  <tr :class="{editing: editing && props.editable}">
    <td class="item-title">{{ props.label ?? props.prop }}</td>
    <td class="item-content">
      <div v-if="editing && props.editable" class="item-input">
        <input v-model="user[props.prop]" />
      </div>
      <template v-else>{{ user[props.prop] }}</template>
    </td>
  </tr>
</template>

<script lang="ts" setup>

import type { IUnionUserInfo } from "@/store/basic";
import type { Ref } from "vue";

const props = withDefaults(defineProps<{
  prop: keyof IUnionUserInfo,
  editable?: boolean
  label?: string
}>(), {
  editable: true
});

const user: Ref<IUnionUserInfo> = inject("userDetail:user") as any;
const editing: Ref<boolean> = inject("userDetail:editing") as any;

defineExpose({
  ok: () => true
});
</script>

<style scoped>

</style>
