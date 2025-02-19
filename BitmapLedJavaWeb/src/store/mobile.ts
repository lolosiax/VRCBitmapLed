import { defineStore } from 'pinia'

export const useMobileStore = defineStore('mobile', {
  state() {
    return {
      color: {
        h: -1,
        s: 0,
        v: 15
      },
      range: {
        start: 0,
        end: 16
      },
      text: '',
      background: false,
    }
  },
  persist: {
    storage: localStorage,
    paths: undefined
  }
})
