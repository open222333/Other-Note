# Python 模組 redis(redis SDK)

```
```

## 目錄

- [Python 模組 redis(redis SDK)](#python-模組-redisredis-sdk)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [GUI工具相關](#gui工具相關)
		- [教學相關](#教學相關)
- [指令](#指令)
- [用法](#用法)
	- [基礎用法](#基礎用法)
	- [哈希表（Hash）: 來存儲和操作鍵值對的集合](#哈希表hash-來存儲和操作鍵值對的集合)
	- [有序集合（Sorted Set）存儲元素和相對應的分數 並根據分數進行排序和查詢](#有序集合sorted-set存儲元素和相對應的分數-並根據分數進行排序和查詢)
	- [遞增計數](#遞增計數)

## 參考資料

[redis pypi](https://pypi.org/project/redis/)

[官方文檔](https://redis-py.readthedocs.io/en/stable/#)

### GUI工具相關

[Redis GUI工具](https://tableplus.com/)

[Redis GUI工具 (下載點在下方介紹)](https://github.com/qishibo/AnotherRedisDesktopManager)

[Redis GUI工具 (Mac)](https://github.com/qishibo/AnotherRedisDesktopManager/releases)

### 教學相關

[Python redis 使用介绍](https://www.runoob.com/w3cnote/python-redis-intro.html)

# 指令

```bash
# 安裝
pip install redis
```

# 用法

## 基礎用法

```Python
import redis

# 建立 Redis 連線
r = redis.Redis(host='localhost', port=6379, db=0)

# 或者使用 Redis URL 連線
r = redis.from_url('redis://localhost:6379/0')

# 設定鍵值對
r.set('key', 'value')

# 獲取鍵對應的值
value = r.get('key')

# 刪除單個鍵
r.delete('key')

# 刪除多個鍵
r.delete('key1', 'key2', 'key3')

# 檢查鍵是否存在
exists = r.exists('key')

# 在列表尾部插入元素
r.rpush('mylist', 'element1', 'element2', 'element3')

# 獲取列表範圍內的元素
elements = r.lrange('mylist', 0, -1)

# 添加元素到集合
r.sadd('myset', 'element1', 'element2', 'element3')

# 獲取集合中的所有元素
members = r.smembers('myset')

# setnx方法 如果key不存在 設置key的值 key:value 回傳 1 有進行設置, 0 未進行設置
r.setnx(key, value)
```

## 哈希表（Hash）: 來存儲和操作鍵值對的集合

```Python
import redis

# 建立 Redis 連線
r = redis.Redis(host='localhost', port=6379, db=0)

### 設定哈希表的鍵值對
# 使用 hset 方法設定單個鍵值對
r.hset('myhash', 'field1', 'value1')

# 使用 hmset 方法設定多個鍵值對
r.hmset('myhash', {'field2': 'value2', 'field3': 'value3'})

### 獲取哈希表中的值
# 使用 hget 方法獲取單個鍵的值
value1 = r.hget('myhash', 'field1')

# 使用 hmget 方法獲取多個鍵的值
values = r.hmget('myhash', 'field1', 'field2', 'field3')

# 使用 hgetall 方法獲取哈希表的所有鍵值對
all_data = r.hgetall('myhash')

# 使用 hkeys 方法獲取哈希表的所有鍵
keys = r.hkeys('myhash')

# 使用 hvals 方法獲取哈希表的所有值
values = r.hvals('myhash')

### 檢查哈希表中的鍵是否存在
# 使用 hexists 方法檢查鍵是否存在
exists = r.hexists('myhash', 'field1')

### 刪除哈希表中的鍵
# 使用 hdel 方法刪除單個鍵
r.hdel('myhash', 'field1')

# 使用 hdel 方法刪除多個鍵
r.hdel('myhash', 'field1', 'field2', 'field3')
```

## 有序集合（Sorted Set）存儲元素和相對應的分數 並根據分數進行排序和查詢

```Python
import redis

# 建立 Redis 連線
r = redis.Redis(host='localhost', port=6379, db=0)

### 新增元素到有序集合
# 使用 zadd 方法新增元素到有序集合
r.zadd('myzset', {'member1': 1, 'member2': 2, 'member3': 3})

### 獲取有序集合中的元素
# 使用 zrange 方法按照索引範圍獲取有序集合的元素
members = r.zrange('myzset', 0, -1)

# 使用 zrevrange 方法按照索引範圍獲取有序集合的元素（逆序）
members = r.zrevrange('myzset', 0, -1)

### 獲取有序集合中的元素和分數
# 使用 zrange 方法獲取有序集合的元素和分數
data = r.zrange('myzset', 0, -1, withscores=True)

# 使用 zrevrange 方法獲取有序集合的元素和分數（逆序）
data = r.zrevrange('myzset', 0, -1, withscores=True)

### 根據分數範圍獲取有序集合的元素
# 使用 zrangebyscore 方法根據分數範圍獲取有序集合的元素
members = r.zrangebyscore('myzset', min_score, max_score)

# 使用 zrevrangebyscore 方法根據分數範圍獲取有序集合的元素（逆序）
members = r.zrevrangebyscore('myzset', max_score, min_score)

### 刪除有序集合中的元素
# 使用 zrem 方法刪除有序集合中的元素
r.zrem('myzset', 'member1', 'member2', 'member3')
```

## 遞增計數

```Python
import redis

'''
使用 SET 命令將計數器的初始值設為 0，然後使用 INCR 命令遞增計數器的值，最後使用 GET 命令獲取計數器的當前值。
在 Python 的 redis 套件中，INCR 命令對應的函式是 incr()。

可以根據需求多次調用 incr() 函式來進行遞增操作。
同樣地，也可以使用 DECR 命令和 decr() 函式來執行遞減操作。

請注意，計數器的值是以字串形式存儲在 Redis 中，所以在使用計數器的值時，可能需要進行類型轉換。
'''

# 建立 Redis 連線
r = redis.Redis(host='localhost', port=6379, db=0)

# 初始化計數器
r.set('counter', 0)

# 遞增計數器的值
r.incr('counter')

# 獲取計數器的當前值
value = r.get('counter')

print(value)  # 輸出結果為 b'1'
```