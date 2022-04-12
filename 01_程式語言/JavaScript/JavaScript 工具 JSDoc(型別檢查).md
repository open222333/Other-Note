# JavaScript 工具 JSDoc(型別檢查)

## 參考資料

[JavaScript 註解 & 型別檢查 - 2021年02月10日](https://chupai.github.io/posts/2102/comments/)

[JSDoc](https://jsdoc.app/index.html)

# 註解

單行註解：

```JavaScript
// Hello
```

多行註解：

```JavaScript
/*
 Hello
 Hi
*/
```

# JSDoc

```
JSDoc 是最通用的 JavaScript 註解規範，透過特定格式的註解，可以快速建立 API 文件。
```

```JavaScript
單一js檔案開啟型別檢查
// @ts-check
忽略單一js檔案的類型檢查
// @ts-nocheck
```

專案根目錄下新增`jsconfig.json`設定檔

```json
{
  "compilerOptions": {
    "checkJs": true // 開啟型別檢查
  },
  // 使用 exclude 或 include 選項，設定要排除或包含的資料夾
  "exclude": ["node_modules", "dist"],
  "include": ["src/**/*"]
}
```

# JSDoc 標籤

[JSDoc Reference](https://www.typescriptlang.org/docs/handbook/jsdoc-supported-types.html)

JSDoc 註解格式必須以 /** 為開頭


## @type

```JavaScript
// JSDoc 註解格式必須以 /** 為開頭
/**
 * 計數變數
 * @type {number}
 */
let count: number;

/** @type {string} */
let str;
str = 'Hello!';

/** @type {Date} */
let now;
now = new Date();

/** @type {HTMLElement} */
let dom;
dom = document.querySelector('body');

// 複合型別
/** @type {string|boolean} */
let x;
x = '123';
x = true;

// 指定陣列元素的型別
/** @type {number[]} */
const ns = [];
[].push(1);

/** @type {Array.<number>} */
const ns = [];

/** @type {Array<number>} */
const ns2 = [];

// 物件字面值
/**
 * @type {{ a: string, b: number }}
 */
let obj;
obj = { a: '123', b: 123 };

// 指定 map-like 和 array-like 的物件
/**
 * @type {Object.<string, number>}
 */
let stringToNumber;

/** @type {Object.<number, object>} */
let arrayLike;

// 預設就是 any 任意型別, * 和 ? 等同 any
/** @type {any} */
let x;

/** @type {*} - can be 'any' type*/
let y;

/** @type {?} - unknown type*/
let z;

// 函式型別
/**
 * @type {function(number): number} Closure syntax
 */
let foo;
foo = function (a) {
  return a * a;
};

/**
 * @type {(a: number) => number} Typescript syntax
 */
let boo;
boo = function (a) {
  return a * a;
};
```

## @param

```JavaScript
// @param 語法和 @type 基本上相同，但用於標明函式參數，所以多了參數名稱。
/**
 * The square of a number
 * @param {number} number - 輸入數字
 * @return {number}
 */
function square(number) {
  return number * number;
}

/**
 * @param {{ name: string, age: number }} person
 */
function foo(person) {
  console.log(person.name, person.age);
}

/**
 * @param {Object} person - 某人
 * @param {string} person.name - 某人的名字
 * @param {number} person.age - 某人的年齡
 */
function foo(person) {
  console.log(person.name, person.age);
}

// ES6
/**
 * @param {Object} person - 某人
 * @param {string} person.name - 名字
 * @param {number} person.age - 年齡
 */
function foo({ name, age }) {
  console.log(name, age);
}

/**
 * @param {string=} p1 - 可選參數（Closure語法）
 * @param {string} [p2] - 可選參數（JSDoc語法）
 * @param {string} [p3 = 'test'] - 有預設值的可選參數（JSDoc語法）
 */
function foo(p1, p2, p3 = 'test') {}
```

## @typedef

```JavaScript
// 描述一個物件型別
/**
 * @typedef {Object} SpecialType - creates a new type named 'SpecialType'
 * @property {string} prop1 - a string property
 * @property {number} prop2 - a number property
 * @prop {number} [prop3] - an optional number property of SpecialType
 */

/** @type {SpecialType} */
let obj;
obj = { prop1: '123', prop2: 123 };
```

## @callback

```JavaScript
// @callback 與 @typedef 相似，但描述的是一個函式
/**
 * @callback Predicate
 * @param {string} data
 * @returns {boolean}
 */

/** @type {Predicate} */
const foo = function (str) {
  return !(str.length % 2);
};
```

## @class

```
ES6 有了 class 後，就沒必要使用 @class 了
```

```JavaScript
// @class（synonyms: @constructor）
// @class 可以標明函式為一個建構函式（Constructor）。
/**
 * Creates a new Person.
 * @class
 */
function Person() {}

const p = new Person();
```

## @this

```JavaScript
// @this 可以明確標示 this 關鍵字在這裡指的是什麼。
// 例如建構函式的方法：
/**
 * @class
 */
function Person() {
  this.name = '';
}

/**
 * @this {Person} - Person 實體
 */
Person.prototype.setName = function (name) {
  this.name = name;
};

// 或監聽器處理函式：
/**
 * @param {Event} event - 事件物件
 * @this {HTMLElement} - 監聽器綁定元素
 */
function clickHandler(event) {
  // ...
}
```

## @extends

```JavaScript
// 如果使用 extends 關鍵字來擴展一個現有的類別的時候，可以使用 @extends 標示。
/** 佇列 */
class Queue {}

/**
 * 優先佇列
 * @extends Queue
 */
class PriorityQueue extends Queue {}
```

## @enum

```JavaScript
// @enum 標籤描述一個靜態屬性值的全部相同的集合，簡單來說就是一個物件內的屬性皆為相同型別，且不允許新增額外屬性。
/** @enum {number} */
const JSDocState = {
  BeginningOfLine: 0,
  SawAsterisk: 1,
  SavingComments: 2,
};
```

## @template

```JavaScript
// @templete 非 JSDoc 標準，只在 google closure compiler 中有提及，可以用來宣告 泛用型別（Generic Type），是 TypeScript 中的型別。
// 泛用型別（Generic Type） 目的在於成員之間提供有意義的約束，這些成員可以是類別的實體、類別的方法、函式參數、函式回傳值。

/**
 * @template T
 * @param {T} x
 * @return {T}
 */
function foo(x) {
  return x;
}
```