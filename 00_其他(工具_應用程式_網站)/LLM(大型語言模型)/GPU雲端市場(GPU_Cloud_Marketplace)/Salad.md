# Salad（SaladCloud）

```
分散式消費級 GPU 網路。
個人電腦閒置時提供算力，租用方以容器形式執行工作負載。
價格是市場最低，但不支援互動式 SSH，僅適合容器化的批次任務與推論 API。
```

## 目錄

- [Salad（SaladCloud）](#saladsaladcloud)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
  - [與其他平台的根本差異](#與其他平台的根本差異)
  - [Container Group](#container-group)
- [使用方式](#使用方式)
  - [部署容器（Web UI）](#部署容器web-ui)
  - [Container Gateway（推論 API）](#container-gateway推論-api)
  - [API 操作](#api-操作)
- [注意事項](#注意事項)

## 參考資料

[SaladCloud 官網](https://salad.com/)

[SaladCloud 文檔](https://docs.salad.com/)

[SaladCloud API 文檔](https://docs.salad.com/reference/saladcloud-api)

---

# 核心概念

## 與其他平台的根本差異

| 項目 | Salad | Vast.ai / RunPod |
|---|---|---|
| GPU 來源 | 個人電腦（遊戲 PC） | 個人主機 / 資料中心 |
| 存取方式 | **僅容器**，無 SSH | SSH 可用 |
| 穩定性 | 低（節點隨時可能離線） | 中～高 |
| 價格 | **最便宜**（$0.001–0.01/GPU hr） | 便宜 |
| 適合用途 | 批次推論、無狀態任務 | 互動開發、訓練 |

## Container Group

Salad 的核心部署單元，類似 Kubernetes Deployment：

- 指定 Docker 映像、GPU 需求、副本數
- Salad 自動在可用節點上分配並執行
- 節點離線時自動遷移到其他節點
- 支援 Container Gateway 對外提供 HTTP API

---

# 使用方式

## 部署容器（Web UI）

1. 登入 [SaladCloud Portal](https://portal.salad.com/)
2. 建立 Container Group
3. 設定：Docker 映像、GPU 需求（型號、VRAM）、CPU、RAM、副本數
4. 啟動後等待節點分配（通常 1–5 分鐘）

## Container Gateway（推論 API）

Container Gateway 讓容器對外暴露 HTTP 端點：

```yaml
# Container Group 設定中啟用 Container Gateway
# Port: 容器內服務監聽的 port（如 8000）
# 啟用後取得公開 URL：https://xxxx.salad.com
```

容器內服務範例（FastAPI）：

```python
from fastapi import FastAPI

app = FastAPI()

@app.post("/generate")
def generate(prompt: str):
    # 呼叫模型...
    return {"result": "..."}

# 啟動：uvicorn main:app --host 0.0.0.0 --port 8000
```

## API 操作

```python
import requests

ORG = "your-org-name"
PROJECT = "your-project"
API_KEY = "your_salad_api_key"
HEADERS = {"Salad-Api-Key": API_KEY, "Content-Type": "application/json"}
BASE = f"https://api.salad.com/api/public/organizations/{ORG}/projects/{PROJECT}"

# 列出 Container Groups
r = requests.get(f"{BASE}/containers", headers=HEADERS)
print(r.json())

# 建立 Container Group
payload = {
    "name": "my-inference-group",
    "display_name": "My Inference",
    "container": {
        "image": "your-dockerhub/your-image:latest",
        "resources": {
            "cpu": 4,
            "memory": 8192,
            "gpu_classes": ["RTX 3090", "RTX 4090"]
        },
        "environment_variables": {"MODEL_PATH": "/models/llama"}
    },
    "replicas": 3,
    "networking": {"protocol": "http", "port": 8000, "auth": False}
}
r = requests.post(f"{BASE}/containers", json=payload, headers=HEADERS)
print(r.json())

# 啟動 / 停止 Container Group
requests.post(f"{BASE}/containers/my-inference-group/start", headers=HEADERS)
requests.post(f"{BASE}/containers/my-inference-group/stop", headers=HEADERS)
```

---

# 注意事項

- **無 SSH**：Salad 不提供互動式 Shell，所有操作透過容器日誌或 API
- **無狀態設計**：節點隨時可能被收回，容器不能依賴本地儲存，資料要寫到外部（S3、R2 等）
- **副本數建議 ≥ 3**：補償節點隨機離線的影響，Gateway 會自動路由到健康節點
- **冷啟動**：首次啟動或節點更換時需要拉 Docker 映像，映像越小啟動越快
- **適合情境**：大量圖片生成、批次文字推論等可重試的無狀態工作
