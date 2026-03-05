import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/auth/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

export function listUser(params) {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

export function saveUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

export function getUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function updatePassword(data) {
  return request({
    url: '/user/password',
    method: 'post',
    params: data
  })
}

export function getUserRoles(userId) {
  return request({
    url: `/user/${userId}/roles`,
    method: 'get'
  })
}

export function assignUserRoles(userId, roleIds) {
  return request({
    url: `/user/${userId}/roles`,
    method: 'post',
    data: roleIds
  })
}
