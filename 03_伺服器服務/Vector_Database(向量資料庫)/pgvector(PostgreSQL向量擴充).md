# pgvector(PostgreSQL向量擴充)

```
PostgreSQL 開源擴充套件，提供向量型別、距離運算與向量索引，讓 Postgres 直接做 Embedding 語意搜尋，不需另架向量資料庫。
```

> 適用版本：pgvector 0.8+、PostgreSQL 13+（`halfvec` / `sparsevec` 需 0.7+，`hnsw.iterative_scan` 需 0.8+）

## 目錄

- [pgvector(PostgreSQL向量擴充)](#pgvectorpostgresql向量擴充)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [概念](#概念)
  - [Embedding 與語意搜尋](#embedding-與語意搜尋)
  - [與專用向量資料庫比較](#與專用向量資料庫比較)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [型別與距離運算子](#型別與距離運算子)
  - [索引](#索引)
  - [語意搜尋](#語意搜尋)
  - [Hybrid Search 混合搜尋](#hybrid-search-混合搜尋)
- [例外狀況](#例外狀況)
  - [type vector does not exist](#type-vector-does-not-exist)
  - [向量維度不符](#向量維度不符)
  - [索引維度超過上限](#索引維度超過上限)
  - [加上 WHERE 後結果變少](#加上-where-後結果變少)

## 參考資料

[pgvector GitHub](https://github.com/pgvector/pgvector)

[pgvector Docker Hub](https://hub.docker.com/r/pgvector/pgvector)

[Elasticsearch & Kibana 筆記](Elastic_Stack/Elasticsearch(搜尋引擎)_&_Kibana(Elasticsearch用戶界面)_筆記.md)

[AI 多人協作開發架構（RAG 向量庫選型）](../../01_程式語言/AI/AI_多人協作開發架構(團隊AI整合).md)

# 概念

## Embedding 與語意搜尋

- Embedding 模型把文字轉成固定維度的數字向量
- **語意相近 → 向量距離近**，關鍵字不同也能搜到

```
"偏好使用繁體中文回答"   → [0.12, -0.83, 0.45, ...]   (例如 1536 維)
"回覆請用中文"           → [0.10, -0.80, 0.47, ...]   ← 距離很近
"使用 PostgreSQL 17"     → [-0.66, 0.21, 0.09, ...]   ← 距離很遠
```

- 查詢時把查詢文字用**同一個模型**轉成向量，再找距離最近的資料
- 換 Embedding 模型 → 維度與語意空間改變，舊向量須全部重新產生；資料表應記錄模型名稱與版本

## 與專用向量資料庫比較

| 面向 | pgvector | 專用向量 DB（Qdrant / Milvus / Pinecone） |
|---|---|---|
| 架構 | 與業務資料同一個 DB | 多一個服務，資料需同步 |
| 條件篩選 / 權限 / JOIN | ✅ 直接用 SQL | metadata filter，能力有限 |
| 交易一致性 | ✅ 資料與向量同一交易 commit / 刪除 | 兩邊可能不一致 |
| 規模 | 數百萬筆無壓力 | 億級、分散式叢集較強 |
| 維運 | 會 Postgres 即可 | 需學新系統 |

- 適合：個人 / 中小型 RAG、AI 記憶系統、需要權限過濾與 Hybrid Search
- 不適合：億級向量、需要水平擴展的大型檢索服務

# 安裝

## docker-compose 部署

官方映像已內建 pgvector：

```yml
services:
  db:
    image: pgvector/pgvector:pg17
    restart: unless-stopped
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
    ports:
      - "127.0.0.1:5432:5432"
    volumes:
      - ./data/postgres:/var/lib/postgresql/data
```

## Debian (Ubuntu)

需先加入 PostgreSQL 官方 APT 源（PGDG），套件名稱依 Postgres 版本：

```bash
sudo apt install postgresql-17-pgvector
```

## Homebrew (MacOS)

```bash
brew install pgvector
```

## 配置文檔

每個資料庫需各自啟用擴充：

```sql
CREATE EXTENSION IF NOT EXISTS vector;

-- 確認版本
SELECT extversion FROM pg_extension WHERE extname = 'vector';
```

### 基本範例

```sql
CREATE TABLE memories (
  id               text PRIMARY KEY,
  content          text NOT NULL,
  category         text,
  embedding        vector(1536),
  embedding_model  text            -- 記錄產生向量的模型與版本
);

CREATE INDEX ON memories USING hnsw (embedding vector_cosine_ops);
```

# 指令

## 型別與距離運算子

| 型別 | 說明 |
|---|---|
| `vector(n)` | 單精度向量，最常用 |
| `halfvec(n)` | 半精度，省一半空間，可索引更高維度 |
| `sparsevec(n)` | 稀疏向量 |
| `bit(n)` | 二進位向量 |

| 運算子 | 距離 | 對應索引 ops |
|---|---|---|
| `<=>` | 餘弦距離（文字 Embedding 最常用） | `vector_cosine_ops` |
| `<->` | 歐氏距離 L2 | `vector_l2_ops` |
| `<#>` | 負內積 | `vector_ip_ops` |
| `<+>` | L1 距離 | `vector_l1_ops` |

- 餘弦相似度 = `1 - (a <=> b)`

## 索引

| 索引 | 特性 | 適用 |
|---|---|---|
| HNSW | 查詢快、召回率高，建立慢、耗記憶體；可在空表先建 | 大多數情境（預設選擇） |
| IVFFlat | 建立快、省記憶體；需先有資料再建，召回率較低 | 資料量大、記憶體有限 |

```sql
-- HNSW
CREATE INDEX ON memories USING hnsw (embedding vector_cosine_ops)
  WITH (m = 16, ef_construction = 64);
SET hnsw.ef_search = 100;          -- 查詢時候選數，越大越準越慢

-- IVFFlat（lists 約為 rows / 1000）
CREATE INDEX ON memories USING ivfflat (embedding vector_cosine_ops)
  WITH (lists = 100);
SET ivfflat.probes = 10;
```

## 語意搜尋

`$1` 為查詢文字經同一模型轉出的向量：

```sql
SELECT id, content, 1 - (embedding <=> $1) AS similarity
FROM memories
WHERE category = 'preferences'
ORDER BY embedding <=> $1
LIMIT 5;
```

## Hybrid Search 混合搜尋

條件篩選 + 全文 + 向量，用 RRF（Reciprocal Rank Fusion）合併排名：

```sql
WITH vec AS (
  SELECT id, row_number() OVER (ORDER BY embedding <=> $1) AS r
  FROM memories
  WHERE category = ANY($3)                         -- 權限 / 條件篩選
  ORDER BY embedding <=> $1
  LIMIT 20
),
fts AS (
  SELECT id, row_number() OVER (
           ORDER BY ts_rank(to_tsvector('simple', content), plainto_tsquery('simple', $2)) DESC
         ) AS r
  FROM memories
  WHERE category = ANY($3)
    AND to_tsvector('simple', content) @@ plainto_tsquery('simple', $2)
  ORDER BY r
  LIMIT 20
)
SELECT m.id, m.content,
       COALESCE(1.0 / (60 + vec.r), 0) + COALESCE(1.0 / (60 + fts.r), 0) AS score
FROM memories m
LEFT JOIN vec ON vec.id = m.id
LEFT JOIN fts ON fts.id = m.id
WHERE vec.id IS NOT NULL OR fts.id IS NOT NULL
ORDER BY score DESC
LIMIT 10;
```

- 型號、版本號、專有名詞靠全文搜尋補強，避免語意搜尋把不同型號視為相同
- 中文全文搜尋需搭配分詞擴充（如 zhparser / pg_jieba）或改用 `pg_trgm`

# 例外狀況

## type vector does not exist

```
ERROR:  type "vector" does not exist
```

- 原因：目前資料庫未啟用擴充（擴充是 per-database）
- 解法：`CREATE EXTENSION IF NOT EXISTS vector;`

## 向量維度不符

```
ERROR:  expected 1536 dimensions, not 768
```

- 原因：寫入或查詢的向量與欄位定義維度不同，通常是換了 Embedding 模型
- 解法：統一模型；換模型時建新欄位 / 新表並重新產生全部向量

## 索引維度超過上限

```
ERROR:  column cannot have more than 2000 dimensions for hnsw index
```

- 原因：`vector` 型別的 HNSW / IVFFlat 索引上限 2000 維
- 解法：改用 `halfvec`（索引上限 4000 維），或選擇較低維度的模型

```sql
CREATE INDEX ON memories USING hnsw ((embedding::halfvec(3072)) halfvec_cosine_ops);
```

## 加上 WHERE 後結果變少

- 原因：近似索引先取 `ef_search` 個候選再套 `WHERE`，篩選後可能不足 `LIMIT` 筆
- 解法：pgvector 0.8+ 開啟迭代掃描，或調高 `hnsw.ef_search`；高選擇性條件可加一般 B-tree 索引或分區

```sql
SET hnsw.iterative_scan = relaxed_order;
```
