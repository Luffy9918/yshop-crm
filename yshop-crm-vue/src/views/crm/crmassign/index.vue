<template>
  <ContentWrap>
    <el-card shadow="never">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 手动分配 -->
        <el-tab-pane label="手动分配" name="manual">
          <el-form
            ref="manualFormRef"
            :model="manualForm"
            :rules="manualRules"
            label-width="120px"
            class="assign-form"
          >
            <el-form-item label="选择客户" prop="customerId">
              <el-select
                v-model="manualForm.customerId"
                placeholder="请选择客户"
                filterable
                clearable
                style="width: 100%"
              >
                <!-- TODO: 集成客户列表API -->
                <el-option
                  v-for="item in customerList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="分配给业务员" prop="staffId">
              <el-select
                v-model="manualForm.staffId"
                placeholder="请选择业务员"
                filterable
                clearable
                style="width: 100%"
              >
                <!-- TODO: 集成员工列表API -->
                <el-option
                  v-for="item in staffList"
                  :key="item.id"
                  :label="item.nickname"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="分配方式" prop="assignType">
              <el-radio-group v-model="batchForm.assignType">
                <el-radio label="balance">平均分配</el-radio>
                <el-radio label="smart">智能分配</el-radio>
              </el-radio-group>
              <div class="form-tip">选择批量分配的方式</div>
            </el-form-item>

            <el-form-item label="分配原因" prop="reason">
              <el-input
                v-model="manualForm.reason"
                type="textarea"
                :rows="3"
                placeholder="请输入分配原因（可选）"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleManualAssign" :loading="submitLoading">
                <Icon icon="ep:check" class="mr-5px" />
                确认分配
              </el-button>
              <el-button @click="resetManualForm">
                <Icon icon="ep:refresh" class="mr-5px" />
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 批量分配 -->
        <el-tab-pane label="批量分配" name="batch">
          <el-form
            ref="batchFormRef"
            :model="batchForm"
            :rules="batchRules"
            label-width="120px"
            class="assign-form"
          >
            <el-form-item label="选择客户" prop="customerIds">
              <el-select
                v-model="batchForm.customerIds"
                placeholder="请选择客户（可多选）"
                filterable
                clearable
                multiple
                style="width: 100%"
              >
                <!-- TODO: 集成客户列表API -->
                <el-option
                  v-for="item in customerList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
              <div class="form-tip">已选择 {{ batchForm.customerIds.length }} 个客户</div>
            </el-form-item>

            <el-form-item label="分配给业务员" prop="staffId">
              <el-select
                v-model="batchForm.staffId"
                placeholder="请选择业务员"
                filterable
                clearable
                style="width: 100%"
              >
                <!-- TODO: 集成员工列表API -->
                <el-option
                  v-for="item in staffList"
                  :key="item.id"
                  :label="item.nickname"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="分配方式" prop="assignType">
              <el-radio-group v-model="batchForm.assignType">
                <el-radio label="balance">平均分配</el-radio>
                <el-radio label="smart">智能分配</el-radio>
              </el-radio-group>
              <div class="form-tip">选择批量分配的方式</div>
            </el-form-item>

            <el-form-item label="分配原因" prop="reason">
              <el-input
                v-model="batchForm.reason"
                type="textarea"
                :rows="3"
                placeholder="请输入分配原因（可选）"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleBatchAssign" :loading="submitLoading">
                <Icon icon="ep:check" class="mr-5px" />
                批量分配
              </el-button>
              <el-button @click="resetBatchForm">
                <Icon icon="ep:refresh" class="mr-5px" />
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 自动分配 -->
        <el-tab-pane label="自动分配" name="auto">
          <el-form
            ref="autoFormRef"
            :model="autoForm"
            :rules="autoRules"
            label-width="120px"
            class="assign-form"
          >
            <el-form-item label="分配规则" prop="conditionType">
              <el-radio-group v-model="autoForm.conditionType">
                <el-radio label="balance">按平均分配</el-radio>
                <el-radio label="smart">智能分配</el-radio>
                <el-radio label="city">按城市分配</el-radio>
                <el-radio label="level">按级别分配</el-radio>
                <el-radio label="industry">按行业分配</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="条件值" prop="conditionValue">
              <el-input
                v-model="autoForm.conditionValue"
                placeholder="请输入条件值（如城市代码、级别ID等）"
                clearable
                style="width: 100%"
              >
                <template #append>
                  <el-button>选择</el-button>
                </template>
              </el-input>
              <div class="form-tip">根据选择的规则类型，输入对应的条件值</div>
            </el-form-item>

            <el-form-item label="分配数量" prop="count">
              <el-input-number
                v-model="autoForm.count"
                :min="1"
                :max="100"
                placeholder="分配数量"
                style="width: 200px"
              />
              <span class="ml-10px">个客户</span>
            </el-form-item>

            <el-form-item label="预览分配">
              <el-button @click="handlePreviewAssign" plain>
                <Icon icon="ep:view" class="mr-5px" />
                预览分配结果
              </el-button>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleAutoAssign" :loading="submitLoading">
                <Icon icon="ep:check" class="mr-5px" />
                开始自动分配
              </el-button>
              <el-button @click="resetAutoForm">
                <Icon icon="ep:refresh" class="mr-5px" />
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </ContentWrap>
</template>

