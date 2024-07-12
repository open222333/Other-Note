# Poste.io(郵件伺服器管理工具)

```
Poste.io 是一個功能豐富的郵件伺服器管理工具，內置簡單的 Web 界面來管理郵件伺服器。
它支持多種語言，包括中文，並且集成了反垃圾郵件和防病毒功能。
```

## 目錄

- [Poste.io(郵件伺服器管理工具)](#posteio郵件伺服器管理工具)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [郵箱腳本功能](#郵箱腳本功能)
    - [測試相關](#測試相關)
- [安裝](#安裝)
  - [docker-compose 部署](#docker-compose-部署)
- [DNS 指向](#dns-指向)
- [腳本](#腳本)
  - [管理後台 過濾信件功能 SIEVE filter](#管理後台-過濾信件功能-sieve-filter)
  - [定期刪除腳本(伺服器)](#定期刪除腳本伺服器)
- [資訊](#資訊)
  - [端口 25](#端口-25)
  - [端口 465](#端口-465)
  - [端口 587 和 Submission Server](#端口-587-和-submission-server)
  - [SMTP(Simple Mail Transfer Protocol)](#smtpsimple-mail-transfer-protocol)
  - [STARTTLS](#starttls)

## 參考資料

[官方網站](https://poste.io/)

[Poste.io API](https://admin%40poste.io:admin@demo.poste.io/admin/api/doc)

[poste.io 文檔](https://poste.io/doc/)

[poste dockerhub](https://hub.docker.com/r/analogic/poste.io/)

[poste.io 文檔 - docker 範例](https://poste.io/open)

[poste.io 範例](https://poste.io/demo)

[poste.io 範例網站 - 瀏覽器儲存帳密](https://demo.poste.io/admin/login#admin@poste.io;admin)

[Frequently asked questions - 常見問題](https://poste.io/doc/faq)

### 郵箱腳本功能

[Sieve（郵件過濾語言）](http://en.wikipedia.org/wiki/Sieve_(mail_filtering_language))

[Sieve Tutorial](https://www.tty1.net/blog/2011/sieve-tutorial_en.html)

### 測試相關

[測試 MX 指向工具](https://mxtoolbox.com/SuperTool.aspx)

# 安裝

## docker-compose 部署

```yml
version: '3'
services:
  poste:
    image: analogic/poste.io
    container_name: poste
    ports:
      - "25:25"
      - "80:80"
      - "443:443"
      - "110:110"
      - "143:143"
      - "993:993"
      - "995:995"
    volumes:
      - ./data:/data
    restart: always
```

```
25，SMTP server for incoming emails
110, POP3 server (STARTTLS required)
143, IMAP server (STARTTLS required)
443，Administration and webmail HTTPS server
587, Submission server(STARTTLS SMTP server for clients)
993, IMAP server(implicit TLS)
995, POP3 server(implicit TLS)
4190，Sieve server(optional)
```

```
在瀏覽器中打開 http://<your-server-ip> 或 https://<your-server-ip>，將會看到 Poste.io 的初始設置界面。按照提示進行配置：

設置管理員帳戶
添加郵件域名
創建郵件帳戶
4. 配置 DNS 記錄
根據 Poste.io 的指示設置 DNS 記錄，包括 MX、SPF、DKIM 和 DMARC 記錄，以確保郵件的正常接收和發送。

5. 設置中文界面
完成初始設置並登錄管理界面後，可以在設置中選擇中文作為界面語言。

6. 高級配置
反垃圾郵件和防病毒：Poste.io 已集成這些功能，可以在管理界面中進行配置和調整。
備份和恢復：定期備份 ./data 目錄，以確保數據安全。
```

```
管理後台
https://admin.example.com/admin/box/

郵箱
https://admin.example.com/webmail/

api文檔
https://admin.example.com/admin/api/doc
```

# DNS 指向

```
$ORIGIN example.com
@     IN  A      10.11.12.13
mail  IN  A      10.11.12.13

; mail server for example.com
@     IN  MX  10 mail.example.com.

; Add SPF record
@     IN  TXT    "v=spf1 mx -all"
```

```
;; A Records
admin.example.com.	1	IN	A	xxx.xxx.xxx.xxx
mail.example.com.	1	IN	A	xxx.xxx.xxx.xxx

;; MX Records
example.com.	1	IN	MX	10 mail.example.com.
mail.example.com.	1	IN	MX	10 mail.example.com.
```

# 腳本

## 管理後台 過濾信件功能 SIEVE filter

路徑

`https://admin.example.com/admin/my/show`

```sieve
require ["fileinto", "vacation", "variables", "copy"];

# 垃圾郵件過濾規則
if header :contains "subject" "*****SPAM*****" {
    fileinto "Junk";
    stop;
}

# 自動回信規則
if header :matches "Subject" "*" {
    set "subjwas" "${1}";
}
vacation
  :days 1
  :addresses ["收件郵箱@mail.example.com"]
  :subject "Re: ${subjwas}"
  "自動轉寄內容";

# 郵件副本重定向規則
if true {
    redirect :copy "轉寄郵箱@mail.example.com";
}
```

## 定期刪除腳本(伺服器)

刪除超過30天的郵件

使用 find 命令來查找並刪除舊郵件

```sh
#!/bin/bash

# 設定郵件存放目錄，根據你的實際路徑調整
MAIL_DIR="/path/to/maildir"

# 刪除超過30天的郵件
find "$MAIL_DIR" -type f -mtime +30 -exec rm -f {} \;

echo "Old emails deleted."
```

設置腳本權限

```sh
chmod +x clean_old_emails.sh
```

設置定時任務

```sh
crontab -e
```

每天凌晨3點運行腳本

```
0 3 * * * /path/to/clean_old_emails.sh
```

手動運行腳本 測試

```sh
./clean_old_emails.sh
```

# 資訊

## 端口 25

```
主要用於伺服器之間的郵件中繼，不建議用於客戶端提交郵件，因為它缺乏安全性，容易被濫用來發送垃圾郵件。
```

## 端口 465

```
一開始被指定為 SMTPS（SMTP Secure），專門用於 SSL/TLS 加密的 SMTP 連接。
但後來被 IETF 廢除，STARTTLS 被推薦作為替代方案。
```

## 端口 587 和 Submission Server

```
用途：

端口 587 被指定為 Submission Port，用於電子郵件客戶端提交電子郵件給電子郵件伺服器，而不是伺服器之間的郵件傳遞。
這與 端口 25 的主要用途不同，後者通常用於電子郵件伺服器之間的郵件中繼。

安全性：

端口 587 支持 STARTTLS，這是一種機制，允許在建立純文本連接之後升級到加密的TLS（傳輸層安全）連接。
這樣可以保護電子郵件在互聯網上傳輸時免受窺探和篡改。

SMTP 認證：

使用 端口 587 時，通常要求客戶端進行 SMTP 認證。
這意味著電子郵件客戶端必須提供有效的用戶名和密碼才能發送電子郵件。
這有助於防止未經授權的用戶使用郵件伺服器發送垃圾郵件。
```

## SMTP(Simple Mail Transfer Protocol)

```
SMTP 是用於發送電子郵件的主要協議。
它定義了如何從發送者的電子郵件客戶端或伺服器發送電子郵件到收件人的電子郵件伺服器。
```
## STARTTLS

```
STARTTLS 是一種在現有的非加密連接上啟動加密的方法。
當客戶端和伺服器都支持 STARTTLS 時，客戶端可以發送 STARTTLS 命令來升級連接到加密的 TLS 連接。
這確保了數據在互聯網上傳輸時的安全性。
```
