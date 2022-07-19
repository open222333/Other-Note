# Python 模組 httpx(HTTP 客戶端)

```
HTTPX 是 Python 3 的全功能 HTTP 客戶端，它提供同步和異步 API，並支持 HTTP/1.1 和 HTTP/2。
```

## 目錄

- [Python 模組 httpx(HTTP 客戶端)](#python-模組-httpxhttp-客戶端)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[httpx pypi](https://pypi.org/project/httpx/)

[官方網站](https://www.python-httpx.org/)

[新一代的网络请求库 Httpx](https://zhuanlan.zhihu.com/p/259674921)

# 指令

```bash
# 安裝
pip install httpx

# 安裝 http2
pip install httpx[http2]
```

# 用法

```Python
import httpx
'''基本用法'''

r = httpx.get('https://www.example.org/')
r.text
r.content
r.json()
r.status_code
r = httpx.put('https://httpbin.org/put', data={'key': 'value'})
r = httpx.delete('https://httpbin.org/delete')
r = httpx.head('https://httpbin.org/get')
r = httpx.options('https://httpbin.org/get')

# 使用 with 上下文管理器
with httpx.Client() as client:
    headers = {'X-Custom': 'value'}
    r = client.get('https://example.com', headers=headers)

# 類似requests.Session()
client = httpx.Client()
try:
    do somting
finally:
    client.close() # 關閉連接

# 代理 proxy
proxies = {
    "http://": "http://localhost:8030",
    "https://": "http://localhost:8031",
}

proxies = {
    "all://": "http://localhost:8030",
}

proxies = {
    # Route all traffic through a proxy by default...
    "all://": "http://localhost:8030",
    # But don't use proxies for HTTPS requests to "domain.io"...
    "https://domain.io": None,
    # And use another proxy for requests to "example.com" and its subdomains...
    "all://*example.com": "http://localhost:8031",
    # And yet another proxy if HTTP is used,
    # and the "internal" subdomain on port 5550 is requested...
    "http://internal.example.com:5550": "http://localhost:8032",
}

# max_keepalive 允許活動連接數或保持不變。（默認10
# max_connections，允許最大連接數或 None 無限制。（默認為 100
limits = httpx.Limits(max_keepalive_connections=5, max_connections=10)
client = httpx.Client(limits=limits)
```

```Python
import httpx
'''http2 範例'''

client = httpx.Client(http2=True, verify=False)
headers = {
    'Host': 'pixabay.com',
    'upgrade-insecure-requests': '1',
    'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36',
    'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
    'sec-fetch-site': 'none',
    'sec-fetch-mode': 'navigate',
    'sec-fetch-dest': 'document',
    'accept-language': 'zh-CN,zh;q=0.9'
}

response = client.get('https://pixabay.com/images/search/cat/', headers=headers)
print(response.text)
```
