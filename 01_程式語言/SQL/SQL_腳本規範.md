# SQL 腳本規範

```
撰寫 SQL 腳本（schema 變更、資料修正、報表查詢）的統一約束：
格式、命名、安全網、交易、效能。以 MySQL 為主，其他 DB 通用。
```

## 目錄

- [SQL 腳本規範](#sql-腳本規範)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [格式](#格式)
- [命名](#命名)
- [安全網（防呆必備）](#安全網防呆必備)
- [交易與鎖](#交易與鎖)
- [效能](#效能)
- [檔案管理](#檔案管理)

## 參考資料

[MySQL 筆記](../../03_伺服器服務/DatabaseServer(資料庫伺服器)/MySQL/MySQL_筆記.md)

[MySQL 官方文檔](https://dev.mysql.com/doc/)

# 格式

- 關鍵字大寫、識別字小寫：`SELECT id FROM orders WHERE ...`。
- 每個子句換行（SELECT / FROM / WHERE / GROUP BY / ORDER BY），多欄位逐欄一行。
- 檔頭註解：用途、執行對象（哪個 DB / 環境）、預期影響筆數、作者與日期。

```sql
-- 用途：修正 2026-07 訂單狀態誤標
-- 對象：prod / order_db
-- 預期影響：約 1,200 筆
-- 日期：2026-07-20
```

# 命名

- 資料表 / 欄位：小寫 `snake_case`；表名用複數或模組前綴，專案內統一。
- 索引：`idx_{表}_{欄位}`；外鍵：`fk_{子表}_{父表}`；唯一鍵：`uk_{表}_{欄位}`。
- 腳本檔名：`{日期}_{動詞}_{對象}.sql`，如 `20260720_fix_order_status.sql`。

# 安全網（防呆必備）

- **先 SELECT 再 UPDATE/DELETE**：修改前先用同條件 `SELECT COUNT(*)` 確認影響筆數與預期一致。
- `UPDATE` / `DELETE` 必帶 `WHERE`；批次操作加 `LIMIT` 分批跑。
- DDL（`DROP` / `ALTER`）前先備份：`CREATE TABLE xxx_bak_20260720 AS SELECT * FROM xxx`（或 mysqldump 單表）。
- 破壞性腳本開頭註明回滾方式（回滾 SQL 或還原備份表）。
- 應用程式端組 SQL 一律參數化查詢，禁止字串拼接。

# 交易與鎖

- 多敘述的資料修正包在交易內，確認無誤才 `COMMIT`：

```sql
START TRANSACTION;
UPDATE ...;
-- 檢查結果
SELECT ... ;
COMMIT;   -- 有問題改 ROLLBACK;
```

- 大量更新避開高峰時段；長交易會擋複寫與其他連線，超過萬筆改分批。
- 線上大表 DDL 評估 `pt-online-schema-change` 或 MySQL 8 `ALGORITHM=INSTANT`。

# 效能

- 上線前用 `EXPLAIN` 確認走索引，避免全表掃描。
- `WHERE` 條件欄位避免套函數（會失效索引）。
- 禁止 `SELECT *` 進應用程式；報表查詢明列欄位。

# 檔案管理

- Schema 變更（migration）與一次性資料修正分開存放。
- Migration 檔案照日期序號排序，執行過的不回頭改內容——要改就開新檔。
