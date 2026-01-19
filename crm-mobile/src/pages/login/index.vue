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
          ></u-input>
        </u-form-item>

        <u-form-item prop="code" borderBottom>
          <u-input
            v-model="formData.code"
            placeholder="请输入验证码"
            prefixIcon="lock"
            :border="false"
            clearable
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
          <u-checkbox v-model="formData.rememberPwd" shape="circle">
            记住密码
          </u-checkbox>
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
  </view>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import type { FormRules } from 'uview-plus'

// 表单数据
const formData = reactive({
  phone: '',
  code: '',
  rememberPwd: false,
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
    { min: 4, max: 6, message: '验证码长度为4-6位', trigger: 'blur' }
  ]
}

const formRef = ref()
const loading = ref(false)
const codeBtnText = ref('获取验证码')
const codeBtnDisabled = ref(false)
let countdown = 0

// 发送验证码
function sendCode() {
  if (!formData.phone) {
    uni.showToast({ title: '请先输入手机号', icon: 'none' })
    return
  }

  if (!/^1[3-9]\d{9}$/.test(formData.phone)) {
    uni.showToast({ title: '手机号格式不正确', icon: 'none' })
    return
  }

  // TODO: 调用发送验证码API
  uni.showToast({ title: '验证码已发送', icon: 'success' })

  // 倒计时
  codeBtnDisabled.value = true
  countdown = 60
  const timer = setInterval(() => {
    countdown--
    codeBtnText.value = `${countdown}秒后重试`
    if (countdown <= 0) {
      clearInterval(timer)
      codeBtnText.value = '获取验证码'
      codeBtnDisabled.value = false
    }
  }, 1000)
}

// 登录
function handleLogin() {
  formRef.value?.validate().then(() => {
    if (!formData.agree) {
      uni.showToast({ title: '请先同意用户协议', icon: 'none' })
      return
    }

    loading.value = true

    // TODO: 调用登录API
    setTimeout(() => {
      loading.value = false
      // 模拟登录成功
      uni.setStorageSync('token', 'mock-token-123456')
      uni.showToast({ title: '登录成功', icon: 'success' })
      uni.switchTab({
        url: '/pages/index/index'
      })
    }, 1000)
  }).catch(() => {
    // 表单验证失败
  })
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
</style>
