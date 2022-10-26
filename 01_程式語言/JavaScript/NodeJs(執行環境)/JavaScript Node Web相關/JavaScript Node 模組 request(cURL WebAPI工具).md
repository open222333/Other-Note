# JavaScript Node 模組 request(cURL WebAPI工具)

```
已棄用！
自 2020 年 2 月 11 日起，已完全棄用。

簡化的 HTTP 客戶端
```

## 目錄

- [JavaScript Node 模組 request(cURL WebAPI工具)](#javascript-node-模組-requestcurl-webapi工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[request npm 頁面](https://www.npmjs.com/package/request)

[cURL轉成個別語言範本](https://curlconverter.com/#node-request)

# 指令

```bash
# 安裝
npm install request
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

var request = require('request');

var headers = {
    'Accept-Encoding': 'gzip, deflate, sdch',
    'Accept-Language': 'en-US,en;q=0.8',
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36',
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    'Referer': 'http://www.wikipedia.org/',
    'Connection': 'keep-alive'
};

var options = {
    url: 'http://en.wikipedia.org/',
    headers: headers
};

function callback(error, response, body) {
    if (!error && response.statusCode == 200) {
        console.log(body);
    }
}

request(options, callback);
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

var request = require('request');

var headers = {
    'Origin': 'http://fiddle.jshell.net',
    'Accept-Encoding': 'gzip, deflate',
    'Accept-Language': 'en-US,en;q=0.8',
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36',
    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
    'Accept': '*/*',
    'Referer': 'http://fiddle.jshell.net/_display/',
    'X-Requested-With': 'XMLHttpRequest',
    'Connection': 'keep-alive'
};

var dataString = 'msg1=wow&msg2=such&msg3=data';

var options = {
    url: 'http://fiddle.jshell.net/echo/html/',
    method: 'POST',
    headers: headers,
    body: dataString
};

function callback(error, response, body) {
    if (!error && response.statusCode == 200) {
        console.log(body);
    }
}

request(options, callback);
```

```JavaScript
// Basic Auth

// cURL Command
// curl "https://api.test.com/" -u "some_username:some_password"

var request = require('request');

var options = {
    url: 'https://api.test.com/',
    auth: {
        'user': 'some_username',
        'pass': 'some_password'
    }
};

function callback(error, response, body) {
    if (!error && response.statusCode == 200) {
        console.log(body);
    }
}

request(options, callback);
```