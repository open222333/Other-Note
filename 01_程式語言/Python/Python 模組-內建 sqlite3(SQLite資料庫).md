# Python 模組-內建 sqlite3(SQLite資料庫)

```
SQLite 是一個C語言庫，它可以提供一種輕量級的基於磁盤的數據庫，這種數據庫不需要獨立的服務器進程，也允許需要使用一種非標準的SQL 查詢語言來訪問它。
一些應用程序可以使用SQLite 作為內部數據存儲。
可以用它來創建一個應用程序原型，然後再遷移到更大的數據庫，比如PostgreSQL 或Oracle。

SQLite資料類型：
    NULL：空值
    INTEGER：整數
    REAL：浮點數
    TEXT：字串
    BLOB：圖片、歌
```

## 目錄

- [Python 模組-內建 sqlite3(SQLite資料庫)](#python-模組-內建-sqlite3sqlite資料庫)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[sqlite3 官方文檔](https://docs.python.org/zh-tw/3/library/sqlite3.html)

# 用法

```Python
conn = sqlite3.connect("資料庫名稱")
conn.close() 結束連線
# 方法 connect()：建立資料庫連線。傳回connect物件。
#     connect物件方法：
#     方法 close()：資料庫連線操作結束。
#     方法 commit()：更新資料庫內容。
#     方法 cursor()：建立cursor物件，可享成一個游標在資料庫中移動，然後執行execute()方法。
#     方法 execute()：執行SQL資料庫指令。建立、新增、消除、修改、擷取資料庫的紀錄(record)。
#     方法 fetchall()：將結果轉成串列，元素是元組。

# SQL使用
# 新增
insert into 表單 values(??????)
    # Larger example
for t in [('2006-03-28', 'BUY', 'IBM', 1000, 45.00),
			('2006-04-05', 'BUY', 'MSOFT', 1000, 72.00),
			('2006-04-06', 'SELL', 'IBM', 500, 53.00),
			]:
c.execute('insert into stocks values (?,?,?,?,?)', t)
# 關鍵字 SELECT：查詢資料。
#     語法：SELECT 欄位(*為全部) from 表單 where 條件
# 關鍵字 UPDATA：更新。
#     語法：UPDATE 表單 set 欄位 新內容 where 標明那一筆紀錄
#     需用commit()更新資料庫
# 關鍵字 DELETE：更新。
#     語法：DELETE from 表單 where 標明那一筆紀錄
#     需用commit()更新資料庫
```
