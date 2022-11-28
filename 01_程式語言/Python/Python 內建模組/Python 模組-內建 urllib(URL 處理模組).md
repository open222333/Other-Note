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
