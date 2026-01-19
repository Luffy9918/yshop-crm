<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="数据源编码" prop="sourceCode">
        <el-input
          v-model="queryParams.sourceCode"
          placeholder="请输入数据源编码"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="渠道" prop="channel">
        <el-input
          v-model="queryParams.channel"
          placeholder="请输入渠道"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['crm:data-source:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-upload
          ref="uploadRef"
          :limit="1"
          :http-request="handleImport"
          :show-file-list="false"
          accept=".xlsx,.xls"
          v-hasPermi="['crm:data-source:import']"
        >
          <el-button type="warning" plain><Icon icon="ep:upload" class="mr-5px" /> 导入</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="数据源编码" align="center" prop="sourceCode" width="150" />
      <el-table-column label="数据源名称" align="center" prop="sourceName" width="150" />
      <el-table-column label="渠道" align="center" prop="channel" width="120" />
      <el-table-column label="获客成本" align="center" prop="acquireCost" width="120">
        <template #default="scope">
          <span>¥{{ scope.row.acquireCost }}</span>
        </template>
      </el-table-column>
      <el-table-column label="获客时间" align="center" prop="acquireTime" width="180">
        <template #default="scope">
          <span v-if="scope.row.acquireTime">{{ formatDate(scope.row.acquireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="区域" align="center" prop="region" width="120" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" width="200" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180"
      />
      <el-table-column label="操作" align="center" fixed="right" width="200">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['crm:data-source:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['crm:data-source:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <DataSourceForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts" name="CrmDataSource">
import { dateFormatter } from '@/utils/formatTime'
import { DataSourceApi, DataSourceVO } from '@/api/crm/datasource'
import DataSourceForm from './DataSourceForm.vue'

/** 数据源管理 列表 */
defineOptions({ name: 'CrmDataSource' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<DataSourceVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  sourceCode: undefined,
  channel: undefined
})
const queryFormRef = ref() // 搜索的表单
const uploadRef = ref() // 上传组件

/** 格式化日期 */
const formatDate = (date: Date) => {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await DataSourceApi.getDataSourcePage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await DataSourceApi.deleteDataSource(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导入操作 */
const handleImport = async (options: any) => {
  try {
    const formData = new FormData()
    formData.append('file', options.file)
    const data = await DataSourceApi.importExcel(formData)
    if (data) {
      message.success('导入成功，共导入 ' + data + ' 条数据')
      await getList()
    }
  } catch (error) {
    message.error('导入失败：' + error.message)
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
