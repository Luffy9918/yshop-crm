<template>
  <view :class="['status-tag', `status-${statusType}`]">
    <text class="tag-text">{{ displayText }}</text>
  </view>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  status: string
  text?: string
}

const props = withDefaults(defineProps<Props>(), {
  status: '',
  text: ''
})

// 状态类型映射
const statusTypeMap: Record<string, string> = {
  // 新客户
  '新线索': 'primary',
  'new': 'primary',
  '新客户': 'primary',

  // 跟进中
  '跟进中': 'warning',
  'following': 'warning',
  '跟进': 'warning',

  // 已成交
  '已成交': 'success',
  'deal': 'success',
  '成交': 'success',
  '已完成': 'success',

  // 流失
  '流失': 'error',
  'lost': 'error',
  '已流失': 'error',

  // 其他
  '待处理': 'info',
  'pending': 'info',
  '处理中': 'primary',
  'processing': 'primary',
  '已关闭': 'info',
  'closed': 'info'
}

const statusType = computed(() => {
  return statusTypeMap[props.status] || 'info'
})

const displayText = computed(() => {
  return props.text || props.status
})
</script>

<style lang="scss" scoped>
.status-tag {
  display: inline-flex;
  align-items: center;
  padding: 8rpx 20rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
  font-weight: 500;

  .tag-text {
    line-height: 1;
  }

  &.status-primary {
    background-color: #ecf5ff;
    color: #409eff;
    border: 1rpx solid #b3d8ff;
  }

  &.status-success {
    background-color: #f0f9ff;
    color: #67c23a;
    border: 1rpx solid #c2e7b0;
  }

  &.status-warning {
    background-color: #fdf6ec;
    color: #e6a23c;
    border: 1rpx solid #f5dab1;
  }

  &.status-error {
    background-color: #fef0f0;
    color: #f56c6c;
    border: 1rpx solid #fbc4c4;
  }

  &.status-info {
    background-color: #f4f4f5;
    color: #909399;
    border: 1rpx solid #d3d4d6;
  }
}
</style>
