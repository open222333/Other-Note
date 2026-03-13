# Python 模組 pyquery(操作 HTML 和 XML)

```
一個用於解析和操作 HTML 和 XML 文件的 Python 函式庫。
它提供了類似於 jQuery 的語法，讓您可以輕鬆地在文件中尋找、篩選和修改元素。
```

## 目錄

- [Python 模組 pyquery(操作 HTML 和 XML)](#python-模組-pyquery操作-html-和-xml)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [教學相關](#教學相關)
- [指令](#指令)
- [用法](#用法)
  - [jquery-like 分析 html](#jquery-like-分析-html)

## 參考資料

[pyquery pypi](https://pypi.org/project/pyquery/)

[pyquery 官方文檔](https://pyquery.readthedocs.io/en/latest/)

[pyquery github](https://github.com/gawel/pyquery)

### 教學相關

[[Python] pyquery 教學](https://zwindr.blogspot.com/2017/12/python-pyquery.html)

[python爬虫神器PyQuery的使用方法](https://segmentfault.com/a/1190000005182997)

[比美麗的湯更美麗：pyquery](https://tw.pycon.org/2017/en-us/events/talk/326506774788046936/)

# 指令

```bash
# 安裝
pip install pyquery
```

# 用法

```Python
from pyquery import PyQuery as pq

# HTML 字符串
html = '''
<html>
  <body>
    <h1 class="header">歡迎使用 pyquery</h1>
    <p>PyQuery 是用於網頁爬蟲的 Python 庫。</p>
    <a href="https://example.com">訪問示例</a>
  </body>
</html>
'''

# 創建 pyquery 對象
doc = pq(html)

# 查找元素並獲取文本和屬性
header = doc('h1.header')
print('標題文本：', header.text())

link = doc('a')
print('鏈接網址：', link.attr('href'))

# 修改元素
header.text('新標題')
link.attr('href', 'https://newexample.com')

# 打印修改後的 HTML
print(doc.html())
```

## jquery-like 分析 html

```Python
from pyquery import PyQuery

dom = PyQuery(url="https://www.google.com.tw/")
links = dom("a")

data = {}
for a in links.items():
    title = a.text().strip()
    data[title] = a.attr['href']
```
