# MongoDB 筆記 Replica-Set(複本集)

```
Replica Set（複本集）是MongoDB中的一個機制，用於提供數據的高可用性和冗餘性。
它通常包含多個MongoDB實例，其中一個是主節點（Primary），其餘為從節點（Secondary）。
這些節點一起形成一個複本集群。

以下是Replica Set的一些重要概念和功能：

主節點（Primary）： 主節點負責接收所有寫入操作，並將這些操作複製到所有從節點。
從節點（Secondary）： 從節點是主節點的複製，它們接收來自主節點的操作並進行重放，以確保數據的冗餘性和可用性。
選舉機制： 如果主節點失敗，複本集會自動進行選舉，選擇一個新的主節點。這確保即使其中一個節點失效，整個系統仍然能夠運作。
冗餘性： Replica Set提供了數據的冗餘存儲，這意味著即使某個節點失效，其他節點仍然可以提供數據。
自動故障轉移： 當主節點失效時，複本集能夠自動選舉一個新的主節點，而不需要手動介入。
讀取分佈： 客戶端可以從複本集中的任何節點讀取數據，而不僅僅是主節點。這允許分佈式讀取操作，提高性能。
優先級設置： 可以配置從節點的優先級，以影響選舉時的節點優先級。這可以用於確保某個節點更有可能被選為主節點。
延遲成員： 可以將某些從節點配置為延遲成員，這樣可以在某些情況下提供數據恢復或故障恢復的時間窗口。
```

## 目錄

