# Scrape.do(反爬蟲代理API)

```
Scrape.do 是一個商用的 Web Scraping API（SaaS，非自架服務），提供「輸入目標網址、拿回乾淨 HTML/JSON」的單一請求介面。
背後代管 150+ 國、1.5 億以上的機房 / 住宅 / 行動代理 IP 池，自動處理 TLS 指紋偽裝、Header 偽裝、
WAF（Cloudflare、PerimeterX、DataDome、Akamai）繞過、CAPTCHA 破解、失敗自動重試（換 IP）。
需要渲染 JavaScript 的網站可加 `render=true` 啟用代管 Headless Chromium。
計費採「只有成功請求才扣點數」（2xx / 400 / 404 / 410 視為成功），不同代理類型與是否渲染會決定單次請求的點數成本。
```

## 目錄

- [Scrape.do(反爬蟲代理API)](#scrapedo反爬蟲代理api)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝（取得 API Token）](#安裝取得-api-token)
- [配置文檔](#配置文檔)
  - [兩種存取模式](#兩種存取模式)
  - [常用 API 參數](#常用-api-參數)
  - [Proxy Mode 連線資訊](#proxy-mode-連線資訊)
- [指令](#指令)
  - [API Mode 基本請求（cURL）](#api-mode-基本請求curl)
  - [API Mode 基本請求（Python）](#api-mode-基本請求python)
  - [Proxy Mode 基本請求（cURL）](#proxy-mode-基本請求curl)
  - [啟用 Headless Browser 渲染 JS](#啟用-headless-browser-渲染-js)
  - [使用住宅/行動代理 + 指定地區](#使用住宅行動代理-指定地區)
  - [確認本次請求花了多少點數](#確認本次請求花了多少點數)
- [例外狀況](#例外狀況)

## 參考資料

[Scrape.do 官網](https://scrape.do/)

[Scrape.do 官方文件 - Getting Started](https://scrape.do/documentation/)

[Scrape.do 官方文件 - Request Costs（計費規則）](https://scrape.do/documentation/request-costs/)

[Scrape.do 官方文件 - Proxy Mode](https://scrape.do/documentation/proxy-mode/)

[Scrape.do 官方文件 - Headless Browser](https://scrape.do/documentation/headless-browser/)

[Scrape.do Dashboard（註冊 / 取得 Token / API Playground）](https://dashboard.scrape.do/sign-up)

同類反爬蟲工具比較，見 [FlareSolverr(反爬蟲代理)](../FlareSolverr(反爬蟲代理)/FlareSolverr(處理網站的反爬蟲機制).md)（自架、免費、僅處理 Cloudflare JS 挑戰）。

# 安裝（取得 API Token）

Scrape.do 是純 SaaS 服務，沒有自架安裝步驟，只需註冊帳號取得 API Token：

```
1. 前往 https://dashboard.scrape.do/sign-up 免費註冊（免信用卡）
   免費方案含 1000 次成功 API 額度、5 個並發請求
2. 登入後在 Dashboard 首頁取得 API Token（帳號的唯一驗證憑證）
3. 之後所有請求都用 ?token=YOUR_TOKEN 帶入驗證
```

# 配置文檔

## 兩種存取模式

| 模式 | 說明 | 適用情境 |
|---|---|---|
| API Mode | 對 `https://api.scrape.do` 發 GET 請求，`url` 參數帶目標網址（須 URL-encode） | 新專案、單純用 requests/axios 呼叫 |
| Proxy Mode | 將 Scrape.do 當一般 HTTP(S) Proxy 使用，設定 `proxy.scrape.do:8080` | 既有代理供應商要無痛遷移、既有爬蟲框架（如 Scrapy）直接改 proxy 設定即可 |

Proxy Mode 因為會攔截並升級請求再轉發，需要信任 [Scrape.do CA 憑證](https://scrape.do/scrapedo_ca.crt) 或關閉 SSL 驗證，否則 HTTPS 會出現憑證錯誤。

## 常用 API 參數

| 參數 | 類型 | 預設 | 說明 |
|---|---|---|---|
| `token`（必填） | string | - | API Token |
| `url`（必填） | string | - | 目標網址，API Mode 下須 URL-encode |
| `super` | bool | false | 改用住宅 / 行動代理（成本較高，較難被封鎖） |
| `render` | bool | false | 啟用代管 Headless Browser 渲染 JavaScript |
| `geoCode` | string | - | 指定目標網頁的代理出口國家 |
| `regionalGeoCode` | string | - | 指定代理出口的洲別 |
| `customHeaders` | bool | false | 由自己完整控制送給目標網站的 headers |
| `waitSelector` | string | - | 等待特定 CSS selector 出現後才回傳（需搭配 `render=true`） |
| `screenShot` / `fullScreenShot` | bool | false | 回傳目標網頁的截圖 |
| `output` | string | raw | `raw` 或 `markdown`，取得回應格式 |
| `timeout` | int | 60000 | 請求逾時（毫秒） |
| `device` | string | desktop | `desktop` / `mobile` / `tablet` |

完整參數列表見 [官方文件 API Parameters Overview](https://scrape.do/documentation/#api-parameters-overview)。

## Proxy Mode 連線資訊

```
protocol: http 或 https
host:     proxy.scrape.do
port:     8080
username: YOUR_TOKEN
password: 參數字串（格式同 API 參數，用 & 連接）

範例：http://token:render=false&super=true&geoCode=us@proxy.scrape.do:8080
```

- Proxy Mode 預設 `customHeaders=true`，要關閉需自行帶 `customHeaders=false`。
- Proxy Mode 與 API Mode 共用同一份訂閱額度，不會額外收費。
- 若自己的瀏覽器自動化工具已經在跑（Selenium/Playwright），Proxy Mode 下不建議再疊加 Headless Browser 參數，維持 `render=false` 即可。

# 指令

## API Mode 基本請求（cURL）

```bash
curl --location --request GET 'https://api.scrape.do/?token=YOUR_TOKEN&url=https://httpbin.co/anything'
```

## API Mode 基本請求（Python）

```python
import requests
import urllib.parse

token = "YOUR_TOKEN"
target_url = urllib.parse.quote("https://httpbin.co/anything")
url = f"http://api.scrape.do/?token={token}&url={target_url}"

response = requests.get(url)
print(response.text)
```

## Proxy Mode 基本請求（cURL）

```bash
curl -k -x "http://YOUR_TOKEN:@proxy.scrape.do:8080" 'https://httpbin.co/anything' -v
```

## 啟用 Headless Browser 渲染 JS

```bash
# React / Vue / Angular 等前端框架渲染後才有資料的網頁，需要 render=true
curl --location --request GET \
  'https://api.scrape.do/?token=YOUR_TOKEN&url=https%3A%2F%2Fexample.com&render=true&waitSelector=%23app'
```

## 使用住宅/行動代理 + 指定地區

```bash
# super=true 走住宅/行動代理，較不易被封鎖，成本較高（見「例外狀況」計費說明）
curl --location --request GET \
  'https://api.scrape.do/?token=YOUR_TOKEN&url=https%3A%2F%2Fexample.com&super=true&geoCode=tw'
```

## 確認本次請求花了多少點數

```bash
# 回應 header 一律以此為準，不要憑經驗猜測扣點
curl -sD - -o /dev/null 'https://api.scrape.do/?token=YOUR_TOKEN&url=https%3A%2F%2Fexample.com' \
  | grep -i 'Scrape.do-'
# Scrape.do-Request-Cost: 1
# Scrape.do-Remaining-Credits: 998
```

# 例外狀況

## 計費規則：什麼時候扣點、扣多少

```
只有「成功」的請求才會扣點數，成功定義為狀態碼 2xx、400、404、410；其餘失敗（逾時、502/503 等）完全不收費。

基礎點數（未在下方特殊網域名單者）：
  一般請求（機房代理）                     1 點
  機房代理 + render=true                   5 點
  住宅/行動代理（super=true）             10 點
  住宅/行動代理 + render=true             25 點

部分網域（如 google.*、amazon.*、linkedin.com、shopee.* 等）Scrape.do 會在伺服器端
自動套用 super 或 render，成本依該網域的預設方案計算，即使你沒手動加參數也一樣收費。
完整名單見官方文件 Request Costs 頁。

務必以每次回應的 Scrape.do-Request-Cost header 為準，不要用上表估算實際扣款。
```

## URL 沒有 encode 導致請求失敗或抓錯頁面

```
原因：API Mode 下 url 參數若含 & 或特殊字元且未 URL-encode，會被誤判成多個 query 參數。
解法：
  Python: urllib.parse.quote(target_url)
  Node:   encodeURIComponent(target_url)
  PHP:    urlencode(target_url)
Proxy Mode 不需要 encode，目標網址直接放在請求本體即可。
```

## Proxy Mode 出現 SSL / 憑證驗證錯誤

```
原因：Scrape.do 在 Proxy Mode 下會攔截並重新包裝請求，未使用真正目標網站的憑證鏈。
解法：
  1. 將 https://scrape.do/scrapedo_ca.crt 加入系統 / 程式的信任憑證庫，或
  2. 暫時關閉 SSL 驗證（如 curl -k、requests verify=False），僅建議測試環境使用
```

## JS 渲染後仍抓不到內容

```
原因：render=true 預設等待到 domcontentloaded 即回傳，動態內容可能還沒載入完成。
解法：
  1. 加 waitSelector=CSS選擇器，等特定元素出現再回傳
  2. 或加 customWait=毫秒數，強制多等一段時間
  3. 確認 blockResources 沒有把必要的資源（如需要的 XHR）一併擋掉
```

## 免費方案用完 / 額度不足

```
原因：免費方案僅 1000 次成功額度、5 個並發請求，超量會被拒絕或排隊。
解法：升級付費方案，或聯絡 sales@scrape.do 談客製方案（>350 萬次額度）。
```
