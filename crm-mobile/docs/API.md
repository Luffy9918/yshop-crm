# API 接口文档

## 基础信息

- **Base URL**: `http://localhost:8080` (开发环境)
- **认证方式**: Bearer Token
- **响应格式**: JSON

## 通用响应格式

```typescript
interface HttpResponse<T = any> {
  code: number      // 状态码，0 表示成功
  data: T           // 响应数据
  msg: string       // 响应消息
}
```

## 认证接口

### 1. 发送验证码

**接口地址**: `POST /api/auth/send-code`

**请求参数**:
```typescript
interface SendCodeParams {
  phone: string  // 手机号
}
```

**响应数据**:
```typescript
interface SendCodeResult {
  success: boolean
}
```

**示例**:
```javascript
const res = await sendCode({ phone: '13800138000' })
// res.code === 0 表示发送成功
```

---

### 2. 验证码登录

**接口地址**: `POST /api/auth/login`

**请求参数**:
```typescript
interface LoginParams {
  phone: string  // 手机号
  code: string   // 验证码
}
```

**响应数据**:
```typescript
interface LoginResult {
  accessToken: string      // 访问令牌
  refreshToken: string     // 刷新令牌
  expiresAt: number        // 过期时间戳
  userInfo: UserInfo       // 用户信息
}

interface UserInfo {
  id: number
  name: string
  phone: string
  avatar?: string
  role: string
}
```

**示例**:
```javascript
const res = await login({ phone: '13800138000', code: '123456' })
if (res.code === 0) {
  const { accessToken, userInfo } = res.data
  // 保存 token，更新用户状态
}
```

---

### 3. 刷新令牌

**接口地址**: `POST /api/auth/refresh-token`

**请求参数**:
```typescript
interface RefreshTokenParams {
  refreshToken: string
}
```

**响应数据**: 与登录接口相同

---

### 4. 获取用户信息

**接口地址**: `GET /api/auth/user-info`

**请求头**:
```
Authorization: Bearer {accessToken}
```

**响应数据**: `UserInfo`

---

### 5. 退出登录

**接口地址**: `POST /api/auth/logout`

**请求头**:
```
Authorization: Bearer {accessToken}
```

**响应数据**: `null`

---

## 客户接口

### 1. 获取客户列表

**接口地址**: `GET /api/customer/list`

**请求参数**:
```typescript
interface CustomerQueryParams {
  page?: number          // 页码，默认 1
  limit?: number         // 每页数量，默认 20
  status?: string        // 客户状态: new/following/deal/lost
  keyword?: string       // 搜索关键词（姓名或手机号）
}
```

**响应数据**:
```typescript
interface CustomerListResult {
  list: Customer[]
  total: number
  page: number
  limit: number
}

interface Customer {
  id: number
  name: string
  phone: string
  avatar?: string
  status: string         // new/following/deal/lost
  dataSource: string     // online/referral/telemarketing/offline/other
  product?: string
  followCount: number
  lastFollowTime?: string
  createTime: string
}
```

**示例**:
```javascript
// 获取第一页的新线索客户
const res = await getCustomerList({
  page: 1,
  limit: 20,
  status: 'new'
})
```

---

### 2. 获取客户详情

**接口地址**: `GET /api/customer/{id}`

**请求头**:
```
Authorization: Bearer {accessToken}
```

**响应数据**: `Customer`

**示例**:
```javascript
const res = await getCustomerDetail(123)
if (res.code === 0) {
  const customer = res.data
  console.log(customer.name)
}
```

---

### 3. 添加客户

**接口地址**: `POST /api/customer/add`

**请求头**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**请求参数**:
```typescript
interface AddCustomerParams {
  name: string
  phone: string
  status?: string
  dataSource?: string
  productId?: string
  loanAmount?: string
  remark?: string
}
```

**响应数据**: `Customer` (新创建的客户)

**示例**:
```javascript
const res = await addCustomer({
  name: '张三',
  phone: '13800138000',
  status: 'new',
  dataSource: 'online'
})
```

---

### 4. 更新客户

**接口地址**: `PUT /api/customer/{id}`

**请求头**: 同添加客户

**请求参数**: `AddCustomerParams`

**响应数据**: `Customer` (更新后的客户)

**示例**:
```javascript
const res = await updateCustomer(123, {
  name: '张三',
  status: 'following'
})
```

---

### 5. 删除客户

**接口地址**: `DELETE /api/customer/{id}`

**请求头**:
```
Authorization: Bearer {accessToken}
```

**响应数据**: `null`

---

## 跟进记录接口

### 1. 获取跟进记录

**接口地址**: `GET /api/follow/list`

**请求参数**:
```typescript
interface FollowQueryParams {
  customerId: number
  page?: number
  limit?: number
}
```

**响应数据**:
```typescript
interface FollowListResult {
  list: FollowRecord[]
  total: number
}

interface FollowRecord {
  id: number
  customerId: number
  title: string
  content: string
  images?: string[]
  followTime: string
  createBy: string
  createTime: string
}
```

---

### 2. 添加跟进记录

**接口地址**: `POST /api/follow/add`

**请求头**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**请求参数**:
```typescript
interface AddFollowParams {
  customerId: number
  title: string
  content: string
  images?: string[]
  followTime?: string
}
```

**响应数据**: `FollowRecord`

**示例**:
```javascript
const res = await addFollowRecord({
  customerId: 123,
  title: '电话跟进',
  content: '客户表示有意向，待进一步沟通',
  images: ['https://example.com/image1.jpg']
})
```

---

## 数据统计接口

### 1. 获取统计数据

**接口地址**: `GET /api/statistics/overview`

**请求参数**:
```typescript
interface StatisticsParams {
  period: 'today' | 'week' | 'month' | 'all'
}
```

**响应数据**:
```typescript
interface StatisticsData {
  overview: {
    totalCustomers: number
    totalCalls: number
    totalDeals: number
    conversionRate: number
  }
  statusDistribution: StatusDistribution[]
  sourceDistribution: SourceDistribution[]
  trendData: TrendData[]
}

interface StatusDistribution {
  status: string
  label: string
  count: number
  percentage: number
}

interface SourceDistribution {
  source: string
  label: string
  count: number
  percentage: number
}

interface TrendData {
  date: string
  newCustomers: number
  deals: number
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 0 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权，需要登录 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

---

## 完整调用示例

```typescript
import { getCustomerList, addCustomer } from '@/api'

// 获取客户列表
async function loadCustomers() {
  try {
    const res = await getCustomerList({
      page: 1,
      limit: 20,
      status: 'new'
    })

    if (res.code === 0) {
      console.log('客户列表:', res.data.list)
      console.log('总数:', res.data.total)
    } else {
      uni.showToast({
        title: res.msg || '加载失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('请求失败:', error)
  }
}

// 添加客户
async function addNewCustomer() {
  try {
    const res = await addCustomer({
      name: '张三',
      phone: '13800138000',
      status: 'new',
      dataSource: 'online'
    })

    if (res.code === 0) {
      uni.showToast({
        title: '添加成功',
        icon: 'success'
      })
    }
  } catch (error) {
    console.error('添加失败:', error)
  }
}
```
