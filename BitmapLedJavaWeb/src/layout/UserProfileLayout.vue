<template>
  <sub-menu :parent-router="userRoute" />
</template>

<script setup>
import SubMenu from "@/layout/submenu/index.vue";
import { userRoute } from "@/router/modules/user";
</script>