<script setup lang="ts">
import { CustomerAssignApi, CustomerAssignReqVO, CustomerBatchAssignReqVO, CustomerAutoAssignReqVO } from '@/api/crm/crmassign'
import { ElMessage } from 'element-plus'

defineOptions({ name: 'CrmAssign' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const activeTab = ref('manual')
const submitLoading = ref(false)

// 客户列表 - TODO: 集成客户列表API
const customerList = ref<any[]>([])

// 员工列表 - TODO: 集成员工列表API
const staffList = ref<any[]>([])

// 手动分配表单
const manualFormRef = ref()
const manualForm = reactive<CustomerAssignReqVO>({
  customerId: undefined,
  staffId: undefined,
  reason: ''
})
const manualRules = reactive({
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  staffId: [{ required: true, message: '请选择业务员', trigger: 'change' }]
})

// 批量分配表单
const batchFormRef = ref()
const batchForm = reactive<CustomerBatchAssignReqVO>({
  customerIds: [],
  staffId: undefined,
  reason: '',
  assignType: 'balance' // 默认平均分配
})
const batchRules = reactive({
  customerIds: [
    { required: true, message: '请选择客户', trigger: 'change' },
    { type: 'array', min: 1, message: '至少选择一个客户', trigger: 'change' }
  ],
  staffId: [{ required: true, message: '请选择业务员', trigger: 'change' }]
})

// 自动分配表单
const autoFormRef = ref()
const autoForm = reactive<CustomerAutoAssignReqVO>({
  conditionType: 'balance',
  conditionValue: '',
  count: 10 // 默认分配10个客户
})
const autoRules = reactive({
  conditionType: [{ required: true, message: '请选择分配规则', trigger: 'change' }],
  conditionValue: [{ required: true, message: '请输入条件值', trigger: 'blur' }]
})

/** 手动分配 */
const handleManualAssign = async () => {
  await manualFormRef.value.validate()
  try {
    submitLoading.value = true
    await CustomerAssignApi.assign(manualForm)
    ElMessage.success('分配成功')
    resetManualForm()
  } catch (error) {
    ElMessage.error('分配失败：' + (error.message || '未知错误'))
  } finally {
    submitLoading.value = false
  }
}

/** 批量分配 */
const handleBatchAssign = async () => {
  await batchFormRef.value.validate()
  try {
    submitLoading.value = true
    const result = await CustomerAssignApi.batchAssign(batchForm)
    ElMessage.success(`成功分配 ${result} 个客户`)
    resetBatchForm()
  } catch (error) {
    ElMessage.error('分配失败：' + (error.message || '未知错误'))
  } finally {
    submitLoading.value = false
  }
}

/** 自动分配 */
const handleAutoAssign = async () => {
  await autoFormRef.value.validate()
  try {
    submitLoading.value = true
    await CustomerAssignApi.autoAssign(autoForm)
    ElMessage.success('自动分配成功')
    resetAutoForm()
  } catch (error) {
    ElMessage.error('分配失败：' + (error.message || '未知错误'))
  } finally {
    submitLoading.value = false
  }
}

/** 预览分配结果 */
const handlePreviewAssign = () => {
  // TODO: 实现预览功能
  ElMessage.info('预览功能开发中...')
}

/** 重置手动分配表单 */
const resetManualForm = () => {
  manualFormRef.value?.resetFields()
}

/** 重置批量分配表单 */
const resetBatchForm = () => {
  batchFormRef.value?.resetFields()
  // 重置后恢复默认值
  batchForm.assignType = 'balance'
}

/** 重置自动分配表单 */
const resetAutoForm = () => {
  autoFormRef.value?.resetFields()
  // 重置后恢复默认值
  autoForm.count = 10
}

/** 初始化 */
onMounted(() => {
  // TODO: 加载客户列表和员工列表
  // loadCustomerList()
  // loadStaffList()
})
</script>

<style scoped lang="scss">
.assign-form {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;

  .form-tip {
    font-size: 12px;
    color: #909399;
    margin-top: 5px;
  }

  .ml-10px {
    margin-left: 10px;
  }
}
</style>
