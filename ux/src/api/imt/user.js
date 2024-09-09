import request from '@/utils/request'

// 查询i茅台用户列表
export function getUserListApi(params) {
  return request({
    url: '/imt/user/list',
    method: 'get',
    params
  })
}

// 登录
export function userLoginApi(data) {
  return request({
    url: '/imt/user/login',
    method: 'post',
    data
  })
}

export function updateUserApi(data) {

  return request({
    url: '/imt/user/update',
    method: 'post',
    data
  })
}

// 根据手机号查询
export function getUserByMobileApi(params) {
  return request({
    url: '/imt/user/getUserByMobile',
    method: 'get',
    params
  })
}

// 发送验证码
export function sendCodeApi(data) {
  return request({
    url: '/imt/user/sendCode',
    method: 'post',
    data
  })
}

// 预约
export function userReservationApi(data) {
  return request({
    url: '/imt/user/reservation',
    method: 'post',
    data
  })
}

// 预约
export function travelRewardApi(data) {
  return request({
    url: '/imt/user/travelReward',
    method: 'post',
    data
  })
}

export function deleteUserApi(data) {
  return request({
    url: '/imt/user/delete',
    method: 'post',
    data
  })
}

