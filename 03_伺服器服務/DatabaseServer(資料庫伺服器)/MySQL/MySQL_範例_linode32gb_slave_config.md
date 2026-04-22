# MySQL 8.4 Slave 設定檔 — Linode 32GB

## 主機規格

| 項目 | 規格 |
|------|------|
| RAM | 32 GB |
| 角色 | Slave |
| server-id | 2（第二台 Slave 改為 3） |

---

## `/etc/mysql/mysql.conf.d/mysqld.cnf`

```ini
[mysqld]
# ─── Basic ───────────────────────────────────────────────
user = mysql
pid-file = /var/run/mysqld/mysqld.pid
socket = /var/run/mysqld/mysqld.sock
port = 3306
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

# ─── Slave 平行複製（加速同步） ───────────────────────────
replica_parallel_workers = 8
replica_parallel_type = LOGICAL_CLOCK
replica_preserve_commit_order = ON
```

---

## 與 Master 設定的差異

| 參數 | Master | Slave | 說明 |
|------|--------|-------|------|
| `server-id` | `1` | `2` / `3` | 每台必須唯一 |
| `read-only` | 無 | `ON` | 防止應用程式誤寫 Slave |
| `super_read_only` | 無 | `ON` | 連 root 也無法寫入，更安全 |
| `log-bin` | 有 | 無（靠 relay-log） | Slave 不需要主動寫 binlog |
| `relay-log` | 無 | `relay-log-replica` | 接收 Master binlog 用 |
| `innodb_buffer_pool_size` | `48G` | `22G` | 32GB RAM 用約 70% |
| `innodb_buffer_pool_instances` | `16` | `8` | 對應 buffer pool 大小 |
| `innodb_redo_log_capacity` | `4G` | `2G` | Slave 寫入壓力較小 |
| `innodb_flush_log_at_trx_commit` | `1` | `2` | Slave 可放寬，效能較好 |
| `max_connections` | `2000` | `1000` | Slave 主要給讀取用 |
| `replica_parallel_workers` | 無 | `8` | 平行複製加速同步 |

---

## 重要說明

### `super_read_only = ON`
比 `read-only` 更嚴格，連有 SUPER 權限的帳號（包含 root）也無法寫入。
Replication 執行緒不受此限制，同步不受影響。

### `innodb_flush_log_at_trx_commit = 2`
Master 必須設 `1`（每筆交易都 flush，最安全）。
Slave 可以設 `2`（每秒 flush 一次），效能更好，因為資料已在 Master 持久化。

### `replica_parallel_workers = 8`
MySQL 8.4 Slave 預設是單執行緒同步，大量寫入時容易落後 Master。
開啟平行複製後可大幅縮短同步延遲，建議設為 CPU 核心數的一半。

---

## 套用步驟

```bash
# 備份原始設定
sudo cp /etc/mysql/mysql.conf.d/mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf.bak

# 編輯設定檔
sudo nano /etc/mysql/mysql.conf.d/mysqld.cnf

# 檢查語法
sudo mysqld --validate-config

# 重啟
sudo systemctl restart mysql

# 確認狀態
sudo systemctl status mysql
```

---

## 套用後確認指令

```sql
-- 確認 read-only
SHOW VARIABLES LIKE 'read_only';
SHOW VARIABLES LIKE 'super_read_only';

-- 確認 GTID
SHOW VARIABLES LIKE 'gtid_mode';

-- 確認平行複製
SHOW VARIABLES LIKE 'replica_parallel_workers';

-- 確認同步狀態
SHOW REPLICA STATUS\G
```
