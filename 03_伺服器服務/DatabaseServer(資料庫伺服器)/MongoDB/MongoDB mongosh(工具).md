# MongoDB mongosh(工具)

```
MongoDB Shell mongosh 是一個功能齊全的 JavaScript 和 Node.js 16.x REPL 環境，用於與 MongoDB 部署進行交互。
您可以使用 MongoDB Shell 直接使用您的數據庫測試查詢和操作。
```

- [MongoDB mongosh(工具)](#mongodb-mongosh工具)
	- [參考資料](#參考資料)
- [安裝步驟 CentOS7](#安裝步驟-centos7)
- [指令](#指令)

## 參考資料

[MongoDB Shell (mongosh) - 遠程部署測試工具](https://www.mongodb.com/docs/mongodb-shell/)

# 安裝步驟 CentOS7

```bash
# https://www.mongodb.com/docs/mongodb-shell/install/#std-label-mdb-shell-install
# 創建 /etc/yum.repos.d/mongodb-org-6.0.repo 文件 填入以下內容
# 這樣就可以直接使用yum安裝mongosh
vim /etc/yum.repos.d/mongodb-org-6.0.repo

[mongodb-org-6.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/6.0/$basearch/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-6.0.asc

# 安裝
yum install -y mongodb-mongosh

# MongoDB 還提供了使用系統 OpenSSL 庫的 mongosh 版本。
yum install -y mongodb-mongosh-shared-openssl11

yum install -y mongodb-mongosh-shared-openssl3

```

# 指令

```bash
# 與mongodb連線
mongosh "mongodb://usernam:password@host_ip:port"

mongosh --host mongodb0.example.com --port 28015

# 要連接到遠程 MongoDB 實例並作為用戶 alice 對 admin 數據庫進行身份驗證：
mongosh "mongodb://mongodb0.example.com:28015" --username alice --authenticationDatabase admin
```
