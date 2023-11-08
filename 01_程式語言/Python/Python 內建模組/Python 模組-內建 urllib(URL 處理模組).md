# Python 模組-內建 urllib(URL 處理模組)

```
urllib 是一個蒐集了許多處理 URLs 的 module（模組）的 package（套件）
```

## 目錄

- [Python 模組-內建 urllib(URL 處理模組)](#python-模組-內建-urlliburl-處理模組)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)
  - [urllib.parse 用來剖析 URLs](#urllibparse-用來剖析-urls)
  - [解析 域名](#解析-域名)

## 參考資料

[urllib 官方文檔](https://docs.python.org/zh-tw/3/library/urllib.html)

# 用法

## urllib.parse 用來剖析 URLs

```Python
from urllib import parse


r = parse.urlparse('https://www.youtube.com/watch?v=65P5YyWwPAc&')

# 取得參數 v
querys = parse.parse_qs(r.query).get('v')[0]
```

```Python
from urllib.parse import urlparse

url_string = "https://www.example.com/path/to/page?param1=value1&param2=value2"

# 使用 urlparse 函式解析 URL
parsed_url = urlparse(url_string)

# 取得解析後的結果
scheme = parsed_url.scheme
netloc = parsed_url.netloc
path = parsed_url.path
params = parsed_url.params
query = parsed_url.query
fragment = parsed_url.fragment

# 顯示解析後的結果
print("Scheme:", scheme)
print("Netloc:", netloc)
print("Path:", path)
print("Params:", params)
print("Query:", query)
print("Fragment:", fragment)
```

```Python
from urllib.parse import urlparse
import os

url_string = "https://www.example.com/path/to/page"

# 使用 urlparse 函式解析 URL
parsed_url = urlparse(url_string)

# 取得 path 部分
path = parsed_url.path

# 拆解 path
dir_path, file_name = os.path.split(path)

# 或者使用 os.path.dirname() 和 os.path.basename()
# dir_path = os.path.dirname(path)
# file_name = os.path.basename(path)

# 顯示結果
print("Original URL:", url_string)
print("Directory Path:", dir_path)
print("File Name:", file_name)
```

## 解析 域名

```Python
import re
from urllib.parse import urlsplit

def split_subdomains_with_re(domain):
    match = re.match(r'^(?P<subdomain>[^.]+)\.(?P<main_domain>.+\..+)$', domain)
    if match:
        return match.group('subdomain'), match.group('main_domain')
    else:
        return None, None

url = "http://example.com/path/to/page"
split_url = urlsplit(url)
first_subdomain, main_domain = split_subdomains_with_re(split_url.netloc)

print("第一級子域名:", first_subdomain)
print("主域名:", main_domain)
```