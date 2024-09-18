# JavaScript Node 模組 puppeteer(網頁自動化)

```
用來完成各種網頁自動化任務。
通過它，可以自動化瀏覽器操作、網頁抓取、測試應用程式等
```

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

## 參考資料

[puppeteer npm 頁面](https://www.npmjs.com/package/puppeteer)

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
