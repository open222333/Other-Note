# MySQL 筆記

```
```

## 目錄

- [MySQL 筆記](#mysql-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [使用者權限相關](#使用者權限相關)
		- [安裝相關](#安裝相關)
		- [Master-Slave(主從環境)相關](#master-slave主從環境相關)
		- [叢集(Cluster)相關](#叢集cluster相關)
		- [操作相關](#操作相關)
		- [備份相關](#備份相關)
			- [備份指令相關](#備份指令相關)
			- [解說相關](#解說相關)
			- [Percona XtraBackup(備份工具)相關](#percona-xtrabackup備份工具相關)
				- [XtraBackup 心得相關](#xtrabackup-心得相關)
- [安裝步驟](#安裝步驟)
	- [CentOS7](#centos7)
		- [配置設定](#配置設定)
	- [MacOS](#macos)
- [指令](#指令)
	- [服務操作](#服務操作)
	- [匯出匯入](#匯出匯入)
		- [匯入資料庫](#匯入資料庫)
- [配置文檔](#配置文檔)
	- [log文檔預設位置](#log文檔預設位置)
- [資料庫指令](#資料庫指令)
	- [資料庫指令 - 使用者](#資料庫指令---使用者)
	- [密碼設定強度修改](#密碼設定強度修改)
- [許可權 列表](#許可權-列表)
- [例外狀況](#例外狀況)
	- [修復損壞的innodb：innodb\_force\_recovery](#修復損壞的innodbinnodb_force_recovery)
	- [\[Warning\] IP address 'xxx.xxx.xxx.xxx' could not be resolved- Name or service not known](#warning-ip-address-xxxxxxxxxxxx-could-not-be-resolved--name-or-service-not-known)
	- [Table 'db.table' doesn't exist (1146)](#table-dbtable-doesnt-exist-1146)
	- [升級為GTID模式](#升級為gtid模式)
	- [ERROR 1872 (HY000): Slave failed to initialize relay log info structure from the repository](#error-1872-hy000-slave-failed-to-initialize-relay-log-info-structure-from-the-repository)
- [Master-Slave 主從架構架設](#master-slave-主從架構架設)
	- [mysql-master設定](#mysql-master設定)
	- [mysql-slave設定](#mysql-slave設定)
	- [備份mysql-master](#備份mysql-master)
	- [恢復備份到mysql-slave](#恢復備份到mysql-slave)
		- [Slave\_SQL\_Running: No, Slave\_IO\_Running: No 解決方案](#slave_sql_running-no-slave_io_running-no-解決方案)
- [Cluster 叢集架設](#cluster-叢集架設)
	- [](#)
	- [指令](#指令-1)
	- [實作](#實作)
		- [Manage node](#manage-node)
		- [Data node](#data-node)
		- [SQL node](#sql-node)
		- [啟動 Cluster 環境](#啟動-cluster-環境)
- [Percona XtraBackup(資料備份的工具)](#percona-xtrabackup資料備份的工具)
	- [MySQL Percona innobackupex 和 xtrabackup 有何不同？](#mysql-percona-innobackupex-和-xtrabackup-有何不同)
	- [安裝 XtraBackup](#安裝-xtrabackup)
	- [指令](#指令-2)
		- [xtrabackup選項](#xtrabackup選項)
			- [用法範例](#用法範例)
		- [innobackupex選項](#innobackupex選項)
		- [備份(即時備份)步驟](#備份即時備份步驟)
	- [說明](#說明)
		- [innobackupex 對比 xtrabackup](#innobackupex-對比-xtrabackup)

## 參考資料

[MySQl官方網站](https://dev.mysql.com/)

[MySQL 全部文檔](https://dev.mysql.com/doc/)

[MySQL 8.0版本 指令](https://dev.mysql.com/doc/refman/8.0/en/mysql-command-options.html)

[MySQL 8.0版本 文檔](https://dev.mysql.com/doc/refman/8.0/en/)

[MySQL 5.7版本 文檔](https://dev.mysql.com/doc/refman/5.7/en/)

[MySQL 教程](https://www.itread01.com/study/mysql-tutorial.html)

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

[MySQL Replication 主從式架構設定教學](https://blog.toright.com/posts/5062/mysql-replication-%E4%B8%BB%E5%BE%9E%E5%BC%8F%E6%9E%B6%E6%A7%8B%E8%A8%AD%E5%AE%9A%E6%95%99%E5%AD%B8.html)

[docker-compose搭建mysql主從環境](https://www.uj5u.com/ruanti/275444.html)

[docker-compose搭建mysql主從環境](https://hub.docker.com/r/bitnami/mysql)

[Slave_IO_Running Slave_SQL_Running 排錯](https://www.cnblogs.com/l-hh/p/9922548.html#_label0)

[mysql系列（一）—— 细说show slave status参数详解（最全）](https://blog.51cto.com/zhengmingjing/1910565)

[MySQL主从复制，启动slave时报错Slave failed to initialize relay log info structure from the repository](https://blog.csdn.net/weixin_37998647/article/details/79950133)

### 叢集(Cluster)相關

```
叢集的概念就是把一台式架構拆分為多台式架構，並且可以提供 HA 高可用性與負載均衡的需求，更不需要擔心延展性的問題，若是 Loading 加大了只需要增加 node 去分擔 Loading
```

[架設 HA 高可用性：MySQL Cluster 叢集 – 7.4.11(5.6.29)](https://shazi.info/%E6%9E%B6%E8%A8%AD-ha-%E9%AB%98%E5%8F%AF%E7%94%A8%E6%80%A7%EF%BC%9Amysql-cluster-%E5%8F%A2%E9%9B%86-7-4-115-6-29/)

[Galera Cluster for MySQL 详解（一）——基本原理](https://blog.csdn.net/wzy0623/article/details/102522268)

[如何建置 MariaDb Galera Cluster](https://gary840227.medium.com/mariadb-cluster-f7220e9eaac8)

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

# 安裝步驟

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
```

```sql
-- 輸入登入密碼(修改預設密碼)
ALTER USER root@'localhost' IDENTIFIED BY 'newpassword';

-- 修改root用戶可任意IP登入
UPDATE mysql.user SET host = '%' WHERE user = 'root';

-- 刷新MySQL的系統權限相關表
FLUSH PRIVILEGES;
```

### 配置設定

```bash
# 修改預設port(需重啟)
vi /etc/my.cnf
```

5.7版本設置變量
https://dev.mysql.com/doc/refman/5.7/en/program-variables.html

```conf
[mysqld]
# MySQL預設3306 Port
port=2020

query_cache_limit=1024M
query_cache_size=1024M
max_connections=10240

### 產品 ###
# 指定MySQL允許的最大連接進程數。
max_connections = 4096
key_buffer_size = 1024M
max_allowed_packet = 2048M
thread_stack = 512K
thread_cache_size = 3072
query_cache_limit = 2048M
query_cache_size = 2048M
innodb_buffer_pool_size = 4G
innodb_log_file_size = 1024M

join_buffer_size = 2048M
sort_buffer_size = 1024M
slow_query_log = on
wait_timeout=600

slow-query-log-file = /var/log/mysql/mysql-slow.log
long_query_time = 2
```

```bash
# 檢視3306 Port是否開啟
firewall-cmd --query-port=3306/tcp

# 開啟
firewall-cmd --zone=public --add-port=3306/tcp --permanent

# 重啟防火牆
firewall-cmd --reload
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

## 匯出匯入

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

### 匯入資料庫

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
# 恢復資料庫
mysql -h(ip) -uroot -p(password) databasename< database.sql
```

# 配置文檔

設定檔位置:

```
全局配置(Global options)
/etc/my.cnf
/etc/mysql/my.cnf
SYSCONFDIR/my.cnf

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

全域變數說明：

```conf
[mysqld]
port = 3306
serverid = 1
socket = /tmp/mysql.sock
skip-locking
	; 避免MySQL的外部鎖定，減少出錯幾率增強穩定性。
skip-name-resolve
	; 禁止MySQL對外部連接進行DNS解析，使用這一選項可以消除MySQL進行DNS解析的時間。
	; 但需要註意，如果開啟該選項，則所有遠程主機連接授權都要使用IP地址方式，否則MySQL將無法正常處理連接請求！
back_log = 384
	; back_log參數的值指出在MySQL暫時停止響應新請求之前的短時間內多少個請求可以被存在堆棧中。
	; 如果系統在一個短時間內有很多連接，則需要增大該參數的值，該參數值指定到來的TCP/IP連接的偵聽隊列的大小。
	; 不同的操作系統在這個隊列大小上有它自己的限制。 試圖設定back_log高於你的操作系統的限制將是無效的。
	; 默認值為50。
	; 對於Linux系統推薦設置為小於512的整數。
key_buffer_size = 256M
	; key_buffer_size指定用於索引的緩沖區大小，增加它可得到更好的索引處理性能。
	; 對於內存在4GB左右的服務器該參數可設置為256M或384M。
	; 註意：該參數值設置的過大反而會是服務器整體效率降低！
max_allowed_packet = 4M
thread_stack = 256K
table_cache = 128K
sort_buffer_size = 6M
	; 查詢排序時所能使用的緩沖區大小。
	; 註意：該參數對應的分配內存是每連接獨占，如果有100個連接，那麽實際分配的總共排序緩沖區大小為100 × 6 ＝ 600MB。
	; 所以，對於內存在4GB左右的服務器推薦設置為6-8M。
read_buffer_size = 4M
	; 讀查詢操作所能使用的緩沖區大小。
	; 和sort_buffer_size一樣，該參數對應的分配內存也是每連接獨享。
join_buffer_size = 8M
	; 聯合查詢操作所能使用的緩沖區大小，和sort_buffer_size一樣，該參數對應的分配內存也是每連接獨享。
myisam_sort_buffer_size = 64M
table_cache = 512
thread_cache_size = 64
query_cache_size = 64M
	; 指定MySQL查詢緩沖區的大小。
	; 可以通過在MySQL控制臺觀察，
	; 如果Qcache_lowmem_prunes的值非常大，則表明經常出現緩沖不夠的情況
	; 如果Qcache_hits的值非常大，則表明查詢緩沖使用非常頻繁，如果該值較小反而會影響效率，那麽可以考慮不用查詢緩沖
	; Qcache_free_blocks，如果該值非常大，則表明緩沖區中碎片很多。
tmp_table_size = 256M
max_connections = 768
	; 指定MySQL允許的最大連接進程數。
	; 如果在訪問時經常出現Too Many Connections的錯誤提示，則需要增大該參數值。
max_connect_errors = 10000000
wait_timeout = 10
	; 指定一個請求的最大連接時間，對於4GB左右內存的服務器可以設置為5-10。
thread_concurrency = 8
	; 該參數取值為服務器邏輯CPU數量*2，在本例中，服務器有2顆物理CPU，而每顆物理CPU又支持H.T超線程，所以實際取值為4*2=8
skip-networking
	; 開啟該選項可以徹底關閉MySQL的TCP/IP連接方式，如果WEB服務器是以遠程連接的方式訪問MySQL數據庫服務器則不要開啟該選項！否則將無法正常連接！
table_cache=1024
	; 物理內存越大,設置就越大.默認為2402,調到512-1024最佳
innodb_additional_mem_pool_size=4M
	; 默認為2M
innodb_flush_log_at_trx_commit=1
	; 設置為0就是等到innodb_log_buffer_size列隊滿後再統一儲存,默認為1
innodb_log_buffer_size=2M
	; 默認為1M
innodb_thread_concurrency=8
	; 你的服務器CPU有幾個就設置為幾,建議用默認一般為8
key_buffer_size=256M
	; 默認為218，調到128最佳
tmp_table_size=64M
	; 默認為16M，調到64-256最掛
read_buffer_size=4M
	; 默認為64K
read_rnd_buffer_size=16M
	; 默認為256K
sort_buffer_size=32M
	; 默認為256K
thread_cache_size=120
	; 默認為60
query_cache_size=32M
```

## log文檔預設位置

```bash
# CentOS 7
cat /var/log/mysqld.log
```

# 資料庫指令

```sql
-- 連線
mysql -h主機地址 -P端口 -u使用者名稱 －p使用者密碼 （注:u與root可以不用加空格，其它也一樣）
-- 斷開
exit （回車）

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

## 資料庫指令 - 使用者

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
SET PASSWORD FOR testuser@'%' = PASSWORD('su.3m, u;6ru');
SET PASSWORD FOR root@'%' = PASSWORD('ZB2tf8U#L6');

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

## 密碼設定強度修改

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


# 許可權 列表

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

# 例外狀況

## 修復損壞的innodb：innodb_force_recovery

[MySQL崩潰-如何修復損壞的innodb：innodb_force_recovery](https://www.twblogs.net/a/5b8201762b71772165af295d)

```bash
# CentOS 7
cat /var/log/mysqld.log

查看錯誤日誌：
InnoDB: Error: could not open single-table tablespace file ./data_dep/report.ibd

innodb引擎出了問題
```

## [Warning] IP address 'xxx.xxx.xxx.xxx' could not be resolved- Name or service not known

[MySQL warning "IP address could not be resolved"](https://serverfault.com/questions/393862/mysql-warning-ip-address-could-not-be-resolved)

[Is DNS the Achilles heel in your MySQL installation?](https://www.percona.com/blog/2008/05/31/dns-achilles-heel-mysql-installation/)

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
```

mysql-master 加入設定到下列(其中一個)設定檔
/etc/my.cnf
/etc/mysql/my.cnf

```conf
[mysqld]
log-bin = mysql-bin
server-id = 1
```

```bash
# 啟動mysql
service mysql start

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
```

mysql-slave 加入設定到下列(其中一個)設定檔
/etc/my.cnf
/etc/mysql/my.cnf

```conf
[mysqld]
server-id = 2
read-only = ON
```

```bash
# 啟動mysql
service mysql start

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

### Slave_SQL_Running: No, Slave_IO_Running: No 解決方案

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

# Cluster 叢集架設

```
MySQL Cluster Nodes：

Manage Nodes：負責監控叢集所有 Nodes 的狀態，並且由此控制所有 Nodes 的替換。
Data Nodes：負責所有 SQL Data 的 Nodes，單純儲存資料，將資料寫在 RAM & Disk。
SQL Nodes：負責 SQL 的 Table schema 和 Client 連接的空間。
```

##

```
多主架構：真正的多主多活群集，可隨時對任何節點進行讀寫。
同步複製：集群不同節點之間數據同步，某節點崩潰時沒有數據丟失。
數據一致：所有節點保持相同狀態，節點之間無數據分歧。
並行複制：重放支持多線程並行執行以獲得更好的性能。
故障轉移：故障節點本身對集群的影響非常小，某節點出現問題時無需切換操作，因此不需要使用VIP，也不會中斷服務。
自動克隆：新增節點會自動拉取在線節點的數據，最終集群所有節點數據一致，而不需要手動備份恢復。
應用透明：提供透明的客戶端訪問，不需要對應用程序進行更改。
```

## 指令

```bash
# 從 Manage node 確認所有 node 狀態
ndb_mgm
```

## 實作

```bash
# 所有的 nodes 都需要安裝 mysql-cluster
wget https://dev.mysql.com/get/Downloads/MySQL-Cluster-7.4/mysql-cluster-gpl-7.4.11-linux-glibc2.5-i686.tar.gz
tar zxvf mysql-cluster-gpl-7.4.11-linux-glibc2.5-i686.tar.gz
mkdir /usr/local/mysql && mv mysql-cluster-gpl-7.4.11-linux-glibc2.5-i686 !$
```

### Manage node

```bash
mkdir /var/lib/mysql-cluster
vim /var/lib/mysql-cluster/config.ini

# ndb_mgm 和 ndb_mgmd 等工具放到 /usr/local/bin 方便使用
cp /usr/local/mysql/bin/ndb_mgm* /usr/local/bin/
```

```ini
; NoOfReplicas=2：代表著存在2份一樣的資料在 Data node，Data node允許著1台的故障容錯還有另一份資料可以正常運行
; EX:若 Data node 只有2台，所以設定2，再多沒有意義只是增加 write loading

; DataMemory & IndexMemory：代表著資料和索引可以儲存的記憶體容量有多大。

; NodeId：每一個 nodes 都必須擁有獨一無二的 id 值

[NDBD DEFAULT]
NoOfReplicas=2
DataMemory=1024M
IndexMemory=256M

[MYSQLD DEFAULT]

[NDB_MGMD DEFAULT]
DataDir=/var/lib/mysql-cluster

[TCP DEFAULT]

[NDB_MGMD]
NodeId=1
HostName=172.10.0.140

[MYSQLD]
NodeId=2
HostName=172.10.0.141

[MYSQLD]
NodeId=3
HostName=172.10.0.142

[NDBD]
NodeId=4
HostName=172.10.0.143
DataDir=/var/lib/mysql-cluster

[NDBD]
NodeId=5
HostName=172.10.0.144
DataDir=/var/lib/mysql-cluster
```

### Data node

```bash
vim /etc/my.cnf
```

```ini
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

### SQL node

```bash
# check permissions
adduser mysql -d /usr/local/mysql
chmod -R root.mysql /usr/local/mysql
chmod -R mysql /usr/local/mysql/data
vim /etc/my.cnf
```

```ini
[mysqld]
datadir=/var/lib/mysql
socket=/var/lib/mysql/mysql.sock
user=mysql
# Disabling symbolic-links is recommended to prevent assorted security risks
symbolic-links=0

ndbcluster
default_storage_engine=ndbcluster
ndb-connectstring=172.10.0.140:1186

[mysql_cluster]
ndb-connectstring=172.10.0.140:1186

[mysqld_safe]
log-error=/var/log/mysqld.log
pid-file=/var/run/mysqld/mysqld.pid

[client]
socket=/usr/local/mysql/data/mysql.sock
```

```bash
/usr/local/mysql/scripts/mysql_install_db --user=mysql
cp /usr/local/mysql/support-files/mysql.server /etc/init.d/mysql.server
```

### 啟動 Cluster 環境

```
啟動順序 Manager node > Data node > SQL node。

如果是第一次啟動 SQL node 請使用 –initial 初始化

!!!!!!
如果已經有資料請絕對不要使用 –initial 否則此 node 的資料全毀
!!!!!!
```

```bash
# in Manage node
ndb_mgmd -v -f /var/lib/mysql-cluster/config.ini

# in Data node , first use --initial
/usr/local/mysql/bin/ndbd --defaults-file=/etc/my.cnf -v

# in SQL node
/etc/init.d/mysql.server start

# 匯入資料庫(ndbcluster)
# cluster 的資料庫類型都必須為 ndbcluster，在匯入資料庫前必須改為 NDBCLUSTER
# Type=InnoDB
mysqldump -uroot -p db | sed 's/ENGINE=InnoDB/ENGINE=NDBCLUSTER/g' > db.sql
# or
# Type=MyISAM
mysqldump -uroot -p db | sed 's/ENGINE=MyISAM/ENGINE=NDBCLUSTER/g' > db.sql
```

```bash
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
```

# Percona XtraBackup(資料備份的工具)

## MySQL Percona innobackupex 和 xtrabackup 有何不同？

xtrabackup：
	檔案格式：備份 innodb、xtradb，不能備份 MyISAM
	innodb 不需要 LOCK 就可以備份
innobackupex：(裡面封裝 xtrabackup 的 script 在裡面)
	支援 innodb、xtradb (靠 xtrabackup) 和 MyISAM (主要做這個)
	MyISAM 備份時會做 READ LOCK
	通常直接使用 innobackupex 即可，若有確認資料庫沒有 MyISAM 格式的話，也可以直接使用 xtrabackup。


## 安裝 XtraBackup

```bash
# centos7
# 安裝 percona-release 的 yum 存儲庫
yum install https://repo.percona.com/yum/percona-release-latest.noarch.rpm -y
# 列出 可安裝套件
yum list | grep percona
# 安裝
yum install percona-xtrabackup-24 -y

# MacOS
brew install percona-xtrabackup

# ubuntu
wget https://repo.percona.com/apt/percona-release_1.0-25.generic_all.deb
dpkg -i percona-release_1.0-25.generic_all.deb
apt-get update
apt-get install percona-xtrabackup-24 -y
```

## 指令

### xtrabackup選項

```bash
# xtrabackup：是用於熱備份innodb, xtradb表中數據的工具，不能備份其他類型的表，也不能備份數據表結構；
xtrabackup
	# --apply-log-only
	# 	prepare備份的時候只執行redo階段，用於增量備份。
	# --backup
	# 	創建備份並且放入--target-dir目錄中
	# --close-files
	# 	不保持文件打開狀態，xtrabackup打開表空間的時候通常不會關閉文件句柄，目的是為了正確處理DDL操作。
	# 	如果表空間數量非常巨大並且不適合任何限制，一旦文件不在被訪問的時候這個選項可以關閉文件句柄.打開這個選項會產生不一致的備份。
	# --compact
	# 	創建一份沒有輔助索引的緊湊備份
	# --compress
	# 	壓縮所有輸出數據，包括事務日誌文件和元數據文件，通過指定的壓縮算法，目前唯一支持的算法是quicklz.結果文件是qpress歸檔格式，每個xtrabackup創建的*.qp文件都可以通過qpress程序提取或者解壓縮
	# --compress-chunk-size
	# 	壓縮線程工作buffer的字節大小，默認是64K
	# --compress-threads
	# 	xtrabackup進行並行數據壓縮時的worker線程的數量，該選項默認值是1，並行壓縮（'compress-threads'）可以和並行文件拷貝('parallel')一起使用。
	# 	例如:'--parallel=4 --compress --compress-threads=2'會創建4個IO線程讀取數據並通過管道傳送給2個壓縮線程。
	# --create-ib-logfile
	# 	這個選項目前還沒有實現，目前創建Innodb事務日誌，你還是需要prepare兩次。
	# --datadir=DIRECTORY
	# 	backup的源目錄，mysql實例的數據目錄。
	# 	從my.cnf中讀取，或者命令行指定。
	# --defaults-extra-file=[MY.CNF]
	# 	在global files文件之後讀取，必須在命令行的第一選項位置指定。
	# --defaults-file=[MY.CNF]
	# 	唯一從給定文件讀取默認選項，必須是個真實文件，必須在命令行第一個選項位置指定。
	# --defaults-group=GROUP-NAME
	# 	從配置文件讀取的組，innobakcupex多個實例部署時使用。
	# --export
	# 	為導出的表創建必要的文件
	# --extra-lsndir=DIRECTORY
	# 	(for --bakcup):在指定目錄創建一份xtrabakcup_checkpoints文件的額外的備份。
	# --incremental-basedir=DIRECTORY
	# 	創建一份增量備份時，這個目錄是增量別分的一份包含了full bakcup的Base數據集。
	# --incremental-dir=DIRECTORY
	# 	prepare增量備份的時候，增量備份在DIRECTORY結合full backup創建出一份新的full backup。
	# --incremental-force-scan
	# 	創建一份增量備份時，強制掃描所有增在備份中的數據頁即使完全改變的page bitmap數據可用。
	# --incremetal-lsn=LSN
	# 	創建增量備份的時候指定lsn。
	# --innodb-log-arch-dir
	# 	指定包含歸檔日誌的目錄。
	# 	只能和xtrabackup --prepare選項一起使用。
	# --innodb-miscellaneous
	# 	從My.cnf文件讀取的一組Innodb選項。
	# 	以便xtrabackup以同樣的配置啟動內置的Innodb。
	# 	通常不需要顯示指定。
	# --log-copy-interval
	# 	這個選項指定了log拷貝線程check的時間間隔（默認1秒）。
	# --log-stream
	# 	xtrabakcup不拷貝數據文件，將事務日誌內容重定向到標準輸出直到--suspend-at-end文件被刪除。
	# 	這個選項自動開啟--suspend-at-end。
	# --no-defaults
	# 	不從任何選項文件中讀取任何默認選項,必須在命令行第一個選項。
	# --databases
	# 	指定了需要備份的數據庫和表。
	# --database-file
	# 	指定包含數據庫和表的文件格式為databasename1.tablename1為一個元素，一個元素一行。
	# --parallel
	# 	指定備份時拷貝多個數據文件並發的進程數，默認值為1。
	# --prepare
	# 	xtrabackup在一份通過--backup生成的備份執行還原操作，以便準備使用。
	# --print-default
	# 	打印程序參數列表並退出，必須放在命令行首位。
	# --print-param
	# 	使xtrabackup打印參數用來將數據文件拷貝到datadir並還原它們。
	# --rebuild_indexes
	# 	在apply事務日誌之後重建innodb輔助索引，只有和--prepare一起才生效。
	# --rebuild_threads
	# 	在緊湊備份重建輔助索引的線程數，只有和--prepare和rebuild-index一起才生效。
	# --stats
	# 	xtrabakcup掃描指定數據文件並打印出索引統計。
	# --stream=name
	# 	將所有備份文件以指定格式流向標準輸出，目前支持的格式有xbstream和tar。
	# --suspend-at-end
	# 	使xtrabackup在--target-dir目錄中生成xtrabakcup_suspended文件。
	# 	在拷貝數據文件之後xtrabackup不是退出而是繼續拷貝日誌文件並且等待知道xtrabakcup_suspended文件被刪除。
	# 	這項可以使xtrabackup和其他程序協同工作。
	# --tables=name
	# 	正則表達式匹配database.tablename。
	# 	備份匹配的表。
	# --tables-file=name
	# 	--tables-file =/tmp/tbname.txt
	# 	指定文件，一個表名一行。
	# --target-dir=DIRECTORY
	# 	指定backup的目的地，如果目錄不存在，xtrabakcup會創建。
	# 	如果目錄存在且為空則成功。
	# 	不會覆蓋已存在的文件。
	# --throttle
	# 	指定每秒操作讀寫對的數量。
	# --tmpdir=name
	# 	當使用--print-param指定的時候打印出正確的tmpdir參數。
	# --to-archived-lsn=LSN
	# 	指定prepare備份時apply事務日誌的LSN，只能和xtarbackup --prepare選項一起用。
	# --user-memory=
	# 	通過--prepare prepare備份時候分配多大內存，目的像innodb_buffer_pool_size。
	# 	默認值100M如果你有足夠大的內存。
	# 	1-2G是推薦值，支持各種單位(1MB,1M,1GB,1G)。
	# --version
	# 	打印xtrabackup版本並退出。
	# --xbstream
	# 	支持同時壓縮和流式化。
	# 	需要客服傳統歸檔tar,cpio和其他不允許動態streaming生成的文件的限制，例如動態壓縮文件，xbstream超越其他傳統流式/歸檔格式的的優點是，並發stream多個文件並且更緊湊的數據存儲（所以可以和--parallel選項選項一起使用xbstream格式進行streaming）。
```

#### 用法範例

```bash
### 匯出
# xtrabackup完全複製
xtrabackup --user={$username} --password={$password} --backup --target-dir={$xtrabackup_path}

# $exclude_list 排除的table 例如： db1.table1 db2.table2 db2.table3
xtrabackup --user={$username} --password={$password} --databases-exclude={$exclude_list} --backup --target-dir={$xtrabackup_path}

### 匯入
# 停止mysql
service mysql stop
systemctl stop mysqld

# 備份mysql目錄(可選)
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
```

### innobackupex選項

```bash
innobackupex

	# --apply-log
	# 	該選項表示同xtrabackup的--prepare參數，一般情況下，在備份完成後，數據還不能用於恢復操作，因為備份的數據中可能會包含尚未提交的事務或已經提交但尚未同步至數據文件中的事務。
	# 	因此，此時數據文件仍處理不一致狀態。
	# 	--apply-log(準備)的作用是通過回滾未提交的事務及同步已經提交的事務至數據文件使數據文件處於一致性狀態。
	# 	對xtrabackup的--prepare參數的封裝
	# --backup-locks
	# 	僅支持percona server5.6，如果server不支持，開啟不讀私人和產生影響
	# --close-files
	# 	2.2.5引入的新特性
	# 	關閉不再訪問的文件句柄，這個選項直接傳遞給xtrabackup，當xtrabackup打開表空間通常並不關閉文件句柄目的是正確的處理DDL操作。
	# 	如果表空間數量巨大，這是一種可以關閉不再訪問的文件句柄的方法。
	# 	使用該選項有風險，會有產生不一致備份的可能
	# --compact
	# 	創建一份沒有輔助索引的緊湊的備份，該選項直接傳遞給xtrabackup
	# --compress
	# 	該選項指導xtrabackup壓縮innodb數據文件的backup的拷貝，直接傳遞給xtrabackup的子進程
	# --compress-threads
	# 	該選項指定並行壓縮的worker線程的數量，直接傳遞給xtrabackup的子進程
	# --compress-chunk-size
	# 	這個選項指定每個壓縮線程的內部worker buffer的大小。
	# 	單位是字節，默認是64K。
	# 	直接傳遞給xtrabackup子進程
	# --copy-back
	# 	執行還原操作，從備份目錄中最近的一份備份中拷貝所有文件到datadir，innobackupex --copy-back選項除非指定innobackupex --force-non-empty-directories選項，否則不會拷貝覆蓋所有的文件
	# --databases=LIST
	# 	指定innoabckupex備份的DB列表，該選項接受一個一個字符串參數或者包含DB列表的文件的全路徑。
	# 	如果沒有指定該選項，所有包含innodb和myam表的DB會被備份，請確認--databases包含所有的innodb數據庫和表，以便所有的innodb.frm文件也同樣備份，如果列表非常長的話。
	# 	可以以文件代替
	# --decompress
	# 	解壓所有值錢通過--compress選項壓縮成的.qp文件。
	# 	innodbakcupex --parallel選項允許多個文件同時解壓。
	# 	為了解壓，qpress工具必須有安裝並且訪問這個文件的權限。
	# 	這個進程將在同一個位置移除原來的壓縮/加密文件
	# --decrypt=ENCRYPTION-ALGORITHM
	# 	解密所有之前通過--encrypt選項加密的.xbcrypt文件。
	# --innobackup --parallel選項允許同時多個文件解密
	# --defaults-file=[MY.CNF]
	# 	該選項指定了從哪個文件讀取MySQL配置，必須放在命令行第一個選項的位置
	# --defaults-extra-file=[MY.CNF]
	# 	指定了在標準defaults-file之前從哪個額外的文件讀取MySQL配置，必須在命令行的第一個選項的位置
	# --default-group=GROUP-NAME
	# 	這個選項接受了一個字符串參數指定讀取配置文件的group，在一機多實例的時候需要指定
	# --encrypt=ENCRYPTION_ALGORITHM
	# 	該選項指定了xtrabackup通過ENCRYPTION_ALGORITHM的算法加密innodb數據文件的備份拷貝，該選項直接傳遞給xtrabackup子進程
	# --encrypt-key=ENCRYPTION_KEY
	# 	指導xtrabackup使用了--encrypt選項時候使用ENCRYPTION_KEY這個KEY，直接傳遞給xtrabackup子進程
	# --encrypt-key-file=ENCRYPTION_KEY_FILE
	# 	這個選項告訴xtrabackup使用--encrypt的時候。
	# Key存在了ENCRYPTION_KEY_FILE這個文件中
	# --encrypt-chunk-size=#
	# 	這個選項指定了每個加密線程內部worker buffer的大小，單位字節，直接傳遞給xtrabackup子進程
	# --export=DIRECTORY
	# 	這個選項直接傳遞給xtrabackup --export選項。
	# 開啟可導出單獨的表之後再導入其他Mysql中
	# --extra-lsndir=DIRECTORY
	# 	這個選項接受一個字符串參數指定保存額外一份xtrabackup_checkpoints文件的目錄，直接傳遞給xtrabackup --extra-lsndir選項
	# --force-non-empty-directories
	# 	指定該參數時候，使得innobackupex --copy-back或innobackupex --move-back選項轉移文件到非空目錄，已存在的文件不會被覆蓋，如果--copy- back和--move-back文件需要從備份目錄拷貝一個在datadir已經存在的文件，會報錯失敗
	# --galera-info
	# 	該選項生成了包含創建備份時候本地節點狀態的文件xtrabackup_galera_info文件，該選項只適用於備份PXC。
	# --history=NAME
	# 	percona server5.6的備份歷史記錄在percona_schema.xtrabackup_history表
	# --host=HOST
	# 	選項指定了TCP/IP連接的數據庫實例IP
	# --ibbackup=IBBACKUP-BINARY
	# 	這個選項指定了使用哪個xtrabackup二進製程序。IBBACKUP-BINARY是運行percona xtrabackup的命令。
	# 	這個選項適用於xtrbackup二進制不在你是搜索和工作目錄，如果指定了該選項，innoabackupex自動決定用的二進製程序
	# --include=REGEXP
	# 	正則表達式匹配表的名字[db.tb]，直接傳遞給xtrabackup --tables選項。
	# --incremental
	# 	這個選項告訴xtrabackup創建一個增量備份，直接傳遞給xtrabakcup子進程，當這個選項指定，需要同時指定--incremental-lisn或者--incremental-basedir。
	# 如果沒有指定，默認傳給xtrabackup --incremental-basedir，值為Backup BASE目錄中的第一個時間戳目錄
	# --incremental-basedir=DIRECTORY
	# 	這個選項接受了一個字符串參數指定含有full backup的目錄為增量備份的base目錄，與--incremental同時使用
	# --incremental-dir=DIRECTORY
	# 	指定了增量備份的目錄，結合full backup生成生成一份新的full bakcup
	# --incremettal-history-name=NAME
	# 	這個選項指定了存儲在PERCONA_SCHEMA.xtrabackup_history基於增量備份的歷史記錄的名字。Percona Xtrabackup搜索歷史表查找最近（innodb_to_lsn）成功備份並且將to_lsn值作為增量備份啟動出事lsn.與innobackupex--incremental-history-uuid互斥。如果沒有檢測到有效的lsn，xtrabackup會返回error
	# --incremetal-history-uuid=UUID
	# 	這個選項指定了存儲在percona_schema.xtrabackup_history基於增量備份的特定歷史記錄的UUID
	# --incremental-lsn=LSN
	# 	這個選項指定增量備份的LSN，與--incremental選項一起使用
	# --kill-long-queries-timeout=SECONDS
	# 	這個選項指定innobackupex從開始執行FLUSH TABLES WITH READ LOCK到kill掉阻塞它的這些查詢之間等待的秒數，默認值為0.以為著Innobakcupex不會kill任何查詢，使用這個選項xtrabackup需要有Process和super權限。

	# --kill-long-query-type=all|select
	# 	指定kill的類型，默認是all
	# --ftwrl-wait-timeout=SECONDS
	# 	執行FLUSH TABLES WITH READ LOCK之前，innobackupex等待阻塞查詢執行完成等待秒數，超時的時候如果查詢仍然沒有執行完，innobackupex會終止並報錯，默認為0，innobakcupex不等待查詢完成立刻FLUSH
	# --ftwrl-wait-threshold=SECONDS
	# 	指定innoabckupex檢測到長查詢和innobackupex --ftwrl-wait-timeount不為0，這個長查詢可以運行的閾值，
	# --ftwrl-wait-query-type=all|update
	# 	指定innobakcupex獲得全局鎖之前允許那種查詢完成，默認是ALL
	# --log-copy-interval=
	# 	這個選項指定了每次拷貝log線程完成檢查之間的間隔（毫秒）
	# --move-back
	# 	從備份目錄中將最近一份備份中的所有文件移動到datadir目錄中
	# --no-lock
	# 	關閉FTWRL的表鎖，只有在你所有表都是Innodb表並且你不關心backup的binlog pos點
	# 	如果有任何DDL語句正在執行或者非InnoDB正在更新時（包括mysql庫下的表） ，都不應該使用這個選項，後果是導致備份數據不一致
	# 	如果考慮備份因為獲得鎖失敗，，可以考慮--safe-slave-backup立刻停止複制線程
	# --no-timestamp
	# 	這個選項阻止在BACKUP-ROOT-DIR裡創建一個時間戳子目錄，指定了該選項的話，備份在BACKUP-ROOT-DIR完成
	# --no-version-check
	# 	這個選項禁用由--version-check打開的version check
	# --parallel=NUMBER-OF-THREADS
	# 	指定xtrabackup並行複制的子進程數。
	# 	注意是文件級別並行，如果有多個ibd文件，他們會並行拷貝，如果所有的表存在一個表空間文件中，沒有任何作用
	# 	直接傳遞給xtrabakcup --parallel選項
	# --password = PASSWORD
	# --port = PORT
	# --rebuild-indexes
	# 	與--apply-log一起用時候才有效。
	# 	並且直接傳遞給xtrabackup，在apply log之後重建所有輔助索引，該選項用於Prepare緊湊備份。
	# --rebuild-threads=NUMBER-OF-THREADS
	# 	與--apply-log和--rebuild-index選項一起用時候才生效，重建索引的時候，xtrabacup以指定的線程數並行的處理表空間文件
	# --redo-only
	# 	這個選項在prepare base full backup，往其中merge增量備份（但不包括最後一個）時候使用。
	# 	傳遞給xtrabackup --apply-log-only的選項。
	# 	這個強制xtrabackup跳過rollback並且只重做redo
	# --rsync
	# 	通過rsync工具優化本地傳輸，當指定這個選項，innobackupex使用rsync拷貝非Innodb文件而替換cp，當有很多DB和表的時候會快很多。
	# 	不能--stream一起使用
	# --safe-slave-backup
	# 	指定的時候innobackupex會在執行FLUSH TABLES WITH READ LOCK停止sql線程，並且直到show status裡slave_open_temp_tables的值為0的時候start backup。
	# 	如果沒有打開的臨時表，就開始備份，否則sql線程start或者stop直到沒有打開的臨時表，如果在innobackupex --safe-slave-backup-timeout之後slave_open_temp_tables的值仍沒有變成0備份就會失敗。
	# 	SQL線程會在backup完成之後重啟。
	# --safe-slave-backup-timeout=SECONDS
	# 	innobackupex --safe-slave-backup應該等多少秒等slave_open_temp_tables變成0，默認是300秒
	# --scpopt=SCP-OPTIONS
	# 	當--remost-host指定的時候，指定傳給scp的命令行選項。
	# 	如果沒有指定，默認為-Cp -c arcfour
	# --slave-info
	# 	對slave進行備份的時候使用，打印出master的名字和binlog pos，同樣將這些信息以change master的命令寫入xtrabackup_slave_info文件。
	# 	可以通過基於這份備份啟動一個從庫並且保存在xtrabackup_slave_info文件中的binlog pos點創建一個新的從庫
	# --socket
	# 	連接本地實例的時候使用
	# --sshopt=SSH-OPTIONS
	# 	在指定了--remost-host的時候，指定傳給ssh的命令行選項
	# --stream=STREAMNAME
	# 	流式備份的格式，backup完成之後以指定格式到STDOUT，目前只支持tar和xbstream。
	# 	使用xbstream為percona xtrabakcup髮型版本，如果在這個選項之後指定了路徑。
	# 	會理解值為tmpdir
	# --tables-file=FILE
	# 	指定含有表列表的文件，格式為database.table，該選項直接傳給xtrabackup --tables-file
	# --throttle=IOS
	# 	指定每秒IO操作的次數，直接傳遞給xtrabackup --throttle選項。
	# 	只作用於bakcup階段有效。
	# 	apply-log和--copy-back不生效不要一起用
	# --tmpdir=DIRECTORY
	# 	指定--stream的時候，指定臨時文件存在哪裡，在streaming和拷貝到遠程server之前，事務日誌首先存在臨時文件裡。
	# --use-memory=#
	# 	只能和--apply-log選項一起使用，prepare a backup的時候，xtrabackup做crash recovery分配的內存大小，單位字節。
	# 	也可(1MB、1M、1G、1GB)，直接傳給xtrabackup --use-memory選項
	# --version
	# 	顯示Innobackupex版本和版權信息後退出
	# --version-check
	# 	innobackupex在與server創建連接之後的備份階段進行版本檢查
```

### 備份(即時備份)步驟

```bash
###############
### 來源主機 ###
###############
# master全表鎖定只讀 - mysql指令
FLUSH TABLES WITH READ LOCK;

# 檢查master是否lock - mysql指令
# bin_log資料(紀錄file和position)
SHOW MASTER STATUS;
+------------------+----------+--------------+------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000001 |     2584 |              |                  |                   |
+------------------+----------+--------------+------------------+-------------------+
# {下方設定 $master bin_log filename} = mysql-bin.000001
# {下方設定 $log position} = 2584

# 備份(可先lock MySQL Table(表))
xtrabackup --user={$username} --password={$password} --backup --target-dir={$xtrabackup_path}

# 將/data目錄傳到mysql-slave機器
rsync -Pr {$xtrabackup_path} root@{$slave ip}:{$xtrabackup_path}

# 解開lock - mysql指令
UNLOCK TABLES;

###############
### 目標主機 ###
###############
# 停止mysql
service mysql stop

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

# 排除部分資料表
xtrabackup --user=$MYSQLDB_USER --password=$MYSQLDB_PASS --databases-exclude="db1.table1 db2.table2" --backup --target-dir={$xtrabackup_path}

# mysql目錄權限修改
chown -R mysql.mysql /var/lib/mysql

# 啟動mysql
service mysql start
```

## 說明

```
Xtrabackup是一個對InnoDB做資料備份的工具，支援線上熱備份(備份時不影響資料讀寫)，是商業備份工具InnoDB Hotbackup的一個很好的替代品。
Xtrabackup有兩個主要的工具：xtrabackup、innobackupex

    xtrabackup只能備份InnoDB和XtraDB兩種資料表，而不能備份MyISAM資料表
    innobackupex是參考了InnoDB Hotbackup的innoback指令碼修改而來的.innobackupex是一個perl指令碼封裝，封裝了xtrabackup。主要是為了方便的 同時備份InnoDB和MyISAM引擎的表，但在處理myisam時需要加一個讀鎖。並且加入了一些使用的選項。如slave-info可以記錄備份恢 復後，作為slave需要的一些資訊，根據這些資訊，可以很方便的利用備份來重做slave。

Xtrabackup可以做

    線上(熱)備份整個庫的InnoDB、 XtraDB表

在xtrabackup的上一次整庫備份基礎上做增量備份(innodb only)
以流的形式產生備份，可以直接儲存到遠端機器上(本機硬碟空間不足時很有用)

MySQL資料庫本身提供的工具並不支援真正的增量備份，二進位制日誌恢復是point-in-time(時間點)的恢復而不是增量備份。
Xtrabackup工具支援對InnoDB儲存引擎的增量備份，工作原理如下：

(1)首先完成一個完全備份，並記錄下此時檢查點的LSN(Log Sequence Number)。
(2)在程序增量備份時，比較表空間中每個頁的LSN是否大於上次備份時的LSN，如果是，則備份該頁，同時記錄當前檢查點的LSN。
首 先，在logfile中找到並記錄最後一個checkpoint(“last checkpoint LSN”)，然後開始從LSN的位置開始拷貝InnoDB的logfile到xtrabackup_logfile接著，開始拷貝全部的資料文 件.ibd在拷貝全部資料檔案結束之後，才停止拷貝logfile。

因為logfile裡面記錄全部的資料修改情況，所以，即時在備份過程中資料檔案被修改過了，恢復時仍然能夠通過解析xtrabackup_logfile保持資料的一致。
```

### innobackupex 對比 xtrabackup

```
xtrabackup 僅複製 InnoDB 數據和日誌。它不會復製表定義文件（.frm 文件）、MyISAM 數據、用戶、權限或位於 InnoDB 數據之外的整個數據庫的任何其他部分。要備份這些數據，您需要一個包裝腳本，例如 innobackupex。
```