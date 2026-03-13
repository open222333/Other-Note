# JavaScript Node 模組 axios(基於 Promise 的 HTTP 客戶端)

```
axios 是一個基於 Promise 的 HTTP 客戶端，可用於瀏覽器和 Node.js 環境。
它使得在應用中發起 HTTP 請求更加方便和靈活。
axios 允許你發起異步請求，處理響應數據，並且可以在請求和響應階段應用攔截器。
```

## 目錄

- [JavaScript Node 模組 axios(基於 Promise 的 HTTP 客戶端)](#javascript-node-模組-axios基於-promise-的-http-客戶端)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[axios npm 頁面](https://www.npmjs.com/package/axios)

# 指令

```bash
# 安裝
npm install axios

yarn add axios
```

# 用法

```JavaScript
import axios from 'axios';

// 設定自定義標頭
const customHeaders = {
  'Authorization': 'Bearer YourAccessToken',
  'Content-Type': 'application/json',
  // 其他自定義標頭...
};

// 發送 GET 請求帶有自定義標頭
axios.get('https://api.example.com/data', { headers: customHeaders })
  .then(response => {
    console.log('響應:', response.data);
  })
  .catch(error => {
    console.error('錯誤:', error);
  });

// 發送 POST 請求帶有自定義標頭
const data = { name: 'John', age: 30 };
axios.post('https://api.example.com/save', data, { headers: customHeaders })
  .then(response => {
    console.log('響應:', response.data);
  })
  .catch(error => {
    console.error('錯誤:', error);
  });
```