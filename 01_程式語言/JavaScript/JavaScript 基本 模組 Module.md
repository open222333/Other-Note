# JavaScript 基本 模組 Module

```
```

## 目錄

- [JavaScript 基本 模組 Module](#javascript-基本-模組-module)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [使用類別、物件和Closures的模組](#使用類別物件和closures的模組)
- [Node中的模組](#node中的模組)
  - [Node的匯出](#node的匯出)
  - [Node的匯入](#node的匯入)
  - [Web上的Node式模組](#web上的node式模組)

## 參考資料

[]()

# 使用類別、物件和Closures的模組

```JavaScript
// 定義一個統計模組
const stats = (function () {
  const sum = (x, y) => x + y;
  const square = (x) => x * x;

  function mean(data) {
    return data.reduce(sum) / data.length;
  }

  function stddev(data) {
    let m = mean(data);
    return Math.sqrt(
      data
        .map((x) => x - m)
        .map(square)
        .reduce(sum) /
        (data.length - 1)
    );
  }

  return { mean, stddev };
})();

// 統計模組使用方式
console.log(stats.mean([1, 3, 5, 7, 9]));
console.log(stats.stddev([1, 3, 5, 7, 9]));
```

# Node中的模組

## Node的匯出

```
Node，定義了一個全域的exports物件，它永遠有定義。
```

```JavaScript
// 若你撰寫會匯出多個值得一個Node模組，可單純把他們指定給這個物件的特性。
const sum = (x, y) => x + y;
const square = (x) => x * x;

exports.mean = (data) => data.reduce(sum) / data.length;
exports.stddev = function (d) {
  let m = exports.mean(d);
  return Math.sqrt(
    d
      .map((x) => x - m)
      .map(square)
      .reduce(sum) / (d, length - 1)
  );
};
```

```JavaScript
// 若你定義的模組通常匯出單一函式或類別，而非充滿函式或類別的一個物件。
module.exports = class BitSet extends AbstractWritableSet {
  // 實作省略
};
```

```JavaScript
// 在模組結尾匯出單一物件，而非一路上逐個匯出函式
const sum = (x, y) => x + y;
const square = (x) => x * x;
const mean = data => data.reduce(sum)/data.length;
const stddev =d => {
	let m = mean(d);
	return Math.sqrt(d.map(x => x - m).map(square).reduce(sum)/(d.length - 1));
};

module.exports = { mean, stddev };
```

## Node的匯入

```
Node模組會藉由呼叫 require() 函式來匯入(imports)另一個模組。
此函式的引數是要被匯入的模組之名稱，而回傳值是該模組所匯出的任何值(函式、類別、物件)。
```

```JavaScript
/**
 * 若要匯入Node內建的模組或透過套件管理器(package manager)安裝的模組，
 * 使用該模組未經資格修飾的名稱(unqualified name)，
 * 不使用會把它變成一個檔案系統路徑的任何 / 字元。
 */
// 這些模組內建於Node
const fs = require("fs"); // 內建的檔案系統模組
const http = require("http"); // 內建的HTTP模組

// express http 伺服器框架是一個第三方模組，需安裝
const express = require("express");
```

```JavaScript
/**
 * 當要匯入自己程式碼的模組，需使用含有程式碼的檔案路徑
 * 可省略.js副檔名
 */
const stats = require('./stats.js');
const BitSet = require('./utils/bitset.js')
```

```JavaScript
/**
 * 當模組只會出單一個函式或類別，所要做的就是 require 它
 * 當一個模組會出具有多個特性的一個物件，可匯入整個物件或只匯入特定的特性
 */
// 匯入整個stats物件
const stats = require('./stats.js');
let average = stats.mean(data);

// 使用解構指定直接把需要的函式匯入至區域命名空間
const { stddev } = require('./stats.js');
let sd = stddev(data);
```

## Web上的Node式模組

```
具有 Exports物件、require()函式 的模組是內建在Node之中。
若以捆裝工具(例如:webpack)處理程式碼，可把此模組用於Web瀏覽器中執行的程式碼。

但現在JavaScript有標準模組語法，使用
```