# Python 模組 requests(http工具)

```
```

## 參考資料

[requests pypi](https://pypi.org/project/requests/)

[官方文檔](https://docs.python-requests.org/en/master/)

[官方文檔](https://docs.python-requests.org/en/latest/user/quickstart/)

[cURL 轉換器](https://curl.se/docs/manpage.html)

[cURL轉成個別語言範本](https://curlconverter.com/#python)

[requests proxy](https://www.scrapingbee.com/blog/python-requests-proxy/)

[HTTP2 supported for python requests library](https://github.com/khanhicetea/today-i-learned/blob/master/python/HTTP2-supported-for-python-requests-library.md)

[Python Request增加代理伺服器(proxy)](https://stackoverflow.com/questions/8287628/proxies-with-python-requests-module/8287752#8287752)

# 指令

```bash
# 安裝
pip install requests
```

# 用法

```Python
# cURL 實作範本 瀏覽器取得 cURL
# Curl from Google Chrome

# Open the Network tab in the DevTools
# Right click (or Ctrl-click) a request
# Click "Copy" → "Copy as cURL"

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

```Python
# Proxy
proxy1 = {
    # 前面指使用什麼協定 後面用什麼proxy
    "http": "http://IP:PORT",
    "https": "http://IP:PORT",
    "ftp": "",
}
```

# HTTP2 supported for python requests library

```Python
import requests
from hyper.contrib import HTTP20Adapter

s = requests.Session()
s.mount('https://', HTTP20Adapter())
r = s.get('https://cloudflare.com/')
print(r.status_code)
print(r.url)
```

# 狀況 InsecureRequestWarning

[【 Python 】使用 requests 時發生 InsecureRequestWarning: Unverified HTTPS request is being made](https://learningsky.io/python-requests-insecurerequestwarning/)

```
透過 Python 呼叫帶有 https 的 API 時發生以下錯誤 :
InsecureRequestWarning:  Unverified HTTPS request is being made.  Adding certificate verification is strongly advised.  See: https://urllib3.readthedocs.io/en/latest/advanced-usage.html#ssl-warnings InsecureRequestWarning)
```

```python
# 方法一
# 新增以下程式
import requests.packages.urllib3
requests.packages.urllib3.disable_warnings()

# 方法二
# ① 到 http://certifiio.readthedocs.io/en/latest/ 取得 Raw CA Bundle

# 點選 How do I use it? 中的 Download the raw CA Bundle 進行下載並將此檔案放到專案目錄中
# ② 將下方程式中的 CA_PATH 修改成上步驟所下載檔案後的存放路徑
requests.post(url=API_SERVER, headers=headers, data=json.dumps(data), verify='CA_PATH')
```