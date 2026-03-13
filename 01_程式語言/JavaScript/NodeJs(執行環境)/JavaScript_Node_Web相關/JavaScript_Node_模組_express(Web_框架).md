# JavaScript Node 模組 express(Web 框架)

```
Express 是一個和多種應用靈活的 node.js Web 框架，提供了各種特性幫助你創建 Web 應用，以及的 HTTP 工具。
```

## 目錄

- [JavaScript Node 模組 express(Web 框架)](#javascript-node-模組-expressweb-框架)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [線上工具相關](#線上工具相關)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[express npm 頁面](https://www.npmjs.com/package/express)

[express-queue npm 頁面 (express 中間件 請求排隊)](https://www.npmjs.com/package/express-queue)

[body-parser npm 頁面 (body解析中間件)](https://www.npmjs.com/package/body-parser#installation)

[express 中文網站](https://expressjs.com/zh-tw/)

[NodeJS HTTP 請求隊列](https://stackoverflow.com/questions/55192900/nodejs-http-request-queue)

### 線上工具相關

[轉換成程式碼](https://curlconverter.com/python/)

[Online REST & SOAP API Testing Tool - 轉換成程式碼](https://reqbin.com/)

# 指令

```bash
# 安裝
npm install express
npm i express

# Install the executable. The executable's major version will match Express's:
npm install -g express-generator@4

# Create the app
express /tmp/foo && cd /tmp/foo

# Start the server
$ npm start
```

# 用法

```JavaScript
//express_demo.js 文件
var express = require('express');
var app = express();

app.get('/', function (req, res) {
   res.send('Hello World');
})

var server = app.listen(8081, function () {

  var host = server.address().address
  var port = server.address().port

  console.log("範例:http://%s:%s", host, port)

})
```