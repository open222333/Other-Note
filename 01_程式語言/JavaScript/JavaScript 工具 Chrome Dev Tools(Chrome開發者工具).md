# JavaScript 工具 Chrome Dev Tools(Chrome開發者工具)

```
google 瀏覽器開發者工具
```

## 目錄

- [JavaScript 工具 Chrome Dev Tools(Chrome開發者工具)](#javascript-工具-chrome-dev-toolschrome開發者工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [檢視 AJAX Request](#檢視-ajax-request)
	- [讓瀏覽器可以直接編輯網頁](#讓瀏覽器可以直接編輯網頁)

## 參考資料

[[學習筆記] Chrome Dev Tools 開發者工具實用功能整理](https://pjchender.blogspot.com/2017/06/chrome-dev-tools.html)

# 用法

```JavaScript
/**
 * 基本 console 使用 幫助視覺化呈現
**/
console.warn('<output>')
console.error('<output>')
console.info('<output>')
console.assert([Condition Expression], '<output>')
console.clear()

// 檢視 HTML 元素
console.dir([DOMElement])
console.table([object/array])


/**
 * 將輸出資料分群顯示
**/
console.group('<groupName>')             // 開始分群，預設展開
console.groupCollapsed('<groupName>')    // 開始分群，預設不展開
console.groupEnd()                       // 結束分群

/**
 * 使用 '%c' 幫輸出的內容添加 CSS 樣式，就可以改變 console.log 輸出的文字樣式
**/
console.log('%c What a Cool Console', 'font-size: 32px; color: red')

// 計數
console.count([String])

// 計時
console.time([String])            // 開始計時
console.timeEnd([String])         // 結束計時

// 監聽事件
monitorEvents(element [,event])     //    監聽某一元素
unmonitorEvents(element [,event])   //    取消監聽某一元素
getEventListeners(element)          //    查看某一元素綁定了哪些事件

// 選擇 DOM 元素
$0                 // 表示當前所選元素
$(selector)        // 等同於 document.querySelector()
$$(selector)       // 等同於 document.querySelectorAll()
```

## 檢視 AJAX Request

```
在 console 視窗中點右鍵，勾選 “Log XMLHTTPRequest” 就可以看到該網站所發出的 AJAX request
```

## 讓瀏覽器可以直接編輯網頁

```JavaScript
// 在 console 中輸入
document.designMode = 'on'
```