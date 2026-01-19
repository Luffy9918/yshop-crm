<template>
  <view class="empty-state" :style="{ paddingTop: paddingTop + 'rpx', paddingBottom: paddingBottom + 'rpx' }">
    <!-- 图标 -->
    <view v-if="type === 'image'" class="empty-image">
      <image :src="imageSrc" mode="aspectFit" class="image"></image>
    </view>
    <view v-else class="empty-icon">
      <u-icon :name="iconName" size="120" :color="iconColor"></u-icon>
    </view>

    <!-- 文字 -->
    <view class="empty-text">
      <text class="text-main">{{ mainText }}</text>
      <text v-if="subText" class="text-sub">{{ subText }}</text>
    </view>

    <!-- 操作按钮 -->
    <view v-if="showAction" class="empty-action">
      <u-button type="primary" size="small" @click="handleAction">
        {{ actionText }}
      </u-button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  type?: 'icon' | 'image'
  icon?: string
  image?: string
  mainText?: string
  subText?: string
  showAction?: boolean
  actionText?: string
  iconColor?: string
  paddingTop?: number
  paddingBottom?: number
}

const props = withDefaults(defineProps<Props>(), {
  type: 'icon',
  icon: 'inbox',
  image: '',
  mainText: '暂无数据',
  subText: '',
  showAction: false,
  actionText: '去添加',
  iconColor: '#c0c4cc',
  paddingTop: 120,
  paddingBottom: 0
})

const emit = defineEmits<{
  (e: 'action'): void
}>()

// 默认图片映射
const defaultImages: Record<string, string> = {
  list: '/static/empty/list.png',
  search: '/static/empty/search.png',
  network: '/static/empty/network.png',
  data: '/static/empty/data.png'
}

// 图标映射
const iconMap: Record<string, string> = {
  list: 'file-text',
  search: 'search',
  network: 'wifi',
  data: 'database',
  inbox: 'inbox',
  'file-text': 'file-text',
  shopping: 'shopping-cart'
}

const iconName = computed(() => {
  return iconMap[props.icon] || props.icon
})

const imageSrc = computed(() => {
  if (props.image) return props.image
  return defaultImages['list'] || ''
})

function handleAction() {
  emit('action')
}
</script>

<style lang="scss" scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  box-sizing: border-box;
}

.empty-image,
.empty-icon {
  margin-bottom: 32rpx;

  .image {
    width: 320rpx;
    height: 320rpx;
  }
}

.empty-text {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 40rpx;

  .text-main {
    font-size: 28rpx;
    color: #909399;
    margin-bottom: 16rpx;
  }

  .text-sub {
    font-size: 24rpx;
    color: #c0c4cc;
  }
}

.empty-action {
  width: 200rpx;
}
</style>
