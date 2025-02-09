//获取用户信息
import request, { post } from '@/utils/request'
import type { BasicModel } from '~/model'

export interface IUser extends BasicModel {
  id: string
  userName: string
  realName: string
  password: string
  phone: string
  isUse: boolean
  legacyId: number
  database: string
  Authorization: string
  avatar: string
}

export interface IUserRole extends BasicModel {
  id: number
  userId: string
  roleId: number
  roleName: string
  roleType: string
  type: string
}

export async function getMyInfo(): Promise<IUser> {
  return post<IUser>('/user/myInfo')
}

export async function getMyRole(): Promise<IUserRole> {
  return post('/user/myRole')
}

//登录
export async function login(data): Promise<IUser> {
  return post('/login', data)
}

//退出登录
export async function logout(): Promise<void> {
  return post('/logout')
}

export async function captcha(): Promise<{ image: string; id: number }> {
  return post('/captcha')
}

export async function verify(id: number, code: string, userName: string, email: string) {
  return post('/verify', { id, code, userName, email })
}

export async function register(userName: string, email: string, code: string, password: string) {
  return post('/register', { code, userName, email, password })
}

export async function getUser(idList): Promise<IUser[]> {
  return post('/user/get', { idList })
}

export async function editUser(data: IUser) {
  return post('/user/edit', data)
}

/**
 * 按照文本搜索前十条用户
 * @param keys
 * @return {Promise<any[]>}
 */
export function userSearching(keys) {
  return post('/user/searching', { keys })
}

//修改密码
export function updatePassword(data: { id?; origin; target; session? }) {
  return post('/user/updatePassword', data)
}

//修改密码
export function setAvatar(id: string, avatar: Blob) {
  const form = new FormData()
  form.append('file', avatar, avatar instanceof File ? avatar.name : `${id}.jpg`)
  return request({
    url: `/user/avatar`,
    params: { id },
    method: 'put',
    data: form
  }) as Promise<any>
}

//修改密码
export function getAvatar(id: string): Promise<Blob> {
  return request({
    url: `/user/avatar?id=${id}`,
    responseType: 'blob',
    headers: {
      'Cache-Control': 'no-cache'
    }
  }) as any
}
