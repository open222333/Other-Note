# MySQL 工具 ProxySQL(高性能 高可用性的 MySQL 代理)

```
ProxySQL 是一個高性能、高可用性的 MySQL 代理，它可以在應用程序和 MySQL 之間充當中間層，提供了許多有用的功能，包括負載均衡、故障轉移、查詢攔截和重寫等。

以下是 ProxySQL 的一些主要特點和功能：

負載均衡： ProxySQL 可以分發流量到多個 MySQL 伺服器，實現負載均衡，確保每個伺服器的資源充分利用。
故障轉移： 在某個 MySQL 伺服器失效時，ProxySQL 能夠自動將流量轉發到其他正常運行的伺服器，實現故障轉移，提高系統的可用性。
查詢攔截和重寫： ProxySQL 允許你攔截和修改發送到 MySQL 的查詢，這使得你可以實現複雜的查詢重寫和監控功能。
連接池： ProxySQL 具有內建的連接池管理，可以有效地處理大量連接，減輕 MySQL 伺服器的壓力。
事務分發： 支援將事務分發到不同的 MySQL 伺服器，這有助於提高整體性能和數據庫的擴展性。
統計和監控： ProxySQL 提供了詳細的統計信息和監控功能，讓你可以實時追蹤查詢性能和系統狀態。
SSL 支援： ProxySQL 支援加密連接，可以通過 SSL/TLS 保護數據在傳輸過程中的安全性。
多機房部署： ProxySQL 具有多機房支援，使得你可以在分佈式環境中部署並協調多個 ProxySQL 實例。
自動配置： ProxySQL 允許通過 MySQL 配置文件和 API 來自動配置，簡化管理和擴展。
```

```
如果已存在"proxysql.db"檔案(在/var/lib/proxysql目錄下)，則ProxySQL服務只有在第一次啟動時才會去讀取proxysql.cnf檔並解析;後面啟動會就不會讀 取proxysql.cnf檔了!

如果想要讓proxysql.cnf檔案裡的設定在重啟proxysql服務後生效(即想要讓proxysql重啟時讀取並解析proxysql.cnf設定檔)，則需要先刪除/var/lib/proxysql/proxysql.db 資料庫文件，然後重新啟動proxysql服務。

這樣就相當於初始化啟動proxysql服務了，會再次生產一個純淨的proxysql.db資料庫檔案(如果之前配置了proxysql相關路由規則等，則就會被抹掉)。

所以你要先刪除/var/lib/proxysql/proxysql.db資料庫文件，然後再重啟/啟動proxysql服務。
```

```
整套配置系統分為三層：頂層為 RUNTIME ,中間層為 MEMORY , 底層也就是持久層 DISK 和 CONFIG FILE 。

RUNTIME ： 代表 ProxySQL 目前生效的正在使用的配置，無法直接修改這裡的配置，必須從下一層 “load” 進來。
MEMORY： MEMORY 圖層上方連接 RUNTIME 圖層，下方連接持久層。 這層可以正常操作 ProxySQL 配置，隨便修改，不會影響生產環境。 修改一個設定一般都是現在 MEMORY 層完成的，確認正常之後在載入達到 RUNTIME 和 持久化的磁碟上。

DISK 和 CONFIG FILE：持久化配置訊息，重啟後記憶體中的配置資訊會遺失，所需要將設定資訊保留在磁碟中。 重啟時，可以從磁碟快速載入回來。
```

## 目錄

