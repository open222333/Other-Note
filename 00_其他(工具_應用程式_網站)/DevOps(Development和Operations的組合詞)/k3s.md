# k3s

```
輕量級 Kubernetes 發行版，由 Rancher Labs（SUSE）開發
單一二進位檔（< 100MB），預設使用 SQLite 取代 etcd
適用場景：邊緣運算、IoT、本地開發、CI/CD、ARM 裝置
完整相容 K8s API，可直接使用 kubectl 操作
```

## 目錄

- [k3s](#k3s)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [k3s 與 K8s 差異](#k3s-與-k8s-差異)
- [安裝](#安裝)
  - [Linux（快速安裝腳本）](#linux快速安裝腳本)
  - [Debian (Ubuntu) — 手動安裝](#debian-ubuntu--手動安裝)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS) — 模擬環境](#homebrew-macos--模擬環境)
  - [Windows — Rancher Desktop](#windows--rancher-desktop)
  - [配置文檔](#配置文檔)
    - [Server 設定（/etc/rancher/k3s/config.yaml）](#server-設定etcrancherk3sconfigyaml)
    - [多節點叢集（Agent 加入）](#多節點叢集agent-加入)
- [指令](#指令)
  - [服務管理](#服務管理)
  - [叢集資訊](#叢集資訊)
  - [常用 kubectl 操作](#常用-kubectl-操作)
- [Helm 搭配 k3s](#helm-搭配-k3s)
- [內建元件](#內建元件)
- [應用範例：Python Flask + MongoDB + Redis 部署](#應用範例python-flask--mongodb--redis-部署)
  - [Manifest 目錄結構](#manifest-目錄結構)
  - [namespace.yaml](#namespaceyaml)
  - [secret.yaml](#secretyaml)
  - [mongodb-statefulset.yaml](#mongodb-statefulseyaml)
  - [redis-deployment.yaml](#redis-deploymentyaml)
  - [flask-api-deployment.yaml](#flask-api-deploymentyaml)
  - [flask-api-hpa.yaml](#flask-api-hpayaml)
  - [ingress.yaml](#ingressyaml)
  - [部署流程](#部署流程)

## 參考資料

[k3s 官方文件](https://docs.k3s.io/)

[k3s GitHub](https://github.com/k3s-io/k3s)

[k3s Quick Start](https://docs.k3s.io/quick-start)

[k3s vs K8s 比較](https://docs.k3s.io/architecture)

---

# k3s 與 K8s 差異

| 項目 | K8s（kubeadm） | k3s |
|---|---|---|
| 二進位大小 | ~500MB（多個元件） | ~100MB（單一二進位） |
| 預設資料庫 | etcd | SQLite（可換 etcd / Postgres / MySQL） |
| 安裝複雜度 | 高（需設定多元件） | 低（單行指令） |
| 記憶體需求 | ~1GB+ | ~512MB |
| Container Runtime | containerd | containerd（內建） |
| Ingress Controller | 需手動安裝 | 內建 Traefik |
| Load Balancer | 需雲端 LB 或 MetalLB | 內建 ServiceLB（Klipper） |
| 適用場景 | 生產大型叢集 | 邊緣、IoT、本地開發 |

---

# 安裝

## Linux（快速安裝腳本）

```bash
# 安裝並啟動 k3s server（Control Plane）
curl -sfL https://get.k3s.io | sh -

# 確認狀態
sudo systemctl status k3s

# 取得 kubeconfig（root 權限）
sudo cat /etc/rancher/k3s/k3s.yaml

# 設定一般使用者可使用 kubectl
mkdir -p ~/.kube
sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
sudo chown $USER ~/.kube/config

# 確認節點
kubectl get nodes
```

```bash
# 安裝指定版本
curl -sfL https://get.k3s.io | INSTALL_K3S_VERSION=v1.31.0+k3s1 sh -

# 安裝並關閉 Traefik（使用自訂 Ingress）
curl -sfL https://get.k3s.io | INSTALL_K3S_EXEC="--disable traefik" sh -
```

## Debian (Ubuntu) — 手動安裝

```bash
# 下載二進位
curl -Lo /usr/local/bin/k3s \
  https://github.com/k3s-io/k3s/releases/latest/download/k3s
chmod +x /usr/local/bin/k3s

# 建立 systemd 服務
curl -Lo /etc/systemd/system/k3s.service \
  https://raw.githubusercontent.com/k3s-io/k3s/main/k3s.service

systemctl enable k3s
systemctl start k3s
```

## RedHat (CentOS)

```bash
# 同 Linux 快速安裝腳本（支援 RHEL / CentOS / Fedora）
curl -sfL https://get.k3s.io | sh -

# 開放防火牆（叢集節點間通訊）
firewall-cmd --permanent --add-port=6443/tcp   # API server
firewall-cmd --permanent --add-port=10250/tcp  # kubelet
firewall-cmd --reload
```

## Homebrew (MacOS) — 模擬環境

> macOS 不支援直接執行 k3s，建議使用 Rancher Desktop 或 OrbStack

```bash
# 安裝 Rancher Desktop（含 k3s）
brew install --cask rancher

# 或使用 OrbStack（整合 k3s）
brew install --cask orbstack
```

## Windows — Rancher Desktop

```powershell
# 安裝 Rancher Desktop（含 k3s + kubectl）
winget install suse.rancher-desktop

# 或透過 WSL2 執行 Linux 版安裝腳本
wsl curl -sfL https://get.k3s.io | sh -
```

## 配置文檔

### Server 設定（/etc/rancher/k3s/config.yaml）

通常在 `/etc/rancher/k3s/config.yaml`

```yaml
# k3s server 設定範例
write-kubeconfig-mode: "0644"    # 讓一般使用者可讀 kubeconfig
disable:
  - traefik                       # 停用內建 Traefik
  - servicelb                     # 停用內建 LoadBalancer
tls-san:
  - "192.168.1.100"               # 額外允許的 API server 位址
  - "k3s.example.com"
cluster-cidr: "10.42.0.0/16"     # Pod 網段（預設）
service-cidr: "10.43.0.0/16"     # Service 網段（預設）
```

### 多節點叢集（Agent 加入）

```bash
# 在 Server 取得 node-token
sudo cat /var/lib/rancher/k3s/server/node-token

# 在 Agent 節點執行（加入叢集）
curl -sfL https://get.k3s.io | \
  K3S_URL=https://<SERVER_IP>:6443 \
  K3S_TOKEN=<NODE_TOKEN> \
  sh -

# 確認所有節點
kubectl get nodes
```

---

# 指令

## 服務管理

```bash
# 啟動 / 停止 / 重啟
sudo systemctl start k3s
sudo systemctl stop k3s
sudo systemctl restart k3s

# 查看 log
sudo journalctl -u k3s -f

# 解除安裝
/usr/local/bin/k3s-uninstall.sh           # Server
/usr/local/bin/k3s-agent-uninstall.sh     # Agent
```

## 叢集資訊

```bash
# 查看節點（k3s 內建的 kubectl）
sudo k3s kubectl get nodes
sudo k3s kubectl get nodes -o wide

# 也可直接用系統 kubectl（設定好 kubeconfig 後）
kubectl get nodes
kubectl cluster-info
kubectl get all --all-namespaces
```

## 常用 kubectl 操作

```bash
# 部署應用
kubectl apply -f deployment.yaml

# 查看 Pod
kubectl get pods -A
kubectl logs <pod-name> -n <namespace> -f

# port-forward 本地測試
kubectl port-forward svc/<name> 8080:80

# 刪除資源
kubectl delete -f deployment.yaml
```

---

# Helm 搭配 k3s

```bash
# 安裝 Helm
curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

# k3s 的 kubeconfig 路徑
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

# 確認 Helm 可連線
helm list -A

# 安裝範例：cert-manager
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  --create-namespace \
  --set crds.enabled=true
```

---

# 內建元件

| 元件 | 說明 | 停用方式 |
|---|---|---|
| **Traefik** | Ingress Controller（L7 路由） | `--disable traefik` |
| **ServiceLB（Klipper）** | Service type=LoadBalancer 支援 | `--disable servicelb` |
| **CoreDNS** | 叢集內 DNS 解析 | `--disable coredns` |
| **Local Path Provisioner** | 本地 PVC 動態佈建 | `--disable local-storage` |
| **Metrics Server** | HPA / `kubectl top` 支援 | `--disable metrics-server` |

```bash
# 查看目前內建元件狀態
kubectl get pods -n kube-system
```

---

# 應用範例：Python Flask + MongoDB + Redis 部署

> 適用於 Flask + Gunicorn + MongoDB + Redis + Nginx 架構的 ERP/WMS 類專案。
> k3s 在單台 VPS（2C4G 起）即可運行完整堆疊，比 docker-compose 多出 HPA 自動擴縮能力。

## Manifest 目錄結構

```
k8s/
├── namespace.yaml
├── secret.yaml               # 對應 .env 敏感變數
├── mongodb-statefulset.yaml  # 有狀態，需 PVC
├── redis-deployment.yaml
├── flask-api-deployment.yaml
├── flask-api-hpa.yaml        # 水平自動擴縮
└── ingress.yaml              # 取代 docker-compose nginx
```

## namespace.yaml

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: erp-wms
```

## secret.yaml

> 比 `.env` 更安全；`stringData` 會自動 base64 編碼，免手動轉換。

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: erp-secrets
  namespace: erp-wms
stringData:
  MONGO_URI: "mongodb://mongo:27017/erp"
  REDIS_URL: "redis://redis:6379/0"
  JWT_SECRET_KEY: "your-secret-key"
  FLASK_PORT: "5000"
  GUNICORN_WORKERS: "4"
  GUNICORN_THREADS: "2"
```

## mongodb-statefulset.yaml

> MongoDB 必須用 StatefulSet + Headless Service，否則 Pod 重建後資料遺失。

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: mongo
  namespace: erp-wms
spec:
  serviceName: mongo
  replicas: 1           # 單機；生產多節點改為 3（需搭配 Replica Set 設定）
  selector:
    matchLabels:
      app: mongo
  template:
    metadata:
      labels:
        app: mongo
    spec:
      containers:
      - name: mongo
        image: mongo:7
        resources:
          requests:
            memory: "512Mi"
          limits:
            memory: "1Gi"
        volumeMounts:
        - name: mongo-data
          mountPath: /data/db
  volumeClaimTemplates:
  - metadata:
      name: mongo-data
    spec:
      accessModes: ["ReadWriteOnce"]
      resources:
        requests:
          storage: 20Gi
---
apiVersion: v1
kind: Service
metadata:
  name: mongo
  namespace: erp-wms
spec:
  clusterIP: None       # headless：StatefulSet DNS 解析需要
  selector:
    app: mongo
  ports:
  - port: 27017
```

## redis-deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis
  namespace: erp-wms
spec:
  replicas: 1
  selector:
    matchLabels:
      app: redis
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
      - name: redis
        image: redis:7-alpine
        args: ["--maxmemory", "128mb", "--maxmemory-policy", "allkeys-lru"]
        resources:
          limits:
            memory: "192Mi"
        ports:
        - containerPort: 6379
---
apiVersion: v1
kind: Service
metadata:
  name: redis
  namespace: erp-wms
spec:
  selector:
    app: redis
  ports:
  - port: 6379
```

## flask-api-deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: flask-api
  namespace: erp-wms
spec:
  replicas: 2
  selector:
    matchLabels:
      app: flask-api
  template:
    metadata:
      labels:
        app: flask-api
    spec:
      containers:
      - name: flask-api
        image: your-registry/python-erp-wms:latest
        command: ["gunicorn", "-c", "gunicorn.py", "run:app"]
        envFrom:
        - secretRef:
            name: erp-secrets
        ports:
        - containerPort: 5000
        readinessProbe:
          httpGet:
            path: /api/docs/    # Swagger 端點，Flask 啟動後才會回應 200
            port: 5000
          initialDelaySeconds: 15
          periodSeconds: 5
        livenessProbe:
          httpGet:
            path: /api/docs/
            port: 5000
          initialDelaySeconds: 30
          periodSeconds: 15
        resources:
          requests:
            cpu: "250m"
            memory: "256Mi"
          limits:
            cpu: "1"
            memory: "512Mi"
---
apiVersion: v1
kind: Service
metadata:
  name: flask-api
  namespace: erp-wms
spec:
  selector:
    app: flask-api
  ports:
  - port: 5000
```

## flask-api-hpa.yaml

> CPU 使用率超過 70% 時自動從 2 個 Pod 擴縮到最多 10 個。
> 需要 Metrics Server（k3s 內建已啟用）。

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: flask-api-hpa
  namespace: erp-wms
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: flask-api
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

## ingress.yaml

> k3s 內建 Traefik，直接用 Traefik annotation；若改裝 Nginx Ingress 則換 annotation。

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: erp-ingress
  namespace: erp-wms
  annotations:
    traefik.ingress.kubernetes.io/router.entrypoints: websecure
    cert-manager.io/cluster-issuer: letsencrypt-prod    # 需先安裝 cert-manager
spec:
  rules:
  - host: erp.yourdomain.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: flask-api
            port:
              number: 5000
  tls:
  - hosts:
    - erp.yourdomain.com
    secretName: erp-tls
```

## 部署流程

```bash
# 1. 安裝 k3s（VPS 上執行）
curl -sfL https://get.k3s.io | sh -

# 2. 安裝 cert-manager（HTTPS 自動簽憑證）
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/latest/download/cert-manager.yaml

# 3. 套用所有 Manifest
kubectl apply -f k8s/

# 4. 確認服務狀態
kubectl get all -n erp-wms

# 5. 確認 Flask Pod 就緒（readinessProbe 通過後才收流量）
kubectl get pods -n erp-wms -w

# 6. 查看 Flask log
kubectl logs -n erp-wms -l app=flask-api -f

# 更新映像後滾動更新（零停機）
kubectl set image deployment/flask-api flask-api=your-registry/python-erp-wms:v2 -n erp-wms
kubectl rollout status deployment/flask-api -n erp-wms
```
