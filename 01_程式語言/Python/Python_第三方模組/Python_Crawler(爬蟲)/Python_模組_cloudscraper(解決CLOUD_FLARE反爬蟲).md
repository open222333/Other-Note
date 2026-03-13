# Python 模組 cloudscraper(解決CLOUD FLARE反爬蟲)

```
```

## 目錄

- [Python 模組 cloudscraper(解決CLOUD FLARE反爬蟲)](#python-模組-cloudscraper解決cloud-flare反爬蟲)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [教學相關](#教學相關)
- [指令](#指令)
- [用法](#用法)
  - [處理 Cookies 和緩存的工作原理](#處理-cookies-和緩存的工作原理)
  - [代理支持 proxies](#代理支持-proxies)
  - [使用 FlareSolverr 配置 Cloudscraper](#使用-flaresolverr-配置-cloudscraper)

## 參考資料

[cloudscraper pypi](https://pypi.org/project/cloudscraper/)

[cloudscraper Github](https://github.com/venomous/cloudscraper)

### 教學相關

[(番外篇-爬蟲)[不做怎麼知道系列之Android開發者的30天後端養成故事 Day24] - 來問問你認識的Youtuber的訂閱數吧~ #crawler #python #socialblade](https://ithelp.ithome.com.tw/articles/10230271)

[使用CLOUDSCRAPER捅穿CLOUD FLARE的5秒盾](https://www.cnblogs.com/yoyo1216/p/17356845.html)

[How to Use Cloudscraper in Python: 2024 Guide](https://iproyal.com/blog/cloudscraper/)

[爬虫：绕过5秒盾Cloudflare和DDoS-GUARD - 搭配 FlareSolverr](https://blog.csdn.net/gwb0516/article/details/132446314)
# 指令

```bash
# 安裝
pip install cloudscraper
```

# 用法

```Python
import cloudscraper


scraper = cloudscraper.create_scraper(
	browser={
		'browser': 'chrome',
		'platform': 'android',
		'desktop': False
	}
)

response = scraper.get(url)

# 打印网页内容
print(response.text)
```

## 處理 Cookies 和緩存的工作原理

Cookies:

```
網站常用 Cookies 作為用戶識別和驗證的手段。
Cloudscraper 自動處理服務器返回的 Cookies 並在後續請求中帶上它們，模擬連續的瀏覽器會話。
```

緩存頭（Cache Headers）:

```
緩存頭用於告訴服務器和客戶端是否可以重用先前的響應數據。
Cloudscraper 可以模仿瀏覽器的緩存行為，讓請求看起來更自然。
```

```Python
import cloudscraper

# 創建 Cloudscraper 會話
scraper = cloudscraper.create_scraper()

# 發送請求，處理 Cookies 和緩存
url = "https://example.com"
response = scraper.get(url)

# 查看自動處理的 Cookies
print("Cookies:", scraper.cookies)

# 查看響應內容
print("Response:", response.text)
```

持久化 Cookies

如果需要在多個請求中共享 Cookies，可以使用 requests.Session 的方式結合 Cloudscraper：

```Python
import cloudscraper

# 創建持久化會話
scraper = cloudscraper.create_scraper(sess=True)

# 第一次請求，設置 Cookies
response = scraper.get("https://example.com")
print("Initial Cookies:", scraper.cookies)

# 第二次請求，自動攜帶 Cookies
response = scraper.get("https://example.com/some-page")
print("Cookies after second request:", scraper.cookies)
```

## 代理支持 proxies

```Python
proxies = {
    "http": "http://your_proxy_address",
    "https": "http://your_proxy_address",
}
scraper = cloudscraper.create_scraper()
response = scraper.get("https://example.com", proxies=proxies)
```

## 使用 FlareSolverr 配置 Cloudscraper

```Python
import cloudscraper
import json

# FlareSolverr 配置
FLARESOLVERR_URL = "http://localhost:8191/v1"

def solve_with_flaresolverr(url):
    """使用 FlareSolverr 處理請求"""
    # 構造 FlareSolverr 請求
    payload = {
        "cmd": "request.get",
        "url": url,
        "maxTimeout": 60000  # 最大超時時間 (毫秒)
    }

    # 發送請求到 FlareSolverr
    response = cloudscraper.create_scraper().post(
        FLARESOLVERR_URL,
        json=payload
    )

    if response.status_code == 200:
        # 返回解決後的 HTML 內容
        result = response.json()
        return result.get("solution", {}).get("response")
    else:
        raise Exception(f"FlareSolverr 出錯: {response.text}")

# 測試用例
if __name__ == "__main__":
    test_url = "https://protected-website.example.com"

    try:
        print("正在通過 FlareSolverr 解決 Cloudflare 挑戰...")
        solved_content = solve_with_flaresolverr(test_url)
        print("成功解決！返回內容如下：")
        print(solved_content[:500])  # 只顯示前 500 字元
    except Exception as e:
        print(f"發生錯誤: {str(e)}")
```