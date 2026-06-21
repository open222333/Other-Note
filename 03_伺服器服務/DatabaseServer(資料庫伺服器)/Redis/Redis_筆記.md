# Redis 筆記

## 目錄

- [Redis 筆記](#redis-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [GUI相關](#gui相關)
    - [安裝步驟 CentOS7](#安裝步驟-centos7)
    - [安裝步驟 Mac](#安裝步驟-mac)
    - [相關筆記](#相關筆記)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
- [配置](#配置)
  - [實際](#實際)
  - [Mac](#mac)
- [配置文件](#配置文件)
- [指令](#指令)
  - [遠端測試](#遠端測試)
  - [搜尋](#搜尋)
    - [KEYS（簡單搜尋，不建議用於正式環境）](#keys簡單搜尋不建議用於正式環境)
    - [SCAN（推薦，不阻塞）](#scan推薦不阻塞)
    - [查詢 Key 資訊](#查詢-key-資訊)
  - [記憶體限制](#記憶體限制)
  - [用量分析](#用量分析)
    - [整體記憶體使用](#整體記憶體使用)
    - [整體狀態](#整體狀態)
    - [Key 數量](#key-數量)
    - [找出佔空間最大的 Key](#找出佔空間最大的-key)
    - [慢查詢分析](#慢查詢分析)
    - [即時監控](#即時監控)
    - [快速健康檢查](#快速健康檢查)

## 參考資料

[官方網站](https://redis.io/)

[官方文檔](https://redis.io/documentation)

[官方文檔 redis 配置 設定檔](https://redis.io/topics/config)

[官方文檔 教學](https://www.tutorialspoint.com/redis/index.htm)

### GUI相關

[Redis GUI工具](https://tableplus.com/)

[Redis GUI工具 (下載點在下方介紹)](https://github.com/qishibo/AnotherRedisDesktopManager)

[Redis GUI工具 (Mac)](https://github.com/qishibo/AnotherRedisDesktopManager/releases)

### 安裝步驟 CentOS7

[Centos安裝與配置Redis](https://iter01.com/569100.html)

[Redis系列 - 環境建置篇](https://jed1978.github.io/2018/05/02/Redis-Environment-Installation-Configuration.html)

### 安裝步驟 Mac

[Install Redis on macOS](https://redis.io/docs/getting-started/installation/install-redis-on-mac-os/)

### 相關筆記

[Redis - Cluster 分散式](./Redis_筆記_Redis_Cluster(分散式部署模式).md)
[Redis - Replication 主從](./Redis_筆記_Redis_Replication(Master–Slave_主從複製).md)
[Redis - Sentinel 哨兵 HA](./Redis_筆記_Redis_Sentinel(哨兵(高可用性)).md)
[Redis - Keepalived HA](./Redis_筆記_Redis_Keepalived(HA工具).md)
[Redis - Predixy Proxy 代理層](./Redis_筆記_Redis_Predixy(高效的_Redis_Proxy(代理層)).md)
[Redis - Codis 分散式叢集](./Redis_筆記_Codis(分散式_Redis_叢集方案).md)

# 安裝

## Debian (Ubuntu)

```bash
apt update
```

```sh
apt install redis-server -y
```

## RedHat (CentOS)

```bash
# 新增EPEL倉庫並更新yum源
yum install epel-release -y
yum update -y

# 安裝Redis資料庫
yum -y install redis
```

# 配置

`設定檔`

```bash
vim /etc/redis.conf
```

```sh
vim /etc/redis/redis.conf
```

```conf
# ── 網路 ──────────────────────────────────────────────
# 127.0.0.1 -::1    ：僅本機（預設，最安全）
# 0.0.0.0 -::*      ：允許所有 IPv4 + IPv6（需搭配防火牆或密碼保護）
# -::* 前綴的 - 表示 IPv6 不可用時不中止啟動
bind 0.0.0.0 -::*

# 監聽 Port
port 6379

# 高並發時建議提高（同步調整 /proc/sys/net/core/somaxconn）
tcp-backlog 511

# 閒置超時（秒），0 = 永不超時
timeout 0

# TCP keepalive，定期偵測並清除死連線
tcp-keepalive 300

# ── 安全 ──────────────────────────────────────────────
# 保護模式：bind 非 127.0.0.1 且無密碼時自動拒絕外部連線
# 已設密碼且有防火牆保護時可關閉
protected-mode no

# 連線密碼
requirepass your_password

# ── 一般 ──────────────────────────────────────────────
# 背景執行（由 systemd 管理時設 no）
daemonize yes

# 日誌等級：debug / verbose / notice / warning
loglevel notice
logfile /var/log/redis/redis-server.log

# 資料庫數量
databases 16

# ── 持久化（RDB 快照）────────────────────────────────
# save ""：關閉自動快照（純快取用途）
# 預設觸發條件（取消 # 啟用）：
# save 3600 1      # 3600 秒內有 ≥1 個 key 變更
# save 300 100     # 300 秒內有 ≥100 個 key 變更
# save 60 10000    # 60 秒內有 ≥10000 個 key 變更
save ""

# RDB 儲存失敗時停止接受寫入（確保資料不靜默遺失）
stop-writes-on-bgsave-error yes

# RDB 壓縮與完整性校驗
rdbcompression yes
rdbchecksum yes

dbfilename dump.rdb
dir /var/lib/redis

# ── 記憶體 ────────────────────────────────────────────
# 記憶體上限（依主機可用量調整，例如 4gb）
# maxmemory 4gb

# 淘汰策略（超過 maxmemory 時的行為，取消 # 並選擇一種）
# allkeys-lru  ：快取用途，淘汰最久未使用的 key（最常用）
# noeviction   ：Session / Queue，超限時拒絕寫入
# volatile-lru ：僅淘汰有設 TTL 的 key
# maxmemory-policy allkeys-lru
```

## 實際

```conf
bind 0.0.0.0 -::*
protected-mode no
requirepass your_password
timeout 10
save ""
```

## Mac

```bash
# 安裝
brew install redis

# 測試 Redis 安裝
# 如果成功，將看到 Redis 的啟動日誌，並且 Redis 將在前台運行。
# 要停止 Redis，請輸入 Ctrl-C。
redis-server

# 後台啟動進程 啟動 Redis 並在登錄時重新啟動它。
brew services start redis

# 檢查已啟動的託管 Redis 的狀態
# 如果服務正在運行:
# redis (homebrew.mxcl.redis)
# Running: ✔
# Loaded: ✔
# User: miranda
# PID: 67975
brew services info redis

# 停止服務
brew services stop redis

# 重啟
brew services restart redis

# 進入 redis
redis-cli
```

# 配置文件

`/etc/redis/redis.conf`

```conf
# 註解或改成 0.0.0.0
bind 127.0.0.1
bind 0.0.0.0

bind 127.0.0.1 -::1
# Redis 支援 IPv4 和 IPv6
bind 0.0.0.0 ::1

port 6379

# 修改密碼 取消註解並改成想設定的密碼
# requirepass foobared
requirepass pwd

# 閒置連線自動關閉時間（秒）（通常預設為 0，代表永不超時）
# timeout 0
timeout 10

# 關閉永久化
# 停用自動快照（RDB）持久化功能。
# 也就是說 Redis 不會自動將資料寫入 dump.rdb 檔案，除非手動執行 SAVE 或 BGSAVE 指令。
save ""

# 預設的 Redis 會定期自動保存資料 會在 /var/lib/redis/dump.rdb（或容器的 /data/dump.rdb）自動產生快照
save 900 1    # 若 900 秒內有至少 1 個 key 被修改，則儲存一次
save 300 10   # 若 300 秒內有至少 10 個 key 被修改，則儲存一次
save 60 10000 # 若 60 秒內有至少 10000 個 key 被修改，則儲存一次
```

# 指令

```sh
# 啟動服務
systemctl start redis
# 開機啟動
systemctl enable redis
systemctl enable redis-server
# 查詢啟動狀態
systemctl status redis
# 重啟
systemctl restart redis
systemctl restart redis-server
# 停止
systemctl stop redis
```

## 遠端測試

```sh
redis-cli -h <Redis-server-IP> -p 6379
```

## 搜尋

### KEYS（簡單搜尋，不建議用於正式環境）

```sh
# 列出所有 key（資料量大時會阻塞）
KEYS *

# 萬用字元搜尋
KEYS user:*          # 前綴符合
KEYS *:session       # 後綴符合
KEYS user:*:token    # 中間萬用

# 單字元萬用
KEYS user:?          # ? 代表一個字元
```

### SCAN（推薦，不阻塞）

```sh
# 基本用法：cursor 從 0 開始，返回值第一個是下一個 cursor，0 表示結束
SCAN 0

# 加上 MATCH 過濾（仍會掃全部，只是過濾回傳結果）
SCAN 0 MATCH user:* COUNT 100

# COUNT 是每次掃描的數量提示，不是返回數量上限
# 重複執行直到 cursor 回到 0，才算掃完所有 key

# 範例：用 redis-cli 一次掃完所有符合的 key
redis-cli --scan --pattern "user:*"
```

### 查詢 Key 資訊

```sh
# 判斷 key 是否存在
EXISTS user:123

# 查看 key 的資料類型
TYPE user:123

# 查看 TTL（-1 = 永不過期，-2 = 不存在）
TTL user:123
PTTL user:123    # 毫秒

# 查看 key 總數（O(1)，不掃描）
DBSIZE
```

## 記憶體限制

### redis.conf 設定

```conf
# 記憶體上限
maxmemory 2gb

# 超過上限時的淘汰策略
maxmemory-policy allkeys-lru
```

| 策略 | 說明 |
|------|------|
| `allkeys-lru` | 刪除最久未使用的 key，對象是全部 key（快取用途首選） |
| `volatile-lru` | 只刪有設 TTL 的 key（LRU 順序） |
| `allkeys-random` | 隨機刪除任意 key |
| `volatile-random` | 隨機刪除有 TTL 的 key |
| `volatile-ttl` | 刪除最快過期的 key |
| `noeviction` | 不刪除，記憶體滿時寫入回傳錯誤（Session / Queue 用途） |

**allkeys-lru 說明**

`allkeys` — 對象是全部的 key（不管有沒有設過期時間）

`lru` — Least Recently Used，最久沒被存取的先刪

適合 Redis 作為**快取（Cache）** 使用：熱門資料持續被存取不會被刪，冷門資料自然淘汰，不需對每個 key 手動設 TTL。

### 即時生效（不重啟）

`CONFIG SET 只修改記憶體中的設定，重啟後會失效`

```bash
redis-cli CONFIG SET maxmemory 2gb
redis-cli CONFIG SET maxmemory-policy allkeys-lru
```

### 確認設定

```bash
redis-cli CONFIG GET maxmemory
redis-cli CONFIG GET maxmemory-policy
```

`合併一行查看`

```bash
redis-cli CONFIG GET maxmemory*
```

輸出範例：

```
1) "maxmemory-policy"
2) "noeviction"
3) "maxmemory-samples"
4) "5"
5) "maxmemory"
6) "2147483648"
```

### 回寫設定檔（重啟後仍生效）

`前提：Redis 啟動時有指定設定檔路徑`

```bash
redis-cli CONFIG REWRITE
```

## 用量分析

### 整體記憶體使用

```bash
redis-cli INFO memory
```

| 欄位 | 說明 |
|------|------|
| `used_memory_human` | 目前使用記憶體 |
| `used_memory_peak_human` | 歷史最高用量 |
| `mem_fragmentation_ratio` | 碎片率，> 1.5 代表碎片嚴重 |

### 整體狀態

```bash
redis-cli INFO all
```

`分區段查看`

```bash
redis-cli INFO stats        # 命中率、連線數
redis-cli INFO replication  # 主從狀態
redis-cli INFO clients      # 目前連線數
```

### Key 數量

```bash
redis-cli DBSIZE        # 目前 DB 的 key 總數
redis-cli INFO keyspace # 各 DB 的 key 數與過期數
```

### 找出佔空間最大的 Key

```bash
redis-cli --bigkeys
```

會掃描全部 key，找出各型別中最大的，生產環境建議離峰執行。

### 慢查詢分析

```bash
redis-cli SLOWLOG GET 10  # 查看最近 10 筆慢查詢
redis-cli SLOWLOG LEN     # 慢查詢總數
redis-cli SLOWLOG RESET   # 清除記錄
```

### 即時監控

```bash
redis-cli MONITOR   # 即時顯示所有指令（高流量慎用）
redis-cli --stat    # 每秒刷新基本統計
redis-cli --latency # 延遲監控
```

### 快速健康檢查

```bash
redis-cli INFO memory    # 記憶體是否快滿
redis-cli INFO keyspace  # key 數量是否異常
redis-cli --bigkeys      # 有無超大 key
redis-cli SLOWLOG GET 10 # 有無慢查詢
```
