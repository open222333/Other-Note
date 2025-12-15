# Redis 筆記 Codis(分散式 Redis 叢集方案)

```
Codis 是一套 分散式 Redis 叢集方案，由國產公司 豌豆莢（Wandoujia） 開發，用來解決 Redis 在大型場景下的 水平擴充（sharding）與高可用性 問題。
```

| 元件                      | 作用                         |
| ----------------------- | -------------------------- |
| **Codis Proxy**         | Redis 的代理層，負責接收請求並路由到正確的分片 |
| **Codis Dashboard**     | 管理界面／API，用來查看叢集狀態、遷移資料、擴容  |
| **Codis Fe/Sentinel**   | 管理元件、Web UI                |
| **Zookeeper / Etcd**    | 儲存叢集配置（slot 分配資訊）          |
| **Redis Server（Group）** | 真正儲存資料的 Redis 主從組          |


主要優勢

1. 水平擴容簡單

    可以動態調整分片、加入或移除 Redis 節點，而不需要停機。

2. 在線資料遷移

    Codis 支援 slot-online-migrate，不會中斷服務。

3. 代理模式（Proxy）穩定可靠

    比 Redis cluster 原生 client 模式更容易管理。

4. 支援舊版 Redis

    Codis 不需要升級到 Redis Cluster（3.0+）即可使用分片能力。

| 項目        | Codis             | Redis Cluster   |
| --------- | ----------------- | --------------- |
| 架構        | Proxy 型           | Client 直連       |
| 擴容難度      | 容易                | 較複雜             |
| 遷移資料      | 支援 online migrate | 支援但複雜           |
| Client 要求 | **無需支援 cluster**  | 必須支援 cluster 協議 |
| 可用性       | 高                 | 高               |
| 維護性       | 易於集中控制            | 用戶端邏輯較複雜        |

## 目錄

- [Redis 筆記 Codis(分散式 Redis 叢集方案)](#redis-筆記-codis分散式-redis-叢集方案)
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
```

# 指令
