/**
 * HTTP响应接口
 */
export interface HttpResponse<T = any> {
  code: number
  data: T
  msg: string
}

/**
 * HTTP请求配置接口
 */
export interface RequestOptions {
  url: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  data?: any
  header?: Record<string, string>
  timeout?: number
  loading?: boolean
}

/**
 * Token信息接口
 */
export interface TokenInfo {
  accessToken: string
  refreshToken: string
  expiresAt: number
}

/**
 * API错误接口
 */
export interface ApiError {
  code: number
  msg: string
  data?: any
}
