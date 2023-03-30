import request from '@/utils/request'

const api_name = '/admin/process'

export default {

  getPageList(pageNum, pageSize, searchObj) {
    return request({
      url: `${api_name}/${pageNum}/${pageSize}`,
      method: 'get',
      params: searchObj // url查询字符串或表单键值对
    })
  }
}
