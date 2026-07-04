# Codex（AI 程式碼生成）

```
OpenAI 開發的 AI 程式碼生成模型，基於 GPT 系列微調而來
支援自然語言轉程式碼、程式碼補全、解釋、重構
現已整合進 OpenAI API（models: gpt-4o、o3 等）及 GitHub Copilot
獨立 Codex API 已於 2023 年 3 月停止服務，功能由 Chat Completions API 取代
```

## 目錄

- [Codex（AI 程式碼生成）](#codexai-程式碼生成)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [產品定位](#產品定位)
- [使用方式](#使用方式)
  - [透過 Chat Completions API（現行方式）](#透過-chat-completions-api現行方式)
  - [System Prompt 最佳實踐](#system-prompt-最佳實踐)
  - [透過 OpenRouter 使用](#透過-openrouter-使用)
- [Codex CLI（本地 AI 程式碼助理）](#codex-cli本地-ai-程式碼助理)
  - [安裝](#安裝)
  - [使用](#使用)
- [GitHub Copilot](#github-copilot)

## 參考資料

[OpenAI Platform](https://platform.openai.com/)

[Chat Completions API 文件](https://platform.openai.com/docs/guides/text-generation)

[Codex CLI GitHub](https://github.com/openai/codex)

[GitHub Copilot](https://github.com/features/copilot)

---

# 產品定位

| 產品 | 說明 | 狀態 |
|---|---|---|
| **Codex API**（舊） | 獨立的程式碼生成 API（`code-davinci-002` 等） | 已停止（2023/03） |
| **Chat Completions API** | `gpt-4o`、`o3` 等模型，功能完全涵蓋 Codex | 現行主要方式 |
| **Codex CLI** | OpenAI 官方本地終端機 AI 助理，類似 Claude Code | 2025 年發布 |
| **GitHub Copilot** | IDE 程式碼補全，底層使用 OpenAI 模型 | 訂閱制，整合 VS Code / JetBrains |

---

# 使用方式

## 透過 Chat Completions API（現行方式）

```bash
pip install openai
```

```python
from openai import OpenAI

client = OpenAI(api_key="sk-...")

response = client.chat.completions.create(
    model="gpt-4o",
    messages=[
        {
            "role": "system",
            "content": "You are an expert programmer. Return only code, no explanation."
        },
        {
            "role": "user",
            "content": "Write a Python function to parse JWT tokens without external libraries."
        }
    ]
)

print(response.choices[0].message.content)
```

## System Prompt 最佳實踐

```python
CODING_SYSTEM_PROMPT = """
You are an expert software engineer.
- Return only working code unless asked for explanation
- Follow the language's idiomatic style
- Include type hints for Python
- Handle edge cases and errors
- Add minimal inline comments only for non-obvious logic
"""
```

## 透過 OpenRouter 使用

```python
from openai import OpenAI

client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key="sk-or-v1-...",
)

response = client.chat.completions.create(
    model="openai/gpt-4o",
    messages=[
        {"role": "system", "content": "You are an expert programmer."},
        {"role": "user", "content": "Write a FastAPI health check endpoint."}
    ]
)
```

---

# Codex CLI（本地 AI 程式碼助理）

OpenAI 於 2025 年發布的開源終端機 AI 助理，功能類似 Claude Code，可在本地執行程式碼、修改檔案、執行指令。

## 安裝

```bash
npm install -g @openai/codex
```

```bash
# 設定 API Key
export OPENAI_API_KEY="sk-..."
```

## 使用

```bash
# 互動模式
codex

# 單次任務
codex "Add type hints to all functions in main.py"

# 指定模型
codex --model o3 "Refactor this module for better testability"

# 安全模式（只讀，不執行指令）
codex --approval-mode suggest "Explain what this codebase does"
```

**Approval Mode 說明：**

| 模式 | 說明 |
|---|---|
| `suggest`（預設） | 只建議，不自動執行 |
| `auto-edit` | 自動修改檔案，執行指令需確認 |
| `full-auto` | 全自動執行（沙盒環境） |

---

# GitHub Copilot

Codex 技術的最主要商業應用，整合於 IDE 中提供即時程式碼補全。

| 項目 | 說明 |
|---|---|
| **IDE 支援** | VS Code、JetBrains、Vim、Neovim、Visual Studio |
| **功能** | 行內補全、Chat、程式碼解釋、單元測試生成 |
| **計費** | 個人 $10/月、企業 $19/用戶/月 |
| **底層模型** | OpenAI GPT-4o 系列（由 GitHub 管理） |

```bash
# VS Code 安裝
# Extensions → 搜尋 "GitHub Copilot" → Install
# 需登入 GitHub 帳戶並啟用訂閱
```
