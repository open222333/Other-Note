# DatabaseServer 總覽

```
本目錄收錄各類資料庫伺服器的筆記。
資料庫依儲存模型分為關聯式（RDBMS）、文件型、鍵值型等，各有不同的適用情境。
```

## 目錄

- [DatabaseServer 總覽](#databaseserver-總覽)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [資料庫清單](#資料庫清單)
- [資料庫類型比較](#資料庫類型比較)
- [各資料庫說明](#各資料庫說明)
  - [MySQL](#mysql)
  - [MongoDB](#mongodb)
  - [Redis](#redis)
  - [Memcached](#memcached)
  - [SQLite](#sqlite)
- [HA / 叢集方案總覽](#ha--叢集方案總覽)
- [需求選擇建議](#需求選擇建議)

## 參考資料

- [MySQL 筆記](./MySQL/MySQL_筆記.md)
- [MongoDB 筆記](./MongoDB/MongoDB_筆記.md)
- [Redis 筆記](./Redis/Redis_筆記.md)
- [SQLite3 筆記](./SQLite/SQLite3_筆記.md)
- [Memcached 筆記](./Memcached/Memcached_筆記.md)

---

# 資料庫清單

| 資料庫 | 類型 | 主要用途 | 筆記 |
|---|---|---|---|
| **MySQL** | 關聯式（RDBMS） | 結構化資料、事務處理 | [MySQL_筆記.md](./MySQL/MySQL_筆記.md) |
| **MongoDB** | 文件型（NoSQL） | 彈性結構、JSON 資料 | [MongoDB_筆記.md](./MongoDB/MongoDB_筆記.md) |
| **Redis** | 鍵值型（In-Memory） | 快取、Session、佇列 | [Redis_筆記.md](./Redis/Redis_筆記.md) |
| **Memcached** | 鍵值型（In-Memory） | 簡單快取 | [Memcached_筆記.md](./Memcached/Memcached_筆記.md) |
| **SQLite** | 關聯式（嵌入式） | 本地儲存、小型應用 | [SQLite3_筆記.md](./SQLite/SQLite3_筆記.md) |

---

# 資料庫類型比較

| 項目 | MySQL | MongoDB | Redis | Memcached | SQLite |
|---|---|---|---|---|---|
| **資料模型** | 表格（Row/Column） | 文件（JSON/BSON） | 鍵值 / 結構 | 鍵值 | 表格（Row/Column） |
| **查詢語言** | SQL | MQL（類 JSON） | 指令集 | 指令集 | SQL |
| **Schema** | 強制（fixed） | 彈性（flexible） | 無 | 無 | 強制（fixed） |
| **ACID 事務** | ✅（InnoDB） | ✅（4.0+，有限） | ✅（單指令原子） | ❌ | ✅ |
| **持久化** | ✅ | ✅ | ✅（RDB / AOF） | ❌（重啟即清空） | ✅ |
| **資料存放** | 磁碟 | 磁碟 | 記憶體（可持久化） | 記憶體 | 單一 `.db` 檔案 |
| **Join / 關聯** | ✅（外鍵、JOIN） | ❌（需嵌入或 `$lookup`） | ❌ | ❌ | ✅ |
| **水平擴展** | 較難（需 ProxySQL 等） | ✅（Sharding 原生） | ✅（Cluster） | ✅ | ❌ |
| **部署複雜度** | 中 | 中 | 低～中 | 低 | 極低（零設定） |
| **適用規模** | 中～大型 | 中～大型 | 任意（輔助層） | 任意（輔助層） | 小型 / 本機 |

---

# 各資料庫說明

## MySQL

**關聯式資料庫，最廣泛使用的開源 RDBMS。**

- 資料以表格儲存，欄位有固定 Schema
- 支援 ACID 事務（InnoDB 引擎）、外鍵、複雜 JOIN
- 適合需要資料一致性、結構明確的業務資料（訂單、帳號、庫存）
- 常見搭配：LAMP / LNMP 架構

**工具生態：**

| 工具 | 用途 | 筆記 |
|---|---|---|
| ProxySQL | 讀寫分離、高可用代理 | [ProxySQL 筆記](./MySQL/MySQL_工具_ProxySQL(高性能_高可用性的_MySQL_代理).md) |
| XtraBackup | 熱備份（不鎖表） | [XtraBackup 筆記](./MySQL/MySQL_工具_Percona_XtraBackup(資料備份的工具).md) |
| Orchestrator | 主從切換、HA | [Orchestrator 筆記](./MySQL/MySQL_工具_Orchestrator(HA-高可用_工具).md) |
| MySQL Router | 輕量路由 | [MySQL Router 筆記](./MySQL/MySQL_工具_MySQL_Router(輕量級的路由器).md) |
| phpMyAdmin | Web 管理介面 | [phpMyAdmin 筆記](./MySQL/MySQL_工具_phpMyAdmin(MySQL資料庫管理工具).md) |
| mydumper | 並行備份還原 | [mydumper 筆記](./MySQL/MySQL_工具_mydumper_myloader(平行備份還原工具).md) |
| mysqlbinlog | Binary Log 檢查 | [mysqlbinlog 筆記](./MySQL/MySQL_工具_mysqlbinlog(檢查主資料庫中的二進制日誌).md) |

## MongoDB

**文件型 NoSQL，彈性 Schema，以 BSON（類 JSON）儲存。**

- 無固定 Schema，同集合內文件結構可不同
- 原生支援水平分片（Sharding），適合大量寫入與快速迭代
- 適合：日誌、內容管理、半結構化資料、需要頻繁改動欄位的場景
- 不適合：強關聯查詢、嚴格事務、財務資料

**工具生態：**

| 工具 | 用途 | 筆記 |
|---|---|---|
| mongosh | 互動式 Shell | [mongosh 筆記](./MongoDB/MongoDB_工具_mongosh(交互式Shell).md) |
| Compass | GUI 管理工具 | [Compass 筆記](./MongoDB/MongoDB_工具_Compass(可視化管理工具).md) |

**叢集模式：**

| 模式 | 筆記 |
|---|---|
| Replica Set（主從複製） | [Replica Set 筆記](./MongoDB/MongoDB_筆記_Replica-Set(複本集).md) |
| Cluster（分片叢集） | [Cluster 筆記](./MongoDB/MongoDB_筆記_Cluster(叢集).md) |

## Redis

**In-Memory 鍵值資料庫，支援多種資料結構。**

- 資料存於記憶體，讀寫速度極快（微秒級）
- 支援 String、Hash、List、Set、Sorted Set、Stream 等結構
- 可持久化（RDB 快照 / AOF 日誌）
- 適合：快取、Session 儲存、排行榜、訊息佇列、分散式鎖

**叢集 / HA 方案：**

| 方案 | 說明 | 筆記 |
|---|---|---|
| Replication（主從） | 讀寫分離，基礎 | [Replication 筆記](./Redis/Redis_筆記_Redis_Replication(Master–Slave_主從複製).md) |
| Sentinel（哨兵） | 自動故障切換 HA | [Sentinel 筆記](./Redis/Redis_筆記_Redis_Sentinel(哨兵(高可用性)).md) |
| Cluster（叢集） | 水平分片，原生擴展 | [Cluster 筆記](./Redis/Redis_筆記_Redis_Cluster(分散式部署模式).md) |
| Predixy | Redis 代理層 | [Predixy 筆記](./Redis/Redis_筆記_Redis_Predixy(高效的_Redis_Proxy(代理層)).md) |
| Keepalived | VIP 切換 HA | [Keepalived 筆記](./Redis/Redis_筆記_Redis_Keepalived(HA工具).md) |
| Codis | 分散式 Redis 方案 | [Codis 筆記](./Redis/Redis_筆記_Codis(分散式_Redis_叢集方案).md) |

## Memcached

**輕量分散式快取，介面比 Redis 簡單。**

- 僅支援簡單鍵值（字串），無額外資料結構
- 不支援持久化，重啟後資料全部消失
- 多執行緒架構，在純快取場景下效能接近 Redis
- 適合：純快取需求、不需持久化、追求極簡部署
- **現代專案大多選 Redis**（功能更豐富），Memcached 常見於舊有系統

## SQLite

**嵌入式關聯式資料庫，零設定、單一檔案。**

- 不需要獨立伺服器程序，直接嵌入應用程式
- 資料庫即一個 `.db` 檔案，可直接複製備份
- 支援標準 SQL、ACID 事務
- 適合：本機工具、行動 App、小型 Web、測試環境、單一使用者應用
- 不適合：高並發寫入、多機共用、大型資料集

---

# HA / 叢集方案總覽

| 資料庫 | 主從複製 | 自動切換（HA） | 水平分片 | 代理層 |
|---|---|---|---|---|
| **MySQL** | ✅ Replication | ✅ Orchestrator | ✅ InnoDB Cluster | ✅ ProxySQL / MySQL Router |
| **MongoDB** | ✅ Replica Set | ✅（Replica Set 內建） | ✅ Sharding | — |
| **Redis** | ✅ Replication | ✅ Sentinel | ✅ Cluster / Codis | ✅ Predixy |
| **Memcached** | ❌ | ❌ | ✅（Client 端一致性雜湊） | — |
| **SQLite** | ❌ | ❌ | ❌ | — |

---

# 需求選擇建議

| 情境 | 推薦 | 理由 |
|---|---|---|
| 結構化業務資料（訂單、帳號） | **MySQL** | ACID、JOIN、Schema 保障資料正確性 |
| 快速迭代、欄位常變動 | **MongoDB** | 彈性 Schema，不需 migration |
| API 快取、Session | **Redis** | 記憶體存取速度快，TTL 自動過期 |
| 排行榜 / 計數器 | **Redis** | Sorted Set / INCR 原子操作 |
| 訊息佇列（輕量） | **Redis Stream / List** | 內建 BLPOP / Stream，無需額外 MQ |
| 行動 App / 桌面工具本地儲存 | **SQLite** | 零設定、單檔、可直接隨 App 打包 |
| 純快取、舊有系統相容 | **Memcached** | 輕量，與舊版 PHP 等生態整合好 |
| 大量非結構化日誌 | **MongoDB** | 文件型插入快，$match 聚合彈性強 |
| 需要地理索引（GeoJSON） | **MongoDB** | 原生 `2dsphere` 索引 |
| 需要全文搜尋 | 搭配 **Elasticsearch** | 純資料庫全文搜尋效果有限 |
