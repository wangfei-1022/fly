import request from '@/utils/request'

// 版本情况
export function getVersion() {
  return request({
    url: '/user/version',
    method: 'get',
  })
}
