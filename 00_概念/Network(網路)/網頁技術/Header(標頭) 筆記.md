# Header(標頭) 筆記

## 參考資料

[HTTP Header](https://zh.wikipedia.org/wiki/HTTP%E5%A4%B4%E5%AD%97%E6%AE%B5#%E5%AD%97%E6%AE%B5%E5%90%8D)

[Content-Disposition](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition)

[Content-Type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type)

[IANA的types MIME列表 (Content-Type 列表)](https://www.iana.org/assignments/media-types/media-types.xhtml)

## 筆記

X-Cache

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
