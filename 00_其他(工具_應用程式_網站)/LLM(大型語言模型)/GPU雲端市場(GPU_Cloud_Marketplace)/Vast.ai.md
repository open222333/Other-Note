# Vast.ai

```
GPU 雲端租用市場。個人主機提供閒置 GPU，租用者透過平台競標或直接租用，
比 AWS / GCP 便宜 5–10 倍，適合跑 AI 推論、訓練、Stable Diffusion、Ollama 等。
```

## 目錄

- [Vast.ai](#vastai)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
  - [實例類型](#實例類型)
  - [搜尋欄位說明](#搜尋欄位說明)
- [安裝 CLI](#安裝-cli)
- [指令](#指令)
  - [搜尋 GPU 機器](#搜尋-gpu-機器)
  - [建立與管理實例](#建立與管理實例)
  - [連線](#連線)
- [常用 Template（映像檔）](#常用-template映像檔)
- [注意事項](#注意事項)

## 參考資料

[Vast.ai 官網](https://vast.ai/)

[Vast.ai CLI 文檔](https://vast.ai/docs/cli/commands)

[Vast.ai API Key](https://cloud.vast.ai/api/)

---

# 核心概念

## 實例類型

| 類型 | 說明 | 適合情境 |
|---|---|---|
| On-demand | 固定時租，隨時可用 | 長時間穩定跑任務 |
| Interruptible（Spot） | 比 On-demand 便宜，可能被中斷 | 短暫測試、可容忍中斷的任務 |

## 搜尋欄位說明

| 欄位 | 說明 |
|---|---|
| `dph_base` | 時租價格（USD/hr） |
| `gpu_name` | GPU 型號（如 `RTX_3090`、`A100_SXM4`） |
| `num_gpus` | GPU 數量 |
| `gpu_ram` | VRAM（GB） |
| `reliability2` | 主機可靠性分數（0–1，建議 > 0.95） |
| `inet_up` | 上傳頻寬（Mbps） |
| `inet_down` | 下載頻寬（Mbps） |
| `disk_space` | 可用磁碟空間（GB） |
| `cuda_max_good` | 支援的最高 CUDA 版本 |

---

# 安裝 CLI

```bash
pip install vastai

# 設定 API Key（從 https://cloud.vast.ai/api/ 取得）
vastai set api-key <YOUR_API_KEY>
```

---

# 指令

## 搜尋 GPU 機器

```bash
# 搜尋可用 Offer（依條件過濾）
vastai search offers 'gpu_name=RTX_3090 num_gpus=1 reliability2>0.95'

# 常見搜尋條件組合
vastai search offers 'gpu_name=A100_SXM4 num_gpus=1 dph_base<2.0'
vastai search offers 'gpu_ram>=24 reliability2>0.99 inet_down>500'

# 輸出 JSON 格式（方便 jq 處理）
vastai search offers 'num_gpus=1' --raw | jq '.[0]'
```

## 建立與管理實例

```bash
# 建立實例（OFFER_ID 從搜尋結果取得）
vastai create instance <OFFER_ID> \
  --image pytorch/pytorch:2.1.0-cuda12.1-cudnn8-runtime \
  --disk 40 \
  --onstart "pip install transformers"

# 列出目前的實例
vastai show instances

# 啟動 / 停止實例
vastai start instance <INSTANCE_ID>
vastai stop instance <INSTANCE_ID>

# 銷毀實例（停止計費）
vastai destroy instance <INSTANCE_ID>

# 查看實例 log
vastai logs <INSTANCE_ID>
```

## 連線

```bash
# 取得 SSH 連線指令
vastai ssh-url <INSTANCE_ID>

# 直接 SSH（會輸出完整指令，複製貼上即可）
ssh -p <PORT> root@<IP>

# 複製檔案到實例
scp -P <PORT> ./model.pt root@<IP>:/root/

# 複製檔案回本機
scp -P <PORT> root@<IP>:/root/output.pt ./
```

---

# 常用 Template（映像檔）

| 用途 | 映像檔 |
|---|---|
| PyTorch | `pytorch/pytorch:2.1.0-cuda12.1-cudnn8-runtime` |
| TensorFlow | `tensorflow/tensorflow:latest-gpu` |
| Ollama | `ollama/ollama` |
| Stable Diffusion WebUI | Vast.ai 官方模板（Web UI 直接選） |
| CUDA 基礎環境 | `nvidia/cuda:12.1.0-devel-ubuntu22.04` |
| Jupyter | `jupyter/scipy-notebook` |

---

# 注意事項

- **可靠性分數**：`reliability2 > 0.95` 才考慮，低分主機容易突然離線
- **磁碟空間**：模型檔案動輒 10–70GB，建立時 `--disk` 給夠
- **計費**：以秒計費，停止（stop）仍計磁碟費，`destroy` 才完全停止
- **Interruptible**：任務中途可能被收回，重要結果要定時備份到外部儲存
- **網路頻寬**：下載大型模型前確認 `inet_down` 夠快，否則等很久
