# Cloudflare SSL/TLS(憑證管理)

```
Cloudflare 提供免費 SSL 憑證與多種 TLS 模式，
管理「使用者 ←→ Cloudflare」與「Cloudflare ←→ 伺服器」兩段連線的加密方式。
```

## 目錄

- [Cloudflare SSL/TLS(憑證管理)](#cloudflare-ssltls憑證管理)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [說明](#說明)
  - [兩段連線架構](#兩段連線架構)
- [SSL/TLS 模式](#ssltls-模式)
  - [Off（關閉）](#off關閉)
  - [Flexible（彈性）](#flexible彈性)
  - [Full（完整）](#full完整)
  - [Full (Strict)（完整嚴格）](#full-strict完整嚴格)
  - [模式選擇建議](#模式選擇建議)
- [Edge Certificate（邊緣憑證）](#edge-certificate邊緣憑證)
  - [Universal SSL（通用 SSL）](#universal-ssl通用-ssl)
  - [Advanced Certificate Manager](#advanced-certificate-manager)
  - [自訂 SSL 憑證（Custom Certificate）](#自訂-ssl-憑證custom-certificate)
- [Origin Certificate（原始伺服器憑證）](#origin-certificate原始伺服器憑證)
  - [建立 Origin Certificate](#建立-origin-certificate)
  - [在 nginx 套用 Origin Certificate](#在-nginx-套用-origin-certificate)
  - [在 Apache 套用 Origin Certificate](#在-apache-套用-origin-certificate)
- [SSL/TLS 進階設定](#ssltls-進階設定)
  - [最低 TLS 版本](#最低-tls-版本)
  - [TLS 1.3](#tls-13)
  - [HSTS（HTTP Strict Transport Security）](#hstshttp-strict-transport-security)
  - [Always Use HTTPS](#always-use-https)
  - [Automatic HTTPS Rewrites](#automatic-https-rewrites)
  - [Minimum TLS Version](#minimum-tls-version)
- [常見使用情境](#常見使用情境)
  - [情境一：伺服器無 SSL 憑證（最快上手）](#情境一伺服器無-ssl-憑證最快上手)
  - [情境二：已有 Let's Encrypt 憑證](#情境二已有-lets-encrypt-憑證)
  - [情境三：使用 Cloudflare Origin Certificate（推薦）](#情境三使用-cloudflare-origin-certificate推薦)
- [確認 SSL 是否正常](#確認-ssl-是否正常)
- [常見問題](#常見問題)

## 參考資料

[Cloudflare SSL/TLS 官方文件](https://developers.cloudflare.com/ssl/)

[Cloudflare SSL/TLS 模式說明](https://developers.cloudflare.com/ssl/origin-configuration/ssl-modes/)

[Origin Certificate 說明](https://developers.cloudflare.com/ssl/origin-configuration/origin-ca/)

[HSTS 設定](https://developers.cloudflare.com/ssl/edge-certificates/additional-options/http-strict-transport-security/)

[Universal SSL](https://developers.cloudflare.com/ssl/edge-certificates/universal-ssl/)

# 說明

Cloudflare SSL 管理的核心是**兩段連線**，兩段可以分別設定加密方式：

## 兩段連線架構

```
使用者（瀏覽器）
      │
      │ ① Edge（前段）：使用者 ↔ Cloudflare
      │    由 Edge Certificate 管理
      │
 Cloudflare 節點
      │
      │ ② Origin（後段）：Cloudflare ↔ 你的伺服器
      │    由 SSL/TLS 模式設定決定
      │
  你的伺服器（nginx / Apache / 任何 Web Server）
```

- **Edge Certificate**：Cloudflare 對使用者端的憑證，免費自動提供（Universal SSL）
- **SSL/TLS Mode**：決定 Cloudflare 到伺服器這段要不要加密、是否驗證憑證

# SSL/TLS 模式

Cloudflare Dashboard → 選擇域名 → **SSL/TLS → Overview**

## Off（關閉）

```
使用者 ──HTTP──▶ Cloudflare ──HTTP──▶ 伺服器
```

- 完全不加密，**不建議使用**
- 網址列顯示「不安全」警告

## Flexible（彈性）

```
使用者 ──HTTPS──▶ Cloudflare ──HTTP──▶ 伺服器
```

- 使用者到 Cloudflare 這段加密（HTTPS）
- Cloudflare 到伺服器這段**不加密**（HTTP）
- **伺服器不需要 SSL 憑證**
- 適合：伺服器還沒設定 SSL、快速測試用途

> ⚠️ **安全性較低**：Cloudflare 到伺服器之間的流量未加密，若伺服器在不受信任的網路環境中有風險。

## Full（完整）

```
使用者 ──HTTPS──▶ Cloudflare ──HTTPS──▶ 伺服器
```

- 兩段都加密
- 伺服器需有 SSL 憑證，但 **Cloudflare 不驗證憑證有效性**（自簽憑證也接受）
- 適合：有 SSL 憑證但不是由受信任 CA 簽發的情況

> ⚠️ 無法防止中間人攻擊（因為不驗證憑證真偽）。

## Full (Strict)（完整嚴格）

```
使用者 ──HTTPS──▶ Cloudflare ──HTTPS──▶ 伺服器（需合法憑證）
```

- 兩段都加密
- 伺服器憑證必須由**受信任 CA 簽發**或使用 **Cloudflare Origin Certificate**
- **最安全的模式**，建議正式環境使用

## 模式選擇建議

| 情境 | 建議模式 |
|------|---------|
| 伺服器完全沒有 SSL | `Flexible`（暫時用，之後升級） |
| 伺服器有自簽憑證 | `Full` |
| 伺服器有 Let's Encrypt 憑證 | `Full (Strict)` |
| 伺服器有 Cloudflare Origin Certificate | `Full (Strict)` |
| 最高安全性需求 | `Full (Strict)` |

# Edge Certificate（邊緣憑證）

Edge Certificate 是 Cloudflare 對使用者端（瀏覽器）提供的 SSL 憑證，由 Cloudflare 管理。

## Universal SSL（通用 SSL）

**免費、自動、無需設定**。

- 所有啟用 Cloudflare Proxy（橘色雲）的域名自動獲得
- 涵蓋根域名（`example.com`）與一層子域名（`www.example.com`、`api.example.com`）
- 憑證由 Cloudflare 與 DigiCert / Let's Encrypt 合作簽發
- **自動續約**，無需人工介入

> ⚠️ **不涵蓋多層子域名**：`sub.api.example.com`（二層子域名）不在 Universal SSL 範圍內，需要 Advanced Certificate Manager。

確認 Universal SSL 狀態：

```
Cloudflare Dashboard → 域名 → SSL/TLS → Edge Certificates
```

狀態顯示 **Active** 表示正常。

## Advanced Certificate Manager

付費功能（每月約 $10），提供：
- 自訂 SAN（多域名）
- 支援多層萬用字元（`*.*.example.com`）
- 自訂憑證有效期
- 自訂 CA（DigiCert / Let's Encrypt）

## 自訂 SSL 憑證（Custom Certificate）

Enterprise 方案可上傳自有憑證：

```
SSL/TLS → Edge Certificates → Upload Custom Certificate
```

上傳時需要：
- 憑證檔案（PEM 格式）
- 私鑰（PEM 格式）

# Origin Certificate（原始伺服器憑證）

Origin Certificate 是 **Cloudflare 簽發給你的伺服器**的憑證，用於 Cloudflare ↔ 伺服器這段加密。

**特點：**
- 免費
- 有效期長（最長 15 年）
- 僅在 Cloudflare Proxy 啟用時有效（非公開 CA 簽發，瀏覽器直連時不受信任）
- 搭配 `Full (Strict)` 模式使用

## 建立 Origin Certificate

1. Cloudflare Dashboard → 選擇域名 → **SSL/TLS → Origin Server**
2. 點擊 **「Create Certificate」**
3. 選擇：
   - **讓 Cloudflare 產生私鑰**（簡單）或自行提供 CSR（進階）
   - 涵蓋的主機名稱（預設包含 `example.com` 與 `*.example.com`）
   - 有效期（15 年建議用於長期服務）
4. 下載兩個檔案：
   - **Origin Certificate**（`example.com.pem`）→ 即 `fullchain` / `cert`
   - **Private Key**（`example.com.key`）→ 私鑰，**只顯示一次，請立即儲存**

> ⚠️ 私鑰離開這個頁面後就無法再次查看，請務必儲存。

## 在 nginx 套用 Origin Certificate

將下載的兩個檔案上傳至伺服器，例如放在 `/etc/ssl/cloudflare/`：

```bash
mkdir -p /etc/ssl/cloudflare
# 將憑證與私鑰上傳或貼上後：
chmod 600 /etc/ssl/cloudflare/example.com.key
chmod 644 /etc/ssl/cloudflare/example.com.pem
```

nginx 設定（`/etc/nginx/conf.d/default.conf`）：

```nginx
server {
    listen 443 ssl;
    server_name example.com *.example.com;

    ssl_certificate     /etc/ssl/cloudflare/example.com.pem;
    ssl_certificate_key /etc/ssl/cloudflare/example.com.key;

    ssl_protocols       TLSv1.2 TLSv1.3;
    ssl_ciphers         HIGH:!aNULL:!MD5;

    location / {
        proxy_pass http://app:5000;
        proxy_set_header Host              $host;
        proxy_set_header X-Real-IP         $remote_addr;
        proxy_set_header X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

server {
    listen 80;
    server_name example.com *.example.com;
    return 301 https://$host$request_uri;
}
```

重新載入 nginx：

```bash
nginx -t && nginx -s reload
# 或 Docker 環境：
docker compose exec nginx nginx -s reload
```

## 在 Apache 套用 Origin Certificate

```apache
<VirtualHost *:443>
    ServerName example.com

    SSLEngine on
    SSLCertificateFile    /etc/ssl/cloudflare/example.com.pem
    SSLCertificateKeyFile /etc/ssl/cloudflare/example.com.key

    ProxyPass        / http://localhost:5000/
    ProxyPassReverse / http://localhost:5000/
</VirtualHost>
```

# SSL/TLS 進階設定

Cloudflare Dashboard → 域名 → **SSL/TLS → Edge Certificates**

## 最低 TLS 版本

設定瀏覽器與 Cloudflare 之間可使用的最低 TLS 版本。

```
SSL/TLS → Edge Certificates → Minimum TLS Version
```

| 設定 | 說明 |
|------|------|
| TLS 1.0 | 最寬鬆，支援老舊瀏覽器（不建議） |
| TLS 1.1 | 已廢棄（不建議） |
| **TLS 1.2** | **建議最低值**，現代瀏覽器都支援 |
| TLS 1.3 | 最安全，但少數舊環境不支援 |

## TLS 1.3

```
SSL/TLS → Edge Certificates → TLS 1.3 → On
```

啟用後同時支援 TLS 1.2 與 1.3，不影響舊瀏覽器相容性，**建議開啟**。

## HSTS（HTTP Strict Transport Security）

強制瀏覽器只透過 HTTPS 連線，防止 SSL stripping 攻擊。

```
SSL/TLS → Edge Certificates → HTTP Strict Transport Security (HSTS)
```

建議設定：

| 選項 | 建議值 | 說明 |
|------|--------|------|
| Enable HSTS | On | 啟用 |
| Max Age | 6 months（15768000 秒）或更長 | 瀏覽器記住的時間 |
| Include Subdomains | On（確認所有子域名都有 HTTPS） | 套用至子域名 |
| Preload | 視需求 | 加入瀏覽器 Preload List（不可輕易關閉） |

> ⚠️ **啟用前請確認**：所有子域名都已有正常 HTTPS，否則開啟後子域名無法以 HTTP 訪問。  
> ⚠️ **Preload 謹慎開啟**：一旦提交至 Preload List，即使關閉 HSTS 也無法立即取消，需等待數月。

## Always Use HTTPS

自動將所有 HTTP 請求重導向至 HTTPS：

```
SSL/TLS → Edge Certificates → Always Use HTTPS → On
```

等效於在 nginx 設定：
```nginx
return 301 https://$host$request_uri;
```

差別是由 Cloudflare 端重導向，不需要修改伺服器設定。

## Automatic HTTPS Rewrites

自動將頁面中的 `http://` 連結改寫為 `https://`，解決 Mixed Content（混合內容）警告：

```
SSL/TLS → Edge Certificates → Automatic HTTPS Rewrites → On
```

> 適合用在網頁中有部分靜態資源仍使用 HTTP 的情況（如舊 CDN 連結）。

# 常見使用情境

## 情境一：伺服器無 SSL 憑證（最快上手）

```
設定：SSL/TLS Mode → Flexible
適用：剛開始、測試環境、伺服器來不及設定 SSL
```

步驟：
1. Cloudflare DNS → 設定 A 記錄並啟用 Proxy（橘色雲）
2. SSL/TLS → Overview → 選擇 **Flexible**
3. SSL/TLS → Edge Certificates → **Always Use HTTPS → On**

完成後使用者訪問 `https://example.com` 即正常，伺服器維持 HTTP。

---

## 情境二：已有 Let's Encrypt 憑證

```
設定：SSL/TLS Mode → Full (Strict)
適用：伺服器已透過 certbot 取得 Let's Encrypt 憑證
```

步驟：
1. 確認 Let's Encrypt 憑證已正確設定（`nginx -t` 無錯誤）
2. SSL/TLS → Overview → 選擇 **Full (Strict)**
3. 啟用 Always Use HTTPS

> Let's Encrypt 憑證每 90 天到期，需設定自動續約（`certbot renew`）。

---

## 情境三：使用 Cloudflare Origin Certificate（推薦）

```
設定：SSL/TLS Mode → Full (Strict)
適用：想免費取得長期有效（15 年）憑證，且流量透過 Cloudflare Proxy
```

步驟：
1. Cloudflare → SSL/TLS → Origin Server → **Create Certificate**
2. 下載憑證（`.pem`）與私鑰（`.key`）至伺服器
3. 設定 nginx / Apache 使用該憑證
4. SSL/TLS → Overview → 選擇 **Full (Strict)**

優勢：
- 免費、有效期長（最長 15 年），不需自動續約
- 支援萬用字元（`*.example.com`），一張憑證涵蓋所有子域名
- 不需開放伺服器 port 80 進行驗證（與 Let's Encrypt 不同）

# 確認 SSL 是否正常

```bash
# 確認 HTTPS 可正常連線
curl -I https://example.com

# 查看憑證詳細資訊（簽發者、有效期）
echo | openssl s_client -connect example.com:443 2>/dev/null \
  | openssl x509 -noout -issuer -dates -subject

# 確認 TLS 版本
curl -vI https://example.com 2>&1 | grep "SSL connection"

# 線上工具：完整 SSL 分析
# https://www.ssllabs.com/ssltest/
# https://www.sslshopper.com/ssl-checker.html
```

Cloudflare Dashboard 確認：

```
SSL/TLS → Edge Certificates
→ 狀態顯示 Active Certificate
→ 確認 Universal SSL 已啟用
```

# 常見問題

**Q：Flexible 模式下，為什麼有時會出現無限重導向（redirect loop）？**

原因：伺服器的 nginx / Apache 設定了 HTTP → HTTPS 重導向，而 Cloudflare 以 HTTP 連入，導致循環。

解決方式：
1. 升級為 `Full` 或 `Full (Strict)` 模式，或
2. 移除伺服器端的 HTTP → HTTPS 重導向，改由 Cloudflare 的 **Always Use HTTPS** 處理

---

**Q：Origin Certificate 和 Let's Encrypt 有什麼差別？**

| 項目 | Origin Certificate | Let's Encrypt |
|------|-------------------|---------------|
| 費用 | 免費 | 免費 |
| 有效期 | 最長 15 年 | 90 天 |
| 自動續約 | 不需要 | 需要（certbot） |
| 驗證方式 | 不需驗證（Cloudflare 直接簽發） | HTTP / DNS 挑戰驗證 |
| 瀏覽器信任（直連） | ❌（僅 Cloudflare Proxy 下有效） | ✅ |
| 萬用字元 | ✅ 免費支援 | ✅（DNS 挑戰才能取得） |

**結論：** 流量通過 Cloudflare Proxy 時，優先使用 Origin Certificate（省事、長效）。需要直連（繞過 Cloudflare）時，才用 Let's Encrypt。

---

**Q：Universal SSL 為什麼還沒生效？**

- 通常需要等待 **15 分鐘至 24 小時**
- 確認 DNS 記錄的 Proxy 已啟用（橘色雲）
- 確認域名已完成 Cloudflare Nameserver 設定
- Dashboard → SSL/TLS → Edge Certificates 查看狀態

---

**Q：如何確認使用者連線的是 Cloudflare 的 SSL 還是伺服器的 SSL？**

啟用 Proxy（橘色雲）後，使用者連線到的是 **Cloudflare 的 Edge Certificate**，與伺服器憑證無關。查看瀏覽器憑證時，簽發者應為 Cloudflare 或其合作 CA（如 DigiCert）。

---

**Q：Full (Strict) 模式下，伺服器自簽憑證可以用嗎？**

不行。`Full (Strict)` 要求憑證由受信任 CA 簽發。  
使用自簽憑證請改用 `Full`（不驗證憑證有效性）。  
或改用 Cloudflare Origin Certificate（由 Cloudflare CA 簽發，Full (Strict) 信任）。
