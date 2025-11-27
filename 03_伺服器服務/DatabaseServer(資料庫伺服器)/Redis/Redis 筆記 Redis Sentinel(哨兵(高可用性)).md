# Redis 筆記 Redis Sentinel(哨兵(高可用性))

```
Sentinel 比較像 單一資料庫的 HA 解決方案
Cluster 是 分片 + HA 的大型分布式架構

自動 Failover
    主節點掛掉 → 自動提升從節點為新主節點。

監控
    持續監控 master / slave 的健康。

通知（Notification）
    若節點掛掉，可以發送事件通知。

客戶端服務發現（Service Discovery）
    讓 client 知道目前的 master 是哪一台。
```

| 功能             | Redis Sentinel | Redis Cluster      |
| -------------- | -------------- | ------------------ |
| 高可用（HA）        | ✅ 是            | ✅ 是                |
| 自動 failover    | ✅ 是            | ✅ 是（在 cluster 節點內） |
| 資料分片（sharding） | ❌ 沒有           | ✅ 有                |
| 多節點讀寫路由        | ❌ 沒有           | ✅ 有                |
| 跨叢集 HA         | ❌ 沒有           | ❌ 沒有               |
| 用途             | 單主 + 多從的高可用    | 分片 + 高可用的集群模式      |

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

[Redis Sentinel 官方文檔](https://redis.io/docs/latest/operate/oss_and_stack/management/sentinel/)

### 心得相關

[Redis sentinel 簡介與部署](https://www.tpisoftware.com/tpu/articleDetails/2029)

### HA(High Availability) 高可用性相關

```
「高可用性」的意思是：
    系統在故障、維護、甚至部分節點掛掉時，仍然能提供服務的能力。

常見目的：
    系統不容易中斷
    單點故障（Single Point of Failure, SPOF）被消除
    服務 uptime 越高越好（例如 99.99%）
```

[High Availability and Scalability with Redis Enterprise - Redis Enterprise 實現高可用性和可擴展性](https://medium.com/%40octoz/high-availability-and-scalability-with-redis-enterprise-54a48edcce17)

[Understanding Redis High-Availability Architectures - 理解 Redis 高可用性架構](https://semaphore.io/blog/redis-architectures?utm_source=chatgpt.com)

[Redis high availability](https://alex.dzyoba.com/blog/redis-ha/?utm_source=chatgpt.com)

[Scale with Redis Cluster | Docs](https://redis.io/docs/latest/operate/oss_and_stack/management/scaling/?utm_source=chatgpt.com)
