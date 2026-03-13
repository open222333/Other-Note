# JavaScript 內建-物件 Promise(非同步操作)

```
Promise 物件代表一個即將完成、或失敗的非同步操作，以及它所產生的值。
```

## 目錄

- [JavaScript 內建-物件 Promise(非同步操作)](#javascript-內建-物件-promise非同步操作)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[Promise MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/Promise)

# 用法

```JavaScript
const promise1 = new Promise((resolve, reject) => {
  setTimeout(() => {
    resolve('foo');
  }, 300);
});

promise1.then((value) => {
  console.log(value);
  // Expected output: "foo"
});

console.log(promise1);
// Expected output: [object Promise]
```