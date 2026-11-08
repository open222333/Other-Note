# Python 模組 SQLAlchemy(SQL ORM工具)

```
一種程式設計技術
用於實現物件導向程式語言裡不同類型系統的資料之間的轉換。
從效果上說，它其實是建立了一個可在程式語言裡使用的「虛擬物件資料庫」。
```

## 目錄

- [Python 模組 SQLAlchemy(SQL ORM工具)](#python-模組-sqlalchemysql-orm工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [配置文檔](#配置文檔)
  - [連線字串（Database URL）與密碼 URL 編碼](#連線字串database-url與密碼-url-編碼)
  - [把密碼填入 env 檔（佔位字串替換）](#把密碼填入-env-檔佔位字串替換)
- [用法](#用法)
  - [動態生成條件](#動態生成條件)
- [狀況處理](#狀況處理)
  - [ImportError: No module named MySQLdb](#importerror-no-module-named-mysqldb)
  - [密碼含特殊字元導致連線字串解析錯誤](#密碼含特殊字元導致連線字串解析錯誤)

## 參考資料

[SQLAlchemy pypi](https://pypi.org/project/SQLAlchemy/)

[SQLAlchemy 文檔](https://docs.sqlalchemy.org/en/14/contents.html)

[Column and Data Types (資料欄位與型態)](https://docs.sqlalchemy.org/en/14/core/type_basics.html#generic-types)

[資料儲存 - SqlAlchemy](https://ithelp.ithome.com.tw/articles/10280600)

[各種資料庫模組引擎配置 Engine Configuration](https://docs.sqlalchemy.org/en/14/core/engines.html)

[Escaping Special Characters such as @ signs in Passwords](https://docs.sqlalchemy.org/en/20/core/engines.html#escaping-special-characters-such-as-signs-in-passwords)

[Docker Compose - env file 語法（引號與變數插值）](https://docs.docker.com/compose/how-tos/environment-variables/variable-interpolation/#env-file-syntax)

[物件關聯對映 Object Relational Mapping](https://zh.wikipedia.org/wiki/%E5%AF%B9%E8%B1%A1%E5%85%B3%E7%B3%BB%E6%98%A0%E5%B0%84)

[Python dotenv 筆記](../Python_模組_dotenv(環境變數).md)

# 指令

```bash
# 安裝
pip install SQLAlchemy
```

# 配置文檔

## 連線字串（Database URL）與密碼 URL 編碼

格式：

```
dialect+driver://username:password@host:port/database
mysql+pymysql://app_user:<URL 編碼後的密碼>@db-host:3306/app_db
```

- 連線字串裡的密碼要先做 **URL 編碼**；密碼含 `@ : / # ? % $` 等字元時，不編碼會被誤判成分隔符號。
- 同一組密碼若另外以獨立變數提供（例如 `MYSQL_PASSWORD`），那裡填**原始密碼**，不要編碼。

取得編碼結果（輸入時畫面不顯示，也不會留在 shell history）：

```bash
python3 -c 'import getpass,urllib.parse as u;print(u.quote(getpass.getpass(),safe=""))'
```

- `safe=""`：連 `/` 也一併編碼（預設 `safe="/"` 不編 `/`，放進 URI 會出錯）。

在程式裡組連線字串時，可以不手動編碼，改用 `URL.create`（SQLAlchemy 1.4+）：

```Python
import os
from sqlalchemy import create_engine
from sqlalchemy.engine import URL

url = URL.create(
    "mysql+pymysql",
    username=os.environ["DB_USER"],
    password=os.environ["DB_PASSWORD"],  # 原始密碼，URL.create 會自行處理
    host=os.environ["DB_HOST"],
    port=3306,
    database=os.environ["DB_NAME"],
)
engine = create_engine(url)
```

## 把密碼填入 env 檔（佔位字串替換）

env 範本裡先用佔位字串，部署時在主機上互動輸入密碼替換，密碼不會出現在畫面、指令或 Git 裡：

```bash
# app.env（範本）
MYSQL_PASSWORD=__DB_PASSWORD__
SQLALCHEMY_DATABASE_URI=mysql+pymysql://app_user:__DB_PASSWORD_URLENC__@db-host:3306/app_db
LOG_SQLALCHEMY_DATABASE_URI=mysql+pymysql://app_user:__DB_PASSWORD_URLENC__@db-host:3306/log_db
```

| 佔位字串 | 填法 |
|---|---|
| `__DB_PASSWORD__` | 原始密碼 |
| `__DB_PASSWORD_URLENC__` | URL 編碼後的密碼（連線字串裡用） |

一次替換全部位置（URI 裡的自動 URL 編碼）：

```bash
python3 -c 'import getpass,urllib.parse as u;p=getpass.getpass("DB password: ");f="env/app.env";s=open(f).read().replace("__DB_PASSWORD_URLENC__",u.quote(p,safe="")).replace("__DB_PASSWORD__",p);open(f,"w").write(s)'
```

確認都已替換（沒有輸出代表完成），並限制檔案權限：

```bash
grep -n "__DB_PASSWORD" env/app.env
chmod 600 env/app.env
```

再啟動服務：

```bash
docker compose up -d --build --no-deps $SERVICE
```

注意：

- 填好密碼的 env 檔不能進版控（加進 `.gitignore`），repo 裡只放含佔位字串的範本。
- Docker Compose 的 `env_file` 會做變數插值：原始密碼含 `$` 時，用單引號包住（`MYSQL_PASSWORD='p@$$w0rd'`）或改寫成 `$$`；URL 編碼後 `$` 變成 `%24`，不受影響。

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

# 狀況處理

## ImportError: No module named MySQLdb

[ImportError: No module named MySQLdb](https://stackoverflow.com/questions/22252397/importerror-no-module-named-mysqldb)

## 密碼含特殊字元導致連線字串解析錯誤

症狀：密碼正確卻連不上，錯誤訊息裡的 host 或 port 不對（例如 `Can't connect to MySQL server on 'xyz@db-host'`、`invalid literal for int()`）。

原因：密碼裡的 `@`、`:`、`/`、`#` 等字元沒有 URL 編碼，被當成連線字串的分隔符號。

解法：密碼改填 URL 編碼後的值（見「連線字串（Database URL）與密碼 URL 編碼」），或在程式裡改用 `URL.create`。
