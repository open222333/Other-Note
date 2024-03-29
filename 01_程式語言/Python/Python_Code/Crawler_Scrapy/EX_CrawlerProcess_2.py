import scrapy
from scrapy.crawler import CrawlerProcess

'''啟動方法'''
class MySpider(scrapy.Spider):
    # Your spider definition
    ...


process = CrawlerProcess(settings={
    "FEEDS": {
        "items.json": {"format": "json"},
    },
})

process.crawl(MySpider)
process.start()  # the script will block here until the crawling is finished
