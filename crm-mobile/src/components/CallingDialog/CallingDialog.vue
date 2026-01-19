<template>
  <view class="calling-dialog" v-if="show" @click="handleMaskClick">
    <view class="dialog-container" @click.stop>
      <!-- 客户信息 -->
      <view class="customer-info">
        <u-avatar :src="customer.avatar || '/static/default-avatar.png'" size="120"></u-avatar>
        <view class="info">
          <text class="name">{{ customer.name }}</text>
          <text class="phone">{{ customer.phone }}</text>
        </view>
      </view>

      <!-- 通话状态 -->
      <view class="call-status">
        <view class="status-icon" :class="`status-${callState}`">
          <u-icon v-if="callState === 'dialing'" name="loading" size="80" color="#ffffff"></u-icon>
          <u-icon v-else-if="callState === 'connected'" name="phone-fill" size="80" color="#ffffff"></u-icon>
          <u-icon v-else name="phone" size="80" color="#ffffff"></u-icon>
        </view>
        <text class="status-text">{{ statusText }}</text>
        <text v-if="callState === 'connected'" class="duration">{{ formattedDuration }}</text>
      </view>

      <!-- 操作按钮 -->
      <view class="actions">
        <u-button
          v-if="callState === 'dialing' || callState === 'connected'"
          type="error"
          size="large"
          circle
          @click="handleHangup"
          class="hangup-btn"
        >
          <u-icon name="close" size="40" color="#ffffff"></u-icon>
        </u-button>

        <u-button
          v-else-if="callState === 'ringing'"
          type="success"
          size="large"
          circle
          @click="handleAnswer"
          class="answer-btn"
        >
          <u-icon name="phone-fill" size="40" color="#ffffff"></u-icon>
        </u-button>
      </view>

      <!-- 备注 -->
      <view class="remark-section" v-if="callState === 'connected'">
        <u-input
          v-model="remark"
          placeholder="通话备注（可选）"
          border="none"
          :customStyle="{ background: '#f8f8f8', padding: '20rpx', borderRadius: '8rpx' }"
        ></u-input>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { CallState, getCallDuration, hangupCall } from '@/utils/sip'

interface Props {
  show: boolean
  customer: {
    id: number
    name: string
    phone: string
    avatar?: string
  }
  callState: CallState
}

const props = defineProps<Props>()
const emit = defineEmits<{
  (e: 'close'): void
  (e: 'hangup', remark?: string): void
  (e: 'answer'): void
}>()

const remark = ref('')

// 状态文本
const statusText = computed(() => {
  const stateMap: Record<CallState, string> = {
    [CallState.IDLE]: '空闲',
    [CallState.DIALING]: '正在拨号...',
    [CallState.RINGING]: '正在呼叫...',
    [CallState.CONNECTED]: '通话中',
    [CallState.ENDED]: '通话结束',
    [CallState.FAILED]: '拨打失败'
  }
  return stateMap[props.callState] || ''
})

// 格式化通话时长
const formattedDuration = computed(() => {
  const duration = getCallDuration()
  const minutes = Math.floor(duration / 60)
  const seconds = duration % 60
  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
})

// 监听通话结束
watch(() => props.callState, (newState) => {
  if (newState === CallState.ENDED || newState === CallState.FAILED) {
    setTimeout(() => {
      emit('hangup', remark.value)
      remark.value = ''
    }, 500)
  }
})

// 点击遮罩
function handleMaskClick() {
  // 通话中不允许关闭
  if (props.callState !== CallState.IDLE) {
    return
  }
  emit('close')
}

// 挂断
async function handleHangup() {
  await hangupCall()
  emit('hangup', remark.value)
  remark.value = ''
}

// 接听
function handleAnswer() {
  emit('answer')
}
</script>

<style lang="scss" scoped>
.calling-dialog {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.dialog-container {
  width: 600rpx;
  background: #ffffff;
  border-radius: 32rpx;
  padding: 60rpx 40rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.customer-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 60rpx;

  .info {
    margin-top: 24rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12rpx;

    .name {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }

    .phone {
      font-size: 28rpx;
      color: #909399;
    }
  }
}

.call-status {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 60rpx;

  .status-icon {
    width: 160rpx;
    height: 160rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 30rpx;

    &.status-dialing {
      background: linear-gradient(135deg, #667eea, #764ba2);
      animation: pulse 1.5s ease-in-out infinite;
    }

    &.status-connected {
      background: #67c23a;
    }

    &.status-ringing {
      background: #e6a23c;
      animation: shake 0.5s ease-in-out infinite;
    }
  }

  .status-text {
    font-size: 32rpx;
    color: #303133;
    margin-bottom: 16rpx;
  }

  .duration {
    font-size: 48rpx;
    font-weight: bold;
    color: #409eff;
  }
}

.actions {
  .hangup-btn,
  .answer-btn {
    width: 120rpx;
    height: 120rpx;
  }

  .hangup-btn {
    background: #f56c6c;
    border-color: #f56c6c;
  }

  .answer-btn {
    background: #67c23a;
    border-color: #67c23a;
  }
}

.remark-section {
  width: 100%;
  margin-top: 40rpx;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

@keyframes shake {
  0%, 100% {
    transform: translateX(0);
  }
  25% {
    transform: translateX(-10rpx);
  }
  75% {
    transform: translateX(10rpx);
  }
}
</style>
