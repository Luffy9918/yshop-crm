/**
 * 认证相关API
 */
import { post } from '@/utils/request'
import type { HttpResponse } from '@/utils/types'

/**
 * 登录请求参数
 */
export interface LoginParams {
  phone: string
  code: string
}

/**
 * 登录响应数据
 */
export interface LoginResult {
  accessToken: string
  refreshToken: string
  expiresAt: number
  userInfo: {
    id: number
    name: string
    phone: string
    avatar?: string
  }
}

/**
 * 发送验证码请求参数
 */
export interface SendCodeParams {
  phone: string
}

/**
 * 用户信息
 */
export interface UserInfo {
  id: number
  name: string
  phone: string
  avatar?: string
  role?: string
  department?: string
}

/**
 * 登录
 */
export function login(params: LoginParams): Promise<HttpResponse<LoginResult>> {
  return post<LoginResult>('/api/auth/login', params)
}

/**
 * 发送验证码
 */
export function sendCode(params: SendCodeParams): Promise<HttpResponse<{ success: boolean }>> {
  return post<{ success: boolean }>('/api/auth/send-code', params)
}

/**
 * 退出登录
 */
export function logout(): Promise<HttpResponse<void>> {
  return post<void>('/api/auth/logout')
}

/**
 * 获取当前用户信息
 */
export function getUserInfo(): Promise<HttpResponse<UserInfo>> {
  return post<UserInfo>('/api/auth/user-info')
}
