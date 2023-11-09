# Python 模組 MongoEngine(mongoDB資料庫)

```
```

## 目錄

- [Python 模組 MongoEngine(mongoDB資料庫)](#python-模組-mongoenginemongodb資料庫)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [mongoengine 與 flask\_mongoengine 差別](#mongoengine-與-flask_mongoengine-差別)
  - [如何在 MongoEngine 中使用 EmbeddedDocumentField](#如何在-mongoengine-中使用-embeddeddocumentfield)

## 參考資料

[mongoengine pypi](https://pypi.org/project/mongoengine/)

[MongoEngine 文檔](https://docs.mongoengine.org/guide/querying.html)

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
