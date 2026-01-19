# 部署指南

本文档说明如何将助贷CRM移动端应用部署到不同平台。

## 目录

- [环境准备](#环境准备)
- [H5 部署](#h5-部署)
- [微信小程序部署](#微信小程序部署)
- [App 部署](#app-部署)

---

## 环境准备

### 1. 安装依赖

```bash
npm install
# 或
pnpm install
```

### 2. 配置环境变量

创建 `.env.production` 文件：

```env
# 生产环境 API 地址
VITE_API_BASE_URL=https://api.yourdomain.com
```

---

## H5 部署

### 1. 构建生产版本

```bash
npm run build:h5
```

构建产物位于 `dist/build/h5/` 目录。

### 2. 部署到服务器

#### 方式一：静态文件服务器

使用 nginx 配置静态文件服务：

```nginx
server {
    listen 80;
    server_name crm.yourdomain.com;

    root /var/www/crm-mobile/dist/build/h5;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # API 代理
    location /api/ {
        proxy_pass https://api.yourdomain.com;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    # gzip 压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;
}
```

#### 方式二：CDN 部署

将 `dist/build/h5/` 目录下的所有文件上传到 CDN：

1. 登录 CDN 控制台
2. 创建静态网站托管
3. 上传构建产物
4. 配置 HTTPS 和自定义域名

### 3. 验证部署

访问部署地址，检查：
- 页面是否正常加载
- API 请求是否正常
- 登录功能是否正常

---

## 微信小程序部署

### 1. 构建小程序

```bash
npm run build:mp-weixin
```

构建产物位于 `dist/build/mp-weixin/` 目录。

### 2. 配置小程序

#### 2.1 使用微信开发者工具

1. 下载并安装 [微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)
2. 打开开发者工具
3. 导入项目，选择 `dist/build/mp-weixin/` 目录
4. 填写 AppID（需要在微信公众平台申请）

#### 2.2 配置服务器域名

在 [微信公众平台](https://mp.weixin.qq.com/) 配置服务器域名：

```
开发 -> 开发管理 -> 开发设置 -> 服务器域名
```

添加以下域名：
- **request 合法域名**: `https://api.yourdomain.com`
- **uploadFile 合法域名**: `https://api.yourdomain.com`
- **downloadFile 合法域名**: `https://api.yourdomain.com`

### 3. 上传代码

在微信开发者工具中：
1. 点击「上传」按钮
2. 填写版本号和项目备注
3. 确认上传

### 4. 提交审核

在 [微信公众平台](https://mp.weixin.qq.com/)：

1. 进入「版本管理」
2. 选择刚上传的版本
3. 点击「提交审核」
4. 填写审核信息
5. 等待审核通过

### 5. 发布线上

审核通过后，点击「发布」按钮将小程序发布上线。

---

## App 部署

### 1. 构建应用

```bash
npm run build:app
```

构建产物位于 `dist/build/app/` 目录。

### 2. 使用 HBuilderX 打包

#### 2.1 安装 HBuilderX

下载并安装 [HBuilderX](https://www.dcloud.io/hbuilderx.html)

#### 2.2 导入项目

1. 打开 HBuilderX
2. 文件 -> 导入 -> 从本地目录导入
3. 选择 `crm-mobile` 项目目录

#### 2.3 发行应用

**Android APK**:
1. 右键项目 -> 发行 -> 原生App-云打包
2. 选择 Android 平台
3. 选择证书（或使用 DCloud 公用证书）
4. 点击打包
5. 等待打包完成，下载 APK

**iOS IPA**:
1. 右键项目 -> 发行 -> 原生App-云打包
2. 选择 iOS 平台
3. 上传 iOS 证书和描述文件
4. 点击打包
5. 等待打包完成，下载 IPA

### 3. 本地打包（可选）

如需本地打包，需要配置 Android Studio 或 Xcode 环境。

详细步骤参考：[本地打包指南](https://nativesupport.dcloud.net.cn/AppDocs/usemodule/androidpack)

### 4. 发布应用

#### Android

将 APK 上传到各应用商店：
- [华为应用市场](https://developer.huawei.com/consumer/cn/service/josp/agc/index.html)
- [小米应用商店](https://dev.mi.com/distribute)
- [OPPO 开放平台](https://open.oppomobile.com/)
- [VIVO 开放平台](https://dev.vivo.com.cn/)
- [应用宝](https://open.tencent.com/)

#### iOS

将 IPA 上传到 App Store Connect：
1. 登录 [App Store Connect](https://appstoreconnect.apple.com/)
2. 创建应用
3. 上传 IPA
4. 填写应用信息
5. 提交审核

---

## 环境配置检查清单

### 开发环境

- [ ] Node.js >= 16.x
- [ ] npm >= 8.x 或 pnpm >= 7.x
- [ ] 依赖已安装

### 生产环境

- [ ] `.env.production` 文件已配置
- [ ] API 地址正确
- [ ] 服务器域名已配置（小程序）

### 发布前检查

- [ ] 代码已通过 ESLint 检查
- [ ] 代码已格式化
- [ ] TypeScript 类型检查通过
- [ ] 测试功能正常
- [ ] 版本号已更新

---

## 常见问题

### Q1: H5 部署后刷新 404

**原因**: HTML5 History 模式需要服务器配置。

**解决**: 配置 nginx `try_files` 指向 `index.html`。

### Q2: 小程序请求失败

**原因**: 服务器域名未配置或未备案。

**解决**:
1. 在微信公众平台配置合法域名
2. 确保域名已备案
3. 使用 HTTPS 协议

### Q3: App 打包失败

**原因**: 证书配置错误或版本冲突。

**解决**:
1. 检查证书是否正确
2. 更新 HBuilderX 到最新版本
3. 检查 manifest.json 配置

### Q4: 构建后白屏

**原因**: 路径配置错误或资源加载失败。

**解决**:
1. 检查 `vite.config.ts` 的 `base` 配置
2. 检查 `manifest.json` 的路径配置
3. 查看控制台错误信息

---

## 版本更新

### H5 更新

直接替换服务器上的构建产物即可。

### 小程序更新

1. 构建新版本
2. 上传新版本代码
3. 提交审核或设为体验版

### App 更新

1. 构建新版本 APK/IPA
2. 上传到应用商店
3. 提交审核
4. 审核通过后发布

或在应用内集成热更新（UniApp 云端一体方案）。
