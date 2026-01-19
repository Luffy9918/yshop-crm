<template>
  <view class="statistics-container">
    <AppNavbar title="数据统计" :show-back="true"></AppNavbar>

    <!-- 筛选栏 -->
    <view class="filter-bar">
      <u-tabs
        :list="periodTabs"
        :current="currentPeriod"
        @click="handlePeriodChange"
        lineColor="#409eff"
        :activeStyle="{ color: '#409eff' }"
      ></u-tabs>
    </view>

    <!-- 统计概览 -->
    <view class="overview-section">
      <view class="section-title">数据概览</view>
      <view class="overview-cards">
        <view class="card">
          <view class="icon" style="background: linear-gradient(135deg, #667eea, #764ba2)">
            <u-icon name="account" color="#ffffff" size="32"></u-icon>
          </view>
          <view class="info">
            <text class="value">{{ overview.totalCustomers }}</text>
            <text class="label">总客户数</text>
          </view>
        </view>

        <view class="card">
          <view class="icon" style="background: linear-gradient(135deg, #f093fb, #f5576c)">
            <u-icon name="phone" color="#ffffff" size="32"></u-icon>
          </view>
          <view class="info">
            <text class="value">{{ overview.totalCalls }}</text>
            <text class="label">拨打次数</text>
          </view>
        </view>

        <view class="card">
          <view class="icon" style="background: linear-gradient(135deg, #43e97b, #38f9d7)">
            <u-icon name="checkbox-mark" color="#ffffff" size="32"></u-icon>
          </view>
          <view class="info">
            <text class="value">{{ overview.totalDeals }}</text>
            <text class="label">成交数量</text>
          </view>
        </view>

        <view class="card">
          <view class="icon" style="background: linear-gradient(135deg, #fa709a, #fee140)">
            <u-icon name="integral" color="#ffffff" size="32"></u-icon>
          </view>
          <view class="info">
            <text class="value">{{ overview.conversionRate }}%</text>
            <text class="label">转化率</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 客户状态分布 -->
    <view class="chart-section">
      <view class="section-title">客户状态分布</view>
      <view class="chart-container">
        <view class="status-chart">
          <view
            v-for="item in statusDistribution"
            :key="item.status"
            class="status-item"
          >
            <view class="bar-wrap">
              <view class="bar-bg">
                <view class="bar-fill" :style="{ width: item.percentage + '%' }"></view>
              </view>
              <text class="percentage">{{ item.percentage }}%</text>
            </view>
              <view class="status-info">
                <StatusTag :status="item.label"></StatusTag>
                <text class="count">{{ item.count }}个</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 数据来源统计 -->
    <view class="chart-section">
      <view class="section-title">数据来源统计</view>
      <view class="chart-container">
        <view class="source-chart">
          <view
            v-for="item in sourceDistribution"
            :key="item.source"
            class="source-item"
          >
            <text class="source-label">{{ item.label }}</text>
            <view class="bar-wrap">
              <view class="bar-bg">
                <view class="bar-fill" :style="{ width: item.percentage + '%' }"></view>
              </view>
              <text class="count">{{ item.count }}个</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 业绩趋势 -->
    <view class="chart-section">
      <view class="section-title">业绩趋势</view>
      <view class="chart-container">
        <view class="trend-chart">
          <view class="trend-legend">
            <view class="legend-item">
              <view class="dot" style="background: #409eff"></view>
              <text>新增客户</text>
            </view>
            <view class="legend-item">
              <view class="dot" style="background: #67c23a"></view>
              <text>成交数量</text>
            </view>
          </view>
          <view class="trend-bars">
            <view
              v-for="(item, index) in trendData"
              :key="index"
              class="trend-item"
            >
              <text class="date-label">{{ item.date }}</text>
              <view class="bars">
                <view
                  class="bar bar-new"
                  :style="{ height: item.newCustomers * 2 + 'rpx' }"
                >
                  <text class="value" v-if="item.newCustomers > 0">{{ item.newCustomers }}</text>
                </view>
                <view
                  class="bar bar-deal"
                  :style="{ height: item.deals * 2 + 'rpx' }"
                >
                  <text class="value" v-if="item.deals > 0">{{ item.deals }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 加载中 -->
    <LoadingSpinner v-if="loading" :mask="false"></LoadingSpinner>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import AppNavbar from '@/components/AppNavbar/AppNavbar.vue'
import StatusTag from '@/components/StatusTag/StatusTag.vue'
import LoadingSpinner from '@/components/LoadingSpinner/LoadingSpinner.vue'

// 周期标签
const periodTabs = ref([
  { name: '今日', key: 'today' },
  { name: '本周', key: 'week' },
  { name: '本月', key: 'month' },
  { name: '全部', key: 'all' }
])

const currentPeriod = ref(0)
const loading = ref(false)

// 统计概览
const overview = reactive({
  totalCustomers: 0,
  totalCalls: 0,
  totalDeals: 0,
  conversionRate: 0
})

// 客户状态分布
const statusDistribution = ref([
  { status: 'new', label: '新线索', count: 0, percentage: 0 },
  { status: 'following', label: '跟进中', count: 0, percentage: 0 },
  { status: 'deal', label: '已成交', count: 0, percentage: 0 },
  { status: 'lost', label: '流失', count: 0, percentage: 0 }
])

// 数据来源分布
const sourceDistribution = ref([
  { source: 'online', label: '线上推广', count: 0, percentage: 0 },
  { source: 'referral', label: '客户推荐', count: 0, percentage: 0 },
  { source: 'telemarketing', label: '电话营销', count: 0, percentage: 0 },
  { source: 'offline', label: '线下活动', count: 0, percentage: 0 }
])

// 业绩趋势数据（最近7天）
const trendData = ref<any[]>([])

onMounted(() => {
  loadStatistics()
})

// 加载统计数据
async function loadStatistics() {
  loading.value = true
  try {
    // TODO: 调用API获取统计数据
    // const res = await getStatistics(periodTabs.value[currentPeriod.value].key)

    // 模拟数据
    setTimeout(() => {
      // 概览数据
      overview.totalCustomers = 156
      overview.totalCalls = 328
      overview.totalDeals = 24
      overview.conversionRate = 15.4

      // 状态分布
      statusDistribution.value = [
        { status: 'new', label: '新线索', count: 45, percentage: 29 },
        { status: 'following', label: '跟进中', count: 68, percentage: 44 },
        { status: 'deal', label: '已成交', count: 24, percentage: 15 },
        { status: 'lost', label: '流失', count: 19, percentage: 12 }
      ]

      // 来源分布
      sourceDistribution.value = [
        { source: 'online', label: '线上推广', count: 62, percentage: 40 },
        { source: 'referral', label: '客户推荐', count: 38, percentage: 24 },
        { source: 'telemarketing', label: '电话营销', count: 31, percentage: 20 },
        { source: 'offline', label: '线下活动', count: 25, percentage: 16 }
      ]

      // 趋势数据
      const today = new Date()
      trendData.value = Array.from({ length: 7 }, (_, i) => {
        const date = new Date(today)
        date.setDate(date.getDate() - (6 - i))
        return {
          date: `${date.getMonth() + 1}/${date.getDate()}`,
          newCustomers: Math.floor(Math.random() * 20) + 5,
          deals: Math.floor(Math.random() * 8) + 1
        }
      })

      loading.value = false
    }, 500)
  } catch (error) {
    console.error('加载统计数据失败:', error)
    loading.value = false
  }
}

// 切换周期
function handlePeriodChange(index: number) {
  currentPeriod.value = index
  loadStatistics()
}
</script>

<style lang="scss" scoped>
.statistics-container {
  min-height: 100vh;
  background-color: #f8f8f8;
  padding-bottom: 40rpx;
}

.filter-bar {
  background: #ffffff;
}

.overview-section,
.chart-section {
  margin: 20rpx 30rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #303133;
    margin-bottom: 20rpx;
  }
}

