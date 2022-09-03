# JavaScript 基本 函式 Function

```
```

## 目錄

- [JavaScript 基本 函式 Function](#javascript-基本-函式-function)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [定義函式](#定義函式)
	- [函式宣告](#函式宣告)
	- [函式運算式(function expressions)](#函式運算式function-expressions)
	- [箭號函式](#箭號函式)
	- [巢狀函式(Nested Functions)](#巢狀函式nested-functions)
- [調用函式](#調用函式)
	- [函式調用](#函式調用)
		- [條件式調用(Conditional Invocation)](#條件式調用conditional-invocation)
	- [方法調用](#方法調用)
		- [方法鏈串(Method Chaining)](#方法鏈串method-chaining)
	- [建構器調用](#建構器調用)
	- [間接調用](#間接調用)
	- [隱含的函式調用](#隱含的函式調用)
- [函式引數與參數](#函式引數與參數)
	- [選擇性參數和預設值](#選擇性參數和預設值)
	- [其餘參數和長度不定的引數列](#其餘參數和長度不定的引數列)
	- [Arguments物件](#arguments物件)
	- [函式呼叫分散運算子(Spread Operator)](#函式呼叫分散運算子spread-operator)
	- [解構函式引數為參數](#解構函式引數為參數)
	- [引數型別](#引數型別)
- [函式作為值](#函式作為值)
	- [定義自己的函式特性](#定義自己的函式特性)
- [函式作為命名空間(Namespaces)](#函式作為命名空間namespaces)
	- [匿名函式(anonymous function)](#匿名函式anonymous-function)
- [閉包(Closures)](#閉包closures)
- [函式特性、方法與建構器](#函式特性方法與建構器)
	- [length特性](#length特性)
	- [name特性](#name特性)
	- [prototype特性](#prototype特性)
	- [call()和apply()方法](#call和apply方法)
	- [bind()方法](#bind方法)
	- [toString()方法](#tostring方法)
	- [Function()建構器](#function建構器)
- [函式型程式設計](#函式型程式設計)
	- [以函式處理陣列](#以函式處理陣列)
- [高階函式(higher-order function)](#高階函式higher-order-function)

## 參考資料

[]()

# 定義函式

## 函式宣告

```
函式名稱會成為一個變數，其值則是函式本身。
```

```JavaScript
// 印出o的每個特性的名稱與值。回傳undefined。
function printprops(o) {
  for (let p in o) {
    console.log(`${p}: ${o[p]}\n`);
  }
}

// 計算笛卡兒點(x1,y1)與(x2,y2)之間的距離(distance)
function distance(x1, y1, x2, y2) {
  let dx = x2 - x1;
  let dy = y2 - y1;
  return Math.sqrt(dx * dx + dy * dy);
}

// 一個地回函是(會呼叫自身的)，用以計算階乘(factorials)
// 回想到x! 是 x與小於它的所有正整數之乘積(product)
function factorial(x) {
  if (x <= 1) return 1;
  return x * factorial(x - 1);
}
```

## 函式運算式(function expressions)

```
看起來像函式宣告
出現在一個較大的運算式或述句的情境中
名稱是選擇性的
```

```JavaScript
// 這個函式運算式定義了會計算其引數平方值的一個函式
// 指定給一個變數
const square = function(x) {
  return x * x;
};

// 函式運算式可以包含名稱，這適合用來進行遞迴(recursion)
const f = function fact(x) {
  if (x <= 1) return 1;
  else return x * fact(x - 1);
};

// 函式運算式也可被用作其他函數的引數
[3, 2, 1].sort(function(a, b) {
  return a - b;
});

// 函式運算式有時會在定義後即刻被調用
let tensquared = (function(x) {
  return x * x;
}(10));
```

## 箭號函式

```
從定義處的範疇繼承this關鍵字，而非像其他方式定義的函式會有他們自己的調用情境
```

```JavaScript
// 以下兩個函式相同
const sum = (x, y => {
  return x + y;
});

const sum2 = (x, y) => x + y;

// 只有一個引數可省略括弧
const polynomial = x => x * x + 2 * x + 3;

// 沒有引數需要有空括弧
const constantFunc = () => 42;

// 箭號函式主體是單一個return述句，但回傳的運算式是一個物件字面值，須將字面值放在括弧內
const f = x => {
  return {
    value: x
  };
}; // f()回傳一個物件
const g = x => ({
  value: x
}); // g()回傳一個物件
const h = x => {
  value: x
}; // 不行 什麼都不回傳
// const i = x => {
//   v: x,
//   w: x
// }; // 語法錯誤

// 製作一個陣列的拷貝並移除null的元素
let filtered = [1, null, 2, 3].filter(x => x !== null); // filtered == [1,2,3]
// 計算一些數字的平方
let squares = [1, 2, 3, 4].map(x => x * x); // squares == [1,4,9,16]
```

## 巢狀函式(Nested Functions)

```JavaScript
// 巢狀函式
function hypotenuse(a, b) {
  function square(x) {
    return x * x;
  }
  // 開根號
  return Math.sqrt(square(a) + square(b));
}
```

# 調用函式

## 函式調用

```JavaScript
// 定義並調用一個函式來判斷我們是否處於嚴格模式
// 嚴格模式 調用情境(this值) 會是 undefined
// 非嚴格模式 調用情境(this值) 會是 全域物件
const strict = (function () {
  return !this;
}());
console.log(strict)
```

### 條件式調用(Conditional Invocation)

```JavaScript
// 完整細節 4.5.1
(f !== null && f !== undefined) ? f(x) : undefined;
```

## 方法調用

```JavaScript
// 物件o 函式f 定義方法m
o.m = f;

// 調用方法m
o.m();

// 調用方法 帶入引數
o.m(x, y);
o["m"](x, y);
a[0](z); // 假設a[0] 是一個函式
```

### 方法鏈串(Method Chaining)

```JavaScript
// 循序執行三個非同步運算，並處理錯誤。
doStepOne().then(doStepTwo).then(doStepThree).catch(handleErrors);

/**
 * 若方法沒有回傳值 可考慮回傳this
 * 若API都使用此方法 則可以使用 方法鏈串
*/
new Square().x(100).y(100).size(50).outline("red").fill("blue").draw();
```

## 建構器調用

在函式或方法調用的前面使用`new`關鍵字。

建構器調用與一般函式或方法調用差別在於處理引數、調用情境(this)、回傳值的方式。

建構器調用會創建一個新的空物件，會繼承該建構器的prototype特性所指定的物件。

```JavaScript
// 以下兩行是等效的
o = new Object();
o = new Object;
```

## 間接調用

JavaScript函式是物件，其中兩個方法`call()`和`apply()`，會間接的調用函式。

## 隱含的函式調用

- 物件定義有取值器(getters)或設值器(setters)，查詢或設定物件特性的值可能會調用那些方法
- 當物件被用在字串情境中(例如與一個字串串接時)，toString()方法會被呼叫。數值情境中，valueOf()會被調用。
- 以迴圈執行可迭代物件會發生數個方法呼叫。
- 帶標記的範本字面值(tagged template literal)是偽裝起來的函式調用。14.5
- 代理物件(proxy objects)的行為完全是由函式控制。

# 函式引數與參數

## 選擇性參數和預設值

```JavaScript
function getPropertyNames_2(o, a = []) {
  for (let property in o) a.push(property);
  return a;
}
```

## 其餘參數和長度不定的引數列

```JavaScript
function max(first = -Infinity, ...rest) {
  let maxValue = first; // 先假設第一個引數最大
  for (let n of rest) {
    if (n > maxValue) {
      maxValue = n;
    }
  }
  return maxValue;
}
```

## Arguments物件

```
Arguments物件是類陣列物件，允許傳入韓式的引數值藉由數字取用，而非藉由名稱。

須避免使用
```

```JavaScript
function max(x) {
  let maxValue = -Infinity;
  // 以迴圈跑過引數，尋找並記起最大的。
  for (let i = 0; i < arguments.length; i++) {
    if (arguments[i] > maxValue) maxValue = arguments[i];
  }
  return maxValue;
}
```

## 函式呼叫分散運算子(Spread Operator)

```
分散運算子 ...
用來拆分或分散一個陣列的元素。
```

```JavaScript
let numbers = [5, 2, 10, -1, 9, 100, 1];
let a = Math.min(...numbers);

// 此函式接受一個函式，並回傳一個包裹起來的版本
function timed(f) {
  return function (...args) {
    console.log(`Entering function ${f.name}`);
    let startTime = Date.now();
    try {
      // 把所有引數傳入縮包裹的函示(wrapped function)
      return f(...args); // 把args再次分散
    }
    finally {
      // 回傳包裹的回傳值之前，印出經過的時間
      console.log(`Exiting ${f.name} after ${Date.now() - startTime}ms`);
    }
  };
}

// 以暴力法(brute force)計算1到n之間數字總和
function benchmark(n) {
  let sum = 0;
  for (let i = 1; i <= n; i++) {
    sum += i;
  }
  return sum;
}

// 調用測試函式 有計時的版本
let result = timed(benchmark)(1000000);
console.log(result);
```

## 解構函式引數為參數

```
以一串引數值調用一個函式，那些值最終會被指定給宣告在函式定義中的參數。
```

```JavaScript
function vectorAdd(v1, v2) {
  return [v1[0] + v2[0], v1[1] + v2[1]];
}

let a = vectorAdd([1, 2], [3, 4]);
console.log(a);

function vectorAdd_2([x1, y1], [x2, y2]) {
  return [x1 + x2, y1 + y2];
}

let b = vectorAdd_2([1, 2], [3, 4]);
console.log(b);

// 以一個純量值(scalar value) 乘上向量{x, y}
function vectorMultiply({
  x,
  y
}, scalar) {
  return {
    x: x * scalar,
    y: y * scalar
  };
}

let c = vectorMultiply({
  x: 1,
  y: 2
}, 2)
console.log(c);

// 把一個名稱的特性解構到具有不同名稱的參數
function vectorAdd_3({
  x: x1,
  y: y1
}, {
  x: x2,
  y: y2
}) {
  return {
    x: x1 + x2,
    y: y1 + y2
  };
}

let d = vectorAdd_3({
  x: 1,
  y: 2
}, {
  x: 3,
  y: 4
});
console.log(d);

// 以解構的參數定義參數預設值。
// 用於2D 3D向量乘法
// 把向量{x,y}或{x,y,z}乘以一個純量值
function vectorMultiply_2({
  x,
  y,
  z = 0
}, scalar) {
  return {
    x: x * scalar,
    y: y * scalar,
    z: z * scalar
  };
}

let e = vectorMultiply_2({
  x: 1,
  y: 2
}, 2);
console.log(e);

// 把指定數目的元素從一個陣列拷貝到另一個陣列中，為每個陣列選擇性指定起始的偏移量(starting offsets)
// 模仿 python 函式引數 name=value
function arraycopy({
  from,
  to = from,
  n = from.length,
  fromIndex = 0,
  toIndex = 0
}) {
  let valuesToCopy = from.slice(fromIndex, fromIndex + n);
  to.splice(toIndex, 0, ...valuesToCopy);
  return to;
}

let f = [1, 2, 3, 4, 5],
  g = [9, 8, 7, 6, 5];

let h = arraycopy({
  from: a,
  n: 3,
  to: b,
  toIndex: 4
});
console.log(h);

// 可在解構一個物件時使用一個其餘參數 ...
// 其餘參數的值會是一個物件
// 把向量{x,y}或{x,y,z}乘以一個純量值，保留其他特性
function vectorMultiply_3({
  x,
  y,
  z = 0,
  ...props
}, scalar) {
  return {
    x: x * scalar,
    y: y * scalar,
    z: z * scalar,
    ...props
  };
}

let i = vectorMultiply_3({
  x: 1,
  y: 2,
  w: -1
}, 2);
console.log(i);
```

## 引數型別

```
JavaScript的方法參數沒有宣告的型別，也不會進行型別檢查。

為參數挑選有描述性的名稱，並在函式的註解中詳細說明。17.8語言擴充功能 可放置一層型別檢查。

建議 函式在接收到不對的值時就失敗
```

```JavaScript
// 執行型別檢查的範例
function sum(a) {
  // 回傳可迭代物件a的元素總和
  // a的元素必須全都是數字
  let total = 0;
  for (let element of a) {
    // 如果a不是可迭代的，拋出TypeError
    if (typeof element !== "number") {
      throw new TypeError("sum(): element must be numbers");
    }
    total += element;
  }
  return total;
}
```

# 函式作為值

```
函式最重要的特色：可以被定義並調用
```

```JavaScript
// 函式定義 創建一個新的函式物件，並指定給變數square
// 一個函式的名稱只是一個變數的名稱，用以參考物件。
function square(x) { return x * x; };

// 函式可以指定給另一個變數，仍然以相同方式運行
let s = square; // 現在s參考跟square相同的函式
square(4); // => 16
s(4); // => 16

// 函式可以被指定給物件的特性，而非變數。 此時稱為'方法'。
let o = { square: function (x) { return x * x; } }; // 物件字面值
let y = o.square(16); // y == 256

// 函式不需要名稱
// 範例 被指定給陣列元素時
let a = [x => x * x, 20];
a[0](a[1]); // => 400
```

## 定義自己的函式特性

```
函式並非原始值(primitive values)，而是特殊的物件，意味著函式可以有特性。
```

```JavaScript
// 初始化此函式物件的counter特性 函式宣告前進行指定
uniqueInteger.counter = 0;

function uniqueInteger() {
  return uniqueInteger.counter++; // 回傳並遞增counter特性
}

console.log(uniqueInteger());
console.log(uniqueInteger());

// 計算階乘 並把結果快取在該函式本身的特性中
function factorial(n) {
  if (Number.isInteger(n) && n > 0) { // 僅限正整數
    if (!(n in factorial)) {
      // 如果沒有快取結果 就計算並快取之
      factorial[n] = n * factorial(n - 1);
    }
    return factorial[n]; // 回傳快取的結果
  } else {
    return NaN; // 輸入有問題回傳NaN
  }
}

factorial[1] = 1;
console.log(factorial(6));
console.log(factorial[5]);
```

# 函式作為命名空間(Namespaces)

```JavaScript
function chuckNamespace() {
  // 程式碼塊
  // 在這裡定義的任何變數都會是此函式的區域值
  // 不會把全域命名空間弄亂
}
```

## 匿名函式(anonymous function)

```JavaScript
(function () {
  // 程式碼塊
}()); // 直截調用
```

# 閉包(Closures)

```
一個函式物件和該函式的變數在其中解析的一個範疇之組合。
```

```JavaScript
// 閉包(Closures)
let scope = "global scope"; // 一個全域變數
function checkscope() {
  let scope = "local scope"; // 一個區域變數
  function f() { return scope; } // 回傳在範疇中的值
  return f();
}
checkscope(); // => "local scope"

function checkscope_1() {
  let scope = "local scope"; // 一個區域變數
  function f() { return scope; } // 回傳在範疇中的值
  return f;
}

let s = checkscope_1()();
console.log(s);

// 修改 8-4-1 uniqueInteger()
// 外層函式回傳後 沒有其他程式碼能看到counter變數
let uniqueInteger = (function () { // 定義並調用
  let counter = 0; // 下面函式的私有狀態
  return function () { return counter++; };
}())

console.log(uniqueInteger());
console.log(uniqueInteger());

// 在同一個外層函式中定義兩個或更多內嵌函式並共用相同的範疇
function counter() {
  let n = 0;
  return {
    count: function () { return n++; },
    reset: function () { n = 0; }
  };
}

let c = counter(),
  d = counter(); // 創建兩個計數器
console.log('--------');
console.log(c.count());
console.log(d.count());
c.reset();
console.log(c.count());
console.log(d.count());

// 結合closure技巧 和 特性取值器(getters)
function counter_getter(n) {
  return {
    // 特性取值器方法回傳並遞增私有計數器變數
    get count() { return n++; },
    // 特性取值器不允許n的值下降
    set count(m) {
      if (m > n) n = m;
      else throw Error("count can only be set to a larger value");
    }
  }
}

let e = counter_getter(1000);
console.log('--------');
console.log(e.count());
console.log(e.count());
e.count = 2000;
console.log(e.count());
e.count = 2000; // 錯誤 只能被設為較大的值
```

# 函式特性、方法與建構器

## length特性

```
唯讀特性length指出該函式的元數，也就是參數列中宣告的參數數目。
```

## name特性

```
唯讀特性name指出該函式的被定義時所用的名稱。
```

## prototype特性

```
prototype特性指向被稱為原型物件的一個物件。
當函式被用作一個建構器(constructor)，新創建的物件會從這個原型物件繼承特性。
```

## call()和apply()方法

```
能間接調用一個函式。

call()和apply()第一個引數都是函式要在其上被調用的物件，這個引數就是調用情境，並且會變成函式主體中this關鍵字的值。
```

```JavaScript
// 把物件o名為m的方法取代成另一個版本
function trace(o, m) {
  let original = o[m]; // 將原方法記錄在closure中
  o[m] = function (...args) {
    // 定義新的方法
    console.log(new Date(), "Entering:", m); // 記錄訊息
    let result = original.apply(this, args); // 調用原本的
    console.log(new Date(), "exiting:", m); // 記錄訊息
    return result;
  };
}
```

## bind()方法

```
主要功能把一個函式繫結(bind)至一個物件

在第一個引數之後，傳入給bind()的任何引數都會跟那個this值一起被繫結。有時被稱作currying
```

```JavaScript
// bind()方法
function f(y) { return this.x + y; };
let o = { x: 1 };
let g = f.bind(o); // 呼叫g(x)會在o上調用f()
g(2)
let p = { x: 10, g };
p.g(2)

// currying
let sum = (x, y) => x + y; // 回傳2個引數的總和
let succ = sum.bind(null, 1);
succ(2) // => 3:x 被繫結至1 傳入2作為y引數

function f(y, z) { return this.x + y + z; }
let g_ = f.bind({ x: 1 }, 2);
g_(3) // => 6
```

## toString()方法

```
函式的toString()方法會回傳該函式的原始碼，內建的函式同長回傳其中包含"[native code]" 之類東西的一個字串最為函式主體。
```

## Function()建構器

```
因函式是物件，所以存在Function()建構器可以用來創建新的函式。

Function()建構器預期任意數目的字串引數。
最後一個引數是函式主體的文字。

Function()建構器 允許JavaScript函式在執行期間(runtime)動態的建立並編譯。

Function()建構器 會在每次被呼叫時剖析(parses)函式主體並創建一個新的函式物件。
建構器的呼叫出現在迴圈或經常呼叫的函式會很沒效率。

Function()建構器 創建的函式並不使用語彙範疇(lexical scoping)，會永遠被當做頂層函式(top-level functions)來編譯。
Function()建構器 最好當成全域範疇
[語彙範疇（Lexical Scope）](https://ithelp.ithome.com.tw/articles/10202649)
```

```JavaScript
const f = new Function("x", "y", "return x * y;");
// 與上方是相同
const f = function (x, y) { return x * y; };

// Function()建構器 語彙範疇(lexical scoping) 最好當成全域範疇
let scope = "global";

function constructFunction() {
  let scope = "local";
  return new Function("return scope;"); // 沒有捕捉區域範疇
}

constructFunction()();
```

# 函式型程式設計

```
JavaScript可把函式當物件操作，因此可運用functional programming技巧。
```

## 以函式處理陣列

```JavaScript
// 以不是function風格 計算平均值與標準差
let data = [1, 2, 3, 5, 5];

// 平均(mean) 是元素總和除以元素的數目
let total = 0;
for (let i = 0; i < data.length; i++) total += data[i];
let mean = total / data.length;

// 計算標準差 每個元素與平均值的偏差(deviation)之平方加總起來
total = 0;
for (let i = 0; i < data.length; i++) {
  let deviation = data[i] - mean;
  total += deviation * deviation;
}

let stddev = Math.sqrt(total * (data.length - 1));

// 以簡潔的function風格 計算平均值與標準差
const sum = (x, y) => x + y;
const square = x => x * x;

let mean_2 = data.reduce(sum) / data.length;
let deviation_2 = map(data, x => x - mean);
let stddev_2 = Math.sqrt(deviation_2.map(square).reduce(sum) / (data.length - 1));
```

# 高階函式(higher-order function)

```
高階函式作用在函式上的一種函式，接受一或多個函式作為引數，並回傳一個新的函式。
```