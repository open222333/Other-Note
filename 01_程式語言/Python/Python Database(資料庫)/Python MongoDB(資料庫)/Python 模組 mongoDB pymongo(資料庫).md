# Python 模組 mongoDB pymongo(資料庫)

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

- [Python 模組 mongoDB pymongo(資料庫)](#python-模組-mongodb-pymongo資料庫)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
	- [Insert](#insert)
	- [Query](#query)
	- [Update](#update)
	- [聚合aggregate](#聚合aggregate)
	- [使用ObjectID搜尋資料](#使用objectid搜尋資料)

## 參考資料

[pymongo pypi](https://pypi.org/project/pymongo/)

[mongodb的官方標準文件](https://docs.mongodb.com/manual/introduction/)

[MongoDB CRUD Operations(各種程式使用的範例)](https://docs.mongodb.com/manual/crud/)

[MongoDB 基礎入門教學：MongoDB Shell 篇](https://blog.gtwang.org/programming/getting-started-with-mongodb-shell-1/)

[cursor– 迭代 MongoDB 查詢結果的工具](https://pymongo.readthedocs.io/en/stable/api/pymongo/cursor.html)

[Python MongoDB](https://www.w3schools.com/python/python_mongodb_query.asp)

[文檔](https://pymongo.readthedocs.io/en/stable/index.html)

[Mongodb的使用方法&與python的互動](https://www.itread01.com/content/1541467390.html)

[MongoDB 查詢資料邏輯運算子語法範例](https://matthung0807.blogspot.com/2019/08/mongodb_50.html)

[MongoDB 查詢資料運算子(左邊列表可選類型)](https://docs.mongodb.com/manual/reference/operator/query/)

[聚合aggregate](https://www.yangyanxing.com/article/aggregate_in_pymongo.html)

# 指令

```bash
# 安裝
pip install sample
```

# 用法

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

## Query

```Python
# 取得全部資料
cursor = db.inventory.find({})

cursor = db.inventory.find({"status": "D"})

cursor = db.inventory.find({"status": {"$in": ["A", "D"]}})

cursor = db.inventory.find({"status": "A", "qty": {"$lt": 30}})
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
