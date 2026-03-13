# MySQL 筆記 Cluster(叢集)

```
MySQL Cluster: 主要用於分散式資料庫系統，支援水平擴展，適用於大規模的高可用性和高容錯性需求。 常用於網路應用和需要分散式儲存的場景。
InnoDB: 預設儲存引擎，適用於交易處理和具有高並發性的應用。 強調資料的一致性和事務性，常見於企業級應用和關係型資料庫。
架構設計：

MySQL Cluster: 使用 NDB 儲存引擎，採用分散式架構，資料分片儲存在不同的節點上，具有自動分區和負載平衡的特性。
InnoDB: 部署在單一伺服器上，使用 B+ 樹索引結構，支援交易和行級鎖定。
可用性和容錯性：

MySQL Cluster: 提供高可用性和容錯性，支援節點故障自動恢復，資料複製和分散式事務。
InnoDB: 透過交易日誌和復原機制來確保資料的一致性，但不具備像 MySQL Cluster 那樣的分散式容錯能力。
性能：

MySQL Cluster: 適用於大規模分散式系統，具有良好的水平擴展性，可以處理大量的並發請求。
InnoDB: 在單一伺服器上處理事務性操作時表現良好，但相對於 MySQL Cluster 在分散式環境中的規模可能較小。
事務支援：

MySQL Cluster: 支援分散式事務，具有 ACID 特性，適用於複雜的多節點操作。
InnoDB: 也支援事務，是一個關係型資料庫引擎，適用於需要強調事務性的應用。
```

## 目錄

