# JavaScript Node 模組 puppeteer(網頁爬蟲)

```
Puppeteer 是一個基於 Node.js 的庫，它提供了一個高級 API，用於自動化和控制無界面的 Chromium（或 Chrome）瀏覽器。
Puppeteer允許進行各種任務，例如網頁抓取、生成 PDF、拍攝截圖和自動化測試，並使用可編程接口。

網頁抓取：Puppeteer 可用於導航和交互式網站，提取數據並執行網頁抓取任務。

PDF 生成：可以使用 Puppeteer 從網頁或 HTML 內容生成 PDF 文件，這對於創建報告和文件非常有用。

截圖：Puppeteer 允許捕獲網頁的截圖，這對於網站監控或視覺回歸測試非常有用。

自動化測試：Puppeteer 常用於對 Web 應用程序進行端到端測試。它可以模擬用戶交互並驗證網頁的行為。

單頁應用程序（SPA）測試：Puppeteer 適用於 SPA，可以等待動態內容加載後再執行操作。

性能監控：Puppeteer 可用於測量網頁性能，包括頁面加載時間等指標。

瀏覽器自動化：可以使用 Puppeteer 編寫瀏覽器操作，如點擊、提交表單和導航。
```

## 目錄

- [JavaScript Node 模組 puppeteer(網頁爬蟲)](#javascript-node-模組-puppeteer網頁爬蟲)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [Page.exposeFunction() method](#pageexposefunction-method)

## 參考資料

[puppeteer npm 頁面](https://www.npmjs.com/package/puppeteer)

[官方文檔](https://pptr.dev/api/puppeteer.puppeteernode)

[Page.exposeFunction() method](https://pptr.dev/api/puppeteer.page.exposefunction)

# 指令

```bash
# 安裝
npm install puppeteer

yarn add puppeteer
```

# 用法

```JavaScript
// 使用 Puppeteer 拍攝網頁截圖的簡單示例
const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  await page.goto('https://example.com');
  await page.screenshot({ path: 'example.png' });

  await browser.close();
})();
```

## Page.exposeFunction() method

```JavaScript
/**
 * 在 Page(WebPage) 插入底層 js 的程式碼，
 * 主要目的是用來避免在頁面執行 js 腳本打 api 傳資料會有跨網域問題，
 * 所以 js 腳本爬到的資料在 js 腳本用 page.exposeFunction ，
 * 透過底層的 function 去打 api
 */
import puppeteer from 'puppeteer';
import crypto from 'crypto';

(async () => {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  page.on('console', msg => console.log(msg.text()));
  await page.exposeFunction('md5', text =>
    crypto.createHash('md5').update(text).digest('hex')
  );
  await page.evaluate(async () => {
    // use window.md5 to compute hashes
    const myString = 'PUPPETEER';
    const myHash = await window.md5(myString);
    console.log(`md5 of ${myString} is ${myHash}`);
  });
  await browser.close();
})();
```

```JavaScript
import puppeteer from 'puppeteer';
import fs from 'fs';

(async () => {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  page.on('console', msg => console.log(msg.text()));
  await page.exposeFunction('readfile', async filePath => {
    return new Promise((resolve, reject) => {
      fs.readFile(filePath, 'utf8', (err, text) => {
        if (err) reject(err);
        else resolve(text);
      });
    });
  });
  await page.evaluate(async () => {
    // use window.readfile to read contents of a file
    const content = await window.readfile('/etc/hosts');
    console.log(content);
  });
  await browser.close();
})();
```