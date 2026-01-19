<template>
  <view class="login-container">
    <view class="login-header">
      <image class="logo" src="/static/logo.png" mode="aspectFit"></image>
      <text class="title">助贷CRM</text>
      <text class="subtitle">移动端管理系统</text>
    </view>

    <view class="login-form">
      <u-form :model="formData" :rules="rules" ref="formRef">
        <u-form-item prop="phone" borderBottom>
          <u-input
            v-model="formData.phone"
            placeholder="请输入手机号"
            prefixIcon="phone"
            :border="false"
            clearable
            maxlength="11"
            type="number"
          ></u-input>
        </u-form-item>

        <u-form-item prop="code" borderBottom>
          <u-input
            v-model="formData.code"
            placeholder="请输入验证码"
            prefixIcon="lock"
            :border="false"
            clearable
            maxlength="6"
            type="number"
          >
            <template #suffix>
              <u-button
                :text="codeBtnText"
                :disabled="codeBtnDisabled"
                @click="sendCode"
                size="mini"
                type="primary"
              ></u-button>
            </template>
          </u-input>
        </u-form-item>

        <view class="form-options">
          <u-checkbox v-model="formData.agree" shape="circle">
            <text>我已阅读并同意</text>
            <text class="link">《用户协议》</text>
          </u-checkbox>
        </view>

        <u-button
          type="primary"
          :loading="loading"
          @click="handleLogin"
          class="login-btn"
        >
          登 录
        </u-button>
      </u-form>
    </view>

    <!-- 测试账号提示 -->
    <view class="test-tips">
      <text class="tips-title">测试账号</text>
      <text class="tips-content">手机号: 13800138000</text>
      <text class="tips-content">验证码: 123456</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onUnmounted } from 'vue'
import { useUserStore } from '@/store'
import type { FormRules } from 'uview-plus'

const userStore = useUserStore()

// 表单数据
const formData = reactive({
  phone: '',
  code: '',
  agree: false
})

// 表单验证规则
const rules: FormRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { pattern: /^\d{4,6}$/, message: '验证码格式不正确', trigger: 'blur' }
  ]
}

const formRef = ref()
const loading = ref(false)
const codeBtnText = ref('获取验证码')
const codeBtnDisabled = ref(false)
let countdown = 0
let countdownTimer: number | null = null

// 组件卸载时清除定时器
onUnmounted(() => {
  if (countdownTimer) {
    clearInterval(countdownTimer)
  }
})

// 发送验证码
async function sendCode() {
  // 验证手机号
  if (!formData.phone) {
    uni.showToast({ title: '请先输入手机号', icon: 'none' })
    return
  }

  if (!/^1[3-9]\d{9}$/.test(formData.phone)) {
    uni.showToast({ title: '手机号格式不正确', icon: 'none' })
    return
  }

  try {
    // 调用发送验证码API
    const res = await userStore.sendCode(formData.phone)

    if (res.code === 0) {
      uni.showToast({ title: '验证码已发送', icon: 'success' })

      // 开始倒计时
      startCountdown()
    } else {
      uni.showToast({ title: res.msg || '发送失败', icon: 'none' })
    }
  } catch (error: any) {
    uni.showToast({ title: error.msg || '发送失败，请重试', icon: 'none' })
  }
}

// 开始倒计时
function startCountdown() {
  codeBtnDisabled.value = true
  countdown = 60

  if (countdownTimer) {
    clearInterval(countdownTimer)
  }

  countdownTimer = setInterval(() => {
    countdown--
    codeBtnText.value = `${countdown}秒后重试`

    if (countdown <= 0) {
      if (countdownTimer) {
        clearInterval(countdownTimer)
        countdownTimer = null
      }
      codeBtnText.value = '获取验证码'
      codeBtnDisabled.value = false
    }
  }, 1000) as unknown as number
}

// 登录
async function handleLogin() {
  try {
    // 表单验证
    await formRef.value?.validate()

    // 验证用户协议
    if (!formData.agree) {
      uni.showToast({ title: '请先同意用户协议', icon: 'none' })
      return
    }

    loading.value = true

    // 调用登录API
    await userStore.login(formData.phone, formData.code)

    uni.showToast({ title: '登录成功', icon: 'success' })

    // 延迟跳转，让用户看到成功提示
    setTimeout(() => {
      uni.switchTab({
        url: '/pages/index/index'
      })
    }, 500)
  } catch (error: any) {
    // 表单验证失败或登录失败
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0 60rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-header {
  text-align: center;
  margin-bottom: 80rpx;

  .logo {
    width: 160rpx;
    height: 160rpx;
    border-radius: 32rpx;
    margin-bottom: 40rpx;
    background: rgba(255, 255, 255, 0.2);
  }

  .title {
    display: block;
    font-size: 48rpx;
    font-weight: bold;
    color: #ffffff;
    margin-bottom: 16rpx;
  }

  .subtitle {
    display: block;
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

.login-form {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 40rpx;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
}

.form-options {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
  margin: 40rpx 0;

  .link {
    color: #409eff;
  }
}

.login-btn {
  margin-top: 40rpx;
  height: 88rpx;
  font-size: 32rpx;
}

.test-tips {
  margin-top: 60rpx;
  padding: 30rpx;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 16rpx;
  border: 1rpx solid rgba(255, 255, 255, 0.2);

  .tips-title {
    display: block;
    font-size: 28rpx;
    font-weight: bold;
    color: #ffffff;
    margin-bottom: 16rpx;
  }

  .tips-content {
    display: block;
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.9);
    margin-bottom: 8rpx;
    line-height: 1.6;
  }
}
</style>
