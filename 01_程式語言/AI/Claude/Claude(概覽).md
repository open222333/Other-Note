# Claude(概覽)

```
Anthropic 開發的 AI 助手，以安全性與指令跟隨能力著稱
```

## 目錄

- [Claude](#claude)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [模型版本](#模型版本)
- [使用方式](#使用方式)
  - [Web 介面](#web-介面)
  - [Claude Code CLI](#claude-code-cli)
  - [API 呼叫](#api-呼叫)
- [主要功能](#主要功能)

## 參考資料

[Claude 官方網站](https://claude.ai)

[Anthropic API 文件](https://docs.anthropic.com)

[Claude Code 文件](https://docs.anthropic.com/claude-code)

# 模型版本

| 模型 | Model ID | 特性 |
|------|----------|------|
| Claude Opus 4.6 | `claude-opus-4-6` | 最強推理，適合複雜任務 |
| Claude Sonnet 4.6 | `claude-sonnet-4-6` | 平衡性能與速度 |
| Claude Haiku 4.5 | `claude-haiku-4-5-20251001` | 輕量快速，適合簡單任務 |

# 使用方式

## Web 介面

```
https://claude.ai
```

## Claude Code CLI

安裝

```bash
npm install -g @anthropic-ai/claude-code
```

啟動

```bash
claude
```

## API 呼叫

```python
import anthropic

client = anthropic.Anthropic(api_key="YOUR_API_KEY")

message = client.messages.create(
    model="claude-sonnet-4-6",
    max_tokens=1024,
    messages=[
        {"role": "user", "content": "Hello, Claude!"}
    ]
)

print(message.content[0].text)
```

# 主要功能

- 程式碼撰寫與 Debug
- 文件分析（PDF、圖片）
- 長文摘要與翻譯
- Claude Code：終端機內 AI 協作開發
