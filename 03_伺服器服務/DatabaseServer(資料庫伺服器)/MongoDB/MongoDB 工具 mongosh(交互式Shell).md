# MongoDB 工具 mongosh(交互式Shell)

```
MongoDB官方提供的交互式Shell，用於與MongoDB數據庫進行互動和管理。
```

## 目錄

- [MongoDB 工具 mongosh(交互式Shell)](#mongodb-工具-mongosh交互式shell)
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

[Install mongosh](https://www.mongodb.com/docs/mongodb-shell/install/#install-mongosh)

[MongoDB Shell (mongosh) - 遠程部署測試工具](https://www.mongodb.com/docs/mongodb-shell/)

### 心得相關

# 安裝

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

創建 `/etc/yum.repos.d/mongodb-org-6.0.repo` 文件

```bash
vim /etc/yum.repos.d/mongodb-org-6.0.repo
```

填入以下內容這樣就可以直接使用 yum 安裝 mongosh

```repo
[mongodb-org-6.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/6.0/$basearch/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-6.0.asc
```

```bash
yum install -y mongodb-mongosh
```

`MongoDB 還提供了使用系統 OpenSSL 庫的 mongosh 版本`

```bash
yum install -y mongodb-mongosh-shared-openssl11
```

```bash
yum install -y mongodb-mongosh-shared-openssl3
```

## Docker 部署

```yml
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 指令

```bash
# 與mongodb連線
mongosh "mongodb://usernam:password@host_ip:port"

mongosh --host mongodb0.example.com --port 28015

# 要連接到遠程 MongoDB 實例並作為用戶 alice 對 admin 數據庫進行身份驗證：
mongosh "mongodb://mongodb0.example.com:28015" --username alice --authenticationDatabase admin
```
