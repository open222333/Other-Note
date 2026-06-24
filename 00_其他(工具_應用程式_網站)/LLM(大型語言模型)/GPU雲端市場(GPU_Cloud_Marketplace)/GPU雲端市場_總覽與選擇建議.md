# GPU 雲端市場 總覽與選擇建議

```
本文整合各主流 GPU 雲端平台的比較，協助依需求快速選出合適平台。
各平台詳細操作請見個別筆記。
```

## 目錄

- [GPU 雲端市場 總覽與選擇建議](#gpu-雲端市場-總覽與選擇建議)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [平台總覽比較](#平台總覽比較)
  - [基本屬性](#基本屬性)
  - [功能支援](#功能支援)
  - [大概價位（2024 年參考）](#大概價位2024-年參考)
- [需求選擇建議](#需求選擇建議)
  - [快速決策表](#快速決策表)
  - [情境對應推薦](#情境對應推薦)
- [各平台簡評](#各平台簡評)

## 參考資料

- [Vast.ai 筆記](./Vast.ai.md)
- [RunPod 筆記](./RunPod.md)
- [Lambda Labs 筆記](./Lambda_Labs.md)
- [TensorDock 筆記](./TensorDock.md)
- [Salad 筆記](./Salad.md)
- [Paperspace 筆記](./Paperspace.md)
- [CoreWeave 筆記](./CoreWeave.md)

---

# 平台總覽比較

## 基本屬性

| 平台 | 類型 | GPU 來源 | 帳號 | 門檻 |
|---|---|---|---|---|
| **Vast.ai** | GPU 市場 | 個人主機 | 即開即用 | 低 |
| **RunPod** | GPU 市場 | 個人 + 資料中心 | 即開即用 | 低 |
| **Lambda Labs** | 專用雲端 | 資料中心 | 即開即用 | 低～中 |
| **TensorDock** | GPU 市場 | 混合 | 即開即用 | 低 |
| **Salad** | 分散式網路 | 個人電腦 | 即開即用 | 低（但需容器化） |
| **Paperspace** | GPU 雲端 + ML 平台 | 資料中心 | 即開即用 | **最低（有免費）** |
| **CoreWeave** | 企業 GPU 雲端 | 資料中心 | 需審核 | 高（需懂 k8s） |

## 功能支援

| 平台 | SSH 存取 | Jupyter Notebook | Serverless | 多機叢集 | 免費方案 |
|---|---|---|---|---|---|
| Vast.ai | ✅ | ✅（自建） | ❌ | ❌ | ❌ |
| RunPod | ✅ | ✅（自建） | ✅ | ❌ | ❌ |
| Lambda Labs | ✅ | ✅（Jupyter 映像） | ❌ | ✅ | ❌ |
| TensorDock | ✅ | ✅（自建） | ❌ | ❌ | ❌ |
| Salad | ❌ | ❌ | ✅ | ❌ | ❌ |
| Paperspace | ✅（Core） | ✅（Gradient） | ❌ | ❌ | ✅ |
| CoreWeave | ✅（k8s exec） | ✅（k8s Pod） | ❌ | ✅ | ❌ |

## 大概價位（2024 年參考）

> 價格浮動，以下為大致區間，實際以各平台當下報價為準。

| 平台 | RTX 3090 (24GB) | A100 40GB | H100 80GB |
|---|---|---|---|
| Vast.ai | $0.15–0.40/hr | $1.0–2.0/hr | $2.5–4.0/hr |
| RunPod Community | $0.15–0.35/hr | $1.0–1.8/hr | $2.5–3.5/hr |
| RunPod Secure | $0.44/hr | $1.99/hr | $3.99/hr |
| Lambda Labs | 無 | $1.29/hr | $2.49/hr |
| TensorDock | $0.10–0.30/hr | $0.8–1.5/hr | $2.0–3.5/hr |
| Salad | $0.01–0.10/hr | - | - |
| Paperspace | $0.51/hr（A4000） | $3.09/hr | - |
| CoreWeave | - | $2.06/hr | $4.25/hr |

---

# 需求選擇建議

## 快速決策表

| 需求 | 推薦平台 | 備選 |
|---|---|---|
| 免費試用 / 學習 | **Paperspace**（免費 GPU Notebook） | - |
| 最便宜、批次推論 | **Salad** | TensorDock |
| 互動開發、彈性最大 | **Vast.ai** | RunPod Community |
| 互動開發 + 好 UI | **RunPod** | Vast.ai |
| 穩定、不想管主機 | **Lambda Labs** | RunPod Secure |
| 推論 API（Serverless） | **RunPod Serverless** | Salad Container Gateway |
| 企業大規模訓練 | **CoreWeave** | Lambda Labs Cluster |
| 需要 Kubernetes | **CoreWeave** | - |
| 只有消費級 GPU 需求 | **Vast.ai** / **TensorDock** | RunPod Community |

## 情境對應推薦

### 個人學習 / 玩模型

```
免費 → Paperspace Gradient 免費 Notebook
需要客製環境 → Vast.ai 或 RunPod（便宜、SSH 自由）
```

### 微調（Fine-tuning）小模型（7B 以下）

```
預算有限 → Vast.ai RTX 3090 / 4090
穩定優先 → RunPod Secure Cloud A4000
```

### 微調中大型模型（13B–70B）

```
A100 40GB → Lambda Labs 或 RunPod Secure
需要多 GPU → Lambda Labs（支援多機）
```

### 推論 API（對外服務）

```
低流量 / 冷啟動可接受 → RunPod Serverless
高流量 / 低延遲 → CoreWeave Deployment 或 Lambda Labs
批次無狀態推論 → Salad Container Gateway（最便宜）
```

### 大規模訓練（LLaMA 70B+、GPT 規模）

```
CoreWeave H100 NVLink 叢集
或
Lambda Labs H100 叢集
（兩者都需要聯繫業務或排期）
```

### 影像生成（Stable Diffusion / ComfyUI）

```
RunPod（模板最多，一鍵啟動）
或 Vast.ai（便宜）
```

---

# 各平台簡評

| 平台 | 優點 | 缺點 | 一句話定位 |
|---|---|---|---|
| **Vast.ai** | 便宜、GPU 種類多、CLI 強大 | UI 較舊、主機品質參差 | 什麼都可以試，追求最低成本 |
| **RunPod** | UI 友善、Serverless 強、模板豐富 | Secure Cloud 略貴 | Vast.ai 的進化版，體驗更好 |
| **Lambda Labs** | 穩定、無個人主機、支援多機叢集 | 庫存常缺、無 Spot 模式 | 需要可靠 GPU 且不想踩坑 |
| **TensorDock** | 比 Vast.ai 更便宜 | 選擇少、無官方 CLI | 極度省錢但接受更少選擇 |
| **Salad** | 價格最低 | 無 SSH、不穩定、需容器化 | 批次推論神器，不適合互動 |
| **Paperspace** | 免費方案、Notebook 整合好 | 付費方案不特別便宜 | 學習入門首選，零門檻 |
| **CoreWeave** | 企業 SLA、k8s 原生、大叢集 | 需申請、需懂 k8s、無個人方案 | 認真做 AI 產品的公司用 |
