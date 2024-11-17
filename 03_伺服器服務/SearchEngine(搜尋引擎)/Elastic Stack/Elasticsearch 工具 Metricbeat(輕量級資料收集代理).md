# Elasticsearch 工具 Metricbeat(輕量級資料收集代理)

```
Metricbeat 是 Elastic Stack 中的一個輕量級資料收集代理，用於收集和發送各種系統和服務的度量數據（metrics），例如 CPU 使用率、內存佔用、磁碟 I/O、網路流量等。它可以幫助管理者和開發者深入了解系統性能，及早發現潛在問題。

Metricbeat 的主要用途
收集系統層級的度量數據

Metricbeat 可以監控操作系統資源使用情況，如 CPU、記憶體、磁碟 I/O 和網絡流量等，適用於 Linux、Windows、macOS 等平台。
監控各種服務和應用程式

支援對不同服務（如 MySQL、Nginx、Redis、Docker、Kubernetes、Apache 等）的度量數據收集，無需進行複雜配置，並能夠輕鬆適配常用的服務。
向 Elastic Stack 發送度量數據

Metricbeat 通常將收集的度量數據發送到 Elasticsearch 中，並可以通過 Kibana 進行可視化展示。這樣可以輕鬆查看和分析系統和服務的健康狀況。
提供即時監控和告警

當 Metricbeat 與 Elasticsearch 和 Kibana 配合使用時，可以通過 Kibana 設置告警。若監控數據超出設定的閾值（例如 CPU 使用率過高），可以自動發送通知。
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

[Metricbeat quick start: installation and configuration](https://www.elastic.co/guide/en/beats/metricbeat/7.13/metricbeat-installation-configuration.html)

# 安裝

## docker-compose 部署

```yml
```

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

```bash
curl -L -O https://artifacts.elastic.co/downloads/beats/metricbeat/metricbeat-7.13.3-x86_64.rpm
sudo rpm -vi metricbeat-7.13.3-x86_64.rpm
```

## Homebrew (MacOS)

```bash
```

## 配置文檔

通常在 `/etc/metricbeat/metricbeat.yml`

```yml
output.elasticsearch:
  hosts: ["<es_url>"]
  username: "elastic"
  password: "<password>"
setup.kibana:
  host: "<kibana_url>"
```

通常在 `/etc/metricbeat/modules.d/system.yml`

啟用並配置 system 模組

```sh
metricbeat modules enable system
```

啟動 Metricbeat

setup 指令載入 Kibana 儀表板。如果儀表板已設置，請省略此指令。

```sh
metricbeat setup
service metricbeat start
```

### 基本範例

```
```
