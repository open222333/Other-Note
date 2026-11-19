# 工作流程 - 專案模板待優化(Template_Repo檢查清單)

```
個人 Template-* 專案模板逐一檢查後的待優化清單：建議先修的問題、各模板共通項、各模板個別項目。
```

> 檢查日期：2026-11-16。完成一項就勾選；全部完成的章節可直接刪除（歷史交給 Git）。

## 目錄

- [工作流程 - 專案模板待優化(Template_Repo檢查清單)](#工作流程---專案模板待優化template_repo檢查清單)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [檢查範圍](#檢查範圍)
  - [建議先修](#建議先修)
  - [各模板共通](#各模板共通)
  - [各模板個別](#各模板個別)
    - [Template-Repo](#template-repo)
    - [Template-DockerRepo](#template-dockerrepo)
    - [Template-PythonRepo](#template-pythonrepo)
    - [Template-PythonToolRepo](#template-pythontoolrepo)
    - [Template-PythonWatcher](#template-pythonwatcher)
    - [Template-PythonCrawler](#template-pythoncrawler)
    - [Template-Backup](#template-backup)
    - [Template-PythonVectorAPI](#template-pythonvectorapi)
    - [Template-PythonFlaskAPI](#template-pythonflaskapi)
    - [Template-VisualTool](#template-visualtool)
    - [Template-ReactDashboard](#template-reactdashboard)
  - [建議處理順序](#建議處理順序)

## 參考資料

[工作流程 - 專案模板(Template_Repo規劃)](工作流程_-_專案模板(Template_Repo規劃).md)

[pip-audit（Python 套件弱點掃描）](https://pypi.org/project/pip-audit/)

[ruff（Python linter）](https://docs.astral.sh/ruff/)

---

## 檢查範圍

| 項目 | 結果 |
|---|---|
| 測試 | 11 個模板全部通過（VectorAPI 有 6 個需要資料庫的測試被跳過） |
| lint | Python 用 ruff、React 用 oxlint，沒有實質錯誤 |
| 套件弱點 | Python 用 `pip-audit`、前端用 `npm audit` |
| 程式碼 | 讀過主要程式碼、Dockerfile、docker-compose |
| 沒做的 | 沒有實際 build Docker 映像；沒有接真的資料庫 |

```bash
# 重新檢查時使用的指令
python -m pytest -q
ruff check --select E,F,W,B,UP,SIM,S --ignore E501,S101 .
pip-audit -r requirements.txt
npm audit --package-lock-only
npm outdated
```

## 建議先修

會造成功能失效、漏通知或安全風險的項目：

- [ ] **PythonCrawler：Playwright 版本對不上**
  - Docker 映像固定 `v1.48.0`，但 `requirements-playwright.txt` 是 `playwright>=1.48`，會裝到較新的版本，與映像內的瀏覽器不相容，Playwright 模式很可能無法啟動（未實際 build 驗證）
  - 做法：改成 `playwright==<與映像相同的版本>`，兩邊一起升級
- [ ] **PythonCrawler：403 / 404 被算成成功**
  - 只有 429、5xx 會重試；403（被擋）會當成正常頁面交給 `parse()`，統計顯示 0 失敗，代理也不會被標記失效
  - 做法：4xx 計入 `failed`；403 / 407 標記代理失效並換代理重試
- [ ] **PythonWatcher：通知送失敗就漏掉告警**
  - `state.update()` 在送出前就記成已通知，`send_all()` 的回傳值沒被使用；網路斷線時告警要等 `REMIND_INTERVAL` 才會再送，恢復通知不會補送
  - 做法：送出成功才更新 `last_notified`，失敗的下一輪重送
- [ ] **PythonWatcher：失敗一次就告警**
  - 沒有「連續失敗 N 次才告警」，單次逾時就會發通知
  - 做法：`[check:*]` 加 `fail_threshold`（預設 2～3）
- [ ] **PythonFlaskAPI：套件有已知弱點**
  - `pymongo 4.10.1`、`requests 2.32.3`、`dnspython 2.8.0` 共 11 筆
  - 前端 `npm audit` 7 筆（6 筆高風險），Vite 還在 4.x
- [ ] **PythonFlaskAPI：資料庫密碼有弱預設值**
  - 沒設 `.env` 時會用 `root_password`、`flask_password`、`redis_password` 啟動
  - 做法：比照 VectorAPI 用 `${MYSQL_ROOT_PASSWORD:?請在 .env 設定}`，沒設就拒絕啟動
- [ ] **PythonFlaskAPI：Swagger 設定寫死個人聯絡資訊與測試網址**
  - `app/__init__.py` 的 `SWAGGER_TEMPLATE`（title、contact、termsOfService）每個新專案都會帶著
  - 做法：改讀設定檔，並可用環境變數在正式環境關閉 Swagger UI
- [ ] **Backup：dump 指令輸出大量錯誤訊息時可能卡死**
  - `run_pipeline()` 等 gzip 結束後才讀 dump 指令的 stderr，緩衝區滿了兩邊會互等
  - 做法：stderr 改寫入暫存檔，或用執行緒同時讀取

## 各模板共通

- [ ] **加 CI**：每個模板附 `.github/workflows`（lint + test + docker build）；VectorAPI 需要 pgvector service 才能跑那 6 個被跳過的測試
- [ ] **打版本 tag**：每個模板打 `v1.0.0`，`copier update` 靠 tag 判斷新版本
- [ ] **鎖定依賴版本**：Python 模板都是 `>=`
  - FlaskAPI 混用 `==` 與 `>=`：舊版 pip 安裝失敗；新版 pip 因 `redis==5.2.1` 被迫退回舊的 celery
  - 做法：`pip-compile` 或 `uv lock` 產生鎖定檔
- [ ] **掛載目錄權限**：容器以 uid 1001 執行，但 `./logs`、`./data` 掛載主機目錄，Linux 主機上很可能寫不進去（未實際重現）；README 補上說明

```bash
# Linux 主機：讓容器內的 uid 1001 可寫入掛載目錄
mkdir -p logs data && sudo chown -R 1001:1001 logs data
```

- [ ] **抽出重複程式碼**
  - `src/__init__.py` 的 `get_setting()` 在 7 個 Python 模板各一份
  - Watcher 與 Backup 的 `src/notify/`、`src/log.py` 完全相同 → 可放進 `Python-CommonTool`
  - VisualTool 與 ReactDashboard 各有一套 `components/ui/`、`theme-provider.tsx`
- [ ] **更新 README「相關模板」表格**：Repo、DockerRepo、PythonRepo、PythonToolRepo、FlaskAPI 只列 5 個，少了後來的 6 個
- [ ] **補 `CLAUDE.md`**：目前只有 FlaskAPI 有
- [ ] **`get_setting()` 的環境變數覆寫只看 key**：不同 section 的同名 key 會互相覆蓋，`INTERVAL`、`TIMEOUT` 這類通用名稱也容易與系統環境變數撞名

## 各模板個別

### Template-Repo

- 內容精簡，只有上面的共通項

### Template-DockerRepo

- [ ] 基礎映像 `alpine:3.20` 偏舊
- [ ] 範例以 root 執行，補上非 root 使用者的寫法
- [ ] compose 補 healthcheck 範例

### Template-PythonRepo

- [ ] `LOG_LEVEL` 填錯會直接崩潰（`logging.basicConfig` 拋例外）→ 無效值退回 `INFO`
- [ ] compose：`./conf` 改唯讀掛載（`:ro`）、補 `restart`
- [ ] `docker/.gitignore` 忽略 `docker/logs`、`docker/data`，但 compose 掛的是 `./logs`，是用不到的殘留
- [ ] 加 `pyproject.toml`（ruff 設定）

### Template-PythonToolRepo

- [ ] 測試沒涵蓋用到 `common_tool` 的 `src/__main__.py`
- [ ] submodule 沒初始化時映像 build 得過，但執行才失敗 → Dockerfile 加檢查
- [ ] 與 PythonRepo 幾乎完全重複，之後改動要兩邊同步

### Template-PythonWatcher

- [ ] 檢查逐一執行，項目多又逾時會超過 `INTERVAL`，healthcheck 可能誤判 → 改用 `ThreadPoolExecutor`
- [ ] `docker stop` 要等 10 秒：Python 當 PID 1 沒處理 SIGTERM → compose 加 `init: true` 或自行處理訊號
- [ ] 設定檔刪掉的檢查項目仍留在 `state.json`
- [ ] 可加的檢查類型：TLS 憑證到期日、HTTP 回應內容關鍵字

### Template-PythonCrawler

- [ ] 中斷後不能續跑（`seen` 只在記憶體）；重跑會在 JSONL / CSV 產生重複資料
- [ ] Playwright：每組（UA、代理）各開一個 context，cookie 不共用，換 UA 時登入狀態會斷；結束時只存第一個 context 的狀態
- [ ] 讀 robots.txt 沒有逾時，也不走代理與速率限制
- [ ] SQLite：沒設定 `OUTPUT_KEY` 時 `item_key` 為 NULL，重跑會重複；每筆 commit 一次較慢
- [ ] CSV 欄位以第一筆為準，之後出現的新欄位會被靜默丟棄
- [ ] `DEFAULT_USER_AGENT` 的 Chrome 版本偏舊

### Template-Backup

- [ ] 上傳前沒加密（資料庫 dump 以明文上雲）→ `age` / `gpg`，或 rclone crypt remote
- [ ] 沒有防止重複執行的鎖（每小時排程可能重疊）→ `flock`
- [ ] Mongo 工具的下載網址寫死 x86_64，ARM 主機裝不了
- [ ] 非 root（uid 1001）可能讀不到主機上權限較嚴的檔案，`files` 類型會失敗
- [ ] 只支援「保留最新 N 份」，可加每日 / 每週 / 每月分層保留
- [ ] 上傳後沒有驗證（`rclone check`）；備份檔大小驟降時沒有警告

### Template-PythonVectorAPI

- [ ] `DocumentIn.content` 沒有長度上限，大文件會讓單一請求佔住很久
- [ ] 用 id 取得 / 刪除文件時不檢查 `workspace_id`
- [ ] Embedding 請求沒有重試（429 / 5xx）
- [ ] API 容器沒有 healthcheck；Ollama 映像用 `latest`
- [ ] 多副本同時啟動時 `alembic upgrade head` 會競爭

### Template-PythonFlaskAPI

- [ ] `app/templates/admin/index.html`（859 行）沒有任何地方引用，可刪
- [ ] 登入沒有帳號鎖定（只有每 IP 每分鐘 10 次）；帳號不存在時不跑 bcrypt，回應時間可分辨帳號是否存在
- [ ] 改密碼後舊 token 仍有效（沒有撤銷機制）
- [ ] 登入失敗會把未限制長度的 username 寫進操作紀錄
- [ ] 前端沒有測試、沒有 lint
- [ ] `requirements-test.txt` 沒有 `-r requirements.txt`（其他模板都有）
- [ ] 執行期映像包含 `frontend/` 原始碼與 `tests/`
- [ ] `/` 健康檢查不檢查資料庫連線
- [ ] README 標題與目錄樹仍是 `Python-FlaskAPI`
- [ ] 映像版本可更新：`nginx:1.26`、`mongo:7`

### Template-VisualTool

- [ ] 整包約 639 KB（gzip 約 196 KB）沒有拆分 → `html-to-image`、各工具改 lazy 載入
- [ ] 只測核心邏輯（8 個測試），沒有元件測試
- [ ] 匯入 JSON 時按取消，`pickJsonFile()` 的 Promise 不會結束
- [ ] nginx 沒有安全標頭；compose 沒有 healthcheck

### Template-ReactDashboard

- [ ] 有新的大版本：`react-router` 8、`@tanstack/react-table` 9、`@hookform/resolvers` 7
- [ ] `docker/nginx.conf.template` 的 `location /api/` 寫死，改了 `VITE_API_BASE_URL` 會對不上
- [ ] nginx 沒有安全標頭（CSP、`X-Frame-Options`、`X-Content-Type-Options`）；compose 沒有 healthcheck
- [ ] token 存 `localStorage`（README 已註明，改 httpOnly cookie 較安全）
- [ ] 沒有 E2E 測試

## 建議處理順序

1. 「建議先修」8 項
2. 共通項：CI、版本 tag、鎖定依賴版本
3. 共通項：抽出重複程式碼（之後修一處即可）
4. 各模板個別項目
