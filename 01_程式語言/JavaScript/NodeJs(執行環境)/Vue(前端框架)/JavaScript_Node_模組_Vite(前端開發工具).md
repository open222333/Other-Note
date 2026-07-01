# JavaScript Node 模組 Vite(前端開發工具)

```
Vite:

Vite 是一個由 Evan You（Vue.js 的創始人）領導的項目，是一個現代化的前端開發工具。
它的目標是提供一種快速、簡單、更可擴展的開發方式。Vite 支持 Vue.js、React、以及其他類似的框架。主要特點包括：
快速的開發伺服器： Vite 使用原生 ESM（ECMAScript 模塊）的開發伺服器，可以實現快速的冷啟動和即時的熱模組替換（HMR）。
即時編譯（Instant Pre-bundling）： Vite 使用即時編譯的方式，僅在需要時編譯所需的部分，而不是將整個應用程式打包。
支援多框架： Vite 不僅支援 Vue.js，還支援其他前端框架，例如 React。
內建 TypeScript 支援： Vite 內置對 TypeScript 的支援。
插件系統： Vite 提供了擴展的插件系統，可以方便地集成其他工具和服務。
Vite 的目標是為開發者提供更快速、更靈活的開發體驗。
```

## 目錄

- [JavaScript Node 模組 Vite(前端開發工具)](#javascript-node-模組-vite前端開發工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [安裝](#安裝)
- [指令](#指令)
  - [清除快取](#清除快取)
- [vite.config 設定](#viteconfigts-設定)
  - [manualChunks（程式碼分割）](#manualchunks程式碼分割)

## 參考資料

[Vite官方網站](https://vitejs.dev/)

[Vite官方中文文檔網站](https://cn.vitejs.dev/guide/)

### 心得相關

[前端开发能不学习webpack直接学vite吗?](https://www.zhihu.com/question/505684041)

# 安裝

```bash
# 全局安装 Vite
npm install -g create-vite
```

# 指令

```bash
# 創建一個新的 Vue 項目, my-project 替換為你想要的項目名稱
create-vite my-project
```

## 清除快取

Vite 的開發伺服器快取存放於 `node_modules/.vite`，遇到以下情況需手動清除：
- 修改 `vite.config.ts` 後仍看到舊行為
- 升級套件版本後出現奇怪錯誤
- HMR 熱更新失效或畫面不正確

```bash
# 清除 Vite 快取
rm -rf node_modules/.vite

# 重新啟動開發伺服器
npm run dev
```

```bash
# 若同時懷疑 node_modules 有問題，完整重裝
rm -rf node_modules/.vite node_modules
npm install
npm run dev
```

# vite.config.ts 設定

## manualChunks（程式碼分割）

Vite build 預設會把所有依賴打包進單一 `index.js`。透過 `manualChunks` 可將第三方套件拆成獨立 chunk，讓瀏覽器**長期快取 vendor 程式碼**，只在業務邏輯變動時重新下載 `index.js`。

```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          // Vue 生態系：版本幾乎不動，適合長快取
          'vendor-vue': ['vue', 'vue-router', 'pinia'],
          // UI 框架
          'vendor-bootstrap': ['bootstrap'],
        },
      },
    },
  },
})
```

效果對比（同專案實測）：

| | 優化前 | 優化後 |
|---|---|---|
| `index.js`（gzip） | 83 KB | 44 KB |
| `vendor-vue.js`（gzip） | — | 39 KB（可長期快取） |

注意事項：
- 只能放**有 JS entry point** 的套件（如 `vue`, `pinia`）
- `bootstrap-icons` 是純 CSS/字型套件，**不能**放入 `manualChunks`，否則 build 報錯
- 分割後的 chunk 由瀏覽器平行下載，實際載入速度取決於 HTTP/2 支援
