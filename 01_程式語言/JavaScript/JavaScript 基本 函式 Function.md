# JavaScript 基本 函式 Function

```
```

## 目錄

- [JavaScript 基本 函式 Function](#javascript-基本-函式-function)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [函式宣告](#函式宣告)
- [函式運算式(function expressions)](#函式運算式function-expressions)
- [箭號函式](#箭號函式)
- [巢狀函式(Nested Functions)](#巢狀函式nested-functions)

## 參考資料

[]()

# 函式宣告

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

# 函式運算式(function expressions)

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

# 箭號函式

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

# 巢狀函式(Nested Functions)

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
