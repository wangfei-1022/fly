import request from '@/utils/request'

// 查询门店
export function listShop(query) {
  return request({
    url: '/imt/shop/list',
    method: 'get',
    params: query
  })
}

// 刷新门店
export function refreshShop() {
  return request({
    url: '/imt/shop/refresh' ,
    method: 'get'
  })
}
