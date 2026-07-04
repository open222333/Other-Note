# Gemini

```
Google DeepMind 開發的多模態 AI，深度整合 Google 生態系
```

## 目錄

- [Gemini](#gemini)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [模型版本](#模型版本)
- [使用方式](#使用方式)
  - [Web 介面](#web-介面)
  - [API 呼叫](#api-呼叫)
- [主要功能](#主要功能)

## 參考資料

[Gemini 官方網站](https://gemini.google.com)

[Google AI Studio](https://aistudio.google.com)

[Gemini API 文件](https://ai.google.dev/docs)

# 模型版本

| 模型 | Model ID | 特性 |
|------|----------|------|
| Gemini 2.5 Pro | `gemini-2.5-pro` | 最強推理，超長 Context |
| Gemini 2.0 Flash | `gemini-2.0-flash` | 快速多模態 |
| Gemini 1.5 Flash | `gemini-1.5-flash` | 輕量版本 |

# 使用方式

## Web 介面

```
https://gemini.google.com
```

## API 呼叫

```python
import google.generativeai as genai

genai.configure(api_key="YOUR_API_KEY")

model = genai.GenerativeModel("gemini-2.0-flash")
response = model.generate_content("Hello, Gemini!")

print(response.text)
```

# 主要功能

- 超長 Context（最高 2M tokens）
- 多模態：文字、圖片、影片、音訊
- Google Search 即時資訊整合
- Google Workspace 整合（Gmail、Docs、Drive）
- NotebookLM：文件 AI 分析工具
