<template>
  <ContentWrap>
    <!-- 总览卡片 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon total">
              <el-icon :size="24"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalCustomers || 0 }}</div>
              <div class="stat-label">总客户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon today">
              <el-icon :size="24"><Calendar /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.newCustomersToday || 0 }}</div>
              <div class="stat-label">今日新增</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon following">
              <el-icon :size="24"><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.followingCustomers || 0 }}</div>
              <div class="stat-label">跟进中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon deal">
              <el-icon :size="24"><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.conversionRate || 0 }}%</div>
              <div class="stat-label">转化率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <!-- 状态分布饼图 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>状态分布</span>
            </div>
          </template>
          <div ref="statusPieChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>

      <!-- 客户趋势折线图 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>客户趋势 (近30天)</span>
            </div>
          </template>
          <div ref="trendLineChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据源ROI柱状图 -->
    <el-row style="margin-top: 16px">
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>数据源ROI分析</span>
            </div>
          </template>
          <div ref="roiBarChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </ContentWrap>
</template>

<script setup lang="ts" name="CrmStatistics">
import { ref, onMounted, onUnmounted } from 'vue'
import { StatisticsApi } from '@/api/crm/crmstatistics'
import type { StatisticsOverviewVO, StatusDistributeVO, CustomerTrendVO, DataSourceRoiVO } from '@/api/crm/crmstatistics'
import * as echarts from 'echarts'
import { User, Calendar, Clock, TrendCharts } from '@element-plus/icons-vue'

// 总览数据
const overview = ref<StatisticsOverviewVO>({
  totalCustomers: 0,
  newCustomersToday: 0,
  followingCustomers: 0,
  dealsThisMonth: 0,
  conversionRate: 0
})

// 图表引用
const statusPieChartRef = ref<HTMLDivElement>()
const trendLineChartRef = ref<HTMLDivElement>()
const roiBarChartRef = ref<HTMLDivElement>()

// 图表实例
let statusPieChart: echarts.ECharts | null = null
let trendLineChart: echarts.ECharts | null = null
let roiBarChart: echarts.ECharts | null = null

// 初始化状态分布饼图
const initStatusPieChart = (data: StatusDistributeVO[]) => {
  if (!statusPieChartRef.value) return

  if (statusPieChart) {
    statusPieChart.dispose()
  }

  statusPieChart = echarts.init(statusPieChartRef.value)

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center'
    },
    series: [
      {
        name: '状态分布',
        type: 'pie',
        radius: '65%',
        data: data.map(item => ({
          value: item.count,
          name: item.status
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }

  statusPieChart.setOption(option)
}

// 初始化客户趋势折线图
const initTrendLineChart = (data: CustomerTrendVO[]) => {
  if (!trendLineChartRef.value) return

  if (trendLineChart) {
    trendLineChart.dispose()
  }

  trendLineChart = echarts.init(trendLineChartRef.value)

  const dates = data.map(item => item.date)
  const newCounts = data.map(item => item.newCustomerCount)
  const dealCounts = data.map(item => item.dealCount)

  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['新增客户', '成交客户']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '新增客户',
        type: 'line',
        data: newCounts,
        smooth: true,
        itemStyle: {
          color: '#409EFF'
        }
      },
      {
        name: '成交客户',
        type: 'line',
        data: dealCounts,
        smooth: true,
        itemStyle: {
          color: '#67C23A'
        }
      }
    ]
  }

  trendLineChart.setOption(option)
}

// 初始化ROI柱状图
const initRoiBarChart = (data: DataSourceRoiVO[]) => {
  if (!roiBarChartRef.value) return

  if (roiBarChart) {
    roiBarChart.dispose()
  }

  roiBarChart = echarts.init(roiBarChartRef.value)

  const names = data.map(item => item.dataSourceName)
  const rois = data.map(item => item.roi)

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: names,
      axisLabel: {
        interval: 0,
        rotate: 30
      }
    },
    yAxis: {
      type: 'value',
      name: 'ROI(倍)'
    },
    series: [
      {
        name: 'ROI',
        type: 'bar',
        data: rois,
        itemStyle: {
          color: (params: any) => {
            const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
            return colors[params.dataIndex % colors.length]
          }
        }
      }
    ]
  }

  roiBarChart.setOption(option)
}

// 加载统计数据
const loadStatistics = async () => {
  // 加载总览
  const overviewRes = await StatisticsApi.getOverview()
  overview.value = overviewRes.data

  // 加载状态分布
  const statusRes = await StatisticsApi.getStatusDistribute()
  initStatusPieChart(statusRes.data || [])

  // 加载客户趋势
  const trendRes = await StatisticsApi.getCustomerTrend({})
  initTrendLineChart(trendRes.data || [])

  // 加载数据源ROI
  const roiRes = await StatisticsApi.getDataSourceRoi()
  initRoiBarChart(roiRes.data || [])
}

// 窗口大小变化时重绘图表
const handleResize = () => {
  statusPieChart?.resize()
  trendLineChart?.resize()
  roiBarChart?.resize()
}

onMounted(() => {
  loadStatistics()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  statusPieChart?.dispose()
  trendLineChart?.dispose()
  roiBarChart?.dispose()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped lang="scss">
.overview-cards {
  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;

      .stat-icon {
        width: 56px;
        height: 56px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;

        &.total {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
        }

        &.today {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          color: white;
        }

        &.following {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          color: white;
        }

        &.deal {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          color: white;
        }
      }

      .stat-info {
        flex: 1;

        .stat-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }
}

.card-header {
  font-weight: bold;
  color: #303133;
}
</style>