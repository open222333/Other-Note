# FileBrowser(基於 Web 的檔案管理工具)

```
FileBrowser 是一個基於 Web 的檔案管理工具，讓使用者能在網頁介面中輕鬆地上傳、下載、刪除、修改和分享檔案。
它支援多使用者和高度自訂功能，適合用於伺服器管理或本地文件管理。

功能特點
跨平台支援：支援 Linux、Windows 和 macOS。
多使用者：支援不同帳號和權限。
易於使用：提供簡潔的 Web 界面，無需額外軟體。
支援 API：可用於自動化操作。
安全性：支援 HTTPS 和 JWT 身份驗證。
自訂功能：可設定首頁、主題、語言等。
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

[File Browser 官方網站](https://filebrowser.org/)

[GitHub](https://github.com/filebrowser/filebrowser)

[FileBrowser Releases](https://github.com/filebrowser/filebrowser/releases)

[官方文件](https://filebrowser.org/features)

### 心得相關

[帶你認識＆安裝「File Browser」這個超棒的雲端文件管理工具](https://medium.com/dean-lin/file-browser-%E8%B6%85%E6%A3%92%E7%9A%84%E9%9B%B2%E7%AB%AF%E6%96%87%E4%BB%B6%E7%AE%A1%E7%90%86%E5%B7%A5%E5%85%B7-871d8605dc41)

[Filebrowser 一个交流使用Filebrowser的网站](https://www.filebrowser.cn/)

# 安裝

## docker-compose 部署

```yml
version: '3.8'

services:
  filebrowser:
    image: filebrowser/filebrowser:latest
    container_name: filebrowser
    ports:
      - "8080:80"  # 將 8080 映射到主機
    volumes:
      - ./data:/srv                 # 存放管理的檔案
      - ./filebrowser.db:/database.db  # 配置檔案
    environment:
      FB_BASEURL: ""  # 可設置基本 URL（默認為空）
    restart: unless-stopped
```

## Debian (Ubuntu)

```bash
apt update && apt upgrade -y
apt install filebrowser
```

確認最新版本後，使用 wget 下載對應的二進位檔案

```sh
wget https://github.com/filebrowser/filebrowser/releases/download/v<版本號>/filebrowser-linux-amd64.tar.gz
```

解壓縮檔案

```sh
tar -xvzf filebrowser-linux-amd64.tar.gz
```

將執行檔移至系統的可執行路徑

```sh
mv filebrowser /usr/local/bin/
```

驗證 FileBrowser 是否安裝成功

```sh
filebrowser --version
```

### 設定 FileBrowser

創建 FileBrowser 的資料目錄

```sh
mkdir -p ~/filebrowser
```

初始化配置

```sh
filebrowser config init
```

（可選）設定伺服器運行端口和路徑

```sh
filebrowser config set --port 8080 --address 0.0.0.0
```

## 配置文檔

通常在 ``

### 基本範例

```
```

# 指令

## 配置 FileBrowser 為 Systemd 服務

建立一個 Systemd 服務檔案

```sh
nano /etc/systemd/system/filebrowser.service
```

```ini
[Unit]
Description=FileBrowser Service
After=network.target

[Service]
Type=simple
ExecStart=/usr/local/bin/filebrowser --config /home/<your-username>/filebrowser/filebrowser.db
Restart=on-failure
User=<your-username>
Group=<your-username>

[Install]
WantedBy=multi-user.target
```

重新載入 Systemd 配置

```sh
systemctl daemon-reload
```

```sh
systemctl start filebrowser
systemctl enable filebrowser
```

## 更改資料目錄

```sh
filebrowser config set --scope /path/to/your/directory
```

## 設定 HTTPS 支援

準備 SSL 憑證和金鑰，然後配置

```sh
filebrowser config set --cert /path/to/cert.pem --key /path/to/key.pem
```
