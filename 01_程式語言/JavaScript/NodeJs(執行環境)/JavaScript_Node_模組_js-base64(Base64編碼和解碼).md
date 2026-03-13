# JavaScript Node 模組 js-base64(Base64編碼和解碼)

```
用於在JavaScript中進行Base64編碼和解碼的庫。
它允許將數據（比如字符串或二進制數據）轉換為Base64編碼，以及將Base64編碼轉換回原始數據。
```

## 目錄

- [JavaScript Node 模組 js-base64(Base64編碼和解碼)](#javascript-node-模組-js-base64base64編碼和解碼)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[js-base64 npm 頁面](https://www.npmjs.com/package/js-base64)

# 指令

```bash
# 安裝
npm install js-base64

yarn add js-base64
```

# 用法

```JavaScript
// 導入js-base64模塊
import { Base64 } from 'js-base64';

// 要編碼的原始字符串
const originalString = 'Hello, this is a test string.';

// 執行 Base64 編碼
const encodedString = Base64.encode(originalString);
console.log(encodedString); // 輸出：SGVsbG8sIHRoaXMgaXMgYSB0ZXN0IHN0cmluZy4=

// 執行 Base64 解碼
const decodedString = Base64.decode(encodedString);
console.log(decodedString); // 輸出：Hello, this is a test string.
```