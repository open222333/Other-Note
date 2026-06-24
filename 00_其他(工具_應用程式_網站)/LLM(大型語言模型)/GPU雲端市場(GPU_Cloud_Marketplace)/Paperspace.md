# Paperspace（DigitalOcean）

```
GPU 雲端平台，2023 年被 DigitalOcean 收購。
分為 Gradient（ML 平台，含 Notebook）和 Core（傳統 GPU VM）兩條產品線。
有免費方案，適合學習和原型開發。
```

## 目錄

- [Paperspace（DigitalOcean）](#paperspaceDigitalOcean)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
  - [Gradient vs Core](#gradient-vs-core)
  - [Gradient Notebook 免費方案](#gradient-notebook-免費方案)
- [安裝 CLI / SDK](#安裝-cli--sdk)
- [指令](#指令)
  - [Gradient CLI](#gradient-cli)
  - [Gradient SDK（Python）](#gradient-sdkpython)
  - [Core（VM）操作](#corevm操作)
- [注意事項](#注意事項)

## 參考資料

[Paperspace 官網](https://www.paperspace.com/)

[Gradient 文檔](https://docs.digitalocean.com/products/paperspace/)

[Paperspace GitHub](https://github.com/Paperspace)

---

# 核心概念

## Gradient vs Core

| | Gradient | Core |
|---|---|---|
| 定位 | ML 平台（Notebook、訓練、部署） | 傳統 GPU VM |
| 存取方式 | Web Notebook、CLI、SDK | SSH |
| 免費方案 | 有（限定 GPU） | 無 |
| 適合對象 | 資料科學家、學習者 | 需要完整 VM 控制的開發者 |

## Gradient Notebook 免費方案

| GPU | VRAM | 限制 |
|---|---|---|
| Free-GPU（M4000） | 8 GB | 6 小時/次，可能需排隊 |
| Free-P5000 | 16 GB | 同上 |

> 免費方案的 GPU 不保證可用，付費方案才有優先權。

---

# 安裝 CLI / SDK

```bash
# Gradient CLI
pip install gradient

# 設定 API Key（從 https://console.paperspace.com/account/api 取得）
gradient apiKey YOUR_API_KEY
# 或設定環境變數
export PAPERSPACE_API_KEY="YOUR_API_KEY"
```

---

# 指令

## Gradient CLI

```bash
# 列出可用機器類型
gradient machines list

# 建立 Notebook
gradient notebooks create \
  --projectId <PROJECT_ID> \
  --machineType A4000 \
  --container "paperspace/gradient-base:pt211-tf215-jax0414-py311" \
  --name "my-notebook"

# 列出 Notebook
gradient notebooks list

# 停止 Notebook
gradient notebooks stop --id <NOTEBOOK_ID>

# 執行訓練任務
gradient jobs create \
  --projectId <PROJECT_ID> \
  --machineType A100 \
  --container "paperspace/gradient-base:pt211-tf215-jax0414-py311" \
  --command "python train.py" \
  --workspace .
```

## Gradient SDK（Python）

```python
import gradient

client = gradient.sdk.GradientClient(api_key="YOUR_API_KEY")

# 建立並執行 Job
job = client.jobs.create(
    project_id="PROJECT_ID",
    machine_type="A4000",
    container="paperspace/gradient-base:pt211-tf215-jax0414-py311",
    command="python train.py",
    workspace_url=".",
)
print(f"Job ID: {job.id}, Status: {job.state}")
```

## Core（VM）操作

Core VM 透過 Web UI 或 DigitalOcean API 管理，SSH 存取：

```bash
# 從 Web UI 建立 Core VM 後
ssh paperspace@<VM_IP>

# 預設有 GPU 驅動，確認 GPU 可用
nvidia-smi
```

---

# 注意事項

- **免費 GPU 常常排隊**：免費方案高峰時段需等待，生產環境用付費
- **Notebook 自動關閉**：免費 Notebook 6 小時後自動停止，付費方案也會在閒置後自動停
- **Storage**：Gradient 提供 5GB 免費 Persistent Storage，付費可擴充
- **整合 DigitalOcean**：2023 年後帳號整合 DigitalOcean，可統一管理帳單
- **適合學習**：免費 GPU Notebook 是學習深度學習的最低門檻選擇之一
