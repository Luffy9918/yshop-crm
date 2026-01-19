/**
 * API统一导出
 */
export * as AuthApi from './auth'
export * as CustomerApi from './customer'

// 便捷导出
export { login, sendCode, logout, getUserInfo } from './auth'
export {
  getCustomerList,
  getCustomerDetail,
  addCustomer,
  updateCustomer,
  deleteCustomer,
  getFollowRecords,
  addFollowRecord,
  getCustomerStats
} from './customer'

// 导出类型
export type { LoginParams, LoginResult, UserInfo } from './auth'
export type {
  Customer,
  CustomerQueryParams,
  CustomerListResult,
  FollowRecord,
  AddFollowParams,
  CustomerStatus
} from './customer'
