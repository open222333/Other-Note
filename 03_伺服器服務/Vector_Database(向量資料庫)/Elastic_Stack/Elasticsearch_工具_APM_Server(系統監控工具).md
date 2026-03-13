# Elasticsearch 工具 APM Server(系統監控工具)

```
APM（Application Performance Monitoring）

Elastic APM 提供應用層的性能監控，能夠詳細跟蹤各請求的響應時間、調用鏈和錯誤狀況等，有助於找出哪些請求或服務引發了流量高峰。

Elastic APM 的基本架構如上圖共包含四個主要的元件

APM Agents： 提供各種語言實作的 Library，能協助開發人員加在應用程式之中，在應用程式執行的過程中負責收集各種效能相關的資訊或是錯誤的資訊，內含許多常用的 framework 或是 library 的整合，像是 cache 或 db 的存取 library，使用這些 library 時就不用額外自己開發要收集的資訊。

APM Server： 以 libbeat 實作的 HTTP server，負責接收各個 APM Agents 所收集到的 APM 資訊，將這些資訊進行驗證及加工處理後，彙整並傳送給 Elasticsearch 進行 Indexing 及儲存。

Elasticsearch： 負責 APM 資料的儲存，提供分析運算、資料生命週期的管理、資料備份等處理核心。

Kibana APM UI： 讓存在 Elasticsearch 裡的 APM 資料能被快速的查閱、追縱、分析的 UI 工具。
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

[Elastic APM 基本介紹](https://training.onedoggo.com/tech-sharing/uncle-joe-teach-es-elastc-observability/traces-guan-cha-ying-yong-cheng-shi-de-xiao-neng-ping-jing/elastic-apm-ji-ben-jie-shao)

[设置 APM 应用](https://elastic.ac.cn/guide/en/kibana/8.14/apm-ui.html)

[Elastic 官方網站 apm docker image](https://www.docker.elastic.co/r/apm)

```
根據 主機架構
x86_64：代表 64 位元的 x86 架構（即 amd64）。
arm64 或 aarch64：代表 64 位元的 ARM 架構。
```

[Quick start development environment](https://www.elastic.co/guide/en/apm/guide/7.17/quick-start-overview.html)

[使用 APM-Integratoin-Testing 建立 Elastic APM 的模擬環境](https://training.onedoggo.com/tech-sharing/uncle-joe-teach-es-elastc-observability/traces-guan-cha-ying-yong-cheng-shi-de-xiao-neng-ping-jing/shi-yong-apmintegratointesting-jian-li-elastic-apm-de-mo-ni-huan-jing)

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
    image: docker.elastic.co/apm/apm-server:7.17.25
    container_name: apm-server
    depends_on:
      - elasticsearch
    cap_add: ["CHOWN", "DAC_OVERRIDE", "SETGID", "SETUID"]
    cap_drop: ["ALL"]
    ports:
        - 8200:8200
    command: >
       apm-server -e
         -E apm-server.rum.enabled=true
         -E setup.kibana.host=kibana:5601
         -E setup.template.settings.index.number_of_replicas=0
         -E apm-server.kibana.enabled=true
         -E apm-server.kibana.host=kibana:5601
         -E output.elasticsearch.hosts=["elasticsearch:9200"]
```

```env
# 設定 Elastic Stack 版本，確保這個版本支持 ElasticSearch、Kibana 和 APM Server
STACK_VERSION=8.10.0

# Elasticsearch 容器名稱
ES_CONTAINER_NAME=elasticsearch
```

### 官方範例

