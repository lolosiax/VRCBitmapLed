import request from '@/utils/request'

export function getList(data) {
  return request({
    url: '/user/list',
    method: 'post',
    data
  })
}

export function doEdit(data) {
  return request({
    url: '/user/edit',
    method: 'post',
    data
  })
}

export function doCreate(data) {
  return request({
    url: '/user/create',
    method: 'post',
    data
  })
}

export function doDelete(data) {
  return request({
    url: '/user/delete',
    method: 'post',
    data
  })
}

// 查询学生信息
export function getStudentInfoByStudentId(data) {
  return request({
    url: '/user/getStudentInfoByStudentId',
    method: 'post',
    data
  })
}
