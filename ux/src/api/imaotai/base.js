import request from '@/utils/request'

// 预约类型
export function getAppointmentTypeApi() {
  return request({
    url: '/imt/shop/appointment/type' ,
    method: 'get'
  })
}
