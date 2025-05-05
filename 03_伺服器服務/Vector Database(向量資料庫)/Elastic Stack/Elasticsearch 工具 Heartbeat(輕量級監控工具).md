# Elasticsearch 工具 Heartbeat(輕量級監控工具)

```
Elasticsearch Heartbeat 是 Elastic Stack 內的一個輕量級監控工具，用於檢查和監控服務、網站以及網絡端點的可用性。通過 Heartbeat，您可以輕鬆地監控網絡端點是否處於上線（up）或下線（down）狀態，並在 Elasticsearch 中記錄這些數據，便於通過 Kibana 進行分析和可視化。

Heartbeat 的主要特點
Ping 檢查：可以執行 ICMP（類似 ping）、TCP、HTTP 等協議的監控。
靈活的間隔設置：可以設置不同的檢查間隔，精確控制檢測頻率。
狀態檢測：每次檢測會返回「up」或「down」狀態，方便檢查端點的可用性。
簡單配置：可以通過 YAML 文件配置要監控的端點、檢測類型和間隔。
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

[Heartbeat quick start: installation and configuration](https://www.elastic.co/guide/en/beats/heartbeat/7.13/heartbeat-installation-configuration.html)

# 安裝

## docker-compose 部署

```yml
```

## Debian (Ubuntu)

```bash
```

## RedHat (CentOS)

```bash
curl -L -O https://artifacts.elastic.co/downloads/beats/heartbeat/heartbeat-${version}-x86_64.rpm
sudo rpm -vi heartbeat-${version}-x86_64.rpm
```

7.13.3 範例

```bash
curl -L -O https://artifacts.elastic.co/downloads/beats/heartbeat/heartbeat-7.13.3-x86_64.rpm
sudo rpm -vi heartbeat-7.13.3-x86_64.rpm
```

## Homebrew (MacOS)

```bash
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 用法

配置 Heartbeat

在 Heartbeat 的 heartbeat.yml 文件中可以配置不同的監控檢查

例如 HTTP、ICMP、TCP 等檢測

```yml
heartbeat.monitors:
- type: http
  id: my-service
  name: My Service
  urls: ["https://example.com"]
  schedule: '@every 10s'  # 每 10 秒檢查一次
  check.response.status: 200  # 預期的 HTTP 狀態碼

- type: tcp
  id: my-tcp-check
  name: My TCP Service
  hosts: ["localhost:9000"]
  schedule: '@every 5s'

- type: icmp
  id: my-ping
  name: Ping My Server
  hosts: ["192.168.1.1"]
  schedule: '@every 30s'
```

在 heartbeat.yml 文件中，配置 Elasticsearch 作為輸出端

```yml
output.elasticsearch:
  hosts: ["http://localhost:9200"]
```

# 指令

啟動 Heartbeat

```sh
./heartbeat -e
```
