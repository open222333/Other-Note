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
