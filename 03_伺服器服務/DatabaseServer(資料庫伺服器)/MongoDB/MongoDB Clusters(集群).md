# MongoDB Clusters(集群)

```
與單服務器 MongoDB 數據庫相比，MongoDB 集群允許 MongoDB 數據庫通過分片跨多個服務器水平擴展，或者通過 MongoDB 副本集複製數據以確保高可用性，從而提高 MongoDB 集群的整體性能和可靠性.

Replica-Set(副本集)
副本集是一組保存相同數據副本的 MongoDB 服務器的複制；
這是生產部署的基本屬性，因為它確保了高可用性和冗餘，這是在故障轉移和計劃維護期間具備的關鍵特性。

Sharded-Cluster(分片集群)
分片集群通常也稱為水平擴展，其中數據分佈在許多服務器上。

MongoDB Atlas Cluster
MongoDB Atlas Cluster 是公共雲中的 NoSQL 數據庫即服務產品（在 Microsoft Azure、谷歌云平台、亞馬遜網絡服務中可用）
```

## 目錄

- [MongoDB Clusters(集群)](#mongodb-clusters集群)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令 - Replica-Set(副本集)](#指令---replica-set副本集)
- [Replica-Set 實作步驟 CentOS7](#replica-set-實作步驟-centos7)
- [Replica-Set 實作步驟 Docker-Compose](#replica-set-實作步驟-docker-compose)
	- [設置 主-讀寫 從-只讀不寫(主掛了不會升為主)](#設置-主-讀寫-從-只讀不寫主掛了不會升為主)

## 參考資料

[MongoDB Sharding 簡介](https://www.mongodb.com/basics/clusters)

[MongoDB Sharding 分散式儲存架構建置 (概念篇)](https://blog.toright.com/posts/4552/mongodb-sharding-%E5%88%86%E6%95%A3%E5%BC%8F%E5%84%B2%E5%AD%98%E6%9E%B6%E6%A7%8B%E5%BB%BA%E7%BD%AE-%E6%A6%82%E5%BF%B5%E7%AF%87.html)

[MongoDB replica set 設定 實體機](https://castion2293.medium.com/mongodb-replica-set-%E8%A8%AD%E5%AE%9A-d890e174d47b)

[Replica Set從副本集中删除成員](https://blog.csdn.net/yaomingyang/article/details/73822775)

[MongoDB 副本集移除成员](https://blog.csdn.net/Alen_Liu_SZ/article/details/101995235)

[Read Preference](https://docs.mongodb.com/manual/core/read-preference/)

[為什麼要用 Docker？如何用 Docker 構築不同 MongoDB 架構？](https://tw.alphacamp.co/blog/mongodb-with-docker)

[30-22之MongoDB的副本集 replica set(2)---使用Docker建立MongoDB Cluster](https://ithelp.ithome.com.tw/articles/10187117)

[使用 docker 建立 MongoDB Replica Set](https://blog.yowko.com/docker-mongodb-replica-set/)

[eugenechen0514/demo_mongo_cluster](https://github.com/eugenechen0514/demo_mongo_cluster)

# 指令 - Replica-Set(副本集)

```JavaScript
// to enable replica mode 啟用
rs.initiate()

// check the replica mode if really enabled 檢查副本模式是否真的啟用
rs.status()

// add secondary to replica set 將次要添加到副本集
rs.add({host: "SECONDARY-IP:27018", priority: 0.5})

// add arbiter to replica set 將仲裁器添加到副本集
rs.addArb("ARBITER-IP:27018")
```

# Replica-Set 實作步驟 CentOS7

```bash
# 修改 mongod.conf
vim /etc/mongod.conf
	# net:
	# 	port: 27017
	# 	bindIp: 0.0.0.0

	# replication:
	# 	replSetName: replicaSet_name

	# // 加入 key驗證 功能(參考 指令 key驗證 生成key)
	# security:
	# 	keyFile: /var/lib/mongodb/mongodb-keyfile

# 在 /etc/hosts 定義這三台要安裝的 hostname 與 IP Address
vim /etc/hosts
	# IPAddress hostname
```

進入mongodb 輸入指令

```bash
# 進入mongo
mongo
```

```JavaScript
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

// 建立 Replica_Set 啟用副本模式
rs.initiate(cfg);

// 透過 rs.status() 查看 Replica Set 設定狀態
rs.status()
```

```bash
# 指令 key驗證 生成key 將生成的key加入
openssl rand -base64 741 > /var/lib/mongodb/mongodb-keyfile
chmod 600 /var/lib/mongodb/mongodb-keyfile
chown mongodb.mongodb /var/lib/mongodb/mongodb-keyfile
```

```JavaScript
cfg = {
    "_id": "RS",
    	"members": [{
        "_id": 0,
        "host": "192.168.131.131"
    },
    {
        "_id": 1,
        "host": "192.168.155.3",
        "votes": 0,
        "priority":0
    },
    {
        "_id": 2,
        "host": "192.168.155.193",
        "votes": 0,
        "priority":0
    }
    ]
};
// 建立 Replica_Set
rs.initiate(cfg);

// 返回包含當前副本集 配置的文檔。
rs.conf()
// https://docs.mongodb.com/manual/reference/method/rs.conf/#mongodb-method-rs.conf
// {
//     _id: <string>,
//     version: <int>,
//     term: <int>,
//     protocolVersion: <number>,
//     writeConcernMajorityJournalDefault: <boolean>,
//     configsvr: <boolean>,
//     members: [
//       {
//         _id: <int>,
//         host: <string>,
//         arbiterOnly: <boolean>,
//         buildIndexes: <boolean>,
//         hidden: <boolean>,
//         priority: <number>,
//         tags: <document>,
//         secondaryDelaySecs: <int>,
//         votes: <number>
//       },
//       ...
//     ],
//     settings: {
//       chainingAllowed : <boolean>,
//       heartbeatIntervalMillis : <int>,
//       heartbeatTimeoutSecs: <int>,
//       electionTimeoutMillis : <int>,
//       catchUpTimeoutMillis : <int>,
//       getLastErrorModes : <document>,
//       getLastErrorDefaults : <document>,
//       replicaSetId: <ObjectId>
//     }
// }

// 通過指令新增 Replica Set 節點
rs.add("xxx.xxx.xxx.xxx:xxxx")
rs.add("mongodb-a3:27019")

// 需進入 PRIMARY 操作
// 通過指令刪除 Replica Set 節點
rs.remove("mongod3.example.net:27017")

// 透過 rs.status() 查看 Replica Set 設定狀態
rs.status()
```

# Replica-Set 實作步驟 Docker-Compose

[Docker Compose 建立 MongoDB Replica Set](https://blog.yowko.com/docker-compose-mongodb-replica-set/)

```yml
# docker-compose.yml
version: "3.7"
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

```bash
# 設定 hosts
# mongo 內部只認得設定 Replica Set 的 member host，但 host 機器不認得 container name
echo "127.0.0.1 mongo1\n127.0.0.1 mongo2\n127.0.0.1 mongo3" >> /etc/hosts

# 啟動 MongoDB Replica Set
docker-compose up -d
```

## 設置 主-讀寫 從-只讀不寫(主掛了不會升為主)

[MongoDB副本集(一主兩從)讀寫分離、故障轉移功能環境部署記錄](https://iter01.com/68390.html)

```
Secondary-Only:實作方式
members[n].priority 為0 vote也要為0
```

[Configure Non-Voting Replica Set Member](https://docs.mongodb.com/manual/tutorial/configure-a-non-voting-replica-set-member/)

```JavaScript
// 1.Connect mongosh to the replica set primary:
mongosh --host "<hostname>:<port>"
// 2.Retrieve the Replica Configuration
cfg = rs.conf();
// 3.Configure the Member to be Non-Voting
n是ID
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
