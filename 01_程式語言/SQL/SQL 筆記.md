# SQL 筆記

## 目錄

- [SQL 筆記](#sql-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)
	- [範例](#範例)

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