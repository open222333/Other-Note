# MongoDB 筆記

```
MongoDB是一種介於關係型和非關係型中間的資料庫。它是文件型資料庫（一個文件包含多個鍵/值對），多個文件組成集合，多個集合組成資料庫。一個MongoDB 例項可以承載多個數據庫。這裡集合的概念類似關係型資料庫中的表。MongoDB的優勢在於資料儲存與互動非常靈活，採用類似JSON格式，能靈活地建立索引以及完成SQL能做的幾乎所有查詢。此外它還是一個支援分散式的資料庫，能自動處理分片實現負載均衡，並且支援使用MapReduce進行復雜的聚合操作。

停止容器運作導致 MongoDB process（(行程）) 停止，類似 kill process 非正常關閉 MongoDB，在有進行讀寫的情況下可能會產生無法預期的情況。
```

```
Cluster

與單服務器 MongoDB 數據庫相比，MongoDB 集群允許 MongoDB 數據庫通過分片跨多個服務器水平擴展，或者通過 MongoDB 副本集複製數據以確保高可用性，從而提高 MongoDB 集群的整體性能和可靠性.

Replica-Set(副本集)
副本集是一組保存相同數據副本的 MongoDB 服務器的複制；
這是生產部署的基本屬性，因為它確保了高可用性和冗餘，這是在故障轉移和計劃維護期間具備的關鍵特性。

Sharded-Cluster(分片集群)
分片集群通常也稱為水平擴展，其中數據分佈在許多服務器上。

MongoDB Atlas Cluster
MongoDB Atlas Cluster 是公共雲中的 NoSQL 數據庫即服務產品（在 Microsoft Azure、谷歌云平台、亞馬遜網絡服務中可用）
```

```
MongoDB Shell mongosh

MongoDB Shell mongosh 是一個功能齊全的 JavaScript 和 Node.js 16.x REPL 環境，用於與 MongoDB 部署進行交互。
您可以使用 MongoDB Shell 直接使用您的數據庫測試查詢和操作。
```

## 目錄

