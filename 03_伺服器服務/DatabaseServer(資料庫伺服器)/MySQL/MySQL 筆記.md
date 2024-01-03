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
      - [Docker相關](#docker相關)
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
  - [安裝 MySQL Router](#安裝-mysql-router)
    - [MySQL Router 配置文檔](#mysql-router-配置文檔)
    - [MySQL Router Docker Compose 配置](#mysql-router-docker-compose-配置)
  - [安装 MySQL Shell](#安装-mysql-shell)
- [指令](#指令)
  - [服務操作](#服務操作)
  - [MySQL Router 指令](#mysql-router-指令)
  - [MySQL Shell 指令](#mysql-shell-指令)
    - [innodb Cluster 相關](#innodb-cluster-相關)
  - [SQL 指令](#sql-指令)
    - [使用者相關](#使用者相關)
    - [密碼設定強度修改](#密碼設定強度修改)
    - [許可權 列表](#許可權-列表)
  - [匯出匯入](#匯出匯入)
    - [匯出 - mysqldump](#匯出---mysqldump)
    - [匯入](#匯入)
  - [mysqlbinlog - 檢查主資料庫中的二進制日誌（Binary Log）](#mysqlbinlog---檢查主資料庫中的二進制日誌binary-log)
- [重大備份](#重大備份)
- [例外狀況](#例外狀況)
  - [\[Warning\] IP address 'xxx.xxx.xxx.xxx' could not be resolved- Name or service not known](#warning-ip-address-xxxxxxxxxxxx-could-not-be-resolved--name-or-service-not-known)
  - [Table 'db.table' doesn't exist (1146)](#table-dbtable-doesnt-exist-1146)

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

[MySQL Shell 下載頁面](https://dev.mysql.com/downloads/shell/)

[MySQL Shell 命令](https://dev.mysql.com/doc/mysql-shell/8.0/en/mysql-shell-commands.html)

[MySQL AdminAPI - 管理 MySQL 實例，使用它們創建 InnoDB Cluster、InnoDB ClusterSet 和 InnoDB ReplicaSet 部署，以及集成 MySQL Router](https://dev.mysql.com/doc/mysql-shell/8.0/en/admin-api-userguide.html)

[MySQL Shell API 8.0.33](https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/group___admin_a_p_i.html)

[朝花夕拾16章MySQL Shell 使用 MySQL Shell 命令](https://www.modb.pro/db/638407)

[技术分享 | mysqlsh 命令行模式 & 密码保存](https://cloud.tencent.com/developer/article/1782068)

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

[4.3.2.1 mysqlrouter — 命令行選項](https://dev.mysql.com/doc/mysql-router/8.0/en/mysqlrouter.html#option_mysqlrouter_force-password-validation)

[Oracle官方轻量级中间件MySQL Router介绍与性能测试](https://www.modb.pro/db/77315)

[Ubuntu20.04安装MySQL Router](http://www.884358.com/ubuntu-install-mysql-router/)

[Docker image - mysql/mysql-router](https://hub.docker.com/r/mysql/mysql-router)

### 使用者權限相關

[MySQL / MariaDB 移除使用者帳號及權限](https://ithelp.ithome.com.tw/articles/10235980)

[權限說明](https://rosalie1211.blogspot.com/2019/03/mysql.html)

[淺談MySQL中授權(grant)和撤銷授權(revoke)用法詳解](https://www.itread01.com/articles/1476680778.html)

[只談MySQL (第四天) 帳號與權限](https://ithelp.ithome.com.tw/articles/10029835)

[6.2.2 Privileges Provided by MySQL](https://dev.mysql.com/doc/refman/8.0/en/privileges-provided.html#priv_all)

[如何在Docker中以bootstrap模式使用compose文件运行mysql路由器](https://cloud.tencent.com/developer/ask/sof/338443)

### 安裝相關

[CENTOS 7 安裝mysql](https://kirby86a.pixnet.net/blog/post/118006518-centos-7-%E5%AE%89%E8%A3%9Dmysql)

[在CentOS7上安裝MySQL5.7](https://dotblogs.com.tw/tinggg01/2018/07/06/153413)

[Unknown table 'COLUMN_STATISTICS' in information_schema (1109)](https://serverfault.com/questions/912162/mysqldump-throws-unknown-table-column-statistics-in-information-schema-1109)

[mysql 優化技巧心得一(key_buffer_size設定)](https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/410436/)

#### Docker相關

[使用docker-compose啟動服務時，初始化資料庫和資料(以Mysql為例)](https://www.tpisoftware.com/tpu/articleDetails/1826)

[docker hub (mysql)](https://hub.docker.com/_/mysql)

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

[[MySQL] SQL_ERROR 1032解决办法](https://www.cnblogs.com/langdashu/p/5920436.html)

[MySQL warning "IP address could not be resolved"](https://serverfault.com/questions/393862/mysql-warning-ip-address-could-not-be-resolved)

[Is DNS the Achilles heel in your MySQL installation?](https://www.percona.com/blog/2008/05/31/dns-achilles-heel-mysql-installation/)

[MySQL崩潰-如何修復損壞的innodb：innodb_force_recovery](https://www.twblogs.net/a/5b8201762b71772165af295d)

[解决 MySQL 报错 “ Column count of mysql.user is wrong...”](https://cloud.tencent.com/developer/article/1662598)

#### InnoDB Cluster 錯誤

[Multi Source Replication MySQL 5.6 to 5.7 GTID Auto Position Issues](https://stackoverflow.com/questions/30606345/multi-source-replication-mysql-5-6-to-5-7-gtid-auto-position-issues)

[Plugin group_replication reported: '[GCS] Connection attempt from IP address ::ffff:10.57.19.100 refused. Address is not in the IP whitelist.'](https://www.cnblogs.com/Miac/p/11990725.html)

[MySQL Shell无法拉起MGR集群解决办法 - rebootClusterFromCompleteOutage失敗 因为MySQL 5.7中还不支持 SET PERSIST 功能](https://ost.51cto.com/posts/16247)

[1114 (HY000): The table is full](https://stackoverflow.com/questions/730579/1114-hy000-the-table-is-full)

[How can I troubleshoot the error "MySQL HA_ERR_RECORD_FILE_FULL" when I use Amazon RDS for MySQL? - 1114](https://repost.aws/knowledge-center/rds-error-mysql-record-file-full)

[7.8.2 Restoring a Cluster from Quorum Loss](https://dev.mysql.com/doc/mysql-shell/8.0/en/restore-cluster-from-quorum-loss.html)

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

### MySQL Router Docker Compose 配置

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

## MySQL Router 指令

```bash
# https://dev.mysql.com/doc/mysql-router/8.0/en/mysqlrouter.html#option_mysqlrouter_force-password-validation
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
// https://dev.mysql.com/doc/dev/mysqlsh-api-javascript/8.0/classmysqlsh_1_1dba_1_1_dba.html#a12f040129a2c4c301392dd69611da0c8
dba.createCluster('ClusterName')
dba.createCluster('ClusterName', {localAddress:'192.168.0.1:3307', ipAllowlist:'192.168.0.0/16,139.144.119.64'})

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
var cluster = dba.getCluster('ClusterName')
cluster.addInstance('root@hostname:3307')

dba.getCluster('ClusterName').addInstance('root@hostname:3307')
dba.getCluster('ClusterName').addInstance('root@139.144.119.64:5306')

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

-- 指定 該使用者能夠操作的數據庫
GRANT ALL PRIVILEGES ON your_database.* TO 'newuser'@'localhost';


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
#   不包含資料
#   -d

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
