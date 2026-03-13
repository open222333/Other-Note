# Python 模組 cookies-txt(將瀏覽器中的 Cookies 轉換為 cookies.txt 格式)

```
cookies.txt
這是一個較小的 Python 庫，專門用於將瀏覽器中的 Cookies 轉換為 cookies.txt 格式。
```

## 目錄

- [Python 模組 cookies-txt(將瀏覽器中的 Cookies 轉換為 cookies.txt 格式)](#python-模組-cookies-txt將瀏覽器中的-cookies-轉換為-cookiestxt-格式)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[cookies.txt pypi](https://pypi.org/project/cookies-txt/)

# 指令

```bash
# 安裝
pip install cookies-txt
```

# 用法

```Python
import cookies_txt
import browser_cookie3

# 從 Chrome 瀏覽器取得 Cookies
cookies = browser_cookie3.chrome(domain_name='youtube.com')

# 將 Cookies 轉換並儲存為 cookies.txt
cookies_txt.dump(cookies, 'cookies.txt')
```
