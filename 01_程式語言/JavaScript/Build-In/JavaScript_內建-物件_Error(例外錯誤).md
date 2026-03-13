# JavaScript 內建-物件 Error(例外錯誤)

```
Error 建構函式能用來建立一個 error 物件。
當執行期間發生錯誤時，Error 物件實體會被拋出。Error 物件也可作為自訂例外的基礎物件，請參考下方的標準內建錯誤類型。
```

## 目錄

- [JavaScript 內建-物件 Error(例外錯誤)](#javascript-內建-物件-error例外錯誤)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[Error MDN Web Doc](https://developer.mozilla.org/zh-TW/docs/Web/JavaScript/Reference/Global_Objects/Error)

# 用法

```JavaScript
/**
 * 自定義Error子類別
 * 處理失敗請求的HTTP狀態碼
 */

class HTTPError extends Error {
  constructor(status, statusText, url) {
    super(`${status} ${statusText}: ${url}`);
    this.status = status;
    this.statusText = statusText;
    this.url = url;
  }

  get name() {
    return "HTTPError";
  }
}

let error = new HTTPError(404, "Not Found", "http://example.com/");
error.status; // => 404
error.statusText; // => "404 Not Found: http://example.com/"
error.name; // => "HTTPError"
```