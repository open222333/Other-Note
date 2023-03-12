# JavaScript 內建-物件 Date(日期與時間)

```
```

## 目錄

- [JavaScript 內建-物件 Date(日期與時間)](#javascript-內建-物件-date日期與時間)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [時間算數](#時間算數)

## 參考資料

[Date MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/Date)

# 用法

```JavaScript
let new = new Date(); // 目前的時間

let epoch = new Date(0); // 1970年1月1日 子夜

let century = new Date(2100, 0, 1, 2, 3, 4, 5); // 2100年1月1日02:03:04.005 本地時間


// 可剖析 Date物件的toString() toUTCString() toISOString()方法
let century = new Date("2100-01-01T00:00:00Z"); // 一個ISO格式的日期

// 格式化與剖析日期字串
let d = new Date(2020, 0, 1, 17, 10, 30);
d.toString() // => "Wed Jan 01 2020 17:10:30 GMT+0800 (Pacific Standard Time)"
d.toUTCString() // => "Wed, 01 Jan 2020 09:10:30 GMT"
d.toLocaleDateString() // => "2020/1/1"
d.toLocaleTimeString() // => "下午5:10:30"
d.toISOString() // => "2020-01-01T09:10:30.000Z"

// FullYear 可替換成 Month Date Hours Minutes Seconds Milliseconds
century.setFullYear(century.getFullYear() + 1); // 增加年欄位
century.getFullYear()
century.getUTCFullYear()
century.setFullYear()
century.getUTCFullYear()


// 時戳(timestamps)
d.setTime(d.getTime() + 3000) // 加30秒

// 測試花多少時間
let startTime  = Date.now()
test(); // 假設執行某些運算
let endTime = Date.now()
console.log(`Spline test took ${endTime - startTime}ms.`);
```

## 時間算數

```JavaScript
// 目前日期加上三個月又兩週的程式碼
let d = new Date();
d.setMonth(d.getMonth() + 3, d.getDate() + 14);
console.log(d.toISOString());
```