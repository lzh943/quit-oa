import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/admin/system/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/admin/system/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/admin/system/logout',
    method: 'post'
  })
}
