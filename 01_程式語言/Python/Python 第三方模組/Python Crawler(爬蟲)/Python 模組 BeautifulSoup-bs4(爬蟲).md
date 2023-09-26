# Python 模組 BeautifulSoup-bs4(爬蟲)

```
無法單獨完整實現Python網頁爬蟲的整個流程，像是發送請求與解析下載的HTML原始碼，所以需要特性的相依性模組來協助，例如：

1. requests:
    對網頁伺服端發送請求，來取得HTML網頁原始碼。
2. html.parser或lxml解析器:
    將取得的HTML原始碼進行解析，才有辦法使用BeautifulSoup套件所提供的搜尋方法，來擷取網頁元素。

特色

    學習曲線較低，非常容易上手
    適用於靜態網頁的爬取

```

## 目錄

- [Python 模組 BeautifulSoup-bs4(爬蟲)](#python-模組-beautifulsoup-bs4爬蟲)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[BeautifulSoup pypi](https://pypi.org/project/BeautifulSoup/)

[官方文檔](https://www.crummy.com/software/BeautifulSoup/bs4/doc/#)

[BeautifulSoup find 的各種用法](http://python-learnnotebook.blogspot.com/2018/01/beautifulsoup-instructions.html)


# 指令

```bash
# 安裝套件(舊版)
pip install BeautifulSoup

# 新的
pip install beautifulsoup4
```

# 用法

```Python
from bs4 import BeautifulSoup

# 安裝 Beautiful Soup 库:
# pip install beautifulsoup4

# HTML 文件示例:
html_doc = """
<html>
<head>
    <title>示例页面</title>
</head>
<body>
    <h1>歡迎使用Beautiful Soup</h1>
    <p class="intro">Beautiful Soup是一個強大的庫，用於解析HTML和XML文檔。</p>
    <p class="content">它可以幫助您提取文檔中的數據，輕鬆處理網頁內容。</p>
    <ul>
        <li>提取標籤內容</li>
        <li>查找元素</li>
        <li>遍歷文檔樹</li>
    </ul>
</body>
</html>
"""

# 創建Beautiful Soup對象並指定解析器：
soup = BeautifulSoup(html_doc, 'html.parser')

# 提取標籤內容：

# 提取<h1>標籤的內容
h1_tag = soup.find('h1')
print(h1_tag.text)  # 輸出：歡迎使用Beautiful Soup

# 提取<p>標籤的內容
p_tags = soup.find_all('p')
for p_tag in p_tags:
    print(p_tag.text)

# 查找元素：

# 查找所有具有"class"屬性為"intro"的<p>標籤
intro_tags = soup.find_all('p', class_='intro')
for intro_tag in intro_tags:
    print(intro_tag.text)

# 遍歷文檔樹：

# 遍歷文檔樹，查找所有<ul>標籤
ul_tags = soup.find_all('ul')
for ul_tag in ul_tags:
    # 查找<ul>標籤內的<li>標籤
    li_tags = ul_tag.find_all('li')
    for li_tag in li_tags:
        print(li_tag.text)
```

```Python
from bs4 import BeautifulSoup

# 匯入Beautiful Soup庫
# pip install beautifulsoup4

# 假設我們有以下HTML文檔：
html_doc = """
<html>
<head>
    <title>示例頁面</title>
</head>
<body>
    <h1>歡迎使用Beautiful Soup</h1>
    <p class="intro">Beautiful Soup是一個強大的庫，用於解析HTML和XML文檔。</p>
    <p class="content">它可以幫助您提取文檔中的數據，輕鬆處理網頁內容。</p>
    <ul>
        <li>提取標籤內容</li>
        <li>查找元素</li>
        <li>遍歷文檔樹</li>
    </ul>
</body>
</html>
"""

# 創建Beautiful Soup對象並指定解析器：
soup = BeautifulSoup(html_doc, 'html.parser')

# 使用CSS選擇器選擇和提取元素：

# 示例 1：選擇所有 <p> 標籤
p_tags = soup.select('p')

# 打印每個 <p> 標籤的文本內容
for p_tag in p_tags:
    print(p_tag.text)

# 示例 2：選擇 <h1> 標籤
h1_tag = soup.select('h1')

# 打印 <h1> 標籤的文本內容
print(h1_tag[0].text)

# 示例 3：選擇帶有 class 屬性為 "intro" 的 <p> 標籤
intro_tags = soup.select('p.intro')

# 打印每個符合條件的 <p> 標籤的文本內容
for intro_tag in intro_tags:
    print(intro_tag.text)

# 示例 4：選擇第一個 <li> 標籤
li_tag = soup.select_one('li')

# 打印第一個 <li> 標籤的文本內容
print(li_tag.text)
```

```Python
# 取得屬性 href
page_url = block.select_one('a').attrs['href']
# 取得屬性 src
img_url = block.select_one('a > img').attrs['src']
```
