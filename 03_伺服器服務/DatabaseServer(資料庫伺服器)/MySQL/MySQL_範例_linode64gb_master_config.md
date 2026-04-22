# MySQL 8.4 調整設定檔 — Linode Dedicated 64GB（Master）

## 主機規格

| 項目 | 規格 |
|------|------|
| RAM | 64 GB |
| CPU | 16 核心（Dedicated） |
| 角色 | Master |

---

## 檔案結構調整

**`/etc/mysql/my.cnf`** — 只保留 includedir，不放任何設定：

```ini
[mysqld_safe]
socket = /var/run/mysqld/mysqld.sock
nice = 0

[mysqld]
!includedir /etc/mysql/conf.d/
!includedir /etc/mysql/mysql.conf.d/
```

**`/etc/mysql/mysql.conf.d/mysqld.cnf`** — 所有設定集中於此：

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
lc-messages-dir = /usr/share/mysql
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
# binlog_format 已廢棄 MySQL 8.4 之後預設就是 ROW
# binlog_format = ROW
gtid-mode = ON
enforce-gtid-consistency = ON
log_replica_updates = ON
# 控制 binlog 檔案保留多久後自動刪除
# expire_logs_days = 10
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

---

## 套用步驟

```bash
# 1. 備份原始檔案
sudo cp /etc/mysql/my.cnf /etc/mysql/my.cnf.bak
sudo cp /etc/mysql/mysql.conf.d/mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf.bak

# 2. 修改 my.cnf（只保留 includedir）
sudo nano /etc/mysql/my.cnf

# 3. 修改 mysqld.cnf（貼上上方完整內容）
sudo nano /etc/mysql/mysql.conf.d/mysqld.cnf

# 4. 檢查設定語法
sudo mysqld --validate-config

# 5. 重啟 MySQL
sudo systemctl restart mysql

# 6. 確認啟動成功
sudo systemctl status mysql
```

---

## 套用後確認指令

```sql
-- 確認 Buffer Pool 大小
SHOW VARIABLES LIKE 'innodb_buffer_pool_size';

-- 確認 GTID 啟用
SHOW VARIABLES LIKE 'gtid_mode';

-- 確認 Binlog 格式
SHOW VARIABLES LIKE 'binlog_format';

-- 確認 redo log 容量
SHOW VARIABLES LIKE 'innodb_redo_log_capacity';

-- 確認 max_connections
SHOW VARIABLES LIKE 'max_connections';
```

---

## 與舊設定差異摘要

| 參數 | 舊值 | 新值 | 原因 |
|------|------|------|------|
| `innodb_buffer_pool_size` | `32G` | `48G` | 64GB RAM 建議用 75% |
| `innodb_buffer_pool_instances` | 未設定 | `16` | 減少鎖定競爭 |
| `innodb_log_file_size` | `1024M` | 移除 | 改用 `innodb_redo_log_capacity` |
| `innodb_redo_log_capacity` | 未設定 | `4G` | 8.4 新參數 |
| `innodb_flush_method` | 未設定 | `O_DIRECT` | Dedicated 主機避免 double buffering |
| `innodb_io_capacity` | 未設定 | `2000` | 對應 SSD IOPS |
| `max_connections` | `4096` | `2000` | 避免記憶體耗盡 |
| `max_connect_errors` | `10000000` | `100000` | 舊值等於沒有防護 |
| `max_allowed_packet` | `2048M` | `256M` | 2048M 過大 |
| `thread_cache_size` | `3072` | `128` | 舊值過大 |
| `log_warnings` | `1` | 移除 | 改用 `log_error_verbosity=2` |
| `query_cache_size` | `1024M` | 移除 | MySQL 8.0 已移除此功能 |
| `query_cache_limit` | `1024M` | 移除 | 同上 |
| `key_buffer_size` | `2048M` | 移除 | MyISAM 專用，InnoDB 不需要 |
| `myisam-recover-options` | `BACKUP` | 移除 | MyISAM 已廢棄 |
| `skip-log-bin` | 存在 | 移除 | 與 `log-bin` 衝突 |
| `relay-log = relay-log-slave` | 存在 | 移除 | Master 不需要 relay-log |
| `symbolic-links` | `0` | 移除 | MySQL 8.4 已移除此選項 |
| `gtid_mode`（my.cnf） | 存在 | 移除 | 集中到 mysqld.cnf 統一管理 |
