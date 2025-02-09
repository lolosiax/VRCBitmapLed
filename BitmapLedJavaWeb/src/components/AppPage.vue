<template>
  <div class="app-page">
    <slot />
    <slot v-if="false" name="left" />
    <slot v-if="false" name="center" />
    <slot v-if="false" name="right" />
  </div>
</template>

<script lang="ts">
import type { VNode } from 'vue'
import { useBasicStore } from '@/store/basic'
import { v4 } from 'uuid'
export default defineComponent({
  name: 'AppPage',
  data() {
    return {
      left: () => [] as VNode[],
      right: () => [] as VNode[],
      center: () => [] as VNode[],
      default: () => [] as VNode[],
      id: v4()
    }
  },
  mounted() {
    this.onShowing()
  },
  activated() {
    this.onShowing()
  },
  beforeUnmount() {
    this.onHide()
  },
  deactivated() {
    this.onHide()
  },
  created() {
    const noop = () => []
    const { left, right, center, default: d } = this.$slots
    this.left = left ?? noop
    this.right = right ?? noop
    this.center = center ?? noop
    this.default = d ?? noop
  },
  methods: {
    onShowing() {
      useBasicStore().$patch(({ navbar }) => {
        navbar.left.set(this.id, this.left)
        navbar.right.set(this.id, this.right)
        navbar.center.set(this.id, this.center)
        navbar.cursor++
      })
    },
    onHide() {
      useBasicStore().$patch(({ navbar }) => {
        navbar.left.delete(this.id)
        navbar.right.delete(this.id)
        navbar.center.delete(this.id)
        navbar.cursor++
      })
    }
  }
})
</script>
