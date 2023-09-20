# Python 基本 爬蟲筆記

## 目錄

- [Python 基本 爬蟲筆記](#python-基本-爬蟲筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [反爬蟲相關](#反爬蟲相關)
		- [特殊案例相關](#特殊案例相關)
- [範例](#範例)
	- [blob video 下載](#blob-video-下載)
	- [反爬蟲](#反爬蟲)
		- [判斷瀏覽器 headers 資訊](#判斷瀏覽器-headers-資訊)
			- [清空 window.navigator](#清空-windownavigator)
		- [使用動態頁面](#使用動態頁面)
		- [加入使用者行為判斷](#加入使用者行為判斷)
		- [模擬真實用戶登入授權](#模擬真實用戶登入授權)
		- [加入驗證碼機制](#加入驗證碼機制)
		- [封鎖代理伺服器與第三方 IP](#封鎖代理伺服器與第三方-ip)
		- [設置網址格式](#設置網址格式)

## 參考資料

[cURL轉成個別語言範本](https://curlconverter.com/#python)

[curl man page](https://curl.se/docs/manpage.html)

[Python學習資源整理 - 爬蟲](https://www.learncodewithmike.com/2020/02/python3-learning.html)

[All User Agent Strings(所有 user-agent 資訊)](http://useragentstring.com/pages/useragentstring.php?name=All)

[XPath教程](https://www.runoob.com/xpath/xpath-tutorial.html)

[CSS 基本](https://developer.mozilla.org/zh-TW/docs/Learn/Getting_started_with_the_web/CSS_basics)

### 反爬蟲相關

[破解反爬蟲的方法](https://steam.oxxostudio.tw/category/python/spider/crack-spider.html)

[2Captcha - 破解驗證碼](https://2captcha.com/)

[2Captcha.com API](https://2captcha.com/2captcha-api#solving_normal_captcha)

[用 2Captcha 通過 CAPTCHA 人機驗證](https://editor.leonh.space/2022/2captcha/)

[免費的 Proxy IP](https://free-proxy-list.net/)

### 特殊案例相關

[blob video 下載 - How do we download a blob url video](https://stackoverflow.com/questions/42901942/how-do-we-download-a-blob-url-video)

[[C#/Java] 针对 QINIU-PROTECTION-10 的m3u8视频文件解密](https://www.cnblogs.com/mq0036/p/14962044.html)

[m3u8加密文件原理及下载脚本](https://blog.csdn.net/devil8123665/article/details/124719006)

# 範例

## blob video 下載

```Python
# 下載 Blob 視頻，您必須從頁面檢查中獲取主段 url，就像給定的圖像一樣，通過代碼中提到它的 url
import requests
import m3u8
import subprocess

master_url ='master_url_from_inspect_network'
# past your page inspect request header

r = requests.get(master_url)
m3u8_master = m3u8.loads(r.text)
print(m3u8_master)
playlist_url =m3u8_master.data['playlists'][0]['uri']
play_r = requests.get(playlist_url)
m3u8_master_play = m3u8.loads(play_r.text)
m3_data=(m3u8_master_play.data)

m3_datas = m3_data['segments'][0]['uri']

with open('video.ts','wb') as fs:
    for segments in m3_data['segments']:
       uri = segments['uri']
       print(uri)
       r = requests.get(uri)
       fs.write(r.content)
```

## 反爬蟲

### 判斷瀏覽器 headers 資訊

利用 headers 判斷來源是否合法，headers 通常會由瀏覽器自動產生，直接透過程式所發出的請求預設沒有 headers，破解難度：低。

加入瀏覽器 headers 資訊
針對「判斷瀏覽器 headers 資訊」的網頁，只要能透過爬蟲程式，送出模擬瀏覽器的 headers 資訊，就能進行破解。

```Python
import requests

url = '要爬的網址'
# 假的 headers 資訊
headers = {'user-agent': 'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36'}
# 加入 headers 資訊
web = requests.get(url, headers=headers)
web.encoding = 'utf8'
print(web.text)
```

```Python
from selenium import webdriver

# 假的 headers 資訊
user_agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0.3 Safari/605.1.15"
opt = webdriver.ChromeOptions()
# 加入 headers 資訊
opt.add_argument('--user-agent=%s' % user_agent)
driver = webdriver.Chrome('./chromedriver', options=opt)
driver.get('要爬的網址')
```

#### 清空 window.navigator

有些反爬蟲的網頁，會檢測瀏覽器的 window.navigator 是否包含 webdriver 屬性，在正常使用瀏覽器的情況下，webdriver 屬性是 undefined，一旦使用了 selenium 函式庫，這個屬性就被初始化為 true，只要藉由 Javascript 判斷這個屬性，就能簡單的進行反爬蟲。

下方的程式使用 selenium webdriver 的 execute_cdp_cmd 的方法，將 webdriver 設定為 undefined，就能避開這個檢查機制。

```Python
from selenium import webdriver

driver = webdriver.Chrome('./chromedriver')
driver.execute_cdp_cmd("Page.addScriptToEvaluateOnNewDocument", {
  "source": """
    Object.defineProperty(navigator, 'webdriver', {
      get: () => undefined
    })
  """
})
```

### 使用動態頁面

將網頁內容全部由動態產生，大幅增加爬蟲處理網頁結構的複雜度，破解難度：中低。

針對「使用動態頁面」的網頁，只要確認動態頁面的架構，就能進行破解，如果打開的網頁是動態頁面，「檢視網頁原始碼」時看到的結構往往會很簡單，通常都只會是一些簡單的 HTML、CSS 和壓縮過的 js 文件。

使用 Chrome 開啟網頁，在網頁任意位置按下滑鼠右鍵，選擇「檢視」，開啟 Chrome 開發者工具，從中就能看到動態網頁載入後的完整架構。

取得網頁結構後，進一步分析網頁結構，下方的程式使用 Selenium 函式庫的功能，抓取特定的網頁元素或進行指定的動作。

```Python
from selenium import webdriver

driver = webdriver.Chrome('./chromedriver')
driver.get('爬取的網址')
# 從載入後的動態網頁裡，找到指定的元素
imgCount = driver.find_element(By.CSS_SELECTOR, 'CSS 選擇器')
```

### 加入使用者行為判斷

在網頁的某些元素，加入使用者行為的判斷，例如滑鼠移動順序、滑鼠是否接觸...等，增加爬蟲處理的難度，破解難度：中。

針對「加入使用者行為判斷」的網頁，確認頁面加入的使用者行為，就能模擬並進行破解，舉例來說，有些網頁會在按鈕加上「滑鼠碰觸」的保護，如果不是真的用滑鼠碰觸，只是用程式撰寫「點擊」指令，就會被當作爬蟲而被阻擋。

下方的程式使用 Selenium 函式庫的功能，模擬出先碰觸元素，再進行點擊的動作，藉此突破這個反爬蟲的機制。

```Python
from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains

submitBtn = driver.find_element(By.CSS_SELECTOR, '#submitBtn')
actions = ActionChains(driver)
# 滑鼠先移到 submitBtn 上，然後再點擊 submitBtn
actions.move_to_element(submitBtn).click(submitBtn)
actions.perform()
```

有些網頁也會判斷使用者刷新網頁的時間 ( 通常使用者不會在極短的時間內連續刷新 )，這時也可以使用 time 函式庫的 sleep 方法讓網頁有所等待，避開這個檢查機制

```Python
from selenium import webdriver
from time import sleep

submitBtn = driver.find_element(By.CSS_SELECTOR, '#submitBtn')
sleep(1)     # 等待一秒
submitBtn.click()
sleep(0.5)   # 等待 0.5 秒
submitBtn.click()
```

### 模擬真實用戶登入授權

在使用者登入時，會將使用者的授權 ( token ) 加入瀏覽器的 Cookie 當中，藉由判斷 Cookie 確認使用者是否合法，破解難度：中。

針對「模擬真實用戶登入授權」的網頁，只要知道 request 與 response 的機制後，取得 Cookie 內的 token 就能破解。舉例來說，下圖為 Ptt 八卦版網頁，從 Chrome 開發者工具裡可以看到所需要的 Cookies 資訊。

知道所需的 cookies 資訊後，就能在 Requests 函式庫裡，增加相對應的資訊，就能順利爬取到資料。

```Python
import requests

cookies = {'over18':'1'}
# 加入 Cookies 資訊
web = requests.get('https://www.ptt.cc/bbs/Gossiping/index.html', cookies=cookies)
print(web.text)
```

### 加入驗證碼機制

相當常見的驗證機制，可相當程度的防堵惡意的干擾與攻擊，對於非人類操作與大量頻繁操作都有不錯的防範機制 ( 例如防堵高鐵搶票、演唱會搶票...等 )，破解難度：高。

針對「加入驗證碼機制」的網頁，必須搭配一些 AI 來處理圖形、數字、文字的識別，通常只要能識別驗證碼就能破解。如果要破解一般驗證碼，需要先將網頁上的驗證碼圖片下載，再將圖片提交到 2Captcha 服務來幫我們進行辨識，等同於執行兩次爬蟲，先爬取目標網頁，在爬取 2Captcha 網頁取得辨識後的驗證碼，最後再把驗證把輸入目標網頁。

```Python
```

### 封鎖代理伺服器與第三方 IP

針對惡意攻擊的 IP 進行封鎖，破解難度：高

針對「封鎖代理伺服器與第三方 IP」的網頁，通常必須更換 IP 或更換代理伺服器才能破解，許多網站上也有提供免費的 Proxy IP，以 Free Proxy List 網站為例，就能取得許多免費的 Proxy IP

```Python
import requests

# 建立 Proxy List
proxy_ips = ['80.93.213.213:3136',
'191.241.226.230:53281',
'207.47.68.58:21231',
'176.241.95.85:48700'
]
# 依序執行 get 方法
for ip in proxy_ips:
    try:
        result = requests.get('https://www.google.com', proxies={'http': 'ip', 'https': ip})
        print(result.text)
    except:
        print(f"{ip} invalid")
```

### 設置網址格式

```
; 需爬取資訊 目標網址
URL=https://www.example.com/

; 設置 其他頁面網址格式 $URL$PAGE_FORMAT {}
; 範例:
; URL = https://www.example.com/
; PAGE_FORMAT = page/{}/
; 結果為 https://www.example.com/page/{num}/
PAGE_FORMAT=
```

```Python
import re

if re.search(r'monsnode.com', URL):
    mc = MonsnodeCrawler(URL, DIR)
    mc.parse()
```