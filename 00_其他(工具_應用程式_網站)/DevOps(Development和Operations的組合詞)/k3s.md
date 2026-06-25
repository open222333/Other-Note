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
