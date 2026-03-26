# MySQL 工具 ProxySQL Admin(管理 ProxySQL 的 Web 界面工具)

```
用於管理 ProxySQL 的 Web 界面工具
```

## 目錄

- [MySQL 工具 ProxySQL Admin(管理 ProxySQL 的 Web 界面工具)](#mysql-工具-proxysql-admin管理-proxysql-的-web-界面工具)
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

## 參考資料

### 心得相關

### 相關筆記

[MySQL 筆記（主）](./MySQL_筆記.md)
[MySQL 筆記 - Replication 主從](./MySQL_筆記_Replication(Master-Slave_主從).md)
[MySQL 筆記 - InnoDB Cluster 叢集](./MySQL_筆記_Cluster(叢集).md)
[MySQL 工具 - ProxySQL](./MySQL_工具_ProxySQL(高性能_高可用性的_MySQL_代理).md)
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
```

## RedHat (CentOS)

```bash
```

## Docker 部署

```yml
version: '3'

services:
  proxysql-admin:
    image: renatomefi/proxysql-admin:latest
    environment:
      - PROXYSQL_HOST=proxysql
      - PROXYSQL_PORT=6032
      - PROXYSQL_USER=admin
      - PROXYSQL_PASSWORD=admin
    ports:
      - "8080:80"
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 指令
