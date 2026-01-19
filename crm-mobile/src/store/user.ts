/**
 * 用户状态管理
 */
import { defineStore } from 'pinia'
import { ref, reactive } from 'vue'
import type { UserInfo } from '@/api'
import { login as loginApi, getUserInfo as getUserInfoApi, setToken, clearToken } from '@/utils/request'

export const useUserStore = defineStore('user', () => {
  // Token
  const token = ref<string>('')

  // 用户信息
  const userInfo = reactive<Partial<UserInfo>>({})

  // 是否已登录
  const isLoggedIn = ref(false)

  /**
   * 登录
   */
  async function login(phone: string, code: string) {
    try {
      const response = await loginApi({ phone, code })
      if (response.code === 0) {
        const { accessToken, refreshToken, expiresAt, userInfo: info } = response.data

        // 保存Token
        setToken({
          accessToken,
          refreshToken,
          expiresAt
        })

        token.value = accessToken

        // 保存用户信息
        Object.assign(userInfo, info)

        isLoggedIn.value = true

        return true
      }
      return false
    } catch (error) {
      console.error('Login failed:', error)
      return false
    }
  }

  /**
   * 获取用户信息
   */
  async function fetchUserInfo() {
    try {
      const response = await getUserInfoApi()
      if (response.code === 0) {
        Object.assign(userInfo, response.data)
        return response.data
      }
    } catch (error) {
      console.error('Fetch user info failed:', error)
    }
  }

  /**
   * 退出登录
   */
  function logout() {
    clearToken()
    token.value = ''
    isLoggedIn.value = false
    Object.keys(userInfo).forEach(key => {
      delete userInfo[key as keyof UserInfo]
    })

    // 跳转到登录页
    uni.reLaunch({
      url: '/pages/login/index'
    })
  }

  /**
   * 初始化（从本地恢复登录状态）
   */
  function init() {
    const savedToken = uni.getStorageSync('access_token')
    if (savedToken) {
      token.value = savedToken
      isLoggedIn.value = true
      // 获取用户信息
      fetchUserInfo()
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    logout,
    fetchUserInfo,
    init
  }
})
