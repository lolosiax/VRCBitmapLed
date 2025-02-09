<template>
  <div class="debugger" title="调试器">
    <el-button v-if="store.enable" class="btn" type="primary" link @click="data.dialog = true">
      <menu-icon icon="bug-fill" />
    </el-button>
    <el-dialog v-if="data.dialog" v-model="data.dialog" :append-to-body="true" title="调试工具" :width="600">
      <div class="debugger-items">
        <p>调试</p>
        <div class="switch-item">
          <p>显示调试工具图标</p>
          <el-switch v-model="store.enable" />
        </div>
      </div>
      <div class="debugger-items">
        <p>UI</p>
        <div class="switch-item">
          <p>显示页面布局网格</p>
          <el-switch v-model="ui.showLayoutGrid" />
        </div>
        <div class="switch-item">
          <p>展示全部系统异常</p>
          <el-switch v-model="ui.displayAllError" />
        </div>
      </div>
      <div class="debugger-items">
        <p>系统状态</p>
        <div class="switch-item">
          <p>重启后保留用户凭据</p>
          <el-switch v-model="basic.saveSession" />
        </div>
      </div>
    </el-dialog>
    <div v-if="ui.showLayoutGrid" v-html="data.gridHtml" />
  </div>
</template>

<script setup lang="ts">
import MenuIcon from '@/components/MenuIcon.vue'
import { useDebuggerStore } from '@/store/debuger'
import bus from '@/utils/bus'
import { keyRef } from '@/hooks/use-keyboard'

const store = useDebuggerStore()
const { ui, basic } = storeToRefs(store)
const ctrl = keyRef('Control')
const alt = keyRef('Alt')
const hot = keyRef('Numpad0')

const data = reactive({
  dialog: false,
  gridHtml: `<style>*{ box-shadow: 0 0 1px 0 blue }</style>`
})

function onDebuggerShow() {
  data.dialog = true
}

onMounted(() => {
  bus.on('debugger:show', onDebuggerShow)
})

onUnmounted(() => {
  bus.off('debugger:show', onDebuggerShow)
})

// 快速填写答案
watch(
  [alt, ctrl, hot],
  ([a, b, c]) => {
    if (a && b && c) {
      onDebuggerShow()
    }
  },
  { deep: true }
)
</script>

<style scoped lang="scss">
.debugger {
  margin-left: 0 !important;

  .btn {
    margin-left: 1em;
    padding: 0.3em !important;
  }
}

.debugger-items {
  padding: 0 2em 0 2em;
  position: relative;

  > p {
    position: relative;
    left: -0.5em;
    margin: 0.5em 0 0.5em 0;
    color: gray;
  }

  > hr {
    margin: 1em 0 0 0;
  }

  .switch-item {
    display: flex;
    align-items: center;
    color: black;

    > p {
      display: block;
      flex-grow: 1;
      margin: 0;
      line-height: 2em;
      font-size: 1.1em;
    }

    .el-switch {
      position: relative;
      transform: scale(1.4);
      right: 2em;
    }
  }
}
</style>
