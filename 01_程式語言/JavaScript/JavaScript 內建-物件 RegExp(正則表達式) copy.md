# JavaScript 內建-物件 RegExp(正則表達式)

```
正規表達式是被用來匹配字串中字元組合的模式。
在 JavaScript 中，正規表達式也是物件，這些模式在 RegExp 的 exec 和 test 方法中，以及 String 的 match、replace、search、split 等方法中被運用。
```

## 目錄

- [JavaScript 內建-物件 RegExp(正則表達式)](#javascript-內建-物件-regexp正則表達式)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [驗證除錯工具相關](#驗證除錯工具相關)
- [用法](#用法)
	- [定義正規表達式](#定義正規表達式)
	- [範例](#範例)
	- [常用函式](#常用函式)
- [旗標(flags)](#旗標flags)
- [特性](#特性)
- [字元](#字元)
	- [普通字元](#普通字元)
	- [非列印字元](#非列印字元)
	- [特殊字元](#特殊字元)
	- [重複字元](#重複字元)
	- [定錨字元](#定錨字元)

## 參考資料

[RegExp MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/RegExp)

[RegExp](https://www.codecademy.com/resources/docs/javascript/regexp)

[正規表達式](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Guide/Regular_Expressions)

[[JS] 正則表達式（Regular Expression, regex）](https://pjchender.dev/javascript/js-regex/)

[Regular expressions](https://javascript.info/regular-expressions)

[Regular expressions](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions)

[正規表示式 Regular Expression](https://atedev.wordpress.com/2007/11/23/%E6%AD%A3%E8%A6%8F%E8%A1%A8%E7%A4%BA%E5%BC%8F-regular-expression/)

[JavaScript RegExp Reference](https://www.w3schools.com/jsref/jsref_obj_regexp.asp)

### 驗證除錯工具相關

[語法測試工具網站 - 推薦](https://regex101.com/)

[語法測試工具網站](https://www.debuggex.com/)

[語法測試工具網站](https://regexr.com/)

# 用法

## 定義正規表達式

```JavaScript
// 特殊字面值語法(literal syntax)
let pattern = /s$/;
```

```JavaScript
let pattern = new RegExp("s$");
```

## 範例

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

// 簡易網址匹配 (有更簡易的URL剖析器 parser)
/**
 * fullurl = match[0]; // fullurl == "http://www.example.com/-d"
 * protocol = match[1]; // protocol == "http"
 * host = match[2]; // host == www.example.com
 * path = match[3]; // path == -d
 * /
/(\w+):\/\/([\w.]+)\/(\S*)/
```

## 常用函式

```JavaScript
// 使用正規表達式字面值（regular expression literal），包含兩個 / 字元之間的模式
var re = /ab+c/;

// 呼叫 RegExp 物件的建構函式
var re = new RegExp('ab+c');

// 搜尋字串中是否有符合的部分，回傳 true/false。
RegExp.prototype.test()

// 以陣列回傳字串中匹配到的部分，否則回傳 null。 會修改lastIndex特性
RegExp.prototype.exec()

// 返回將字符串與正則表達式匹配的所有結果的迭代器，包括捕獲組。
String.prototype.matchAll()
const regexp = /t(e)(st(\d?))/g;
const str = 'test1test2';

const array = [...str.matchAll(regexp)];

console.log(array[0]);
// Expected output: Array ["test1", "e", "st1", "1"]

console.log(array[1]);
// Expected output: Array ["test2", "e", "st2", "2"]


// 以陣列回傳字串中匹配到的部分，否則回傳 null。
String.prototype.match()
let url = /(\w+):\/\/([\w.]+)\/(\S*)/;
let text = "Visit my blog at http://www.example.com/-d";
let match = text.match(url);
if (match !== null) {
	fullurl = match[0]; // fullurl == "http://www.example.com/-d"
	protocol = match[1]; // protocol == "http"
	host = match[2]; // host == www.example.com
	path = match[3]; // path == -d
}

let url = /(?<protocol>\w+):\/\/(?<host>[\w.]+)\/(?<path>\S*)/;
let text = "Visit my blog at http://www.example.com/-d";
let match = text.match(url);
match[0] // => "http://www.example.com/-d"
match.input // => text
match.index // => 17
match.groups.protocol // => "http"
match.groups.host // => "www.example.com"
match.groups.path // => "-d"


// 尋找字串中匹配的部分，並取代之。
String.prototype.replace()
text.replace(/javascript/gi, "JavaScript")

// 尋找字串中是否有符合的部分，有的話回傳 index，否則回傳 -1。
String.prototype.search()
"JavaScript".search(/script/ui) // => 4
"Python".search(/script/ui) // => -1

// 在字串根據匹配到的項目拆成陣列。
String.prototype.split()
"1, 2, 3,\n4, 5".split(/\s*,\s*/) // => [ '1', '2', '3', '4', '5' ]
```

# 旗標(flags)

```
i:忽略大小寫。

g:全局匹配。表示會可比對多個成功的結果，預設沒有 g 時，就是比對到一個就停止

m:允許對多行文字匹配。意思是變成每一行文字都有字首與字尾，變成可能每一行文字都是一組字首字尾，原本沒有定義 m 時，則為所有文字的開頭為字首，與最後一個字為字尾，這樣只有一組字首字尾。`\r\n 意思是換行

s:允許.匹配換行符。

y:ES6 特性，黏性匹配（sticky match），以 RegExp 實例的 lastIndex 值作為索引，從字串該索引後進行匹配。

u:ES6 特性，將 \u{...} 視為 Unicode 碼點來匹配。
```

# 特性

```
global：使用 g 時為 true。
ignoreCase：使用 i 時為 true。
lastIndex：開始下一輪 exec 比對時為 true。
muliline：使用 m 時為 true。
source：正規式來源字元。
```

# 字元

## 普通字元

普通字元包括沒有顯式指定為元字元的所有可列印和不可列印字元。

這包括所有大寫和小寫字母、所有數字、所有標點符號和一些其他符號。

```
[ABC]	匹配 [...] 中的所有字元，例如 [aeiou] 匹配字串 "google itread01 taobao" 中所有的 e o u a 字母。
[^ABC]	匹配除了 [...] 中字元的所有字元，例如 [^aeiou] 匹配字串 "google itread01 taobao" 中除了 e o u a 字母的所有字母。
[A-Z]	表示一個區間，匹配所有大寫字母，[a-z] 表示所有小寫字母。
.		匹配除換行符（\n、\r）之外的任何單個字元，相等於 [^\n\r]。
[\s\S]	匹配所有。\s 是匹配所有空白符，包括換行，\S 非空白符，不包括換行。
\w		匹配字母、數字、下劃線。等價於 [A-Za-z0-9_]
```

## 非列印字元

非列印字元也可以是正則表示式的組成部分

```
\cx	匹配由x指明的控制字元。例如， \cM 匹配一個 Control-M 或回車符。x 的值必須為 A-Z 或 a-z 之一。否則，將 c 視為一個原義的 'c' 字元。
\f	匹配一個換頁符。等價於 \x0c 和 \cL。
\n	匹配一個換行符。等價於 \x0a 和 \cJ。
\r	匹配一個回車符。等價於 \x0d 和 \cM。
\s	匹配任何空白字元，包括空格、製表符、換頁符等等。等價於 [ \f\n\r\t\v]。注意 Unicode 正則表示式會匹配全形空格符。
\S	匹配任何非空白字元。等價於 [^ \f\n\r\t\v]。
\t	匹配一個製表符。等價於 \x09 和 \cI。
\v	匹配一個垂直製表符。等價於 \x0b 和 \cK。
```

## 特殊字元

```
$	匹配輸入字串的結尾位置。如果設定了 RegExp 物件的 Multiline 屬性，則 $ 也匹配 '\n' 或 '\r'。要匹配 $ 字元本身，請使用 \$。
( )	標記一個子表示式的開始和結束位置。子表示式可以獲取供以後使用。要匹配這些字元，請使用 \( 和 \)。
*	匹配前面的子表示式零次或多次。要匹配 * 字元，請使用 \*。
+	匹配前面的子表示式一次或多次。要匹配 + 字元，請使用 \+。
.	匹配除換行符 \n 之外的任何單字元。要匹配 . ，請使用 \. 。
[	標記一箇中括號表示式的開始。要匹配 [，請使用 \[。
?	匹配前面的子表示式零次或一次，或指明一個非貪婪限定符。要匹配 ? 字元，請使用 \?。
\	將下一個字元標記為或特殊字元、或原義字元、或向後引用、或八進位制轉義符。例如， 'n' 匹配字元 'n'。'\n' 匹配換行符。序列 '\\' 匹配 "\"，而 '\(' 則匹配 "("。
^	匹配輸入字串的開始位置，除非在方括號表示式中使用，當該符號在方括號表示式中使用時，表示不接受該方括號表示式中的字元集合。要匹配 ^ 字元本身，請使用 \^。
{	標記限定符表示式的開始。要匹配 {，請使用 \{。
|	指明兩項之間的一個選擇。要匹配 |，請使用 \|。
```

## 重複字元

```
*	匹配前面的子表示式零次或多次。例如，zo* 能匹配 "z" 以及 "zoo"。* 等價於{0,}。
+	匹配前面的子表示式一次或多次。例如，'zo+' 能匹配 "zo" 以及 "zoo"，但不能匹配 "z"。+ 等價於 {1,}。
?	匹配前面的子表示式零次或一次。例如，"do(es)?" 可以匹配 "do" 、 "does" 中的 "does" 、 "doxy" 中的 "do" 。? 等價於 {0,1}。
{n}	n 是一個非負整數。匹配確定的 n 次。例如，'o{2}' 不能匹配 "Bob" 中的 'o'，但是能匹配 "food" 中的兩個 o。
{n,}	n 是一個非負整數。至少匹配n 次。例如，'o{2,}' 不能匹配 "Bob" 中的 'o'，但能匹配 "foooood" 中的所有 o。'o{1,}' 等價於 'o+'。'o{0,}' 則等價於 'o*'。
{n,m}	m 和 n 均為非負整數，其中n <= m。最少匹配 n 次且最多匹配 m 次。例如，"o{1,3}" 將匹配 "fooooood" 中的前三個 o。'o{0,1}' 等價於 'o?'。請注意在逗號和兩個數之間不能有空格。
```

## 定錨字元

```
^	匹配輸入字串開始的位置。如果設定了 RegExp 物件的 Multiline 屬性，^ 還會與 \n 或 \r 之後的位置匹配。
$	匹配輸入字串結尾的位置。如果設定了 RegExp 物件的 Multiline 屬性，$ 還會與 \n 或 \r 之前的位置匹配。
\b	匹配一個單詞邊界，即字與空格間的位置。
\B	非單詞邊界匹配。
```
