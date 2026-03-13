# JavaScript Node 模組 element-plus(UI元素)

```
```

## 目錄

- [JavaScript Node 模組 element-plus(UI元素)](#javascript-node-模組-element-plusui元素)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
	- [安裝](#安裝)
- [用法](#用法)
	- [瀏覽器直接引入](#瀏覽器直接引入)
		- [unpkg](#unpkg)
		- [jsDelivr](#jsdelivr)

## 參考資料

[element-plus npm 頁面](https://www.npmjs.com/package/element-plus)

[Element Plus 官方](https://element-plus.org/en-US/#/zh-CN)

[网站快速成型工具](https://element.eleme.io/#/zh-CN/component/layout)

[組件文檔](https://element-plus.org/zh-CN/component/button.html)

# 指令

## 安裝

```bash
# NPM
$ npm install element-plus --save

# Yarn
$ yarn add element-plus

# pnpm
$ pnpm install element-plus

# 引入 Element Plus 組件：
# 在你的 Vue 項目中，可以選擇全局引入或按需引入 Element Plus 組件。
# 通常，按需引入是比較推薦的做法，因為這樣可以減少打包後的體積。
npm install babel-plugin-component --save-dev
```

# 用法

```json
// 項目根目錄中的 babel.config.js（或 .babelrc）文件中添加如下配置
{
  "plugins": [
    [
      "component",
      {
        "libraryName": "element-plus",
        "styleLibraryName": "theme-chalk"
      }
    ]
  ]
}
```

## 瀏覽器直接引入

### unpkg

```html
<head>
  <!-- Import style -->
  <link rel="stylesheet" href="//unpkg.com/element-plus/dist/index.css" />
  <!-- Import Vue 3 -->
  <script src="//unpkg.com/vue@3"></script>
  <!-- Import component library -->
  <script src="//unpkg.com/element-plus"></script>
</head>
```

### jsDelivr

```html
<head>
  <!-- Import style -->
  <link
    rel="stylesheet"
    href="//cdn.jsdelivr.net/npm/element-plus/dist/index.css"
  />
  <!-- Import Vue 3 -->
  <script src="//cdn.jsdelivr.net/npm/vue@3"></script>
  <!-- Import component library -->
  <script src="//cdn.jsdelivr.net/npm/element-plus"></script>
</head>
```