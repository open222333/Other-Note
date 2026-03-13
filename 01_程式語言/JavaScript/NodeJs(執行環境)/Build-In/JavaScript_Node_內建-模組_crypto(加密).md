# JavaScript Node 內建-模組 crypto(加密)

```
Node.js 的一個內建模組，用於實現各種加密功能，如哈希、密鑰生成、加密和解密等。
```

## 目錄

- [JavaScript Node 內建-模組 crypto(加密)](#javascript-node-內建-模組-crypto加密)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [](#)
- [用法](#用法)
  - [CCM模式](#ccm模式)

## 參考資料

[crypto npm 頁面](https://www.npmjs.com/package/crypto)

[crypto 官方api文檔](https://nodejs.org/docs/latest-v18.x/api/crypto.html)

[Node.js v22.2.0 documentation](https://nodejs.org/api/crypto.html)

###

[class-hash](https://nodejs.org/api/crypto.html#class-hash)

[CCM模式](https://nodejs.org/api/crypto.html#cryptohashalgorithm-data-outputencoding)

# 用法

## CCM模式

```JavaScript
const { Buffer } = require("node:buffer"); // 引入 Buffer 模組，用於處理二進位資料
const { createCipheriv, createDecipheriv, randomBytes } = require("node:crypto"); // 引入 crypto 模組中的必要方法

const key = "keykeykeykeykeykeykeykey"; // 定義密鑰（24 字節，對應 AES-192）
const nonce = randomBytes(12); // 生成 12 字節的隨機 nonce

const aad = Buffer.from("0123456789", "hex"); // 定義附加認證數據（AAD）

const cipher = createCipheriv("aes-192-ccm", key, nonce, {
  authTagLength: 16, // 設定認證標籤長度
});
const plaintext = "Hello world"; // 要加密的明文
cipher.setAAD(aad, {
  plaintextLength: Buffer.byteLength(plaintext), // 設定 AAD，並指定明文長度
});
const ciphertext = cipher.update(plaintext, "utf8"); // 加密明文
cipher.final(); // 完成加密操作
const tag = cipher.getAuthTag(); // 獲取認證標籤

// 現在可以傳輸 { ciphertext, nonce, tag } 到接收端。

const decipher = createDecipheriv("aes-192-ccm", key, nonce, {
  authTagLength: 16, // 設定解密器的認證標籤長度
});
decipher.setAuthTag(tag); // 設定認證標籤
decipher.setAAD(aad, {
  plaintextLength: ciphertext.length, // 設定 AAD，並指定密文長度
});
const receivedPlaintext = decipher.update(ciphertext, null, "utf8"); // 解密密文

try {
  decipher.final(); // 完成解密操作並進行認證檢查
} catch (err) {
  throw new Error("Authentication failed!", { cause: err }); // 如果認證失敗，拋出錯誤
}

console.log(receivedPlaintext); // 輸出解密後的明文
```

```
說明：
    密鑰和 nonce：key 是一個 24 字節的字串，用於 AES-192 加密。
    nonce 是一個隨機生成的 12 字節的字串，用於保證每次加密結果不同。
    
附加認證數據 (AAD)：aad 是用於認證的附加數據，在解密時需要相同的 AAD 來驗證數據的完整性和真實性。

加密過程：
    創建加密器 cipher。
    設定 AAD 及其對應的明文長度。
    使用 cipher.update 加密明文，並生成密文。
    調用 cipher.final 完成加密操作。
    使用 cipher.getAuthTag 獲取認證標籤，用於解密時的完整性驗證。
解密過程：
    創建解密器 decipher。
    設定解密所需的認證標籤 tag。
    設定 AAD 及其對應的密文長度。
    使用 decipher.update 解密密文，並生成明文。
    調用 decipher.final 完成解密操作，並進行認證標籤驗證。如果認證失敗，會拋出錯誤。
```