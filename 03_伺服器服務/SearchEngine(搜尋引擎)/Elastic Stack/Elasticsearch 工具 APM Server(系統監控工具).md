# Elasticsearch 工具 APM Server(系統監控工具)

```
Elastic APM 提供應用層的性能監控，能夠詳細跟蹤各請求的響應時間、調用鏈和錯誤狀況等，有助於找出哪些請求或服務引發了流量高峰。
```

## 目錄

- [Linux 工具 sample($num)()](#linux-工具-sample$num)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [配置文檔](#配置文檔)
    - [基本範例](#基本範例)
- [指令](#指令)
  - [服務操作](#服務操作)

## 參考資料

[elastic 官方網站 apm docker image](https://www.docker.elastic.co/r/apm)

[Quick start development environment](https://www.elastic.co/guide/en/apm/guide/7.17/quick-start-overview.html)

# 安裝

## docker-compose 部署

```yml
version: '3'
services:
  elasticsearch:
    image: elasticsearch:${STACK_VERSION}
    container_name: ${ES_CONTAINER_NAME}
    privileged: true
    restart: always
    environment:
      - "ES_JAVA_OPTS=-Xms16g -Xmx16g" # 設置使用jvm內存大小
      - "ES_HEAP_SIZE=512m"
      - "MAX_OPEN_FILES=65535"
      - "MAX_LOCKED_MEMORY=unlimited"
    volumes:
      - ./es/plugins:/usr/share/elasticsearch/plugins
      - ./es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml:ro
      - ./es/data:/usr/share/elasticsearch/data:rw # 數據文件掛載
      - ./es/logs:/usr/share/elasticsearch/logs:rw
    ports:
      - 9200:9200
      - 9300:9300

  kibana:
    image: kibana:${STACK_VERSION}
    container_name: kibana
    depends_on:
      - elasticsearch # kibana在elasticsearch啟動之後再啟動
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200 # 設置訪問elasticsearch的地址
      I18N_LOCALE: zh-CN
    ports:
      - 5601:5601

  apm-server:
    # 根據 主機架構
    # x86_64：代表 64 位元的 x86 架構（即 amd64）。
    # arm64 或 aarch64：代表 64 位元的 ARM 架構。
    # https://www.docker.elastic.co/r/apm
    image: docker.elastic.co/apm/apm-server:sha256-ff9bb3eac97600a86976b71f999fb86e2500ae1621a6ee2feb44b8ad646a3ff6
    container_name: apm-server
    depends_on:
      - elasticsearch
    environment:
      - "ELASTICSEARCH_HOSTS=http://elasticsearch:9200" # 設置 APM Server 連接的 Elasticsearch 地址
      - "APM_SERVER_HOST=0.0.0.0:8200" # 設置 APM Server 的監聽地址
    ports:
      - 8200:8200
    volumes:
      - ./apm-server/config/apm-server.yml:/usr/share/apm-server/apm-server.yml:ro
```

```env
# 設定 Elastic Stack 版本，確保這個版本支持 ElasticSearch、Kibana 和 APM Server
STACK_VERSION=8.10.0

# Elasticsearch 容器名稱
ES_CONTAINER_NAME=elasticsearch
```

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

```bash
curl -L -O https://artifacts.elastic.co/downloads/apm-server/apm-server-${version}-x86_64.rpm
sudo rpm -vi apm-server-${version}-x86_64.rpm
```

7.13.3

```sh
curl -L -O https://artifacts.elastic.co/downloads/apm-server/apm-server-7.13.3-x86_64.rpm
sudo rpm -vi apm-server-7.13.3-x86_64.rpm
```

## Homebrew (MacOS)

```bash
```

## 配置文檔

通常在 `apm-server.yml`

### 基本範例

```yml
output.elasticsearch:
    hosts: ["<es_url>"]
    username: <username>
    password: <password>
```

### 範例

```yml
# ========================= APM Server Configuration ==========================

apm-server:
  # 監聽地址和端口
  host: "0.0.0.0:8200"

  # 允許未經驗證的代理將數據傳送到 APM Server（僅限開發環境）
  rum:
    enabled: true
    allow_origins: ["*"]

  # 設置 API 安全性（選擇性）
  api_key:
    enabled: true

  # 定義影響數據指標收集的選項
  capture_personal_data: true

# =========================== Elasticsearch Output ===========================

output.elasticsearch:
  # Elasticsearch 地址
  hosts: ["http://elasticsearch:9200"]

  # 如果使用了 API 金鑰或基本驗證，請在這裡進行配置
  # api_key: "your_api_key"
  # username: "elastic"
  # password: "your_password"

  # 設置批量處理
  bulk_max_size: 50
  flush_interval: 1s

# ============================== Logging 配置 ==============================

logging:
  # 設置日誌等級（可選：error, warning, info, debug）
  level: info

  # 日誌輸出到 stdout
  to_stdout: true
  json: true

  # 日誌文件路徑（選擇性，Docker 時無需此項）
  # files:
  #   path: "/var/log/apm-server"
  #   name: "apm-server.log"
  #   keepfiles: 7
  #   permissions: 0644

# ============================== Instrumentation ==============================

# 選擇性：啟用內建指標（如 APM Server 自身的健康狀態）
instrumentation:
  enabled: true
  environment: "production"
  # 如果想把 APM Server 自己的指標數據送到其他 APM Server，這裡可以設置 API Endpoint。
  # hosts:
```

### Docker

```yml
# 連接至 Elasticsearch
output.elasticsearch:
  hosts: ["http://elasticsearch:9200"]
  username: "elastic"            # 設定為 Elasticsearch 使用者名稱
  password: "your_password"       # 設定為 Elasticsearch 密碼

# APM Server 基本設定
apm-server:
  host: "0.0.0.0:8200"            # 監聽所有 IP 地址上的 8200 埠

  # 設定允許的環境
  rum.enabled: true               # 開啟 RUM 支援，用於前端應用程式追蹤

  # 設置 API 金鑰（選擇性）
  api_key.enabled: true

# 採樣率設定
# 控制追蹤的採樣比例，1.0 表示 100% 的請求會被追蹤
# 可根據需求調整追蹤量，減少負載
sampling:
  keep_unsampled: true
  rate: 1.0                       # 預設為 100% 取樣

# 日誌等級 (DEBUG, INFO, WARN, ERROR)
logging.level: info
logging.to_files: true
logging.files:
  path: "/var/log/apm-server"
  name: "apm-server"
  keepfiles: 7                    # 保留 7 個日誌檔案
  permissions: 0644               # 設定檔案權限

# 視需求設定資料保護功能
data_streams.enabled: true        # 使用 Data Streams 發送資料
data_streams.namespace: "default"

# 其他進階設定
monitoring:
  enabled: true                   # 啟用內建的 Elastic 監控
  logs: true                      # 收集 APM Server 自身的日誌
  metrics: true                   # 收集 APM Server 自身的指標

queue:
  mem:
    events: 4096                  # 設定內存中的事件數量
    flush.min_events: 512         # 最少事件數量後刷新資料
    flush.timeout: 1s             # 超過 1 秒後刷新資料
```

# 指令

```sh
service apm-server start
```

# 例外狀況

## exec /usr/bin/tini: exec format error

```
嘗試運行的映像與主機的架構不相容
```

確認映像的架構：檢查拉取的 APM Server 映像是否適合主機架構。可以使用以下命令檢查映像的詳細資訊

```bash
docker image inspect docker.elastic.co/apm/apm-server:${STACK_VERSION}
```

Linux 或 macOS

x86_64：代表 64 位元的 x86 架構（即 amd64）。

arm64 或 aarch64：代表 64 位元的 ARM 架構。

```sh
uname -m
```

Windows PowerShell

```PowerShell
[System.Environment]::Is64BitOperatingSystem
```

docker

```
PowerSdocker info | grep Architecture
```
