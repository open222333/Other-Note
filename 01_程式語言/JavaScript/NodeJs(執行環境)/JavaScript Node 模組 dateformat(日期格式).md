# JavaScript Node 模組 dateformat(日期)

```
根據Steven Levithan's excellent dateFormat() function 做出的node.js套件包
```

## 目錄

- [JavaScript Node 模組 dateformat(日期)](#javascript-node-模組-dateformat日期)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[dateformat npm 頁面](https://www.npmjs.com/package/dateformat)

[Steven Levithan's excellent dateFormat()](https://blog.stevenlevithan.com/archives/javascript-date-format)

# 指令

```bash
# 安裝
npm install dateformat

dateformat --help
```

# 用法

```JavaScript
// 注意dateformat版本 dateformat@3.0.3可用此例子
const dateformat = require('dateformat');
let now = new Date();
dateformat(now, 'dddd, mmmm dS, yyyy, h:MM:ss TT');// returns 'Tuesday, March 16th, 2021, 11:32:08 PM'
```