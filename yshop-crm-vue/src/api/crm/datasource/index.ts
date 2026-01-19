import request from '@/config/axios'

// 数据源 VO
export interface DataSourceVO {
  id: number // ID
  sourceCode: string // 数据源编码
  sourceName: string // 数据源名称
  channel: string // 渠道
  acquireCost: number // 获客成本
  acquireTime: Date // 获客时间
  region: string // 区域
  remark: string // 备注
  status: number // 状态: 0-禁用, 1-启用
  createTime: Date // 创建时间
}

// 数据源 API
export const DataSourceApi = {
  // 查询数据源分页
  getDataSourcePage: async (params: any) => {
    return await request.get({ url: `/crm/data-source/page`, params })
  },

  // 查询数据源详情
  getDataSource: async (id: number) => {
    return await request.get({ url: `/crm/data-source/get?id=` + id })
  },

  // 新增数据源
  createDataSource: async (data: DataSourceVO) => {
    return await request.post({ url: `/crm/data-source/create`, data })
  },

  // 修改数据源
  updateDataSource: async (data: DataSourceVO) => {
    return await request.put({ url: `/crm/data-source/update`, data })
  },

  // 删除数据源
  deleteDataSource: async (id: number) => {
    return await request.delete({ url: `/crm/data-source/delete?id=` + id })
  },

  // 导出数据源 Excel
  exportDataSource: async (params) => {
    return await request.download({ url: `/crm/data-source/export-excel`, params })
  },

  // 导入 Excel
  importExcel: async (data: FormData) => {
    return await request.post({ url: `/crm/data-source/import-excel`, data })
  }
}
