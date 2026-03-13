# JavaScript 內建-物件 Console(終端機輸出)

```
Console 對象提供對瀏覽器調試控制台的訪問（例如 Firefox 中的 Web 控制台）。
它的具體工作方式因瀏覽器而異，但通常會提供一組事實上的功能。
```

## 目錄

- [JavaScript 內建-物件 Console(終端機輸出)](#javascript-內建-物件-console終端機輸出)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [函式相關](#函式相關)
- [用法](#用法)
	- [計數 console.count()](#計數-consolecount)
	- [分組 console.group()](#分組-consolegroup)
	- [計時器 console.time()](#計時器-consoletime)
	- [表格 console.table()](#表格-consoletable)

## 參考資料

[Console MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/API/console)

[Console - 標準化](https://console.spec.whatwg.org/)

### 函式相關

[Console.table()](https://developer.mozilla.org/zh-CN/docs/Web/API/console/table)

# 用法

```JavaScript
// 常用函式

/**
 * 將引數轉為字串並輸出至主控台
 */
console.log()

// %s: 字串
// %i or %d: 整數
// %f: 浮點數
// %o: for DOM element
// %O: for JS object
// %c : web中會被解讀成css樣式。Node會忽略
let a = "apple";
console.log(
  "%c a = %s",
  "color:red; font-size: 35px; background-color: yellow",
  a
);

// 與console.log()相同，輸出訊息可能會加上圖示顯示(Level)等級，可能可以過濾主控台訊息
console.debug()
console.info()
console.warn()
// 將引數轉為字串並輸出至主控台 輸出至stderr資料流而非stdout
console.error()

/**
 * 若第一個引數為truthy(該斷言通過了)，此函式什麼都不做
 * 若第一個引數為false或falsy值，被印出的方式向使用console.error()並帶有Assertion failed 前綴
 */
console.assert()

/**
 * 清空主控台
 */
console.clear()

/**
 * 會紀錄(log)引數，輸出架上堆疊軌跡(stack trace)。Node中會跑到stderr。
 */
console.trace()
```

## 計數 console.count()

```JavaScript
// 非標準: 該特性是非標準的，請盡量不要在生產環境中使用它
// 建議生產環境不要使用

/**
 * console.count([label])
 * 如果有 label，此函數輸出為那個指定的 label 和 count() 被調用的次數。
 * 如果 label 被忽略，此函數輸出 count() 在其所處位置上被調用的次數。
 */
console.count()
/**
 * console.countReset([label])
 * 如果提供了參數label，此函數會重置與 label 關聯的計數。
 * 如果省略了參數label，此函數會重置默認的計數器。
 */
console.countReset()
```

## 分組 console.group()

```JavaScript
/**
 * group(label)
 * 創建一個新的分組。
 * 隨後輸出到控制台上的內容都會被添加一個縮進，表示該內容屬於當前分組，直到調用 console.groupEnd() 之後，當前分組結束。
 */
console.group()
/**
 * 創建一個新的分組。
 * 隨後輸出到控制台上的內容都會被添加一個縮進，表示該內容屬於當前分組，直到調用 console.groupEnd() 之後，當前分組結束。
 * 和 console.group()方法的不同點是，新建的分組默認是折疊的。用戶必須點擊一個按鈕才能將折疊的內容打開。
 */
console.groupCollapsed()
// 結束分組
console.groupEnd()
```

## 計時器 console.time()

```JavaScript
/**
 * console.time(timerName)
 * 啟動一個計時器來跟踪某一個操作的佔用時長。
 * 每一個計時器必須擁有唯一的名字，頁面中最多能同時運行 10,000 個計時器。
 * 當以此計時器名字為參數調用 console.timeEnd() 時，瀏覽器將以毫秒為單位，輸出對應計時器所經過的時間。
 */
console.time()

/**
 * console.timeLog(label)
 * 在控制台輸出計時器的值，該計時器必須已經通過 console.time() 啟動。
 * label 計時器索引。
 *
 * 如果沒有傳入 label 參數，則以 default: 作為引導返回數據
 * 如果傳入了一個已經存在的 label ，則會以 label: 作為引導返回數據
 */
console.timeLog()

/**
 * console.timeEnd(label)
 * 停止一個通過 console.time() 啟動的計時器
 * label 需要停止的計時器名字。一旦停止，計時器所經過的時間會被自動輸出到控制台。
 */

console.time("answer time");
alert("Click to continue");
console.timeLog("answer time");
alert("Do a bunch of other stuff...");
console.timeEnd("answer time");
```

## 表格 console.table()

```JavaScript
/**
 * 輸出表格
 * console.table(data [, columns]);
 * data 要顯示的數據。必須是數組或對象。
 * columns 一個包含列的名稱的數組。
 */
// 一個由字符串組成的數組
console.table(["apples", "oranges", "bananas"]);
// ┌─────────┬───────────┐
// │ (index) │  Values   │
// ├─────────┼───────────┤
// │    0    │ 'apples'  │
// │    1    │ 'oranges' │
// │    2    │ 'bananas' │
// └─────────┴───────────┘

// 一個屬性值是字串的對比
function Person(firstName, lastName) {
  this.firstName = firstName;
  this.lastName = lastName;
}
let me = new Person("John", "Smith");
console.table(me);
// ┌───────────┬─────────┐
// │  (index)  │ Values  │
// ├───────────┼─────────┤
// │ firstName │ 'John'  │
// │ lastName  │ 'Smith' │
// └───────────┴─────────┘

// 二元數組
let people = [["John", "Smith"], ["Jane", "Doe"], ["Emily", "Jones"]]
console.table(people);
// ┌─────────┬─────────┬─────────┐
// │ (index) │    0    │    1    │
// ├─────────┼─────────┼─────────┤
// │    0    │ 'John'  │ 'Smith' │
// │    1    │ 'Jane'  │  'Doe'  │
// │    2    │ 'Emily' │ 'Jones' │
// └─────────┴─────────┴─────────┘

// 一個包含對象的數字組
function Person(firstName, lastName) {
  this.firstName = firstName;
  this.lastName = lastName;
}
var john = new Person("John", "Smith");
var jane = new Person("Jane", "Doe");
var emily = new Person("Emily", "Jones");
console.table([john, jane, emily]);
// ┌─────────┬───────────┬──────────┐
// │ (index) │ firstName │ lastName │
// ├─────────┼───────────┼──────────┤
// │    0    │  'John'   │ 'Smith'  │
// │    1    │  'Jane'   │  'Doe'   │
// │    2    │  'Emily'  │ 'Jones'  │
// └─────────┴───────────┴──────────┘

// 屬性名是對象的對象
function Person(firstName, lastName) {
  this.firstName = firstName;
  this.lastName = lastName;
}
var family = {};
family.mother = new Person("Jane", "Smith");
family.father = new Person("John", "Smith");
family.daughter = new Person("Emily", "Smith");
console.table(family);
// ┌──────────┬───────────┬──────────┐
// │ (index)  │ firstName │ lastName │
// ├──────────┼───────────┼──────────┤
// │  mother  │  'Jane'   │ 'Smith'  │
// │  father  │  'John'   │ 'Smith'  │
// │ daughter │  'Emily'  │ 'Smith'  │
// └──────────┴───────────┴──────────┘

// 對像數組，只打印 firstName
function Person(firstName, lastName) {
  this.firstName = firstName;
  this.lastName = lastName;
}
var john = new Person("John", "Smith");
var jane = new Person("Jane", "Doe");
var emily = new Person("Emily", "Jones");
console.table([john, jane, emily], ["firstName"]);
// ┌─────────┬───────────┐
// │ (index) │ firstName │
// ├─────────┼───────────┤
// │    0    │  'John'   │
// │    1    │  'Jane'   │
// │    2    │  'Emily'  │
// └─────────┴───────────┘
```