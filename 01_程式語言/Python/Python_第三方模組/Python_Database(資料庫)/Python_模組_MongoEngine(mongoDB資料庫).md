# Python 模組 MongoEngine(mongoDB資料庫)

```
```

## 目錄

- [Python 模組 MongoEngine(mongoDB資料庫)](#python-模組-mongoenginemongodb資料庫)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [欄位運算子（欄位\_\_運算子）](#欄位運算子欄位__運算子)
  - [Q 物件：用 AND / OR 組合查詢條件](#q-物件用-and--or-組合查詢條件)
  - [mongoengine 與 flask\_mongoengine 差別](#mongoengine-與-flask_mongoengine-差別)
  - [如何在 MongoEngine 中使用 EmbeddedDocumentField](#如何在-mongoengine-中使用-embeddeddocumentfield)

## 參考資料

[mongoengine pypi](https://pypi.org/project/mongoengine/)

[MongoEngine 文檔](https://docs.mongoengine.org/guide/querying.html)

[MongoEngine 文檔 - Advanced queries（Q 物件）](https://docs.mongoengine.org/guide/querying.html#advanced-queries)

[MongoEngine - Query Operators](https://www.tutorialspoint.com/mongoengine/mongoengine_query_operators.htm)

# 指令

```bash
# 安裝
pip install mongoengine
```

# 用法

```Python
from mongoengine import connect, Document, StringField

# 連接到 MongoDB 數據庫
connect('mydatabase', host='localhost', port=27017)

# 定義模型類別
class User(Document):
    username = StringField(required=True, max_length=50)
    email = StringField(required=True, max_length=50)

# 創建一個新的用戶
new_user = User(username='john_doe', email='john@example.com')
new_user.save()

# 查詢用戶
user = User.objects(username='john_doe').first()

# 更新用戶信息
user.email = 'new_email@example.com'
user.save()

# 刪除用戶
user.delete()
```

## 欄位運算子（欄位__運算子）

MongoEngine 的查詢條件寫成「**欄位名稱 + 雙底線 + 運算子**」，例如 `role__in`：`role` 是 Document 的欄位，`__in` 是運算子，對應 MongoDB 的 `$in`。沒有運算子時就是「等於」。

```Python
User.objects(role__in=['admin', 'editor'])
# → {'role': {'$in': ['admin', 'editor']}}

Q(role__in=['admin', 'editor'])       # 放在 Q 裡寫法一樣
```

常見運算子（右欄是實際產生的 MongoDB 查詢）：

| 寫法 | MongoDB | 意思 |
|---|---|---|
| `role='admin'` | `{'role': 'admin'}` | 等於 |
| `role__ne='admin'` | `{'role': {'$ne': 'admin'}}` | 不等於 |
| `role__in=[...]` | `$in` | 在清單內 |
| `role__nin=[...]` | `$nin` | 不在清單內 |
| `age__gt=18` | `$gt` | 大於（`__gte` 大於等於、`__lt` 小於、`__lte` 小於等於） |
| `nickname__exists=False` | `$exists` | 欄位是否存在 |
| `username__contains='jo'` | 正規表示式 `/jo/` | 包含（分大小寫） |
| `username__icontains='jo'` | 正規表示式 `/jo/i` | 包含（不分大小寫） |
| `username__startswith='jo'` | 正規表示式 `/^jo/` | 開頭是（`__endswith` 結尾是；加 `i` 前綴不分大小寫） |
| `username__iexact='john'` | 正規表示式 `/^john$/i` | 完全相等但不分大小寫 |
| `tags__all=['a', 'b']` | `$all` | 陣列欄位同時包含這些值 |
| `tags__size=2` | `$size` | 陣列長度等於 |
| `age__not__gt=30` | `$not` | 反向（`__not` 可以接在其他運算子前） |

字串類運算子（`contains`、`icontains`、`startswith` 等）底層是正規表示式，大量資料時無法有效利用索引（開頭比對 `startswith` 例外），要注意效能。

**與 pymongo 寫法對照**：pymongo 直接寫 MongoDB 的查詢字典，MongoEngine 用 `欄位__運算子` 的關鍵字參數，兩者效果相同。

```Python
# pymongo
db.user.find({'role': {'$in': roles}, 'age': {'$gte': 18}})

# MongoEngine
User.objects(role__in=roles, age__gte=18)
```

注意：pymongo 用的是**資料庫裡實際的欄位名稱**；MongoEngine 用的是 **Document 類別的屬性名稱**。若欄位有設定 `db_field`（例如 `code = StringField(db_field='product_code')`），兩邊寫的名稱會不同（pymongo 寫 `product_code`、MongoEngine 寫 `code`），對照舊程式時要先確認對應關係。

## Q 物件：用 AND / OR 組合查詢條件

`Q` 是 MongoEngine 的查詢條件物件，把多個條件用 `&`（AND）、`|`（OR）組起來。一般的關鍵字參數查詢（`User.objects(a=1, b=2)`）只能做 AND，需要 OR 或巢狀條件時就用 `Q`。

```Python
from datetime import datetime, timedelta
from mongoengine import connect, Document, StringField, IntField, ListField, DateTimeField
from mongoengine.queryset.visitor import Q

connect('mydatabase', host='localhost', port=27017)

class User(Document):
    username = StringField(required=True)
    role = StringField()            # 'admin' / 'editor' / 'member'
    status = StringField()          # 'active' / 'banned'
    age = IntField()
    tags = ListField(StringField())
    created_at = DateTimeField()

# AND：年滿 18 且帳號啟用
users = User.objects(Q(age__gte=18) & Q(status='active'))

# OR：管理員或編輯
users = User.objects(Q(role='admin') | Q(role='editor'))

# 巢狀：(管理員 或 編輯) 且 帳號啟用
users = User.objects((Q(role='admin') | Q(role='editor')) & Q(status='active'))

# Q 與關鍵字參數混用：關鍵字參數之間、與 Q 之間都是 AND
users = User.objects(Q(role='admin') | Q(tags='vip'), status='active')

# 也可以放進 filter()，可以一路串接
users = User.objects.filter(Q(username__icontains='john') | Q(tags__in=['vip', 'beta']))
users = users.filter(created_at__gte=datetime.now() - timedelta(days=30))
```

動態組條件（條件數量不固定，例如搜尋關鍵字）：

```Python
import operator
from functools import reduce

keywords = ['john', 'mary', 'tom']

# 寫法一：reduce 把多個 Q 用 OR 串起來
query = reduce(operator.or_, [Q(username__icontains=k) for k in keywords])
users = User.objects(query)

# 寫法二：從空的 Q() 開始累加（空 Q 會被忽略）
query = Q()
if keywords:
    query &= reduce(operator.or_, [Q(username__icontains=k) for k in keywords])
min_age = 18
if min_age is not None:
    query &= Q(age__gte=min_age)
users = User.objects(query)
```

注意事項：

- **要用 `&`、`|`，不能用 `and`、`or`**：`Q(a=1) and Q(b=2)` 是 Python 的布林運算，只會回傳其中一個 Q，條件會被默默丟掉，查詢不會報錯但結果是錯的。
- **運算子優先順序**：`&` 比 `|` 先算，`Q(a) | Q(b) & Q(c)` 等於 `Q(a) | (Q(b) & Q(c))`，混用時一律加括號。
- **否定條件**：Q 沒有 `~` 反向用法，改用欄位運算子，例如 `Q(status__ne='banned')`、`Q(role__nin=['admin'])`、`Q(age__not__gt=30)`。
- 欄位運算子（`__gte`、`__icontains`、`__in` 等）在 Q 裡和一般查詢寫法一樣，見上方「欄位運算子」。
- 匯入路徑：`from mongoengine.queryset.visitor import Q` 與 `from mongoengine import Q` 是同一個物件，兩種都可以。
- 除錯時可以看產生出來的 MongoDB 查詢（`_query` 是內部屬性，只拿來除錯，不要寫進正式程式）：

```Python
print(User.objects((Q(role='admin') | Q(role='editor')) & Q(status='active'))._query)
# {'$and': [{'$or': [{'role': 'admin'}, {'role': 'editor'}]}, {'status': 'active'}]}
```

## mongoengine 與 flask_mongoengine 差別

```Python
from mongoengine import Document, StringField
from mongoengine import connect

# 設定 MongoDB 連接
connect(
    db='mydatabase',        # 數據庫名稱
    host='localhost',        # 主機地址
    port=27017,              # 端口號
    username='your_username',  # 如果需要驗證，添加用戶名
    password='your_password',  # 如果需要驗證，添加密碼
    authentication_source='admin'  # 如果需要驗證，指定驗證數據庫
)

class User(Document):
    username = StringField()
    email = StringField()
```

```Python
from flask import Flask
from flask_mongoengine import MongoEngine

app = Flask(__name__)
app.config['MONGODB_SETTINGS'] = {
    'db': 'mydatabase',
    'host': 'localhost',
    'port': 27017,
    'username': 'your_username',  # 如果需要驗證，添加用戶名
    'password': 'your_password',  # 如果需要驗證，添加密碼
    'authentication_source': 'admin'  # 如果需要驗證，指定驗證數據庫
}

db = MongoEngine(app)

class User(db.Document):
    username = db.StringField()
    email = db.StringField()
```

## 如何在 MongoEngine 中使用 EmbeddedDocumentField

```Python
# https://docs.mongoengine.org/guide/defining-documents.html#embedded-documents
from mongoengine import Document, EmbeddedDocument

class Address(EmbeddedDocument):
    street = StringField(required=True)
    city = StringField(required=True)
    state = StringField(required=True)

class Person(Document):
    name = StringField(required=True)
    address = EmbeddedDocumentField(Address, required=True)

# 創建一個人物對象
person = Person(name="John Doe", address={"street": "123 Main St", "city": "City", "state": "State"})

# 保存到數據庫
person.save()
```
