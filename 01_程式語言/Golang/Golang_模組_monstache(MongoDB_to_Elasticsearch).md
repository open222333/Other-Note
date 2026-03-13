# Golang 模組 monstache(MongoDB to Elasticsearch)

```
可用於將 MongoDB 數據同步到 Elasticsearch 的最全面的庫

monstache 實現實時同步的原理涉及 MongoDB 的 Change Streams、MongoDB Oplog（操作日誌）以及 Elasticsearch 的 Bulk API。

下面是 monstache 實時同步的基本原理：

Change Streams：
MongoDB 提供了 Change Streams API，允許客戶端訂閱對集合的更改。monstache 利用 Change Streams 來監聽 MongoDB 中的變更，包括插入、更新和刪除操作。

Oplog Tailer：
MongoDB 的操作日誌（Oplog）是一個特殊的集合，記錄了對 MongoDB 數據庫的每個寫入操作。monstache 使用 Oplog Tailer 來實時訂閱 Oplog 中的變更。這使得 monstache 能夠在沒有對數據庫性能產生顯著影響的情況下獲取變更。

實時處理：
當 MongoDB 中發生變更時，monstache 會實時處理 Change Streams 或 Oplog 中的變更事件。根據配置，monstache 可以選擇性地過濾和轉換這些事件。

Elasticsearch Bulk API：
處理變更事件後，monstache 將變更數據轉換為符合 Elasticsearch Bulk API 格式的文檔，並將這些文檔批量索引到 Elasticsearch。Bulk API 允許一次性提交多個文檔，提高了索引效率。

實現實時同步：
通過監聽 MongoDB 的變更並實時將這些變更同步到 Elasticsearch，monstache 實現了 MongoDB 數據到 Elasticsearch 的實時同步。

這種基於 Change Streams 和 Oplog 的實時同步機制，使得 monstache 能夠在 MongoDB 中發生變更時迅速捕獲並同步到 Elasticsearch，從而確保兩個數據庫之間的數據保持實時性。這對於支持實時搜索和分析等應用場景非常有用。
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
  - [undefined: "sync/atomic".Int64](#undefined-syncatomicint64)
- [Mapping(映射)](#mapping映射)
- [pm2 執行守護程式](#pm2-執行守護程式)
- [參數詳細說明](#參數詳細說明)
  - [direct-read-dynamic-exclude-regex 匹配規則 範例](#direct-read-dynamic-exclude-regex-匹配規則-範例)
    - [排除指定資料庫以及集合](#排除指定資料庫以及集合)
  - [change-stream-namespaces 與 direct-read-namespaces 差別](#change-stream-namespaces-與-direct-read-namespaces-差別)
    - [direct-read-namespaces](#direct-read-namespaces)
    - [change-stream-namespaces](#change-stream-namespaces)
    - [兩者的差異](#兩者的差異)
      - [同步對象](#同步對象)
      - [使用時機](#使用時機)
      - [範圍](#範圍)
    - [如何搭配使用](#如何搭配使用)
    - [同時存在的運行邏輯](#同時存在的運行邏輯)

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

# 查看所有分支
git branch -a

# 查看所有標籤
git tag -l

# 選擇版本
git checkout <branch-or-tag-to-build>
git checkout v6.7.10

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
# MongoDB collection 的同步設置
direct-read-namespaces = ["mydb.mycollection"]
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
# 可選：對 MongoDB 進行增量同步，而不是從頭開始
# 增量同步（Incremental Sync）是指在資料同步過程中，只同步自上次同步以來的變更，而不是每次都從頭開始同步所有資料。這種方法主要用於優化效能，尤其是當資料量龐大或變更頻繁時，增量同步可以減少不必要的資料傳輸和處理負擔。
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

## undefined: "sync/atomic".Int64

清除 go 快取

```bash
go clean -modcache
```

檢查環境變數：

確保環境變數（例如 GOROOT、GOPATH）被正確設置

```bash
echo $GOROOT
echo $GOPATH
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

# 參數詳細說明

## direct-read-dynamic-exclude-regex 匹配規則 範例

### 排除指定資料庫以及集合

Python 可用

