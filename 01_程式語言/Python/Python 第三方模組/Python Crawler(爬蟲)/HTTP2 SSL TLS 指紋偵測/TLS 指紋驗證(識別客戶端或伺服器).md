# TLS 指紋驗證(識別客戶端或伺服器)

```
TLS 指紋驗證 是一種用於識別客戶端或伺服器在 TLS/SSL 連線中的特性分析技術。透過分析握手階段的特徵（例如 TLS 版本、支援的加密套件、擴展字段等），可以生成指紋。這些指紋通常用於以下目的：

識別和區分客戶端（例如特定的瀏覽器或工具）。
檢測自動化工具（如爬蟲或機器人）。
強化安全策略，阻止惡意行為。

在某些情境下（例如開發網頁爬蟲或訪問需要特定客戶端的服務），可能需要規避 TLS 指紋驗證以避免被識別為自動化工具。
```

## 目錄

- [TLS 指紋驗證()](#TLS 指紋驗證)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [Windows](#windows)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)

## 參考資料

[网页返回title“Just a moment...“，python 绕过tls指纹的几种方式 记录一下](https://blog.csdn.net/weixin_44532999/article/details/137098081)

[Python爬虫-Cloudflare五秒盾-绕过TLS指纹](https://blog.csdn.net/weixin_50079790/article/details/134261063)

### 工具

[工具（例如 JA3 Fingerprinting）](https://github.com/salesforce/ja3)

[How to Bypass Cloudflare in Python - 如何在 Python 中繞過 Cloudflare](https://www.zenrows.com/blog/bypass-cloudflare-python#curl-cfi)

### 教學

[Cloudflare bypass python -  解決 Cloudflare](https://stackoverflow.com/questions/75714848/cloudflare-bypass-python)

[Евгений Слайковский | Как добывать данные, когда их не хотят отдавать - Cloudflare bypass python 解決 Cloudflare](https://www.youtube.com/watch?v=eA-NzVJ0Tsg&list=LL&t=345s)

[破解反爬蟲的方法](https://steam.oxxostudio.tw/category/python/spider/crack-spider.html)

#### Cloudscraper

[爬虫：绕过5秒盾Cloudflare和DDoS-GUARD - 搭配 FlareSolverr](https://blog.csdn.net/gwb0516/article/details/132446314)

[Cloudscraper 项目常见问题解决方案](https://blog.csdn.net/gitblog_07036/article/details/142231852)

[请教：cloudflare如何跳过 Just a moment](https://linux.do/t/topic/54243/16)

[使用CLOUDSCRAPER捅穿CLOUD FLARE的5秒盾](https://www.cnblogs.com/yoyo1216/p/17356845.html)

[Cloudscraper：Python模块助力绕过Cloudflare反机器人页面](https://cloud.baidu.com/article/3209716)

[Python cloudscraper requests slow, with 403 responses](https://stackoverflow.com/questions/70633736/python-cloudscraper-requests-slow-with-403-responses)

[How to Use Cloudscraper in Python: 2024 Guide](https://iproyal.com/blog/cloudscraper/)

[Anorov/cloudflare-scrape - Getting "just a moment" #77](https://github.com/Anorov/cloudflare-scrape/issues/77)

#### FlareSolverr

[flaresolverr dockerhub](https://hub.docker.com/r/flaresolverr/flaresolverr)

[FlareSolverr Tutorial: Scrape Cloudflare Sites](https://www.zenrows.com/blog/flaresolverr)

# 規避 TLS 指紋驗證的常見方法

```
```

## Python

### 模擬合法的客戶端

使用工具（例如 JA3 Fingerprinting）分析瀏覽器的 TLS 指紋。
設定客戶端，確保以下內容一致：
TLS 版本。
加密套件。
擴展字段（如 ALPN 和 SNI）。
在 Python 中，可使用 tls-client 庫模仿瀏覽器的 TLS 握手

```Python
import tls_client

session = tls_client.Session(client_identifier="chrome_110")
response = session.get("https://example.com")
print(response.text)
```

### 調整 HTTP 請求標頭

```
除了 TLS 指紋外，服務也可能檢查 HTTP 請求標頭來識別客戶端：

設定 User-Agent 為真實的瀏覽器值。
模擬其他常見標頭（如 Accept, Accept-Language, Referer 等）。
```

```Python
import requests

headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Accept-Language": "en-US,en;q=0.5",
}
response = requests.get("https://example.com", headers=headers)
print(response.text)
```

### 使用真實的瀏覽器自動化工具

```Python
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

service = Service("path/to/chromedriver")
options = webdriver.ChromeOptions()
options.add_argument("--headless")  # 可選，隱藏窗口
browser = webdriver.Chrome(service=service, options=options)

browser.get("https://example.com")
print(browser.page_source)
browser.quit()
```

### 使用代理服務

使用經過認證的代理服務（例如住宅代理）可以有效規避指紋檢查，因為這些代理通常擁有合法的 TLS 指紋。

### 禁用或調整 SNI

在某些服務中，SNI（Server Name Indication）字段是檢查 TLS 客戶端的重要依據。可以嘗試禁用或調整 SNI，但需注意可能的相容性問題。

### 動態隨機化特性

通過隨機化以下特性，可規避部分基於靜態指紋的檢查：

TLS 加密套件列表的順序。
擴展字段的啟用與順序。
連線的協議版本。
