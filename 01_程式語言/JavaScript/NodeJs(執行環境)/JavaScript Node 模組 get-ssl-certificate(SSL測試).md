# JavaScript Node 模組 get-ssl-certificate(SSL測試)

```
返回網站 SSL 證書的零依賴實用程序
```

## 參考資料

[get-ssl-certificate npm 頁面](https://www.npmjs.com/package/get-ssl-certificate)

# 指令

```bash
# 安裝
npm install --save get-ssl-certificate
```

# 用法

```JavaScript
// 載入套件
const sslCertificate = require('get-ssl-certificate')

// 傳遞 url / domain name 參數 Timeout(單位ms) Port(預設 443) Protocol(預設 https:)
sslCertificate.get('nodejs.org', 250, 443, 'https:').then(function (certificate) {
  console.log(certificate)
  // certificate is a JavaScript object


  console.log(certificate.issuer)
  // { C: 'GB',
  //   ST: 'Greater Manchester',
  //   L: 'Salford',
  //   O: 'COMODO CA Limited',
  //   CN: 'COMODO RSA Domain Validation Secure Server CA' }

  // 有效期 起始
  console.log(certificate.valid_from)
  // 'Aug  14 00:00:00 2017 GMT'

  // 有效期 結束
  console.log(certificate.valid_to)
  // 'Nov 20 23:59:59 2019 GMT'

  // 如果有 certificate.raw 屬性，那麼你可以訪問 certificate.pemEncoded
  console.log(certificate.pemEncoded)
  // -----BEGIN CERTIFICATE-----
  // ...
  // -----END CERTIFICATE-----
});
```