.overview-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;

  .card {
    background: #ffffff;
    border-radius: 16rpx;
    padding: 30rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: column;
    align-items: center;

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
        font-size: 36rpx;
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

.chart-container {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.status-chart {
  .status-item {
    margin-bottom: 30rpx;

    &:last-child {
      margin-bottom: 0;
    }

    .bar-wrap {
      display: flex;
      align-items: center;
      gap: 16rpx;
      margin-bottom: 12rpx;

      .bar-bg {
        flex: 1;
        height: 24rpx;
        background: #f0f0f0;
        border-radius: 12rpx;
        overflow: hidden;

        .bar-fill {
          height: 100%;
          border-radius: 12rpx;
          background: linear-gradient(90deg, #409eff, #66b1ff);
          transition: width 0.3s;
        }
      }

      .percentage {
        font-size: 24rpx;
        color: #606266;
        font-weight: bold;
      }
    }

    .status-info {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 20rpx;

      .count {
        font-size: 24rpx;
        color: #909399;
      }
    }
  }
}

.source-chart {
  .source-item {
    margin-bottom: 30rpx;

    &:last-child {
      margin-bottom: 0;
    }

    .source-label {
      display: block;
      font-size: 28rpx;
      color: #606266;
      margin-bottom: 12rpx;
    }

    .bar-wrap {
      display: flex;
      align-items: center;
      gap: 16rpx;

      .bar-bg {
        flex: 1;
        height: 20rpx;
        background: #f0f0f0;
        border-radius: 10rpx;
        overflow: hidden;

        .bar-fill {
          height: 100%;
          border-radius: 10rpx;
          background: linear-gradient(90deg, #67c23a, #85ce61);
          transition: width 0.3s;
        }
      }

      .count {
        font-size: 24rpx;
        color: #909399;
      }
    }
  }
}

.trend-chart {
  .trend-legend {
    display: flex;
    justify-content: center;
    gap: 40rpx;
    margin-bottom: 40rpx;

    .legend-item {
      display: flex;
      align-items: center;
      gap: 12rpx;

      .dot {
        width: 16rpx;
        height: 16rpx;
        border-radius: 50%;
      }

      text {
        font-size: 24rpx;
        color: #606266;
      }
    }
  }

  .trend-bars {
    .trend-item {
      display: flex;
      align-items: center;
      margin-bottom: 24rpx;

      &:last-child {
        margin-bottom: 0;
      }

      .date-label {
        width: 100rpx;
        font-size: 24rpx;
        color: #909399;
        text-align: center;
      }

      .bars {
        flex: 1;
        display: flex;
        gap: 8rpx;
        height: 200rpx;
        align-items: flex-end;

        .bar {
          flex: 1;
          border-radius: 8rpx 8rpx 0 0;
          position: relative;
          min-height: 4rpx;
          transition: height 0.3s;

          &.bar-new {
            background: #409eff;
          }

          &.bar-deal {
            background: #67c23a;
          }

          .value {
            position: absolute;
            top: -24rpx;
            left: 0;
            right: 0;
            text-align: center;
            font-size: 22rpx;
            color: #606266;
          }
        }
      }
    }
  }
}
</style>
