import { defineStore } from 'pinia'

export const useDebuggerStore = defineStore('debugger', {
  state() {
    return {
      enable: import.meta.env.VITE_APP_ENV !== 'prod',
      ui: {
        showLayoutGrid: false,
        displayAllError: false
      },
      basic: {
        saveSession: true
      }
    }
  },
  persist: {
    storage: localStorage,
    paths: undefined
  }
})
