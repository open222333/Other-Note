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
  - [新增資料](#新增資料)
  - [哈希表（Hash）: 來存儲和操作鍵值對的集合](#哈希表hash-來存儲和操作鍵值對的集合)
  - [有序集合（Sorted Set）存儲元素和相對應的分數 並根據分數進行排序和查詢](#有序集合sorted-set存儲元素和相對應的分數-並根據分數進行排序和查詢)
  - [遞增計數](#遞增計數)
  - [Redis Cluster 自動 Failover（官方自動處理）— 基本 Demo](#redis-cluster-自動-failover官方自動處理-基本-demo)
  - [跨兩組 Redis Cluster 的自動 Failover 程式](#跨兩組-redis-cluster-的自動-failover-程式)

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

## 新增資料

```Python
import redis

# 建立 Redis 連線
r = redis.Redis(
    host='localhost',
    port=6379,
    password=None,  # 如果設定密碼請填寫
    decode_responses=True  # 自動轉成字串，避免 bytes
)

# 測試：新增 key/value
result = r.set("test_key", "Hello Redis!")

print("SET 結果:", result)

# 測試：取得資料
value = r.get("test_key")
print("GET 結果:", value)
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

## Redis Cluster 自動 Failover（官方自動處理）— 基本 Demo

前提：使用的是 Redis Cluster（6.x / 7.x），每個 slot 都有 master + replica。

```
若某個 master 掛掉 → 驅動會自動重試、更新 slot mapping
角色升降（failover）後 → 自動切到新的 master
slot redirect (MOVED, ASK) → 自動處理
```

```
pip install redis>=5.0.0
```

```Python
from redis.cluster import RedisCluster
from redis.exceptions import RedisClusterException

def get_cluster_client():
    startup_nodes = [
        {"host": "10.0.0.1", "port": 6379},
        {"host": "10.0.0.2", "port": 6379},
        {"host": "10.0.0.3", "port": 6379},
    ]

    try:
        client = RedisCluster(
            startup_nodes=startup_nodes,
            decode_responses=True,
            read_from_replicas=False,  # 有需要也可 True
            socket_timeout=3,
            reinitialize_steps=5,      # 重新加载 cluster slots 次數
            max_connections=50,
        )
        print("Connected to Redis Cluster")
        return client
    except RedisClusterException as e:
        print("Cluster connection failed:", e)
        return None


if __name__ == "__main__":
    rc = get_cluster_client()

    while True:
        try:
            rc.set("hello", "world")
            print(rc.get("hello"))
        except Exception as e:
            print("Cluster error:", e)
```

## 跨兩組 Redis Cluster 的自動 Failover 程式

```
Cluster A：主要使用
Cluster B：備援（DR）
```

```
自動偵測 A cluster 是否可用

A 掛掉 → 自動切到 B cluster
A 恢復 → 自動切回 A（可選）

自動連線兩邊 cluster
Cluster 錯誤自動 fallback
可選是否 "自動再切回 A"
```

```Python
from redis.cluster import RedisCluster
from redis.exceptions import RedisClusterException, RedisError
import time


class DualClusterRedis:
    def __init__(self, cluster_a_nodes, cluster_b_nodes, auto_failback=True):
        self.cluster_a_nodes = cluster_a_nodes
        self.cluster_b_nodes = cluster_b_nodes
        self.auto_failback = auto_failback

        self.client_a = None
        self.client_b = None
        self.active_client = None

        self.connect_clusters()

    def connect_cluster(self, nodes):
        try:
            client = RedisCluster(
                startup_nodes=nodes,
                decode_responses=True,
                socket_timeout=2,
                max_connections=50
            )
            return client
        except RedisClusterException:
            return None

    def connect_clusters(self):
        print("Trying to connect Cluster A...")
        self.client_a = self.connect_cluster(self.cluster_a_nodes)

        if self.client_a:
            print("Connected to Cluster A")
            self.active_client = self.client_a
            return

        print("Cluster A failed, trying Cluster B...")

        self.client_b = self.connect_cluster(self.cluster_b_nodes)

        if self.client_b:
            print("Connected to Cluster B")
            self.active_client = self.client_b
            return

        print("Both clusters unavailable!!")
        self.active_client = None

    def get_client(self):
        # If using B but A is back, auto failback
        if self.auto_failback and self.active_client == self.client_b:
            test_a = self.connect_cluster(self.cluster_a_nodes)
            if test_a:
                print("Cluster A recovered, switching back.")
                self.client_a = test_a
                self.active_client = self.client_a

        return self.active_client

    def safe_execute(self, func, *args, **kwargs):
        for attempt in range(2):  # retry at most twice (switch cluster)
            client = self.get_client()
            if client is None:
                raise Exception("No Redis Cluster available")

            try:
                return getattr(client, func)(*args, **kwargs)
            except (RedisClusterException, RedisError) as e:
                print(f"Redis error: {e}")
                print("Switching cluster and retrying...")
                # Switch to the other cluster
                if self.active_client == self.client_a:
                    self.active_client = self.client_b
                else:
                    self.active_client = self.client_a

                time.sleep(1)

        raise Exception("Failed on both clusters")


# ====== Example Usage ======
if __name__ == "__main__":
    clusterA_nodes = [
        {"host": "10.0.0.1", "port": 6379},
        {"host": "10.0.0.2", "port": 6379},
    ]

    clusterB_nodes = [
        {"host": "20.0.0.1", "port": 6379},
        {"host": "20.0.0.2", "port": 6379},
    ]

    rc = DualClusterRedis(clusterA_nodes, clusterB_nodes)

    while True:
        try:
            rc.safe_execute("set", "key", "value")
            print(rc.safe_execute("get", "key"))
        except Exception as e:
            print("Fatal:", e)

        time.sleep(2)
```

| 能力                             | 支援                    |
| ------------------------------ | --------------------- |
| Redis Cluster 自動 failover      | ✅（官方）                 |
| 自動偵測 A cluster 掛掉              | ✅                     |
| 自動切換到 B cluster                | ✅                     |
| B 使用期間自動偵測 A 是否復活              | ✅（可選）                 |
| 自動切回 A cluster                 | ✅（auto_failback=True） |
| 支援 MOVED / ASK / slot redirect | ✅（官方）                 |
