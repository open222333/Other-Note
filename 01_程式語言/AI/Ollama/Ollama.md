# Ollama

```
本地端執行開源 LLM 的工具，無需網路、資料不外傳
```

## 目錄

- [Ollama](#ollama)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [Linux](#linux)
  - [Windows](#windows)
  - [docker-compose 部署](#docker-compose-部署)
- [常用模型](#常用模型)
- [指令](#指令)
- [API 呼叫](#api-呼叫)

## 參考資料

[Ollama 官方網站](https://ollama.com)

[Ollama GitHub](https://github.com/ollama/ollama)

[Ollama 模型庫](https://ollama.com/library)

# 安裝

## Homebrew (MacOS)

```bash
brew install ollama
```

## Linux

```bash
curl -fsSL https://ollama.com/install.sh | sh
```

## Windows

至 [https://ollama.com/download](https://ollama.com/download) 下載安裝檔

## docker-compose 部署

```yml
services:
  ollama:
    image: ollama/ollama
    container_name: ollama
    ports:
      - "11434:11434"
    volumes:
      - ollama_data:/root/.ollama
    restart: unless-stopped

volumes:
  ollama_data:
```

# 常用模型

| 模型 | 指令 | 特性 |
|------|------|------|
| Llama 3.2 | `ollama pull llama3.2` | Meta 通用模型 |
| Gemma 3 | `ollama pull gemma3` | Google 輕量模型 |
| Qwen 2.5 | `ollama pull qwen2.5` | 阿里巴巴，中文強 |
| DeepSeek-R1 | `ollama pull deepseek-r1` | 強化推理 |
| Mistral | `ollama pull mistral` | 歐洲開源模型 |
| CodeLlama | `ollama pull codellama` | 程式碼專用 |

# 指令

```bash
# 下載並執行模型
ollama run llama3.2

# 只下載模型
ollama pull llama3.2

# 列出已下載模型
ollama list

# 刪除模型
ollama rm llama3.2

# 查看執行中的模型
ollama ps

# 停止模型
ollama stop llama3.2
```

# API 呼叫

Ollama 預設在 `http://localhost:11434` 提供 REST API

```bash
curl http://localhost:11434/api/generate -d '{
  "model": "llama3.2",
  "prompt": "Hello, Ollama!",
  "stream": false
}'
```

Python 使用

```python
import ollama

response = ollama.chat(
    model="llama3.2",
    messages=[{"role": "user", "content": "Hello, Ollama!"}]
)

print(response["message"]["content"])
```
