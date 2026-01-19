<template>
  <view :class="['loading-spinner', `loading-${type}`]" :style="{ background: mask ? 'rgba(0, 0, 0, 0.5)' : 'transparent' }">
    <view v-if="type === 'spinner'" class="spinner-content">
      <view class="spinner">
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
      </view>
      <text v-if="text" class="loading-text">{{ text }}</text>
    </view>

    <view v-else-if="type === 'dot'" class="dot-content">
      <view class="dot-flip">
        <view></view>
        <view></view>
        <view></view>
      </view>
      <text v-if="text" class="loading-text">{{ text }}</text>
    </view>

    <view v-else-if="type === 'bar'" class="bar-content">
      <view class="bar-spinner">
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
        <view></view>
      </view>
      <text v-if="text" class="loading-text">{{ text }}</text>
    </view>
  </view>
</template>

<script setup lang="ts">
interface Props {
  type?: 'spinner' | 'dot' | 'bar'
  text?: string
  mask?: boolean
}

withDefaults(defineProps<Props>(), {
  type: 'spinner',
  text: '加载中...',
  mask: true
})
</script>

<style lang="scss" scoped>
.loading-spinner {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.spinner-content,
.dot-content,
.bar-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.7);
  border-radius: 16rpx;
  padding: 40rpx;
  min-width: 200rpx;
  min-height: 200rpx;
}

.loading-text {
  margin-top: 24rpx;
  font-size: 26rpx;
  color: #ffffff;
}

/* Spinner 样式 */
.spinner {
  width: 80rpx;
  height: 80rpx;
  position: relative;
  display: inline-block;

  view {
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
  }

  view:before {
    content: '';
    display: block;
    margin: 0 auto;
    width: 15%;
    height: 15%;
    background-color: #ffffff;
    border-radius: 100%;
    animation: spinner-fade 1.2s infinite ease-in-out both;
  }

  view:nth-child(1) {
    transform: rotate(0deg);
  }
  view:nth-child(2) {
    transform: rotate(30deg);
  }
  view:nth-child(3) {
    transform: rotate(60deg);
  }
  view:nth-child(4) {
    transform: rotate(90deg);
  }
  view:nth-child(5) {
    transform: rotate(120deg);
  }
  view:nth-child(6) {
    transform: rotate(150deg);
  }
  view:nth-child(7) {
    transform: rotate(180deg);
  }
  view:nth-child(8) {
    transform: rotate(210deg);
  }
  view:nth-child(9) {
    transform: rotate(240deg);
  }
  view:nth-child(10) {
    transform: rotate(270deg);
  }
  view:nth-child(11) {
    transform: rotate(300deg);
  }
  view:nth-child(12) {
    transform: rotate(330deg);
  }

  view:nth-child(1):before {
    animation-delay: -1.2s;
  }
  view:nth-child(2):before {
    animation-delay: -1.1s;
  }
  view:nth-child(3):before {
    animation-delay: -1s;
  }
  view:nth-child(4):before {
    animation-delay: -0.9s;
  }
  view:nth-child(5):before {
    animation-delay: -0.8s;
  }
  view:nth-child(6):before {
    animation-delay: -0.7s;
  }
  view:nth-child(7):before {
    animation-delay: -0.6s;
  }
  view:nth-child(8):before {
    animation-delay: -0.5s;
  }
  view:nth-child(9):before {
    animation-delay: -0.4s;
  }
  view:nth-child(10):before {
    animation-delay: -0.3s;
  }
  view:nth-child(11):before {
    animation-delay: -0.2s;
  }
  view:nth-child(12):before {
    animation-delay: -0.1s;
  }
}

@keyframes spinner-fade {
  0%,
  39%,
  100% {
    opacity: 0.2;
    transform: scale(0.6);
  }
  40% {
    opacity: 1;
    transform: scale(1);
  }
}

/* Dot 样式 */
.dot-flip {
  display: flex;
  gap: 16rpx;

  view {
    width: 20rpx;
    height: 20rpx;
    border-radius: 50%;
    background-color: #ffffff;
    animation: flip 1.2s infinite ease-in-out both;
  }

  view:nth-child(1) {
    animation-delay: -0.32s;
  }
  view:nth-child(2) {
    animation-delay: -0.16s;
  }
  view:nth-child(3) {
    animation-delay: 0s;
  }
}

@keyframes flip {
  0%,
  80%,
  100% {
    transform: scale(0);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* Bar 样式 */
.bar-spinner {
  display: flex;
  gap: 8rpx;

  view {
    width: 8rpx;
    height: 60rpx;
    background-color: #ffffff;
    animation: bar-stretch 1.2s infinite ease-in-out;
  }

  view:nth-child(1) {
    animation-delay: -1.1s;
  }
  view:nth-child(2) {
    animation-delay: -1s;
  }
  view:nth-child(3) {
    animation-delay: -0.9s;
  }
  view:nth-child(4) {
    animation-delay: -0.8s;
  }
  view:nth-child(5) {
    animation-delay: -0.7s;
  }
  view:nth-child(6) {
    animation-delay: -0.6s;
  }
  view:nth-child(7) {
    animation-delay: -0.5s;
  }
  view:nth-child(8) {
    animation-delay: -0.4s;
  }
}

@keyframes bar-stretch {
  0%,
  40%,
  100% {
    transform: scaleY(0.4);
  }
  20% {
    transform: scaleY(1);
  }
}
</style>
