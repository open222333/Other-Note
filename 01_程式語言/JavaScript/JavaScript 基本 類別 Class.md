# JavaScript 基本 類別 Class

```
一個類別的成員(members)，或者說實體(instances)，會有自己的特性(properties)存放或定義它們的狀態，定義其行為的方法(methods)
```

## 目錄

- [JavaScript 基本 類別 Class](#javascript-基本-類別-class)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [類別與原型](#類別與原型)
- [類別與建構器](#類別與建構器)

## 參考資料

[]()

# 類別與原型

`工廠函式`

```JavaScript
// 簡單的JavaScript類別

function range(from, to) {
  // 工廠函式 回傳一個新的範圍(range)物件
  let r = Object.create(range.methods); // 使用Object.create() 創建一個物件繼承 定義共有的方法

  // 儲存新的範圍物件
  r.from = from;
  r.to = to;

  return r;
}

// 這個原型物件定義所有的範圍物件都會繼承的方法
range.methods = {
  // 若x在範圍中 則回傳true 是用數值範圍 文字 Date
  includes(x) { return this.from <= x && x <= this.to; },

  // 使這個類別的實體可迭代的一個產生器函式 只能用於數值範圍
  *[Symbol.iterator]() {
    for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
  },

  // 回傳該範圍的一個字串表示值
  toString() { return "(" + this.from + "..." + this.to + ")"; }
};

// range物件使用範例
let r = range(1, 3); // 創建range物件
r.includes(2) // true : 2在範圍中
r.toString() // => "(1...3)"
console.log([...r]); // 藉由迭代器轉成陣列
```

# 類別與建構器

`建構式函式 不創建以及回傳該物件 只有初始化this`

`由工廠函式修改成建構式函式`

```JavaScript
// 類別與建構器

/**
 * 以下為舊式定義類別方法 JavaScript已有class並擁有良好的支援
*/

// 使用建構器的一個Range類別
function Range(from, to) {
  // Range物件的一個建構式函式
  // 不創建以及回傳該物件 只有初始化this
  this.from = from;
  this.to = to;
}

// 所有的Range物件都繼承此物件
// 此特性需命名為prototype才能運作
Range.prototype = {
  // 若x在此範圍中 回傳true 適用數值 文字 Date
  includes: function (x) { return this.from <= x && x <= this.to; },
  // 使類別的實體可迭式的一個產生器函式 適用 數值
  [Symbol.iterator]: function* () {
    for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
  },
  // 回傳該範圍的一個字串表示值
  toString: function () { return "(" + this.from + "..." + this.to + ")"; }
};
```