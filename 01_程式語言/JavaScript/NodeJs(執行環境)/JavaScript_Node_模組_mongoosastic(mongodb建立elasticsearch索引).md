# JavaScript Node 模組 mongoosastic(mongodb建立elasticsearch索引)

```
Mongoosastic 是一個 mongoose 插件，可以自動將你的模型索引到 elasticsearch 中
```

## 目錄

- [JavaScript Node 模組 mongoosastic(mongodb建立elasticsearch索引)](#javascript-node-模組-mongoosasticmongodb建立elasticsearch索引)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[mongoosastic npm 頁面](https://www.npmjs.com/package/mongoosastic)

[Mongoosastic - NodeJS](https://github.com/mongoosastic/mongoosastic)

# 指令

```bash
# 安裝
npm install mongoosastic
```

# 用法

```JavaScript
// Setup your mongoose model to use the plugin
const mongoose     = require('mongoose')
const mongoosastic = require('mongoosastic')
const Schema       = mongoose.Schema

var User = new Schema({
    name: String,
    email: String,
    city: String
})

User.plugin(mongoosastic)
// Query your Elasticsearch with the search() method (added by the plugin)
const results = await User.search({
  query_string: {
    query: "john"
  }
});
```