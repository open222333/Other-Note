# Python 模組 BeautifulSoup-bs4(爬蟲)

```
爬蟲程式偽裝成瀏覽器方法：加上標頭(headers)

1. 為什麼要設定headers?<br>
	在請求網頁爬取的時候，輸出的text資訊中會出現抱歉，無法訪問等字眼，這就是禁止爬取，需要通過反爬機制去解決這個問題。
	headers是解決requests請求反爬的方法之一，相當於我們進去這個網頁的伺服器本身，假裝自己本身在爬取資料。

2. headers在哪裡找？<br>谷歌或者火狐瀏覽器，在網頁面上點選右鍵，–>檢查–>剩餘按照圖中顯示操作，需要按Fn+F5刷新出網頁來這裡寫圖片描述
3. headers中有很多內容，主要常用的就是user-agent 和 host，他們是以鍵對的形式展現出來，如果user-agent 以字典鍵對形式作為headers的內容，就可以反爬成功，就不需要其他鍵對否則，需要加入headers下的更多鍵對形式。

```

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
```
