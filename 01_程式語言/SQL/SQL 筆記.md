# SQL 筆記

## 目錄

- [SQL 筆記](#sql-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [範例](#範例)
		- [鎖定](#鎖定)
		- [解除鎖定](#解除鎖定)
		- [模糊匹配(可能導致性能問題，特別是當數據量龐大時)](#模糊匹配可能導致性能問題特別是當數據量龐大時)
		- [重複出現統計 COUNT](#重複出現統計-count)
		- [合併 JOIN](#合併-join)

## 參考資料

[SQL語法手冊](http://tw.gitbook.net/sql/index.html)

[SQL語法手冊](https://www.1keydata.com/tw/sql/sql.html)

# 用法

## 範例

```sql
-- 將資料從資料庫中的表格內選出
SELECT "欄位名" FROM "表格名";

-- 選擇性地選出資料
SELECT "欄位名" FROM "表格名" WHERE "條件";

-- 表格/欄位內有哪些不同的值
SELECT DISTINCT "欄位名" FROM "表格名";

-- { }+ 代表{ }之內的情況會發生一或多次。
-- 在這裡的意思就是 AND 加 簡單條件及 OR 加簡單條件的情況可以發生一或多次。
-- 另外，可以用 ( ) 來代表條件的先後次序。
SELECT "欄位名" FROM "表格名" WHERE "簡單條件" {[AND|OR] "簡單條件"}+;
```

### 鎖定

```sql
-- 共享鎖（Shared Lock）
-- 用於讀取資料，多個事務可以同時持有共享鎖，互不干擾。當一個事務持有共享鎖時，其他事務可以讀取資料但無法修改。
SELECT * FROM table_name WITH (SHARED LOCK);

-- 排他鎖（Exclusive Lock）
-- 用於寫入或修改資料，一個事務獨佔地持有排他鎖，其他事務無法同時持有共享或排他鎖。
SELECT * FROM table_name WITH (EXCLUSIVE LOCK);

-- 行鎖定（Row Locking）
-- 僅鎖定特定的行，而不是整個資料表。可以使用 WHERE 條件來指定需要鎖定的行。
SELECT * FROM table_name WHERE condition FOR UPDATE;

-- 表鎖定（Table Locking）
-- 鎖定整個資料表，適用於需要完全獨佔資料表的情況。
LOCK TABLE table_name IN EXCLUSIVE MODE;
```

### 解除鎖定

```sql
-- COMMIT
-- 當一個事務完成後，可以使用 COMMIT 來提交事務並釋放所持有的鎖定。
-- 這將確保事務所做的修改對其他事務可見。
COMMIT;

-- ROLLBACK
-- 如果需要取消一個事務的修改並解鎖資源，可以使用 ROLLBACK。
-- 這將還原事務開始之前的狀態。
ROLLBACK;

-- UNLOCK TABLES
-- 如果使用了表級別的鎖定（如表鎖定），可以使用 UNLOCK TABLES 來釋放資料表的鎖定。
UNLOCK TABLES;

-- RELEASE SAVEPOINT
-- 如果在事務中使用了 SAVEPOINT 來劃分子事務，可以使用 RELEASE SAVEPOINT 來釋放子事務的鎖定。
RELEASE SAVEPOINT savepoint_name;
```

### 模糊匹配(可能導致性能問題，特別是當數據量龐大時)

```sql
-- 用於從名為 table 的表中選擇所有列（*），並按照 col 列的值進行降序排序，同時只檢索 col 列中包含字串 '1111' 的行。
SELECT * FROM `table` WHERE `col` LIKE '%1111%' ORDER BY `col` DESC

-- SELECT *: 選擇表中的所有列。
-- FROM table: 從名為 table 的表中進行查詢。
-- WHERE col LIKE '%1111%': 條件篩選，只返回 col 列中包含字串 '1111' 的行。LIKE 是用於模糊匹配的運算符，% 表示匹配任意字符（包括零個字符）。
-- ORDER BY col DESC: 按 col 列的值進行降序排序。
```

### 重複出現統計 COUNT

```sql
-- 統計某個列中值的重複出現次數，可以使用 SQL 的 COUNT 函數
SELECT col, COUNT(col) AS count
FROM `table`
GROUP BY col
ORDER BY count DESC;
```

### 合併 JOIN

```sql
-- 創建兩個表格 table1 和 table2 用於示例
CREATE TABLE table1 (
    id INT PRIMARY KEY,
    common_key INT,
    value1 VARCHAR(50)
);

CREATE TABLE table2 (
    id INT PRIMARY KEY,
    common_key INT,
    value2 VARCHAR(50)
);

-- 插入數據到 table1
INSERT INTO table1 (id, common_key, value1) VALUES
    (1, 1, 'A'),
    (2, 2, 'B'),
    (3, 3, 'C');

-- 插入數據到 table2
INSERT INTO table2 (id, common_key, value2) VALUES
    (1, 2, 'X'),
    (2, 3, 'Y'),
    (3, 4, 'Z');

-- 顯示原始數據
SELECT * FROM table1;
SELECT * FROM table2;

-- INNER JOIN 查詢，返回共同匹配的行
SELECT *
FROM table1
INNER JOIN table2 ON table1.common_key = table2.common_key;

-- LEFT JOIN 查詢，返回 table1 的所有行以及與之匹配的 table2 行
SELECT *
FROM table1
LEFT JOIN table2 ON table1.common_key = table2.common_key;

-- RIGHT JOIN 查詢，返回 table2 的所有行以及與之匹配的 table1 行
SELECT *
FROM table1
RIGHT JOIN table2 ON table1.common_key = table2.common_key;
```
