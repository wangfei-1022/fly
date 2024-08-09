import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/login/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/login/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/dashboard/index'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  }
]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
  {
    path: '/system/user',
    component: Layout,
    meta: { title: '基础配置', icon: 'search', affix: true },
    children: [
      {
        path: 'user/list',
        component: () => import('@/views/system/user/index'),
        name: 'AuthRole',
        meta: { title: '用户管理', activeMenu: '/system/user' }
      },
      {
        path: 'role/user',
        component: () => import('@/views/system/role/index'),
        name: 'AuthRole',
        meta: { title: '角色管理', activeMenu: '/system/user' }
      },
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/config/index'),
        name: 'AuthRole',
        meta: { title: '手机维护', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/xiaomi',
    component: Layout,
    meta: { title: '小米', icon: 'github', affix: true },
    children: [
      {
        path: 'xiaomi/user',
        component: () => import('@/views/xiaomi/user/index'),
        name: 'AuthRole',
        meta: { title: '用户列表', activeMenu: '/system/user' }
      },
      {
        path: 'xiaomi/:userId(\\d+)',
        component: () => import('@/views/xiaomi/config/index'),
        name: 'AuthRole',
        meta: { title: '小米预约', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/imaotai',
    component: Layout,
    meta: { title: 'I茅台', icon: 'question', affix: true },
    children: [
      {
        path: 'user/list',
        component: () => import('@/views/imaotai/user/index'),
        name: 'AuthRole',
        meta: { title: '用户列表', activeMenu: '/system/user' }
      },
      {
        path: 'appointment/list',
        component: () => import('@/views/imaotai/shop/index'),
        name: 'IMaoTaiAppointmentProjectList',
        meta: { title: '预约项目', activeMenu: '/system/user' }
      },
      {
        path: 'shop/list',
        component: () => import('@/views/imaotai/shop/index'),
        name: 'IMaoTaiShopList',
        meta: { title: '门店列表', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/log',
    component: Layout,
    meta: { title: '日志管理', icon: 'fullscreen', affix: true },
    children: [
      {
        path: 'imaotai/user',
        component: () => import('@/views/imaotai/user/index'),
        name: 'AuthRole',
        meta: { title: '登录日志', activeMenu: '/system/user' }
      },
      {
        path: 'imaotai/pro',
        component: () => import('@/views/imaotai/user/index'),
        name: 'AuthRole',
        meta: { title: '操作日志', activeMenu: '/system/user' }
      }
    ]
  },
]

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
