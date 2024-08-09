import request from '@/utils/request'

// 查询i茅台商品列表
export function listShop(query) {
  return request({
    url: '/imaotai/shop/list',
    method: 'get',
    params: query
  })
}

// 删除i茅台商品
export function refreshShop() {
  return request({
    url: '/imaotai/shop/refresh' ,
    method: 'get'
  })
}
