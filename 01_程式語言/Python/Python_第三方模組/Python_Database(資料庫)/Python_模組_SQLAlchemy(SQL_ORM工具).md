# Python 模組 SQLAlchemy(SQL ORM工具)

```
一種程式設計技術
用於實現物件導向程式語言裡不同類型系統的資料之間的轉換。
從效果上說，它其實是建立了一個可在程式語言裡使用的「虛擬物件資料庫」。
```

## 參考資料

[SQLAlchemy pypi](https://pypi.org/project/SQLAlchemy/)

[SQLAlchemy 文檔](https://docs.sqlalchemy.org/en/14/contents.html)

[Column and Data Types (資料欄位與型態)](https://docs.sqlalchemy.org/en/14/core/type_basics.html#generic-types)

[資料儲存 - SqlAlchemy](https://ithelp.ithome.com.tw/articles/10280600)

[各種資料庫模組引擎配置 Engine Configuration](https://docs.sqlalchemy.org/en/14/core/engines.html)

[物件關聯對映 Object Relational Mapping](https://zh.wikipedia.org/wiki/%E5%AF%B9%E8%B1%A1%E5%85%B3%E7%B3%BB%E6%98%A0%E5%B0%84)


# 指令

```bash
# 安裝
pip install SQLAlchemy
```

# 用法

```Python
```

# 狀況處理

## ImportError: No module named MySQLdb

[ImportError: No module named MySQLdb](https://stackoverflow.com/questions/22252397/importerror-no-module-named-mysqldb)

# 用法

## 動態生成條件

```Python
from sqlalchemy import and_, or_

# 構建 AND 條件
condition_and = and_(table.c.column1 == 'value1', table.c.column2 == 'value2')

# 構建 OR 條件
condition_or = or_(table.c.column1 == 'value1', table.c.column2 == 'value2')


conditions_dict = {'column1': 'value1', 'column2': 'value2'}

# 動態生成 AND 條件
condition_and_dynamic = and_(*[table.c[column] == value for column, value in conditions_dict.items()])
```