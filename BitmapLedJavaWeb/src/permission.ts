import router, { roleRoutes } from '@/router'
import { filterRouters, filterTagRouters, progressClose, progressStart, setRouterFromDatabase } from '@/hooks/use-permission'
import { useBasicStore } from '@/store/basic'
import { getMyInfo, getMyRole } from '@/api/user'
import { getMenuConfig } from '@/api/menu'
import settings from '@/settings'

//路由进入前拦截
//to:将要进入的页面 vue-router4.0 不推荐使用next()
const whiteList = ['/login', '/404', '/401', '/500'] // no redirect whitelist
router.beforeEach(async (to) => {
  progressStart()
  // document.title = langTitle(to.meta?.title) // i18 page title
  let title = (to.meta?.title ?? settings.title) as string
  if (title !== settings.title) title = `${title} - ${settings.title}`
  document.title = title
  const basicStore = useBasicStore()
  if (to.path === '/500') {
    return true
  }
  if (to.path === '/login') {
    return true
  }

  //1.判断token
  if (basicStore.token) {
    //2.判断是否获取用户信息
    if (!basicStore.getUserInfo) {
      try {
        const [userData, userRole, exp] = await Promise.all([getMyInfo(), getMyRole(), getMenuConfig()])
        //3.保存用户信息到store
        basicStore.setUserInfo({
          userInfo: userData,
          roles: [userRole.roleType],
          codes: [userRole.roleId]
        })
        //const routes = await getRouterList({ roleId: userRole.roleId })
        const routes = roleRoutes()
        filterRouters(routes, userRole.type)
        filterTagRouters(routes, exp)

        //4.动态路由权限筛选
        setRouterFromDatabase(routes)
        //5.再次执行路由跳转
        return { ...to, replace: true }
      } catch (e) {
        console.error(`route permission error${e}`)
        progressClose()
        return `/500?redirect=${to.path}`
      }
    } else {
      return true
    }
  } else {
    if (!basicStore.allRoutes.length) {
      setRouterFromDatabase(roleRoutes())
      basicStore.getUserInfo = true
      return { ...to, replace: true }
    }

    if (window) return true

    if (!whiteList.includes(to.path)) {
      return `/login?redirect=${to.path}`
    } else {
      return true
    }
  }
})
//路由进入后拦截
router.afterEach(() => {
  progressClose()
})
