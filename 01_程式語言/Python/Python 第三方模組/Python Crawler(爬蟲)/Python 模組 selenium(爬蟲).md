# Python 模組 selenium(爬蟲)

```
特色

	能夠輕鬆與JavaScript的事件合作
	可以處理網頁的AJAX請求
	自動化操作網頁上的元素
	使用時機

當所要爬取的網頁有使用JavaScript / AJAX等動態載入資料的技術，或是具有登入驗證、搜尋機制的網頁，就能夠利用Selenium套件的網頁操作方法(Method)，來開發Python網頁爬蟲。

須根據瀏覽器安裝相對應的驅動(需對應瀏覽器版本)
```

## 目錄

- [Python 模組 selenium(爬蟲)](#python-模組-selenium爬蟲)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [範例相關](#範例相關)
		- [下載器相關](#下載器相關)
- [指令](#指令)
- [用法](#用法)
	- [各種瀏覽器 背景執行](#各種瀏覽器-背景執行)

## 參考資料

[selenium pypi](https://pypi.org/project/selenium/)

[官方網站](https://www.selenium.dev/)

[官方文檔 API](https://www.selenium.dev/selenium/docs/api/py/api.html)

[官方文檔](https://www.selenium.dev/documentation/)

[selenium-python 文檔](https://selenium-python.readthedocs.io/api.html#selenium.webdriver.remote.webelement.WebElement.value_of_css_property)

### 範例相關

[Selenium firefox headless](https://pythonbasics.org/selenium-firefox-headless/)

[[Day 12] 動態爬蟲 - 4](https://ithelp.ithome.com.tw/articles/10243628)

[Selenium3 自动化测试实战:基于Python 语言](https://yun.weicheng.men/Book/Selenium3%E8%87%AA%E5%8A%A8%E5%8C%96%E6%B5%8B%E8%AF%95%E5%AE%9E%E6%88%98%E2%80%94%E2%80%94%E5%9F%BA%E4%BA%8EPython%E8%AF%AD%E8%A8%80.pdf)

[Python 與自動化測試的敲門磚](https://ithelp.ithome.com.tw/users/20144024/ironman/5372)

[nickchen1998/2022_ithelp_marathon](https://github.com/nickchen1998/2022_ithelp_marathon)

### 下載器相關

[webdriver下載](https://www.selenium.dev/documentation/webdriver/capabilities/)

[Remote WebDriver](https://www.selenium.dev/documentation/webdriver/remote_webdriver/)

[chrome下載位置](https://chromedriver.chromium.org/downloads)

[Firefox下載位置](https://github.com/mozilla/geckodriver)

# 指令

```bash
# 安裝
pip install selenium
```

# 用法

## 各種瀏覽器 背景執行

```Python
from selenium import webdriver
from msedge.selenium_tools import Edge, EdgeOptions

# 設置 Edge 選項
options = EdgeOptions()
options.use_chromium = True  # 使用 Chromium 版本的 Edge
options.add_argument('--headless')  # 啟用無頭模式

# 啟動 Edge 瀏覽器
driver = Edge(options=options)

# 在這裡進行你的自動化任務

# 關閉瀏覽器
driver.quit()
```

```Python
from selenium import webdriver
from selenium.webdriver.firefox.options import Options

# 設置 Firefox 選項
options = Options()
options.headless = True

# 啟動 Firefox 瀏覽器
driver = webdriver.Firefox(options=options)

# 在這裡進行你的自動化任務

# 關閉瀏覽器
driver.quit()
```

```Python
from selenium import webdriver
from selenium.webdriver.chrome.options import Options

# 設置 Chrome 選項
chrome_options = Options()
chrome_options.add_argument('--headless')  # 啟用無頭模式

# 啟動 Chrome 瀏覽器
driver = webdriver.Chrome(options=chrome_options)

# 在這裡進行你的自動化任務

# 關閉瀏覽器
driver.quit()
```