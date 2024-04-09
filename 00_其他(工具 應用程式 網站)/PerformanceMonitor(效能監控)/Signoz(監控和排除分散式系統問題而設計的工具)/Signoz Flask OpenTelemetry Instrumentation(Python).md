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

## 參考資料

[signoz 官方網站](https://signoz.io/)

[signoz github](https://github.com/signoz/signoz)

[signoz flask](https://signoz.io/docs/instrumentation/flask/)

# 安裝

安裝 OpenTelemetry 依賴項

```bash
pip install opentelemetry-distro==0.43b0
pip install opentelemetry-exporter-otlp==1.22.0
```

# 用法

## 指令應用

自動安裝和配置 OpenTelemetry 相關的庫和組件，包括 OpenTelemetry API、SDK、Exporter 等

```bash
opentelemetry-bootstrap --action=install
```

設置環境變數

```ini
OTEL_RESOURCE_ATTRIBUTES=service.name=<servine_name>
OTEL_EXPORTER_OTLP_ENDPOINT="https://ingest.{region}.signoz.cloud:443"
OTEL_EXPORTER_OTLP_HEADERS="signoz-access-token=SIGNOZ_INGESTION_KEY"
OTEL_EXPORTER_OTLP_PROTOCOL=grpc
```

```bash
opentelemetry-instrument <your_run_command>
```

OTEL_RESOURCE_ATTRIBUTES=service.name=<service_name>：

這個環境變數用來設置 OpenTelemetry 的資源屬性，其中 <service_name> 是你的應用服務的名稱。這個資源屬性將被用來標識和分類你的數據。

OTEL_EXPORTER_OTLP_ENDPOINT="https://ingest.{region}.signoz.cloud:443"：

這個環境變數設置了 OpenTelemetry 的 OTLP Exporter 的端點地址。請將 {region} 替換為實際的區域名稱。這個端點地址用於將 OpenTelemetry 數據傳送到 Signoz 服務中。

OTEL_EXPORTER_OTLP_HEADERS="signoz-access-token=SIGNOZ_INGESTION_KEY"：

這個環境變數設置了 OTLP Exporter 的 HTTP 標頭，其中 SIGNOZ_INGESTION_KEY 是你的 Signoz 數據傳送密鑰。這個密鑰用於授權數據的傳送。

OTEL_EXPORTER_OTLP_PROTOCOL=grpc：

這個環境變數設置了 OTLP Exporter 使用的協議，這裡設置為 gRPC 協議。

opentelemetry-instrument <your_run_command>：

最後，使用 opentelemetry-instrument 命令來啟用 OpenTelemetry 監控和追踪功能，並指定你要運行的命令 <your_run_command>。

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


