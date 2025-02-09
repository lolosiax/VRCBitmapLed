<template>
  <el-select-v2
    :model-value="modelValue" :options="icons" filterable placeholder="请选择菜单图标"
    :height="500" @update:model-value="v => $emit('update:modelValue', v)">
    <template #default="{item}">
      <div class="icon-frame">
        <i :class="'icon bi bi-' + item.value" />
        <span>{{ item.value }}</span>
      </div>
    </template>
  </el-select-v2>
</template>

<script>
import { allIcons } from "@/utils/common-util";

export default {
  name: "IconSelector",
  props: {
    modelValue: { type: String, default: "" }
  },
  emits: ["update:modelValue"],
  data() {
    return {
      icons: allIcons().map(it => ({ value: it, label: it }))
    };
  }
};
</script>

<style scoped lang="scss">

.icon-frame {
  display: flex;
  align-items: center;

  .icon {
    font-size: 1.5em;
    margin-right: 1em;
  }
}
</style>
