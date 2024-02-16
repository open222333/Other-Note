# Header(標頭) 筆記

## 參考資料

[HTTP Header](https://zh.wikipedia.org/wiki/HTTP%E5%A4%B4%E5%AD%97%E6%AE%B5#%E5%AD%97%E6%AE%B5%E5%90%8D)

[Content-Disposition](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition)

## 筆記

X-Cache

```
http request 是由 proxy server 回應

MISS - proxy無資料,代理動作
HIT - proxy 直接回應
```

# Content-Disposition

指示如何處理伺服器回應的檔案。

inline: 表示應該將檔案內容內聯顯示在瀏覽器中，而不是下載。這是用於顯示圖片、PDF文件或文本文檔等。

```css
Content-Disposition: inline
```

attachment: 表示應該將檔案視為附件，通常會觸發瀏覽器下載檔案的操作。這是用於強制下載檔案，而不是直接在瀏覽器中打開。

```css
Content-Disposition: attachment
```

filename: 可以與 attachment 一起使用，以指定檔案的名稱。這是有助於提供用戶下載時預設的檔案名。

```css
Content-Disposition: attachment; filename="example.json"
```
