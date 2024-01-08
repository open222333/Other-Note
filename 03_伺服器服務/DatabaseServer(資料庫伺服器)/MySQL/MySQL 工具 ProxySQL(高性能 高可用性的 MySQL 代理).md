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

## 目錄

- [MySQL 工具 ProxySQL(高性能 高可用性的 MySQL 代理)](#mysql-工具-proxysql高性能-高可用性的-mysql-代理)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Docker 部署](#docker-部署)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [服務操作](#服務操作)

## 參考資料

[官方網站](https://proxysql.com/)

[官方網站 Initial Configuration](https://proxysql.com/documentation/ProxySQL-Configuration/)

[官方 ProxySQL Docker Image](https://hub.docker.com/r/proxysql/proxysql)

[Admin Variables](https://proxysql.com/Documentation/global-variables/admin-variables/)

[MySQL Variables](https://proxysql.com/Documentation/global-variables/mysql-variables/)

[MySQL Monitor Variables](https://proxysql.com/Documentation/global-variables/mysql-monitor-variables/)

### 心得相關

[使用 ProxySQL 來簡化 MySQL 的讀寫分離](https://blog.yowko.com/proxysql/)

```
實作一主兩從的一個MySQL叢集和一個ProxySQL代理，ProxySQL代理MYSQL叢集的資料請求並且進行讀寫分離。
```

[用Docker实现MySQL ProxySQL读写分离](https://blog.breezelin.cn/practice-mysql-proxysql-docker-compose.html)

# 安裝

## Debian (Ubuntu)

```bash
# 更新套件列表
apt-get update

# 安裝 ProxySQL
apt-get install -y proxysql
```

## RedHat (CentOS)

```bash
# 安裝 EPEL 存儲庫
yum install epel-release

# 更新套件列表
yum update

# 安裝 ProxySQL
yum install -y proxysql
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

# 指令

`使用 MySQL 客戶端連接到 ProxySQL`

```bash
mysql -h <proxySQL_host> -P <proxySQL_port> -u <mysql_username> -p
```

<proxySQL_host>: 你的 ProxySQL 主機的 IP 地址或主機名。

<proxySQL_port>: ProxySQL 監聽的端口，通常是 6033，但根據你的配置可能有所不同。

<mysql_username>: MySQL 用戶名。

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
