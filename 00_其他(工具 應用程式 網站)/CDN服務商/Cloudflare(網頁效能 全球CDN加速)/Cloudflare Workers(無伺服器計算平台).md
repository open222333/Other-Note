# Cloudflare Workers(無伺服器計算平台)

```
Cloudflare Workers 是一種無伺服器計算平台，允許開發者在 Cloudflare 的邊緣網絡上運行 JavaScript、TypeScript、Rust 和 WebAssembly 代碼。

這個平台使得開發者可以在靠近用戶的地方處理請求，從而減少延遲，提高應用的性能和可靠性。

Cloudflare Workers 的一些關鍵特點和用途：

邊緣計算:

    Cloudflare Workers 在 Cloudflare 的全球數據中心網絡中運行，這意味著你的代碼會在離用戶最近的地方執行，減少請求的往返時間和延遲。

無伺服器架構:

    你不需要管理伺服器，配置基礎設施，或者處理擴展問題。Cloudflare Workers 會自動處理擴展和資源分配。

高性能:

    Cloudflare Workers 設計用於快速啟動和高效執行，通常用於處理 HTTP 請求和響應。

廣泛的使用場景:

    內容緩存和優化: 動態調整和緩存內容，減少伺服器負載。
    安全和訪問控制: 實現自定義的安全策略和訪問控制。
    API 閘道: 構建和管理 API 請求的路由和處理。
    邊緣計算: 在邊緣進行計算和數據處理，減少中央伺服器的負擔。

支持多種語言和工具:

    主要支持 JavaScript 和 TypeScript，但也支持通過 WebAssembly 運行其他語言的代碼，比如 Rust 和 Python。
```

## 目錄

- [Cloudflare Workers(無伺服器計算平台)](#cloudflare-workers無伺服器計算平台)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [範例相關](#範例相關)
- [安裝](#安裝)
- [指令](#指令)
- [範例](#範例)
  - [基本](#基本)

## 參考資料

[Cloudflare Workers 網站](https://workers.cloudflare.com/)

[官方文檔](https://developers.cloudflare.com/workers/)

### 範例相關

[Sign requests](https://developers.cloudflare.com/workers/examples/signing-requests/)

[HTTP Basic Authentication](https://developers.cloudflare.com/workers/examples/basic-auth/)

[JavaScript Node 模組 wrangler(Cloudflare Workers 的命令行工具)](https://github.com/open222333/Other-Note/blob/main/01_%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/JavaScript/NodeJs(%E5%9F%B7%E8%A1%8C%E7%92%B0%E5%A2%83)/JavaScript%20Node%20%E6%A8%A1%E7%B5%84%20wrangler(Cloudflare%20Workers%20%E7%9A%84%E5%91%BD%E4%BB%A4%E8%A1%8C%E5%B7%A5%E5%85%B7).md)

# 安裝

安裝 wrangler CLI

```bash
npm install -g @cloudflare/wrangler

npm install -g wrangler
```

# 指令

初始化一個新的 Cloudflare Workers 項目

```bash
wrangler generate my-worker
cd my-worker
```

部署到 Cloudflare Workers:

```bash
wrangler publish
```

初始化

```bash
wrangler init
```

在使用 Cloudflare Wrangler 初始化專案時，您會被問到要建立哪種類型的應用程式。以下是各選項的詳細解說：

"Hello World" Worker

```
這是一個最基本的範例 Worker，它返回一個簡單的 "Hello World" 訊息。
適合初學者瞭解 Cloudflare Workers 的基礎功能。
```

"Hello World" Worker (Python)

```
類似於上述的 "Hello World" Worker，但使用 Python 編寫，而不是預設的 JavaScript。
這對於希望使用 Python 開發 Cloudflare Workers 的開發者非常有用。
```

"Hello World" Durable Object

```
這是一個使用 Durable Objects 的範例 Worker。
Durable Objects 是 Cloudflare 的一種持久存儲機制，適合需要持久存儲和狀態管理的應用程式。
```

Website or web app

```
這個選項適用於希望在 Cloudflare Workers 上託管完整網站或 Web 應用程式的開發者。
Wrangler 會生成一個適合託管網站的專案結構。
```

Example router & proxy Worker

```
這是一個範例 Worker，演示了如何使用 Cloudflare Workers 作為路由器和代理。
適合需要處理請求路由和代理轉發的應用場景。
```

Scheduled Worker (Cron Trigger)

```
這個選項建立一個定時任務 Worker，利用 Cloudflare Workers 的 Cron Triggers 功能定期運行程式碼。
適合需要定時執行任務的應用程式。
```

Queue consumer & producer Worker
這個選項建立一個隊列消費者和生產者 Worker。適合需要處理消息隊列的應用場景，如任務調度、異步處理等。

API starter (OpenAPI compliant)

```
這個選項建立一個符合 OpenAPI 標準的 API 專案範本，適合希望構建和發布符合 OpenAPI 規範的 API 服務的開發者。
```

Worker built from a template hosted in a git repository

```
這個選項允許您從託管在 Git 存儲庫中的範本建立 Worker。
適合需要使用特定範本或已有範本的開發者。
```

詳細說明 Queue consumer & producer Worker

```
選擇 "Queue consumer & producer Worker" 會生成一個範例專案，該專案展示了如何在 Cloudflare Workers 中使用隊列來生產和消費消息。
這對於需要異步任務處理和隊列管理的應用程式非常有用。
```

範例步驟

選擇隊列消費者和生產者 Worker：

```sh
wrangler init
```

然後選擇 "Queue consumer & producer Worker"。

生成的文件結構：

    該專案將包括配置隊列、生產消息的程式碼，以及消費消息的程式碼範例。

配置和使用：

    配置 Cloudflare Workers 環境。
    編寫生產者程式碼，將消息放入隊列。
    編寫消費者程式碼，從隊列中消費消息。


# 範例

## 基本

修改 src/index.js 文件

```JavaScript
addEventListener('fetch', event => {
    event.respondWith(handleRequest(event.request))
})

async function handleRequest(request) {
    return new Response('Hello World', { status: 200 })
}
```

配置 wrangler.toml 文件

```toml
name = "my-worker"
type = "javascript"

account_id = "your-cloudflare-account-id"
workers_dev = true
```
