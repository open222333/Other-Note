# JavaScript JQuery 內建 ajax(非同步的JavaScript與XML技術)

```
AJAX即「Asynchronous JavaScript and XML」（非同步的JavaScript與XML技術），指的是一套綜合了多項技術的瀏覽器端網頁開發技術。
```

## 目錄

- [JavaScript JQuery 內建 ajax(非同步的JavaScript與XML技術)](#javascript-jquery-內建-ajax非同步的javascript與xml技術)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[jQuery.ajax()](https://api.jquery.com/jquery.ajax/#jQuery-ajax-url-settings)

# 用法

```JavaScript
$.ajax({
  url: , // 要請求資料的網址
  method: , // 請求資料的方式(Ex:POST / GET / PUT...等)
  dataType: , // 請求資料的類型(Ex:xml, json, script, or html...等)
  data: ,// 如果需要傳送資料時，則將資料設定在這裡。

  success: function (res) {
	// 當成功接收到資料時，success 會執行並顯示結果。
	console.log(res)
  },
  
  error: function (err) {
	// 當接收資料失敗時，error 會執行並顯示結果。
    console.log(err)
  },
});
```
