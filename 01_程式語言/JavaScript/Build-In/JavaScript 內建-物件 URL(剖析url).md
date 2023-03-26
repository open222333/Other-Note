# JavaScript 內建-物件 URL(剖析url)

```
URL 介面提供了建立 URL 物件的靜態方法

URL 標准定義了 URL、域、IP 地址、application/x-www-form-urlencoded 格式及其 API。
```

## 目錄

- [JavaScript 內建-物件 URL(剖析url)](#javascript-內建-物件-url剖析url)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [函式相關](#函式相關)
- [用法](#用法)
- [傳統的URL函式(已棄用)](#傳統的url函式已棄用)

## 參考資料

[URL MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/API/URL)

[URL - 標準化](https://url.spec.whatwg.org/)

### 函式相關

[encodeURIComponent()](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/encodeURIComponent)

[decodeURIComponent()](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/decodeURIComponent)

# 用法

```JavaScript
let url = new URL("http://example.com:8000/path/name?q=term#fragment");
url.href; // => http://example.com:8000/path/name?q=term#fragment
url.origin; // => http://example.com:8000
url.protocol; // => http:
url.host; // => example.com:8000
url.hostname; // => example.com
url.port; // => 8000
url.pathname; // => /path/name
url.search; // => ?q=term
url.hash; // => #fragment

// 剖析使用者以及密碼 但通常不使用
let ftpurl = new URL("ftp://admin:13337!@ftp.example.com/");
ftpurl.href; // => ftp://admin:13337!@ftp.example.com/
ftpurl.origin; // => ftp://ftp.example.com
ftpurl.username; // => admin
ftpurl.password; // => 13337!

// 特性 protocol,host 是唯讀的，其餘特性可讀可寫新增特性
let newurl = new URL("http://example.com");
newurl.pathname = "api/search"; // 新增api端點路徑
newurl.search = "q=test"; // 新增查詢參數
newurl.toString(); // => http://example.com/api/search?q=test

// 加上標點符號並轉譯成url的特殊字元
let escapesurl = new URL("http://example.com");
escapesurl.pathname = "paht with spaces"; // 有空白字元
escapesurl.search = "q=foo#bar";
escapesurl.pathname; // => /paht%20with%20spaces
escapesurl.search; // => ?q=foo%23bar
escapesurl.href; // => http://example.com/paht%20with%20spaces?q=foo%23bar

// 使用 searchParams 新增參數
let paramsurl = new URL("http://example.com");
paramsurl.search; // => "" 還沒有
paramsurl.searchParams.append("q", "term"); // 新增一個搜尋參數
paramsurl.search; // => ?q=term
paramsurl.searchParams.set("q", "x"); // 設置搜尋參數的值
paramsurl.search; // => ?q=x
paramsurl.searchParams.get("q", "x"); // 取得搜尋參數的值
paramsurl.searchParams.has("q"); // true: 存在q參數
paramsurl.searchParams.has("p"); // false: 沒有p參數
paramsurl.searchParams.append("opts", "1"); // 新增一個搜尋參數
paramsurl.search; // => ?q=x&opts=1
paramsurl.searchParams.append("opts", "&"); // 新增相同參數名稱 不同值
paramsurl.search; // => ?q=x&opts=1&opts=%26 有轉譯
paramsurl.searchParams.get("opts"); // "1": 第一個值
paramsurl.searchParams.getAll("opts"); // [ '1', '&' ] 所有值
paramsurl.searchParams.sort(); // 依照字母排列
paramsurl.search; // => ?opts=1&opts=%26&q=x
paramsurl.searchParams.set("opts", "y");
paramsurl.search; // => ?opts=y&q=x
// 可迭代
[...paramsurl.searchParams];
paramsurl.searchParams.delete("opts"); // 刪除opts參數
paramsurl.search; // => ?q=x
paramsurl.href; // => http://example.com/?q=x

// searchParams特性是一個URKSearchParams物件
let params2url = new URL("http://example.com");
let params = new URLSearchParams();
params.append("q", "term");
params.append("opts", "exact");
params.toString(); // => q=term&opts=exact
params2url.search = params;
params2url.href; // => http://example.com/?q=term&opts=exact
```

# 傳統的URL函式(已棄用)

```JavaScript
// 已被棄用
escape()
unescape()

// 替代
// 函數通過將特定字符的每個實例替換為一個、兩個、三或四轉義序列來對統一資源標識符 (URI) 進行編碼 (該字符的 UTF-8 編碼僅為四轉義序列) 由兩個 "代理" 字符組成)。
encodeURI()
// 解碼由encodeURI 創建或其他流程得到的統一資源標識符（URI）。
decodeURI()

/**
 * 通過將特定字符的每個實例替換成代表字符的 UTF-8 編碼的一個、兩個、三個或四個轉義序列來編碼 URI（只有由兩個“代理”字符組成的字符會被編碼為四個轉義序列）。
 * 與 encodeURI() 相比，此函數會編碼更多的字符，包括 URI 語法的一部分。
 */
encideURIComponent()
// 解碼由 encodeURIComponent 方法或者其他類似方法編碼的部分統一資源標識符（URI）。
decodeURIComponent()
```