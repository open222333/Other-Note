# JavaScript 工具 Polyfill.io(模擬新的 JavaScript 功能在舊瀏覽器)(已有資安問題)

```
Polyfill.io 是一個服務，它可以根據使用者的瀏覽器需求動態地加載所需的 JavaScript polyfills。polyfills 是指那些用來模擬新的 JavaScript 功能在舊瀏覽器中的行為的代碼，這樣就可以在舊的瀏覽器中使用新的 JavaScript 功能。


除了 Polyfill.io，還有一些其他工具和服務可以用來實現相似的功能，即提供 polyfills 以確保在舊瀏覽器上也能運行現代 JavaScript 特性。

以下是一些類似的工具：

1. Babel
Babel 是一個 JavaScript 編譯器，主要用於將現代 JavaScript（包括 ECMAScript 2015+ 和 JSX）轉譯為向後兼容的 JavaScript 代碼。Babel 通常與 polyfill 庫結合使用，如 @babel/polyfill 或 core-js，以確保在舊瀏覽器中的兼容性。

2. core-js
core-js 是一個模塊化的標準庫 polyfill，可以讓開發者選擇性地加載所需的 polyfills。它包括大量的 ECMAScript 特性和其他標準的 polyfills。

3. es5-shim
es5-shim 提供 ECMAScript 5 特性的 polyfill。這些 shim 使得老舊瀏覽器可以支持 ECMAScript 5 的標準特性，如 Array.prototype.forEach、Object.create 等。

4. Modernizr
Modernizr 是一個 JavaScript 庫，可以檢測當前瀏覽器所支持的 HTML5 和 CSS3 特性。雖然它本身不提供 polyfills，但它可以與其他 polyfill 庫結合使用，根據檢測結果有選擇地加載 polyfills。

5. Promise-polyfill
Promise-polyfill 是一個實現 ES6 Promises 的 polyfill 庫。對於那些不支持原生 Promise 的舊瀏覽器（如 IE），這個庫非常有用。

6. Web Components Polyfills
Web Components Polyfills 是一組專門針對 Web Components 標準（如 Shadow DOM、Custom Elements 和 HTML Imports）的 polyfills。

7. HTML5 Shiv
HTML5 Shiv 是一個用於 Internet Explorer 6-8 支持 HTML5 標籤的腳本。它使這些舊版本的 IE 能夠正確地渲染和風格化 HTML5 標籤。

8. Autoprefixer
Autoprefixer 是一個 PostCSS 插件，用於自動添加不同瀏覽器所需的 CSS 前綴。雖然它不是傳統意義上的 polyfill，但它在解決跨瀏覽器兼容性問題上非常有用。

這些工具和服務提供了各種解決方案，以確保現代 web 技術在老舊瀏覽器上的兼容性。選擇哪個工具取決於你的項目需求和開發環境。
```

## 目錄

- [JavaScript 工具 Polyfill.io(模擬新的 JavaScript 功能在舊瀏覽器)(已有資安問題)](#javascript-工具-polyfillio模擬新的-javascript-功能在舊瀏覽器已有資安問題)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [資安相關](#資安相關)
- [用法](#用法)

## 參考資料

### 資安相關

[【資安日報】6月27日，舊版瀏覽器網站相容套件Polyfill.io被中國公司買下，驚傳被植入惡意程式碼，恐影響逾10萬網站](https://www.ithome.com.tw/news/163687)

[【資安日報】6月28日，前幾天Polyfill.io供應鏈攻擊事件曝光震撼整個IT界，後續傳出中國CDN業者另起爐灶，再度對10萬網站下手](https://www.ithome.com.tw/news/163709)

[發動供應鏈攻擊的Polyfill.io經營者傳出另起爐灶，再度向超過10萬網站傳送惡意程式碼](https://www.ithome.com.tw/news/163707)

[請儘速遠離 cdn.polyfill.io 之惡意程式碼淺析](https://blog.huli.tw/2024/06/25/stop-using-polyfill-io/)

# 用法

加載 Polyfill.io：
可以通過向的 HTML 文件中添加一個腳本標籤來加載 Polyfill.io。Polyfill.io 會根據使用者的瀏覽器自動檢測並加載所需的 polyfills。

```html
<script src="https://polyfill.io/v3/polyfill.min.js"></script>
```

指定所需的功能：

可以指定哪些 JavaScript 功能需要 polyfill。例如，如果只需要 Array.prototype.includes 的 polyfill，可以這樣做：

```html
<script src="https://polyfill.io/v3/polyfill.min.js?features=Array.prototype.includes"></script>
```

指定瀏覽器的目標版本：

還可以指定瀏覽器的目標版本，這樣 Polyfill.io 會根據目標版本加載適當的 polyfills：

```html
<script src="https://polyfill.io/v3/polyfill.min.js?features=default,Array.prototype.includes&targets=defaults,not IE 11"></script>
```