# MongoDB 筆記

```
MongoDB是一種介於關係型和非關係型中間的資料庫。它是文件型資料庫（一個文件包含多個鍵/值對），多個文件組成集合，多個集合組成資料庫。一個MongoDB 例項可以承載多個數據庫。這裡集合的概念類似關係型資料庫中的表。MongoDB的優勢在於資料儲存與互動非常靈活，採用類似JSON格式，能靈活地建立索引以及完成SQL能做的幾乎所有查詢。此外它還是一個支援分散式的資料庫，能自動處理分片實現負載均衡，並且支援使用MapReduce進行復雜的聚合操作。

停止容器運作導致 MongoDB process（(行程）) 停止，類似 kill process 非正常關閉 MongoDB，在有進行讀寫的情況下可能會產生無法預期的情況。
```

## 目錄

- [MongoDB 筆記](#mongodb-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝步驟 CentOS7](#安裝步驟-centos7)
	- [配置檔案設定](#配置檔案設定)
	- [防火牆設定](#防火牆設定)
- [安裝步驟 MacOS](#安裝步驟-macos)
- [指令](#指令)
- [指令 匯入匯出](#指令-匯入匯出)
- [資料庫指令](#資料庫指令)
- [資料庫指令 使用者](#資料庫指令-使用者)
	- [mongodb 使用者許可權角色說明](#mongodb-使用者許可權角色說明)

## 參考資料

[centos安裝](https://iter01.com/156322.html)

[中文文檔](https://www.docs4dev.com/docs/zh/mongodb/v3.6/reference/reference-program-mongorestore.html)

[MongoDB CRUD Operations(各種程式使用的範例)](https://docs.mongodb.com/manual/crud/)

[Mondb 邏輯運算子](https://www.mongodb.com/docs/manual/reference/operator/query/)

[mongodb下載地址](https://www.mongodb.com/download-center#community)

[mongodb 正則表達式](https://www.mongodb.com/docs/manual/reference/operator/query/regex/)

[{ 欄位: { 運算子: 條件 } }](https://www.uj5u.com/shujuku/12759.html)

[MongoDB 查詢資料邏輯運算子語法範例](https://matthung0807.blogspot.com/2019/08/mongodb_50.html)

[mongodb - 聚合管道抛出错误 "A pipeline stage specification object must contain exactly one field."](https://www.coder.work/article/39368)

[Install MongoDB 5.0 on CentOS 8/7 & RHEL 8/7](https://computingforgeeks.com/how-to-install-mongodb-on-centos-rhel-linux/)

[mongod 資料庫指令 manpage](https://docs.mongodb.com/manual/reference/program/mongod/)

[mongodump reference page(文檔)](https://docs.mongodb.com/database-tools/mongodump/#mongodb-binary-bin.mongodump)

[mongorestore reference page(文檔)](https://docs.mongodb.com/database-tools/mongorestore/#mongodb-binary-bin.mongorestore)

[db.createUser() - 創建使用者](https://www.mongodb.com/docs/manual/reference/method/db.createUser/)

# 安裝步驟 CentOS7

```bash
# 建立.repo檔案，生成mongodb的源
vim /etc/yum.repos.d/mongodb-org-4.0.repo # 舊版
vim /etc/yum.repos.d/mongodb-org-5.0.repo
```

```repo
# 新增以下配置資訊：
[mongodb-org-4.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/#releasever/mongodb-org/4.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-4.0.asc

[mongodb-org-5.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/5.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-5.0.asc

====================================
詳解：
name         # 名稱
baseurl      # 獲得下載的路徑
gpkcheck=1   # 表示對從這個源下載的rpm包進行校驗
enable=1     # 表示啟用這個源。
gpgkey       # gpg驗證
====================================
```

```bash
# 安裝
yum install mongodb-org -y

# 修改 mongod.conf
vim /etc/mongod.conf
	# net:
	# 	port: 27017
	# 	bindIp: 0.0.0.0

# 驗證安裝結果
# 檢視資料庫的程式是否存在
rpm -qa |grep mongodb

rpm -ql mongodb-org-server

# 開啟MongoDB
sudo service mongod start
systemctl start mongod.service

# 啟動服務
systemctl start mongod
# 開機啟動
systemctl enable mongod
# 查詢啟動狀態
systemctl status mongod
# 重啟
systemctl restart mongod
# 停止
systemctl stop mongod

# 解除安裝MongoDB
sudo yum erase $(rpm -qa | grep mongodb-org)
# 刪除日誌檔案
sudo rm -r /var/log/mongodb
# 刪除資料檔案
sudo rm -r /var/lib/mongo

# MongoDB預設埠是27017，檢視是否開啟
netstat -natp | grep 27017
```

## 配置檔案設定

`bin/mongod.cfg`文件中會有 dbPath 和logPath的配置

位置:`/etc/mongod.conf`(設定完需重啟)

```conf
; 繫結ip預設127.0.0.1只允許本地連線，所以修改為bindIp:0.0.0.0
; network interfaces
net:
  port: 27017
  bindIp: 0.0.0.0

; 身份驗證
security:
  authorization: "enabled"   # disable or enabled
```

## 防火牆設定

```bash
### 開放對外埠 方法一

# 檢視防火牆狀態
systemctl status firewalld
# mongodb預設埠號
firewall-cmd --zone=public --add-port=27017/tcp --permanent
# 重新載入防火牆
firewall-cmd --reload
# 檢視埠號是否開放成功，輸出yes開放成功，no則失敗
firewall-cmd --zone=public --query-port=27017/tcp

### 開放對外埠 方法二

iptables -A INPUT -p tcp -m state --state NEW -m tcp --dport 27017 -j ACCEPT
```

# 安裝步驟 MacOS

```bash
# 安裝
brew install mongodb

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

## 配置檔案的位置
/etc/mongod.conf

	# 預設端⼝
	27017
	# 日誌的位置
	/var/log/mongodb/mongod.log
```


# 指令

```bash
# 執行檔mongodb 用來連入DB, 預設port 27017
# 進入後指令查看下方
mongo

# 查看log(需安裝jq)
cat mongod.log | jq

# 啟動服務
systemctl start mongod
# 查詢啟動狀態
systemctl status mongod
# 重新啟動
systemctl restart mongod
# 停止服務
systemctl stop mongod
# 開機啟動
systemctl enable mongod

# 創建數據庫文件的存放位置，啟動mongodb服務時需要先確定數據庫文件存放的位置，否則係統不會自動創建，啟動會不成功。
mongod --dbpath
# 表示日誌文件存放的路徑
mongod --logpath
# 表示以追加的方式寫日誌文件
mongod --logappend
```

# 指令 匯入匯出

```bash
# 匯出
mongodump
    # -h: 要備份的 MongoDB 連線位置 服務器地址，例如：127.0.0.1:27017
    # -d: 要備份的 Database 名稱
    # -u: 資料庫使用者名稱
    # -p: 資料庫密碼
    # -o：備份的數據存放位置，例如：c:\data\dump，目錄需要提前建立

# 匯入
mongorestore $path
    # -h --host: 要還原的 MongoDB 連線位置 服務器地址，例如：127.0.0.1:27017
    # -d --db: 要還原的 Database 名稱
    # -c --collection:
    # -u: 資料庫使用者名稱
    # -p: 資料庫密碼
    # --dir --directoryperdb: 指定要還原的資料庫檔案來源目錄名稱 不能同時指定<path> 和--dir 選項，--dir也可以設置備份目錄。
    # --drop: 如果資料庫存在就刪除重新建立 (小心使用)
    # <path>:設置備份數據所在位置，例如：c:\data\dump\test。
	# dump檔匯入
	--archive=mongo.dump
```

# 資料庫指令

```js
// 安全的 關閉 MongoDB 需要連入資料庫
use admin
db.shutdownServer()

// 以admin 身分登入
db.auth("admin","{PASSWORD}")

// 顯示DB
show dbs

// 切換dbname, 注意大小寫, 用法跟MySql類似
use dbname

// 顯示集合
show collections

// 建立users集合
db.createCollection(‘users’)

// 刪除集合users
db.users.drop()
db.runCommand({"drop","users"})

// 刪除目前DB
db.runCommand({"dropDatabase": 1})

// help
odb.help();
odb.yourColl.help();
odb.youColl.find().help();

// 刪除當前DB
db.dropDatabase()

// 從指定的機器上複製DB
db.cloneDatabase(“127.0.0.1”)

// 從指定的機器複製到本地的 temp DB
db.copyDatabase("mydb", "temp", "127.0.0.1")

// 查看當前DB
db

// 查看版本
db.version()

// 查看當前的db 版本
db.getMongo()

// 新增
save()

// 建立一個 users的集合 並且寫入一筆{"name":"lecaf"} 資料
db.users.save({"name":"lecaf"})

// 在users集合中寫入新資料，如果没有users，mongodb會自動建立一個
db.users.insert({"name":"ghost", "age":10})

// save()和insert()也存在著些許區別：若新增的數據主鍵已經存在，insert()會不做操作並提示錯誤，而save() 則更改原來的內容為新內容。

insert({ _id : 1, " name " : " n1 "})   // _id是主键
insert({ _id : 1, " name " : " n2 " })  // 會顯示錯誤, 因為1已經有資料了
save({ _id : 1, " name " : " n2 " })    // 會把 n1 更新為 n2 ，類似update。


// 刪除users集合所有資料
db.users.remove()

// 刪除users集合下name=lecaf的該筆資料
db.users.remove({"name": "lecaf"})

// 查詢users集合中所有資料
db.users.find()

// 查詢users集合中符合name=feng的所有資料
db.users.find({“name”:”feng”})

// 查詢users集合中的第一筆資料
db.users.findOne()

// 查詢users集合中name=feng的第一筆資料
db.users.findOne({“name”:”feng”})

// 修改資料，其中name=lecaf 為查詢條件，"age":10是修改内容，除了主键，其他内容会被第二個參數替換，主键不能修改, 只能新增刪除.
db.users.update({"name":"lecaf"}, {"age":10})

// 查詢key=value的資料
db.collection.find({ "key" : value })

// 查詢key > value
db.collection.find({ "key" : { $gt: value } })

// 查詢key < value
db.collection.find({ "key" : { $lt: value } })

// 查詢key >= value
db.collection.find({ "key" : { $gte: value } })

// 查詢key <= value
db.collection.find({ "key" : { $lte: value } })

// 查詢value1 < key <value2
db.collection.find({ "key" : { $gt: value1 , $lt: value2 } })

// 查詢key <> value
db.collection.find({ "key" : { $ne: value } })

// 條件運算, 相當於key % 10 == 1
db.collection.find({ "key" : { $mod : [ 10 , 1 ] } })

// 不屬於 1,2,3任一
db.collection.find({ "key" : { $nin: [ 1, 2, 3 ] } })

// 屬於 1,2,3任一
db.collection.find({ "key" : { $in: [ 1, 2, 3 ] } })

// $size 值為1
db.collection.find({ "key" : { $size: 1 } })

// 字串 "key"存在則返回true, 反之 false
db.collection.find({ "key" : { $exists : true|false } })

// 正則表示式, "i"忽略大小寫, "m" 可查詢多行
db.collection.find({ "key": /^val.*val$/i })

// $or或 （備註：MongoDB 1.5.3板之後才支援），a=1或者b=2的資料都會列出
db.collection.find({ $or : [{a : 1}, {b : 2} ] })

// 符合條件key=value ，a=1或者b=2的資料都會列出
db.collection.find({ "key": value , $or : [{ a : 1 } , { b : 2 }] })

// 内嵌對象中的值查詢，注意："key.subkey"必須加上引號
db.collection.find({ "key.subkey" :value })
db.collection.find({ "key": { $not : /^val.*val$/i } })

// 1代表升冪排列，-1代表降冪
db.collection.find({}).sort({ "key1" : -1 ,"key2" : 1 })

// 控制返回結果數量，如果參數是0，則當作沒有約束，limit()將不起作用(會對傳入參數求求絕對值)
b.collection.find({}).limit(5)

// 控制返回結果跳過多少數量，如果參數是0，則當作沒有約束，skip()將不起作用，或者說跳過了0條。 （參數不能為負數）可用於分頁，limit是pageSize;skip 是第n頁*pageSize
db.collection.find({}).skip(5)

// 可用來做分頁，跳過5條數據再取5條數據
db.collection.find({}).skip(5).limit(5)
db.collection.find({}).count(true)

// 返回結果集的條數
count()

// 在加入skip()和limit()這兩個操作時，要獲得實際返回的結果數，需要一個參數true，否則返回的是符合查詢條件的結果，而不是數量
db.collection.find({}).skip(5).limit(5).count(true)

// 組合查詢修改刪除
db.users.findAndModify({
    query: {age: {$gte: 25}},
    sort: {age: -1},
    update: {
        $set: {name: 'a2'},
        $inc: {age: 2}
    },
    remove: true
});

// mongodb query範例
// 聚合
// 判斷 最後更新和創造只差一個月 以及一個月內
db.avdata_video.aggregate([
    {
        $addFields:{days:{$divide:[{$subtract: ["$avdata_updated_at","$avdata_created_at"]}, 60 * 60 * 24 * 1000]}}
    },
    {
        $match:{$or:[{days:{$lte:30}}, {avdata_created_at:{$gte:ISODate("2021-07-25T00:00:00.000+08:00")}}]}
    }
]).count();

// 找重複
db.avdata_long_video.aggregate()
      .group({ _id: "$avkey", count: { $sum: 1 } })
      .match({count:{ $gt : 1 }})

// 刪除資料
db.products.remove( { qty: { $gt: 20 } } )
```

# 資料庫指令 使用者

```JavaScript
// 連線進mongodb 設定管理者帳密
mongo mongodb:27019

// 建立使用者，設定賬號，密碼，許可權
// admin資料庫
use admin
db.createUser({ user:"root", pwd:"123456", roles:["root"] })

// 其他資料庫
use test
db.createUser({ user:"admin", pwd:"123456", roles:["readWrite", "dbAdmin"] })
db.createUser( { user: "accountAdmin01",
                 pwd: passwordPrompt(),  // Or  "<cleartext password>"
                 customData: { employeeId: 12345 },
                 roles: [ { role: "clusterAdmin", db: "admin" },
                          { role: "readAnyDatabase", db: "admin" },
                          "readWrite"] },
               { w: "majority" , wtimeout: 5000 } )

// 修改使用者
db.updateUser(用户名，更新，* writeConcern *)
db.updateUser("user123",{pwd: "KNlZmiaNUp0B",customData: { title: "Senior Manager" }})

// 修改mongodb.conf檔案，啟用身份驗證
// vim /etc/mongod.conf
// security:
//   authorization: enabled   # disable or enabled

// 使用者認證
use admin
db.auth("root", "123456")

db.updateUser(user, writeConcern) // 更新使用者
db.dropUser('test') // 刪除使用者

db.auth("name","pass")    //認證使用者，成功會回傳 1
db.dropUser("name")       //刪除使用者
db.system.users.find()    //查詢使用者
```

## mongodb 使用者許可權角色說明

```
規則  說明
root
只在admin資料庫中可用。超級賬號，超級許可權

Read
允許使用者讀取指定資料庫

readWrite
允許使用者讀寫指定資料庫

dbAdmin
允許使用者在指定資料庫中執行管理函式，如索引建立、刪除，檢視統計或訪問system.profile

userAdmin
允許使用者向system.users集合寫入，可以找指定資料庫裡建立、刪除和管理使用者

clusterAdmin
只在admin資料庫中可用，賦予使用者所有分片和複製集相關函式的管理許可權

readAnyDatabase
只在admin資料庫中可用，賦予使用者所有資料庫的讀許可權

readWriteAnyDatabase
只在admin資料庫中可用，賦予使用者所有資料庫的讀寫許可權

userAdminAnyDatabase
只在admin資料庫中可用，賦予使用者所有資料庫的userAdmin許可權

dbAdminAnyDatabase
只在admin資料庫中可用，賦予使用者所有資料庫的dbAdmin許可權
```

