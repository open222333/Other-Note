# MySQL 筆記 Replication(Master-Slave 主從)

```
MySQL Replication

主從複製是一種常見的資料庫架構，通常用於提升系統效能、資料備份和故障恢復。在MySQL中，主從複製由一個主伺服器（Master）和一個或多個從伺服器（Slave）組成。

以下是主從複製的基本工作流程：

主伺服器（Master）：

主伺服器負責處理寫操作（插入、更新、刪除），是主要的資料庫伺服器。
所有寫操作都在主伺服器上執行，並且主伺服器將這些寫操作的變更記錄到二進位日誌（Binary Log）中。
從伺服器（Slave）：

從伺服器負責複製主伺服器上資料的副本。
從伺服器連接到主伺服器，通過主伺服器的二進位日誌中的事件來複製資料變更。
從伺服器上的資料庫是主伺服器上資料庫的精確副本。
主從同步流程：

主伺服器將寫操作的變更記錄到二進位日誌。
從伺服器連接到主伺服器，並請求獲取二進位日誌中的變更。
主伺服器將變更發送給從伺服器，從伺服器將這些變更應用到自己的資料庫中。
用途和好處：

讀寫分離： 透過將讀操作分發到從伺服器，可以減輕主伺服器的負載，提高系統的讀性能。
資料備份： 從伺服器可用於備份資料，而不影響主伺服器的性能。
故障恢復： 如果主伺服器發生故障，可以快速切換到從伺服器，確保系統的可用性。
配置和設定：

在主伺服器上啟用二進位日誌。
配置從伺服器以連接到主伺服器。
在從伺服器上啟用複製功能。
在MySQL中，可以通過修改配置檔或使用SQL命令來配置主從複製。具體的配置步驟可能略有不同，取決於MySQL版本和特定的需求。主從複製是一個強大的工具，但在使用之前應該仔細考慮資料一致性、延遲和故障恢復等因素。
```

```
MySQL Group Replication(MGR)

在 MySQL Group Replication 中，如果只有兩個節點，且主節點掛掉，從節點不會自動升級為主節點。
MySQL Group Replication 的高可用性和故障轉移機制通常需要至少三個節點來實現。
在一個三節點的 Group Replication 部署中，當主節點失效時，剩餘的兩個節點可以透過投票選舉新的主節點。
這是因為需要超過半數的節點的投票來決定新主節點。
當只有兩個節點時，沒有足夠的節點來進行投票，因此無法自動進行故障轉移。
如果只有兩個節點，建議考慮新增第三個節點以實現 Group Replication 的正常運作。
三節點的配置不僅提供了更好的高可用性，而且更容易進行故障轉移。
請注意，Group Replication 還需要確保網路的可靠性，以防止因網路分區而導致的問題。
在任何資料庫高可用性部署中，建議使用奇數節點，以確保在故障轉移時能夠得到正確的投票結果。
```

# MySQL Replication 和 MySQL Group Replication

## MySQL Replication

主從模型： MySQL Replication 是一種主從模型的複製，其中有一個主資料庫（Master）和一個或多個從資料庫（Slave）。

複製方式：資料從主庫非同步傳輸到從庫，主庫將變更記錄（binlog）傳送到從庫，從庫將這些變更套用至自己的資料集。

讀寫分離：可以透過配置讀寫分離，即主庫處理寫入操作，而從庫用於讀取操作，提高了讀取效能。

設定簡單：相對而言，MySQL Replication 的設定相對簡單，特別是在小規模的環境中。

## MySQL Group Replication

多主複製： MySQL Group Replication 是基於群組的複製，允許多個節點同時寫入。每個節點都可以接收寫入操作，然後這些寫入操作會在整個群組中同步。

一致性： MySQL Group Replication 強調資料的一致性，確保所有節點在任何給定時間點都具有相同的資料集。

自動故障轉移：具有自動故障轉移功能，當群組中的主節點發生故障時，會自動選舉新的主節點。

交易一致性：保證交易一致性，如果交易在一個節點上提交，則它將在群組中的所有節點上提交。

分散式資料庫： Group Replication 適用於建置分散式資料庫系統，確保高可用性和容錯性。

