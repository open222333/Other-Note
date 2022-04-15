# JavaScript Node 模組 node-fetch(cURL WebAPI工具)

```
串接 API
```

## 參考資料

[node-fetch npm 頁面](https://www.npmjs.com/package/node-fetch)

[【Node.js】使用 Fetch 來抓資料吧 node-fetch](https://mike2014mike.github.io/study/2018/10/05/node-js-fetch/)

[cURL轉成個別語言範本](https://curlconverter.com/#node)

# 指令

```bash
# 安裝
npm install node-fetch
```

# 用法

```JavaScript
// GET

// cURL Command
// curl 'http://en.wikipedia.org/' \
//     -H 'Accept-Encoding: gzip, deflate, sdch' \
//     -H 'Accept-Language: en-US,en;q=0.8' \
//     -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36' \
//     -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8' \
//     -H 'Referer: http://www.wikipedia.org/' \
//     -H 'Connection: keep-alive' --compressed

var fetch = require('node-fetch');

fetch('http://en.wikipedia.org/', {
    headers: {
        'Accept-Encoding': 'gzip, deflate, sdch',
        'Accept-Language': 'en-US,en;q=0.8',
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
        'Referer': 'http://www.wikipedia.org/',
        'Connection': 'keep-alive'
    }
});
```

```JavaScript
// POST

// cURL Command
// curl 'http://fiddle.jshell.net/echo/html/' \
//     -H 'Origin: http://fiddle.jshell.net' \
//     -H 'Accept-Encoding: gzip, deflate' \
//     -H 'Accept-Language: en-US,en;q=0.8' \
//     -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36' \
//     -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
//     -H 'Accept: */*' \
//     -H 'Referer: http://fiddle.jshell.net/_display/' \
//     -H 'X-Requested-With: XMLHttpRequest' \
//     -H 'Connection: keep-alive' \
//     --data 'msg1=wow&msg2=such&msg3=data' --compressed

var fetch = require('node-fetch');

fetch('http://fiddle.jshell.net/echo/html/', {
    method: 'POST',
    headers: {
        'Origin': 'http://fiddle.jshell.net',
        'Accept-Encoding': 'gzip, deflate',
        'Accept-Language': 'en-US,en;q=0.8',
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36',
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'Accept': '*/*',
        'Referer': 'http://fiddle.jshell.net/_display/',
        'X-Requested-With': 'XMLHttpRequest',
        'Connection': 'keep-alive'
    },
    body: 'msg1=wow&msg2=such&msg3=data'
});
```

```JavaScript
// Basic Auth

// cURL Command
// curl "https://api.test.com/" -u "some_username:some_password"

var fetch = require('node-fetch');

fetch('https://api.test.com/', {
    headers: {
        'Authorization': 'Basic ' + btoa('some_username:some_password')
    }
});
```