# 程式設計 - 視覺UI工具模板(前端架構選型)

```
視覺類工具（產生器、編輯器、建構器、畫布）的可重用模板設計建議，以及 Next.js / Vue 3 / Flask 技術組合的選型比較。
```

## 目錄

- [程式設計 - 視覺UI工具模板(前端架構選型)](#程式設計---視覺ui工具模板前端架構選型)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [共用架構建議](#共用架構建議)
    - [版面架構](#版面架構)
    - [狀態管理](#狀態管理)
    - [Schema 驅動屬性面板](#schema-驅動屬性面板)
    - [畫布與互動](#畫布與互動)
    - [主題與設計系統](#主題與設計系統)
    - [匯入、匯出與保存](#匯入匯出與保存)
    - [開發體驗](#開發體驗)
    - [開發優先順序](#開發優先順序)
  - [模板功能類型](#模板功能類型)
  - [技術組合選擇](#技術組合選擇)
    - [Next.js 單獨使用](#nextjs-單獨使用)
    - [Next.js + Flask](#nextjs--flask)
    - [Vue 3 + Flask](#vue-3--flask)
    - [選擇建議](#選擇建議)
  - [React 與 Vue 生態對照](#react-與-vue-生態對照)

## 參考資料

[Next.js 官方文件](https://nextjs.org/docs)

[shadcn/ui](https://ui.shadcn.com/)

[shadcn-vue（Vue 移植版）](https://www.shadcn-vue.com/)

[React Flow（xyflow）](https://reactflow.dev/)

[Vue Flow](https://vueflow.dev/)

[程式設計 - SPA CSR SSR MPA SSG(前端框架)](程式設計_-_SPA_CSR_SSR_MPA_SSG(前端框架).md)

[程式設計 - 專案檔案結構(程式概念)](程式設計_-_專案檔案結構(程式概念).md)

[工作流程 - 專案模板(Template_Repo規劃)](工作流程_-_專案模板(Template_Repo規劃).md)

[JavaScript React 筆記](../01_程式語言/JavaScript/React(函式庫)/JavaScript_React_筆記.md)

[JavaScript Node 模組 Vue(前端框架)](../01_程式語言/JavaScript/NodeJs(執行環境)/Vue(前端框架)/JavaScript_Node_模組_Vue(前端框架).md)

[JavaScript Node 模組 Vite(前端開發工具)](../01_程式語言/JavaScript/NodeJs(執行環境)/Vue(前端框架)/JavaScript_Node_模組_Vite(前端開發工具).md)

[Python 模組 Flask(網站框架)](../01_程式語言/Python/Python_第三方模組/Python_Web應用框架(網站)/Python_Web_Flask(網站)/Python_模組_Flask(網站框架).md)

---

## 共用架構建議

> 先做共用核心（工作台版面、store、undo、schema 表單、匯出），各工具只替換「畫布」層。

### 版面架構

- 三欄工作台：左＝工具列 / 圖層、中＝畫布 / 預覽、右＝屬性面板
- shadcn `Resizable` 做可拖拉面板、`Sidebar` 做可收合側欄
- 上方 Toolbar（`Menubar` / `ToggleGroup` / `Tooltip`），下方可選 Status bar
- 手機版側欄改 `Sheet` / `Drawer`；或明確決定只支援桌面

### 狀態管理

- 編輯器狀態用 Zustand（React）/ Pinia（Vue），選擇性訂閱避免畫布整片重繪
- **Undo / Redo 一開始就設計**（`zundo` 或 command pattern），後補成本很高
- 狀態分三類：

| 類型 | 說明 | 是否保存 |
|---|---|---|
| 文件資料 | 使用者作品內容 | ✅ |
| UI 狀態 | 選取、zoom、開啟中的面板 | ❌（部分可同步到 URL，如 `nuqs`） |
| 暫時狀態 | 拖曳中、hover | ❌ |

### Schema 驅動屬性面板

- 以 Zod schema 定義每個元件 / 工具的屬性，自動產生表單
- 新增工具只寫 schema，不重刻 UI

| 屬性型別 | 對應元件 |
|---|---|
| number | `Slider` + `Input` |
| enum | `Select` / `ToggleGroup` |
| boolean | `Switch` |
| color | 自製 Color picker |

### 畫布與互動

| 工具性質 | 方案 |
|---|---|
| DOM 預覽（CSS / 元件產生器） | 直接 render |
| 節點 / 流程圖 | React Flow（xyflow） |
| 自由繪圖、大量物件 | Konva / Fabric.js / tldraw |
| 拖拉排序 | dnd-kit |

- 共通功能：縮放 / 平移、對齊格線、選取框
- 快捷鍵 `react-hotkeys-hook`，搭配 shadcn `Command` 做 ⌘K 指令面板

### 主題與設計系統

- shadcn 以 CSS 變數做主題，搭配 `next-themes` 支援深色模式
- 「工具本身 UI 主題」與「使用者作品主題」的 CSS 變數用不同前綴隔離
- 圖示統一 `lucide-react`；工具類介面偏緊湊，可將元件預設尺寸調小一號

### 匯入、匯出與保存

- 文件格式 JSON，加 `version` 欄位以便 migration
- 匯出：PNG / SVG（`html-to-image`）、程式碼複製（語法高亮）、JSON 下載
- 本地自動存檔：IndexedDB（`idb-keyval`），之後再接後端雲端存檔

### 開發體驗

- TypeScript strict、ESLint + Prettier、Tailwind class 排序
- Storybook 或 `/playground` 頁面測元件
- Vitest 測 store / undo 邏輯，Playwright 做 E2E

### 開發優先順序

```
版面 → Store + Undo → Schema 表單 → 一個範例工具 → 匯出與存檔 → 快捷鍵 / 指令面板
```

## 模板功能類型

| # | 類型 | 典型功能 | 核心技術 | 難度 |
|---|---|---|---|---|
| 1 | 產生器 / 設定器 | 調參數、即時預覽、複製程式碼（漸層、陰影、主題產生器） | Zod schema 表單、程式碼高亮 | ★ |
| 2 | 表單建構器 | 拖拉欄位、設定驗證、匯出 JSON / 程式碼 | dnd-kit、react-hook-form、Zod | ★★ |
| 3 | Dashboard 建構器 | 拖拉 widget 排版、選資料來源與圖表類型 | react-grid-layout、Recharts | ★★ |
| 4 | 頁面 / 區塊建構器 | 從區塊庫組 landing page、編輯文字樣式、匯出 | dnd-kit、樹狀資料結構、iframe 預覽 | ★★★ |
| 5 | 節點 / 流程編輯器 | 節點連線（workflow、AI pipeline、狀態機） | React Flow | ★★★ |
| 6 | 圖片 / 圖卡編輯器 | 文字 / 圖片 / 形狀圖層、套模板、匯出 PNG | Konva / Fabric.js | ★★★ |
| 7 | 白板 / 自由畫布 | 無限畫布、手繪、便利貼、多人協作 | tldraw，或自製 + Yjs | ★★★★ |
| 8 | 資料視覺化工具 | 上傳 CSV、選欄位、產生並匯出圖表 | Papa Parse、Recharts / ECharts | ★★ |

- 快速驗證模板架構 → 1
- 最通用的骨架（選取、圖層樹、屬性面板、undo）→ 4 或 5
- 1、2、4、6 可共用大部分核心；5、7 主要替換畫布層

## 技術組合選擇

### Next.js 單獨使用

Next.js 是全端框架，以前端為主，內建輕量後端能力：

- Route Handlers（`app/api/.../route.ts`）：REST API
- Server Actions：直接在 server 端執行存資料等邏輯
- Middleware：登入驗證、轉址
- 可直接連 DB（Prisma / Drizzle + PostgreSQL）
- 畫布函式庫用 `dynamic(..., { ssr: false })` 載入，避開 SSR 問題

| 需求 | Next.js 可否處理 |
|---|---|
| 編輯器 UI、預覽、匯出 | ✅ 純前端 |
| 本地存檔（IndexedDB） | ✅ 不需後端 |
| 登入、雲端存檔、分享連結 | ✅ Server Actions + DB + Auth.js |
| 呼叫 AI API（隱藏 API key） | ✅ Route Handler |
| 多人即時協作（WebSocket） | ⚠️ 需另外服務（Liveblocks / PartyKit / 自架） |
| 重運算、長任務、排程 | ⚠️ 建議獨立後端或 queue |

### Next.js + Flask

```
瀏覽器 → Next.js（UI、路由）→ rewrites 代理 → Flask（API、Python 運算）→ DB / 儲存 / AI 模型
```

- 適合：需要 Python 生態（Pillow、OpenCV、pandas、ML 模型）或已有 Python 後端
- 不適合：只有 CRUD / 登入 / 存檔（Next.js 內建即可，多一個服務多一份維護）
- 整合要點：
  - `next.config` 的 `rewrites` 將 `/api/py/*` 轉到 Flask，避開 CORS
  - 權責切清楚：Next.js 只做 UI，資料與邏輯統一放 Flask，避免兩邊寫 DB
  - 登入統一由一方負責（例如 Flask 發 JWT，存 httpOnly cookie）
  - Flask 產生 OpenAPI（flask-smorest / APIFlask），前端用 `openapi-typescript` 產生型別
  - 長任務交給 Celery / RQ + Redis，前端以輪詢或 SSE 查進度
- 若尚未定案，可考慮 FastAPI：內建 OpenAPI、Pydantic 驗證（對應 Zod）、原生 async / WebSocket

```
apps/
  web/        Next.js + shadcn/ui
  api/        Flask 或 FastAPI
packages/
  types/      由 OpenAPI 產生的共用型別
docker-compose.yml
```

### Vue 3 + Flask

視覺工具重前端互動、SSR 價值低，**Vite SPA + Flask API** 分工最單純。

| 項目 | Next.js + Flask | Vue 3 + Flask |
|---|---|---|
| 前端角色 | 有自己的 server 層，與 Flask 部分重疊 | 純 SPA，後端完全交給 Flask |
| SSR / SEO | 有 | 無（需要時改用 Nuxt） |
| 部署 | 兩個服務（Node + Python） | 前端 build 成靜態檔，一個 Python 服務即可 |
| 學習曲線 | React + App Router 概念較多 | 較易上手 |

- 開發：Vite `server.proxy` 將 `/api` 轉到 Flask
- 上線：`vite build` 後由 Flask 或 Nginx 提供靜態檔，`/api` 反向代理到 Flask（gunicorn）
- 同網域可直接用 Flask session cookie，比 JWT 簡單

```
frontend/          Vue 3 + Vite + TS
  src/
    components/ui/     shadcn-vue 元件
    features/<tool>/   各工具（schema、canvas、panel）
    stores/            Pinia
backend/           Flask
  app/
    api/               Blueprints
    services/          商業邏輯
    models/
docker-compose.yml
```

### 選擇建議

| 情境 | 建議組合 |
|---|---|
| 純前端工具、不需 Python | Next.js 或 Vue 3 單獨使用 |
| 需要 SEO、要用 React 專屬視覺套件（tldraw 等） | Next.js（+ Flask / FastAPI 視需求） |
| 團隊熟 Python、要架構單純與部署方便 | Vue 3 + Flask |
| 需要影像 / 資料 / AI 運算 | 任一前端 + Python 後端（Flask / FastAPI） |

## React 與 Vue 生態對照

| 需求 | React | Vue 3 |
|---|---|---|
| UI 元件 | shadcn/ui | shadcn-vue |
| 狀態管理 | Zustand | Pinia |
| Undo / Redo | zundo | VueUse `useRefHistory` / `useManualRefHistory` |
| 表單 + 驗證 | react-hook-form + Zod | VeeValidate + Zod |
| 拖拉 | dnd-kit | vue-draggable-plus |
| 流程圖 | React Flow | Vue Flow |
| 畫布 | react-konva | vue-konva |
| 快捷鍵 | react-hotkeys-hook | VueUse `useMagicKeys` |
| URL 狀態 | nuqs | Vue Router query + VueUse |

- tldraw 僅有 React 版，白板類模板在 Vue 選擇較少
