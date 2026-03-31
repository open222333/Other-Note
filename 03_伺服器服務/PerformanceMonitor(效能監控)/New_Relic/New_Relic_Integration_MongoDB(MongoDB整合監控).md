# New Relic Integration MongoDB(MongoDB整合監控)

```
New Relic MongoDB On-host Integration 透過在伺服器安裝 nri-mongodb，
讓 Infrastructure Agent 能夠定期向 MongoDB 收集以下指標：

連線數、操作次數、延遲（latency）
記憶體使用量、WiredTiger Cache 狀態
Replica Set 狀態與同步延遲（replication lag）
資料庫、Collection 層級的讀寫統計
```

## 目錄

- [New Relic Integration MongoDB(MongoDB整合監控)](#new-relic-integration-mongodbbmongodb整合監控)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [配置文檔](#配置文檔)
    - [基本範例（standalone）](#基本範例standalone)
    - [Replica Set 範例](#replica-set-範例)
    - [需要驗證的範例](#需要驗證的範例)
- [MongoDB 監控帳號設定](#mongodb-監控帳號設定)
- [指令](#指令)
  - [驗證 Integration 是否運作](#驗證-integration-是否運作)

## 參考資料

[MongoDB Integration 官方文檔](https://docs.newrelic.com/docs/infrastructure/host-integrations/host-integrations-list/mongodb/mongodb-monitoring-integration/)

[MongoDB Integration GitHub](https://github.com/newrelic/nri-mongodb)

[New Relic Infrastructure On-host Integrations 官方文檔](https://docs.newrelic.com/docs/infrastructure/host-integrations/host-integrations-list/)

### 相關筆記

[New_Relic(雲端應用程式效能監控).md](New_Relic(雲端應用程式效能監控).md)

# 安裝

## Debian (Ubuntu)

```bash
# 確認 Infrastructure Agent 已安裝
systemctl status newrelic-infra

# 安裝 nri-mongodb
sudo apt-get install nri-mongodb -y
```

## RedHat (CentOS)

```bash
# 確認 Infrastructure Agent 已安裝
systemctl status newrelic-infra

# 安裝 nri-mongodb
sudo yum install nri-mongodb -y
```

## 配置文檔

通常在 `/etc/newrelic-infra/integrations.d/mongodb-config.yml`

```bash
# 複製範例設定檔
sudo cp /etc/newrelic-infra/integrations.d/mongodb-config.yml.sample \
        /etc/newrelic-infra/integrations.d/mongodb-config.yml

sudo vim /etc/newrelic-infra/integrations.d/mongodb-config.yml
```

### 基本範例（standalone）

```yml
integrations:
  - name: nri-mongodb
    env:
      # MongoDB 連線位址
      HOST: localhost
      PORT: 27017

      # 監控帳號（需在 MongoDB 建立，參考下方說明）
      USERNAME: newrelic
      PASSWORD: YOUR_PASSWORD
      AUTH_SOURCE: admin

      # 收集 cluster 資訊（mongos 需設為 true）
      CLUSTER_NAME: my-mongodb-cluster

      # 收集層級：database / collection
      ENABLE_COLLECTION_METRICS: "true"
      ENABLE_TOP_METRICS: "true"

    interval: 30
    labels:
      env: production
      role: mongodb
    inventory_source: config/mongodb
```

### Replica Set 範例

```yml
integrations:
  - name: nri-mongodb
    env:
      HOST: localhost
      PORT: 27017
      USERNAME: newrelic
      PASSWORD: YOUR_PASSWORD
      AUTH_SOURCE: admin
      CLUSTER_NAME: my-replica-set

      # Replica Set 模式需列出所有成員
      MONGODB_URI: "mongodb://newrelic:YOUR_PASSWORD@host1:27017,host2:27017,host3:27017/?replicaSet=rs0&authSource=admin"

      ENABLE_COLLECTION_METRICS: "true"
      ENABLE_TOP_METRICS: "true"

    interval: 30
    labels:
      env: production
      role: mongodb-replica
    inventory_source: config/mongodb
```

### 需要驗證的範例

```yml
integrations:
  - name: nri-mongodb
    env:
      HOST: localhost
      PORT: 27017
      USERNAME: newrelic
      PASSWORD: YOUR_PASSWORD
      AUTH_SOURCE: admin

      # TLS/SSL 連線（選填）
      SSL: "true"
      SSL_CA_CERTS: /path/to/ca.pem
      SSL_CERT: /path/to/client.pem

      CLUSTER_NAME: my-mongodb-cluster

    interval: 30
    labels:
      env: production
    inventory_source: config/mongodb
```

# MongoDB 監控帳號設定

在 MongoDB 建立專用監控帳號（最小權限）：

```javascript
// 連線至 MongoDB
mongosh

// 切換至 admin 資料庫
use admin

// 建立監控帳號
db.createUser({
  user: "newrelic",
  pwd: "YOUR_PASSWORD",
  roles: [
    { role: "clusterMonitor", db: "admin" },
    { role: "read", db: "local" }
  ]
})

// 驗證帳號
db.auth("newrelic", "YOUR_PASSWORD")
```

# 指令

## 驗證 Integration 是否運作

```bash
# 重新啟動 Infrastructure Agent 讓設定生效
sudo systemctl restart newrelic-infra

# 查看 Infrastructure Agent 日誌，確認 nri-mongodb 有無錯誤
sudo tail -f /var/log/newrelic-infra/newrelic-infra.log | grep -i mongo

# 手動執行 integration 測試輸出
sudo /var/db/newrelic-infra/newrelic-integrations/bin/nri-mongodb \
  --hostname localhost \
  --port 27017 \
  --username newrelic \
  --password YOUR_PASSWORD \
  --authSource admin \
  --cluster_name test \
  --pretty

# 查看 integration 狀態
sudo systemctl status newrelic-infra
```

## 服務操作

```bash
# 重新啟動（套用設定變更後需重啟）
systemctl restart newrelic-infra

# 查詢啟動狀態
systemctl status newrelic-infra

# 查看即時日誌
journalctl -u newrelic-infra -f
```
