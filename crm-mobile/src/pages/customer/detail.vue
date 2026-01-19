<template>
  <view class="customer-detail-container">
    <!-- 客户基本信息 -->
    <view class="customer-header">
      <view class="avatar-section">
        <u-avatar :src="customer.avatar" size="140"></u-avatar>
        <view class="info">
          <text class="name">{{ customer.name }}</text>
          <u-tag :text="customer.status" :type="getStatusType(customer.status)"></u-tag>
        </view>
      </view>

      <view class="quick-actions">
        <u-button type="primary" size="small" @click="handleCall">
          <u-icon name="phone" size="28"></u-icon>
          拨打
        </u-button>
        <u-button size="small" @click="handleFollow">
          <u-icon name="edit-pen" size="28"></u-icon>
          跟进
        </u-button>
        <u-button size="small" @click="handleEdit">
          <u-icon name="edit" size="28"></u-icon>
          编辑
        </u-button>
      </view>
    </view>

    <!-- 详细信息卡片 -->
    <view class="info-section">
      <view class="section-title">基本信息</view>
      <view class="info-card">
        <view class="info-item">
          <text class="label">手机号码</text>
          <text class="value">{{ customer.phone }}</text>
        </view>
        <view class="info-item">
          <text class="label">数据来源</text>
          <text class="value">{{ customer.dataSource }}</text>
        </view>
        <view class="info-item">
          <text class="label">意向产品</text>
          <text class="value">{{ customer.product || '未选择' }}</text>
        </view>
        <view class="info-item">
          <text class="label">贷款金额</text>
          <text class="value">{{ customer.loanAmount || '-' }}</text>
        </view>
        <view class="info-item">
          <text class="label">创建时间</text>
          <text class="value">{{ customer.createTime }}</text>
        </view>
        <view class="info-item">
          <text class="label">跟进次数</text>
          <text class="value">{{ customer.followCount }}次</text>
        </view>
      </view>
    </view>

    <!-- 跟进记录 -->
    <view class="follow-section">
      <view class="section-title">
        <text>跟进记录</text>
        <u-button size="mini" type="primary" @click="addFollow">添加</u-button>
      </view>

      <view class="follow-list">
        <view v-for="record in followRecords" :key="record.id" class="follow-item">
          <view class="follow-header">
            <text class="title">{{ record.title }}</text>
            <text class="time">{{ record.createTime }}</text>
          </view>
          <view class="follow-content">{{ record.content }}</view>
          <view class="follow-footer">
            <text class="follow-by">记录人：{{ record.createBy }}</text>
          </view>
        </view>

        <view v-if="followRecords.length === 0" class="empty-state">
          <u-empty mode="list" text="暂无跟进记录"></u-empty>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'

// 客户信息
const customer = reactive({
  id: 0,
  name: '',
  phone: '',
  avatar: '/static/default-avatar.png',
  status: '',
  dataSource: '',
  product: '',
  loanAmount: '',
  createTime: '',
  followCount: 0
})

// 跟进记录
const followRecords = ref([
  {
    id: 1,
    title: '初次联系',
    content: '客户对贷款产品感兴趣，已发送产品介绍',
    createTime: '2024-01-18 10:30',
    createBy: '业务员A'
  }
])

onLoad((options: any) => {
  if (options.id) {
    customer.id = options.id
    loadCustomerDetail(options.id)
  }
})

// 加载客户详情
function loadCustomerDetail(id: number) {
  // TODO: 调用API获取客户详情
  Object.assign(customer, {
    id,
    name: '张三',
    phone: '138****1234',
    status: '跟进中',
    dataSource: '线上推广',
    product: '信用贷款',
    loanAmount: '50万',
    createTime: '2024-01-15',
    followCount: 3
  })
}

// 拨打电话
function handleCall() {
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
function handleFollow() {
  uni.navigateTo({
    url: `/pages/customer/follow?customerId=${customer.id}`
  })
}

function addFollow() {
  handleFollow()
}

// 编辑客户
function handleEdit() {
  uni.navigateTo({
    url: `/pages/customer/edit?id=${customer.id}`
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
.customer-detail-container {
  min-height: 100vh;
  background-color: #f8f8f8;
  padding-bottom: 40rpx;
}

.customer-header {
  background: linear-gradient(135deg, #667eea, #764ba2);
  padding: 40rpx 30rpx;

  .avatar-section {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;

    .info {
      margin-left: 24rpx;
      display: flex;
      flex-direction: column;
      gap: 16rpx;

      .name {
        font-size: 36rpx;
        font-weight: bold;
        color: #ffffff;
      }
    }
  }

  .quick-actions {
    display: flex;
    gap: 20rpx;
  }
}

.info-section,
.follow-section {
  margin: 20rpx 30rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #303133;
    margin-bottom: 20rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .info-card {
    background: #ffffff;
    border-radius: 16rpx;
    padding: 24rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

    .info-item {
      display: flex;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .label {
        width: 160rpx;
        font-size: 28rpx;
        color: #909399;
      }

      .value {
        flex: 1;
        font-size: 28rpx;
        color: #606266;
        text-align: right;
      }
    }
  }
}

.follow-section {
  .follow-list {
    background: #ffffff;
    border-radius: 16rpx;
    padding: 20rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

    .follow-item {
      padding: 24rpx;
      border-bottom: 1rpx solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .follow-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16rpx;

        .title {
          font-size: 28rpx;
          font-weight: bold;
          color: #303133;
        }

        .time {
          font-size: 24rpx;
          color: #909399;
        }
      }

      .follow-content {
        font-size: 26rpx;
        color: #606266;
        line-height: 1.6;
        margin-bottom: 12rpx;
      }

      .follow-footer {
        .follow-by {
          font-size: 24rpx;
          color: #909399;
        }
      }
    }

    .empty-state {
      padding: 80rpx 0;
    }
  }
}
</style>
