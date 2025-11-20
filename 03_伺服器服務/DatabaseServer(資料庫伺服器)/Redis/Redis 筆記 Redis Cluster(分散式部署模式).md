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

## 配置文檔

通常在 ``

### 基本範例

```
```

# 指令

