<template>
  <view class="mine-container">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <view class="user-info">
        <u-avatar :src="user.avatar" size="120"></u-avatar>
        <view class="info">
          <text class="name">{{ user.name }}</text>
          <text class="role">{{ user.role }}</text>
        </view>
      </view>
      <view class="stats">
        <view class="stat-item">
          <text class="value">{{ user.todayCalls }}</text>
          <text class="label">今日拨打</text>
        </view>
        <view class="stat-item">
          <text class="value">{{ user.monthCustomers }}</text>
          <text class="label">本月客户</text>
        </view>
        <view class="stat-item">
          <text class="value">{{ user.monthDeals }}</text>
          <text class="label">本月成交</text>
        </view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-item" @click="goToPage('/pages/mine/profile')">
        <view class="left">
          <u-icon name="account" size="40" color="#409eff"></u-icon>
          <text>个人信息</text>
        </view>
        <u-icon name="arrow-right" size="32" color="#c0c4cc"></u-icon>
      </view>

      <view class="menu-item" @click="goToPage('/pages/mine/statistics')">
        <view class="left">
          <u-icon name="chart" size="40" color="#67c23a"></u-icon>
          <text>数据统计</text>
        </view>
        <u-icon name="arrow-right" size="32" color="#c0c4cc"></u-icon>
      </view>

      <view class="menu-item" @click="goToPage('/pages/mine/settings')">
        <view class="left">
          <u-icon name="setting" size="40" color="#e6a23c"></u-icon>
          <text>系统设置</text>
        </view>
        <u-icon name="arrow-right" size="32" color="#c0c4cc"></u-icon>
      </view>

      <view class="menu-item" @click="goToPage('/pages/mine/about')">
        <view class="left">
          <u-icon name="info-circle" size="40" color="#909399"></u-icon>
          <text>关于我们</text>
        </view>
        <u-icon name="arrow-right" size="32" color="#c0c4cc"></u-icon>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section">
      <u-button type="error" @click="handleLogout" plain>
        退出登录
      </u-button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

// 用户信息
const user = reactive({
  avatar: '/static/default-avatar.png',
  name: '业务员',
  role: '销售专员',
  todayCalls: 28,
  monthCustomers: 156,
  monthDeals: 12
})

// 跳转页面
function goToPage(url: string) {
  uni.navigateTo({ url })
}

// 退出登录
function handleLogout() {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        uni.removeStorageSync('token')
        uni.reLaunch({
          url: '/pages/login/index'
        })
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.mine-container {
  min-height: 100vh;
  background-color: #f8f8f8;
}

.user-card {
  background: linear-gradient(135deg, #667eea, #764ba2);
  padding: 60rpx 30rpx 40rpx;
  margin-bottom: 20rpx;

  .user-info {
    display: flex;
    align-items: center;
    margin-bottom: 40rpx;

    .info {
      margin-left: 24rpx;
      display: flex;
      flex-direction: column;
      gap: 12rpx;

      .name {
        font-size: 36rpx;
        font-weight: bold;
        color: #ffffff;
      }

      .role {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }

  .stats {
    display: flex;
    justify-content: space-around;

    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;

      .value {
        font-size: 40rpx;
        font-weight: bold;
        color: #ffffff;
        margin-bottom: 8rpx;
      }

      .label {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
}

.menu-section {
  background: #ffffff;
  margin: 20rpx 30rpx;
  border-radius: 16rpx;
  overflow: hidden;

  .menu-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #ebeef5;

    &:last-child {
      border-bottom: none;
    }

    .left {
      display: flex;
      align-items: center;
      gap: 24rpx;

      text {
        font-size: 28rpx;
        color: #606266;
      }
    }
  }
}

.logout-section {
  padding: 60rpx 30rpx;
}
</style>