- [MongoDB 筆記 Replica-Set(複本集)](#mongodb-筆記-replica-set複本集)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Docker 部署](#docker-部署)
    - [docker-compose.yml](#docker-composeyml)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [建立 副本集](#建立-副本集)
  - [删除 節點](#删除-節點)
  - [key驗證 生成key 將生成的key加入](#key驗證-生成key-將生成的key加入)
  - [MongoDB 實例配置為使用複製集 (replica set)](#mongodb-實例配置為使用複製集-replica-set)
  - [設置 主-讀寫 從-只讀不寫(主掛了不會升為主)](#設置-主-讀寫-從-只讀不寫主掛了不會升為主)
- [例外狀況](#例外狀況)
  - [MongoServerError\[InvalidReplicaSetConfig\]: Our replica set config is invalid or we are not a member of it](#mongoservererrorinvalidreplicasetconfig-our-replica-set-config-is-invalid-or-we-are-not-a-member-of-it)
    - [強制重建副本集](#強制重建副本集)
- [特別工具](#特別工具)
  - [匯出匯入腳本](#匯出匯入腳本)

## 參考資料

[MongoDB Sharding 簡介](https://www.mongodb.com/basics/clusters)

[Read Preference](https://docs.mongodb.com/manual/core/read-preference/)

[eugenechen0514/demo_mongo_cluster](https://github.com/eugenechen0514/demo_mongo_cluster)

### 心得相關

[為什麼要用 Docker？如何用 Docker 構築不同 MongoDB 架構？](https://tw.alphacamp.co/blog/mongodb-with-docker)

[30-22之MongoDB的副本集 replica set(2)---使用Docker建立MongoDB Cluster](https://ithelp.ithome.com.tw/articles/10187117)

[使用 docker 建立 MongoDB Replica Set](https://blog.yowko.com/docker-mongodb-replica-set/)

[MongoDB副本集(一主兩從)讀寫分離、故障轉移功能環境部署記錄](https://iter01.com/68390.html)

[Configure Non-Voting Replica Set Member - 設置不參與投票的成員](https://docs.mongodb.com/manual/tutorial/configure-a-non-voting-replica-set-member/)

[MongoDB Sharding 分散式儲存架構建置 (概念篇)](https://blog.toright.com/posts/4552/mongodb-sharding-%E5%88%86%E6%95%A3%E5%BC%8F%E5%84%B2%E5%AD%98%E6%9E%B6%E6%A7%8B%E5%BB%BA%E7%BD%AE-%E6%A6%82%E5%BF%B5%E7%AF%87.html)

[MongoDB replica set 設定 實體機](https://castion2293.medium.com/mongodb-replica-set-%E8%A8%AD%E5%AE%9A-d890e174d47b)

[DockerCompose 建立 MongoDB Replica Set](https://blog.yowko.com/docker-compose-mongodb-replica-set/)

[Replica Set從副本集中删除成員](https://blog.csdn.net/yaomingyang/article/details/73822775)

[MongoDB 副本集移除成员](https://blog.csdn.net/Alen_Liu_SZ/article/details/101995235)

[配置MongoDB replication遇到的坑](http://www.knockatdatabase.com/2022/06/02/mongodb-replication-erros-statestr-startup/)

# 安裝

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

```bash
# 修改 mongod.conf
vim /etc/mongod.conf
```

```yaml
net:
    port: 27017
    # 修改成 0.0.0.0 對外開放
    bindIp: 0.0.0.0

# 常用
replication:
    oplogSizeMB: 15000
    # MongoServerError[NewReplicaSetConfigurationIncompatible]
    # 如果名稱不一致 加入節點會提示上方錯誤
    replSetName: RS

# 加入 key驗證 功能(參考 指令 key驗證 生成key) 並傳送到每個節點
security:
    authorization: enabled
    keyFile: /var/lib/mongodb/mongo.key
```

配置 /etc/hosts (每台主機都要)

```sh
# 在 /etc/hosts 定義安裝的 hostname 與 IP Address
vim /etc/hosts
```

範例

```
192.168.1.1	mongo-primary
192.168.1.2	mongo-secondary
```

## Docker 部署

### docker-compose.yml

```yml
version: "3"
services:
	mongo1:
		container_name: mongo1
		image: mongo
		ports:
		  - 27017:27017
		restart: always
		# command: --replSet rs0 # 啟用replSet
		entrypoint: [ "mongod","--port","27017", "--bind_ip_all",   "--replSet", "rs0" ]
	mongo2:
		container_name: mongo2
		image: mongo
		ports:
		  - 27027:27027
		restart: always
		entrypoint: [ "mongod","--port","27027", "--bind_ip_all",   "--replSet", "rs0" ]
	mongo3:
		container_name: mongo3
		image: mongo
		ports:
		  - 27037:27037
		restart: always
		entrypoint: [ "mongod","--port","27037", "--bind_ip_all",   "--replSet", "rs0" ]
		healthcheck:
		  test: ["CMD","mongo","--host","mongo1","--port","27017",  "--eval", 'rs.initiate( { _id : "rs0",members: [{ _id: 0,     host: "mongo1:27017" },{ _id: 1, host: "mongo2:27027" },{   _id: 2, host: "mongo3:27037" }   ]})']
		  interval: 15s
		  timeout: 10s
		  retries: 3
		  start_period: 10s
```

`設定 hosts`

```bash
# mongo 內部只認得設定 Replica Set 的 member host，但 host 機器不認得 container name
echo "127.0.0.1 mongo1\n127.0.0.1 mongo2\n127.0.0.1 mongo3" >> /etc/hosts

# 啟動 MongoDB Replica Set
docker-compose up -d
```

```bash
# 初始化複製集： 使用 MongoDB 的 shell 連接到容器並執行 rs.initiate() 來初始化複製集。
docker exec -it mongo mongo
```

```javascript
// 在 MongoDB shell 中執行
rs.initiate()
// 在 MongoDB shell 中確認複製集配置
rs.conf()
```

## 配置文檔

通常在 `/etc/mongod.conf`

### 基本範例

```yml
net:
    port: 27017
    # 修改成 0.0.0.0 對外開放
    bindIp: 0.0.0.0

# 常用
replication:
    oplogSizeMB: 15000
    # MongoServerError[NewReplicaSetConfigurationIncompatible]
    # 如果名稱不一致 加入節點會提示上方錯誤
    replSetName: RS

# 加入 key驗證 功能(參考 指令 key驗證 生成key) 並傳送到每個節點
security:
    authorization: enabled
    keyFile: /var/lib/mongodb/mongo.key
```

# 指令

## 建立 副本集

```JavaScript
rs.initiate({
    _id: "RS",
    members: [
        { _id: 0, host: "192.168.1.1:27017" }
    ]
});
```

```JavaScript
// 進入mongodb 輸入指令
// 進入mongo bash 指令
// mongo
cfg = {
    "_id": "RS",	// replica set 名字
    "members": [{
        "_id": 0,
        "host": "<hostname>:<port>" //
    },
    {
        "_id": 1,
        "host": "<hostname>:<port>",
        // 以下是實作 Secondary-Only
        "votes": 0, // 要設定priority為0 votes須為0
        "priority":0 // priority為0 不會變成 PRIMARY
    },
    {
        "_id": 2,
        "host": "<hostname>:<port>",
        "votes": 0,
        "priority":0
    }
    ]
};

// 返回包含當前副本集 配置的文檔。
// https://docs.mongodb.com/manual/reference/method/rs.conf/#mongodb-method-rs.conf
rs.conf()

// 通過指令新增 Replica Set 節點
rs.add("xxx.xxx.xxx.xxx:xxxx")
rs.add("mongodb-a3:27019")

// 需進入 PRIMARY 操作
// 通過指令刪除 Replica Set 節點
rs.remove("mongod3.example.net:27017")

// 建立 Replica_Set 啟用副本模式
rs.initiate(cfg);
rs.initiate()

// 透過 rs.status() 查看 Replica Set 設定狀態
rs.status()

// add secondary to replica set 將次要添加到副本集
rs.add({host: "SECONDARY-IP:27018", priority: 0.5})

// add arbiter to replica set 將仲裁器添加到副本集
rs.addArb("ARBITER-IP:27018")
```

## 删除 節點

```JavaScript
rs.remove("mongod3.example.net:27017")
```

## key驗證 生成key 將生成的key加入

```bash
openssl rand -base64 741 > /var/lib/mongodb/mongodb-keyfile
chmod 600 /var/lib/mongodb/mongodb-keyfile
```

RedHat (CentOS)

```sh
chown mongodb.mongodb /var/lib/mongodb/mongodb-keyfile
```

Debian (Ubuntu)

```sh
chown mongodb:mongodb /var/lib/mongodb/mongodb-keyfile
```

分發到其他節點

```sh
scp /var/lib/mongo/mongodb-keyfile <user>@<other-node>:/var/lib/mongo/mongodb-keyfile
```

其他節點 需注意 key 權限

```sh
chmod 600 /var/lib/mongodb/mongo.key

chown mongodb.mongodb /var/lib/mongodb/mongodb-keyfile
# or
chown mongodb:mongodb /var/lib/mongodb/mongo.key
```

## MongoDB 實例配置為使用複製集 (replica set)

```yml
version: "3"
services:
    mongo:
        container_name: mongo
        image: mongo:4.2
        restart: always
        ports:
            - 27017:27017
        volumes:
            - ./data/mongo:/data/db
        mem_limit: 4g
        mem_reservation: 4g
        command: ["--replSet", "rs0"]  # 新增這一行
        # command: mongod --replSet rs0  # 设置副本集名称为 rs0
```

```bash
# 進入 MongoDB 容器
docker exec -it mongo bash
# 連接到 MongoDB
mongo
```

```Javascript
// 初始化副本集
rs.initiate()
// 驗證副本集的狀態
rs.status()
```

## 設置 主-讀寫 從-只讀不寫(主掛了不會升為主)

```
Secondary-Only:實作方式
members[n].priority 為0 vote也要為0
```

```JavaScript
// 1.Connect mongosh to the replica set primary:
mongosh --host "<hostname>:<port>"
// 2.Retrieve the Replica Configuration
cfg = rs.conf();
// 3.Configure the Member to be Non-Voting
// n是ID
cfg.members[n].votes = 0;
cfg.members[n].priority = 0;

cfg = rs.conf();
cfg.members[1].votes = 0;
cfg.members[1].priority = 0;
cfg.members[2].votes = 0;
cfg.members[2].priority = 0;
// 4.Reconfigure the Replica Set with the New Configuration
rs.reconfig(cfg);

// 一次只能更改一個成員

cfg = rs.conf();
cfg.members[1].votes = 0;
cfg.members[1].priority = 0;
rs.reconfig(cfg);

cfg = rs.conf();
cfg.members[2].votes = 0;
cfg.members[2].priority = 0;
rs.reconfig(cfg);
```

# 例外狀況

## MongoServerError[InvalidReplicaSetConfig]: Our replica set config is invalid or we are not a member of it

```
MongoServerError[InvalidReplicaSetConfig]: Our replica set config is invalid or we are not a member of it 表示當前的節點配置不屬於副本集，或副本集的配置無效。可能的原因包括：

副本集名稱（replica set name）不匹配。
當前節點的配置在副本集設定中不存在。
網絡連線問題導致無法與副本集的其他節點通信。
```

### 強制重建副本集

停止所有 MongoDB 節點

```sh
systemctl stop mongod
```

清除現有的副本集元數據 刪除數據目錄中的所有內容（確保這些節點是全新的或數據不重要）

```sh
rm -rf /var/lib/mongodb/*
```

修改 MongoDB 配置
在每個節點的配置文件（通常是 /etc/mongod.conf）中，設置新的副本集名稱

```yaml
replication:
  replSetName: "newReplicaSet"
```

# 特別工具

## 匯出匯入腳本

```env
# 遠端 MongoDB 設定
REMOTE_HOST=遠端主機IP或主機名
REMOTE_PORT=27017
REMOTE_USER=遠端用戶名
REMOTE_PASS=遠端密碼
REMOTE_AUTH_DB=admin

# 本地 MongoDB 設定
LOCAL_HOST=127.0.0.1
LOCAL_PORT=27017
LOCAL_USER=本地用戶名
LOCAL_PASS=本地密碼
LOCAL_AUTH_DB=admin

# 備份目錄
DUMP_DIR=/tmp/mongo_backup

# 啟用清理備份
CLEAN_BACKUP=true
```

`backup_and_restore.sh`

!!! bash backup_and_restore.sh

```sh
#!/bin/bash

# 載入 mongo.env 配置
if [ -f "mongo.env" ]; then
    source mongo.env
else
    echo "mongo.env 檔案不存在，請確認配置是否正確。"
    exit 1
fi

# 驗證變數是否正確讀取
echo "REMOTE_HOST=$REMOTE_HOST"
echo "REMOTE_PORT=$REMOTE_PORT"
echo "DUMP_DIR=$DUMP_DIR"

# 建立備份目錄
mkdir -p "$DUMP_DIR"

echo "=== 開始從遠端主機匯出資料 ==="

# 構建 mongodump 指令
MONGO_CMD="mongodump --host $REMOTE_HOST --port $REMOTE_PORT --out $DUMP_DIR"

# 如果有設定用戶名和密碼
if [ -n "$REMOTE_USER" ] && [ -n "$REMOTE_PASS" ]; then
    MONGO_CMD="mongodump --host $REMOTE_HOST --port $REMOTE_PORT --username $REMOTE_USER --password $REMOTE_PASS --authenticationDatabase $REMOTE_AUTH_DB --out $DUMP_DIR"
fi

# 印出執行的 mongodump 指令
echo "執行的 mongodump 指令：$MONGO_CMD"

# 執行 mongodump
$MONGO_CMD

if [ $? -ne 0 ]; then
    echo "匯出失敗，請檢查連線與參數是否正確。"
    exit 1
fi

echo "=== 匯出完成，備份檔案存放於 $DUMP_DIR ==="

echo "=== 開始匯入到本地主機 ==="

# 匯入到本地 MongoDB
MONGO_RESTORE_CMD="mongorestore --host $LOCAL_HOST --port $LOCAL_PORT --drop $DUMP_DIR"

# 如果有設定用戶名和密碼
if [ -n "$LOCAL_USER" ] && [ -n "$LOCAL_PASS" ]; then
    MONGO_RESTORE_CMD="mongorestore --host $LOCAL_HOST --port $LOCAL_PORT --username $LOCAL_USER --password $LOCAL_PASS --authenticationDatabase $LOCAL_AUTH_DB --drop $DUMP_DIR"
fi

# 印出執行的 mongorestore 指令
echo "執行的 mongorestore 指令：$MONGO_RESTORE_CMD"

# 執行 mongorestore
$MONGO_RESTORE_CMD

if [ $? -ne 0 ]; then
    echo "匯入失敗，請檢查本地 MongoDB 是否正在執行。"
    exit 1
fi

echo "=== 匯入完成 ==="

# 根據環境變數來決定是否清理備份資料
if [ "$CLEAN_BACKUP" == "true" ]; then
    echo "=== 清理備份資料 ==="
    rm -rf "$DUMP_DIR"
    echo "備份資料已清理"
else
    echo "=== 保留備份資料 ==="
fi

echo "=== 作業完成 ==="
```