```yml
version: '2.2'
services:
  apm-server:
    image: docker.elastic.co/apm/apm-server:7.17.25
    depends_on:
      elasticsearch:
        condition: service_healthy
      kibana:
        condition: service_healthy
    cap_add: ["CHOWN", "DAC_OVERRIDE", "SETGID", "SETUID"]
    cap_drop: ["ALL"]
    ports:
        - 8200:8200
    networks:
        - elastic
    command: >
       apm-server -e
         -E apm-server.rum.enabled=true
         -E setup.kibana.host=kibana:5601
         -E setup.template.settings.index.number_of_replicas=0
         -E apm-server.kibana.enabled=true
         -E apm-server.kibana.host=kibana:5601
         -E output.elasticsearch.hosts=["elasticsearch:9200"]
    healthcheck:
      interval: 10s
      retries: 12
      test: curl --write-out 'HTTP %{http_code}' --fail --silent --output /dev/null http://localhost:8200/

  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.17.25
    environment:
        - bootstrap.memory_lock=true
        - cluster.name=docker-cluster
        - cluster.routing.allocation.disk.threshold_enabled=false
        - discovery.type=single-node
        - ES_JAVA_OPTS=-XX:UseAVX=2 -Xms1g -Xmx1g
    ulimits:
      memlock:
        hard: -1
        soft: -1
    volumes:
        - esdata:/usr/share/elasticsearch/data
    ports:
        - 9200:9200
    networks:
        - elastic
    healthcheck:
      interval: 20s
      retries: 10
      test: curl -s http://localhost:9200/_cluster/health | grep -vq '"status":"red"'

  kibana:
    image: docker.elastic.co/kibana/kibana:7.17.25
    depends_on:
      elasticsearch:
        condition: service_healthy
    environment:
      ELASTICSEARCH_URL: http://elasticsearch:9200
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
    ports:
        - 5601:5601
    networks:
        - elastic
    healthcheck:
      interval: 10s
      retries: 20
      test: curl --write-out 'HTTP %{http_code}' --fail --silent --output /dev/null http://localhost:5601/api/status

volumes:
  esdata:
    driver: local

networks:
  elastic:
    driver: bridge
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

## 代理 APM Agent

```
APM（Application Performance Monitoring）Agent 是一種用於監控應用效能的工具，通常負責追蹤應用程式的各種執行數據，例如請求的延遲、錯誤、資源使用情況等。APM Agent 可以幫助開發者快速定位性能瓶頸和錯誤，以提升應用程式的穩定性和效能。

在 Python 中，Elastic APM Agent 是一個常見的選擇，通常用於監控 Flask、Django 等框架。Elastic APM Agent 將監控數據發送至 Elastic APM Server，然後在 Elasticsearch 中存儲和分析這些

常見監控數據
Elastic APM 可以收集以下數據

請求和響應時間：包括 HTTP 請求的處理時間和回應狀態。
錯誤和異常：包括伺服器錯誤或其他異常事件。
資料庫查詢：SQL 查詢的執行時間和效能。
外部 API 調用：調用外部 API 的延遲和錯誤。
```

### Python Flask

監控 Flask 應用

```sh
pip install elastic-apm[flask]
```

`app.py`

```Python
from flask import Flask
from elasticapm.contrib.flask import ElasticAPM

app = Flask(__name__)

# 配置 APM Agent
app.config['ELASTIC_APM'] = {
    'SERVICE_NAME': 'my-flask-app',  # 替換為應用程式名稱
    'SECRET_TOKEN': 'YOUR_SECRET_TOKEN',  # 可選，安全性憑證
    'SERVER_URL': 'http://localhost:8200'  # Elastic APM Server 的 URL
}

# 初始化 APM
apm = ElasticAPM(app)

# 啟動 Flask 應用
if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=5000)
```

docker-compose

```yml
  apm-ui-flask:
    build:
      # Dockerfile 路徑
      context: .
      dockerfile: Dockerfile
    image: apm-ui-flask
    container_name: apm-ui-flask
    depends_on:
      - apm-server
    environment:
      - "ELASTIC_APM_SERVICE_NAME=apm-ui-flask"
      - "ELASTIC_APM_SERVER_URL=http://apm-server:8200"
      - "ELASTIC_APM_ENVIRONMENT=production"
      - "ELASTIC_APM_SECRET_TOKEN="
    volumes:
      - ./app:/app
    working_dir: /app
    command: bash -c 'python -u app.py'
```

`requirements.txt`

```
elastic-apm[flask]
```

```Dockerfile
FROM python:3.9.16-buster

WORKDIR /usr/src/app
COPY . .

RUN apt-get update; exit 0
RUN apt-get install software-properties-common -y
RUN apt-add-repository non-free -y
RUN apt-get update
RUN update-ca-certificates
RUN apt-get install vim net-tools iftop -y

RUN /usr/local/bin/python -m pip install --upgrade pip
RUN pip install -r requirements.txt
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
