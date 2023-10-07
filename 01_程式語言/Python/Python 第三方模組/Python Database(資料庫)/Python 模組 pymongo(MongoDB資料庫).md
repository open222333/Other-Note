# Python 模組 pymongo(MongoDB資料庫)

```
列出了傳統 SQL 與 MongoDB 的概念性對應關係：

SQL			MongoDB
database	database
table		collection
row			document
column		field

mongoDB相較於mySQL：插入較慢 讀取快

NoSQL最常⻅的解釋是“non-relational”，“Not Only SQL”也被很多⼈接受， 指的是⾮關係型的資料庫

易擴充套件： NoSQL資料庫種類繁多，但是⼀個共同的特點都是去掉關係資料庫的關係型特性。

⾼效能： NoSQL資料庫都具有⾮常⾼的讀寫效能，尤其在⼤資料量下，同樣表現優秀。

靈活的資料模型： NoSQL⽆需事先為要儲存的資料建⽴欄位，隨時可以儲存⾃定義的資料格式。
```

## 目錄

- [Python 模組 pymongo(MongoDB資料庫)](#python-模組-pymongomongodb資料庫)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [查詢相關](#查詢相關)
    - [指令相關](#指令相關)
    - [教學相關](#教學相關)
- [指令](#指令)
- [用法](#用法)
  - [Insert](#insert)
  - [index](#index)
  - [Query](#query)
  - [Update](#update)
  - [聚合aggregate](#聚合aggregate)
  - [使用ObjectID搜尋資料](#使用objectid搜尋資料)
  - [slaveOk 更換寫法](#slaveok-更換寫法)

## 參考資料

[pymongo pypi](https://pypi.org/project/pymongo/)

[mongodb的官方標準文件](https://docs.mongodb.com/manual/introduction/)

[MongoDB CRUD Operations(各種程式使用的範例)](https://docs.mongodb.com/manual/crud/)

[PyMongo 4.3.2 Documentation - 官方文檔](https://pymongo.readthedocs.io/en/stable/index.html)

[Read Preference (讀取偏好參考) - 官方文檔](https://www.mongodb.com/docs/manual/core/read-preference/)

[Read Preference 詳細說明 - 官方文檔](https://www.mongodb.com/docs/manual/core/read-preference/#std-label-replica-set-read-preference-modes)

### 查詢相關

[Python MongoDB Query](https://www.w3schools.com/python/python_mongodb_query.asp)

[Query Documents](https://www.mongodb.com/docs/manual/tutorial/query-documents/)

[Query for Null or Missing Fields](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#std-label-faq-developers-query-for-nulls)

[cursor– 迭代 MongoDB 查詢結果的工具](https://pymongo.readthedocs.io/en/stable/api/pymongo/cursor.html)

[MongoDB 查詢資料邏輯運算子語法範例](https://matthung0807.blogspot.com/2019/08/mongodb_50.html)

[MongoDB 查詢資料運算子(左邊列表可選類型)](https://docs.mongodb.com/manual/reference/operator/query/)

[聚合aggregate](https://www.yangyanxing.com/article/aggregate_in_pymongo.html)

### 指令相關

[MongoDB 基礎入門教學：MongoDB Shell 篇](https://blog.gtwang.org/programming/getting-started-with-mongodb-shell-1/)

### 教學相關

[Mongodb的使用方法&與python的互動](https://www.itread01.com/content/1541467390.html)

# 指令

```bash
# 安裝
pip install pymongo
```

# 用法

```Python
from pymongo import MongoClient

client = MongoClient('127.0.0.1', 27017)
# 顯示所有db
dbs = client.list_database_names()
# 顯示db下所有collection
client[db].list_collection_names()
```

```Python
from pymongo.collation import Collation
from pprint import pformat

def save_to_mongo(data: dict, col_client: Collection, unset: list = None, **query):
    """儲存資料至mongo

    Args:
        data (dict): 新增或更新資料 ex: {'code': '1', 'name':'2'}
        unset (list, optional): 移除欄位 名稱. Defaults to None. ex: ['code']
        query (optional): 更新資料時需輸入查詢條件. Defaults to None. ex: comic_id=1
    """
    if len(query) > 0 and col_client.find_one(query):
        update_query = {}
        if unset:
            unset_data = {}
            for filed in unset:
                unset_data[filed] = 1
            update_query['$unset'] = unset_data
        data['modified_date'] = datetime.now()
        update_query['$set'] = data
        col_client.update_one(query, update_query)
        print(f'更新資料 mongodb {collection}\n查詢條件: {query}\n內容: {pformat(update_query)}\n')
    else:
        data['creation_date'] = datetime.now()
        data['modified_date'] = datetime.now()
        print(f'新增資料 mongodb {collection}\n內容: {pformat(data)}\n')
        col_client.insert_one(data)
```

## Insert

```Python
db.inventory.insert_one(
    {
        "item": "canvas",
        "qty": 100,
        "tags": ["cotton"],
        "size": {"h": 28, "w": 35.5, "uom": "cm"},
    }
)

db.inventory.insert_many(
    [
        {
            "item": "canvas",
            "qty": 100,
            "size": {"h": 28, "w": 35.5, "uom": "cm"},
            "status": "A",
        },
        {
            "item": "journal",
            "qty": 25,
            "size": {"h": 14, "w": 21, "uom": "cm"},
            "status": "A",
        },
    ]
)
```

```Python
from pymongo import MongoClient

# 連接到 MongoDB
client = MongoClient('mongodb://localhost:27017/')
db = client['your_database_name']
collection = db['your_collection_name']

# 要更新的文檔條件
query = {'_id': 1}

# 要移除的欄位
field_to_remove = 'field_name_to_remove'

# 使用 $unset 運算子來移除欄位
update_query = {'$unset': {field_to_remove: 1}}

# 執行更新操作
collection.update_one(query, update_query)

# 關閉連接
client.close()
```

## index

```Python
from pymongo import MongoClient

client = MongoClient("mongodb://localhost:27017/")
db = client["您的資料庫名稱"]
collection = db["您的集合名稱"]

# 建立單一欄位的升序索引
collection.create_index("欄位名稱")

# 建立單一欄位的降序索引
collection.create_index([("欄位名稱", pymongo.DESCENDING)])

# 建立複合索引
collection.create_index([("欄位1", pymongo.ASCENDING), ("欄位2", pymongo.DESCENDING)])

# 建立全文搜尋索引
collection.create_index([("文字欄位", pymongo.TEXT)])

# 建立雜湊索引
collection.create_index("雜湊欄位", hashField=True)

# 建立唯一索引
collection.create_index("唯一欄位", unique=True)

# 建立具有過期時間的索引（TTL 索引）
collection.create_index("過期時間欄位", expireAfterSeconds=3600)

# 創建索引
index_names = ["code"]
existing_indexes = collection.index_information()
for index_name in index_names:
    if index_name not in existing_indexes:
        collection.create_index(index_name)
```

## Query

```Python
# 取得全部資料
cursor = db.inventory.find({})

cursor = db.inventory.find({"status": "D"})

cursor = db.inventory.find({"status": {"$in": ["A", "D"]}})

cursor = db.inventory.find({"status": "A", "qty": {"$lt": 30}})

# 查詢不包含字段的文檔
cursor = db.inventory.find({"item": {"$exists": False}})
```

## Update

```Python
db.inventory.update_one(
    {"item": "paper"},
    {"$set": {"size.uom": "cm", "status": "P"}, "$currentDate": {"lastModified": True}},
)

db.inventory.update_many(
    {"qty": {"$lt": 50}},
    {"$set": {"size.uom": "in", "status": "P"}, "$currentDate": {"lastModified": True}},
)

db.inventory.replace_one(
    {"item": "paper"},
    {
        "item": "paper",
        "instock": [{"warehouse": "A", "qty": 60}, {"warehouse": "B", "qty": 40}],
    },
)
```

## 聚合aggregate

```Python
# 聚合aggregate
from pymongo import MongoClient

client = MongoClient(host=['%s:%s'%(mongoDBhost,mongoDBport)])
G_mongo = client[mongoDBname]['FruitPrice']

pipeline = [
    {'$group': {'_id': "$fName", 'count': {'$sum': 1}}},
]
for i in G_mongo['test'].aggregate(pipeline):
    print i

pipeline = [
    {
        "$addFields": {
            "created_updated_days_divide": {"$divide": [{"$subtract": ["$avdata_updated_at", "$avdata_created_at"]}, 60 * 60 * 24 * 1000]},
            "now_updated_days_divide": {"$divide": [{"$subtract": [ datetime.now(), "$avdata_updated_at"]}, 60 * 60 * 24 * 1000]}}
    },
    {
        "$match": {"$or":[{"created_updated_days_divide": {"$lte": 30}}, {"now_updated_days_divide": {"$lte": 30}}]}
    }
]
```

## 使用ObjectID搜尋資料

```Python
from pymongo import MongoClient
from bson import ObjectId

client = MongoClient('127.0.0.1:27017')
collection = client['db']['collection']

data = collection.find_one({'col.col2': ObjectId('id')})
```

## slaveOk 更換寫法

```Python
# ssl connection for pymongo > 2.3
if pymongo.version >= "2.3":
	if replica is None:
		con = pymongo.MongoClient(host, port)
		con = pymongo.MongoClient(host, port, slaveOk=True)
	else:
		# slaveOk 更改為使用 SECONDARY_PREFERRED
		con = pymongo.MongoClient(
			host,
			port,
			username=usernam,
			password=password,
			authSource=auth_db,
			read_preference=pymongo.ReadPreference.SECONDARY_PREFERRED
		)

```
