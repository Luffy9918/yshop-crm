<template>
  <view class="follow-add-container">
    <AppNavbar title="添加跟进" :show-back="true"></AppNavbar>

    <view class="form-container">
      <u-form :model="formData" :rules="rules" ref="formRef" labelPosition="top">
        <!-- 跟进信息 -->
        <view class="form-section">
          <view class="section-title">跟进信息</view>

          <u-form-item label="跟进标题" prop="title" required borderBottom>
            <u-input
              v-model="formData.title"
              placeholder="请输入跟进标题"
              :border="false"
              clearable
            ></u-input>
          </u-form-item>

          <u-form-item label="跟进方式" prop="method" borderBottom>
            <u-picker
              :show="methodPickerShow"
              :columns="methodColumns"
              keyName="label"
              @confirm="onMethodConfirm"
              @cancel="methodPickerShow = false"
            ></u-picker>
            <u-input
              v-model="methodLabel"
              placeholder="请选择跟进方式"
              :border="false"
              readonly
              suffixIcon="arrow-down"
              @click="methodPickerShow = true"
            ></u-input>
          </u-form-item>

          <u-form-item label="跟进状态" prop="status" borderBottom>
            <u-radio-group v-model="formData.status" placement="row">
              <u-radio
                v-for="item in statusOptions"
                :key="item.value"
                :name="item.value"
                :label="item.label"
                :customStyle="{ marginRight: '20rpx' }"
              ></u-radio>
            </u-radio-group>
          </u-form-item>

          <u-form-item label="跟进时间" prop="followTime" borderBottom>
            <u-datetime-picker
              :show="timePickerShow"
              v-model="formData.followTime"
              mode="datetime"
              @confirm="onTimeConfirm"
              @cancel="timePickerShow = false"
            ></u-datetime-picker>
            <u-input
              v-model="displayTime"
              placeholder="请选择跟进时间"
              :border="false"
              readonly
              suffixIcon="calendar"
              @click="timePickerShow = true"
            ></u-input>
          </u-form-item>
        </view>

        <!-- 跟进内容 -->
        <view class="form-section">
          <view class="section-title">跟进内容</view>

          <u-form-item label="跟进内容" prop="content" borderBottom>
            <u-textarea
              v-model="formData.content"
              placeholder="请详细记录跟进情况、客户反馈等"
              :border="false"
              count
              maxlength="500"
            ></u-textarea>
          </u-form-item>

          <u-form-item label="下次跟进时间" prop="nextFollowTime" borderBottom>
            <u-datetime-picker
              :show="nextTimePickerShow"
              v-model="formData.nextFollowTime"
              mode="datetime"
              @confirm="onNextTimeConfirm"
              @cancel="nextTimePickerShow = false"
            ></u-datetime-picker>
            <u-input
              v-model="displayNextTime"
              placeholder="请选择下次跟进时间（可选）"
              :border="false"
              readonly
              suffixIcon="calendar"
              @click="nextTimePickerShow = true"
            ></u-input>
          </u-form-item>
        </view>

        <!-- 附件信息 -->
        <view class="form-section">
          <view class="section-title">附件信息</view>

          <u-form-item label="添加图片" borderBottom>
            <u-upload
              :fileList="fileList"
              @afterRead="afterRead"
              @delete="deleteFile"
              :maxCount="9"
              :maxSize="5 * 1024 * 1024"
              previewFullImage
              :deletable="true"
            ></u-upload>
          </u-form-item>
        </view>
      </u-form>

      <!-- 提交按钮 -->
      <view class="submit-section">
        <u-button
          type="primary"
          :loading="loading"
          @click="handleSubmit"
          class="submit-btn"
        >
          保存跟进
        </u-button>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import AppNavbar from '@/components/AppNavbar/AppNavbar.vue'
import { addFollowRecord } from '@/api'
import type { FormRules } from 'uview-plus'

// 客户ID
const customerId = ref(0)