總的來說，MySQL Replication 主要用於主從複製，用於讀寫分離和備份，而MySQL Group Replication 更適合建構分散式資料庫系統，具有多主複製、自動故障轉移和一致性等特性。

## 目錄

- [MySQL 筆記 Replication(Master-Slave 主從)](#mysql-筆記-replicationmaster-slave-主從)
- [MySQL Replication 和 MySQL Group Replication](#mysql-replication-和-mysql-group-replication)
  - [MySQL Replication](#mysql-replication)
  - [MySQL Group Replication](#mysql-group-replication)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [Master-Slave(主從環境)相關](#master-slave主從環境相關)
    - [keepalived(實現高可用性的工具) 相關](#keepalived實現高可用性的工具-相關)
    - [錯誤處理相關](#錯誤處理相關)
      - [Last\_SQL\_Errno: 1032](#last_sql_errno-1032)
- [指令](#指令)
  - [基本用法](#基本用法)
  - [keepalived (實作高可用)](#keepalived-實作高可用)
- [例外狀況](#例外狀況)
  - [修復 master slave 最快速方法](#修復-master-slave-最快速方法)
  - [修復 master slave Slave\_SQL\_Running: No, Slave\_IO\_Running: No 解決方案](#修復-master-slave-slave_sql_running-no-slave_io_running-no-解決方案)
  - [ERROR 1872 (HY000): Slave failed to initialize relay log info structure from the repository](#error-1872-hy000-slave-failed-to-initialize-relay-log-info-structure-from-the-repository)
    - [Error in applier for group\_replication\_recovery: Could not execute Write\_rows event on table iavnight\_cpi.ad\_process; The table 'ad\_process' is full, Error\_code: 1114](#error-in-applier-for-group_replication_recovery-could-not-execute-write_rows-event-on-table-iavnight_cpiad_process-the-table-ad_process-is-full-error_code-1114)
    - [Last\_Errno: 1594](#last_errno-1594)
    - [Last\_Errno: 1032](#last_errno-1032)
  - [主從資料不一致 (Replication Error 1032)](#主從資料不一致-replication-error-1032)
    - [使用 pt-table-sync 自動修復 (可以邊同步邊修資料，不需要停 Master。)](#使用-pt-table-sync-自動修復-可以邊同步邊修資料不需要停-master)

## 參考資料

[MySQl官方網站](https://dev.mysql.com/)

[MySQL 全部文檔](https://dev.mysql.com/doc/)

[MySQL 8.0版本 指令](https://dev.mysql.com/doc/refman/8.0/en/mysql-command-options.html)

[MySQL 8.0版本 文檔 - 使用搜尋查詢](https://dev.mysql.com/doc/refman/8.0/en/)

[MySQL 5.7版本 文檔 - 使用搜尋查詢](https://dev.mysql.com/doc/refman/5.7/en/)

[MySQL 教程](https://www.itread01.com/study/mysql-tutorial.html)

[MySQL Community Downloads - MySQL 社區下載](https://dev.mysql.com/downloads/)

[Connectors and APIs - MySQL 連接器和 API 是用於將不同編程語言的應用程序連接到 MySQL 數據庫服務器的驅動程序和庫](https://dev.mysql.com/doc/index-connectors.html)

[Other-MySQLReplication 範例](https://github.com/open222333/Other-MySQLReplication.git)

### Master-Slave(主從環境)相關

[MySQL如何不停机维护主从同步](https://zhuanlan.zhihu.com/p/472339202)

[MySQL Replication 主從式架構設定教學](https://blog.toright.com/posts/5062/mysql-replication-%E4%B8%BB%E5%BE%9E%E5%BC%8F%E6%9E%B6%E6%A7%8B%E8%A8%AD%E5%AE%9A%E6%95%99%E5%AD%B8.html)

[docker-compose搭建mysql主從環境](https://www.uj5u.com/ruanti/275444.html)

[docker-compose搭建mysql主從環境](https://hub.docker.com/r/bitnami/mysql)

[Slave_IO_Running Slave_SQL_Running 排錯](https://www.cnblogs.com/l-hh/p/9922548.html#_label0)

[mysql系列（一）—— 细说show slave status参数详解（最全）](https://blog.51cto.com/zhengmingjing/1910565)

[MySQL 中常见的几种高可用架构部署方案解析](https://www.jb51.net/article/281852.htm)

### keepalived(實現高可用性的工具) 相關

[Linux 工具 keepalived(實現高可用性的工具).md](../../../02_作業系統/Unix/Linux/Linux%20工具/Linux%20工具%20keepalived(實現高可用性的工具).md)

[一个月后，我们又从 MySQL 双主切换成了主-从！](https://www.51cto.com/article/713751.html)

[keepalived+MySQL实现高可用(雙主)](https://www.cnblogs.com/lijiaman/p/13430668.html)

### 錯誤處理相關

[MySQL主从复制，启动slave时报错Slave failed to initialize relay log info structure from the repository](https://blog.csdn.net/weixin_37998647/article/details/79950133)

[解决Mysql复制Relay log read failure 的问题](https://blog.51cto.com/wuwei5460/1552798)

[MySQL relay log corrupted, how do I fix it? Tried but failed](https://dba.stackexchange.com/questions/53893/mysql-relay-log-corrupted-how-do-i-fix-it-tried-but-failed)

[MySQL Replication 遇到 Got fatal error 1236 from master 修復](https://blog.longwin.com.tw/2013/09/mysql-replication-error-1236-fix-2013/)

[MySQL主从复制，启动slave时报错1872 Slave failed to initialize relay log info structure from the repository](https://blog.51cto.com/u_15127597/4309432)

[MySQL replication error 1594](https://dba.stackexchange.com/questions/69394/mysql-replication-error-1594)

[MySQL 主从失败报错：Last_SQL_Errno: 1594](https://www.cnblogs.com/cyleon/p/10679341.html)

#### Last_SQL_Errno: 1032

[[MySQL] SQL_ERROR 1032解决办法 ](https://www.cnblogs.com/langdashu/p/5920436.html)

[How to Fix MySQL Error 1032 in Simple Steps](https://10web.io/blog/mysql-error-1032/)

# 指令

## 基本用法

`mysql-master設定`

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
systemctl start mysqld
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

CREATE USER 'replication'@'%' IDENTIFIED BY 'replicationpassword';
GRANT REPLICATION SLAVE ON *.* TO 'replication'@'replicationpassword';

FLUSH PRIVILEGES;

-- 檢查使用者 列出所有使用者帳號
select User, Host From mysql.user;
```

`mysql-slave設定`

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

`備份mysql-master`

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

`恢復備份到mysql-slave`

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
MASTER_PASSWORD='password',
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

## keepalived (實作高可用)

```

```

# 例外狀況

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

CHANGE MASTER TO
  MASTER_HOST = 'IP',
  MASTER_USER = 'replication',
  MASTER_PASSWORD = 'password',
  MASTER_LOG_FILE = 'mysql-bin.$binlog_number',
  MASTER_LOG_POS =  $postion;

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

```sql
-- https://ost.51cto.com/posts/16247

-- 获取正确的 group_replication_group_name。实例重启完成后，
-- 读取 mysql_innodb_cluster_metadata.clusters 这个元数据表，获取正确的 group name。
select attributes->'$.group_replication_group_name' from mysql_innodb_cluster_metadata.clusters;
+----------------------------------------------+
| attributes->'$.group_replication_group_name' |
+----------------------------------------------+
| "bc664a9b-9b5b-11ec-8a73-525400c5601a"       |
+----------------------------------------------+

-- 在每个节点上手动修改 group_replication_group_name 。
set global group_replication_group_name = "bc664a9b-9b5b-11ec-8a73-525400c5601a";
set global group_replication_group_name = "";

-- 再次执行 dba.rebootClusterFromCompleteOutage() 就行了。
dba.rebootClusterFromCompleteOutage()
```

### Error in applier for group_replication_recovery: Could not execute Write_rows event on table iavnight_cpi.ad_process; The table 'ad_process' is full, Error_code: 1114

```sql
-- https://stackoverflow.com/questions/730579/1114-hy000-the-table-is-full
```

### Last_Errno: 1594

Last_Error: Relay log read failure: Could not parse relay log event entry. The possible reasons are: the master's binary log is corrupted (you can check this by running 'mysqlbinlog' on the binary log), the slave's relay log is corrupted (you can check this by running 'mysqlbinlog' on the relay log), a network problem, or a bug in the master's or slave's MySQL code. If you want to check the master's binary log or slave's relay log, you will be able to know their names by issuing 'SHOW SLAVE STATUS' on this slave.

```sql
-- 檢查slave狀態
show slave status\G
-- Check Relay_Master_Log_File and Exec_Master_Log_Pos
-- Relay_Master_Log_File: mysql-bin.004772    slave函式庫已讀取的master的binlog
-- Exec_Master_Log_Pos: 516345810             在slave上已經執行的position位置點

-- 停用slave，以slave已經讀取的binlog文件，和已經執行的position為起點，重新設定同步
-- 停止slave
stop slave;
-- 手動設定master資料 linode部分 ip可以使用內網ip
-- master 輸入下面指令取的資訊
-- show master status;
change master to
master_log_file='mysql-bin.004772',
master_log_pos=516345810;
-- 執行slave
start slave;
-- 檢查slave狀態
show slave status\G
```

```
檢查主庫的二進制日誌:
使用 SHOW SLAVE STATUS; 在從庫上查看主庫的二進制日誌文件名稱和位置。
連接到主 MySQL 伺服器，使用 mysqlbinlog 檢查主庫中錯誤訊息中提到的二進制日誌文件，檢查是否存在任何損壞或問題。

檢查從庫的中繼日誌:
使用 mysqlbinlog 檢查從庫中錯誤訊息中提到的中繼日誌文件，查看是否存在損壞。
如果中繼日誌損壞，你可以停止從庫 (STOP SLAVE;)，跳過損壞的中繼日誌事件 (SET GLOBAL SQL_SLAVE_SKIP_COUNTER = [損壞事件的數量];)，然後重新啟動從庫 (START SLAVE;)。

檢查網路問題:
確保主庫和從庫之間的網路連接正常，並且沒有任何網路問題。

檢查 MySQL 版本和修補:
檢查你使用的 MySQL 版本，確保它是最新的，並且查看是否有任何已知問題。如果有，考慮升級到修復問題的版本。

重新啟動 MySQL 從庫:
嘗試重新啟動從庫，停止從庫 (STOP SLAVE;)，然後重新啟動 (START SLAVE;)，最後檢查狀態 (SHOW SLAVE STATUS\G)。
```

```bash
mysqlbinlog [Master_Log_File] | less
```

檢查二進制日誌文件：
在 mysqlbinlog 輸出中查找是否存在任何錯誤消息、損壞或不正確的日誌條目。
如果有錯誤或損壞，你可能需要使用備份還原該二進制日誌，或者在主庫上進行修復。

根據發現的問題進行修復：
如果發現二進制日誌損壞，可以嘗試使用主庫上的備份進行還原，或者查找和修復損壞的日誌條目。
記得在執行任何修復操作之前，確保有充分的數據備份以防萬一。

### Last_Errno: 1032

Last_Error: Could not execute Update_rows event on table avnight.member_log; Can't find record in 'member_log', Error_code: 1032; handler error HA_ERR_KEY_NOT_FOUND; the event's master log mysql-bin.000077, end_log_pos 309389912

```
錯誤代碼: 1032
錯誤描述: Can't find record in 'member_log'
原因: 從伺服器試圖執行來自主伺服器的一個 UPDATE 或 DELETE 操作，但該操作所要修改或刪除的記錄在從伺服器的 member_log 表中不存在。這可能是因為主伺服器與從伺服器之間的資料不同步。
```

## 主從資料不一致 (Replication Error 1032)

### 使用 pt-table-sync 自動修復 (可以邊同步邊修資料，不需要停 Master。)

Percona Toolkit 裡有個工具 pt-table-sync，可以自動比對 Master/Slave 資料並補齊差異。

```sh
pt-table-sync --execute --verbose \
  h=master_host,u=repl,p=xxx,D=avnight,t=member_check_in \
  h=slave_host,u=repl,p=xxx
```
