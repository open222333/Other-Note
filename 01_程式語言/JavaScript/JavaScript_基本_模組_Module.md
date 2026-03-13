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
- [ES6中的模組](#es6中的模組)
	- [ES6的匯出](#es6的匯出)
	- [ES6的匯入](#es6的匯入)
	- [重新匯出](#重新匯出)
- [Web上的ES6模組](#web上的es6模組)
	- [使用import()的動態載入](#使用import的動態載入)
	- [import.meta.url](#importmetaurl)

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

# ES6中的模組

## ES6的匯出

```
ES6模組匯出 在宣告前面加上關鍵字 export

若不想讓export關鍵字四散
以普通的方式定義常數、變數、函式、類別，通常在結尾寫單一個export述句
```

```JavaScript
// ES6的匯出
export const PI = Math.PI;
export function degreesToRadians(d) {
  return (d * PI) / 180;
}

export class Circle {
  constructor(r) {
    this.r = r;
  }
  area() {
    return PI * this.r * this.r;
  }
}
```

```JavaScript
// 若不想讓export關鍵字四散
// 以普通的方式定義常數、變數、函式、類別，通常在結尾寫單一個export述句
const PI = Math.PI;
function degreesToRadians(d) {
  return (d * PI) / 180;
}

class Circle {
  constructor(r) {
    this.r = r;
  }
  area() {
    return PI * this.r * this.r;
  }
}

// 此匯出語法 只是要求要有放在區括號內的一個以逗號區隔開來的識別字串列
export { Circle, degreesToRadians, PI };
```

```JavaScript
// 預設匯出
// 只匯出一個值(通常是函式或類別)，通常使用 export default
// 不同於export語法，若看到 export default 後面接{}，會直接匯出物件字面值

// 模組有普通匯出也有一組預設匯出是合法的，但不常見。
// 一個模組只能有一個預設匯出

export default class BitSet {
	// 實作省略
}

// 匯出更名, 此{}非物件字面值。
export {
	layout as calculateLayout,
	render as renderLayout
};
```

## ES6的匯入

```
以 import 關鍵字 匯入其它模組所匯出的值。
以 from 關鍵字 接字串字面值，指出要匯入的模組名稱，所指定的模組的預設匯出值會變成目前模組中所指定的那個識別字的值。

Web瀏覽器中，字串字面值會被解讀成URL
Node中，字串字面值會被解讀為當前模組的一個檔案名稱

模組指定值需以絕對路徑或相對路徑或是完整的URL
```

```JavaScript
import BitSet from './bitset.js';

// 匯入多個值
import { mean, stddev } from "./stats.js";

// 匯入所有東西，會創造一個物件並指定給stats的常數，非預設匯出會成為物件的特性。
import * as stats from "./stats.js";

// 同時用到 export 以及 export default, 同時匯入 模組的預設 以及 具名匯出,(不常見的)。
import Histogram, { mean, stddev } from "./histogram-stats.js";

// 引用完全沒有匯出的模組。單純使用import關鍵字以及模組指定符
import "./analytics.js";

// 兩個模組使用相同名稱
import { render as renderImage } from "./imageutils.js";
import { render as renderUI } from "./ui.js";

// 同時匯入 模組的預設 以及 具名匯出 的另一種方式
import { default as Histogram, mean, stddev } from "./histogram-stats.js";
```

## 重新匯出

```
假設
./stats/mean.js 內定義 mean()
./stats/stddev.js 內定義 stddev()
建立一個 ./stats.js 使其能以一行程式碼將兩者都匯入
```

```JavaScript
import { mean } from "./stats/mean.js";
import { stddev } from "./stats/stddev.js";
export { mean, stddev };

// ES6 重新匯出
export { mean } from "./stats/mean.js";
export { stddev } from "./stats/stddev.js";

// 可使用通配符(wildcard), 匯出所有具名值
export * from "./stats/mean.js";
export * from "./stats/stddev.js";

// 使用as重新命名
export { mean, mean as average } from "./stats/mean.js";
export { stddev } from "./stats/stddev.js";

// 以export default定義
export { default as mean } from "./stats/mean.js";
export { default as stddev } from "./stats/stddev.js";

// 若re-export另一個模組的一個具名符號作為模組的預設匯出
// 可先import再接著一次export default 或 下方述句
export { mean as default } from "./stats.js";
```

# Web上的ES6模組

```
Web瀏覽器需支援 type="module"
```

```html
<!-- 使用 <script type="module"> 定義模組化js進入點 -->
<script type="module">import "./main.js";</script>
```

## 使用import()的動態載入

```JavaScript
// 靜態匯入
import * as stats from "./stats.js";

// 動態匯入
import("./stats.js").then(stats => {
	let average = stats.mean(data);
})

// 動態匯入 async函式
async analyzeData(data) {
	let stats = await import("./stats.js");
	return {
		average: stats.mean(data),
		stddev: stats.stddev(data);
	}
}
```

## import.meta.url

```
能夠參考儲存在與該模組相同目錄(或相對於該目錄的地方)中的影像、資料檔案或其他資源。
URL()建構器 依據一個絕對URL解析一個相對URL。
```

```JavaScript
/**
 * 例子：
 * 假設模組需本地化的字串，而這些本地化檔案(loaclization files)儲存在一個l10n/目錄中，而其所在目錄與模組相同
*/
function loaclStringsURL(locale) {
	return new URL(`l10/${locale}.json`, import.meta.url);
}
```