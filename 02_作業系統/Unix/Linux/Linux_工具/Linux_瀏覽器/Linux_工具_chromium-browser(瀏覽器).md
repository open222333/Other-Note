# Linux 工具 chromium-browser(瀏覽器)

```
```

## 目錄

- [Linux 工具 chromium-browser($num)()](#linux-工具-chromium-browser$num)
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

[chromium-browser($num) — Linux man page](https://linux.die.net/man/$num/chromium-browser)

[chromium-browser($num) — Linux manual page](https://man7.org/linux/man-pages/man$num/chromium-browser.$num.html)

[ChromeDriver 官方下載頁面](https://developer.chrome.com/docs/chromedriver/downloads?hl=zh-tw)

# 安裝

## Debian (Ubuntu)

```bash
# 安裝 Chrome 無頭瀏覽器
apt-get update
apt-get install -y chromium-browser
# 安裝 ChromeDriver
apt-get install -y chromium-chromedriver
```

## RedHat (CentOS)

```bash
# 安裝 EPEL 及相關的軟件庫
yum install epel-release -y
# 安裝 Chromium
yum install chromium -y
```

檢查安裝的 Chromium 版本

```bash
chromium-browser --version
```

假設輸出是 Chromium 110.0.5481.77，需要下載與此版本對應的 ChromeDriver。

```bash
wget https://chromedriver.storage.googleapis.com/110.0.5481.77/chromedriver_linux64.zip
```

解壓 ChromeDriver 並移動到 /usr/local/bin/

```bash
unzip chromedriver_linux64.zip
mv chromedriver /usr/local/bin/
chmod +x /usr/local/bin/chromedriver
```

確認 ChromeDriver 安裝成功

```bash
chromedriver --version
```

## 配置文檔

通常在 `/etc/systemd/system/chromium-browser.service`

### 基本範例

```ini
[Unit]
Description=Chromium Browser in Headless Mode
After=network.target

[Service]
Type=simple
ExecStart=/usr/bin/chromium-browser --headless --no-sandbox --disable-gpu --remote-debugging-port=9222
Restart=on-failure
User=your_username

[Install]
WantedBy=multi-user.target
```

重新加載 systemd

```bash
systemctl daemon-reload
```

# 指令

```bash
# 使用命令行啟動 Chromium（無頭瀏覽器模式）
chromium-browser --headless --no-sandbox --disable-gpu --remote-debugging-port=9222
```

## 服務操作

```bash
# 啟動服務
systemctl start chromium-browser

# 查詢啟動狀態
systemctl status chromium-browser

# 重新啟動
systemctl restart chromium-browser

# 停止服務
systemctl stop chromium-browser

# 開啟開機自動啟動
systemctl enable chromium-browser

# 關閉開機自動啟動
systemctl disable chromium-browser
```
