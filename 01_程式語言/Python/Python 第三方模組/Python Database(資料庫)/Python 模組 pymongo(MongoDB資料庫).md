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
  - [實現 join](#實現-join)
  - [聚合aggregate](#聚合aggregate)
    - [並行連接兩個集合](#並行連接兩個集合)
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

`查詢某個欄位是否存在並且有值`

```Python
db.your_collection.find({ "your_field": { "$exists": true, "$ne": null } })
```

`搜尋包含特定值`

```Python
db.post.find({ "your_field": { "$elemMatch": { "$eq": "your_value" } } })
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

`移除欄位`

```Python
# 指定要移除欄位的條件
query = {"your_query_field": "your_query_value"}

# 使用 $unset 操作符來移除指定欄位
update = {"$unset": {"field_to_remove": 1}}

# 執行更新操作
collection.update_many(query, update)
```

## 實現 join

```Python
client = MongoClient('mongodb://localhost:27017/?readPreference=primaryPreferred&appname=MongoDB%20Compass&ssl=false')
result = client['db']['collection1'].aggregate([
    {
        '$lookup': {
            'from': 'collection2',
            'localField': '$collection1field_commom',
            'foreignField': '$collection2field_commom',
            'as': 'joinedResult'
        }
    }
])
```

只取 collection2 部分欄位

```Python
from pymongo import MongoClient

# 連接到MongoDB
client = MongoClient('mongodb://localhost:27017/')
db = client['your_database']

# 使用$lookup進行連接，並只取collection2中的部分字段
result = db.collection1.aggregate([
    {
        '$lookup': {
            'from': 'collection2',
            'let': {'field1_value': '$field1'},  # 定義變數field1_value，值為collection1中的field1欄位值
            'pipeline': [
                {'$match': {'$expr': {'$eq': ['$field2', '$$field1_value']}}},  # 根據field2與field1_value才能進行匹配
                {'$project': {'_id': 0, 'field2': 1, 'field3': 1}}  # 只取field2和field3字段，可依需求調整
            ],
            'as': 'joined_data'  # 結果存放的現場名稱
        }
    }
])

# 打印连接后的结果
for doc in result:
    print(doc)
```

將其他集合的欄位 添加至當前集合的欄位

```Python
# 使用$lookup和pipeline進行連接，並將多個字段合併為一個字段
result = db.orders.aggregate([
    {
        '$lookup': {
            'from': 'customers',
            'localField': 'customer_id',
            'foreignField': '_id',
            'as': 'customer_info'
        }
    },
    # 展開customer_info字段，將其變成一個對象
    {
        '$unwind': '$customer_info'
    },
    # $concat是MongoDB中用於字串拼接的運算符，它可以將多個字串連接成一個字串。
    # 例如，如果$customer_info.customer_id的值為"123"，$customer_info.product_id的值為"456"，那麼經過$concat運算後，combined_field欄位的值就會是"123-456"。
    {
        '$addFields': {
            'combined_field': {'$concat': ['$customer_info.customer_id', '-', '$customer_info.product_id']}
        }
    },
    # 保留 bool 型態
    {
        '$addFields': {
            'combined_field': {
                '$concat': [
                    {'$cond': {'if': {'$eq': ['$customer_info.customer_id', True]}, 'then': 'true', 'else': 'false'}}
                ]
            }
        }
    },
    # 去除customer_info字段，只保留combined_field字段
    {
        '$project': {
            'customer_info': 0
        }
    }
])
```

combined_field 轉換成 bool 型態

```Python
result = db.orders.aggregate([
    {
        '$lookup': {
            'from': 'customers',
            'localField': 'customer_id',
            'foreignField': '_id',
            'as': 'customer_info'
        }
    },
    {
        '$unwind': '$customer_info'
    },
    {
        '$addFields': {
            'combined_field': {
                '$concat': [
                    {'$cond': {'if': {'$eq': ['$customer_info.customer_id', True]}, 'then': 'true', 'else': 'false'}}
                ]
            }
        }
    },
    {
        '$addFields': {
            'combined_field_bool': {
                '$cond': {'if': {'$eq': ['$combined_field', 'true']}, 'then': True, 'else': False}
            }
        }
    },
    {
        '$project': {
            'customer_info': 0,  # 去除customer_info字段
            'combined_field': 0  # 去除combined_field字段，保留combined_field_bool字段
        }
    },
    {
        '$sort': {
            'customer_info.modified_date': -1  # 按照modified_date降序排序，如果需要升序，將-1改成1
        }
    },
    {
        "$match": {
            "field1": value1,
            "field2": value2,
        }
    }
])
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

### 並行連接兩個集合

假設有兩個集合 collection1 和 collection2，它們都有一個名為 field_name 的字段，要找到兩個集合中 field_name 字段相同的文件。

```Python
from pymongo import MongoClient

def get_common_field_documents(db, collection1_name, collection2_name, field_name):
    """使用 $lookup 比較兩個集合中的特定欄位，取出相同值的文件

    Args:
        db: MongoDB 數據庫對象
        collection1_name (str): 第一個集合名稱
        collection2_name (str): 第二個集合名稱
        field_name (str): 要比較的欄位名稱

    Returns:
        list: 包含共同欄位值的文件列表
    """
    pipeline = [
        {
            '$lookup': {
                'from': collection2_name,
                'localField': field_name,
                'foreignField': field_name,
                'as': 'matched_docs'
            }
        },
        {
            '$match': {
                'matched_docs': {'$ne': []}
            }
        }
    ]

    collection1 = db[collection1_name]
    result = list(collection1.aggregate(pipeline))
    return result

# 連接到 MongoDB
client = MongoClient('mongodb://localhost:27017/')
db = client['your_database_name']

# 設置集合名稱和欄位名稱
collection1_name = 'collection1'
collection2_name = 'collection2'
field_name = 'field_name'

# 比較並取得共同的文件
common_documents = get_common_field_documents(db, collection1_name, collection2_name, field_name)

for doc in common_documents:
    print(doc)
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
