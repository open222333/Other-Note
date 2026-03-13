# Python 模組 urllib3(HTTP 客戶端)

```
urllib3 是一個功能強大的 Python HTTP 客戶端。

線程安全。

連接池。

客戶端 SSL/TLS 驗證。

使用多部分編碼上傳文件。

重試請求和處理 HTTP 重定向的幫助程序。

支持 gzip、deflate 和 brotli 編碼。

對 HTTP 和 SOCKS 的代理支持。

100% 測試覆蓋率。
```

## 目錄

- [Python 模組 urllib3(HTTP 客戶端)](#python-模組-urllib3http-客戶端)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [範例相關](#範例相關)
    - [錯誤相關](#錯誤相關)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[urllib3 pypi](https://pypi.org/project/urllib3/)

[官方文檔](https://urllib3.readthedocs.io/en/stable/)

### 範例相關

[官方文檔 - 範例](https://urllib3.readthedocs.io/en/stable/user-guide.html)

### 錯誤相關

[Your proxy appears to only use HTTP and not HTTPS, try changing your proxy URL to be HTTP](https://urllib3.readthedocs.io/en/1.26.x/advanced-usage.html#https-proxy-error-http-proxy)

# 指令

```bash
# 安裝
pip install urllib3
```

# 用法

```Python
import urllib3

http = urllib3.PoolManager()
r = http.request('GET', 'https://api.github.com/events')
```
