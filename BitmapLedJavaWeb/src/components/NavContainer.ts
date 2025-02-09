import { useBasicStore } from '@/store/basic'
import {Comment} from "@vue/runtime-core";
import { v4 } from 'uuid'
import type {Prop, VNode} from 'vue'
import {defineComponent} from 'vue'

export default defineComponent({
  name: 'NavContainer',
  props: {
    align: {
      type: String,
      required: false,
      default: 'left'
    } as Prop<'left' | 'center' | 'right' | undefined>
  },
  data() {
    return {
      left: () => [] as VNode[],
      right: () => [] as VNode[],
      center: () => [] as VNode[],
      default: () => [] as VNode[],
      id: v4(),
      show: false
    }
  },
  watch: {
    align(value: 'left' | 'center' | 'right' = 'left', old: string) {
      if (!this.show || value === old) return;
      const noop = () => [];
      this.left = this.right = this.center = noop;
      this[value] = this.$slots['default'] ?? noop;
      useBasicStore().$patch(({ navbar }) => {
        navbar.left.set(this.id, this.left)
        navbar.right.set(this.id, this.right)
        navbar.center.set(this.id, this.center)
        navbar.cursor++
      })
    }
  },
  mounted() {
    this[this.align!] = this.$slots['default'] ?? (() => []);
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
    const { default: d } = this.$slots
    this.default = d ?? noop
  },
  methods: {
    onShowing() {
      this.show = true
      useBasicStore().$patch(({ navbar }) => {
        navbar.left.set(this.id, this.left)
        navbar.right.set(this.id, this.right)
        navbar.center.set(this.id, this.center)
        navbar.cursor++
      })
    },
    onHide() {
      this.show = false
      useBasicStore().$patch(({ navbar }) => {
        navbar.left.delete(this.id)
        navbar.right.delete(this.id)
        navbar.center.delete(this.id)
        navbar.cursor++
      })
    }
  },
  render(){
    return h(Comment, "nav container");
  }
})
