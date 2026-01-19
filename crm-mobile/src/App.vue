<template>
  <view>
    <router-view />
  </view>
</template>

<script setup lang="ts">
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app'
import { useUserStore } from '@/store'

const userStore = useUserStore()

onLaunch(() => {
  console.log('App Launch')
  // 初始化用户状态
  userStore.init()
  // 应用启动时检查登录状态
  checkLogin()
})

onShow(() => {
  console.log('App Show')
})

onHide(() => {
  console.log('App Hide')
})

function checkLogin() {
  if (!userStore.isLoggedIn) {
    // 未登录，跳转到登录页
    uni.redirectTo({
      url: '/pages/login/index'
    })
  }
}
</script>

<style lang="scss">
@import 'uview-plus/index.scss';

/* 全局样式 */
page {
  background-color: #f8f8f8;
  font-size: 14px;
  line-height: 1.6;
}

/* 通用类 */
.container {
  padding: 20rpx;
}

.flex {
  display: flex;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.text-center {
  text-align: center;
}

.text-primary {
  color: #409eff;
}

.text-success {
  color: #67c23a;
}

.text-warning {
  color: #e6a23c;
}

.text-danger {
  color: #f56c6c;
}

.text-info {
  color: #909399;
}
</style>
