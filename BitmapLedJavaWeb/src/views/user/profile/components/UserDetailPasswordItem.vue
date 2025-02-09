<template>
  <tr v-if="editing" :class="{editing}">
    <td class="item-title">密码</td>
    <td class="item-content">
      <div class="item-input">
        <input v-model="passwords.a" type="password" placeholder="若要修改密码，请在此处输入" />
      </div>
    </td>
  </tr>

  <tr v-if="editing && passwords.a" :class="{editing}">
    <td class="item-title">再输入一遍密码</td>
    <td class="item-content">
      <div class="item-input" :class="{error}">
        <input v-model="passwords.b" type="password" />
      </div>
      <span v-if="error" class="tip">两次输入的密码不一致</span>
    </td>
  </tr>
</template>

<script lang="ts" setup>

import type { IUnionUserInfo } from "@/store/basic";
import type { Ref } from "vue";

const user: Ref<IUnionUserInfo> = inject("userDetail:user") as any;
const editing: Ref<boolean> = inject("userDetail:editing") as any;

const passwords = reactive({
  a: "",
  b: ""
});

const error = computed(() => {
  return passwords.a && passwords.b && (passwords.a !== passwords.b);
});

defineExpose({
  ok() {
    if (passwords.a) {
      if (passwords.a != passwords.b) {
        throw new Error("两次输入的密码不一致");
      }
      user.value.password = passwords.a;
      return true;
    }
    return true;
  }
});
</script>

<style scoped>
.item-content {
  .error::after {
    background-color: red;
  }

  .tip {
    font-size: 0.8em;
    bottom: 3px;
    color: red;
  //text-shadow: 1px 1px 1px darkred;
  }
}
</style>
