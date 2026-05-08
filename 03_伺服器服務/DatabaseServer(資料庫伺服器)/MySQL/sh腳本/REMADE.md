# 腳本用法

## check_all_tables.sh

```
檢查整個資料庫的所有表，Slave 若缺表就自動從 Master 匯出 schema 建立。

只建立缺表

主機/帳號/資料庫 → 用輸入的
密碼 → 使用 read -s 隱藏輸入
```

建立檔案 貼上內容並存檔

```sh
nano check_all_tables.sh
```

給執行權限

```sh
chmod +x check_all_tables.sh
```

只檢查（不建立）

```sh
./check_all_tables.sh --dry-run
```

```sh
bash check_all_tables.sh --dry-run
```

真正執行（缺表會被建立）

```sh
./check_all_tables.sh
```

```sh
bash check_all_tables.sh
```

程式會提示輸入 Master/Slave 連線資訊，輸入時 Slave/Master 密碼是隱藏的（不會顯示）。

## check_table_binlog.sh

```
去 Master 的 binlog 裡搜尋 table 相關的操作，這樣你就能知道它到底只是「舊事件」還是「目前還有人在寫」。
```

```
互動輸入 Master / Slave 連線資訊 + 要檢查的表
從 Master 讀 binlog 列表，逐一檢查是否有該表的操作
顯示 Master 檢查結果
從 Slave 讀 binlog 列表，逐一檢查是否有該表的操作
顯示 Slave 檢查結果
```

執行

```sh
bash check_table_binlog.sh
```

## create_export_temp_table.sh

```
從指定資料表建立暫存表，將資料匯出為 CSV 檔案後自動刪除暫存表。
匯出路徑固定為 /var/lib/mysql-files/<table>_temp.txt（MySQL secure_file_priv 目錄）。

使用 getopts 旗標傳入參數（適合腳本呼叫或自動化流程）。
```

參數說明

| 旗標 | 說明 | 必填 | 預設值 |
|------|------|------|--------|
| `-u` | MySQL 使用者 | 是 | — |
| `-p` | MySQL 密碼 | 是 | — |
| `-d` | 資料庫名稱 | 是 | — |
| `-t` | 資料表名稱 | 是 | — |
| `-h` | 主機位址 | 否 | `127.0.0.1` |
| `-P` | 連線埠 | 否 | `3306` |

執行範例

```sh
bash create_export_temp_table.sh -u root -p MyPass -d mydb -t mytable
```

```sh
bash create_export_temp_table.sh -u root -p MyPass -d mydb -t mytable -h 192.168.1.10 -P 3307
```

流程：建立 `<table>_temp` → 匯出 CSV 到 `/var/lib/mysql-files/` → 刪除暫存表

> 注意：腳本目前只 SELECT `column1, column2`，使用前需依實際欄位修改腳本內容。

## create_export_temp_table_2.sh

```
功能與 create_export_temp_table.sh 相同，但改用位置參數傳入（適合手動執行或簡單測試）。
```

用法

```sh
bash create_export_temp_table_2.sh <user> <password> <database> <table> [host] [port]
```

執行範例

```sh
bash create_export_temp_table_2.sh root MyPass mydb mytable
```

```sh
bash create_export_temp_table_2.sh root MyPass mydb mytable 192.168.1.10 3307
```

| 位置 | 說明 | 必填 | 預設值 |
|------|------|------|--------|
| `$1` | MySQL 使用者 | 是 | — |
| `$2` | MySQL 密碼 | 是 | — |
| `$3` | 資料庫名稱 | 是 | — |
| `$4` | 資料表名稱 | 是 | — |
| `$5` | 主機位址 | 否 | `127.0.0.1` |
| `$6` | 連線埠 | 否 | `3306` |

> 與 v1 差異：使用位置參數而非 `-u/-p/-d/-t` 旗標，密碼直接明文傳入。

## mysqldump_timer.sh

```
依日期產生檔名，對多張資料表執行 mysqldump，並自動清除 2 天前的舊備份檔。
同時支援使用 INTO OUTFILE 格式匯出 CSV（mysqldump --tab）。
```

```
日期格式：YYYY-MM-DD（以系統日期為準）
備份路徑：/tmp/testSpeed/
CSV 匯出路徑：/var/lib/mysql-files/（mysqldump --tab 指定）
自動清除：/tmp/testSpeed/ 下超過 2 天的 .sql 檔
```

直接執行（需先修改腳本內的密碼、資料庫名、資料表名）

```sh
bash mysqldump_timer.sh
```

腳本說明

| 動作 | 說明 |
|------|------|
| 第一個 dump | 匯出完整資料表（不含 CREATE DATABASE，使用 `--replace`） |
| 第二個 dump | 加上 `-w` 篩選條件，只匯出當天 timestamp 之後的資料 |
| 第三個 dump | 匯出另一張資料表 |
| `--tab` dump | 將資料以逗號分隔 CSV 格式匯出至 `/var/lib/mysql-files/` |
| `find` 清理 | 刪除 `/tmp/testSpeed/` 下 2 天以上的 `.sql` 備份檔 |

> 注意：腳本內密碼為明文硬編碼，使用前請替換為實際帳密與資料表名稱，建議配合 crontab 定期執行。

## mysqldump_and_sync_timer.sh

```
兩階段備份腳本：
  階段一：mysqldump | gzip 壓縮備份（進度條顯示）
  階段二：rsync 傳送至遠端主機（spinner 顯示），傳送成功後刪除本地備份
兩階段分別計時，最後輸出總結。全程寫入 log，任一失敗以非零退出碼結束。
```

流程

