import request from '@/utils/request'

// 查询i茅台用户列表
export function getUserListApi(query) {
  return request({
    url: '/imaotai/user/list',
    method: 'get',
    params: query
  })
}
