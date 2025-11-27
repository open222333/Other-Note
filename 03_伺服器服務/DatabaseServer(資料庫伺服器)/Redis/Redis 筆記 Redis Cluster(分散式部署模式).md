# Redis 筆記 Redis Cluster(集群)

```
Redis Cluster 是 Redis 官方提供的 分散式部署模式，支援：

自動分片（Sharding）
高可用（Primary + Replica）
自動故障轉移
無需 Sentinel（Cluster 自帶容錯）

Node A contains hash slots from 0 to 5500.
Node B contains hash slots from 5501 to 11000.
Node C contains hash slots from 11001 to 16383.

建議的正式環境配置（最常見）
3 個 Master + 3 個 Slave = 6 個節點
```

Redis Cluster 最低要求

| 項目            | 數量      |
| ------------- | ------- |
| **最少 Master** | **3 個** |
| **最少 Slave**  | 0（可選）   |

## 目錄

- [Linux 工具 sample($num)()](#linux-工具-sample$num)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [服務操作](#服務操作)

## 參考資料

[官方文檔 Scale with Redis Cluster](https://redis.io/docs/latest/operate/oss_and_stack/management/scaling/)

[官方文檔 Redis cluster specification](https://redis.io/docs/latest/operate/oss_and_stack/reference/cluster-spec/)

### 心得相關

[Redis Cluster 介紹](https://isdaniel.github.io/redis-cluster-introduce-01/)

# 安裝

## docker-compose 部署

```yml
version: "3.8"

services:

  redis-7001:
    image: redis:7
    container_name: redis-7001
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7001:7001"
    volumes:
      - ./conf/redis/7001:/etc/redis
    networks:
      - redis-cluster-net

  redis-7002:
    image: redis:7
    container_name: redis-7002
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7002:7002"
    volumes:
      - ./conf/redis/7002:/etc/redis
    networks:
      - redis-cluster-net

  redis-7003:
    image: redis:7
    container_name: redis-7003
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7003:7003"
    volumes:
      - ./conf/redis/7003:/etc/redis
    networks:
      - redis-cluster-net

  redis-7004:
    image: redis:7
    container_name: redis-7004
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7004:7004"
    volumes:
      - ./conf/redis/7004:/etc/redis
    networks:
      - redis-cluster-net

  redis-7005:
    image: redis:7
    container_name: redis-7005
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7005:7005"
    volumes:
      - ./conf/redis/7005:/etc/redis
    networks:
      - redis-cluster-net

  redis-7006:
    image: redis:7
    container_name: redis-7006
    command: ["redis-server", "/etc/redis/redis.conf"]
    ports:
      - "7006:7006"
    volumes:
      - ./conf/redis/7006:/etc/redis
    networks:
      - redis-cluster-net

networks:
  redis-cluster-net:
    driver: bridge
```

redis.conf（每個端口一份）

例如：conf/redis/7001/redis.conf

其他 7001/7002/7003/7004/7005/7006 只需要改 port 即可

```conf
# 本 Redis 節點提供服務的主端口
# Redis Cluster 還會自動使用 7001 + 10000 = 17002 作為 cluster bus port
port 7001

# 啟用 Redis Cluster 模式
# 若不啟用則只能當單機 Redis
cluster-enabled yes

# Redis 啟動後會自動生成的 Cluster 拓樸資訊檔案
# 包含 node ID、slot 分配、master/replica、其他節點資訊
# 此檔案請不要手動修改
cluster-config-file nodes.conf

# Cluster 節點通訊的超時時間（毫秒）
# 若節點 5000ms（5 秒）沒有收到 heartbeat 就判定該節點 FAIL
# 也會影響 failover 的速度
cluster-node-timeout 5000

# 啟用 AOF（Append Only File）持久化
# 所有寫入操作都會記錄到 appendonly.aof，以確保資料安全性
appendonly yes

# 允許來自所有 IP 的連線
# 若 Redis 需要被外部或其他容器訪問，通常設為 0.0.0.0
bind 0.0.0.0

# 關閉 Redis 的保護模式
# 在 bind 為 0.0.0.0 時，官方預設會啟動 protected-mode 阻擋外部連線
# 設為 no 表示完全允許外部連線（請確保內網或加上密碼）
protected-mode no
```

# 配置文檔

## Redis Cluster 最小可運作配置

```conf
###############################################
# Redis Cluster 必要設定（最小可運作配置）
###############################################

# Redis 的主要服務端口。
# 每個節點還會自動使用 (port + 10000) 作為 cluster bus port，
# 用來與其他節點進行 gossip/heartbeat。
port 7002


# 啟用 Cluster 模式。
# 若不啟用則此節點只能作為單機 Redis。
cluster-enabled yes


# Cluster 拓樸資訊儲存檔案。
# Redis 啟動後會自動生成此檔案，
# 存放 node ID、slot 分配、角色（master/replica）、其他節點資訊。
# ⚠️ 不可手動修改。
cluster-config-file nodes.conf


# 判斷節點故障的逾時時間（毫秒）。
# 若某個節點在 cluster bus 上 5000ms 沒回應 heartbeat，
# 其他節點就會標記它為 FAIL，並可能觸發 failover。
cluster-node-timeout 5000


# 接受所有來源連線，用於 Docker / 多主機環境。
# 若不設定為 0.0.0.0，其他節點可能無法加入 cluster。
bind 0.0.0.0


# Redis 的保護模式（建議只在內網環境關閉）。
# 若 bind 0.0.0.0 而 protected-mode yes，
# redis 會阻擋外部 IP 連線，導致 Cluster 無法運作。
protected-mode no


###############################################################
# 若 Redis 實際運行 IP 與對外 IP 不同（如 Docker / NAT / Proxy），
# 必須加入 cluster-announce 相關設定！
###############################################################

# 對其他節點宣告本節點的 IP。
# 若在容器內運作，一定要改為主機 IP。
# cluster-announce-ip <你的實際主機 IP>

# 對其他節點宣告對外提供的 port（可省略）。
# cluster-announce-port 7002

# 對其他節點宣告 cluster bus port。
# cluster-announce-bus-port 17002
```

# AOF（Append Only File）

AOF 是 Redis 的一種持久化機制，會把每一條對資料庫的寫入命令（例如 SET、DEL）以 Redis 協議的命令格式 逐行 append 到一個檔案（預設 appendonly.aof）。

當 Redis 重啟時，它會讀這個 AOF，並重放（replay）檔案內的命令來恢復資料。

因為是記錄每個寫命令，所以 AOF 在資料安全（durability）上通常比 RDB（snapshot）更強 — RDB 是周期性 snapshot，可能丟失最近一段時間的變更。


大多生產環境的折衷選擇：appendonly yes + appendfsync everysec（效能與資料安全性平衡）。

如果系統非常要求零資料遺失（金融等場景）可考慮 appendfsync always，但需確保磁碟 I/O 能負擔（並準備承受較高延遲）。

啟用 AOF 時務必、務必搭配監控與磁碟/IO 性能測試（避免磁碟成為瓶頸）。

設定 auto-aof-rewrite-min-size（例如 64mb）以避免在檔案很小時就頻繁 rewrite。

在有 replication（主從）架構下，注意主節點的 AOF 行為會影響整體延遲；從節點通常也可以啟用 AOF（視需求而定）。

與 RDB 的搭配

Redis 可以同時啟用 RDB（snapshot）與 AOF。實務常見策略：

開 appendonly yes（AOF）+ 保留 RDB（快速 snapshot，供某些情境下快速恢復用）

在某些 Redis 版本/配置中，AOF 重寫會使用 RDB preamble 來加速載入（也就是在 AOF 內嵌一段 RDB），不過這是進階設定/版本相關，請視使用的 Redis 版本再確認。

## 優點與缺點（實務考量）

優點

更高的資料完整性（特別是 appendfsync always / everysec 下），重啟時能更完整回復最近的變更。

AOF 可讀性高（是 Redis 命令序列），方便排查。

缺點

檔案通常比 RDB 大（記錄每一條命令），需要更多磁碟空間。

寫入量大時，磁碟 I/O 負擔顯著（特別是 always 模式）。

AOF 重放（重啟恢復）速度可能比 RDB 慢（因為要重放許多命令）。

若磁碟效能差，會影響整體延遲/吞吐。

## 主要設定（常見項目）與含意

```conf
###############################################
# AOF（Append Only File）持久化設定
###############################################

# 是否啟用 AOF 持久化機制。
# yes  = 啟用（推薦大多數生產環境）。
# no   = 關閉（僅依 RDB snapshot 持久化）。
# AOF 會將每一條寫入操作（如：SET、DEL）依次追加到 appendonly.aof。
appendonly yes


# AOF 檔案名稱。預設為 appendonly.aof。
# 可以依需要修改名稱，例如 appendonly-7002.aof。
appendfilename "appendonly.aof"


# AOF 同步模式（非常重要，影響效能與安全）：
#   always   = 每次寫入都 fsync，最安全但最慢（高 I/O 壓力）。
#   everysec = 每秒 fsync 一次（預設值），效能與安全折衷，建議使用。
#   no       = 不 fsync，交給作業系統決定。效能最高但可能丟資料。
appendfsync everysec


# 在執行 AOF 重寫（BGREWRITEAOF）期間，是否暫停 fsync。
#   yes = 在 rewrite 時暫停 fsync（效能較高，但若當機資料風險增加）。
#   no  = 即使在 rewrite 也照常 fsync（預設，資料更安全）。
no-appendfsync-on-rewrite no


# 自動 AOF 重寫觸發條件（減少 AOF 檔案大小用）：
# 當目前 AOF 大小 >= 上次重寫後大小 * (1 + 此百分比/100)，就會觸發 rewrite。
# 預設為 100（表示至少比原大小增長 100% 時重寫）。
auto-aof-rewrite-percentage 100


# 自動觸發重寫的最小 AOF 檔案大小。
# 避免在 AOF 檔案很小（如小於 64MB）時就重寫，造成不必要的 I/O。
auto-aof-rewrite-min-size 64mb


# 啟用 AOF 檔案損壞（crash）時自動檢查修復。
# 建議保留預設 yes，可在 Redis 啟動時自動修補最後不完整的命令。
aof-load-truncated yes


# Redis 4.0 之後，AOF rewrite 會自動使用 RDB preamble 加速恢復。
# 若設為 yes，重啟時會先載入 RDB 片段，然後 replay AOF incremental 的部分。
# 建議保持預設（通常為 yes）。
aof-use-rdb-preamble yes
```

# 支援跨叢集 (multi-cluster) HA／跨 cluster 自動 failover

```
Redis Cluster 官方定義：Cluster 的 HA 是「節點級別／分片級別」，不是「多 cluster 切換」

在官方說明中，Redis Cluster 的重點是「資料分片 + 多節點 + replica + slot-sharding + node-level failover」。
也就是「當 cluster 中某些節點／主節點 (master) 故障，cluster 可自動由其 replica 接管 (failover)」。

官方 scaling/operate 文檔有明確說明：Redis Cluster 可以「在某些節點失敗或無法通訊時繼續作業 (continue operations)」。
但也指出「如果大多數 master 都不可用，cluster 將無法繼續運作」——也就是說，Cluster 的高可用性侷限於 同一 cluster 範圍內。

換句話說，Redis Cluster 的 HA 是 node-level / shard-level，而不是 cluster-level（multi-cluster）。
沒有官方機制能讓 client 在「Cluster A 全部 down → 自動切到 Cluster B」。

因此，Redis 官方並沒有「cluster 間 failover / HA」的定義或支援。
```

| 項目                                       | Redis Cluster (官方) 支援 | 不支援 / 未定義                          |
| ---------------------------------------- | --------------------- | ---------------------------------- |
| 資料分片 (Sharding)                          | ✅ 支援                  | —                                  |
| 節點 (node) 故障 failover (master → replica) | ✅ 支援                  | —                                  |
| 多 node／分片容錯 (部分節點故障)                     | ✅ 支援 (只要大部分主節點存活)     | 如果大多主節點 down 就不可用 ([Redis][1])     |
| 「跨 cluster (多 cluster) 備援 / 自動切換 / HA」   | **✖ 不支援 (官方沒有這層機制)**  | 需要由 client / proxy / 自己實作 fallback |

