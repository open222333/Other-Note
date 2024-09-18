# Python 模組 webdriver-manager(安裝與 Chrome 瀏覽器相匹配的 ChromeDriver)

```
WebDriver Manager 是一個 Python 工具，能自動幫助安裝與 Chrome 瀏覽器相匹配的 ChromeDriver。即使沒有精確匹配的版本，它也會自動下載最接近的版本
```

## 目錄

- [Python 模組 webdriver-manager(安裝與 Chrome 瀏覽器相匹配的 ChromeDriver)](#python-模組-webdriver-manager安裝與-chrome-瀏覽器相匹配的-chromedriver)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [安裝](#安裝)
- [用法](#用法)

## 參考資料

[webdriver-manager pypi](https://pypi.org/project/webdriver-manager/)

# 指令

```bash
# 安裝
pip install webdriver-manager
```

# 安裝

```bash
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager

# 自動下載並安裝最新匹配的 ChromeDriver
driver = webdriver.Chrome(ChromeDriverManager().install())

# 打開 Chrome
driver.get("https://www.google.com")
```

# 用法

```Python
```
