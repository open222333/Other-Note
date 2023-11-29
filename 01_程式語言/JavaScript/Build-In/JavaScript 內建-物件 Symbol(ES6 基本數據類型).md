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