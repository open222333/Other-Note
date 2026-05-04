# Claude

## 目錄

- [介紹](#介紹)
- [官方資源](#官方資源)
- [模型系列](#模型系列)
- [Claude Code (CLI)](#claude-code-cli)
- [API](#api)
- [重要功能](#重要功能)

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
