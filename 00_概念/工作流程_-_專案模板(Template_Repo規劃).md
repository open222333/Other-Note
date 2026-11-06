# 工作流程 - 專案模板(Template_Repo規劃)

```
個人 Template-* 專案模板的現況、分層關係、待新增模板清單與維護建議。
```

## 目錄

- [工作流程 - 專案模板(Template_Repo規劃)](#工作流程---專案模板template_repo規劃)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [現有模板](#現有模板)
    - [共同慣例](#共同慣例)
    - [分層關係](#分層關係)
  - [待新增模板](#待新增模板)
    - [優先度高](#優先度高)
    - [優先度中](#優先度中)
    - [暫緩](#暫緩)
  - [維護建議](#維護建議)

## 參考資料

[程式設計 - 視覺UI工具模板(前端架構選型)](程式設計_-_視覺UI工具模板(前端架構選型).md)

[程式設計 - 專案檔案結構](程式設計_-_專案檔案結構(程式概念).md)

[copier（專案模板產生工具）](https://copier.readthedocs.io/)

[cookiecutter（專案模板產生工具）](https://cookiecutter.readthedocs.io/)

[GitHub - Creating a template repository](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-template-repository)

---

## 現有模板

| 模板 | 用途 | 內容 |
|---|---|---|
| `Template-Repo` | 最小骨架，不綁語言 | `conf/`、`src/` |
| `Template-PythonRepo` | Python 專案起始 | `src` 套件、設定讀取（環境變數優先）、pytest、Docker（python:3.13-slim、非 root） |
| `Template-PythonToolRepo` | Python CLI 工具 | PythonRepo + `common_tool` submodule（Log、ProgressBar） |
| `Template-DockerRepo` | 包裝既有服務 / 第三方映像 | `docker/Dockerfile`、`docker-compose.yml`、唯讀掛載設定 |
| `Template-PythonFlaskAPI` | Flask API 完整範本 | Swagger、JWT + 角色權限、Vue 3 後台、MongoDB / MySQL / Redis、Celery、nginx、Rate Limiting |
| `Template-PythonWatcher` | 監控 + 通知 | `[check:*]` 設定 http / tcp / disk 檢查、門檻、告警去重（狀態改變 / 提醒 / 恢復）、Telegram / Mail、heartbeat healthcheck |
| `Template-PythonCrawler` | 爬蟲 | requests / Playwright、指數退避重試、UA / Proxy 輪換、Cookie 保存、每網域速率限制、robots.txt、JSONL / CSV / SQLite 輸出 |
| `Template-Backup` | 備份（Python） | files / MySQL / PostgreSQL / Mongo、sha256、保留 N 份輪替、rclone / S3 上傳、通知、`verify` / `restore`、`RESTORE.md` |
| `Template-PythonVectorAPI` | 語意搜尋 / RAG | FastAPI + pgvector、Embedding Provider（Ollama / OpenAI 相容）、切塊匯入、向量 / 全文 / Hybrid 搜尋、Alembic、reembed |
| `Template-VisualTool` | 視覺 UI 工具（React） | Vite + shadcn/ui 三欄工作台、Zustand + zundo undo、Zod schema 自動表單、匯出 PNG / SVG / JSON、⌘K 指令面板 |
| `Template-ReactDashboard` | 後台 Dashboard（React） | Vite + shadcn/ui、可收合側欄 + 麵包屑 + ⌘K、React Router、React Query + API client（後端格式轉換集中於 adapters）、伺服器端資料表格（網址同步）、CRUD 範例、角色權限、mock API |

### 共同慣例

- 設定範本：`conf/config.ini.default`、`.env.default`（複製後使用，不入版控）
- 容器：`docker/Dockerfile`、非 root 執行
- 測試：`pytest.ini`、`requirements-test.txt`
- 建新專案：clone 後 `rm -rf .git && git init`，不保留模板歷史
- 需要通知的 Python 模板（Watcher、Backup）共用同一套 `src/notify/`（console / telegram / mail），機密只放 `.env`

### 分層關係

```
Template-Repo
├── Template-PythonRepo
│   ├── Template-PythonToolRepo
│   │   └── (待新增) Template-TelegramBot
│   ├── Template-PythonWatcher       ✅（不掛 common_tool，標準 logging）
│   ├── Template-PythonCrawler       ✅（不掛 common_tool，標準 logging）
│   ├── Template-Backup              ✅
│   ├── Template-PythonVectorAPI     ✅
│   └── (待新增) Template-PythonWorker
├── Template-DockerRepo
│   └── (待新增) Template-DockerStack
├── Template-PythonFlaskAPI
├── Template-VisualTool              ✅（React + shadcn/ui，獨立前端）
├── Template-ReactDashboard          ✅（React + shadcn/ui，後台前端；與 VisualTool 共用 UI 元件與主題）
└── (待新增) Template-VueWeb
```

## 待新增模板

### 優先度高

已有多個專案重複同一種模式：

| 模板 | 對應現有專案 | 內容重點 |
|---|---|---|
| `Template-VueWeb` | JavaScript-Vue3、Other-ToolWeb、Other-StarCitizenToolWeb、FlaskAPI 的 frontend | Vue 3 + Vite + TS + Pinia + Router、shadcn-vue、`/api` proxy、build 靜態檔 + nginx Dockerfile |
| `Template-DockerStack` | Other-MySQLCluster、Other-MySQLReplication、Other-ProxySQL、Other-Elasticstack、Other-MailServer | 基於 DockerRepo：多 service、`init/` 初始化腳本、healthcheck 與啟動順序、`scripts/` 驗證與故障演練 |

### 優先度中

| 模板 | 理由 | 內容重點 |
|---|---|---|
| `Template-PythonWorker` | Python-Celery；FlaskAPI 的 Celery 可獨立 | Celery worker + beat + Redis，不含 API |
| `Template-CloudflareWorker` | JavaScript-CloudflareWorkers | wrangler、TS、Vitest、KV / D1 綁定範例、多環境 `wrangler.toml` |
| `Template-BashScript` | Bash-Code、Bash-DBBackup | `set -euo pipefail`、參數解析、log 函式、`.env` 讀取、shellcheck |
| `Template-TelegramBot` | Telegram 通知需求、Watcher 可共用 | python-telegram-bot、指令路由、webhook / polling 切換、Docker |

### 暫緩

| 模板 | 啟動時機 |
|---|---|
| `Template-PythonFastAPI` | 需要不含向量搜尋的一般 FastAPI 骨架時（可從 PythonVectorAPI 抽出 DB / Alembic / API Key 部分） |
| `Template-NextjsApp` | 需要 React 專屬生態（如 tldraw）時 |
| `Template-GoCLI` | 開始有 Go 專案時 |

## 維護建議

- **延續分層**：新模板建在既有模板之上（共用 `common_tool` Log 等），不從零複製
- **每個模板放 `CLAUDE.md`**：目前只有 FlaskAPI 有，補齊讓 AI 協作規則一致
- **共用 CI**：每個模板附 `.github/workflows`（lint + test + docker build）與 `pre-commit`
- **變數化建立**：用 copier / cookiecutter 在建立時替換專案名稱、port、是否啟用 DB，取代手動 `rm -rf .git` 後逐一修改

```bash
# copier 範例：由模板建立新專案
pipx install copier
copier copy <模板 repo URL> your-new-project
```

- **GitHub Template repository**：在 repo Settings 勾選 Template repository，可直接「Use this template」建立新專案
