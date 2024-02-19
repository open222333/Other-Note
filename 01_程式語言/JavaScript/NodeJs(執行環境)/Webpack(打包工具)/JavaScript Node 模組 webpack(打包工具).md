# JavaScript Node 模組 webpack(打包工具)

```
Webpack 是一個現代的 JavaScript 模組化打包工具。它主要用於將多個前端資源，如 JavaScript、CSS、圖片等，打包成一個或多個最終部署的檔案，以提高前端應用程式的性能和效能。

以下是 Webpack 的一些主要特點和概念：

模組化打包： Webpack 支援 CommonJS、AMD、ES6 等模組系統，使得應用程式的代碼可以模組化組織。

入口點（Entry）： 定義應用程式的入口點，Webpack 從這個入口點開始構建應用程式的依賴圖。

載入器（Loader）： Webpack 使用載入器來處理非 JavaScript 文件。載入器允許你在 import 進應用程式的非 JavaScript 文件上應用轉換或處理。

插件（Plugin）： Webpack 使用插件來執行各種任務，例如壓縮代碼、提取共享代碼、自動生成 HTML 頁面等。

分割代碼（Code Splitting）： Webpack 支援代碼分割，允許你將應用程式打包成多個檔案，以便更有效地管理資源和提高加載速度。

熱模組替換（Hot Module Replacement，HMR）： Webpack 的 HMR 功能使得在應用程式運行時，可以替換、添加或刪除模組，並在不重新載入整個頁面的情況下進行實時更新。

生態系統： Webpack 有豐富的生態系統，可以與各種前端工具和框架（如 React、Vue、Angular）無縫集成。
```

## 目錄

- [JavaScript Node 模組 webpack(打包工具)](#javascript-node-模組-webpack打包工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[webpack npm 頁面](https://www.npmjs.com/package/webpack)

[關於 Webpack，它是什麼？能夠做什麼？為什麼？怎麼做？— freeCodeCamp 的筆記](https://askie.today/what-is-webpack/)

[前端模組化與打包工具 (Webpack、Rollup、Parcel)](https://linyencheng.github.io/2022/10/05/relationships-between-frontend-and-backend/js-module-loader/)

# 指令

確保開發環境中已經安裝了 Node.js 和 npm

```bash
# 安裝 將 Webpack 安裝為開發依賴項（--save-dev 表示開發依賴項）
npm install webpack webpack-cli --save-dev
```

# 用法

基本配置文件 webpack.config.js

```JavaScript
const path = require('path');

module.exports = {
  entry: './src/index.js',
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'dist'),
  },
};
```

開始使用 Webpack 進行打包

```bash
npx webpack
```