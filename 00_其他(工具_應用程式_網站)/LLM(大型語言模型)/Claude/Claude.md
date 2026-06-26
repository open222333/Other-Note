# Claude

## 目錄

- [介紹](#介紹)
- [官方資源](#官方資源)
- [模型系列](#模型系列)
- [Claude Code (CLI)](#claude-code-cli)
- [API](#api)
- [常見連線錯誤](#常見連線錯誤)
- [重要功能](#重要功能)
- [例外狀況](#例外狀況)

## 介紹

Claude 是由 [Anthropic](https://www.anthropic.com/) 開發的大型語言模型，以安全性 (AI Safety)、可靠性與理解深度為核心設計理念。

## 官方資源

[Claude 官方網站](https://claude.ai/)

[Anthropic 官網](https://www.anthropic.com/)

[Claude 模型總覽 (官方文件)](https://docs.anthropic.com/en/docs/about-claude/models/overview)

[Claude API 文件](https://docs.anthropic.com/)

[Claude Code 文件](https://docs.anthropic.com/en/docs/claude-code/overview)

## 模型系列

### Claude 4（最新）

| 模型 | Model ID | 特性 |
|------|----------|------|
| Claude Opus 4.7 | `claude-opus-4-7` | 最高能力，適合高複雜度任務 |
| Claude Sonnet 4.6 | `claude-sonnet-4-6` | 效能與速度平衡，日常首選 |
| Claude Haiku 4.5 | `claude-haiku-4-5-20251001` | 輕量快速，適合簡單高頻任務 |

> 開發應用時預設使用最新最強的模型。

## Claude Code (CLI)

Claude Code 是 Anthropic 官方的 CLI 工具，讓開發者可在終端機中以自然語言進行程式碼開發、除錯、重構等任務，並深度整合 git 工作流程。

提供形式：CLI、桌面應用程式（Mac/Windows）、Web（claude.ai/code）、IDE 擴充（VS Code、JetBrains）

### 安裝

```bash
npm install -g @anthropic-ai/claude-code
```

### 主要功能

- 多檔案上下文理解與修改
- Git 整合（commit、diff、PR）
- MCP（Model Context Protocol）Server 整合
- 自訂 Slash Commands
- Hooks（事件觸發的 shell 指令）
- 並行 Sub-agent 任務執行

## API

[Anthropic API 文件](https://docs.anthropic.com/en/api/)

[Python SDK](https://github.com/anthropics/anthropic-sdk-python)

[TypeScript/Node.js SDK](https://github.com/anthropics/anthropic-sdk-typescript)

### 重要 API 功能

| 功能 | 說明 |
|------|------|
| **Prompt Caching** | 快取重複的 system prompt / 文件，大幅降低成本與延遲 |
| **Tool Use** | 讓模型呼叫自定義工具或外部 API |
| **Extended Thinking** | 啟用內部推理步驟，提升複雜問題的準確性 |
| **Batch API** | 非同步批次處理，成本降低 50% |
| **Files API** | 上傳檔案並跨請求引用 |
| **Citations** | 引用來源段落，提升回答可信度 |

## 常見連線錯誤

開啟 Claude（claude.ai）或使用 Claude Code / VS Code 擴充功能時若出現連線錯誤，通常是本機網路或瀏覽器設定問題，與 Claude 服務本身無關。

### ERR_CONNECTION_RESET / ECONNRESET

瀏覽器出現 `ERR_CONNECTION_RESET`，或 VS Code 擴充功能出現 `API Error: Unable to connect to API (ECONNRESET)`。

**根本原因（最常見）：瀏覽器 Proxy 擴充功能**

Claude Code 登入採用 OAuth 流程：`claude.com` → 307 redirect → `claude.ai`

若瀏覽器安裝了 Proxy / VPN 擴充功能（如 SwitchyOmega），會攔截 `claude.ai` 流量導致：

```
claude.ai 被 Proxy 攔截 → ERR_CONNECTION_RESET
→ OAuth 登入頁面無法載入
→ VS Code 擴充功能認證失敗 → ECONNRESET
```

**解決方式**

1. 開啟 `chrome://extensions/`
2. 找到 Proxy / VPN 類擴充功能（SwitchyOmega 等）
3. 關閉或停用
4. 重新在 VS Code 執行 Claude 登入流程

**排錯優先順序**

1. 先確認基本連線是否正常

```bash
# 能回傳 4xx 代表網路可達 api.anthropic.com
curl -v https://api.anthropic.com/v1/messages \
  -H "x-api-key: test" \
  -H "anthropic-version: 2023-06-01"
```

2. 確認 OAuth redirect 是否正常

```bash
curl -v "https://claude.com/cai/oauth/authorize" 2>&1 | grep location
# 應看到：location: https://claude.ai/oauth/authorize
```

3. 測試 claude.ai 連線

```bash
curl -s -o /dev/null -w "%{http_code}\n" https://claude.ai
# 應回傳 200 或 403；若 ECONNRESET 代表被 Proxy 攔截
```

4. **檢查瀏覽器 Proxy / VPN 擴充功能**（最常見原因，應提前排查）

5. 若以上均正常仍有問題，再清除網路快取

**Windows**

```powershell
netsh winsock reset
netsh int ip reset
ipconfig /flushdns
```

> 執行後重新開機。

**macOS**

```bash
sudo dscacheutil -flushcache; sudo killall -HUP mDNSResponder
```

**已排除的常見誤判原因**

| 假設 | 排除方式 |
|------|----------|
| 網路不穩定 | curl 多次測試全部正常 |
| ISP 封鎖 | traceroute 路由完整 |
| DNS 封鎖 | 換 IP 直連仍可到達 |
| Claude Code 版本 | 重裝無法解決 OAuth 問題 |

### DNS_PROBE_FINISHED_NXDOMAIN

DNS 無法解析 `claude.ai`，瀏覽器找不到伺服器。

**Windows**

```powershell
ipconfig /flushdns
nslookup claude.ai
```

**macOS**

```bash
sudo dscacheutil -flushcache; sudo killall -HUP mDNSResponder
dig claude.ai
```

其他排查步驟：
- 換用 DNS `8.8.8.8`（Google）或 `1.1.1.1`（Cloudflare）
- 關閉 VPN / Proxy 後重試
- 以無痕模式或其他瀏覽器測試

> 詳細說明見 [NetWork_筆記.md](../../../00_概念/網路/NetWork_筆記.md)

## 重要功能

### Extended Thinking（延伸思考）

Claude 在回答前進行內部推理（`<thinking>` 區塊），適合數學、邏輯推理、複雜分析等需要多步驟思考的任務。

### Artifacts

在 Claude.ai 網頁介面中，Claude 可產生可互動的輸出內容，包含程式碼、SVG 圖表、React 元件、完整網頁等，可即時預覽與編輯。

### Projects

在 Claude.ai 建立專案（Project），可持久化儲存：

- **System Prompt**（自訂角色與行為規則）
- **知識文件**（上傳 PDF、程式碼、規格書等）

讓 Claude 在每次對話中保持一致的上下文，適合長期協作的工作專案。

### Model Context Protocol (MCP)

MCP 是 Anthropic 提出的開放標準，讓 Claude（及其他 LLM）能以統一介面連接外部資料來源與工具（如資料庫、API、檔案系統等）。

[MCP 官方文件](https://modelcontextprotocol.io/)

---

# 例外狀況

## VS Code 擴充功能 ECONNRESET（Chrome Proxy 攔截 OAuth）

**現象：**
- VS Code Claude 擴充功能出現 `API Error: Unable to connect to API (ECONNRESET)`
- Chrome 開啟 Claude 登入頁面出現 `ERR_CONNECTION_RESET`
- curl 測試 `api.anthropic.com` 正常，traceroute 路由完整

**根本原因：**

Claude Code 登入採用 OAuth 流程，`claude.com` 會 307 redirect 到 `claude.ai`：

```
claude.com/cai/oauth/authorize → 307 → claude.ai/oauth/authorize
```

若瀏覽器安裝了 Proxy 擴充功能（SwitchyOmega 等）並攔截 `claude.ai`：

```
claude.ai 被 Proxy 攔截 → ERR_CONNECTION_RESET
→ OAuth 登入頁面無法載入
→ VS Code 擴充功能認證失敗 → ECONNRESET
```

**解決方式：**

1. 開啟 `chrome://extensions/`
2. 找到 Proxy / VPN 類擴充功能，關閉開關
3. 重新在 VS Code 執行 Claude 登入流程

**診斷指令：**

```bash
# 確認 api.anthropic.com 可達（回傳 4xx 代表正常）
curl -v https://api.anthropic.com/v1/messages \
  -H "x-api-key: test" \
  -H "anthropic-version: 2023-06-01"

# 確認 OAuth redirect 是否正常
curl -v "https://claude.com/cai/oauth/authorize" 2>&1 | grep location
# 應看到：location: https://claude.ai/oauth/authorize

# 測試 claude.ai 連線（被 Proxy 攔截時會 ECONNRESET，正常應回傳 200/403）
curl -s -o /dev/null -w "%{http_code}\n" https://claude.ai
```

**已排除的誤判原因：**

| 假設 | 排除原因 |
|------|----------|
| 網路不穩定 | curl 多次測試全部正常 |
| ISP 封鎖 | traceroute 路由完整到達目標 IP |
| DNS 封鎖 | 換 IP 直連仍可到達 |
| Claude Code 版本問題 | 重裝無法解決 OAuth 問題 |
