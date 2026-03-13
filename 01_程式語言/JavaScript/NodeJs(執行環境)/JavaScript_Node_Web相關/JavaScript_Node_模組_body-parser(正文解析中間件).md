# JavaScript Node 模組 body-parser(正文解析中間件)

```
在處理程序之前在中間件中解析傳入的請求正文，可在 req.body 屬性下使用。
```

## 目錄

- [JavaScript Node 模組 body-parser(正文解析中間件)](#javascript-node-模組-body-parser正文解析中間件)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
	- [Express/Connect 頂級通用](#expressconnect-頂級通用)
	- [更改解析器接受的類型](#更改解析器接受的類型)

## 參考資料

[body-parser npm 頁面](https://www.npmjs.com/package/body-parser)

# 指令

```bash
# 安裝
npm install body-parser
```

# 用法

## Express/Connect 頂級通用

```JavaScript
/**
 * 演示添加通用 JSON 和 URL 編碼解析器作為頂級中間件，它將解析所有傳入請求的正文。
 */
var express = require('express')
var bodyParser = require('body-parser')

var app = express()

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }))

// parse application/json
app.use(bodyParser.json())

app.use(function (req, res) {
  res.setHeader('Content-Type', 'text/plain')
  res.write('you posted:\n')
  res.end(JSON.stringify(req.body, null, 2))
})
```

```JavaScript
/**
 * 演示了將正文解析器專門添加到需要它們的路由中。
 * 一般來說，這是將 body-parser 與 Express 一起使用的最推薦方式。
 */
var express = require('express')
var bodyParser = require('body-parser')

var app = express()

// create application/json parser
var jsonParser = bodyParser.json()

// create application/x-www-form-urlencoded parser
var urlencodedParser = bodyParser.urlencoded({ extended: false })

// POST /login gets urlencoded bodies
app.post('/login', urlencodedParser, function (req, res) {
  res.send('welcome, ' + req.body.username)
})

// POST /api/users gets JSON bodies
app.post('/api/users', jsonParser, function (req, res) {
  // create user in req.body
})
```
## 更改解析器接受的類型

```JavaScript
/**
 * 所有解析器都接受一個類型選項，該選項允許您更改中間件將解析的 Content-Type。
 */
var express = require('express')
var bodyParser = require('body-parser')

var app = express()

// parse various different custom JSON types as JSON
app.use(bodyParser.json({ type: 'application/*+json' }))

// parse some custom thing into a Buffer
app.use(bodyParser.raw({ type: 'application/vnd.custom-type' }))

// parse an HTML body into a string
app.use(bodyParser.text({ type: 'text/html' }))
```