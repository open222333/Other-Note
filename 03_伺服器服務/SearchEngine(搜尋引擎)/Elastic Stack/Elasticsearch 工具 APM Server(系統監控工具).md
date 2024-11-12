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

[]()

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
    image: apm-server:${STACK_VERSION}
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
  # 如果您想把 APM Server 自己的指標數據送到其他 APM Server，這裡可以設置 API Endpoint。
  # hosts:
```

# 指令

```sh
service apm-server start
```