- [MySQL 工具 ProxySQL(高性能 高可用性的 MySQL 代理)](#mysql-工具-proxysql高性能-高可用性的-mysql-代理)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [腳本相關](#腳本相關)
    - [心得相關](#心得相關)
    - [percona 相關](#percona-相關)
    - [例外狀況相關](#例外狀況相關)
    - [SQL 語句 (轉址)`                              `](#sql-語句-轉址------------------------------)
    - [相關筆記](#相關筆記)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Docker 部署](#docker-部署)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
    - [Cluster叢集配置：(讓所有proxysql同步)](#cluster叢集配置讓所有proxysql同步)
- [指令](#指令)
  - [進行基本設定](#進行基本設定)
  - [服務操作](#服務操作)
  - [查看目前所有設定](#查看目前所有設定)
  - [透過 ProxySQL 連接到已設定的 MySQL 伺服器](#透過-proxysql-連接到已設定的-mysql-伺服器)
  - [ProxySQL 操作](#proxysql-操作)
    - [使用者](#使用者)
    - [MySQL 伺服器](#mysql-伺服器)
    - [設定路由規則](#設定路由規則)
    - [應用配置](#應用配置)
    - [設定](#設定)
    - [修改 mysql\_variables（proxysql.db 已存在時）](#修改-mysql_variablesproxysqldb-已存在時)
    - [ProxySQL Cluster 相關](#proxysql-cluster-相關)
    - [觀察群集狀況 （所有 ProxySQL 節點上都可以查看）](#觀察群集狀況-所有-proxysql-節點上都可以查看)
  - [設定 ProxySQL 監聽端口](#設定-proxysql-監聽端口)
  - [高可用步驟 (MySQL Replication)](#高可用步驟-mysql-replication)
    - [MySQL 配置所需帳戶](#mysql-配置所需帳戶)
    - [ProxySQL 設定對外存取帳號 (透過 ProxySQL 連線 MySQL)](#proxysql-設定對外存取帳號-透過-proxysql-連線-mysql)
    - [ProxySQL 建立群組](#proxysql-建立群組)
    - [ProxySQL 新增主從伺服器節點](#proxysql-新增主從伺服器節點)
    - [ProxySQL 設置監控 MySQL 後端節點](#proxysql-設置監控-mysql-後端節點)
    - [設定讀寫分離策略：路由規則](#設定讀寫分離策略路由規則)
    - [測試讀寫分離](#測試讀寫分離)
  - [完整初始化設定（Master-Slave 讀寫分離）](#完整初始化設定master-slave-讀寫分離)
    - [Step 1：設定 Replication Hostgroup](#step-1設定-replication-hostgroup)
    - [Step 2：設定 MySQL Servers（含 weight / max_connections）](#step-2設定-mysql-servers含-weight--max_connections)
    - [Step 3：設定 Query Rules（讀寫分離）](#step-3設定-query-rules讀寫分離)
    - [Step 4：設定 MySQL Users](#step-4設定-mysql-users)
    - [最終確認](#最終確認)
    - [建立 MySQL 與 ProxySQL 應用帳號](#建立-mysql-與-proxysql-應用帳號)
  - [Slave 狀態管理（下線 / 重新啟用）](#slave-狀態管理下線--重新啟用)
    - [下線 Slave（OFFLINE_HARD）](#下線-slaveoffline_hard)
    - [重新啟用 Slave](#重新啟用-slave)
    - [OFFLINE_HARD vs OFFLINE_SOFT 說明](#offline_hard-vs-offline_soft-說明)
  - [高可用步驟 (MySQL Group Replication)](#高可用步驟-mysql-group-replication)
    - [群組](#群組)
    - [添加 mysql](#添加-mysql)
    - [路由](#路由)
    - [監控 (高可用)](#監控-高可用)
    - [修改伺服器的狀態](#修改伺服器的狀態)
  - [常用查詢](#常用查詢)
    - [查看監控](#查看監控)
- [測試意外宕機，故障轉移 (MGR)](#測試意外宕機故障轉移-mgr)
  - [恢復](#恢復)
    - [保持資料完整](#保持資料完整)
    - [啟動組](#啟動組)
    - [查看 各表格是否自動恢復](#查看-各表格是否自動恢復)
- [例外狀況](#例外狀況)
  - [Can't connect to local MySQL server through socket '/var/lib/mysql/mysql. sock' (2)](#cant-connect-to-local-mysql-server-through-socket-varlibmysqlmysql-sock-2)
  - [MySQL 8.4 caching_sha2_password 不相容](#mysql-84-caching_sha2_password-不相容)
  - [connection is locked to hostgroup X but trying to reach hostgroup Y](#connection-is-locked-to-hostgroup-x-but-trying-to-reach-hostgroup-y)
- [高可用 說明](#高可用-說明)
- [ProxySQL 部署方案](#proxysql-部署方案)
  - [MySQL 節點故障 vs ProxySQL 本身故障](#mysql-節點故障-vs-proxysql-本身故障)
    - [MySQL 節點故障 → ProxySQL 自動處理](#mysql-節點故障--proxysql-自動處理)
    - [ProxySQL 本身故障 → 需要手動切換](#proxysql-本身故障--需要手動切換)
  - [方案一：HA 雙機熱備（Keepalived + VIP）](#方案一ha-雙機熱備keepalived--vip)
    - [架構](#架構)
    - [適用場景](#適用場景)
    - [Keepalived 設定重點](#keepalived-設定重點)
    - [故障切換驗證](#故障切換驗證)
  - [方案二：分用途入口（寫入專用 / 唯讀專用）](#方案二分用途入口寫入專用--唯讀專用)
    - [架構](#架構-1)
    - [路由規則差異](#路由規則差異)
    - [ProxySQL B 路由規則設定](#proxysql-b-路由規則設定)
    - [適用場景](#適用場景-1)
  - [方案三：HA 熱備（無自動切換機制）](#方案三ha-熱備無自動切換機制)
    - [架構](#架構-2)
    - [MySQL 單節點故障保證](#mysql-單節點故障保證)
    - [與方案一的差異](#與方案一的差異)
    - [手動切換流程](#手動切換流程)
    - [目前節點設定（avnight 環境）](#目前節點設定avnight-環境)
- [腳本](#腳本)
  - [gr\_sw\_mode\_checker.sh](#gr_sw_mode_checkersh)
  - [gr\_mw\_mode\_sw\_cheker.sh](#gr_mw_mode_sw_chekersh)
  - [proxysql\_groupreplication\_checker.sh](#proxysql_groupreplication_checkersh)
  - [定期檢查 MySQL 伺服器的健康狀態-](#定期檢查-mysql-伺服器的健康狀態-)

## 參考資料

[官方網站](https://proxysql.com/)

[官方網站 Initial Configuration](https://proxysql.com/documentation/ProxySQL-Configuration/)

[官方 ProxySQL Docker Image](https://hub.docker.com/r/proxysql/proxysql)

[官方網站 文檔](https://proxysql.com/Documentation/)

[Download and Install ProxySQL](https://proxysql.com/documentation/installing-proxysql/)

[Admin Variables](https://proxysql.com/Documentation/global-variables/admin-variables/)

[MySQL Variables](https://proxysql.com/Documentation/global-variables/mysql-variables/)

[MySQL Monitor Variables](https://proxysql.com/Documentation/global-variables/mysql-monitor-variables/)

### 腳本相關

[ZzzCrazyPig/proxysql_groupreplication_checker - 設定proxysql故障轉移 腳本](https://github.com/ZzzCrazyPig/proxysql_groupreplication_checker)

[ZzzCrazyPig/proxysql_groupreplication_checker - 設定proxysql故障轉移 腳本說明](https://github.com/ZzzCrazyPig/proxysql_groupreplication_checker/blob/master/README_Chinese.md)

[lefred/proxysql_groupreplication_checker](https://github.com/lefred/proxysql_groupreplication_checker)

### 心得相關

[使用 ProxySQL 來簡化 MySQL 的讀寫分離](https://blog.yowko.com/proxysql/)

```
實作一主兩從的一個MySQL叢集和一個ProxySQL代理，ProxySQL代理MYSQL叢集的資料請求並且進行讀寫分離。
```

[用Docker实现MySQL ProxySQL读写分离](https://blog.breezelin.cn/practice-mysql-proxysql-docker-compose.html)

[ProxySQL 基础篇](https://www.cnblogs.com/keme/p/12290977.html)

[Setting Up ProxySQL and MySQL Replication using Docker](https://medium.com/technology-hits/setting-up-proxysql-and-mysql-replication-using-docker-35afe395b4e7)

[wagnerjfr/docker-proxysql-mysql](https://github.com/wagnerjfr/docker-proxysql-mysql)

[【MySql】ProxySQL指南](https://blog.csdn.net/weixin_44231544/article/details/129155140)

[ProxySQL+Mysql实现数据库读写分离实战](https://segmentfault.com/a/1190000022074101)

[MySQL中间件：ProxySQL](https://www.cnblogs.com/f-ck-need-u/p/7586194.html#middleware)

[MySQL/MariaDB系列文章目录](https://www.cnblogs.com/f-ck-need-u/p/7586194.html#middleware)

[ProxySQL配置与高可用(MySQL Group Replication)](https://www.yoyoask.com/?p=3560)

[CentOS 7.6配置MySQL 5.7 MGR单主高可用+ProxySQL实现读写分离和故障转移](https://blog.51cto.com/qiuyue/2413300?source=drh)

[MySQL高可用架构MHA+ProxySQL实现读写分离和负载均衡](https://bbs.huaweicloud.com/blogs/344705)

[重要参考步骤---ProxySQL实现读写分离(MySQL Replication)](https://www.cnblogs.com/hahaha111122222/p/16295607.html)

[（5.20）mysql中的ProxySQL实现读写分离与读负载均衡【转】](https://www.cnblogs.com/gered/p/12856263.html)

[关于ProxySQL路由的简述 - mysql_query_rules](https://www.cnblogs.com/hahaha111122222/p/16382884.html)

### percona 相關

[ProxySQL、proxysql-admin 和 percona-scheduler-admin 文檔](https://docs.percona.com/proxysql/index.html)

### 例外狀況相關

[Can't connect to local MySQL server through socket '/var/lib/mysql/mysql. sock' (2)](https://github.com/xinity/pxc_swarm/issues/2)

[Ubuntu 18.04 - Fresh ProxySQL install - no mysqld.sock port found](https://github.com/sysown/proxysql/issues/2135)

### SQL 語句 (轉址)`                                `

[骏马金龙](https://www.cnblogs.com/f-ck-need-u/p/9300829.html#1%E5%85%B3%E4%BA%8Eproxysql%E8%B7%AF%E7%94%B1%E7%9A%84%E7%AE%80%E8%BF%B0)

[骏马金龙 - 基于SQL语句路由](https://www.cnblogs.com/f-ck-need-u/p/9300829.html#6%E5%9F%BA%E4%BA%8Esql%E8%AF%AD%E5%8F%A5%E8%B7%AF%E7%94%B1)

### 相關筆記

[MySQL 筆記（主）](./MySQL_筆記.md)
[MySQL 筆記 - Replication 主從](./MySQL_筆記_Replication(Master-Slave_主從).md)
[MySQL 筆記 - InnoDB Cluster 叢集](./MySQL_筆記_Cluster(叢集).md)
[MySQL 工具 - ProxySQL Admin Web UI](./MySQL_工具_ProxySQL_Admin(管理_ProxySQL_的_Web_界面工具).md)
[MySQL 工具 - Orchestrator HA](./MySQL_工具_Orchestrator(HA-高可用_工具).md)
[MySQL 工具 - MySQL Router](./MySQL_工具_MySQL_Router(輕量級的路由器).md)
[MySQL 工具 - MySQL Shell](./MySQL_工具_MySQL_Shell(交互式的命令行工具).md)
[MySQL 工具 - Percona XtraBackup 備份](./MySQL_工具_Percona_XtraBackup(資料備份的工具).md)
[MySQL 工具 - Percona pt-table-sync 修復複製錯誤](./MySQL_工具_Percona_pt-table-sync(修復複製錯誤_1032).md)
[MySQL 工具 - mysqlbinlog](./MySQL_工具_mysqlbinlog(檢查主資料庫中的二進制日誌).md)
[MySQL 工具 - phpMyAdmin](./MySQL_工具_phpMyAdmin(MySQL資料庫管理工具).md)
[MySQL 工具 - Adminer](./MySQL_工具_Adminer(輕量級MySQL管理工具).md)

# 安裝

## Debian (Ubuntu)

```bash
# 更新套件列表
apt-get update

# 安裝 ProxySQL
apt-get install -y proxysql

# 安裝 mysql 工具
apt-get install -y mysql-client mysql-server
```

## RedHat (CentOS)

```bash
# 安裝 EPEL 存儲庫
yum install epel-release

# 更新套件列表
yum update

# 安裝 ProxySQL
yum install -y proxysql

# 安裝 mysql 工具
yum install -y mysql
```

## Docker 部署

```yml
version: '3'
services:
  proxysql:
    image: proxysql/proxysql:2.0.17
    container_name: proxysql
    volumes:
      - ./proxysql.cnf:/etc/proxysql.cnf
    ports:
      - "6032:6032"
      - "6033:6033"
    environment:
      - MYSQL_ROOT_PASSWORD=adminadmin
    restart: always
```

## 配置文檔

> ⚠️ **`proxysql.cnf` 僅第一次啟動時有效**
>
> ProxySQL 首次啟動時讀取 `proxysql.cnf` 並產生 `proxysql.db`。
> 之後重啟只讀 `proxysql.db`，**修改 `.cnf` 不會生效**。
>
> 若 `proxysql.db` 已存在，須透過 Admin 介面用 SQL 修改，
> 詳見 [修改 mysql\_variables（proxysql.db 已存在時）](#修改-mysql_variablesproxysqldb-已存在時)。

通常在 `/etc/proxysql/proxysql.cnf`

`官方 dockerhub 範例`

```ini
datadir="/var/lib/proxysql"

admin_variables=
{
	admin_credentials="admin:admin;radmin:radmin"
	mysql_ifaces="0.0.0.0:6032"
}

mysql_variables=
{
	threads=4
	max_connections=2048
	default_query_delay=0
	default_query_timeout=36000000
	have_compress=true
	poll_timeout=2000
	interfaces="0.0.0.0:6033"
	default_schema="information_schema"
	stacksize=1048576
	server_version="5.5.30"
	connect_timeout_server=3000
	monitor_username="monitor"
	monitor_password="monitor"
	monitor_history=600000
	monitor_connect_interval=60000
	monitor_ping_interval=10000
	monitor_read_only_interval=1500
	monitor_read_only_timeout=500
	ping_interval_server_msec=120000
	ping_timeout_server=500
	commands_stats=true
	sessions_sort=true
	connect_retries_on_failure=10
}
```

### 基本範例

```ini
# 基本設定
datadir="/var/lib/proxysql"  # ProxySQL 數據目錄
logfile="/var/log/proxysql.log"  # ProxySQL 日誌文件
pidfile="/var/run/proxysql/proxysql.pid"  # ProxySQL PID 文件
admin_variables= {
    admin_credentials="admin:adminadmin"  # 管理員憑據
    mysql_ifaces="0.0.0.0:6032"  # MySQL 接口地址和端口
    refresh_interval=2000  # 管理器刷新間隔（毫秒）
    web_enabled=true  # 啟用 Web 界面
    web_port=6080  # Web 界面端口
    web_user="admin"  # Web 界面用戶名
    web_passwd="admin"  # Web 界面密碼
}

# MySQL 伺服器組配置
mysql_groups = (
    {
        writer_hostgroup=10,  # 寫入操作的 Hostgroup
        backup_hostgroup=20,  # 備份操作的 Hostgroup
        reader_hostgroup=30,  # 讀取操作的 Hostgroup
        offline_hostgroup=9999,  # 下線操作的 Hostgroup
        max_writers=1,  # 最大寫入數
        writer_is_also_reader=1  # 寫入操作是否同時是讀取操作
    },
)

# 監聽端口配置
mysql_variables = (
    {
        variable_name="admin_variables.admin_credentials",  # 參數名稱
        variable_value="admin:adminadmin"  # 參數值
    },
)

# 查詢攔截和重寫配置
; 這個查詢規則的作用是，當有 SQL 查詢匹配正則表達式 ^SELECT.*FOR UPDATE$ 時，將該查詢發送到 Hostgroup 20。
; 這可能用於特定類型的查詢進行路由或處理。
mysql_query_rules = (
    {
        rule_id=1,  # 規則 ID
        match_digest="^SELECT.*FOR UPDATE$",  # 匹配的 SQL 語句
        destination_hostgroup=20,  # 目標 Hostgroup
        apply=1  # 是否應用此規則
    },
)

# 默認連接池配置
mysql_servers = (
    {
        hostgroup_id=10,  # 默認 Hostgroup ID
        hostname="mysql_server1_ip",  # 默認連接的 MySQL 伺服器 IP 地址
        port=3306,  # 默認連接的 MySQL 伺服器端口
        max_connections=100,  # 最大連接數
        weight=100  # 默認權重
    },
    {
        hostgroup_id=20,  # 默認 Hostgroup ID
        hostname="mysql_server2_ip",  # 默認連接的 MySQL 伺服器 IP 地址
        port=3306,  # 默認連接的 MySQL 伺服器端口
        max_connections=100,  # 最大連接數
        weight=100  # 默認權重
    },
)
```

```ini
datadir="/var/lib/proxysql"

admin_variables=
{
    admin_credentials="admin:admin;radmin:radmin"
    mysql_ifaces="0.0.0.0:6032"
    refresh_interval=2000
}

mysql_variables=
{
    threads=4
    max_connections=2048
    default_query_delay=0
    default_query_timeout=36000000
    have_compress=true
    poll_timeout=2000
    interfaces="0.0.0.0:6033;/tmp/proxysql.sock"
    default_schema="information_schema"
    stacksize=1048576
    server_version="5.1.30"
    connect_timeout_server=10000
    monitor_history=60000
    monitor_connect_interval=200000
    monitor_ping_interval=200000
    ping_interval_server_msec=10000
    ping_timeout_server=200
    commands_stats=true
    sessions_sort=true
    monitor_username="monitor"
    monitor_password="monitor"
}

mysql_replication_hostgroups =
(
    { writer_hostgroup=10 , reader_hostgroup=20 , comment="host groups" }
)

mysql_servers =
(
    { address="source" , port=3306 , hostgroup=10, max_connections=100 , max_replication_lag = 5 },
    { address="replica1" , port=3306 , hostgroup=20, max_connections=100 , max_replication_lag = 5 },
    { address="replica2" , port=3306 , hostgroup=20, max_connections=100 , max_replication_lag = 5 }
)

mysql_query_rules =
(
    {
        rule_id=100
        active=1
        match_pattern="^SELECT .* FOR UPDATE"
        destination_hostgroup=10
        apply=1
    },
    {
        rule_id=200
        active=1
        match_pattern="^SELECT .*"
        destination_hostgroup=20
        apply=1
    },
    {
        rule_id=300
        active=1
        match_pattern=".*"
        destination_hostgroup=10
        apply=1
    }
)

mysql_users =
(
    { username = "root" , password = "mypass" , default_hostgroup = 10 , active = 1 }
)
```

### Cluster叢集配置：(讓所有proxysql同步)

```ini
admin_variables=
{
        admin_credentials="admin:admin;cluster_kevin:123456"
#       mysql_ifaces="127.0.0.1:6032;/tmp/proxysql_admin.sock"
        mysql_ifaces="0.0.0.0:6032"
#       refresh_interval=2000
#       debug=true
        cluster_username="cluster_kevin"
        cluster_password="123456"
        cluster_check_interval_ms=200
        cluster_check_status_frequency=100
        cluster_mysql_query_rules_save_to_disk=true

        cluster_mysql_servers_save_to_disk=true
        cluster_mysql_users_save_to_disk=true

        cluster_proxysql_servers_save_to_disk=true

        cluster_mysql_query_rules_diffs_before_sync=3
        cluster_mysql_servers_diffs_before_sync=3
        cluster_mysql_users_diffs_before_sync=3
        cluster_proxysql_servers_diffs_before_sync=3
}

proxysql_servers =
(
        {
                hostname="192.168.6.121"
                port=6032
                weight=1
                comment="ProxySQL-node1"
        },
        {
                hostname="192.168.6.122"
                port=6032
                weight=1
                comment="ProxySQL-node2"
        },
        {
                hostname="192.168.6.123"
                port=6032
                weight=1
                comment="ProxySQL-node3"
        }
)
```

# 指令

`查看 ProxySQL 的版本`

```bash
proxysql --version
```

`使用 MySQL 客戶端連接到 ProxySQL`

```bash
mysql -h <proxySQL_host> -P <proxySQL_port> -u <mysql_username> -p
```

<proxySQL_host>: 你的 ProxySQL 主機的 IP 地址或主機名。

<proxySQL_port>: ProxySQL 監聽的端口，通常是 6033，但根據你的配置可能有所不同。

<mysql_username>: MySQL 用戶名。

## 進行基本設定

`連接到 ProxySQL 管理`

```bash
mysql -h127.0.0.1 -P6032 -uadmin -p --prompt='ProxyAdmin> '
```

`查看 databases`

```sql
SHOW DATABASES;
```

```
+-----+---------------+-------------------------------------+
| seq | name          | file                                |
+-----+---------------+-------------------------------------+
| 0   | main          |                                     |
| 2   | disk          | /var/lib/proxysql/proxysql.db       |
| 3   | stats         |                                     |
| 4   | monitor       |                                     |
| 5   | stats_history | /var/lib/proxysql/proxysql_stats.db |
+-----+---------------+-------------------------------------+

main：記憶體設定資料庫，表裡存放後端db實例、使用者驗證、路由規則等資訊。
表名以runtime_開頭的表示proxysql目前運行的配置內容，不能透過dml語句修改，只能修改對應的不以runtime_ 開頭的（在記憶體）裡的表，然後LOAD 使其生效， SAVE 使其存到硬碟以供下次重啟加載。
disk：是持久化到硬碟的配置，sqlite資料檔。
stats：是proxysql運行抓取的統計信息，包括到後端各指令的執行次數、流量、processlist、查詢種類匯總/執行時間等等。
monitor：庫儲存 monitor 模組收集的信息，主要是對後端db的健康/延遲檢查。
```

## 服務操作

```bash
# 啟動服務
systemctl start proxysql

# 查詢啟動狀態
systemctl status proxysql

# 重新啟動
systemctl restart proxysql

# 停止服務
systemctl stop proxysql

# 開啟開機自動啟動
systemctl enable proxysql

# 關閉開機自動啟動
systemctl disable proxysql

### 不是所有的服務都支持 ###
# (start, stop, restart, try-restart, reload, force-reload, status)
# 重新載入
service proxysql reload
```

## 查看目前所有設定

```sql
-- 查看 server 清單（master/slave 分組）
SELECT hostgroup_id, hostname, port, status, weight FROM mysql_servers ORDER BY hostgroup_id;

-- 查看 replication hostgroup 設定
SELECT * FROM mysql_replication_hostgroups;

-- 查看 query rules
SELECT rule_id, active, match_pattern, destination_hostgroup, comment FROM mysql_query_rules ORDER BY rule_id;

-- 查看 user 設定
SELECT username, active, default_hostgroup, transaction_persistent FROM mysql_users;
```

### 確認設定已生效（Runtime 驗證）

ProxySQL 設定分三層：memory（暫存）→ runtime（生效）→ disk（持久化）。
`LOAD ... TO RUNTIME` 後才真正生效；`SAVE ... TO DISK` 後重啟才不會遺失。

**查看 runtime（當前生效）**

```sql
-- 路由規則是否生效
SELECT rule_id, active, match_pattern, destination_hostgroup
FROM runtime_mysql_query_rules ORDER BY rule_id;

-- Server 狀態是否生效
SELECT hostgroup_id, hostname, port, status, weight
FROM runtime_mysql_servers ORDER BY hostgroup_id;

-- User 設定是否生效
SELECT username, active, default_hostgroup, transaction_persistent
FROM runtime_mysql_users;
```

**對比 memory vs runtime**

若兩者結果不同，表示改了但忘了執行 `LOAD ... TO RUNTIME`。

```sql
SELECT rule_id, destination_hostgroup FROM mysql_query_rules ORDER BY rule_id;         -- memory
SELECT rule_id, destination_hostgroup FROM runtime_mysql_query_rules ORDER BY rule_id; -- runtime（生效）
```

**確認路由命中情況**

```sql
-- hits 數字增加表示該 rule 有被使用
SELECT rule_id, hits, destination_hostgroup, match_pattern
FROM stats_mysql_query_rules ORDER BY rule_id;
```

**確認各 hostgroup 流量**

```sql
-- Queries 持續增加，確認 SELECT 走 HG2、寫入走 HG1
SELECT hostgroup, srv_host, srv_port, ConnUsed, ConnFree, Queries
FROM stats_mysql_connection_pool ORDER BY hostgroup;
```

## 透過 ProxySQL 連接到已設定的 MySQL 伺服器

ProxySQL 的管理端口（6032）

ProxySQL 的 MySQL 連接端口（通常是 6033）

--prompt 定制提示符的外觀和格式

```bash
mysql -uyour_username -pyour_password -h127.0.0.1 -P6032 --prompt='ProxySQL> '
```

```bash
mysql -uyour_username -pyour_password -h127.0.0.1 -P6033 --prompt='MySQL> '
```

## ProxySQL 操作

### 使用者

`新增使用者`

```sql
INSERT INTO mysql_users (username, password, active, default_hostgroup)
VALUES ('your_username', 'your_password', 1, 1);
```

`修改使用者`

```sql
UPDATE mysql_users
SET username = 'new_username', password = 'new_password'
WHERE username = 'old_username';
```

`刪除使用者`

ProxySQL 層（在 ProxySQL Admin 執行）：

```sql
DELETE FROM mysql_users WHERE username = 'your_username';
LOAD MYSQL USERS TO RUNTIME;
SAVE MYSQL USERS TO DISK;
```

MySQL 層（在 MySQL 執行）——確認該 user 的所有 host：

```sql
-- 查詢確認該 user 存在（含所有 host）
SELECT user, host FROM mysql.user WHERE user = 'your_username';

-- 依查到的 host 刪除
DROP USER 'your_username'@'%';
FLUSH PRIVILEGES;
```

若不確定 host，批量產生 DROP 指令後再執行：

```sql
SELECT CONCAT("DROP USER '", user, "'@'", host, "';")
FROM mysql.user
WHERE user = 'your_username';
```

`應用 MYSQL USERS 配置`

```sql
LOAD MYSQL USERS TO RUNTIME;
SAVE MYSQL USERS TO DISK;
```

`查看 設定的 mysql users`

```sql
SELECT * FROM mysql_users;
```

### MySQL 伺服器

```sql
-- 將 MySQL 伺服器加入 master 群組
INSERT INTO mysql_servers (hostgroup_id, hostname, port)
VALUES (1, 'master_server', 3306);

-- 將 MySQL 伺服器加入 slave 群組
INSERT INTO mysql_servers (hostgroup_id, hostname, port)
VALUES (2, 'slave_server', 3306);
```

```sql
-- 更新現有的 MySQL 伺服器配置
UPDATE mysql_servers
SET hostname = '新的主機名稱', port = 新的端口
WHERE hostgroup_id = 你的主機群組ID AND hostname = '舊的主機名稱' AND port = 舊的端口;
```

`應用 MYSQL SERVERS 配置`

```sql
LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

`查看 設定的 mysql servers`

```sql
SELECT * FROM mysql_servers;
```

`清除所有 MYSQL SERVERS`

```sql
DELETE FROM mysql_servers;
```

### 設定路由規則

`設定路由`

match_digest：

通常是大小寫不敏感的。
Digest 是對查詢內容的散列值，一個字母的大小寫變化通常不會影響 digest 的值。

match_pattern：

大小寫敏感性取決於正則表達式的具體實現。
在一些正則表達式引擎中，可以通過添加標誌（如 i）來使匹配變為大小寫不敏感，但在 ProxySQL 中具體實現可能有所不同。

```sql
-- 將 SELECT 查詢導向到 slave 群組
INSERT INTO mysql_query_rules (rule_id, active, match_digest, destination_hostgroup)
VALUES (1, 1, '^SELECT.*', 2);
-- 將所有其他查詢導向到 master 群組
INSERT INTO mysql_query_rules (rule_id, active, destination_hostgroup)
VALUES (2, 1, 1);
```

`刪除路由`

```sql
DELETE FROM mysql_query_rules WHERE rule_id = your_rule_id;
```

`清除所有路由规则`

```sql
DELETE FROM mysql_query_rules;
```

`常用路由`

```sql
INSERT INTO mysql_query_rules(rule_id,active,match_digest,destination_hostgroup,apply)
VALUES(1,1,'^SELECT.*FOR UPDATE$',1,1);
INSERT INTO mysql_query_rules(rule_id,active,match_digest,destination_hostgroup,apply)
VALUES(2,1,'^SELECT',2,1);
```

`修改 ProxySQL 中的路由規則`

```sql
UPDATE mysql_query_rules
SET rule_definition = '新的規則'
WHERE rule_id = 你的規則ID;
```

`應用 MYSQL QUERY RULES 配置`

```sql
LOAD MYSQL QUERY RULES TO RUNTIME;
SAVE MYSQL QUERY RULES TO DISK;
```

`檢視 ProxySQL 中的路由規則`

```sql
SELECT * FROM mysql_query_rules;
```

### 應用配置

`更新配置到RUNTIME中`

```sql
LOAD MYSQL USERS TO RUNTIME;
LOAD MYSQL SERVERS TO RUNTIME;
LOAD MYSQL QUERY RULES TO RUNTIME;
LOAD MYSQL VARIABLES TO RUNTIME;
LOAD ADMIN VARIABLES TO RUNTIME;
```

`將所有配置儲存至磁碟上`

所有設定資料都保存到磁碟上，永久寫入/var/lib/proxysql/proxysql.db這個檔案中

```sql
SAVE MYSQL USERS TO DISK;
SAVE MYSQL SERVERS TO DISK;
SAVE MYSQL QUERY RULES TO DISK;
SAVE MYSQL VARIABLES TO DISK;
SAVE ADMIN VARIABLES TO DISK;
```

### 設定

`調整 mysql-connections_max_connect_timeout 參數來增加連接超時的時間`

```sql
UPDATE global_variables SET variable_value = 20000 WHERE variable_name = 'mysql-connections_max_connect_timeout';
LOAD MYSQL VARIABLES TO RUNTIME;
SAVE MYSQL VARIABLES TO DISK;
```

### 修改 mysql_variables（proxysql.db 已存在時）

`proxysql.db` 已存在時，重啟不會重讀 `proxysql.cnf`，需透過 Admin 介面用 SQL 修改。

```sql
UPDATE global_variables SET variable_value='4'        WHERE variable_name='mysql-threads';
UPDATE global_variables SET variable_value='2048'     WHERE variable_name='mysql-max_connections';
UPDATE global_variables SET variable_value='0'        WHERE variable_name='mysql-default_query_delay';
UPDATE global_variables SET variable_value='36000000' WHERE variable_name='mysql-default_query_timeout';
UPDATE global_variables SET variable_value='true'     WHERE variable_name='mysql-have_compress';
UPDATE global_variables SET variable_value='2000'     WHERE variable_name='mysql-poll_timeout';
UPDATE global_variables SET variable_value='0.0.0.0:6033' WHERE variable_name='mysql-interfaces';
UPDATE global_variables SET variable_value='information_schema' WHERE variable_name='mysql-default_schema';
UPDATE global_variables SET variable_value='1048576'  WHERE variable_name='mysql-stacksize';
UPDATE global_variables SET variable_value='8.4.0'    WHERE variable_name='mysql-server_version';
UPDATE global_variables SET variable_value='3000'     WHERE variable_name='mysql-connect_timeout_server';
UPDATE global_variables SET variable_value='monitor'  WHERE variable_name='mysql-monitor_username';
UPDATE global_variables SET variable_value='密碼'     WHERE variable_name='mysql-monitor_password';
UPDATE global_variables SET variable_value='600000'   WHERE variable_name='mysql-monitor_history';
UPDATE global_variables SET variable_value='60000'    WHERE variable_name='mysql-monitor_connect_interval';
UPDATE global_variables SET variable_value='10000'    WHERE variable_name='mysql-monitor_ping_interval';
UPDATE global_variables SET variable_value='1500'     WHERE variable_name='mysql-monitor_read_only_interval';
UPDATE global_variables SET variable_value='500'      WHERE variable_name='mysql-monitor_read_only_timeout';
UPDATE global_variables SET variable_value='120000'   WHERE variable_name='mysql-ping_interval_server_msec';
UPDATE global_variables SET variable_value='500'      WHERE variable_name='mysql-ping_timeout_server';
UPDATE global_variables SET variable_value='true'     WHERE variable_name='mysql-commands_stats';
UPDATE global_variables SET variable_value='true'     WHERE variable_name='mysql-sessions_sort';
UPDATE global_variables SET variable_value='10'       WHERE variable_name='mysql-connect_retries_on_failure';

LOAD MYSQL VARIABLES TO RUNTIME;
SAVE MYSQL VARIABLES TO DISK;
```

確認結果：

```sql
-- 查詢所有 mysql 變數
SELECT variable_name, variable_value
FROM global_variables
WHERE variable_name LIKE 'mysql-%'
ORDER BY variable_name;

-- 查詢單一變數
SELECT variable_name, variable_value
FROM global_variables
WHERE variable_name = 'mysql-max_connections';
```

### ProxySQL Cluster 相關

### 觀察群集狀況 （所有 ProxySQL 節點上都可以查看）

```sql
SELECT * FROM proxysql_servers;
```

```sql
SELECT * FROM stats_proxysql_servers_metrics;
```

```sql
SELECT hostname,port,comment,Uptime_s,last_check_ms
FROM stats_proxysql_servers_metrics;
```

```sql
SELECT hostname,name,checksum,updated_at
FROM stats_proxysql_servers_checksums;
```

## 設定 ProxySQL 監聽端口

`設定 ProxySQL 監聽端口`

```sql
UPDATE global_variables
SET variable_value='0.0.0.0'
WHERE variable_name='mysql-interfaces';
UPDATE global_variables
SET variable_value='6033'
WHERE variable_name='mysql-port';
```

`重新載入設定`

```sql
LOAD MYSQL VARIABLES TO RUNTIME;
SAVE MYSQL VARIABLES TO DISK;
```

## 高可用步驟 (MySQL Replication)

### MySQL 配置所需帳戶

`mysql 建立 proxysql 所需 監控帳戶(monitor) 對外存取帳戶(proxysql)`

```sql
CREATE USER 'monitor'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'monitor'@'%';
CREATE USER 'proxysql'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'proxysql'@'%';
FLUSH PRIVILEGES;
```

### ProxySQL 設定對外存取帳號 (透過 ProxySQL 連線 MySQL)

```sql
INSERT INTO mysql_users (username, password, active, default_hostgroup)
VALUES ('proxysql', 'password', 1, 1);
LOAD MYSQL USERS TO RUNTIME;
SAVE MYSQL USERS TO DISK;
```

### ProxySQL 建立群組

```sql
INSERT INTO mysql_replication_hostgroups (writer_hostgroup, reader_hostgroup, comment)
VALUES (1, 2, 'proxy');
LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

### ProxySQL 新增主從伺服器節點

```sql
INSERT INTO mysql_servers (hostgroup_id, hostname, port)
VALUES (1, 'master', 3306);
INSERT INTO mysql_servers (hostgroup_id, hostname, port)
VALUES (2, 'slave1', 3306);
LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

### ProxySQL 設置監控 MySQL 後端節點

```sql
SET mysql-monitor_username='monitor';
SET mysql-monitor_password='password';
LOAD MYSQL VARIABLES TO RUNTIME;
SAVE MYSQL VARIABLES TO DISK;
```

`修改變數的方式`

```sql
UPDATE global_variables SET variable_value='monitor'
WHERE variable_name='mysql-monitor_username';
UPDATE global_variables SET variable_value='password'
WHERE variable_name='mysql-monitor_password';
LOAD MYSQL VARIABLES TO RUNTIME;
SAVE MYSQL VARIABLES TO DISK;
```

### 設定讀寫分離策略：路由規則

把所有以select 開頭的語句全部分配到讀組中，讀組編號是 2

把^SELECT.*FOR UPDATE$語句，這是一個特殊的select語句，會產生一個寫鎖(排他鎖)，把他分到編號為 1 的寫組中，其他所有操作都會預設路由到寫組中

^SELECT.*FOR UPDATE$ 規則的rule_id 必須要小於普通的select規則的rule_id，因為ProxySQL是根據rule_id的順序進行規則比對的。

第一方案

```sql
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (1, 1, '^SELECT.*FOR UPDATE$', 1, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (2, 1, '^SELECT', 2, 1);
INSERT INTO mysql_query_rules(rule_id,active,match_pattern,destination_hostgroup,apply)
VALUES (3, 1, '^select.*for update$', 1, 1);
INSERT INTO mysql_query_rules(rule_id,active,match_pattern,destination_hostgroup,apply)
VALUES (4, 1, '^select', 2, 1);
LOAD MYSQL QUERY RULES TO RUNTIME;
SAVE MYSQL QUERY RULES TO DISK;
```

第二方案

```sql
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (1, 1, '^SELECT', 2, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (2, 1, '.*', 1, 1);
```

### 測試連線（確認可透過 ProxySQL 存取 MySQL 資料）

透過 ProxySQL 對外埠（6033）連線，確認帳號權限與後端路由正常。

**基本連線測試**

```bash
# 互動式登入（手動下查詢）
mysql -u<user> -p -h127.0.0.1 -P6033

# 快速非互動測試（不需進入 shell）
mysql -u<user> -p<password> -h127.0.0.1 -P6033 -e "SELECT 1;"
```

**確認可存取資料庫與資料表**

```bash
# 列出所有資料庫（確認帳號有權限）
mysql -u<user> -p<password> -h127.0.0.1 -P6033 -e "SHOW DATABASES;"

# 列出指定 DB 的資料表
mysql -u<user> -p<password> -h127.0.0.1 -P6033 -e "SHOW TABLES FROM <db_name>;"

# 實際查詢一筆資料
mysql -u<user> -p<password> -h127.0.0.1 -P6033 -e "SELECT * FROM <db_name>.<table_name> LIMIT 1;"
```

**確認連到哪台後端 MySQL**

```bash
# 回傳後端 MySQL 的 server_id，多執行幾次可觀察是否輪流
mysql -u<user> -p<password> -h127.0.0.1 -P6033 -e "SELECT @@server_id, @@hostname;"
```

**確認管理介面（port 6032）可連**

```bash
mysql -uadmin -padmin -h127.0.0.1 -P6032 -e "SELECT * FROM mysql_servers\G"
```

> 若連線失敗，先確認：ProxySQL 服務是否啟動（`systemctl status proxysql`）、帳號是否已加入 `mysql_users` 並 `LOAD MYSQL USERS TO RUNTIME`、後端 MySQL 節點的 `status` 是否為 `ONLINE`（`SELECT hostname, status FROM mysql_servers;`）。

---

### 測試讀寫分離

`測試讀取操作`

```bash
mysql -uproxysql -p -h127.0.0.1 -P6033 --prompt='MySQL> ' -e 'select @@server_id'
```

```bash
mysql -uproxysql -p -h127.0.0.1 -P6033 --prompt='MySQL> ' -e "SELECT CONCAT('Hostname: ', @@hostname) AS Hostname;" --prompt='MySQL> '
```

`測試寫入操作，以事務持久化進行測試`

```bash
mysql -uproxysql -p -h127.0.0.1 -P6033 --prompt='MySQL> ' -e '\
    start transaction;\
    select @@server_id;\
    commit;\
    select @@server_id;'
```

```bash
mysql -uproxysql -p -h127.0.0.1 -P6033 --prompt='MySQL> ' -e "start transaction; SELECT CONCAT('Hostname before commit: ', @@hostname) AS Hostname; commit; SELECT CONCAT('Hostname after commit: ', @@hostname) AS Hostname;"
```

`查看某個特定查詢的路由結果`

```sql
SELECT * FROM stats_mysql_query_digest WHERE digest_text = 'YOUR_QUERY';
```

## 完整初始化設定（Master-Slave 讀寫分離）

`依序執行以下 4 個步驟，每步執行後驗證結果，確認正確再繼續下一步`

### Step 1：設定 Replication Hostgroup

```sql
INSERT INTO mysql_replication_hostgroups (writer_hostgroup, reader_hostgroup, check_type)
VALUES (1, 2, 'read_only');

LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

`驗證`

```sql
SELECT * FROM mysql_replication_hostgroups;
```

### Step 2：設定 MySQL Servers（含 weight / max_connections）

Master 同時加入 Hostgroup 2（weight 高於 Slave）作為讀取備援，Slave 加入 Hostgroup 2 分擔讀取。

```sql
INSERT INTO mysql_servers (hostgroup_id, hostname, port, weight, max_connections)
VALUES (1, 'master-ip', 3306, 1, 10000);

INSERT INTO mysql_servers (hostgroup_id, hostname, port, weight, max_connections)
VALUES (2, 'master-ip', 3306, 5, 10000);

INSERT INTO mysql_servers (hostgroup_id, hostname, port, weight, max_connections)
VALUES (2, 'slave-ip', 3306, 3, 10000);

LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

`驗證`

```sql
SELECT hostgroup_id, hostname, port, status, weight, max_connections FROM mysql_servers ORDER BY hostgroup_id;
```

### Step 3：設定 Query Rules（讀寫分離）

```sql
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (1, 1, '^INSERT', 1, 1);

INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (2, 1, '^UPDATE', 1, 1);

INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (3, 1, '^SELECT.*FOR UPDATE$', 1, 1);

INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (4, 1, '^SELECT', 2, 1);

INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (5, 1, '.*', 1, 1);

LOAD MYSQL QUERY RULES TO RUNTIME;
SAVE MYSQL QUERY RULES TO DISK;
```

| rule_id | 說明 |
|---------|------|
| 1 | `INSERT` → Master（Hostgroup 1） |
| 2 | `UPDATE` → Master（Hostgroup 1） |
| 3 | `SELECT ... FOR UPDATE` → Master（Hostgroup 1），避免鎖定問題 |
| 4 | `SELECT` → Reader（Hostgroup 2），讀寫分離核心規則 |
| 5 | 其餘所有語句 → Master（Hostgroup 1），作為 fallback |

`驗證`

```sql
SELECT rule_id, active, match_pattern, destination_hostgroup, apply FROM mysql_query_rules ORDER BY rule_id;
```

### Step 4：設定 MySQL Users

`transaction_persistent=1`：同一 transaction 內的所有語句強制走同一個 hostgroup，避免讀到尚未同步的資料

```sql
INSERT INTO mysql_users (username, password, active, default_hostgroup, transaction_persistent)
VALUES ('使用者名稱', '密碼', 1, 1, 1);

LOAD MYSQL USERS TO RUNTIME;
SAVE MYSQL USERS TO DISK;
```

`驗證`

```sql
SELECT username, active, default_hostgroup, transaction_persistent FROM mysql_users;
```

### 最終確認

```sql
SELECT * FROM mysql_replication_hostgroups;
SELECT hostgroup_id, hostname, port, status, weight FROM mysql_servers ORDER BY hostgroup_id;
SELECT rule_id, active, match_pattern, destination_hostgroup FROM mysql_query_rules ORDER BY rule_id;
SELECT username, active, default_hostgroup, transaction_persistent FROM mysql_users;
```

### 建立 MySQL 與 ProxySQL 應用帳號

`MySQL 層（在 Master 執行）`

```sql
CREATE USER '帳號'@'%' IDENTIFIED BY '密碼';
GRANT ALL PRIVILEGES ON 資料庫.* TO '帳號'@'%';
FLUSH PRIVILEGES;
```

> MySQL 8.4 已移除 `mysql_native_password`，建立使用者時不需要指定 `WITH mysql_native_password`，但 ProxySQL 對 `caching_sha2_password` 的支援尚不完整，詳見[例外狀況：MySQL 8.4 caching_sha2_password 不相容](#mysql-84-caching_sha2_password-不相容)。

`ProxySQL 層（在 ProxySQL Admin 執行）`

```sql
INSERT INTO mysql_users (username, password, active, default_hostgroup, transaction_persistent)
VALUES ('帳號', '密碼', 1, 1, 1);

LOAD MYSQL USERS TO RUNTIME;
SAVE MYSQL USERS TO DISK;
```

---

## Slave 狀態管理（下線 / 重新啟用）

### 下線 Slave（OFFLINE_HARD）

`適用情境：Slave 驗證異常、維護、或與 ProxySQL 不相容時，先下線 Slave，將所有流量導向 Master`

```sql
-- 1. 將 Slave 設為 OFFLINE_HARD
UPDATE mysql_servers SET status='OFFLINE_HARD' WHERE hostname='slave-ip';

-- 2. 將 SELECT 路由改導向 Master（Hostgroup 1）
UPDATE mysql_query_rules SET destination_hostgroup=1 WHERE rule_id=4;

-- 3. 套用到 runtime
LOAD MYSQL SERVERS TO RUNTIME;
LOAD MYSQL QUERY RULES TO RUNTIME;

-- 4. 存檔（重啟 ProxySQL 後仍生效）
SAVE MYSQL SERVERS TO DISK;
SAVE MYSQL QUERY RULES TO DISK;
```

`確認`

```sql
SELECT hostgroup_id, hostname, port, status, weight FROM mysql_servers ORDER BY hostgroup_id;
SELECT rule_id, active, match_pattern, destination_hostgroup FROM mysql_query_rules ORDER BY rule_id;
```

### 重新啟用 Slave

```sql
-- 1. 將 Slave 設回 ONLINE
UPDATE mysql_servers SET status='ONLINE' WHERE hostname='slave-ip';

-- 2. 將 SELECT 路由改回 Reader（Hostgroup 2）
UPDATE mysql_query_rules SET destination_hostgroup=2 WHERE rule_id=4;

-- 3. 套用到 runtime
LOAD MYSQL SERVERS TO RUNTIME;
LOAD MYSQL QUERY RULES TO RUNTIME;

-- 4. 存檔
SAVE MYSQL SERVERS TO DISK;
SAVE MYSQL QUERY RULES TO DISK;
```

`確認`

```sql
SELECT hostgroup_id, hostname, port, status, weight FROM mysql_servers ORDER BY hostgroup_id;
SELECT rule_id, active, match_pattern, destination_hostgroup FROM mysql_query_rules ORDER BY rule_id;
```

### OFFLINE_HARD vs OFFLINE_SOFT 說明

| 狀態 | 行為 |
|------|------|
| `OFFLINE_HARD` | 立即中斷所有連線，不接受新連線 |
| `OFFLINE_SOFT` | 等待現有連線結束後再下線（較溫和） |

---

## 高可用步驟 (MySQL Group Replication)

`使用 ProxySQL 管理用戶登入到 ProxySQL 控制台`

```bash
mysql -u admin -p -h 127.0.0.1 -P 6032 --prompt='ProxySQLAdmin> '
```

### 群組

`設定 hostgroup`

```sql
UPDATE mysql_servers
SET max_connections = 10000
WHERE hostgroup_id = 1;
UPDATE mysql_servers
SET max_connections = 10000
WHERE hostgroup_id = 2;
UPDATE mysql_servers
SET comment = 'read_only'
WHERE hostgroup_id = 2;
```

`重新載入設定`

```sql
LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

`添加 Host Groups`

`配置 Failover 規則`

Failover 是指在一個系統組件或伺服器失效時，自動或手動將流量或工作負載切換到另一個可用的組件或伺服器的過程。

這是為了確保系統的可用性和持續運行，即使某些組件或伺服器出現故障。

設定 ProxySQL 規則，以確保在主資料庫失效後從資料庫能夠升級為主資料庫。

```sql
INSERT INTO mysql_replication_hostgroups (writer_hostgroup, reader_hostgroup)
VALUES (1, 2);
```

`重新載入設定`

```sql
LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

`查看 MySQL 主從`

```sql
SELECT * FROM mysql_replication_hostgroups;
```

### 添加 mysql

`設定 MySQL 主從伺服器`

```sql
INSERT INTO mysql_servers (hostgroup_id, hostname, port)
VALUES (1, 'master', 3306);
INSERT INTO mysql_servers (hostgroup_id, hostname, port)
VALUES (2, 'slave1', 3306);
```

`重新載入設定`

```sql
LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

### 路由

`添加 Query Rules`

```sql
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (1, 1, '^SELECT.*FOR UPDATE$', 1, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (2, 1, '^SELECT', 2, 1);
```

```sql
INSERT INTO mysql_query_rules(rule_id,active,match_pattern,destination_hostgroup,apply)
VALUES (3, 1,'^select.*for update$', 1, 1);
INSERT INTO mysql_query_rules(rule_id,active,match_pattern,destination_hostgroup,apply)
VALUES (4, 1,'^select', 2, 1);
```

```sql
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (1, 1, '^INSERT', 1, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (2, 1, '^UPDATE', 1, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (3, 1, '^SELECT.*FOR UPDATE$', 1, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (4, 1, '^SELECT', 2, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (5, 1, '.*', 1, 1);
```

`重新載入設定`

```sql
LOAD MYSQL QUERY RULES TO RUNTIME;
SAVE MYSQL QUERY RULES TO DISK;
```

`查看路由`

```sql
SELECT * FROM mysql_query_rules;
```

```sql
SELECT rule_id,active,match_pattern,destination_hostgroup,apply
FROM mysql_query_rules;
```

### 監控 (高可用)

`在 mysql 上新增監控的用戶`

```sql
GRANT SELECT ON *.* TO 'monitor'@'%' IDENTIFIED BY 'monitorpassword';
FLUSH PRIVILEGES;
```

`設定 MySQL 伺服器健康檢查的監控用戶名稱`

mysql-monitor_username 是 ProxySQL 中用於 MySQL 伺服器健康檢查的監控用戶名稱的全局變數。

mysql-monitor_password 是 ProxySQL 中用於 MySQL 伺服器健康檢查的監控用戶密碼的全局變數。

這兩個全局變數的配置主要是為了指定 ProxySQL 在進行 MySQL 伺服器健康檢查時所使用的用戶名稱和密碼。

這樣可以確保 ProxySQL 能夠通過這個用戶進行檢查，並根據 MySQL 伺服器的回應確定其狀態。

這也是確保 ProxySQL 能夠正確執行健康檢查的一個重要配置。

`在proxysql主機端設定監控用戶`

```sql
SET mysql-monitor_username='monitor';
SET mysql-monitor_password='monitorpassword';
UPDATE global_variables
SET variable_value='monitor'
WHERE variable_name='mysql-monitor_username';
UPDATE global_variables
SET variable_value='monitorpassword'
WHERE variable_name='mysql-monitor_password';
```

`重新載入設定`

```sql
LOAD MYSQL VARIABLES TO RUNTIME;
SAVE MYSQL VARIABLES TO DISK;
```

`查看 監控用戶`

```sql
SELECT * FROM global_variables
WHERE variable_name IN('mysql-monitor_username','mysql-monitor_password');
```

`檢查連接到MySQL的日誌`

```sql
SELECT * FROM monitor.mysql_server_ping_log
ORDER BY time_start_us
DESC LIMIT 6;
```

```sql
SELECT * FROM monitor.mysql_server_connect_log
ORDER BY time_start_us
DESC LIMIT 6;
```

`查看read_only的日誌監控`

```sql
SELECT * FROM mysql_server_read_only_log
LIMIT 10;
```

`設定 MySQL 伺服器健康檢查的間隔`

```sql
UPDATE global_variables
SET variable_value = '2000-10000'
WHERE variable_name = 'mysql-check_interval';
```

`設定 MySQL 伺服器健康檢查的超時時間`

```sql
UPDATE global_variables
SET variable_value = '600'
WHERE variable_name = 'mysql-check_timeout';
```

`配置健康檢查間隔`

腳本設置 這裡使用 gr_sw_mode_checker.sh

active : 1: 表示使活性發揮作用
interval_ms : 每隔多久執行一次腳本 (eg: 5000(ms) = 5s 表示每隔 5s 腳本被呼叫一次)
filename : 指定腳本的具體路徑，如上面的/var/lib/proxysql/checker.log
arg1~arg4 : 指定給予腳本的參數

arg1 -> 指定writehostgroup_id
arg2 -> 指定readhostgroup_id
arg3 -> 寫入節點是否可以用於讀取, 1(YES, 預設值), 0(NO)
arg4 -> 日誌文件，預設：'./checker.log'

```sql
INSERT INTO scheduler(active,interval_ms,filename,arg1,arg2,arg3,arg4)
VALUES(1,5000,'/script/proxysql/gr_sw_mode_checker.sh',1,2,1,'/logs/proxysql/gr_sw_mode_checker.log');
```

```sql
UPDATE scheduler
SET active = 1,
    interval_ms = 5000,
    filename = '/script/proxysql/gr_sw_mode_checker.sh',
    arg1 = 1,
    arg2 = 2,
    arg3 = 1,
    arg4 = '/logs/proxysql/gr_sw_mode_checker.log'
WHERE id = 1;
```


`重新載入設定`

```sql
LOAD SCHEDULER TO RUNTIME;
SAVE SCHEDULER TO DISK;
```

`檢視 scheduler`

```sql
SELECT * FROM scheduler;
```

### 修改伺服器的狀態

`設定故障切換`

SET status='OFFLINE_SOFT':
這是設定 status 欄位的值為 OFFLINE_SOFT。
status 欄位表示 伺服器的狀態，OFFLINE_SOFT 表示軟關機，即將伺服器標記為離線，但允許現有連接繼續使用。

WHERE hostname='master_host': 這是一個條件語句，表示只將符合指定主機名為 'master_host' 的伺服器應用此更新。只有符合條件的記錄才會被更新。

將伺服器標記為 "OFFLINE_SOFT" 可能是為了暫時停用它，而不是直接停止 MySQL 服務。

```sql
UPDATE mysql_servers
SET status='OFFLINE_SOFT'
WHERE hostname='master';
```

`重新載入設定`

```sql
LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

## 常用查詢

`查看 ProxySQL 分組`

```sql
SELECT * FROM mysql_replication_hostgroups;
```

`查看 設定的 mysql servers`

```sql
SELECT * FROM mysql_servers;
```

`查看 設定的 mysql users`

```sql
SELECT * FROM mysql_users;
```

`統計各種SQL類型的執行次數與時間`

```sql
SELECT * FROM stats_mysql_commands_counters;
```

`查看連接後端MySQL的連接池信息`

```sql
SELECT * FROM stats_mysql_connection_pool;
```

`查看路由規則表`

```sql
SELECT rule_id,active,match_pattern,destination_hostgroup,apply
FROM mysql_query_rules;
```

`與MySQL相關的代理程式級別的全域統計`

```sql
SELECT * FROM stats_mysql_global;
```

`統計路由命中次數`

```sql
SELECT * FROM stats_mysql_processlist;
```

`從 stats_mysql_query_digest 表中選擇一些關於查詢摘要統計資料的字段，並按 first_seen 降序排列，然後限制結果集的數量為 5 個。`

```sql
SELECT
  hostgroup AS hg,
  count_star,
  FROM_UNIXTIME(first_seen) AS first_seen_time,
  sum_time,
  digest,
  digest_text
FROM
  stats_mysql_query_digest
ORDER BY
  first_seen DESC
LIMIT 5;
```

hostgroup：查詢所屬的主機群組。

count_star：查詢執行的次數。

FROM_UNIXTIME(first_seen)：查詢首次執行的時間，已轉換為日期時間格式。

sum_time：查詢總共執行的時間（單位可能是微秒）。

digest：查詢摘要的雜湊值。

digest_text：查詢語句的文字形式。

`儲存monitor模組收集的信息，主要是對後端db的健康/延遲檢查`

`查看monitor資料庫中的表`

```sql
SHOW tables FROM monitor;
```

`查看請求路由資訊`

```sql
SELECT hostgroup,schemaname,username,digest_text,count_star FROM stats_mysql_query_digest;
```

### 查看監控

`檢查連接到MySQL的日誌`

```sql
SELECT * FROM monitor.mysql_server_ping_log
ORDER BY time_start_us
DESC LIMIT 6;
```

```sql
SELECT * FROM monitor.mysql_server_connect_log
ORDER BY time_start_us
DESC LIMIT 6;
```

`查看read_only的日誌監控`

```sql
SELECT * FROM mysql_server_read_only_log LIMIT 10;
```

# 測試意外宕機，故障轉移 (MGR)

`在 mysql 中查看目前group群組情況`

```sql
SELECT
    MEMBER_ID,
    MEMBER_HOST,
    MEMBER_PORT,
    MEMBER_STATE,
    IF(global_status.VARIABLE_NAME IS NOT NULL,
        'PRIMARY',
        'SECONDARY') AS MEMBER_ROLE
FROM
    performance_schema.replication_group_members
        LEFT JOIN
    performance_schema.global_status ON global_status.VARIABLE_NAME = 'group_replication_primary_member'
        AND global_status.VARIABLE_VALUE = replication_group_members.MEMBER_ID;
```

`查看 sql 請求路由訊息`

```sql
SELECT hostgroup,schemaname,username,digest_text,count_star
FROM stats_mysql_query_digest;
```

## 恢復

### 保持資料完整

如果其他節點不斷有資料寫入

因為之前宕機的是主(Master)

再重新加入群組要以 slave 的身份

reset master 以 slave 身份仍然不能正常加入

匯出一份完整數據(帶gtid的) 匯入當機的 mysql 後 直接啟動組

```bash
mysqldump -uroot -p --all-databases --triggers --routines --events --skip-lock-tables > all.sql
```

### 啟動組

注意這個允許本地不想交的指令必須執行

否則proxysql之前的查詢主從節點健康的視圖你會無法使用

自然proxysql就無法辨識已經恢復正常

```sql
-- 群組複製允許本地不相交
SET global group_replication_allow_local_disjoint_gtids_join=ON;
SET SESSION binlog_format = 'ROW';
SET GLOBAL binlog_format = 'ROW';
START GROUP_REPLICATION;
```

`查看 MGR 組狀態`

```sql
SELECT
    MEMBER_ID,
    MEMBER_HOST,
    MEMBER_PORT,
    MEMBER_STATE,
    IF(global_status.VARIABLE_NAME IS NOT NULL,
        'PRIMARY',
        'SECONDARY') AS MEMBER_ROLE
FROM
    performance_schema.replication_group_members
        LEFT JOIN
    performance_schema.global_status ON global_status.VARIABLE_NAME = 'group_replication_primary_member'
        AND global_status.VARIABLE_VALUE = replication_group_members.MEMBER_ID;
```

### 查看 各表格是否自動恢復

`sys.gr_member_routing_candidate_status` 表是MySQL Group Replication 中的一個系統表，用於提供有關Group Replication 成員（節點）的資訊。

以下是對該資料表的查詢以及欄位的說明：

```sql
SELECT * FROM sys.gr_member_routing_candidate_status;
```

此查詢傳回有關Group Replication 成員的目前狀態和路由候選狀態的資訊。

`sys.gr_member_routing_candidate_status` 表的主要欄位說明：

- **MEMBER_ID：** Group Replication 成員的識別碼。

- **MEMBER_HOST：** 成員的主機名稱或IP 位址。

- **MEMBER_PORT：** 成員的連接埠號碼。

- **MEMBER_STATE：** 成員的目前狀態，例如'ONLINE' 表示在線。

- **MEMBER_ROLE：** 成員的角色，例如'PRIMARY' 表示主節點。

- **ROUTING_SUPPORT：** 成員是否支援路由功能，如果支持，值為'YES'。

- **ROUTING_CANDIDATE_STATUS：** 成員在路由中的候選狀態，例如'ACTIVE' 表示是活躍的候選者。

這個表的查詢結果將顯示Group Replication 成員的詳細信息，包括其當前狀態和是否支援路由功能。
候選狀態可以用於了解成員是否可以用作路由目標。
請注意，使用這個表的查詢需要確保使用者俱有執行相關查詢的權限，而MySQL 版本必須支援`sys` schema 和Group Replication。

`查看 proxysql 節點是否恢復`

```sql
SELECT * FROM mysql_servers;
```

# 例外狀況

## Can't connect to local MySQL server through socket '/var/lib/mysql/mysql. sock' (2)

```bash
mysql -h127.0.0.1 -P6032 -uadmin -p --prompt='ProxyAdmin> '
```

## MySQL 8.4 caching_sha2_password 不相容

MySQL 8.4 已完全移除 `mysql_native_password`，僅支援 `caching_sha2_password`。ProxySQL 對 `caching_sha2_password` 的支援不完整，部分 user 連線會發生驗證失敗。

**症狀**：應用程式透過 ProxySQL 連線時驗證失敗，直接連線 MySQL 則正常。

**處置方式**：將 Slave 設為 `OFFLINE_HARD`，並將所有 SELECT 流量導向 Master，詳見 [Slave 狀態管理（下線 / 重新啟用）](#slave-狀態管理下線--重新啟用)。

## connection is locked to hostgroup X but trying to reach hostgroup Y

**完整錯誤訊息**

```
ERROR: connection is locked to hostgroup 1 but trying to reach hostgroup 2
```

**原因**

當連線在 transaction 期間（或被鎖定在某個 hostgroup 後），若後續查詢被 query rule 路由到不同 hostgroup，ProxySQL 無法在同一連線中途切換，即報此錯。

| 觸發情境 | 說明 |
|---------|------|
| 顯式 transaction | `BEGIN` 之後有 SELECT 被 read rule 路由到 HG2 |
| `SET autocommit=0` | 隱式開啟 transaction，後續 SELECT 被導向 reader |
| `transaction_persistent=0` | query rule 未開啟，transaction 中仍重新評估路由 |

**修正方式**

對所有 active query rule 開啟 `transaction_persistent=1`，確保 transaction 內的查詢不再重新評估路由，全部留在 transaction 開始的 hostgroup：

```sql
UPDATE mysql_query_rules
SET transaction_persistent = 1
WHERE active = 1;

LOAD MYSQL QUERY RULES TO RUNTIME;
SAVE MYSQL QUERY RULES TO DISK;
```

**確認**

```sql
SELECT rule_id, active, match_pattern, destination_hostgroup, transaction_persistent
FROM mysql_query_rules ORDER BY rule_id;
```

# 高可用 說明

在 ProxySQL 中實現高可用性（High Availability）通常需要考慮兩個方面：ProxySQL 本身的高可用性和背後的 MySQL 伺服器的高可用性。
以下是一個簡單的示例，說明如何設定 ProxySQL 的高可用性。

ProxySQL 本身的高可用性：
使用 Keepalived：

安裝 Keepalived 並設定 Virtual IP（VIP）。
在兩個或多個 ProxySQL 伺服器上安裝並運行 Keepalived。
Keepalived 會檢測 ProxySQL 伺服器的狀態，並在主伺服器故障時將 VIP 切換到備用伺服器。
使用 HAProxy：

安裝 HAProxy 並配置它來檢測 ProxySQL 狀態。
在兩個或多個 ProxySQL 伺服器上運行 HAProxy。
HAProxy 可以在主伺服器失效時將流量切換到備用伺服器。
MySQL 伺服器的高可用性：
使用 MySQL 主從複製：

配置 MySQL 主從複製，其中 ProxySQL 主要連接到主伺服器。
如果主伺服器失效，ProxySQL 可以自動切換到備用伺服器。
使用 Galera Cluster：

如果你使用的是 Galera Cluster，ProxySQL 可以連接到 Galera Cluster 中的任何節點，實現高可用性。
使用 MySQL Group Replication：

如果你使用的是 MySQL Group Replication，ProxySQL 可以連接到 Group Replication 中的任何可用節點。
設定 ProxySQL：
安裝 ProxySQL：

在每個 ProxySQL 伺服器上安裝 ProxySQL。
設定 ProxySQL 管理用戶：

使用 ProxySQL 管理用戶登入到 ProxySQL 控制台。
設定 MySQL 主從伺服器：

在 ProxySQL 控制台中添加 MySQL 主伺服器和備用伺服器的配置。
設定監聽端口：

配置 ProxySQL 監聽端口，以便應用程式可以連接到 ProxySQL。
設定 Query Rules 和 Host Groups：

根據需要設定 ProxySQL 的 Query Rules 和 Host Groups。
設定高可用性檢測：

配置 ProxySQL 以檢測 MySQL 伺服器的可用性，並根據需要調整健康檢查的參數。
設定故障切換：

根據需要配置 ProxySQL 進行故障切換，以在主伺服器故障時切換到備用伺服器。
上述步驟僅為一個簡單的示例，實際的設定取決於你的環境和需求。
在設定 ProxySQL 的高可用性時，確保仔細考慮你的 MySQL 環境的特點，以及選擇的高可用性解決方案。

# ProxySQL 部署方案

## MySQL 節點故障 vs ProxySQL 本身故障

這兩種故障的層次不同，處理方式也不同。

### MySQL 節點故障 → ProxySQL 自動處理

ProxySQL 內建 monitor 模組，持續對後端節點執行 ping / connect / read_only 檢查，節點掛掉時自動標為 `OFFLINE`，無需手動介入。

| 故障節點 | ProxySQL 行為 | 對應用程式的影響 |
|---------|--------------|----------------|
| **Slave 掛掉** | HG2 移除 slave，只剩 master（weight=5） | 讀寫全部正常，讀流量全打 master |
| **Master 掛掉** | HG1 無 ONLINE 節點 | **寫入失敗**（INSERT/UPDATE 報錯），SELECT 仍正常（走 HG2 slave） |

### ProxySQL 本身故障 → 需要手動切換

「故障時需手動切換」指的是 **ProxySQL 這一層掛掉**，與 MySQL 節點無關。

```
應用程式 → proxysql-1（這台掛了）→ 連不到任何 MySQL
```

因為沒有 VIP 自動轉移，應用程式必須手動改設定，將連線目標切到另一台 ProxySQL：

```
應用程式 → proxysql-2（手動改設定）→ 正常
```

這是「無自動切換機制」的核心限制——MySQL 層的健康切換是自動的，ProxySQL 層本身的 HA 需要額外工具（Keepalived）才能自動化。

---

## 方案一：HA 雙機熱備（Keepalived + VIP）

### 架構

```
應用程式
    │
    ▼
  VIP（Keepalived 浮動 IP）
    │
    ├─── ProxySQL Primary（MASTER）  ←── 正常時接管流量
    └─── ProxySQL Standby（BACKUP）  ←── Primary 宕機時自動接管
              │
    ┌─────────┴─────────┐
    ▼                   ▼
MySQL Master        MySQL Slave
（HG1 寫入 + HG2 讀取）  （HG2 讀取）
```

- 兩台 ProxySQL 設定**完全相同**，後端指向同一組 MySQL
- Keepalived 管理浮動 VIP；Primary 宕機時 VIP 自動轉到 Standby
- 應用程式只連 VIP，不感知 ProxySQL 切換

### 適用場景

需要 ProxySQL 層零停機切換，對應用透明的生產環境。

### Keepalived 設定重點

Primary 主機 `/etc/keepalived/keepalived.conf`：

```
vrrp_script chk_proxysql {
    script "mysql -uradmin -p<RADMIN_PASSWORD> -h127.0.0.1 -P6032 -e 'SELECT 1' > /dev/null 2>&1"
    interval 2
    weight   -20
    fall     2
    rise     2
}

vrrp_instance VI_PROXYSQL {
    state  MASTER
    interface eth0
    virtual_router_id 51
    priority  100
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass <KEEPALIVED_AUTH_PASS>
    }
    virtual_ipaddress {
        192.168.x.x/24
    }
    track_script {
        chk_proxysql
    }
}
```

Standby 主機改兩項：`state BACKUP`、`priority 90`。

### 故障切換驗證

```bash
# 停止 Primary ProxySQL，確認 VIP 轉到 Standby
docker-compose stop proxysql
ip addr show | grep <VIP>   # 在 Standby 主機執行，應出現 VIP

# 確認應用仍可連線
mysql -uproxysql -p<PASSWORD> -h<VIP> -P6033 -e "SELECT 1;"
```

---

## 方案二：分用途入口（寫入專用 / 唯讀專用）

### 架構

```
寫入應用（Write App）
    │
    ▼
ProxySQL A（讀寫分離，5 條規則）
    ├─── INSERT / UPDATE / DDL → MySQL Master（HG1）
    └─── SELECT               → MySQL Master + Slave（HG2）

唯讀應用（Read-Only App）
    │
    ▼
ProxySQL B（全部導讀取群組，1 條規則）
    └─── .*                   → MySQL Slave（HG2）
```

- ProxySQL A：標準 5 條讀寫分離規則，一般應用使用
- ProxySQL B：全部流量導向 HG2，報表查詢、資料匯出等大量讀取任務使用，完全不碰 master

### 路由規則差異

| | ProxySQL A（讀寫） | ProxySQL B（唯讀） |
|--|--|--|
| 規則數量 | 5 條 | 1 條 |
| INSERT / UPDATE | → HG1（master） | slave 直接拒絕 |
| SELECT | → HG2（master+slave） | → HG2（slave only） |
| SELECT FOR UPDATE | → HG1（master） | → HG2（需應用層確保不用） |

### ProxySQL B 路由規則設定

```sql
DELETE FROM mysql_query_rules;

INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (1, 1, '.*', 2, 1);

LOAD MYSQL QUERY RULES TO RUNTIME;
SAVE MYSQL QUERY RULES TO DISK;
```

ProxySQL B 的 HG2 建議**只放 slave**，確保讀取完全不影響 master：

```sql
DELETE FROM mysql_servers;

-- HG1 保留 master 供 hostgroup 健康檢查，但不對外路由
INSERT INTO mysql_servers (hostgroup_id, hostname, port, weight, max_connections)
VALUES (1, 'mysql-master', 3306, 1, 1);

-- HG2 唯讀：只放 slave
INSERT INTO mysql_servers (hostgroup_id, hostname, port, weight, max_connections)
VALUES (2, 'mysql-slave', 3306, 1, 10000);

LOAD MYSQL SERVERS TO RUNTIME;
SAVE MYSQL SERVERS TO DISK;
```

### 適用場景

讀寫流量來源明確分離、有大量唯讀查詢（報表、分析）不希望影響主節點的場景。

---

## 方案三：HA 熱備（無自動切換機制）

目前 `proxysql_avnight_master` / `proxysql_avnight_slave` 採用此方案。

### 架構

```
應用程式 A → proxysql_avnight_master（ProxySQL 1）
應用程式 B → proxysql_avnight_slave（ProxySQL 2）
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
    MySQL Master           MySQL Slave
    （HG1 寫入 + HG2 讀取）  （HG2 讀取）
```

- 兩台 ProxySQL 設定完全相同，各自獨立服務不同的應用程式
- 無 Keepalived / VIP，ProxySQL 層故障時需手動切換應用連線目標
- MySQL 節點故障由 ProxySQL 內建 monitor 自動處理（無需手動）

### MySQL 單節點故障保證

**只要 MySQL 只有一台故障，應用程式不需要更改連線設定（IP / Port），讀取仍可正常使用。**

| 故障節點 | ProxySQL 自動行為 | 讀取 | 寫入 |
|---------|-----------------|------|------|
| **Slave 故障** | HG2 移除 slave，讀流量全轉 master（weight=5） | ✅ 正常 | ✅ 正常 |
| **Master 故障** | HG1 無節點，HG2 仍有 slave | ✅ 正常 | ❌ 失敗 |

應用程式連線的是 ProxySQL IP，不是 MySQL IP。只要 ProxySQL 存活，節點切換對應用透明。

### 與方案一的差異

| | 方案一（Keepalived） | 方案三（無自動切換） |
|--|--|--|
| MySQL 單節點故障 | 自動，應用透明 | 自動，應用透明 |
| ProxySQL 層故障切換 | 自動（VIP 轉移） | 手動（改應用連線設定） |
| 複雜度 | 需安裝設定 Keepalived | 僅部署兩台 ProxySQL |
| 適合場景 | 生產、對停機完全不容忍 | MySQL 層 HA 足夠，可接受 ProxySQL 層短暫停機 |

### 手動切換流程

當 ProxySQL 1 故障時：

```bash
# 確認 ProxySQL 2 後端節點正常
mysql -uadmin -p<PASSWORD> -h<PROXYSQL_2_IP> -P6032 -e "
  SELECT hostgroup_id, hostname, port, status FROM mysql_servers;
"

# 將應用設定中的 ProxySQL 連線 IP 從 ProxySQL_1_IP 改為 ProxySQL_2_IP
# 重啟應用或熱更新設定
```

### 目前節點設定（avnight 環境）

| Hostgroup | 成員 | IP | Weight |
|-----------|------|----|--------|
| HG1（寫入） | MySQL Master | 192.168.146.53 | 1 |
| HG2（讀取） | MySQL Master | 192.168.146.53 | 5 |
| HG2（讀取） | MySQL Slave  | 192.168.146.232 | 3 |

路由規則（兩台 ProxySQL 相同）：

| rule_id | match_pattern | 目標 HG | 說明 |
|---------|---------------|---------|------|
| 1 | `^INSERT` | 1 | 寫入 → master |
| 2 | `^UPDATE` | 1 | 更新 → master |
| 3 | `^SELECT.*FOR UPDATE$` | 1 | 排他鎖 → master |
| 4 | `^SELECT` | 2 | 讀取 → HG2（master+slave） |
| 5 | `.*` | 1 | 兜底 → master |

# 腳本

## gr_sw_mode_checker.sh

實作單主模式下的MySQL群組複製高可用和讀寫分離的腳本，限制只能有一個節點用於寫入。

```bash
#!/bin/bash
#
# author: CrazyPig
# date: 2017-01-08
# version: 1.0

function usage() {
  echo "Usage: $0 <hostgroup_id write> <hostgroup_id read> [write node can be read : 1(YES: default) or 0(NO)] [log_file]"
  exit 0
}

if [ "$1" = '-h' -o "$1" = '--help' ] || [ -z "$1" -o -z "$2" ]; then
  usage
fi

# receive input arg
writeGroupId="${1:-1}"
readGroupId="${2:-2}"
writeNodeCanRead="${3:-1}"
errFile="${4:-"./checker.log"}"

# variable define
proxysql_user="admin"
proxysql_password="admin"
proxysql_host="127.0.0.1"
proxysql_port="6032"

switchOver=0
timeout=3

# enable(1) debug info or not(0)
debug=1
function debug() {
  local appendToFile="${2:-0}"
  local msg="[`date \"+%Y-%m-%d %H:%M:%S\"`] $1"
  if [ $debug -eq 1 ]; then
    echo $msg
  fi
  if [ $appendToFile -eq 1 ]; then
    echo $msg >> $errFile
  fi
}

debug "writeGroupId : $writeGroupId, readGroupId : $readGroupId, writeNodeCanRead : $writeNodeCanRead, errFile : $errFile"
proxysql_cmd="mysql -u$proxysql_user -p$proxysql_password -h$proxysql_host -P$proxysql_port -Nse"
debug "proxysql_cmd : $proxysql_cmd"
mysql_credentials=$($proxysql_cmd "SELECT variable_value FROM global_variables WHERE variable_name IN ('mysql-monitor_username','mysql-monitor_password') ORDER BY variable_name DESC")
mysql_user=$(echo $mysql_credentials | awk '{print $1}')
mysql_password=$(echo $mysql_credentials | awk '{print $2}')
debug "mysql_user : $mysql_user, mysql_password : $mysql_password"
mysql_cmd="mysql -u$mysql_user -p$mysql_password"
debug "mysql_cmd : $mysql_cmd"

update_servers_cmd_opts="LOAD MYSQL SERVERS TO RUNTIME; SAVE MYSQL SERVERS TO DISK;"

# check node status is OK or not
function isNodeOk() {
  local gr_status=$1
  local trx_behind=$2
  debug "gr_status : $gr_status, trx_behind : $trx_behind"
  if [ "$gr_status" == "YES" ]; then
    return 1
  elif [ "$gr_status" == "" -o "$gr_status" == "NO" ]; then
    return 0
  fi
}

# main logic

# find current write node
read cwn_hostname cwn_port <<< $($proxysql_cmd "SELECT hostname,port FROM mysql_servers WHERE hostgroup_id = $writeGroupId LIMIT 1;")
debug "current write node hostname : ${cwn_hostname}, port : ${cwn_port}"
# for every read node in read hostgroup
output=$($proxysql_cmd "SELECT hostgroup_id, hostname, port, status FROM mysql_servers WHERE hostgroup_id = $readGroupId;" 2>> $errFile)
while read hostgroup_id hostname port status
do
  # check node is ok
  read gr_status trx_behind <<< $(timeout $timeout $mysql_cmd -h$hostname -P$port -Nse "SELECT viable_candidate, transactions_behind FROM sys.gr_member_routing_candidate_status" 2>>$errFile | tail -1 2>>$errFile)
  isNodeOk $gr_status $trx_behind
  isOK=$?
  debug "node [hostgroup_id: $hostgroup_id, hostname: $hostname, port: $port, status: $status, isOK: $isOK ]"
  # node is current write node
  if [ "$hostname" == "$cwn_hostname" -a "$port" == "$cwn_port" ]; then
    debug "node is the current write node"
    if [ $isOK -eq 0 ]; then
      # need to find new write node
      switchOver=1
      debug "current write node [hostgroup_id: $hostgroup_id, hostname: $hostname, port: $port, isOK: $isOK] is not OK, we need to do switch over" 1
      $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_SOFT' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
      $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_HARD' WHERE hostgroup_id = $writeGroupId AND hostname = '$cwn_hostname' AND port = $cwn_port; ${update_servers_cmd_opts}" 2>> $errFile
    else # isOK = 1
      read isPrimaryNode <<< $(timeout $timeout $mysql_cmd -h$hostname -P$port -Nse "SELECT IF((SELECT @@server_uuid) = (SELECT VARIABLE_VALUE FROM performance_schema.global_status WHERE VARIABLE_NAME='group_replication_primary_member'), 1, 0);" 2>> $errFile)
      if [ $isPrimaryNode -eq 0 ]; then
        debug "current write node [hostgroup_id: $hostgroup_id, hostname: $hostname, port: $port, isOK: $isOK] is no longer the write node, we need to do switch over" 1
        $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_HARD' WHERE hostgroup_id = $writeGroupId AND hostname = '$hostname' AND port = '$port'; ${update_servers_cmd_opts}" 2>> $errFile
        switchOver=1
        if [ "$status" != "ONLINE" ]; then
          debug "isOK : $isOK, write node can be read, will update node status to ONLINE [hostgroup_id: $readGroupId, hostname: $hostname, port: $port]"
          $proxysql_cmd "UPDATE mysql_servers SET status = 'ONLINE' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
        fi
        continue
      fi
      if [ $writeNodeCanRead -eq 0 ]; then # write node can not be read
        debug "isOK : $isOK, write node can not be read, will update node status to OFFLINE_SOFT [hostgroup_id: $readGroupId, hostname: $hostname, port: $port]"
        $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_SOFT' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
      else
        if [ "$status" != "ONLINE" ]; then
          debug "isOK : $isOK, write node can be read, will update node status to ONLINE [hostgroup_id: $readGroupId, hostname: $hostname, port: $port]"
          $proxysql_cmd "UPDATE mysql_servers SET status = 'ONLINE' WHERE hostgroup_id = $writeGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
          $proxysql_cmd "UPDATE mysql_servers SET status = 'ONLINE' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
        fi
      fi
    fi
  # node is not current write node and status is not OK
  elif [ $isOK -eq 0 ]; then
    debug "read node [hostgroup_id: $hostgroup_id, hostname: $hostname, port: $port, isOK: $isOK] is not OK, we will set it's status to be 'OFFLINE_SOFT'" 1
    $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_SOFT' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
  elif [ $isOK -eq 1 -a "$status" == "OFFLINE_SOFT" ]; then
    debug "read node [hostgroup_id: $hostgroup_id, hostname: $hostname, port: $port, isOK: $isOK] is OK, but is's status is 'OFFLINE_SOFT', we will update it to be 'ONLINE' 1"
    $proxysql_cmd "UPDATE mysql_servers SET status = 'ONLINE' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
  fi
done <<EOF
$output
EOF

successSwitchOver=0
if [ $switchOver -eq 1 ]; then
  debug "now we will select one normal node from read hostgroup to be the new node ..." 1
  # we need to find new primary node from read hostgroup
output1=$($proxysql_cmd "SELECT hostname, port FROM mysql_servers WHERE hostgroup_id = $readGroupId AND status = 'ONLINE';" 2>> $errFile)
  while read hostname port
  do
    read isPrimaryNode <<< $(timeout $timeout $mysql_cmd -h$hostname -P$port -Nse "SELECT IF((SELECT @@server_uuid) = (SELECT VARIABLE_VALUE FROM performance_schema.global_status WHERE VARIABLE_NAME= 'group_replication_primary_member'), 1, 0);" 2>> $errFile)
    if [ "$isPrimaryNode" != "" -a $isPrimaryNode -eq 1 ]; then
      # success in finding new primary node from read hostgroup
      debug "success in finding new primary node from read hostgroup [hostgroup_id: $readGroupId, hostname: $hostname, port: $port]" 1
      $proxysql_cmd "UPDATE mysql_servers SET hostname = '$hostname', port = '$port', status = 'ONLINE' WHERE hostgroup_id = $writeGroupId; ${update_servers_cmd_opts}" 2>> $errFile
      successSwitchOver=1
      if [ $writeNodeCanRead -eq 0 ]; then
        $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_SOFT' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
      fi
      break
    fi
  done <<EOF
$output1
EOF
  # can not find new primary node from read hostgroup
  if [ $successSwitchOver -eq 0 ]; then
    debug "from read hostgroup, we can not find new node to be write node" 1
  fi
fi
```

## gr_mw_mode_sw_cheker.sh

實作Multi-Primary模式下的MySQL Group Replication高可用且讀寫分離的腳本，限制只能有一個節點可以寫入。

```bash
#!/bin/bash
#
# author: CrazyPig
# date: 2017-01-07
# version: 1.0

function usage() {
  echo "Usage: $0 <hostgroup_id write> <hostgroup_id read> [write node can be read : 1(YES: default) or 0(NO)] [log_file]"
  exit 0
}

if [ "$1" = '-h' -o "$1" = '--help' ] || [ -z "$1" -o -z "$2" ]; then
  usage
fi

# receive input arg
writeGroupId="${1:-1}"
readGroupId="${2:-2}"
writeNodeCanRead="${3:-1}"
errFile="${4:-"./checker.log"}"

# variable define
proxysql_user="admin"
proxysql_password="admin"
proxysql_host="127.0.0.1"
proxysql_port="6032"

switchOver=0
timeout=3

# enable(1) debug info or not(0)
debug=1
function debug() {
  local appendToFile="${2:-0}"
  local msg="[`date \"+%Y-%m-%d %H:%M:%S\"`] $1"
  if [ $debug -eq 1 ]; then
    echo $msg
  fi
  if [ $appendToFile -eq 1 ]; then
    echo $msg >> $errFile
  fi
}

debug "writeGroupId : $writeGroupId, readGroupId : $readGroupId, writeNodeCanRead : $writeNodeCanRead, errFile : $errFile"
proxysql_cmd="mysql -u$proxysql_user -p$proxysql_password -h$proxysql_host -P$proxysql_port -Nse"
debug "proxysql_cmd : $proxysql_cmd"
mysql_credentials=$($proxysql_cmd "SELECT variable_value FROM global_variables WHERE variable_name IN ('mysql-monitor_username','mysql-monitor_password') ORDER BY variable_name DESC")
mysql_user=$(echo $mysql_credentials | awk '{print $1}')
mysql_password=$(echo $mysql_credentials | awk '{print $2}')
debug "mysql_user : $mysql_user, mysql_password : $mysql_password"
mysql_cmd="mysql -u$mysql_user -p$mysql_password"
debug "mysql_cmd : $mysql_cmd"

update_servers_cmd_opts="LOAD MYSQL SERVERS TO RUNTIME; SAVE MYSQL SERVERS TO DISK;"

# check node status is OK or not
function isNodeOk() {
  local gr_status=$1
  local trx_behind=$2
  debug "gr_status : $gr_status, trx_behind : $trx_behind"
  if [ "$gr_status" == "YES" ]; then
    return 1
  elif [ "$gr_status" == "" -o "$gr_status" == "NO" ]; then
    return 0
  fi
}

# main logic

# find current write node
read cwn_hostname cwn_port <<< $($proxysql_cmd "SELECT hostname,port FROM mysql_servers WHERE hostgroup_id = $writeGroupId LIMIT 1;")
debug "current write node hostname : ${cwn_hostname}, port : ${cwn_port}"
# for every read node in read hostgroup
output=$($proxysql_cmd "SELECT hostgroup_id, hostname, port, status FROM mysql_servers WHERE hostgroup_id = $readGroupId;" 2>> $errFile)
while read hostgroup_id hostname port status
do
  # check node is ok
  read gr_status trx_behind <<< $(timeout $timeout $mysql_cmd -h$hostname -P$port -Nse "SELECT viable_candidate, transactions_behind FROM sys.gr_member_routing_candidate_status" 2>>$errFile | tail -1 2>>$errFile)
  isNodeOk $gr_status $trx_behind
  isOK=$?
  debug "node [hostgroup_id: $hostgroup_id, hostname: $hostname, port: $port, status: $status, isOK: $isOK ]"
  # node is current write node
  if [ "$hostname" == "$cwn_hostname" -a "$port" == "$cwn_port" ]; then
    debug "node is the current write node"
    if [ $isOK -eq 0 ]; then
      # need to find new write node
      switchOver=1
      debug "current write node [hostgroup_id: $hostgroup_id, hostname: $hostname, port: $port, isOK: $isOK] is not OK, we need to do switch over" 1
      $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_SOFT' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
      $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_HARD' WHERE hostgroup_id = $writeGroupId AND hostname = '$cwn_hostname' AND port = $cwn_port; ${update_servers_cmd_opts}" 2>> $errFile
    else # isOK = 1
      if [ $writeNodeCanRead -eq 0 ]; then # write node can not be read
        debug "isOK : $isOK, write node can not be read, will update node status to OFFLINE_SOFT [hostgroup_id: $readGroupId, hostname: $hostname, port: $port]"
        $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_SOFT' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
      else
        if [ "$status" != "ONLINE" ]; then
          debug "isOK : $isOK, write node can be read, will update node status to ONLINE [hostgroup_id: $readGroupId, hostname: $hostname, port: $port]"
          $proxysql_cmd "UPDATE mysql_servers SET status = 'ONLINE' WHERE hostgroup_id = $writeGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
          $proxysql_cmd "UPDATE mysql_servers SET status = 'ONLINE' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
        fi
      fi
    fi
  # node is not current write node and status is not OK
  elif [ $isOK -eq 0 ]; then
    debug "read node [hostgroup_id: $hostgroup_id, hostname: $hostname, port: $port, isOK: $isOK] is not OK, we will set it's status to be 'OFFLINE_SOFT'" 1
    $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_SOFT' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
  elif [ $isOK -eq 1 -a "$status" == "OFFLINE_SOFT" ]; then
    debug "read node [hostgroup_id: $hostgroup_id, hostname: $hostname, port: $port, isOK: $isOK] is OK, but is's status is 'OFFLINE_SOFT', we will update it to be 'ONLINE' 1"
    $proxysql_cmd "UPDATE mysql_servers SET status = 'ONLINE' WHERE hostgroup_id = $readGroupId AND hostname = '$hostname' AND port = $port; ${update_servers_cmd_opts}" 2>> $errFile
  fi
done <<EOF
$output
EOF

if [ $switchOver -eq 1 ]; then
  debug "now we will select one normal node from read hostgroup to be the new node ..." 1
  read nwn_hostname nwn_port <<< $($proxysql_cmd "SELECT hostname, port FROM mysql_servers WHERE hostgroup_id = $readGroupId AND status = 'ONLINE' LIMIT 1;" 2>> $errFile)
  if [ "$nwn_hostname" != "" ]; then
    debug "find new write node from read hostgroup, [hostgroup_id: $readGroupId, hostname : $nwn_hostname, port : $nwn_port]" 1
    $proxysql_cmd "UPDATE mysql_servers SET hostname = '$nwn_hostname', port = $nwn_port, status = 'ONLINE' WHERE hostgroup_id = $writeGroupId; ${update_servers_cmd_opts}" 2>> $errFile
    if [ $writeNodeCanRead -eq 0 ]; then
      $proxysql_cmd "UPDATE mysql_servers SET status = 'OFFLINE_SOFT' WHERE hostgroup_id = $readGroupId AND hostname = '$nwn_hostname' AND port = $nwn_port; ${update_servers_cmd_opts}" 2>> $errFile
    fi
  else
    debug "from read hostgroup, we can not find new node to be write node" 1
  fi
fi
```

## proxysql_groupreplication_checker.sh

腳本修改自 : https://github.com/lefred/proxysql_groupreplication_checker，提供了MGR Multi-Primary模式下讀寫分離功能，以及寫入節點故障切換功能。

```bash
#!/bin/bash
## inspired by proxysql_galera_checker.sh
# Author: Frédéric -lefred- Descamps
# version: 0.1
# 2016-08-25

# CHANGE THOSE
PROXYSQL_USERNAME="admin"
PROXYSQL_PASSWORD="admin"
PROXYSQL_HOSTNAME="127.0.0.1"
PROXYSQL_PORT="6032"
#

function usage()
{
  echo "Usage: $0 <hostgroup_id write> [hostgroup_id read] [number writers] [writers are readers 0|1} [log_file]"
  exit 0
}

if [ "$1" = '-h' -o "$1" = '--help'  -o -z "$1" ]
then
  usage
fi

if [ $# -lt 1 ]
then
  echo "Invalid number of arguments"
  usage
fi

HOSTGROUP_WRITER_ID="${1}"
HOSTGROUP_READER_ID="${2:--1}"
NUMBER_WRITERS="${3:-0}"
WRITER_IS_READER="${4:-1}"
ERR_FILE="${5:-/dev/null}"

#echo "Hostgroup writers $HOSTGROUP_WRITER_ID"
#echo "Hostgroup readers $HOSTGROUP_READER_ID"
#echo "Number of writers $NUMBER_WRITERS"
#echo "Writers are readers $WRITER_IS_READER"
#echo "log file $ERR_FILE"

#Timeout exists for instances where mysqld may be hung
TIMEOUT=10

PROXYSQL_CMDLINE="mysql -u$PROXYSQL_USERNAME -p$PROXYSQL_PASSWORD -h $PROXYSQL_HOSTNAME -P $PROXYSQL_PORT --protocol=tcp -Nse"
MYSQL_CREDENTIALS=$($PROXYSQL_CMDLINE "SELECT variable_value FROM global_variables WHERE variable_name IN ('mysql-monitor_username','mysql-monitor_password') ORDER BY variable_name DESC")
MYSQL_USERNAME=$(echo $MYSQL_CREDENTIALS | awk '{print $1}')
MYSQL_PASSWORD=$(echo $MYSQL_CREDENTIALS | awk '{print $2}')
#echo $MYSQL_CREDENTIALS
#echo $MYSQL_USERNAME $MYSQL_PASSWORD
MYSQL_CMDLINE="timeout $TIMEOUT mysql -u$MYSQL_USERNAME -p$MYSQL_PASSWORD "

$PROXYSQL_CMDLINE "SELECT hostgroup_id, hostname, port, status, max_replication_lag FROM mysql_servers WHERE hostgroup_id IN ($HOSTGROUP_WRITER_ID, $HOSTGROUP_READER_ID) AND status <> 'OFFLINE_HARD'" | while read hostgroup server port stat max_replication_lag
do
  read GR_STATUS READONLY TRX_BEHIND <<<$($MYSQL_CMDLINE -h $server -P $port -Nse "SELECT viable_candidate, read_only, transactions_behind FROM sys.gr_member_routing_candidate_status" 2>>${ERR_FILE} | tail -1 2>>${ERR_FILE})
  echo "`date` Check server $hostgroup:$server:$port , status $stat , GR_STATUS $GR_STATUS, READONLY $READONLY, TRX_BEHIND $TRX_BEHIND" >> ${ERR_FILE}
  UPDATE_STATS_ONLINE_CMD="UPDATE mysql_servers SET status='ONLINE' WHERE hostgroup_id=$hostgroup AND hostname='$server' AND port='$port'; LOAD MYSQL SERVERS TO RUNTIME;"
  UPDATE_STATS_OFFLINE_SOFT_CMD="UPDATE mysql_servers SET status='OFFLINE_SOFT' WHERE hostgroup_id=$hostgroup AND hostname='$server' AND port='$port'; LOAD MYSQL SERVERS TO RUNTIME;"
  if [ "${GR_STATUS}" == "" -a "${READONLY}" == "" -a "${TRX_BEHIND}" == "" ] ; then
    # case : mysql server can not reach
    echo "`date` Changing server $hostgroup:$server:$port to status OFFLINE_SOFT" >> ${ERR_FILE}
    $PROXYSQL_CMDLINE "${UPDATE_STATS_OFFLINE_SOFT_CMD}" 2>> ${ERR_FILE}
  elif [ "${GR_STATUS}" == "YES" -a "${READONLY}" == "NO" -a "$stat" != "ONLINE" -a ${TRX_BEHIND} -le $max_replication_lag ] ; then
    echo "`date` Changing server $hostgroup:$server:$port to status ONLINE" >> ${ERR_FILE}
    $PROXYSQL_CMDLINE "${UPDATE_STATS_ONLINE_CMD}" 2>> ${ERR_FILE}
  elif [ "${GR_STATUS}" == "YES" -a "${READONLY}" == "YES" -a "$stat" != "ONLINE" -a "$hostgroup" == "$HOSTGROUP_READER_ID" -a ${TRX_BEHIND} -le $max_replication_lag ] ; then
    echo "`date` Changing server $hostgroup:$server:$port to status ONLINE" >> ${ERR_FILE}
    $PROXYSQL_CMDLINE "${UPDATE_STATS_ONLINE_CMD}" 2>> ${ERR_FILE}
  elif [ "${GR_STATUS}" == "NO" -o "${READONLY}" == "YES" -a "$stat" = "ONLINE" -a "$hostgroup" == "$HOSTGROUP_WRITER_ID" ] ; then
    echo "`date` Changing server $hostgroup:$server:$port to status OFFLINE_SOFT" >> ${ERR_FILE}
    $PROXYSQL_CMDLINE "${UPDATE_STATS_OFFLINE_SOFT_CMD}" 2>> ${ERR_FILE}
  elif [ "${GR_STATUS}" == "YES" -a "${READONLY}" == "NO" -a "$stat" = "ONLINE" -a ${TRX_BEHIND} -gt $max_replication_lag ] ; then
    echo "`date` Changing server $hostgroup:$server:$port to status OFFLINE_SOFT" >> ${ERR_FILE}
    $PROXYSQL_CMDLINE "${UPDATE_STATS_OFFLINE_SOFT_CMD}" 2>> ${ERR_FILE}
  fi
done

if [ $NUMBER_WRITERS -gt 0 ]
then
  CONT=0
  # Only check online servers
  $PROXYSQL_CMDLINE "SELECT hostname, port FROM mysql_servers WHERE hostgroup_id = $HOSTGROUP_WRITER_ID AND status = 'ONLINE' order by hostname, port" | while read server port
  do
    if [ $CONT -ge $NUMBER_WRITERS ]
    then
      # Number of writers reached, disabling extra servers
      echo "`date` Number of writers reached, disabling extra write server $HOSTGROUP_WRITER_ID:$server:$port to status OFFLINE_SOFT" >> ${ERR_FILE}
      $PROXYSQL_CMDLINE "UPDATE mysql_servers set status = 'OFFLINE_SOFT' WHERE hostgroup_id = $HOSTGROUP_WRITER_ID AND hostname = '$server' AND port = $port;" 2>> ${ERR_FILE}
    fi
    CONT=$(( $CONT + 1 ))
  done
fi

if [ $WRITER_IS_READER -eq 0 ]
then
  # Writer is not a read node, but only if we have another read node online
  READER_NON_WRITER=$($PROXYSQL_CMDLINE "SELECT count(*) FROM mysql_servers ms1 LEFT JOIN mysql_servers ms2 ON ms1.hostname = ms2.hostname AND ms1.port = ms2.port AND ms1.hostgroup_id <> ms2.hostgroup_id WHERE ms1.hostgroup_id = $HOSTGROUP_READER_ID AND ms1.status = 'ONLINE' AND (ms2.hostgroup_id = $HOSTGROUP_WRITER_ID OR ms2.hostgroup_id IS NULL) AND (ms2.status = 'OFFLINE_SOFT' OR ms2.hostgroup_id IS NULL);" 2>>${ERR_FILE})
  if [ $READER_NON_WRITER -gt 0 ]
  then
    $PROXYSQL_CMDLINE "SELECT hostname, port FROM mysql_servers WHERE hostgroup_id = $HOSTGROUP_WRITER_ID AND status = 'ONLINE' order by hostname, port" | while read server port
    do
      echo "`date` Disabling read for write server $HOSTGROUP_READER_ID:$server:$port to status OFFLINE_SOFT" >> ${ERR_FILE}
      $PROXYSQL_CMDLINE "UPDATE mysql_servers set status = 'OFFLINE_SOFT' WHERE hostgroup_id = $HOSTGROUP_READER_ID AND hostname = '$server' AND port = $port;" 2>> ${ERR_FILE}
    done
  else
    echo "`date` Not enough read servers, we won't disable read in write servers" >> ${ERR_FILE}
  fi
fi

echo "`date` Enabling config" >> ${ERR_FILE}
$PROXYSQL_CMDLINE "LOAD MYSQL SERVERS TO RUNTIME;" 2>> ${ERR_FILE}

exit 0
```

## 定期檢查 MySQL 伺服器的健康狀態-

```bash
#!/bin/bash

# Define variables
PROXYSQL_ADMIN_USER="admin"
PROXYSQL_ADMIN_PASSWORD="admin"
PROXYSQL_ADMIN_HOST="127.0.0.1"
PROXYSQL_ADMIN_PORT="6032"

# Health check for MySQL server
check_mysql_health() {
    # Get the list of MySQL servers from ProxySQL
    mysql_servers=$(mysql -h $PROXYSQL_ADMIN_HOST -P $PROXYSQL_ADMIN_PORT -u $PROXYSQL_ADMIN_USER -p$PROXYSQL_ADMIN_PASSWORD -N -e "SELECT hostgroup_id,hostname,port,status FROM mysql_servers;")

    # Loop through each MySQL server
    while read -r line; do
        hostgroup_id=$(echo $line | awk '{print $1}')
        hostname=$(echo $line | awk '{print $2}')
        port=$(echo $line | awk '{print $3}')
        status=$(echo $line | awk '{print $4}')

        # Check the health of the MySQL server
        if [ "$status" -eq 1 ]; then
            echo "MySQL server $hostname:$port in hostgroup $hostgroup_id is UP."
        else
            echo "MySQL server $hostname:$port in hostgroup $hostgroup_id is DOWN."
            # Perform additional actions if the server is down, such as restarting it
            # /path/to/restart_mysql.sh $hostname $port
        fi
    done <<< "$mysql_servers"
}

# Run the health check function
check_mysql_health
```

```bash
#!/bin/bash

# ProxySQL 管理員帳號和密碼
PROXYSQL_ADMIN_USER="admin"
PROXYSQL_ADMIN_PASSWORD="admin"
PROXYSQL_ADMIN_HOST="127.0.0.1"
PROXYSQL_ADMIN_PORT="6032"

# MySQL 伺服器健康檢查函數
check_mysql_health() {
    mysql_servers=$(mysql -h $PROXYSQL_ADMIN_HOST -P $PROXYSQL_ADMIN_PORT -u $PROXYSQL_ADMIN_USER -p$PROXYSQL_ADMIN_PASSWORD -N -e "SELECT hostgroup_id,hostname,port,status FROM mysql_servers;")

    while read -r line; do
        hostgroup_id=$(echo $line | awk '{print $1}')
        hostname=$(echo $line | awk '{print $2}')
        port=$(echo $line | awk '{print $3}')
        status=$(echo $line | awk '{print $4}')

        # 進行健康檢查
        if [ "$status" -eq 1 ]; then
            echo "MySQL server $hostname:$port in hostgroup $hostgroup_id is UP."
        else
            echo "MySQL server $hostname:$port in hostgroup $hostgroup_id is DOWN."
            # 執行更換伺服器狀態的操作
            change_mysql_server_status $hostgroup_id $hostname $port
        fi
    done <<< "$mysql_servers"
}

# 更換 MySQL 伺服器狀態函數
change_mysql_server_status() {
    hostgroup_id=$1
    hostname=$2
    port=$3

    # 在這裡可以執行更換伺服器狀態的操作，例如使用 ProxySQL 的 UPDATE 指令
    mysql -h $PROXYSQL_ADMIN_HOST -P $PROXYSQL_ADMIN_PORT -u $PROXYSQL_ADMIN_USER -p$PROXYSQL_ADMIN_PASSWORD -e "UPDATE mysql_servers SET status = 1 WHERE hostgroup_id = $hostgroup_id AND hostname = '$hostname' AND port = $port;"

    echo "Changed MySQL server status for $hostname:$port in hostgroup $hostgroup_id."
}

# 執行健康檢查
check_mysql_health
```

