import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'

export default defineConfig({
  plugins: [uni()],
  server: {
    port: 3000,
    host: '0.0.0.0',
    open: false
  },
  resolve: {
    alias: {
      '@': '/src'
    }
  },
  build: {
    // 启用代码压缩
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true, // 移除 console
        drop_debugger: true // 移除 debugger
      }
    },
    // 分包策略
    rollupOptions: {
      output: {
        manualChunks: {
          // 将 Vue 相关库打包到单独的 chunk
          'vue-vendor': ['vue', 'pinia'],
          // 将 uView Plus UI 库单独打包
          'uview-vendor': ['uview-plus']
        },
        // 文件名 hash
        chunkFileNames: 'js/[name]-[hash].js',
        entryFileNames: 'js/[name]-[hash].js',
        assetFileNames: '[ext]/[name]-[hash].[ext]'
      }
    },
    // chunk 大小警告阈值 (KB)
    chunkSizeWarningLimit: 500
  },
  // 优化依赖预构建
  optimizeDeps: {
    include: ['vue', 'pinia', 'uview-plus']
  }
})
