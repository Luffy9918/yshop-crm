<template>
  <view class="customer-add-container">
    <AppNavbar title="添加客户" :show-back="true"></AppNavbar>

    <view class="form-container">
      <u-form :model="formData" :rules="rules" ref="formRef" labelPosition="top">
        <!-- 基本信息 -->
        <view class="form-section">
          <view class="section-title">基本信息</view>

          <u-form-item label="客户姓名" prop="name" required borderBottom>
            <u-input
              v-model="formData.name"
              placeholder="请输入客户姓名"
              :border="false"
              clearable
            ></u-input>
          </u-form-item>

          <u-form-item label="手机号码" prop="phone" required borderBottom>
            <u-input
              v-model="formData.phone"
              placeholder="请输入手机号码"
              :border="false"
              clearable
              maxlength="11"
              type="number"
            ></u-input>
          </u-form-item>

          <u-form-item label="客户状态" prop="status" borderBottom>
            <u-picker
              :show="statusPickerShow"
              :columns="statusColumns"
              keyName="label"
              @confirm="onStatusConfirm"
              @cancel="statusPickerShow = false"
            ></u-picker>
            <u-input
              v-model="statusLabel"
              placeholder="请选择客户状态"
              :border="false"
              readonly
              suffixIcon="arrow-down"
              @click="statusPickerShow = true"
            ></u-input>
          </u-form-item>
        </view>

        <!-- 业务信息 -->
        <view class="form-section">
          <view class="section-title">业务信息</view>

          <u-form-item label="数据来源" prop="dataSource" borderBottom>
            <u-picker
              :show="sourcePickerShow"
              :columns="sourceColumns"
              keyName="label"
              @confirm="onSourceConfirm"
              @cancel="sourcePickerShow = false"
            ></u-picker>
            <u-input
              v-model="dataSourceLabel"
              placeholder="请选择数据来源"
              :border="false"
              readonly
              suffixIcon="arrow-down"
              @click="sourcePickerShow = true"
            ></u-input>
          </u-form-item>

          <u-form-item label="意向产品" prop="productId" borderBottom>
            <u-picker
              :show="productPickerShow"
              :columns="productColumns"
              keyName="label"
              @confirm="onProductConfirm"
              @cancel="productPickerShow = false"
            ></u-picker>
            <u-input
              v-model="productLabel"
              placeholder="请选择意向产品"
              :border="false"
              readonly
              suffixIcon="arrow-down"
              @click="productPickerShow = true"
            ></u-input>
          </u-form-item>

          <u-form-item label="贷款金额" prop="loanAmount" borderBottom>
            <u-input
              v-model="formData.loanAmount"
              placeholder="请输入贷款金额（万元）"
              :border="false"
              type="number"
              suffixIcon="currency-circle"
            ></u-input>
          </u-form-item>
        </view>

        <!-- 备注信息 -->
        <view class="form-section">
          <view class="section-title">备注信息</view>

          <u-form-item label="备注" prop="remark" borderBottom>
            <u-textarea
              v-model="formData.remark"
              placeholder="请输入备注信息"
              :border="false"
              count
            ></u-textarea>
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
          保存客户
        </u-button>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import AppNavbar from '@/components/AppNavbar/AppNavbar.vue'
import { addCustomer } from '@/api'
import type { FormRules } from 'uview-plus'

// 表单数据
const formData = reactive({
  name: '',
  phone: '',
  status: '',
  dataSource: '',
  productId: '',
  loanAmount: '',
  remark: ''
})

// 表单验证规则
const rules: FormRules = {
  name: [
    { required: true, message: '请输入客户姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择客户状态', trigger: 'change' }
  ]
}

const formRef = ref()
const loading = ref(false)

// 选择器状态
const statusPickerShow = ref(false)
const sourcePickerShow = ref(false)
const productPickerShow = ref(false)

// 客户状态选项
const statusColumns = ref([
  { label: '新线索', value: 'new' },
  { label: '跟进中', value: 'following' },
  { label: '已成交', value: 'deal' },
  { label: '流失', value: 'lost' }
])

// 数据来源选项
const sourceColumns = ref([
  { label: '线上推广', value: 'online' },
  { label: '客户推荐', value: 'referral' },
  { label: '电话营销', value: 'telemarketing' },
  { label: '线下活动', value: 'offline' },
  { label: '其他', value: 'other' }
])

// 产品选项（模拟数据，实际应从API获取）
const productColumns = ref([
  { label: '信用贷款', value: 1 },
  { label: '抵押贷款', value: 2 },
  { label: '担保贷款', value: 3 },
  { label: '公积金贷款', value: 4 }
])

// 显示标签
const statusLabel = computed(() => {
  const item = statusColumns.value.find(i => i.value === formData.status)
  return item?.label || ''
})

const dataSourceLabel = computed(() => {
  const item = sourceColumns.value.find(i => i.value === formData.dataSource)
  return item?.label || ''
})

const productLabel = computed(() => {
  const item = productColumns.value.find(i => i.value === formData.productId)
  return item?.label || ''
})

// 状态选择确认
function onStatusConfirm(e: any) {
  formData.status = e.value[0].value
  statusPickerShow.value = false
}

// 数据来源选择确认
function onSourceConfirm(e: any) {
  formData.dataSource = e.value[0].value
  sourcePickerShow.value = false
}

// 产品选择确认
function onProductConfirm(e: any) {
  formData.productId = e.value[0].value
  productPickerShow.value = false
}

// 提交表单
async function handleSubmit() {
  try {
    // 表单验证
    await formRef.value?.validate()

    loading.value = true

    // 调用添加客户API
    const res = await addCustomer(formData)

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
    console.error('添加客户失败:', error)
    // 表单验证失败或其他错误
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.customer-add-container {
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