- [MySQL 筆記 Cluster(叢集)](#mysql-筆記-cluster叢集)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [MySQL Shell 相關](#mysql-shell-相關)
    - [MySQL Router 相關](#mysql-router-相關)
    - [叢集(Cluster)相關](#叢集cluster相關)
      - [InnoDB Cluster](#innodb-cluster)
      - [NDB Cluster (MySQL NDB Cluster 7.6 要求 MySQL Server 的最低版本為 MySQL Server 8.0)](#ndb-cluster-mysql-ndb-cluster-76-要求-mysql-server-的最低版本為-mysql-server-80)
    - [錯誤處理相關](#錯誤處理相關)
      - [InnoDB Cluster 錯誤](#innodb-cluster-錯誤)
- [安裝步驟](#安裝步驟)
  - [安裝 NDB Cluster](#安裝-ndb-cluster)
  - [安裝 InnoDB Cluster](#安裝-innodb-cluster)
    - [安裝 MySQL Router](#安裝-mysql-router)
      - [MySQL Router 配置文檔](#mysql-router-配置文檔)
      - [MySQL Router Docker Compose 配置](#mysql-router-docker-compose-配置)
    - [安装 MySQL Shell](#安装-mysql-shell)
- [指令](#指令)
  - [MySQL Router 指令](#mysql-router-指令)
  - [MySQL Shell 指令](#mysql-shell-指令)
  - [mysqlbinlog - 檢查主資料庫中的二進制日誌（Binary Log）](#mysqlbinlog---檢查主資料庫中的二進制日誌binary-log)
  - [ndb Cluster 相關](#ndb-cluster-相關)
- [SQL 指令](#sql-指令)
  - [innodb cluster 相關](#innodb-cluster-相關)
- [Cluster 叢集架設](#cluster-叢集架設)
  - [說明](#說明)
    - [GTID 模式](#gtid-模式)
  - [NDB Cluster 實作](#ndb-cluster-實作)
    - [Linux](#linux)
      - [Manage node (管理節點) - 負責監控叢集所有 Nodes 的狀態，並且由此控制所有 Nodes 的替換。](#manage-node-管理節點---負責監控叢集所有-nodes-的狀態並且由此控制所有-nodes-的替換)
      - [Data node - 負責所有 SQL Data 的 Nodes，單純儲存資料，將資料寫在 RAM \& Disk。](#data-node---負責所有-sql-data-的-nodes單純儲存資料將資料寫在-ram--disk)
      - [SQL node (原本的 MySQL Server) - 負責 SQL 的 Table schema 和 Client 連接的空間。](#sql-node-原本的-mysql-server---負責-sql-的-table-schema-和-client-連接的空間)
  - [InnoDB Cluster 實作](#innodb-cluster-實作)
    - [說明](#說明-1)
    - [MySQL 5.7 設定檔](#mysql-57-設定檔)
    - [MySQL 5.7 SQL指令](#mysql-57-sql指令)
    - [MySQL 5.7 mysqlsh指令](#mysql-57-mysqlsh指令)
    - [MySQL 5.7 router 設定檔](#mysql-57-router-設定檔)
    - [MySQL 5.7 router 指令](#mysql-57-router-指令)
    - [初始化 Group Replication (全部成員掛掉,但設定還在)](#初始化-group-replication-全部成員掛掉但設定還在)
        - [優化配置](#優化配置)
    - [使用程式腳本建立 InnoDB Cluster](#使用程式腳本建立-innodb-cluster)
      - [JavaScript](#javascript)
      - [Python](#python)
- [重大備份](#重大備份)
- [例外狀況](#例外狀況)
  - [has the following errant GTIDs that do not exist in the cluster](#has-the-following-errant-gtids-that-do-not-exist-in-the-cluster)
  - [修復損壞的innodb：innodb\_force\_recovery](#修復損壞的innodbinnodb_force_recovery)
  - [升級為GTID模式](#升級為gtid模式)
  - [ERROR 3009 (HY000): Column count of mysql.user is wrong. Expected 45, found 43. Created with MySQL 50739, now running 50742. Please use mysql\_upgrade to fix this error.](#error-3009-hy000-column-count-of-mysqluser-is-wrong-expected-45-found-43-created-with-mysql-50739-now-running-50742-please-use-mysql_upgrade-to-fix-this-error)
  - [rebootClusterFromCompleteOutage 發生錯誤](#rebootclusterfromcompleteoutage-發生錯誤)
    - [表已滿 無法寫入](#表已滿-無法寫入)
  - [重啟後 使用 mysqlsh 創建 cluster 失敗](#重啟後-使用-mysqlsh-創建-cluster-失敗)
  - [所有成員都重新啟動過](#所有成員都重新啟動過)

## 參考資料

[MySQl官方網站](https://dev.mysql.com/)

[MySQL 全部文檔](https://dev.mysql.com/doc/)

[MySQL 8.0版本 指令](https://dev.mysql.com/doc/refman/8.0/en/mysql-command-options.html)

[MySQL 8.0版本 文檔 - 使用搜尋查詢](https://dev.mysql.com/doc/refman/8.0/en/)

[MySQL 5.7版本 文檔 - 使用搜尋查詢](https://dev.mysql.com/doc/refman/5.7/en/)

[MySQL 教程](https://www.itread01.com/study/mysql-tutorial.html)

[MySQL Community Downloads - MySQL 社區下載](https://dev.mysql.com/downloads/)

[Connectors and APIs - MySQL 連接器和 API 是用於將不同編程語言的應用程序連接到 MySQL 數據庫服務器的驅動程序和庫](https://dev.mysql.com/doc/index-connectors.html)

### MySQL Shell 相關

[MySQL 工具 MySQL Shell(交互式的命令行工具)](../MySQL/MySQL%20工具%20MySQL%20Shell(交互式的命令行工具).md)

### MySQL Router 相關

[MySQL 工具 Mysql Router(輕量級的路由器)](../MySQL/MySQL%20工具%20MySQL%20Router(輕量級的路由器).md)

### 叢集(Cluster)相關

```
叢集的概念就是把一台式架構拆分為多台式架構，並且可以提供 HA 高可用性與負載均衡的需求，更不需要擔心延展性的問題，若是 Loading 加大了只需要增加 node 去分擔 Loading

MySQL InnoDB Cluster 用於實現高可用性的數據庫集群

MySQL Cluster (NDB Cluster) 則用於構建高性能和實時的數據庫集群
MySQL Server 5.7 版本不支持 NDB Cluster
```

[Galera Cluster for MySQL 详解（一）——基本原理](https://blog.csdn.net/wzy0623/article/details/102522268)

[如何建置 MariaDb Galera Cluster](https://gary840227.medium.com/mariadb-cluster-f7220e9eaac8)

[mysql-shell部署MGR](https://blog.51cto.com/u_15072912/4389165)

[MYSQL innodb cluster 到底会不会因为网络影响性能](https://www.modb.pro/db/24595)

#### InnoDB Cluster

```
高可用性和容錯性解決方案，用於構建具有自動故障轉移和數據備份能力的數據庫集群。

主要基於 MySQL 的 InnoDB 存儲引擎，使用組複製（Group Replication）技術來實現多主複製和自動故障轉移。

InnoDB Cluster 提供了一組工具和功能，使可以輕鬆地設置和管理具有高可用性和自動故障恢復能力的 MySQL 數據庫集群。
```

[17.2.1.3 User Credentials - 用戶權限](https://dev.mysql.com/doc/refman/5.7/en/group-replication-user-credentials.html)

[Docker Compose Setup for InnoDB Cluster](https://dev.mysql.com/blog-archive/docker-compose-setup-for-innodb-cluster/)

[neumayer/mysql-docker-compose-examples](https://github.com/neumayer/mysql-docker-compose-examples/tree/master)

[centos7+mysql5.7集群安装](https://blog.csdn.net/onlycool_me/article/details/78614400?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-5-78614400-blog-114349883.235%5Ev38%5Epc_relevant_anti_vip_base&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-5-78614400-blog-114349883.235%5Ev38%5Epc_relevant_anti_vip_base&utm_relevant_index=6)

[Docker Compose Setup for InnoDB Cluster](https://dev.mysql.com/blog-archive/docker-compose-setup-for-innodb-cluster/)

[Centos 7.5基于MySQL 5.7的 InnoDB Cluster 多节点高可用集群环境部署记录](https://www.cnblogs.com/kevingrace/p/10466530.html)

[MySQL InnoDB Cluster 搭建 (MySQL 5.7.25)](https://dongrenwen.github.io/2020/04/17/install-mysql-cluster/#%E8%BD%AF%E4%BB%B6%E7%89%88%E6%9C%AC)

[《Day16》如何建置MySQL Innodb cluster](https://ithelp.ithome.com.tw/articles/10239904)

[Setting up an InnoDB Cluster With a Few Lines of Code - 用幾行代碼設置一個InnoDB集群](https://www.percona.com/blog/setting-up-an-innodb-cluster-with-a-few-lines-of-code/)

[MySQL InnoDB Cluster环境搭建和简单测试](https://cloud.tencent.com/developer/article/1069016)

[基于MySQL innodb cluster和MySQL router的高可用与读写分离](https://www.cnblogs.com/fander/p/10071357.html)

[Ubuntu20.04搭建MySQL InnoDB 集群](http://www.884358.com/ubuntu-mysql-innodb-cluster/)

[MySQL InnoDB Cluster - 完整教學](https://blog.51cto.com/u_4223248/5594829#4-%E9%9B%86%E7%BE%A4%E5%A4%9A%E6%95%B0%E8%8A%82%E7%82%B9%E5%BC%82%E5%B8%B8%E6%81%A2%E5%A4%8D)

[Mysql InnoDB Cluster集群 日常維護命令](https://www.cnblogs.com/wangjunjiehome/p/16267655.html)

[MySQL-innodb-cluster高可用 - 兩個router 測試](https://skyisfuck.gitee.io/docs/mysql/mysql-innodb-cluster/MySQL-innodb-cluster/)

[設置 InnoDB Cluster 和 MySQL Router - 說明 Incremental recovery,Clone](https://dev.mysql.com/doc/mysql-shell/8.0/en/setting-up-innodb-cluster-and-mysql-router.html)

[MySQL InnoDB Cluster - 心得](https://www.cnblogs.com/zhenxing/p/15102381.html)

[【InnoDB Cluster】修改已有集群实例名称及成员实例选项](https://blog.csdn.net/wudi53433927/article/details/128026314)

[MySQL InnoDB Cluster - mysql shell 選項範例](https://www.twblogs.net/a/610b71acb2b95a05ecfbd5b3)

[基于mysqlsh部署mysql8.0的MGR - 准备账号](https://www.cnblogs.com/bjx2020/p/15469530.html)

[15.14 InnoDB Startup Options and System Variables - MySQL 8.0 InnoDB系統選項](https://dev.mysql.com/doc/refman/8.0/en/innodb-parameters.html)

[14.15 InnoDB Startup Options and System Variables - MySQL 5.7 InnoDB系統選項](https://dev.mysql.com/doc/refman/5.7/en/innodb-parameters.html)

[17.3.1 Group Replication Requirements - 用組複製的服務器實例必須滿足要求](https://dev.mysql.com/doc/refman/5.7/en/group-replication-requirements.html)

[MySQL InnoDB Cluster - Navigating the Cluster - Group or Replica Set 狀態說明](https://dev.mysql.com/blog-archive/mysql-innodb-cluster-navigating-the-cluster/)

[17.1.3.3 Fault-tolerance - MySQL 5.7 容錯, 正常節點低於數量會出現 NO_Quorum 狀態](https://dev.mysql.com/doc/refman/8.0/en/group-replication-fault-tolerance.html)

[MySQL InnoDB Cluster 8.0 – A Complete Deployment Walk-Through: Part One](https://severalnines.com/blog/mysql-innodb-cluster-80-complete-deployment-walk-through-part-one/)

[MySQL InnoDB Cluster 8.0 – A Complete Operation Walk-through: Part Two](https://severalnines.com/blog/mysql-innodb-cluster-80-complete-operation-walk-through-part-two/)

[MySQL8.0 高可用叢集化 · mysql-shell · mysql-router · docker · 單主多從 - mysql router docker image 範例](https://tw511.com/a/01/53756.html)

#### NDB Cluster (MySQL NDB Cluster 7.6 要求 MySQL Server 的最低版本為 MySQL Server 8.0)

```
一種高性能、實時數據庫集群解決方案，專為需要在高負載情況下處理大量事務和數據的應用程序而設計。

MySQL Cluster 使用 NDB 存儲引擎，該存儲引擎支持內存中數據存儲和分佈式存儲，並提供了高可用性和分區容錯性。

MySQL Cluster 適用於需要水平擴展和處理大量事務的應用場景，例如電信、金融和在線遊戲等。
```

[Chapter 21 MySQL NDB Cluster 7.5 and NDB Cluster 7.6 - MySQL 5.7 叢集](https://dev.mysql.com/doc/refman/5.7/en/mysql-cluster.html)

[21.4.2.1 NDB Cluster 數據節點配置參數](https://dev.mysql.com/doc/refman/5.7/en/mysql-cluster-params-ndbd.html)

[21.4.1 NDB Cluster 的快速測試設置](https://dev.mysql.com/doc/refman/5.7/en/mysql-cluster-quick.html)

[官方下載 NDB Cluster MySQL Community Downloads - MySQL Cluster](https://dev.mysql.com/downloads/cluster/)

[官方 NDB Cluster 發行指南 版本說明 ](https://dev.mysql.com/doc/index-cluster.html)

[官方 NDB Cluster 用戶手冊(右上角可切換版本)](https://dev.mysql.com/doc/mysql-cluster-manager/8.0/en/)

[架設 HA 高可用性：MySQL Cluster 叢集 – 7.4.11(5.6.29)](https://shazi.info/%E6%9E%B6%E8%A8%AD-ha-%E9%AB%98%E5%8F%AF%E7%94%A8%E6%80%A7%EF%BC%9Amysql-cluster-%E5%8F%A2%E9%9B%86-7-4-115-6-29/)

[Mysql Cluster 資料叢集](https://youyouyou.pixnet.net/blog/post/119326123-mysql-cluster-%E5%AF%A6%E5%81%9A)

### 錯誤處理相關

#### InnoDB Cluster 錯誤

[Multi Source Replication MySQL 5.6 to 5.7 GTID Auto Position Issues](https://stackoverflow.com/questions/30606345/multi-source-replication-mysql-5-6-to-5-7-gtid-auto-position-issues)

[Plugin group_replication reported: '[GCS] Connection attempt from IP address ::ffff:10.57.19.100 refused. Address is not in the IP whitelist.'](https://www.cnblogs.com/Miac/p/11990725.html)

[MySQL Shell无法拉起MGR集群解决办法 - rebootClusterFromCompleteOutage失敗 因为MySQL 5.7中还不支持 SET PERSIST 功能](https://ost.51cto.com/posts/16247)

[1114 (HY000): The table is full](https://stackoverflow.com/questions/730579/1114-hy000-the-table-is-full)

[How can I troubleshoot the error "MySQL HA_ERR_RECORD_FILE_FULL" when I use Amazon RDS for MySQL? - 1114](https://repost.aws/knowledge-center/rds-error-mysql-record-file-full)

[7.8.2 Restoring a Cluster from Quorum Loss](https://dev.mysql.com/doc/mysql-shell/8.0/en/restore-cluster-from-quorum-loss.html)

# 安裝步驟

## 安裝 NDB Cluster

```bash
# 所有的 nodes 都需要安裝 mysql-cluster
wget https://dev.mysql.com/get/Downloads/MySQL-Cluster-7.4/mysql-cluster-gpl-7.4.11-linux-glibc2.5-i686.tar.gz
# wget https://dev.mysql.com/get/Downloads/MySQL-Cluster-7.6/mysql-cluster-gpl-7.6.27-linux-glibc2.12-x86_64.tar.gz
tar -zxvf mysql-cluster-gpl-7.4.11-linux-glibc2.5-i686.tar.gz
mkdir /usr/local/mysql
mv mysql-cluster-gpl-7.4.11-linux-glibc2.5-i686/* /usr/local/mysql

wget https://dev.mysql.com/get/Downloads/MySQL-Cluster-7.5/mysql-cluster-gpl-7.5.31-linux-glibc2.12-i686.tar.gz
tar -zxvf mysql-cluster-gpl-7.5.31-linux-glibc2.12-i686.tar.gz
mkdir /usr/local/mysql
mv mysql-cluster-gpl-7.5.31-linux-glibc2.12-x86_64/* /usr/local/mysql

# ndb_mgm 和 ndb_mgmd 等工具放到 /usr/local/bin 方便使用
cp /usr/local/mysql/bin/ndb_mgm* /usr/local/bin/
```

## 安裝 InnoDB Cluster

### 安裝 MySQL Router

```bash
# 安裝 MySQL 軟件庫的配置文件
# Debian 系統
dpkg -i mysql-apt-config_0.8.25-1_all.deb

# 更新套件信息
apt-get update

# 安裝 MySQL Router
apt-get -y install mysql-router

# CentOS 7 系統
# 安裝 MySQL 軟件庫的 RPM 配置文件
rpm -Uvh mysql80-community-release-el7-7.noarch.rpm

# 安裝 MySQL Router
yum install -y mysql-router-community

# 查看系統架構
uname -m

# CentOS 7 系統 自己下載安裝
# https://dev.mysql.com/downloads/router/
yum update -y
yum install -y wget tar
wget https://dev.mysql.com/get/Downloads/MySQL-Router/mysql-router-8.1.0-linux-glibc2.28-x86_64.tar.xz
tar -zxvf mysql-router-8.1.0-linux-glibc2.28-x86_64.tar.xz
mkdir /usr/local/mysql-router
mv /root/mysql-router-8.1.0-linux-glibc2.28-x86_64/* /usr/local/mysql-router
export PATH=$PATH:/usr/local/mysql-router/bin
echo "export PATH=$PATH:/usr/local/mysql-router/bin" >> /root/.bashrc
source /root/.bashrc
```

#### MySQL Router 配置文檔

```conf
# [4.1 Configuration File Syntax - 配置文件語法](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-syntax.html)
# [4.3.3 Configuration File Options - 配置文檔選項](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-conf-options.html)
# [4.3.4 Configuration File Example - 配置文檔範例](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-example.html)

# MySQL Router sample configuration
#
# The following is a sample configuration file which shows
# most of the plugins available and most of their options.
#
# The paths used are defaults and should be adapted based
# on how MySQL Router was installed, for example, using the
# CMake option CMAKE_INSTALL_PREFIX
#
# The logging_folder is kept empty so message go to the
# console.
#

[DEFAULT]
# 設定日誌文件的保存目錄
logging_folder = /home/cluster/mysql-router-2.1.6/logs
# 設定插件文件的目錄
plugin_folder = /home/cluster/mysql-router-2.1.6/lib/mysqlrouter
# 設定配置文件的目錄
config_folder = /home/cluster/mysql-router-2.1.6
# 設定運行時文件的目錄
runtime_folder = /home/cluster/mysql-router-2.1.6/run
# 設定資料文件的目錄
data_folder = /var/lib
# 設定金鑰環的路徑
keyring_path = /var/lib/keyring-data
# 設定主金鑰的路徑
master_key_path = /var/lib/keyring-key

[logger]
# 設定日誌記錄的級別
level = INFO

[routing:basic_failover]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address = 0.0.0.0
# 綁定的監聽端口
bind_port = 23306
# 設定模式，這裡設定為 read-write，表示這個路由器實例可以處理讀和寫的查詢
mode = read-write
# 設定目標資料庫實例的地址和端口，這裡設定兩個目標實例
destinations = 192.168.62.37:3306,192.168.62.15:3306

[routing:balancing]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address = 0.0.0.0
# 綁定的監聽端口
bind_port = 23307
# 設定最大連接數
max_connections = 1024
# 設定模式，這裡設定為 read-only，表示這個路由器實例只處理讀取查詢
mode = read-only
# 設定目標資料庫實例的地址和端口
destinations = 192.168.62.15:3306,192.168.62.37:3306

[keepalive]
# 設定心跳檢測的間隔時間，這裡設定為 60 秒
# 在設定的間隔時間內，MySQL Router 會定期向後端數據庫服務器發送心跳消息，以確保連接的有效性。
interval = 60
```

```conf
# [4.1 Configuration File Syntax - 配置文件語法](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-syntax.html)
# [4.3.3 Configuration File Options - 配置文檔選項](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-conf-options.html)
# [4.3.4 Configuration File Example - 配置文檔範例](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-example.html)

[DEFAULT]
# 設定日誌文件的保存目錄
# logging_folder = /home/cluster/mysql-router-2.1.6/logs
# 設定插件文件的目錄
# plugin_folder = /home/cluster/mysql-router-2.1.6/lib/mysqlrouter
# 設定配置文件的目錄
# config_folder = /home/cluster/mysql-router-2.1.6
# 設定運行時文件的目錄
# runtime_folder = /home/cluster/mysql-router-2.1.6/run

[logger]
# 設定日誌記錄的級別
level = INFO

[metadata_cache:avnight]
# 元數據緩存的名稱，可根據需要命名
router_id=1
# 啟動元數據緩存時要連接的目標 MySQL 服務器地址和端口，多個地址使用逗號分隔
bootstrap_server_addresses=node_1:3306,node_2:4306,node_3:5306,node_4:6306,node_5:7306,node_6:8306,node_7:9306
# 連接 MySQL 服務器時要使用的用戶名
user=root
# 元數據緩存所屬的集群名稱，用於識別元數據緩存屬於哪個集群
metadata_cluster=avnight
# 元數據的存活時間（Time-To-Live），單位是秒。這裡設置為 0.5 秒，表示元數據在緩存中的存活時間很短。
ttl=0.5

[routing:primary]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address=0.0.0.0
# 綁定的監聽端口
bind_port=6446
# 設定最大連接數
max_connections = 1024
# 設定模式，這裡設定為 read-write，表示這個路由器實例可以處理讀和寫的查詢
mode=read-write
# 設定目標資料庫實例的地址和端口
destinations=metadata-cache://avnight/default?role=PRIMARY
# 假設有三個目標資料庫實例，它們的地址分別是 A、B 和 C。如果路由策略設置為 routing_strategy=round-robin，則第一個查詢會被發送到實例 A，第二個查詢會被發送到實例 B，第三個查詢會被發送到實例 C，然後循環進行。
# 配置將查詢請求按照順序依次分發給不同的目標資料庫實例，實現查詢負載均衡。
# 這種策略適用於平均分配查詢請求的情況，但不考慮目標實例的當前負載或可用性。
routing_strategy=round-robin
protocol=classic

[routing:secondary]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address=0.0.0.0
# 綁定的監聽端口
bind_port=6447
# 設定最大連接數
max_connections = 1024
# 設定模式，這裡設定為 read-only，表示這個路由器實例只處理讀取查詢
mode = read-only
# 設定目標資料庫實例的地址和端口
# destinations: 這個參數指定了路由的目標，即要將查詢路由到哪些資料庫實例。
# 在這個範例中，metadata-cache://ClusterName/default?role=SECONDARY 表示要將查詢路由到元數據緩存（metadata cache）中標記為 SECONDARY 角色的資料庫實例。
# metadata-cache://: 這是一個指示 MySQL Router 使用元數據緩存的 URL 方案。
# ClusterName/default: ClusterName 是元數據緩存的名稱，default 表示元數據緩存中的預設配置。
# role=SECONDARY: 這是一個查詢參數，表示只選擇元數據緩存中標記為 SECONDARY 角色的資料庫實例作為目標。這通常用於路由讀取查詢到次要節點，以實現讀取的負載平衡和高可用性。
destinations=metadata-cache://avnight/default?role=SECONDARY
# 這個參數設定了路由策略為“循環分發（Round Robin）加備用（Fallback）”。
# 這意味著 MySQL Router 會按照設定的順序將查詢請求依次分發給目標資料庫實例，如果其中某個實例無法正常響應或處於不可用狀態，Router 會自動將查詢轉發給下一個可用的實例。
# 這樣可以在一定程度上實現高可用性和負載均衡。
routing_strategy=round-robin-with-fallback
# 這個參數設定了數據庫連接所使用的協議為“經典協議（Classic Protocol）”。
# MySQL Router 支持多種協議，包括經典協議和 X Protocol。經典協議是傳統的 MySQL 連接協議，而 X Protocol 則是 MySQL 的新一代協議，提供更好的性能和功能支持。
protocol=classic

[keepalive]
# 設定心跳檢測的間隔時間，這裡設定為 60 秒
# 在設定的間隔時間內，MySQL Router 會定期向後端數據庫服務器發送心跳消息，以確保連接的有效性。
interval = 60
```

```conf
; https://www.cnblogs.com/fander/p/10071357.html
; mysqlrouter.conf
[DEFAULT]
name=system
user=root
keyring_path=/opt/mysql-8.0-router/data/keyring
master_key_path=/opt/mysql-8.0-router/bin/.././mysqlrouter.key
connect_timeout=30
read_timeout=30

[logger]
level = INFO

[metadata_cache:fandercluster]
router_id=1
bootstrap_server_addresses=mysql://192-168-199-122:3306,mysql://192-168-199-123:3306,mysql://192-168-199-121:3306
user=mysql_router1_klh7m3xmmru0
metadata_cluster=fandercluster
ttl=0.5

[routing:fandercluster_default_rw]
bind_address=0.0.0.0
bind_port=6446
# destinations: 這個參數指定了路由的目標，即要將查詢路由到哪些資料庫實例。
# 在這個範例中，metadata-cache://fandercluster/default?role=PRIMARY 表示要將查詢路由到元數據緩存（metadata cache）中標記為 PRIMARY 角色的資料庫實例。
# metadata-cache://: 這是一個指示 MySQL Router 使用元數據緩存的 URL 方案。
# fandercluster/default: fandercluster 是元數據緩存的名稱，default 表示元數據緩存中的預設配置。
# role=PRIMARY: 這是一個查詢參數，表示只選擇元數據緩存中標記為 PRIMARY 角色的資料庫實例作為目標。這通常用於路由讀取查詢到次要節點，以實現讀取的負載平衡和高可用性。
destinations=metadata-cache://fandercluster/default?role=PRIMARY
# 假設有三個目標資料庫實例，它們的地址分別是 A、B 和 C。如果路由策略設置為 routing_strategy=round-robin，則第一個查詢會被發送到實例 A，第二個查詢會被發送到實例 B，第三個查詢會被發送到實例 C，然後循環進行。
# 配置將查詢請求按照順序依次分發給不同的目標資料庫實例，實現查詢負載均衡。
# 這種策略適用於平均分配查詢請求的情況，但不考慮目標實例的當前負載或可用性。
routing_strategy=round-robin
# 這個參數設定了數據庫連接所使用的協議為“經典協議（Classic Protocol）”。
# MySQL Router 支持多種協議，包括經典協議和 X Protocol。經典協議是傳統的 MySQL 連接協議，而 X Protocol 則是 MySQL 的新一代協議，提供更好的性能和功能支持。
protocol=classic

[routing:fandercluster_default_ro]
bind_address=0.0.0.0
bind_port=6447
destinations=metadata-cache://fandercluster/default?role=SECONDARY
#routing_strategy=round-robin
routing_strategy=round-robin-with-fallback
protocol=classic

[routing:fandercluster_default_x_rw]
bind_address=0.0.0.0
bind_port=64460
destinations=metadata-cache://fandercluster/default?role=PRIMARY
routing_strategy=round-robin
protocol=x

[routing:fandercluster_default_x_ro]
bind_address=0.0.0.0
bind_port=64470
destinations=metadata-cache://fandercluster/default?role=SECONDARY
routing_strategy=round-robin
protocol=x
```

```conf
; router.conf
[DEFAULT]
user=mysqlrouter
keyring_path=/var/lib/mysqlrouter/keyring

[mysqlrouter]
user=mysqlrouter
password=routerpassword

; 此處的 routing:primary 表示主要路由
[routing:primary]
bind_address=127.0.0.1
bind_port=3306
destinations=127.0.0.1:3306
routing_strategy=first-available
```

#### MySQL Router Docker Compose 配置

```yml
version: '2'
services:

common: &baseDefine
    environment:
        MYSQL_HOST: "192.168.213.6"
        MYSQL_PORT: 3306
        MYSQL_USER: 'root'
        MYSQL_PASSWORD: 'urpwd.root'
        MYSQL_INNODB_CLUSTER_MEMBERS: 3
        MYSQL_CREATE_ROUTER_USER: 0
    image: "docker.io/mysql/mysql-router:latest"

    volumes:
        - "./conf:/tmp/myrouter"

    network_mode: "host"

boot:
    container_name: "mysql_router_boot"

    command: ["mysqlrouter","--bootstrap","root@192.168.213.7:3306","--directory","/tmp/myrouter","--conf-use-sockets","--conf-skip-tcp",
                    "--force","--strict","--user","mysqlrouter","--account","sqlrouter","--account-create","if-not-exists"]

    <<: *baseDefine


run:
    container_name: "mysql_router"
    restart: always

    command: ["mysqlrouter","--config","/tmp/myrouter/mysqlrouter.conf"]

    <<: *baseDefine
```

```bash
#!/bin/bash
# Copyright (c) 2018, 2021, Oracle and/or its affiliates.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; version 2 of the License.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
set -e

if [ "$1" = 'mysqlrouter' ]; then
    if [[ -z $MYSQL_HOST || -z $MYSQL_PORT || -z $MYSQL_USER || -z $MYSQL_PASSWORD ]]; then
	    echo "We require all of"
	    echo "    MYSQL_HOST"
	    echo "    MYSQL_PORT"
	    echo "    MYSQL_USER"
	    echo "    MYSQL_PASSWORD"
	    echo "to be set."
	    echo "In addition you can set"
	    echo "    MYSQL_INNODB_CLUSTER_MEMBERS "
	    echo "    MYSQL_CREATE_ROUTER_USER"
	    echo "    MYSQL_ROUTER_BOOTSTRAP_EXTRA_OPTIONS"
	    echo "Exiting."
	    exit 1
    fi

    PASSFILE=$(mktemp)
    echo "$MYSQL_PASSWORD" > "$PASSFILE"
    if [ -z $MYSQL_CREATE_ROUTER_USER ]; then
      echo "$MYSQL_PASSWORD" >> "$PASSFILE"
      MYSQL_CREATE_ROUTER_USER=1
      echo "[Entrypoint] MYSQL_CREATE_ROUTER_USER is not set, Router will generate a new account to be used at runtime."
      echo "[Entrypoint] Set it to 0 to reuse $MYSQL_USER instead."
    elif [ "$MYSQL_CREATE_ROUTER_USER" = "0" ]; then
      echo "$MYSQL_PASSWORD" >> "$PASSFILE"
      echo "[Entrypoint] MYSQL_CREATE_ROUTER_USER is 0, Router will reuse $MYSQL_USER account at runtime"
    else
      echo "[Entrypoint] MYSQL_CREATE_ROUTER_USER is not 0, Router will generate a new account to be used at runtime"
    fi

    DEFAULTS_EXTRA_FILE=$(mktemp)
    cat >"$DEFAULTS_EXTRA_FILE" <<EOF
[client]
password="$MYSQL_PASSWORD"
EOF
    unset MYSQL_PASSWORD
    max_tries=12
    attempt_num=0
    until (echo > "/dev/tcp/$MYSQL_HOST/$MYSQL_PORT") >/dev/null 2>&1; do
      echo "[Entrypoint] Waiting for mysql server $MYSQL_HOST ($attempt_num/$max_tries)"
      sleep $(( attempt_num++ ))
      if (( attempt_num == max_tries )); then
        exit 1
      fi
    done
    echo "[Entrypoint] Succesfully contacted mysql server at $MYSQL_HOST:$MYSQL_PORT. Checking for cluster state."
    if ! [[ "$(mysql --defaults-extra-file="$DEFAULTS_EXTRA_FILE" -u "$MYSQL_USER" -h "$MYSQL_HOST" -P "$MYSQL_PORT" -e "show status;" 2> /dev/null)" ]]; then
      echo "[Entrypoint] ERROR: Can not connect to database. Exiting."
      exit 1
    fi
    if [[ -n $MYSQL_INNODB_CLUSTER_MEMBERS ]]; then
      attempt_num=0
      echo $attempt_num
      echo $max_tries
      until [ "$(mysql --defaults-extra-file="$DEFAULTS_EXTRA_FILE" -u "$MYSQL_USER" -h "$MYSQL_HOST" -P "$MYSQL_PORT" -N performance_schema -e "select count(MEMBER_STATE) >= $MYSQL_INNODB_CLUSTER_MEMBERS from replication_group_members where MEMBER_STATE = 'ONLINE';" 2> /dev/null)" -eq 1 ]; do
             echo "[Entrypoint] Waiting for $MYSQL_INNODB_CLUSTER_MEMBERS cluster instances to become available via $MYSQL_HOST ($attempt_num/$max_tries)"
             sleep $(( attempt_num++ ))
             if (( attempt_num == max_tries )); then
                     exit 1
             fi
      done
      echo "[Entrypoint] Successfully contacted cluster with $MYSQL_INNODB_CLUSTER_MEMBERS members. Bootstrapping."
    fi
    if [ $(id -u) = "0" ]; then
      opt_user=--user=mysqlrouter
    fi
    if [ "$MYSQL_CREATE_ROUTER_USER" = "0" ]; then
        echo "[Entrypoint] Succesfully contacted mysql server at $MYSQL_HOST. Trying to bootstrap reusing account \"$MYSQL_USER\"."
        mysqlrouter --bootstrap "$MYSQL_USER@$MYSQL_HOST:$MYSQL_PORT" --directory /tmp/mysqlrouter --force --account-create=never --account=$MYSQL_USER $opt_user $MYSQL_ROUTER_BOOTSTRAP_EXTRA_OPTIONS < "$PASSFILE" || exit 1
    else
        echo "[Entrypoint] Succesfully contacted mysql server at $MYSQL_HOST. Trying to bootstrap."
        mysqlrouter --bootstrap "$MYSQL_USER@$MYSQL_HOST:$MYSQL_PORT" --directory /tmp/mysqlrouter --force $opt_user $MYSQL_ROUTER_BOOTSTRAP_EXTRA_OPTIONS < "$PASSFILE" || exit 1
    fi

    sed -i -e 's/logging_folder=.*$/logging_folder=/' /tmp/mysqlrouter/mysqlrouter.conf
    echo "[Entrypoint] Starting mysql-router."
    exec "$@" --config /tmp/mysqlrouter/mysqlrouter.conf

    rm -f "$PASSFILE"
    rm -f "$DEFAULTS_EXTRA_FILE"
    unset DEFAULTS_EXTRA_FILE
else
    exec "$@"
fi
```

### 安装 MySQL Shell

```bash
brew install mysql-shell

# Ubuntu
wget https://dev.mysql.com/get/mysql-apt-config_0.8.17-1_all.deb
dpkg -i mysql-apt-config_0.8.17-1_all.deb
apt update
apt install mysql-shell

# CentOS7
yum update -y
yum install -y wget tar
wget https://dev.mysql.com/get/Downloads/MySQL-Shell/mysql-shell-8.1.1-linux-glibc2.28-x86-64bit.tar.gz
tar -zxvf mysql-shell-8.1.1-linux-glibc2.28-x86-64bit.tar.gz
mkdir /usr/local/mysql-shell
mv /root/mysql-shell-8.1.1-linux-glibc2.28-x86-64bit/* /usr/local/mysql-shell
export PATH=$PATH:/usr/local/mysql-shell/bin
echo "export PATH=$PATH:/usr/local/mysql-shell/bin" >> /root/.bashrc
source /root/.bashrc


wget https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
rpm -ivh mysql80-community-release-el7-3.noarch.rpm
yum install -y mysql-shell
```

# 指令

## MySQL Router 指令

```bash
# https://dev.mysql.com/doc/mysql-router/8.0/en/mysqlrouter.html#option_mysqlrouter_force-password-validation
# 查看指令
mysqlrouter --help

# 啟動 MySQL Router
mysqlrouter -c /path/to/router.conf
```

## MySQL Shell 指令

```JavaScript
// 通過 shell 連接 mysql
shell.connect('root@localhost:3306')

// 檢查實例配置，此處根據報錯修改配置文件，修改後需要重啟 MySQL(檢查正常會回傳 status:ok, 非必要)
dba.checkInstanceConfiguration('root@node_1:3306')
dba.checkInstanceConfiguration('root@node_2:3306')
dba.checkInstanceConfiguration('root@node_3:3306')

// 分別登陸到每個節點的主機，再登陸 mysql-shell 進行持久化操作
shell.connect('root@node_1:3306')
shell.connect('root@node_2:3306')
shell.connect('root@node_3:3306')

// \disconnect  //退出連接

// 自動設置 Group Replication： 如果當前實例還沒有啟用 Group Replication，函數將自動執行必要的步驟來啟用 Group Replication。
// 自動加入 InnoDB Cluster： 如果當前實例是 InnoDB Cluster 的一部分，函數將自動將實例加入到 InnoDB Cluster 中。
// 配置和驗證參數： 函數將根據配置文件和集群設置來配置和驗證實例的參數，以確保其與其他實例保持一致。
// 持久化到配置⽂件中 (此功能僅適用於本地實例)
// 這個功能只能在本地的 MySQL 實例上使用，無法用於遠程的 MySQL 實例。
// 在這個上下文中， "本地實例" 指的是執行 MySQL Shell 的計算機上的 MySQL 伺服器。
dba.configureLocalInstance()

// 創建集群 (shell.connect 登入到一台主機 在執行)
// https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/classmysqlsh_1_1dba_1_1_dba.html#a12f040129a2c4c301392dd69611da0c8
dba.createCluster('ClusterName')
// 需要用白名單選項 才能成功加入
dba.createCluster('ClusterName', {ipAllowlist:'192.168.0.0/16,xxx.xxx.xxx.xxx,xxx.xxx.xxx.xxx'})
dba.createCluster('ClusterName', {localAddress:'192.168.0.1:3307', ipAllowlist:'192.168.0.0/16,xxx.xxx.xxx.xxx'})

// 查看集群狀態
var cluster = dba.getCluster('ClusterName')
cluster.status()

dba.getCluster('ClusterName').status()

// 獲取集群結構
var cluster = dba.getCluster('ClusterName')
cluster.describe()

dba.getCluster('ClusterName').describe()

// 添加新節點
// https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/classmysqlsh_1_1dba_1_1_cluster.html#a92471821375405214215958ee924087f
// 出現錯誤則 到錯誤的節點進入mysql 使用 reset master;
var cluster = dba.getCluster('ClusterName')
cluster.addInstance('root@hostname:3307')

dba.getCluster('ClusterName').addInstance('root@hostname:3307')
dba.getCluster('ClusterName').addInstance('root@xxx.xxx.xxx.xxx:5306')

// 重新掃描集群
// https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/classmysqlsh_1_1dba_1_1_cluster.html#a96c63d07c753c4482d60fc6eea9a895f
dba.getCluster('ClusterName').rescan()

// 重新將節點加入
// https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/classmysqlsh_1_1dba_1_1_cluster.html#abd828ca439e5dfe39b95f8670bc82155
var cluster = dba.getCluster('ClusterName')
cluster.rejoinInstance('root@hostname:3307')

dba.getCluster('ClusterName').rejoinInstance('root@hostname:3307')

// 手動切換主節點(5.7.43 沒有)
// https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/classmysqlsh_1_1dba_1_1_replica_set.html#a03aaee3c962d3d3b35feafbc29fbbd2b
var cluster = dba.getCluster('ClusterName')
cluster.setPrimaryInstance('root@hostname:3307');

dba.getCluster('ClusterName').setPrimaryInstance('root@hostname:3307');

// https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/classmysqlsh_1_1dba_1_1_replica_set.html#a60ed4953b1b856c416a4836450680056
dba.getCluster('ClusterName').forcePrimaryInstance('root@hostname:3307');

// 移除節點
// 普通移除
var cluster = dba.getCluster('ClusterName')
cluster.removeInstance('root@hostname:3307')

dba.getCluster('ClusterName').removeInstance('root@hostname:3307')

// 強制移除
var cluster = dba.getCluster('ClusterName')
cluster.removeInstance('root@hostname:3307',{force: true})

dba.getCluster('ClusterName').removeInstance('root@hostname:3307',{force: true})

// 重新啟動集群(當所有成員都處於離線狀態時，使集群恢復在線狀態。)
// https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/classmysqlsh_1_1dba_1_1_dba.html#ac68556e9a8e909423baa47dc3b42aadb
dba.rebootClusterFromCompleteOutage('ClusterName')

// 解散集群 (無法訪問集群成員)
var cluster = dba.getCluster('ClusterName')
cluster.dissolve({force:true})

dba.getCluster('ClusterName').dissolve({force:true})

// 刪除 Metadata schema
// 這個選項會刪除 Metadata schema，然後你可以重新建立一個新的 MySQL InnoDB Cluster。
// 請注意，這會導致 Metadata 中的任何相關資訊都會丟失，包括以前建立的 Cluster 設定。
dba.dropMetadataSchema('ClusterName')

// 查看集群描述
dba.getCluster('ClusterName').describe()

// 切換到多主模式
dba.getCluster('ClusterName').switchToMultiPrimaryMode()

// 切換到單主模式
dba.getCluster('ClusterName').switchToSinglePrimaryMode('root@hostname:3306')

// 查看當前集群的配置屬性
dba.getCluster('ClusterName').options()
// 更改集群設置(用來設置所有節點的參數)
dba.getCluster('ClusterName').setOption('clusterName','newCluster')
// 更改集群實例設置(用來對指定節點配置屬性)
dba.getCluster('ClusterName').setInstanceOption('root@172.27.8.2:3306', 'exitStateAction', 'READ_ONLY')

// Loss of Quorum
// 副本集的許多成員變得無法訪問，以至於它不再擁有多數，它將不再擁有法定人數，並且無法對任何更改做出決定。
// 將其重新配置為僅考慮當前成員ONLINE並忽略所有其他成員。
dba.getCluster('ClusterName').forceQuorumUsingPartitionOf("root@192.168.1.30")

// 檢視路由資訊：listRouters()
dba.getCluster('ClusterName').listRouters()
```

## mysqlbinlog - 檢查主資料庫中的二進制日誌（Binary Log）

```
要檢查主資料庫中的二進制日誌（Binary Log），可以使用 MySQL 的命令行工具 mysqlbinlog。
這個工具可以查看和解析 MySQL 的二進制日誌，以了解其中的更新操作。
```

```bash
# 進入 MySQL 的安裝目錄，並找到二進制日誌的儲存位置。
# 通常，MySQL 的二進制日誌儲存在 datadir 目錄下的 mysql-bin 目錄中。
# 可以使用以下指令查找 datadir 的位置：
mysql -u root -p -e "SHOW VARIABLES LIKE 'datadir';"

# 使用 mysqlbinlog 命令檢視二進制日誌。
# 請將 mysql-bin.000001 替換為錯誤訊息中提供的實際日誌檔案名稱，並將 566933897 替換為結束日誌位置：
mysqlbinlog mysql-bin.000001 --start-position=566933897

# 利用mysqlbinlog工具找出440267874的事件
mysqlbinlog --base64-output=decode-rows -vv mysql-bin.000003 |grep -A 20 '440267874'
mysqlbinlog --base64-output=decode-rows -vv mysql-bin.000003 --stop-position=440267874 | tail -20
mysqlbinlog --base64-output=decode-rows -vv mysql-bin.000003 > decode.log
```

## ndb Cluster 相關

```bash
# 啟動順序 Manager node > Data node > SQL node。
# 如果是第一次啟動 SQL node 請使用 –initial 初始化
# 初始化 MySQL Cluster
ndb_initialize

# 從 Manage node 確認所有 node 狀態
ndb_mgm

# -- NDB Cluster -- Management Client --
# ndb_mgm> show

# Connected to Management Server at: localhost:1186
# Cluster Configuration
# ---------------------
# [ndbd(NDB)]	2 node(s)
# id=3    @172.10.0.143  (mysql-5.6.29 ndb-7.4.11, Nodegroup: 0)
# id=4	@172.10.0.144  (mysql-5.6.29 ndb-7.4.11, Nodegroup: 0, *)

# [ndb_mgmd(MGM)]	1 node(s)
# id=1	@172.10.0.140  (mysql-5.6.29 ndb-7.4.11)

# [mysqld(API)]	2 node(s)
# id=2	@172.10.0.141  (mysql-5.6.29 ndb-7.4.11)
# id=3	@172.10.0.142  (mysql-5.6.29 ndb-7.4.11)


# 啟動管理節點：(在管理節點上執行)
ndb_mgmd --config-file=config.ini
ndb_mgmd -f /path/to/config.ini

ndb_mgmd -v -f /var/lib/mysql-cluster/config.ini
	-v 啟用詳細的日誌輸出（verbose）
	-f 指定配置文件的路徑。 # 包含有關集群的配置信息，例如節點的 IP 地址、端口、數據存儲位置

# 啟動數據節點（Data Node）
ndbd
ndbd --defaults-file=/etc/my.cnf -v
	# 當你第一次啟動 MySQL Cluster 或添加新的數據節點時，可以使用此選項來進行初始化。
	# 如果已經有資料請絕對不要使用 –initial 否則此 node 的資料全毀
	--initial 表示在初始化過程中運行 ndbd。
	-v 表示啟用詳細的日誌輸出（verbose）
	--defaults-file 指定用於配置 ndbd 的配置文件的路徑

# 啟動 SQL node
systemctl start mysqld
```

# SQL 指令

## innodb cluster 相關

```sql
-- mysql 5.7
-- replication_group_members 表是 MySQL InnoDB Cluster 中的一個特殊表，用於儲存集群中的複製組成員（Replication Group Members）的相關信息。
-- 當建立一個 InnoDB Cluster 時，它將包含多個 MySQL 實例，這些實例相互之間進行複製以實現數據的同步和冗余。
-- replication_group_members 表的作用是追蹤這些成員的相關數據。
SELECT * FROM replication_group_members;

-- member_id: 每個集群成員都有一個唯一的 member_id。這是集群內部用於識別成員的唯一標識。
-- member_host: 成員的主機名或 IP 地址。
-- member_port: 成員的端口號。
-- member_state: 成員的狀態，例如 'ONLINE' 表示成員在線，'OFFLINE' 表示成員離線，等等。
-- member_role: 成員在集群中的角色，例如 'PRIMARY' 表示主成員，'SECONDARY' 表示次要成員。
-- member_version: 成員的 MySQL 版本。
-- member_weight: 成員的權重，用於決定成員在集群中的處理負載。
-- member_metadata: 成員的元數據，用於描述成員的額外信息。
-- member_mode: 成員的模式，通常是 'R/W'（讀寫模式）或 'R/O'（只讀模式）。
-- member_status: 成員的狀態，例如 'OK' 表示正常，'ERROR' 表示出現錯誤。
-- member_uuid: 成員的唯一標識符。
-- member_primary_uuid: 主成員的唯一標識符。
-- 通過查詢 replication_group_members 表，可以了解有關 InnoDB Cluster 中成員的詳細信息，包括狀態、角色、版本等。這對於集群的監控、故障排除和管理非常有用。
```

```sql
-- mysql 8.0
-- performance_schema.replication_group_members 是 MySQL 數據庫中的一個 Performance Schema 表，用於記錄和提供關於 MySQL 复制（Replication）集群成員的信息。
-- 用於監控和查詢 MySQL 复制集群成員信息的表。
-- 這個表包含了有關 MySQL 复制集群成員的各種指標和狀態信息，讓你可以更好地監控和管理 MySQL 复制集群的運行狀態。
-- 通過查詢這個表，你可以了解到 MySQL 复制集群中每個成員的運行狀態、角色、主機名等信息，這對於進行故障排除、性能監控和管理操作非常有用。例如，你可以檢查成員是否正常運行、誰是主要成員，以及在需要進行故障切換或重建時可以提供有關成員的詳細信息。
SELECT * FROM performance_schema.replication_group_members;
-- MEMBER_ID: 這是每個 MySQL 复制成員的唯一識別 ID。
-- MEMBER_HOST: 复制成員的主機名或 IP 地址。
-- MEMBER_PORT: 复制成員的 MySQL 連接端口。
-- MEMBER_STATE: 复制成員的狀態，例如 "ONLINE" 表示成員正常運行。
-- MEMBER_ROLE: 复制成員的角色，可能是 "PRIMARY"（主要）或 "SECONDARY"（次要）。
```

# Cluster 叢集架設

## 說明

```
多主架構：真正的多主多活群集，可隨時對任何節點進行讀寫。
同步複製：集群不同節點之間數據同步，某節點崩潰時沒有數據丟失。
數據一致：所有節點保持相同狀態，節點之間無數據分歧。
並行複制：重放支持多線程並行執行以獲得更好的性能。
故障轉移：故障節點本身對集群的影響非常小，某節點出現問題時無需切換操作，因此不需要使用VIP，也不會中斷服務。
自動克隆：新增節點會自動拉取在線節點的數據，最終集群所有節點數據一致，而不需要手動備份恢復。
應用透明：提供透明的客戶端訪問，不需要對應用程序進行更改。

數據分佈和冗餘：通常情況下，你會希望至少有一個 Data Node，以確保數據的冗餘存儲。如果只有一個 Data Node，那麼你的數據將不具備冗餘，一旦該節點出現故障，數據將不可用。

性能平衡：SQL Node 負責處理查詢，而 Data Node 負責存儲數據和執行一些數據操作。如果 SQL Node 非常多，而 Data Node 很少，可能會導致 SQL 查詢的性能問題。相反，如果 Data Node 很多，但 SQL Node 很少，則可能影響查詢性能。

資源分配：每個節點需要一定的計算和存儲資源。確保在部署時平衡資源，避免某些節點過於擁擠，而其他節點資源空閑。
```

### GTID 模式

```
GTID（Global Transaction Identifier）模式是 MySQL 中用於跨多個服務器保證事務一致性的一種方法。GTID 是一種唯一標識事務的方式，它可以確保在分佈式系統中，每個事務都具有全局唯一的標識，無論事務在哪個服務器上執行。

在 GTID 模式下，每個事務都會被賦予一個全局唯一的 ID，這個 ID 包括了事務在特定服務器上的執行信息，例如服務器的 UUID 和事務的序號。這樣，無論事務在哪個服務器上執行，它的 GTID 都是唯一的，這使得在不同服務器上的事務相互之間可以進行溝通和追蹤。

GTID 模式的優點包括：

全局唯一性： 每個事務的 GTID 都是全局唯一的，不受服務器或數據庫更改的影響。
易於故障恢復： GTID 可以用於確定故障發生前已提交的事務，從而更容易進行故障恢復。
跨服務器複製： GTID 可以使服務器之間的數據複製更可靠和一致。
簡化拓撲變更： 在拓撲變更（例如添加或移除主從關係）時，GTID 可以幫助確定在哪個事務上進行切換。
MySQL 提供了幾種不同的 GTID 模式，包括：

GTID_OFF： 不使用 GTID。
GTID_ON： 啟用 GTID，但不強制使用。
GTID_MODE=ON_PERMISSIVE： 啟用 GTID，並允許使用非 GTID 的事務。
GTID_MODE=ON_COMMIT： 啟用 GTID，並要求事務在提交時生成 GTID。
GTID_MODE=ON_PURGE： 啟用 GTID，並自動清理無效的 GTID。
在設置 GTID 模式時，需要確保所有參與的 MySQL 服務器都支持所選的模式，並進行相應的配置。 GTID 可以在 MySQL 的配置文件中進行設置，也可以在運行時使用相應的 SQL 命令進行設置和查詢。詳細的配置和操作可以參考 MySQL 官方文檔。
```

## NDB Cluster 實作

### Linux

#### Manage node (管理節點) - 負責監控叢集所有 Nodes 的狀態，並且由此控制所有 Nodes 的替換。

```bash
# 創建資料夾並編輯配置文檔提供給管理節點使用
mkdir /var/lib/mysql-cluster
vim /var/lib/mysql-cluster/config.ini
```

```ini
[ndbd default]
# NoOfReplicas=2：代表著存在2份一樣的資料在 Data node，Data node允許著1台的故障容錯還有另一份資料可以正常運行
# EX:若 Data node 只有2台，所以設定2，再多沒有意義只是增加 write loading
NoOfReplicas=2             # 數據副本數量
# DataMemory：這個配置項指定了數據節點用於存儲數據的內存量。在 MySQL Cluster 中，數據被分割成多個片段（fragments），每個片段都存儲在數據節點的內存中。這個配置項的值通常需要根據你的數據量和內存容量來進行調整。在這個例子中，數據節點的內存存儲被設定為 1024MB（1GB）。
DataMemory=1024M
# IndexMemory：這個配置項指定了數據節點用於存儲索引的內存量。索引是用於加速數據查詢的重要組件，因此將一部分內存分配給索引存儲可以提高查詢性能。和 DataMemory 一樣，IndexMemory 的值也需要根據你的索引大小和內存容量進行調整。在這個例子中，索引節點的內存存儲被設定為 256MB。
IndexMemory=256M

[ndb_mgmd default]
# DataDir: 指定管理節點的數據存儲目錄。管理節點負責維護集群的元數據（例如配置和拓撲信息）。這個目錄將包含相關的元數據文件。
DataDir=/var/lib/mysql-cluster
# PortNumber: 指定管理節點使用的 TCP 端口號。其他節點（例如 MySQL Server 和 Data Node）將使用這個端口連接到管理節點。
# ArbitrationRank: 指定管理節點的仲裁排名。在配置數據、拓撲或故障檢測時，仲裁節點（Arbitration Node）將根據這個排名來做出決定。
# Timeout: 指定管理節點之間通信的超時時間。如果通信超時，則可能觸發重新選舉。
# 其他管理節點相關的配置項：你可以在 [ndb_mgmd default] 區塊中設定其他與管理節點相關的配置項，例如 NodeId、NodeGroup、HostName 等等。

[mysqld default]
# 設定 MySQL Data Node （NDB Data Node）的默認配置
# datamemory: 指定每個 MySQL Data Node 可用於數據存儲的內存大小。例如：datamemory=1024M 表示每個 Data Node 將使用 1024MB 的內存進行數據存儲。
# indexmemory: 指定每個 MySQL Data Node 可用於索引存儲的內存大小。例如：indexmemory=256M 表示每個 Data Node 將使用 256MB 的內存進行索引存儲。
# 其他 MySQL 相關的配置項：你可以在 [mysqld default] 區塊中設定其他與 MySQL 相關的配置項，例如 maxnoofconcurrentoperations、maxnoofattributes、maxnooftables 等等。

[tcp default]
# portnumber: 指定節點之間通信使用的 TCP 端口號。例如：portnumber=2202 表示使用 2202 端口進行通信。
# bindaddress: 指定要綁定的 IP 地址，即指定節點監聽的 IP 地址。例如：bindaddress=0.0.0.0 表示監聽所有可用 IP 地址。
# sndbuf: 指定 TCP 發送緩衝區大小，用於調整 TCP 發送性能。
# rcvbuf: 指定 TCP 接收緩衝區大小，用於調整 TCP 接收性能。
# 其他 TCP 相關的配置項：你可以在 [tcp default] 區塊中設定其他與 TCP 相關的配置項，例如 maxconnections、maxreplicas、timeout 等等。

[ndb_mgmd]
# 這是管理節點的配置區塊，可以指定管理節點的 HostName 和 NodeId。
HostName=192.168.1.100     # 管理節點 IP 地址
NodeId=1                   # 節點 ID

[ndbd]
HostName=192.168.1.101     # 數據節點 IP 地址
# NodeId：每一個 nodes 都必須擁有獨一無二的 id 值
NodeId=2                   # 節點 ID

[ndbd]
HostName=192.168.1.102     # 數據節點 IP 地址
NodeId=3                   # 節點 ID

[mysqld]
# 這是 SQL 節點（MySQL 服務節點）的配置區塊
HostName=192.168.1.103     # SQL 節點 IP 地址
```

#### Data node - 負責所有 SQL Data 的 Nodes，單純儲存資料，將資料寫在 RAM & Disk。

```ini
# vim /etc/my.cnf
[mysqld]
# enable cluster service
ndbcluster

# with connect manage node
ndb-connectstring=172.10.0.140:1186

# defaulte database engine
default-storage-engine=NDBCLUSTER

# setting character
skip-character-set-client-handshake
character-set-server = utf8
collation-server = utf8_general_ci
init-connect = SET NAMES utf8

[mysql_cluster]
# cluster management node
ndb-connectstring=172.10.0.140:1186
```

#### SQL node (原本的 MySQL Server) - 負責 SQL 的 Table schema 和 Client 連接的空間。

```ini
# vim /etc/my.cnf
[mysqld]
# 將存儲引擎設置為 NDB
default_storage_engine = ndb
# 設置 MySQL Cluster 管理節點 (ndb_mgmd) 的 IP 地址和端口
ndb-connectstring = <mgmd_host>:<mgmd_port>

# 設置網絡接口
bind-address = <interface_ip>
# 指定 MySQL 的數據目錄，這是存放 MySQL 數據文件的位置。
datadir=/var/lib/mysql
# 指定 MySQL 服務器的 socket 文件的位置，用於與服務器進行通信。
socket=/var/lib/mysql/mysql.sock
# 指定 MySQL 服務器的運行用戶
user=mysql
# 禁用符號鏈接
symbolic-links=0
# 設定默認的存儲引擎為 ndbcluster
default_storage_engine=ndbcluster
# 定 MySQL Cluster 管理節點的 IP 地址和端口，以便 MySQL 服務器可以連接到管理節點
ndb-connectstring=172.10.0.140:1186

[mysql_cluster]
# MySQL 服務器在集群中都有一個唯一的節點 ID
ndb_nodeid = <node_id>

[mysqld_safe]
# 指定 MySQL 服務器的錯誤日誌文件的位置
log-error=/var/log/mysqld.log
# 指定 MySQL 服務器的進程 ID 文件的位置
pid-file=/var/run/mysqld/mysqld.pid

[client]
# 指定 MySQL 客戶端的 socket 文件的位置，用於與 MySQL 服務器進行通信。
socket=/usr/local/mysql/data/mysql.sock
```

## InnoDB Cluster 實作

### 說明

```
InnoDB Cluster 架構主要由以下三個主要組件組成：

Primary Component（管理節點）：
Primary Component 是 InnoDB Cluster 中的主節點，負責處理寫入事務和協調集群內部的操作。
僅有一個 Primary Component（管理節點），用於確保寫入操作的一致性和高可用性。
可以使用 MySQL Shell 來管理管理節點，進行集群操作。

Secondary Component（一般節點）：
Secondary Component 是 InnoDB Cluster 中的一般節點，主要用於處理讀取查詢。
可以有多個 Secondary Component，這些節點根據需要進行擴展，提供更好的讀取能力。
所有 Secondary Component 之間的數據同步通過 Group Replication 技術實現。

Group Replication：
Group Replication 是 MySQL 的一個內建插件，用於實現多主從同步複製，確保數據的一致性和高可用性。
Group Replication 使用基於 Paxos 協議的分佈式一致性機制，確保所有節點間的數據同步。
Group Replication 還提供了故障檢測和自動故障轉移等功能，以實現高可用性和無單點故障。
簡單來說，InnoDB Cluster 通過將一個管理節點（Primary Component）和多個一般節點（Secondary Component）組合成一個高可用性集群，
並通過 Group Replication 技術實現數據同步和自動故障恢復。
這種架構可以確保在任何一個節點故障時，其他節點能夠繼續提供服務，從而實現高可用性和容錯能力。



在 InnoDB Cluster 中，讀取操作通常是通過連接到管理節點（Primary Component）進行的。
管理節點負責處理寫入事務和協調集群內部的操作，並確保寫入操作的一致性。
同時，管理節點也可以處理讀取查詢，並將讀取請求轉發到適當的一般節點（Secondary Component）上。

在 InnoDB Cluster 中，當應用程序或用戶發出讀取請求時，通常會連接到管理節點。
管理節點可以使用其內部的路由機制，將讀取請求分發到可用的一般節點，以實現負載均衡和提供高效的讀取能力。
這使得 InnoDB Cluster 能夠根據每個節點的當前狀態和可用性來決定將讀取請求路由到哪個一般節點。

需要注意的是，讀取操作的路由和分發是由管理節點自動處理的，應用程序和用戶不需要直接指定要連接到哪個一般節點。
這樣的架構確保了在 InnoDB Cluster 中實現高效的讀取和負載均衡，同時確保數據的一致性和高可用性。

當原本的「管理節點」發生故障或不可用時，你仍然可以連線到它以便連接到新的管理節點。
在 InnoDB Cluster 中，每個節點都知道整個集群的狀態和拓撲，包括哪個節點是當前的「管理節點」。
這使得當「管理節點」發生故障並更換時，你可以透過其他節點知道新的「管理節點」的位置。

在連接到故障的「管理節點」之後，你可以進行相關操作，例如查看集群的狀態、重新配置集群、或者執行故障轉移操作以選擇新的「管理節點」。
這有助於確保即使在故障發生時，你仍然能夠維護和管理整個 InnoDB Cluster。



MySQL InnoDB Cluster 在 MySQL Community Edition 和 MySQL Enterprise Edition 之間的主要功能差異可能會因版本和更新而有所變化。
以下是一些可能的差異，但請注意這只是一個大致的指南，實際差異可能因具體版本而異：

高可用性和故障恢復功能：InnoDB Cluster 提供了自動故障檢測、自動故障轉移和自動故障恢復的功能，這些功能可以在 Community Edition 中使用。
然而，某些進階的高可用性功能（如自動平衡和自動伸縮）可能只在 Enterprise Edition 中提供。

管理和監控工具：MySQL Enterprise Edition 通常提供更強大的管理和監控工具，
例如 MySQL Enterprise Monitor 和 MySQL Enterprise Backup，這些工具可能不包含在 Community Edition 中。

安全功能：Enterprise Edition 可能提供更多的安全功能，如進階的安全插件和功能，以幫助保護數據和系統。

支援和服務：購買 MySQL Enterprise Edition 可能會包括 MySQL 的商業支援，這包括技術支援、更新和补丁、安全性修復等。
```

### MySQL 5.7 設定檔

```conf
# 啟用二進制日誌
log-bin = mysql-bin
# 設定伺服器 ID
server-id = <unique_id>

# 將 binlog checksum 設置為 NONE
# Binlog checksum 是 MySQL 中的一種安全機制，用於檢測二進制日誌（binlog）中的數據完整性。
# 它通過在 binlog 中添加一個校驗和（checksum）來確保日誌中的數據未被損壞或更改。
# 這有助於提高數據庫的可靠性和安全性，防止因日誌數據損壞而導致的數據不一致性或重要信息的丟失。
# NONE（默認選項）：表示不使用校驗和，二進制日誌中不包含校驗和信息。這意味著數據完整性不會得到額外的檢查。
# CRC32：使用 CRC32 算法計算校驗和，並將其附加到每條二進制日誌記錄中。CRC32 是循環冗餘校驗的一種算法，用於檢測數據錯誤。
# NEW_CRC：這是 MySQL 5.6.2 版本引入的新算法，與 CRC32 不同。NEW_CRC 也是一種循環冗餘校驗算法，但它更加強大，可以檢測到更多類型的數據錯誤。
binlog_checksum=NONE

# 啟用 GTID (Global Transaction ID)
# 啟用 GTID 一致性強制
enforce_gtid_consistency=ON
# 啟用 GTID 模式
gtid_mode=ON
# 啟用日誌從伺服器更新
log_slave_updates=ON
# 將主伺服器資訊存儲庫設置為 TABLE
master_info_repository=TABLE
# 將中繼日誌資訊存儲庫設置為 TABLE
relay_log_info_repository=TABLE
# 將事務寫集提取設置為 XXHASH64
transaction_write_set_extraction=XXHASH64
# 配置 MySQL 服務器監聽的 IP 和端口
bind-address=0.0.0.0
# port=3306

## Group Replication 非必要(可用 mysql shell 完成)
# Group Replication（群組複製）是 MySQL 數據庫中的一個高可用性和容錯容忍性解決方案，通過在多個 MySQL 實例之間複製和同步數據，實現數據的自動備份和故障轉移。
# 它是 MySQL InnoDB Cluster 技術的核心組件之一，可以用於構建具有高可用性和自動故障恢復功能的數據庫集群。

# 多主複製：Group Replication 允許在集群中的多個 MySQL 實例之間進行多主複製，每個實例都可以讀取和寫入數據。
# 同步複製：Group Replication 保證數據在集群中的所有實例之間的同步，確保每個實例上的數據都是一致的。
# 自動故障轉移：當主實例失敗時，Group Replication 可自動選擇一個可用的實例作為新的主實例，以實現自動故障轉移，並保證系統的持續運行。
# 動態成員管理：新的實例可以動態地加入或退出集群，Group Replication 會自動適應集群拓撲的變化，確保數據同步和高可用性。
# 一致性和耐久性：Group Replication 確保所有實例上的數據一致，並提供耐久性，確保數據在實例崩潰後不會丟失。
# 數據路由：Group Replication 提供了自動的數據路由機制，用戶可以通過連接到任何集群成員來進行讀寫操作，系統會自動將數據路由到正確的實例上。
# 加密和安全性：Group Replication 支持 SSL 加密，確保在數據傳輸過程中的安全性。
# 擴展性：Group Replication 允許在集群中添加更多的實例，以擴展系統的讀取能力。

# 設定 Group Replication 插件
plugin-load = "group_replication.so"
# 設定管理節點模式
group_replication_single_primary_mode = ON
# 啟用 Group Replication
group_replication = FORCE_PLUS_PERMANENT
# 設定 Group Replication 插件
group_replication_group_name = "myCluster"  # Group Replication 集群的名稱
# 指定 Group Replication 本地通信地址
group_replication_local_address = "hostname:port"  # 本地節點的主機名和端口
# 設定 Group Replication 群組種子節點
group_replication_group_seeds = "seed1:port,seed2:port,..."  # 初始節點的主機名和端口列表

# 設定 Group Replication 自動增加主機名標識
loose-group_replication_bootstrap_group = OFF  # 是否允許自動啟動新的 Group Replication 群組
loose-group_replication_start_on_boot = ON    # 是否在啟動時自動啟動 Group Replication

# 設定 SSL 加密（可選）
require_secure_transport = ON  # 是否要求使用 SSL 安全傳輸
loose-group_replication_ssl_mode = REQUIRED  # Group Replication 使用的 SSL 模式
loose-group_replication_recovery_use_ssl = 1  # 是否在恢復過程中使用 SSL

# 設定自動遞增的鎖模式(使用2)
# 設定 InnoDB 存儲引擎的自動遞增鎖模式
# innodb_autoinc_lock_mode 參數控制了在使用 AUTO_INCREMENT 列時的鎖定行為。

# traditional（預設值）：
# 鎖定整個表，以確保同一時刻只有一個事務可以插入行。可能導致鎖定競爭和效能下降。
innodb_autoinc_lock_mode = 0
# consecutive：
# 只鎖定自動遞增列的鎖定索引，允許不同的事務同時插入行，只要使用不同的 AUTO_INCREMENT 值。
# 可以減少鎖定競爭，特別是對於並發插入操作。
innodb_autoinc_lock_mode = 1
# interleaved：
# 鎖定 AUTO_INCREMENT 值的某個範圍，讓插入操作在自動遞增列上交叉執行，減少鎖定競爭。
innodb_autoinc_lock_mode = 2
```

### MySQL 5.7 SQL指令

```sql
-- 每台節點

-- 停止將 SQL 語句寫入二進制日誌
SET SQL_LOG_BIN=0;
-- 創建用於複製的用戶
-- CREATE USER rpl_user@'%': 這部分指定了要創建的使用者名稱和允許連接的主機或 IP 地址。
-- rpl_user 是使用者名稱，'%' 表示允許從任何主機或 IP 地址連接。
-- IDENTIFIED WITH 'mysql_native_password': 這是指定該使用者的身份驗證方法。在這種情況下，使用的是 MySQL 本地密碼（'mysql_native_password'）。
-- 這是一種常見的身份驗證方法，用於使用使用者名稱和密碼進行身份驗證。
-- BY 'password': 這是指定使用者的密碼。在這個示例中，密碼是 'password'。
CREATE USER rpl_user@'%' IDENTIFIED WITH 'mysql_native_password' BY 'password';
-- 為複製用戶授予 REPLICATION SLAVE 權限
GRANT REPLICATION SLAVE ON *.* TO rpl_user@'%';

-- 創建具有超級權限的用戶（可選）
CREATE USER root@'%' IDENTIFIED BY 'root';
-- 為超級用戶授予所有權限，包括授予權限的能力（可選）
GRANT ALL ON *.* TO root@'%' WITH GRANT OPTION;

-- 創建給innodb使用的帳號 每個都建立
CREATE USER 'innodbAdmin'@'%' identified by 'dWUYSCWpyx';
GRANT ALL PRIVILEGES ON *.* TO  'innodbAdmin'@'%' WITH GRANT OPTION;


-- 刷新權限
FLUSH PRIVILEGES;
-- 恢復將 SQL 語句寫入二進制日誌
SET SQL_LOG_BIN=1;

-- 安裝 Group Replication 插件
INSTALL PLUGIN group_replication SONAME 'group_replication.so';
-- 查看已安裝的插件
SHOW PLUGINS;
-- 設置複製主機的使用者名稱和密碼
CHANGE MASTER TO MASTER_USER='rpl_user', MASTER_PASSWORD='password' FOR CHANNEL 'group_replication_recovery';
-- 刷新權限
FLUSH PRIVILEGES;
```

### MySQL 5.7 mysqlsh指令

```JavaScript
// 使用 MySQL Shell

// 檢查實例配置，此處根據報錯修改配置文件，修改後需要重啟 MySQL
dba.checkInstanceConfiguration('root@manager_node:3306')
dba.checkInstanceConfiguration('root@node_1:3306')
dba.checkInstanceConfiguration('root@node_2:3306')
dba.checkInstanceConfiguration('root@node_3:3306')

// 分別登陸到每個節點的主機，再登陸 mysql-shell 進行持久化操作
shell.connect('root@localhost:3306')

shell.connect('root@manager_node:3306')
shell.connect('root@node_1:3306')
shell.connect('root@node_2:3306')
shell.connect('root@node_3:3306')
// 自動設置 Group Replication： 如果當前實例還沒有啟用 Group Replication，函數將自動執行必要的步驟來啟用 Group Replication。
// 自動加入 InnoDB Cluster： 如果當前實例是 InnoDB Cluster 的一部分，函數將自動將實例加入到 InnoDB Cluster 中。
// 配置和驗證參數： 函數將根據配置文件和集群設置來配置和驗證實例的參數，以確保其與其他實例保持一致。
// 持久化到配置⽂件中 (此功能僅適用於本地實例)
// 這個功能只能在本地的 MySQL 實例上使用，無法用於遠程的 MySQL 實例。
// 在這個上下文中， "本地實例" 指的是執行 MySQL Shell 的計算機上的 MySQL 伺服器。
dba.configureLocalInstance()
// 根據下面提示輸入 my.cnf 到完整路徑
// Please specify the path to the MySQL configuration file: /etc/my.cnf

// 範例 在 manager_node 建立集群
shell.connect('root@localhost:3306')

// 創建集群 名稱:ClusterName
dba.createCluster('ClusterName')

// 加入集群
var cluster = dba.getCluster('ClusterName')
cluster.addInstance('rpl_user@node_1:3306')
cluster.addInstance('rpl_user@node_2:3306')
cluster.addInstance('rpl_user@node_3:3306')

// 查看集群狀態
// 方法一：
var cluster = dba.getCluster('ClusterName')
cluster.status()

// 方法二：
dba.getCluster('ClusterName').status()
```

### MySQL 5.7 router 設定檔

```conf
# [4.1 Configuration File Syntax - 配置文件語法](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-syntax.html)
# [4.3.3 Configuration File Options - 配置文檔選項](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-conf-options.html)
# [4.3.4 Configuration File Example - 配置文檔範例](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-example.html)

[DEFAULT]
# 設定日誌文件的保存目錄
logging_folder = /home/cluster/mysql-router-2.1.6/logs
# 設定插件文件的目錄
plugin_folder = /home/cluster/mysql-router-2.1.6/lib/mysqlrouter
# 設定配置文件的目錄
config_folder = /home/cluster/mysql-router-2.1.6
# 設定運行時文件的目錄
runtime_folder = /home/cluster/mysql-router-2.1.6/run

[logger]
# 設定日誌記錄的級別
level = INFO

[routing:master]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address = 0.0.0.0
# 綁定的監聽端口
bind_port = 23306
# 設定模式，這裡設定為 read-write，表示這個路由器實例可以處理讀和寫的查詢
mode = read-write
# 設定目標資料庫實例的地址和端口，這裡設定兩個目標實例
destinations = manager_node:3306

[routing:secondary]
# 綁定的 IP 地址，0.0.0.0 表示所有可用的 IP 地址
bind_address = 0.0.0.0
# 綁定的監聽端口
bind_port = 23307
# 設定最大連接數
max_connections = 1024
# 設定模式，這裡設定為 read-only，表示這個路由器實例只處理讀取查詢
mode = read-only
# 設定目標資料庫實例的地址和端口
destinations = node_1:3306,node_2:3306,node_3:3306

[keepalive]
# 設定心跳檢測的間隔時間，這裡設定為 60 秒
# 在設定的間隔時間內，MySQL Router 會定期向後端數據庫服務器發送心跳消息，以確保連接的有效性。
interval = 60
```

### MySQL 5.7 router 指令

```bash
# 初始化mysql-router PRIMARY節點即可
mysqlrouter --bootstrap root@manager_node:3306 --user=root
    # Please enter MySQL password for root:

    # Bootstrapping system MySQL Router instance...
    # Checking for old Router accounts
    # Creating account mysql_router1_klh7m3xmmru0@'%'
    # MySQL Router  has now been configured for the InnoDB cluster 'fandercluster'.

    # The following connection information can be used to connect to the cluster after MySQL Router has been started with generated configuration..

    # Classic MySQL protocol connections to cluster 'fandercluster':
    # - Read/Write Connections: localhost:6446
    # - Read/Only Connections: localhost:6447
    # X protocol connections to cluster 'fandercluster':
    # - Read/Write Connections: localhost:64460
    # - Read/Only Connections: localhost:64470

# 初始化後mysql-router自動生成一個默認的配置文件 /usr/local/mysqlrouter/mysqlrouter.conf

# 使用指定的配置文件來啟動啟動 MySQL Router
mysqlrouter -c /path/to/router.conf
```

### 初始化 Group Replication (全部成員掛掉,但設定還在)

```
MySQL 實例正在使用 Group Replication，但目前它還未指定主要成員（primary member）。

在正常的 Group Replication 部署中，一個成員會被選為主要成員，其他成員將與之同步。
group_replication_primary_member 變數是空的，這可能表示 Group Replication 尚未完全初始化或者存在一些配置問題。
```

`檢查 Group Replication 的狀態`

```sql
SHOW GLOBAL STATUS LIKE 'group_replication%';
```

`初始化 Group Replication`

```sql
SET GLOBAL group_replication_bootstrap_group = ON;
SET GLOBAL group_replication_bootstrap_group = OFF;
```

`檢查其他 Group Replication 變數`

```sql
SHOW VARIABLES LIKE 'group_replication%';
```


##### 優化配置

```bash
# 調整系統參數以優化 MySQL 數據庫性能和穩定性
# 添加內容到 /etc/sysctl.conf 檔案中，並執行 sysctl -p 以應用新的系統參數設定
cat >> /etc/sysctl.conf <<EOF
# 最大異步輸入輸出的佇列長度
fs.aio-max-nr = 1048576
# 最大文件句柄數
fs.file-max = 681574400
# 最大共享內存大小
kernel.shmmax = 137438953472
# 最大共享內存區段數
kernel.shmmni = 4096
# IPC 資源信號量設定
kernel.sem = 250 32000 100 200
# 本地端口範圍
net.ipv4.ip_local_port_range = 9000 65000
# 網絡接收緩衝區大小
net.core.rmem_default = 262144
# 最大網絡接收緩衝區大小
net.core.rmem_max = 4194304
# 網絡發送緩衝區大小
net.core.wmem_default = 262144
# 最大網絡發送緩衝區大小
net.core.wmem_max = 1048586
EOF

# 應用新的系統參數設定
sysctl -p

# 最大文件句柄數（File Descriptor Limit）是指操作系統允許進程或應用程式同時打開的文件（包括檔案、套接字等）的最大數量。
# 每個打開的文件都會對應到一個文件句柄，用於標識和訪問該文件。
# 這個限制影響著系統能夠同時處理多少個文件操作，包括讀取、寫入、網絡通信等。

# 如果最大文件句柄數設定得太小，系統可能會出現文件操作被阻塞的情況，尤其是在高流量的網絡應用中。
# 通常，數據庫伺服器、網頁伺服器和其他高併發應用程式都需要較高的最大文件句柄數，以確保能夠處理大量的同時連接和文件操作。

# 透過調整系統的最大文件句柄數，可以增加系統處理高併發情況下的能力，從而提升應用程式的性能和穩定性。
# 然而，需要注意的是，過高的最大文件句柄數也可能導致系統資源浪費，因此需要根據實際情況進行調整。

# 設定用戶 mysql 的資源限制
cat >> /etc/security/limits.conf <<EOF
# 最大進程數
mysql soft nproc 65536
# 最大進程數
mysql hard nproc 65536
# 最大文件句柄數
mysql soft nofile 65536
# 最大文件句柄數
mysql hard nofile 65536
EOF

# 在登入時應用資源限制
cat >> /etc/pam.d/login <<EOF
session required /lib/security/pam_limits.so
session required pam_limits.so
EOF

# 設定用戶 mysql 登入時的資源限制
cat >> /etc/profile<<EOF
if [ \$USER = "mysql" ]; then
# 最大進程數和文件句柄數
ulimit -u 16384 -n 65536
fi
EOF

# 使新的資源限制立即生效
source /etc/profile
```

```conf
# 這些設定通常在系統的 /etc/sysctl.conf 或 /etc/sysctl.d/ 目錄下進行配置
# 針對 Linux 作業系統的內核參數進行的調整，用來優化系統性能、提升網絡連接效率、以及管理系統的資源

# 設定用戶端口範圍，用於分配給客戶端的臨時端口
net.ipv4.ip_local_port_range = 1024 65535
# 設定 SYN 佇列的長度，這是等待三次握手完成的連線請求的佇列大小
net.ipv4.tcp_max_syn_backlog = 65535
# 設定系統最大檔案句柄的數量，這是同時打開的檔案和目錄的最大數量
fs.file-max = 655350
# 啟用 SYN cookies 來處理 SYN 等待佇列溢出的情況，這可以防止少量的 SYN 攻擊
net.ipv4.tcp_syncookies = 1
# 開啟 TCP 連接中 TIME-WAIT 狀態的快速回收，這可以減少 TIME-WAIT 狀態的持續時間
net.ipv4.tcp_tw_recycle = 1
# 設定每個連線的最大長度，這影響同時處理的連線數量
net.core.somaxconn = 65535
# 設定網絡接收佇列的最大長度，這是等待處理的網絡封包的佇列大小
net.core.netdev_max_backlog = 65535
# 控制 TCP 連線結束時等待的時間，這是確保結束的連線不會持續佔用資源
net.ipv4.tcp_fin_timeout = 60
# 設定單個共享記憶體段的最大值，這是共享內存的大小限制
kernel.shmmax = 4294967285
# 設定 Linux 系統的交換分區使用方式，0 表示儘可能不使用交換分區
vm.swappiness = 0
```

### 使用程式腳本建立 InnoDB Cluster

#### JavaScript

```JavaScript
// https://www.percona.com/blog/setting-up-an-innodb-cluster-with-a-few-lines-of-code/
print('InnoDB cluster set up\n');
print('==================================\n');
print('Setting up a Percona Server for MySQL - InnoDB cluster.\n\n');

var dbPass = shell.prompt('Password for the MySQL root account: ', { type: "password" });
var numNodes = shell.prompt('Number of data nodes: ');
var dbHosts = [];

for (let i = 1; i <= numNodes; i++) {
    var hostName = shell.prompt('Hostname for node' + i + ': ');
    dbHosts.push(hostName);
}

function sleep(milliseconds) {
    const date = Date.now();
    let currentDate = null;
    do {
        currentDate = Date.now();
    } while (currentDate - date < milliseconds);
}

print('\nNumber of Hosts: ' + dbHosts.length + '\n');
print('\nList of hosts:\n');
for (let s = 0; s < dbHosts.length; s++) {
    print('Host: ' + dbHosts[s] + '\n');
}

function setupCluster() {
    print('\nConfiguring the instances.');
    for (let n = 0; n < dbHosts.length; n++) { print('\n=> ');
        dba.configureInstance('root@' + dbHosts[n] + ':3306', { clusterAdmin: "", clusterAdminPassword: '', password: dbPass, interactive: false, restart: true });
    }
    print('\nConfiguring Instances completed.\n\n');

    sleep(5000); // source: https://www.sitepoint.com/delay-sleep-pause-wait/

    print('Setting up InnoDB Cluster.\n\n');
    shell.connect({ user: 'root', password: dbPass, host: dbHosts[0], port: 3306 });

    var cluster = dba.createCluster("InnoDBCluster");

    print('Adding instances to the cluster.\n');
    for (let x = 1; x < dbHosts.length; x++) { print('\n=> ');
        cluster.addInstance('root@' + dbHosts[x] + ':3306', { password: dbPass, recoveryMethod: 'clone' });
    }
    print('\nInstances successfully added to the cluster.\n');
}

try {
    setupCluster();

    print('\nInnoDB cluster deployed successfully.\n');
} catch (e) {
    print('\nThe InnoDB cluster could not be created.\n');
    print(e + '\n');
}
```

#### Python

```Python
import time

print('InnoDB cluster set up\n')
print('==================================\n')
print('Setting up a Percona Server for MySQL - InnoDB cluster.\n\n')

dbPass = input('Password for the MySQL root account: ')
numNodes = int(input('Number of data nodes: '))
dbHosts = []

for i in range(1, numNodes + 1):
    hostName = input('Hostname for node' + str(i) + ': ')
    dbHosts.append(hostName)

def sleep(milliseconds):
    date = time.time()
    currentDate = None
    while currentDate is None or (currentDate - date) < milliseconds / 1000:
        currentDate = time.time()

print('\nNumber of Hosts: ' + str(len(dbHosts)) + '\n')
print('\nList of hosts:\n')
for host in dbHosts:
    print('Host: ' + host + '\n')

def setupCluster():
    print('\nConfiguring the instances.')
    for host in dbHosts:
        print('\n=> ')
        # Replace the following line with your actual configuration logic
        # dba.configureInstance('root@' + host + ':3306', { clusterAdmin: "", clusterAdminPassword: '', password: dbPass, interactive: False, restart: True })

    print('\nConfiguring Instances completed.\n\n')
    sleep(5)  # Sleep for 5 seconds

    print('Setting up InnoDB Cluster.\n\n')
    # Replace the following line with your actual connection and cluster setup logic
    # shell.connect(user='root', password=dbPass, host=dbHosts[0], port=3306)
    # cluster = dba.createCluster("InnoDBCluster")

    print('Adding instances to the cluster.\n')
    for x in range(1, len(dbHosts)):
        print('\n=> ')
        # Replace the following line with your actual instance addition logic
        # cluster.addInstance('root@' + dbHosts[x] + ':3306', { password: dbPass, recoveryMethod: 'clone' })

    print('\nInstances successfully added to the cluster.\n')

try:
    setupCluster()
    print('\nInnoDB cluster deployed successfully.\n')
except Exception as e:
    print('\nThe InnoDB cluster could not be created.\n')
    print(str(e) + '\n')
```

# 重大備份

執行數據庫操作之前進行備份是一個良好的實踐，以確保在出現問題時能夠復原數據。要進行備份，你應該包括以下內容：

- 數據庫： 對你要進行操作的數據庫進行備份。
  使用 mysqldump、MySQL Shell、MySQL Workbench 或其他備份工具，創建數據庫的備份文件（通常是 .sql 文件）。

```bash
mysqldump -u username -p dbname > backup.sql
```

- 配置文件： 對 MySQL 配置文件（通常是 my.cnf 或 my.ini）進行備份。
  這對於你更改了配置，並希望返回原始配置時很有用。

- 日誌文件： 如果你對 MySQL 的二進制日誌（binary logs）進行了配置，你可能希望對這些日誌進行備份。
  這些日誌記錄了數據庫的更改，可用於數據庫的還原。

```bash
cp /path/to/mysql/data/mysql-bin.* /path/to/backup/
```
其他設定文件： 如果你進行了其他系統配置，例如 MySQL Socket 文件位置、SSL 憑證等，也應該對這些文件進行備份。

確保備份文件存儲在一個安全的位置，最好是離數據庫伺服器足夠遠的地方。使用日期或描述性的標籤命名備份文件，以便在需要時能夠方便地識別和還原。

# 例外狀況

## has the following errant GTIDs that do not exist in the cluster

```
innodb cluster 遇到狀況

node_1:3306 具有以下不正常的 GTID，這些 GTID 在集群中不存在：
120c119a-3cc8-11ee-af4c-0242ac130004:1-5
```

```JavaScript
// https://stackoverflow.com/questions/62758154/mysql-server-5-7-cant-add-new-cluster-instance-with-mysql-shell-cluster-addi

// 通過清空表解決了這個問題：mysql.gtid_execulated
// https://dev.mysql.com/doc/refman/5.7/en/replication-administration-skip.html
// 手動丟棄額外的 GTID 事件(無效)
cluster = dba.getCluster()
cluster.setRecoveryMethod("discard")
cluster.addInstance("username@hostname:port")

// 通過克隆方法完全覆蓋狀態(MySQL 8.0以上)
cluster = dba.getCluster()
cluster.setRecoveryMethod("clone")
cluster.addInstance("username@hostname:port")

cluster.addInstance('root@node_1:3306', { recoveryMethod: 'clone' });
```

```sql
-- RESET MASTER; 是用來清除 MySQL 主資料庫的二進制日誌（binary logs）的指令。這個操作應該要謹慎執行，因為它會刪除所有的二進制日誌文件，並且可能會導致數據丟失。

-- 以下是在執行 RESET MASTER; 指令時需要注意的幾點：

-- 數據丟失： RESET MASTER; 會刪除所有的二進制日誌，這可能導致您無法進行數據恢復。請確保您有備份數據的方法，以免丟失重要的數據。

-- 從後備複製： 如果您的數據庫設置了從站（slave）來進行數據複製，那麼在執行 RESET MASTER; 後，從站可能無法再進行更新。您需要重新配置從站以便它能夠從新的主資料庫中獲取數據。

-- 二進制日誌： 在執行 RESET MASTER; 後，系統會創建一個新的二進制日誌文件，並開始記錄新的變更。已經存在的二進制日誌文件會被刪除。

-- 權限檢查： 執行 RESET MASTER; 需要足夠的權限，通常需要超級用戶權限（如 SUPER 權限）。請確保您具有足夠的權限才能執行該操作。

-- 影響其他操作： 在執行 RESET MASTER; 時，您可能需要考慮到正在執行的其他操作，例如備份、數據複製等。該操作可能會中斷這些操作，因此請確保您選擇了適當的時間來執行它。

-- 總之，RESET MASTER; 是一個強大且有潛在風險的指令，您應該在執行之前仔細考慮它可能對您的數據庫和應用程序造成的影響。最好的做法是在執行之前進行備份，以防止意外情況的發生。

-- 到出現錯誤的主機 node_1:3306
RESET MASTER;
```

## 修復損壞的innodb：innodb_force_recovery

```bash
# CentOS 7
cat /var/log/mysqld.log

查看錯誤日誌：
InnoDB: Error: could not open single-table tablespace file ./data_dep/report.ibd

innodb引擎出了問題
```

## 升級為GTID模式

加入設定到下列(其中一個)設定檔
/etc/my.cnf
/etc/mysql/my.cnf

```conf
gtid_mode = on
enforce_gtid_consistency = on
```

## ERROR 3009 (HY000): Column count of mysql.user is wrong. Expected 45, found 43. Created with MySQL 50739, now running 50742. Please use mysql_upgrade to fix this error.

```
```

## rebootClusterFromCompleteOutage 發生錯誤

```
Dba.rebootClusterFromCompleteOutage: Unable to get an InnoDB cluster handle. The instance 'node_1:3306' may belong to a different cluster from the one registered in the Metadata since the value of 'group_replication_group_name' does not match the one registered in the Metadata: possible split-brain scenario. Please retry while connected to another member of the cluster. (RuntimeError)
```

```sql
-- https://ost.51cto.com/posts/16247

-- 获取正确的 group_replication_group_name。实例重启完成后，
-- 读取 mysql_innodb_cluster_metadata.clusters 这个元数据表，获取正确的 group name。
select attributes->'$.group_replication_group_name' from mysql_innodb_cluster_metadata.clusters;
+----------------------------------------------+
| attributes->'$.group_replication_group_name' |
+----------------------------------------------+
| "bc664a9b-9b5b-11ec-8a73-525400c5601a"       |
+----------------------------------------------+

-- 在每个节点上手动修改 group_replication_group_name 。
set global group_replication_group_name = "bc664a9b-9b5b-11ec-8a73-525400c5601a";
set global group_replication_group_name = "";

-- 再次执行 dba.rebootClusterFromCompleteOutage() 就行了。
dba.rebootClusterFromCompleteOutage()
```

### 表已滿 無法寫入

```
Error in applier for group_replication_recovery: Could not execute Write_rows event on table db_name.table_name; The table 'table_name' is full, Error_code: 1114

這個錯誤表示在MySQL複製中發生了問題，具體而言是因為 'table_name' 表已滿，無法執行 Write_rows 事件。
```

```sql
-- https://stackoverflow.com/questions/730579/1114-hy000-the-table-is-full
```

檢查表空間： 查詢可以查看表的大小和剩餘空間

```sql
-- 查詢可以查看表的大小和剩餘空間：
SELECT table_name, engine, table_rows, data_length, index_length, data_free
FROM information_schema.tables
WHERE table_schema = 'db_name' AND table_name = 'table_name';
```

優化表： 執行優化表的操作可能有助於釋放空間並提高表的性能。

```sql
OPTIMIZE TABLE db_name.table_name;
```

調整配置： 檢查MySQL伺服器的配置，特別是 innodb_buffer_pool_size 和 innodb_log_file_size 這樣的配置項。

這些配置項可能需要根據表的大小和伺服器性能進行調整。

清理不必要的數據： 如果表包含歷史或不必要的數據，考慮清理或歸檔這些數據，以減小表的大小。

監控磁盤空間： 確保MySQL伺服器運行的磁盤有足夠的空間。

如果磁盤空間不足，可能導致表無法寫入。

考慮分區： 如果表中包含大量數據，可以考慮使用分區來分割數據，以便更有效地管理表的大小。

## 重啟後 使用 mysqlsh 創建 cluster 失敗

```
Dba.createCluster: dba.createCluster: Unable to create cluster. The instance '192.168.154.112:1106' has a populated Metadata schema and belongs to that Metadata. Use either dba.dropMetadataSchema() to drop the schema, or dba.rebootClusterFromCompleteOutage() to reboot the cluster from complete outage. (RuntimeError)
```

`刪除現有的 Metadata 模式`

```sql
dba.dropMetadataSchema();
```

`重新啟動集群`

```sql
dba.rebootClusterFromCompleteOutage();
```

## 所有成員都重新啟動過

```
MySQL 實例正在使用 Group Replication，但目前它還未指定主要成員（primary member）。

在正常的 Group Replication 部署中，一個成員會被選為主要成員，其他成員將與之同步。
group_replication_primary_member 變數是空的，這可能表示 Group Replication 尚未完全初始化或者存在一些配置問題。
```

`初始化 Group Replication步驟`

`檢查 Group Replication 的狀態`

```sql
SHOW GLOBAL STATUS LIKE 'group_replication%';
```

`初始化 Group Replication`

```sql
SET GLOBAL group_replication_bootstrap_group = ON;
SET GLOBAL group_replication_bootstrap_group = OFF;
```

`啟動 Group Replication`

```sql
START GROUP_REPLICATION;
```

`檢查其他 Group Replication 變數`

```sql
SHOW VARIABLES LIKE 'group_replication%';
```

```sql
SHOW GLOBAL STATUS LIKE 'group_replication%';
```
