# Docker Mailserver(後端郵件伺服器)

```
Docker Mailserver 是一個易於部署且可配置的完整郵件伺服器套件。

Docker Mailserver 本身是一個後端郵件伺服器，主要通過配置文件和命令行進行管理，並沒有特定的圖形用戶界面 (GUI)，因此也沒有區分語言介面。
```

## 目錄

- [Docker Mailserver(後端郵件伺服器)](#docker-mailserver後端郵件伺服器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [](#)
- [安裝](#安裝)
  - [RedHat (CentOS)](#redhat-centos)
- [指令](#指令)
  - [docker-compose 啟動](#docker-compose-啟動)

## 參考資料

[docker-mailserver Github](https://github.com/docker-mailserver/docker-mailserver)

[docker-mailserver 文檔](https://docker-mailserver.github.io/docker-mailserver/latest/)

###

[MXToolbox - 檢查 MX 記錄](https://mxtoolbox.com/)

MXToolbox 是一個在線工具，提供多種工具來檢查和診斷 DNS 和郵件伺服器配置。

MX 記錄:

    確保列出的 MX 記錄包含郵件伺服器的正確主機名（例如 mail.example.com）。
    檢查優先級是否正確（通常設置為 10 或根據您的需要）。

A 記錄:

    確保 MX 記錄指向的主機名（例如 mail.example.com）有對應的 A 記錄，且該記錄指向您的郵件伺服器的正確 IP 地址。
1. 進一步檢查
MXToolbox 提供多種工具來進一步檢查和診斷您的郵件伺服器配置：

SMTP Test:

    測試 SMTP 伺服器的連接性和配置。
    在主頁的下拉菜單中選擇 "SMTP Test"，然後輸入郵件伺服器的主機名（例如 mail.example.com）。
    Blacklist Check:

    檢查郵件伺服器是否在任何黑名單上。
    在主頁的下拉菜單中選擇 "Blacklist Check"，然後輸入郵件伺服器的 IP 地址。

DNS Check:

    檢查 DNS 記錄的完整性和正確性。
    在主頁的下拉菜單中選擇 "DNS Check"，然後輸入域名。

以下是一個 MX 記錄查詢的範例輸出：

```
Priority    Hostname                 IP Address
10          mail.example.com         192.0.2.1
```

Priority: MX 記錄的優先級。數字越小優先級越高。

Hostname: 郵件伺服器的主機名。

IP Address: 主機名解析後的 IP 地址。

# 安裝

## RedHat (CentOS)

確保已安裝 Docker 和 Docker Compose

下載 Docker Mailserver

```bash
git clone https://github.com/docker-mailserver/docker-mailserver.git
cd docker-mailserver
```

配置 env

```bash
cp .env.sample .env
```

創建配置目錄

```bash
mkdir -p config/{ssl,postfix,opendkim,opendmarc,postscreen}
```

docker-compose.yml

```yml
version: '3.8'

services:
  mailserver:
    image: docker.io/mailserver/docker-mailserver:latest
    hostname: mail
    domainname: example.com
    container_name: mailserver
    ports:
      - "25:25"
      - "143:143"
      - "587:587"
      - "993:993"
    volumes:
      - ./config:/tmp/docker-mailserver
    env_file: .env
    cap_add:
      - NET_ADMIN
      - SYS_PTRACE
    restart: always
```

設置 DNS 記錄

```
A 記錄
主機名：mail
值：郵件伺服器的 IP 地址

MX 記錄
主機名：域名（例如 example.com）
值：mail.example.com
優先級：10（或根據需要設置的優先級）
```

# 指令

## docker-compose 啟動

```bash
docker-compose up -d mailserver
```

創建郵件用戶

```bash
docker exec -it mailserver setup email add user@example.com
```

設置密碼

```bash
docker exec -it mailserver setup email passwd user@example.com
```