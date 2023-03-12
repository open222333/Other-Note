# JavaScript JQuery 內建 loadingModal(全屏加載模式 指示器)

```
用於 jQuery 的全屏加載模式/指示器插件 - loadingModal
```

## 目錄

- [JavaScript JQuery 內建 loadingModal(全屏加載模式 指示器)](#javascript-jquery-內建-loadingmodal全屏加載模式-指示器)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[Fullscreen Loading Modal / Indicator Plugin For jQuery - loadingModal](https://www.jqueryscript.net/loading/Fullscreen-Loading-Modal-Indicator-Plugin-For-jQuery-loadingModal.html)

# 用法

```html
<!-- 將主樣式表 jquery.loadingModal.css 放在 html 頁面的 -->
<link rel="stylesheet" href="css/jquery.loadingModal.css">

<!-- 將 jQuery 庫和 JavaScript 文件 jquery.loadingModal.js 放在文檔的末尾 -->
<script src="//code.jquery.com/jquery-3.1.1.slim.min.js"></script>
<script src="js/jquery.loadingModal.js"></script>
```

```JavaScript
// 以下是配置全屏加載模式的默認值的完整選項。
$('body').loadingModal({
  position: 'auto',
  text: '',
  color: '#fff',
  opacity: '0.7',
  backgroundColor: 'rgb(0,0,0)',
  animation: 'doubleBounce'
});

// hide the loading modal
$('body').loadingModal('hide');

// destroy the plugin
$('body').loadingModal('destroy');
```
