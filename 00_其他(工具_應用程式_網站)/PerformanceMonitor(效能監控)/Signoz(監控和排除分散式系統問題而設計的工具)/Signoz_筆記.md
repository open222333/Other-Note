# Signoz 筆記

```
Signoz 是一款針對監控和排除分散式系統問題而設計的工具。
它提供了可觀察性功能，有助於了解複雜系統的性能和行為。
```

## 目錄

- [Signoz 筆記](#signoz-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [OpenTelemetry 相關](#opentelemetry-相關)
    - [Docker 相關](#docker-相關)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
    - [Docker](#docker)

## 參考資料

[signoz 官方網站](https://signoz.io/)

[signoz github](https://github.com/signoz/signoz)

### OpenTelemetry 相關

[opentelemetry 官方網站](https://opentelemetry.io/)

[opentelemetry Configuration](https://opentelemetry.io/docs/collector/configuration/)

### Docker 相關

[signoz github docker基本文檔](https://github.com/SigNoz/signoz/blob/develop/deploy/docker/clickhouse-setup/docker-compose.yaml)

# 安裝

在使用 Signoz 容器時，`可能`會用到以下端口：

API 服務端口（例如 80 或者 8080）：用於訪問 Signoz 的 API 接口，進行數據操作和查詢。
數據庫服務端口（例如 9000 或者 5432）：用於訪問 Signoz 的數據庫服務，存儲和查詢監控數據。
追踪服務端口（例如 9411 或者 16686）：用於訪問 Signoz 的追踪服務，查看系統的追踪信息。
Web 界面端口（例如 3000 或者 8081）：用於訪問 Signoz 的 Web 界面，查看監控面板和操作界面。

## Debian (Ubuntu)

```bash
git clone -b main https://github.com/SigNoz/signoz.git
cd signoz/deploy/
./install.sh
```

## RedHat (CentOS)

```bash
git clone -b main https://github.com/SigNoz/signoz.git
cd signoz/deploy/
./install.sh
```

### Docker

```bash
git clone -b main https://github.com/SigNoz/signoz.git
cd signoz/deploy/
docker compose -f docker/clickhouse-setup/docker-compose.yaml up -d
```
