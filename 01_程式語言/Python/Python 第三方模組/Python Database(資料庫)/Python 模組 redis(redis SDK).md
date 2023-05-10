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

```Python
# Assuming you run Redis on localhost:6379 (the default)
import redis
r = redis.Redis()
r.ping()

# Running redis on foo.bar.com, port 12345
import redis
r = redis.Redis(host='foo.bar.com', port=12345)
r.ping()

# Another example with foo.bar.com, port 12345
import redis
r = redis.from_url('redis://foo.bar.com:12345')
r.ping()

# redis連線
redis_pool = redis.ConnectionPool(
    host='127.0.0.1',
    port=6739,
    db=10,
	# 以encoding方式解碼，然後返回字串。如果是字串，就根據encoding編碼成bytes
    decode_responses=True
)
redis_client = redis.Redis(connection_pool=redis_pool)
# 如果key不存在 設置key的值 name:value 
# 回傳 1 有進行設置, 0 未進行設置
redis_client.setnx(name, value)

# 若 name 在鍵值內 進行刪除 
if name in redis_client.keys():
	redis_client.delete(name)
```
