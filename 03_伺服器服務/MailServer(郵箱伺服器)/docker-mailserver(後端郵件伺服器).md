# docker-mailserver(後端郵件伺服器)

```
Docker Mailserver 是一個易於部署且可配置的完整郵件伺服器套件。

Docker Mailserver 本身是一個後端郵件伺服器，主要通過配置文件和命令行進行管理，並沒有特定的圖形用戶界面 (GUI)，因此也沒有區分語言介面。
```

## 目錄

- [docker-mailserver(後端郵件伺服器)](#docker-mailserver後端郵件伺服器)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [](#)
- [安裝](#安裝)
  - [RedHat (CentOS)](#redhat-centos)
- [指令](#指令)
  - [docker-compose 啟動](#docker-compose-啟動)
  - [測試](#測試)

## 參考資料

[docker-mailserver Github](https://github.com/docker-mailserver/docker-mailserver)

[docker-mailserver 文檔](https://docker-mailserver.github.io/docker-mailserver/latest/)

[docker-mailserver 文檔 - 環境變數](https://docker-mailserver.github.io/docker-mailserver/latest/config/environment/)

[dockerhub](https://hub.docker.com/r/mailserver/docker-mailserver/)

[docker-mailserver 文檔 - 基本安裝教學](https://docker-mailserver.github.io/docker-mailserver/latest/examples/tutorials/basic-installation/)

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
services:
  mailserver:
    image: ghcr.io/docker-mailserver/docker-mailserver:latest
    container_name: mailserver
    # Provide the FQDN of your mail server here (Your DNS MX record should point to this value)
    hostname: mail.example.com
    ports:
      - "25:25"
      - "465:465"
      - "587:587"
      - "993:993"
    volumes:
      - ./docker-data/dms/mail-data/:/var/mail/
      - ./docker-data/dms/mail-state/:/var/mail-state/
      - ./docker-data/dms/mail-logs/:/var/log/mail/
      - ./docker-data/dms/config/:/tmp/docker-mailserver/
      - /etc/localtime:/etc/localtime:ro
    environment:
      - ENABLE_RSPAMD=1
      - ENABLE_CLAMAV=1
      - ENABLE_FAIL2BAN=1
    cap_add:
      - NET_ADMIN # For Fail2Ban to work
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

```
$ORIGIN example.com
@     IN  A      10.11.12.13
mail  IN  A      10.11.12.13

; mail server for example.com
@     IN  MX  10 mail.example.com.

; Add SPF record
@     IN  TXT    "v=spf1 mx -all"
```

防火牆狀態（基於 Ubuntu）

```bash
ufw status

# 允許 SMTP 端口
ufw allow 25
ufw allow 587
ufw allow 993
```

# 指令

命令setup將在容器內運行。

產生 DKIM 金鑰

```bash
setup config dkim
```

/tmp/docker-mailserver/opendkim/keys/example.com/mail.txt

別名可確保發送至這些帳戶的任何電子郵件都會轉發到第三方電子郵件地址 ( external-account@gmail.com)，並在其中檢索它們（例如：透過第三方網路或行動應用程式），而不是直接docker-mailserer使用POP3 / IMAP 連接。

```bash
setup email add admin@example.com passwd123
setup email add info@example.com passwd123
setup alias add admin@example.com external-account@gmail.com
setup alias add info@example.com external-account@gmail.com
setup email list
setup alias list
```

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

## 測試

使用 telnet 連接到 Docker Mailserver 的 SMTP 端口

```bash
telnet mail.example.com 25
```

成功連接，會看到類似於以下的響應

```
Trying 192.168.1.1...
Connected to mail.example.com.
Escape character is '^]'.
220 mail.example.com ESMTP Postfix
```

在 telnet 會話中，輸入以下命令來發送測試郵件

```bash
HELO mail.example.com
MAIL FROM:<test@example.com>
RCPT TO:<user@example.com>
DATA
Subject: Test Email

This is a test email.
.
QUIT
```

避免 SMTP Command Pipelining 問題，逐行發送 SMTP 命令：

```
HELO mail.lovecutesealbaby.com
```

收到回應：

```
250 mail.lovecutesealbaby.com
```

發送 MAIL FROM 命令：

```
MAIL FROM:<test@lovecutesealbaby.com>
```

收到回應：

```
250 2.1.0 Ok
```

發送 RCPT TO 命令：

```
RCPT TO:<user@lovecutesealbaby.com>
```

收到回應：

```
250 2.1.5 Ok
```

發送 DATA 命令：

```
DATA
```

收到回應：

```
354 End data with <CR><LF>.<CR><LF>
```

輸入郵件內容，並以單獨一行的 . 結束：

```
Subject: Test Email

This is a test email.
.
```

收到回應：

```
250 2.0.0 Ok: queued as 12345
```

發送 QUIT 命令：

```
QUIT
```

查看伺服器日誌以查找可能的錯誤或警告

```bash
docker logs mailserver
```

```
stored mail into mailbox 'INBOX'
```
