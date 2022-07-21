# Python 模組 elasticsearch(搜尋引擎)

```
Elasticsearch 的官方低級客戶端。它的目標是為 Python 中所有與 Elasticsearch 相關的代碼提供共同基礎
```

## 目錄

- [Python 模組 elasticsearch(搜尋引擎)](#python-模組-elasticsearch搜尋引擎)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[elasticsearch pypi](https://pypi.org/project/elasticsearch/)

[elasticsearch 文檔](https://elasticsearch-py.readthedocs.io/en/v8.3.2/)

# 指令

```bash
# 安裝
pip install elasticsearch
```

# 用法

```Python
from datetime import datetime
from elasticsearch import Elasticsearch
es = Elasticsearch()

doc = {
    'author': 'kimchy',
    'text': 'Elasticsearch: cool. bonsai cool.',
    'timestamp': datetime.now(),
}
resp = es.index(index="test-index", id=1, document=doc)
print(resp['result'])

resp = es.get(index="test-index", id=1)
print(resp['_source'])

es.indices.refresh(index="test-index")

resp = es.search(index="test-index", query={"match_all": {}})
print("Got %d Hits:" % resp['hits']['total']['value'])
for hit in resp['hits']['hits']:
    print("%(timestamp)s %(author)s: %(text)s" % hit["_source"])
```
