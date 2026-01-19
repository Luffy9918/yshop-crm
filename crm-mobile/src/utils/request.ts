/**
 * 网络请求封装
 * 包含请求拦截、响应拦截、Token刷新等功能
 */
import type { HttpResponse, RequestOptions, TokenInfo } from './types'
import { handleApiError } from './errorHandler'
// 重新导出 auth API
export { login, sendCode, logout, getUserInfo } from '../api/auth'

// API基础URL
const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// Token存储键
const TOKEN_KEY = 'access_token'
const REFRESH_TOKEN_KEY = 'refresh_token'

// 是否正在刷新Token
let isRefreshing = false
// 失败队列
let failedQueue: Array<(token: string) => void> = []

/**
 * 从本地存储获取Token
 */
function getToken(): string | null {
  return uni.getStorageSync(TOKEN_KEY) || null
}

/**
 * 保存Token到本地存储
 */
function setToken(tokenInfo: TokenInfo): void {
  uni.setStorageSync(TOKEN_KEY, tokenInfo.accessToken)
  uni.setStorageSync(REFRESH_TOKEN_KEY, tokenInfo.refreshToken)
  uni.setStorageSync('token_expires_at', tokenInfo.expiresAt.toString())
}

/**
 * 清除Token
 */
function clearToken(): void {
  uni.removeStorageSync(TOKEN_KEY)
  uni.removeStorageSync(REFRESH_TOKEN_KEY)
  uni.removeStorageSync('token_expires_at')
}

/**
 * 刷新Token
 */
async function refreshToken(): Promise<string> {
  const refreshToken = uni.getStorageSync(REFRESH_TOKEN_KEY)
  if (!refreshToken) {
    throw new Error('No refresh token available')
  }

  try {
    const response = await uni.request({
      url: `${BASE_URL}/api/auth/refresh-token`,
      method: 'POST',
      data: { refreshToken },
      header: {
        'Content-Type': 'application/json'
      }
    }) as any

    if (response.data.code === 0) {
      const tokenInfo: TokenInfo = response.data.data
      setToken(tokenInfo)
      return tokenInfo.accessToken
    } else {
      throw new Error('Refresh token failed')
    }
  } catch (error) {
    clearToken()
    throw error
  }
}

/**
 * 处理Token刷新成功
 */
function processRefreshQueue(newToken: string): void {
  failedQueue.forEach((callback) => callback(newToken))
  failedQueue = []
}

/**
 * 处理Token刷新失败
 */
function processRefreshQueueFailed(): void {
  failedQueue.forEach((callback) => callback(''))
  failedQueue = []
}

/**
 * 网络请求主函数
 */
export function request<T = any>(options: RequestOptions): Promise<HttpResponse<T>> {
  return new Promise((resolve, reject) => {
    // 显示loading
    if (options.loading !== false) {
      uni.showLoading({
        title: '加载中...',
        mask: true
      })
    }

    // 获取Token
    const token = getToken()

    // 构建请求头
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      ...options.header
    }

    if (token) {
      headers['Authorization'] = `Bearer ${token}`
    }

    // 发起请求
    uni.request({
      url: `${BASE_URL}${options.url}`,
      method: options.method || 'GET',
      data: options.data,
      header: headers,
      timeout: options.timeout || 30000
    })
      .then((res: any) => {
        // 隐藏loading
        if (options.loading !== false) {
          uni.hideLoading()
        }

        const response = res.data as HttpResponse<T>

        // 处理业务错误
        if (response.code !== 0) {
          // Token过期，尝试刷新
          if (response.code === 401) {
            // 如果正在刷新，加入队列
            if (isRefreshing) {
              return new Promise((resolveRefresh) => {
                failedQueue.push((newToken: string) => {
                  if (newToken) {
                    // 用新Token重试请求
                    request<T>(options).then(resolve).catch(reject)
                  } else {
                    reject(response)
                  }
                  resolveRefresh(null as any)
                })
              })
            }

            isRefreshing = true

            // 刷新Token
            return refreshToken()
              .then((newToken) => {
                isRefreshing = false
                processRefreshQueue(newToken)
                // 用新Token重试请求
                return request<T>(options).then(resolve).catch(reject)
              })
              .catch((error) => {
                isRefreshing = false
                processRefreshQueueFailed()
                // Token刷新失败，跳转登录
                uni.showToast({
                  title: '登录已过期，请重新登录',
                  icon: 'none'
                })
                setTimeout(() => {
                  uni.reLaunch({
                    url: '/pages/login/index'
                  })
                }, 1500)
                reject(error)
              })
          }

          // 其他业务错误
          handleApiError(
            {
              code: response.code,
              message: response.msg || '请求失败',
              data: response.data
            },
            `API: ${options.method} ${options.url}`
          )
          reject(response)
        }

        // 请求成功
        resolve(response)
      })
      .catch((error: any) => {
        // 隐藏loading
        if (options.loading !== false) {
          uni.hideLoading()
        }

        // 使用全局错误处理器
        handleApiError(error, `Network: ${options.method} ${options.url}`)

        reject({
          code: error.statusCode || -1,
          msg: error.errMsg || '网络请求失败',
          data: error
        })
      })
  })
}

/**
 * GET请求
 */
export function get<T = any>(url: string, data?: any, options?: Partial<RequestOptions>): Promise<HttpResponse<T>> {
  return request<T>({
    url,
    method: 'GET',
    data,
    ...options
  })
}

/**
 * POST请求
 */
export function post<T = any>(url: string, data?: any, options?: Partial<RequestOptions>): Promise<HttpResponse<T>> {
  return request<T>({
    url,
    method: 'POST',
    data,
    ...options
  })
}

/**
 * PUT请求
 */
export function put<T = any>(url: string, data?: any, options?: Partial<RequestOptions>): Promise<HttpResponse<T>> {
  return request<T>({
    url,
    method: 'PUT',
    data,
    ...options
  })
}

/**
 * DELETE请求
 */
export function del<T = any>(url: string, data?: any, options?: Partial<RequestOptions>): Promise<HttpResponse<T>> {
  return request<T>({
    url,
    method: 'DELETE',
    data,
    ...options
  })
}

export default {
  request,
  get,
  post,
  put,
  delete: del,
  getToken,
  setToken,
  clearToken
}
