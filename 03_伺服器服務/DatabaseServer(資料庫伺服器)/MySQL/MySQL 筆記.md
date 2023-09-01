# MySQL 筆記

```
RDBMS

關聯資料庫是一組資訊，以預先定義的關係彙整資料，可將資料儲存在一或多個資料表 (或「關係」) 的資料欄或資料列中，以便輕鬆查看及瞭解各種不同資料結構之間有何關聯。

「關係」是不同資料表之間的邏輯連結，是根據這些資料表之間的互動來建立。
```

## 目錄

- [MySQL 筆記](#mysql-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [MySQL Shell 相關](#mysql-shell-相關)
    - [MySQL Router 相關](#mysql-router-相關)
    - [使用者權限相關](#使用者權限相關)
    - [安裝相關](#安裝相關)
    - [Master-Slave(主從環境)相關](#master-slave主從環境相關)
    - [叢集(Cluster)相關](#叢集cluster相關)
      - [InnoDB Cluster](#innodb-cluster)
      - [NDB Cluster](#ndb-cluster)
    - [操作相關](#操作相關)
    - [備份相關](#備份相關)
      - [備份指令相關](#備份指令相關)
      - [解說相關](#解說相關)
      - [Percona XtraBackup(備份工具)相關](#percona-xtrabackup備份工具相關)
        - [XtraBackup 心得相關](#xtrabackup-心得相關)
    - [錯誤處理相關](#錯誤處理相關)
      - [InnoDB Cluster 錯誤](#innodb-cluster-錯誤)
- [安裝步驟](#安裝步驟)
  - [配置文檔](#配置文檔)
  - [MacOS](#macos)
  - [CentOS7](#centos7)
  - [安裝 NDB Cluster](#安裝-ndb-cluster)
  - [安裝 MySQL Router](#安裝-mysql-router)
    - [MySQL Router 配置文檔](#mysql-router-配置文檔)
  - [安装 MySQL Shell](#安装-mysql-shell)
- [指令](#指令)
  - [MySQL Router 指令](#mysql-router-指令)
  - [MySQL Shell 指令](#mysql-shell-指令)
    - [innodb Cluster 相關](#innodb-cluster-相關)
  - [SQL 指令](#sql-指令)
    - [使用者相關](#使用者相關)
    - [密碼設定強度修改](#密碼設定強度修改)
    - [許可權 列表](#許可權-列表)
    - [innodb cluster相關](#innodb-cluster相關)
  - [服務操作](#服務操作)
  - [匯出匯入](#匯出匯入)
    - [匯出 - mysqldump](#匯出---mysqldump)
    - [匯入](#匯入)
  - [mysqlbinlog - 檢查主資料庫中的二進制日誌（Binary Log）](#mysqlbinlog---檢查主資料庫中的二進制日誌binary-log)
  - [ndb Cluster 相關](#ndb-cluster-相關)
- [Master-Slave 主從架構架設](#master-slave-主從架構架設)
  - [mysql-master設定](#mysql-master設定)
  - [mysql-slave設定](#mysql-slave設定)
  - [備份mysql-master](#備份mysql-master)
  - [恢復備份到mysql-slave](#恢復備份到mysql-slave)
- [Cluster 叢集架設](#cluster-叢集架設)
  - [說明](#說明)
    - [GTID 模式](#gtid-模式)
  - [NDB Cluster 實作](#ndb-cluster-實作)
    - [Linux](#linux)
      - [Manage node (管理節點) - 負責監控叢集所有 Nodes 的狀態，並且由此控制所有 Nodes 的替換。](#manage-node-管理節點---負責監控叢集所有-nodes-的狀態並且由此控制所有-nodes-的替換)
      - [Data node - 負責所有 SQL Data 的 Nodes，單純儲存資料，將資料寫在 RAM \& Disk。](#data-node---負責所有-sql-data-的-nodes單純儲存資料將資料寫在-ram--disk)
      - [SQL node (原本的 MySQL Server) - 負責 SQL 的 Table schema 和 Client 連接的空間。](#sql-node-原本的-mysql-server---負責-sql-的-table-schema-和-client-連接的空間)
  - [InnoDB Cluster 實作](#innodb-cluster-實作)
    - [MySQL 5.7](#mysql-57)
        - [優化配置](#優化配置)
    - [使用程式腳本建立 InnoDB Cluster](#使用程式腳本建立-innodb-cluster)
      - [JavaScript](#javascript)
      - [Python](#python)
- [例外狀況](#例外狀況)
  - [has the following errant GTIDs that do not exist in the cluster](#has-the-following-errant-gtids-that-do-not-exist-in-the-cluster)
  - [修復 master slave 最快速方法](#修復-master-slave-最快速方法)
  - [修復 master slave Slave\_SQL\_Running: No, Slave\_IO\_Running: No 解決方案](#修復-master-slave-slave_sql_running-no-slave_io_running-no-解決方案)
  - [修復損壞的innodb：innodb\_force\_recovery](#修復損壞的innodbinnodb_force_recovery)
  - [\[Warning\] IP address 'xxx.xxx.xxx.xxx' could not be resolved- Name or service not known](#warning-ip-address-xxxxxxxxxxxx-could-not-be-resolved--name-or-service-not-known)
  - [Table 'db.table' doesn't exist (1146)](#table-dbtable-doesnt-exist-1146)
  - [升級為GTID模式](#升級為gtid模式)
  - [ERROR 1872 (HY000): Slave failed to initialize relay log info structure from the repository](#error-1872-hy000-slave-failed-to-initialize-relay-log-info-structure-from-the-repository)
  - [ERROR 3009 (HY000): Column count of mysql.user is wrong. Expected 45, found 43. Created with MySQL 50739, now running 50742. Please use mysql\_upgrade to fix this error.](#error-3009-hy000-column-count-of-mysqluser-is-wrong-expected-45-found-43-created-with-mysql-50739-now-running-50742-please-use-mysql_upgrade-to-fix-this-error)

## 參考資料

[MySQl官方網站](https://dev.mysql.com/)

[MySQL 全部文檔](https://dev.mysql.com/doc/)

[MySQL 8.0版本 指令](https://dev.mysql.com/doc/refman/8.0/en/mysql-command-options.html)

[MySQL 8.0版本 文檔](https://dev.mysql.com/doc/refman/8.0/en/)

[MySQL 5.7版本 文檔](https://dev.mysql.com/doc/refman/5.7/en/)

[MySQL 教程](https://www.itread01.com/study/mysql-tutorial.html)

[MySQL Community Downloads - MySQL 社區下載](https://dev.mysql.com/downloads/)

### MySQL Shell 相關

[MySQL Shell 下載頁面](https://dev.mysql.com/downloads/shell/)

[mysql-shell 指令](https://dev.mysql.com/doc/mysql-shell/8.0/en/mysql-shell-commands.html)

### MySQL Router 相關

```
MySQL Router 的一些主要用法和特點：

高可用性： MySQL Router 可以在數個 MySQL 服務器之間進行負載均衡和數據路由，以實現高可用性。它可以檢測到服務器的可用性並自動將流量重定向到可用的服務器。

負載均衡： MySQL Router 可以根據配置的負載均衡策略將流量分發到多個 MySQL 服務器上，以確保各個服務器的負載分配均衡。

讀寫分離： MySQL Router 支持讀寫分離，可以將讀取請求路由到一個或多個讀取實例，同時將寫入請求路由到主要寫入實例。這有助於提高讀取效能和分擔主要寫入實例的負擔。

自動故障切換： MySQL Router 可以自動檢測數據庫服務器的故障，並將流量重定向到可用的服務器，以確保應用程序的連接不受影響。

SSL 支持： MySQL Router 支持 SSL 加密，可以保護數據在客戶端和服務器之間的傳輸。

動態配置： MySQL Router 可以通過配置文件進行動態配置，您可以定義路由規則、服務器組和讀寫分離設置。
```

[MySQL Router 8.0 - 官方文件](https://dev.mysql.com/doc/mysql-router/8.0/en/)

[MySQL Community Downloads - MySQL Router](https://dev.mysql.com/downloads/router/)

[Chapter 2 Installing MySQL Router - 安裝](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-installation.html)

[2.1 Installing MySQL Router on Linux](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-installation-linux.html)

[4.1 Configuration File Syntax - 配置文件語法](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-syntax.html)

[4.3.3 Configuration File Options - 配置文檔選項](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-conf-options.html)

[4.3.4 Configuration File Example - 配置文檔範例](https://dev.mysql.com/doc/mysql-router/8.0/en/mysql-router-configuration-file-example.html)

[Oracle官方轻量级中间件MySQL Router介绍与性能测试](https://www.modb.pro/db/77315)

[Ubuntu20.04安装MySQL Router](http://www.884358.com/ubuntu-install-mysql-router/)

### 使用者權限相關

[MySQL / MariaDB 移除使用者帳號及權限](https://ithelp.ithome.com.tw/articles/10235980)

[權限說明](https://rosalie1211.blogspot.com/2019/03/mysql.html)

[淺談MySQL中授權(grant)和撤銷授權(revoke)用法詳解](https://www.itread01.com/articles/1476680778.html)

[只談MySQL (第四天) 帳號與權限](https://ithelp.ithome.com.tw/articles/10029835)

[6.2.2 Privileges Provided by MySQL](https://dev.mysql.com/doc/refman/8.0/en/privileges-provided.html#priv_all)

### 安裝相關

[CENTOS 7 安裝mysql](https://kirby86a.pixnet.net/blog/post/118006518-centos-7-%E5%AE%89%E8%A3%9Dmysql)

[在CentOS7上安裝MySQL5.7](https://dotblogs.com.tw/tinggg01/2018/07/06/153413)

[Unknown table 'COLUMN_STATISTICS' in information_schema (1109)](https://serverfault.com/questions/912162/mysqldump-throws-unknown-table-column-statistics-in-information-schema-1109)

[mysql 優化技巧心得一(key_buffer_size設定)](https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/410436/)

### Master-Slave(主從環境)相關

[MySQL如何不停机维护主从同步](https://zhuanlan.zhihu.com/p/472339202)

[MySQL Replication 主從式架構設定教學](https://blog.toright.com/posts/5062/mysql-replication-%E4%B8%BB%E5%BE%9E%E5%BC%8F%E6%9E%B6%E6%A7%8B%E8%A8%AD%E5%AE%9A%E6%95%99%E5%AD%B8.html)

[docker-compose搭建mysql主從環境](https://www.uj5u.com/ruanti/275444.html)

[docker-compose搭建mysql主從環境](https://hub.docker.com/r/bitnami/mysql)

[Slave_IO_Running Slave_SQL_Running 排錯](https://www.cnblogs.com/l-hh/p/9922548.html#_label0)

[mysql系列（一）—— 细说show slave status参数详解（最全）](https://blog.51cto.com/zhengmingjing/1910565)

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

#### NDB Cluster

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

### 操作相關

[MySQL列出所有表](https://www.yiibai.com/mysql/show-tables.html)

[MySQL 在线开启/关闭GTID](http://blog.itpub.net/31429259/viewspace-2643665/)

### 備份相關

[完全備份、增量備份與差異備份之間的差別](https://www.ubackup.com/tw/features/incremental-differential-backup.html)

#### 備份指令相關

[mysqlimport](https://dev.mysql.com/doc/refman/8.0/en/mysqlimport.html)

[mysql匯入匯出sql檔案](https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/38165/)

[mysql匯入匯出sql檔案 - workbench操作步驟](https://blog.hungwin.com.tw/mysql-workbench-backup/#i-8)

[mysql 导出表结构和数据](https://www.jianshu.com/p/e2410e9e8571)

#### 解說相關

[恢复mysql数据结构（.frm）和数据（.ibd）](https://cloud.tencent.com/developer/article/2069047?from=15425&areaSource=102001.3&traceId=tgUxumfIzi91Zj2x4aECD)

#### Percona XtraBackup(備份工具)相關

[percona 官網](https://www.percona.com/software/documentation)

[xtrabackup 選項參考](https://docs.percona.com/percona-xtrabackup/2.4/xtrabackup_bin/xbk_option_reference.html)

[innobackupex 選項參考](https://docs.percona.com/percona-xtrabackup/2.4/innobackupex/innobackupex_option_reference.html)

##### XtraBackup 心得相關

[mysql数据库备份：Xtrabackup - ](https://zhuanlan.zhihu.com/p/503948465)

```
xtrabackup：是用於熱備份innodb, xtradb表中數據的工具，不能備份其他類型的表，也不能備份數據表結構
innobackupex：是將xtrabackup進行封裝的perl腳本，可以備份和恢復MyISAM表以及數據表結構
```

[xtrabackup backup and restore mysql database](https://pankajconnect.medium.com/xtrabackup-backup-and-restore-mysql-database-221300bd8fef)

[在 Red Hat Enterprise Linux 和 CentOS 上安裝 Percona XtraBackup](https://docs.percona.com/percona-xtrabackup/2.4/installation/yum_repo.html#whats-in-each-rpm-package)

[Running Percona XtraBackup in a Docker container](https://www.percona.com/doc/percona-xtrabackup/2.4/installation/docker.html)

[bitnami/percona-xtrabackup](https://hub.docker.com/r/bitnami/percona-xtrabackup/)

[Xtrabackup介紹](https://www.itread01.com/content/1547450246.html)

[xtrabackup - 手冊頁](https://www.mankier.com/1/xtrabackup#)

[innobackupex參數說明](https://www.cnblogs.com/weiyiming007/p/10282593.html)

[xtrabackup 使用說明](https://www.cnblogs.com/linhaifeng/articles/15021166.html)

[【MYSQL】Percona XtraBackup 備份指令與還原](https://rosalie1211.blogspot.com/2019/04/mysqlpercona-xtrabackup.html)

[MySQL Percona innobackupex 和 XtraBackup 有何不同？](https://blog.longwin.com.tw/2022/09/mysql-percona-innobackupex-xtrabackup-different-2022/)

[Can I backup remote databases from my local server - 我可以從本地服務器備份遠程數據庫嗎](https://forums.percona.com/t/can-i-backup-remote-databases-from-my-local-server/2334/3)

[dijeesh-mysql_remote_xtrabackup Github](https://github.com/dijeesh/mysql_remote_xtrabackup/blob/master/mysql_remote_stream_backup.sh)

[如何將 mysql xtrabackup 帶到遠程服務器](https://stackoverflow.com/questions/71567854/how-to-take-mysql-xtrabackup-to-a-remote-server)

[MySQL8.0 使用Xtrabackup对数据库进行部分备份恢复](https://www.modb.pro/db/448714)

### 錯誤處理相關

[MySQL主从复制，启动slave时报错Slave failed to initialize relay log info structure from the repository](https://blog.csdn.net/weixin_37998647/article/details/79950133)

[解决Mysql复制Relay log read failure 的问题](https://blog.51cto.com/wuwei5460/1552798)

[MySQL relay log corrupted, how do I fix it? Tried but failed](https://dba.stackexchange.com/questions/53893/mysql-relay-log-corrupted-how-do-i-fix-it-tried-but-failed)

[MySQL Replication 遇到 Got fatal error 1236 from master 修復](https://blog.longwin.com.tw/2013/09/mysql-replication-error-1236-fix-2013/)

[MySQL主从复制，启动slave时报错1872 Slave failed to initialize relay log info structure from the repository](https://blog.51cto.com/u_15127597/4309432)

[[MySQL] SQL_ERROR 1032解决办法](https://www.cnblogs.com/langdashu/p/5920436.html)

[MySQL warning "IP address could not be resolved"](https://serverfault.com/questions/393862/mysql-warning-ip-address-could-not-be-resolved)

[Is DNS the Achilles heel in your MySQL installation?](https://www.percona.com/blog/2008/05/31/dns-achilles-heel-mysql-installation/)

[MySQL崩潰-如何修復損壞的innodb：innodb_force_recovery](https://www.twblogs.net/a/5b8201762b71772165af295d)

[解决 MySQL 报错 “ Column count of mysql.user is wrong...”](https://cloud.tencent.com/developer/article/1662598)

#### InnoDB Cluster 錯誤

[Multi Source Replication MySQL 5.6 to 5.7 GTID Auto Position Issues](https://stackoverflow.com/questions/30606345/multi-source-replication-mysql-5-6-to-5-7-gtid-auto-position-issues)

# 安裝步驟

## 配置文檔

```
設定檔位置:
全局配置(Global options)
/etc/my.cnf
/etc/mysql/my.cnf
SYSCONFDIR/my.cnf

log文檔預設位置
# CentOS 7
cat /var/log/mysqld.log

服務器特定(Server-specific options)
$MYSQL_HOME/my.cnf

使用指定文檔
defaults-extra-file
	--defaults-extra-file
	https://dev.mysql.com/doc/refman/5.7/en/option-file-options.html#option_general_defaults-extra-file

用戶特定選項
~/.my.cnf

客戶端特定的登錄路徑選項
~/.mylogin.cnf
```

```conf
; 全域變數說明：
[mysqld]
port = 3306
serverid = 1
socket = /tmp/mysql.sock
# 告訴系統不要執行 SSL/TLS 驗證，而是允許非加密的通信。
skip_ssl
# 指示資料庫在執行某些查詢時是否應該跳過對數據行的鎖定操作。鎖定操作是用來確保多個同時執行的事務不會相互干擾，從而確保數據的完整性和一致性。
skip-locking
# 避免MySQL的外部鎖定，減少出錯幾率增強穩定性。
skip-name-resolve
# 登錄時跳過權限檢查
skip-grant-tables
# 禁止MySQL對外部連接進行DNS解析，使用這一選項可以消除MySQL進行DNS解析的時間。
# 但需要註意，如果開啟該選項，則所有遠程主機連接授權都要使用IP地址方式，否則MySQL將無法正常處理連接請求！
back_log = 384
# back_log參數的值指出在MySQL暫時停止響應新請求之前的短時間內多少個請求可以被存在堆棧中。
# 如果系統在一個短時間內有很多連接，則需要增大該參數的值，該參數值指定到來的TCP/IP連接的偵聽隊列的大小。
# 不同的操作系統在這個隊列大小上有它自己的限制。 試圖設定back_log高於你的操作系統的限制將是無效的。
# 默認值為50。
# 對於Linux系統推薦設置為小於512的整數。
key_buffer_size = 256M
# key_buffer_size指定用於索引的緩沖區大小，增加它可得到更好的索引處理性能。
# 對於內存在4GB左右的服務器該參數可設置為256M或384M。
# 註意：該參數值設置的過大反而會是服務器整體效率降低！
max_allowed_packet = 4M
thread_stack = 256K
table_cache = 128K
sort_buffer_size = 6M
# 查詢排序時所能使用的緩沖區大小。
# 註意：該參數對應的分配內存是每連接獨占，如果有100個連接，那麽實際分配的總共排序緩沖區大小為100 × 6 ＝ 600MB。
# 所以，對於內存在4GB左右的服務器推薦設置為6-8M。
read_buffer_size = 4M
# 讀查詢操作所能使用的緩沖區大小。
# 和sort_buffer_size一樣，該參數對應的分配內存也是每連接獨享。
join_buffer_size = 8M
# 聯合查詢操作所能使用的緩沖區大小，和sort_buffer_size一樣，該參數對應的分配內存也是每連接獨享。
myisam_sort_buffer_size = 64M
table_cache = 512
thread_cache_size = 64
query_cache_size = 64M
# 指定MySQL查詢緩沖區的大小。
# 可以通過在MySQL控制臺觀察，
# 如果Qcache_lowmem_prunes的值非常大，則表明經常出現緩沖不夠的情況
# 如果Qcache_hits的值非常大，則表明查詢緩沖使用非常頻繁，如果該值較小反而會影響效率，那麽可以考慮不用查詢緩沖
# Qcache_free_blocks，如果該值非常大，則表明緩沖區中碎片很多。
tmp_table_size = 256M
max_connections = 768
# 指定MySQL允許的最大連接進程數。
# 如果在訪問時經常出現Too Many Connections的錯誤提示，則需要增大該參數值。
max_connect_errors = 10000000
wait_timeout = 10
# 指定一個請求的最大連接時間，對於4GB左右內存的服務器可以設置為5-10。
thread_concurrency = 8
# 該參數取值為服務器邏輯CPU數量*2，在本例中，服務器有2顆物理CPU，而每顆物理CPU又支持H.T超線程，所以實際取值為4*2=8
skip-networking
# 開啟該選項可以徹底關閉MySQL的TCP/IP連接方式，如果WEB服務器是以遠程連接的方式訪問MySQL數據庫服務器則不要開啟該選項！否則將無法正常連接！
table_cache=1024
# 物理內存越大,設置就越大.默認為2402,調到512-1024最佳
innodb_additional_mem_pool_size=4M
# 默認為2M
innodb_flush_log_at_trx_commit=1
# 設置為0就是等到innodb_log_buffer_size列隊滿後再統一儲存,默認為1
innodb_log_buffer_size=2M
# 默認為1M
innodb_thread_concurrency=8
# 你的服務器CPU有幾個就設置為幾,建議用默認一般為8
key_buffer_size=256M
# 默認為218，調到128最佳
tmp_table_size=64M
# 默認為16M，調到64-256最掛
read_buffer_size=4M
# 默認為64K
read_rnd_buffer_size=16M
# 默認為256K
sort_buffer_size=32M
# 默認為256K
thread_cache_size=120
# 默認為60
query_cache_size=32M

; 5.7版本設置變量
; https://dev.mysql.com/doc/refman/5.7/en/program-variables.html
[mysqld]
# MySQL預設3306 Port
port = 2020
# 設定查詢緩存的限制大小（單位：字節）
query_cache_limit = 1024M
# 設定查詢緩存的總大小（單位：字節）
query_cache_size = 1024M
# 允許的最大連接數
max_connections = 10240

### 產品 ###
# 指定MySQL允許的最大連接進程數
max_connections = 4096
# MyISAM 存儲引擎的鍵緩衝區大小
key_buffer_size = 1024M
# 允許的最大封包大小
max_allowed_packet = 2048M
# 線程堆棧大小
thread_stack = 512K
# 線程緩衝區大小
thread_cache_size = 3072
# 查詢緩存限制大小
query_cache_limit = 2048M
# 查詢緩存總大小
query_cache_size = 2048M
# InnoDB 存儲引擎的緩衝池大小
innodb_buffer_pool_size = 4G
# InnoDB 存儲引擎的日誌文件大小
innodb_log_file_size = 1024M
# 連接緩衝區大小
join_buffer_size = 2048M
# 排序緩衝區大小
sort_buffer_size = 1024M
# 開啟慢查詢日誌
slow_query_log = on
# 慢查詢日誌文件
slow-query-log-file = /var/log/mysql/mysql-slow.log
# 慢查詢閾值（單位：秒）
long_query_time = 2
# 連接的等待超時時間（單位：秒）
wait_timeout = 600
```

## MacOS

```bash
# 安裝 wget
brew install wget

# mysql 5.7可以通過brew安裝
brew install mysql@5.7

# brew安裝
# If you need to have mysql-client first in your PATH, run:
#   echo 'export PATH="/usr/local/opt/mysql-client/bin:$PATH"' >> ~/.profile

# For compilers to find mysql-client you may need to set:
#   export LDFLAGS="-L/usr/local/opt/mysql-client/lib"
#   export CPPFLAGS="-I/usr/local/opt/mysql-client/include"
brew install mysql-client

# 啟動
brew services start mysql@5.7

# 停止
brew services stop mysql@5.7

# 移除
brew uninstall mysql@5.7
```

## CentOS7

```bash
# 安裝 wget
yum install wget -y

# 安裝MySQL Yum repository
# 已過期 2022
wget https://repo.mysql.com//mysql57-community-release-el7-11.noarch.rpm
yum localinstall mysql57-community-release-el7-11.noarch.rpm -y
rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2022

# 確認yum repository已經安裝
yum repolist enabled | grep "mysql.*-community.*"

# 查看MySQL發行版本
yum repolist all | grep mysql

# 安裝MySQL
yum install mysql-community-server -y

# 查看預設密碼(須先start)
cat /var/log/mysqld.log | grep 'temporary password'

# 看情況執行
# 執行 MySQL 的初始化過程，創建數據庫系統表和初始數據
/usr/local/mysql/scripts/mysql_install_db --user=mysql
# 將 MySQL 官方提供的管理腳本複製到系統初始化腳本的目錄中，以便你可以使用系統的服務管理工具（如 service 或 systemctl）來控制 MySQL 服務的啟動、停止和管理。
cp /usr/local/mysql/support-files/mysql.server /etc/init.d/mysql.server
```

```sql
-- 輸入登入密碼(修改預設密碼)
ALTER USER root@'localhost' IDENTIFIED BY 'newpassword';

-- 修改root用戶可任意IP登入
UPDATE mysql.user SET host = '%' WHERE user = 'root';

-- 刷新MySQL的系統權限相關表
FLUSH PRIVILEGES;
```

```bash
# 檢視3306 Port是否開啟
firewall-cmd --query-port=3306/tcp

# 開啟
firewall-cmd --zone=public --add-port=3306/tcp --permanent

# 重啟防火牆
firewall-cmd --reload
```

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

## 安裝 MySQL Router

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

### MySQL Router 配置文檔

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

## 安装 MySQL Shell

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
# 查看指令
mysqlrouter --help

# 啟動 MySQL Router
mysqlrouter -c /path/to/router.conf
```

## MySQL Shell 指令

```bash
# 查看版本
mysqlsh --version

# 啟動 MySQL Shell，並連接到 MySQL 數據庫，執行各種管理和查詢任務
mysqlsh -u username -h hostname -p

# 重啟mgr集群
mysqlsh --uri root@node_1:3306
```

```JavaScript
// 執行數據庫管理操作時控制輸出的詳細信息級別
// dba.verbose=0：只顯示最基本的輸出信息，適用於只關注操作結果的情況。
// dba.verbose=1：顯示較詳細的輸出信息，包括操作的進度和狀態。適用於需要了解操作進展情況的情況。
// dba.verbose=2：顯示最詳細的輸出信息，包括操作的詳細過程、進度、狀態和日誌。適用於需要深入了解操作細節和可能的問題排查的情況。
```

### innodb Cluster 相關

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

// 創建集群
dba.createCluster('ClusterName')

// 查看集群狀態
// 方法一：
var cluster = dba.getCluster('ClusterName')
cluster.status()

dba.getCluster('ClusterName').status()

// 獲取集群結構
var cluster = dba.getCluster('ClusterName')
cluster.describe()

dba.getCluster('ClusterName').describe()


// 添加新節點
var cluster = dba.getCluster('ClusterName')
cluster.addInstance('root@hostname:3307')

dba.getCluster('ClusterName').addInstance('root@hostname:3307')

//
dba.getCluster('ClusterName').rescan()

// 重新將節點加入
var cluster = dba.getCluster('ClusterName')
cluster.rejoinInstance('root@hostname:3307')

dba.getCluster('ClusterName').rejoinInstance('root@hostname:3307')

// 手動切換主節點(5.7.43 沒有)
var cluster = dba.getCluster('ClusterName')
cluster.setPrimaryInstance('root@hostname:3307');

dba.getCluster('ClusterName').setPrimaryInstance('root@hostname:3307');

// 移除節點
// 普通移除
var cluster = dba.getCluster('ClusterName')
cluster.removeInstance('root@hostname:3307')

dba.getCluster('ClusterName').removeInstance('root@hostname:3307')

// 強制移除
var cluster = dba.getCluster('ClusterName')
cluster.removeInstance('root@hostname:3307',{force: true})

dba.getCluster('ClusterName').removeInstance('root@hostname:3307',{force: true})

// 重新啟動集群
dba.rebootClusterFromCompleteOutage('ClusterName');

// 解散集群
var cluster = dba.getCluster('ClusterName')
cluster.dissolve({force:true})

dba.getCluster('ClusterName').dissolve({force:true})

// 刪除 Metadata schema
// 這個選項會刪除 Metadata schema，然後你可以重新建立一個新的 MySQL InnoDB Cluster。
// 請注意，這會導致 Metadata 中的任何相關資訊都會丟失，包括以前建立的 Cluster 設定。
dba.dropMetadataSchema()

// 查看集群描述
dba.getCluster('ClusterName').describe()

// 切換到多主模式
dba.getCluster('ClusterName').switchToMultiPrimaryMode()

// 切換到單主模式
dba.getCluster('ClusterName').switchToSinglePrimaryMode('root@hostname:3306')

// 更改集群設置
dba.getCluster('ClusterName').setOption('clusterName','newCluster')
// 更改集群實例設置
dba.getCluster('ClusterName').setInstanceOption('root@172.27.8.2:3306', 'exitStateAction', 'READ_ONLY')
```

## SQL 指令

```sql
-- 連線
-- mysql -h主機地址 -P端口 -u使用者名稱 －p使用者密碼 （注:u與root可以不用加空格，其它也一樣）
-- 斷開
-- exit （回車）

-- 建立授權
grant select on 資料庫.* to 使用者名稱@登入主機 identified by \”密碼\”
-- 修改密碼
mysqladmin -u使用者名稱 -p舊密碼 password 新密碼
-- 刪除授權
revoke select,insert,update,delete om *.* fromtest2@localhost;

-- 顯示資料庫
show databases;
-- 顯示資料表
show tables;
-- 顯示錶結構
describe 表名;

-- 建立庫
create database [數據庫名];
-- 刪除庫
drop database 庫名;
-- 使用庫
use 庫名;

-- 建立表
create table 表名 (欄位設定列表);
CREATE TABLE test (First_Name char(50),Last_Name char(50));
-- 刪除表
drop table 表名;
-- 修改表
alter table t1 rename t2
-- 查詢表
select * from 表名;
-- 清空表
delete from 表名;
-- 備份表:
mysqlbinmysqldump -h(ip) -uroot -p(password) databasenametablename > tablename.sql
-- 恢復表:
mysqlbinmysql -h(ip) -uroot -p(password) databasenametablename < tablename.sql（操作前先把原來表刪除）

-- 增加列
ALTER TABLE t2 ADD c INT UNSIGNED NOT NULL AUTO_INCREMENT,ADDINDEX (c);
-- 修改列
ALTER TABLE t2 MODIFY a TINYINT NOT NULL, CHANGE b cCHAR(20);
-- 刪除列
ALTER TABLE t2 DROP COLUMN c;

-- master全表鎖定只讀
FLUSH TABLES WITH READ LOCK;
--
SHOW MASTER STATUS;
```

### 使用者相關

```sql
-- 查看預設密碼
cat /var/log/mysqld.log | grep 'temporary password'

	-- 狀況：
	-- ERROR 1820 (HY000): You must reset your password using ALTER USER statement before executing this statement.
	-- 需更改預設密碼

-- 輸入登入密碼(修改預設密碼)
ALTER USER root@'localhost' IDENTIFIED BY 'password';

-- 刷新MySQL的系統權限相關表
FLUSH PRIVILEGES;

-- 列出所有使用者帳號
SELECT User,Host FROM mysql.user;

-- 列出目前使用者的權限
SHOW GRANTS;
SHOW GRANTS FOR CURRENT_USER;
SHOW GRANTS FOR CURRENT_USER();

-- 列出使用者的權限
SHOW GRANTS FOR username;

-- 查看user表確認擁有的權限
SELECT * FROM user WHERE user='username'\G

-- 創建新使用者
CREATE USER 'newuser'@'localhost' IDENTIFIED BY 'newpassword';

-- 修改用戶可任意IP登入
UPDATE mysql.user SET host = '%' WHERE user = 'user';

-- 修改使用者密碼
SET PASSWORD FOR '目標使用者'@'主機' = PASSWORD('密碼');
SET PASSWORD FOR testuser@'%' = PASSWORD('11111111111');

-- 刷新MySQL的系統權限相關表
flush privileges;
FLUSH PRIVILEGES;

-- 查看使用者
SELECT User,Host FROM mysql.user;

-- 查看帳號擁有的權限
SELECT * FROM mysql.user where user='user'\G

-- 給予權限 dbname.table *為全部 權限參考下方許可權列表
GRANT 權限 ON 數據庫對象 TO 用戶
GRANT privileges ON dbname.table TO 'username'@'host' IDENTIFIED BY 'password';

-- 給帳號username'@'localhost對所有資料庫擁有SELECT,INSERT,UPDATE,DELETE的權限
GRANT CREATE,SELECT,INSERT,UPDATE,DELETE ON *.* TO 'user'@'%' IDENTIFIED BY 'password';

-- ERROR 1064 (42000) 使用到保留字 需用``包住

-- 移除權限 權限參考下方許可權列表
REVOKE 權限 ON 數據庫對象 FROM 用戶
REVOKE privileges ON dbname.table FROM 'user'@'host';

-- 刪除使用者
DROP USER 'user'@'host';

-- 修改root 主機名
UPDATE mysql.user SET host='%' WHERE user='root';
```

### 密碼設定強度修改

```sql
-- 查看現有參數
SHOW variables LIKE '%password%';

-- | validate_password.length                     | 8               | <== 8個字元
-- | validate_password.mixed_case_count           | 1               | <== 大小寫字元數
-- | validate_password.number_count               | 1               | <== 數字數
-- | validate_password.policy                     | MEDIUM          | <== 強度政策
-- | validate_password.special_char_count         | 1               | <== 特殊字元數

SET GLOBAL validate_password_policy=0;
SET GLOBAL validate_password_length=4;
SET GLOBAL validate_password_special_char_count =0;

-- 0 /LOW 只檢查長度
-- 1 /MEDIUM 檢查長度、英文、數字、特殊字元要照表操課
-- 2 /STRONG 除了上面以外，還要檢查是否是字典檔的字
```

### 許可權 列表

```sql
-- 對資料庫:
ALL PRIVILEGES、ALTER、CREATE、DELETE、DROP、FILE、INDEX、INSERT、
PROCESS、REFERENCES、RELOAD、SELECT、SHUTDOWN、UPDATE、USAGE
共15個, 比前面多了一個"ALL PRIVILEGE"(就是包含全部權限在內)
-- 對資料表:
SELECT、INSERT、UPDATE、DELETE、CREATE、DROP、INDEX、ALTER,create temporary tables, execute, create view, show view,
共八個
-- 對資料欄:
SELECT、INSERT、UPDATE
只有三個

SELECT
INSERT
UPDATE
DELETE
DROP
CREATE
CREATE USER
ALTER
ALTER ROUTINE (使用alter procedure和drop procedure)
CREATE ROUTINE (使用create procedure)
CREATE TEMPORARY TABLES （使用create temporary table）
CREATE VIEW
EXECUTE (使用call和儲存過程)
EVENT
FILE （使用select into outfile 和 load data infile）
GRANT OPTION (可以使用grant和revoke)

ALL, ALL PRIVILEGES

    These privilege specifiers are shorthand for “all privileges available at a given privilege level” (except GRANT OPTION). For example, granting ALL at the global or table level grants all global privileges or all table-level privileges, respectively.


INDEX （可以使用create index和drop index）
LOCK TABLES (鎖表)
PROCESS (使用show full processlist)
RELOAD （使用flush）
REPLICATION CLIENT (伺服器位置訪問)
REPLICATION SLAVE (由複製從屬使用)
SHOW DATABASES
SHOW VIEW
SHUT DOWN （使用mysqladmin shutdown 來關閉mysql）
SUPER
USAGE (無訪問許可權)
```

### innodb cluster相關

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

## 服務操作

```bash
# 查看版本
mysql -V

# 啟動服務
systemctl start mysqld

# 查詢啟動狀態
systemctl status mysqld

# 重新啟動
systemctl restart mysqld

# 停止服務
systemctl stop mysqld

# 開啟開機自動啟動
systemctl enable mysqld

# 關閉開機自動啟動
systemctl disable mysqld

# (start, stop, restart, try-restart, reload, force-reload, status)
# 重新載入
service mysqld reload

# 登入MySQL
mysql -u root -p (password)
```

## 匯出匯入

### 匯出 - mysqldump

```bash
`匯出資料和表結構`
mysqldump -h source_MySQL_DB_instance_endpoint \
    -u user \
    -ppassword \
    --port=3306 \
    --single-transaction \
    --routines \
    --triggers \
    --databases  database database2 \
    --compress  \
    --port 3306

# -d 只匯出表結構不導表資料
# mysqldump -u[使用者名稱] -h[ip] -p[密碼] -P[埠號] 資料庫名 表名 >匯出的檔名.sql
mysqldump -uuser -h127.0.0.1 -ppassword -P3306 --no-data dbname tablename>name.sql


mysqldump -h 127.0.0.1 \
    -u root \
    -pAVNIGHTAVNIGHT \
    --port=3306 \
    --single-transaction \
    --routines \
    --triggers \
    --databases  avnight \
    --compress

# mysqldump -h hostname -u使用者名稱 -p密碼 資料庫名 > 資料庫名.sql
# 	由於在 mysqldump 8 中默認啟用了一個新標誌。您可以通過添加 --column-statistics=0 來禁用它。
# 	--column-statistics=0
# 	多個資料庫
# 	--databases db1 db2
# 	在每個創建數據庫表語句前添加刪除數據庫表的語句
# 	--add-drop-table
# 	備份數據庫表時鎖定數據庫表
# 	--add-locks
# 	備份MySQL服務器上的所有數據庫
# 	--all-databases
# 	添加註釋信息
# 	--comments
# 	壓縮模式，產生更少的輸出
# 	--compact
# 	輸出完成的插入語句
# 	--complete-insert
# 	指定要備份的數據庫
# 	--databases -d
# 	指定默認字符集
# 	--default-character-set
# 	當出現錯誤時仍然繼續備份操作
# 	--force
# 	指定要備份數據庫的服務器
# 	--host -h
# 	備份前，鎖定所有數據庫表
# 	--lock-tables
# 	禁止生成創建數據庫語句
# 	--no-create-db
# 	禁止生成創建數據庫庫表語句
# 	--no-create-info
# 	連接MySQL服務器的密碼
# 	--password -p
# 	MySQL服務器的端口號
# 	--port
# 	連接MySQL服務器的用戶名。
# 	--user -u

`只匯出表結構`
mysqldump -u使用者名稱 -p密碼 -d 資料庫名 > 資料庫名.sql
# 注：/usr/local/mysql/bin/  —>  mysql的data目錄

# 備份資料庫
mysqldump -h(ip) -uroot -p(password) databasename table1 table2 > database.sql

# 複製資料庫
mysqldump –-all-databases >all-databases.sql
# 修復資料庫
mysqlcheck -A -o -uroot -p54safer

# 文字資料匯入
load data local infile \”檔名\” into table 表名;

# 資料匯入
mysqlimport database tables.txt

# mysql服務的啟動和停止
net stop mysql
net start mysql
```

### 匯入

```bash
`方法一`
# 選擇資料庫
mysql>use $dbname;

# 設定資料庫編碼
mysql>set $dbname utf8;

# 匯入資料（注意sql檔案的路徑）
mysql>source path/$name.sql;

`方法二`
# mysql -u使用者名稱 -p密碼 資料庫名 < 資料庫名.sql
mysql -u$username -p $dbname < $name.sql

mysql -u$username -p < $name.sql
# 恢復資料庫
mysql -h(ip) -uroot -p(password) databasename< database.sql
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


# Master-Slave 主從架構架設

```
MYSQL_REPLICATION_MODE: 複製模式。可能的值master/ slave。沒有默認值。
MYSQL_REPLICATION_USER：第一次運行時在主服務器上創建的複制用戶。沒有默認值。
MYSQL_REPLICATION_PASSWORD: 複製用戶密碼。沒有默認值。
MYSQL_MASTER_HOST: 複製主機的主機名/IP（從參數）。沒有默認值。
MYSQL_MASTER_PORT_NUMBER: 複製master的服務器端口（slave參數）。默認為3306.
MYSQL_MASTER_ROOT_USER：複製主機上的用戶可以訪問MYSQL_DATABASE（從參數）。默認為root
MYSQL_MASTER_ROOT_PASSWORD：複製主機上的用戶密碼，可以訪問MYSQL_DATABASE（從參數）。沒有默認值。
```

## mysql-master設定

```bash
# 登入mysql
mysql -u{$username} -p{$password}
```

```sql
-- 確認mysql-master server_id和log_bin變數
-- mysql-master 若server_id(1)和log_bin(ON)變數正確跳至-建立master-slave使用者
show variables like 'server_id%';
show variables like 'log_bin%';
```

```bash
# 停止mysql
service mysql stop
systemctl sstoptart mysqld
```

```conf
# mysql-master 加入設定到下列(其中一個)設定檔
# /etc/my.cnf
# /etc/mysql/my.cnf
[mysqld]
log-bin = mysql-bin
server-id = 1
```

```bash
# 啟動mysql
service mysql start
systemctl start mysqld

# 登入mysql
mysql -u{$username} -p{$password}
```

```sql
-- 確認mysql-master server_id和log_bin變數
show variables like 'server_id%';
show variables like 'log_bin%';

-- 建立master-slave使用者
-- 要授予此帳戶複製所需的權限，請使用該GRANT 語句。
-- 如果僅為複制目的創建帳戶，則該帳戶只需要 REPLICATION SLAVE權限。
-- 例如，要設置一個repl可以從example.com域內的任何主機連接以進行複制 的新用戶
CREATE USER 'replication'@'192.168%' IDENTIFIED BY '.wFb9A?$9*WN';
GRANT REPLICATION SLAVE ON *.* TO 'replication'@'192.168%';

-- 檢查使用者 列出所有使用者帳號
select User, Host From mysql.user;
```

## mysql-slave設定

```bash
# 登入mysql
mysql -u{$username} -p{$password}
```

```sql
-- 檢查mysql-slave server_id和read_only變數
-- mysql-slave 若server_id(2)和read_only(ON)變數正確跳至第三步
show variables like 'server_id%';
show variables like 'read_only%';
```

```bash
# 停止mysql
service mysql stop
systemctl sstoptart mysqld
```

```conf
# mysql-slave 加入設定到下列(其中一個)設定檔
# /etc/my.cnf
# /etc/mysql/my.cnf
[mysqld]
server-id = 2
read-only = ON
```

```bash
# 啟動mysql
service mysql start
systemctl start mysqld

# 登入mysql
mysql -u{$username} -p{$password}
```

```sql
-- 確認mysql-slave server_id和read_only變數
show variables like 'server_id%';
show variables like 'read_only%';
```

## 備份mysql-master

```sql
-- master全表鎖定只讀
FLUSH TABLES WITH READ LOCK;

-- 檢查master是否lock
-- bin_log資料(紀錄file和position)
SHOW VARIABLES LIKE 'Position%';
-- 顯示master狀態：該File列顯示日誌文件的名稱並Position顯示文件中的位置。
-- 記錄這些值。稍後在設置副本時需要它們。
-- 它們表示副本應開始處理來自源的新更新的複制坐標。
SHOW MASTER STATUS;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000001 |     2584 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
-- 下方設定 {$master bin_log filename} = mysql-bin.000001
-- 下方設定 {$log position} = 2584
```

```bash
# xtrabackup完全備份
xtrabackup --user={$username} --password={$password} --backup --target-dir={$xtrabackup_path}

# 將/data目錄傳到mysql-slave機器
# 後面 $xtrabackup_path 不要包含本身資料夾: /root/mysql_back20230608 -> /root
# linode部分 ip可以使用內網ip
rsync -Pr {$xtrabackup_path} root@{$slave_ip}:{$xtrabackup_path}
```

```sql
-- 解開lock
UNLOCK TABLES;
```

## 恢復備份到mysql-slave

```bash
# 停止mysql
service mysql stop
systemctl stop mysqld

# 備份mysql目錄
cd /var/lib
cp -r ./mysql ./mysql.bak

# mysql目錄資料清空
cd mysql
rm -rf ./*

# 準備備份
xtrabackup --prepare --target-dir={$xtrabackup_path}

# 恢復備份
xtrabackup --copy-back --target-dir={$xtrabackup_path}

# mysql目錄權限修改
chown -R mysql.mysql /var/lib/mysql

# 啟動mysql
service mysql start
systemctl start mysqld

#登入mysql檢查
mysql -u{$username} -p{$password}
```

```sql
-- mysql-slave設定master資料
CHANGE MASTER TO
MASTER_HOST='{$master ip}',
MASTER_PORT=3306,
MASTER_USER='replication',
MASTER_PASSWORD='.wFb9A?$9*WN',
MASTER_LOG_FILE='{$master bin_log filename}',
MASTER_LOG_POS={$log position};

-- 執行slave
start slave;

-- 確認master狀態
show master status\G

-- 確認slave狀態
-- Slave_IO_State: Waiting for master to send event
-- Slave_IO_Running: Yes
-- Slave_SQL_Running: Yes
-- Last_IO_Error: 排錯 錯誤訊息
show slave status\G
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

### MySQL 5.7

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
GRANT ALL on *.* to root@'%' with grant option;

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

## 修復 master slave 最快速方法

```sql
-- master 鎖定表寫入 進行 資料匯出 bin-log 複製
FLUSH TABLES WITH READ LOCK;

SHOW MASTER STATUS;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000001 |     2584 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
```

```bash
xtrabackup --user=root --password --backup --target-dir=/root/xtrabackup_$date
cp /var/lib/mysql/mysql-bin.$binlog_number /root
scp /root/mysql-bin.$binlog_number root@$slave_ip:/root
```

```sql
-- 匯出資料 搜集完必須資訊後 解鎖寫入
UNLOCK TABLES;
```

```bash
# 將/data目錄傳到mysql-slave機器
rsync -Pr /root/xtrabackup_$date root@$slave_ip:/root

cd /var/lib/mysql
rm -rf ./*

xtrabackup --prepare --target-dir=/root/xtrabackup_$date
xtrabackup --copy-back --target-dir=/root/xtrabackup_$date

cp /root/mysql-bin.$binlog_number /var/lib/mysql
chown -R mysql.mysql /var/lib/mysql
```

```sql
-- 重置 slave
STOP SLAVE;
RESET SLAVE;

-- 修改設定
CHANGE MASTER TO
MASTER_LOG_FILE='mysql-bin.$binlog_number',
MASTER_LOG_POS=$postion;

-- 啟動 slave
START SLAVE;

-- 查看 slave 狀態
SHOW SLAVE STATUS\G
```

## 修復 master slave Slave_SQL_Running: No, Slave_IO_Running: No 解決方案

```sql
-- 第一種 直接跳過一行造成停止的SQL指令
stop slave;
set global SQL_SLAVE_SKIP_COUNTER=1;
start slave;
show slave status\G

-- 第二種 直接指定Master記錄的File及Position到Slave
-- Slave_IO_Running: No 也適用
-- === master部分 ===
-- 查看Master主機的File和Position的值。
show master status;
+——————+———–+————–+——————+
| File | Position | Binlog_Do_DB | Binlog_Ignore_DB |
+——————+———–+————–+——————+
| mysql-bin.000118 | 199777882 | | |
+——————+———–+————–+——————+

-- === slave部分 ===
-- 停止slave
stop slave;
-- 手動設定master資料 linode部分 ip可以使用內網ip
change master to
master_host=’master_ip’ ,
master_user=’user’,
master_password=’pwd’,
master_port=3306,
master_log_file=’mysql-bin.000118′,
master_log_pos=199777882;
-- 執行slave
start slave;
-- 檢查slave狀態
show slave status\G
```

## 修復損壞的innodb：innodb_force_recovery

```bash
# CentOS 7
cat /var/log/mysqld.log

查看錯誤日誌：
InnoDB: Error: could not open single-table tablespace file ./data_dep/report.ibd

innodb引擎出了問題
```

## [Warning] IP address 'xxx.xxx.xxx.xxx' could not be resolved- Name or service not known

```
一
    CentOS 7
    /etc/my.cnf
    添加 skip_name_resolve
    跳過反向解析

二(沒用)
    CentOS 7
    /etc/hosts
    添加 X.X.X.X some_name
    0.0.0.0 : 全都通過

    echo "192.241.xx.xx venus.example.com venus" >> /etc/hosts
```

## Table 'db.table' doesn't exist (1146)

```sql
-- 檢查原因
mysql> check table db.table;
```

## 升級為GTID模式

加入設定到下列(其中一個)設定檔
/etc/my.cnf
/etc/mysql/my.cnf

```conf
gtid_mode = on
enforce_gtid_consistency = on
```

## ERROR 1872 (HY000): Slave failed to initialize relay log info structure from the repository

```
主從備份遇到此錯誤
mysql> start slave;
ERROR 1872 (HY000): Slave failed to initialize relay log info structure from the repository
```

```sql
mysql> reset slave;
Query OK, 0 rows affected (0.10 sec)

-- 看情況執行
mysql> change master to master_host='IP', master_port=3306, master_user='用户名', MASTER_PASSWORD='密码', master_log_file='mysql-bin.000594', MASTER_LOG_POS=98175332;

mysql> start slave;
Query OK, 0 rows affected (0.10 sec)
```

## ERROR 3009 (HY000): Column count of mysql.user is wrong. Expected 45, found 43. Created with MySQL 50739, now running 50742. Please use mysql_upgrade to fix this error.

```
```