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
		- [教學相關](#教學相關)
		- [SQLAlchemy 相關](#sqlalchemy-相關)
- [用法](#用法)
	- [使用 SQLAlchemy 操作 SQLite3 資料庫](#使用-sqlalchemy-操作-sqlite3-資料庫)

## 參考資料

[sqlite3 官方文檔](https://docs.python.org/zh-tw/3/library/sqlite3.html)

[SQLite 官網](https://www.sqlite.org/index.html)

[SQL Tutorial](https://www.w3schools.com/sql/)

[PEP 249 – Python Database API Specification v2.0](https://peps.python.org/pep-0249/)

### 教學相關

[Python3+SQLAlchemy+Sqlite3实现ORM教程](https://www.cnblogs.com/lsdb/p/9835894.html)

### SQLAlchemy 相關

[SQLite](https://docs.sqlalchemy.org/en/20/dialects/sqlite.html)

[SQLAlchemy 2.0 Documentation - Using INSERT Statements](https://docs.sqlalchemy.org/en/20/tutorial/data_insert.html)

[SQLAlchemy 2.0 Documentation - Using SELECT Statements](https://docs.sqlalchemy.org/en/20/tutorial/data_select.html)

[SQLAlchemy 2.0 Documentation - Using UPDATE and DELETE Statements¶](https://docs.sqlalchemy.org/en/20/tutorial/data_update.html)

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

```Python
import sqlite3

# 連接到 SQLite 資料庫（如果不存在，會自動建立）
conn = sqlite3.connect('example.db')

# 創建一個游標物件，用於執行 SQL 查詢
cursor = conn.cursor()

# 創建一個表格
cursor.execute('''
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY,
        name TEXT NOT NULL,
        age INTEGER
    )
''')

# 插入一些資料
cursor.execute("INSERT INTO users (name, age) VALUES (?, ?)", ('Alice', 25))
cursor.execute("INSERT INTO users (name, age) VALUES (?, ?)", ('Bob', 30))

# 提交更改
conn.commit()

# 查詢資料
cursor.execute("SELECT * FROM users")
rows = cursor.fetchall()

# 印出結果
for row in rows:
    print(row)

# 關閉連接
conn.close()
```

```Python
import sqlite3

class SQLiteDatabase:
    def __init__(self, db_path):
        self.db_path = db_path
        self.conn = None
        self.cursor = None

    def connect(self):
        self.conn = sqlite3.connect(self.db_path)
        self.cursor = self.conn.cursor()

    def disconnect(self):
        if self.conn:
            self.conn.close()

    def execute_query(self, query, params=None):
        if not self.conn:
            self.connect()

        if params:
            self.cursor.execute(query, params)
        else:
            self.cursor.execute(query)

    def fetch_all(self):
        return self.cursor.fetchall()

    def commit(self):
        if self.conn:
            self.conn.commit()

    def close(self):
        if self.conn:
            self.conn.close()

# 使用範例
db_path = "example.db"
db = SQLiteDatabase(db_path)

# 創建表格
create_table_query = """
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT,
    email TEXT
);
"""
db.execute_query(create_table_query)
db.commit()

# 插入資料
insert_data_query = "INSERT INTO users (username, email) VALUES (?, ?);"
user_data = ("john_doe", "john@example.com")
db.execute_query(insert_data_query, user_data)
db.commit()

# 查詢資料
select_data_query = "SELECT * FROM users;"
db.execute_query(select_data_query)
results = db.fetch_all()

for result in results:
    print(result)

# 關閉資料庫連接
db.close()
```

## 使用 SQLAlchemy 操作 SQLite3 資料庫

```bash
# 使用 SQLAlchemy 操作 SQLite3 資料庫時，你需要先安裝 SQLAlchemy
pip install sqlalchemy
```

```Python
# 使用 SQLAlchemy 連接 SQLite3 資料庫
from sqlalchemy import create_engine, Column, Integer, String, MetaData, Table

# 連接 SQLite3 資料庫
engine = create_engine('sqlite:///example.db', echo=True)  # echo=True 可以顯示 SQL 語句

# 獲取元數據對象
metadata = MetaData()

# 定義表結構
users = Table('users', metadata,
              Column('id', Integer, primary_key=True),
              Column('name', String),
              Column('age', Integer)
              )

# 創建表
metadata.create_all(engine)
```

```Python
# 插入資料
from sqlalchemy import insert

# 獲取資料庫連接
conn = engine.connect()

# 插入資料
conn.execute(insert(users).values(name='John Doe', age=30))
conn.execute(insert(users).values(name='Jane Doe', age=25))
```

```Python
# 查詢資料
from sqlalchemy import select

# 查詢資料
result = conn.execute(select(users))

# 顯示查詢結果
for row in result:
    print(row)
```

```Python
from sqlalchemy import create_engine, Table, Column, Integer, String, MetaData

class SQLiteDatabase:
    def __init__(self, db_path):
        self.db_path = db_path
        self.engine = None
        self.metadata = MetaData()

    def connect(self):
        self.engine = create_engine(f'sqlite:///{self.db_path}')
        self.metadata.create_all(self.engine)

    def disconnect(self):
        if self.engine:
            self.engine.dispose()

    def insert(self, table_name, values):
        table = Table(table_name, self.metadata, autoload_with=self.engine)
        ins = table.insert().values(values)
        with self.engine.connect() as connection:
            connection.execute(ins)

    def select(self, table_name, condition=None):
        table = Table(table_name, self.metadata, autoload_with=self.engine)
        sel = table.select().where(condition) if condition else table.select()
        with self.engine.connect() as connection:
            result = connection.execute(sel)
            return result.fetchall()

    def delete(self, table_name, condition):
        table = Table(table_name, self.metadata, autoload_with=self.engine)
        delete_stmt = table.delete().where(condition)
        with self.engine.connect() as connection:
            connection.execute(delete_stmt)

    def update(self, table_name, values, condition):
        table = Table(table_name, self.metadata, autoload_with=self.engine)
        update_stmt = table.update().values(values).where(condition)
        with self.engine.connect() as connection:
            connection.execute(update_stmt)

# 示例用法
db = SQLiteDatabase("example.db")

# 插入數據
db.insert('users', {'name': 'John Doe', 'age': 25})

# 查詢數據
result = db.select('users')
print(result)

# 更新數據
db.update('users', {'age': 26}, condition='name = "John Doe"')

# 再次查詢數據
result = db.select('users')
print(result)

# 刪除數據
db.delete('users', condition='name = "John Doe"')

# 最終查詢
result = db.select('users')
print(result)

# 關閉數據庫連接
db.disconnect()
```

```Python
# 動態生成條件
from sqlalchemy import and_, or_

# 構建 AND 條件
condition_and = and_(table.c.column1 == 'value1', table.c.column2 == 'value2')

# 構建 OR 條件
condition_or = or_(table.c.column1 == 'value1', table.c.column2 == 'value2')

conditions_dict = {'column1': 'value1', 'column2': 'value2'}

# 動態生成 AND 條件
condition_and_dynamic = and_(*[table.c[column] == value for column, value in conditions_dict.items()])
```

```Python
from sqlalchemy import create_engine, Table, MetaData, Column, Integer, and_, or_

def build_condition(table, conditions_dict):
    conditions = []

    for column, value in conditions_dict.items():
        if isinstance(value, dict):
            # 如果值是字典，遞迴處理
            sub_condition = build_condition(table, value)
            conditions.append(sub_condition)
        elif '>' in value:
            condition = table.c[column] > int(value.split('>')[1])
        elif '<' in value:
            condition = table.c[column] < int(value.split('<')[1])
        # 可以添加其他比較條件的判斷

        conditions.append(condition)

    # 檢查是否包含 AND 或 OR 條件
    if 'AND' in conditions_dict:
        conditions.append(and_(*conditions_dict['AND']))
    if 'OR' in conditions_dict:
        conditions.append(or_(*conditions_dict['OR']))

    return and_(*conditions)

# 假設有一個名為 'mytable' 的表格，包含 'column1' 和 'column2' 兩個欄位
engine = create_engine('sqlite:///:memory:')
metadata = MetaData()
mytable = Table('mytable', metadata,
    Column('column1', Integer),
    Column('column2', Integer)
)
metadata.create_all(engine)

# 使用包含 AND 和 OR 條件的字典動態生成條件
conditions_dict = {
    'column1': '> 10',
    'column2': {'< 5', 'AND': ['> 2', '< 8']},
    'OR': ['column1 > 15', 'column2 < 3']
}
condition = build_condition(mytable, conditions_dict)

# 列印生成的條件
print(condition)
```