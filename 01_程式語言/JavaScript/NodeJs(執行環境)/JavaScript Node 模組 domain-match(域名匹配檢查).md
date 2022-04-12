# JavaScript Node 模組 domain-match(域名匹配檢查)

```
檢查給定的 url 是否與簡單的域名模式匹配。
```

## 參考資料

[domain-match npm 頁面](https://www.npmjs.com/package/domain-match/v/1.0.0)

# 指令

```bash
# 安裝
npm install domain-match
```

# 用法

```JavaScript
var domainMatch = require('domain-match');
var matched = domainMatch('*.abc.com/prefix/path', 'http://www.abc.com/prefix/path/filename.ext');
// matched == true
```