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
- [腳本](#腳本)
  - [管理後台 過濾信件功能 SIEVE filter](#管理後台-過濾信件功能-sieve-filter)
  - [定期刪除腳本(伺服器)](#定期刪除腳本伺服器)

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
https://example.com/admin/box/

郵箱
https://example.com/webmail/

api文檔
https://example.com/admin/api/doc
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
