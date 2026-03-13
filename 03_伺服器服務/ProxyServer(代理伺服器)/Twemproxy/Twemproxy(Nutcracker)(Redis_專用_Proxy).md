# Twemproxy(Nutcracker)(Redis 專用 Proxy)

```
Twemproxy 是 Twitter 開源的一款 高效 Redis/Memcached 代理（Proxy），主要特點是提供：

自動 sharding（分片）
連線數減少、連線池管理
高效能、低延遲
後端節點故障容錯（簡易版）

非常適合在有多台 Redis 伺服器、但應用程式不想自己管理分片邏輯時使用。
```

## 目錄

- [Twemproxy(Nutcracker)(Redis 專用 Proxy)](#twemproxynutcrackerredis-專用-proxy)
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
- [指令](#指令)

## 參考資料

[]()

# 安裝

## docker-compose 部署

```yml
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

通常在 ``

### 基本範例

```
redis_cluster:
  listen: 0.0.0.0:6380
  hash: ketama
  distribution: ketama
  auto_eject_hosts: true
  server_retry_timeout: 2000
  server_failure_limit: 1
  servers:
    - 192.168.1.10:6379:1
    - 192.168.1.11:6379:1
    - 192.168.1.12:6379:1
```

| 設定項                    | 重點                     |
| ---------------------- | ---------------------- |
| `listen`               | Twemproxy 對外提供服務的 port |
| `hash`, `distribution` | 使用一致性哈希（分片策略）          |
| `auto_eject_hosts`     | 失敗節點自動剔除               |
| `servers`              | 後端 Redis 節點（:1 代表權重）   |


# 指令
