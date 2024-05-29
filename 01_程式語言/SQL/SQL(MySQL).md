# SQL(MySQL)

```
```

## 目錄

- [SQL(MySQL)](#sqlmysql)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [取得資料庫中表格的架構資訊（包括列名、資料類型、約束等）](#取得資料庫中表格的架構資訊包括列名資料類型約束等)
  - [使用 SQL 查詢來計算符合條件的資料行數](#使用-sql-查詢來計算符合條件的資料行數)

## 參考資料

[]()

# 指令

# 用法

## 取得資料庫中表格的架構資訊（包括列名、資料類型、約束等）

```sql
SELECT
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    CHARACTER_MAXIMUM_LENGTH
FROM
    INFORMATION_SCHEMA.COLUMNS
WHERE
    TABLE_NAME = 'your_table_name' AND
    TABLE_SCHEMA = 'your_database_name';
```

## 使用 SQL 查詢來計算符合條件的資料行數

```sql
-- 假設 date 列是儲存日期的列，可以使用 DATEDIFF 函數和目前日期來篩選數據
SELECT COUNT(*) AS record_count
FROM 'your_table_name'
WHERE date < NOW() - INTERVAL 6 MONTH;

-- NOW() - INTERVAL 6 MONTH：計算出六個月前的日期。
-- WHERE date < ...：篩選出日期列中早於六個月前的所有記錄。 COUNT(*)：統計滿足條件的記錄數量。
-- 步驟解釋 取得半年前的日期：使用 NOW() 函數取得目前日期和時間，再減去 INTERVAL 6 MONTH 得到六個月前的日期。
-- 篩選記錄：在 WHERE 子句中篩選出 date 欄位值小於六個月前的所有記錄。
-- 計數：使用 COUNT(*) 函數統計篩選出的記錄數量。
```

確認資料

```sql
SELECT *
FROM 'your_table_name'
WHERE date < NOW() - INTERVAL 1 YEAR
-- 後面
ORDER BY date DESC
-- 10筆
LIMIT 10;
```

刪除資料

```sql
DELETE FROM 'your_table_name'
WHERE date < NOW() - INTERVAL 1 YEAR;
```

查看結果

```sql
SELECT COUNT(*) AS remaining_records
FROM 'your_table_name';
```

```sql
-- 一年以上
NOW() - INTERVAL 1 YEAR
-- 半年
NOW() - INTERVAL 6 MONTH
```