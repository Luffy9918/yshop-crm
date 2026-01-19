<template>
  <view class="customer-card" @click="handleClick">
    <view class="card-header">
      <view class="left">
        <u-avatar :src="customer.avatar" size="80"></u-avatar>
        <view class="info">
          <text class="name">{{ customer.name }}</text>
          <text class="phone">{{ maskedPhone }}</text>
        </view>
      </view>
      <StatusTag :status="customer.status"></StatusTag>
    </view>

    <view class="card-body">
      <view class="info-row">
        <text class="label">数据来源：</text>
        <text class="value">{{ customer.dataSource }}</text>
      </view>
      <view class="info-row">
        <text class="label">跟进次数：</text>
        <text class="value">{{ customer.followCount }}次</text>
      </view>
      <view v-if="customer.product" class="info-row">
        <text class="label">意向产品：</text>
        <text class="value">{{ customer.product }}</text>
      </view>
      <view class="info-row">
        <text class="label">最后跟进：</text>
        <text class="value">{{ customer.lastFollowTime || '-' }}</text>
      </view>
    </view>

    <view v-if="showActions" class="card-footer">
      <u-button size="small" type="primary" @click.stop="handleCall">
        <u-icon name="phone" size="28"></u-icon>
        拨打
      </u-button>
      <u-button size="small" @click.stop="handleFollow">
        <u-icon name="edit-pen" size="28"></u-icon>
        跟进
      </u-button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import StatusTag from '../StatusTag/StatusTag.vue'

export interface Customer {
  id: number
  name: string
  phone: string
  avatar?: string
  status: string
  dataSource: string
  product?: string
  followCount: number
  lastFollowTime?: string
  loanAmount?: string
}

interface Props {
  customer: Customer
  showActions?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  showActions: true
})

const emit = defineEmits<{
  (e: 'click', customer: Customer): void
  (e: 'call', customer: Customer): void
  (e: 'follow', customer: Customer): void
}>()

// 手机号脱敏
const maskedPhone = computed(() => {
  const phone = props.customer.phone
  if (!phone || phone.length < 7) return phone
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
})

function handleClick() {
  emit('click', props.customer)
}

function handleCall() {
  emit('call', props.customer)
}

function handleFollow() {
  emit('follow', props.customer)
}
</script>

<style lang="scss" scoped>
.customer-card {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s;

  &:active {
    transform: scale(0.98);
    opacity: 0.9;
  }
}

.card-header {
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

.card-body {
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
      flex-shrink: 0;
    }

    .value {
      font-size: 26rpx;
      color: #606266;
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}

.card-footer {
  display: flex;
  gap: 20rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid #ebeef5;
}
</style>
