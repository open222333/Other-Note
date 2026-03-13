# JavaScript 內建-物件 Map(映射)

```
一個Map物件代表一組 鍵值(keys)的值，其中給個鍵值都有另一個與之關聯的值(或他映射至的值)。
某個意義上，一個映射就像是一個陣列。但映射允許使用任意值作為索引(keys)。
```

## 目錄

- [JavaScript 內建-物件 Map(映射)](#javascript-內建-物件-map映射)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [WeakMap類別](#weakmap類別)

## 參考資料

[Map MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/Map)

# 用法

```JavaScript
// 創建一個新個空映射
let m = new Map();
let n = new Map([
	["one", 1],
	["two", 2]
]);

// 鍵值與值與n相同的新映射
let copy = new Map(n);

// 擁有兩個特性的物件
let o = { x: 1, y: 2};

// 等同new Map([["x", 1], ["y", 2]])
let p = new Map(Object.entries(o));

// 鍵值數量
m.size

// 將鍵值映射至值
m.set("one", 1);
m.set("two", 2).set("three", 3);

// 取得指定鍵值的值，若無則回傳undefined
m.get("one");

// 是否有鍵值 回傳布林值
m.has("one");

// 刪除鍵值 回傳布林值 鍵值不存在回傳false
m.delete("one");

// 刪除所有鍵值與值
m.clear();

// 可使用任何值包含物件、陣列當鍵值，但每個物件或陣列都不相同即使有完全相同的特性。
// 依照同一性而非相等性比較鍵值。
let x = new Map();
x.set({}, 1).set({}, 2);
x.size // => 2
x.get({}); // => undefined

// 迭代
let m = new Map([["x", 1], ["y", 2]]);

for (let [key, value] of m) {
	// key value
}

// 取得所有鍵值
m.keys() // => "x", "y"

// 取得值
m.values() // => 1, 2

// 取得鍵值 值
m.entries() // => ["x", 1], ["y", 2]

// forEach() 迭代
m.forEach(value, key) => {
	// 注意為 value key 而非 key value
}
```

## WeakMap類別

```
Map類別的變體，不會防止其鍵值被垃圾回收(JavaScript直譯器回收不在可及(reachable)因此無法為程式所用的物件的記憶體的過程)。

鍵值必須是物件或陣列，不能使用原始值(primitive values)，僅實作get()、set()、has()、delete()方法，不可迭代，沒有size特性。
```