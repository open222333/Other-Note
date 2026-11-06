# Template-PythonVectorAPI

```
語意搜尋 API 起始模板
FastAPI + PostgreSQL / pgvector，內建 Embedding Provider 抽象層、切塊匯入、向量 / 全文 / Hybrid 搜尋
```

適用：RAG 知識庫、AI 記憶系統、文件 / 筆記語意搜尋。模板只負責「存進去、找回來」，生成回答交給使用模板的專案。

測試環境：Python 3.13 / PostgreSQL 17 / pgvector 0.8+

## 目錄

- [功能](#功能)
- [目錄結構](#目錄結構)
- [基於此模板建立新專案](#基於此模板建立新專案)
- [設定](#設定)
- [本機執行](#本機執行)
- [Docker](#docker)
- [資料模型](#資料模型)
- [API](#api)
- [搜尋模式](#搜尋模式)
- [Embedding Provider](#embedding-provider)
- [更換 Embedding 模型](#更換-embedding-模型)
- [擴充方式](#擴充方式)
- [注意事項](#注意事項)

## 功能

- **FastAPI**：async API、自動產生 OpenAPI（`/docs`）
- **PostgreSQL + pgvector**：一個 DB 同時存資料與向量，交易一致
- **Embedding Provider 抽象層**：OpenAI 相容 API / Ollama（可完全離線）
- **匯入流程**：文件 → 切塊（chunking）→ 批次產生 Embedding
- **三種搜尋**：`vector`、`fulltext`、`hybrid`（RRF 合併排名）
- **模型版本檢查**：啟動時比對設定模型與 DB 內向量模型，不一致即警告
- **Alembic migration**：第一版即建立 extension、資料表、HNSW / trigram 索引
- **Docker Compose**：api + db，選配 ollama

## 目錄結構

```
Template-PythonVectorAPI/
├── conf/
│   └── config.ini.default        # 設定檔範本（複製為 config.ini）
├── docker/
│   └── Dockerfile                # python:3.13-slim，非 root 執行
├── migrations/                   # Alembic
│   └── versions/
│       └── 0001_init.py          # CREATE EXTENSION vector / pg_trgm、資料表、索引
├── src/
│   ├── __init__.py               # 讀取設定（BASE_DIR、get_setting）
│   ├── main.py                   # FastAPI app 進入點
│   ├── db.py                     # async engine / session
│   ├── api/
│   │   ├── documents.py          # 文件 CRUD
│   │   ├── search.py             # 搜尋
│   │   └── health.py             # 健康檢查
│   ├── models/                   # documents、chunks、embeddings
│   ├── schemas/                  # Pydantic request / response
│   ├── embedding/
│   │   ├── base.py               # EmbeddingProvider 介面
│   │   ├── openai_compat.py      # OpenAI / 相容 API（DeepSeek、vLLM…）
│   │   └── ollama.py             # 本地模型
│   ├── ingest/
│   │   ├── chunker.py            # 切塊策略
│   │   └── pipeline.py           # 切塊 → embed → 寫入
│   └── search/
│       ├── vector.py
│       ├── fulltext.py
│       └── hybrid.py             # RRF
├── scripts/
│   ├── reembed.py                # 更換模型時重新產生全部向量
│   └── seed_demo.py              # Demo 資料（標記 is_demo）
├── tests/
├── .env.default                  # 環境變數範本（複製為 .env）
├── alembic.ini
├── docker-compose.yml
├── requirements.txt
├── requirements-test.txt
└── pytest.ini
```

## 基於此模板建立新專案

```bash
git clone <此模板 URL> your-new-project
cd your-new-project
rm -rf .git && git init          # 不保留模板歷史
cp conf/config.ini.default conf/config.ini
cp .env.default .env             # 填入 POSTGRES_PASSWORD、EMBEDDING_* 等
```

## 設定

讀取順序：**環境變數 > `conf/config.ini` > 預設值**（與其他 Python 模板相同，`from src import get_setting`）。

| 變數 | 說明 | 預設 |
|---|---|---|
| `POSTGRES_HOST` / `POSTGRES_PORT` | 資料庫位址 | `db` / `5432` |
| `POSTGRES_USER` / `POSTGRES_PASSWORD` / `POSTGRES_DB` | 資料庫帳密 | — |
| `EMBEDDING_PROVIDER` | `openai_compat` / `ollama` | `ollama` |
| `EMBEDDING_BASE_URL` | Provider 端點 | `http://ollama:11434` |
| `EMBEDDING_API_KEY` | 外部 API 金鑰（Ollama 不需要） | — |
| `EMBEDDING_MODEL` | 模型名稱 | `nomic-embed-text` |
| `EMBEDDING_DIM` | 向量維度，須與模型一致 | `768` |
| `CHUNK_SIZE` / `CHUNK_OVERLAP` | 切塊字數 / 重疊字數 | `800` / `100` |
| `SEARCH_DEFAULT_MODE` | 預設搜尋模式 | `hybrid` |
| `LOG_LEVEL` | log 等級 | `INFO` |

- 預設 Provider 為 Ollama：**不設定外部服務時，資料不會傳出本機**
- `EMBEDDING_DIM` 寫入 migration 的 `vector(n)` 定義，建立專案時先決定好

## 本機執行

```bash
python -m venv .venv && source .venv/bin/activate
pip install -r requirements-test.txt
docker compose up -d db            # 只起資料庫
alembic upgrade head               # 建立 extension 與資料表
uvicorn src.main:app --reload      # http://localhost:8000/docs
pytest                             # 測試（需 db 運行）
```

## Docker

```bash
docker compose up -d --build                      # api + db
docker compose --profile ollama up -d --build     # 加上本地 Embedding 模型
docker compose exec ollama ollama pull nomic-embed-text
docker compose exec api alembic upgrade head
```

- DB 使用 `pgvector/pgvector:pg17`，資料持久化於 `./data/postgres`
- 預設只綁定 `127.0.0.1`，遠端存取需自行加 TLS 與認證

## 資料模型

```
documents 1 ── * chunks 1 ── * embeddings
```

| 資料表 | 主要欄位 | 說明 |
|---|---|---|
| `documents` | `id`、`workspace_id`、`title`、`source`、`metadata jsonb`、`is_demo`、`created_at`、`updated_at` | 原始文件 |
| `chunks` | `id`、`document_id`、`seq`、`content`、`content_tsv` | 切塊內容，全文索引在此 |
| `embeddings` | `chunk_id`、`model_name`、`dim`、`embedding vector(n)`、`created_at` | 向量與產生模型；同一 chunk 可並存多個模型 |

- 內容與向量分表：重新切塊或換模型不影響原始文件
- 預留 `workspace_id` 與 `metadata`：權限 / 條件篩選直接在 SQL `WHERE` 完成
- 刪除文件時，chunks 與 embeddings 以 `ON DELETE CASCADE` 在同一交易刪除

## API

```http
POST   /api/v1/documents          # 新增文件（自動切塊 + 產生向量）
GET    /api/v1/documents
GET    /api/v1/documents/{id}
DELETE /api/v1/documents/{id}

POST   /api/v1/search
GET    /api/v1/health
```

搜尋請求範例：

```json
{
  "query": "偏好的回答語言",
  "mode": "hybrid",
  "filters": { "workspace_id": "default", "source": ["notes"] },
  "limit": 5
}
```

回應附各來源分數，方便調整排序：

```json
{
  "results": [
    {
      "chunk_id": "chk_0001",
      "document_id": "doc_0001",
      "content": "偏好使用繁體中文回答",
      "score": 0.0325,
      "scores": { "vector_rank": 1, "fulltext_rank": 2 }
    }
  ]
}
```

## 搜尋模式

| 模式 | 方式 | 適合 |
|---|---|---|
| `vector` | pgvector 餘弦距離 + HNSW | 意思相近、用詞不同 |
| `fulltext` | `pg_trgm` 相似度（不需分詞，中英文皆可） | 型號、版本號、專有名詞 |
| `hybrid` | 兩者各取前 N 名，以 RRF 合併 | 預設；兼顧語意與精確詞 |

- 中文斷詞品質要求高時，可將 `fulltext` 換成 zhparser / pg_jieba 的 `tsvector`
- 篩選條件很嚴格時，啟用 `SET hnsw.iterative_scan = relaxed_order` 避免結果不足

## Embedding Provider

新增 Provider 只需實作 `src/embedding/base.py` 介面並在設定中指定：

```python
class EmbeddingProvider(Protocol):
    model_name: str
    dim: int

    async def embed(self, texts: list[str]) -> list[list[float]]: ...
```

| Provider | 設定 | 備註 |
|---|---|---|
| `ollama` | `EMBEDDING_BASE_URL=http://ollama:11434` | 離線可用 |
| `openai_compat` | `EMBEDDING_BASE_URL`、`EMBEDDING_API_KEY` | OpenAI 及相容 API |

## 更換 Embedding 模型

不同模型的向量不能混用。啟動時若設定的 `EMBEDDING_MODEL` 與 DB 中向量的 `model_name` 不一致，`/api/v1/health` 會回報 `embedding_model_mismatch`。

```bash
# 1. 修改 .env 的 EMBEDDING_MODEL / EMBEDDING_DIM
# 2. 維度改變時新增 migration 調整 vector(n)
# 3. 重新產生全部向量（舊模型向量保留至完成後再清除）
docker compose exec api python scripts/reembed.py --batch-size 64
docker compose exec api python scripts/reembed.py --purge-old
```

## 擴充方式

| 需求 | 做法 |
|---|---|
| 大量匯入 | 加 Redis + RQ / Celery，將 `ingest/pipeline.py` 改為背景任務 |
| 認證 | 在 `api/` 加 API Key 或 JWT dependency |
| 新資料來源 | 在 `ingest/` 新增 loader（Markdown、PDF、CSV…） |
| RAG 問答 | 使用專案自行呼叫 LLM，模板 `search` 結果作為 context |
| MCP Server | 另起 process，透過本模板 API 或共用 `search/` 模組 |

## 注意事項

- 金鑰只放 `.env` / `config.ini`，**不要**寫進 `*.default`
- 不在 log 中輸出完整文件內容或查詢內容（`LOG_LEVEL=DEBUG` 亦同）
- `vector` 型別索引上限 2000 維；超過改用 `halfvec`（上限 4000 維）
- Demo 資料以 `is_demo` 標記，正式環境執行 `seed_demo.py --purge` 清除
- `docker-compose.yml` 不宣告 `version:`
