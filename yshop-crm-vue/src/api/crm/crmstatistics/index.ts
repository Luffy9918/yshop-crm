import request from '@/config/axios'

// 统计总览 VO
export interface StatisticsOverviewVO {
  totalCustomers: number // 总客户数
  newCustomersToday: number // 今日新增客户
  followingCustomers: number // 跟进中客户数
  dealsThisMonth: number // 本月成交数
  conversionRate: number // 转化率(%)
}

// 客户趋势 VO
export interface CustomerTrendVO {
  date: string // 日期
  newCustomerCount: number // 新增客户数
  dealCount: number // 成交客户数
}

// 状态分布 VO
export interface StatusDistributeVO {
  status: string // 状态名称
  statusCode: string // 状态编码
  count: number // 客户数量
  percent: number // 百分比(%)
}

// 数据源ROI VO
export interface DataSourceRoiVO {
  dataSourceId: number // 数据源ID
  dataSourceName: string // 数据源名称
  customerCount: number // 客户数量
  dealCount: number // 成交数量
  dealAmount: number // 成交金额
  acquireCost: number // 获客成本
  roi: number // ROI(倍)
}

// 统计 API
export const StatisticsApi = {
  // 获取统计总览
  getOverview: async () => {
    return await request.get({ url: `/crm/statistics/overview` })
  },

  // 获取客户趋势
  getCustomerTrend: async (params: any) => {
    return await request.get({ url: `/crm/statistics/customer-trend`, params })
  },

  // 获取状态分布
  getStatusDistribute: async () => {
    return await request.get({ url: `/crm/statistics/status-distribute` })
  },

  // 获取数据源ROI
  getDataSourceRoi: async () => {
    return await request.get({ url: `/crm/statistics/data-source-roi` })
  }
}