import type { BootstrapIcons, RouteRawConfig } from "~/basic";

export const userRoute = {
  path: '/user',
  name: 'User',
  component: () => import('@/layout/UserProfileLayout.vue'),
  meta: { title: '用户中心', icon: 'person' as BootstrapIcons },
  hidden: true,
  children: [
    {
      path: 'profile',
      name: 'Profile',
      meta: {title: '个人信息', icon: 'text-left' as BootstrapIcons},
      component: () => import('@/views/user/profile/index.vue')
    }
  ]
} as RouteRawConfig
