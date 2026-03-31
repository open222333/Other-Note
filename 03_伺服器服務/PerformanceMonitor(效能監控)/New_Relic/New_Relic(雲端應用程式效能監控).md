# New Relic(雲端應用程式效能監控)

```
New Relic 是一套雲端 SaaS 應用程式效能監控（APM）平台，透過在應用程式或伺服器安裝 Agent，
自動收集效能指標並傳送至 New Relic 雲端，提供以下功能：

APM（應用程式效能監控）— 追蹤程式碼層級效能瓶頸、慢查詢、錯誤率
Infrastructure 監控 — 監控伺服器 CPU、記憶體、磁碟、網路資源
Log Management — 集中收集與搜尋應用程式日誌
Distributed Tracing — 追蹤跨服務請求鏈路（微服務架構常用）
Browser 監控 — 前端使用者體驗與頁面效能追蹤
Synthetic 監控 — 模擬使用者行為定期測試服務可用性
Alerts & Dashboards — 設定閾值告警，自訂監控儀表板
支援多語言 Agent：Python、PHP、Node.js、Java、Go、Ruby、.NET
```

## 目錄

- [New Relic(雲端應用程式效能監控)](#new-relic雲端應用程式效能監控)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Infrastructure Agent - Debian (Ubuntu)](#infrastructure-agent---debian-ubuntu)
  - [Infrastructure Agent - RedHat (CentOS)](#infrastructure-agent---redhat-centos)
  - [Python APM Agent](#python-apm-agent)
  - [PHP APM Agent - Debian (Ubuntu)](#php-apm-agent---debian-ubuntu)
  - [PHP APM Agent - RedHat (CentOS)](#php-apm-agent---redhat-centos)
  - [配置文檔](#配置文檔)
    - [Infrastructure Agent 基本範例](#infrastructure-agent-基本範例)
    - [Python APM 基本範例](#python-apm-基本範例)
    - [PHP APM 基本範例](#php-apm-基本範例)
- [指令](#指令)
  - [Infrastructure Agent 服務操作](#infrastructure-agent-服務操作)
  - [Python APM 操作](#python-apm-操作)
  - [PHP APM 操作](#php-apm-操作)
- [環境變數](#環境變數)

## 參考資料

[New Relic 官方網站](https://newrelic.com/)

[New Relic Docs 文檔](https://docs.newrelic.com/)

[New Relic 登入](https://one.newrelic.com/)

[New Relic GitHub](https://github.com/newrelic)

### Infrastructure Agent 相關

[Infrastructure Agent 官方文檔](https://docs.newrelic.com/docs/infrastructure/install-infrastructure-agent/get-started/install-infrastructure-agent/)

[Infrastructure Agent - Linux 安裝](https://docs.newrelic.com/docs/infrastructure/install-infrastructure-agent/linux-installation/install-infrastructure-monitoring-agent-linux/)

### Python APM 相關

[Python APM Agent 官方文檔](https://docs.newrelic.com/docs/apm/agents/python-agent/getting-started/introduction-new-relic-python/)

[Python APM Agent - PyPI](https://pypi.org/project/newrelic/)

### PHP APM 相關

[PHP APM Agent 官方文檔](https://docs.newrelic.com/docs/apm/agents/php-agent/getting-started/introduction-new-relic-php/)

### On-host Integration 相關

[On-host Integrations 官方文檔](https://docs.newrelic.com/docs/infrastructure/host-integrations/host-integrations-list/)

[MongoDB Integration 官方文檔](https://docs.newrelic.com/docs/infrastructure/host-integrations/host-integrations-list/mongodb/mongodb-monitoring-integration/)

### 相關筆記

[New_Relic_Integration_MongoDB(MongoDB整合監控).md](New_Relic_Integration_MongoDB(MongoDB整合監控).md)

# 安裝

## Guided Install（互動式安裝）

```bash
# 下載並執行 New Relic CLI
curl -Ls https://download.newrelic.com/install/newrelic-cli/scripts/install.sh | bash

# 執行 guided install
sudo NEW_RELIC_API_KEY=<API_KEY> \
     NEW_RELIC_ACCOUNT_ID=<ACCOUNT_ID> \
     /usr/local/bin/newrelic install
```

### Guided Install 結果

```
--------------------
What's next?
⊘  MongoDB Integration  (unsupported)  ← 需手動安裝，參考下方連結
✔  Infrastructure Agent  (installed)
✔  Logs Integration      (installed)

Installation was successful overall, however, one or more installations could not be completed.
--------------------
```

MongoDB Integration 在 guided install 中標示為 `unsupported`，需手動安裝。

參考：[New_Relic_Integration_MongoDB(MongoDB整合監控).md](New_Relic_Integration_MongoDB(MongoDB整合監控).md)

## Infrastructure Agent - Debian (Ubuntu)

```bash
# 加入 New Relic GPG key 與 repository
curl -fsSL https://download.newrelic.com/infrastructure_agent/gpg/newrelic-infra.gpg | \
    sudo gpg --dearmor -o /etc/apt/trusted.gpg.d/newrelic-infra.gpg

echo "deb https://download.newrelic.com/infrastructure_agent/linux/apt focal main" | \
    sudo tee /etc/apt/sources.list.d/newrelic-infra.list

sudo apt-get update
sudo apt-get install newrelic-infra -y
```

## Infrastructure Agent - RedHat (CentOS)

```bash
# 加入 New Relic repository
sudo curl -o /etc/yum.repos.d/newrelic-infra.repo \
    https://download.newrelic.com/infrastructure_agent/linux/yum/el/7/x86_64/newrelic-infra.repo

sudo yum -q makecache -y --disablerepo='*' --enablerepo='newrelic-infra'
sudo yum install newrelic-infra -y
```

## Python APM Agent

```bash
# 安裝 Python APM Agent
pip install newrelic

# 產生設定檔
newrelic-admin generate-config YOUR_LICENSE_KEY newrelic.ini
```

## PHP APM Agent - Debian (Ubuntu)

```bash
# 使用官方腳本安裝
curl -L https://download.newrelic.com/install/newrelic-cli/scripts/install.sh | bash
sudo NEW_RELIC_API_KEY=YOUR_API_KEY \
     NEW_RELIC_ACCOUNT_ID=YOUR_ACCOUNT_ID \
     /usr/local/bin/newrelic install -n php-agent-installer
```

## PHP APM Agent - RedHat (CentOS)

```bash
# 使用官方腳本安裝
curl -L https://download.newrelic.com/install/newrelic-cli/scripts/install.sh | bash
sudo NEW_RELIC_API_KEY=YOUR_API_KEY \
     NEW_RELIC_ACCOUNT_ID=YOUR_ACCOUNT_ID \
     /usr/local/bin/newrelic install -n php-agent-installer
```

## 配置文檔

### Infrastructure Agent 基本範例

通常在 `/etc/newrelic-infra.yml`

```yml
# New Relic License Key（必填）
license_key: YOUR_LICENSE_KEY

# 自訂顯示名稱（選填，預設使用主機名）
display_name: my-server-name

# Log 層級（verbose、debug、info、warning、error）
log_level: info

# 自訂標籤（選填，用於篩選主機）
custom_attributes:
  environment: production
  team: backend
```

### Python APM 基本範例

通常在 `newrelic.ini`（由 `newrelic-admin generate-config` 產生）

```ini
[newrelic]
; New Relic License Key（必填）
license_key = YOUR_LICENSE_KEY

; 應用程式名稱（顯示於 New Relic 後台）
app_name = My Python Application

; 監控開關
monitor_mode = true

; Log 層級（critical、error、warning、info、debug）
log_level = info

; Log 輸出路徑
log_file = /var/log/newrelic/python-agent.log
```

### PHP APM 基本範例

通常在 `/etc/php/newrelic.ini` 或 `/etc/php.d/newrelic.ini`

```ini
; New Relic License Key（必填）
newrelic.license = "YOUR_LICENSE_KEY"

; 應用程式名稱
newrelic.appname = "My PHP Application"

; 監控開關
newrelic.enabled = true

; 分散式追蹤開關
newrelic.distributed_tracing_enabled = true
```

# 指令

## Infrastructure Agent 服務操作

```bash
# 啟動服務
systemctl start newrelic-infra

# 查詢啟動狀態
systemctl status newrelic-infra

# 重新啟動
systemctl restart newrelic-infra

# 停止服務
systemctl stop newrelic-infra

# 開啟開機自動啟動
systemctl enable newrelic-infra

# 關閉開機自動啟動
systemctl disable newrelic-infra
```

## Python APM 操作

```bash
# 產生設定檔
newrelic-admin generate-config YOUR_LICENSE_KEY newrelic.ini

# 驗證設定檔
newrelic-admin validate-config newrelic.ini

# 以 APM Agent 啟動 Python 程式
NEW_RELIC_CONFIG_FILE=newrelic.ini newrelic-admin run-program python app.py

# 以 APM Agent 啟動 gunicorn
NEW_RELIC_CONFIG_FILE=newrelic.ini newrelic-admin run-program gunicorn app:application

# 以 APM Agent 啟動 uwsgi
NEW_RELIC_CONFIG_FILE=newrelic.ini newrelic-admin run-program uwsgi uwsgi.ini
```

## PHP APM 操作

```bash
# 安裝後重新啟動 PHP-FPM
systemctl restart php-fpm

# 或重新啟動 Apache
systemctl restart apache2

# 檢查 PHP 是否載入 New Relic 模組
php -m | grep newrelic

# 查看 New Relic PHP daemon 狀態
systemctl status newrelic-daemon
```

# 環境變數

```bash
# Infrastructure Agent
NEW_RELIC_LICENSE_KEY=YOUR_LICENSE_KEY
NEW_RELIC_DISPLAY_NAME=my-server-name

# Python APM Agent
NEW_RELIC_LICENSE_KEY=YOUR_LICENSE_KEY
NEW_RELIC_APP_NAME=My Python Application
NEW_RELIC_CONFIG_FILE=newrelic.ini
NEW_RELIC_LOG=stdout
NEW_RELIC_LOG_LEVEL=info

# 關閉監控（用於開發環境）
NEW_RELIC_MONITOR_MODE=false
```