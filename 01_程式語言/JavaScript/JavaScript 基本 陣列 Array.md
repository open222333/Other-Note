# JavaScript 基本 陣列 Array

## 目錄

- [JavaScript 基本 陣列 Array](#javascript-基本-陣列-array)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [陣列方法](#陣列方法)
  - [陣列 方法 - 迭代](#陣列-方法---迭代)
  - [陣列 方法 - 攤平(flattened)](#陣列-方法---攤平flattened)
  - [陣列 方法 - 相加](#陣列-方法---相加)
  - [陣列 方法 - 堆疊與佇列](#陣列-方法---堆疊與佇列)
  - [陣列 方法 - 操作子陣列](#陣列-方法---操作子陣列)
  - [陣列 方法 - 搜尋與排序](#陣列-方法---搜尋與排序)
  - [陣列 方法 - 字串轉換](#陣列-方法---字串轉換)
  - [靜態的陣列函式](#靜態的陣列函式)
- [類陣列物件](#類陣列物件)

## 參考資料

# 陣列方法

## 陣列 方法 - 迭代

```javascript
// forEach(): 為每個元素調用所指定的函式
// 計算總和
let data = [1, 2, 3, 4, 5],
  sum = 0;
data.forEach(value => {
  sum += value;
});
console.log(sum);
// 遞增每個元素
data.forEach(function(v, i, a) {
  a[i] = v + 1;
});
console.log(data);

// map(): 被調用的陣列的每個元素傳入指定的函式
let a = [1, 2, 3];
console.log(a.map(x => x * x));
console.log(a);

// filter():回傳陣列並篩選符合條件的元素
let a2 = [5, 4, 3, 2, 1];
console.log(a2.filter(x => x < 3));
console.log(a2.filter((x, i) => i % 2 === 0));
// let dense = sparse.filter(() => true);
a2 = a2.filter(x => x !== undefined && x !== null);
console.log(a2);

// find() findIndex() 與 filter()類似 但找到第一個符合條件即停止迭代
let a3 = [1, 2, 3, 4, 5];
// findIndex() 回傳索引值
console.log(a3.findIndex(x => x === 3));
console.log(a3.findIndex(x => x < 0)); // -1
// find() 回傳值
console.log(a3.find(x => x % 5 === 0));
console.log(a3.find(x => x % 7 === 0)); // undefiined

// every() 和 some() 陣列判別式
// every() 須全都符合條件才返回true
console.log(a3.every(x => x < 10)); // true 所有值都小於10
console.log(a3.every(x => x % 2 === 0)); // false 並非每個數都偶數
// some() 部分符合就返回true
console.log(a3.some(x => x % 2 === 0)); // true
console.log(a3.some(isNaN)) // false

// reduce() 和 reduceRight()
// reduce(function(函式), initial value(初始值)) 低索引到高索引
console.log(a3.reduce((x, y) => x + y, 0)); // 值的總和
console.log(a3.reduce((x, y) => x * y, 1)); // 值的乘積
console.log(a3.reduce((x, y) => (x > y) ? x : y)); // 最大值
// reduceRight(function(函式), initial value(初始值)) 高索引到低索引
let a4 = [2, 3, 4];
console.log(a4.reduceRight((acc, val) => Math.pow(val, acc))); // 2^(3^4)
```

## 陣列 方法 - 攤平(flattened)

```JavaScript
// flat() 和 flatMap()
// 攤平
console.log([1, [2, 3]].flat()); // [ 1, 2, 3 ]
console.log([1, [2, [3]]].flat()); // [ 1, 2, [ 3 ] ]
// 攤平多層
let a = [1, [2, [3, [4]]]];
console.log(a.flat(1)); // [ 1, 2, [ 3, [ 4 ] ] ]
console.log(a.flat(2)); // [ 1, 2, 3, [ 4 ] ]
console.log(a.flat(3)); // [ 1, 2, 3, 4 ]
console.log(a.flat(4)); // [ 1, 2, 3, 4 ]
// flat().map() = flatMap()
let phrases = ['hello world', 'the definitive guide'];
let words = phrases.flatMap(phrases => phrases.split(" "));
console.log(words); // [ 'hello', 'world', 'the', 'definitive', 'guide' ]
console.log([-2, -1, 1, 2].flatMap(x => x < 0 ? [] : Math.sqrt(x))); // [ 1, 1.4142135623730951 ] = [1, 2**0.5]
```

## 陣列 方法 - 相加

```JavaScript
// concat() 相加陣列
// 回傳並創建新陣列 可考慮使用 push() splice()就地修改陣列
let a = [1, 2, 3];
console.log(a.concat(4, 5)); // [ 1, 2, 3, 4, 5 ]
console.log(a.concat([4, 5], [6, 7])); 
// [
//   1, 2, 3, 4,
//   5, 6, 7
// ]
console.log(a.concat(4, [5, [6, 7]])); // [ 1, 2, 3, 4, 5, [ 6, 7 ] ]
console.log(a); // [ 1, 2, 3 ] - 原陣列沒有變
```

## 陣列 方法 - 堆疊與佇列

```JavaScript
// push() pop() shift() unshift()的堆疊與佇列
// push() 從結尾插入
// pop()  從結尾移除
console.log('====push pop====')
let stack = [];
stack.push(1, 2);
console.log(stack);
console.log(stack.pop());
console.log(stack);
stack.push(3);
console.log(stack);
console.log(stack.pop());
console.log(stack);
stack.push([4, 5]);
console.log(stack);
console.log(stack.pop());
console.log(stack);
console.log(stack.pop());
console.log(stack);

console.log('====shift====')
// shift() 從開頭開始移除
let q = [];
q.push(1, 2);
console.log(q.shift());
q.push(3);
console.log(q.shift());
console.log(q.shift());

console.log('====unshift====')
// unshift() 多個引數同時插入
// 一個個插入與同時插入 順序不同
let a = [];
console.log(a.unshift(1));
console.log(a.unshift(2));
console.log(a.unshift(3));
console.log(a.unshift(4));
console.log(a.unshift(5));
console.log(a)
a = [];
console.log(a.unshift(1, 2, 3, 4, 5));
console.log(a);
```

## 陣列 方法 - 操作子陣列

```JavaScript
// 使用 slice()、splice()、fill()、與copyWithin()操作子陣列
// slice(start, end)
// 回傳指定陣列的一個切片或子陣列 
let a = [1, 2, 3, 4, 5];
console.log('a陣列:' + a);
console.log(a.slice(0, 3)); // [ 1, 2, 3 ]
console.log(a.slice(3)); // [ 4, 5 ]
console.log(a.slice(1, -1)); // [ 2, 3, 4 ]
console.log(a.slice(-3, -2)); // [ 3 ]
console.log('a陣列:' + a);

// splice()
// 在陣列中插入或移除的通用方法，會修改被調用的陣列，前兩個引數指出哪些元素要被刪除，之後任意引數則是欲加入元素
let b = [1, 2, 3, 4, 5, 6, 7, 8];
console.log('b陣列:' + b);
console.log(b.splice(4)); // [ 5, 6, 7, 8 ]
console.log('b陣列:' + b);
console.log(b.splice(1, 2)); // [ 2, 3 ]
console.log('b陣列:' + b);
console.log(b.splice(1, 1)); // [ 4 ]
console.log('b陣列:' + b);

let c = [1, 2, 3, 4, 5];
console.log('c陣列:' + c);
console.log(c.splice(2, 0, 'a', 'b')); // []
console.log('c陣列:' + c);
console.log(c.splice(2, 2, [1, 2], 3)); // [ 'a', 'b' ]
console.log('c陣列:' + c);

// fill()
// 將一個陣列的元素或切片設為一個指定的值，修改被調用的陣列
let d = new Array(5); // 宣告長度為5 沒有元素的陣列
console.log('d陣列:' + d);
console.log(d.fill(0)); // [ 0, 0, 0, 0, 0 ]
console.log('d陣列:' + d);
console.log(d.fill(9, 1)); // [ 0, 9, 9, 9, 9 ]
console.log('d陣列:' + d);
console.log(d.fill(8, 2, -1)); // [ 0, 9, 8, 8, 9 ]
console.log('d陣列:' + d);

// copyWithin(目的索引, 要拷貝的元素索引(預設0), 要拷貝的元素索引結尾)
// 把陣列的一個切片複製到該陣列的新位置，會修改被調用的陣列。高效能，適用於具型陣列。
let e = [1, 2, 3, 4, 5];
console.log('e陣列:' + e);
console.log(e.copyWithin(1)); // 
console.log('e陣列:' + e);
console.log(e.copyWithin(2, 3, 5)); // 把3-5 拷貝到索引2
console.log('e陣列:' + e);
console.log(e.copyWithin(0. - 2));
console.log('e陣列:' + e);
```

## 陣列 方法 - 搜尋與排序

```JavaScript
// 陣列的搜尋和排序方法
// indexOf() 
// 從開頭到結尾搜尋，回傳指定值的元素索引，若無則回傳-1
// lastIndexOf()
// 從結尾到開頭搜尋，回傳指定值的元素索引，若無則回傳-1
let a = [0, 1, 2, 1, 0];
console.log('a.indexOf(1) => ' + a.indexOf(1));
console.log('a.lastIndexOf(1) => ' + a.lastIndexOf(1));
console.log('a.indexOf(1) => ' + a.indexOf(3));

function findall(a, x) {
  /**
   * 陣列a找出值x的所有出現處，並回傳含有匹配索引的陣列
   */
  let results = [],
    len = a.length,
    pos = 0;
  while(pos < len) {
    pos = a.indexOf(x, pos);
    if (pos === -1) break;
    results.push(pos);
    pos = pos + 1;
  }
  return results;
}

// includes()
// 回傳陣列是否含有引數的值，是則回傳true，否則回傳false
let b = [1, true, 3 ,NaN];
console.log('b.includes(true) => ' + b.includes(true));
console.log('b.includes(2) => ' + b.includes(2));
console.log('b.includes(NaN) => ' + b.includes(NaN));
console.log('b.indexOf(NaN) => ' + b.indexOf(NaN));

// sort()
// 就地排序一個陣列
let c = ['banana', 'cherry', 'apple'];
console.log('c 排序前 => ' + c);
c.sort();
console.log('c 排序後 => ' + c);

let d = [33,4,1111,222];
console.log('d 排序前 => ' + d);
d.sort();
console.log('d 排序(字母排序)後 => ' + d);
d.sort(function(a, b) {
  /**
   * 比較函式 回傳 < 0, 0, > 0
   */
  return a - b;
})
console.log('d 排序(數值順序)後 => ' + d);
d.sort((a, b) => b - a);
console.log('d 反向排序(數值順序)後 => ' + d);

let e = ['ant', 'Bug', 'cat', 'Dog'];
console.log('e 排序前 => ' + e);
e.sort();
console.log('e 排序(區分大小寫)後 => ' + e);
e.sort(function(s, t) {
  /**
   * 不區分大小寫排序
   */
  let a = s.toLowerCase();
  let b = t.toLowerCase();
  if (a < b) return -1;
  if (a > b) return 1;
})
console.log('e 排序(不區分大小寫)後 => ' + e);

// reverse()
// 反轉陣列元素順序。
let f = [1,2,3];
console.log('f 反轉排序前 => ' + f);
f.reverse();
console.log('f 反轉排序後 => ' + f);
```

## 陣列 方法 - 字串轉換

```JavaScript
// 陣列對字串的轉換
// join()
// 將陣列內所有元素轉成字串,沒有指定則使用逗號
let a = [1, 2, 3];
console.log('a => ' + a);
console.log('a.join() 轉成字串 沒有指定引數 => ' + a.join());;
console.log('a.join(\" \") 轉成字串 => ' + a.join(" "));
console.log('a.join(\"\") 轉成字串 => ' + a.join(""));
let b = new Array(10); // 長度10沒有元素的陣列
console.log('b.join(\"-\") => ' + b.join("-"));

// toString() 類似不帶引數的join()
let c = [1,2,3].toString();
console.log('[1,2,3].toString() => ' + c);
let d = ['a', 'b', 'c'].toString();
console.log("['a', 'b', 'c'].toString() => " + d);
let e = [1, [2, 'c']].toString();
console.log("[1, [2, 'c']].toString() => " + e);
```

## 靜態的陣列函式

```JavaScript
// 靜態的陣列函式
let a = Array.isArray([]);
console.log('Array.isArray([]); => ' + a);
let b = Array.isArray({});
console.log('Array.isArray({}); => ' + b);
```

# 類陣列物件

```JavaScript
function isArrayLike(o) {
  /**
   * 判斷o是否為一個類陣列物件
   * 字串和函式有數值的length特性，但會被typeof測試排除
   * DOM的文字節點(text nodes) 有數值的length特性，可能需要o.nodeType !== 3 排除
   */
  if (o && // o不是null, undefined
    typeof o === 'object' && // o是一個物件
    Number.isFinite(o.length) && // o.length是一個有限數字
    o.length >= 0 && // o.length 是非負的
    Number.isInteger(o.length) && // o.length 是一個整數
    o.length < 4294967295 // o.length < 2^32-1
  ) {
    return true;
  } else {
    return false;
  }
}
```

