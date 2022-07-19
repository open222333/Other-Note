# JavaScript 基本 陣列 Array

## 目錄

- [JavaScript 基本 陣列 Array](#javascript-基本-陣列-array)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [陣列的迭代方法](#陣列的迭代方法)

## 參考資料

# 陣列的迭代方法

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