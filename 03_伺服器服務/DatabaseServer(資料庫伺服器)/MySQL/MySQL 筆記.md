# MySQL 筆記

```
```

## 目錄

- [MySQL 筆記](#mysql-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [使用者權限相關](#使用者權限相關)
		- [指令相關](#指令相關)
		- [安裝相關](#安裝相關)
		- [Master-Slave(主從環境)相關](#master-slave主從環境相關)
		- [Percona XtraBackup(備份工具)相關](#percona-xtrabackup備份工具相關)
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
- [Master-Slave 主從架構](#master-slave-主從架構)
	- [自行架設成功的步驟](#自行架設成功的步驟)
	- [公司文檔 步驟](#公司文檔-步驟)
		- [1.mysql-master設定](#1mysql-master設定)
		- [mysql-master server\_id和log\_bin變數正確跳至第一步-建立master-slave使用者](#mysql-master-server_id和log_bin變數正確跳至第一步-建立master-slave使用者)
		- [2.mysql-slave設定](#2mysql-slave設定)
		- [mysql-slave server\_id和read\_only變數正確跳至第三步](#mysql-slave-server_id和read_only變數正確跳至第三步)
		- [mysql-master 加入下列到 my.cnf](#mysql-master-加入下列到-mycnf)
		- [3.備份mysql-master](#3備份mysql-master)
- [例外狀況](#例外狀況)
	- [MySQL 除錯 - 修復損壞的innodb：innodb\_force\_recovery](#mysql-除錯---修復損壞的innodbinnodb_force_recovery)
	- [MySQL 除錯 - \[Warning\] IP address 'xxx.xxx.xxx.xxx' could not be resolved- Name or service not known](#mysql-除錯---warning-ip-address-xxxxxxxxxxxx-could-not-be-resolved--name-or-service-not-known)
	- [MySQL 除錯 - Table 'db.table' doesn't exist (1146)](#mysql-除錯---table-dbtable-doesnt-exist-1146)
- [Percona XtraBackup(資料備份的工具)](#percona-xtrabackup資料備份的工具)
	- [MySQL Percona innobackupex 和 xtrabackup 有何不同？](#mysql-percona-innobackupex-和-xtrabackup-有何不同)
	- [安裝 XtraBackup](#安裝-xtrabackup)
	- [指令](#指令-1)
		- [備份(即時備份)步驟](#備份即時備份步驟)
	- [說明](#說明)

## 參考資料

[SQL筆記](../../../../../01_程式語言/SQL/SQL%20筆記.md)

[MySQl官方網站](https://dev.mysql.com/)

[MySQL 全部文檔](https://dev.mysql.com/doc/)

[MySQL 8.0版本 指令](https://dev.mysql.com/doc/refman/8.0/en/mysql-command-options.html)

[MySQL 5.7版本 文檔](https://dev.mysql.com/doc/refman/5.7/en/)

### 使用者權限相關

[MySQL / MariaDB 移除使用者帳號及權限](https://ithelp.ithome.com.tw/articles/10235980)

[權限說明](https://rosalie1211.blogspot.com/2019/03/mysql.html)

[淺談MySQL中授權(grant)和撤銷授權(revoke)用法詳解](https://www.itread01.com/articles/1476680778.html)

[只談MySQL (第四天) 帳號與權限](https://ithelp.ithome.com.tw/articles/10029835)

[6.2.2 Privileges Provided by MySQL](https://dev.mysql.com/doc/refman/8.0/en/privileges-provided.html#priv_all)

### 指令相關

[mysqlimport](https://dev.mysql.com/doc/refman/8.0/en/mysqlimport.html)

[mysql匯入匯出sql檔案](https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/38165/)

[mysql匯入匯出sql檔案 - workbench操作步驟](https://blog.hungwin.com.tw/mysql-workbench-backup/#i-8)

### 安裝相關

[CENTOS 7 安裝mysql](https://kirby86a.pixnet.net/blog/post/118006518-centos-7-%E5%AE%89%E8%A3%9Dmysql)

[在CentOS7上安裝MySQL5.7](https://dotblogs.com.tw/tinggg01/2018/07/06/153413)

[Unknown table 'COLUMN_STATISTICS' in information_schema (1109)](https://serverfault.com/questions/912162/mysqldump-throws-unknown-table-column-statistics-in-information-schema-1109)

### Master-Slave(主從環境)相關

[MySQL Replication 主從式架構設定教學](https://blog.toright.com/posts/5062/mysql-replication-%E4%B8%BB%E5%BE%9E%E5%BC%8F%E6%9E%B6%E6%A7%8B%E8%A8%AD%E5%AE%9A%E6%95%99%E5%AD%B8.html)

[docker-compose搭建mysql主從環境](https://www.uj5u.com/ruanti/275444.html)

[docker-compose搭建mysql主從環境](https://hub.docker.com/r/bitnami/mysql)

### Percona XtraBackup(備份工具)相關

[percona 官網](https://www.percona.com/software/documentation)

[Running Percona XtraBackup in a Docker container](https://www.percona.com/doc/percona-xtrabackup/2.4/installation/docker.html)

[bitnami/percona-xtrabackup](https://hub.docker.com/r/bitnami/percona-xtrabackup/)

[Xtrabackup介紹](https://www.itread01.com/content/1547450246.html)

[xtrabackup - 手冊頁](https://www.mankier.com/1/xtrabackup#)

[MySQL Percona innobackupex 和 XtraBackup 有何不同？](https://blog.longwin.com.tw/2022/09/mysql-percona-innobackupex-xtrabackup-different-2022/)

[xtrabackup 選項參考](https://docs.percona.com/percona-xtrabackup/2.4/xtrabackup_bin/xbk_option_reference.html)

[innobackupex 選項參考](https://docs.percona.com/percona-xtrabackup/2.4/innobackupex/innobackupex_option_reference.html)

[Can I backup remote databases from my local server - 我可以從本地服務器備份遠程數據庫嗎](https://forums.percona.com/t/can-i-backup-remote-databases-from-my-local-server/2334/3)

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
# 	所有資料庫
# 	--all-databases
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
# 恢復資料庫
mysql -h(ip) -uroot -p(password) databasename< database.sql
# 複製資料庫
mysqldump –all-databases >all-databases.sql
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
mysql>use abc;

# 設定資料庫編碼
mysql>set names utf8;

# 匯入資料（注意sql檔案的路徑）
mysql>source /home/abc/abc.sql;

`方法二`
# mysql -u使用者名稱 -p密碼 資料庫名 < 資料庫名.sql
mysql -uabc_f -p abc < abc.sql
```

# 配置文檔

[mysql 優化技巧心得一(key_buffer_size設定)](https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/410436/)


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

# Master-Slave 主從架構

```
MYSQL_REPLICATION_MODE: 複製模式。可能的值master/ slave。沒有默認值。
MYSQL_REPLICATION_USER：第一次運行時在主服務器上創建的複制用戶。沒有默認值。
MYSQL_REPLICATION_PASSWORD: 複製用戶密碼。沒有默認值。
MYSQL_MASTER_HOST: 複製主機的主機名/IP（從參數）。沒有默認值。
MYSQL_MASTER_PORT_NUMBER: 複製master的服務器端口（slave參數）。默認為3306.
MYSQL_MASTER_ROOT_USER：複製主機上的用戶可以訪問MYSQL_DATABASE（從參數）。默認為root
MYSQL_MASTER_ROOT_PASSWORD：複製主機上的用戶密碼，可以訪問MYSQL_DATABASE（從參數）。沒有默認值。
```

## 自行架設成功的步驟

進入MySQL

```sql
SHOW VARIABLES LIKE 'Position%';

-- ======== master server 部分 ========

-- 確認mysql-master server_id和log_bin變數
SHOW VARIABLES LIKE 'server_id%';
SHOW VARIABLES LIKE 'log_bin%';

-- 顯示master狀態：該File列顯示日誌文件的名稱並Position顯示文件中的位置。
-- 記錄這些值。稍後在設置副本時需要它們。
-- 它們表示副本應開始處理來自源的新更新的複制坐標。
SHOW MASTER STATUS;

-- 建立master-slave使用者：要創建新帳戶，請使用CREATE USER。
-- 要授予此帳戶複製所需的權限，請使用該GRANT 語句。
-- 如果僅為複制目的創建帳戶，則該帳戶只需要 REPLICATION SLAVE權限。
-- 例如，要設置一個repl可以從example.com域內的任何主機連接以進行複制 的新用戶
CREATE USER 'user'@'host' IDENTIFIED BY 'password';
GRANT REPLICATION SLAVE ON *.* TO 'user'@'host';

-- 列出所有使用者帳號
SELECT User,Host FROM mysql.user;

-- ======== slave server 部分 =====

-- 檢查mysql-slave server_id和read_only變數
SHOW VARIABLES LIKE 'server_id%';
SHOW VARIABLES LIKE 'read_only%';

-- 新增mysql-slave設定master資料 綁定到master
CHANGE MASTER TO MASTER_HOST = 'master ip',
MASTER_PORT = 3306,
MASTER_USER = 'user',
MASTER_PASSWORD = 'password',

-- 在master server時，輸入 SHOW MASTER STATUS 找出的資訊;
MASTER_LOG_FILE = 'master bin_log filename',
MASTER_LOG_POS = 'log position';

-- 確認slave狀態：SHOW SLAVE STATUS，注意：Slave_IO_Running: Yes、Slave_SQL_Running: Yes
SHOW SLAVE STATUS\G

-- 執行slave
START SLAVE;
```

## 公司文檔 步驟

前置安裝 xtrabackup2.4

```bash
# centos7
install xtrabackup2.4
yum install https://repo.percona.com/yum/percona-release-latest.noarch.rpm
yum list | grep percona
# percona-xtrabackup-24 MySQL 大量資料時 可用工具
yum install percona-xtrabackup-24

# ubuntu
wget https://repo.percona.com/apt/percona-release_1.0-25.generic_all.deb
dpkg -i percona-release_1.0-25.generic_all.deb
apt-get update
apt-get install percona-xtrabackup-24 -y
```

### 1.mysql-master設定

```bash
# 登入mysql
mysql -u{$username} -p{$password}
```

```sql
-- 確認mysql-master server_id和log_bin變數
show variables like 'server_id%';
show variables like  'log_bin%';
```

### mysql-master server_id和log_bin變數正確跳至第一步-建立master-slave使用者

```bash
# 停止mysql
service mysql stop
```

mysql-master 加入下列到 /etc/my.cnf

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
show variables like  'log_bin%';

-- 建立master-slave使用者
CREATE USER 'replication'@'192.168%' IDENTIFIED BY '.wFb9A?$9*WN';
GRANT REPLICATION SLAVE ON *.* TO 'replication'@'192.168%';

-- 檢查使用者
select User, Host From mysql.user;
```

### 2.mysql-slave設定

```bash
# 登入mysql
mysql -u{$username} -p{$password}
```

```sql
-- 檢查mysql-slave server_id和read_only變數
show variables like 'server_id%';
show variables like  'read_only%';
```

### mysql-slave server_id和read_only變數正確跳至第三步

```bash
# 停止mysql
service mysql stop
```

### mysql-master 加入下列到 my.cnf

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
show variables like  'read_only%';
```

### 3.備份mysql-master

```sql
-- master全表鎖定只讀
FLUSH TABLES WITH READ LOCK;

-- 檢查master是否lock
```

# 例外狀況

## MySQL 除錯 - 修復損壞的innodb：innodb_force_recovery

[MySQL崩潰-如何修復損壞的innodb：innodb_force_recovery](https://www.twblogs.net/a/5b8201762b71772165af295d)

```bash
# CentOS 7
cat /var/log/mysqld.log

查看錯誤日誌：
InnoDB: Error: could not open single-table tablespace file ./data_dep/report.ibd

innodb引擎出了問題
```

## MySQL 除錯 - [Warning] IP address 'xxx.xxx.xxx.xxx' could not be resolved- Name or service not known

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

## MySQL 除錯 - Table 'db.table' doesn't exist (1146)

```sql
-- 檢查原因
mysql> check table db.table;
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
# 安裝
yum install percona-xtrabackup-24 -y

# MacOS
brew install percona-xtrabackup
```

## 指令

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
