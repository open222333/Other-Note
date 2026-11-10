# MySQL 工具 mydumper myloader(平行備份還原工具)

```
mydumper：多執行緒平行備份，輸出為目錄（每張表一個檔案）。
myloader：多執行緒平行還原，速度遠快於 mysql < dump.sql。
適合大型資料庫備份與跨版本遷移（如 MySQL 5.7 → 8.4）。

兩者為配對工具，不能混用：
  - mydumper 輸出的目錄格式（含 metadata、每表獨立 .sql）只有 myloader 能讀取。
  - mysqldump 產生的 .sql 無法透過 myloader 匯入。
  - 備份用哪個工具，還原就必須用對應的工具。
```

> 適用版本：部分內容以 CentOS 7、MySQL 5.x、Ubuntu 16.04 撰寫（皆已停止維護），新版系統的套件名稱與指令可能不同

## 目錄

- [MySQL 工具 mydumper myloader(平行備份還原工具)](#mysql-工具-mydumper-myloader平行備份還原工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [相關筆記](#相關筆記)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
- [指令](#指令)
  - [mydumper 備份](#mydumper-備份)
    - [備份全部資料庫](#備份全部資料庫)
    - [備份指定資料庫](#備份指定資料庫)
    - [備份指定資料表](#備份指定資料表)
  - [myloader 還原](#myloader-還原)
    - [還原全部資料庫](#還原全部資料庫)
    - [還原指定資料庫](#還原指定資料庫)
  - [常用參數](#常用參數)
    - [mydumper 參數](#mydumper-參數)
    - [myloader 參數](#myloader-參數)
  - [長時間備份還原：tmux 執行 + tail -f 監看 log](#長時間備份還原tmux-執行--tail--f-監看-log)
  - [備份完成後的完整性檢查](#備份完成後的完整性檢查)
  - [還原到雲端 Managed Database（SSL）](#還原到雲端-managed-databasessl)
- [與 mysqldump 比較](#與-mysqldump-比較)
- [匯入加速設定](#匯入加速設定)
- [Replica 相關](#replica-相關)
  - [對現有 Replica 的影響](#對現有-replica-的影響)
  - [從 Replica 執行備份（降低 Master 負擔）](#從-replica-執行備份降低-master-負擔)
  - [利用備份建立新 Replica](#利用備份建立新-replica)
    - [1. 還原備份至新 Replica](#1-還原備份至新-replica)
    - [2. 查看 metadata 取得 binlog 位置](#2-查看-metadata-取得-binlog-位置)
    - [3. 設定 Replica 連線](#3-設定-replica-連線)
    - [4. 確認同步狀態](#4-確認同步狀態)
- [版本差異（0.9.x vs 1.0.x）](#版本差異09x-vs-10x)
- [MySQL 5.7 → 8.4 遷移注意事項](#mysql-57--84-遷移注意事項)
  - [JSON 欄位匯入失敗](#json-欄位匯入失敗)
  - [修正方式](#修正方式)

## 參考資料

[mydumper GitHub](https://github.com/mydumper/mydumper)

[mydumper 官方文件](https://mydumper.github.io/mydumper/)

### 相關筆記

[MySQL 筆記（主）](./MySQL_筆記.md)
[MySQL 工具 - Percona XtraBackup 備份](./MySQL_工具_Percona_XtraBackup(資料備份的工具).md)
[MySQL 工具 - mysqlbinlog](./MySQL_工具_mysqlbinlog(檢查主資料庫中的二進制日誌).md)

# 安裝

## Debian (Ubuntu)

```bash
apt install mydumper
```

若版本較舊或找不到套件，從 GitHub Releases 下載 deb 安裝：

```bash
# 查詢最新版本號後替換 x.x.x
wget https://github.com/mydumper/mydumper/releases/download/vx.x.x/mydumper_x.x.x-focal_amd64.deb
dpkg -i mydumper_x.x.x-focal_amd64.deb
```

## RedHat (CentOS)

yum / dnf 預設 repo 不包含 mydumper，需從 GitHub Releases 直接下載 RPM 安裝。

先安裝相依套件：

```bash
# CentOS 7
yum install -y glib2 pcre zlib

# CentOS 8 / Rocky / AlmaLinux
dnf install -y glib2 pcre zlib
```

從 GitHub Releases 下載 RPM 安裝（依版本選擇對應 el 號碼）：

```bash
# CentOS 7（el7）
yum install -y https://github.com/mydumper/mydumper/releases/download/v1.0.1-1/mydumper-1.0.1-1.el7.x86_64.rpm

# CentOS 8 / Rocky 8 / AlmaLinux 8（el8）
dnf install -y https://github.com/mydumper/mydumper/releases/download/v1.0.1-1/mydumper-1.0.1-1.el8.x86_64.rpm

# CentOS 9 / Rocky 9 / AlmaLinux 9（el9）
dnf install -y https://github.com/mydumper/mydumper/releases/download/v1.0.1-1/mydumper-1.0.1-1.el9.x86_64.rpm
```

確認版本：

```bash
mydumper --version
```

> 最新版本請至 [GitHub Releases](https://github.com/mydumper/mydumper/releases) 查詢，替換上方 URL 中的版本號。

# 指令

## mydumper 備份

### 備份全部資料庫

```bash
mydumper \
  -u root -p password -h 127.0.0.1 -P 3306 \
  --threads 8 \
  --compress \
  --trx-consistency-only \
  --routines --events --triggers \
  --exclude-databases mysql,sys,information_schema,performance_schema \
  --outputdir /backup/mydumper
```

### 備份指定資料庫

```bash
mydumper \
  -u root -p password -h 127.0.0.1 -P 3306 \
  --threads 8 \
  --compress \
  --trx-consistency-only \
  --database mydb \
  --outputdir /backup/mydumper
```

### 備份指定資料表

```bash
mydumper \
  -u root -p password -h 127.0.0.1 -P 3306 \
  --threads 8 \
  --database mydb \
  --tables-list table1,table2 \
  --outputdir /backup/mydumper
```

## myloader 還原

### 還原全部資料庫

```bash
myloader \
  -u root -p password -h 127.0.0.1 -P 3306 \
  --threads 8 \
  --directory /backup/mydumper \
  --overwrite-tables
```

### 還原指定資料庫

```bash
myloader \
  -u root -p password -h 127.0.0.1 -P 3306 \
  --threads 8 \
  --database mydb \
  --directory /backup/mydumper \
  --overwrite-tables
```

## 常用參數

### mydumper 參數

| 參數 | 說明 |
|------|------|
| `-u` / `--user` | MySQL 使用者 |
| `-p` / `--password` | MySQL 密碼 |
| `-h` / `--host` | 主機位址 |
| `-P` / `--port` | 連線埠 |
| `-t` / `--threads` | 平行執行緒數（建議設為 CPU 核心數） |
| `-o` / `--outputdir` | 輸出目錄 |
| `-c` / `--compress` | 壓縮輸出（`.gz`） |
| `-B` / `--database` | 備份指定資料庫 |
| `-T` / `--tables-list` | 備份指定資料表（逗號分隔） |
| `--exclude-databases` | 排除指定資料庫（逗號分隔） |
| `--trx-consistency-only` | 僅對 InnoDB 使用一致性快照（速度較快） |
| `--routines` | 包含 Stored Procedures / Functions |
| `--events` | 包含 Events |
| `--triggers` | 包含 Triggers |
| `--no-data` | 只備份 schema，不備份資料 |
| `--rows` | 每個資料檔的最大列數（切分大表） |
| `-a` / `--ask-password` | 互動輸入密碼（不留在指令與 history，取代 `-p`） |
| `-S` / `--socket` | 走本機 socket 連線（例 `/var/run/mysqld/mysqld.sock`） |
| `-L` / `--logfile` | log 寫到檔案（不輸出到畫面） |
| `-v` / `--verbose` | log 詳細程度：`0` 靜音、`1` 錯誤、`2` 警告、`3` 資訊 |
| `-l` / `--long-query-guard` | 長查詢等待秒數上限，超過就中止（大表可設 `86400`） |

### myloader 參數

| 參數 | 說明 |
|------|------|
| `-u` / `--user` | MySQL 使用者 |
| `-p` / `--password` | MySQL 密碼 |
| `-h` / `--host` | 主機位址 |
| `-P` / `--port` | 連線埠 |
| `-t` / `--threads` | 平行執行緒數 |
| `-d` / `--directory` | 備份目錄 |
| `-B` / `--database` | 還原至指定資料庫（可重新命名） |
| `--overwrite-tables` | 目標資料表已存在時自動 DROP 再建立 |
| `--enable-binlog` | 還原時啟用 binlog（預設關閉以加速） |
| `--skip-triggers` | 不還原 Triggers |
| `--skip-post` | 不執行 post-import SQL（如重建 index） |
| `-s` / `--source-db` | 從備份中選擇指定來源資料庫 |

## 長時間備份還原：tmux 執行 + tail -f 監看 log

大型資料庫備份/還原動輒數小時，固定流程：**舊 log 移開 → tmux 裡執行並寫 log → 另一個視窗 `tail -f` 看進度 → 完成後 grep 檢查錯誤**。

**1. 重跑前先把舊 log 移開**

同名 log 若被接在舊內容後面寫，完成後 `grep -c CRITICAL` 會把上一次的錯誤也算進去，誤判這次失敗。舊 log 移到另一個目錄留作紀錄，用不到再刪：

```bash
mkdir -p /data/old_run && mv /data/dump_full.log /data/load_full.log /data/old_run/ && ls /data /data/old_run
```

**2. 在 tmux 裡執行，log 寫到檔案**

```bash
tmux new -s dump

mkdir -p /data && rm -rf /data/dump_full && time mydumper \
  -S /var/run/mysqld/mysqld.sock -u root -a \
  -B mydb -t 4 -r 500000 -c gzip --trx-tables -l 86400 \
  -o /data/dump_full \
  -L /data/dump_full.log --verbose 3
```

- `time`：結束時印出總耗時，方便下次估時間。
- `-L` + `--verbose 3`：進度寫進 log 檔，畫面上幾乎沒有輸出，要靠下一步看進度。
- `rm -rf` 輸出目錄：mydumper 要求輸出目錄是空的；目錄不存在也不影響。

**3. 另開視窗看進度**

```
Ctrl+b c        tmux 開新 window（或 Ctrl+b % / Ctrl+b " 分割 pane，一邊跑一邊看）
```

```bash
tail -f /data/dump_full.log

# 只看錯誤
tail -f /data/dump_full.log | grep --line-buffered -E 'CRITICAL|ERROR|WARNING'
```

- 在監看視窗按 `Ctrl+C` 只會停掉 `tail`，不會中斷備份。
- 要離開 SSH：`Ctrl+b d` detach，之後 `tmux a -t dump` 回來。

**4. 完成後檢查**

```bash
grep -c CRITICAL /data/dump_full.log      # 0 才算成功
grep -n CRITICAL /data/dump_full.log | head
```

**5. 看懂 `time` 的輸出**

```
real    19m10s    ← 實際經過時間（遷移停機時間看這個）
user    68m...    ← 所有執行緒加總的 CPU 時間（多執行緒時會大於 real）
sys     ...       ← 系統呼叫（磁碟 I/O 等）耗用的 CPU 時間
```

- 換版本或換參數後 `real` 變長時，比對 `user`：`user` 大增多半是壓縮（`-c gzip`）或匯出的資料變多，不一定是異常。
- 每次的 `real` 記進遷移時間紀錄，用來估正式切換的停機時間。

## 備份完成後的完整性檢查

還原前先確認備份完整，不完整的備份匯進去只會浪費一輪時間：

```bash
du -sh /data/dump_full
ls /data/dump_full/*-schema.sql.gz | grep -v 'schema-create' | wc -l
grep -ciE 'error|critical' /data/dump_full.log
tail -3 /data/dump_full.log
grep -A3 -i 'master' /data/dump_full/metadata
```

| 檢查 | 預期 |
|---|---|
| `du -sh` | 和來源資料量同一個數量級（壓縮後通常為原本的幾分之一）；明顯過小代表資料不完整 |
| `-schema.sql.gz` 數量 | 等於來源資料庫的表數（`<db>-schema-create` 是建庫語法，要排除；有 view 時也會被算進去） |
| `grep -ci 'error\|critical'` | `0` |
| `tail -3` | 有 `Finished dump at` |
| `metadata` | 有 binlog 檔名、位置與 GTID，代表這份備份的時間點（之後接 Replica 或補增量要用，見「查看 metadata 取得 binlog 位置」） |

來源表數查法：

```sql
SELECT COUNT(*) FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'mydb' AND TABLE_TYPE = 'BASE TABLE';
```

## 還原到雲端 Managed Database（SSL）

雲端代管資料庫（Linode / Akamai Managed Database 等）通常是**非預設 port、強制 SSL**，連線參數照控制台提供的填：

```bash
# 1. 先清掉上一次失敗匯入的資料（確認是目標 DB，不是來源！）
mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p --ssl-mode=REQUIRED \
  -e "DROP DATABASE IF EXISTS mydb; SHOW DATABASES;"

# 2. 舊的 load log 已移開後，在 tmux 裡還原
tmux new -s load
time myloader \
  -h $DB_HOST -P $DB_PORT -u $DB_USER -a --ssl-mode=REQUIRED \
  -d /data/dump_full -t 4 \
  -L /data/load_full.log --verbose 3

# 3. 另開視窗監看，完成後檢查
tail -F /data/load_full.log
grep -c CRITICAL /data/load_full.log
```

- 重匯前一定要 DROP 舊資料：上一輪部分成功的表會讓這輪出現重複資料或錯誤（或改用 `--overwrite-tables`）。
- `DROP DATABASE` 前再確認一次 `$DB_HOST` 是目標主機。
- 代管資料庫的管理帳號通常沒有 SUPER 權限，匯入 Trigger / Routine 的 `DEFINER` 可能失敗，看 log 的 CRITICAL 判斷。

tail / tmux 用法細節見 [tail 筆記](../../../02_作業系統/Unix/Linux/Linux_工具/Linux_文本編碼/Linux_工具_tail(顯示檔案最後幾行).md)、[tmux 筆記](../../../02_作業系統/Unix/Linux/Linux_工具/Linux_工具_tmux(終端機管理工具).md)。

# 與 mysqldump 比較

| 項目 | mysqldump | mydumper |
|------|-----------|----------|
| 備份速度 | 單執行緒，慢 | 多執行緒，快 3～10 倍 |
| 還原速度 | 單執行緒，慢 | 多執行緒，快 5～10 倍 |
| 輸出格式 | 單一 `.sql` 或 `.sql.gz` | 目錄，每張表一個檔案 |
| 大表切分 | 不支援 | `--rows` 自動切分 |
| 一致性快照 | 支援 | 支援（`--trx-consistency-only`） |
| 適用場景 | 小型備份、腳本簡單 | 大型資料庫、遷移、需要快速還原 |

# 匯入加速設定

還原前在 MySQL 執行，可大幅縮短匯入時間（完成後記得改回）：

```sql
-- 關閉 binlog（不需要 replication 時）
SET GLOBAL sql_log_bin = 0;

-- 放寬 InnoDB flush 策略（0 最快，1 最安全，2 折衷）
SET GLOBAL innodb_flush_log_at_trx_commit = 2;
SET GLOBAL sync_binlog = 0;
```

還原完成後改回：

```sql
SET GLOBAL sql_log_bin = 1;
SET GLOBAL innodb_flush_log_at_trx_commit = 1;
SET GLOBAL sync_binlog = 1;
```

# Replica 相關

## 對現有 Replica 的影響

### mydumper 備份

| 執行位置 | 參數 | Replica 影響 |
|---------|------|-------------|
| Master 上 | `--trx-consistency-only` | 幾乎無影響，InnoDB 用一致性快照，不加全局讀鎖 |
| Master 上 | 不加該參數 | 短暫 FTWRL 全局讀鎖，期間 Replica 可能短暫 lag，dump 完即恢復 |
| Replica 上執行 | — | 完全不影響 Master，是備份的推薦做法 |

### myloader 還原

| 設定 | Replica 影響 |
|------|-------------|
| 預設（不加 `--enable-binlog`） | 匯入資料不寫 binlog → Replica 收不到，兩邊資料不一致 |
| 加 `--enable-binlog` | 資料正常寫 binlog → Replica 同步，速度稍慢 |

若要保持 Replica 同步，myloader 必須加 `--enable-binlog`：

```bash
myloader \
  -u root -p password \
  --threads 8 \
  --enable-binlog \
  --directory /backup/mydumper \
  --overwrite-tables
```

## 從 Replica 執行備份（降低 Master 負擔）

在現有 Replica 上執行 mydumper，可避免備份時影響 Master 的寫入：

```bash
mydumper \
  -u root -p password -h 127.0.0.1 -P 3306 \
  --threads 8 \
  --compress \
  --trx-consistency-only \
  --replica-data \
  --routines --events --triggers \
  --exclude-databases mysql,sys,information_schema,performance_schema \
  --outputdir /backup/mydumper
```

| 參數 | 說明 |
|------|------|
| `--replica-data` | 暫停 SQL thread，將 Master binlog 位置記錄至 metadata（舊版為 `--slave-data`） |

> ⚠️ mydumper 會短暫停止 Replica SQL thread 以取得一致的 binlog 位置，備份完成後自動恢復。

## 利用備份建立新 Replica

從現有 mydumper 備份（含 binlog 位置）直接建立新 Replica，不需要對 Master 進行全量備份。

### 1. 還原備份至新 Replica

```bash
myloader \
  -u root -p password -h 127.0.0.1 -P 3306 \
  --threads 8 \
  --directory /backup/mydumper \
  --overwrite-tables
```

### 2. 查看 metadata 取得 binlog 位置

```bash
cat /backup/mydumper/metadata
```

輸出範例：

```
Started dump at: 2026-04-29 10:00:00
SHOW MASTER STATUS:
	Log: mysql-bin.000123
	Pos: 456789
	GTID: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx:1-5000

Finished dump at: 2026-04-29 10:05:00
```

### 3. 設定 Replica 連線

**GTID 模式（推薦）：**

```sql
STOP REPLICA;
RESET REPLICA ALL;
RESET BINARY LOGS AND GTIDS;

CHANGE REPLICATION SOURCE TO
  SOURCE_HOST='master_ip',
  SOURCE_USER='repl',
  SOURCE_PASSWORD='repl密碼',
  SOURCE_AUTO_POSITION=1;

START REPLICA;
```

**傳統 binlog position 模式（非 GTID）：**

```sql
STOP REPLICA;
RESET REPLICA ALL;

CHANGE REPLICATION SOURCE TO
  SOURCE_HOST='master_ip',
  SOURCE_USER='repl',
  SOURCE_PASSWORD='repl密碼',
  SOURCE_LOG_FILE='mysql-bin.000123',
  SOURCE_LOG_POS=456789;

START REPLICA;
```

### 4. 確認同步狀態

```sql
SHOW REPLICA STATUS\G
```

確認以下兩項為 `Yes`：

```
Replica_IO_Running: Yes
Replica_SQL_Running: Yes
```

# 版本差異（0.9.x vs 1.0.x）

mydumper 0.9.x 與 1.0.x 的部分參數不相容，在舊系統（如 Ubuntu 16.04 xenial）上預設安裝的版本通常為 0.9.x。

| 功能 | 0.9.x | 1.0.x |
|------|-------|-------|
| 鎖定模式 | `--less-locking` | `--less-locking` 或 `--sync-thread-lock-mode NO_LOCK` + `--trx-tables` |
| 寫入已存在的目錄 | 不支援（每次需要空目錄） | `--dirty`（允許目錄已有檔案） |
| `--tables-list` 格式 | 純 table 名稱，需同時加 `--database` | `db.table` 格式，不需 `--database` |
| JSON 匯出格式 | 依 libmysqlclient 版本，可能使用 `_binary` 前綴 | 較新版與 MySQL 8.x 相容 |

## 0.9.x 多次備份同一目錄

0.9.x 不支援 `--dirty`，多次備份若目錄已有檔案會報錯。  
解法：每次備份使用暫存子目錄，完成後將 `.sql(.gz)` 合併至主目錄：

```bash
# Run 1：備份 schema → 直接寫入 OUTDIR（此時為空目錄）
mydumper ... --no-data --outputdir "$OUTDIR"

# Run 2：備份資料 → 先寫暫存目錄，再合併
TMP="${OUTDIR}/.run2_tmp"
mkdir -p "$TMP"
mydumper ... --no-schemas --outputdir "$TMP"
mv "$TMP"/*.sql.gz "$OUTDIR"/
rm -rf "$TMP"
```

# MySQL 5.7 → 8.4 遷移注意事項

## JSON 欄位匯入失敗

**症狀**（myloader 錯誤訊息）：

```
CRITICAL: Error restoring <db>.<table> from file ...
Error (1366) at line ...:
Incorrect integer value: '...' for column '...' at row 1
-- 或 --
Cannot create a JSON value from a string with CHARACTER SET 'binary'
```

**根本原因**：

mydumper 0.9.x 若以舊版 MySQL 5.7.x client library（如 5.7.11）建置，匯出 JSON 欄位時會產生 `_binary '...'` 前綴格式。此格式在 MySQL 8.x 為非法 JSON 值，匯入會失敗。

mydumper 1.0.x 以 MySQL 5.7.44+ 建置的版本，JSON 匯出格式與 MySQL 8.x 相容，不會有此問題。

確認版本：

```bash
mydumper --version
# 範例：mydumper 0.9.1, built against MySQL 5.7.11   ← 有此問題
# 範例：mydumper v1.0.1-1, built against MySQL 5.7.44 ← 正常
```

## 修正方式

**方式一：升級 mydumper（根本解法）**

將來源主機的 mydumper 升級至 1.0.x（需確認 OS 版本相容性），重新執行備份與還原。

**方式二：改用 mysqldump 備份受影響的資料庫（暫時解法）**

若來源主機無法安裝新版 mydumper（如 Ubuntu 16.04 已 EOL），對含 JSON 欄位的資料庫改用 `mysqldump --hex-blob`：

```bash
# 備份整個資料庫（在來源主機執行）
mysqldump \
  --single-transaction \
  --hex-blob \
  --routines --events --triggers \
  -u root -p \
  mydb > /tmp/mydb_full.sql

# 壓縮（可選）
gzip /tmp/mydb_full.sql
```

傳輸並還原至目標主機：

```bash
# 目標主機
mysql -u root -p mydb < /tmp/mydb_full.sql
# 或
zcat /tmp/mydb_full.sql.gz | mysql -u root -p mydb
```

> `--hex-blob` 會將 BLOB 與 JSON 欄位以十六進制字串輸出，MySQL 8.x 可正常匯入。  
> 此方式為單執行緒，資料庫較大時速度較慢；若只有少數資料庫受影響，可針對性使用。
