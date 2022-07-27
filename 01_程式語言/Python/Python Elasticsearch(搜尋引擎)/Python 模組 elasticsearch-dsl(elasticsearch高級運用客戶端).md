# Python 模組 elasticsearch-dsl(elasticsearch高級運用客戶端)

```
Elasticsearch DSL 是一個高級庫，其目的是幫助編寫和運行針對 Elasticsearch 的查詢。

它建立在官方的低級客戶端（elasticsearch-py）之上。
```

## 目錄

- [Python 模組 elasticsearch-dsl(elasticsearch高級運用客戶端)](#python-模組-elasticsearch-dslelasticsearch高級運用客戶端)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[elasticsearch-dsl pypi](https://pypi.org/project/elasticsearch-dsl/)

[elasticsearch-dsl 文檔](https://elasticsearch-dsl.readthedocs.io/en/latest/)

# 指令

```bash
# 安裝
pip install elasticsearch-dsl
```

# 用法

```Python
from elasticsearch import Elasticsearch
from elasticsearch_dsl import Search

client = Elasticsearch()

s = Search(using=client, index="my-index") \
    .filter("term", category="search") \
    .query("match", title="python")   \
    .exclude("match", description="beta")

s.aggs.bucket('per_tag', 'terms', field='tags') \
    .metric('max_lines', 'max', field='lines')

response = s.execute()

for hit in response:
    print(hit.meta.score, hit.title)

for tag in response.aggregations.per_tag.buckets:
    print(tag.key, tag.max_lines.value)
```
