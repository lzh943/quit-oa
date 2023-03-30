import request from '@/utils/request'

const api_name = '/admin/process/processType'

export default {
  findAll() {
    return request({
      url: `${api_name}/findAll`,
      method: 'get'
    })
  },
  getPageList(pageNum, pageSize) {
    return request({
      url: `${api_name}/${pageNum}/${pageSize}`,
      method: 'get'
    })
  },
  getById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })
  },
  save(role) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: role
    })
  },
  updateById(role) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: role
    })
  },
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  }
}
