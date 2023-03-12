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
- [具型陣列(typed arrays)](#具型陣列typed-arrays)
	- [具型陣列 用法](#具型陣列-用法)

## 參考資料

[Set MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/Set)

[使用 JavaScript 處理二進位資料 - Buffer v.s View](https://weihanglo.tw/posts/2017/binary-data-manipulations-in-javascript/)

[DataView 類別](https://learn.microsoft.com/zh-tw/dotnet/api/system.data.dataview?view=net-7.0)

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

# 具型陣列(typed arrays)

```
ES6 新功能
並非陣列，Array.isArray()回傳false

ES6 規範了三個 Typed Array 相關物件，對應類別如下：

ArrayBuffer：Buffer，代表一段記憶體區塊，僅能透過 View 操作其內容。
ArrayBuffer 代表一段固定大小的記憶體區塊，也稱為 byte-array。主要的功能就是配置實體記憶體來儲存 raw binary data。一般很少直接操作 ArrayBuffer，實際上也只能將其 reference 傳給其他物件，讓其他物件來處理／使用資料。

TypedArray：View，儲存固定型別資料的 Array，例如 Uint8Array（8-bit unsigned integer）、Float64Array（64-bit IEEE floating point number)。
DataView：View，不限制型別，可自定義從哪個 byte，以什麼型別，用哪種 byte order（endian）存取。

JavaScrip並無TypedArray類別，而是提供11種具型陣列

Int8Array() 有號位元組
Uint8Array() 無號位元組 0-255之間的數字 若超出則會 wraps around 得到其他的值
Uint8ClampedArray() 不帶迴轉(rollover)的無號位元組 0-255之間的數字 若超出則會clamps為255或0
Int16Array() 有號的16位元短整數
Uint16Array() 無號的16位元短整數
Int32Array() 有號的32位元整數
Uint32Array() 無號的32位元整數
BigInt64Array() 有號的64位元 BigInt值(ES2020)
BigUint64Array() 無號的64位元 BigInt值(ES2020)
Float32Array() 32位元浮點數值
Float64Array() 64位元浮點數值: 常規的JavaScript數字
```

## 具型陣列 用法

```JavaScript
// 陣列元素會被初始化為 0, 0n, 0.0
let bytes = new Uint8Array(1024); // 1024位元組
let matrix = new Float64Array(9); // 3*3矩陣
let point = new Int16Array(3); // 3D空間中的一個點
let rgba = new Uint8ClampedArray(4); // 一個4位元組的RGBA像素值
let sudoku = new Int8Array(81); // 一個9*9的數獨板

// 指定元素值 of
let white = Uint8ClampedArray.of(255, 255, 255, 0); // RGBA不透明的白色

// 拷貝既存的具型陣列 from
let ints = Uint32Array.from(white);

// 值被截斷(truncated)以符合具型陣列的型別限制，不會有警告或錯誤
// 浮點數被截斷為整數 較長的整數被截斷為8位元
Uint8Array.of(1.23, 2.99, 45000); // => new Uint8Array([1, 2, 200])
```