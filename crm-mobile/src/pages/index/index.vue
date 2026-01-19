<template>
  <view class="index-container">
    <!-- 顶部导航 -->
    <view class="header">
      <view class="greeting">
        <text class="greeting-text">{{ greetingText }}</text>
        <text class="date-text">{{ currentDate }}</text>
      </view>
      <view class="user-info" @click="goToMine">
        <u-avatar :src="userStore.userInfo.avatar || '/static/default-avatar.png'" size="80"></u-avatar>
      </view>
    </view>

    <!-- 今日概览 -->
    <view class="overview-section">
      <view class="section-title">
        <text class="title">今日概览</text>
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
            <u-icon name="checkbox-mark" color="#ffffff" size="48"></u-icon>
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
        <view class="action-icon" style="background: #ecf5ff">
          <u-icon name="list" size="40" color="#409eff"></u-icon>
        </view>
        <text>客户列表</text>
      </view>
      <view class="action-item" @click="goToCallRecords">
        <view class="action-icon" style="background: #f0f9ff">
          <u-icon name="clock" size="40" color="#67c23a"></u-icon>
        </view>
        <text>拨打记录</text>
      </view>
      <view class="action-item" @click="goToFollowRecords">
        <view class="action-icon" style="background: #fdf6ec">
          <u-icon name="edit-pen" size="40" color="#e6a23c"></u-icon>
        </view>
        <text>跟进记录</text>
      </view>
      <view class="action-item" @click="goToAddCustomer">
        <view class="action-icon" style="background: #fef0f0">
          <u-icon name="plus" size="40" color="#f56c6c"></u-icon>
        </view>
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
        <view v-for="item in todoList" :key="item.id" class="todo-item" @click="goToCustomerDetail(item.customerId)">
          <view class="customer-info">
            <text class="name">{{ item.customerName }}</text>
            <StatusTag :status="item.status"></StatusTag>
          </view>
          <view class="todo-info">
            <text class="time">{{ item.followTime }}</text>
            <text class="desc">{{ item.followDesc }}</text>
          </view>
        </view>

        <view v-if="todoList.length === 0 && !loading" class="empty-state">
          <EmptyState mainText="暂无待办事项" subText="太棒了，今天的工作已完成！"></EmptyState>
        </view>

        <LoadingSpinner v-if="loading" :mask="false"></LoadingSpinner>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useUserStore } from '@/store'
import StatusTag from '@/components/StatusTag/StatusTag.vue'
import EmptyState from '@/components/EmptyState/EmptyState.vue'
import LoadingSpinner from '@/components/LoadingSpinner/LoadingSpinner.vue'

// 今日概览数据
interface Overview {
  followingCount: number
  todayCalls: number
  monthDeals: number
}

// 待办事项
interface TodoItem {
  id: number
  customerId: number
  customerName: string
  status: string
  followTime: string
  followDesc: string
}

const userStore = useUserStore()
const loading = ref(false)

// 今日概览数据
const overview = reactive<Overview>({
  followingCount: 0,
  todayCalls: 0,
  monthDeals: 0
})

// 待办列表
const todoList = ref<TodoItem[]>([])

// 当前日期
const currentDate = ref('')
const greetingText = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 17) return '下午好'
  if (hour < 19) return '傍晚好'
  return '晚上好'
})

onMounted(() => {
  const date = new Date()
  const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  currentDate.value = `${date.getMonth() + 1}月${date.getDate()}日 ${weekDays[date.getDay()]}`

  loadOverview()
  loadTodoList()
})

// 加载概览数据
async function loadOverview() {
  loading.value = true
  try {
    // TODO: 调用API获取数据
    // const res = await getOverview()
    // Object.assign(overview, res.data)

    // 模拟数据
    setTimeout(() => {
      Object.assign(overview, {
        followingCount: 12,
        todayCalls: 28,
        monthDeals: 5
      })
      loading.value = false
    }, 500)
  } catch (error) {
    loading.value = false
  }
}

// 加载待办列表
async function loadTodoList() {
  loading.value = true
  try {
    // TODO: 调用API获取数据
    // const res = await getTodoList()
    // todoList.value = res.data

    // 模拟数据
    setTimeout(() => {
      todoList.value = [
        {
          id: 1,
          customerId: 1,
          customerName: '张三',
          status: '跟进中',
          followTime: '10:30',
          followDesc: '跟进贷款申请进度'
        },
        {
          id: 2,
          customerId: 2,
          customerName: '李四',
          status: '新线索',
          followTime: '14:00',
          followDesc: '初次联系，了解需求'
        }
      ]
      loading.value = false
    }, 500)
  } catch (error) {
    loading.value = false
  }
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
function goToCustomerDetail(customerId: number) {
  uni.navigateTo({ url: `/pages/customer/detail?id=${customerId}` })
}

// 跳转到个人中心
function goToMine() {
  uni.switchTab({ url: '/pages/mine/index' })
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

  .greeting {
    display: flex;
    flex-direction: column;
    gap: 8rpx;

    .greeting-text {
      font-size: 36rpx;
      font-weight: bold;
      color: #ffffff;
    }

    .date-text {
      font-size: 24rpx;
      color: rgba(255, 255, 255, 0.8);
    }
  }

  .user-info {
    cursor: pointer;
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
      transition: all 0.3s;

      &:active {
        transform: scale(0.95);
        opacity: 0.8;
      }

      .icon {
        width: 88rpx;
        height: 88rpx;
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
    cursor: pointer;
    transition: all 0.3s;

    &:active {
      transform: scale(0.9);
      opacity: 0.8;
    }

    .action-icon {
      width: 88rpx;
      height: 88rpx;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: center;
    }

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
    min-height: 300rpx;

    .todo-item {
      padding: 24rpx;
      border-bottom: 1rpx solid #ebeef5;
      cursor: pointer;
      transition: all 0.3s;

      &:active {
        background-color: #f8f8f8;
      }

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

  .more {
    font-size: 24rpx;
    color: #409eff;
    cursor: pointer;
  }
}
</style>
