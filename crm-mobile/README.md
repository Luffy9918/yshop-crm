# 助贷CRM移动端

> 基于 UniApp + Vue 3 + TypeScript 开发的跨平台移动应用

## 项目简介

助贷CRM移动端是助贷CRM系统的移动应用，为业务员提供外勤场景下的客户管理、拨打和跟进功能。

## 主要功能

### 已完成功能

- ✅ **用户认证**
  - 手机号验证码登录
  - Token 自动刷新机制
  - 登录状态持久化

- ✅ **客户管理**
  - 客户列表（下拉刷新、上拉加载更多）
  - 客户详情查看
  - 添加/编辑客户
  - 客户状态筛选
  - 客户搜索

- ✅ **跟进记录**
  - 添加跟进记录
  - 图片上传支持
  - 跟进历史查看
  - 跟进时间记录

- ✅ **数据统计**
  - 数据概览卡片（客户数、拨打次数、成交数、转化率）
  - 客户状态分布图表
  - 数据来源统计
  - 业绩趋势图表（7天）
  - 时间周期筛选（今日/本周/本月/全部）

- ✅ **SIP 软电话**
  - 拨打电话功能
  - 通话状态显示
  - 通话计时
  - 通话备注

- ✅ **性能优化**
  - 代码分割和懒加载
  - 生产环境自动移除 console
  - 构建资源 Hash 缓存

- ✅ **错误处理**
  - 全局错误捕获
  - 统一错误提示
  - API 错误处理

## 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | UniApp, Vue 3 (Composition API) |
| 语言 | TypeScript |
| 状态管理 | Pinia |
| UI 组件 | uView Plus 2.x |
| 构建工具 | Vite 5.x |
| 代码规范 | ESLint, Prettier |

## 项目结构

```
crm-mobile/
├── src/
│   ├── api/                 # API 接口
│   │   ├── auth.ts         # 认证接口
│   │   ├── customer.ts     # 客户接口
│   │   └── index.ts        # 统一导出
│   ├── components/          # 公共组件
│   │   ├── AppNavbar/      # 导航栏
│   │   ├── CustomerCard/   # 客户卡片
│   │   ├── StatusTag/      # 状态标签
│   │   ├── EmptyState/     # 空状态
│   │   ├── LoadingSpinner/ # 加载动画
│   │   ├── TabBar/         # 底部导航
│   │   ├── CallingDialog/  # 通话对话框
│   │   └── index.ts        # 组件导出
│   ├── pages/              # 页面
│   │   ├── login/          # 登录页
│   │   ├── index/          # 首页
│   │   ├── customer/       # 客户管理
│   │   │   ├── list.vue    # 客户列表
│   │   │   ├── detail.vue  # 客户详情
│   │   │   ├── add.vue     # 添加客户
│   │   │   ├── edit.vue    # 编辑客户
│   │   │   └── follow.vue  # 添加跟进
│   │   └── mine/           # 个人中心
│   │       ├── index.vue   # 我的
│   │       └── statistics.vue  # 数据统计
│   ├── store/              # 状态管理
│   │   ├── user.ts         # 用户状态
│   │   └── index.ts        # Store 导出
│   ├── utils/              # 工具函数
│   │   ├── request.ts      # 网络请求
│   │   ├── types.ts        # 类型定义
│   │   ├── sip.ts          # SIP 电话
│   │   └── errorHandler.ts # 错误处理
│   ├── App.vue             # 根组件
│   ├── main.ts             # 入口文件
│   ├── pages.json          # 页面配置
│   └── manifest.json       # 应用配置
├── .eslintrc.js            # ESLint 配置
├── .prettierrc.js          # Prettier 配置
├── vite.config.ts          # Vite 配置
├── package.json            # 项目依赖
└── tsconfig.json           # TypeScript 配置
```

## 快速开始

### 环境要求

- Node.js >= 16.x
- npm >= 8.x 或 pnpm >= 7.x

### 安装依赖

```bash
npm install
# 或
pnpm install
```

### 开发模式

```bash
npm run dev
```

访问 http://localhost:3000

### 构建发布

```bash
# H5
npm run build:h5

# 微信小程序
npm run build:mp-weixin

# App
npm run build:app
```

## 代码规范

### 代码检查

```bash
# 运行 ESLint
npm run lint

# 自动修复
npm run lint:fix
```

### 代码格式化

```bash
npm run format
```

### 类型检查

```bash
npm run type-check
```

## 环境变量

在项目根目录创建 `.env` 文件：

```env
# API 基础地址
VITE_API_BASE_URL=http://localhost:8080
```

## 页面说明

### 登录页 (`/pages/login/index`)
- 手机号输入
- 验证码登录
- 自动登录

### 首页 (`/pages/index/index`)
- 工作概览
- 快捷入口
- 待办事项

### 客户列表 (`/pages/customer/list`)
- 客户搜索
- 状态筛选
- 下拉刷新、上拉加载

### 客户详情 (`/pages/customer/detail`)
- 客户基本信息
- 跟进记录
- 拨打电话

### 数据统计 (`/pages/mine/statistics`)
- 数据概览
- 客户状态分布
- 数据来源统计
- 业绩趋势

### 个人中心 (`/pages/mine/index`)
- 用户信息
- 数据统计入口
- 退出登录

## 开发指南

### 组件使用

```vue
<template>
  <CustomerCard
    :customer="customerData"
    @click="handleDetail"
    @call="handleCall"
    @follow="handleFollow"
  />
</template>

<script setup lang="ts">
import { CustomerCard } from '@/components'
import type { Customer } from '@/components'

const customerData: Customer = {
  id: 1,
  name: '张三',
  phone: '13800138000',
  status: 'new',
  // ...
}
</script>
```

### API 调用

```typescript
import { getCustomerList, addCustomer } from '@/api'

// 获取客户列表
const res = await getCustomerList({ page: 1, limit: 20 })

// 添加客户
await addCustomer({
  name: '张三',
  phone: '13800138000',
  status: 'new'
})
```

### 状态管理

```typescript
import { useUserStore } from '@/store'

const userStore = useUserStore()

// 登录
await userStore.login('13800138000', '123456')

// 获取用户信息
console.log(userStore.userInfo)

// 退出登录
userStore.logout()
```

## 版本历史

### v1.0.0 (2024-01)
- ✅ 用户认证功能
- ✅ 客户管理功能
- ✅ 跟进记录功能
- ✅ SIP 软电话集成
- ✅ 数据统计功能
- ✅ 性能优化
- ✅ 错误处理

## 许可证

MIT
