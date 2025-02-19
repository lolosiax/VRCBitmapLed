import { createRouter, createWebHistory } from 'vue-router'
import type { RouterTypes } from '~/basic'
import settings from '@/settings'
import { userRoute } from '@/router/modules/user'
import { mobileRoute } from '@/router/modules/mobile'

const Layout = () => import('@/layout/default/index.vue')

export function constantRoutes(): RouterTypes {
  return [
    {
      path: '/login',
      component: () => import('@/views/login/index.vue'),
      hidden: true
    },
    {
      path: '/404',
      component: () => import('@/views/error-page/404.vue'),
      hidden: true
    },
    {
      path: '/401',
      component: () => import('@/views/error-page/401.vue'),
      hidden: true
    },
    {
      path: '/500',
      component: () => import('@/views/error-page/500.vue'),
      hidden: true
    },
    {
      path: '/',
      component: Layout,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/dashboard/index.vue'),
          //using el svg icon, the elSvgIcon first when at the same time using elSvgIcon and icon
          meta: { title: 'Dashboard', icon: 'house-door', affix: true }
        }
      ]
    }
  ]
}

//角色和code数组动态路由
export function roleRoutes(): RouterTypes {
  return [mobileRoute, userRoute]
}

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export function asyncRoutes(): RouterTypes {
  return [
    // 404 page must be placed at the end !!!
    { path: '/:catchAll(.*)', name: 'CatchAll', redirect: '/404', hidden: true }
  ]
}

const router = createRouter({
  history: createWebHistory(settings.viteBasePath),
  scrollBehavior: () => ({ top: 0 }),
  routes: constantRoutes()
})

export default router
