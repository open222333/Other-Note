# JavaScript Node 模組 cloudflare(cloudflare API)

```
用於 Node.js 的Cloudflare v4 API綁定，提供了一個“麵包”（瀏覽、讀取、編輯、添加和刪除）界面。
```

## 目錄

- [JavaScript Node 模組 cloudflare(cloudflare API)](#javascript-node-模組-cloudflarecloudflare-api)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[cloudflare npm 頁面](https://www.npmjs.com/package/cloudflare)

[cloudflare api 文檔](https://cloudflare.github.io/node-cloudflare/)

[cloudflare JSDoc](https://cloudflare.github.io/node-cloudflare/)

# 指令

```bash
# 安裝
npm install cloudflare

npm i cloudflare
```

# 用法

```JavaScript
var cloudflare = require('cloudflare')({
  email: 'you@example.com',
  key: 'your Cloudflare API key'
});
```