# Kubernetes (K8s)

```
容器編排平台：自動部署、擴展、管理容器化應用
核心概念：Pod / Node / Cluster / Service / Deployment / Namespace
```

## 目錄

- [Kubernetes (K8s)](#kubernetes-k8s)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署（本地開發）](#docker-compose-部署本地開發)
  - [Debian (Ubuntu) — kubeadm 叢集](#debian-ubuntu--kubeadm-叢集)
  - [Homebrew (MacOS) — minikube 本地](#homebrew-macos--minikube-本地)
  - [Windows — minikube](#windows--minikube)
  - [kubectl 設定（kubeconfig）](#kubectl-設定kubeconfig)
- [核心概念](#核心概念)
  - [架構總覽](#架構總覽)
  - [物件層級](#物件層級)
- [Manifest 範例](#manifest-範例)
  - [Deployment](#deployment)
  - [Service](#service)
  - [ConfigMap](#configmap)
  - [Secret](#secret)
  - [Ingress](#ingress)
  - [PersistentVolumeClaim (PVC)](#persistentvolumeclaim-pvc)
- [指令](#指令)
  - [叢集資訊](#叢集資訊)
  - [Namespace](#namespace)
  - [Pod 操作](#pod-操作)
  - [Deployment 操作](#deployment-操作)
  - [Service 操作](#service-操作)
  - [Log / 除錯](#log--除錯)
  - [Apply / Delete](#apply--delete)
- [常見部署模式](#常見部署模式)
  - [Rolling Update（預設）](#rolling-update預設)
  - [藍綠部署（Blue-Green）](#藍綠部署blue-green)
  - [Canary Release](#canary-release)
- [Helm](#helm)
  - [安裝 Helm](#安裝-helm)
  - [常用指令](#常用指令)

## 參考資料

[Kubernetes 官方文件](https://kubernetes.io/docs/)

[kubectl 速查表](https://kubernetes.io/docs/reference/kubectl/quick-reference/)

[Helm 官方文件](https://helm.sh/docs/)

[minikube 官方文件](https://minikube.sigs.k8s.io/docs/)

---

# 安裝

## docker-compose 部署（本地開發）

> 本地學習環境建議使用 minikube 或 kind，而非 docker-compose 直接模擬 K8s。

```bash
# 使用 kind（Kubernetes IN Docker）
# 安裝 kind
brew install kind   # macOS

# 建立本地叢集
kind create cluster --name dev

# 確認叢集
kubectl cluster-info --context kind-dev
```

## Debian (Ubuntu) — kubeadm 叢集

```bash
# 安裝 containerd
sudo apt-get install -y containerd

# 新增 K8s apt 來源
curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.31/deb/Release.key | \
  sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg

echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] \
  https://pkgs.k8s.io/core:/stable:/v1.31/deb/ /' | \
  sudo tee /etc/apt/sources.list.d/kubernetes.list

sudo apt-get update
sudo apt-get install -y kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl

# 初始化 Master Node
sudo kubeadm init --pod-network-cidr=10.244.0.0/16

# 設定 kubeconfig
mkdir -p $HOME/.kube
sudo cp /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

# 安裝 CNI（Flannel）
kubectl apply -f https://github.com/flannel-io/flannel/releases/latest/download/kube-flannel.yml
```

## Homebrew (MacOS) — minikube 本地

```bash
brew install minikube kubectl

# 啟動叢集
minikube start --driver=docker

# 停止
minikube stop

# 刪除
minikube delete
```

## Windows — minikube

```powershell
# 需先安裝 Docker Desktop
winget install Kubernetes.minikube
winget install Kubernetes.kubectl

minikube start
```

## kubectl 設定（kubeconfig）

```bash
# 預設設定檔位置
~/.kube/config

# 切換 context（叢集）
kubectl config get-contexts
kubectl config use-context <context-name>

# 設定預設 namespace
kubectl config set-context --current --namespace=my-app
```

---

# 核心概念

## 架構總覽

```
Cluster
├── Control Plane（Master）
│   ├── kube-apiserver      ← 所有操作入口
│   ├── etcd                ← 叢集狀態儲存（key-value）
│   ├── kube-scheduler      ← 決定 Pod 分配到哪個 Node
│   └── kube-controller-manager ← 維持期望狀態
└── Worker Node（可多台）
    ├── kubelet             ← 管理此 Node 上的 Pod
    ├── kube-proxy          ← 網路規則（Service 路由）
    └── Container Runtime   ← containerd / Docker
```

## 物件層級

| 物件 | 說明 | 對應概念 |
|---|---|---|
| **Cluster** | 整個 K8s 環境 | 整座資料中心 |
| **Namespace** | 邏輯隔離空間 | 虛擬分組（dev / prod） |
| **Node** | 實體或虛擬機器 | 一台伺服器 |
| **Pod** | 最小部署單位，含一或多個 container | 一個應用程式實例 |
| **Deployment** | 管理 Pod 副本數與更新策略 | 應用部署配置 |
| **Service** | 穩定的網路端點，對應一組 Pod | 負載均衡器 + DNS |
| **ConfigMap** | 非敏感設定（env / config file） | 設定檔 |
| **Secret** | 敏感設定（密碼 / token），Base64 編碼 | 加密設定檔 |
| **Ingress** | HTTP/HTTPS 路由規則（L7） | Nginx reverse proxy |
| **PVC / PV** | 持久化儲存申請 / 實際儲存資源 | 硬碟掛載 |

---

# Manifest 範例

## Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
  namespace: production
  labels:
    app: my-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
        - name: my-app
          image: my-registry/my-app:1.2.0
          ports:
            - containerPort: 8080
          env:
            - name: DB_HOST
              valueFrom:
                configMapKeyRef:
                  name: my-app-config
                  key: db_host
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: my-app-secret
                  key: db_password
          resources:
            requests:
              cpu: "100m"
              memory: "128Mi"
            limits:
              cpu: "500m"
              memory: "512Mi"
          readinessProbe:
            httpGet:
              path: /health
              port: 8080
            initialDelaySeconds: 5
            periodSeconds: 10
          livenessProbe:
            httpGet:
              path: /health
              port: 8080
            initialDelaySeconds: 15
            periodSeconds: 20
```

## Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-app-svc
  namespace: production
spec:
  selector:
    app: my-app          # 對應 Deployment 的 labels
  ports:
    - protocol: TCP
      port: 80            # Service 對外 port
      targetPort: 8080    # Container port
  type: ClusterIP         # ClusterIP（內部）/ NodePort（節點IP+Port）/ LoadBalancer（雲端LB）
```

## ConfigMap

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-app-config
  namespace: production
data:
  db_host: "postgres-svc"
  db_name: "mydb"
  log_level: "info"
```

## Secret

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: my-app-secret
  namespace: production
type: Opaque
data:
  db_password: cGFzc3dvcmQxMjM=   # echo -n 'password123' | base64
```

```bash
# 建立 Secret（建議用指令，避免 base64 直接寫在 YAML）
kubectl create secret generic my-app-secret \
  --from-literal=db_password=password123 \
  --namespace=production
```

## Ingress

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-app-ingress
  namespace: production
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  ingressClassName: nginx
  tls:
    - hosts:
        - api.example.com
      secretName: tls-secret
  rules:
    - host: api.example.com
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: my-app-svc
                port:
                  number: 80
```

## PersistentVolumeClaim (PVC)

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: my-app-storage
  namespace: production
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
  storageClassName: standard
```

```yaml
# 在 Deployment 中掛載
volumes:
  - name: app-data
    persistentVolumeClaim:
      claimName: my-app-storage
containers:
  - name: my-app
    volumeMounts:
      - mountPath: /data
        name: app-data
```

---

# 指令

## 叢集資訊

```bash
kubectl cluster-info
kubectl get nodes
kubectl get nodes -o wide          # 含 IP、OS、Runtime 資訊
kubectl describe node <node-name>
kubectl top nodes                  # 需安裝 metrics-server
```

## Namespace

```bash
kubectl get namespaces
kubectl create namespace staging
kubectl delete namespace staging

# 查看特定 namespace 的所有資源
kubectl get all -n staging
```

## Pod 操作

```bash
kubectl get pods -n <namespace>
kubectl get pods -n <namespace> -o wide    # 顯示 Node 與 IP
kubectl get pods --all-namespaces

kubectl describe pod <pod-name> -n <namespace>
kubectl exec -it <pod-name> -n <namespace> -- /bin/bash
kubectl cp <pod-name>:/path/to/file ./local-file -n <namespace>

kubectl delete pod <pod-name> -n <namespace>
```

## Deployment 操作

```bash
kubectl get deployments -n <namespace>
kubectl describe deployment <name> -n <namespace>

# 更新映像檔
kubectl set image deployment/<name> <container>=<image>:<tag> -n <namespace>

# 手動擴縮
kubectl scale deployment/<name> --replicas=5 -n <namespace>

# 回滾
kubectl rollout history deployment/<name> -n <namespace>
kubectl rollout undo deployment/<name> -n <namespace>
kubectl rollout undo deployment/<name> --to-revision=2 -n <namespace>

# 查看更新狀態
kubectl rollout status deployment/<name> -n <namespace>
```

## Service 操作

```bash
kubectl get services -n <namespace>
kubectl describe service <name> -n <namespace>

# 本地 port-forward（不透過 Ingress，直接測試）
kubectl port-forward svc/<name> 8080:80 -n <namespace>
kubectl port-forward pod/<pod-name> 8080:8080 -n <namespace>
```

## Log / 除錯

```bash
# 查看 Log
kubectl logs <pod-name> -n <namespace>
kubectl logs <pod-name> -n <namespace> --tail=100
kubectl logs <pod-name> -n <namespace> -f            # 即時串流
kubectl logs <pod-name> -n <namespace> -c <container-name>  # 多 container pod

# 查看前一次崩潰的 log
kubectl logs <pod-name> -n <namespace> --previous

# 查看事件（排查 Pod 啟動失敗原因）
kubectl get events -n <namespace> --sort-by=.lastTimestamp
kubectl describe pod <pod-name> -n <namespace>    # Events 區塊最重要
```

## Apply / Delete

```bash
# 套用 Manifest
kubectl apply -f deployment.yaml
kubectl apply -f ./k8s/                 # 套用目錄下所有 yaml

# 刪除資源
kubectl delete -f deployment.yaml
kubectl delete deployment <name> -n <namespace>

# Dry Run（確認不實際執行）
kubectl apply -f deployment.yaml --dry-run=client
```

---

# 常見部署模式

## Rolling Update（預設）

```yaml
strategy:
  type: RollingUpdate
  rollingUpdate:
    maxSurge: 1         # 最多多出 1 個 Pod
    maxUnavailable: 0   # 確保全程不中斷
```

```
舊 Pod 逐步替換，零停機時間
適合：一般 Web 應用
```

## 藍綠部署（Blue-Green）

```bash
# 同時部署新版（green），Service 切換流量
kubectl apply -f deployment-green.yaml

# 切換 Service selector
kubectl patch service my-app-svc \
  -p '{"spec":{"selector":{"version":"green"}}}'

# 確認無誤後刪除舊版（blue）
kubectl delete deployment my-app-blue
```

```
新舊版本並存，瞬間切換，回滾快速
缺點：需 2x 資源
```

## Canary Release

```yaml
# 主要版本：90% 流量（9 個副本）
# Canary 版本：10% 流量（1 個副本）
# 兩個 Deployment 共用同一個 Service selector（app: my-app）

# canary-deployment.yaml
metadata:
  name: my-app-canary
spec:
  replicas: 1
  selector:
    matchLabels:
      app: my-app
      version: canary
  template:
    metadata:
      labels:
        app: my-app
        version: canary
    spec:
      containers:
        - name: my-app
          image: my-registry/my-app:2.0.0
```

---

# Helm

Helm 是 K8s 的套件管理工具，將一組 Manifest 打包為 Chart，方便版本管理與環境差異化。

## 安裝 Helm

```bash
# macOS
brew install helm

# Linux
curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash
```

## 常用指令

```bash
# 新增 Chart Repository
helm repo add stable https://charts.helm.sh/stable
helm repo update

# 搜尋
helm search repo nginx

# 安裝 Chart
helm install my-nginx stable/nginx-ingress \
  --namespace ingress-nginx \
  --create-namespace \
  --set controller.replicaCount=2

# 升級
helm upgrade my-nginx stable/nginx-ingress --set controller.replicaCount=3

# 查看已安裝
helm list -A

# 回滾
helm rollback my-nginx 1

# 移除
helm uninstall my-nginx -n ingress-nginx

# 產生 Manifest（不安裝，僅輸出 YAML）
helm template my-nginx stable/nginx-ingress > rendered.yaml
```

```bash
# 建立自訂 Chart
helm create my-chart
# 結構：
# my-chart/
# ├── Chart.yaml        ← 基本資訊（名稱、版本）
# ├── values.yaml       ← 預設參數值
# └── templates/        ← Manifest 模板（使用 Go template 語法）
#     ├── deployment.yaml
#     └── service.yaml
```
