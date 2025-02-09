import { defineStore } from 'pinia'

export const userRouteStore = defineStore('route', {
  state() {
    return {
      routes: [],
      accessRoutes: [],
      partialRoutes: []
    }
  },
  getters: {
    routes: (state) => state.routes,
    accessRoutes: (state) => state.accessRoutes,
    partialRoutes: (state) => state.partialRoutes
  },
  actions: {
    setRoutes() {}
  }
})
