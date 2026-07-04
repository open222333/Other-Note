# OpenRouter

```
統一的 LLM API 閘道，單一端點存取 100+ 個模型（OpenAI、Anthropic、Google、Meta 等）
API 格式與 OpenAI 相容，只需切換 base_url 即可無縫切換模型
按使用量計費，部分模型提供免費額度
```

## 目錄

- [OpenRouter](#openrouter)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
- [快速開始](#快速開始)
  - [取得 API Key](#取得-api-key)
  - [Python（openai SDK）](#pythonopenai-sdk)
  - [JavaScript / Node.js](#javascript--nodejs)
  - [curl](#curl)
- [模型 ID 格式](#模型-id-格式)
  - [常用模型清單](#常用模型清單)
- [進階功能](#進階功能)
  - [模型 Fallback（備援）](#模型-fallback備援)
  - [Provider 偏好設定](#provider-偏好設定)
- [計費與限制](#計費與限制)

## 參考資料

[官網](https://openrouter.ai/)

[模型清單](https://openrouter.ai/models)

[API 文件](https://openrouter.ai/docs)

[OpenRouter Playground](https://openrouter.ai/playground)

---

# 核心概念

| 概念 | 說明 |
|---|---|
| **統一端點** | `https://openrouter.ai/api/v1`，格式與 OpenAI API 完全相容 |
| **模型 ID** | `provider/model-name`，如 `anthropic/claude-sonnet-4-5` |
| **Credits** | 預付點數，依模型 token 用量扣除 |
| **Fallback** | 指定備援模型，主模型不可用時自動切換 |
| **Provider Routing** | 控制優先使用哪個雲端供應商的同款模型 |
| **免費模型** | 部分模型有每日免費額度（model ID 後綴 `:free`） |

---

# 快速開始

## 取得 API Key

1. 前往 [openrouter.ai](https://openrouter.ai/) 登入
2. 進入 **Keys** 頁面 → 建立新 Key
3. 儲存 Key（格式：`sk-or-v1-...`）

## Python（openai SDK）

```bash
pip install openai
```

```python
from openai import OpenAI

client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key="sk-or-v1-...",
)

response = client.chat.completions.create(
    model="anthropic/claude-sonnet-4-5",
    messages=[
        {"role": "user", "content": "Hello!"}
    ]
)

print(response.choices[0].message.content)
```

```python
# 加入 HTTP-Referer 與 X-Title（選填，用於 openrouter.ai 的使用統計）
response = client.chat.completions.create(
    model="anthropic/claude-sonnet-4-5",
    messages=[{"role": "user", "content": "Hello!"}],
    extra_headers={
        "HTTP-Referer": "https://your-app.com",
        "X-Title": "Your App Name",
    }
)
```

## JavaScript / Node.js

```bash
npm install openai
```

```javascript
import OpenAI from "openai";

const client = new OpenAI({
  baseURL: "https://openrouter.ai/api/v1",
  apiKey: "sk-or-v1-...",
});

const response = await client.chat.completions.create({
  model: "google/gemini-2.0-flash-001",
  messages: [{ role: "user", content: "Hello!" }],
});

console.log(response.choices[0].message.content);
```

## curl

```bash
curl https://openrouter.ai/api/v1/chat/completions \
  -H "Authorization: Bearer sk-or-v1-..." \
  -H "Content-Type: application/json" \
  -d '{
    "model": "meta-llama/llama-3.3-70b-instruct",
    "messages": [{"role": "user", "content": "Hello!"}]
  }'
```

---

# 模型 ID 格式

```
{provider}/{model-name}:{variant}

範例：
  anthropic/claude-sonnet-4-5
  openai/gpt-4o
  google/gemini-2.0-flash-001
  meta-llama/llama-3.3-70b-instruct
  mistralai/mistral-7b-instruct:free   ← 免費版
```

## 常用模型清單

| 模型 | ID | 特性 |
|---|---|---|
| Claude Sonnet 4.5 | `anthropic/claude-sonnet-4-5` | 效能與速度平衡 |
| Claude Haiku 4.5 | `anthropic/claude-haiku-4-5` | 快速、低成本 |
| GPT-4o | `openai/gpt-4o` | OpenAI 旗艦 |
| GPT-4o mini | `openai/gpt-4o-mini` | 低成本 |
| Gemini 2.0 Flash | `google/gemini-2.0-flash-001` | Google 快速模型 |
| Gemini 2.5 Pro | `google/gemini-2.5-pro-preview` | Google 高能力 |
| Llama 3.3 70B | `meta-llama/llama-3.3-70b-instruct` | Meta 開源 |
| Mistral 7B (免費) | `mistralai/mistral-7b-instruct:free` | 免費額度 |

> 完整清單見 [openrouter.ai/models](https://openrouter.ai/models)，可依價格、context window 篩選。

---

# 進階功能

## 模型 Fallback（備援）

指定多個模型，主模型失敗或超載時自動切換：

```python
response = client.chat.completions.create(
    model="anthropic/claude-sonnet-4-5",
    messages=[{"role": "user", "content": "Hello!"}],
    extra_body={
        "models": [
            "anthropic/claude-sonnet-4-5",
            "openai/gpt-4o",                 # 第一備援
            "google/gemini-2.0-flash-001",   # 第二備援
        ]
    }
)
```

## Provider 偏好設定

同一個模型可能由多個雲端供應商提供，可指定偏好：

```python
response = client.chat.completions.create(
    model="meta-llama/llama-3.3-70b-instruct",
    messages=[{"role": "user", "content": "Hello!"}],
    extra_body={
        "provider": {
            "order": ["Fireworks", "Together"],   # 優先順序
            "allow_fallbacks": True
        }
    }
)
```

---

# 計費與限制

| 項目 | 說明 |
|---|---|
| **計費單位** | 依各模型的 input / output token 價格扣除 Credits |
| **免費模型** | 加 `:free` 後綴，有每日用量限制，可能較慢 |
| **儲值** | 最低 $5 起，信用卡或加密貨幣 |
| **Rate Limit** | 依帳戶層級與模型而異，免費模型限制較嚴 |

```python
# 查詢可用模型與價格
import httpx

models = httpx.get("https://openrouter.ai/api/v1/models").json()
for m in models["data"][:5]:
    print(m["id"], m["pricing"])
```
