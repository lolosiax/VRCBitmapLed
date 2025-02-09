import request, { post } from '@/utils/request'

export function getTree(data) {
  return post('/menu/list', data)
}

export function doAdd(data) {
  return request({
    url: '/menu/create',
    method: 'post',
    data
  })
}
export function doUpdate(data) {
  return request({
    url: '/menu/update',
    method: 'post',
    data
  })
}

export function doDelete(data) {
  return request({
    url: '/menu/destroy',
    method: 'post',
    data
  })
}
