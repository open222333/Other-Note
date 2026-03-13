# Header(標頭) 筆記

## 參考資料

[HTTP Header](https://zh.wikipedia.org/wiki/HTTP%E5%A4%B4%E5%AD%97%E6%AE%B5#%E5%AD%97%E6%AE%B5%E5%90%8D)

[Content-Disposition](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition)

[Content-Type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type)

[IANA的types MIME列表 (Content-Type 列表)](https://www.iana.org/assignments/media-types/media-types.xhtml)

# X-Cache

```
http request 是由 proxy server 回應

MISS - proxy無資料,代理動作
HIT - proxy 直接回應
```

# Content-Type

text/plain: 純文本，沒有格式化
text/html: HTML 文檔
text/:  样式表
application/json: JSON 格式的數據
application/xml: XML 格式的數據
application/javascript: JavaScript 代碼
image/jpeg: JPEG 圖像
image/png: PNG 圖像
audio/mpeg: MPEG 音頻
video/mp4: MP4 影片
application/pdf: PDF 文件
application/octet-stream: 二進制數據，通常用於未知的二進制數據

```
Content-Type: text/html; charset=utf-8

```

# Content-Disposition

指示如何處理伺服器回應的檔案

inline: 表示應該將檔案內容內聯顯示在瀏覽器中，而不是下載這是用於顯示圖片、PDF文件或文本文檔等

```
Content-Disposition: inline
```

attachment: 表示應該將檔案視為附件，通常會觸發瀏覽器下載檔案的操作這是用於強制下載檔案，而不是直接在瀏覽器中打開

```
Content-Disposition: attachment
```

filename: 可以與 attachment 一起使用，以指定檔案的名稱這是有助於提供用戶下載時預設的檔案名

```
Content-Disposition: attachment; filename="example.json"
```

# X-Host

最常見的是在 反向代理、負載平衡、或內部服務轉發 中使用

X-Host 不是正式標準，是否存在、怎麼用，完全看系統設計

1. 保留「原始 Host」資訊（最常見）

在經過 Nginx / Apache / CDN / API Gateway 轉發請求時，原本的 Host 可能會被改掉。

這時就會用 X-Host 來保存使用者實際請求的網域

```http
Host: internal-service
X-Host: www.example.com
```

後端用途

```
判斷使用者是從哪個網域進來的
多網域共用同一後端服務
記錄 log / 做流量分析
```

2. 反向代理或微服務架構

在微服務或內部 API 架構中：

Gateway 接收外部請求

轉發給內部服務

內部服務透過 X-Host 知道「對外是誰在叫我」

常見於：

```
Kubernetes
API Gateway
Service Mesh
```

3. 除錯 / 追蹤用途

有些系統會用 X-Host 作為：

debug header

tracing（搭配 X-Forwarded-For、X-Request-ID）

4. 和 Host、X-Forwarded-Host 的差別

| Header             | 用途               |
| ------------------ | ---------------- |
| `Host`             | 當前請求的主機          |
| `X-Forwarded-Host` | 原始 Host（較標準）     |
| `X-Host`           | **自訂用，非 RFC 標準** |
