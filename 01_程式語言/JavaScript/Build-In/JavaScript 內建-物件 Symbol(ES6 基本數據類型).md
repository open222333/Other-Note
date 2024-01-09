# JavaScript 內建-物件 Symbol(ES6 基本數據類型)

```
Symbol 是 ECMAScript 6（ES6）引入的一個新的基本數據類型。
它是一種原始數據類型，表示唯一的標識符。
每個 Symbol 實例都是唯一的，並且不能被相等性操作符（如 ===）視為相等。
```

## 目錄

- [JavaScript 內建-物件 Symbol(ES6 基本數據類型)](#javascript-內建-物件-symboles6-基本數據類型)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [Symbol.hasInstance](#symbolhasinstance)
	- [Symbol.toStringTag](#symboltostringtag)
	- [Symbol.species](#symbolspecies)
	- [Symbol.isConcatSpreadable](#symbolisconcatspreadable)
	- [Symbol.toPrimitive](#symboltoprimitive)

## 參考資料

[Symbol MDN Web Doc](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Symbol)

# 用法

```JavaScript
// 1. 創建唯一標識符
const mySymbol = Symbol();

// 2. 給 Symbol 添加描述
const namedSymbol = Symbol("mySymbolDescription");

// 3.1 作為對象屬性名稱，避免衝突
const obj = {
  [Symbol("key")]: "value"
};

// 3.2 作為常量或枚舉值，避免命名衝突
const LogLevel = {
  INFO: Symbol("info"),
  WARN: Symbol("warn"),
  ERROR: Symbol("error")
};

// 3.3 作為私有屬性名稱，模擬私有屬性
const privateProperty = Symbol("privateProperty");
class MyClass {
  constructor() {
    this[privateProperty] = "I am private";
  }
  getPrivate() {
    return this[privateProperty];
  }
}

// 4. 使用 Symbol.for 創建全局 Symbol
const globalSymbol = Symbol.for("globalSymbol");

// 5. 使用 Symbol.keyFor 獲取已註冊 Symbol 的 key
const key = Symbol.keyFor(globalSymbol);

// 6. 使用 Symbol.iterator 定義對象的默認迭代器
const iterableObj = {
  [Symbol.iterator]: function* () {
    yield 1;
    yield 2;
    yield 3;
  }
};

// 7. 使用 Symbol.species 設置構造函數創建衍生對象的 Symbol
class MyArray extends Array {
  static get [Symbol.species]() {
    return Array;
  }
}

// 8. 其他內建 Symbol，例如 Symbol.toStringTag、Symbol.toPrimitive、Symbol.isConcatSpreadable 等

// 註解：Symbol 的用途包括創建唯一標識符、避免命名衝突、作為私有屬性、定義迭代器、設置衍生對象構造函數等。
```

## Symbol.hasInstance

```JavaScript
// Symbol.hasInstance

/**
 * Symbol.hasInstance 主要用於定義對象的 instanceof 操作符的行為。
 * 當一個對象被當作構造函數（constructor）使用時，instanceof 操作符會檢查這個對象的原型鏈上是否存在 Symbol.hasInstance 方法，並調用這個方法。
 */

// 定義自定義對象的 Symbol.hasInstance 方法
class MyObject {
  static [Symbol.hasInstance](instance) {
    // 定義對象被 instanceof 時的行為
    return instance && instance.hasOwnProperty("myProperty");
  }
}

const obj = { myProperty: "Hello" };

console.log(obj instanceof MyObject); // true

// 內建對象的 Symbol.hasInstance 方法
class CustomArray {
  static [Symbol.hasInstance](instance) {
    return Array.isArray(instance);
  }
}

const myArray = [1, 2, 3];

console.log(myArray instanceof CustomArray); // true

// 定義可作為一型別 來與 instanceof 並用的一個物件
let iint8 = {
  [Sysbol.hasInstance](x) {
    return Number.isInteger(x) && x >= 0 && x <= 255;
  },
};

128 instanceof uint8; // => true
256 instanceof uint8; // => false : 太大
Math.PI instanceof uint8; // => false : 不是整數
```

## Symbol.toStringTag

```JavaScript
// classof 使用範例
// 自定義類別
class Range {
  get [Symbol.toStringTag]() {
    return "Range";
  }
}

let r = new Range(1, 10);
console.log(Object.prototype.toLocaleString.call(r)); // [object Range]
console.log(classof(r)); // Range
```

## Symbol.species

```
Symbol.species 是 JavaScript 中的一個內建符號（Symbol），它用於定義在衍生（derivative）對象中，用於創建新實例的建構函數。

在 JavaScript 中，有些內建對象（例如 Array、Map、Set 等）具有衍生機制，這允許你創建一個新對象，該對象與原始對象共享某些特性，但也有一些不同。
例如，使用 Array 的 map 方法創建一個新數組時，新數組的構造函數是 Array，因此可以確保它具有相同的行為。

Symbol.species 的作用是為衍生對象指定一個建構函數，該建構函數用於創建衍生對象的實例。
如果衍生對象沒有自己的 Symbol.species 屬性，則將使用其父對象的 Symbol.species。
```

```Javascript
class MyArray extends Array {
  // 使用 Symbol.species 定義衍生對象的建構函數
  static get [Symbol.species]() {
    return Array;
  }
}

const myArray = new MyArray(1, 2, 3);
const mappedArray = myArray.map(x => x * 2);

console.log(mappedArray instanceof MyArray); // false
console.log(mappedArray instanceof Array);   // true
在這個例子中，Symbol.species 被用來指定 MyArray 的衍生對象的建構函數為 Array。因此，mappedArray 是由 Array 創建的新數組，而不是 MyArray。
```

```JavaScript
// Symbol.species
// 一個簡單的Array子類別
// 會為第一和最後一個元素新增取值器
class EZArray extends Array {
  get first() {
    return this[0];
  }
  get last() {
    return this[this.length - 1];
  }
}

let e = new EZArray(1, 2, 3);
let f = e.map((x) => x * x);
console.log(e.last); // 3: EZArray 的最後一個元素 e
console.log(f.last); // 9: f 也是一個擁有 last 特性的 EZArray

// Symbol.species 是唯讀的
// 預設情況下 子類別建構器會繼承取值器函式
// 不希望子類別建構器使用自己的物種

// 方法一
// 使用 defineProperty
Object.defineProperty(EZArray, Symbol.species, { value: Array });

// 方法二
class EZArray2 extends Array {
  static get [Symbol.species]() {
    return Array;
  }
  get first() {
    return this[0];
  }
  get last() {
    return this[this.length - 1];
  }
}
let e2 = new EZArray2(1, 2, 3);
let f2 = e2.map((x) => x * x);
console.log(e2.last); // 3: EZArray 的最後一個元素 e
console.log(f2.last); // undefined: f 是 Array, 沒有 last 取值器
```

## Symbol.isConcatSpreadable

```
Symbol.isConcatSpreadable是一個JavaScript的Symbol，用於自定義對象的可擴展性，特別是在使用Array.prototype.concat()方法時的行為。

當一個對象的Symbol.isConcatSpreadable屬性被設置為true時，該對象將被視為可展開，並在使用concat方法時展開其元素。
當該屬性被設置為false或者未被設置時，對象的行為將取決於其本身，不會被展開。
```

```JavaScript
// obj的Symbol.isConcatSpreadable被設置為true，因此在concat方法中它的元素被展開到新的陣列中。
let obj = {
  length: 2,
  0: 'foo',
  1: 'bar',
  [Symbol.isConcatSpreadable]: true
};

let arr = ['baz', 'qux'];

let result = arr.concat(obj);

console.log(result);
// Output: ['baz', 'qux', 'foo', 'bar']
```

```JavaScript
// Symbol.isConcatSpreadable
//用於自定義對象的可擴展性，特別是在使用Array.prototype.concat()方法時的行為。
// 當一個對象的Symbol.isConcatSpreadable屬性被設置為true時，該對象將被視為可展開，並在使用concat方法時展開其元素。
// 當該屬性被設置為false或者未被設置時，對象的行為將取決於其本身，不會被展開。

// 情境一
// 若建立了一個類陣列(array-like ch7-9)物件
// 並希望它被傳入concat()時行為會像一個真正的陣列
let arraylike = {
  length: 1,
  0: 1,
  [Symbol.isConcatSpreadable]: true,
};
[].concat(arraylike); // [1]

// 情境二
// 陣列的子類別都是預設可分散的(spreadable)的
// 若定義了一個陣列子類別並且不希望它與concat()並用的時候行為跟陣列一樣
// 可以新增一個取值器到子類別
class NonSpreadableArray extends Array {
  get [Symbol.isConcatSpreadable]() {
    return false;
  }
}

let a = new NonSpreadableArray(1, 2, 3);
[].concat(a).length; // => 1, 若 a 被分散(spreadable), 長度就會是三個元素
```

## Symbol.toPrimitive

```
Symbol.toPrimitive 是 JavaScript 中的一個特殊 symbol，它用於定義對象轉換為原始值的行為。當一個對象出現在一個需要原始值的上下文中（例如數學運算、字符串拼接等），JavaScript 引擎會嘗試調用該對象上的 Symbol.toPrimitive 方法。

Symbol.toPrimitive 是一個接受一個參數 hint 的方法，hint 可以是以下三種值之一：

"number"：表示引擎需要一個數字。
"string"：表示引擎需要一個字符串。
"default"：表示引擎需要根據上下文的默認行為（通常是數字）。
```

```JavaScript
const myObject = {
  [Symbol.toPrimitive](hint) {
    if (hint === 'number') {
      return 42;
    }
    if (hint === 'string') {
      return 'Hello, world!';
    }
    return 'default';
  }
};

console.log(myObject + 10);  // 52 (因為 hint 是 'number'，返回數字)
console.log(String(myObject));  // 'Hello, world!' (因為 hint 是 'string'，返回字符串)
console.log(myObject);  // 'default'（因為 hint 是 'default'，返回默認值）
```
