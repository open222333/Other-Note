# MySQL 工具 mydumper/myloader(平行備份還原工具)

```
mydumper：多執行緒平行備份，輸出為目錄（每張表一個檔案）。
myloader：多執行緒平行還原，速度遠快於 mysql < dump.sql。
適合大型資料庫備份與跨版本遷移（如 MySQL 5.7 → 8.4）。
```

## 目錄

- [MySQL 工具 mydumper/myloader(平行備份還原工具)](#mysql-工具-mydumpermyloader平行備份還原工具)
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
- [與 mysqldump 比較](#與-mysqldump-比較)
- [匯入加速設定](#匯入加速設定)

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

```bash
yum install mydumper
```

或從 GitHub Releases 下載 rpm 安裝：

```bash
wget https://github.com/mydumper/mydumper/releases/download/vx.x.x/mydumper-x.x.x-1.el7.x86_64.rpm
rpm -ivh mydumper-x.x.x-1.el7.x86_64.rpm
```

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
