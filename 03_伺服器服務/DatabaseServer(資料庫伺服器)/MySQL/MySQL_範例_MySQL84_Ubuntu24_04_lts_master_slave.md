# Ubuntu 24.04 LTS 安裝 MySQL 8.4 完整步驟
# 1 Master + 1 Slave（全新主機）

## 目錄

- [環境](#環境)
- [共同步驟（兩台都執行）](#master-與-slave-共同步驟兩台都執行)
  - [一、下載 MySQL 8.4 官方 deb bundle](#一下載-mysql-84-官方-deb-bundle)
  - [二、解壓縮](#二解壓縮)
  - [三、安裝依賴套件](#三安裝依賴套件)
  - [四、依序安裝 MySQL 套件](#四依序安裝-mysql-套件)
  - [五、清除下載檔](#五清除下載檔)
  - [六、確認版本](#六確認版本)
  - [七、確認服務狀態](#七確認服務狀態)
  - [八、執行安全性初始化](#八執行安全性初始化)
- [Master 專屬步驟](#master-專屬步驟)
  - [八、編輯 my.cnf](#八編輯-etcmysqlmycnf)
  - [九、編輯 mysqld.cnf（64GB）](#九編輯-etcmysqlmysqlconfdmysqldcnflinode-dedicated-64gb)
  - [十、檢查語法並重啟](#十檢查語法並重啟)
  - [十一、建立 Replication 帳號](#十一建立-replication-帳號)
- [Slave 專屬步驟](#slave-專屬步驟)
  - [八、編輯 my.cnf](#八編輯-etcmysqlmycnf-1)
  - [九、編輯 mysqld.cnf（32GB）](#九編輯-etcmysqlmysqlconfdmysqldcnflinode-32gb)
  - [十、檢查語法並重啟](#十檢查語法並重啟-1)
  - [十一、設定連線到 Master](#十一設定連線到-master)
  - [十二、啟動同步](#十二啟動同步)
  - [十三、確認同步狀態](#十三確認同步狀態)
- [驗證同步與只讀](#驗證同步與只讀)
  - [Master 建立測試資料](#master-建立測試資料)
  - [Slave 確認同步](#slave-確認同步)
  - [Slave 測試只讀](#slave-測試只讀應失敗)
  - [清除測試資料](#清除測試資料master-執行)
- [常見錯誤處理](#常見錯誤處理)
  - [Replica_IO_Running: No](#replica_io_running-no)
  - [防火牆開放 3306](#防火牆開放-3306)
  - [確認 replicator 帳號權限](#確認-replicator-帳號權限)

---

## 環境

| 角色 | 主機 | server-id |
|------|------|-----------|
| Master | 192.168.1.1 | 1 |
| Slave | 192.168.1.2 | 2 |

---

# Master 與 Slave 共同步驟（兩台都執行）

## 一、下載 MySQL 8.4 官方 deb bundle

> 官方 APT repo GPG key 有過期問題，改用直接下載 deb bundle 安裝。

```bash
wget https://dev.mysql.com/get/Downloads/MySQL-8.4/mysql-server_8.4.5-1ubuntu24.04_amd64.deb-bundle.tar
```

## 二、解壓縮

```bash
tar xf mysql-server_8.4.5-1ubuntu24.04_amd64.deb-bundle.tar
```

## 三、安裝依賴套件

```bash
sudo apt-get install -y libaio-dev libmecab-dev
```

## 四、依序安裝 MySQL 套件

| 套件 | 說明 |
|------|------|
| `mysql-common` | 共用設定檔與基礎元件，所有套件的依賴基礎 |
| `mysql-community-client-plugins` | 驗證 plugin（caching_sha2_password 等） |
| `mysql-community-client-core` | MySQL client 核心執行檔 |
| `mysql-community-client` | MySQL client 完整套件（含工具指令） |
| `mysql-client` | client meta 套件，確保依賴完整 |
| `mysql-community-server-core` | MySQL server 核心執行檔（mysqld） |
| `mysql-community-server` | MySQL server 完整套件（含初始化與服務） |

```bash
sudo dpkg -i mysql-common_8.4.5-1ubuntu24.04_amd64.deb
sudo dpkg -i mysql-community-client-plugins_8.4.5-1ubuntu24.04_amd64.deb
sudo dpkg -i mysql-community-client-core_8.4.5-1ubuntu24.04_amd64.deb
sudo dpkg -i mysql-community-client_8.4.5-1ubuntu24.04_amd64.deb
sudo dpkg -i mysql-client_8.4.5-1ubuntu24.04_amd64.deb
sudo dpkg -i mysql-community-server-core_8.4.5-1ubuntu24.04_amd64.deb
sudo dpkg -i mysql-community-server_8.4.5-1ubuntu24.04_amd64.deb
```

> **安裝 `mysql-community-server` 時會跳出互動介面：**
> - **Enter root password** — 輸入 root 密碼
> - **Re-enter root password** — 再次確認密碼
> - **Select default authentication plugin** — 選擇 **Use Strong Password Encryption**（caching_sha2_password）
>
> 請記住此密碼，後續登入 MySQL 會用到。

## 五、清除下載檔

```bash
rm -f mysql-server_8.4.5-1ubuntu24.04_amd64.deb-bundle.tar
rm -f mysql-apt-config_0.8.33-1_all.deb.1
rm -f mysql-common_8.4.5-1ubuntu24.04_amd64.deb
rm -f mysql-community-client-plugins_8.4.5-1ubuntu24.04_amd64.deb
rm -f mysql-community-client-core_8.4.5-1ubuntu24.04_amd64.deb
rm -f mysql-community-client_8.4.5-1ubuntu24.04_amd64.deb
rm -f mysql-client_8.4.5-1ubuntu24.04_amd64.deb
rm -f mysql-community-server-core_8.4.5-1ubuntu24.04_amd64.deb
rm -f mysql-community-server_8.4.5-1ubuntu24.04_amd64.deb
```

## 六、確認版本

```bash
mysql --version
# 應回傳 mysql  Ver 8.4.5
```

## 七、確認服務狀態

```bash
sudo systemctl status mysql
sudo systemctl enable mysql
```

## 八、執行安全性初始化

```bash
sudo mysql_secure_installation
```

| 問題 | 選擇 | 說明 |
|------|------|------|
| Setup VALIDATE PASSWORD component? | `y` | 啟用密碼強度驗證，要求密碼包含大小寫、數字、特殊符號 |
| Remove anonymous users? | `y` | 移除匿名帳號，避免任何人不需帳號就能登入 |
| Disallow root login remotely? | `y` | 禁止 root 從遠端登入，只允許本機操作 |
| Remove test database? | `y` | 移除預設測試資料庫，任何人都能存取，正式環境不需要 |
| Reload privilege tables now? | `y` | 讓以上所有變更立即生效 |

### 關於 root 密碼

Ubuntu 預設使用 **auth_socket** 認證，直接用以下指令登入：

```bash
sudo mysql
```

若需改為密碼登入：

```sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH caching_sha2_password BY '強密碼';
FLUSH PRIVILEGES;
```

---

# Master 專屬步驟

## 八、編輯 `/etc/mysql/my.cnf`

只保留 includedir，不放任何設定：

```ini
[mysqld_safe]
socket = /var/run/mysqld/mysqld.sock
nice = 0

[mysqld]
!includedir /etc/mysql/conf.d/
!includedir /etc/mysql/mysql.conf.d/
```

## 九、編輯 `/etc/mysql/mysql.conf.d/mysqld.cnf`（Linode Dedicated 64GB）

```ini
[mysqld]
# ─── Basic ───────────────────────────────────────────────
user = mysql
pid-file = /var/run/mysqld/mysqld.pid
socket = /var/run/mysqld/mysqld.sock
port = 3306
basedir = /usr
datadir = /var/lib/mysql
tmpdir = /tmp
lc-messages-dir = /usr/share/mysql-8.4
skip-external-locking
bind-address = 0.0.0.0

# ─── Timeout ─────────────────────────────────────────────
wait_timeout = 120
interactive_timeout = 120

# ─── Connection ──────────────────────────────────────────
max_allowed_packet = 256M
max_connections = 2000
max_connect_errors = 100000
thread_cache_size = 128
thread_stack = 512K

# ─── Logging ─────────────────────────────────────────────
log_error_verbosity = 2
log_error = /var/log/mysql/error.log
slow_query_log = ON
slow-query-log-file = /var/log/mysql/mysql-slow.log
long_query_time = 2

# ─── Replication (Master) ────────────────────────────────
server-id = 1
log-bin = mysql-bin
gtid-mode = ON
enforce-gtid-consistency = ON
log_replica_updates = ON
binlog_expire_logs_seconds = 864000
max_binlog_size = 1024M

# ─── InnoDB ──────────────────────────────────────────────
innodb_buffer_pool_size = 48G
innodb_buffer_pool_instances = 16
innodb_redo_log_capacity = 4G
innodb_log_buffer_size = 64M
innodb_flush_log_at_trx_commit = 1
innodb_flush_method = O_DIRECT
innodb_file_per_table = ON
innodb_io_capacity = 2000
innodb_io_capacity_max = 4000
innodb_read_io_threads = 8
innodb_write_io_threads = 8

# ─── Table Cache ─────────────────────────────────────────
table_open_cache = 8192
table_definition_cache = 4096
open_files_limit = 65535

# ─── Buffer ──────────────────────────────────────────────
sort_buffer_size = 4M
join_buffer_size = 4M
read_buffer_size = 2M
read_rnd_buffer_size = 4M

# ─── Temp Table ──────────────────────────────────────────
tmp_table_size = 256M
max_heap_table_size = 256M
```

## 十、檢查語法並重啟

```bash
sudo mysqld --validate-config
sudo systemctl restart mysql
sudo systemctl status mysql
```

## 十一、建立 Replication 帳號

MySQL 8.4 已完全移除 `mysql_native_password`，Replica 連線必須使用 `caching_sha2_password`。
Slave 連線時需加入 `GET_SOURCE_PUBLIC_KEY=1` 才能正常驗證（見 Slave 步驟十一）。

```sql
sudo mysql

CREATE USER 'replicator'@'%' IDENTIFIED WITH caching_sha2_password BY '強密碼';
GRANT REPLICATION SLAVE ON *.* TO 'replicator'@'%';
FLUSH PRIVILEGES;
```

確認帳號與 GTID 正常：

```sql
SELECT user, host, plugin FROM mysql.user;
SHOW GRANTS FOR 'replicator'@'%';
SHOW VARIABLES LIKE 'gtid_mode';
SHOW VARIABLES LIKE 'log_bin';
SHOW MASTER STATUS;
```

---

# Slave 專屬步驟

## 八、編輯 `/etc/mysql/my.cnf`

```ini
[mysqld_safe]
socket = /var/run/mysqld/mysqld.sock
nice = 0

[mysqld]
!includedir /etc/mysql/conf.d/
!includedir /etc/mysql/mysql.conf.d/
```

## 九、編輯 `/etc/mysql/mysql.conf.d/mysqld.cnf`（Linode 32GB）

```ini
[mysqld]
# ─── Basic ───────────────────────────────────────────────
user = mysql
pid-file = /var/run/mysqld/mysqld.pid
socket = /var/run/mysqld/mysqld.sock
port = 3306
basedir = /usr
datadir = /var/lib/mysql
tmpdir = /tmp
lc-messages-dir = /usr/share/mysql-8.4
skip-external-locking
bind-address = 0.0.0.0

# ─── Timeout ─────────────────────────────────────────────
wait_timeout = 120
interactive_timeout = 120

# ─── Connection ──────────────────────────────────────────
max_allowed_packet = 256M
max_connections = 1000
max_connect_errors = 100000
thread_cache_size = 64
thread_stack = 512K

# ─── Logging ─────────────────────────────────────────────
log_error_verbosity = 2
log_error = /var/log/mysql/error.log
slow_query_log = ON
slow-query-log-file = /var/log/mysql/mysql-slow.log
long_query_time = 2

# ─── Replication (Slave) ─────────────────────────────────
server-id = 2
gtid-mode = ON
enforce-gtid-consistency = ON
log_replica_updates = ON
relay-log = relay-log-replica
read-only = ON
super_read_only = ON
binlog_expire_logs_seconds = 864000
max_binlog_size = 1024M

# ─── 平行複製 ─────────────────────────────────────────────
replica_parallel_workers = 8
replica_preserve_commit_order = ON
# replica_parallel_type 在 MySQL 8.4 已廢棄，預設即為 LOGICAL_CLOCK，不需設定

# ─── InnoDB ──────────────────────────────────────────────
innodb_buffer_pool_size = 22G
innodb_buffer_pool_instances = 8
innodb_redo_log_capacity = 2G
innodb_log_buffer_size = 32M
innodb_flush_log_at_trx_commit = 2
innodb_flush_method = O_DIRECT
innodb_file_per_table = ON
innodb_io_capacity = 2000
innodb_io_capacity_max = 4000
innodb_read_io_threads = 8
innodb_write_io_threads = 8

# ─── Table Cache ─────────────────────────────────────────
table_open_cache = 4096
table_definition_cache = 2048
open_files_limit = 65535

# ─── Buffer ──────────────────────────────────────────────
sort_buffer_size = 2M
join_buffer_size = 2M
read_buffer_size = 1M
read_rnd_buffer_size = 2M

# ─── Temp Table ──────────────────────────────────────────
tmp_table_size = 128M
max_heap_table_size = 128M
```

## 十、檢查語法並重啟

```bash
sudo mysqld --validate-config
sudo systemctl restart mysql
sudo systemctl status mysql
```

## 十一、設定連線到 Master

MySQL 8.4 使用 `caching_sha2_password`，需加入 `GET_SOURCE_PUBLIC_KEY=1` 取得 RSA 公鑰進行驗證：

```sql
sudo mysql

CHANGE REPLICATION SOURCE TO
  SOURCE_HOST='192.168.1.1',
  SOURCE_PORT=3306,
  SOURCE_USER='replicator',
  SOURCE_PASSWORD='強密碼',
  SOURCE_AUTO_POSITION=1,
  GET_SOURCE_PUBLIC_KEY=1;
```

## 十二、啟動同步

```sql
START REPLICA;
```

## 十三、確認同步狀態

```sql
SHOW REPLICA STATUS\G
```

| 欄位 | 期望值 |
|------|--------|
| `Replica_IO_Running` | `Yes` |
| `Replica_SQL_Running` | `Yes` |
| `Seconds_Behind_Source` | `0` |
| `Last_IO_Error` | 空白 |
| `Last_SQL_Error` | 空白 |

---

# 驗證同步與只讀

## Master 建立測試資料

```sql
CREATE DATABASE test_replication;
USE test_replication;
CREATE TABLE hello (id INT PRIMARY KEY, msg VARCHAR(50));
INSERT INTO hello VALUES (1, 'from master');
```

## Slave 確認同步

```sql
USE test_replication;
SELECT * FROM hello;
# 應回傳 from master
```

## Slave 測試只讀（應失敗）

```sql
-- 一般帳號
INSERT INTO hello VALUES (2, 'from slave');
# ERROR 1290: running with --read-only option

-- root 帳號
sudo mysql
INSERT INTO hello VALUES (3, 'root write');
# ERROR 1290: running with --super-read-only option
```

## 清除測試資料（Master 執行）

```sql
DROP DATABASE test_replication;
```

---

# 常見錯誤處理

## Replica_IO_Running: No

```sql
SHOW REPLICA STATUS\G
-- 查看 Last_IO_Error

STOP REPLICA;
RESET REPLICA;
CHANGE REPLICATION SOURCE TO ...;
START REPLICA;
```

## 防火牆開放 3306

```bash
# 在 Master 執行
sudo ufw allow from 192.168.1.2 to any port 3306
```

## 確認 replicator 帳號權限

```sql
SHOW GRANTS FOR 'replicator'@'%';
```

## mysqld --validate-config 錯誤：Can't find error-message file

```
[ERROR] Can't find error-message file '/usr/share/mysql/english/errmsg.sys'.
Check error-message file location and 'lc-messages-dir' configuration directive.
```

MySQL 8.4 官方 deb bundle 將 error message 檔案安裝在 `/usr/share/mysql-8.4/`，
而非舊路徑 `/usr/share/mysql/`。

**修正**：將 `mysqld.cnf` 中的設定改為：

```ini
lc-messages-dir = /usr/share/mysql-8.4
```

## mysqld --validate-config 警告：replica-parallel-type deprecated

```
[Warning] The syntax '--replica-parallel-type' is deprecated and will be removed in a future release.
```

MySQL 8.4 移除了 `replica_parallel_type` 選項，`LOGICAL_CLOCK` 已成為預設且唯一模式，不需要設定此參數。

**修正**：從 `mysqld.cnf` 中移除 `replica_parallel_type = LOGICAL_CLOCK` 這一行。
