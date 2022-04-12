# Python 模組 BeautifulSoup-bs4(爬蟲)

## 參考資料

[文檔](https://www.crummy.com/software/BeautifulSoup/bs4/doc/#)

# beautifulsoup4模組

```bash
# 安裝套件
pip install beautifulsou
```

[BeautifulSoup find 的各種用法](http://python-learnnotebook.blogspot.com/2018/01/beautifulsoup-instructions.html)


無法單獨完整實現Python網頁爬蟲的整個流程，像是發送請求與解析下載的HTML原始碼，所以需要特性的相依性模組來協助，例如：

1. requests:
    對網頁伺服端發送請求，來取得HTML網頁原始碼。
2. html.parser或lxml解析器:
    將取得的HTML原始碼進行解析，才有辦法使用BeautifulSoup套件所提供的搜尋方法，來擷取網頁元素。

特色

    學習曲線較低，非常容易上手
    適用於靜態網頁的爬取
