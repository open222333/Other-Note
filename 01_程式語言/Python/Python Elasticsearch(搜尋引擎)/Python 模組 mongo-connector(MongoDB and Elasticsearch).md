# Python 模組 mongo-connector(MongoDB and Elasticsearch)

```
```

## 目錄

- [Python 模組 mongo-connector(MongoDB and Elasticsearch)](#python-模組-mongo-connectormongodb-and-elasticsearch)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[mongo-connector pypi](https://pypi.org/project/mongo-connector/)

[mongo-connector github](https://github.com/yougov/mongo-connector)

# 指令

```bash
# 安裝
pip install mongo-connector
```

# 用法

```bash
mongo-connector -m <mongodb server hostname>:<replica set port> \
                -t <replication endpoint URL, eg http://localhost:8983/solr> \
                -d <name of doc manager, eg, solr_doc_manager>

mongo-connector -m localhost:27017 -t localhost:9200 -d elastic2_doc_manager --admin-username <username> --password <password>;
```
