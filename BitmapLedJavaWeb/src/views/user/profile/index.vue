<template>
  <div class="user-profile">
    <user-detail />
  </div>
</template>

<script setup lang="ts">
import UserDetail from "@/views/user/profile/components/UserDetail.vue";
</script>

<style scoped lang="scss">
.user-profile {
  max-width: 1024px;
  margin: 0 auto;

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
      cursor: pointer;
      -webkit-user-drag: none;
      -moz-user-drag: none;
      user-drag: none;
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
      pointer-events: none;

      .bi {
        font-size: 2em;
      }
    }
  }
}
</style>
