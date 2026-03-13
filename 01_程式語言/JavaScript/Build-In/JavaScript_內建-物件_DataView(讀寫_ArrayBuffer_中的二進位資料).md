# JavaScript 內建-物件 DataView(讀寫 ArrayBuffer 中的二進位資料)

```
DataView 視圖提供了一個底層介面來讀寫 ArrayBuffer 中的二進位資料。

DataView 能用多種不同的型別對 ArrayBuffer 進行修改、解讀，且可自訂資料的位元組順序而不受系統平台限制。

DataView 物件僅為視圖，並不會存放資料，所有的資料皆實際儲存於 ArrayBuffer 物件當中。
```

## 目錄

- [JavaScript 內建-物件 DataView(讀寫 ArrayBuffer 中的二進位資料)](#javascript-內建-物件-dataview讀寫-arraybuffer-中的二進位資料)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[DataView MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/DataView)

# 用法

```JavaScript
// DataView 和 位元組序(Endianness)

/**
 * 若整數0x00000001
 * 小端序平台 在記憶體中的安排方式為 01 00 00 00
 * 大端序平台 在記憶體中的安排方式為 00 00 00 01
 *
 * 大多數CPU架構為 小端序
 * 許多網路協定以及某些二進位為 大端序
 */
let littleEndian = new Int8Array(new Int32Array([1]).buffer)[0] === 1;

/**
 * 假設有二進位資料的位元組所構成的具型陣列要處理
 *
 * 建立DataView物件 使其可以彈性的讀寫來自位元組的值
 */
let view = new DataView(bytes.buffer, bytes.byteOffset, bytes.byteLength);
let int = view.getInt32(0); // 從位元組0讀取大端序的有號整數
int = view.getInt32(4, false); // 下一個整數也是大端序
int = view.getUint32(8, true); // 下一個整數小端序而且無號
view.setUint32(8, int, false); // 以大端序格式寫回
```