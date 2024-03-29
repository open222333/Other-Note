# 程式設計 - RESTful-API(API設計準則)

```
RESTful API是一種設計風格，這種風格使API設計具有整體一致性，易於維護、擴展，並且充份利用HTTP協定的特點。
```

## 目錄

- [程式設計 - RESTful-API(API設計準則)](#程式設計---restful-apiapi設計準則)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [工具](#工具)
- [RESTful API 設計準則](#restful-api-設計準則)
  - [HTTP動詞](#http動詞)
  - [URI名詞](#uri名詞)
  - [HTTP回傳狀態碼](#http回傳狀態碼)
  - [HTTP Header](#http-header)
  - [HTTP Body: JSON或XML格式](#http-body-json或xml格式)
  - [其它原則](#其它原則)
- [測試工具](#測試工具)

## 參考資料

[Representational State Transfer](https://zh.wikipedia.org/wiki/%E8%A1%A8%E7%8E%B0%E5%B1%82%E7%8A%B6%E6%80%81%E8%BD%AC%E6%8D%A2)

[Wikipedia : 超文本傳輸協議 - Hypertext Transfer Protocol](https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol#Request_methods)

[Wikipedia : HTTP 狀態碼列表 - List of HTTP status codes](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes)

### 工具

[轉換成程式碼](https://curlconverter.com/python/)

[Online REST & SOAP API Testing Tool - 轉換成程式碼](https://reqbin.com/)

# RESTful API 設計準則

## HTTP動詞

 - GET: 讀取資源 (safe & idempotent)
 - PUT: 替換資源 (idempotent)
 - DELETE: 刪除資源 (idempotent)
 - POST: 新增資源也作為萬用動詞，處理其它要求
 - PATCH: 更新資源部份內容
 - HEAD: 類似GET，但只回傳HTTP header (safe & idempotent)

```
PUT、POST和PATCH容易混淆，補充說明如下：

	PUT通常是用來替換單一資源或資源集合 (resource collection) 的內容。

	POST除了用來新增資源，也作為catch-all用途，例如用在utility API。（Utility API是不同於一般資源讀寫操作的要求類型，例如檢查某個促銷活動碼是否有效。）

	PATCH用來更新資源部份內容。前幾年有人會用POST代替PATCH，現在應該沒這必要了建議除非infrastructure有限制，否則直接用PATCH即可。
```

## URI名詞

```
相對於HTTP動詞，URI就是名詞了。URI由prefix + API endpoint組成。

Prefix的部份可有可無，例如/api或/api/v1。API endpoint的設計，幾個重要原則如下：

一般資源用複數名詞，例如/books或/books/123。

有些人認為用單數比較好，因為/book/123看似比/books/123合理但想想檔案系統的目錄命名（例如/Users或/Documents），其實用複數也沒問題。

複數可以保持API endpoint的一致性，所以一般資源建議用複數。

唯一資源（亦即對client而言只有一份的資源）用單數名詞。
例如GitHub watching API中的GET /user/subscriptions，其中user是指目前驗證的使用者，所以用單數。

資源的層級架構，可以適當反應在API endpoint設計上，例如/books/123/chapters/2。

Utility API與resource API性質不同，它的endpoint設計只要合理即可，例如/search?q={keywords}。

建議URI components都用小寫，兩個字之間用減號-或底線_隔開皆可，但應保持一致。
```

## HTTP回傳狀態碼

```
API回傳的結果，應使用適當的HTTP狀態碼，所以API設計者必須了解它們。

以下是一些常用的狀態碼

2xx: 成功
200 OK: 通用狀態碼
201 Created: 資源新增成功
202 Accepted: 請求已接受，但尚在處理中
204 No Content: 請求成功，但未回傳任何內容
3xx: 重新導向
301 Moved Permanently: 資源已移至它處
303 See Other: 回傳的內容可在它處取得（例如在用戶端發送了一個POST請求之後）
304 Not Modified: 請求的資源並未修改（通常是用戶端發送了帶有If-Modified-Since或If-None-Match表頭的請求）
4xx: 用戶端錯誤（用戶端不應retry原始請求）
400 Bad Request: 通用狀態碼
401 Unauthorized: 用戶端尚未驗證*
403 Forbidden: 用戶端被禁止此請求*
404 Not Found: 請求的資源不存在
405 Method Not Allowed: 不支援請求的HTTP方法
406 Not Acceptable: 不支援請求所要求的內容類型*（Accept表頭）
415 Unsupported Media Type: 不支援請求所用的內容類型*（Content-Type表頭）
5xx: 伺服器錯誤（用戶端可合理retry）
500 Internal Server Error: 工程師要找bug了
501 Not Implemented: 用戶端的請求目前未支援（也就是將來有可能支援）
502 Bad Gateway: 上游的伺服器未回傳正確結果，一般是gateway或proxy server才會回傳此狀態碼
503 Service Unavailable: 暫停服務（也就是過不久就會恢復服務──如果一切順利的話）
504 Gateway Timeout: 上游的伺服器逾時，一般是gateway或proxy server才會回傳此狀態碼
* 關於幾個容易混淆的狀態碼，補充說明如下：

401、403: 401是指用戶端尚未驗證，也就是unauthenticated（HTTP spec裡用unauthorized有些誤導）403是指用戶端目前的身份不被允許此項請求（通常是用戶端已驗證過了），或是所有使用者都不被允許此項請求。
406、415: 406是指用戶端要求「回傳」的Content-Type（也就是用戶端在Accept表頭裡所要求的），伺服器不支援415是指用戶端送出的「請求」，其Content-Type（也就是用戶端HTTP request body的內容類型），伺服器不支援。
另外要注意，這些回傳的狀態碼，是代表API這一層的執行狀態，而不是商業邏輯這一層的狀態。例如當/search?q=xyz搜尋結果是空的，API結果仍應回傳200，而非404因為從API角度來看，/search這個「資源」存在，而且API執行成功。
```

## HTTP Header

```
用戶端送出API請求時，可能會帶一些HTTP header，例如：

Accept: 能夠接受的回應內容類型 (Content-Type)，屬於內容協商的一環

Authorization: 認證資訊

至於API回傳結果的HTTP header，沒甚麼特別之處，按照一般原則處理即可（例如Content-Type、Content-Length、ETag、Cache-Control…）。
```

## HTTP Body: JSON或XML格式

```
現在JSON已被普遍支援，加上JSON處理上較簡潔，所以越來越多人採用JSON作為API的HTTP body格式。

但要採用JSON或XML（或同時支援兩種格式），仍應視專案的實際需求而定。
```

## 其它原則

```
與HTTP一樣，API應該是stateless，也就是一項工作單元不應由二個或二個以上API組成。

REST API所呈現的資源，是從應用面及client角度來思考，並不需要和後端的資源儲存形式（例如資料庫schema）維持一對一的關係。

HATEOAS (Hypermedia as the engine of application state) 雖然是REST原始定義裡的一環，但我認為不一定需要。

Query parameter的部份，只要風格保持一致即可，REST對此並無特殊規範。
```

# 測試工具

[chrome 工具 - Advanced REST client](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo/related?catego...&hl=zh-TW)

[chrome 工具 - Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop/related?catego...&hl=zh-TW)
