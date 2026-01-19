<template>
  <view class="customer-list-container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <u-search
        v-model="searchKeyword"
        placeholder="搜索客户姓名、手机号"
        :show-action="true"
        action-text="搜索"
        @search="handleSearch"
        @custom="handleSearch"
      ></u-search>
    </view>

    <!-- 筛选标签 -->
    <view class="filter-tags">
      <u-tabs
        :list="statusTabs"
        :current="currentStatus"
        @click="handleStatusChange"
        lineColor="#409eff"
        :activeStyle="{ color: '#409eff' }"
      ></u-tabs>
    </view>

    <!-- 客户列表 -->
    <view class="customer-list">
      <view
        v-for="customer in customerList"
        :key="customer.id"
        class="customer-item"
        @click="goToDetail(customer.id)"
      >
        <view class="customer-header">
          <view class="left">
            <u-avatar :src="customer.avatar" size="80"></u-avatar>
            <view class="info">
              <text class="name">{{ customer.name }}</text>
              <text class="phone">{{ customer.phone }}</text>
            </view>
          </view>
          <u-tag :text="customer.status" :type="getStatusType(customer.status)"></u-tag>
        </view>

        <view class="customer-body">
          <view class="info-row">
            <text class="label">数据来源：</text>
            <text class="value">{{ customer.dataSource }}</text>
          </view>
          <view class="info-row">
            <text class="label">跟进次数：</text>
            <text class="value">{{ customer.followCount }}次</text>
          </view>
          <view class="info-row">
            <text class="label">最后跟进：</text>
            <text class="value">{{ customer.lastFollowTime }}</text>
          </view>
        </view>

        <view class="customer-footer">
          <u-button size="small" type="primary" @click.stop="handleCall(customer)">
            <u-icon name="phone" size="28"></u-icon>
            拨打
          </u-button>
          <u-button size="small" @click.stop="handleFollow(customer)">
            <u-icon name="edit-pen" size="28"></u-icon>
            跟进
          </u-button>
        </view>
      </view>

      <!-- 空状态 -->
      <view v-if="customerList.length === 0 && !loading" class="empty-state">
        <u-empty mode="list" text="暂无客户数据"></u-empty>
      </view>
    </view>

    <!-- 加载更多 -->
    <view v-if="hasMore" class="load-more">
      <u-loadmore :status="loadStatus" @loadmore="loadMore"></u-loadmore>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'

// 状态标签
const statusTabs = ref([
  { name: '全部', key: '' },
  { name: '新线索', key: 'new' },
  { name: '跟进中', key: 'following' },
  { name: '已成交', key: 'deal' },
  { name: '流失', key: 'lost' }
])

const currentStatus = ref(0)
const searchKeyword = ref('')
const loading = ref(false)
const hasMore = ref(true)
const loadStatus = ref('loadmore')

// 客户列表
const customerList = ref([
  {
    id: 1,
    name: '张三',
    phone: '138****1234',
    avatar: '/static/default-avatar.png',
    status: '跟进中',
    dataSource: '线上推广',
    followCount: 3,
    lastFollowTime: '2024-01-18 10:30'
  },
  {
    id: 2,
    name: '李四',
    phone: '139****5678',
    avatar: '/static/default-avatar.png',
    status: '新线索',
    dataSource: '客户推荐',
    followCount: 0,
    lastFollowTime: '-'
  }
])

let page = 1
const pageSize = 20

onMounted(() => {
  loadCustomerList()
})

// 加载客户列表
function loadCustomerList() {
  if (loading.value) return

  loading.value = true
  // TODO: 调用API获取客户列表
  setTimeout(() => {
    loading.value = false
  }, 1000)
}

// 搜索
function handleSearch() {
  page = 1
  customerList.value = []
  loadCustomerList()
}

// 切换状态
function handleStatusChange(index: number) {
  currentStatus.value = index
  page = 1
  customerList.value = []
  loadCustomerList()
}

// 加载更多
function loadMore() {
  if (!hasMore.value || loading.value) return

  page++
  loadStatus.value = 'loading'
  loadCustomerList()
}

// 跳转到详情
function goToDetail(id: number) {
  uni.navigateTo({ url: `/pages/customer/detail?id=${id}` })
}

// 拨打电话
function handleCall(customer: any) {
  uni.showModal({
    title: '拨打电话',
    content: `确定要拨打 ${customer.name} (${customer.phone}) 吗？`,
    success: (res) => {
      if (res.confirm) {
        // TODO: 调用SIP软电话SDK
        uni.showToast({ title: '正在拨打...', icon: 'loading' })
      }
    }
  })
}

// 添加跟进
function handleFollow(customer: any) {
  uni.navigateTo({
    url: `/pages/customer/follow?customerId=${customer.id}`
  })
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
.customer-list-container {
  min-height: 100vh;
  background-color: #f8f8f8;
  padding-bottom: 20rpx;
}

.search-bar {
  padding: 20rpx;
  background: #ffffff;
}

.filter-tags {
  background: #ffffff;
  margin-bottom: 20rpx;
}

.customer-list {
  padding: 0 20rpx;

  .customer-item {
    background: #ffffff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

    .customer-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;
      padding-bottom: 20rpx;
      border-bottom: 1rpx solid #ebeef5;

      .left {
        display: flex;
        align-items: center;
        gap: 20rpx;

        .info {
          display: flex;
          flex-direction: column;
          gap: 8rpx;

          .name {
            font-size: 28rpx;
            font-weight: bold;
            color: #303133;
          }

          .phone {
            font-size: 24rpx;
            color: #909399;
          }
        }
      }
    }

    .customer-body {
      margin-bottom: 20rpx;

      .info-row {
        display: flex;
        margin-bottom: 12rpx;

        &:last-child {
          margin-bottom: 0;
        }

        .label {
          font-size: 26rpx;
          color: #909399;
          width: 160rpx;
        }

        .value {
          font-size: 26rpx;
          color: #606266;
          flex: 1;
        }
      }
    }

    .customer-footer {
      display: flex;
      gap: 20rpx;
    }
  }

  .empty-state {
    padding: 120rpx 0;
  }
}

.load-more {
  padding: 20rpx 0;
}
</style>
