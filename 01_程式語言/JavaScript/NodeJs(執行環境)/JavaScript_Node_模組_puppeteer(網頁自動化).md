# JavaScript Node 模組 puppeteer(網頁自動化)

```
用來完成各種網頁自動化任務。
通過它，可以自動化瀏覽器操作、網頁抓取、測試應用程式等
```

基於 Node.js、控制無頭 Chromium / Chrome 的高階 API，常見用途：

- 網頁抓取：導航互動式網站並擷取資料
- PDF 生成：從網頁或 HTML 產生 PDF 報告
- 截圖：網站監控、視覺回歸測試
- 自動化測試：模擬使用者操作做端到端測試，可等待 SPA 動態內容載入
- 效能監控：量測頁面載入時間等指標

## 目錄

- [JavaScript Node 模組 puppeteer(網頁自動化)](#javascript-node-模組-puppeteer網頁自動化)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [開啟瀏覽器並截圖](#開啟瀏覽器並截圖)
  - [無頭模式（Headless）](#無頭模式headless)
  - [獲取網頁內容](#獲取網頁內容)
  - [填寫表單並提交](#填寫表單並提交)
  - [等待某個元素](#等待某個元素)
  - [模擬點擊](#模擬點擊)
  - [錯誤處理與排錯](#錯誤處理與排錯)
  - [Page.exposeFunction() method](#pageexposefunction-method)

## 參考資料

[puppeteer npm 頁面](https://www.npmjs.com/package/puppeteer)

[官方文檔](https://pptr.dev/api/puppeteer.puppeteernode)

[Page.exposeFunction() method](https://pptr.dev/api/puppeteer.page.exposefunction)

# 指令

```bash
# 安裝
npm install puppeteer


npm install puppeteer --save


yarn add puppeteer
```

# 用法

## 開啟瀏覽器並截圖

```JavaScript
const puppeteer = require('puppeteer');

(async () => {
  // 啟動瀏覽器
  const browser = await puppeteer.launch();

  // 打開新頁面
  const page = await browser.newPage();

  // 瀏覽到指定網址
  await page.goto('https://example.com');

  // 截圖並保存到本地檔案
  await page.screenshot({ path: 'example.png' });

  // 關閉瀏覽器
  await browser.close();
})();
```

## 無頭模式（Headless）

```JavaScript
const browser = await puppeteer.launch({ headless: false });
```

## 獲取網頁內容

抓取 example.com 的標題

```JavaScript
const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  await page.goto('https://example.com');

  // 抓取標題內容
  const title = await page.title();
  console.log('Page title:', title);

  await browser.close();
})();
```

## 填寫表單並提交

在 Google 搜尋框中輸入內容並進行搜尋

```JavaScript
const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch({ headless: false });
  const page = await browser.newPage();
  await page.goto('https://www.google.com');

  // 模擬填寫搜尋框並按下 Enter 鍵
  await page.type('input[name="q"]', 'Puppeteer 教學');
  await page.keyboard.press('Enter');

  // 等待搜尋結果頁面載入
  await page.waitForNavigation();

  console.log('Search completed');
  await browser.close();
})();
```

## 等待某個元素

```JavaScript
await page.waitForSelector('button.some-class');
```

## 模擬點擊

```JavaScript
await page.click('button.some-class');
```

## 錯誤處理與排錯

```JavaScript
try {
  await page.waitForSelector('button.some-class', { timeout: 5000 });
  await page.click('button.some-class');
} catch (error) {
  console.error('元素未找到或超時', error);
}
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
