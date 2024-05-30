# JavaScript Node 模組 wrangler(Cloudflare Workers 的命令行工具)

```
Wrangler 是一個用於管理和部署 Cloudflare Workers 的命令行工具。

它幫助開發者生成、測試和發布 Workers 代碼，並提供配置、監控和調試功能。通過 Wrangler，開發者可以更方便地在 Cloudflare 的全球邊緣網絡上運行和管理無伺服器應用。
```

## 目錄

- [JavaScript Node 模組 wrangler(Cloudflare Workers 的命令行工具)](#javascript-node-模組-wranglercloudflare-workers-的命令行工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [指令相關](#指令相關)
- [安裝](#安裝)
- [指令](#指令)
- [範例](#範例)
  - [在 Cloudflare Workers 中將請求回源](#在-cloudflare-workers-中將請求回源)

## 參考資料

[wrangler npm 頁面](https://www.npmjs.com/package/wrangler)

[wrangler 文檔](https://developers.cloudflare.com/workers/wrangler/)

[wrangler 指令](https://developers.cloudflare.com/workers/wrangler/commands/)

[wrangler.toml 配置](https://developers.cloudflare.com/workers/wrangler/configuration/)

### 指令相關

[wrangler deploy](https://developers.cloudflare.com/workers/wrangler/commands/#deploy)

# 安裝

```bash
# 安裝 Node.js 版本至少 16.x
npm install -g wrangler

# 更新
npm install -g wrangler@latest
```

# 指令

初始化專案

```bash
wrangler init js-work
```

部署到 cloudflare

```bash
wrangler deploy
    SCRIPT
        您的 Worker 的入口點的路徑。
        僅當您wrangler.toml不包含密鑰時才需要main（例如main = "index.js"）。
    --name  自選
        名稱。
    --env  自選
        在特定環境下執行。
    --compatibility-flags, --compatibility-flag  自選
        用於相容性檢查的標誌。
    --routes, --route  自選
        將部署此 Worker 的路線。
        例如：--route example.com/*。
    --node-compat  自選
        啟用 Node.js 相容性。
```


# 範例

## 在 Cloudflare Workers 中將請求回源

```JavaScript
addEventListener('fetch', event => {
  event.respondWith(handleRequest(event.request))
})

async function handleRequest(request) {
  // 原始伺服器的 URL
  const originUrl = 'https://example.com' + new URL(request.url).pathname

  // 設定回源的選項
  const fetchOptions = {
    method: request.method,
    headers: request.headers,
    body: request.method === 'POST' ? request.body : null
  }

  // 將請求發送回原始伺服器
  const response = await fetch(originUrl, fetchOptions)

  // 返回原始伺服器的響應
  return response
}
```