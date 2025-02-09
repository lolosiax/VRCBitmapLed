import type { Ref } from 'vue'

const keyCache: { [P: string]: number } = {}
const keyStatus: { [P: string]: boolean } = reactive({})

document.addEventListener('keydown', (ev) => {
  keyCache[ev.key] = keyCache[ev.code] = Date.now()
  if (!keyStatus[ev.key]) {
    keyStatus[ev.key] = keyStatus[ev.code] = true
  }
})

document.addEventListener('keyup', (ev) => {
  keyCache[ev.key] = keyCache[ev.code] = -1
  keyStatus[ev.key] = keyStatus[ev.code] = false
})

const INTERVAL_TIME = 500
setInterval(() => {
  const now = Date.now()
  for (const [k, time] of Object.entries(keyCache)) {
    if (now - time > INTERVAL_TIME) {
      delete keyCache[k]
      keyStatus[k] = false
    }
  }
}, INTERVAL_TIME)

export function useKeyboard(): { [P: string]: Ref<boolean> } {
  return new Proxy(
    {},
    {
      get(_: {}, p: string): any {
        return keyRef(p)
      }
    }
  )
}

type Key = 'Shift' | 'Alt' | 'Control' | 'Escape' | 'Enter' | string

export function keyRef(key: Key): Ref<boolean> {
  if (key! in keyStatus) keyStatus[key] = false
  return toRef(keyStatus, key)
}
