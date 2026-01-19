import { createSSRApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import uViewPlus from 'uview-plus'
import { initGlobalErrorHandler, createVueErrorHandler } from './utils/errorHandler'

export function createApp() {
  const app = createSSRApp(App)
  const pinia = createPinia()

  app.use(pinia)
  app.use(uViewPlus)

  // 初始化全局错误处理
  initGlobalErrorHandler()

  // Vue 错误处理
  app.config.errorHandler = createVueErrorHandler()

  return {
    app,
    pinia
  }
}

createApp().app.mount('#app')
