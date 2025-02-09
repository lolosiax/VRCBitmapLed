/*
 * 声明.d.ts文件规范
 * 导出的类型以大写开头
 * 对象：config
 * 数组：options
 * 枚举：emu
 * 函数：Fn
 * 属性：props
 * 实例：instance
 * */

/*router*/
import type { RouteRecordRaw } from 'vue-router'

import type icons from 'bootstrap-icons/font/bootstrap-icons.json'
export type BootstrapIcons = keyof typeof icons;

export interface RawConfig {
  /** 是否隐藏路由 */
  hidden: boolean
  /** TODO 不知道。 */
  alwaysShow: boolean
  /** TODO 不知道。 */
  code: number
  /** 路由名称 */
  name: string
  /** 完整路径，一般不填 */
  fullPath: string
  /** 路径，可以为相对路径 */
  path: string
  /** 元数据，没有元数据时不显示菜单 */
  meta: Omit<Partial<RawConfigMeta>, 'title'> & Required<Pick<RawConfigMeta, 'title'>>
  /** 子路由 */
  children: RouterTypes
  /** 重定向，绝对路径 */
  redirect: string
  /** 是否显示子菜单 */
  expand: boolean
}

export interface RawConfigMeta {
  /** 路由标题，显示在菜单按钮上 */
  title: string
  /** 路由图标 */
  icon: BootstrapIcons
  affix: boolean
  activeMenu: string
  breadcrumb: boolean
  roles: Array<string>
  tag: string | undefined
  code: number
  cachePage: boolean
  leaveRmCachePage: boolean
  closeTabRmCache: boolean
}

export type RouteRawConfig = RouteRecordRaw & Partial<RawConfig>
export type RouterTypes = Array<RouteRawConfig>

/*settings*/
export interface SettingsConfig {
  title: string
  sidebarLogo: boolean
  showLeftMenu: boolean
  ShowDropDown: boolean
  showHamburger: boolean
  isNeedLogin: boolean
  isNeedNprogress: boolean
  showTagsView: boolean
  tagsViewNum: number
  openProdMock: boolean
  errorLog: string | Array<string>
  permissionMode: string
  delWindowHeight: string
  tmpToken: string
  showNavbarTitle: boolean
  showTopNavbar: boolean
  mainNeedAnimation: boolean
  viteBasePath: string
  defaultLanguage: string
  defaultSize: string
  defaultTheme: string
  plateFormId: number
}

export {}
