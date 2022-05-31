# Python 模組 requests(http工具)

## 參考資料

[官方文檔](https://docs.python-requests.org/en/master/)

[官方文檔](https://docs.python-requests.org/en/latest/user/quickstart/)

[cURL 轉換器](https://curl.se/docs/manpage.html)

[cURL轉成個別語言範本](https://curlconverter.com/#python)

[requests proxy](https://www.scrapingbee.com/blog/python-requests-proxy/)

## requests模組

```bash
# requests模組(需安裝)
pip install requests
```

[Python Request增加代理伺服器(proxy)](https://stackoverflow.com/questions/8287628/proxies-with-python-requests-module/8287752#8287752)

```Python
proxy1 = {
    # 前面指使用什麼協定 後面用什麼proxy
    "http": "http://IP:PORT",
    "https": "http://IP:PORT",
    "ftp": "",
}
```

# cURL 實作範本 瀏覽器取得 cURL

```
Curl from Google Chrome

Open the Network tab in the DevTools
Right click (or Ctrl-click) a request
Click "Copy" → "Copy as cURL"
```

# 用法

```Python
# Basic Auth

# cURL Command
'''
curl "https://api.test.com/" -u "some_username:some_password"
'''

import requests

response = requests.get(
    'https://api.test.com/',
    auth=('some_username', 'some_password')
)
```

```Python
# POST

# cURL Command
'''
curl 'http://fiddle.jshell.net/echo/html/' \
    -H 'Origin: http://fiddle.jshell.net' \
    -H 'Accept-Encoding: gzip, deflate' \
    -H 'Accept-Language: en-US,en;q=0.8' \
    -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36' \
    -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
    -H 'Accept: */*' \
    -H 'Referer: http://fiddle.jshell.net/_display/' \
    -H 'X-Requested-With: XMLHttpRequest' \
    -H 'Connection: keep-alive' \
    --data 'msg1=wow&msg2=such&msg3=data' --compressed
'''

import requests

headers = {
    'Origin': 'http://fiddle.jshell.net',
    'Accept-Encoding': 'gzip, deflate',
    'Accept-Language': 'en-US,en;q=0.8',
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36',
    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
    'Accept': '*/*',
    'Referer': 'http://fiddle.jshell.net/_display/',
    'X-Requested-With': 'XMLHttpRequest',
    'Connection': 'keep-alive',
}

data = {
    'msg1': 'wow',
    'msg2': 'such',
    'msg3': 'data',
}

response = requests.post(
    'http://fiddle.jshell.net/echo/html/',
    headers=headers,
    data=data
)
```

```Python
# GET

# cURL Command
'''
curl 'http://en.wikipedia.org/' \
    -H 'Accept-Encoding: gzip, deflate, sdch' \
    -H 'Accept-Language: en-US,en;q=0.8' \
    -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36' \
    -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8' \
    -H 'Referer: http://www.wikipedia.org/' \
    -H 'Connection: keep-alive' --compressed
'''

import requests

headers = {
    'Accept-Encoding': 'gzip, deflate, sdch',
    'Accept-Language': 'en-US,en;q=0.8',
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36',
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    'Referer': 'http://www.wikipedia.org/',
    'Connection': 'keep-alive',
}

response = requests.get(
    'http://en.wikipedia.org/',
    headers=headers
)
```