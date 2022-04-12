# JavaScript Node 模組 dns-sync(域名dns指向取得)

```
取得域名指向紀錄
```

## 參考資料

[dns-sync npm 頁面](https://www.npmjs.com/package/dns-sync)

# 指令

```bash
# 安裝
npm install dns-sync
```

# 用法

```JavaScript
var dnsSync = require('dns-sync');

console.log(dnsSync.resolve('www.paypal.com')); //should return the IP address
console.log(dnsSync.resolve('www.non-host.something')); //should return null
console.log(dnsSync.resolve('www.google.com', 'AAAA')); //should return AAAA records
console.log(dnsSync.resolve('google.com', 'NS')); //should return NS record
```