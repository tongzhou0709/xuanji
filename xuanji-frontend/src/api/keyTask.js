import request from '@/utils/request'

// 分页查询重点任务列表
export function getKeyTaskPage(params) {
  return request({
    url: '/keyTask/page',
    method: 'get',
    params
  })
}

// 根据ID查询重点任务详情
export function getKeyTaskById(id) {
  return request({
    url: `/keyTask/${id}`,
    method: 'get'
  })
}

// 创建重点任务
export function createKeyTask(data) {
  return request({
    url: '/keyTask',
    method: 'post',
    data
  })
}

// 更新重点任务
export function updateKeyTask(data) {
  return request({
    url: '/keyTask',
    method: 'put',
    data
  })
}

// 删除重点任务
export function deleteKeyTask(id) {
  return request({
    url: `/keyTask/${id}`,
    method: 'delete'
  })
}

// 获取所有牵头部门
export function getAllLeadDepartments() {
  return request({
    url: '/keyTask/departments',
    method: 'get'
  })
}

// 获取所有牵头人
export function getAllLeadPersons() {
  return request({
    url: '/keyTask/persons',
    method: 'get'
  })
}