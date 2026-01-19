/**
 * 客户相关API
 */
import { get, post, put, del } from '@/utils/request'
import type { HttpResponse } from '@/utils/types'

/**
 * 客户状态枚举
 */
export enum CustomerStatus {
  NEW = 'new',           // 新线索
  FOLLOWING = 'following',  // 跟进中
  DEAL = 'deal',        // 已成交
  LOST = 'lost'         // 流失
}

/**
 * 客户信息接口
 */
export interface Customer {
  id: number
  name: string
  phone: string
  avatar?: string
  status: CustomerStatus
  dataSource: string
  product?: string
  loanAmount?: string
  followCount: number
  lastFollowTime?: string
  createTime: string
  updateTime?: string
}

/**
 * 客户列表查询参数
 */
export interface CustomerQueryParams {
  page?: number
  pageSize?: number
  status?: CustomerStatus
  keyword?: string
  dataSource?: string
}

/**
 * 客户列表响应
 */
export interface CustomerListResult {
  list: Customer[]
  total: number
  page: number
  pageSize: number
}

/**
 * 跟进记录接口
 */
export interface FollowRecord {
  id: number
  customerId: number
  title: string
  content: string
  createTime: string
  createBy: string
}

/**
 * 添加跟进请求参数
 */
export interface AddFollowParams {
  customerId: number
  title: string
  content: string
}

/**
 * 获取客户列表
 */
export function getCustomerList(params?: CustomerQueryParams): Promise<HttpResponse<CustomerListResult>> {
  return get<CustomerListResult>('/api/customer/list', params)
}

/**
 * 获取客户详情
 */
export function getCustomerDetail(id: number): Promise<HttpResponse<Customer>> {
  return get<Customer>(`/api/customer/${id}`)
}

/**
 * 添加客户
 */
export function addCustomer(data: Partial<Customer>): Promise<HttpResponse<Customer>> {
  return post<Customer>('/api/customer/add', data)
}

/**
 * 更新客户
 */
export function updateCustomer(id: number, data: Partial<Customer>): Promise<HttpResponse<Customer>> {
  return put<Customer>(`/api/customer/${id}`, data)
}

/**
 * 删除客户
 */
export function deleteCustomer(id: number): Promise<HttpResponse<void>> {
  return del<void>(`/api/customer/${id}`)
}

/**
 * 获取客户跟进记录
 */
export function getFollowRecords(customerId: number): Promise<HttpResponse<FollowRecord[]>> {
  return get<FollowRecord[]>(`/api/customer/${customerId}/follows`)
}

/**
 * 添加跟进记录
 */
export function addFollowRecord(data: AddFollowParams): Promise<HttpResponse<FollowRecord>> {
  return post<FollowRecord>('/api/follow/add', data)
}

/**
 * 获取客户统计
 */
export function getCustomerStats(): Promise<HttpResponse<{
  total: number
  newCount: number
  followingCount: number
  dealCount: number
  lostCount: number
}>> {
  return get('/api/customer/stats')
}
