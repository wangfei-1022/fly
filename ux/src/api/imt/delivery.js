import request from '@/utils/request'

// 查询在各省各市的投放量查询
export function getDeliverySearchApi(query) {
  return request({
    url: '/imt/shop/delivery/search',
    method: 'get',
    params: query
  })
}
