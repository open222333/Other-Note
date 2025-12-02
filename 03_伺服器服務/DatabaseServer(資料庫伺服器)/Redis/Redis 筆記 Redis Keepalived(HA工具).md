# Redis 筆記 Redis Keepalived(HA工具)

## 目錄

- [Redis 筆記 Redis Keepalived(HA工具)](#redis-筆記-redis-keepalivedha工具)
  - [目錄](#目錄)
- [參考資料](#參考資料)
    - [心得相關](#心得相關)
- [範例](#範例)
  - [Keepalived + Redis Sentinel（推薦）](#keepalived--redis-sentinel推薦)
  - [Keepalived + 自訂 Redis 健康檢查腳本, Minimal HA](#keepalived--自訂-redis-健康檢查腳本-minimal-ha)
    - [Keepalived 主機設定（MASTER 範例）](#keepalived-主機設定master-範例)
    - [Keepalived 備援機設定（BACKUP 範例）](#keepalived-備援機設定backup-範例)
    - [確認正常運作](#確認正常運作)
  - [多台 keepalived 都指向同一 Redis（無主從）](#多台-keepalived-都指向同一-redis無主從)


# 參考資料

[Linux 工具 keepalived(高可用性（High Availability, HA）的工具)](https://github.com/open222333/Other-Note/blob/main/02_%E4%BD%9C%E6%A5%AD%E7%B3%BB%E7%B5%B1/Unix/Linux/Linux%20%E5%B7%A5%E5%85%B7/Linux%20High%20Availability%20HA(%E9%AB%98%E5%8F%AF%E7%94%A8%E6%80%A7%E5%B7%A5%E5%85%B7)/Linux%20%E5%B7%A5%E5%85%B7%20keepalived(%E9%AB%98%E5%8F%AF%E7%94%A8%E6%80%A7%EF%BC%88High%20Availability%2C%20HA%EF%BC%89%E7%9A%84%E5%B7%A5%E5%85%B7).md)

### 心得相關

[Redis高可用方案：使用Keepalived实现主备双活](https://www.cnblogs.com/sowler/p/18387006)

[keepalived+haproxy+redis-cluster 高可用负载均衡redis集群搭建](https://blog.csdn.net/drhrht/article/details/123643314)

[redis分布式集群-redis+keepalived+ haproxy](https://zhuanlan.zhihu.com/p/649926976)

[keepalived+haproxy+redis-cluster 高可用负载均衡redis集群搭建](https://developer.aliyun.com/article/1095147)

# 範例

## Keepalived + Redis Sentinel（推薦）

Redis 主從交給 Sentinel 管理

Keepalived 只負責 VIP

VIP 連到 Redis Master

```
         VIP 10.0.0.100
        /              \
Keepalived A       Keepalived B
        \              /
         +------------+
            Redis Master
            Redis Slave
```

## Keepalived + 自訂 Redis 健康檢查腳本, Minimal HA

健康檢查 script 需要：

```
redis-cli PING
檢查是 master 還是 slave
判斷是否可接管 VIP
```

```
Script 每 2 秒檢查 Redis：

    能回應 PONG
        -（選擇性）角色是否是 Master

若 Redis 無回應或角色不對：

    check_redis.sh exit 1
    Keepalived 減少 priority
    VIP 漂移到另一台 Keepalived

另一台 Keepalived 成為 MASTER，接管 VIP

客戶端永遠只連：
    10.0.0.100:6379

```

`/etc/keepalived/check_redis.sh`

```sh
#!/bin/bash

REDIS_CLI="/usr/bin/redis-cli"
HOST="127.0.0.1"
PORT="6379"

# 檢查 Redis 是否回應 PONG
PONG=$($REDIS_CLI -h $HOST -p $PORT ping 2>/dev/null)
if [ "$PONG" != "PONG" ]; then
    echo "Redis PING failed"
    exit 1
fi

# 如果只有單機 Redis，不需要下面這段。
# 若有主從架構，可啟用 MASTER 檢查（避免 slave 搶 VIP）。
CHECK_ROLE=0  # 0 = 不檢查 | 1 = 要檢查 Master

if [ $CHECK_ROLE -eq 1 ]; then
    ROLE=$($REDIS_CLI -h $HOST -p $PORT INFO replication | grep role: | cut -d: -f2 | tr -d '\r')
    if [ "$ROLE" != "master" ]; then
        echo "Redis is not master"
        exit 1
    fi
fi

exit 0
```

設定 Script 權限

```sh
chmod +x /etc/keepalived/check_redis.sh
```

### Keepalived 主機設定（MASTER 範例）

`/etc/keepalived/keepalived.conf`

```sh
vrrp_script chk_redis {
    script "/etc/keepalived/check_redis.sh"
    interval 2        # 每 2 秒檢查一次 Redis
    timeout 3
    fall 3            # 連續 3 次失敗 → 此節點降級
    rise 2            # 連續 2 次成功 → 此節點可升級
}

vrrp_instance VI_1 {
    state MASTER
    interface eth0
    virtual_router_id 51
    priority 150
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 123456
    }

    virtual_ipaddress {
        10.0.0.100/24
    }

    track_script {
        chk_redis
    }
}
```

### Keepalived 備援機設定（BACKUP 範例）

`/etc/keepalived/keepalived.conf`

```sh
vrrp_script chk_redis {
    script "/etc/keepalived/check_redis.sh"
    interval 2
    timeout 3
    fall 3
    rise 2
}

vrrp_instance VI_1 {
    state BACKUP
    interface eth0
    virtual_router_id 51
    priority 100      # 比 MASTER 低
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 123456
    }

    virtual_ipaddress {
        10.0.0.100/24
    }

    track_script {
        chk_redis
    }
}
```

### 確認正常運作

在 MASTER 上停止 Redis：

```sh
systemctl stop redis
```

然後看 VIP 是否漂移：

```sh
ip a | grep 10.0.0.100
```

備援機會拿到 VIP，服務不中斷。

## 多台 keepalived 都指向同一 Redis（無主從）

只有 單 Redis 節點（沒有主從），那 Keepalived 只是提供多台備援機器來管理 VIP，實際 Redis 只有一台。
