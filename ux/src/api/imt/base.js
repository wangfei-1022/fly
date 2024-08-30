import request from '@/utils/request'

// 预约类型
export function getAppointmentTypeApi() {
  return request({
    url: '/imt/shop/appointment/type' ,
    method: 'get'
  })
}

// 预约时间类型
export function getAppointmentTimeTypeApi() {
  return request({
    url: '/imt/shop/appointment/time/type' ,
    method: 'get'
  })
}
