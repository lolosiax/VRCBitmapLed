import type { BootstrapIcons, RouteRawConfig } from "~/basic";

export const mobileRoute = {
  path: '/mobile',
  name: 'Mobile',
  component: () => import('@/layout/MobileLayout.vue'),
  meta: { title: '移动端', icon: 'phone' as BootstrapIcons },
  redirect: '/mobile/index',
  hidden: true,
  children: [
    {
      path: 'index',
      name: 'MobileIndex',
      meta: {title: '移动端主面板', icon: 'phone' as BootstrapIcons},
      component: () => import('@/views/mobile/index.vue')
    }
  ]
} as RouteRawConfig
