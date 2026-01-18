import request from '@/config/axios'

// 客户分配 Request VO
export interface CustomerAssignReqVO {
  customerId: number // 客户ID
  staffId: number // 业务员ID
  reason?: string // 分配原因
}

// 批量客户分配 Request VO
export interface CustomerBatchAssignReqVO {
  customerIds: number[] // 客户ID列表
  staffId: number // 业务员ID
  reason?: string // 分配原因
}

// 自动分配 Request VO
export interface CustomerAutoAssignReqVO {
  customerId?: number // 客户ID（可选，用于单个客户自动分配）
  conditionType: string // 条件类型（如：city, level, industry等）
  conditionValue: string // 条件值
}

// 客户分配 API
export const CustomerAssignApi = {
  // 手动分配客户
  assign: async (data: CustomerAssignReqVO) => {
    return await request.post({ url: `/crm/customer-assign/assign`, data })
  },

  // 批量分配客户
  batchAssign: async (data: CustomerBatchAssignReqVO) => {
    return await request.post({ url: `/crm/customer-assign/batch-assign`, data })
  },

  // 自动分配客户
  autoAssign: async (data: CustomerAutoAssignReqVO) => {
    return await request.post({ url: `/crm/customer-assign/auto-assign`, data })
  }
}
