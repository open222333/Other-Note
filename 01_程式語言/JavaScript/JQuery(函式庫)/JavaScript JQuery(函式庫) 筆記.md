# JavaScript JQuery(函式庫) 筆記

```
jQuery是一套跨瀏覽器的JavaScript函式庫，用於簡化HTML與JavaScript之間的操作。
```

## 參考資料

[官網](https://jquery.com/)

[官網API文檔](https://api.jquery.com/)

[下載](https://jquery.com/download/)

[jQuery Tutorial](https://www.w3schools.com/jquery/default.asp)

[RUNOOB](https://www.runoob.com/jquery/jquery-ref-selectors.html)

[API文檔](https://www.runoob.com/manual/jquery/)

[參考手冊](https://www.runoob.com/jquery/jquery-ref-selectors.html)

[JQuery UI 下載](https://jqueryui.com/download/all/)

[jQuery是什麼，它跟JavaScript有什麼關係？它又有什麼能耐呢？](https://progressbar.tw/posts/6)

## $.ajax()

```js
$.ajax({
  url: ,
  method: ,
  dataType: ,
  data:
    success: function (res) {
      console.log(res)
    },
  error: function (err) {
    console.log(err)
  },
});
```

```
url: 要請求資料的網址
method: 請求資料的方式(Ex:POST / GET / PUT...等)
dataType: 請求資料的類型(Ex:xml, json, script, or html...等)
data: 如果需要傳送資料時，則將資料設定在這裡。

success: 當成功接收到資料時，success 會執行並顯示結果。
error : 當接收資料失敗時，error 會執行並顯示結果。
```

# JQuery

# $() jQuery Selectors

```
jQuery 選擇器允許您選擇和操作 HTML 元素。

jQuery 選擇器用於根據名稱、id、類、類型、屬性、屬性值等“查找”（或選擇）HTML 元素。它基於現有的CSS 選擇器，此外，它還有一些自己的自定義選擇器。

jQuery 中的所有選擇器都以美元符號和括號開頭：$()。
```

Syntax | Description
--- | ---
$("*") | Selects all elements
$(this) | Selects the current HTML element
$("p.intro") | Selects all <p> elements with class="intro"
$("p:first") | Selects the first <p> element
$("ul li:first") | Selects the first <li> element of the first <ul>
$("ul li:first-child") | Selects the first <li> element of every <ul>
$("[href]") | Selects all elements with an href attribute
$("a[target='_blank']") | Selects all <a> elements with a target attribute value equal to "_blank"
$("a[target!='_blank']") | Selects all <a> elements with a target attribute value NOT equal to "_blank"
$(":button") | Selects all <button> elements and <input> elements of type="button"
$("tr:even") | Selects all even <tr> elements
$("tr:odd") | Selects all odd <tr> elements