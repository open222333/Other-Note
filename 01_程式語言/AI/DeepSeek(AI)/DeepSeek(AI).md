# DeepSeek

```
中國深度求索開發的開源 AI，以低成本強推理著稱
```

## 目錄

- [DeepSeek](#deepseek)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [模型版本](#模型版本)
- [使用方式](#使用方式)
  - [Web 介面](#web-介面)
  - [API 呼叫](#api-呼叫)
  - [本地端執行（Ollama）](#本地端執行ollama)
- [主要功能](#主要功能)

## 參考資料

[DeepSeek 官方網站](https://www.deepseek.com)

[DeepSeek API 文件](https://api-docs.deepseek.com)

[DeepSeek GitHub](https://github.com/deepseek-ai)

# 模型版本

| 模型 | Model ID | 特性 |
|------|----------|------|
| DeepSeek-V3 | `deepseek-chat` | 通用對話，高性價比 |
| DeepSeek-R1 | `deepseek-reasoner` | 強化推理，Chain-of-Thought |

# 使用方式

## Web 介面

```
https://chat.deepseek.com
```

## API 呼叫

DeepSeek API 相容 OpenAI 格式

```python
from openai import OpenAI

client = OpenAI(
    api_key="YOUR_API_KEY",
    base_url="https://api.deepseek.com"
)

response = client.chat.completions.create(
    model="deepseek-chat",
    messages=[
        {"role": "user", "content": "Hello, DeepSeek!"}
    ]
)

print(response.choices[0].message.content)
```

## 本地端執行（Ollama）

```bash
# R1 推理模型
ollama pull deepseek-r1

ollama run deepseek-r1
```

# 主要功能

- DeepThink（R1）：顯示推理過程的 Chain-of-Thought
- 程式碼撰寫與分析
- 數學與邏輯推理
- 開源模型可自行部署
- 相容 OpenAI API 格式，易於遷移
