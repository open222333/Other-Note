# JavaScript 筆記

## 目錄

- [JavaScript 筆記](#javascript-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [應用範例 參考資料](#應用範例-參考資料)
- [Javascrapt 基本 語彙結構](#javascrapt-基本-語彙結構)
- [Javascrapt 基本 運算子](#javascrapt-基本-運算子)
- [Javascrapt 基本 述句(statements)](#javascrapt-基本-述句statements)
- [Javascrapt 基本 型別 值 變數](#javascrapt-基本-型別-值-變數)
- [Javascrapt 基本 i++ ++i 差別](#javascrapt-基本-i-i-差別)
- [框架](#框架)
- [使用 format 格式化字符串](#使用-format-格式化字符串)

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

[線上 格式化](https://beautifier.io/)

# 應用範例 參考資料

[【JAVASCRIPT】使用彈出視窗內的表單編輯DataTables源資料](https://www.796t.com/post/YjJkdnM=.html)

[使用 JavaScript 建立表格](https://www.delftstack.com/zh-tw/howto/javascript/create-table-javascript/)

[不用前端框架 手把手打造基礎SPA網站](https://ithelp.ithome.com.tw/users/20108836/ironman/3401)

[將checkbox設定為只能選其中一個](https://ithelp.ithome.com.tw/questions/10194587?sc=pt)

[預設讓 checkbox 為勾選](https://hsuchihting.github.io/jQuery/20200612/3811422616/)

[使用 jQuery的 $.Ajax() 技術和介接api吧](https://ithelp.ithome.com.tw/articles/10226692)

# Javascrapt 基本 語彙結構

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

# Javascrapt 基本 運算子

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

# Javascrapt 基本 述句(statements)

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


# Javascrapt 基本 型別 值 變數

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

# Javascrapt 基本 i++ ++i 差別

[javascript ++i 和 i++ 的差別](https://tools.wingzero.tw/article/sn/242)

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

# 框架

```
把框架想像成是「整合html、js和css的格式」
```

# 使用 format 格式化字符串

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