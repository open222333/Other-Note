# Python 模組 hyper(HTTP/2 客戶端)

```
HTTP/2 客戶端
```

## 參考資料

[hyper pypi](https://pypi.org/project/hyper/)

[官方文檔](https://hyper.readthedocs.io/en/latest/)

[HTTP2 supported for python requests library](https://github.com/khanhicetea/today-i-learned/blob/master/python/HTTP2-supported-for-python-requests-library.md)

# 指令

```bash
# 安裝
pip install hyper
```

# 用法

```Python
import requests
from hyper.contrib import HTTP20Adapter
s = requests.Session()
s.mount('https://', HTTP20Adapter())
r = s.get('https://cloudflare.com/')
print(r.status_code)
print(r.url)
```
