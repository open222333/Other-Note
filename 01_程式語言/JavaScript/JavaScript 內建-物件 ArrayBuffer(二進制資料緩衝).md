# JavaScript 內建-物件 ArrayBuffer(二進制資料緩衝)

```
ArrayBuffer 物件是一種表示通用、固定大小的原始二進制資料緩衝。

想要直接操作一個 ArrayBuffer 物件的內容是不可能的。

若要讀寫該緩衝的內容則必須透過視圖，可以選擇建立一個 DataView 視圖物件或是一個限定其成員為某種型別的 TypedArray 視圖物件，它們皆能以特定的型別解讀、修改 ArrayBuffer。


對一塊記憶體的一個不透明參考(opaque reference)

一種資料型態，用於表示通用的固定長度二進制資料緩衝區。
不能直接操作 ArrayBuffer 的內容。
但可以建立一個「型別陣列視圖 (typed array view) 」或一個 DataView，它以特定格式代表緩衝區，並使用它讀取和寫入緩衝區的內容，根據你存取 ArrayBuffer 的方式不同，在同一台機器上可以得到不同的 endianness (byte order)。

若指定一個位元偏移量，須以型別的倍數。
```

## 目錄

- [JavaScript 內建-物件 ArrayBuffer(二進制資料緩衝)](#javascript-內建-物件-arraybuffer二進制資料緩衝)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [偏移量](#偏移量)
- [用法](#用法)

## 參考資料

[ArrayBuffer MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/ArrayBuffer)

# 偏移量

```
一個陣列或資料結構物件內的偏移量是指數組或物件的起點與所要尋找的值之間的距離，該距離是一個整數值。
例如在字元陣列 "abcdef "中，字元 "d "與a之間的偏移量為3。
```

# 用法

```JavaScript
// new ArrayBuffer(<length>)
// 建立一個 ArrayBuffer，參數表示該 ArrayBuffer 需要幾個位元組（bytes）
let buffer = new ArrayBuffer(8); // 佔用 8 個位元組（bytes）的 Buffer
buffer.byteLength; // 8，指該 buffer 佔用記憶體的長度（位元組）

// 透過 slice 來複製 ArrayBuffer
// slice(startIndex, endIndex) 結束時不包含 end
let buf1 = new ArrayBuffer(8);
let buf2 = buf1.slice(0); // 從頭複製到尾

let asbytes = new Uint8Array(buffer); // 視為位元組
let asints = new Int32Array(buffer); // 視為32位元的有號整數
let lastK = new Uint8Array(buffer, 1023 * 1024); // 最後一個KB作為位元組
let ints2 = new Int32Array(buffer, 1024, 256); // 第二個KB 作為256個整數
```
