import request from '@/utils/request'

// 查询i茅台商品列表
export function getItemListApi(query) {
  return request({
    url: '/imt/item/list',
    method: 'get',
    params: query
  })
}

// 删除i茅台商品
export function refreshItemApi() {
  return request({
    url: '/imt/item/refresh' ,
    method: 'get'
  })
}