```
^(?!admin|config|local).*|\.(m3_u8|m3u8|account|.*log.*)
```

```
^(?!admin\\.|config\\.|local\\.).*\\.(m3_u8|m3u8|account|.*log.*)
```

## change-stream-namespaces 與 direct-read-namespaces 差別

### direct-read-namespaces

用途：

這個參數用於 一次性讀取 MongoDB 資料 並將其同步到 Elasticsearch。當你啟用 direct-read-namespaces 時，Monstache 會在初始同步時將指定的 MongoDB collections 的所有現有資料完整地讀取並同步到 Elasticsearch。

典型場景：

通常用於首次同步或全量同步。如果你希望 Elasticsearch 包含 MongoDB 中某些 collections 的所有資料，可以使用這個參數。

範例：

```toml
direct-read-namespaces = ["mydb.mycollection"]
```

這會同步 mydb.mycollection 中的所有現有文件到 Elasticsearch。

`注意事項：direct-read-namespaces 主要針對 MongoDB 現有的資料，一次性進行同步，它不會持續監聽變更。`

### change-stream-namespaces

用途：

這個參數用於 監聽 MongoDB 中的實時資料變更，並將這些變更（插入、更新、刪除）同步到 Elasticsearch。啟用 change-stream-namespaces 後，Monstache 會持續監聽指定的 MongoDB collections 中的變更，並且只將變更的部分同步到 Elasticsearch，而不會讀取現有的全部資料。

典型場景：

用於實時同步。如果你只關注 MongoDB 中發生的變更（例如新增、修改或刪除操作），並希望這些變更能夠實時反映在 Elasticsearch 中，可以使用這個參數。

範例：

```toml
change-stream-namespaces = ["mydb.mycollection"]
```

這會監聽 mydb.mycollection 中所有未來的變更，並將變更的文件同步到 Elasticsearch。

`注意事項：change-stream-namespaces 不會同步現有的資料，它僅監聽變更事件。因此，這個參數適合那些只需要同步新資料或變更資料的情況。`

### 兩者的差異

#### 同步對象

direct-read-namespaces：一次性同步現有的所有資料（全量同步）。
change-stream-namespaces：監聽並同步資料變更（增量同步）。

#### 使用時機

direct-read-namespaces：用於首次同步 MongoDB 現有資料到 Elasticsearch，或需要同步整個資料集時使用。
change-stream-namespaces：用於持續同步 MongoDB 中的實時變更，而不是同步整個資料集。

#### 範圍

direct-read-namespaces：只能針對現有的資料集。
change-stream-namespaces：專注於監聽變更事件，實時捕捉資料的更新、刪除和插入操作。

### 如何搭配使用

通常在首次同步時，會使用 direct-read-namespaces 來將整個資料集同步到 Elasticsearch，之後再使用 change-stream-namespaces 來實時監控資料變更，保持 Elasticsearch 與 MongoDB 資料的一致性。這樣既可以獲得完整的資料同步，又可以保持最新的資料變更。

### 同時存在的運行邏輯

direct-read-namespaces 用於在程序啟動時對指定的 collections 進行一次性全量同步，將所有現有資料從 MongoDB 同步到 Elasticsearch。

change-stream-namespaces 用於監聽指定的 collections 的實時變更事件（插入、更新、刪除），並將這些變更同步到 Elasticsearch。

這種組合配置常用於需要同步大量現有資料並保持實時更新的場景，例如數據分析平台、全文檢索系統等，這樣可以兼顧效率和資料一致性。

這種組合在實現 MongoDB 和 Elasticsearch 的同步時非常常見。

```toml
# 全量同步，將 mydb.mycollection 中所有現有資料同步到 Elasticsearch
direct-read-namespaces = ["mydb.mycollection"]

# 實時監控 mydb.mycollection，將所有插入、更新、刪除事件同步到 Elasticsearch
change-stream-namespaces = ["mydb.mycollection"]

# 啟用 oplog 監聽，用於監聽實時變更
enable-oplog = true

# 其他配置選項
elasticsearch-urls = ["http://127.0.0.1:9200"]
gzip = true
elasticsearch-max-conns = 4
resume = true
resume-name = "default"
```