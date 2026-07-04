# Claude Code 專案設定

```
讓 Claude Code 在每次 session 中依照專案規則行動
設定方式：CLAUDE.md（專案根目錄）、settings.json（.claude/）
```

## 目錄

- [Claude Code 專案設定](#claude-code-專案設定)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [運作原理](#運作原理)
- [CLAUDE.md](#claudemd)
  - [載入順序](#載入順序)
  - [常見指令內容](#常見指令內容)
  - [讓每次新增筆記都依照範本](#讓每次新增筆記都依照範本)
- [settings.json](#settingsjson)
  - [權限設定](#權限設定)
  - [常用欄位](#常用欄位)
- [Hooks](#hooks)
  - [事件類型](#事件類型)
  - [範例：新增筆記後自動執行 linter](#範例新增筆記後自動執行-linter)
- [Slash Commands（自訂指令）](#slash-commands自訂指令)
  - [建立方式](#建立方式)
  - [範例](#範例)
- [MCP Server 整合](#mcp-server-整合)

## 參考資料

[Claude Code 官方文件](https://docs.anthropic.com/en/docs/claude-code/overview)

[CLAUDE.md 說明](https://docs.anthropic.com/en/docs/claude-code/memory)

[settings.json 說明](https://docs.anthropic.com/en/docs/claude-code/settings)

---

# 運作原理

Claude Code 啟動時會依序讀取以下設定：

```
~/.claude/CLAUDE.md            ← 全域（所有專案共用）
  └── <project>/CLAUDE.md      ← 專案根目錄（本專案規則）
        └── <subdir>/CLAUDE.md ← 子目錄（可選，區域規則）
```

`CLAUDE.md` 的內容會被放入每個 session 的 system prompt，相當於永久指令，不需每次重新說明。

---

# CLAUDE.md

## 載入順序

| 檔案位置 | 適用範圍 | 說明 |
|---|---|---|
| `~/.claude/CLAUDE.md` | 所有專案 | 個人偏好、全域行為規則 |
| `<repo_root>/CLAUDE.md` | 此 repo 所有 session | 專案規範、筆記格式、命名規則 |
| `<subdir>/CLAUDE.md` | 僅此子目錄 | 子模組或特殊區域規則 |

## 常見指令內容

```markdown
# 專案規則

## 語言
所有回覆使用繁體中文

## 程式碼風格
- Python: PEP 8，字串用雙引號
- 函式命名: snake_case

## 禁止行為
- 不得修改 00_sample/ 內的範本檔
- 不得建立沒有對應 sample 結構的 md 檔
```

## 讓每次新增筆記都依照範本

在專案根目錄建立 `CLAUDE.md`，加入對應範本規則：

```markdown
## 新增筆記規則

建立新 Markdown 筆記時，依照 `00_sample/` 的對應範本：

| 筆記類型 | 使用範本 |
|---|---|
| 工具 / 服務（含安裝步驟） | `00_sample/00_common_sample.md` |
| 概念 / 方法論（無安裝步驟） | `00_sample/00_note_sample.md` |
| Python 語法筆記 | `00_sample/python_sample.md` |
```

> `CLAUDE.md` 在每次 session 啟動時自動載入，不需手動 @ 引用。

---

# settings.json

位置：`.claude/settings.json`（專案層）或 `~/.claude/settings.json`（全域層）

## 權限設定

```json
{
  "permissions": {
    "allow": [
      "Bash(git status)",
      "Bash(git diff*)",
      "Bash(python -c ':*)"
    ],
    "deny": [
      "Bash(rm -rf*)"
    ]
  }
}
```

## 常用欄位

| 欄位 | 說明 | 範例 |
|---|---|---|
| `permissions.allow` | 自動允許的工具呼叫（不跳確認） | `["Bash(git log*)"]` |
| `permissions.deny` | 永遠拒絕的工具呼叫 | `["Bash(rm -rf*)"]` |
| `model` | 指定預設模型 | `"claude-sonnet-4-6"` |
| `apiKeyHelper` | 取得 API Key 的 shell 指令 | `"cat ~/.secrets/anthropic"` |

---

# Hooks

Hooks 是在特定事件發生時自動執行的 shell 指令，設定於 `settings.json`。

## 事件類型

| 事件 | 觸發時機 |
|---|---|
| `PreToolUse` | 工具呼叫執行前 |
| `PostToolUse` | 工具呼叫執行後 |
| `Notification` | Claude 發送通知時 |
| `Stop` | Claude 完成回覆時 |
| `SubagentStop` | Sub-agent 完成時 |

## 範例：新增筆記後自動執行 linter

```json
{
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "Write",
        "hooks": [
          {
            "type": "command",
            "command": "echo '[Hook] 檔案已寫入：$CLAUDE_TOOL_OUTPUT'"
          }
        ]
      }
    ]
  }
}
```

---

# Slash Commands（自訂指令）

讓使用者在對話中輸入 `/指令名稱` 來觸發預設行為。

## 建立方式

在 `.claude/commands/` 建立 `.md` 檔，檔名即為指令名稱：

```
.claude/
└── commands/
    ├── new-note.md       ← /new-note
    └── commit.md         ← /commit
```

## 範例

`.claude/commands/new-note.md`

```markdown
依照 `00_sample/00_common_sample.md` 的結構，在指定路徑建立新筆記。
檔名使用 PascalCase，開頭摘要用繁體中文填寫。
```

使用時：

```
/new-note 03_伺服器服務/Kubernetes/Kubernetes_基礎指令.md
```

---

# MCP Server 整合

MCP（Model Context Protocol）Server 讓 Claude Code 可呼叫外部工具。

設定於 `~/.claude/claude_desktop_config.json` 或 `.mcp.json`：

```json
{
  "mcpServers": {
    "filesystem": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-filesystem", "/path/to/dir"]
    }
  }
}
```

常用 MCP Server：

| Server | 功能 |
|---|---|
| `@modelcontextprotocol/server-filesystem` | 讀寫本地檔案 |
| `@modelcontextprotocol/server-github` | 操作 GitHub Issues / PRs |
| `@modelcontextprotocol/server-postgres` | 查詢 PostgreSQL |
| `@modelcontextprotocol/server-brave-search` | 網路搜尋 |
