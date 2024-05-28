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
- [用法](#用法)

## 參考資料

[Cloudflare Workers 網站](https://workers.cloudflare.com/)

[官方文檔](https://developers.cloudflare.com/workers/)

### 範例相關

[Sign requests](https://developers.cloudflare.com/workers/examples/signing-requests/)

[HTTP Basic Authentication](https://developers.cloudflare.com/workers/examples/basic-auth/)

# 安裝

安裝 wrangler CLI

```bash
npm install -g @cloudflare/wrangler
```

初始化一個新的 Cloudflare Workers 項目

```bash
wrangler generate my-worker
cd my-worker
```

部署到 Cloudflare Workers:

```bash
wrangler publish
```

# 用法

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
