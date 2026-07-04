# Claude Dispatch(多Agent分派)

```
透過 Claude Agent SDK 將任務分派給多個子 Agent 並行執行
```

## 目錄

- [Claude Dispatch](#claude-dispatch)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [概念](#概念)
- [Agent 類型](#agent-類型)
- [使用方式](#使用方式)
  - [安裝](#安裝)
  - [基本範例](#基本範例)
  - [並行 Dispatch](#並行-dispatch)
- [常見應用情境](#常見應用情境)
- [與 Claude Code Channels 的差別](#與-claude-code-channels-的差別)

## 參考資料

[Claude Agent SDK 文件](https://docs.anthropic.com/claude-code/sdk)

[Claude Code Hooks 文件](https://docs.anthropic.com/claude-code/hooks)

# 概念

```
Orchestrator（主 Agent）
    ├── Sub-Agent A  → 負責任務 A
    ├── Sub-Agent B  → 負責任務 B
    └── Sub-Agent C  → 負責任務 C
```

- **Orchestrator**：接收任務、規劃步驟、分派給子 Agent
- **Sub-Agent**：執行單一具體任務，完成後回傳結果
- 子 Agent 可並行執行，大幅縮短總時間

# Agent 類型

Claude Code 內建 Agent 工具提供以下類型：

| 類型 | 說明 |
|------|------|
| `general-purpose` | 通用型，可搜尋、讀寫檔案、執行指令 |
| `Explore` | 快速探索程式碼庫，搜尋檔案與關鍵字 |
| `Plan` | 設計實作方案，分析架構 |
| `claude-code-guide` | Claude Code 功能說明專用 |

# 使用方式

## 安裝

```bash
npm install -g @anthropic-ai/claude-code
pip install anthropic
```

## 基本範例

```python
import anthropic

client = anthropic.Anthropic()

# 建立帶有工具的 Agent
response = client.messages.create(
    model="claude-sonnet-4-6",
    max_tokens=4096,
    tools=[
        {
            "type": "computer_20241022",
            "name": "computer",
            "display_width_px": 1280,
            "display_height_px": 800,
        }
    ],
    messages=[
        {"role": "user", "content": "分析這個專案並產出報告"}
    ]
)
```

## 並行 Dispatch

```python
import asyncio
import anthropic

client = anthropic.Anthropic()

async def run_agent(task: str) -> str:
    response = client.messages.create(
        model="claude-sonnet-4-6",
        max_tokens=2048,
        messages=[{"role": "user", "content": task}]
    )
    return response.content[0].text

async def dispatch():
    tasks = [
        "分析 src/ 目錄的程式碼品質",
        "列出所有 TODO 和 FIXME",
        "檢查 requirements.txt 有無過時套件",
    ]
    # 並行執行所有任務
    results = await asyncio.gather(*[run_agent(t) for t in tasks])
    return results

asyncio.run(dispatch())
```

# 常見應用情境

- **大型 Codebase 分析**：拆分多個子目錄，各自分派一個 Agent 同時分析
- **多檔案同步修改**：多個 Agent 分別處理不同模組
- **測試 + 文件同步產生**：一個 Agent 寫測試，另一個更新文件
- **CI/CD 自動化**：自動化流程中呼叫 Claude Agent 進行審查或修改

# 與 Claude Code Channels 的差別

| | Dispatch | Channels |
|---|---|---|
| **方向** | Claude → 對外派出子 Agent | 外部 → 推入 Claude session |
| **發起者** | 程式碼（Orchestrator）主動分派 | 外部平台（Telegram 等）主動推送 |
| **執行模型** | 多個子 Agent 並行執行 | 單一 session 接收事件後處理 |
| **互動方式** | 非即時、任務導向 | 即時、事件驅動 |
| **使用場景** | 大型任務拆解、並行分析 | 遠端控制、監控警報、手機發指令 |
| **技術基礎** | Claude Agent SDK（Python） | MCP Channel Plugin |
| **需要登入** | API Key 即可 | 需 claude.ai 帳號 |

> **簡單記憶**：Dispatch 是 Claude 主動「派工」給多個 Agent；Channels 是外部「傳話」進來給 Claude。
