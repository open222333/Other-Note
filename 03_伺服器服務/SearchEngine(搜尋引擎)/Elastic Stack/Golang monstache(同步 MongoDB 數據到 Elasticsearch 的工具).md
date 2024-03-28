# Golang monstache(同步 MongoDB 數據到 Elasticsearch 的工具)

```
基於 Go 語言開發的，可以實現 MongoDB 中數據的增量同步、轉換和索引。Monstache 具有以下特點和功能：

即時同步：Monstache 可以實時監聽 MongoDB 中的變更（如新增、更新、刪除），並將這些變更即時同步到 Elasticsearch 中，保持兩個數據庫之間的數據一致性。

配置彈性：使用者可以通過配置文件來定義同步規則、字段映射、索引設置等，以滿足不同的同步需求和業務邏輯。

多種同步模式：Monstache 支持多種同步模式，如全量同步、增量同步、增量更新等，用戶可以根據實際情況選擇適合的同步模式。

支援腳本和轉換：使用者可以使用 JavaScript 腳本來對同步的數據進行轉換和加工，以滿足特定的業務需求。

性能優化：Monstache 在設計上優化了性能，可以有效地處理大量的數據同步和索引操作，並支援多線程處理。

監控和日誌：Monstache 提供了監控和日誌功能，用戶可以實時查看同步狀態、錯誤日誌等信息，便於故障排除和性能優化。
```

## 目錄

- [Golang monstache(同步 MongoDB 數據到 Elasticsearch 的工具)](#golang-monstache同步-mongodb-數據到-elasticsearch-的工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [配置文檔](#配置文檔)
- [腳本範例(放在 toml 檔 \[\[script\]\])](#腳本範例放在-toml-檔-script)
  - [自動生成別名](#自動生成別名)

## 參考資料

[monstache-showcase/docker-compose.sc.yml](https://github.com/rwynn/monstache-showcase/blob/a25cddeedc9e8f1481aa7de19cd634158792b28c/docker-compose.sc.yml#L53)

[monstache Configuration](https://rwynn.github.io/monstache-site/config/)

[Monstache](https://rwynn.github.io/monstache-site/start/)

[從mongodb到elasticsearch的實時同步 - 包含分詞器](https://www.cxyzjd.com/article/zhangyonguu/80914496)

# 安裝

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

```bash
# 安裝gcc
yum install gcc -y

# 在 CentOS 上安裝 Go
wget https://golang.org/dl/go1.17.1.linux-amd64.tar.gz
tar -C /usr/local -xzf go1.17.1.linux-amd64.tar.gz
export PATH=$PATH:/usr/local/go/bin
export GOPATH=$HOME/go
source ~/.bashrc
go version

# somewhere outside your $GOPATH(golang 第三方套件安裝路徑)
# cd ~/build

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

## 配置文檔

```conf
# toml檔
# connection settings

# connect to MongoDB using the following URL
# MongoDB實例的主節點訪問地址
# /?connect=direct 直連
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
# 另外，為了清楚起見，monstache 僅在配置中啟用文件內容索引時才需要 ingest-attachment 插件。 如果將以下內容更改為 false monstache 將不會嘗試需要攝取附件的請求。
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

# 排除
direct-read-dynamic-exclude-regex = ".*(dbname1|dbname2).*\\.(m3_u8|m3u8|.*log.*).*"
direct-read-dynamic-exclude-regex = ".*\\.(.*m3_u8.*|.*m3u8.*|account|.*log.*)"
direct-read-dynamic-exclude-regex = "(admin|config|local)\\..*|.*\\.(.*m3_u8.*|.*m3u8.*|account|.*log.*|login)"
direct-read-dynamic-exclude-regex = ".*\\.(.*m3_u8.*|.*m3u8.*|account|.*log.*|login)"
direct-read-split-max = 1

# direct-read-split-max 設置確實可能會導致使用大量記憶體，特別是當設置為較大的值時。
# 這是因為該設置決定了一次處理的最大文件數量，如果設置得太大，系統可能需要同時處理大量的文件，導致記憶體壓力增加。
# 為了降低記憶體使用量，可以嘗試將 direct-read-split-max 設置為較小的值，例如設置為 1 或 2，這樣可以減少系統同時處理的文件數量，從而減輕記憶體壓力。
# 另外，還可以優化的程序邏輯，以減少在單個操作中需要處理的數據量，從而進一步減少記憶體需求。
direct-read-split-max = 1

[[script]]
script="""
module.exports = function (doc, ns) {
  var index = "{名稱}-{日期}." + ns.split(".")[1];
  doc._meta_monstache = { index: index };
  return doc;
}
"""
```

# 腳本範例(放在 toml 檔 \[\[script\]\])

## 自動生成別名

```JavaScript
module.exports = function (doc, ns) {
  var indexName = "{名稱}-{日期}." + ns.split(".")[1];
  var aliasName = "my_alias_" + ns.split(".")[1];

  doc._meta_monstache = { index: indexName };

  // 使用 Elasticsearch 的 REST API 創建別名
  var createAliasUrl = "http://localhost:9200/_aliases";
  var aliasRequestBody = {
    actions: [
      { add: { index: indexName, alias: aliasName } }
    ]
  };

  fetch(createAliasUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(aliasRequestBody)
  })
  .then(response => response.json())
  .then(data => {
    console.log("Alias created:", data);
  })
  .catch(error => {
    console.error("Error creating alias:", error);
  });

  return doc;
}
```

`自動生成別名並刪除舊的別名`

```JavaScript
module.exports = function (doc, ns, raw) {
  var indexName = "{名稱}-{日期}." + ns.split(".")[1];
  var aliasName = "my_alias_" + ns.split(".")[1];

  doc._meta_monstache = { index: indexName };

  // 使用 Elasticsearch 的 REST API 創建別名
  var createAliasUrl = "http://localhost:9200/_aliases";
  var aliasRequestBody = {
    actions: [
      { add: { index: indexName, alias: aliasName } }
    ]
  };

  // 刪除舊的索引別名
  var deleteAliasUrl = "http://localhost:9200/_aliases";
  var deleteAliasRequestBody = {
    actions: [
      { remove_index: { index: "*", alias: aliasName } }
    ]
  };

  // 發送創建別名的請求
  fetch(createAliasUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(aliasRequestBody)
  })
  .then(response => response.json())
  .then(data => {
    console.log("Alias created:", data);

    // 成功創建別名後，發送刪除舊別名的請求
    fetch(deleteAliasUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(deleteAliasRequestBody)
    })
    .then(response => response.json())
    .then(data => {
      console.log("Old aliases deleted:", data);
    })
    .catch(error => {
      console.error("Error deleting old aliases:", error);
    });
  })
  .catch(error => {
    console.error("Error creating alias:", error);
  });

  return doc;
}
```
