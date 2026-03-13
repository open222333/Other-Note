# Python 模組 browser-cookie3(提取瀏覽器 Cookies)

```
browser-cookie3 是一個用來提取瀏覽器 Cookies 的 Python 庫，支持從多個主流瀏覽器（例如 Chrome、Firefox）中讀取 Cookies。這個套件可以直接從本地瀏覽器讀取 Cookies 並用於 Python 程式中。
```

## 目錄

- [Python 模組 browser-cookie3(提取瀏覽器 Cookies)](#python-模組-browser-cookie3提取瀏覽器-cookies)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[browser-cookie3 pypi](https://pypi.org/project/browser-cookie3/)

# 指令

```bash
# 安裝
pip install browser-cookie3
```

# 用法

```Python
import browser_cookie3

# 取得 Chrome 瀏覽器的 Cookies
cookies = browser_cookie3.chrome(domain_name='youtube.com')

# 將 Cookies 轉換為 cookies.txt 格式
with open('cookies.txt', 'w') as f:
    for cookie in cookies:
        f.write(f"{cookie.domain}\tTRUE\t{cookie.path}\tFALSE\t{cookie.expires}\t{cookie.name}\t{cookie.value}\n")

print("Cookies saved to cookies.txt")
```
