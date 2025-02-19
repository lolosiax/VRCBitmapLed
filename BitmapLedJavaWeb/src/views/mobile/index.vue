<script setup lang="ts">
import { HSVtoRGB } from '@/utils/color'
import { useMobileStore } from '@/store/mobile'
import { setMobileText, switchTimer } from '@/api/mobile'
import { ElMessage } from 'element-plus'

const store = useMobileStore()
const text = toRef(store, 'text')
const range = store.range
const color = store.color
const background = toRef(store, 'background')
let timer = $ref(undefined) as boolean | undefined

const displayColor = computed(() => {
  let { h, s, v } = color
  if (h == -1) {
    h = 0
    s = 0
    v = v / 15
  } else {
    h = (h / 11) * 360
    v = (v + 1) / 4
    s = (s + 1) / 4
  }
  const [r, g, b] = HSVtoRGB(h, s, v)
  const rgb = ((r << 16) | (g << 8) | b).toString(16).padStart(6, '0')
  return `#${rgb}`
})

async function doSend() {
  await setMobileText(text.value, range.start, range.end, displayColor.value, background.value)
  ElMessage.success('操作成功')
}


async function changeTimer(){
  timer = await switchTimer()
}
</script>

<template>
  <div class="mobile">
    在下侧输入文本以进行显示
    <el-input v-model="text" type="textarea" :rows="16" />
    <div class="right-float">
      <div class="right-float-box">
        <el-form size="large" label-width="auto">
          <el-form-item label="设为背景色">
            <el-switch v-model="background" />
          </el-form-item>
          <el-form-item label="色彩">
            <div class="color-view" :style="{ backgroundColor: displayColor }" />
          </el-form-item>
        </el-form>
        <el-button type="primary" size="large" @click="doSend">发送</el-button>
      </div>
    </div>
    <el-form size="large" label-width="auto">
      <el-form-item label="起始行数">
        <el-input-number v-model="range.start" :min="0" :max="15" />
      </el-form-item>
      <el-form-item label="结束行数">
        <el-input-number v-model="range.end" :min="1" :max="16" />
      </el-form-item>
      <el-form-item label="色相">
        <el-slider v-model="color.h" class="color-h" :min="-1" :max="11" />
      </el-form-item>
      <el-form-item label="明度">
        <el-slider v-model="color.v" class="color-v" :min="0" :max="color.h == -1 ? 15 : 3" />
      </el-form-item>
      <el-form-item v-if="color.h != -1" label="饱和度">
        <el-slider v-model="color.s" class="color-s" :min="0" :max="3" />
      </el-form-item>
      <el-form-item label="时钟">
        <el-button @click="changeTimer">
          <template v-if="timer === undefined">未知</template>
          <template v-else-if="timer">已打开</template>
          <template v-else>已关闭</template>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss">
.mobile {
  .el-textarea {
    font-size: 1em;
    font-family: '黑体', monospace;

    &:deep(.el-textarea__inner) {
      line-height: 1;
    }
  }

  .right-float {
    position: relative;
    z-index: 1;

    .right-float-box {
      position: absolute;
      right: 0;
      top: 0;
      width: 50dvw;

      .color-view {
        width: 25dvw;
        height: 3em;
        border: 1px solid black;
        border-radius: 0.5em;
      }

      .el-row {
        width: 100%;
      }

      .el-button {
        position: absolute;
        right: 0;
        top: 0;
      }
    }
  }

  .el-form {
    margin-right: 1em;

    :deep(.el-slider__bar) {
      --el-slider-main-bg-color: transparent;
    }

    .color-h:deep(.el-slider__runway) {
      background: linear-gradient(
        to right,
        hsl(0 0% 0%) 0%,
        hsl(0 0% 100%) 8.3%,
        hsl(0 100% 50%) 8.31%,
        hsl(45 100% 50%),
        hsl(90 100% 50%),
        hsl(135 100% 50%),
        hsl(180 100% 50%),
        hsl(225 100% 50%),
        hsl(270 100% 50%),
        hsl(315 100% 50%),
        hsl(360 100% 50%)
      );
    }

    .color-s:deep(.el-slider__runway) {
      background: linear-gradient(to right, hsl(0 0% 50%), hsl(0 100% 50%));
    }

    .color-v:deep(.el-slider__runway) {
      background: linear-gradient(to right, hsl(0 0% 0%), hsl(0 0% 100%));
    }
  }
}
</style>
