# TensorDock

```
GPU 雲端市場，定位與 Vast.ai 相近，價格通常更便宜。
以部署 VM 為主，提供 SSH 存取，支援多種 GPU 型號。
適合預算有限、需要穩定 SSH 環境的使用者。
```

## 目錄

- [TensorDock](#tensordock)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
- [安裝 CLI](#安裝-cli)
- [指令](#指令)
- [注意事項](#注意事項)

## 參考資料

[TensorDock 官網](https://tensordock.com/)

[TensorDock 文檔](https://docs.tensordock.com/)

[TensorDock API 文檔](https://documenter.getpostman.com/view/20973002/2s8YzMYRDk)

---

# 核心概念

| 項目 | 說明 |
|---|---|
| 部署方式 | VM（Virtual Machine），SSH 存取 |
| GPU 來源 | 資料中心 + 個人主機混合市場 |
| OS | Ubuntu 20.04 / 22.04 |
| 最低租用 | 無最低時數限制 |
| 價格定位 | 比 Vast.ai 略便宜，但 GPU 種類較少 |

---

# 安裝 CLI

TensorDock 主要透過 Web UI 或 REST API 操作，官方無獨立 CLI 工具。

```bash
# 透過 API 操作（需先在 Web UI 取得 API Token）
# API Token: https://dashboard.tensordock.com/api

# 用 curl 呼叫 API
curl -X POST https://marketplace.tensordock.com/api/v0/client/deploy/hostnodevm \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "api_key=YOUR_KEY&api_token=YOUR_TOKEN&password=YOUR_VM_PASS&name=my-vm&gpu_count=1&gpu_model=geforcertx4090-pcie-24gb&vcpus=4&ram=16&storage=100&operating_system=Ubuntu 22.04 LTS"
```

---

# 指令

常用 API 操作（以 Python requests 為主）：

```python
import requests

BASE = "https://marketplace.tensordock.com/api/v0"
KEY = "your_api_key"
TOKEN = "your_api_token"
AUTH = {"api_key": KEY, "api_token": TOKEN}

# 列出可用 GPU 機器
r = requests.get(f"{BASE}/client/hostnodes", params=AUTH)
for node_id, node in r.json().get("hostnodes", {}).items():
    gpus = node.get("specs", {}).get("gpu", {})
    for gpu_name, gpu_info in gpus.items():
        if gpu_info.get("amount", 0) > 0:
            print(f"{node_id} | {gpu_name} x{gpu_info['amount']}")

# 部署 VM
r = requests.post(f"{BASE}/client/deploy/hostnodevm", data={
    **AUTH,
    "password": "StrongPass123!",
    "name": "my-training-vm",
    "gpu_count": 1,
    "gpu_model": "geforcertx3090-pcie-24gb",
    "vcpus": 8,
    "ram": 32,
    "storage": 100,
    "operating_system": "Ubuntu 22.04 LTS",
})
print(r.json())

# 列出已部署的 VM
r = requests.get(f"{BASE}/client/list", params=AUTH)
print(r.json())

# 刪除 VM
r = requests.post(f"{BASE}/client/delete/vm", data={**AUTH, "server": "VM_ID"})
print(r.json())
```

SSH 連線：

```bash
# VM 建立後從 API 回傳或 Web UI 取得 IP / Port
ssh -p <PORT> user@<IP>
```

---

# 注意事項

- **庫存少**：熱門 GPU 型號偶爾缺貨，選擇不如 Vast.ai 多
- **無官方 CLI**：需透過 Web UI 或 REST API 管理，操作比 Vast.ai / RunPod 稍繁瑣
- **VM 模式**：給的是完整 VM，彈性高但啟動稍慢（1–3 分鐘）
- **網路費用**：部分方案有流量上限，大量資料傳輸前確認
