# JavaScript Node 模組 Vue(前端框架)

```
Vue.js是一個用於建立使用者介面的開源Model–view–viewmodel前端JavaScript框架，也是一個建立單頁應用的Web應用框架。
```

## 目錄

- [JavaScript Node 模組 Vue(前端框架)](#javascript-node-模組-vue前端框架)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [Vue3相關](#vue3相關)
    - [Vite網站](#vite網站)
    - [正式部署相關](#正式部署相關)
    - [教學相關](#教學相關)
    - [UI元素](#ui元素)
    - [VSCode相關](#vscode相關)
    - [瀏覽器開發者插件](#瀏覽器開發者插件)
- [安裝](#安裝)
  - [Vue CLI](#vue-cli)
  - [docker部署](#docker部署)
- [指令](#指令)
  - [Vue CLI](#vue-cli-1)
- [專案結構](#專案結構)
- [用法](#用法)
  - [Vue3 指令](#vue3-指令)
- [VSCode套件](#vscode套件)
  - [Vetur](#vetur)
  - [Volar](#volar)
- [.gitignore範本](#gitignore範本)
- [範例](#範例)
  - [使用 HTML script 標籤引用 vue](#使用-html-script-標籤引用-vue)
  - [選項式API (Options API)](#選項式api-options-api)
  - [組合式API (Composition API)](#組合式api-composition-api)
  - [使用指定的端口](#使用指定的端口)

## 參考資料

[官方網站](https://cn.vuejs.org/)

### Vue3相關

[Vue3官方中文文档](https://cn.vuejs.org/guide/introduction.html)

[Vue3官方API文档](https://cn.vuejs.org/api/)

[Vue3官方示例](https://cn.vuejs.org/examples/#hello-world)

[Vue3 瀏覽器工具 Vue Devtools](https://devtools.vuejs.org/guide/installation.html)

[Built-in Directives(內置指令)](https://vuejs.org/api/built-in-directives.html)

[Vue3 組件](https://vuejs.org/guide/essentials/component-basics.html)

[Vue3 組件(中文)](https://cn.vuejs.org/guide/essentials/component-basics.html)

[Vue3 教程 - 菜鳥教程](https://www.runoob.com/vue3/vue3-tutorial.html)

### Vite網站

[Vite官方網站](https://vitejs.dev/)

### 正式部署相關

[生产部署](https://cn.vuejs.org/guide/best-practices/production-deployment.html)

### 教學相關

[重新認識 Vue.js](https://book.vue.tw/)

[Vue3 基礎教學【Proladon】](https://www.youtube.com/watch?v=FkVJCy3dao4&list=PLSCgthA1AnifSzKdpV4FWq1pLVF4FbZ4K)

[【 Tool 】透過 Docker 建立 Vue 開發環境](https://learningsky.io/use-docker-to-create-vue-development-environment/)

### UI元素

[Element Plus 官網](https://element-plus.org/en-US/)

[Tailwind UI](https://tailwindui.com/)

[Tailblocks](https://tailblocks.cc/)

[Tailwind Components](https://tailwindcomponents.com/)

[Tailwind Kit](https://www.tailwind-kit.com/components)

[Shades Generator for Tailwind](https://javisperez.github.io/tailwindcolorshades/)

[Creator vitawind v2](https://vitawind.vercel.app/scaffolding/creator/)

### VSCode相關

[[Vue] Day3 工具：VS Code 前端套件介紹](https://ithelp.ithome.com.tw/m/articles/10293208)

[【整理分享】6 個 Vue3 開發必備的 VSCode 外掛](https://tw511.com/a/01/47269.html)

[怎麼設定VSCode，蘇爽的偵錯Vue、React 程式碼！](https://tw511.com/a/01/43125.html)

### 瀏覽器開發者插件

[Vue Devtools](https://devtools.vuejs.org/)

[Chrome 插件 - Vue.js devtools](https://chrome.google.com/webstore/detail/vuejs-devtools/nhdogjmejiglipccpnnnanhbledajbpd)

[Firefox 所属插件页 - Vue.js devtools](https://addons.mozilla.org/en-US/firefox/addon/vue-js-devtools/)

# 安裝

## Vue CLI

```bash
# 全局安裝 Vue CLI
npm install -g @vue/cli
```

```bash
# 舊版
npm install -g vue-cli
```

## docker部署

```dockerfile
FROM node:14.18.2-buster

WORKDIR /app

COPY package*.json ./
RUN npm install

# 安裝常用工具和依賴
RUN apt-get update -y && \
    apt-get upgrade -y && \
    apt-get install -y \
        build-essential \
        curl \
        nmap \
        git \
        nano \
    && rm -rf /var/lib/apt/lists/*

# 全局安裝 Vue CLI
RUN npm install -g @vue/cli@4.5.15
```

```yml
version: '3'
services:
  vue_web:
    build:
      # Dockerfile 路徑
      context: ./frontend
      dockerfile: Dockerfile.custom
    volumes:
      - .:/app
    ports:
      - "8080:80"
    # 添加 serve 命令以啟動 Vue 開發伺服器
    command: ["npm", "run", "serve"]
```

# 指令

```bash
# Vue CLI 2.x 中的初始化命令
# webpack: 這是使用的模板名稱，表示使用 webpack 作為構建工具和打包工具。Webpack 是一個現代的 JavaScript 應用程式的靜態模塊打包工具。
vue init webpack project_name
```

創建專案

```bash
npm create vue@latest
```

```bash
cd vue_project_name
npm install
npm run dev
```

將應用程式發佈到生產環境時

會在 ./dist 資料夾中為你的應用程式建立一個生產環境的建置版本

```bash
npm run build
```

## Vue CLI

`創建一個新的 Vue 項目, my-project 替換為你想要的項目名稱`

```bash
vue create my-project
```

`啟動開發伺服器`

```bash
npm run serve
```

`檢視專案配置`

```bash
vue inspect
```

`添加 Vue 插件`

```bash
vue add @vue/cli-plugin-some-plugin
```

# 專案結構

```csharp
my-project/
  |- node_modules/    # 專案依賴的第三方模組
  |- public/          # 公共資源資料夾
  |   |- index.html   # 應用程式的入口 HTML 檔案
  |   |- favicon.ico  # 網站的圖示檔案
  |- src/             # 原始碼資料夾
  |   |- assets/      # 靜態資源資料夾（如圖片、樣式檔案等）
  |   |- components/  # Vue 元件資料夾
  |   |- views/       # 視圖元件資料夾
  |   |- App.vue      # 應用程式的根元件
  |   |- main.js      # 應用程式的入口檔案
  |- .gitignore       # Git 版本控制忽略檔案清單
  |- babel.config.js  # Babel 設定檔案
  |- package.json     # 專案設定和依賴清單
  |- README.md        # 專案說明文件
```

```
node_modules/：存放專案所依賴的第三方模組，該資料夾由 npm 或 yarn 自動安裝生成。
public/：公共資源資料夾，包含了專案的入口 HTML 檔案（index.html）、圖示檔案（favicon.ico）等靜態檔案。
src/：原始碼資料夾，是開發過程中的主要工作目錄。
assets/：靜態資源資料夾，存放專案所需的圖片、樣式檔案等。
components/：Vue 元件資料夾，存放專案的可重用元件。
views/：視圖元件資料夾，存放專案的頁面級元件。
App.vue：應用程式的根元件，是所有其他元件的容器。
main.js：應用程式的入口檔案，初始化 Vue 實例並掛載根元件。
.gitignore：Git 版本控制忽略檔案清單，用於指定哪些檔案和資料夾應該被排除在版本控制之外。
babel.config.js：Babel 設定檔案，用於配置 JavaScript 的轉譯規則。
package.json：專案設定和依賴清單，包含了專案的基本資訊、指令腳本、開發相依性和產品相依性等。
README.md：專案說明文件，用於描述專案的功能、使用方法、貢獻指南等資訊。
```

# 用法

## Vue3 指令

```
v-bind 用於將 Vue 實例的數據綁定到 HTML 元素的屬性上。
v-if 用於根據表達式的值來條件性地渲染元素或組件。
v-show Vue.js 提供的一種指令，用於根據表達式的值來條件性地顯示或隱藏元素。
v-for 用於根據數組或對象的屬性值來循環渲染元素或組件。
v-on 用於在 HTML 元素上綁定事件監聽器，使其能夠觸發 Vue 實例中的方法或函數。
v-model 用於在表單控件和 Vue 實例的數據之間創建雙向數據綁定。
```

# VSCode套件

## Vetur

```
Vetur 是 Vue 的一個工具包，不僅讓 Vue 的語法可以突出顯示，也提供了 Vue.js 的語言功能(例如：語法高亮、智慧感知、片段、格式)，讓我們在撰寫時可以更便利輕鬆。

而 Vetur 在維護上也處理得很好，甚至連 Vue3 Typescript 都有支援。
```

## Volar

```
相信使用 VSCode 開發 Vue2 的同學一定對 Vetur 外掛不會陌生，作為 Vue2 配套的 VSCode 外掛，它的主要作用是對 Vue 單檔案元件提供高亮、語法支援以及語法檢測。（學習視訊分享：）

而隨著 Vue3 正式版釋出，Vue 團隊官方推薦 Volar 外掛來代替 Vetur 外掛，不僅支援 Vue3 語言高亮、語法檢測，還支援 TypeScript 和基於 vue-tsc 的型別檢查功能
```

# .gitignore範本

```
.DS_Store
node_modules
/dist

# local env files
.env.local
.env.*.local

# Log files
npm-debug.log*
yarn-debug.log*
yarn-error.log*
pnpm-debug.log*

# Editor directories and files
.idea
.vscode
*.suo
*.ntvs*
*.njsproj
*.sln
*.sw?
```

# 範例

## 使用 HTML script 標籤引用 vue

jsDelivr：jsDelivr 是一個開源的 CDN 服務商，提供了廣泛的 npm 托管包的快速、可靠的分發。
它支持 JavaScript、CSS、圖像等資源的分發，並且提供了版本管理、自動壓縮等功能。

unpkg：unpkg 是另一個流行的開源 CDN 服務商，也提供了許多 npm 托管包的 CDN 分發。
它以簡單、直觀的方式提供了對各種 npm 包的訪問，並且支持通過版本號或標籤來引入特定版本的資源。

```html
<!-- Vue 2.x-->
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js" ></script>
<!-- Vue 3.x-->
<script src="https://cdn.jsdelivr.net/npm/vue@next"></script>
<script src="https://unpkg.com/vue@next"></script>
```

## 選項式API (Options API)

```vue
<script>
export default {
  // data() 傳回的屬性將會成為響應式的狀態
  // 並且暴露在 `this` 上
  data() {
    return {
      count: 0
    }
  },

  // methods 是一些用來更改狀態與觸發更新的函數
  // 它們可以在模板中作為事件處理器綁定
  methods: {
    increment() {
      this.count++
    }
  },

  // 生命週期鉤子會在元件生命週期的各個不同階段被呼叫
  // 例如這個函數就會在元件掛載完成後被呼叫
  mounted() {
    console.log(`The initial count is ${this.count}.`)
  }
}
</script>

<template>
  <button @click="increment">Count is: {{ count }}</button>
</template>
```

## 組合式API (Composition API)

```vue
<script setup>
import { ref, onMounted } from 'vue'

// 響應式狀態
const count = ref(0)

// 用來修改狀態、觸發更新的函數
function increment() {
  count.value++
}

// 生命週期鉤子
onMounted(() => {
  console.log(`The initial count is ${count.value}.`)
})
</script>

<template>
  <button @click="increment">Count is: {{ count }}</button>
</template>
```

## 使用指定的端口

打開 package.json 文件。

在 scripts 部分中找到 dev 命令。

添加 --port 選項，指定你想要的端口號。例如，使用端口號為 3000：

```json
"scripts": {
  "dev": "vue-cli-service serve --port 3000"
}
```

使用的是 Vite 作為開發伺服器

打開你的 vite.config.js 文件（如果專案中沒有，你可能需要創建一個）。

在 vite.config.js 中，你可以像下面這樣指定 server.port：

```JavaScript
export default {
  server: {
    port: 3000,
  },
};
```
