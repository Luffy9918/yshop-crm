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
    <scroll-view
      class="scroll-container"
      scroll-y
      @scrolltolower="loadMore"
      refresher-enabled
      :refresher-triggered="refreshing"
      @refresherrefresh="onRefresh"
    >
      <view class="customer-list">
        <CustomerCard
          v-for="customer in customerList"
          :key="customer.id"
          :customer="customer"
          :show-actions="true"
          @click="goToDetail"
          @call="handleCall"
          @follow="handleFollow"
        />

        <!-- 空状态 -->
        <view v-if="customerList.length === 0 && !loading" class="empty-state">
          <EmptyState
            mainText="暂无客户数据"
            subText="快去添加您的第一个客户吧"
            icon="inbox"
          ></EmptyState>
        </view>

        <!-- 加载更多 -->
        <view v-if="customerList.length > 0" class="load-more">
          <u-loadmore v-if="hasMore" :status="loadStatus" @loadmore="loadMore"></u-loadmore>
          <view v-else class="no-more">没有更多了</view>
        </view>

        <!-- 加载中 -->
        <LoadingSpinner v-if="loading && customerList.length === 0" :mask="false"></LoadingSpinner>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import type { Customer } from '@/components'
import { CustomerCard, EmptyState, LoadingSpinner } from '@/components'
import { getCustomerList } from '@/api'

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
const refreshing = ref(false)
const hasMore = ref(true)
const loadStatus = ref('loadmore')

// 客户列表
const customerList = ref<Customer[]>([])

let page = 1
const pageSize = 20
let totalPage = 0

onLoad((options: any) => {
  // 如果有传入的状态参数
  if (options?.status) {
    const index = statusTabs.value.findIndex(tab => tab.key === options.status)
    if (index !== -1) {
      currentStatus.value = index
    }
  }
  loadCustomerList()
})

onMounted(() => {
  loadCustomerList()
})

// 加载客户列表
async function loadCustomerList(isRefresh = false) {
  if (loading.value && !isRefresh) return

  loading.value = true

  try {
    const params: any = {
      page: isRefresh ? 1 : page,
      pageSize
    }

    // 添加状态筛选
    const currentKey = statusTabs.value[currentStatus.value].key
    if (currentKey) {
      params.status = currentKey
    }

    // 添加搜索关键词
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }

    const res = await getCustomerList(params)

    if (res.code === 0) {
      const { list, total } = res.data

      if (isRefresh || page === 1) {
        customerList.value = list
      } else {
        customerList.value.push(...list)
      }

      // 计算总页数
      totalPage = Math.ceil(total / pageSize)
      hasMore.value = page < totalPage

      // 更新加载状态
      if (!hasMore.value) {
        loadStatus.value = 'nomore'
      } else {
        loadStatus.value = 'loadmore'
      }
    }
  } catch (error) {
    console.error('加载客户列表失败:', error)
    uni.showToast({
      title: '加载失败，请重试',
      icon: 'none'
    })
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

// 下拉刷新
function onRefresh() {
  refreshing.value = true
  page = 1
  loadCustomerList(true)
}

// 搜索
function handleSearch() {
  page = 1
  customerList.value = []
  loadCustomerList(true)
}

// 切换状态
function handleStatusChange(index: number) {
  currentStatus.value = index
  page = 1
  customerList.value = []
  loadCustomerList(true)
}

// 加载更多
function loadMore() {
  if (!hasMore.value || loading.value) return

  page++
  loadStatus.value = 'loading'
  loadCustomerList()
}

// 跳转到详情
function goToDetail(customer: Customer) {
  uni.navigateTo({ url: `/pages/customer/detail?id=${customer.id}` })
}

// 拨打电话
function handleCall(customer: Customer) {
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
function handleFollow(customer: Customer) {
  uni.navigateTo({
    url: `/pages/customer/follow?customerId=${customer.id}`
  })
}
</script>

<style lang="scss" scoped>
.customer-list-container {
  min-height: 100vh;
  background-color: #f8f8f8;
  padding-bottom: 120rpx;
}

.search-bar {
  padding: 20rpx;
  background: #ffffff;
}

.filter-tags {
  background: #ffffff;
  margin-bottom: 20rpx;
}

.scroll-container {
  height: calc(100vh - 200rpx);
}

.customer-list {
  padding: 0 20rpx;
  min-height: 100%;

  .empty-state {
    padding: 120rpx 0;
  }

  .load-more {
    padding: 20rpx 0;

    .no-more {
      text-align: center;
      font-size: 24rpx;
      color: #c0c4cc;
      padding: 20rpx 0;
    }
  }
}
</style>
