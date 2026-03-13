# Python 模組 playwright(現代化的瀏覽器自動化庫)

```
Playwright 是一個現代化的瀏覽器自動化庫，支援多瀏覽器和多語言，可以用於編寫爬蟲、自動化測試和其他需要模擬瀏覽器行為的任務。它支持 Chrome、Firefox、WebKit（三大瀏覽器引擎），並具有速度快、穩定性高、易於使用的特點。

以下是 Playwright 的用法介紹和示例：

Playwright 的特性
多瀏覽器支持：
支援 Chromium、Firefox 和 WebKit，並支持無頭模式和有頭模式。

多平台支持：
可在 Windows、macOS 和 Linux 上運行。

多語言支持：
支援 Python、JavaScript/Node.js、Java 和 C#。

自動化測試功能：
支持錄製腳本、自動等待元素、模擬鍵盤輸入、點擊和拖放等操作。

內置網絡攔截：
可以監聽和修改網絡請求/響應，用於處理 Cookies 和驗證問題。
```

## 目錄

- [Python 模組 playwright(現代化的瀏覽器自動化庫)](#python-模組-playwright現代化的瀏覽器自動化庫)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [安裝](#安裝)
- [用法](#用法)
  - [啟動瀏覽器並加載網頁](#啟動瀏覽器並加載網頁)
  - [處理表單和模擬輸入](#處理表單和模擬輸入)
  - [模擬移動設備](#模擬移動設備)
  - [攔截和修改請求](#攔截和修改請求)
  - [使用代理](#使用代理)
  - [解決 Cloudflare 防護](#解決-cloudflare-防護)
  - [使用上下文管理多個頁面](#使用上下文管理多個頁面)
  - [處理 Cookies](#處理-cookies)

## 參考資料

[playwright pypi](https://pypi.org/project/playwright/)

[microsoft/playwright Github](https://github.com/microsoft/playwright)

# 指令

```bash
# 安裝
pip install playwright
```

# 安裝

安裝瀏覽器引擎

執行以下命令安裝所需的瀏覽器

```bash
playwright install
```

# 用法

## 啟動瀏覽器並加載網頁

以下示例展示了如何啟動 Chromium 瀏覽器，打開一個網頁並截圖

```Python
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    # 啟動 Chromium 瀏覽器
    browser = p.chromium.launch(headless=False)  # headless=True 表示無頭模式
    page = browser.new_page()

    # 打開目標網頁
    page.goto("https://example.com")

    # 截圖
    page.screenshot(path="screenshot.png")

    # 關閉瀏覽器
    browser.close()
```

## 處理表單和模擬輸入

模擬用戶輸入、點擊按鈕

```Python
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(headless=False)
    page = browser.new_page()

    # 打開網頁
    page.goto("https://example.com/login")

    # 填寫表單
    page.fill("input[name='username']", "my_username")
    page.fill("input[name='password']", "my_password")

    # 點擊提交按鈕
    page.click("button[type='submit']")

    # 等待導航完成
    page.wait_for_load_state("networkidle")

    # 關閉瀏覽器
    browser.close()
```

## 模擬移動設備

Playwright 支持模擬不同的設備和視窗大小

```Python
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)

    # 模擬 iPhone 12
    iphone = p.devices['iPhone 12']
    context = browser.new_context(**iphone)
    page = context.new_page()

    page.goto("https://example.com")
    page.screenshot(path="mobile_view.png")

    browser.close()
```

## 攔截和修改請求

```Python
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(headless=False)
    page = browser.new_page()

    # 攔截並修改請求標頭
    page.route("**/*", lambda route, request: route.continue_(headers={
        **request.headers,
        "User-Agent": "MyCustomUserAgent/1.0"
    }))

    page.goto("https://httpbin.org/headers")
    print(page.content())

    browser.close()
```

## 使用代理

設置代理伺服器，用於匿名訪問

```Python
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(proxy={"server": "http://myproxyserver:port"})
    page = browser.new_page()
    page.goto("https://example.com")
    print(page.title())
    browser.close()
```

## 解決 Cloudflare 防護

模擬真實用戶行為（例如，等待 JS 加載完成）可以繞過一些簡單的 Cloudflare 防護

```Python
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(headless=False)
    page = browser.new_page()

    # 打開 Cloudflare 防護網站
    page.goto("https://www.cloudflareprotectedwebsite.com")

    # 等待 JS 挑戰完成
    page.wait_for_load_state("networkidle")

    print(page.title())
    browser.close()
```

## 使用上下文管理多個頁面

Playwright 支持多個瀏覽上下文，用於模擬多個用戶

```Python
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(headless=False)

    # 創建多個上下文
    context1 = browser.new_context()
    context2 = browser.new_context()

    page1 = context1.new_page()
    page2 = context2.new_page()

    page1.goto("https://example.com")
    page2.goto("https://example.org")

    browser.close()
```

## 處理 Cookies

讀取和設置 Cookies

```Python
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(headless=False)
    page = browser.new_page()

    # 設置 Cookies
    page.context.add_cookies([{
        "name": "session_id",
        "value": "123456",
        "domain": "example.com",
        "path": "/"
    }])

    # 打開網頁並檢查 Cookies
    page.goto("https://example.com")
    print(page.context.cookies())

    browser.close()
```
