import { baseUrl } from '@/utils/request'
import type { VNode } from 'vue'
import { nextTick } from 'vue'
import { defineStore } from 'pinia'
import type { RouterTypes } from '~/basic'
import defaultSettings from '@/settings'
import router, { constantRoutes } from '@/router'
import type { IUser } from '@/api/user'
import userImage from '@/assets/layout/user.png'

export const useBasicStore = defineStore('basic', {
  state: () => {
    return {
      token: '',
      getUserInfo: false,
      keepSession: true, // 如果需要丢失登录，将它设置为false。
      online: true,
      userInfo: {
        userName: '',
        realName: '',
        avatar: '',
        phone: '',
        id: '',
        isUse: true
      }, //user info
      //router
      allRoutes: [] as RouterTypes,
      buttonCodes: [],
      filterAsyncRoutes: [],
      roles: [] as Array<string>,
      codes: [] as Array<number>,
      //keep-alive
      cachedViews: [] as Array<string>,
      cachedViewsDeep: [] as Array<string>,
      //other
      sidebar: { opened: true },
      //axios req collection
      axiosPromiseArr: [] as Array<ObjKeys>,
      settings: defaultSettings,
      noMainPadding: false,
      navbar: {
        cursor: 0,
        left: new Map<string, () => VNode[]>(),
        right: new Map<string, () => VNode[]>(),
        center: new Map<string, () => VNode[]>()
      }
    }
  },
  persist: [
    {
      storage: localStorage,
      paths: ['token']
    },
    {
      storage: sessionStorage,
      paths: ['keepSession']
    }
  ],
  getters: {
    avatar(): string {
      if (this.userInfo?.avatar) {
        return `${baseUrl}user/avatar/get/${this.userInfo.id}`
      } else return userImage
    },
    isAdmin(): boolean {
      return this.roles.some((role) => role.includes('admin'))
    },
    isSuperAdmin(): boolean {
      return this.roles.some((role) => role == 'super_admin')
    },
    isUser(): boolean {
      return this.roles.some((role) => role.includes('user'))
    }
  },
  actions: {
    setToken(data) {
      this.token = data
    },
    setFilterAsyncRoutes(routes) {
      this.$patch((state) => {
        state.filterAsyncRoutes = routes
        state.allRoutes = constantRoutes().concat(routes)
      })
    },
    setUserInfo({ userInfo, roles, codes }) {
      this.$patch((state) => {
        state.roles = roles
        state.codes = codes
        state.getUserInfo = true
        state.userInfo = userInfo
      })
    },
    resetState() {
      this.$patch((state) => {
        state.token = '' //reset token
        state.roles = []
        state.codes = []
        //reset router
        state.allRoutes = []
        state.buttonCodes = []
        state.filterAsyncRoutes = []
        //reset userInfo
        state.userInfo = {
          userName: '',
          realName: '',
          avatar: '',
          phone: '',
          id: '',
          isUse: true
        }
      })
      this.getUserInfo = false
    },
    resetStateAndToLogin() {
      this.resetState()
      nextTick(() => {
        router.push({ path: '/login' })
      })
    },
    setSidebarOpen(data) {
      this.$patch((state) => {
        state.sidebar.opened = data
      })
    },
    setToggleSideBar() {
      this.$patch((state) => {
        state.sidebar.opened = !state.sidebar.opened
      })
    },

    /*keepAlive缓存*/
    addCachedView(view) {
      this.$patch((state) => {
        if (state.cachedViews.includes(view)) return
        state.cachedViews.push(view)
      })
    },

    delCachedView(view) {
      this.$patch((state) => {
        const index = state.cachedViews.indexOf(view)
        index > -1 && state.cachedViews.splice(index, 1)
      })
    },
    /*third  keepAlive*/
    addCachedViewDeep(view) {
      this.$patch((state) => {
        if (state.cachedViewsDeep.includes(view)) return
        state.cachedViewsDeep.push(view)
      })
    },
    setCacheViewDeep(view) {
      this.$patch((state) => {
        const index = state.cachedViewsDeep.indexOf(view)
        index > -1 && state.cachedViewsDeep.splice(index, 1)
      })
    }
  }
})

export interface IUserInfo {
  userName: string
  realName: string
  avatar: string
  phone: string
  email: string
  id: string
  isUse: boolean
  team: string
  iGameKey: string
}

export interface IUnionUserInfo extends IUserInfo, IUser {}
