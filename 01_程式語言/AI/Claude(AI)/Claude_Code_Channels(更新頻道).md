# Claude Code Channels

```
透過外部平台（Telegram、Discord、iMessage 等）即時推送訊息至正在執行的 Claude Code session
```

## 目錄

- [Claude Code Channels](#claude-code-channels)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [概念](#概念)
- [支援的 Channel](#支援的-channel)
- [安裝與設定](#安裝與設定)
  - [Fakechat（測試用）](#fakechat測試用)
  - [Telegram](#telegram)
  - [Discord](#discord)
  - [多 Channel 同時啟用](#多-channel-同時啟用)
- [運作方式](#運作方式)
  - [事件格式](#事件格式)
  - [雙向溝通（Permission Relay）](#雙向溝通permission-relay)
- [存取控制](#存取控制)
- [限制](#限制)
- [自訂 Channel](#自訂-channel)
- [與 Claude Dispatch 的差別](#與-claude-dispatch-的差別)

## 參考資料

[Claude Code Channels 文件](https://docs.anthropic.com/en/docs/claude-code/channels)

[Claude Code Channels Reference](https://docs.anthropic.com/en/docs/claude-code/channels-reference)

[claude-plugins-official GitHub](https://github.com/anthropics/claude-plugins-official)

# 概念

```
外部平台（Telegram / Discord / iMessage）
        ↓
Channel MCP Server（本機執行）
        ↓
Claude Code Session（推送事件）
```

- 將外部訊息、警報、Webhook **即時推入**正在執行的 Claude Code session
- Claude 可在本機環境中直接對事件作出反應（讀寫檔案、執行指令）
- 目前為 **Research Preview** 階段（需 claude.ai 帳號登入，不支援 Console / API Key）

# 支援的 Channel

| Channel | 說明 | 平台限制 |
|---------|------|----------|
| `fakechat` | 本機測試用，開啟 `localhost:8787` | 無 |
| `telegram` | DM Bot 發訊息給 Claude | 無 |
| `discord` | DM Bot 發訊息給 Claude | 無 |
| `imessage` | 直接讀取 Messages 資料庫 | macOS 限定 |

# 安裝與設定

## Fakechat（測試用）

```bash
# 安裝
/plugin install fakechat@claude-plugins-official
/reload-plugins

# 啟動（開啟 http://localhost:8787 即可輸入訊息）
claude --channels plugin:fakechat@claude-plugins-official
```

## Telegram

```bash
# 1. 至 BotFather 建立 Bot，取得 Token
# 2. 安裝 plugin
/plugin install telegram@claude-plugins-official
/reload-plugins

# 3. 設定 Token
/telegram:configure <BOTFATHER_TOKEN>

# 4. 啟動（帶 --channels 旗標）
claude --channels plugin:telegram@claude-plugins-official

# 5. 傳訊息給 Bot，取得 pairing code 後核准
/telegram:access pair <CODE>

# 6. 鎖定只允許自己（建議）
/telegram:access policy allowlist
```

## Discord

```bash
/plugin install discord@claude-plugins-official
/reload-plugins
/discord:configure <DISCORD_BOT_TOKEN>
claude --channels plugin:discord@claude-plugins-official
/discord:access pair <CODE>
/discord:access policy allowlist
```

## 多 Channel 同時啟用

```bash
claude --channels plugin:telegram@claude-plugins-official plugin:discord@claude-plugins-official
```

# 運作方式

## 事件格式

外部訊息到達時，Claude 以 XML tag 接收：

```xml
<channel source="telegram" chat_id="12345" sender="username">
  使用者傳送的訊息內容
</channel>
```

## 雙向溝通（Permission Relay）

啟用雙向 Channel 後，Claude 需要工具核准時可轉發至外部平台：

1. Claude 產生含 5 碼 ID 的核准請求，推送至 Channel
2. 使用者在手機回覆 `yes <id>` 或 `no <id>`
3. 本機終端機或遠端，先回覆者生效

```bash
# Claude 回覆工具（傳回外部平台，不顯示於終端機）
reply "任務完成，共修改 3 個檔案"
```

# 存取控制

| 指令 | 說明 |
|------|------|
| `/<channel>:access pair <code>` | 核准新裝置 pairing code |
| `/<channel>:access policy allowlist` | 只允許 allowlist 內的 sender |
| `/<channel>:access policy open` | 允許所有人（不建議） |
| `/mcp` | 查看目前 Channel 連線狀態 |

# 限制

- 僅在 session **開啟期間**接收事件（關閉後不會排隊）
- 需明確加上 `--channels` 旗標才會啟用
- 僅支援 **claude.ai 帳號**登入（Console / API Key 不支援）
- 回覆訊息不顯示於終端機，只傳回來源平台

# 自訂 Channel

實作 MCP Server 並宣告 `claude/channel` capability：

```typescript
import { Server } from '@modelcontextprotocol/sdk/server/index.js'
import { StdioServerTransport } from '@modelcontextprotocol/sdk/server/stdio.js'

const mcp = new Server(
  { name: 'my-channel', version: '0.0.1' },
  {
    capabilities: { experimental: { 'claude/channel': {} } },
    instructions: '訊息以 <channel source="my-channel"> 格式傳入，請依內容執行對應任務。',
  },
)

await mcp.connect(new StdioServerTransport())

// 推送事件給 Claude
await mcp.notification({
  method: 'notifications/claude/channel',
  params: {
    content: '警報：部署失敗，請檢查 logs',
    meta: { severity: 'high', alert_id: 'deploy-001' },
  },
})
```

**註冊至 `.mcp.json`：**

```json
{
  "mcpServers": {
    "my-channel": { "command": "bun", "args": ["./my-channel.ts"] }
  }
}
```

**啟動（Research Preview 需加旗標）：**

```bash
claude --dangerously-load-development-channels server:my-channel
```

# 與 Claude Dispatch 的差別

| | Channels | Dispatch |
|---|---|---|
| **方向** | 外部 → 推入 Claude session | Claude → 對外派出子 Agent |
| **發起者** | 外部平台（Telegram 等）主動推送 | 程式碼（Orchestrator）主動分派 |
| **執行模型** | 單一 session 接收事件後處理 | 多個子 Agent 並行執行 |
| **互動方式** | 即時、事件驅動 | 非即時、任務導向 |
| **使用場景** | 遠端控制、監控警報、手機發指令 | 大型任務拆解、並行分析 |
| **技術基礎** | MCP Channel Plugin | Claude Agent SDK（Python） |
| **需要登入** | 需 claude.ai 帳號 | API Key 即可 |

> **簡單記憶**：Channels 是外部「傳話」進來給 Claude；Dispatch 是 Claude 主動「派工」給多個 Agent。
