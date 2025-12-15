# Redis 筆記 Redis Predixy(高效的 Redis Proxy(代理層))

```
Predixy 是一個 高效的 Redis Proxy（代理層），它用來在 Redis 客戶端與 Redis 伺服器之間做中介，提供更好的 負載均衡、讀寫分離、連線管理、故障切換 等功能。它常被用在高併發或大型系統中，讓 Redis 架構更穩定、可擴展。

Predixy 是用 C++ 開發的 Redis 高效代理，主要用途包括：

1. Redis 集群（Cluster）代理

    支援 Redis Cluster 的自動分片（slot）轉發
    自動處理 MOVED、ASK 重定向
    讓客戶端不需要了解 Redis Cluster 的細節（無 cluster-aware 需求）

2. 支援讀寫分離

    可以設定主節點（Master）和從節點（Slave）
    自動將讀請求分流到 Slave，降低 Master 壓力

3. 支援負載均衡

    支援多種 LB 演算法（如輪詢、最小連線數等）
    Slave 節點可以設定權重（weight）

4. 自動故障切換與節點健康檢查

    當 Redis 節點掛掉時，Predixy 會自動繞過故障節點
    不需要修改客戶端設定

5. 高效能

    C++ 編寫，單機可承受非常高的 QPS
    支援 epoll、非阻塞 IO
```

常見用途

```
1. 想用 Redis Cluster，但客戶端不支援 cluster

    Predixy 幫你處理每個 key 的 slot，client 完全無痛接入。

2. 想做 Redis 讀寫分離

    Predixy 會自動判斷哪些是讀、哪些是寫，並把讀操作送到 Slave。

3. 想用負載均衡分散 Redis 壓力

    適合高併發 API 系統或後端服務。
```

| Proxy         | Cluster 支援 | 讀寫分離     | 負載均衡  | 穩定度 | 備註                      |
| ------------- | ---------- | -------- | ----- | --- | ----------------------- |
| **Predixy**   | ✅          | ✅        | ✅ 多算法 | 高   | 官方仍更新                   |
| **Twemproxy** | ❌          | ❌        | 基本    | 高   | 不支援 Redis Cluster       |
| **Codis**     | ✅          | 半（需特殊配置） | 有     | 高   | 架構較重，需要 dashboard/proxy |


## 目錄

- [Redis 筆記 Redis Predixy(高效的 Redis Proxy(代理層))](#redis-筆記-redis-predixy高效的-redis-proxy代理層)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [心得相關](#心得相關)
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

[Github Predixy](https://github.com/joyieldInc/predixy?utm_source=chatgpt.com)

[Predixy 文檔 中文版](https://github.com/joyieldInc/predixy/blob/master/README_CN.md)

[Predixy 文檔](https://joyieldinc.github.io/predixy/?utm_source=chatgpt.com)

### 心得相關

[Redis代理与集群的总结报告 redis代理 prodixy](https://www.cnblogs.com/gered/p/15210798.html#autoid-1-5-0)

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

通常在 `conf/redis.conf`

### 基本範例

```conf
Name redis_cluster
Type cluster
RedisClusterPool {
    Hash crc16
    MasterRead no
}
Server {
    Addr 192.168.1.10:7000
}
Server {
    Addr 192.168.1.10:7001
}
Server {
    Addr 192.168.1.10:7002
}
```

# 指令

```sh
predixy ./predixy.conf
```
