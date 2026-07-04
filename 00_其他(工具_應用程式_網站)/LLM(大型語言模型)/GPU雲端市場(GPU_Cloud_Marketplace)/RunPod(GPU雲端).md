# RunPod

```
GPU 雲端市場，與 Vast.ai 最相似的競品。
分為 Secure Cloud（資料中心機器，更穩定）和 Community Cloud（個人主機，較便宜）。
另有 Serverless GPU，按請求計費，適合推論 API。
```

## 目錄

- [RunPod](#runpod)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
  - [Pod 類型](#pod-類型)
  - [Serverless vs Pod 差異](#serverless-vs-pod-差異)
- [安裝 CLI](#安裝-cli)
- [指令](#指令)
  - [Pod 管理（CLI）](#pod-管理cli)
  - [Serverless（Python SDK）](#serverlesspython-sdk)
- [常用 Template](#常用-template)
- [注意事項](#注意事項)

## 參考資料

[RunPod 官網](https://www.runpod.io/)

[RunPod 文檔](https://docs.runpod.io/)

[runpodctl CLI](https://github.com/runpod/runpodctl)

[RunPod Python SDK](https://github.com/runpod/runpod-python)

---

# 核心概念

## Pod 類型

| 類型 | 說明 | 可靠性 | 價格 |
|---|---|---|---|
| Secure Cloud | 資料中心 GPU，SLA 保障 | 高 | 較貴 |
| Community Cloud | 個人主機出租，類似 Vast.ai | 中 | 便宜 |
| Serverless | 按請求計費，無需管理 Pod | 高 | 依用量 |

## Serverless vs Pod 差異

| | Pod（On-demand） | Serverless |
|---|---|---|
| 計費 | 時租（秒計） | 每次請求（GPU 秒） |
| 啟動速度 | 即時（Pod 已開） | 冷啟動 1–30 秒 |
| 適合情境 | 互動開發、長任務 | 推論 API、批次處理 |
| 閒置費用 | 有（Pod 開著就收費） | 無 |

---

# 安裝 CLI

```bash
# CLI 工具
pip install runpodctl

# Python SDK（Serverless 用）
pip install runpod

# 設定 API Key（從 https://www.runpod.io/console/user/settings 取得）
runpodctl config --apiKey <YOUR_API_KEY>
```

---

# 指令

## Pod 管理（CLI）

```bash
# 列出所有 Pod
runpodctl get pod

# 建立 Pod（從 Web UI 選好設定後也可用 CLI）
runpodctl create pod \
  --name my-pod \
  --gpuType "NVIDIA RTX A4000" \
  --imageName "runpod/pytorch:2.1.0-py3.10-cuda12.1.1-devel-ubuntu22.04" \
  --containerDiskInGb 20 \
  --volumeInGb 50

# 停止 Pod
runpodctl stop pod <POD_ID>

# 啟動已停止的 Pod
runpodctl start pod <POD_ID>

# 刪除 Pod
runpodctl remove pod <POD_ID>

# SSH 連線（RunPod 提供完整指令）
runpodctl ssh <POD_ID>
```

## Serverless（Python SDK）

```python
import runpod

runpod.api_key = "YOUR_API_KEY"

# 呼叫 Serverless Endpoint
endpoint = runpod.Endpoint("ENDPOINT_ID")

# 同步執行
result = endpoint.run_sync({"input": {"prompt": "Hello world"}})
print(result)

# 非同步執行
job = endpoint.run({"input": {"prompt": "Hello world"}})
output = job.output(timeout=60)
print(output)
```

Serverless handler 範例（`handler.py`）：

```python
import runpod

def handler(job):
    input_data = job["input"]
    prompt = input_data.get("prompt", "")
    # 處理邏輯...
    return {"result": f"Processed: {prompt}"}

runpod.serverless.start({"handler": handler})
```

---

# 常用 Template

| 用途 | Template 名稱 |
|---|---|
| PyTorch 開發 | `RunPod PyTorch 2.x` |
| Ollama | `Ollama` |
| Stable Diffusion WebUI | `Stable Diffusion A1111` |
| ComfyUI | `ComfyUI` |
| Jupyter Notebook | `Jupyter Notebook` |
| 自訂 Docker | 任意 Docker Hub 映像 |

---

# 注意事項

- **Secure Cloud 優先**：如果任務不能中斷，選 Secure Cloud，Community Cloud 主機可能隨時離線
- **停止 ≠ 刪除**：停止 Pod 仍收 Volume 儲存費，刪除才完全停止計費
- **Serverless 冷啟動**：第一次請求較慢（需啟動容器），可設定 `min_workers=1` 保持一個熱機
- **Volume**：跨 Pod 保留資料用，模型檔案建議存 Volume 不用每次重下載
