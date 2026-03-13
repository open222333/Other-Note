# MySQL 工具 MySQL Shell(交互式的命令行工具)

```
MySQL Shell 是 MySQL 提供的一個交互式的命令行工具，它具有強大的功能，並支援多種 MySQL 相關的操作。以下是 MySQL Shell 的一些特點和功能：

多語言支援： MySQL Shell 支援多種編程語言，包括 JavaScript、Python 和 SQL。這意味著你可以使用不同的語言來編寫和執行 MySQL 相關的操作。

交互式操作： MySQL Shell 提供一個交互式的命令行界面，讓你能夠直接與 MySQL 數據庫進行互動。你可以執行 SQL 查詢、管理數據庫對象，以及執行各種 MySQL Shell 的內建命令。

JSON 支援： MySQL Shell 具有強大的 JSON 支援，允許你使用 JSON 格式來處理數據。這對於與 NoSQL 數據庫或應用程序進行交互非常有用。

高可用性和集群管理： MySQL Shell 允許你輕鬆地管理 MySQL InnoDB Cluster 和 Group Replication。你可以使用 Shell 來設置、監控和管理高可用性集群。

自動完成和歷史記錄： MySQL Shell 提供自動完成功能，以幫助你更快地輸入命令。它還保留歷史記錄，讓你能夠輕鬆檢查之前執行的命令。

腳本執行： 你可以將 SQL 腳本和其他語言的腳本直接在 MySQL Shell 中執行，這對於自動化任務和批處理操作非常方便。

插件支援： MySQL Shell 支援插件，這擴展了其功能。你可以根據需要安裝和使用不同的插件，以滿足特定的需求。


```

## 目錄

- [MySQL 工具 MySQL Shell(交互式的命令行工具)](#mysql-工具-mysql-shell交互式的命令行工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [安裝](#安裝)
  - [MacOS](#macos)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Docker 部署](#docker-部署)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)

## 參考資料

[MySQL Shell 下載頁面](https://dev.mysql.com/downloads/shell/)

[MySQL Shell 命令](https://dev.mysql.com/doc/mysql-shell/8.0/en/mysql-shell-commands.html)

[MySQL AdminAPI - 管理 MySQL 實例，使用它們創建 InnoDB Cluster、InnoDB ClusterSet 和 InnoDB ReplicaSet 部署，以及集成 MySQL Router](https://dev.mysql.com/doc/mysql-shell/8.0/en/admin-api-userguide.html)

[MySQL Shell API 8.0.33](https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/group___admin_a_p_i.html)

### 心得相關

[朝花夕拾16章MySQL Shell 使用 MySQL Shell 命令](https://www.modb.pro/db/638407)

[技术分享 | mysqlsh 命令行模式 & 密码保存](https://cloud.tencent.com/developer/article/1782068)

# 安裝

## MacOS

```bash
brew install mysql-shell
```

## Debian (Ubuntu)

```bash
wget https://dev.mysql.com/get/mysql-apt-config_0.8.17-1_all.deb
dpkg -i mysql-apt-config_0.8.17-1_all.deb
apt update
apt install mysql-shell
```

## RedHat (CentOS)

```bash
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
# 查看版本
mysqlsh --version

# 啟動 MySQL Shell，並連接到 MySQL 數據庫，執行各種管理和查詢任務
mysqlsh -u username -h hostname -p

# 重啟mgr集群
mysqlsh --uri root@node_1:3306
```

```
\help（\h 或 \?）：打印有關 MySQL Shell 的幫助，或搜索在線幫助。
\quit（\q 或 \exit）：退出 MySQL Shell。
\：在 SQL 模式下，開始多行模式。當輸入空行時，代碼會被緩存並執行。
\status（\s）：顯示當前 MySQL Shell 狀態。
\js：將執行模式切換為 JavaScript。
\py：將執行模式切換為 Python。
\sql：將執行模式切換為 SQL。
\connect（\c）：連接到 MySQL 實例。
\reconnect：重新連接到同一個 MySQL 實例。
\disconnect：斷開全局會話。
\use（\u）：指定要使用的架構。
\source（\. 或 source，無反斜杠）：使用活動語言執行腳本文件。
\warnings（\W）：顯示語句生成的任何警告。
\nowarnings（\w）：不顯示語句生成的任何警告。
\history：查看和編輯命令行歷史記錄。
\rehash：手動更新自動完成名稱緩存。
\option：查詢和更改 MySQL Shell 配置選項。
\show：使用提供的選項和參數運行指定的報告。
\watch：使用提供的選項和參數運行指定的報告，並定期刷新結果。
\edit（\e）：在默認系統編輯器中打開命令，然後將其顯示在 MySQL Shell 中。
\pager（\P）：配置 MySQL Shell 用於顯示文本的分頁器。
\nopager：禁用 MySQL Shell 配置使用的任何分頁器。
\system（\!）：運行指定的操作系統命令並在 MySQL Shell 中顯示結果。
```

```JavaScript
// 執行數據庫管理操作時控制輸出的詳細信息級別
// dba.verbose=0：只顯示最基本的輸出信息，適用於只關注操作結果的情況。
// dba.verbose=1：顯示較詳細的輸出信息，包括操作的進度和狀態。適用於需要了解操作進展情況的情況。
// dba.verbose=2：顯示最詳細的輸出信息，包括操作的詳細過程、進度、狀態和日誌。適用於需要深入了解操作細節和可能的問題排查的情況。
```
