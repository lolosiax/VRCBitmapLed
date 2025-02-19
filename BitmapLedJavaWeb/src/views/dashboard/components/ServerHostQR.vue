<script setup lang="ts">
import type { IServerHost } from '@/api/mobile'
import { getServerHost } from '@/api/mobile'
import QRCode from 'qrcode'
import settings from '@/settings'

let address = $ref([]) as IServerHost[]

let activeAddress = $ref() as IServerHost | undefined
const shift = keyRef('Shift')
let qrCode = $ref() as string | undefined
const store = useBasicStore()

onMounted(async () => {
  address = await getServerHost()
  activeAddress = address[0]
})

watch(
  () => activeAddress?.address,
  async (it) => {
    if (!it) return ''
    if (import.meta.env.VITE_APP_ENV == "dev"){
      it = it.replace(/\d+$/, location.port.toString())
    }
    it = `http://${it}${settings.viteBasePath}mobile?token=${store.token}`
    qrCode = await QRCode.toDataURL(it, { width: 300, margin: 0 })
  }
)

function displayName(it: IServerHost) {
  if (shift.value) return it.address
  const name = it.address
  if (name.includes(']')) {
    return name.slice(1).split(']')[0]
  }
  return name.split(':')[0]
}
</script>

<template>
  <div class="server-host-qr">
    <img v-if="qrCode" :src="qrCode" alt="二维码" />
    <el-select v-model="activeAddress" value-key="address">
      <el-option v-for="it in address" :key="it.address" :label="displayName(it)" :value="it">
        {{ displayName(it) }}
        <br />
        <small>{{ it.interfaceName }}</small>
      </el-option>
    </el-select>
    <div class="tips">使用手机浏览器扫描二维码进行远程操作</div>
  </div>
</template>

<style scoped lang="scss">
.server-host-qr {
  padding: 2em;

  img {
    width: 100%;
  }

  .tips {
    color: gray;
    font-size: 0.9em;
    text-align: center;
  }
}
</style>
