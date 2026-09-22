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
  - [修改 rs 主機名成內網ＩＰ](#修改-rs-主機名成內網ｉｐ)
  - [檢查同步狀態](#檢查同步狀態)
  - [強制升為 Primary](#強制升為-primary)
  - [重置副本集](#重置副本集)
  - [設定 priority 與 votes](#設定-priority-與-votes)
- [例外狀況](#例外狀況)
  - [Secondary 維護重啟後意外當選 Primary](#secondary-維護重啟後意外當選-primary)
  - [MongoServerError\[InvalidReplicaSetConfig\]: Our replica set config is invalid or we are not a member of it](#mongoservererrorinvalidreplicasetconfig-our-replica-set-config-is-invalid-or-we-are-not-a-member-of-it)
    - [強制重建副本集](#強制重建副本集)
- [MongoDB Replica Set 跨叢集做 HA](#mongodb-replica-set-跨叢集做-ha)
- [特別工具](#特別工具)
  - [匯出匯入腳本](#匯出匯入腳本)
  - [Mongo 連線錯誤檢測工具（mongo_conn_doctor.py）](#mongo-連線錯誤檢測工具mongo_conn_doctorpy)

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
storage:
  dbPath: /var/lib/mongodb
  journal:
    enabled: true
  wiredTiger:
    engineConfig:
      cacheSizeGB: 24

systemLog:
  destination: file
  logAppend: true
  logRotate: reopen
  path: /var/log/mongodb/mongod.log

net:
    port: 27017
    # 修改成 0.0.0.0 對外開放
    bindIp: 0.0.0.0
    maxIncomingConnections: 65536

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

副本集管理權限 必須用 root 或 clusterAdmin 帳號登入主節點 (PRIMARY)

```sh
mongo -u "admin" -p "你的密碼" --authenticationDatabase "admin"
```

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
openssl rand -base64 741 > /var/lib/mongodb/<mongodb-keyfile>
chmod 600 /var/lib/mongodb/<mongodb-keyfile>
```

RedHat (CentOS)

```sh
chown mongodb.mongodb /var/lib/mongodb/<mongodb-keyfile>
```

Debian (Ubuntu)

```sh
chown mongodb:mongodb /var/lib/mongodb/<mongodb-keyfile>
```

分發到其他節點

```sh
scp /var/lib/mongo/<mongodb-keyfile> <user>@<other-node>:/var/lib/mongo
```

```sh
scp /var/lib/mongodb/<mongodb-keyfile> <user>@<other-node>:/var/lib/mongodb
```

其他節點 需注意 key 權限

```sh
chmod 400 /var/lib/mongodb/mongo.key

chown mongodb.mongodb /var/lib/mongodb/<mongodb-keyfile>
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

priority=0 與 votes 的關係有兩種用法，效果不同：

| 設定 | 效果 |
|------|------|
| `priority: 0, votes: 1` | 永遠不成為 Primary，但保留投票權（推薦） |
| `priority: 0, votes: 0` | 完全排除在選舉之外（Non-Voting Member，不推薦用在正常 Secondary） |

> ⚠️ 若所有節點都設為 `votes: 0` 只留 Primary 有投票權，Primary 重啟時將無法選出新 Primary 導致叢集卡住。正常 Secondary 建議保留 `votes: 1`。

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

// 一次只能更改一個成員 (只設定 Secondary)

cfg = rs.conf();
cfg.members[1].votes = 0;
cfg.members[1].priority = 0;
rs.reconfig(cfg);

cfg = rs.conf();
cfg.members[2].votes = 0;
cfg.members[2].priority = 0;
rs.reconfig(cfg);
```

## 設定 priority 與 votes

| 參數 | 說明 |
|------|------|
| `priority` | 選舉優先權，值越高越優先成為 Primary；`0` 表示永遠不成為 Primary |
| `votes` | 投票權，`1` 表示參與選舉投票，`0` 表示無投票權 |

> ⚠️ `priority: 0` 不等於 `votes: 0`。Secondary 保留 `votes: 1` 可繼續參與投票但不會成為 Primary。
> 若將預期的 Primary 錯誤設為 `priority: 0, votes: 0`，選舉時它將完全被排除，導致其他節點意外當選。

`固定 Primary / Secondary 角色`

```javascript
cfg = rs.conf()

// 預期 Primary：調高 priority，確保選舉優先
cfg.members[0].priority = 10
cfg.members[0].votes = 1

// 預期 Secondary：priority=0 不成為 Primary，保留投票權
cfg.members[1].priority = 0
cfg.members[1].votes = 1

rs.reconfig(cfg)

// 確認設定
rs.conf()
```

`每次 rs.reconfig() 後務必執行 rs.conf() 確認 priority 與 votes 正確。`

---

## 修改 rs 主機名成內網ＩＰ

```JavaScript
cfg = rs.conf();
cfg.members[n].host = "192.168.148.19:27017";
rs.reconfig(cfg, { force: true })
```

確保 n 是正確的索引

在配置中，cfg.members[n] 中的 n 應該是你想要修改的節點索引。

如果你不確定索引值，可以先列出 cfg.members 的內容，來找到正確的索引。

```JavaScript
cfg = rs.conf();
cfg.members.forEach((member, index) => {
    print(`Index: ${index}, Host: ${member.host}`);
});
```

## 檢查同步狀態

```JavaScript
rs.printReplicationInfo();
```

## 強制升為 Primary

```JavaScript
rs.reconfig(
  {
    _id: "<RS>",
    members: [
      { _id: 0, host: "<IP>:27017" }
    ]
  },
  { force: true }
)
```

## 重置副本集

停掉 MongoDB

```sh
systemctl stop mongod
```

刪除 local 資料夾（裡面存放副本集狀態）

```sh
rm -rf /var/lib/mongo/local
```

重新啟動 MongoDB

```sh
systemctl start mongod
```

# 例外狀況

## Secondary 維護重啟後意外當選 Primary

**症狀**：Secondary 節點維護重啟後觸發選舉，意外成為 Primary，原本的 Primary 降為 Secondary，應用程式寫入失敗。

**根本原因**：原本 Primary 的設定為 `priority: 0, votes: 0`，選舉時完全被排除，Secondary 成為唯一候選人自動當選。

**排查步驟**

```bash
# 1. 連入目前的 Primary
mongo -u root -p --authenticationDatabase admin
```

```javascript
// 2. 確認目前角色
rs.status()

// 3. 確認設定
rs.conf()
// 找出哪個節點的 priority / votes 設定異常
```

**修正（在 Primary 上執行）**

```javascript
cfg = rs.conf()

// 恢復應為 Primary 的節點（priority 調高，補回投票權）
cfg.members[0].priority = 10
cfg.members[0].votes = 1

// Secondary 保持不成為 Primary，保留投票權
cfg.members[1].priority = 0
cfg.members[1].votes = 1

rs.reconfig(cfg)

// 確認選舉結果
rs.conf()
rs.status()
```

**預防措施**

- 每次 `rs.reconfig()` 後執行 `rs.conf()` 確認 `priority` 與 `votes` 正確
- 維護前先確認各節點設定，避免觸發異常選舉
- 建立監控告警，Primary 節點角色變更時立即通知

---

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

# MongoDB Replica Set 跨叢集做 HA

```
```

# 特別工具

## 匯出匯入腳本

鎖定資料庫 (只讀模式)

```JavaScript
use admin
db.fsyncLock()
```

解鎖資料庫

```JavaScript
use admin
db.fsyncUnlock()
```

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
# echo "REMOTE_HOST=$REMOTE_HOST"
# echo "REMOTE_PORT=$REMOTE_PORT"
# echo "DUMP_DIR=$DUMP_DIR"

# 顯示載入的配置
echo "=== 配置參數 ==="
echo "遠端主機：$REMOTE_HOST"
echo "遠端埠號：$REMOTE_PORT"
echo "備份目錄：$DUMP_DIR"
echo "本地主機：$LOCAL_HOST"
echo "本地埠號：$LOCAL_PORT"
echo "================="

# 驗證必需變數是否存在
# if [[ -z "$REMOTE_HOST" || -z "$REMOTE_PORT" || -z "$DUMP_DIR" || -z "$LOCAL_HOST" || -z "$LOCAL_PORT" ]]; then
#     echo "錯誤：REMOTE_HOST、REMOTE_PORT、DUMP_DIR、LOCAL_HOST 或 LOCAL_PORT 未設定，請檢查 mongo.env。"
#     exit 1
# fi

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

## Mongo 連線錯誤檢測工具（mongo_conn_doctor.py）

連不上某個 Mongo（或連得上但行為怪怪的）時，依序跑過一系列獨立診斷步驟，把「到底卡在哪一層」找出來：

```
DNS 解析 → TCP 通不通 → (可選) SSH tunnel → Mongo handshake
（含 replica set 拓樸／內網 IP 偵測）→ 帳號密碼驗證 → 實際查詢指定的 collection
```

每一步都獨立 try/except，一步失敗不影響後面繼續跑；最後印出結構化報告，針對常見錯誤附上具體建議——尤其是**replica set 成員為內網 IP、導致 `ReplicaSetNoPrimary` / `No replica set members found` 的情況**：一般連線方式（不加 `directConnection`）會嘗試連去 replica set 回報的其他成員做拓樸探索，如果那些成員是內網 IP，外部主機連不到就會卡住；解法是連線時固定加上 `directConnection=True` + `readPreference=primaryPreferred`（或 secondary/nearest），跳過拓樸探索、只認指定的這一個 host。

**全程只讀**：唯一會對資料庫執行的指令是 `ping` / `hello`（或 `ismaster`）/ `find_one` / `estimated_document_count`，不會寫入或修改任何資料。單一檔案、零額外相依（除了 `pymongo` 本身），可以整個複製到其他專案的 `src/` 底下直接 `import` 使用。

```python
#!/usr/bin/env python3
"""mongo_conn_doctor.py — Mongo 連線錯誤檢測工具。

用途：連不上某個 Mongo（或連得上但行為怪怪的）時，一次跑過一系列獨立診斷步驟，
把「到底卡在哪一層」找出來：

    DNS 解析 → TCP 通不通 → (可選) SSH tunnel → Mongo handshake
    （含 replica set 拓樸／內網 IP 偵測）→ 帳號密碼驗證 → 實際查詢指定的 collection

每一步都獨立 try/except，一步失敗不影響後面繼續跑；最後印出結構化報告，
針對常見錯誤附上具體建議（尤其是 replica set 成員為內網 IP、導致
ReplicaSetNoPrimary / No replica set members found 的情況）。

**全程只讀**：唯一會對資料庫執行的指令是 ping / hello（或 ismaster）/
find_one / estimated_document_count，不會寫入或修改任何資料。

**單一檔案、零額外相依（除了 pymongo 本身）**：這支檔案不依賴同目錄下任何其他
模組，可以整個複製到其他專案的 src/ 底下直接 import 使用，不用連 conf/、src/
一起搬過去。
"""
from __future__ import annotations

import argparse
import ipaddress
import re
import socket
import sys
import time
from configparser import ConfigParser
from dataclasses import dataclass

try:
    from pymongo import MongoClient
    from pymongo.errors import OperationFailure, PyMongoError
except ImportError:  # pragma: no cover - 由各步驟自己檢查、給出友善訊息
    MongoClient = None
    OperationFailure = PyMongoError = Exception


OK, WARN, FAIL, SKIP = "OK", "WARN", "FAIL", "SKIP"
_ICON = {OK: "✓", WARN: "⚠", FAIL: "✗", SKIP: "·"}


# ---------------------------------------------------------------------------
# 報告結構
# ---------------------------------------------------------------------------

@dataclass
class StepResult:
    name: str
    status: str
    message: str
    detail: str = ""
    suggestion: str = ""


class Report:
    def __init__(self):
        self.steps: list[StepResult] = []

    def add(self, name, status, message, detail="", suggestion=""):
        result = StepResult(name, status, message, detail, suggestion)
        self.steps.append(result)
        return result

    def worst_status(self):
        if any(s.status == FAIL for s in self.steps):
            return FAIL
        if any(s.status == WARN for s in self.steps):
            return WARN
        return OK

    def render(self):
        lines = ["=" * 78, "  mongo_conn_doctor 診斷報告", "=" * 78]
        for s in self.steps:
            lines.append("")
            lines.append(f"[{_ICON[s.status]} {s.status}] {s.name}")
            lines.append(f"  {s.message}")
            for line in s.detail.splitlines():
                lines.append(f"    {line}")
            if s.suggestion:
                lines.append(f"  → 建議：{s.suggestion}")

        counts = {OK: 0, WARN: 0, FAIL: 0, SKIP: 0}
        for s in self.steps:
            counts[s.status] += 1

        lines.append("")
        lines.append("-" * 78)
        lines.append(
            f"共 {len(self.steps)} 個檢查項目："
            f"通過 {counts[OK]}　警告 {counts[WARN]}　失敗 {counts[FAIL]}　略過 {counts[SKIP]}"
        )
        lines.append("=" * 78)
        return "\n".join(lines)


# ---------------------------------------------------------------------------
# 連線參數：CLI 直填 或 重用既有 config.ini
# ---------------------------------------------------------------------------

def _load_ini_section(path, section_name):
    parser = ConfigParser()
    if not parser.read(path):
        raise FileNotFoundError(f"讀不到設定檔：{path}")
    if section_name not in parser:
        raise KeyError(
            f"設定檔 {path} 裡沒有 [{section_name}] 這個區塊，"
            f"現有區塊：{', '.join(parser.sections())}"
        )
    return parser[section_name]


def _conn_defaults(timeout):
    return {
        "host": None, "port": 27017, "database": "avnight", "collection": None,
        "username": None, "password": None, "auth_source": None, "replica_set": None,
        "ssh_host": None, "ssh_port": 22, "ssh_username": None, "ssh_password": None,
        "ssh_private_key_path": None, "ssh_private_key_passphrase": None,
        "tls": False, "timeout": timeout,
    }


def _apply_ini_section(conn, config_path, section_name):
    section = _load_ini_section(config_path, section_name)
    conn["host"] = section.get("HOST", "").strip() or None
    conn["port"] = section.getint("PORT", fallback=27017)
    conn["database"] = section.get("DATABASE", fallback="avnight")
    conn["collection"] = section.get("COLLECTION", "").strip() or None
    conn["username"] = section.get("USERNAME", "").strip() or None
    conn["password"] = section.get("PASSWORD", "").strip() or None
    conn["auth_source"] = section.get("AUTH_SOURCE", "").strip() or None
    conn["replica_set"] = section.get("REPLICA_SET", "").strip() or None
    conn["ssh_host"] = section.get("SSH_HOST", "").strip() or None
    conn["ssh_port"] = section.getint("SSH_PORT", fallback=22)
    conn["ssh_username"] = section.get("SSH_USERNAME", "").strip() or None
    conn["ssh_password"] = section.get("SSH_PASSWORD", "").strip() or None
    conn["ssh_private_key_path"] = section.get("SSH_PRIVATE_KEY_PATH", "").strip() or None
    conn["ssh_private_key_passphrase"] = section.get("SSH_PRIVATE_KEY_PASSPHRASE", "").strip() or None


def build_conn(args):
    """把 --config/--section（可選）+ CLI 參數合併成一份連線設定 dict。
    CLI 給的值一律優先於 ini 裡的值。給 CLI 的 main() 用；程式呼叫請直接用 diagnose()。
    """
    conn = _conn_defaults(args.timeout)

    if args.config:
        if not args.section:
            raise ValueError("有給 --config 就一定要搭配 --section 指定要讀哪個區塊")
        _apply_ini_section(conn, args.config, args.section)

    overrides = {
        "host": args.host, "port": args.port, "database": args.database,
        "collection": args.collection, "username": args.username, "password": args.password,
        "auth_source": args.auth_source, "replica_set": args.replica_set,
        "ssh_host": args.ssh_host, "ssh_port": args.ssh_port,
        "ssh_username": args.ssh_username, "ssh_password": args.ssh_password,
        "ssh_private_key_path": args.ssh_private_key_path,
        "ssh_private_key_passphrase": args.ssh_private_key_passphrase,
    }
    for key, value in overrides.items():
        if value is not None:
            conn[key] = value

    if args.tls:
        conn["tls"] = True

    if not conn["host"]:
        raise ValueError("沒有指定 --host，也沒有從 --config/--section 讀到 HOST")

    return conn


# ---------------------------------------------------------------------------
# 各項診斷步驟
# ---------------------------------------------------------------------------

def step_dns(report, host, port):
    try:
        infos = socket.getaddrinfo(host, port, proto=socket.IPPROTO_TCP)
        ips = sorted({info[4][0] for info in infos})
        report.add("DNS 解析", OK, f"{host} 解析到 {len(ips)} 個位址：{', '.join(ips)}")
    except socket.gaierror as e:
        report.add(
            "DNS 解析", FAIL, f"無法解析主機名稱 {host}：{e}",
            suggestion="確認主機名稱有沒有打錯；如果是內網主機名稱，可能需要連 VPN 或用"
            "內網 DNS 才能解析，也可以先改填 IP 直接測。",
        )


def step_tcp(report, name, host, port, timeout):
    start = time.monotonic()
    try:
        with socket.create_connection((host, port), timeout=timeout):
            elapsed = (time.monotonic() - start) * 1000
        report.add(name, OK, f"TCP 連線到 {host}:{port} 成功（{elapsed:.0f}ms）")
        return True
    except socket.timeout:
        report.add(
            name, FAIL, f"TCP 連線到 {host}:{port} 逾時（{timeout}s）",
            suggestion="通常是防火牆/安全群組擋掉了這個 port、或路由不到，也可能該服務只監聽"
            "內網介面，需要 SSH tunnel 才連得到。",
        )
        return False
    except OSError as e:
        report.add(
            name, FAIL, f"TCP 連線到 {host}:{port} 失敗：{e}",
            suggestion="connection refused 通常代表這個 port 根本沒服務在監聽（port 打錯，或"
            "服務沒啟動）；其他錯誤請確認主機名稱/IP 是否正確。",
        )
        return False


def step_ssh_tunnel(report, conn):
    ok = step_tcp(report, "SSH 主機 TCP 連線", conn["ssh_host"], conn["ssh_port"], conn["timeout"])
    if not ok:
        report.add("SSH tunnel", SKIP, "SSH 主機 TCP 都連不上，跳過建立 tunnel")
        return None

    try:
        from sshtunnel import SSHTunnelForwarder
    except ImportError:
        report.add(
            "SSH tunnel", FAIL, "沒有安裝 sshtunnel 套件",
            suggestion="pip install -r requirements.txt（含 sshtunnel + paramiko<3.0）",
        )
        return None

    try:
        tunnel = SSHTunnelForwarder(
            (conn["ssh_host"], conn["ssh_port"]),
            ssh_username=conn["ssh_username"],
            ssh_password=conn["ssh_password"],
            ssh_pkey=conn["ssh_private_key_path"],
            ssh_private_key_password=conn["ssh_private_key_passphrase"],
            remote_bind_address=(conn["host"], conn["port"]),
        )
        tunnel.start()
        report.add(
            "SSH tunnel", OK,
            f"已建立 {conn['ssh_username'] or '(未指定帳號)'}@{conn['ssh_host']}:{conn['ssh_port']}"
            f" → {conn['host']}:{conn['port']} 的 tunnel（本機 127.0.0.1:{tunnel.local_bind_port}）",
        )
        return tunnel
    except Exception as e:
        report.add(
            "SSH tunnel", FAIL, f"建立 SSH tunnel 失敗：{e}",
            suggestion="確認 SSH 帳密／金鑰路徑是否正確，以及這個帳號是否真的能 SSH 進去這台主機。",
        )
        return None


def _private_ip_note(addr):
    """addr 是 'host:port' 字串。回傳提示文字，如果 host 是內網/本機 IP。
    如果 host 是網域名稱（不是 IP），回傳 None，不做判斷（連得到連不到要另外測）。
    """
    host = addr.rsplit(":", 1)[0]
    try:
        ip = ipaddress.ip_address(host)
    except ValueError:
        return None
    if ip.is_private or ip.is_loopback:
        return f"{addr} 是內網/本機位址，外部主機通常連不到"
    return None


def step_handshake(report, connect_host, connect_port, tls):
    if MongoClient is None:
        report.add("Mongo handshake（direct connection）", FAIL, "沒有安裝 pymongo",
                    suggestion="pip install -r requirements.txt")
        return None

    client = MongoClient(
        connect_host, connect_port,
        directConnection=True, serverSelectionTimeoutMS=5000, tls=tls,
    )
    try:
        try:
            hello = client.admin.command("hello")
        except PyMongoError:
            hello = client.admin.command("ismaster")
    except Exception as e:
        report.add(
            "Mongo handshake（direct connection）", FAIL,
            f"TCP 連得上，但 {connect_host}:{connect_port} 對 hello/ismaster 指令沒有正常回應：{e}",
            suggestion="確認這個 port 上跑的真的是 mongod/mongos，不是其他服務；如果錯誤跟 "
            "TLS/SSL 有關，試著加 --tls。",
        )
        return None
    finally:
        client.close()

    is_primary = bool(hello.get("ismaster") or hello.get("isWritablePrimary"))
    is_secondary = bool(hello.get("secondary"))
    set_name = hello.get("setName")
    members = list(hello.get("hosts") or []) + list(hello.get("passives") or [])
    me = hello.get("me")

    role = "PRIMARY" if is_primary else ("SECONDARY" if is_secondary else "未知角色（也可能是 standalone/mongos）")
    detail_lines = [f"me={me or '(未回報)'}　角色={role}"]

    warn_notes = []
    if set_name:
        others = [m for m in members if m != me]
        detail_lines.append(f"replica set 名稱：{set_name}")
        detail_lines.append(f"其他已知成員：{', '.join(others) or '(無)'}")
        for m in others:
            note = _private_ip_note(m)
            if note:
                warn_notes.append(note)

    status = OK
    suggestion = ""
    if warn_notes:
        status = WARN
        detail_lines.extend(warn_notes)
        suggestion = (
            "這是 replica set，且其他成員是內網位址：如果之後用一般方式連線（不指定 "
            "directConnection）遇到 ReplicaSetNoPrimary / No replica set members found，"
            "就是 pymongo 想連去這些內網成員做拓樸探索、但連不到。解法是連線時固定加上 "
            "directConnection=True + readPreference=primaryPreferred（或 secondaryPreferred/"
            "nearest），跳過拓樸探索、只認你指定的這一個 host（check_subtitle 已經是這樣設定）。"
        )

    report.add(
        "Mongo handshake（direct connection）", status,
        f"成功連上 {connect_host}:{connect_port}，角色：{role}"
        + (f"，屬於 replica set「{set_name}」" if set_name else "（非 replica set 成員）"),
        detail="\n".join(detail_lines),
        suggestion=suggestion,
    )
    return hello


def step_full_discovery(report, connect_host, connect_port, replica_set, tls, timeout):
    """刻意不加 directConnection，重現「一般連線方式」會不會卡住的拓樸探索行為。
    只有在上一步的 handshake 顯示這是 replica set 時才有意義，所以由呼叫端決定要不要跑。
    """
    if MongoClient is None:
        report.add("Mongo 拓樸探索（未指定 directConnection）", SKIP, "沒有安裝 pymongo")
        return

    kwargs = {"serverSelectionTimeoutMS": int(timeout * 1000), "tls": tls}
    if replica_set:
        kwargs["replicaSet"] = replica_set

    client = MongoClient(connect_host, connect_port, **kwargs)
    try:
        client.admin.command("ping")
    except PyMongoError as e:
        message = str(e)
        unreachable = sorted(set(re.findall(r"\('([\d.:a-fA-F]+)',\s*(\d+)\)", message)))
        detail = "\n".join(f"{ip}:{port}" for ip, port in unreachable) or "(從錯誤訊息解析不到明確的成員清單，見下方原始訊息)"
        if not unreachable:
            detail += f"\n原始錯誤：{message}"
        report.add(
            "Mongo 拓樸探索（未指定 directConnection）", WARN,
            f"如預期：{timeout:.0f}s 內連不上完整的 replica set 拓樸（{type(e).__name__}）",
            detail=f"錯誤訊息中列到的成員：\n{detail}",
            suggestion="這證實了一般連線方式（不加 directConnection）會卡在這些連不到的內網"
            "成員上；正式程式的連線設定務必加 directConnection=True。",
        )
        return
    except Exception as e:
        report.add("Mongo 拓樸探索（未指定 directConnection）", WARN, f"連線方式異常：{e}")
        return
    finally:
        client.close()

    report.add(
        "Mongo 拓樸探索（未指定 directConnection）", OK,
        "不加 directConnection 也連得上、能正常完成拓樸探索（代表所有成員都是外部可連的，"
        "不強制用 directConnection 也沒關係）",
    )


def step_auth(report, connect_host, connect_port, conn):
    if not conn["username"]:
        report.add("帳號密碼驗證", SKIP, "沒有提供 --username，跳過驗證測試")
        return None
    if MongoClient is None:
        report.add("帳號密碼驗證", FAIL, "沒有安裝 pymongo", suggestion="pip install -r requirements.txt")
        return None

    kwargs = {
        "directConnection": True,
        "serverSelectionTimeoutMS": 5000,
        "username": conn["username"],
        "password": conn["password"],
        "readPreference": "primaryPreferred",
        "tls": conn["tls"],
    }
    if conn["auth_source"]:
        kwargs["authSource"] = conn["auth_source"]

    client = MongoClient(connect_host, connect_port, **kwargs)
    try:
        client.admin.command("ping")
    except OperationFailure as e:
        details = getattr(e, "details", None) or {}
        report.add(
            "帳號密碼驗證", FAIL,
            f"驗證失敗（code={e.code}）：{details.get('errmsg', str(e))}",
            suggestion="確認帳號密碼是否正確、AUTH_SOURCE 是否跟這個帳號實際建立在哪個資料庫一致"
            "（常見錯誤：帳號建在 admin，AUTH_SOURCE 卻填成目標資料庫名稱，反之亦然）。",
        )
        client.close()
        return None
    except PyMongoError as e:
        report.add("帳號密碼驗證", FAIL, f"連線層級失敗（不是帳密問題）：{e}")
        client.close()
        return None

    report.add("帳號密碼驗證", OK, f"帳號 {conn['username']} 驗證成功")
    return client


def step_query_collection(report, client, conn):
    if client is None:
        report.add("Collection 查詢", SKIP, "前一步沒有成功建立可用連線，跳過")
        return
    if not conn["collection"]:
        report.add("Collection 查詢", SKIP, "沒有提供 --collection，跳過實際查詢測試")
        return

    coll = client[conn["database"]][conn["collection"]]
    start = time.monotonic()
    try:
        doc = coll.find_one()
        count = coll.estimated_document_count()
        elapsed = (time.monotonic() - start) * 1000
    except Exception as e:
        report.add(
            "Collection 查詢", FAIL,
            f"查詢 {conn['database']}.{conn['collection']} 失敗：{e}",
            suggestion="確認資料庫/collection 名稱是否正確、這個帳號有沒有讀取這個資料庫的權限。",
        )
        return

    fields = ", ".join(sorted(doc.keys())) if doc else "(collection 是空的，或這個帳號看不到任何文件)"
    report.add(
        "Collection 查詢", OK,
        f"find_one() 成功（{elapsed:.0f}ms），estimated_document_count()={count}",
        detail=f"取到的文件欄位：{fields}",
    )


# ---------------------------------------------------------------------------
# 主流程（給 CLI 的 main() 跟給程式呼叫的 diagnose() 共用）
# ---------------------------------------------------------------------------

def run_report(conn, skip_full_discovery_test=False):
    """依照已經組好的 conn 連線設定 dict，跑完整套診斷流程，回傳 Report。
    不印出、不呼叫 sys.exit。一般不用直接呼叫這個 — CLI 用 main()，
    程式呼叫用下面的 diagnose()（不用自己組 conn dict）。
    """
    report = Report()
    tunnel = None
    client = None

    try:
        step_dns(report, conn["host"], conn["port"])

        if conn["ssh_host"]:
            tunnel = step_ssh_tunnel(report, conn)
            if tunnel is not None:
                connect_host, connect_port = "127.0.0.1", tunnel.local_bind_port
            else:
                connect_host, connect_port = conn["host"], conn["port"]
                report.add(
                    "後續 Mongo 測試", SKIP,
                    "SSH tunnel 沒建立成功；仍照原本的 host/port 測一次 Mongo（大概率也連不到，"
                    "但留著方便對照）。",
                )
        else:
            step_tcp(report, "Mongo host TCP 連線", conn["host"], conn["port"], conn["timeout"])
            connect_host, connect_port = conn["host"], conn["port"]

        hello = step_handshake(report, connect_host, connect_port, conn["tls"])

        if hello and hello.get("setName") and not skip_full_discovery_test:
            step_full_discovery(
                report, connect_host, connect_port,
                conn["replica_set"] or hello.get("setName"), conn["tls"], conn["timeout"],
            )

        client = step_auth(report, connect_host, connect_port, conn)
        step_query_collection(report, client, conn)
    finally:
        if client is not None:
            client.close()
        if tunnel is not None:
            tunnel.stop()

    return report


def diagnose(
    host=None, port=None, *, database=None, collection=None,
    username=None, password=None, auth_source=None, replica_set=None, tls=False,
    ssh_host=None, ssh_port=None, ssh_username=None, ssh_password=None,
    ssh_private_key_path=None, ssh_private_key_passphrase=None,
    timeout=5.0, skip_full_discovery_test=False,
    config=None, section=None,
):
    """給其他專案 import 這支檔案後直接呼叫的入口，不用碰 argparse、不用自己組 conn dict。

    兩種給連線設定的方式（可以混用；直接給的關鍵字參數一律優先於從 ini 讀到的值）：
      1. 直接給 host/port/username/password/... 等關鍵字參數
      2. 給 config=ini路徑 + section=區塊名稱，重用既有 config.ini

    回傳 Report：
      - report.render()        → 純文字報告，直接印出來或寫進日誌都可以
      - report.worst_status()  → OK/WARN/FAIL，用來判斷要不要繼續／要不要告警
      - report.steps           → StepResult 的 list，要自己組報告可以逐項讀
                                  name/status/message/detail/suggestion

    全程只讀，不印東西、不呼叫 sys.exit，方便包進自己的程式或測試裡。
    """
    conn = _conn_defaults(timeout)

    if config:
        if not section:
            raise ValueError("有給 config 就一定要搭配 section 指定要讀哪個區塊")
        _apply_ini_section(conn, config, section)

    overrides = {
        "host": host, "port": port, "database": database, "collection": collection,
        "username": username, "password": password, "auth_source": auth_source,
        "replica_set": replica_set, "ssh_host": ssh_host, "ssh_port": ssh_port,
        "ssh_username": ssh_username, "ssh_password": ssh_password,
        "ssh_private_key_path": ssh_private_key_path,
        "ssh_private_key_passphrase": ssh_private_key_passphrase,
    }
    for key, value in overrides.items():
        if value is not None:
            conn[key] = value

    if tls:
        conn["tls"] = True

    if not conn["host"]:
        raise ValueError("沒有指定 host，也沒有從 config/section 讀到 HOST")

    return run_report(conn, skip_full_discovery_test=skip_full_discovery_test)


# ---------------------------------------------------------------------------
# CLI
# ---------------------------------------------------------------------------

def parse_args():
    parser = argparse.ArgumentParser(
        description=(
            "Mongo 連線錯誤檢測工具：依序測 DNS/TCP/SSH tunnel/Mongo handshake/帳密/"
            "collection 查詢，找出到底卡在哪一層。"
        ),
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog=__doc__,
    )
    parser.add_argument("--config", metavar="PATH", help="重用既有 config.ini")
    parser.add_argument("--section", metavar="NAME", help="--config 裡要讀的區塊名稱")

    parser.add_argument("--host", help="Mongo 主機（IP 或網域名稱），沒給 --config 時必填")
    parser.add_argument("--port", type=int, help="Mongo port，預設 27017")
    parser.add_argument("--database", help="要測試查詢的資料庫名稱，預設 avnight")
    parser.add_argument("--collection", help="要測試查詢的 collection 名稱（不給就跳過查詢測試）")
    parser.add_argument("--username")
    parser.add_argument("--password")
    parser.add_argument("--auth-source")
    parser.add_argument("--replica-set", help="只用在「未指定 directConnection」那個拓樸探索測試")
    parser.add_argument("--tls", action="store_true", help="連線時開啟 TLS")

    parser.add_argument("--ssh-host")
    parser.add_argument("--ssh-port", type=int)
    parser.add_argument("--ssh-username")
    parser.add_argument("--ssh-password")
    parser.add_argument("--ssh-private-key-path")
    parser.add_argument("--ssh-private-key-passphrase")

    parser.add_argument(
        "--skip-full-discovery-test", action="store_true",
        help="跳過「未指定 directConnection」那個比較慢、用來重現拓樸探索問題的測試",
    )
    parser.add_argument("--timeout", type=float, default=5.0, metavar="SECONDS", help="各項網路測試的逾時秒數，預設 5")
    parser.add_argument("-o", "--output", metavar="PATH", help="報告輸出路徑（純文字）")

    return parser.parse_args()


def main() -> int:
    args = parse_args()

    if not args.config and not args.host:
        print("✗ 至少要給 --host，或用 --config + --section 指定既有設定檔", file=sys.stderr)
        return 3

    try:
        conn = build_conn(args)
    except Exception as e:
        print(f"✗ 參數錯誤：{e}", file=sys.stderr)
        return 3

    report = run_report(conn, skip_full_discovery_test=args.skip_full_discovery_test)

    text = report.render()
    print(text)
    if args.output:
        with open(args.output, "w", encoding="utf-8") as f:
            f.write(text)
        print(f"\n報告已儲存至：{args.output}")

    return {OK: 0, WARN: 1, FAIL: 2}[report.worst_status()]


if __name__ == "__main__":
    sys.exit(main())
```

**用法範例**

```bash
# 一：當 CLI 用，直接給連線參數
python mongo_conn_doctor.py --host 10.0.0.5 --port 27017 \
    --username myuser --password 'xxx' --auth-source mydb \
    --database mydb --collection mycollection

# 二：當 CLI 用，重用既有 config.ini（欄位命名比照下方「當函式庫用」段落）
python mongo_conn_doctor.py --config conf/config.ini \
    --section MONGO_PRIMARY

# 三：當 CLI 用，只想看網路層、不帶帳密（不給 --username 就會跳過驗證/查詢測試）
python mongo_conn_doctor.py --host mongo.example.com --port 27017
```

```python
# 四：當函式庫用（複製這支檔案到自己專案的 src/ 之後）
import mongo_conn_doctor as mcd

report = mcd.diagnose(
    host="10.0.0.5", username="myuser", password="xxx",
    auth_source="mydb", database="mydb", collection="mycollection",
)
# 或直接重用既有 config.ini：
# report = mcd.diagnose(config="conf/config.ini", section="MONGO_PRIMARY")

print(report.render())              # 純文字報告
if report.worst_status() == mcd.FAIL:
    raise RuntimeError("Mongo 連線檢查失敗，細節見上面報告")
# report.steps 是 StepResult 的 list，要自己組報告（例如丟進日誌系統）也可以逐項讀
```

Exit code（CLI 模式）：`0` = 全部通過；`1` = 有警告（例如偵測到內網 replica set 成員）但沒有致命錯誤；`2` = 至少一項致命失敗；`3` = 參數錯誤。
