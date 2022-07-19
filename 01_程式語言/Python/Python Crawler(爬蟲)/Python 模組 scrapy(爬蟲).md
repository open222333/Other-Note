# Python 模組 scrapy(爬蟲)

```
由於Scrapy是一個框架，所以有一定的專案架構及執行流程，除了未來很好維護外，也能夠輕鬆的移轉既有的專案到另一個專案。

特色

	執行速度較快
	允許客製化功能來進行擴充
	內建多種檔案格式輸出(JSON、XML、CSV)
	內建支援XPath及CSS表達式來擷取資料
	完善的互動式偵錯工具(Scrapy Shell)

使用時機

    適用於大型的Python網頁爬蟲專案，有一定的效率要求及需要處理複雜的爬取邏輯，Scrapy就會是一個很好的選擇。


文檔說明：

	scrapy.cfg: 項目的配置文件。
	mySpider/: 項目的Python模塊，將會從這裡引用代碼。
	mySpider/items.py: 項目的目標文件。
	mySpider/pipelines.py: 項目的管道文件。
	mySpider/settings.py: 項目的設置文件。
	mySpider/spiders/: 存儲爬蟲代碼目錄。
```

## 目錄

- [Python 模組 scrapy(爬蟲)](#python-模組-scrapy爬蟲)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
	- [設定檔相關](#設定檔相關)
- [用法](#用法)
- [zyte ScrapyHub](#zyte-scrapyhub)
	- [參考資料](#參考資料-1)
- [指令 zyte](#指令-zyte)

## 參考資料

[scrapy pypi](https://pypi.org/project/scrapy/)

[scrapy官方文檔 (全面)](https://docs.scrapy.org/en/latest/#)

[scrapy文檔](https://scrapy-chs.readthedocs.io/zh_CN/0.24/index.html)

[scrapy文檔 Selector](https://docs.scrapy.org/en/latest/topics/selectors.html#module-scrapy.selector)

[菜鳥教程 製作Scrapy爬蟲](https://www.runoob.com/w3cnote/scrapy-detail.html)

[[Scrapy教學3]如何有效利用Scrapy框架建立網頁爬蟲看這篇就懂](https://www.learncodewithmike.com/2021/01/scrapy-create-spider.html)

[Scrapy Item Pipeline 存入資料庫](https://ithelp.ithome.com.tw/articles/10207157)

[response objects - 爬取後回傳](https://doc.scrapy.org/en/1.3/topics/request-response.html#response-objects)


[Passing additional data to callback functions](https://doc.scrapy.org/en/1.3/topics/request-response.html#passing-additional-data-to-callback-functions)

[Scrapy-Request和Response（請求和響應) 將附加數據傳遞給回調函數](https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/416586/)

[Scrapy 隨機代理實現 - scrapy 編寫中間件 添加代理(proxy)](https://ithelp.ithome.com.tw/articles/10208773)

[Scrapy 爬免費代理(Proxy)](https://ithelp.ithome.com.tw/articles/10208575)

[Scrapy and proxies](https://stackoverflow.com/questions/4710483/scrapy-and-proxies)

[(spider 帶參數)How to pass a user defined argument in scrapy spider](https://stackoverflow.com/questions/15611605/how-to-pass-a-user-defined-argument-in-scrapy-spider)

# 指令

```bash
# 安裝
pip install Scrapy

# 執行後如果沒有顯示錯誤訊息，代表安裝成功
scrapy bench

# 建立Scrapy專案：
scrapy startproject 你的專案名稱

# 建立爬蟲
scrapy genspider 爬蟲名稱 "域名"

# crawl是基於專案執行，runspide是基於檔案執行，
# 按照scrapy的蜘蛛格式編寫了一個py檔案，如果不想建立專案，就可以使用runspider
# 執行爬蟲
scrapy crawl 爬蟲名稱
	-o 檔名 輸出檔案

# 執行爬蟲
scrapy runspider 檔案名.py
```

## 設定檔相關

```Python
# setting.py

# 不遵守 robot協議
ROBOTSTXT_OBEY = False

# docker 寫法可用
SPLASH_URL = "http://splash:8050"
```

```bash
# 這個命令比較重要，主要是偵錯用，裡面還有很多細節的命令
# 最簡單常用的的就是偵錯，檢視我們的選擇器是否有正確選中某個元素

## 排錯用指令
scrapy shell "url"
# 直接執行response命令
# 測試獲取標題的選擇器是否正確
response.css("title").extract_first()

# 測試xpath路徑選擇是否正確
response.xpath("//*[@id='mainContent']/div/div/div[2]/a/span").extract()
```

# 用法

`scrapy.Spider 範例`

```Python
class SampleSpider(scrapy.Spider):
    '''範本'''
    name = 'Sample'
    site_name = ''
    video_type = ''
    video_item = ''
    video_filter = ''
    target_url = ''
    user_agent = FakeUserAgent().google

    # 客製化設定 一般讀取settings 這只使用在此爬蟲
    custom_settings = {
        'TEST': 'value',
    }

    def __init__(self, pages=5, wait_sec=30, *tags):
        self.pages = int(pages)
        self.wait_sec = int(wait_sec)
        self.tags = [tag for tag in tags]

    def get_proxy(self):
        # proxy = 'http://host_ip:port'
        '''從設定取得 代理服務器的 地址,帳號,密碼'''
        proxy = self.settings['PROXY_URL']
        username = self.settings['PROXY_USERNAME']
        password = self.settings['PROXY_PASSWORD']

        # 驗證帳號密碼
        auth = f'{username}:{password}'.encode(encoding='ISO-8859-1')
        auth = b'Basic ' + urlsafe_b64encode(auth)

        return {
            'proxy': proxy,
            'auth': auth
        }

    def start_requests(self):
        start_urls = []
        items = ['/']

        start_urls.append(self.target_url)
        if self.pages > 1:
            for item in items:
                for num in range(1, self.pages + 1):
                    start_urls.append(self.target_url + f'{item}{num}')

        # proxy 資訊
        proxy_info = self.get_proxy()

        for url in start_urls:
            yield SplashRequest(
                url=url,
                callback=self.parse,
                meta={
                    # 'data': data,
                    'proxy': proxy_info['proxy'],
                },
                headers={
                    'User-Agent': self.user_agent,
                    'Proxy-Authorization':  proxy_info['auth']
                },
                args={
                    'wait': self.wait_sec,
                    # 'splash_headers': {'User-Agent': self.user_agent}
                }
            )

    def parse(self, response):
        item = VideoItem()
        soup = BeautifulSoup(response.body, 'lxml')
        target = True

        videos = soup.select('')
        for video in videos:

            # 排除標籤內有tags
            tags_select = soup.select('')
            for tag in tags_select:
                if tag.text in self.tags:
                    target = False
                    break

            data = {}
            try:
                cover = video.select_one('')
                title = video.select_one('')
                views = video.select_one('')
                video_page_url = video.select_one('')
            except:
                traceback.print_exc()

            if target:
                data['spider_code'] = self.name
                data['site_name'] = self.site_name
                data['video_type'] = self.video_type
                data['video_item'] = self.video_item
                data['video_filter'] = self.video_filter
                data['cover'] = cover
                data['title'] = title
                data['views'] = get_SI_prefix_num(str(views))
                data['video_page_url'] = video_page_url

                item.set_item(data)
                yield item
```

```Python
# 多層頁面
class SampleTwoParseSpider(scrapy.Spider):
    '''需爬內頁'''
    name = 'SampleTwoParse'
    site_name = ''
    video_type = ''
    video_item = ''
    video_filter = ''
    target_url = ''
    user_agent = FakeUserAgent().google

    def __init__(self, pages=5, wait_sec=30, *tags):
        self.pages = int(pages)
        self.wait_sec = int(wait_sec)
        self.tags = [tag for tag in tags]

    def start_requests(self):
        start_urls = []
        items = ['/']

        start_urls.append(self.target_url)
        if self.pages > 1:
            for item in items:
                for num in range(1, self.pages + 1):
                    start_urls.append(self.target_url + f'{item}{num}')

        for url in start_urls:
            yield SplashRequest(
                url=url,
                callback=self.first_parse,
                args={
                    'wait': self.wait_sec,
                    'splash_headers': {'User-Agent': self.user_agent}
                }
            )

    def first_parse(self, response):
        soup = BeautifulSoup(response.body, 'lxml')

        videos = soup.select('')
        for video in videos:
            data = {}
            try:
                cover = video.select_one('')
                title = video.select_one('')
                video_page_url = video.select_one('')
            except:
                traceback.print_exc()

            data['cover'] = cover
            data['title'] = title
            data['video_page_url'] = video_page_url

            yield SplashRequest(
                url=data['video_page_url'],
                callback=self.parse,
				# meta 可將資料保留 並傳到下一個parse
                meta={'data': data},
                args={
                    'wait': self.wait_sec,
                    'splash_headers': {'User-Agent': self.user_agent}
                }
            )

    def parse(self, response):
        item = VideoItem()
        soup = BeautifulSoup(response.body, 'lxml')
        data = response.request.meta['data']
        target = True

        try:
            views = soup.select_one('')
        except:
            traceback.print_exc()

        # 排除標籤內有tags
        tags_select = soup.select('')
        for tag in tags_select:
            if tag.text in self.tags:
                target = False
                break

        if target:
            data['spider_code'] = self.name
            data['site_name'] = self.site_name
            data['video_type'] = self.video_type
            data['video_item'] = self.video_item
            data['video_filter'] = self.video_filter
            data['views'] = get_SI_prefix_num(str(views))

            item.set_item(data)
            yield item

```

```Python
#mySpider/spider/itcast.py
import scrapy

class TestSpider(scrapy.Spider):
    # 這個爬蟲的識別名稱
    name = "test"
    # 搜索的域名範圍
    allowed_domains = ["test.com"]
    # 從這裡開始抓取數據
    start_urls = (
        'http://www.test.com/',
    )

    # 解析
    def parse(self, response):
        pass
```

```Python
# meta 可將資料保留 並傳到下一個parse
def parse_page(self, response):
    # do some processing
    return scrapy.Request("http://www.example.com/otherpage",
        meta={'cookiejar': response.meta['cookiejar']},
        callback=self.parse_other_page)
```

```Python
# scrapy crawl myspider -a category=electronics -a domain=system
class MySpider(scrapy.Spider):
    name = 'myspider'

    def __init__(self, category='', **kwargs):
        self.start_urls = [f'http://www.example.com/{category}']  # py36
        super().__init__(**kwargs)  # python3

    def parse(self, response)
        self.log(self.domain)  # system
```

```Python
# scrapy crawl quotes -a num=7
import scrapy

class QuotesSpider(scrapy.Spider):
    name = 'quotes'
    allowed_domains = ['quotes.com']

    def __init__(self,num='', *args,**kwargs):
        super().__init__(*args, **kwargs)
        self.num = num
        self.start_urls = [f'http://quotes.com/{self.num}']
```

```Python
# spider 帶參數
# scrapy crawl quotes -a num=7
import scrapy

class QuotesSpider(scrapy.Spider):
    name = 'quotes'
    allowed_domains = ['quotes.com']

    def start_requests(self):

        num = getattr(self, num, False)
        if num:
            url = f'hppt://quotes.com/{num}'
            yield scrapy.Request(url)
```

```Python
# 使用程式啟動scrapy spider
def run_spider_one(spider):
    # 設定工作資料夾
    import os
    from scrapy import cmdline

    os.chdir(os.path.dirname(__file__))

    args = f"scrapy crawl {spider_name}".split()
    cmdline.execute(args)


def run_spider_two(spider: scrapy.Spider):
    '''spider: scrapy.Spider 類別實例'''
    from scrapy.crawler import CrawlerProcess
    from scrapy.utils.project import get_project_settings
    import os
    os.chdir('scrapy_project')
    process = CrawlerProcess(get_project_settings())
    process.crawl(spider)
    process.start()
```

```Python
# 中間件 proxy 實作範例
from w3lib.http import basic_auth_header

class CustomProxyMiddleware(object):
    '''proxy
    使用 proxy

    要確保在HttpProxyMiddleware之前
    DOWNLOADER_MIDDLEWARES = {
        'myproject.middlewares.CustomProxyMiddleware': 350,
        'scrapy.downloadermiddlewares.httpproxy.HttpProxyMiddleware': 400,
    }

    '''

    def process_request(self, request, spider):
        request.meta['proxy'] = "http://192.168.1.1:8050"
        request.headers['Proxy-Authorization'] = basic_auth_header('proxy_user', 'proxy_pass')
```

# zyte ScrapyHub

```
可將Scrapy上傳至此網站管理並操作
```

## 參考資料

[提供Splash 伺服器](https://www.zyte.com/)

[API文檔](https://docs.zyte.com/scrapy-cloud/items.html)

[scrapinghub 文檔](https://python-scrapinghub.readthedocs.io/en/latest/)

[zyte API規範](https://docs.zyte.com/zyte-api/openapi.html#zyte-openapi-spec)

[zyte 取的item api](https://docs.zyte.com/scrapy-cloud/items.html#items-project-id-spider-id-job-id-item-no-field-name)

[Client interface for Scrapinghub API(zyte 提供給python的api)](https://python-scrapinghub.readthedocs.io/en/latest/index.html)

# 指令 zyte

```bash
# 安裝
pip install shub

# 登入
shub login

# API key: APIkey(project內提供)
shub deploy projectID(project內提供)

# 登出
shub logout
```

`若需要額外的模組 requirements.txt`

`添加到scrapinghub.yml：`

```yml
projects:
	default: 12345
requirements:
	file: requirements.txt
```
