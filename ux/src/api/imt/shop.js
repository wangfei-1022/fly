import request from '@/utils/request'

// 查询门店
export function listShop(query) {
  return request({
    url: '/imt/shop/list',
    method: 'get',
    params: query
  })
}

// 查询门店
export function getListShopAllApi(query) {
  return request({
    url: '/imt/shop/list/no/page',
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

// 获取省
export function getProvinceListApi() {
  return request({
    url: '/imt/shop/province' ,
    method: 'get'
  })
}

// 获取省
export function getCityListApi(params) {
  return request({
    url: '/imt/shop/province/city' ,
    method: 'get',
    params
  })
}

// 获取省
export function getDistrictListApi(params) {
  return request({
    url: '/imt/shop/province/city/district' ,
    method: 'get',
    params
  })
}
