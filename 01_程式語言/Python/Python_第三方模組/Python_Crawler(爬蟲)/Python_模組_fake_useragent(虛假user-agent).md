# Python 模組 fake_useragent(虛假user-agent)

```
爬蟲程式偽裝成瀏覽器方法：加上標頭(headers)

1. 為什麼要設定headers?<br>
	在請求網頁爬取的時候，輸出的text資訊中會出現抱歉，無法訪問等字眼，這就是禁止爬取，需要通過反爬機制去解決這個問題。
	headers是解決requests請求反爬的方法之一，相當於我們進去這個網頁的伺服器本身，假裝自己本身在爬取資料。

2. headers在哪裡找？<br>谷歌或者火狐瀏覽器，在網頁面上點選右鍵，–>檢查–>剩餘按照圖中顯示操作，需要按Fn+F5刷新出網頁來這裡寫圖片描述
3. headers中有很多內容，主要常用的就是user-agent 和 host，他們是以鍵對的形式展現出來，如果user-agent 以字典鍵對形式作為headers的內容，就可以反爬成功，就不需要其他鍵對否則，需要加入headers下的更多鍵對形式。

```

## 目錄

- [Python 模組 fake_useragent(虛假user-agent)](#python-模組-fake_useragent虛假user-agent)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[fake_useragent pypi](https://pypi.org/project/fake_useragent/)

[爬蟲進化 – 偽裝篇 fake_useragent 介紹](https://weikaiwei.com/python/python-crawler-fake-useragent/)


# 指令

```bash
# 安裝
pip install fake_useragent
```

# 用法

```Python
from fake_useragent import UserAgent
ua = UserAgent()

ua.ie
# Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US);
ua.msie
# Mozilla/5.0 (compatible; MSIE 10.0; Macintosh; Intel Mac OS X 10_7_3; Trident/6.0)'
ua['Internet Explorer']
# Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; GTB7.4; InfoPath.2; SV1; .NET CLR 3.3.69573; WOW64; en-US)
ua.opera
# Opera/9.80 (X11; Linux i686; U; ru) Presto/2.8.131 Version/11.11
ua.chrome
# Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.2 (KHTML, like Gecko) Chrome/22.0.1216.0 Safari/537.2'
ua.google
# Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/537.13 (KHTML, like Gecko) Chrome/24.0.1290.1 Safari/537.13
ua['google chrome']
# Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11
ua.firefox
# Mozilla/5.0 (Windows NT 6.2; Win64; x64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1
ua.ff
# Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:15.0) Gecko/20100101 Firefox/15.0.1
ua.safari
# Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25

# and the best one, random via real world browser usage statistic
ua.random
```
