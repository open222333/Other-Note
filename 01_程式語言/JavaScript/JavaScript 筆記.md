# JavaScript 筆記

## 目錄

- [JavaScript 筆記](#javascript-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [應用範例部分](#應用範例部分)
		- [標準化工具](#標準化工具)
		- [函式相關](#函式相關)
		- [基礎相關](#基礎相關)
		- [術語](#術語)
			- [框架](#框架)
- [語彙結構](#語彙結構)
- [運算子](#運算子)
	- [i++ ++i 差別](#i-i-差別)
- [述句(statements)](#述句statements)
- [型別 值 變數](#型別-值-變數)
	- [使用 format 格式化字符串](#使用-format-格式化字符串)
- [迭代器與產生器](#迭代器與產生器)
	- [迭代器(iterator)](#迭代器iterator)
	- [產生器(generator)](#產生器generator)
		- [yield\*和遞迴產生器](#yield和遞迴產生器)
		- [產生器進階功能](#產生器進階功能)
			- [產生器函式的回傳值](#產生器函式的回傳值)
			- [yield運算式的值](#yield運算式的值)
- [非同步 (Asynchronous)](#非同步-asynchronous)
	- [使用	Callbacks 的非同步程式設計](#使用callbacks-的非同步程式設計)
		- [計時器(Timers)](#計時器timers)
		- [事件 (Events)](#事件-events)
		- [網路事件](#網路事件)
		- [Node中的callbacks和事件](#node中的callbacks和事件)
- [全域函式](#全域函式)
	- [計時器](#計時器)
		- [setTimeout()](#settimeout)
		- [clearTimeout()](#cleartimeout)
		- [setInterval()](#setinterval)
		- [clearInterval()](#clearinterval)

## 參考資料

[Web APIs](https://developer.mozilla.org/zh-TW/docs/Web/API)

```
Web APIs - 說明

當你使用 JavaScript 為網站寫程式碼時，有很多很棒的 API 可以使用。

以下清單列出所有能夠用在你開發網路程式或網站時的介面（即是物件的類型）。
```

[JavaScript 基礎 - 基本語法、值的型別、變數、迴圈、函數](https://hackmd.io/@Heidi-Liu/note-js101#%E5%80%BC%E7%9A%84%E5%9E%8B%E6%85%8B)

[函式中的參數 (parameters), 引數 (arguments), 預設值 (default parameters), 和其餘運算子 (rest operator)](https://medium.com/itsems-frontend/javascript-parameters-and-arguments-5844261fe6cd)

[JavaScript系列叢書](https://github.com/getify/You-Dont-Know-JS/tree/1ed-zh-CN)

[Google Hosted Libraries(將常用框架放在google伺服器)](https://developers.google.com/speed/libraries#jquery)

[JavaScript基礎教學](https://www.fooish.com/javascript/
https://pjchender.dev/javascript/js-map/)

[【javascript】3小時初學者教學](https://www.youtube.com/watch?v=yZwlW5INhgk)

[[Guide] 用 JavaScript 來取得表單元素內容的值（取值）](https://pjchender.dev/webapis/guide-webapis-form-value/)

除錯Javascript程式碼

[UI範例](https://www.jeasyui.com/index.php)

引用 JavaScript 檔案

語法：

```js
<script src="JavaScript 檔案位址.js"></script>

<script>
...JavaScript 程式碼...
</script>
```

[線上練習寫 JavaScript](https://jsfiddle.net/)

```
剛開始學習JavaScript的時候，JSFiddle是很方便的線上工具，可以直接在線上編寫、執行和測試 JavaScript。
```

### 應用範例部分

[【JAVASCRIPT】使用彈出視窗內的表單編輯DataTables源資料](https://www.796t.com/post/YjJkdnM=.html)

[使用 JavaScript 建立表格](https://www.delftstack.com/zh-tw/howto/javascript/create-table-javascript/)

[不用前端框架 手把手打造基礎SPA網站](https://ithelp.ithome.com.tw/users/20108836/ironman/3401)

[將checkbox設定為只能選其中一個](https://ithelp.ithome.com.tw/questions/10194587?sc=pt)

[預設讓 checkbox 為勾選](https://hsuchihting.github.io/jQuery/20200612/3811422616/)

[使用 jQuery的 $.Ajax() 技術和介接api吧](https://ithelp.ithome.com.tw/articles/10226692)

[javascript ++i 和 i++ 的差別](https://tools.wingzero.tw/article/sn/242)

### 標準化工具

[ESLint](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint)

[js-beautify - 線上 格式化](https://beautifier.io/)

### 函式相關

[setTimeout()](https://developer.mozilla.org/en-US/docs/Web/API/setTimeout)

[clearTimeout()](https://developer.mozilla.org/en-US/docs/Web/API/clearTimeout)

[setInterval()](https://developer.mozilla.org/en-US/docs/Web/API/setInterval)

[clearInterval()](https://developer.mozilla.org/en-US/docs/Web/API/clearInterval)

### 基礎相關

[認識 JavaScript Iterable 和 Iterator - Symbol 迭代器](https://jiepeng.me/2018/04/19/iterable-and-iterator-in-javascript)

[認識生成器](https://ithelp.ithome.com.tw/articles/10280527)

### 術語

#### 框架

```
把框架想像成是「整合html、js和css的格式」
```

# 語彙結構

```
註解:
    單行
    // 內容
    /* 內容 */

    多行
    /*
     * 內容 (開頭的*並非必要)
     * 內容
     */

分號 ;
	JavaScript 的每一個語句以分號 ; 來做結束。

	JavaScript 的結尾分號 ; 是可以省略的 (optional)為了讓程式碼更清楚也避免拿掉後可能讓程式碼解析錯誤，一般最好加上分號。

變數作用域 (Variable Scope)
	JavaScript 的變數有其作用的範圍，在作用範圍以外的程式碼就無法存取到該變數。
	JavaScript 的 scope 是所謂的 function scope，亦即一個 function 中會建立一個新的 scope。

	依 function scope 我們可以將變數分為兩類作用域：

		局部變數 (Local variables)
		全域變數 (Global variables)

駝峰式命名 (Camel Case)
	JavaScript 習慣的變數命名風格是所謂的駝峰式命名 (camel case)。

	像是 firstName, lastName, carName，第二個字的字母大寫。
```

# 運算子

```
遞增, 遞減
++, --

反轉位元 ,反轉布林值
~, !

刪除
delete

判斷運算元型別
typeof

回傳undefined值
void

指數, 加法(串接字串, 轉換為數字), 減法(負值), 乘法, 除法, 餘數
**, + , -, * , /, %

左位移, 有正負號擴充的右位移, 有零擴充的右位移
<< , >>, >>>

比較
<, <=, >, >=

測試物件的類別
instanceof

測試特性是否存在
in

測試不嚴格的相等性
==

測試嚴格的相等性
===

測試不嚴格的不等性
!=

測試嚴格的不等性
!==

計算逐位元的AND
&

計算逐位元的XOR
^

計算逐位元的OR
|

計算邏輯的AND
&&

計算邏輯的OR
||

選擇第一個有定義的運算元
??

選擇第二或第三個運算元
?:

指定一個變數或特性
=

進行運算並指定
**=, *=, /=, %=, +=, -=, &=, ^=, |=, <<=, >>=, >>>=
```

## i++ ++i 差別

```
在 javascript 裡面使用 ++i 或 i++ 都可以代表原始值加一，不過還是有一點差別

基本上這個對原始的 i 沒有影響，不過如果要賦予值就會有差別了，

來看一下 i++ 的狀況：
    let a=1, b=1;
    b=a++;
    console.log(b) // b=1

如果 ++ 在後面，會先把值賦予後才 +1，所以 b 會是原本 a 的值。

如果是 ++i 的狀況
    let x=3, y=3;
    y=++x;
    console.log("y",y) // y=4

如果++ 在前，就會先把值加上去之後才賦予值到左邊，所以 y 會是 4。

這就是這兩者的差別。
```

# 述句(statements)

```
statement - 述句
expression - 表達式

運算式

複合述句
    {
        //;
        //;
    }

空述句
    ;

條件式(if else)
    if (expression) {
        statement1
    } else if {
        statement2
    } else {
        statement3
    }

條件式(switch)
    switch(expression) {
        case :
            statements
            break;
        case :
            statements
            break;
        default:
            statements
            break;
    }

迴圈(do-while)
    while (expression) {
        statement
    }

    do {
        statement
    } while (expression) {
        statement
    }

迴圈(for of/in)
	for (initialExpression; condition; incrementExpression) {
	  // statements
	}

    for (initiablize; test; increment) {
        statement
    }

    for (variable in object) {
        statement
    }

    for (let i of iterable) {
        statement
    }

跳耀述句
    labeled:
        帶有標籤的述句
        identifier: statement

    break:
        跳出迴圈

    continue:
        跳出並重啟迴圈進行下一個迭代

    return:
        只能出現在函式
        return expression;

    throw:
        發生例外狀況時拋出例外
        throw expression;

    try/catch/finally:
        例外處理
        try {
            // 若無錯誤則執行到底部
            statement
        } catch (e) {
            // 依照e參考被拋出的Error物件
        } finally {
            // 不論try區塊發生什麼都會執行此區塊
        }

其他述句:
    with:
        使物件特性像區域變數。
        效能下降，安全性，因此不建議使用。嚴格模式禁止使用。
        with (object) {
            statement
        }

    debugger:
        類似中斷點(breakpoint)，除錯用。

    use strict:
        ES5引進，類似述句。

宣告:
    const:
        宣告常數

    let:
        宣告變數

    var:
        ES6之前，宣告變數的方法

    function:
        宣告定義函式
        function functionName(parameter1, parameter2=default, ...args) {
			// ...args(其餘參數 - rest parameter) 會是一個陣列
		    // statements

		    // return value;
		}

    class:
        宣告創建一個類別

    import:
        匯入

    export:
        匯出
```

# 型別 值 變數

```
JavaScript 中，可以分為兩大類資料型態：

第一種是基本資料型態 (primitive data types)，基本資料型態包含了：

	布林值 (Boolean): 只包含兩種值 true / false
		JavaScript 只有對下面這些值會判斷為 false 其他都是 true：

			布林值 false
			undefined
			null
			數值 0
			NaN
			空字串 ''

	null: null 是一個特殊值 (keyword)，表示這變數裡面沒有東西
	undefined: undefined 也是一個特殊值 (keyword)，表示值還沒有定義或還未指定
	數值 (Number): 數值類型的值，像是 42 / 3.14159 / 0
	字串 (String): 像是 'hello world' / 汽車
		跳脫字元 \ 可以用來跳脫這些特殊字元：

			特殊符號	表示的符號
			\0	NULL 字元
			'	單引號
			"	雙引號
			\	反斜線
			\n	換行符號
			\r	carriage return 回車鍵
			\t	tab
			\v	vertical tab
			\b	backspace
			\f	form feed
			\uXXXX	unicode codepoint
	Symbol

第二種是複合資料型態 (composite data types)，包含了：

	陣列 (Array): 陣列用來儲存多個資料，陣列中的資料數量，就是這個陣列的長度 (length)
	物件 (Object): 基本上，基本資料型態以外的都是物件型態

型別：
    原始型別(primitive types)：
        數值 字串 布林值 null undefined
    物件型別(object types)：
        非數值 字串 布林值 null undefined

實用物件 陣列(Array):
    帶有編號的值的有序群集

實用物件 Map:
    值的集合

實用物件 Set:
    鍵值(keys)到值(values)的一種映射(mapping)
```

## 使用 format 格式化字符串

```JavaScript
// 方式1：使用ES6
var name = 'letian'
var s = `Hello ${name}`
console.log(s)

// 方法2：在 String 原型中增加 format 函数
String.prototype.format = function() {
    var formatted = this;
    for( var arg in arguments ) {
        formatted = formatted.replace("{" + arg + "}", arguments[arg]);
    }
    return formatted;
};

var s = '你好 {0} {1}'.formar('value1', 123)
console.log(s)
```

# 迭代器與產生器

## 迭代器(iterator)

```
可迭代物件 具有一個特殊迭代器方法的任何物件，會回傳一個迭代器物件。
迭代器 是具有next()方法的任何物件，此方法會回傳一個迭代結果物件。
迭代結果物件 是具有value和done特性的物件。

關閉一個迭代器 return()方法
迭代器物件可以實作一個return()方法
如果迭代在next()回傳done特性被設為true的迭代結果前就停止(例如:使用break述句離開for/of迴圈)
直譯器會檢查該迭代器物件是否有一個return()方法
若存在此方法，直譯器會不帶引數引用return()方法，賦予迭代器關閉檔案、釋放記憶體或進行其他清理工作的機會。
return()必須回傳一個迭代結果物件，此物件特性會被忽略，但回傳非物件值會是一種錯誤。
```

```JavaScript
class Range {
  constructor(from, to) {
    this.from = from;
    this.to = to;
  }

  // 使一個Range表現得像是由數字組成的一個Set
  has(x) {
    return typeof x === "number" && this.from <= x && x <= this.to;
  }

  // 使用集合記法(set notation)回傳該範圍的字串表示值
  toString() {
    return `x | ${this.from} ≤ x ≤ ${this.to}`;
  }

  // 回傳一個迭代氣物件來使一個Range可迭代
  // 注意到此方法的名稱是一個特符號，而非一個字串
  [Symbol.iterator]() {
    /**
     * 每個迭代器實體都必須獨立於其他實體來迭代該範圍
     * 所以需要一個狀態變數追蹤在迭代中的位置
     * 必須從第一個>= from 得整數開始
     */
    let next = Math.ceil(this.from); // 這是回傳的下一個值
    let last = this.to; // 不會回傳大於這個的任何東西
    return {
      // 這是一個迭代器物件
      // next()方法使它成為一個迭代器物件
      // 它必須回傳一個迭代器結果物件
      next() {
        return next <= last // 如果尚未回傳最後一個值
          ? {value: next++} // 就回傳下一個值並遞增他
          : {done: true}; // 否則 指出我們完成了
      },

      // 為了方便，使這個迭代器本身是可迭代的
      [Symbol.iterator]() {
        return this;
      },
    };
  }
}
```

## 產生器(generator)

```
定義產生器函式(generator function)
以關鍵字 function* 定義
執行產生器函式會回傳產生器物件(generator object)
產生器物件是一個迭代器
呼叫他的next()方法會導致該產生器韓式的主體從頭開始執行或從目前的位置
直到抵達一個yield述句

function 之後加了個 * 符號，這表示這會是個產生器函式，只有在產生器函式中，才可以使用 yield。
```

```JavaScript
// 產出一位數質數(基數為10)之集合的一個產生器函式
function* oneDigitprimes() {
  /**
   * 調用此函式不會執行程式碼，
   * 而只是回傳一個產生器物件呼叫那個產生器的next()方法會執行其程式碼，
   * 直到有一個yield述句為next()方法提供回傳值。
   */
  yield 2;
  yield 3;
  yield 5;
  yield 7;
}

// 調用產生器函式
let primes = oneDigitprimes();
// 產生器是會迭代所產出那些值得一種迭代器物件
primes.next().value; // => 2
primes.next().value; // => 3
primes.next().value; // => 5
primes.next().value; // => 7
primes.next().done; // => true

// 產生器有一個Symbol.迭代器方法使他們可以迭代
primes[Symbol.iterator](); // => primes

// 可像對其他可迭代型別那樣來使用迭代器
[...oneDigitprimes()]; // => [ 2, 3, 5, 7 ]
console.log([...oneDigitprimes()]);
let sum = 0;
for (let primes of oneDigitprimes()) sum += primes;
sum; // => 17
console.log(sum);

// 以運算式形式定義產生器
const seq = function* (from, to) {
  for (let i = from; i <= to; i++) yield i;
};
[...seq(3, 5)]; // => [ 3, 4, 5 ]
console.log([...seq(3, 5)]);

// 物件與類別字面值中，可使用簡寫記法在定義方法的時候完全省略function關鍵字
let o = {
  x: 1,
  y: 2,
  z: 3,
  // 定義產生器，在方法前面加*
  *g() {
    for (let key of Object.keys(this)) {
      yield key;
    }
  },
};
[...o.g()]; // => [ 'x', 'y', 'z', 'g' ]
console.log([...o.g()]);


// 迭代器範例所展示的[Symbol.iterator]()方法 可修改成以下
*[Symbol.iterator]() {
for (let x = Math.ceil(this.from); x <= this.to; x++) yield x;
}
```

```JavaScript
// 產生器範例

/**
 * 產生器函式 產出斐波那契數(Fibonacci numbers)
 */
function* fibonacciSequence() {
  let x = 0,
    y = 1;
  for (;;) {
    // 無窮迴圈
    yield y;
    [x, y] = [y, x + y]; // 注意 解構指定
  }
}

/**
 * 回傳 第n個婓波那契數
 */
function fibonacci(n) {
  // fibonacciSequence有無窮迴圈會一直產生值
  for (let f of fibonacciSequence()) {
    if (n-- <= 0) return f;
  }
}

fibonacci(20); // => 10946

// 產出所指定的可迭代物件的前n個元素
function* take(n, iterable) {
  let it = iterable[Symbol.iterator](); // 取得可迭代物件的迭代器
  while (n-- > 0) {
    let next = it.next(); // 從迭代器取得下個項目
    if (next.done) return; // 若沒有值 則回傳
    else yield next.value; // 否則產出該值
  }
}

// 前5個婓波那契數所構成的一個陣列
[...take(5, fibonacciSequence())]; // => [ 1, 1, 2, 3, 5 ]

// 給定可迭代物件所成的一個陣列，以交錯的順序產出他們的元素
function* zip(...iterables) {
  // 為每個可迭代物件取得一個迭代器
  let iterators = iterables.map((i) => i[Symbol.iterator]());
  let index = 0;
  while (iterators.length > 0) {
    // 還有其他迭代器時
    if (index >= iterators.length) {
      // 最後一個迭代器
      index = 0; // 回到第一個
    }
    // 從下一個迭代器取得下一個項目
    let item = iterators[index].next();
    if (item.done) {
      // 迭代器完成
      iterators.splice(index, 1); // 從陣列移除
    } else {
      yield item.value;
      index++;
    }
  }
}

// 產出一位數質數(基數為10)之集合的一個產生器函式
function* oneDigitprimes() {
  /**
   * 調用此函式不會執行程式碼，
   * 而只是回傳一個產生器物件呼叫那個產生器的next()方法會執行其程式碼，
   * 直到有一個yield述句為next()方法提供回傳值。
   */
  yield 2;
  yield 3;
  yield 5;
  yield 7;
}

// 交錯產出三個可迭代物件
[...zip(oneDigitprimes(), "ab", [0])]; // => [2,'a',0,3,'b',5,7]
```

### yield*和遞迴產生器

```JavaScript
// yield*和遞迴產生器

function* oneDigitprimes() {
  yield 2;
  yield 3;
  yield 5;
  yield 7;
}

// 循序非交錯產出多個可迭代物件的元素
function* sequence(...iterables) {
  for (let iterable of iterables) {
    for (let item of iterable) {
      yield item;
    }
  }
}

[...sequence("abc", oneDigitprimes())]; // => ['a', 'b', 'c', 2, 3, 5, 7]
console.log([...sequence("abc", oneDigitprimes())]);

// 使用yield*簡化
function* sequence2(...iterables) {
  for (let iterable of iterables) {
    yield* iterable;
  }
}

[...sequence2("abc", oneDigitprimes())]; // => ['a', 'b', 'c', 2, 3, 5, 7]
console.log([...sequence2("abc", oneDigitprimes())]);
```

### 產生器進階功能

#### 產生器函式的回傳值

```JavaScript
// 一個產生器函式的回傳值
function* oneAndDone() {
  yield 1;
  return "done"; // 回傳值不會出現在一般迭代中
}

[...oneAndDone()]; // => [1]
console.log([...oneAndDone()]);

// 但如果明確呼叫next()可以取用回傳值
let generator = oneAndDone();
generator.next(); // => { value: 1, done: false }
generator.next(); // => { value: 'done', done: true }
generator.next(); // => { value: undefined, done: true }
```

#### yield運算式的值

```
yield 是一個運算式 可以有一個值
```

```JavaScript
// 一個yield運算式的值
// next()的第一次調用啟動產生器，但傳入那次調用的值並無法被產生器取用
function* smallNumbers() {
  console.log("next() invoked the first time; argument discarded");
  let y1 = yield 1; // y1 == "b"
  console.log("next() invoked a second time with argument", y1);
  let y2 = yield 2; // y2 == "c"
  console.log("next() invoked a third time with argument", y2);
  let y3 = yield 3; // y3 == "d"
  console.log("next() invoked a fourth time with argument", y3);
  return 4;
}

let g = smallNumbers();
console.log("generator created; no code runs yet");
let n1 = g.next("a"); // n1.value == 1
console.log("generator yielded", n1.value);
let n2 = g.next("b"); // n2.value == 2
console.log("generator yielded", n2.value);
let n3 = g.next("c"); // n3.value == 3
console.log("generator yielded", n3.value);
let n4 = g.next("d"); // n4 == { value: 4, done: true }
console.log("generator returned", n4.value);
```

# 非同步 (Asynchronous)

```
停止運算，等候資料抵達或某些事件發生。
```

## 使用	Callbacks 的非同步程式設計

### 計時器(Timers)

```JavaScript
// 計時器(Timers)

/**
 * setTimeout() 函式
 * https://developer.mozilla.org/zh-CN/docs/Web/API/setTimeout
 */

/**
 * setInterval() 函式
 * https://developer.mozilla.org/zh-CN/docs/Web/API/setInterval
 */
function checkForUpdates() {
  // 虛構的檢查更新函式
  console.log("test");
}

// setTimeout() 會呼叫指定的callback函式部傳入任何引數
// let updateIntervalId = setTimeout(checkForUpdates(), 60000);

// 若是要檢查更新 會希望重複執行 使用setInterval
// 一秒後呼叫checkForUpdates 在那之後每6秒重複呼叫一次
let updateIntervalId = setInterval(checkForUpdates, 1000);

// setInterval()所回傳的值(updateIntervalId)可用來停止重複調用
function stopCheckingForUpdates() {
  clearInterval(updateIntervalId);
}

setTimeout(stopCheckingForUpdates, 5000);
```

### 事件 (Events)

```JavaScript
// 事件 (Events)
/**
 * 客戶端的JavaScript 程式幾乎都是 事件驅動
 * 以addEventListener()來註冊的callback函式
 * 這些callback函式被稱為 事件處理器(event handlers) 或 事件收聽器(event listeners)
 */

// 請求Web瀏覽器回傳一個物件來表示
// 匹配這個CSS選擇器(selector)的HTML <button> 元素
let okay = document.querySelector("#confirmUpdateDialog button.okay");

function applyUpdate() {
  // 虛構的callback函式
  console.log("test");
}

// 註冊一個callback函式 使用者點擊按鈕時調用
okay.addEventListener("click", applyUpdate);
```

### 網路事件

```JavaScript
// 網路事件
/**
 * 網路請求(network requests)
 * XMLHttpRequest類別 在目前(2023)已大多被fetch() API 取代
 */

function getCurrentVersionNumber(versionCallback) {
  //對後端的版本API發出一個以指令搞控制的HTTP請求
  let request = new XMLHttpRequest();
  request.open("GET", "http://www.example.com/api/version");
  request.send();

  // 註冊一個會再回應(response)抵達時被調用的callback
  request.onload = function () {
    if (request.status === 200) {
      // 若HTTP狀態是良好的 取得版本號碼並呼叫callback
      let currentVersion = parseFloat(request.responseText);
      versionCallback(null, currentVersion);
    } else {
      // 否則回報一個錯誤給callback
      versionCallback(response.statusText, null);
    }
  };

  // 註冊另一個callback 在網路發生錯誤時被調用
  request.onerror = request.ontimeout = function (e) {
    versionCallback(e.type, null);
  };
}
```

### Node中的callbacks和事件

```JavaScript
// Node 中的 Callbacks 和事件
/**
 * 範例
 * 讀取一個檔案之內容的預設API
 * 並會在檔案的內容讀取玩時調用一個callback函式
 */

// fs模組具有檔案系統相關的API
const fs = require("fs");
let options = {
  // 存放程式選項的一個物件
};

function startProgram(option) {
  // 模擬執行程式
  console.log(option);
}

// 讀取組態檔(configuration file)後 呼叫callback程式
/**
 * fs.readFile()函式最後的引數為 有兩個參數的callback
 * 若成功會使用callback第二個引數
 * 若有錯誤則傳入callback第一個引數
 * 以下以 => 箭號函式 表達 callback
 */
fs.readFile("config.json", "utf-8", (err, text) => {
  if (err) {
    // 若有錯誤，顯示警告訊息，但繼續執行
    console.warn("Could not read config file:", err);
  } else {
    // 否則，剖析檔案內容，並指定給選項物件
    Object.assign(options, JSON.parse(text));
  }

  // 不管什麼情形都開始執行程式
  startProgram(options);
});

/**
 * 範例
 * 對一個URL的內容發出一個HTTP請求
 * 有兩層非同步程式碼，並以事件收聽器來進行處理
 */
const https = require("https");

// 讀取指定URL的文字內容，並且非同步的傳給callback
function getText(url, callback) {
  // 發出 HTTP GET 請求
  request = https.get(url);

  // 註冊一個函式來處理"response"事件
  request.on("response", (response) => {
    // 這個response事件代表已經接收到回應標頭(response headers)
    let httpStatus = response.statusCode;

    // 尚未接收到HTTP回應的主體(body)
    // 註冊更多處理器在抵達時呼叫，預期為Unicode文字
    response.setEncoding("utf-8");
    let body = "";

    // 此事件處理器會在主體有一部分(chunk)已經就緒時被呼叫
    response.on("data", (chunk) => {
      body += chunk;
    });

    // 此事件處理器會在回應完成時呼叫
    response.on("end", () => {
      // 根據http status傳入callback
      if (httpStatus === 200) {
        callback(null, body);
      } else {
        callback(httpStatus, null);
      }
    });
  });

  // 註冊一個事件處理器應付低階的網路錯誤
  request.on("error", (err) => {
    callback(err, null);
  });
}
```

# 全域函式

## 計時器

```
setTimeout() 和 setInterval() 共用一個編號池，技術上，clearTimeout() 和 clearInterval() 可以互換。
但是，為了避免混淆，不要混用取消定時函數。
```

### setTimeout()

```JavaScript
// 設置一個計時器，一旦計時器到期，該計時器就會執行一個函數或指定的代碼段。
/**
 * setTimeout(code)
 * setTimeout(code, delay)
 * setTimeout(functionRef)
 * setTimeout(functionRef, delay)
 * setTimeout(functionRef, delay, param1)
 * setTimeout(functionRef, delay, param1, param2)
 * setTimeout(functionRef, delay, param1, param2, ... paramN)
 *
 * function 是你想要在到期時間 (delay毫秒) 之後執行的函數。
 * code 這是一個可選語法，你可以使用字符串而不是function ，在delay毫秒之後編譯和執行字符串 (使用該語法是不推薦的， 原因和使用 eval()一樣，有安全風險)。
 * delay 可選 延遲的毫秒數 (一秒等於 1000 毫秒)，函數的調用會在該延遲之後發生。如果省略該參數，delay 取默認值 0，意味著“馬上”執行，或者盡快執行。不管是哪種情況，實際的延遲時間可能會比期待的 (delay 毫秒數) 值長，原因請查看實際延時比設定值更久的原因：最小延遲時間。
 * arg1, ..., argN 可選 附加參數，一旦定時器到期，它們會作為參數傳遞給function
 *
 * 返回值timeoutID是一個正整數，表示定時器的編號。這個值可以傳遞給clearTimeout()來取消該定時器。
 */
// 計時器
/**
 * 示範如何使用setTimeout() setInterval() clearInterval() 搭配Console API 顯示簡單的數位時鐘
 */

setTimeout(() => {
  console.log("Ready...");
}, 1000);
setTimeout(() => {
  console.log("set...");
}, 2000);
setTimeout(() => {
  console.log("go!");
}, 3000);

// 一秒一次: 清空主控台，並印出目前時間
let clock = setInterval(() => {
  console.clear();
  console.log(new Date().toLocaleTimeString());
}, 1000);

// 十秒之後，停止上面重複的程式碼
setTimeout(() => {
  clearInterval(clock);
}, 10000);
```

### clearTimeout()

```JavaScript
/**
 * clearTimeout(timeoutID)
 * timeoutID 要取消的超時的標識符。此 ID 由對 setTimeout() 的相應調用返回。
 */
const alarm = {
  remind(aMessage) {
    alert(aMessage);
    this.timeoutID = undefined;
  },

  setup() {
    if (typeof this.timeoutID === "number") {
      this.cancel();
    }

    this.timeoutID = setTimeout(
      (msg) => {
        this.remind(msg);
      },
      1000,
      "Wake up!"
    );
  },

  cancel() {
    clearTimeout(this.timeoutID);
  },
};
window.addEventListener("click", () => alarm.setup());
```

### setInterval()

```JavaScript
/**
 * setInterval(code)
 * setInterval(code, delay)
 * setInterval(func)
 * setInterval(func, delay)
 * setInterval(func, delay, arg0)
 * setInterval(func, delay, arg0, arg1)
 * setInterval(func, delay, arg0, arg1, ... argN)
 *
 * func 要重複調用的函數，每經過指定 delay 毫秒後執行一次。第一次調用發生在 delay 毫秒之後。
 * code 這個語法是可選的，你可以傳遞一個字符串來代替一個函數對象，你傳遞的字符串會被編譯然後每經過 delay 毫秒執行一次。這個語法因為與 eval() 存在相同的安全風險所以不推薦使用。
 * delay 是每次延遲的毫秒數（一秒等於 1000 毫秒），函數的每次調用會在該延遲之後發生。如果未指定，則其默認值為 0。參見下方的延遲限制以了解詳細的 delay 的取值範圍
 * arg1, ..., argN 可選 當計時結束的時候，將被傳遞給 func 函數的附加參數
 *
 * 返回值
 * intervalID 是一個非零數值，用來標識通過 setInterval() 創建的定時器，這個值可以用來作為 clearInterval() 的參數來清除對應的定時器。
 */
var intervalID = setInterval(myCallback, 500, 'Parameter 1', 'Parameter 2');

function myCallback(a, b)
{
 // Your code here
 // Parameters are purely optional.
 console.log(a);
 console.log(b);
}
```

### clearInterval()

```JavaScript
/**
 * clearInterval(intervalID)
 * intervalID 要取消的重複操作的標識符。此 ID 由對 的相應調用返回setInterval()。
 */
const intervalID = setInterval(myCallback, 500, "Parameter 1", "Parameter 2");

function myCallback(a, b) {
  // Your code here
  // Parameters are purely optional.
  console.log(a);
  console.log(b);
}

```