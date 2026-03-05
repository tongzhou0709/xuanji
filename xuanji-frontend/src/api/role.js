import request from '@/utils/request'

export function listRole() {
  return request({
    url: '/role/list',
    method: 'get'
  })
}

export function saveRole(data) {
  return request({
    url: '/role',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/role',
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}

export function getRoleMenus(id) {
  return request({
    url: `/role/${id}/menus`,
    method: 'get'
  })
}

export function assignMenus(id, menuIds) {
  return request({
    url: `/role/${id}/menus`,
    method: 'post',
    data: menuIds
  })
}

export function listMenu() {
  return request({
    url: '/menu/list',
    method: 'get'
  })
}
