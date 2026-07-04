# FlareSolverr(處理網站的反爬蟲機制)

```
FlareSolverr 是一款專門用來處理網站的反爬蟲機制（例如 Cloudflare 的 DDoS 防護）的工具。它通過啟動一個基於瀏覽器的解決方案（例如使用 Puppeteer 驅動無頭瀏覽器），自動處理 Cloudflare JS 挑戰、CAPTCHA 和其他反爬蟲機制。

FlareSolverr 的功能
處理 Cloudflare 和 DDoS 防護：
自動處理 Cloudflare 的挑戰，包括 JavaScript 檢測和部分 CAPTCHA 驗證。

模擬完整的瀏覽器行為：
使用無頭瀏覽器（如 Chromium）執行請求，模擬真實用戶的行為。

支持 HTTP API：
提供一個簡單的 HTTP API 接口，讓用戶可以輕鬆集成到自己的爬蟲或其他應用中。

可配置代理：
支持設置代理伺服器，增加匿名性和分散流量來源。

FlareSolverr 是一個代理服務，用於繞過 Cloudflare 的反機器人檢測。
它接收 HTTP 請求，並通過自動化的瀏覽器（例如 Headless Chrome）處理 Cloudflare 挑戰，返回解決後的內容。
```

## 目錄

- [FlareSolverr(處理網站的反爬蟲機制)](#flaresolverr處理網站的反爬蟲機制)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [教學相關](#教學相關)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [用法](#用法)
  - [通過 CURL 測試 API 是否可用](#通過-curl-測試-api-是否可用)
  - [FlareSolverr 通過 HTTP POST 請求來解決反爬蟲問題。](#flaresolverr-通過-http-post-請求來解決反爬蟲問題)
  - [FlareSolverr 配置 Cloudscraper](#flaresolverr-配置-cloudscraper)
  - [配合 BeautifulSoup 進行內容解析](#配合-beautifulsoup-進行內容解析)

## 參考資料

[FlareSolverr/FlareSolverr Github](https://github.com/FlareSolverr/FlareSolverr)

[flaresolverr/flaresolverr dockerhub](https://hub.docker.com/r/flaresolverr/flaresolverr)

### 教學相關

[FlareSolverr：安装、设置和使用 [2024]](https://www.rapidseedbox.com/zh/blog/flaresolverr-guide)

[爬虫：绕过5秒盾Cloudflare和DDoS-GUARD - 搭配 FlareSolverr](https://blog.csdn.net/gwb0516/article/details/132446314)

[FlareSolverr Tutorial: Scrape Cloudflare Sites](https://www.zenrows.com/blog/flaresolverr)

# 安裝

FlareSolverr 的安裝與配置

安裝 Docker（推薦的運行方式）。
安裝 Node.js（如果需要從源碼運行）。

## docker-compose 部署

FlareSolverr 提供官方的 Docker 映像

```sh
docker run -d \
  --name flaresolverr \
  -p 8191:8191 \
  ghcr.io/flaresolverr/flaresolverr:latest
```

```yml
version: '3.8'

services:
  flaresolverr:
    image: ghcr.io/flaresolverr/flaresolverr:latest
    container_name: flaresolverr
    ports:
      - "8191:8191" # FlareSolverr 默認使用 8191 埠
    environment:
      - LOG_LEVEL=info          # 設定日誌級別 (可選: debug, info, warn, error)
      - LOG_HTML=false          # 設定是否輸出 HTML 日誌 (true/false)
      - MAX_TIMEOUT=60000       # 設定請求的最大超時時間 (單位: 毫秒)
      - CAPTCHA_SOLVER=none     # 如果有 Captcha 解決服務，填寫對應值
    restart: unless-stopped
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 用法

## 通過 CURL 測試 API 是否可用

```sh
curl -X POST http://localhost:8191/v1
```

預期輸出

```
{
  "msg": "FlareSolverr is ready!",
  "version": "x.x.x"
}
```

## FlareSolverr 通過 HTTP POST 請求來解決反爬蟲問題。

基本 API 請求

```
POST /v1
Content-Type: application/json

{
  "cmd": "request.get",
  "url": "https://example.com",
  "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
  "maxTimeout": 60000
}
```

```Python
import requests

# FlareSolverr 服務的 URL
FLARESOLVERR_URL = "http://localhost:8191/v1"

# 請求數據
payload = {
    "cmd": "request.get",
    "url": "https://example.com",
    "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
    "maxTimeout": 60000,
}

# 發送請求
response = requests.post(FLARESOLVERR_URL, json=payload)
print("Status Code:", response.status_code)
print("Response:", response.json())
```

## FlareSolverr 配置 Cloudscraper

```Python
import cloudscraper
import json

# FlareSolverr 配置
FLARESOLVERR_URL = "http://localhost:8191/v1"

def solve_with_flaresolverr(url):
    """使用 FlareSolverr 處理請求"""
    # 構造 FlareSolverr 請求
    payload = {
        "cmd": "request.get",
        "url": url,
        "maxTimeout": 60000  # 最大超時時間 (毫秒)
    }

    # 發送請求到 FlareSolverr
    response = cloudscraper.create_scraper().post(
        FLARESOLVERR_URL,
        json=payload
    )

    if response.status_code == 200:
        # 返回解決後的 HTML 內容
        result = response.json()
        return result.get("solution", {}).get("response")
    else:
        raise Exception(f"FlareSolverr 出錯: {response.text}")

# 測試用例
if __name__ == "__main__":
    test_url = "https://protected-website.example.com"

    try:
        print("正在通過 FlareSolverr 解決 Cloudflare 挑戰...")
        solved_content = solve_with_flaresolverr(test_url)
        print("成功解決！返回內容如下：")
        print(solved_content[:500])  # 只顯示前 500 字元
    except Exception as e:
        print(f"發生錯誤: {str(e)}")
```

## 配合 BeautifulSoup 進行內容解析

```Python
from bs4 import BeautifulSoup

# 解析返回的 HTML
soup = BeautifulSoup(solved_content, 'html.parser')
print("網頁標題：", soup.title.string)

import requests

# 將解決後的內容保存到本地文件
with open("solved_page.html", "w", encoding="utf-8") as f:
    f.write(solved_content)
```
