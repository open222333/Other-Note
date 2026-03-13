# Redis 筆記 Redis Replication(Master–Slave 主從複製)

```
Redis replication (Master–Slave) 是最基本的複製架構：

Master（主）：可以讀 & 寫
Slave（從）：只能讀（read-only）
Slave 會持續複製 Master 的資料，保持同步


Slave 主要用來：

分擔讀取壓力（read scaling）
做資料備援
當 Master 掛掉時，可由 sentinel / cluster 接管（Master-Slave 本身無自動切換）
```

常見架構

```
      Write
   Client → Master
               ↓ Replication
             Slave 1 (Read)
             Slave 2 (Read)
```

## 目錄

- [Redis 筆記 Redis Replication(Master–Slave 主從複製)](#redis-筆記-redis-replicationmasterslave-主從複製)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [配置](#配置)
  - [slave 的設定檔](#slave-的設定檔)

## 參考資料

[官方文檔 Redis replication](https://redis.io/docs/latest/operate/oss_and_stack/management/replication/)

### 心得相關

[Redis Master-Slave](https://stevenitlife.blogspot.com/2018/09/redis-master-slave.html)

# 配置

## slave 的設定檔

預設的設定檔

```
/etc/redis/redis.conf
```

複製一份成 redis-slave.conf

修改裡面的內容如下:

```conf
; port 是 slave redis 要傾聽的 port 號
; master 已經使用了預設的 6379
; 這裡改成 6380 避免衝突
port 6380

; 指定 pidfile
; UNIX / Linux 的習慣
; 許多程式執行起來後會產生一個 pid 檔
; 如果要把這個程式停掉，可以刪除 pid 檔
; 程式就會停止了
pidfile /var/run/redis_6380.pid

; dir 是運行過程中的 dump 檔要存在那個目錄
dir ./slave

; slaveof 指出這個 slave 要跟隨那一個 master
; 這裡指向 port 6379 的那一個 Redis
slaveof 127.0.0.1 6379


; 設定redis master
slaveof {redis-master ip} {port}

; 只讀
slave-read-only yes

; 關閉永久化
save ""
```

```bash
# 重啟redis-slave
redis-server restart
# 檢查redis master-slave
redis-cli
info replicationÏ
```
