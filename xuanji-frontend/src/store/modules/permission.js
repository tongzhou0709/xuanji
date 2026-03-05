import { asyncRoutes, constantRoutes } from '@/router'

function hasPermission(permissions, route) {
  if (!route.meta || (!route.meta.menuCode && !route.meta.permission)) {
    return true
  }
  if (route.meta && route.meta.hidden) {
    if (route.path.includes('/system/user/detail') || route.path.includes('/profile')) {
      return true
    }
    return false
  }
  if (route.meta && route.meta.permission) {
    return permissions.some(permission => route.meta.permission.includes(permission))
  }
  if (route.meta && route.meta.menuCode) {
    return permissions.includes(route.meta.menuCode) || permissions.includes('*:*:*')
  }
  if (permissions.includes('*:*:*')) {
    return true
  }
  return false
}

function filterHiddenRoutes(routes) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (tmp.meta && tmp.meta.hidden) {
      return
    }
    if (tmp.children) {
      tmp.children = filterHiddenRoutes(tmp.children)
    }
    res.push(tmp)
  })
  return res
}

export function filterAsyncRoutes(routes, permissions) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(permissions, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, permissions)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, permissions) {
    return new Promise(resolve => {
      let accessedRoutes
      if (permissions.includes('*:*:*')) {
        accessedRoutes = asyncRoutes || []
      } else {
        accessedRoutes = filterAsyncRoutes(asyncRoutes, permissions)
      }
      accessedRoutes = filterHiddenRoutes(accessedRoutes)
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
