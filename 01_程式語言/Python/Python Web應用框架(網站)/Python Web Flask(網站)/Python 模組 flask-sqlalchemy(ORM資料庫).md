# Python 模組 flask-sqlalchemy(ORM資料庫)

```
使用原生 SQL，也同時支援 ORM 框架來操作資料庫

可支援市面上常用的資料庫 sqlite、Mysql、PostgreSQL、MSSql、Oracle
可以使用原生 SQL下指令，也同時支援 ORM 框架來操作資料庫，可以隨時切換很方便。
```

## 目錄

- [Python 模組 flask-sqlalchemy(ORM資料庫)](#python-模組-flask-sqlalchemyorm資料庫)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [配置相關](#配置相關)
- [指令](#指令)
- [用法](#用法)
	- [連線](#連線)
		- [方法一 ：使用 sqlite 資料庫連線](#方法一-使用-sqlite-資料庫連線)
		- [方法二 ：使用 MySQL 資料庫連線](#方法二-使用-mysql-資料庫連線)
		- [方法三 ：使用 PostgreSQL 資料庫連線](#方法三-使用-postgresql-資料庫連線)
	- [配置](#配置)
		- [用於 debug 的參數: SQLALCHEMY_RECORD_QUERIES](#用於-debug-的參數-sqlalchemy_record_queries)
	- [ORM](#orm)
		- [ORM 設置](#orm-設置)
		- [ORM 操作](#orm-操作)
	- [一對多模型](#一對多模型)
		- [操作一對多資料庫](#操作一對多資料庫)
	- [多對多模型](#多對多模型)
		- [定義](#定義)
		- [範例](#範例)
			- [實作操作多對多資料庫](#實作操作多對多資料庫)

## 參考資料

[flask-sqlalchemy pypi](https://pypi.org/project/flask-sqlalchemy/)

[flask-sqlalchemy Github](https://github.com/pallets/flask-sqlalchemy)

[flask-sqlalchemy - 官方文件](https://flask-sqlalchemy.palletsprojects.com/en/latest/)

[SQLAlchemy - 官方文件](https://www.sqlalchemy.org/)

[[Flask教學] Flask-SQLAlchemy 資料庫連線&設定入門(一)](https://www.maxlist.xyz/2019/11/10/flask-sqlalchemy-setting/)

[[Flask教學] Flask-SQLAlchemy 參數設置(進階)](https://www.maxlist.xyz/2020/10/06/flask-sqlalchemy-parameter/)

[[Flask教學] Flask-SQLAlchemy 資料庫操作-ORM篇(二)](https://www.maxlist.xyz/2019/10/30/flask-sqlalchemy/)

[[Flask教學] Flask-SQLAlchemy -ORM 一對多關聯篇 (三)](https://www.maxlist.xyz/2019/11/24/flask-sqlalchemy-orm/)

[[Flask教學] Flask-SQLAlchemy -ORM 多對多關聯篇 (四)](https://www.maxlist.xyz/2019/11/24/flask-sqlalchemy-orm2/)

[[Flask教學] Flask-SQLAlchemy 資料庫操作-SQL指令篇(五)](https://www.maxlist.xyz/2019/11/09/sqlalchemy-sql/)

### 配置相關

[Connection URL Format - 連接 URL 格式](https://flask-sqlalchemy.palletsprojects.com/en/latest/config/#connection-url-format)

# 指令

```bash
# 安裝
pip install flask-sqlalchemy
```

# 用法

## 連線

### 方法一 ：使用 sqlite 資料庫連線

```Python
# main.py

from flask_sqlalchemy import SQLAlchemy
from flask import Flask

db = SQLAlchemy()

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = "sqlite:////tmp/test.db"

db.init_app(app)

@app.route('/create_db')
def index():
    db.create_all()
    return 'ok'

if __name__ == "__main__":
    app.run()
```

### 方法二 ：使用 MySQL 資料庫連線

```bash
# 安裝 pymsql
pip install pymysql
```

```Python
# main.py

from flask_sqlalchemy import SQLAlchemy
from flask import Flask

db = SQLAlchemy()

app = Flask(__name__)

app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SQLALCHEMY_DATABASE_URI'] = "mysql+pymysql://user_name:password@IP:3306/db_name"

db.init_app(app)

@app.route('/')
def index():

    sql_cmd = """
        select *
        from product
        """

    query_data = db.engine.execute(sql_cmd)
    print(query_data)
    return 'ok'


if __name__ == "__main__":
    app.run()
```

### 方法三 ：使用 PostgreSQL 資料庫連線

```bash
# 安裝 psycopg2
pip install psycopg2-binary
```

```Python
# main.py

from flask_sqlalchemy import SQLAlchemy
from flask import Flask

db = SQLAlchemy()

app = Flask(__name__)

app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SQLALCHEMY_DATABASE_URI'] = "postgresql://user_name:password@IP:5432/db_name"

db.init_app(app)

@app.route('/')
def index():

    sql_cmd = """
        select *
        from product
        """

    query_data = db.engine.execute(sql_cmd)
    print(query_data)
    return 'ok'


if __name__ == "__main__":
    app.run()
```

## 配置

```Python
from flask import Flask
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)

# 資料庫連線參數 參考 Connection URL Format - 連接 URL 格式
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:////absolute/path/to/foo.db'
# 對資料庫的任何操作，都會在 console 顯示
app.config['SQLALCHEMY_ECHO'] = True
app.config['SQLALCHEMY_RECORD_QUERIES'] = True

# 以下 v3.0 被移除使用
'''
app.config['SQLALCHEMY_POOL_RECYCLE'] = 90
app.config['SQLALCHEMY_POOL_TIMEOUT'] = 900
app.config['SQLALCHEMY_POOL_SIZE'] = 10
app.config['SQLALCHEMY_MAX_OVERFLOW'] = 5
'''

app.config['SQLALCHEMY_ENGINE_OPTIONS'] = {
    "pool_pre_ping": True,
    "pool_recycle": 300,
    'pool_timeout': 900,
    'pool_size': 10,
    'max_overflow': 5,
}

db = SQLAlchemy(app)

@app.route('/')
def demo():
    return 'Hello Flask'

if __name__ == "__main__":
    app.run(debug=True)
```

### 用於 debug 的參數: SQLALCHEMY_RECORD_QUERIES

```Python
from flask_sqlalchemy import get_debug_queries

'''
當 query.duration 時間大於多少時，記錄在 log 的應用。
'''

@app.after_request
def after_request(reponse):
    for query in get_debug_queries():
        print(query.statement) # SQL 查詢語法
        print(query.parameters) # SQL 查詢語法的參數
        print(query.duration) # 查詢花費的時間
        print(query.context) # 請求的來源上下文
    return reponse


@app.route('/test')
def test():
    p = Person.query.filter_by(username='Max').first()
    return 'ok'
```

## ORM

### ORM 設置

```
primary_key = True (設定為主鍵)
unique = True (設定為不重複值)
nullable = True (允許為空值)
index = True (建立索引值)
default = ‘預設值’
```

```Python
# main.py

from flask import Flask, render_template, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

app = Flask(__name__)

# MySql datebase
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SQLALCHEMY_DATABASE_URI'] = "mysql+pymysql://account:password@IP:3306/db"

db = SQLAlchemy(app)

# 模型( model )定義
class Product(db.Model):
    __tablename__ = 'product'
    pid = db.Column(db.Integer, primary_key=True)
    name = db.Column(
        db.String(30), unique=True, nullable=False)
    price = db.Column(db.Integer, nullable=False)
    img = db.Column(
        db.String(100), unique=True, nullable=False)
    description = db.Column(
        db.String(255), nullable=False)
    state = db.Column(
        db.String(10), nullable=False)
    insert_time = db.Column(db.DateTime, default=datetime.now)
    update_time = db.Column(
        db.DateTime, onupdate=datetime.now, default=datetime.now)


    def __init__(self, name, price, img, description, state):
        self.name = name
        self.price = price
        self.img = img
        self.description = description
        self.state = state


@app.route('/')
def index():
    # Create data
    db.create_all()

    return 'ok'


if __name__ == "__main__":
    app.run(debug=True)
```

### ORM 操作

```Python
# 單筆新增資料：利用 add 的方法即可新增單筆資料
product_max = Product('Max', 8888,'https://picsum.photos/id/1047/1200/600', '', '')
db.session.add(product_max)
db.session.commit()

# 多筆新增資料：可以將資料 append 到 list 中，然後用 db.session.add_all(p) 的方式進行新增
# Add List data
p1 = Product('Isacc', 8888, 'https://picsum.photos/id/1047/1200/600', '', '')
p2 = Product('Dennis', 9999,'https://picsum.photos/id/1049/1200/600', '', '')
p3 = Product('Joey', 7777, 'https://picsum.photos/id/1033/1200/600', '', '')
p4 = Product('Fergus', 6666,'https://picsum.photos/id/1041/1200/600', '', '')
p5 = Product('Max', 5555, 'https://picsum.photos/id/1070/1200/600', '', '')
p6 = Product('Jerry', 4444, 'https://picsum.photos/id/1044/1200/600', '', '')
p = [p1, p2, p3, p4, p5, p6]

db.session.add_all(p)
db.session.commit()

# 讀取資料
# 如果 query 沒有找到資料，會返回 None
# Read data
query = Product.query.filter_by(name='Max').first()
print(query.name)
print(query.price)

# 篩選: filter_by

query = Product.query.filter_by(name='Max').first()

# 可以用動態參數傳入
filters = {'name': 'Max', 'price': 5555}
query = Product.query.filter_by(**filters).first()

# 排序
Product.query.filter_by(name='Max').order_by("value desc")

# 刪除資料
# Delete data
query = Product.query.filter_by(name='Max').first()
db.session.delete(query)
db.session.commit()

# 更新資料
# Updata data
query = Product.query.filter_by(name='Max').first()

# 將 price 修改成 10 塊
query.price = 10
db.session.commit()
```

## 一對多模型

`Product 和 AddToCar 這兩個部分，他們的關係為一對多 (表示為一個 Product 可以被多個 AddToCar 包含)`

```Python
from flask import Flask, render_template, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

app = Flask(__name__)
# MySql datebase
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SQLALCHEMY_DATABASE_URI'] = "mysql+pymysql://account:password@IP:3306/db"

db = SQLAlchemy(app)

class Product(db.Model):
    __tablename__ = 'product'
    pid = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(30), unique=True, nullable=False)
    price = db.Column(db.Integer, nullable=False)
    img = db.Column(db.String(100), unique=True, nullable=False)
    description = db.Column(db.String(255), nullable=True)
    state = db.Column(db.String(10), nullable=False)
    insert_time = db.Column(db.DateTime, default=datetime.now, nullable=False)
    update_time = db.Column(
        db.DateTime, onupdate=datetime.now, default=datetime.now, nullable=False)

    db_product_addtocar = db.relationship("AddToCar", backref="product")

    def __init__(self, name, price, img, description, state):
        self.name = name
        self.price = price
        self.img = img
        self.description = description
        self.state = state

class User(db.Model):
    __tablename__ = 'user'
    uid = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(30), unique=True, nullable=False)
    password = db.Column(db.String(255), nullable=False)
    role = db.Column(db.String(10), nullable=False)
    insert_time = db.Column(db.DateTime, default=datetime.now, nullable=False)
    update_time = db.Column(
        db.DateTime, onupdate=datetime.now, default=datetime.now, nullable=False)

    db_user_atc = db.relationship("AddToCar", backref="user")

    def __init__(self, name, password, role):
        self.name = name
        self.password = password
        self.role = role

class AddToCar(db.Model):
    __tablename__ = 'addtocar'
    id = db.Column(db.Integer, primary_key=True)
    quantity = db.Column(db.Integer, nullable=False)
    state = db.Column(db.String(5), nullable=False)
    insert_time = db.Column(db.DateTime, default=datetime.now, nullable=False)
    update_time = db.Column(
        db.DateTime, onupdate=datetime.now, default=datetime.now, nullable=False)

    uid = db.Column(db.Integer, db.ForeignKey('user.uid'), nullable=False)
    pid = db.Column(db.Integer, db.ForeignKey('product.pid'), nullable=False)

    def __init__(self, uid, pid, quantity, state):
        self.uid = uid
        self.pid = pid
        self.quantity = quantity
        self.state = state
```

### 操作一對多資料庫

```Python
# 新增產品
p1 = Product('Isacc', 8888, 'https://picsum.photos/id/1047/1200/600', '', 'Y')
p2 = Product('Dennis', 9999,'https://picsum.photos/id/1049/1200/600', '', 'Y')
p3 = Product('Joey', 7777, 'https://picsum.photos/id/1033/1200/600', '', 'Y')

# 新增使用者
u1 = User('Max', '123456', 'Admin')

# 新增購物車
atc1 = AddToCar(1, 1, 5, 'Y')

db.session.add_all([atc1])
db.session.commit()

# 查看
query = AddToCar.query.first()
print(query.product.name)
print(query.user.name)

# --> # Isacc
# --> # Max

db.session.add_all([p1, p2, p3, u1])
db.session.commit()
```

## 多對多模型

### 定義

```
Step1. 設定 db.relationship(…) 關係
Step2. 設定 db.Table(‘relations’…)，這設定會自動在資料庫建立新的關聯表
Step3. 在 db.Table(‘relations’…) 內設定 db.ForeignKey(…) 關係
```

### 範例

```
在SQLAlchemy中設置多對多關聯時，並不需要去特別的定義一個 Model 來做中繼，而是透過Table的方法，來設罝MetaData，並記錄兩個 Model 的ForeignKey使用。

“db.Table(‘relations’…)” 的 relations 是待會 SQLAlchemy 會自動生成的關聯表名稱

”db.Column(‘tagid_rt’…)“ 則是設定裡面有的欄位
```

```Python
from flask import Flask, render_template, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

app = Flask(__name__)

# MySql datebase
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SQLALCHEMY_DATABASE_URI'] = "mysql+pymysql://account:password@IP:3306/db"

db = SQLAlchemy(app)

relations = db.Table(
    'relations',
    db.Column('tagid_rt', db.Integer, db.ForeignKey('tag_mul_to_mul.tagid')),
    db.Column('pid_rt', db.Integer, db.ForeignKey('product_mul_to_mul.pid')))


class Product(db.Model):
    __tablename__ = 'product_mul_to_mul'
    pid = db.Column(db.Integer, primary_key=True)
    name = db.Column(
        db.String(30), unique=True, nullable=False)
    price = db.Column(db.Integer, nullable=False)
    img = db.Column(
        db.String(100), unique=True, nullable=False)
    description = db.Column(
        db.String(255), nullable=False)
    state = db.Column(
        db.String(10), nullable=False)
    insert_time = db.Column(db.DateTime, default=datetime.now)
    update_time = db.Column(
        db.DateTime, onupdate=datetime.now, default=datetime.now)

    db_product_tag_rel = db.relationship(
        "Tag", secondary=relations, backref="product")

    def __init__(self, name, price, img, description, state):
        self.name = name
        self.price = price
        self.img = img
        self.description = description
        self.state = state


class Tag(db.Model):
    __tablename__ = 'tag_mul_to_mul'
    tagid = db.Column(db.Integer, primary_key=True)
    tag_type = db.Column(db.String(30))
    insert_time = db.Column(db.DateTime, default=datetime.now)
    update_time = db.Column(
        db.DateTime, onupdate=datetime.now, default=datetime.now)

    def __init__(self, tag_type):
        self.tag_type = tag_type
```

#### 實作操作多對多資料庫

```Python
# 建立表格
db.create_all()

# 新增產品
p1 = Product('Isacc', 8888, 'https://picsum.photos/id/1047/1200/600', '',
                'Y')
p2 = Product('Dennis', 9999, 'https://picsum.photos/id/1049/1200/600', '',
                'Y')
p3 = Product('Joey', 7777, 'https://picsum.photos/id/1033/1200/600', '',
                'Y')

# commit
db.session.add_all([p1, p2, p3])
db.session.commit()

# 新增商品類型
tag1 = Tag('免運費')
tag2 = Tag('新貨到')

# commit
db.session.add_all([tag1, tag2])
db.session.commit()

# 將商品新增類型
p1.db_product_tag_rel = [tag1, tag2]
p2.db_product_tag_rel = [tag1]
p3.db_product_tag_rel = [tag2]

db.session.commit()

# 查看商品
query = Product.query.filter_by(name='Isacc').first()
print(query.name)

# 輸出> Isacc

# 查看商品標籤
tags = query.db_product_tag_rel

for i in tags:
    print(i.tag_type)

# 輸出> 免運費
# 輸出> 新貨到
```