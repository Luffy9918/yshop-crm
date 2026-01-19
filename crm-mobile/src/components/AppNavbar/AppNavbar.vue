<template>
  <view class="app-navbar" :style="{ paddingTop: statusBarHeight + 'px' }">
    <view class="navbar-content">
      <!-- 左侧返回按钮 -->
      <view v-if="showBack" class="navbar-back" @click="handleBack">
        <u-icon name="arrow-left" size="20" :color="titleColor"></u-icon>
      </view>

      <!-- 左侧自定义内容 -->
      <view v-else-if="leftContent" class="navbar-left">
        <slot name="left"></slot>
      </view>

      <!-- 标题 -->
      <view class="navbar-title" :style="{ color: titleColor }">
        {{ title }}
      </view>

      <!-- 右侧自定义内容 -->
      <view class="navbar-right">
        <slot name="right">
          <view v-if="showMore" class="navbar-more" @click="handleMore">
            <u-icon name="more-dot-fill" size="20" :color="titleColor"></u-icon>
          </view>
        </slot>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Props {
  title?: string
  showBack?: boolean
  showMore?: boolean
  backgroundColor?: string
  titleColor?: string
  fixed?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  showBack: false,
  showMore: false,
  backgroundColor: '#ffffff',
  titleColor: '#303133',
  fixed: true
})

const emit = defineEmits<{
  (e: 'back'): void
  (e: 'more'): void
}>()

const statusBarHeight = ref(0)
const leftContent = ref(false)

onMounted(() => {
  // 获取状态栏高度
  const systemInfo = uni.getSystemInfoSync()
  statusBarHeight.value = systemInfo.statusBarHeight || 0

  // 检查是否有左侧插槽内容
  leftContent.value = !!props.$slots?.left
})

function handleBack() {
  emit('back')
  uni.navigateBack({
    delta: 1
  })
}

function handleMore() {
  emit('more')
}
</script>

<style lang="scss" scoped>
.app-navbar {
  width: 100%;
  background-color: v-bind(backgroundColor);
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  z-index: 999;

  &.fixed {
    position: fixed;
    top: 0;
    left: 0;
  }
}

.navbar-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 30rpx;
}

.navbar-back,
.navbar-more {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.navbar-left {
  width: 120rpx;
  display: flex;
  align-items: center;
}

.navbar-title {
  flex: 1;
  font-size: 32rpx;
  font-weight: bold;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding: 0 20rpx;
}

.navbar-right {
  width: 120rpx;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
</style>
