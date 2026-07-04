# CoreWeave

```
企業級 GPU 雲端，Kubernetes 原生架構。
專注大規模 AI 訓練叢集，客戶多為 AI 公司、研究機構。
需申請帳號（非即開即用），適合有持續大量 GPU 需求的組織。
```

## 目錄

- [CoreWeave](#coreweave)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
  - [與一般 GPU 市場的差異](#與一般-gpu-市場的差異)
  - [主要 GPU 型號](#主要-gpu-型號)
- [架構：Kubernetes 原生](#架構kubernetes-原生)
  - [部署範例](#部署範例)
- [儲存](#儲存)
- [網路](#網路)
- [注意事項](#注意事項)

## 參考資料

[CoreWeave 官網](https://www.coreweave.com/)

[CoreWeave 文檔](https://docs.coreweave.com/)

[CoreWeave GitHub](https://github.com/coreweave)

---

# 核心概念

## 與一般 GPU 市場的差異

| 項目 | CoreWeave | Vast.ai / RunPod |
|---|---|---|
| 帳號申請 | 需審核（企業優先） | 即開即用 |
| 部署方式 | Kubernetes（k8s） | SSH VM / 容器 |
| 規模 | 支援數百 GPU 叢集 | 最多幾十 GPU |
| GPU 來源 | 資料中心（純商業機房） | 混合（部分個人） |
| 定位 | 企業 / 研究機構 | 個人 / 新創 |
| 最低門檻 | 無（但需申請） | 無 |

## 主要 GPU 型號

| GPU | VRAM | 適合用途 |
|---|---|---|
| H100 SXM5 80GB NVLink | 80 GB | 超大模型訓練（LLaMA 70B+） |
| H100 PCIe 80GB | 80 GB | 訓練、推論 |
| A100 80GB NVLink | 80 GB | 大型模型訓練 |
| A100 40GB | 40 GB | 訓練、微調 |
| RTX A6000 48GB | 48 GB | 推論、影像生成 |

---

# 架構：Kubernetes 原生

CoreWeave 使用標準 Kubernetes API，所有資源以 k8s 資源描述：

```bash
# 設定 kubeconfig（從 CoreWeave 取得）
export KUBECONFIG=~/.kube/coreweave.yaml

# 確認節點
kubectl get nodes

# 列出可用 GPU 資源
kubectl get node -o json | jq '.items[].status.allocatable["nvidia.com/gpu"]'
```

## 部署範例

PyTorch 訓練 Job：

```yaml
# training-job.yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: pytorch-training
spec:
  template:
    spec:
      containers:
        - name: trainer
          image: pytorch/pytorch:2.1.0-cuda12.1-cudnn8-runtime
          command: ["python", "train.py"]
          resources:
            requests:
              cpu: "8"
              memory: "64Gi"
              nvidia.com/gpu: "8"
            limits:
              nvidia.com/gpu: "8"
          volumeMounts:
            - name: model-storage
              mountPath: /models
      volumes:
        - name: model-storage
          persistentVolumeClaim:
            claimName: my-model-pvc
      restartPolicy: Never
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: gpu.nvidia.com/class
                    operator: In
                    values:
                      - H100_NVLINK_80GB
```

```bash
kubectl apply -f training-job.yaml
kubectl logs -f job/pytorch-training
```

推論 Deployment（長期服務）：

```yaml
# inference-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: llm-inference
spec:
  replicas: 2
  selector:
    matchLabels:
      app: llm-inference
  template:
    metadata:
      labels:
        app: llm-inference
    spec:
      containers:
        - name: inference
          image: vllm/vllm-openai:latest
          args: ["--model", "meta-llama/Llama-2-7b-chat-hf"]
          resources:
            limits:
              nvidia.com/gpu: "1"
          ports:
            - containerPort: 8000
```

---

# 儲存

```yaml
# PersistentVolumeClaim（高效能 NVMe SSD）
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: my-model-pvc
spec:
  accessModes:
    - ReadWriteMany          # 多 Pod 共用
  storageClassName: ceph-ssd # 高效能
  resources:
    requests:
      storage: 500Gi
```

儲存類型：

| StorageClass | 說明 | 適合情境 |
|---|---|---|
| `ceph-ssd` | NVMe SSD，低延遲 | 模型檔案、訓練資料 |
| `ceph-hdd` | HDD，便宜 | 冷儲存、備份 |
| `sharedfs` | NFS，多 Pod 共用 | 分散式訓練共用資料集 |

---

# 網路

CoreWeave 提供 InfiniBand 互連，支援高速多機訓練（NCCL）：

```bash
# 分散式訓練（多節點）
torchrun \
  --nnodes=4 \
  --nproc_per_node=8 \
  --rdzv_backend=c10d \
  --rdzv_endpoint=$MASTER_ADDR:29500 \
  train.py
```

---

# 注意事項

- **需申請**：提交申請後等待審核（通常幾個工作天），不適合立即需要的情境
- **k8s 門檻**：需要熟悉 Kubernetes，不適合不懂 k8s 的個人用戶
- **無免費試用**：從第一個 Pod 開始計費，無免費額度
- **最適情境**：LLM 訓練、大型推論服務、需要穩定 SLA 的 AI 產品
- **成本**：比 Vast.ai / RunPod 貴，但比 AWS / GCP 便宜約 30–50%
