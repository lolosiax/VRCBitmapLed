import request from '@/utils/request'

export function getList(data) {
  return request({
    url: '/role/queryRoleByPage',
    method: 'post',
    data
  })
}

export function doAdd(data) {
  return request({
    url: '/role/create',
    method: 'post',
    data
  })
}
export function doUpdate(data) {
  return request({
    url: '/role/update',
    method: 'post',
    data
  })
}

export function doDelete(data) {
  return request({
    url: '/role/destroy',
    method: 'post',
    data
  })
}
/**
 * 获取角色列表
 * @return {Promise<AxiosResponse<{id: number, type:string, roleName: string}[]>>}
 */
export function getRoleList() {
  return request({
    url: '/role/list',
    method: 'post'
  })
}

/**
 * 根据用户ID获取角色ID
 */
export function getRoleByUserId(userId) {
  return request({
    url: '/userRole/getByUserId',
    method: 'post',
    data: {
      userId
    }
  })
}
