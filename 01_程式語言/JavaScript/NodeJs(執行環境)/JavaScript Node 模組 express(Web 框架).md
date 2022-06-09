# JavaScript Node 模組 express(Web 框架)

```
Express 是一個和多種應用靈活的 node.js Web 框架，提供了各種特性幫助你創建 Web 應用，以及的 HTTP 工具。
```

## 參考資料

[express npm 頁面](https://www.npmjs.com/package/express)

[express 中文網站](https://expressjs.com/zh-tw/)

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