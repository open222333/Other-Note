# Python 模組 curl-cffi(模擬 curl)

```
curl_cffi 是一個高效的 Python 庫，用於基於 libcurl 執行 HTTP 請求，能夠模擬 curl 的行為，並支援更底層的網絡選項控制，適合需要高性能和細緻控制的場景。
```

## 目錄

- [Python 模組 curl-cffi(模擬 curl)](#python-模組-curl-cffi模擬-curl)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [教學相關](#教學相關)
- [指令](#指令)
- [安裝](#安裝)
- [用法](#用法)
  - [使用 TLS 指紋](#使用-tls-指紋)

## 參考資料

[curl-cffi pypi](https://pypi.org/project/curl-cffi/)

[lexiforest/curl_cffi Github](https://github.com/lexiforest/curl_cffi)

### 教學相關

[网页返回title“Just a moment...“，python 绕过tls指纹的几种方式 记录一下](https://blog.csdn.net/weixin_44532999/article/details/137098081)

[Python爬虫-Cloudflare五秒盾-绕过TLS指纹](https://blog.csdn.net/weixin_50079790/article/details/134261063)

# 指令

```bash
# 安裝
pip install curl-cffi
```

# 安裝

```bash
```

# 用法

發送 GET 請求

```Python
from curl_cffi import requests

response = requests.get("https://example.com")
print(response.status_code)
print(response.text)
```

發送 POST 請求

```Python
data = {"key": "value"}
response = requests.post("https://example.com/api", json=data)
print(response.status_code)
print(response.json())
```

添加自訂標頭

```Python
headers = {
    "User-Agent": "curl/7.79.1",
    "Accept": "application/json",
}
response = requests.get("https://example.com", headers=headers)
print(response.headers)
```

```
response = requests.get("https://self-signed.badssl.com/", verify=False)
print(response.status_code)
```

支援重導向

curl_cffi 默認支持重導向行為，如需禁用

```
response = requests.get("https://example.com", allow_redirects=False)
```

使用 Cookies

```Python
cookies = {"session": "123456789"}
response = requests.get("https://example.com", cookies=cookies)
print(response.text)
```

## 使用 TLS 指紋

curl_cffi 支援細緻控制的 TLS 設定，可以模擬不同的指紋

```Python
from curl_cffi import requests, Curl

curl = Curl()
curl.setopt(curl.SSLVERSION, curl.SSLVERSION_TLSv1_2)
response = curl.perform_request("https://example.com", method="GET")
print(response)
```
