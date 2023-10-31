# JavaScript 筆記 Metaprogramming(元程式設計)

```
Metaprogramming是指一種程式設計的方式，其中程式碼被設計來操作或生成其他程式碼，通常在執行時期（runtime）而非編譯時期。
這使得程式碼能夠動態地改變、擴展或生成新的程式碼，增強了程式的靈活性和泛用性。

以下是一些常見的metaprogramming的技術和用法：

宏展開（Macro Expansion）：

在編譯時期，將宏（macro）轉換為實際的程式碼。這樣的宏可以擴展為一系列的原始程式碼，以減少冗餘或提高可讀性。
代碼生成（Code Generation）：

在執行時期生成程式碼。這可以通過字串操作、模板引擎或動態語言的 eval 函式實現。
動態語言特性：

許多動態語言（如Python、Ruby）具有反射（reflection）和元類（metaclasses）等功能，允許在執行時期動態地檢查和修改程式碼。
裝飾器（Decorators）：

在某些程式語言中，可以使用裝飾器來修改或擴展函式的行為。這是Python中的一個例子。
模板程式設計（Template Programming）：

使用模板進行程式碼生成，這在C++等語言中很常見。模板允許編寫通用程式碼，以處理各種不同類型的數據。
自省（Introspection）：

在執行時期檢查程式碼的特性，這通常涉及使用反射或相似的機制。
Metaprogramming通常需要謹慎使用，因為它可能使程式碼更難理解、維護，並引入潛在的錯誤。然而，在某些情況下，它也是一種強大的工具，可以提高開發效率和程式碼的泛用性。
```

## 目錄

- [JavaScript 筆記 Metaprogramming(元程式設計)](#javascript-筆記-metaprogramming元程式設計)
	- [目錄](#目錄)
- [特性的屬性](#特性的屬性)
	- [Object.getOwnPropertyDescriptor()](#objectgetownpropertydescriptor)
	- [Object.defineProperty()](#objectdefineproperty)
	- [Object.defineProperties()](#objectdefineproperties)
	- [Object.isExtensible() 以及 Object.preventExtensions()](#objectisextensible-以及-objectpreventextensions)
	- [Object.seal(), Object.freeze(), Object.isFrozen()](#objectseal-objectfreeze-objectisfrozen)

# 特性的屬性

```
configurable（可配置）：
表示該屬性是否可以被刪除，以及是否可以修改它的特性。如果設置為false，則無法使用 delete 刪除該屬性，並且無法修改其特性。

enumerable（可枚舉）：
表示該屬性是否能夠在 for...in 循環中被枚舉。如果設置為false，則該屬性將不會出現在枚舉中。

writable（可寫入）：
表示該屬性是否可以被賦值。如果設置為false，則無法修改該屬性的值。

extensible（可擴充）：
新的特性是否可被新增到那個物件。
一旦使一個物件變為不可擴充，將無法再次使它變成可擴充。

get（getter）：
一個用於獲取屬性值的函式。當訪問屬性時調用。

set（setter）：
一個用於設置屬性值的函式。當修改屬性時調用。
```

## Object.getOwnPropertyDescriptor()

```JavaScript
// Object.getOwnPropertyDescriptor()
// 為一個指定的物件指名的特性獲取特性描述器
// 回傳 { value: 1, writable:true, enunerable:true, configurable:true}
Object.getOwnPropertyDescriptor({ x: 1 }, "x");

// 這裡有一個物件具有一個唯讀的存取器特性
const random = {
  get octet() {
    return Math.floor(Math.random() * 256);
  },
};

// 回傳 {get: /*func*/, set:undefined, enumerable:true, configurable:true}
Object.getOwnPropertyDescriptor(random, "octet");

// 為繼承而來的特性和不存在的特性回傳 undefined
Object.getOwnPropertyDescriptor({}, "x"); // => undefined 沒有這個特秀
Object.getOwnPropertyDescriptor({}, "toString"); // => 繼承的
```

## Object.defineProperty()

```JavaScript
// Object.defineProperty()
// 設定一個特性的屬性或以指定的屬性建立一個新的特性, 省略的屬性會被當作 false undefined
let o = {}; // 沒有特性
// 新增一個不可列舉的資料特性x並帶有值 1
Object.defineProperty(o, "x", {
  value: 1,
  writable: true,
  enumerable: false,
  configurable: true,
});

console.log(o.x); // => 1
console.log(Object.keys(o)); // => []

// 修改特性x讓他是唯讀的
Object.defineProperty(o, "x", { writable: false });
// 試著修改特性的值
o.x = 2; // 失敗 嚴格模式中會擲出 TypeError
console.log(o.x); // => 1
// 把x從一個資料特性變為存取器特性
Object.defineProperty(o, "x", {
  get: function () {
    return 0;
  },
});
console.log(o.x); // => 0
```

## Object.defineProperties()

```JavaScript
// Object.defineProperties()
// 一次建立或修改多個特性
let p = Object.defineProperties(
  {},
  {
    x: { value: 1, writable: true, enumerable: true, configurable: true },
    y: { value: 1, writable: true, enumerable: true, configurable: true },
    r: {
      get() {
        return Math.sqrt(this.x * this.x + this.y + this.y);
      },
      enumerable: true,
      configurable: true,
    },
  }
);

console.log(`Object.defineProperties() p.r => ${p.r}`);
```

## Object.isExtensible() 以及 Object.preventExtensions()

```JavaScript
// 用來限制對象的修改，提高程式的穩定性和可靠性。

// Object.isExtensible()
// 檢查對象是否是可擴展的（extensible）

// Object.preventExtensions()
// 防止對象擴展，即禁止向其添加新屬性，現有的特性也無法配置或刪除。

const obj = { property: "value" };

console.log(Object.isExtensible(obj)); // true

// 防止對象擴展，即禁止向其添加新屬性
Object.preventExtensions(obj);

console.log(Object.isExtensible(obj)); // false
```

## Object.seal(), Object.freeze(), Object.isFrozen()

```JavaScript
// Object.seal() 方法將對象密封，防止向其添加新屬性並將現有屬性設為不可配置。
// 密封的對象仍然可以修改現有屬性的值。
// Object.isSealed(obj) 可以用於檢查對象是否被密封。

const obj = { property: "value" };
console.log(Object.isSealed(obj)); // false

Object.seal(obj);

// 無法添加新屬性
obj.newProperty = "new value";
console.log(obj.newProperty); // undefined

// 可以修改現有屬性的值
obj.property = "new value";
console.log(obj.property); // "new value"

console.log(Object.isSealed(obj)); // true

// Object.freeze() 方法凍結對象，防止向其添加新屬性，將現有屬性設為不可配置和不可寫。
// 凍結的對象的屬性值不能被修改。
// Object.isFrozen(obj) 可以用於檢查對象是否被凍結。

const obj = { property: "value" };
console.log(Object.isFrozen(obj)); // false

Object.freeze(obj);

// 無法添加新屬性
obj.newProperty = "new value";
console.log(obj.newProperty); // undefined

// 不能修改現有屬性的值
obj.property = "new value";
console.log(obj.property); // "value"

console.log(Object.isFrozen(obj)); // true
```