import request from '@/utils/request'

export function listOrg(params) {
  return request({
    url: '/org/list',
    method: 'get',
    params
  })
}

export function saveOrg(data) {
  return request({
    url: '/org',
    method: 'post',
    data
  })
}

export function updateOrg(data) {
  return request({
    url: '/org',
    method: 'put',
    data
  })
}

export function deleteOrg(id) {
  return request({
    url: `/org/${id}`,
    method: 'delete'
  })
}
