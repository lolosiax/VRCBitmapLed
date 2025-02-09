<template>
  <tr :class="{editing}">
    <td class="item-title">头像</td>
    <td class="item-content">
      <div v-if="!editing" class="avatar-frame">
        <img :src="user.avatar || UserImage" class="avatar" alt="用户头像" />
      </div>
      <div v-else class="avatar-frame">
        <img :src="user.avatar || UserImage" class="avatar" alt="用户头像" />
        <div class="avatar-tip" @click="showAvatarEditor = true">
          <i class="bi bi-pen-fill" />
          <span>修改头像</span>
        </div>
      </div>
      <avatar-edit v-model="showAvatarEditor" />
    </td>
  </tr>
</template>

<script lang="ts" setup>
import UserImage from "@/assets/layout/user.png";
import type { IUnionUserInfo } from "@/store/basic";
import type { Ref } from "vue";
import AvatarEdit from "@/views/user/profile/components/AvatarEditor.vue";

const showAvatarEditor = ref(false);
const user: Ref<IUnionUserInfo> = inject("userDetail:user") as any;
const editing: Ref<boolean> = inject("userDetail:editing") as any;

const userInfo = toRef(useBasicStore(), "userInfo");

watch(() => userInfo.value.avatar, it => {
  user.value.avatar = it || "";
});

defineExpose({
  ok: () => true
});
</script>

<style scoped lang="scss">

.avatar-frame {
  position: relative;
  height: 96px;
  width: 96px;
  overflow: hidden;
  border-radius: 24%;

  &:not(:hover) .avatar-tip {
    display: none;
  }

  .avatar {
    height: 96px;
    width: 96px;
    pointer-events: none;
  }

  .avatar-tip {
    position: absolute;
    left: 0;
    top: 0;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: #88888888;
    color: white;
    user-select: none;
    cursor: pointer;

    .bi {
      font-size: 2em;
    }
  }
}
</style>
