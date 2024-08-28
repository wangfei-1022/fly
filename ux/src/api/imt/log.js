import request from '@/utils/request'

// 查询
export function getLogListApi(query) {
  return request({
    url: '/imt/log/list',
    method: 'get',
    params: query
  })
}

// 查询
export function deleteLogApi(query) {
  return request({
    url: '/imt/log/list',
    method: 'get',
    params: query
  })
}

// 查询
export function cleanLogApi(query) {
  return request({
    url: '/imt/log/list',
    method: 'get',
    params: query
  })
}
