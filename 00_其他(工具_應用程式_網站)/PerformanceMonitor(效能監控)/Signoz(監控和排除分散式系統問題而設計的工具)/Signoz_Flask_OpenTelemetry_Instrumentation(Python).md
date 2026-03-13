# Signoz Flask OpenTelemetry Instrumentation(Python)

```
Signoz 是一款針對監控和排除分散式系統問題而設計的工具。
它提供了可觀察性功能，有助於了解複雜系統的性能和行為。
```

## 目錄

- [Signoz Flask OpenTelemetry Instrumentation(Python)](#signoz-flask-opentelemetry-instrumentationpython)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [用法](#用法)
  - [指令應用](#指令應用)
  - [程式碼內應用](#程式碼內應用)
- [範例](#範例)
  - [Python 3.6.15 Dockerfile 範例](#python-3615-dockerfile-範例)

## 參考資料

[signoz 官方網站](https://signoz.io/)

[signoz github](https://github.com/signoz/signoz)

[signoz Flask OpenTelemetry Instrumentation](https://signoz.io/docs/instrumentation/flask/)

[OpenTelemetry-Python API Reference](https://opentelemetry-python.readthedocs.io/en/latest/examples/fork-process-model/README.html)

# 安裝

安裝 OpenTelemetry 依賴項

```bash
pip install opentelemetry-distro==0.43b0
pip install opentelemetry-exporter-otlp==1.22.0
```

```bash
pip install opentelemetry-instrument==1.2.0
```

# 用法

## 指令應用

自動安裝和配置 OpenTelemetry 相關的庫和組件，包括 OpenTelemetry API、SDK、Exporter 等

```bash
opentelemetry-bootstrap --action=install
```

設置環境變數

使用SignOz雲端服務 需付費取得 SIGNOZ_INGESTION_KEY

```ini
; 設置 OpenTelemetry 的資源屬性，其中 <service_name> 是應用服務的名稱。
; 這個資源屬性將被用來標識和分類數據。
OTEL_RESOURCE_ATTRIBUTES=service.name=<servine_name>
; 設置了 OpenTelemetry 的 OTLP Exporter 的端點地址。
; 請將 {region} 替換為實際的區域名稱。
; 這個端點地址用於將 OpenTelemetry 數據傳送到 Signoz 服務中。
OTEL_EXPORTER_OTLP_ENDPOINT="https://ingest.{region}.signoz.cloud:443"
; 設置 OTLP Exporter 的 HTTP 標頭，其中 SIGNOZ_INGESTION_KEY 是 Signoz 數據傳送密鑰。
; 這個密鑰用於授權數據的傳送。
OTEL_EXPORTER_OTLP_HEADERS="signoz-access-token=SIGNOZ_INGESTION_KEY"
; 設置了 OTLP Exporter 使用的協議，這裡設置為 gRPC 協議。
OTEL_EXPORTER_OTLP_PROTOCOL=grpc
```

自建服務後台

gRPC 導出器

http://localhost:4317

HTTP 導出器

http://localhost:4318

```ini
OTEL_RESOURCE_ATTRIBUTES=service.name=<service_name> \
OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4317" \
OTEL_EXPORTER_OTLP_PROTOCOL=grpc opentelemetry-instrument <your run command>
```

啟用 OpenTelemetry 監控和追踪功能，並指定要運行的命令 <your_run_command>。

```bash
opentelemetry-instrument <your_run_command>
```

## 程式碼內應用

配置 Signoz 資訊：

在 Flask 應用中，配置 Signoz 的資訊，包括 Signoz 網址和服務名稱。

可以在應用的配置文件中設置這些信息，或者直接在代碼中設置：

```Python
from flask import Flask
from signoz import Signoz

app = Flask(__name__)
app.config['SIGNOZ_ENDPOINT'] = 'https://your-signoz-instance-url.com/v1/traces'
app.config['SIGNOZ_SERVICE_NAME'] = 'your-flask-app'

signoz = Signoz(app)

# 啟用 OpenTelemetry 儀表
# 使用 Signoz 提供的 signoz 物件來啟用 OpenTelemetry 儀表。
# 這將自動將 OpenTelemetry 相關中間件添加到 Flask 應用中，開始收集和傳遞數據。
signoz.instrument(app)
```
# 範例

## Python 3.6.15 Dockerfile 範例

```Dockerfile
FROM python:3.6.15-buster
WORKDIR /usr/src/app
COPY . .

RUN apt-get update; exit 0
RUN apt-get install vim net-tools iftop -y

RUN pip install --upgrade pip
RUN pip install opentelemetry-distro==0.33b0
RUN pip install opentelemetry-exporter-otlp==1.12.0
RUN pip install -r requirements.txt
RUN opentelemetry-bootstrap --action=install
```

```env
# SignOz
# 自建後台 Send Traces to Self-Hosted SigNoz
OTEL_RESOURCE_ATTRIBUTES="service.name=<server_name>"
OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4317"
OTEL_EXPORTER_OTLP_PROTOCOL=grpc
```

```yml
version: '3'
services:
  api:
    build:
      context: .
      dockerfile: Dockerfile
    image: av-crawler
    container_name: api
    hostname: api-container
    env_file: sample.env
    volumes:
      - .:/usr/src/app
    ports:
      - 81:81
    command: bash -c 'opentelemetry-instrument gunicorn -b 0.0.0.0:81 -c config/gunicorn.py "app:create_application()" & celery worker -A app.celery -l info -E -P gevent --purge -n worker%i@%h'
```
