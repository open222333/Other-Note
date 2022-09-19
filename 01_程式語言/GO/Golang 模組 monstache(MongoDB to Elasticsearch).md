# Golang 模組 monstache(MongoDB to Elasticsearch)

```
可用於將 MongoDB 數據同步到 Elasticsearch 的最全面的庫
```

## 目錄

- [Golang 模組 monstache(MongoDB to Elasticsearch)](#golang-模組-monstachemongodb-to-elasticsearch)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝步驟](#安裝步驟)
- [用法](#用法)
- [配置文檔](#配置文檔)
	- [中間件](#中間件)
- [狀況](#狀況)
	- [使用golang連接mongo server selection error: server selection timeout, current topology](#使用golang連接mongo-server-selection-error-server-selection-timeout-current-topology)
- [Mapping(映射)](#mapping映射)
- [pm2 執行守護程式](#pm2-執行守護程式)

## 參考資料

[github.com/rwynn/monstache GoDoc 文檔](https://pkg.go.dev/github.com/rwynn/monstache)

[官方文檔](https://rwynn.github.io/monstache-site/)

[官方文檔 - Configuration](https://rwynn.github.io/monstache-site/config/)

[官方文檔 - index-mapping](https://rwynn.github.io/monstache-site/advanced/#index-mapping)

[解决Monstache在启动的时候无法将Mongodb分表的同步到Elasticsearch - JS腳本](http://www.ireage.com/monstache/2021/03/19/monstache_split_table_conf.html)

# 安裝步驟

```bash
# 安裝gcc
yum install gcc -y

# somewhere outside your $GOPATH(golang 第三方套件安裝路徑)
cd ~/build

# clone 專案
git clone https://github.com/rwynn/monstache.git
cd monstache

# 選擇版本
git checkout <branch-or-tag-to-build>

# 安裝
go install

# 執行同步(需先編輯config.toml)
monstache -f /path/to/config.toml
```

# 用法

```bash
# 查看版本
monstache -v

# 啟動(需先建立 TOML config file.)
monstache -f /path/to/config.toml
```

# 配置文檔

config.toml

```conf
# toml檔
# connection settings

# connect to MongoDB using the following URL
# MongoDB實例的主節點訪問地址
# /?connect=direct 單節點直接連接
# https://www.mongodb.com/docs/mongodb-shell/connect/
mongo-url = "mongodb://someuser:password@localhost:40001"

# connect to the Elasticsearch REST API at the following node URLs
# Elasticsearch的訪問地址。
elasticsearch-urls = ["https://es1:9200", "https://es2:9200"]

# frequently required settings

# if you need to seed an index from a collection and not just listen and sync changes events
# you can copy entire collections or views from MongoDB to Elasticsearch
direct-read-namespaces = ["mydb.mycollection", "db.collection", "test.test", "db2.myview"]

# if you want to use MongoDB change streams instead of legacy oplog tailing use change-stream-namespaces
# change streams require at least MongoDB API 3.6+
# if you have MongoDB 4+ you can listen for changes to an entire database or entire deployment
# in this case you usually don't need regexes in your config to filter collections unless you target the deployment.
# to listen to an entire db use only the database name.  For a deployment use an empty string.
# 追蹤更新自動同步 mongodb 需使用 replica sets
# 如果要使用MongoDB變更流功能，需要指定此參數。啟用此參數後，oplog追踪會被設置為無效
# MongoDB 版本4以上 可更改 db.collection 使用 db 追蹤整個db
change-stream-namespaces = ["mydb.mycollection", "db.collection", "test.test"]

# 追蹤全部的
change-stream-namespaces = [""]

# additional settings

# if you don't want to listen for changes to all collections in MongoDB but only a few
# e.g. only listen for inserts, updates, deletes, and drops from mydb.mycollection
# this setting does not initiate a copy, it is only a filter on the change event listener
# 通過正則表達式指定需要監聽的集合。
namespace-regex = '^mydb\.mycollection$'

# compress requests to Elasticsearch
gzip = true

# generate indexing statistics
stats = true

# index statistics into Elasticsearch
index-stats = true

# use the following user name for Elasticsearch basic auth
# 訪問Elasticsearch的用戶名。
elasticsearch-user = "someuser"

# use the following password for Elasticsearch basic auth
# 訪問Elasticsearch的用戶密碼。
elasticsearch-password = "somepassword"

# use 4 go routines concurrently pushing documents to Elasticsearch
# 定義連接ES的線程數。默認為4，即使用4個Go線程同時將數據同步到ES。
elasticsearch-max-conns = 4

# use the following PEM file to connections to Elasticsearch
elasticsearch-pem-file = "/path/to/elasticCert.pem"

# validate connections to Elasticsearch
elastic-validate-pem-file = true

# propogate dropped collections in MongoDB as index deletes in Elasticsearch
# 表示當刪除MongoDB集合時，會同時刪除ES中對應的索引。
dropped-collections = true

# propogate dropped databases in MongoDB as index deletes in Elasticsearch
# 表示當刪除MongoDB數據庫時，會同時刪除ES中對應的索引。
dropped-databases = true

# do not start processing at the beginning of the MongoDB oplog
# if you set the replay to true you may see version conflict messages
# in the log if you had synced previously. This just means that you are replaying old docs which are already
# in Elasticsearch with a newer version. Elasticsearch is preventing the old docs from overwriting new ones.
replay = false

# resume processing from a timestamp saved in a previous run
# Monstache會將已成功同步到ES的MongoDB操作的時間戳寫入monstache.monstache集合中。當Monstache因為意外停止時，可通過該時間戳恢復同步任務，避免數據丟失。如果指定了cluster-name，該參數將自動開啟
resume = true

# do not validate that progress timestamps have been saved
resume-write-unsafe = false

# override the name under which resume state is saved
resume-name = "default"

# use a custom resume strategy (tokens) instead of the default strategy (timestamps)
# tokens work with MongoDB API 3.6+ while timestamps work only with MongoDB API 4.0+
# 指定恢復策略。
# Strategy 0 -default- Timestamp based resume of change streams. Compatible with MongoDB API 4.0+.
# Stategy 1 Token based resume of change streams. Compatible with MongoDB API 3.6+.
resume-strategy = 1

# exclude documents whose namespace matches the following pattern
namespace-exclude-regex = '^mydb\.ignorecollection$'

# turn on indexing of GridFS file content
# https://github.com/rwynn/monstache/issues/33
# 另外，為了清楚起見，monstache 僅在配置中啟用文件內容索引時才需要 ingest-attachment 插件。 如果您將以下內容更改為 false monstache 將不會嘗試需要攝取附件的請求。
index-files = false

# turn on search result highlighting of GridFS content
file-highlighting = true

# index GridFS files inserted into the following collections
file-namespaces = ["users.fs.files"]

# print detailed information including request traces
verbose = true

# enable clustering mode
# 指定集群名稱。
cluster-name = 'apollo'

# do not exit after full-sync, rather continue tailing the oplog
exit-after-direct-reads = false
```

## 中間件

```go
package main
import (
    "github.com/rwynn/monstache/monstachemap"
    "strings"
)
// a plugin to convert document values to uppercase
func Map(input *monstachemap.MapperPluginInput) (output *monstachemap.MapperPluginOutput, err error) {
    doc := input.Document
    for k, v := range doc {
        switch v.(type) {
        case string:
            doc[k] = strings.ToUpper(v.(string))
        }
    }
    output = &monstachemap.MapperPluginOutput{Document: doc}
    return
}
```

# 狀況

## 使用golang連接mongo server selection error: server selection timeout, current topology

```
最終排查到問題原因：mongo配置了集群，但是連接時只指定了單節點，mongo沒有發現副本節點，所以連接失敗 解決辦法：在連接時指定單節點連接 connect=direct
```

# Mapping(映射)

[官方文檔 - index-mapping](https://rwynn.github.io/monstache-site/advanced/#index-mapping)

```
默認映射如下：

對於 6.2 之前的 Elasticsearch

Elasticsearch index name    <= MongoDB database name . MongoDB collection name
Elasticsearch type          <= MongoDB collection name
Elasticsearch document _id  <= MongoDB document _id

對於 Elasticsearch 6.2+

Elasticsearch index name    <= MongoDB database name . MongoDB collection name
Elasticsearch type          <= _doc
Elasticsearch document _id  <= MongoDB document _id
```

```conf
# 添加到 TOML 配置文件中來覆蓋每個集合的索引和類型映射：
[[mapping]]
; 應用在 db.collection
namespace = "test.test"
; 命名index
index = "index1"
; 覆蓋類型 不建議
type = "type1"

[[mapping]]
namespace = "test.test2"
index = "index2"
type = "type2"

; 確保在 elasticsearch.yml 中未禁用自動索引創建，或者在使用 Monstache 之前創建目標索引。
; 如果必須控制自動索引創建，請將 monstache 將創建的 elasticsearch.yml 中的所有索引列入白名單。
```

# pm2 執行守護程式

```json
{
  "apps" : [{
    "name"        : "monstache",
    "script"      : "/home/go/bin/monstache",
    "watch"       : true,
    "cwd"         : "/home",
    "args"        : "-f /path/config.toml"
  }]
}
```

```
pm2 start filename.json
```