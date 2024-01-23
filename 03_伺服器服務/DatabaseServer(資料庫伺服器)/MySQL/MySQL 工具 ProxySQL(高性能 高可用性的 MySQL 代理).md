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
    - [SQL 語句 (轉址)](#sql-語句-轉址)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Docker 部署](#docker-部署)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [進行基本設定](#進行基本設定)
  - [服務操作](#服務操作)
  - [透過 ProxySQL 連接到已設定的 MySQL 伺服器](#透過-proxysql-連接到已設定的-mysql-伺服器)
  - [ProxySQL 操作](#proxysql-操作)
    - [使用者](#使用者)
    - [MySQL 伺服器](#mysql-伺服器)
    - [設定路由規則](#設定路由規則)
    - [應用配置](#應用配置)
    - [設定](#設定)
  - [基本步驟 - 透過 ProxySQL 連線 MySQL](#基本步驟---透過-proxysql-連線-mysql)
  - [高可用步驟](#高可用步驟)
    - [群組](#群組)
    - [路由](#路由)
    - [監控 (高可用)](#監控-高可用)
    - [修改伺服器的狀態](#修改伺服器的狀態)
- [例外狀況](#例外狀況)
  - [Can't connect to local MySQL server through socket '/var/lib/mysql/mysql. sock' (2)](#cant-connect-to-local-mysql-server-through-socket-varlibmysqlmysql-sock-2)
- [高可用 說明](#高可用-說明)
- [腳本](#腳本)
  - [gr\_sw\_mode\_checker.sh](#gr_sw_mode_checkersh)
  - [gr\_mw\_mode\_sw\_cheker.sh](#gr_mw_mode_sw_chekersh)
  - [proxysql\_groupreplication\_checker.sh](#proxysql_groupreplication_checkersh)

## 參考資料

[官方網站](https://proxysql.com/)

[官方網站 Initial Configuration](https://proxysql.com/documentation/ProxySQL-Configuration/)

[官方 ProxySQL Docker Image](https://hub.docker.com/r/proxysql/proxysql)

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

[骏马金龙](https://www.cnblogs.com/f-ck-need-u/p/9300829.html#1%E5%85%B3%E4%BA%8Eproxysql%E8%B7%AF%E7%94%B1%E7%9A%84%E7%AE%80%E8%BF%B0)

[MySQL中间件：ProxySQL](https://www.cnblogs.com/f-ck-need-u/p/7586194.html#middleware)

[MySQL/MariaDB系列文章目录](https://www.cnblogs.com/f-ck-need-u/p/7586194.html#middleware)

[ProxySQL配置与高可用](https://www.yoyoask.com/?p=3560)

[CentOS 7.6配置MySQL 5.7 MGR单主高可用+ProxySQL实现读写分离和故障转移](https://blog.51cto.com/qiuyue/2413300?source=drh)

[MySQL高可用架构MHA+ProxySQL实现读写分离和负载均衡](https://bbs.huaweicloud.com/blogs/344705)

### percona 相關

[ProxySQL、proxysql-admin 和 percona-scheduler-admin 文檔](https://docs.percona.com/proxysql/index.html)

### 例外狀況相關

[Can't connect to local MySQL server through socket '/var/lib/mysql/mysql. sock' (2)](https://github.com/xinity/pxc_swarm/issues/2)

[Ubuntu 18.04 - Fresh ProxySQL install - no mysqld.sock port found](https://github.com/sysown/proxysql/issues/2135)

### SQL 語句 (轉址)

[基于SQL语句路由](https://www.cnblogs.com/f-ck-need-u/p/9300829.html#6%E5%9F%BA%E4%BA%8Esql%E8%AF%AD%E5%8F%A5%E8%B7%AF%E7%94%B1)

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

# 指令

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
mysql -h127.0.0.1 -P6032 -uadmin -p
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

## 透過 ProxySQL 連接到已設定的 MySQL 伺服器

ProxySQL 的管理端口（6032）

ProxySQL 的 MySQL 連接端口（通常是 6033）

```bash
mysql -u your_username -pyour_password -h 127.0.0.1 -P 6033 --prompt='MySQL> '
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

### 設定路由規則

`設定路由`

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

## 基本步驟 - 透過 ProxySQL 連線 MySQL

`MySQL 新增使用者`

```sql
CREATE USER 'proxysql'@'%' IDENTIFIED BY 'proxysqlpassword';
GRANT ALL PRIVILEGES ON *.* TO 'proxysql'@'%';
FLUSH PRIVILEGES;
```

`ProxySQL 新增使用者`

```sql
INSERT INTO mysql_users (username, password, active, default_hostgroup)
VALUES ('proxysql', 'proxysqlpassword', 1, 1);
LOAD MYSQL USERS TO RUNTIME;
SAVE MYSQL USERS TO DISK;
```

## 高可用步驟

`使用 ProxySQL 管理用戶登入到 ProxySQL 控制台`

```bash
mysql -u admin -p -h 127.0.0.1 -P 6032
```

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
LOAD MYSQL HOSTGROUPS TO RUNTIME;
SAVE MYSQL HOSTGROUPS TO DISK;
```

### 路由

`添加 Query Rules`

```sql
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (1, 1, '^SELECT.*FOR UPDATE$', 1, 1);
INSERT INTO mysql_query_rules (rule_id, active, match_pattern, destination_hostgroup, apply)
VALUES (2, 1, '^SELECT', 2, 1);
```

`重新載入設定`

```sql
LOAD MYSQL QUERY RULES TO RUNTIME;
SAVE MYSQL QUERY RULES TO DISK;
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
SELECT * FROM monitor.mysql_server_connect_log
ORDER BY time_start_us
DESC LIMIT 6;
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
VALUES(1,5000,'/var/lib/proxysql/gr_sw_mode_checker.sh',1,2,1,'/var/lib/proxysql/gr_sw_mode_checker.log');
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

# 例外狀況

## Can't connect to local MySQL server through socket '/var/lib/mysql/mysql. sock' (2)

```bash
mysql -h127.0.0.1 -P6032 -uadmin -p
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
