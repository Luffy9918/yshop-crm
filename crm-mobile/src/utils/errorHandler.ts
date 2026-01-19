/**
 * 全局错误处理工具
 */

export interface ErrorInfo {
  code?: number | string
  message: string
  stack?: string
  context?: string
}

export type ErrorHandler = (error: ErrorInfo) => void

// 错误处理器列表
const errorHandlers: ErrorHandler[] = []

/**
 * 注册错误处理器
 */
export function registerErrorHandler(handler: ErrorHandler) {
  errorHandlers.push(handler)
}

/**
 * 移除错误处理器
 */
export function unregisterErrorHandler(handler: ErrorHandler) {
  const index = errorHandlers.indexOf(handler)
  if (index > -1) {
    errorHandlers.splice(index, 1)
  }
}

/**
 * 触发错误处理
 */
export function triggerError(error: ErrorInfo) {
  // 打印到控制台
  console.error('[Error]', error.context || 'Unknown', error)

  // 通知所有处理器
  errorHandlers.forEach(handler => {
    try {
      handler(error)
    } catch (err) {
      console.error('[ErrorHandler] 处理器执行失败:', err)
    }
  })

  // 显示用户提示
  showUserError(error.message)
}

/**
 * 显示用户友好的错误提示
 */
function showUserError(message: string) {
  uni.showToast({
    title: message,
    icon: 'none',
    duration: 3000
  })
}

/**
 * 处理 API 错误
 */
export function handleApiError(error: any, context?: string) {
  const errorInfo: ErrorInfo = {
    code: error?.code || error?.statusCode || 'UNKNOWN',
    message: getErrorMessage(error),
    stack: error?.stack,
    context
  }
  triggerError(errorInfo)
}

/**
 * 获取用户友好的错误消息
 */
function getErrorMessage(error: any): string {
  // 网络错误
  if (error?.errMsg) {
    if (error.errMsg.includes('timeout')) {
      return '网络连接超时，请检查网络'
    }
    if (error.errMsg.includes('fail')) {
      return '网络连接失败，请检查网络'
    }
  }

  // HTTP 状态码错误
  if (error?.statusCode) {
    const statusMessages: Record<number, string> = {
      400: '请求参数错误',
      401: '登录已过期，请重新登录',
      403: '没有权限访问',
      404: '请求的资源不存在',
      500: '服务器错误，请稍后重试',
      502: '网关错误，请稍后重试',
      503: '服务暂时不可用'
    }
    return statusMessages[error.statusCode] || `请求失败 (${error.statusCode})`
  }

  // 业务错误码
  if (error?.code) {
    const code = String(error.code)
    if (code === '401') {
      return '登录已过期，请重新登录'
    }
  }

  // 自定义错误消息
  if (error?.message) {
    return error.message
  }

  // 默认消息
  return '操作失败，请稍后重试'
}

/**
 * 捕获异步错误
 */
export function asyncErrorHandler<T extends (...args: any[]) => any>(
  fn: T,
  context?: string
): T {
  return ((...args: any[]) => {
    try {
      const result = fn(...args)
      // 处理 Promise
      if (result && typeof result.catch === 'function') {
        return result.catch((error: any) => {
          handleApiError(error, context)
          throw error
        })
      }
      return result
    } catch (error: any) {
      handleApiError(error, context)
      throw error
    }
  }) as T
}

/**
 * Vue 错误处理器
 */
export function createVueErrorHandler() {
  return (err: any, vm: any, info: string) => {
    triggerError({
      code: 'VUE_ERROR',
      message: err?.message || 'Vue 组件错误',
      stack: err?.stack,
      context: `Vue: ${info}`
    })
  }
}

/**
 * 全局未捕获的 Promise 处理器
 */
export function handleUnhandledRejection(event: PromiseRejectionEvent) {
  handleApiError(event.reason, 'UnhandledRejection')
}

/**
 * 初始化全局错误处理
 */
export function initGlobalErrorHandler() {
  // Vue 错误处理（在 main.ts 中调用）
  // app.config.errorHandler = createVueErrorHandler()

  // 全局未捕获的 Promise
  // @ts-ignore
  if (typeof window !== 'undefined') {
    // @ts-ignore
    window.addEventListener('unhandledrejection', handleUnhandledRejection)
  }
}
