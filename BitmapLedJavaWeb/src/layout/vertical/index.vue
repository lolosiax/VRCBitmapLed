<script setup lang="ts" name="VerticalLayout">
import AppMain from '@/layout/default/app-main/index.vue'
import Navbar from '@/layout/vertical/Navbar.vue'

</script>

<template>
  <div class="vertical-layout">
    <Navbar />
    <AppMain />
  </div>
</template>

<style scoped lang="scss">
.vertical-layout {
  height: 100dvh;
  display: grid;
  grid-template-rows: auto 1fr;

  .app-main {
    overflow: auto;
  }
}
</style>