// 表单数据
const formData = reactive({
  title: '',
  method: '',
  status: 'pending',
  followTime: Date.now(),
  content: '',
  nextFollowTime: null
})

// 表单验证规则
const rules: FormRules = {
  title: [
    { required: true, message: '请输入跟进标题', trigger: 'blur' },
    { min: 2, max: 50, message: '标题长度为2-50个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入跟进内容', trigger: 'blur' },
    { min: 5, max: 500, message: '内容长度为5-500个字符', trigger: 'blur' }
  ]
}

const formRef = ref()
const loading = ref(false)

// 选择器状态
const methodPickerShow = ref(false)
const timePickerShow = ref(false)
const nextTimePickerShow = ref(false)

// 跟进方式选项
const methodColumns = ref([
  { label: '电话', value: 'phone' },
  { label: '微信', value: 'wechat' },
  { label: '面谈', value: 'meeting' },
  { label: '短信', value: 'sms' },
  { label: '邮件', value: 'email' },
  { label: '其他', value: 'other' }
])

// 跟进状态选项
const statusOptions = ref([
  { label: '待跟进', value: 'pending' },
  { label: '跟进中', value: 'following' },
  { label: '已完成', value: 'completed' }
])

// 文件列表
const fileList = ref<any[]>([])

// 显示标签
const methodLabel = computed(() => {
  const item = methodColumns.value.find(i => i.value === formData.method)
  return item?.label || ''
})

// 格式化显示时间
const displayTime = computed(() => {
  if (!formData.followTime) return ''
  const date = new Date(formData.followTime)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
})

const displayNextTime = computed(() => {
  if (!formData.nextFollowTime) return ''
  const date = new Date(formData.nextFollowTime)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
})

// 页面加载时获取客户ID
onLoad((options: any) => {
  if (options.customerId) {
    customerId.value = Number(options.customerId)
  }
})

// 跟进方式选择确认
function onMethodConfirm(e: any) {
  formData.method = e.value[0].value
  methodPickerShow.value = false
}

// 时间选择确认
function onTimeConfirm(e: any) {
  formData.followTime = e.value
  timePickerShow.value = false
}

// 下次跟进时间选择确认
function onNextTimeConfirm(e: any) {
  formData.nextFollowTime = e.value
  nextTimePickerShow.value = false
}

// 文件读取后
function afterRead(event: any) {
  fileList.value.push({
    url: event.file.url,
    status: 'uploading',
    message: '上传中...'
  })

  // TODO: 实际上传文件到服务器
  setTimeout(() => {
    const file = fileList.value[fileList.value.length - 1]
    file.status = 'success'
    file.message = '上传成功'
  }, 1000)
}

// 删除文件
function deleteFile(event: any) {
  fileList.value.splice(event.index, 1)
}

// 提交表单
async function handleSubmit() {
  try {
    // 表单验证
    await formRef.value?.validate()

    loading.value = true

    // 准备提交数据
    const submitData = {
      customerId: customerId.value,
      ...formData,
      attachments: fileList.value.map(f => f.url)
    }

    // 调用添加跟进记录API
    const res = await addFollowRecord(submitData)

    if (res.code === 0) {
      uni.showToast({
        title: '添加成功',
        icon: 'success'
      })

      // 延迟返回，让用户看到成功提示
      setTimeout(() => {
        uni.navigateBack()
      }, 500)
    } else {
      uni.showToast({
        title: res.msg || '添加失败',
        icon: 'none'
      })
    }
  } catch (error: any) {
    console.error('添加跟进记录失败:', error)
    // 表单验证失败或其他错误
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.follow-add-container {
  min-height: 100vh;
  background-color: #f8f8f8;
}

.form-container {
  padding: 20rpx 30rpx;
}

.form-section {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #303133;
    margin-bottom: 30rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #ebeef5;
  }
}

.submit-section {
  margin-top: 60rpx;
  padding: 0 30rpx;

  .submit-btn {
    height: 88rpx;
    font-size: 32rpx;
  }
}
</style>
