# ChatGPT

```
OpenAI 開發的對話式 AI，最廣泛使用的 AI 助手
```

## 目錄

- [ChatGPT](#chatgpt)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [模型版本](#模型版本)
- [使用方式](#使用方式)
  - [Web 介面](#web-介面)
  - [API 呼叫](#api-呼叫)
- [主要功能](#主要功能)

## 參考資料

[ChatGPT 官方網站](https://chatgpt.com)

[OpenAI API 文件](https://platform.openai.com/docs)

[OpenAI Cookbook](https://cookbook.openai.com)

# 模型版本

| 模型 | Model ID | 特性 |
|------|----------|------|
| GPT-4o | `gpt-4o` | 多模態，支援圖片與語音 |
| GPT-4o mini | `gpt-4o-mini` | 輕量低成本 |
| o1 | `o1` | 強化推理，適合數學邏輯 |
| o3-mini | `o3-mini` | 快速推理模型 |

# 使用方式

## Web 介面

```
https://chatgpt.com
```

## API 呼叫

```python
from openai import OpenAI

client = OpenAI(api_key="YOUR_API_KEY")

response = client.chat.completions.create(
    model="gpt-4o",
    messages=[
        {"role": "user", "content": "Hello, ChatGPT!"}
    ]
)

print(response.choices[0].message.content)
```

# 主要功能

- 對話問答與文字生成
- 圖片理解與生成（DALL-E）
- 語音輸入輸出
- GPT Store：自訂 GPT 應用
- 程式碼 Interpreter：執行 Python 程式碼
- Plugins / Actions 整合外部服務
