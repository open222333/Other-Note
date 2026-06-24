# Lambda Labs

```
專注 AI / ML 的 GPU 雲端，非市場模式，全為資料中心機器。
穩定性比 Vast.ai / RunPod Community 高，適合正式訓練與長時間任務。
提供 On-demand、Reserved（年約）兩種方案。
```

## 目錄

- [Lambda Labs](#lambda-labs)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
  - [實例類型](#實例類型)
  - [GPU 型號與常見規格](#gpu-型號與常見規格)
- [安裝 CLI / SDK](#安裝-cli--sdk)
- [指令](#指令)
  - [Lambda Cloud CLI](#lambda-cloud-cli)
  - [Python API](#python-api)
- [Filesystem（持久儲存）](#filesystem持久儲存)
- [注意事項](#注意事項)

## 參考資料

[Lambda Labs 官網](https://lambdalabs.com/)

[Lambda Cloud 文檔](https://docs.lambdalabs.com/)

[Lambda Cloud API 文檔](https://cloud.lambdalabs.com/api/v1/docs)

[Lambda Labs GitHub](https://github.com/lambdal)

---

# 核心概念

## 實例類型

| 類型 | 說明 | 適合情境 |
|---|---|---|
| On-demand | 隨用隨付，時租 | 彈性訓練、實驗 |
| Reserved（1yr / 3yr） | 年約，折扣約 40–60% | 長期生產環境 |
| On-demand Cluster | 多機 GPU 叢集（8–512 GPU） | 大規模訓練 |

## GPU 型號與常見規格

| GPU | VRAM | 適合用途 |
|---|---|---|
| H100 SXM5 80GB | 80 GB | 大型模型訓練、推論 |
| H100 PCIe 80GB | 80 GB | 訓練、推論 |
| A100 SXM4 80GB | 80 GB | 大型模型（較舊但成熟） |
| A100 PCIe 40GB | 40 GB | 中型模型訓練 |
| A6000 48GB | 48 GB | 推論、微調 |
| RTX 6000 Ada 48GB | 48 GB | 推論、影像生成 |

---

# 安裝 CLI / SDK

```bash
# Lambda Cloud CLI（非官方，社群維護）
pip install lambda-cloud

# 官方 Python REST API（用 requests 直接呼叫）
pip install requests
```

---

# 指令

## Lambda Cloud CLI

```bash
# 設定 API Key（從 https://cloud.lambdalabs.com/api-keys 取得）
export LAMBDA_API_KEY="your_api_key"

# 列出可用實例類型（含目前是否有貨）
curl -u "$LAMBDA_API_KEY:" \
  https://cloud.lambdalabs.com/api/v1/instance-types | jq .

# 列出目前的實例
curl -u "$LAMBDA_API_KEY:" \
  https://cloud.lambdalabs.com/api/v1/instances | jq .

# 啟動實例
curl -u "$LAMBDA_API_KEY:" \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "region_name": "us-west-2",
    "instance_type_name": "gpu_1x_a100_sxm4",
    "ssh_key_names": ["my-ssh-key"],
    "quantity": 1
  }' \
  https://cloud.lambdalabs.com/api/v1/instance-operations/launch | jq .

# 終止實例
curl -u "$LAMBDA_API_KEY:" \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"instance_ids": ["INSTANCE_ID"]}' \
  https://cloud.lambdalabs.com/api/v1/instance-operations/terminate | jq .
```

## Python API

```python
import requests

API_KEY = "your_api_key"
BASE = "https://cloud.lambdalabs.com/api/v1"
AUTH = (API_KEY, "")

# 列出可用實例類型
r = requests.get(f"{BASE}/instance-types", auth=AUTH)
for name, info in r.json()["data"].items():
    avail = info["regions_with_capacity_available"]
    if avail:
        print(f"{name}: ${info['instance_type']['price_cents_per_hour']/100:.2f}/hr, regions={[a['name'] for a in avail]}")

# 列出目前實例
r = requests.get(f"{BASE}/instances", auth=AUTH)
print(r.json())
```

---

# Filesystem（持久儲存）

Lambda 提供跨實例的 NFS 掛載儲存：

```bash
# 從 Web UI 建立 Filesystem 後，啟動實例時指定掛載點
# 掛載後路徑：/home/ubuntu/my-filesystem/

# 模型存到 Filesystem 避免每次重下載
huggingface-cli download meta-llama/Llama-2-7b \
  --local-dir /home/ubuntu/models/llama2-7b
```

---

# 注意事項

- **庫存緊張**：熱門 GPU（H100、A100）常常沒貨，需要搶或提前預約
- **SSH Key 必填**：啟動前要在 Web UI 上傳 SSH 公鑰
- **無 Spot 模式**：Lambda 不提供可中斷實例，穩定但不便宜
- **Filesystem 費用**：儲存獨立計費（約 $0.20/GB/month），不用時記得刪除
- **預設映像**：Ubuntu 22.04 + CUDA + PyTorch，SSH 後即可用
