# JavaScript 內建-物件 TypedArray(類陣列（array-like）視圖)

```
TypedArray 物件表示了一個底層 ArrayBuffer 的類陣列（array-like）視圖，它能以限定的型別解讀、修改 ArrayBuffer。

但並沒有名為 TypedArray 的內建物件，TypedArray 也不存在可直接呼叫的建構式。真正能夠使用的是幾個原型繼承自 TypedArray 的內建物件，它們可以建立限定成員型別的物件實體來操作 ArrayBuffer。

這些 TypedArray 型別的物件僅為視圖，並不會存放資料，所有的資料皆實際儲存於 ArrayBuffer 物件當中。以下將說明每種限定成員型別之 TypedArray 的共同屬性與方法。
```

## 目錄

- [JavaScript 內建-物件 TypedArray(類陣列（array-like）視圖)](#javascript-內建-物件-typedarray類陣列array-like視圖)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [偏移量](#偏移量)
- [用法](#用法)
	- [範例 厄拉托西尼篩法(sieve of Eratosthenes) 找出最大質數](#範例-厄拉托西尼篩法sieve-of-eratosthenes-找出最大質數)

## 參考資料

[TypedArray MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/TypedArray)

# 偏移量

```
一個陣列或資料結構物件內的偏移量是指數組或物件的起點與所要尋找的值之間的距離，該距離是一個整數值。
例如在字元陣列 "abcdef "中，字元 "d "與a之間的偏移量為3。
```

# 用法

```JavaScript
// 具型陣列的方法與特性

/**
 * 具型陣列有固定長度，因此length特性是唯讀的
 * 改變陣列長度的方法都無實作，push() pop() unshift() shift() splice()
 * 改變內容但不改變長度都有實作，sort() reverse() fill()
 */

// set(陣列或具型陣列, 元素偏移量 預設為0) 拷貝一個普通陣列或具型陣列的元素至具型陣列來一次設定該具型陣列的多個元素
// slice(start, end) 回傳一個新陣列物件，為原陣列選擇之begin 至end（不含end）部分的淺拷貝（shallow copy）。而原本的陣列將不會被修改。
let bytes = new Uint8Array(1024); // 一個1K的緩衝區
let pattern = new Uint8Array([0, 1, 2, 3]); // 4個位元組的一個陣列
bytes.set(pattern);
bytes.set(pattern, 4);
bytes.set([0, 1, 2, 3], 8);
bytes.slice(0, 12); // => new Uint8Array([0,1,2,3,0,1,2,3,0,1,2,3])

// subarray(start, end) 回傳現有陣列一個新的view
let ints = new Int16Array([0, 1, 2, 3, 4, 5, 6, 7, 8, 9]);
let last3 = ints.subarray(ints.length - 3, ints.length); // 取最後三個
last3[0]; // => 7 等於 ints[7]
ints[9] = -1; // 改變原陣列的值
last3[2]; // => -1 子陣列也會改變

/**
 * buffer特性 陣列的ArrayBuffer
 * byteOffset 該陣列的資料在底層緩衝區中的起始位置
 * byteLength 陣列的資料長度
 * 對於任何具型陣列a 以下不變式永遠為真
 * a.length * a.BYTES_PER_ELEMENT === a.byteLength // => true
 */
last3.buffer === ints.buffer // => true, 相同緩衝區的不同觀點
last3.byteOffset // => 14 這個view從該緩衝區的位元組14開始
last3.byteLength // => 6 這個view有6個位元組(3個16位元組)長
last3.buffer.byteLength // => 20 底層緩衝區有20個位元組

/**
 * ArrayBuffer 是隱藏起來的位元組塊
 * 能以具型陣列存取那些位元組
 */
let byte2s = new Uint8Array(8);
byte2s[0] = 1;
byte2s.buffer[0] // => undefined: 緩衝區沒有索引0
byte2s.buffer[1] = 255; // => 不正常的設定緩衝區的一個位元組
byte2s.buffer[1] // => 255 設定一個普通的js特性
byte2s[1] // => 0
```

## 範例 厄拉托西尼篩法(sieve of Eratosthenes) 找出最大質數

```JavaScript
// 回傳小於n的最大質數,使用厄拉托西尼篩法(sieve of Eratosthenes)
function sieve(n) {
  // 若x為合數(composite), a[x] 就會是1
  let a = new Uint8Array(n + 1);
  // 別處理比這還大的因數
  let max = Math.floor(Math.sqrt(n));
  // 2是第一個質數(prime)
  let p = 2;
  // 對於小於max的質數，把p的倍數標示為合數
  while (p <= max) {
    for (let i = 2 * p; i <= n; i += p) a[i] = 1;
    // 下一個未標示的索引就是質數
    while (a[++p]) /** 空的 */;
  }
  // 往回跑迴圈 以找出最大質數
  while (a[n]) n--;
  return n;
}
```