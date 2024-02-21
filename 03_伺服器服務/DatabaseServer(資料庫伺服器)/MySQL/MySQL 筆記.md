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
    - [使用者權限相關](#使用者權限相關)
    - [安裝相關](#安裝相關)
      - [Docker相關](#docker相關)
    - [操作相關](#操作相關)
    - [備份相關](#備份相關)
      - [備份指令相關](#備份指令相關)
      - [解說相關](#解說相關)
    - [架構相關](#架構相關)
    - [錯誤處理相關](#錯誤處理相關)
- [安裝步驟](#安裝步驟)
  - [配置文檔](#配置文檔)
  - [MacOS](#macos)
  - [CentOS7](#centos7)
  - [安裝 MySQL 工具](#安裝-mysql-工具)
    - [Debian (Ubuntu)](#debian-ubuntu)
    - [RedHat (CentOS)](#redhat-centos)
    - [Homebrew (MacOS)](#homebrew-macos)
- [資料型態](#資料型態)
- [指令](#指令)
  - [查看資訊](#查看資訊)
    - [查看 MySQL 使用容量](#查看-mysql-使用容量)
  - [服務操作](#服務操作)
  - [SQL 指令](#sql-指令)
    - [使用者相關](#使用者相關)
    - [密碼設定強度修改](#密碼設定強度修改)
    - [許可權 列表](#許可權-列表)
  - [匯出匯入](#匯出匯入)
    - [匯出 - mysqldump](#匯出---mysqldump)
    - [匯入](#匯入)
  - [測試用](#測試用)
    - [模擬長時間連線](#模擬長時間連線)
      - [引入延遲（睡眠）](#引入延遲睡眠)
      - [確保在某個事務中選定的行在該事務完成之前不會被其他事務修改](#確保在某個事務中選定的行在該事務完成之前不會被其他事務修改)
      - [START TRANSACTION;](#start-transaction)
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

[mysql/docker-healthcheck](https://github.com/docker-library/healthcheck/blob/40afbf64d69cf933af0da4df6383958a29113601/mysql/docker-healthcheck)

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

### 架構相關

[MySQL 中常见的几种高可用架构部署方案解析](https://www.jb51.net/article/281852.htm)

### 錯誤處理相關

[[MySQL] SQL_ERROR 1032解决办法](https://www.cnblogs.com/langdashu/p/5920436.html)

[MySQL warning "IP address could not be resolved"](https://serverfault.com/questions/393862/mysql-warning-ip-address-could-not-be-resolved)

[Is DNS the Achilles heel in your MySQL installation?](https://www.percona.com/blog/2008/05/31/dns-achilles-heel-mysql-installation/)

[MySQL崩潰-如何修復損壞的innodb：innodb_force_recovery](https://www.twblogs.net/a/5b8201762b71772165af295d)

[解决 MySQL 报错 “ Column count of mysql.user is wrong...”](https://cloud.tencent.com/developer/article/1662598)

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


brew install -y mysql

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

## 安裝 MySQL 工具

### Debian (Ubuntu)

```bash
apt-get update
apt-get install mysql-client mysql-server
```

### RedHat (CentOS)

```bash
yum install mysql
```

### Homebrew (MacOS)

```bash
brew install mysql
```

# 資料型態

`數值型態`

```
TINYINT
SMALLINT
MEDIUMINT
INT 或 INTEGER
BIGINT
```

`浮點數型態`

```
FLOAT
DOUBLE
DECIMAL 或 NUMERIC
```

`日期型態`

```
DATE
```

`時間型態`

```
TIME
```

`日期和時間型態`

```
DATETIME
TIMESTAMP
YEAR
```

`定長字串型態`

```
CHAR
```

`可變長字串型態`

```
VARCHAR
```

`文本型態`

```
TINYTEXT
TEXT
MEDIUMTEXT
LONGTEXT
```

`二進制型態`

```
BINARY
VARBINARY
TINYBLOB
BLOB
MEDIUMBLOB
LONGBLOB
```

`枚舉型態`

```
ENUM
```

`集合型態`

```
SET
```

`JSON 型態`

```
JSON
```

`幾何型態`

```
GEOMETRY
POINT
LINESTRING
POLYGON
```

# 指令

## 查看資訊

### 查看 MySQL 使用容量

```bash
du -h /var/lib/mysql
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

-- 顯示資料庫 細節
-- https://dev.mysql.com/doc/refman/8.0/en/show-create-database.html
SHOW CREATE DATABASE db_name\G

-- 顯示資料庫 字符集和排序規則
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME
FROM INFORMATION_SCHEMA.SCHEMATA
WHERE SCHEMA_NAME = 'your_database_name';

-- 顯示表 字符集和排序規則
SELECT TABLE_NAME, TABLE_COLLATION
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'your_database_name';

-- 查看表的結構（列信息等）
DESCRIBE your_table_name;

-- 修改整個數據庫的字符集和排序規則
ALTER DATABASE your_database_name
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 修改表字符集和排序規則
ALTER TABLE your_table_name
CONVERT TO CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 建立庫
CREATE DATABASE [數據庫名];

-- 建立庫 指定字符集和排序規則
CREATE DATABASE your_database_name
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 刪除庫
DROP DATABASE 庫名;

-- 使用庫
USE 庫名;

-- 建立表
create table 表名 (欄位設定列表);
CREATE TABLE test (First_Name char(50),Last_Name char(50));

CREATE TABLE table_name (
    column1 datatype1,
    column2 datatype2,
    ...
);

-- 指定主鍵（Primary Key）
CREATE TABLE table_name (
    column1 datatype1 PRIMARY KEY,
    column2 datatype2,
    ...
);

-- 指定自動遞增的列（Auto-increment）
CREATE TABLE table_name (
    id INT AUTO_INCREMENT PRIMARY KEY,
    column1 datatype1,
    column2 datatype2,
    ...
);

-- 設定默認值（Default Value）
CREATE TABLE table_name (
    column1 datatype1 DEFAULT default_value,
    column2 datatype2 DEFAULT default_value,
    ...
);

-- 指定外鍵（Foreign Key）
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    order_date DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- 設定索引（Index）
CREATE TABLE table_name (
    column1 datatype1,
    column2 datatype2,
    ...
    INDEX index_name (column1, column2, ...);
);

-- 指定表格引擎（Table Engine）
CREATE TABLE table_name (
    column1 datatype1,
    column2 datatype2,
    ...
) ENGINE=InnoDB;

-- 設定字符集（Character Set）和校對規則（Collation）
CREATE TABLE table_name (
    column1 datatype1,
    column2 datatype2,
    ...
) CHARACTER SET utf8 COLLATE utf8_general_ci;

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
# 匯出資料和表結構
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

mysqldump -h hostname -u使用者名稱 -p密碼 資料庫名 > 資料庫名.sql

	由於在 mysqldump 8 中默認啟用了一個新標誌。您可以通過添加 --column-statistics=0 來禁用它。
	--column-statistics=0
	多個資料庫
	--databases db1 db2
	在每個創建數據庫表語句前添加刪除數據庫表的語句
	--add-drop-table
	備份數據庫表時鎖定數據庫表
	--add-locks
	備份MySQL服務器上的所有數據庫
	--all-databases
	添加註釋信息
	--comments
	壓縮模式，產生更少的輸出
	--compact
	輸出完成的插入語句
	--complete-insert
	指定要備份的數據庫
	--databases -d
	指定默認字符集
	--default-character-set
	當出現錯誤時仍然繼續備份操作
	--force
	指定要備份數據庫的服務器
	--host -h
	備份前，鎖定所有數據庫表
	--lock-tables
	禁止生成創建數據庫語句
	--no-create-db
	禁止生成創建數據庫庫表語句
	--no-create-info
	連接MySQL服務器的密碼
	--password -p
	MySQL服務器的端口號
	--port
	連接MySQL服務器的用戶名。
	--user -u
    不包含資料
    -d
    是 MySQL 的參數，用於在備份過程中處理 GTID（全域事務識別碼）。 GTID 用於在主從複製環境中追蹤事務。
    --set-gtid-purged

# 只匯出表結構
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

## 測試用

### 模擬長時間連線

```sql
START TRANSACTION;
SELECT * FROM database.table WHERE id = 1 FOR UPDATE;
SELECT SLEEP(100);
```

#### 引入延遲（睡眠）

```sql
SELECT SLEEP(n);
```

```sql
-- 導致 MySQL 休眠 100 秒
SELECT SLEEP(100);
```

#### 確保在某個事務中選定的行在該事務完成之前不會被其他事務修改

這個SQL 查詢語句是一個帶有FOR UPDATE子句的SELECT查詢，用於在交易中鎖定選定的行，以防止其他交易對這些行進行修改。
這是一種控制並發存取的方法，通常用於確保在某個事務中選定的行在該事務完成之前不會被其他事務修改。

具體來說，FOR UPDATE子句是用於在選定的行上設定寫鎖。
這樣，其他事務如果嘗試在這些行上執行寫入操作，就會被阻塞，直到擁有寫鎖的事務完成。

這種技術在需要確保事務中某些資料不會被其他事務並發修改的情況下非常有用。
然而，需要注意，使用這種方式可能導致效能問題和死鎖的風險，因此應謹慎使用，並確保在程式中正確處理鎖定的釋放。

```sql
SELECT * FROM database.table WHERE id = 1 FOR UPDATE;
```

#### START TRANSACTION;

"start transaction" 這個片語通常出現在資料庫管理系統（DBMS）和交易控制語言的上下文中。在資料庫的背景中，一個交易是一系列包含一個或多個 SQL 陳述式的工作單位。交易的目的是確保資料的一致性和完整性。

以下是 "start transaction" 陳述式的解釋：

交易開始：

"start transaction" 陳述式標誌著資料庫中一個交易的開始。它表示接下來的一系列 SQL 陳述式將被視為一個單一、原子性的工作單位。
原子性：

交易遵循原子性的原則，這意味著要麼交易中的所有陳述式都成功執行，要麼一個都不執行。如果交易中的任何部分失敗，整個交易都將被回滾，確保資料庫保持一致的狀態。
隔離性：

交易還提供隔離性，這意味著一個交易所做的更改對其他交易是不可見的，直到這些更改被提交。這有助於防止數據不一致和衝突。
一致性：

"start transaction" 陳述式有助於資料庫的一致性。它確保資料庫從一個一致的狀態轉移到另一個狀態，並且對數據的任何修改都符合定義的規則和約束。
持久性：

一旦交易成功完成，對資料庫所做的更改就變得永久。這稱為持久性。即使系統在交易提交後崩潰或遇到故障，交易所做的更改也會被保留。

```sql
-- 在這個例子中，交易以 "START TRANSACTION" 開始，後面是一系列修改資料庫的 SQL 陳述式。
-- 如果所有陳述式都成功執行，則使用 "COMMIT" 陳述式提交交易。
-- 如果交易中發生任何錯誤，可以使用 "ROLLBACK" 陳述式將其回滾，還原交易中所做的任何更改。
-- 開始交易
START TRANSACTION;

-- 交易內的 SQL 陳述式
UPDATE table1 SET column1 = 'value1' WHERE condition1;
INSERT INTO table2 (column2) VALUES ('value2');
DELETE FROM table3 WHERE condition3;

-- 提交交易
COMMIT;
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
