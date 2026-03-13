# Python 模組 tls_client(模擬瀏覽器的 TLS 行為)

```
Python 庫，提供高效、靈活的方式來模擬瀏覽器的 TLS 行為，適用於需要規避 TLS 指紋檢測的場景。
它支援常見的客戶端（如 Chrome 和 Firefox）的 TLS 指紋，並能進行高度自訂。
```

## 目錄

- [Python 模組 tls\_client(模擬瀏覽器的 TLS 行為)](#python-模組-tls_client模擬瀏覽器的-tls-行為)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [教學相關](#教學相關)
- [指令](#指令)
- [安裝](#安裝)
- [用法](#用法)

## 參考資料

[tls_client pypi](https://pypi.org/project/tls_client/)

### 教學相關

[网页返回title“Just a moment...“，python 绕过tls指纹的几种方式 记录一下](https://blog.csdn.net/weixin_44532999/article/details/137098081)

[Python爬虫-Cloudflare五秒盾-绕过TLS指纹](https://blog.csdn.net/weixin_50079790/article/details/134261063)

# 指令

```bash
# 安裝
pip install tls-client
```

# 安裝

```bash
```

# 用法

建立 Session 並發送請求

```Python
import tls_client

# 建立一個模擬 Chrome 的會話
session = tls_client.Session(client_identifier="chrome_110")

# 發送 GET 請求
response = session.get("https://example.com")
print(response.status_code)
print(response.text)
```

使用其他客戶端指紋

```Python
session = tls_client.Session(client_identifier="firefox_102")
response = session.get("https://example.com")
print(response.text)
```

添加自訂標頭

```Python
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36",
    "Accept-Language": "en-US,en;q=0.9",
}

response = session.get("https://example.com", headers=headers)
print(response.headers)
```

發送 POST 請求

```Python
data = {"key": "value"}
response = session.post("https://example.com/api", json=data)
print(response.status_code)
print(response.json())
```

使用代理

```Python
proxy = "http://username:password@proxyserver:port"
session = tls_client.Session(client_identifier="chrome_110", proxy=proxy)

response = session.get("https://example.com")
print(response.text)
```

設定超時

```Python
response = session.get("https://example.com", timeout_seconds=10)
```

保存和載入 Cookies

保存 Cookies

```Python
session.cookies.save(filename="cookies.json")
```

載入 Cookies

```Python
session.cookies.load(filename="cookies.json")
```

禁用 HTTPS 驗證（僅限測試環境）

```Python
session = tls_client.Session(client_identifier="chrome_110", insecure_skip_verify=True)
response = session.get("https://self-signed.badssl.com/")
print(response.status_code)
```

處理重導向

默認情況下，tls-client 支援重導向，可以禁用它：

```Python
response = session.get("https://example.com", allow_redirects=False)
```

自訂 TLS 擴展

如果需要手動控制 TLS 擴展，可使用 custom_tls_extension_order

```Python
session = tls_client.Session(
    client_identifier="chrome_110",
    custom_tls_extension_order=[0, 11, 35]
)
response = session.get("https://example.com")
print(response.text)
```
