# Python 模組 mysql-connector-python(MySQL官方提供的客戶端模組)

```
mysql-connector-python 是 MySQL 官方提供的 Python 客戶端模組，用於在 Python 程序中與 MySQL 數據庫進行交互。它提供了一個方便的界面，可以讓您執行 SQL 查詢、插入、更新、刪除數據，以及執行存儲過程等操作。以下是一些 mysql-connector-python 的主要特點和說明：

官方支持： mysql-connector-python 是 MySQL 官方提供的客戶端模組，因此您可以確保它與 MySQL 數據庫的相容性和穩定性。

安裝： 您可以使用 pip 命令輕鬆安裝 mysql-connector-python 模組，例如：pip install mysql-connector-python。

連接數據庫： 通過提供主機名、用戶名、密碼和數據庫名等信息，您可以創建一個數據庫連接對象，以便與 MySQL 數據庫進行交互。

執行 SQL 查詢： 使用連接對象的游標（cursor）可以執行 SQL 查詢，並從結果集中獲取數據。

參數化查詢： mysql-connector-python 支持參數化查詢，這有助於防止 SQL 注入攻擊。

事務管理： 您可以使用 commit 和 rollback 方法來管理事務，確保數據的完整性和一致性。

錯誤處理： mysql-connector-python 提供了處理數據庫錯誤的方式，讓您能夠捕獲並處理各種錯誤情況。

數據型別轉換： mysql-connector-python 自動處理 Python 數據類型和 MySQL 數據類型之間的轉換。

支持連接池： 您可以使用連接池來管理和重複使用數據庫連接，以提高性能和效率。

支持高級功能： mysql-connector-python 還提供了高級功能，如加密、SSL 連接、數據壓縮等。

總之，mysql-connector-python 是一個功能豐富且易於使用的模組，使得在 Python 中操作 MySQL 數據庫變得非常方便。通過這個模組，您可以快速開發 Python 應用程序，並與 MySQL 數據庫進行交互。
```

## 目錄

- [Python 模組 mysql-connector-python(MySQL官方提供的客戶端模組)](#python-模組-mysql-connector-pythonmysql官方提供的客戶端模組)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[mysql-connector-python pypi](https://pypi.org/project/mysql-connector-python/)

[MySQL Connector/Python Developer Guide - MySQL 連接器/Python 開發人員指南](https://dev.mysql.com/doc/connector-python/en/)

# 指令

```bash
# 安裝
pip install mysql-connector-python
```

# 用法

```Python
import mysql.connector

# 連接到 MySQL 數據庫
connection = mysql.connector.connect(
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
