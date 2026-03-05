import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/views/layout/index'
import ParentView from '@/components/ParentView/index'
import { getToken } from '@/utils/auth'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import store from '@/store'

Vue.use(VueRouter)

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/',
    component: () => import('@/views/layout/index'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: '首页', icon: 'el-icon-s-home', menuCode: 'dashboard' }
      }
    ]
  },
  {
    path: '/system/user/detail',
    component: () => import('@/views/system/user/detail'),
    hidden: true
  },
  {
    path: '/profile',
    component: () => import('@/views/layout/index'),
    redirect: '/profile/index',
    hidden: true,
    meta: { menuCode: 'system:profile:list' },
    children: [
      {
        path: 'index',
        name: 'Profile',
        component: () => import('@/views/profile/index'),
        meta: { title: '个人信息', hidden: true, menuCode: 'system:profile:list' }
      }
    ]
  }
]

export const asyncRoutes = [
  {
    path: '/task',
    component: Layout,
    name: 'Task',
    meta: { title: '任务管理', icon: 'el-icon-s-claim', menuCode: 'task' },
    children: [
      {
        path: 'key',
        name: 'KeyTask',
        component: ParentView,
        meta: { title: '重点任务', icon: 'el-icon-s-flag' },
        children: [
          {
            path: 'board',
            name: 'KeyTaskBoard',
            component: () => import('@/views/system/task/key/board/index'),
            meta: { title: '任务看板', icon: 'el-icon-data-board' }
          },
          {
            path: 'list',
            name: 'KeyTaskList',
            component: () => import('@/views/keyTask/index'),
            meta: { title: '任务列表', icon: 'el-icon-tickets' }
          },
          {
            path: 'import',
            name: 'KeyTaskImport',
            component: () => import('@/views/system/task/key/import/index'),
            meta: { title: '批量导入', icon: 'el-icon-upload2' }
          }
        ]
      },
      {
        path: 'project',
        name: 'ProjectWork',
        component: ParentView,
        meta: { title: '项目工作', icon: 'el-icon-s-management' },
        children: [
          {
            path: 'board',
            name: 'ProjectBoard',
            component: () => import('@/views/system/task/project/board/index'),
            meta: { title: '项目看板', icon: 'el-icon-data-analysis' }
          },
          {
            path: 'manage',
            name: 'ProjectManage',
            component: () => import('@/views/system/task/project/manage/index'),
            meta: { title: '项目管理', icon: 'el-icon-notebook-1' }
          },
          {
            path: 'pm',
            name: 'ProjectPm',
            component: () => import('@/views/system/task/project/pm/index'),
            meta: { title: '项目经理管理', icon: 'el-icon-s-custom' }
          },
          {
            path: 'maintain',
            name: 'ProjectMaintain',
            component: () => import('@/views/system/task/project/maintain/index'),
            meta: { title: '项目维护', icon: 'el-icon-set-up' }
          }
        ]
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    name: 'System',
    meta: { title: '系统管理', icon: 'el-icon-setting', menuCode: 'system' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index'),
        meta: { title: '用户管理', icon: 'el-icon-user', menuCode: 'system:user:list' }
      },
      {
        path: 'org',
        name: 'Org',
        component: () => import('@/views/system/org/index'),
        meta: { title: '机构管理', icon: 'el-icon-office-building', menuCode: 'system:org:list' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role/index'),
        meta: { title: '角色管理', icon: 'el-icon-s-custom', menuCode: 'system:role:list' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role/index'),
        meta: { title: '角色管理', icon: 'el-icon-s-custom' }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index'),
        meta: { title: '菜单管理', icon: 'el-icon-menu' }
      }
    ]
  },
  {
    path: '/notice',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Notice',
        component: () => import('@/views/system/notice/index'),
        meta: { title: '公告管理', icon: 'el-icon-bell' }
      }
    ]
  },
  {
    path: '/admin',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Admin',
        component: () => import('@/views/system/admin/index'),
        meta: { title: '后台管理', icon: 'el-icon-monitor' }
      }
    ]
  },
  {
    path: '/evaluation',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Evaluation',
        component: () => import('@/views/system/evaluation/index'),
        meta: { title: '评价管理', icon: 'el-icon-star-on' }
      }
    ]
  },
  {
    path: '/weekly',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Weekly',
        component: () => import('@/views/system/weekly/index'),
        meta: { title: '周报管理', icon: 'el-icon-notebook-2' }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new VueRouter({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/404']

router.beforeEach(async (to, from, next) => {
  NProgress.start()
  document.title = to.meta.title ? `${to.meta.title} - 事务管理系统` : '事务管理系统'

  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      next('/dashboard')
    } else {
      const hasRoutes = store.getters.routes && store.getters.routes.length > constantRoutes.length
      
      if (hasRoutes) {
        next()
      } else {
        try {
          const { permissions } = await store.dispatch('user/getInfo')
          const accessRoutes = await store.dispatch('permission/generateRoutes', permissions)
          router.addRoutes(accessRoutes)
          
          next()
        } catch (error) {
          await store.dispatch('user/resetToken')
          Message.error(error.message || '获取用户信息失败')
          next(`/login?redirect=${to.path}`)
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      Message.warning('请先登录')
      next(`/login?redirect=${to.path}`)
    }
  }
  NProgress.done()
})

router.afterEach(() => {
  NProgress.done()
})

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
