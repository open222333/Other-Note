# JavaScript 物件 RegExp(正則表達式)

```
正規表達式是被用來匹配字串中字元組合的模式。
在 JavaScript 中，正規表達式也是物件，這些模式在 RegExp 的 exec 和 test 方法中，以及 String 的 match、replace、search、split 等方法中被運用。
```

## 參考資料

[正規表達式](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Guide/Regular_Expressions)

[[JS] 正則表達式（Regular Expression, regex）](https://pjchender.dev/javascript/js-regex/)

[Regular expressions](https://javascript.info/regular-expressions)

[Regular expressions](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions)

[正規表示式 Regular Expression](https://atedev.wordpress.com/2007/11/23/%E6%AD%A3%E8%A6%8F%E8%A1%A8%E7%A4%BA%E5%BC%8F-regular-expression/)

# 用法

```JavaScript
'str'.match(/[0-9]+/); 			// 1 次以上的數字，等同於 "\d"
'str'.match(/[A-Za-z]+/); 		// 1 次以上的英文字
'str'.match(/[A-Za-z0-9_]+/);	// 1 次以上的英數字含底線，等同於 "\w"
'str'.match(/.+/); 				// 1 次以上的任意字元

// * 表示前一個字元可以是 0 個或多個，例如 /ab*c/，因此 ac, abc, abbbbc 都符合規則。
// + 表示前一個字元可以是 1 個或多個，例如 /a+b/ ，ab, aaaaab 都符合規則。
// ? 表示前一個字元可以是 0 個或 1 個
// ^ 匹配輸入的開頭，例如 /^a/ ， a dog 會符合，但 cats 中的 a 不會。
// $ 匹配輸入的結尾，例如 /t$/，eat 會符合，但 eaten 中的 t 不會。
// . 用來表示任意字元
```

```JavaScript
// 使用正規表達式字面值（regular expression literal），包含兩個 / 字元之間的模式
var re = /ab+c/;

// 呼叫 RegExp 物件的建構函式
var re = new RegExp('ab+c');

// 搜尋字串中是否有符合的部分，回傳 true/false。
RegExp.prototype.test()

// 以陣列回傳字串中匹配到的部分，否則回傳 null。
RegExp.prototype.exec()

// 以陣列回傳字串中匹配到的部分，否則回傳 null。
String.prototype.match()

// 尋找字串中匹配的部分，並取代之。
String.prototype.replace()

// 尋找字串中是否有符合的部分，有的話回傳 index，否則回傳 -1。
String.prototype.search()

// 在字串根據匹配到的項目拆成陣列。
String.prototype.split()
```
