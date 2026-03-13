# JavaScript Node 模組 puppeteer-real-browser(控制和自動化基於 Chromium 的瀏覽器操作)

```

Puppeteer 是一個由 Google 開發的 Node.js 庫，用於控制和自動化基於 Chromium 的瀏覽器操作。
它提供了一個高級 API，使可以輕鬆地編寫 Node.js 腳本來模擬人類對瀏覽器的交互行為，比如點擊、填寫表單、截圖、PDF生成等等。

Puppeteer 的一些主要特點包括：

控制瀏覽器: Puppeteer 可以啟動一個獨立的 Chromium 瀏覽器進程，並且可以通過 Puppeteer API 控制瀏覽器的各種操作。

自動化任務: 可以編寫腳本來自動執行瀏覽器操作，比如填寫表單、點擊按鈕、獲取元素內容等，從而實現自動化任務。

截圖和PDF生成: Puppeteer 可以幫助截取網頁的截圖，也可以將網頁保存為 PDF 文件。

性能測試: Puppeteer 還可以用於測試網頁的性能，比如載入時間、內存使用量等。

爬蟲和網絡數據擷取: 由於 Puppeteer 可以模擬瀏覽器行為，因此它也常用於爬蟲和網絡數據擷取。
```

## 目錄

- [JavaScript Node 模組 puppeteer-real-browser(控制和自動化基於 Chromium 的瀏覽器操作)](#javascript-node-模組-puppeteer-real-browser控制和自動化基於-chromium-的瀏覽器操作)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[puppeteer-real-browser npm 頁面](https://www.npmjs.com/package/puppeteer-real-browser)

[puppeteer-real-browser Github](https://github.com/zfcsoftware/puppeteer-real-browser)

[Puppeteer 文檔](https://pptr.dev/)

# 指令

```bash
# 安裝
npm install puppeteer-real-browser

yarn add puppeteer-real-browser
```

# 用法

打開 Google 首頁，搜索關鍵字並截取一張截圖

```JavaScript
const puppeteer = require('puppeteer');

(async () => {
  // 啟動 Puppeteer
  const browser = await puppeteer.launch();

  // 創建一個新的瀏覽器頁面
  const page = await browser.newPage();

  // 訪問 Google 首頁
  await page.goto('https://www.google.com');

  // 在搜索框中輸入關鍵字並提交搜索
  await page.type('input[name="q"]', 'Puppeteer');
  await page.keyboard.press('Enter');

  // 等待搜索結果加載完成
  await page.waitForSelector('div#search');

  // 截取搜索結果頁面的截圖
  await page.screenshot({ path: 'example.png' });

  // 關閉瀏覽器
  await browser.close();
})();
```