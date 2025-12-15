# HAProxy(High Availability Proxy) Redis部分

```
HAProxy(High Availability Proxy) 是一款非常流行的 負載平衡器（Load Balancer）與反向代理（Reverse Proxy）。

主要用於：

    在多台後端伺服器之間分散流量
    提供高可用性（High Availability）
    SSL/TLS 終結（SSL termination）
    健康檢查（Health checks）
    高性能的 L4/L7 負載平衡
    支援 TCP、HTTP、HTTPS、gRPC 等協議
```

## 目錄

- [HAProxy(High Availability Proxy) Redis部分](#haproxyhigh-availability-proxy-redis部分)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [Windows](#windows)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
    - [可自行加 Redis 後端](#可自行加-redis-後端)
    - [負責 Redis 負載平衡與健康檢查](#負責-redis-負載平衡與健康檢查)
    - [stats UI（方便觀察）](#stats-ui方便觀察)
- [指令](#指令)
  - [測試 redis](#測試-redis)
    - [檢查 HAProxy 前端是否可回應](#檢查-haproxy-前端是否可回應)
    - [寫入透過 HAProxy（6379） 讀取透過 HAProxy（6379）](#寫入透過-haproxy6379-讀取透過-haproxy6379)
    - [測試權重](#測試權重)

## 參考資料

[haproxy 官方網站](https://www.haproxy.com/blog/haproxy-advanced-redis-health-check)

# 安裝

## docker-compose 部署

```yml
version: "3.9"

services:
  haproxy:
    image: haproxy:2.8
    container_name: haproxy
    ports:
      - "80:80"      # HTTP
      - "443:443"    # HTTPS，如果需要的話
    volumes:
      - ./haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg:ro
    restart: unless-stopped
```

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

```bash
```

## Homebrew (MacOS)

```bash
```

## Windows

```
```

## 配置文檔

haproxy.cfg 最後一行缺少換行符 (LF)，HAProxy 會把這視為「檔案被截斷（truncated）」而拒絕啟動。

這是 非常常見 的錯誤，通常發生在用編輯器建立最後一行後沒有按 Enter。

通常在 ``

### 基本範例

```conf
global
    log /dev/log local0
    maxconn 4096
    daemon

defaults
    mode http
    log global
    option httplog
    option dontlognull
    timeout connect 5s
    timeout client  50s
    timeout server  50s

frontend http_front
    bind *:80
    default_backend http_back

backend http_back
    balance roundrobin
    server web1 192.168.0.11:80 check
    server web2 192.168.0.12:80 check
```

### 可自行加 Redis 後端

haproxy.cfg

```conf
# ==============================================
# 全域配置 (Global)
# ==============================================
global
    log stdout format raw local0   # 日誌輸出到 stdout，格式為 raw，方便 Docker 查看
    maxconn 4096                   # 最大同時連線數限制 4096

# ==============================================
# 預設配置 (Defaults)
# ==============================================
defaults
    log global                     # 使用 global 定義的日誌設定
    mode tcp                        # 運行在 TCP 模式，適合 Redis
    option dontlognull              # 不記錄空連線日誌
    timeout connect 5000ms          # 與後端建立連線超時 5 秒
    timeout client 50000ms          # 與客戶端連線空閒超時 50 秒
    timeout server 50000ms          # 與後端連線空閒超時 50 秒

# ==============================================
# 前端配置 (Frontend)
# ==============================================
frontend redis_front
    bind *:6379                     # HAProxy 監聽本機所有 IP 的 Redis 標準端口 6379
    default_backend redis_back      # 將所有請求導向 redis_back 後端

# ==============================================
# 後端配置 (Backend)
# ==============================================
backend redis_back
    balance roundrobin              # 使用 round-robin 負載平衡策略
    # Redis 節點列表，可自行修改為你的 Redis 主機
    server redis1 192.168.1.100:6379 check  # 後端 Redis 節點 redis1，每次請求會檢查健康狀態
    server redis2 192.168.1.101:6379 check  # 後端 Redis 節點 redis2，每次請求會檢查健康狀態

```

### 負責 Redis 負載平衡與健康檢查

```conf
# ==============================================
# 預設配置（defaults）
# ==============================================
defaults REDIS
    mode tcp                 # 使用 TCP 模式，Redis 是 TCP 協議
    timeout connect 4s       # 與後端建立連線的超時時間 4 秒
    timeout server 30s       # 與後端連線空閒超時 30 秒
    timeout client 30s       # 與客戶端連線空閒超時 30 秒

# ==============================================
# 前端配置（Frontend）
# ==============================================
frontend ft_redis
    bind 10.0.0.1:6379 name redis  # HAProxy 監聽本機 IP 10.0.0.1 的 Redis 端口
    default_backend bk_redis       # 所有請求導向名為 bk_redis 的後端

# ==============================================
# 後端配置（Backend）
# ==============================================
backend bk_redis
    option tcp-check               # 啟用 TCP 健康檢查
    # 以下是 TCP 健康檢查步驟：
    tcp-check send PING\r\n        # 發送 PING 命令
    tcp-check expect string +PONG  # 期望返回 +PONG
    tcp-check send info replication\r\n  # 發送 info replication 命令
    tcp-check expect string role:master   # 期望返回 role:master，確保是 master 節點
    tcp-check send QUIT\r\n        # 發送 QUIT 命令結束檢查
    tcp-check expect string +OK    # 期望返回 +OK
    # 定義 Redis 後端節點，並設定權重
    server R1 10.0.0.11:6379 weight 80 check inter 1s  # Redis 節點 R1，權重 80
    server R2 10.0.0.12:6379 weight 20 check inter 1s  # Redis 節點 R2，權重 20

```

### stats UI（方便觀察）

```conf
# ==============================================
# stats UI（方便觀察）
# ==============================================
listen stats
    bind *:8404
    mode http
    stats enable
    stats uri /
    stats refresh 3s
```

Stats UI 中重要欄位

| 欄位                    | 意義                       |
| --------------------- | ------------------------ |
| **Wght**              | 設定的權重（80 / 20）           |
| **Act**               | 是否 active                |
| **Chk**               | 健康檢查狀況                   |
| **Last**              | 最後一次的 Health Check 結果    |
| **Cur / Max / Limit** | 目前連線數、最大連線數、限制           |
| **EReq**              | 失敗的 requests             |
| **Econ**              | 連線錯誤                     |
| **Eresp**             | 回應錯誤                     |
| **Req**               | HAProxy 傳給後端的總 request 數 |


# 指令

啟動／重新載入 HAProxy

```sh
systemctl restart haproxy
systemctl reload haproxy
```

檢查設定是否正確

```sh
haproxy -c -f /etc/haproxy/haproxy.cfg
```

## 測試 redis

```sh
docker network create my_shared_net
```

HAProxy 部分

```yml
version: "3.9"

services:
  haproxy:
    image: haproxy:2.8
    container_name: haproxy-redis
    hostname: haproxy-redis
    ports:
      - "6379:6379"
    networks:
      - my_shared_net
    volumes:
      - ./conf/haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg:ro
    restart: unless-stopped

networks:
  my_shared_net:
    external: true
```

```conf
# ==============================================
# 預設配置（defaults）
# ==============================================
defaults REDIS
    mode tcp                 # 使用 TCP 模式，Redis 是 TCP 協議
    timeout connect 4s       # 與後端建立連線的超時時間 4 秒
    timeout server 30s       # 與後端連線空閒超時 30 秒
    timeout client 30s       # 與客戶端連線空閒超時 30 秒

# ==============================================
# 前端配置（Frontend）
# ==============================================
frontend ft_redis
    # bind 10.0.0.1:6379 name redis  # HAProxy 監聽本機 IP 10.0.0.1 的 Redis 端口
    bind *:6379
    default_backend bk_redis       # 所有請求導向名為 bk_redis 的後端

# ==============================================
# 後端配置（Backend）
# ==============================================
backend bk_redis
    option tcp-check               # 啟用 TCP 健康檢查
    # 以下是 TCP 健康檢查步驟：
    tcp-check send PING\r\n        # 發送 PING 命令
    tcp-check expect string +PONG  # 期望返回 +PONG
    # tcp-check send info replication\r\n  # 發送 info replication 命令
    # tcp-check expect string role:master   # 期望返回 role:master，確保是 master 節點
    tcp-check send QUIT\r\n        # 發送 QUIT 命令結束檢查
    tcp-check expect string +OK    # 期望返回 +OK
    # 定義 Redis 後端節點，並設定權重
    server R1 redis1:6379 weight 80 check inter 1s  # Redis 節點 R1，權重 80
    server R2 redis2:6379 weight 20 check inter 1s  # Redis 節點 R2，權重 20

# ==============================================
# stats UI（方便觀察）
# ==============================================
listen stats
    bind *:8404
    mode http
    stats enable
    stats uri /
    stats refresh 3s

```

Redis 部分

```yml
version: "3.9"

services:
  redis1:
    image: redis:7
    container_name: redis1
    hostname: redis1
    networks:
      - my_shared_net
    ports:
      - "6380:6379"
    volumes:
      - ./data/redis1_data:/data
    command: ["redis-server"]

  redis2:
    image: redis:7
    container_name: redis2
    hostname: redis2
    networks:
      - my_shared_net
    ports:
      - "6381:6379"
    volumes:
      - ./data/redis2_data:/data
    command: ["redis-server"]

networks:
  my_shared_net:
    external: true
```

### 檢查 HAProxy 前端是否可回應

```sh
redis-cli -h 127.0.0.1 -p 6379 ping
```

### 寫入透過 HAProxy（6379） 讀取透過 HAProxy（6379）

```sh
redis-cli -p 6379 set who_am_i 123
redis-cli -p 6379 get who_am_i
redis-cli -p 6379 get who_am_i
redis-cli -p 6379 get who_am_i
```

### 測試權重

兩台 Redis 放不同的 prefix

```sh
for i in {1..100}; do redis-cli -p 6380 set r1_key_$i "from R1"; done
for i in {1..100}; do redis-cli -p 6381 set r2_key_$i "from R2"; done
```

使用 HAProxy（6379）大量 GET 觸發分流

```sh
for i in {1..1000}; do redis-cli -p 6379 get r1_key_$i > /dev/null 2>&1; done
for i in {1..1000}; do redis-cli -p 6379 get r2_key_$i > /dev/null 2>&1; done
```