```
[階段一] 取得目標 DB 清單 → 過濾系統 DB
  → 單一檔模式：mysqldump --all-databases | gzip → all_databases-DATE.sql.gz（spinner）
  → 獨立檔模式：每個 DB 各自 mysqldump | gzip → <db>-DATE.sql.gz（進度條）
  ↓
[階段二] rsync 傳送備份目錄至遠端（spinner）→ 成功後刪除本地 .sql.gz
  ↓
輸出總結：階段一耗時 / 階段二耗時 / 總耗時
```

用法

```sh
bash mysqldump_and_sync_timer.sh [--pass] [--single|--multi] <mode> [databases...]
```

模式

| 模式 | 說明 | 預設輸出 |
|------|------|---------|
| `all` | 備份所有使用者資料庫（系統 DB 自動排除） | 單一 `all_databases-DATE.sql.gz` |
| `list db1 db2` | 只備份指定的資料庫 | 每個 DB 各自獨立 `.sql.gz` |

選項

| 旗標 | 說明 |
|------|------|
| `--single` | 強制合併為單一 `all_databases-DATE.sql.gz` |
| `--multi` | 強制每個 DB 各自獨立（覆蓋 `all` 的預設） |
| `--pass` | 使用密碼傳送（`sshpass`），預設使用 SSH Key |
| `-h` / `--help` | 顯示說明 |

執行範例

```sh
bash mysqldump_and_sync_timer.sh all                  # 單一檔案（all 預設）
bash mysqldump_and_sync_timer.sh --multi all          # 每個 DB 獨立檔案
bash mysqldump_and_sync_timer.sh --pass all           # 單一檔案 + 密碼傳送
bash mysqldump_and_sync_timer.sh list mydb1 mydb2     # 獨立檔案（list 預設）
bash mysqldump_and_sync_timer.sh --single list mydb1  # 單一檔案
```

搭配 crontab（每天凌晨 2 點執行）

```sh
0 2 * * * /bin/bash /path/to/mysqldump_and_sync_timer.sh all
```

必填設定（腳本頂部 `!! 使用前請修改以下設定 !!` 區塊）

| 變數 | 說明 |
|------|------|
| `DB_USER` / `DB_PASS` | MySQL 帳密 |
| `DB_HOST` / `DB_PORT` | MySQL 主機與埠 |
| `BACKUP_DIR` | 本地備份暫存路徑 |
| `REMOTE_USER` / `REMOTE_HOST` / `REMOTE_DIR` | rsync 遠端目標 |
| `SSH_KEY` | SSH 私鑰路徑（預設認證方式） |
| `REMOTE_PASS` | SSH 密碼（使用 `--pass` 時） |
| `LOG_FILE` | 日誌路徑 |

備註

- `all` 模式需帳號具備 `SHOW DATABASES` 權限
- `all_databases` 單一檔模式使用 `--all-databases`；獨立檔模式每個 DB 使用 `PIPESTATUS` 同時檢查 mysqldump 與 gzip exit code
- rsync 使用 `--delete` 保持遠端一致；SSH 連線 timeout 10 秒
- 傳送成功才刪除本地 `.sql.gz`，失敗時保留
- exit code `0` = 兩階段全部成功；`1` = 任一失敗

## mysqlrestore_and_fix_mysql84_timer.sh

```
兩階段還原腳本：
  階段一：fix_mysql84.py 修正 SQL 相容性（字元集、欄位寬度、collation 等）
  階段二：mysql 匯入還原至目標資料庫（spinner 顯示）
兩階段分別計時，最後輸出總結。任一失敗以非零退出碼結束。
```

流程

```
[前置] 檢查輸入檔、fix_mysql84.py、Python 是否存在
  ↓
[階段一] fix_mysql84.py 修正 SQL → <input>_mysql84.sql（進度條顯示）
         （若輸入為 .sql.gz 則先自動解壓縮，修正後刪除暫存解壓檔）
  ↓
[階段二] mysql < 修正後的 SQL → 匯入目標資料庫（spinner 顯示）
  ↓
輸出總結：階段一耗時 / 階段二耗時 / 總耗時
```

用法

```sh
bash mysqlrestore_and_fix_mysql84_timer.sh <input.sql[.gz]> [database]
bash mysqlrestore_and_fix_mysql84_timer.sh -h
```

| 參數 | 說明 | 必填 |
|------|------|------|
| `<input.sql[.gz]>` | 要還原的 SQL 檔（支援 `.sql` 或 `.sql.gz`） | 是 |
| `[database]` | 匯入目標資料庫；省略則匯入全部資料庫（適用 `--all-databases` dump） | 否 |

執行範例

```sh
bash mysqlrestore_and_fix_mysql84_timer.sh /tmp/all_databases.sql.gz        # 匯入全部資料庫
bash mysqlrestore_and_fix_mysql84_timer.sh /tmp/dump.sql.gz mydb            # 匯入指定資料庫
```

必填設定（腳本頂部 `!! 使用前請修改以下設定 !!` 區塊）

| 變數 | 說明 |
|------|------|
| `DB_USER` / `DB_PASS` | MySQL 帳密 |
| `DB_HOST` / `DB_PORT` | MySQL 主機與埠 |
| `FIX_SCRIPT` | fix_mysql84.py 的完整路徑 |

備註

- 輸入 `.sql.gz` 時自動解壓，修正完成後刪除暫存的 `.sql` 解壓檔
- 修正後輸出自動命名為 `<原檔名>_mysql84.sql`
- fix_mysql84.py 修正內容包含：整數寬度移除、utf8→utf8mb4、ROW_FORMAT 移除、collation 統一等
- exit code `0` = 兩階段全部成功；`1` = 任一失敗
