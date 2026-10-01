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
- [新增網域（多網域部署，共用現有主機）](#新增網域多網域部署共用現有主機)
  - [架構說明](#架構說明)
  - [建立網域與 DKIM](#建立網域與-dkim)
  - [設定 DNS](#設定-dns)
  - [確認 PTR 反解](#確認-ptr-反解)
  - [建立信箱](#建立信箱)
  - [驗證](#驗證)
  - [郵件客戶端設定](#郵件客戶端設定)
  - [選用：開放 587 / 465](#選用開放-587--465)
  - [選用：以 mail.newdomain.com 開 Webmail](#選用以-mailnewdomaincom-開-webmail)
  - [用 API 操作（參考，實際欄位以 /admin/api/doc 為準）](#用-api-操作參考實際欄位以-adminapidoc-為準)
  - [檢查清單](#檢查清單)
- [腳本](#腳本)
  - [管理後台 過濾信件功能 SIEVE filter](#管理後台-過濾信件功能-sieve-filter)
  - [定期刪除腳本(伺服器)](#定期刪除腳本伺服器)
- [指令](#指令)
  - [查看 Poste 的資料庫/設定 在容器內找封鎖相關檔案](#查看-poste-的資料庫設定-在容器內找封鎖相關檔案)
  - [Haraka 的封鎖設定](#haraka-的封鎖設定)
    - [加入白名單](#加入白名單)
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

# 新增網域（多網域部署，共用現有主機）

讓同一台 Poste 主機（已有其他網域在用）再多收發一個新網域的信，不需要為新網域另外架主機。以下以新網域 `newdomain.com`、既有郵件主機 `mail.example.com`（`xxx.xxx.xxx.xxx`）為例。

## 架構說明

```
寄件者 → 查 newdomain.com 的 MX → mail.example.com (xxx.xxx.xxx.xxx:25) → Poste
                                                                      └─ 網域 newdomain.com → 信箱 user@newdomain.com
```

- `newdomain.com` 是**郵件網域**（信箱地址 @ 後面的部分），要在 Poste 的 Virtual domains 建立。
- `mail.example.com` 是**郵件主機**，MX 指向它即可，不需要另外建一個 `mail.newdomain.com` 的 Poste 網域。
- 共用主機的好處：SMTP/IMAP 憑證、PTR 反解、Nginx 設定都不用改。
- 若誤建了 `mail.newdomain.com` 這個網域（常見誤解：以為網域要跟主機名稱一致），先確認該網域下沒有信箱，再到 Virtual domains 刪除；資料目錄 `data/poste/domains/mail.newdomain.com` 應變空。

## 建立網域與 DKIM

1. **Virtual domains** → **Create new domain**，Name 填 `newdomain.com`（不要加 `mail.`）。
2. 其他選項維持預設：

| 選項 | 說明 | 建議 |
|---|---|---|
| Domain bin | 收下所有不存在信箱的信（catch-all） | 關閉，避免收大量垃圾信 |
| Forward | 整個網域轉寄到另一網域 | 關閉 |
| Force route | 強制經指定主機寄出 | 關閉 |

3. 點進 `newdomain.com`，DKIM 區塊按 **Create new key**，記下畫面顯示的 **Selector**（例如 `s202601011`）與 **TXT 值**（`k=rsa; p=MIIBIjAN...`），下一步 DNS 要用。

## 設定 DNS

到 `newdomain.com` 的 DNS 服務商新增：

| 類型 | 主機名稱 | 值 | 說明 |
|---|---|---|---|
| MX | `@` | `10 mail.example.com.` | 收信 |
| TXT | `@` | `v=spf1 mx a:mail.example.com -all` | SPF：只允許此主機寄信 |
| TXT | `<selector>._domainkey` | `k=rsa; p=MIIB...`（上一步產生） | DKIM 簽章 |
| TXT | `_dmarc` | `v=DMARC1; p=quarantine; rua=mailto:postmaster@newdomain.com` | DMARC 政策 |

注意事項：

- 若 `newdomain.com` 已有 SPF（`v=spf1 ...`），要**合併成一筆**，不能有兩筆 SPF TXT。
- DMARC 初期可用 `p=none` 觀察，確認正常後再改 `quarantine` / `reject`。
- `rua` 收件信箱需存在（下面會建立 `postmaster@newdomain.com`）。
- DKIM 值很長，部分 DNS 服務商需拆成多段字串，注意不要多出空白或換行。
- 若 `newdomain.com` 原本有其他 MX（例如 Google Workspace），要先刪除，否則信件會分流。

## 確認 PTR 反解

PTR 由 **VPS 服務商控制台**（Reverse DNS / rDNS）設定，所有共用這台主機的網域共用同一筆即可，不用逐網域設定。

```sh
dig +short -x xxx.xxx.xxx.xxx
# 應回傳 mail.example.com.
```

## 建立信箱

**Email accounts** → **Create new address**，`<帳號>@newdomain.com`。至少建立：

- `postmaster@newdomain.com`（RFC 規定必備，也收 DMARC 報告）
- 實際要用的信箱

若只是收信轉寄、不需要獨立信箱，可勾 **Redirect only** 並填轉寄目標。

## 驗證

```sh
# DNS 檢查（主機沒裝 dig 可在其他機器跑，或用 https://mxtoolbox.com/SuperTool.aspx）
dig +short MX newdomain.com
dig +short TXT newdomain.com
dig +short TXT <selector>._domainkey.newdomain.com
dig +short TXT _dmarc.newdomain.com
```

```sh
# 收信測試：從外部信箱寄到 postmaster@newdomain.com，觀察日誌
docker logs -f poste 2>&1 | grep -i newdomain
```

寄信測試：用 Webmail（`https://admin.example.com/webmail/`）以 `postmaster@newdomain.com` 登入寄給 Gmail，開「顯示原始郵件」確認 `SPF: PASS`、`DKIM: PASS (newdomain.com)`、`DMARC: PASS`；也可以寄到 https://www.mail-tester.com/ 拿評分（目標 9/10 以上）。DNS 生效通常 1 小時內，最長 48 小時。

## 郵件客戶端設定

客戶端主機名稱一律填 `mail.example.com`（這樣憑證才會吻合，不要填 `newdomain.com`）：

| 項目 | 值 |
|---|---|
| 帳號 | 完整信箱 `user@newdomain.com` |
| IMAP | `mail.example.com` : 993（SSL/TLS） |
| POP3 | `mail.example.com` : 995（SSL/TLS） |
| SMTP | `mail.example.com` : 587（STARTTLS）或 465（SSL/TLS），需先開放見下節 |

## 選用：開放 587 / 465

若 `docker-compose.yml` 預設沒對外開放 587、465，郵件客戶端就只能收信、不能用 SMTP 寄信（Webmail 不受影響）。要開放：

```yaml
# docker-compose.yml 的 poste.ports
    ports:
      - "25:25"
      - "110:110"
      - "143:143"
      - "465:465"
      - "587:587"
      - "993:993"
      - "995:995"
```

```sh
# 開放防火牆（firewalld）
firewall-cmd --permanent --add-port=587/tcp --add-port=465/tcp && firewall-cmd --reload
```

```sh
# 重建容器（會短暫中斷收發信）
docker compose up -d poste
```

## 選用：以 mail.newdomain.com 開 Webmail

只有想額外用 `https://mail.newdomain.com` 開 Webmail 時才需要，純收發信不需要。

```sh
# 1. DNS 先加一筆 A 記錄：mail.newdomain.com → xxx.xxx.xxx.xxx
# 2. 取得 newdomain.com 的 SSL 憑證，放到 conf/ssl/newdomain.com/（fullchain.pem、privkey.pem）
# 3. 複製既有 Nginx 設定並替換網域
cp conf/mail.example.com.conf conf/nginx/mail.newdomain.com.conf
sed -i "s/example.com/newdomain.com/g" conf/nginx/mail.newdomain.com.conf

# 4. 檢查語法並重新載入
docker compose exec nginx nginx -t && docker compose exec nginx nginx -s reload
```

憑證續期腳本（例如 `php-cli/renew-ssl.php`）如果是寫死單一網域，記得額外處理新網域的續期，不會自動一起續。

## 用 API 操作（參考，實際欄位以 `/admin/api/doc` 為準）

```sh
ADMIN='admin@mail.example.com:密碼'
API='https://admin.example.com/admin/api/v1'

# 刪除誤建的網域
curl -u "$ADMIN" -X DELETE "$API/domains/mail.newdomain.com"

# 建立新網域
curl -u "$ADMIN" -X POST "$API/domains" -d "name=newdomain.com"

# 產生 / 查詢 DKIM
curl -u "$ADMIN" -X PUT "$API/domains/newdomain.com/dkim"
curl -u "$ADMIN" "$API/domains/newdomain.com/dkim"

# 建立信箱
curl -u "$ADMIN" -X POST "$API/boxes" -d "email=postmaster@newdomain.com" -d "passwordPlaintext=強密碼" -d "name=Postmaster"
```

## 檢查清單

- [ ] 刪除誤建的 `mail.newdomain.com` 網域（如果有）
- [ ] 建立網域 `newdomain.com`
- [ ] 產生 DKIM，記下 selector 與值
- [ ] DNS：MX、SPF、DKIM、DMARC
- [ ] PTR 指向共用主機 `mail.example.com`
- [ ] 建立 `postmaster@newdomain.com` 與實際信箱
- [ ] 外部寄入測試成功
- [ ] 寄出測試 SPF / DKIM / DMARC 皆 PASS
- [ ] （選用）開放 587 / 465
- [ ] （選用）`mail.newdomain.com` Webmail

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

# 指令

## 查看 Poste 的資料庫/設定 在容器內找封鎖相關檔案

```sh
find / -name "*.db" 2>/dev/null
ls /data/
```

## Haraka 的封鎖設定

查看 Haraka 的封鎖設定

```sh
ls /usr/lib/node_modules/Haraka/config/
```

找 blacklist 相關檔案

```sh
find /usr/lib/node_modules/Haraka -name "*black*" -o -name "*block*" -o -name "*deny*" 2>/dev/null
```

### 加入白名單

查看 Haraka 的 whitelist 設定位置

```sh
find /usr/lib/node_modules/Haraka/config -name "*white*" -o -name "*allow*" 2>/dev/null
find /data -name "*white*" -o -name "*allow*" 2>/dev/null
```

通常 Haraka 的 IP 白名單在這裡

```sh
cat /usr/lib/node_modules/Haraka/config/connect.rdns_access.whitelist
# 或
cat /data/haraka/config/connect.rdns_access.whitelist
```

加入 Docker 網段

```sh
echo "172.18.0.0/16" >> /path/to/whitelist檔案
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
