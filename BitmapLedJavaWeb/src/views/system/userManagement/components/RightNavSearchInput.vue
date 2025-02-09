<template>
  <el-input v-model.trim="systemStore.userMng.searchKey" placeholder="按下回车键搜索" />
</template>

<script lang="ts" setup>
import { useSystemStore } from "@/store/system";

const systemStore = useSystemStore();
</script>

<style scoped>

</style>
