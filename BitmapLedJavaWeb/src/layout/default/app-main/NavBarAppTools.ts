import { Fragment } from '@vue/runtime-core'
import type { Prop, VNode } from 'vue'
import { Comment, defineComponent } from 'vue'

export default defineComponent({
  name: 'NavBarAppTools',
  props: {
    position: {
      type: String,
      require: true
    } as Prop<'left' | 'right' | 'center'>
  },
  render() {
    const navbar = useBasicStore().navbar
    const sorts = navbar[this.position!] as Map<string, () => VNode[]>
    if (!sorts) return h(Comment, 'nav bar app tools')

    let out = [] as VNode[]
    for (const value of sorts.values()) {
      try {
        out.push(...value())
      } catch (e) {
        console.error(e)
      }
    }
    if (this.$props.position === 'right') out = out.reverse()
    return h(Fragment, out)
  }
})
