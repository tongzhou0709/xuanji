import request from '@/utils/request'

export function listMenu() {
  return request({
    url: '/menu/list',
    method: 'get'
  })
}

export function saveMenu(data) {
  return request({
    url: '/menu',
    method: 'post',
    data
  })
}

export function updateMenu(data) {
  return request({
    url: '/menu',
    method: 'put',
    data
  })
}

export function deleteMenu(id) {
  return request({
    url: `/menu/${id}`,
    method: 'delete'
  })
}
