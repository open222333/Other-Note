# Python 模組 pymysql(MySQL資料庫)

```
```

## 目錄

- [Python 模組 pymysql(MySQL資料庫)](#python-模組-pymysqlmysql資料庫)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [語法相關](#語法相關)
- [指令](#指令)
- [用法](#用法)
  - [基本2](#基本2)
  - [基本2](#基本2-1)
  - [與flask current\_app](#與flask-current_app)

## 參考資料

[pymysql pypi](https://pypi.org/project/pymysql/)

[Python MySQL Order By](https://www.w3schools.com/python/python_mysql_orderby.asp)

[Python3 MySQL 數據庫連接 - PyMySQL 驅動](https://www.runoob.com/python3/python3-mysql.html)

### 語法相關

[SQL語法手冊](http://tw.gitbook.net/sql/index.html)

[SQL語法手冊](https://www.1keydata.com/tw/sql/sql.html)

[ROLLBACK 回滾](https://zh.wikipedia.org/zh-tw/%E5%9B%9E%E6%BB%BE)

# 指令

```bash
# 安裝
pip install pymysql
```

# 用法

## 基本2

```Python
import pymysql

# 連接到 MySQL 數據庫
connection = pymysql.connect(
    host='localhost',
    user='username',
    password='password',
    database='dbname'
)

try:
    # 創建游標
    with connection.cursor() as cursor:
        # 執行查詢
        sql = "SELECT * FROM mytable"
        cursor.execute(sql)

        # 獲取結果
        results = cursor.fetchall()

        # 輸出結果
        for row in results:
            print(row)
finally:
    # 關閉連接
    connection.close()
```

## 基本2

```Python
import pymysql

# 連接資料庫
conn = pymysql.connect(
	host="localhost",
	port=3306,
	user="root",
	passwd="",
	db="mysql"
)

# cursor() 方法創建一個游標對象 cursor
cur = conn.cursor()

# 使用 execute() 方法執行 SQL
cur.execute("SELECT Host,User FROM user")

# 表的細節
print(cur.description)
for row in cur:
    print(row)

# 使用 execute() 方法執行 SQL INSERT
sql = """INSERT INTO EMPLOYEE(FIRST_NAME,
         LAST_NAME, AGE, SEX, INCOME)
         VALUES ('Mac', 'Mohan', 20, 'M', 2000)"""

try:
	cur.execute(sql)
	# 提交到數據庫執行
	conn.commit()
except:
	# 如果發生錯誤則回滾
	# 回滾（英語：rollback）是資料庫技術中的操作，放棄修改，使資料庫狀態恢復到此前的某個時刻。
	conn.rollback()

# 使用 fetchone() 方法獲取單條數據
data = cursor.fetchone()

# 使用 fetchall() 方法獲取所有數據
cursor.fetchall()

# 使用 fetchmany() 方法獲取指定數量數據
cursor.fetchmany()
print ("Database version : %s " % data)


cur.close()
conn.close()
```

## 與flask current_app

```Python
import pymysql.cursors
from flask import _app_ctx_stack, current_app


class FlaskMySQL(object):

    def __init__(self, app=None):
        self.app = app
        if app is not None:
            self.init_app(app)

    def init_app(self, app):
        app.config.setdefault('MYSQL_HOST', 'localhost')
        app.config.setdefault('MYSQL_USER', None)
        app.config.setdefault('MYSQL_PASSWORD', None)
        app.config.setdefault('MYSQL_DB', None)
        app.config.setdefault('MYSQL_PORT', 3306)
        app.config.setdefault('MYSQL_UNIX_SOCKET', None)
        app.config.setdefault('MYSQL_CONNECT_TIMEOUT', 10)
        app.config.setdefault('MYSQL_READ_DEFAULT_FILE', None)
        app.config.setdefault('MYSQL_USE_UNICODE', True)
        app.config.setdefault('MYSQL_CHARSET', 'utf8')
        app.config.setdefault('MYSQL_SQL_MODE', None)
        app.config.setdefault('MYSQL_CURSORCLASS', None)

        if hasattr(app, 'teardown_appcontext'):
            app.teardown_appcontext(self.teardown)

    @property
    def connect(self):
        kwargs = {}

        if current_app.config['MYSQL_HOST']:
            kwargs['host'] = current_app.config['MYSQL_HOST']

        if current_app.config['MYSQL_USER']:
            kwargs['user'] = current_app.config['MYSQL_USER']

        if current_app.config['MYSQL_PASSWORD']:
            kwargs['passwd'] = current_app.config['MYSQL_PASSWORD']

        if current_app.config['MYSQL_DB']:
            kwargs['db'] = current_app.config['MYSQL_DB']

        if current_app.config['MYSQL_PORT']:
            kwargs['port'] = current_app.config['MYSQL_PORT']

        if current_app.config['MYSQL_UNIX_SOCKET']:
            kwargs['unix_socket'] = current_app.config['MYSQL_UNIX_SOCKET']

        if current_app.config['MYSQL_CONNECT_TIMEOUT']:
            kwargs['connect_timeout'] = \
                current_app.config['MYSQL_CONNECT_TIMEOUT']

        if current_app.config['MYSQL_READ_DEFAULT_FILE']:
            kwargs['read_default_file'] = \
                current_app.config['MYSQL_READ_DEFAULT_FILE']

        if current_app.config['MYSQL_USE_UNICODE']:
            kwargs['use_unicode'] = current_app.config['MYSQL_USE_UNICODE']

        if current_app.config['MYSQL_CHARSET']:
            kwargs['charset'] = current_app.config['MYSQL_CHARSET']

        if current_app.config['MYSQL_SQL_MODE']:
            kwargs['sql_mode'] = current_app.config['MYSQL_SQL_MODE']

        if current_app.config['MYSQL_CURSORCLASS']:
            kwargs['cursorclass'] = getattr(pymysql.cursors, current_app.config['MYSQL_CURSORCLASS'])

        return pymysql.connect(**kwargs)

    @property
    def connection(self):
        """Attempts to connect to the MySQL server.

        :return: Bound MySQL connection object if successful or ``None`` if
            unsuccessful.
        """

        ctx = _app_ctx_stack.top
        if ctx is not None:
            if not hasattr(ctx, 'pymysql_db'):
                ctx.pymysql_db = self.connect
            return ctx.pymysql_db

    def teardown(self, exception):
        ctx = _app_ctx_stack.top
        if hasattr(ctx, 'pymysql_db'):
            ctx.pymysql_db.close()
```
