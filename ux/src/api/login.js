import request from '@/utils/request'

// 登录方法
export function login(data) {
  return request({
    url: '/system/user/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/system/user/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/system/user/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/system/user/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCaptchaApi() {
  return request({
    url: '/system/user/getCaptcha',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}
