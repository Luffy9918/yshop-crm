<template>
  <view class="index-container">
    <!-- 顶部导航 -->
    <view class="header">
      <text class="title">工作台</text>
      <view class="user-info">
        <u-avatar :src="user.avatar" size="80"></u-avatar>
      </view>
    </view>

    <!-- 今日概览 -->
    <view class="overview-section">
      <view class="section-title">
        <text class="title">今日概览</text>
        <text class="date">{{ currentDate }}</text>
      </view>

      <view class="overview-cards">
        <view class="card" @click="goToCustomers('following')">
          <view class="icon" style="background: linear-gradient(135deg, #667eea, #764ba2)">
            <u-icon name="account" color="#ffffff" size="48"></u-icon>
          </view>
          <view class="info">
            <text class="value">{{ overview.followingCount }}</text>
            <text class="label">待跟进</text>
          </view>
        </view>

        <view class="card" @click="goToCallRecords">
          <view class="icon" style="background: linear-gradient(135deg, #f093fb, #f5576c)">
            <u-icon name="phone" color="#ffffff" size="48"></u-icon>
          </view>
          <view class="info">
            <text class="value">{{ overview.todayCalls }}</text>
            <text class="label">今日拨打</text>
          </view>
        </view>

        <view class="card" @click="goToCustomers('deal')">
          <view class="icon" style="background: linear-gradient(135deg, #43e97b, #38f9d7)">
            <u-icon name="checkmark" color="#ffffff" size="48"></u-icon>
          </view>
          <view class="info">
            <text class="value">{{ overview.monthDeals }}</text>
            <text class="label">本月成交</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 快捷入口 -->
    <view class="quick-actions">
      <view class="action-item" @click="goToCustomers()">
        <u-icon name="list" size="64" color="#409eff"></u-icon>
        <text>客户列表</text>
      </view>
      <view class="action-item" @click="goToCallRecords">
        <u-icon name="clock" size="64" color="#67c23a"></u-icon>
        <text>拨打记录</text>
      </view>
      <view class="action-item" @click="goToFollowRecords">
        <u-icon name="edit-pen" size="64" color="#e6a23c"></u-icon>
        <text>跟进记录</text>
      </view>
      <view class="action-item" @click="goToAddCustomer">
        <u-icon name="plus" size="64" color="#f56c6c"></u-icon>
        <text>添加客户</text>
      </view>
    </view>

    <!-- 待办事项 -->
    <view class="todo-section">
      <view class="section-title">
        <text class="title">待办事项</text>
        <text class="more" @click="goToCustomers('following')">更多 ></text>
      </view>

      <view class="todo-list">
        <view v-for="item in todoList" :key="item.id" class="todo-item" @click="goToCustomerDetail(item.id)">
          <view class="customer-info">
            <text class="name">{{ item.customerName }}</text>
            <u-tag :text="item.status" size="mini" :type="getStatusType(item.status)"></u-tag>
          </view>
          <view class="todo-info">
            <text class="time">{{ item.followTime }}</text>
            <text class="desc">{{ item.followDesc }}</text>
          </view>
        </view>

        <view v-if="todoList.length === 0" class="empty-state">
          <u-empty mode="list" text="暂无待办事项"></u-empty>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'

// 用户信息
const user = reactive({
  avatar: '/static/default-avatar.png',
  name: '业务员'
})

// 今日概览数据
const overview = reactive({
  followingCount: 12,
  todayCalls: 28,
  monthDeals: 5
})

// 待办列表
const todoList = ref([
  {
    id: 1,
    customerName: '张三',
    status: '跟进中',
    followTime: '10:30',
    followDesc: '跟进贷款申请进度'
  },
  {
    id: 2,
    customerName: '李四',
    status: '新线索',
    followTime: '14:00',
    followDesc: '初次联系，了解需求'
  }
])

// 当前日期
const currentDate = ref('')

onMounted(() => {
  const date = new Date()
  currentDate.value = `${date.getMonth() + 1}月${date.getDate()}日`
  loadOverview()
})

// 加载概览数据
function loadOverview() {
  // TODO: 调用API获取数据
}

// 跳转到客户列表
function goToCustomers(status?: string) {
  const url = status ? `/pages/customer/list?status=${status}` : '/pages/customer/list'
  uni.switchTab({ url })
}

// 跳转到拨打记录
function goToCallRecords() {
  uni.showToast({ title: '拨打记录功能开发中', icon: 'none' })
}

// 跳转到跟进记录
function goToFollowRecords() {
  uni.showToast({ title: '跟进记录功能开发中', icon: 'none' })
}

// 添加客户
function goToAddCustomer() {
  uni.navigateTo({ url: '/pages/customer/add' })
}

// 跳转到客户详情
function goToCustomerDetail(id: number) {
  uni.navigateTo({ url: `/pages/customer/detail?id=${id}` })
}

// 获取状态标签类型
function getStatusType(status: string) {
  const typeMap: Record<string, string> = {
    '新线索': 'primary',
    '跟进中': 'warning',
    '已成交': 'success',
    '流失': 'error'
  }
  return typeMap[status] || 'info'
}
</script>

<style lang="scss" scoped>
.index-container {
  min-height: 100vh;
  background-color: #f8f8f8;
  padding-bottom: 120rpx;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: linear-gradient(135deg, #667eea, #764ba2);

  .title {
    font-size: 36rpx;
    font-weight: bold;
    color: #ffffff;
  }

  .user-info {
    // 用户信息样式
  }
}

.overview-section {
  margin: 30rpx;

  .overview-cards {
    display: flex;
    gap: 20rpx;
    margin-top: 20rpx;

    .card {
      flex: 1;
      background: #ffffff;
      border-radius: 16rpx;
      padding: 30rpx 20rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

      .icon {
        width: 80rpx;
        height: 80rpx;
        border-radius: 16rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 16rpx;
      }

      .info {
        display: flex;
        flex-direction: column;
        align-items: center;

        .value {
          font-size: 40rpx;
          font-weight: bold;
          color: #303133;
          margin-bottom: 8rpx;
        }

        .label {
          font-size: 24rpx;
          color: #909399;
        }
      }
    }
  }
}

.quick-actions {
  display: flex;
  justify-content: space-around;
  background: #ffffff;
  margin: 0 30rpx 30rpx;
  padding: 40rpx 0;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

  .action-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 16rpx;

    text {
      font-size: 24rpx;
      color: #606266;
    }
  }
}

.todo-section {
  margin: 0 30rpx 30rpx;

  .todo-list {
    background: #ffffff;
    border-radius: 16rpx;
    padding: 20rpx;
    margin-top: 20rpx;

    .todo-item {
      padding: 24rpx;
      border-bottom: 1rpx solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .customer-info {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16rpx;

        .name {
          font-size: 28rpx;
          font-weight: bold;
          color: #303133;
        }
      }

      .todo-info {
        display: flex;
        flex-direction: column;
        gap: 8rpx;

        .time {
          font-size: 24rpx;
          color: #909399;
        }

        .desc {
          font-size: 26rpx;
          color: #606266;
        }
      }
    }

    .empty-state {
      padding: 80rpx 0;
    }
  }
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .title {
    font-size: 32rpx;
    font-weight: bold;
    color: #303133;
  }

  .date {
    font-size: 24rpx;
    color: #909399;
  }

  .more {
    font-size: 24rpx;
    color: #409eff;
  }
}
</style>
