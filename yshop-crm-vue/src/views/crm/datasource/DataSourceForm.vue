<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="数据源编码" prop="sourceCode">
        <el-input v-model="formData.sourceCode" placeholder="请输入数据源编码" />
      </el-form-item>
      <el-form-item label="数据源名称" prop="sourceName">
        <el-input v-model="formData.sourceName" placeholder="请输入数据源名称" />
      </el-form-item>
      <el-form-item label="渠道" prop="channel">
        <el-input v-model="formData.channel" placeholder="请输入渠道" />
      </el-form-item>
      <el-form-item label="获客成本" prop="acquireCost">
        <el-input-number v-model="formData.acquireCost" :min="0" :precision="2" placeholder="请输入获客成本" />
      </el-form-item>
      <el-form-item label="获客时间" prop="acquireTime">
        <el-date-picker
          v-model="formData.acquireTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择获客时间"
        />
      </el-form-item>
      <el-form-item label="区域" prop="region">
        <el-input v-model="formData.region" placeholder="请输入区域" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts" name="DataSourceForm">
import { DataSourceApi, DataSourceVO } from '@/api/crm/datasource'

/** 数据源 表单 */
defineOptions({ name: 'DataSourceForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  sourceCode: undefined,
  sourceName: undefined,
  channel: undefined,
  acquireCost: undefined,
  acquireTime: undefined,
  region: undefined,
  status: 1,
  remark: undefined
})
const formRules = reactive({
  sourceCode: [{ required: true, message: '数据源编码不能为空', trigger: 'blur' }],
  sourceName: [{ required: true, message: '数据源名称不能为空', trigger: 'blur' }],
  channel: [{ required: true, message: '渠道不能为空', trigger: 'blur' }],
  acquireCost: [{ required: true, message: '获客成本不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type) + '数据源'
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await DataSourceApi.getDataSource(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as DataSourceVO
    if (formType.value === 'create') {
      await DataSourceApi.createDataSource(data)
      message.success(t('common.createSuccess'))
    } else {
      await DataSourceApi.updateDataSource(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    sourceCode: undefined,
    sourceName: undefined,
    channel: undefined,
    acquireCost: undefined,
    acquireTime: undefined,
    region: undefined,
    status: 1,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>
