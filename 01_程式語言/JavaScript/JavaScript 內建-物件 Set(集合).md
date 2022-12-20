# JavaScript 內建-物件 Set(集合)

```
集合是值得一個集群，與陣列不同的是 集合沒有索引，集合不允許重複。
```

## 目錄

- [JavaScript 內建-物件 Set(集合)](#javascript-內建-物件-set集合)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [WeakSet類別](#weakset類別)

## 參考資料

[Set MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/Set)

# 用法

```JavaScript
// 創建Set物件
let s = new Set();
// let s = new Ser([1, s]);

// 拷貝s的元素的新集合
let t = new Set(s);
let unique = new Set("Mississippi"); // 4個元素 M i s p

// 集合有幾個值
unique.size // 4

// 新增 回傳被調用的Set
s.add(1).add(2);

// 刪除 回傳boolean值
s.delete(1);

// 移除所有元素
s.clear();

// 是否有元素 回傳boolean值
s.has(2);

// 遍歷集合中的元素
s.forEach()

let oneDigitPrimes = new Set([2,3,5,7])
// Set可迭代 範例使用for/of迴圈
let sum = 0;
for (let p of oneDigitPrimes) {
	sum += p;
}

// 分散運算子 將集合轉換為Array
[...oneDigitPrimes]

// forEach範例
let product = 1;
oneDigitPrimes.forEach(n => { product *= n;});
product // => 210: 2 * 3 * 5 * 7
```

## WeakSet類別

```
Set類別的變體，不會防止其鍵值被垃圾回收(JavaScript直譯器回收不在可及(reachable)因此無法為程式所用的物件的記憶體的過程)。

成員不能使用原始值(primitive values)，僅實作add()、has()、delete()方法，不可迭代，沒有size特性。
```