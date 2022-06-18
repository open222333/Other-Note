# Python 模組 mongoDB pymongo(資料庫)

## 參考資料

[MongoDB CRUD Operations(各種程式使用的範例)](https://docs.mongodb.com/manual/crud/)

[MongoDB 基礎入門教學：MongoDB Shell 篇](https://blog.gtwang.org/programming/getting-started-with-mongodb-shell-1/)

[cursor– 迭代 MongoDB 查詢結果的工具](https://pymongo.readthedocs.io/en/stable/api/pymongo/cursor.html)

[Python MongoDB](https://www.w3schools.com/python/python_mongodb_query.asp)

[文檔](https://pymongo.readthedocs.io/en/stable/index.html)

[Mongodb的使用方法&與python的互動](https://www.itread01.com/content/1541467390.html)

[MongoDB 查詢資料邏輯運算子語法範例](https://matthung0807.blogspot.com/2019/08/mongodb_50.html)

[MongoDB 查詢資料運算子(左邊列表可選類型)](https://docs.mongodb.com/manual/reference/operator/query/)

## 聚合 參考

[聚合aggregate](https://www.yangyanxing.com/article/aggregate_in_pymongo.html)

```python
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

## 參考

列出了傳統 SQL 與 MongoDB 的概念性對應關係：
```
SQL			MongoDB
database	database
table		collection
row			document
column		field
```

mongoDB相較於mySQL：插入較慢 讀取快


1. nosql的介紹
```
“NoSQL”⼀詞最早於1998年被⽤於⼀個輕量級的關係資料庫的名字
隨著web2.0的快速發展， NoSQL概念在2009年被提了出來
NoSQL在2010年⻛⽣⽔起， 現在國內外眾多⼤⼩⽹站， 如facebook、 google、 淘寶、 京東、 百度等， 都在使⽤nosql開發⾼效能的產品
對於⼀名程式設計師來講， 使⽤nosql已經成為⼀條必備技能
NoSQL最常⻅的解釋是“non-relational”， “Not Only SQL”也被很多⼈接受， 指的是⾮關係型的資料庫
```

2. mongodb的優勢
```
易擴充套件： NoSQL資料庫種類繁多， 但是⼀個共同的特點都是去掉關係資料庫的關係型特性。 資料之間⽆關係， 這樣就⾮常容易擴充套件
⼤資料量， ⾼效能： NoSQL資料庫都具有⾮常⾼的讀寫效能， 尤其在⼤資料量下， 同樣表現優秀。 這得益於它的⽆關係性， 資料庫的結構簡單
靈活的資料模型： NoSQL⽆需事先為要儲存的資料建⽴欄位， 隨時可以儲存⾃定義的資料格式。 ⽽在關係資料庫⾥， 增刪欄位是⼀件⾮常麻煩的事情。 如果是⾮常⼤資料量的表， 增加欄位太複雜
```

3. 安裝
[Install MongoDB Community Edition on Ubuntu](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/)

```bash
sudo apt-get install -y mongodb-org
```


brew安裝

```bash
brew install mongodb
```


4. mongodb的啟動
```bash
# 檢視幫助
mongod –help
# 啟動
brew services start mongodb
# 停止
brew services stop mongodb
# 重啟
brew services restart mongodb
# 檢視是否啟動成功
ps -ef|grep mongod
```
## 配置檔案的位置
/etc/mongod.conf

```conf
# 預設端⼝
27017
# 日誌的位置
/var/log/mongodb/mongod.log
```

[mongodb的官方標準文件](https://docs.mongodb.com/manual/introduction/)

---
# mongodb資料庫的命令

```js
// 檢視當前的資料庫
db
// 檢視所有的資料庫
show dbs /show databases
// 切換資料庫
use db_name
// 刪除當前的資料庫
db.dropDatabase()
```
## mongodb集合

預設不需要手動建立集合:
```
向不存在的集合中第⼀次加⼊資料時，集合會被創建出來
```

```js
// 手動建立結合
db.createCollection(name,options)
db.createCollection("test")
db.createCollection("test01",{ capped:true,size:10})

// 引數capped：
// 預設值為false表示不設定上限,值為true表示設定上限

// 引數size：
// 當capped值為true時，需要指定此引數，表示上限⼤⼩,當⽂檔達到上限時，會將之前的資料覆蓋，單位為位元組

// 檢視集合
show collections
// 刪除集合
db.集合名稱.drop()
```

1. mongodb中常見的資料型別
```
常見型別

    Object ID
    ⽂檔ID

    String
    字串， 最常⽤， 必須是有效的UTF-8

    Boolean
    儲存⼀個布林值， true或false

    Integer
    整數可以是32位或64位， 這取決於伺服器

    Double
    儲存浮點值

    Arrays
    陣列或列表， 多個值儲存到⼀個鍵

    Object
    ⽤於嵌⼊式的⽂檔， 即⼀個值為⼀個⽂檔

    Null
    儲存Null值

    Timestamp
    時間戳， 表示從1970-1-1到現在的總秒數

    Date
    儲存當前⽇期或時間的UNIX時間格式


注意點

1. 建立⽇期語句如下：引數的格式為YYYY-MM-DD new 例：Date('2018-10-01')

2. 每個⽂檔都有⼀個屬性，為_id，保證每個⽂檔的唯⼀性可以⾃⼰去設定_id插⼊⽂檔，如果沒有提供，那麼MongoDB為每個⽂檔提供了⼀個獨特的_id，型別為objectID

3. objectID是⼀個12位元組的⼗六進位制數,每個位元組兩位，一共是24位的字串：前4個位元組為當前時間戳,接下來3個位元組的機器ID接下來的2個位元組中MongoDB的服務程序id 最後3個位元組是簡單的增量值
```

## mongodb的增刪改查
```js
// collection 需帶入集合名稱
// mongodb的插入
db.collection.insert(document)
db.test.insert({name:'kungs',gender:1})
db.test.insert({_id:"20181001",name:'kungs',gender:1})

// 注意：插⼊⽂檔時， 如果不指定_id引數， MongoDB會為⽂檔分配⼀個唯⼀的ObjectId

// mongodb的儲存
db.collection.save(document)

// 如果⽂檔的id已經存在則修改， 如果⽂檔的id不存在則新增

// mongodb的簡單查詢
db.collection.find()

// mongodb的更新
db.collection.update(quer, update, {multi: boolean})

// 引數query:查詢條件
// 引數update:更新操作符
// 引數multi:可選， 預設是false，表示只更新找到的第⼀條記錄， 值為true表示把滿⾜條件的⽂檔全部更新

db.test.update({name:‘kungs’},{name:‘kungs8’}) // 更新一條

// 注意：“multi update only works with $ operators”


// mongodb的刪除
db.collection.remove(query,{justOne: boolean})

// 引數query:可選，刪除的⽂檔的條件
// 引數justOne:可選， 如果設為true或1， 則只刪除⼀條， 預設false， 表示刪除多條

// mongodb的高階查詢
// 資料查詢
db.collection.find({條件⽂檔})

// 查詢，只返回第⼀個
db.collection.findOne({條件⽂檔})
// 將結果格式化
db.collection.find({條件⽂檔}).pretty()

// 比較運算子
// 練習資料
{"name" : "郭靖", "hometown" : "蒙古", "age" : 20, "gender" : true }
{"name" : "⻩蓉", "hometown" : "桃花島", "age" : 18, "gender" : false }
{"name" : "華箏", "hometown" : "蒙古", "age" : 18, "gender" : false }
{"name" : "⻩藥師", "hometown" : "桃花島", "age" : 40, "gender" : true }
{"name" : "段譽", "hometown" : "⼤理", "age" : 16, "gender" : true }
{"name" : "段王爺", "hometown" : "⼤理", "age" : 45, "gender" : true }
{"name" : "洪七公", "hometown" : "華⼭", "age" : 18, "gender" : true }


// 等於        預設是等於判斷， 沒有運算子
// ⼩於        $lt         (less than)
// ⼩於等於     $lte     (less than equal)
// ⼤於         $gt  (greater than)
// ⼤於等於    $gte
// 不等於      $ne

// 例如：
// 查詢年齡大於18的所有人物
db.test.find({age:{$gte:18}})

// 邏輯運算子
// 邏輯運算子主要指與、或邏輯

// and：在json中寫多個條件即可
// 查詢年齡⼤於或等於18， 並且性別為true的人物
db.test.find({age:{$gte:18},gender:true})

// or:使⽤$or，值為陣列，陣列中每個元素為json
// 查詢年齡⼤於18， 或性別為false的人物
db.test.find({$or:[{age:{$gt:18}},{gender:false}]})

// 查詢年齡⼤於18或性別為男， 並且姓名是郭靖
db.test.find({$or:[{age:{$gte:18}},{gender:true}],name:'gj'})

// 範圍運算子
// 使⽤$in， $nin 判斷資料是否在某個陣列內
//查詢年齡為18、 28的人
db.test.find({age:{$in:[18,28,38]}})

// ⽀持正則表示式
// 使⽤ //或 $regex 編寫正則表示式

// 建立products資料：
{ "_id" : 100, "sku" : "abc123", "description" : "Single line description." }
{ "_id" : 101, "sku" : "abc789", "description" : "First line\nSecond line" }
{ "_id" : 102, "sku" : "xyz456", "description" : "Many spaces before     line" }
{ "_id" : 103, "sku" : "xyz789", "description" : "Multiple\nline description" }

// 查詢products以abc開頭的資料
db.products.find({sku:/^abc/})

// 查詢sku以789結尾的資料
db.products.find({sku:{$regex:'789$'}})

// skip和limit
// ⽅法limit()： ⽤於讀取指定數量的⽂檔
db.集合名稱.find().limit(NUMBER)

// 查詢2條學⽣資訊
db.test.find().limit(2)

// ⽅法skip()： ⽤於跳過指定數量的⽂檔
db.集合名稱.find().skip(NUMBER)
db.test.find().skip(2)

// 同時使用
db.test.find().limit(4).skip(5)
db.test.find().skip(5).limit(4)
// 注意：先使用skip在使用limit的效率要高於前者

// 自定義查詢*
// 由於mongo的shell是一個js的執行環境 使⽤ $where 後⾯寫⼀個函式， 返回滿⾜條件的資料

// 查詢年齡⼤於30的學⽣
db.test.find({
    $where:function() {
        return this.age>30;}
})

// 投影( find() )
// 在查詢到的返回結果中， 只選擇必要的欄位，命令為：
db.集合名稱.find({},{欄位名稱:1,...})

// 引數為欄位與值， 值為1表示顯示， 值為0不顯
// 特別注意： 對於_id列預設是顯示的， 如果不顯示需要明確設定為0，例：
db.test.find({},{_id:0,name:1,gender:1})

// 排序( sort() )
// ⽤於對 集進⾏排序，命令為：
db.集合名稱.find().sort({欄位:1,...})
// 引數1為升序排列 引數-1為降序排列例：

// 根據性別降序， 再根據年齡升序
db.test.find().sort({gender:-1,age:1})

// 統計個數( count() )
// ⽤於統計結果集中⽂檔條數
db.集合名稱.find({條件}).count()
db.集合名稱.count({條件})
db.test.find({gender:true}).count()
db.test.count({age:{$gt:20},gender:true})

// 消除重複( distinct() )
// 對資料進⾏去重
db.集合名稱.distinct('去重欄位',{條件})
db.test.distinct('hometown',{age:{$gt:18}})
```
---
# mongodb的聚合操作

```
聚合(aggregate)是基於資料處理的聚合管道，每個文件通過一個由多個階段（stage）組成的管道，可以對每個階段的管道進行分組、過濾等功能，然後經過一系列的處理，輸出相應的結果。語法：
```


```js
db.集合名稱.aggregate({管道:{表示式}})
```

## mongodb的常用管道和表示式

```
$group： 將集合中的⽂檔分組， 可⽤於統計結果
$match： 過濾資料， 只輸出符合條件的⽂檔
$project： 修改輸⼊⽂檔的結構， 如重新命名、 增加、 刪除欄位、 建立計算結果
$sort： 將輸⼊⽂檔排序後輸出
$limit： 限制聚合管道返回的⽂檔數
$skip： 跳過指定數量的⽂檔， 並返回餘下的⽂檔
```

表示式：處理輸⼊⽂檔並輸出 語法：表示式:’$列名’ 常⽤表示式:

```
$sum： 計算總和， $sum:1 表示以⼀倍計數
$avg： 計算平均值
$min： 獲取最⼩值
$max： 獲取最⼤值
$push： 在結果⽂檔中插⼊值到⼀個數組中
```


## 管道命令之$group
按照某個欄位進行分組
$group是所有聚合命令中用的最多的一個命令，用來將集合中的文件分組，可用於統計結果

使用示例如下

```js
db.test.aggregate(
    {$group:
        {
            _id:"$gender",
            counter:{$sum:1}
        }
    }
)
```
其中注意點：

1. db.db_name.aggregate： 是語法，所有的管道命令都需要寫在其中
2. _id： 表示分組的依據，按照哪個欄位進行分組，需要使用`$gender`表示選擇這個欄位進行分組
3. $sum:1： 表示把每條資料作為1進行統計，統計的是該分組下面資料的條數

## group by null

當我們需要統計整個文件的時候，$group 的另一種用途就是把整個文件分為一組進行統計

使用例項如下：
```js
db.test.aggregate(
    {$group:
        {
            _id:null,
            counter:{$sum:1}
        }
    }
)
```

注意點：

_id:null 表示不指定分組的欄位，即統計整個文件，此時獲取的counter表示整個文件的個數

## 資料透視
正常情況在統計的不同性別的資料的時候，需要知道所有的name，需要逐條觀察，如果通過某種方式把所有的name放到一起，那麼此時就可以理解為資料透視

使用示例如下：

1. 統計不同性別的人
```js
   db.test.aggregate(
       {$group:
           {
               _id:null,
               name:{$push:"$name"}
           }
       }
   )
```

2. 使用$$ROOT可以將整個文件放入陣列中
```js
db.test.aggregate(
       {$group:
           {
               _id:null,
               name:{$push:"$$ROOT"}
           }
       }
   )
```
## 示例
對於如下資料，需要統計出每個country/province下的userid的數量（同一個userid只統計一次）
```js
{ "country" : "china", "province" : "sh", "userid" : "a" }
{  "country" : "china", "province" : "sh", "userid" : "b" }
{  "country" : "china", "province" : "sh", "userid" : "a" }
{  "country" : "china", "province" : "hf", "userid" : "c" }
{  "country" : "china", "province" : "hf", "userid" : "da" }
{  "country" : "china", "province" : "hf", "userid" : "fa" }

// 參考答案
db.tv3.aggregate(
  {$group:{_id:{country:'$country',province:'$province',userid:'$userid'}}},
  {$group:{_id:{country:'$_id.country',province:'$_id.province'},count:{$sum:1}}}
```

## 管道命令之$match

match用於進行資料的過濾，是在能夠在聚合操作中使用的命令，和find區別在於 match用於進行數據的過濾，是在能夠在聚合操作中使用的命令，和find區別在於match 操作可以把結果交給下一個管道處理，而find不行

使用示例如下：

1. 查詢年齡大於20的學生
```js
db.test.aggregate(
    {$match:{age:{$gt:20}}
    )
```
2. 查詢年齡大於20的男女學生的人數

```js
db.test.aggregate(
    {$match:{age:{$gt:20}}
    {$group:{_id:"$gender",counter:{$sum:1}}}
    )
```

## 管道命令之$project

$project用於修改文件的輸入輸出結構，例如重新命名，增加，刪除欄位

使用示例如下：

查詢學生的年齡、姓名，僅輸出年齡姓名

```js
db.stu.aggregate(
    {$project:{_id:0,name:1,age:1}}
    )
```

查詢男女生人生，輸出人數

```js
db.stu.aggregate(
    {$group:{_id:"$gender",counter:{$sum:1}}}
    {$project:{_id:0,counter:1}}
    )
```

## 示例
對於如下資料：統計出每個country/province下的userid的數量（同一個userid只統計一次），結果中的欄位為{country:""，province:""，counter:"*"}

```js
{ "country" : "china", "province" : "sh", "userid" : "a" }
{  "country" : "china", "province" : "sh", "userid" : "b" }
{  "country" : "china", "province" : "sh", "userid" : "a" }
{  "country" : "china", "province" : "hf", "userid" : "c" }
{  "country" : "china", "province" : "hf", "userid" : "da" }
{  "country" : "china", "province" : "hf", "userid" : "fa" }
// 參考答案

db.tv3.aggregate(
  {$group:{_id:{country:'$country',province:'$province',userid:'$userid'}}},
  {$group:{_id:{country:'$_id.country',province:'$_id.province'},count:{$sum:1}}},
  {$project:{_id:0,country:'$_id.country',province:'$_id.province',counter:'$count'}}
  )
```

## 管道命令之$sort

$sort用於將輸入的文件排序後輸出

使用示例如下：

查詢學生資訊，按照年齡升序

```js
db.stu.aggregate({$sort:{age:1}})
```

查詢男女人數，按照人數降序

```js
db.stu.aggregate(
    {$group:{_id:"$gender",counter:{$sum:1}}},
    {$sort:{counter:-1}}
)
```

## 管道命令之$skip 和 $limit
$limit限制返回資料的條數
$skip 跳過指定的文件數，並返回剩下的文件數
同時使用時先使用skip在使用limit
使用示例如下：

查詢2條學生資訊
```js
db.test.aggregate(
    {$limit:2}
)
```

查詢從第三條開始的學生資訊

```js
db.test.aggregate(
    {$skip:3}
)
```

統計男女生人數，按照人數升序，返回第二條資料

```js
db.test.aggregate(
    {$group:{_id:"$gender",counter:{$sum:1}}},
    {$sort:{counter:-1}},
    {$skip:1},
    {$limit:1}
)
```
---
# Mongdb的索引備份以及和python互動


## mongdb建立索引的目的
加快查詢速度
進行資料的去重

## mongodb建立簡單的索引方法

語法：

```js
db.集合.ensureIndex({屬性:1})  # 1表示升序， -1表示降序
db.集合.createIndex({屬性:1})
```

例項：

```js
db.test.ensureIndex({name:1})
```

1.4 索引的檢視
預設情況下_id是集合的索引

檢視方式：
```js
db.collection_name.getIndexes()
```

---
# mongodb的備份和恢復

## 備份
備份的語法：
```bash
mongodump -h dbhost -d dbname -o dbdirectory
    # -h： 伺服器地址， 也可以指定端⼝號,預設情況下是本地(127.0.0.1)
    # -d： 需要備份的資料庫名稱
    # -o： 備份的資料存放位置， 此⽬錄中存放著備份出來的資料

mongodump -h 127.0.0.1:27017 -d test1 -o ~/Desktop/
```

## 恢復
恢復語法：
```bash
mongorestore -h dbhost -d dbname --dir dbdirectory
    # -h： 伺服器地址
    # -d： 需要恢復的資料庫例項
    # –dir： 備份資料所在位置

mongorestore -h 127.0.0.1:27017 -d test2 --dir ~/Desktop/test1
```

pymongo 提供了mongdb和python互動的所有方法

安裝方式:
```bash
pip install pymongo
```

```python
from pymongo import MongoClient
client = MongoClient(host,port)
collection = client[db名][集合名]
# 新增一條資料
ret = collection.insert_one({"name":"10086","age":100})
print(ret)

# 新增多條資料
item_list = [{"name":"py_{}".format(i)} for i in range(10)]

# insert_many接收一個列表，列表中為所有需要插入的字典
t = collection.insert_many(item_list)

# 查詢一條資料
#find_one查詢並且返回一個結果,接收一個字典形式的條件
t = collection.find_one({"name":"py_5"})
print(t)

# 查詢全部資料
# 結果是一個Cursor遊標物件，是一個可迭代物件，可以類似讀檔案的指標，但是隻能夠進行一次讀取
# find返回所有滿足條件的結果，如果條件為空，則返回資料庫的所有
t = collection.find({"name":"py_5"})
# 結果是一個Cursor遊標物件，是一個可迭代物件，可以類似讀檔案的指標，
for i in t:
    print(i)
for i in t: # 此時t中沒有內容
    print(i)

# 更新一條資料 注意使用$set命令
# update_one更新一條資料
collection.update_one({"name":"py_5"},{"$set":{"name":"new_test10005"}})

# 更行全部資料
# update_one更新全部資料
collection.update_many({"name":"py_5"},{"$set":{"name":"new_py_5"}})

# 刪除一條資料
# delete_one刪除一條資料
collection.delete_one({"name":"py_10"})
9. 刪除全部資料

# delete_may刪除所有滿足條件的資料
collection.delete_many({"name":"py_10"})
```

示例 例項：
1. 使用python向集合t3中插入1000條文件，文件的屬性包括_id、name
    1) _id的值為0、1、2...999
    2) _id的值為'py0'、'py1',...
2. 查詢顯示出_id為100的整倍數的文件，如100、200...，並將name輸出

```python
from pymongo import MongoClient

# client = MongoClient(host="127.0.0.1", port=27017)
client = MongoClient()  # 本機，引數可以不用寫

collection = client["test1000"]["t3"]

temp_list = [{"_id": i, "name": "py{}".format(i)} for i in range(1000)]
print(temp_list)
collection.insert_many(temp_list)

ret_list = list(collection.find({}))
ret = [i["name"] for i in ret_list if i["_id"]%100==0 and i["_id"]!=0]
print(ret)
```

---
# MongoEngine 和python互動
pymongo操作MongoDB資料庫，直接把對資料庫的操作程式碼寫在腳本里，應用的程式碼耦合性太強，不利於程式碼的優化管理。

通常情況下應用是使用MVC框架來設計的，為了更好地維持MVC結構，需要把資料庫操作部分作為model抽離出來，這就需要藉助MongoEngine

MongoEngine是一個物件文件對映器（ODM），相當於一個基於SQL的物件關係對映器（ORM）

MongoEngine提供的抽象是基於類的，建立的所有模型都是類

---
## MongoEngine安裝
```
pip3 install mongoengine
```

先宣告一個繼承自MongoEngine.Document的類

在類中宣告一些屬性，相當於建立一個用來儲存資料的資料結構，即資料以類似資料結構的形式存入資料庫中，通常把這樣的一些類都存放在一個指令碼中，作為應用的Model模組

```python
from mongoengine import *
connect('test', host='localhost', port=27017)
import datetime
class Page(Document):
    title = StringField(max_length=200, required=True)
    date_modified = DateTimeField(default=datetime.datetime.utcnow)

# Create a new page and add tags
page = Page(title='Using MongoEngine')
page.tags = ['mongodb', 'mongoengine']
page.save()

Page.objects(tags='mongoengine').count()
```

具體操作的方法見下面連結

[Docs參考](http://docs.mongoengine.org/tutorial.html)