- [MongoDB 筆記](#mongodb-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [安裝相關](#安裝相關)
      - [解除安裝](#解除安裝)
    - [查詢相關](#查詢相關)
    - [操作相關](#操作相關)
    - [備份腳本相關](#備份腳本相關)
    - [例外相關](#例外相關)
    - [指令相關](#指令相關)
      - [鎖定資料庫](#鎖定資料庫)
    - [升級相關](#升級相關)
- [安裝](#安裝)
  - [Docker部署](#docker部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
    - [解除安裝](#解除安裝-1)
    - [Ubuntu 20.04(Monog 4.4)](#ubuntu-2004monog-44)
  - [RedHat (CentOS)](#redhat-centos)
    - [解除安裝](#解除安裝-2)
  - [配置檔案設定](#配置檔案設定)
  - [防火牆設定](#防火牆設定)
    - [CentOS Database tool](#centos-database-tool)
  - [MacOS](#macos)
- [升級](#升級)
  - [RedHat (CentOS7)](#redhat-centos7)
- [指令](#指令)
  - [匯入匯出](#匯入匯出)
- [資料庫指令](#資料庫指令)
  - [刪除](#刪除)
    - [刪除符合條件以外的資料](#刪除符合條件以外的資料)
  - [查詢](#查詢)
    - [找重複](#找重複)
    - [比對字元](#比對字元)
    - [欄位是否有值 數量統計](#欄位是否有值-數量統計)
    - [取得日期範圍內資料](#取得日期範圍內資料)
  - [使用者](#使用者)
    - [mongodb 使用者許可權角色說明](#mongodb-使用者許可權角色說明)
  - [特殊用法範例](#特殊用法範例)
    - [監視和診斷資料庫效能 db.currentOp()](#監視和診斷資料庫效能-dbcurrentop)
  - [連接字符串URI格式](#連接字符串uri格式)
  - [使用 fsync 鎖定資料庫](#使用-fsync-鎖定資料庫)
- [特別工具](#特別工具)
  - [匯出匯入腳本](#匯出匯入腳本)
- [配置範例](#配置範例)
  - [正式生產環境可用的設定模板](#正式生產環境可用的設定模板)

## 參考資料

[mongodb 官網](https://www.mongodb.com/home)

[mongodb 手冊](https://www.mongodb.com/docs/manual/)

[mongodb 下載地址](https://www.mongodb.com/download-center#community)

[mongo 官方 dockerhub](https://hub.docker.com/_/mongo)

### 安裝相關

[centos安裝](https://iter01.com/156322.html)

[Install MongoDB 5.0 on CentOS 8/7 & RHEL 8/7](https://computingforgeeks.com/how-to-install-mongodb-on-centos-rhel-linux/)

[Installing the Database Tools on Linux - 在 Linux 上安裝數據庫工具](https://www.mongodb.com/docs/database-tools/installation/installation-linux/)

[database-tools rpm](https://www.mongodb.com/try/download/database-tools)

[Installing the Database Tools on macOS - 在 macOS 上安裝資料庫工具](https://www.mongodb.com/docs/database-tools/installation/installation-macos/)

#### 解除安裝

[How to Uninstall MongoDB - 官方](https://www.mongodb.com/resources/products/fundamentals/uninstall-mongodb)

### 查詢相關

[Mondb 邏輯運算子](https://www.mongodb.com/docs/manual/reference/operator/query/)

[MongoDB 查詢資料邏輯運算子語法範例](https://matthung0807.blogspot.com/2019/08/mongodb_50.html)

[mongodb 正則表達式](https://www.mongodb.com/docs/manual/reference/operator/query/regex/)

[mongodb高級聚合查詢 - { 欄位: { 運算子: 條件 } }](https://www.uj5u.com/shujuku/12759.html)

### 操作相關

[MongoDB CRUD Operations(各種程式使用的範例 選擇操作右上角選擇程式語言) - 增刪查改，增加、刪除、查詢、改正](https://docs.mongodb.com/manual/crud/)

[db.createUser() - 創建使用者](https://www.mongodb.com/docs/manual/reference/method/db.createUser/)

[Connection String URI Format - 連線資料庫字串格式](https://www.mongodb.com/docs/manual/reference/connection-string/)

### 備份腳本相關

[Linux下shell脚本实现mongodb定时自动备份](https://www.cnblogs.com/Sungeek/p/11904825.html)

### 例外相關

[mongodb - 聚合管道抛出错误 "A pipeline stage specification object must contain exactly one field."](https://www.coder.work/article/39368)

### 指令相關

[mongod 資料庫指令 manpage](https://docs.mongodb.com/manual/reference/program/mongod/)

[mongodump reference page(文檔) - 匯出](https://docs.mongodb.com/database-tools/mongodump/#mongodb-binary-bin.mongodump)

[mongorestore reference page(文檔) - 匯入](https://docs.mongodb.com/database-tools/mongorestore/#mongodb-binary-bin.mongorestore)

[mongorestore - 匯入 中文文檔](https://www.docs4dev.com/docs/zh/mongodb/v3.6/reference/reference-program-mongorestore.html)

#### 鎖定資料庫

[db.fsyncLock()](https://www.mongodb.com/zh-cn/docs/manual/reference/method/db.fsyncLock/)

### 升級相關

[MongoDB Versioning](https://www.mongodb.com/zh-cn/docs/v6.0/reference/versioning/#std-label-release-version-numbers)

[升级到MongoDB的最新自我管理补丁版本](https://www.mongodb.com/zh-cn/docs/v6.0/tutorial/upgrade-revision/)

# 安裝

## Docker部署

```yml
version: "3"
services:
  mongo:
    image: mongo
    container_name: mongo1
    ports:
      - 31141:27017
    # expose: # 開給內網
    #     - 5000
    command: mongod --replSet RS --bind_ip_all --dbpath /data/db
    volumes:
      - ./data/mongo:/data/db
```

## Debian (Ubuntu)

查看 Ubuntu 版本

```sh
lsb_release -a
```

更新系統套件庫

```bash
apt update
apt upgrade -y
```

導入 MongoDB 公鑰

```sh
curl -fsSL https://www.mongodb.org/static/pgp/server-6.0.asc | gpg --dearmor -o /usr/share/keyrings/mongodb-server-6.0.gpg
```

```sh
curl -fsSL https://www.mongodb.org/static/pgp/server-5.0.asc | gpg --dearmor -o /usr/share/keyrings/mongodb-server-5.0.gpg
```

添加 MongoDB 軟體來源庫

使用的是 Debian，將 ubuntu 替換為 debian

MongoDB 6.0 版本 MongoDB 軟體來源庫

```sh
echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-6.0.gpg ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/6.0 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-6.0.list
```

MongoDB 5.0 版本 MongoDB 軟體來源庫

```sh
echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-5.0.gpg ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/5.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-5.0.list
```

更新軟件包列表 安裝 MongoDB

```sh
apt update
apt upgrade -y
```

```
mongodb-org 是一個元包（meta-package），它包含以下子套件：

mongodb-org-server
mongodb-org-mongos
mongodb-org-shell
mongodb-org-tools

如果只指定版本為 $version，但沒有為上述子套件指定版本，可能導致：

子套件的版本與 mongodb-org 不匹配。
系統會自動安裝子套件的最新版本，而不是與 $version 對應的版本。
```

mongodb 需要特定版本的 libssl1.1

```sh
wget http://archive.ubuntu.com/ubuntu/pool/main/o/openssl/libssl1.1_1.1.1f-1ubuntu2_amd64.deb
dpkg -i libssl1.1_1.1.1f-1ubuntu2_amd64.deb
```

```sh
apt install -y mongodb-org
```

```sh
apt install -y mongodb-org=$version mongodb-org-server=$version mongodb-org-shell=$version mongodb-org-mongos=$version mongodb-org-tools=$version
```

```sh
# 啟動服務
systemctl start mongod
# 開機啟動
systemctl enable mongod
# 查詢啟動狀態
systemctl status mongod
```

### 解除安裝

```sh
systemctl stop mongod
```

```sh
apt purge mongodb-org* -y
apt autoremove -y
```

MongoDB 的預設資料和日誌目錄通常位於以下路徑。執行以下命令刪除這些目錄

```sh
rm -rf /var/lib/mongo
rm -rf /var/log/mongodb
rm -rf /var/lib/mongodb/*
```

刪除 MongoDB 的配置檔案

```sh
rm -rf /etc/mongod.conf
rm -rf /etc/mongodb.conf
```

### Ubuntu 20.04(Monog 4.4)

注意：MongoDB 4.4 官方僅支援到 Ubuntu 20.04（Focal）／18.04（Bionic）等版本，若在 Ubuntu 22.04（Jammy）上安裝可能需要額外依賴或 workaround

更新套件清單、安裝前置工具

```sh
apt update
apt install -y gnupg apt-transport-https ca-certificates
```

匯入 MongoDB 的 GPG 金鑰

```sh
wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | sudo apt-key add -
```

新增 MongoDB 4.4 的 APT 倉庫

```sh
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/4.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.4.list
```

更新 apt 並安裝 MongoDB

```sh
apt update
apt install -y mongodb-org
```

啟動並設為開機啟動

```sh
systemctl start mongod
systemctl enable mongod
```

驗證安裝版本

```sh
mongod --version
# 或
mongo --version
```

## RedHat (CentOS)

```bash
# 建立.repo檔案，生成mongodb的源
vim /etc/yum.repos.d/mongodb-org-4.0.repo # 舊版
vim /etc/yum.repos.d/mongodb-org-5.0.repo
```

```repo
# 新增以下配置資訊：
[mongodb-org-4.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/#releasever/mongodb-org/4.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-4.0.asc

[mongodb-org-5.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/5.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-5.0.asc


====================================
詳解：
name         # 名稱
baseurl      # 獲得下載的路徑
gpkcheck=1   # 表示對從這個源下載的rpm包進行校驗
enable=1     # 表示啟用這個源。
gpgkey       # gpg驗證
====================================
```

```bash
# 安裝
yum install mongodb-org -y

# 修改 mongod.conf
vim /etc/mongod.conf
	# net:
	# 	port: 27017
	# 	bindIp: 0.0.0.0

# 驗證安裝結果
# 檢視資料庫的程式是否存在
rpm -qa |grep mongodb

rpm -ql mongodb-org-server

# 開啟MongoDB
service mongod restart
systemctl start mongod.service

# 啟動服務
systemctl start mongod
# 開機啟動
systemctl enable mongod
# 查詢啟動狀態
systemctl status mongod
# 重啟
systemctl restart mongod
# 停止
systemctl stop mongod

# MongoDB預設埠是27017，檢視是否開啟
netstat -natp | grep 27017
```

### 解除安裝

```sh
# 解除安裝MongoDB
yum erase $(rpm -qa | grep mongodb-org)

yum erase mongodb-org* -y
```

MongoDB 的預設資料和日誌目錄通常位於以下路徑。執行以下命令刪除這些目錄

```sh
rm -rf /var/lib/mongo
rm -rf /var/log/mongodb
rm -rf /var/lib/mongodb/*
```

刪除 MongoDB 的配置檔案

```sh
rm -rf /etc/mongod.conf
rm -rf /etc/mongodb.conf
```

## 配置檔案設定

`bin/mongod.cfg`文件中會有 dbPath 和logPath的配置

位置:`/etc/mongod.conf`(設定完需重啟)

```conf
; 繫結ip預設127.0.0.1只允許本地連線，所以修改為bindIp:0.0.0.0
; network interfaces
net:
  port: 27017
  bindIp: 0.0.0.0

; 身份驗證
security:
  authorization: "enabled"   # disable or enabled
```

```sh
# 重啟
systemctl restart mongod
```

## 防火牆設定

```bash
### 開放對外埠 方法一

# 檢視防火牆狀態
systemctl status firewalld
# mongodb預設埠號
firewall-cmd --zone=public --add-port=27017/tcp --permanent
# 重新載入防火牆
firewall-cmd --reload
# 檢視埠號是否開放成功，輸出yes開放成功，no則失敗
firewall-cmd --zone=public --query-port=27017/tcp

### 開放對外埠 方法二

iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 27017 -j ACCEPT
```

### CentOS Database tool

```bash
wget https://fastdl.mongodb.org/tools/db/mongodb-database-tools-rhel70-x86_64-100.6.1.rpm

yum install -y mongodb-database-tools-*-100.6.0.rpm
```

## MacOS

```bash
# 安裝 Database Tools
# https://www.mongodb.com/docs/database-tools/installation/installation-macos/
brew tap mongodb/brew
brew install mongodb-database-tools
# 更新
brew upgrade mongodb-database-tools

# 安裝
brew install mongodb

# 檢視幫助
mongod –help
# 啟動
brew services start mongodb
# 停止
brew services stop mongodb
# 重啟
brew services restart mongodb
# 檢視是否啟動成功
ps -ef|grep mongod

## 配置檔案的位置
/etc/mongod.conf

	# 預設端⼝
	27017
	# 日誌的位置
	/var/log/mongodb/mongod.log
```

# 升級

`注意事項`

```
版本升級策略：檢查升級指南，確保從當前版本到目標版本的升級路徑是受支持的。例如，從 3.x 到 4.x 的升級可能需要先升級到 4.0，再升級到 4.4。

備份數據：升級前務必備份數據，以防升級失敗導致數據丟失。

升級後測試：升級完成後，測試應用程序與 MongoDB 的兼容性。
```

## RedHat (CentOS7)

備份數據 在升級之前，務必備份 MongoDB 數據

```sh
mongodump --out /path/to/backup
```

編輯 MongoDB 的存儲庫 根據目標版本更新 /etc/yum.repos.d/mongodb-org.repo

```ini
[mongodb-org-4.4]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/4.4/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-4.4.asc
```

清理 YUM 緩存

```sh
yum clean all
```

升級 MongoDB 軟件包

```sh
yum update mongodb-org
```

重啟 MongoDB 並驗證升級

```sh
systemctl restart mongod
mongod --version
```

# 指令

```sh
# 啟動服務
systemctl start mongod
# 開機啟動
systemctl enable mongod
# 查詢啟動狀態
systemctl status mongod
# 重啟
systemctl restart mongod
# 停止
systemctl stop mongod
```

```bash
# 執行檔mongodb 用來連入DB, 預設port 27017
# 進入後指令查看下方
# 從 MongoDB 6.0 開始，官方將 MongoDB Shell 分離為一個單獨的工具，名為 mongosh
mongosh

mongo [options] [db address] [file names (ending in .js)]
	# db address can be:
	# foo                   foo database on local machine
	# 192.169.0.5/foo       foo database on 192.168.0.5 machine
	# 192.169.0.5:9999/foo  foo database on 192.168.0.5 machine on port 9999
	# Options:
	# --shell                               run the shell after executing files
	# --nodb                                don't connect to mongod on startup - no
	# 										'db address' arg expected
	# --norc                                will not run the ".mongorc.js" file on
	# 										start up
	# --quiet                               be less chatty
	# --port arg                            port to connect to
	# --host arg                            server to connect to
	# --eval arg                            evaluate javascript
	# -h [ --help ]                         show this usage information
	# --version                             show version information
	# --verbose                             increase verbosity
	# --ipv6                                enable IPv6 support (disabled by
	# 										default)
	# --ssl                                 use SSL for all connections
	# --sslCAFile arg                       Certificate Authority file for SSL
	# --sslPEMKeyFile arg                   PEM certificate/key file for SSL
	# --sslPEMKeyPassword arg               password for key in PEM file for SSL
	# --sslCRLFile arg                      Certificate Revocation List file for
	# 										SSL
	# --sslAllowInvalidHostnames            allow connections to servers with
	# 										non-matching hostnames
	# --sslAllowInvalidCertificates         allow connections to servers with
	# 										invalid certificates
	# --sslFIPSMode                         activate FIPS 140-2 mode at startup

	# Authentication Options:
	# -u [ --username ] arg                 username for authentication
	# -p [ --password ] arg                 password for authentication
	# --authenticationDatabase arg          user source (defaults to dbname)
	# --authenticationMechanism arg (=MONGODB-CR)
	# 										authentication mechanism
	# --gssapiServiceName arg (=mongodb)    Service name to use when authenticating
	# 										using GSSAPI/Kerberos
	# --gssapiHostName arg                  Remote host name to use for purpose of
	# 										GSSAPI/Kerberos authentication

# 查看log(需安裝jq)
cat mongod.log | jq

# 啟動服務
systemctl start mongod
# 查詢啟動狀態
systemctl status mongod
# 重新啟動
systemctl restart mongod
# 停止服務
systemctl stop mongod
# 開機啟動
systemctl enable mongod

# 創建數據庫文件的存放位置，啟動mongodb服務時需要先確定數據庫文件存放的位置，否則係統不會自動創建，啟動會不成功。
mongod --dbpath
# 表示日誌文件存放的路徑
mongod --logpath
# 表示以追加的方式寫日誌文件
mongod --logappend
```

## 匯入匯出

```bash
# 匯出
mongodump
    # -h: 要備份的 MongoDB 連線位置 服務器地址，例如：127.0.0.1:27017
    # -d: 要備份的 Database 名稱
    # -u: 資料庫使用者名稱
    # -p: 資料庫密碼
    # -o：備份的數據存放位置，例如：c:\data\dump，目錄需要提前建立

# 匯入
mongorestore $path
    # -h --host: 要還原的 MongoDB 連線位置 服務器地址，例如：127.0.0.1:27017
    # -d --db: 要還原的 Database 名稱
    # -c --collection:
    # -u: 資料庫使用者名稱
    # -p: 資料庫密碼
    # --dir --directoryperdb: 指定要還原的資料庫檔案來源目錄名稱 不能同時指定<path> 和--dir 選項，--dir也可以設置備份目錄。
    # --drop: 如果資料庫存在就刪除重新建立 (小心使用)
    # <path>:設置備份數據所在位置，例如：c:\data\dump\test。
	# dump檔匯入
	--archive=mongo.dump
```

# 資料庫指令

```JavaScript
// 安全的 關閉 MongoDB 需要連入資料庫
use admin
db.shutdownServer()

// 以 admin 身分登入 dbname
use dbname
db.auth("admin", "{PASSWORD}")

// 顯示資料庫列表
show dbs

// 顯示當前資料庫中的集合（類似關聯式資料庫中的表）
show collections

// 顯示使用者
show users

// 切換dbname, 注意大小寫, 用法跟MySql類似
// 若不存在則創建db
use dbname

// 顯示集合
show collections

// 建立users集合
db.createCollection(‘users’)

// 刪除集合users
db.users.drop()
db.runCommand({"drop","users"})

// 刪除目前DB
db.runCommand({"dropDatabase": 1})

// help
odb.help();
odb.yourColl.help();
odb.youColl.find().help();

// 刪除當前DB
db.dropDatabase()

// 從指定的機器上複製DB
db.cloneDatabase(“127.0.0.1”)

// 從指定的機器複製到本地的 temp DB
db.copyDatabase("mydb", "temp", "127.0.0.1")

// 查看當前DB
db

// 查看版本
db.version()

// 查看當前的db 版本
db.getMongo()

// 新增
save()

// 建立一個 users的集合 並且寫入一筆{"name":"lecaf"} 資料
db.users.save({"name":"lecaf"})

// 在users集合中寫入新資料，如果没有users，mongodb會自動建立一個
db.users.insert({"name":"ghost", "age":10})

// save()和insert()也存在著些許區別：若新增的數據主鍵已經存在，insert()會不做操作並提示錯誤，而save() 則更改原來的內容為新內容。
insert({ _id : 1, " name " : " n1 "})   // _id是主键
insert({ _id : 1, " name " : " n2 " })  // 會顯示錯誤, 因為1已經有資料了
save({ _id : 1, " name " : " n2 " })    // 會把 n1 更新為 n2 ，類似update。
```

## 刪除

```JavaScript
// 刪除users集合所有資料
db.users.remove()

// 刪除users集合下name=lecaf的該筆資料
db.users.remove({"name": "lecaf"})
```

### 刪除符合條件以外的資料

```
$not	反向匹配，但需搭配其他條件運算符，例如 { $not: { $eq: "AWTB" } } 才正確。
$ne	    不等於，例如 { "origin": { $ne: "AWTB" } }，更適合你的需求。
$nor	保留反向匹配組合條件，例如 { $nor: [{ "origin": "AWTB" }] }，與 $ne 效果類似，但更靈活。
```

$not 本身是 MongoDB 的一個邏輯運算符，它必須與具體的運算符（例如 $eq, $gte, $regex 等）一起使用，否則會引發錯誤。

改用 $ne（"不等於"）來刪除 origin 不等於 "A" 的所有文件

```JavaScript
db.users.deleteMany({ "origin": { $ne: "A" } });
```

```JavaScript
db.users.deleteMany({
    $not: { age: { $gte: 30 }, name: "Charlie" }
});
```

```JavaScript
db.users.deleteMany({
    $expr: { $not: { $and: [{ $gte: ["$age", 30] }, { $eq: ["$name", "Charlie"] }] } }
});
```

```JavaScript
db.users.deleteMany({
    age: { $not: { $gt: 25 } }
});
```

```JavaScript
db.users.deleteMany({
    $nor: [
        { age: { $gte: 30 } },
        { name: "Bob" }
    ]
});
```

## 查詢

```JavaScript
// 查詢users集合中所有資料
db.users.find()

// 查詢users集合中符合name=feng的所有資料
db.users.find({“name”:”feng”})

// 查詢users集合中的第一筆資料
db.users.findOne()

// 查詢users集合中name=feng的第一筆資料
db.users.findOne({“name”:”feng”})

// 修改資料，其中name=lecaf 為查詢條件，"age":10是修改内容，除了主键，其他内容会被第二個參數替換，主键不能修改, 只能新增刪除.
db.users.update({"name":"lecaf"}, {"age":10})

// 查詢key=value的資料
db.collection.find({ "key" : value })

// 查詢key > value
db.collection.find({ "key" : { $gt: value } })

// 查詢key < value
db.collection.find({ "key" : { $lt: value } })

// 查詢key >= value
db.collection.find({ "key" : { $gte: value } })

// 查詢key <= value
db.collection.find({ "key" : { $lte: value } })

// 查詢value1 < key <value2
db.collection.find({ "key" : { $gt: value1 , $lt: value2 } })

// 查詢key <> value
db.collection.find({ "key" : { $ne: value } })

// 條件運算, 相當於key % 10 == 1
db.collection.find({ "key" : { $mod : [ 10 , 1 ] } })

// 不屬於 1,2,3任一
db.collection.find({ "key" : { $nin: [ 1, 2, 3 ] } })

// 屬於 1,2,3任一
db.collection.find({ "key" : { $in: [ 1, 2, 3 ] } })

// $size 值為1
db.collection.find({ "key" : { $size: 1 } })

// 字串 "key"存在則返回true, 反之 false
db.collection.find({ "key" : { $exists : true|false } })

// 正則表示式, "i"忽略大小寫, "m" 可查詢多行
db.collection.find({ "key": /^val.*val$/i })

// $or或 （備註：MongoDB 1.5.3板之後才支援），a=1或者b=2的資料都會列出
db.collection.find({ $or : [{a : 1}, {b : 2} ] })

// 符合條件key=value ，a=1或者b=2的資料都會列出
db.collection.find({ "key": value , $or : [{ a : 1 } , { b : 2 }] })

// 内嵌對象中的值查詢，注意："key.subkey"必須加上引號
db.collection.find({ "key.subkey" :value })
db.collection.find({ "key": { $not : /^val.*val$/i } })

// 1代表升冪排列，-1代表降冪
db.collection.find({}).sort({ "key1" : -1 ,"key2" : 1 })

// 控制返回結果數量，如果參數是0，則當作沒有約束，limit()將不起作用(會對傳入參數求求絕對值)
b.collection.find({}).limit(5)

// 控制返回結果跳過多少數量，如果參數是0，則當作沒有約束，skip()將不起作用，或者說跳過了0條。 （參數不能為負數）可用於分頁，limit是pageSize;skip 是第n頁*pageSize
db.collection.find({}).skip(5)

// 可用來做分頁，跳過5條數據再取5條數據
db.collection.find({}).skip(5).limit(5)
db.collection.find({}).count(true)

// 返回結果集的條數
count()

// 在加入skip()和limit()這兩個操作時，要獲得實際返回的結果數，需要一個參數true，否則返回的是符合查詢條件的結果，而不是數量
db.collection.find({}).skip(5).limit(5).count(true)

// 組合查詢修改刪除
db.users.findAndModify({
    query: {age: {$gte: 25}},
    sort: {age: -1},
    update: {
        $set: {name: 'a2'},
        $inc: {age: 2}
    },
    remove: true
});

// mongodb query範例
// 聚合
// 判斷 最後更新和創造只差一個月 以及一個月內
db.collection.aggregate([
    {
        $addFields:{days:{$divide:[{$subtract: ["$avdata_updated_at","$avdata_created_at"]}, 60 * 60 * 24 * 1000]}}
    },
    {
        $match:{$or:[{days:{$lte:30}}, {avdata_created_at:{$gte:ISODate("2021-07-25T00:00:00.000+08:00")}}]}
    }
]).count();

// 刪除資料
db.products.remove( { qty: { $gt: 20 } } )
```

### 找重複

```JavaScript
db.collection.aggregate()
	// 表示按照 id 欄位的值進行分組。
	// count: { $sum: 1 } 表示對每個分組中的文件進行計數，結果保存在 count 欄位中。
    .group({ _id: "$id", count: { $sum: 1 } })
	// 篩選出 count 欄位值大於 1 的文檔，即表示有重複的 id 值
    .match({count:{ $gt : 1 }})
	.sort({id: -1})
```

### 比對字元

```JavaScript
db.long_video.find({
    "$and": [
        {"video_translation.language_code": {"$nin": ["zh-CN"]}},
        {"video_translation.language_code": {"$in": ["zh-TW"]}}
    ],
    "$expr": {
        "$gte": [{"$strLenCP": "description"}, 10]
    }
})
    .projection({})
    .sort({modified_date:-1})
    .limit(100)
    // .count()
```

```
$expr: 用於執行複雜的查詢表達式，可以在單個文件中同時比較多個欄位。
$and: 將多個條件組合在一起，要求所有條件都滿足。
```

```JavaScript
db.long_video.find({
    "$and": [
        { "video_translation.language_code": { "$nin": ["zh-CN"] } },
        { "video_translation.language_code": { "$in": ["zh-TW"] } },
        {
            $where: function() {
                for (var i = 0; i < this.video_translation.length; i++) {
                    var translation = this.video_translation[i];
                    if (translation.title.length > 20) {
                        return true;
                    }
                }
            },
        }
    ]
}).count()
```

```JavaScript
var textSumLength = 0;
var elementCount = 0;
db.long_video.find({
    "$and": [
        { "video_translation.language_code": { "$nin": ["zh-CN"] } },
        { "video_translation.language_code": { "$in": ["zh-TW"] } },
    ]
}).forEach(function(myDoc) {
    elementCount++;
    if (myDoc.video_translation) {
        var max_title_length = 0;
        var max_description_length = 0;
        for (var i = 0; i < myDoc.video_translation.length; i++) {
            var translation = myDoc.video_translation[i];

            if (translation.title && translation.title.length > max_title_length) {
                max_title_length = translation.title.length;
            }
            if (translation.description && translation.description.length > max_description_length) {
                max_description_length = translation.description.length;
            }
        }
    }

    textSumLength = textSumLength + (max_title_length + max_description_length);
    console.log(myDoc.avkey + ' - ' + elementCount + ' -' + textSumLength);
})
```

### 欄位是否有值 數量統計

```JavaScript
// 替換 'your_collection' 為實際的集合名稱
var collectionName = 'your_collection';

// 列出集合中所有的欄位
var fields = db[collectionName].findOne();

// 定義聚合管道
var pipeline = [];

// 遍歷每個欄位，統計非空值的數量
for (var field in fields) {
    if (fields.hasOwnProperty(field)) {
        var stage = {
            $group: {
                _id: null,
                count: {
                    $sum: {
                        $cond: {
                            if: { $ne: ["$" + field, null] },
                            then: 1,
                            else: 0
                        }
                    }
                }
            }
        };

        // 添加到聚合管道
        stage.$group["_id"] = "$" + field;
        pipeline.push(stage);
    }
}

// 執行聚合查詢
var result = db[collectionName].aggregate(pipeline);

// 顯示結果
result.forEach(function (stat) {
    print("欄位 " + stat._id + " 的非空值數量：" + stat.count);
});
```

### 取得日期範圍內資料

```JavaScript
db.collection.find({"modified_date": {"$gte": ISODate("2024-04-15T00:00:00Z"), "$lt": ISODate("2024-04-16T00:00:00Z")}})
    .count()
```

```JavaScript
db.collection.find({"modified_date": {"$regex": "^2024-04-15"}})
   .count()
```

## 使用者

```JavaScript
// 建立使用者，設定賬號，密碼，許可權
// admin資料庫
use admin
db.createUser({ user:"root", pwd:"123456", roles:["root"] })


use admin
db.createUser({
  user: "admin",
  pwd: "你的密碼",
  roles: [ { role: "root", db: "admin" } ]
})


// 其他資料庫
use test
db.createUser({ user:"admin", pwd:"123456", roles:["readWrite", "dbAdmin"] })
db.createUser( { user: "accountAdmin01",
                 pwd: passwordPrompt(),  // Or  "<cleartext password>"
                 customData: { employeeId: 12345 },
                 roles: [ { role: "clusterAdmin", db: "admin" },
                          { role: "readAnyDatabase", db: "admin" },
                          "readWrite"] },
               { w: "majority" , wtimeout: 5000 } )

// 修改使用者
db.updateUser(用户名，更新，* writeConcern *)
db.updateUser("user123",{pwd: "KNlZmiaNUp0B", customData: { title: "Senior Manager" }})

db.dropUser('test') // 刪除使用者

db.system.users.find()    //查詢使用者
```

### mongodb 使用者許可權角色說明

```
規則  說明
root
只在admin資料庫中可用。超級賬號，超級許可權

Read
允許使用者讀取指定資料庫

readWrite
允許使用者讀寫指定資料庫

dbAdmin
允許使用者在指定資料庫中執行管理函式，如索引建立、刪除，檢視統計或訪問system.profile

userAdmin
允許使用者向system.users集合寫入，可以找指定資料庫裡建立、刪除和管理使用者

clusterAdmin
只在admin資料庫中可用，賦予使用者所有分片和複製集相關函式的管理許可權

readAnyDatabase
只在admin資料庫中可用，賦予使用者所有資料庫的讀許可權

readWriteAnyDatabase
只在admin資料庫中可用，賦予使用者所有資料庫的讀寫許可權

userAdminAnyDatabase
只在admin資料庫中可用，賦予使用者所有資料庫的userAdmin許可權

dbAdminAnyDatabase
只在admin資料庫中可用，賦予使用者所有資料庫的dbAdmin許可權
```

## 特殊用法範例

### 監視和診斷資料庫效能 db.currentOp()

```JavaScript
// https://www.mongodb.com/docs/v7.0/reference/method/db.currentOp/
db.currentOp({
    active: true,
    //waitingForLock : true,
    //secs_running : { $gt : 3 }, //longer than 3 seconds
    //$ownOps:true, //returns information on the current user’s operations only.
    //$all:true, //including operations on idle connections and system operations
    //ns : /^db\.collection/
})?.inprog
```

## 連接字符串URI格式

```
mongodb://[username:password@]host1[:port1][,...hostN[:portN]][/[defaultauthdb][?options]]
```

## 使用 fsync 鎖定資料庫

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

# 配置範例

`bin/mongod.cfg`文件中會有 dbPath 和logPath的配置

位置:`/etc/mongod.conf`(設定完需重啟)

```sh
systemctl restart mongod
```

## 正式生產環境可用的設定模板

```yaml
# mongod.conf
# MongoDB 主要設定檔，使用 YAML 格式。
# 更多設定項官方文件：
#   https://docs.mongodb.org/manual/reference/configuration-options/

# ==========================================================
# 📦 資料儲存設定
# ==========================================================
storage:
  dbPath: /var/lib/mongodb        # MongoDB 資料庫檔案存放位置（建議不要放在 / 根目錄）
  journal:
    enabled: true                 # 啟用 journal 機制（確保崩潰後資料一致性）
  wiredTiger:                     # WiredTiger 為 MongoDB 預設儲存引擎
    engineConfig:
      cacheSizeGB: 24             # 設定 WiredTiger 緩存使用量（單位 GB）
                                  # 建議設定為實體記憶體的約 50%，避免吃光 RAM

# 若要更換引擎，可取消註解並設定：
#  engine: wiredTiger
#  mmapv1:
#  wiredTiger:

# ==========================================================
# 🧾 系統日誌設定
# ==========================================================
systemLog:
  destination: file               # 將日誌輸出到檔案（非 syslog）
  logAppend: true                 # 啟用追加模式（不覆蓋舊日誌）
  logRotate: reopen               # 允許透過 logrotate 重新開啟日誌檔案
  path: /var/log/mongodb/mongod.log  # 指定日誌檔案路徑

# ==========================================================
# 🌐 網路介面設定
# ==========================================================
net:
  port: 27017                     # MongoDB 預設監聽的 TCP 埠號
  bindIp: 0.0.0.0                 # 允許所有 IP 連線（⚠️ 建議限制特定內網 IP 以提高安全性）
  maxIncomingConnections: 65536   # 最大同時連線數（根據主機負載可調整）

# ==========================================================
# ⚙️ 進程管理（可選）
# ==========================================================
#processManagement:
#  fork: true                     # 若使用舊版 SysV，可設定讓 MongoDB 在背景執行

# ==========================================================
# 🔐 安全性設定
# ==========================================================
security:
  authorization: enabled          # 啟用使用者帳號密碼驗證（必開！）
  keyFile: /var/lib/mongodb/mongo.key  # Replica Set 節點間驗證用的金鑰檔
                                       # 需權限設為 400 並屬於 mongodb 用戶

# ==========================================================
# 🧠 操作效能分析（可選）
# ==========================================================
#operationProfiling:
#  mode: slowOp                   # 可設定紀錄慢查詢，預設關閉

# ==========================================================
# 🔁 複製集設定（Replica Set）
# ==========================================================
replication:
  oplogSizeMB: 15000              # oplog 大小（MB）
                                  # 建議依磁碟空間調整，越大可保留更久的操作記錄
  replSetName: avnight            # 複製集名稱（所有節點需一致）

# ==========================================================
# 🌎 分片（Sharding）設定（可選）
# ==========================================================
#sharding:
#  clusterRole: shardsvr          # 可設定為 configsvr 或 shardsvr

# ==========================================================
# 🏢 僅限 Enterprise 版本功能（社群版無效）
# ==========================================================
#auditLog:
#  destination: file
#  path: /var/log/mongodb/audit.log

#snmp:
#  subagent: true                 # SNMP 整合監控
```

| 功能項               | 說明                                                          |
| ----------------- | ----------------------------------------------------------- |
| **authorization** | 啟用帳號密碼驗證後，需先建立管理員帳號 (`use admin; db.createUser(...)`) 才能登入。 |
| **keyFile**       | 用於多節點複製集之間的自動驗證，所有節點需使用相同的 keyFile。                         |
| **bindIp**        | 若為外部伺服器開放服務，建議改為內部 IP（例如 `192.168.1.10`），或使用防火牆限制存取。        |
| **cacheSizeGB**   | 建議值為記憶體的一半，例如系統有 48GB RAM → 設 24GB。                         |
| **oplogSizeMB**   | 決定副本集可回放操作的時間長度（例如 15000MB 約能保存數小時到數天，依資料變動速度而定）。           |

`systemLog`

| 參數                                    | 功能說明                                                                                                                                            |
| ------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------- |
| **destination: file**                 | 指定日誌輸出目的地為「檔案」。<br>若設定為 `syslog` 則會寫入系統的 syslog，而不是檔案。                                                                                          |
| **logAppend: true**                   | 表示在重啟 MongoDB 時 **追加日誌** 到同一個檔案（不會覆蓋原有內容）。<br>若設為 `false`，則每次重啟 MongoDB 都會覆蓋舊日誌。                                                                |
| **logRotate: reopen**                 | 控制當外部程式（例如 `logrotate`）輪替日誌時的行為：<br> - `reopen`：在收到 `SIGUSR1` 訊號時重新開啟日誌檔案（適合搭配 Linux 的 `logrotate`）。<br> - `rename`：MongoDB 自行重新命名並建立新檔案（較少使用）。 |
| **path: /var/log/mongodb/mongod.log** | 指定日誌檔案路徑與檔名。預設放在 `/var/log/mongodb/`。<br>需確保 MongoDB 有權限寫入此檔案目錄。                                                                                |
