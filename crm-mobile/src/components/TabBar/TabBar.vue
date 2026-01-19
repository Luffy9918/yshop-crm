<template>
  <view class="tab-bar" :style="{ paddingBottom: safeAreaBottom + 'px' }">
    <view
      v-for="(item, index) in tabList"
      :key="index"
      :class="['tab-item', { active: currentIndex === index }]"
      @click="handleTabClick(index, item)"
    >
      <view class="tab-icon">
        <u-icon
          v-if="currentIndex === index"
          :name="item.selectedIconPath"
          size="24"
          :color="currentIndex === index ? activeColor : inactiveColor"
        ></u-icon>
        <u-icon
          v-else
          :name="item.iconPath"
          size="24"
          :color="currentIndex === index ? activeColor : inactiveColor"
        ></u-icon>
        <!-- 徽标 -->
        <view v-if="item.badge && item.badge > 0" class="tab-badge">
          <text>{{ item.badge > 99 ? '99+' : item.badge }}</text>
        </view>
        <!-- 红点 -->
        <view v-else-if="item.dot" class="tab-dot"></view>
      </view>
      <text class="tab-label" :style="{ color: currentIndex === index ? activeColor : inactiveColor }">
        {{ item.text }}
      </text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'

export interface TabItem {
  text: string
  iconPath: string
  selectedIconPath: string
  badge?: number
  dot?: boolean
  pagePath: string
}

interface Props {
  activeColor?: string
  inactiveColor?: string
}

const props = withDefaults(defineProps<Props>(), {
  activeColor: '#409eff',
  inactiveColor: '#909399'
})

const emit = defineEmits<{
  (e: 'change', index: number, item: TabItem): void
}>()

const currentIndex = ref(0)
const safeAreaBottom = ref(0)
const tabList = ref<TabItem[]>([
  {
    text: '首页',
    iconPath: 'home',
    selectedIconPath: 'home-fill',
    pagePath: '/pages/index/index'
  },
  {
    text: '客户',
    iconPath: 'account',
    selectedIconPath: 'account-fill',
    pagePath: '/pages/customer/list'
  },
  {
    text: '我的',
    iconPath: 'account',
    selectedIconPath: 'account-fill',
    pagePath: '/pages/mine/index'
  }
])

onMounted(() => {
  // 获取安全区域底部高度
  const systemInfo = uni.getSystemInfoSync()
  safeAreaBottom.value = systemInfo.safeAreaInsets ? systemInfo.safeAreaInsets.bottom : 0

  // 获取当前页面路径，设置激活状态
  const pages = getCurrentPages()
  if (pages.length > 0) {
    const currentPage = pages[pages.length - 1]
    const route = currentPage.route || ''
    const index = tabList.value.findIndex((item) => route.includes(item.pagePath))
    if (index !== -1) {
      currentIndex.value = index
    }
  }
})

function handleTabClick(index: number, item: TabItem) {
  if (currentIndex.value === index) return

  currentIndex.value = index
  emit('change', index, item)

  // 跳转页面
  uni.switchTab({
    url: item.pagePath
  })
}

// 设置徽标
function setBadge(index: number, badge: number) {
  if (tabList.value[index]) {
    tabList.value[index].badge = badge
  }
}

// 设置红点
function setDot(index: number, dot: boolean) {
  if (tabList.value[index]) {
    tabList.value[index].dot = dot
  }
}

defineExpose({
  setBadge,
  setDot
})
</script>

<style lang="scss" scoped>
.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  background: #ffffff;
  border-top: 1rpx solid #e5e5e5;
  box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.05);
  z-index: 999;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12rpx 0;
  position: relative;
  transition: all 0.3s;

  &.active {
    .tab-label {
      font-weight: bold;
    }
  }
}

.tab-icon {
  position: relative;
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 4rpx;
}

.tab-badge {
  position: absolute;
  top: -6rpx;
  right: -6rpx;
  min-width: 32rpx;
  height: 32rpx;
  background: #f56c6c;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
  border: 2rpx solid #ffffff;

  text {
    font-size: 20rpx;
    color: #ffffff;
    line-height: 1;
    transform: scale(0.8);
  }
}

.tab-dot {
  position: absolute;
  top: 0;
  right: 0;
  width: 16rpx;
  height: 16rpx;
  background: #f56c6c;
  border-radius: 50%;
  border: 2rpx solid #ffffff;
}

.tab-label {
  font-size: 22rpx;
  line-height: 1;
}
</style>
