# GoDaddy(域名註冊及網站代管)

```
```

## 目錄

- [GoDaddy(域名註冊及網站代管)](#godaddy域名註冊及網站代管)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [購買域名並設定至 Cloudflare](#購買域名並設定至-cloudflare)
  - [說明](#說明)
  - [流程概覽](#流程概覽)
  - [步驟一：在 GoDaddy 購買域名](#步驟一在-godaddy-購買域名)
  - [步驟二：在 Cloudflare 新增網站](#步驟二在-cloudflare-新增網站)
    - [取得 Cloudflare Nameserver](#取得-cloudflare-nameserver)
  - [步驟三：在 GoDaddy 修改 Nameserver](#步驟三在-godaddy-修改-nameserver)
  - [步驟四：在 Cloudflare 設定 DNS 記錄](#步驟四在-cloudflare-設定-dns-記錄)
    - [常見 DNS 記錄類型](#常見-dns-記錄類型)
    - [範例：網站指向主機 IP](#範例網站指向主機-ip)
    - [範例：子域名 CNAME](#範例子域名-cname)
    - [範例：MX 郵件記錄](#範例mx-郵件記錄)
  - [步驟五：確認設定生效](#步驟五確認設定生效)
  - [Cloudflare Proxy（橘色雲）說明](#cloudflare-proxy橘色雲說明)
  - [DNSSEC 設定](#dnssec-設定)
  - [常見問題](#常見問題)
- [範例](#範例)
  - [Python](#python)

## 參考資料

[The GoDaddy API](https://developer.godaddy.com/)

[Cloudflare - 更換 Nameserver 官方說明](https://developers.cloudflare.com/dns/zone-setups/full-setup/setup/)

[GoDaddy - 變更 Nameserver](https://tw.godaddy.com/help/change-nameservers-for-my-domains-664)

[Cloudflare DNS 記錄說明](https://developers.cloudflare.com/dns/manage-dns-records/how-to/create-dns-records/)

# 購買域名並設定至 Cloudflare

## 說明

將域名的 DNS 管理從 GoDaddy 移交給 Cloudflare，可以享有：

- Cloudflare 提供的 DDoS 防護與 CDN 加速
- 免費 SSL/TLS 憑證（HTTPS）
- 更快速的 DNS 解析
- 更方便的 DNS 管理介面
- 防火牆規則、頁面規則等進階功能

> **注意**：將 Nameserver 指向 Cloudflare 後，GoDaddy 的 DNS 管理介面將不再生效，所有 DNS 記錄須在 Cloudflare 管理。

## 流程概覽

```
1. GoDaddy 購買域名
        ↓
2. Cloudflare 新增網站，取得兩組 Nameserver
        ↓
3. GoDaddy 後台將 Nameserver 替換為 Cloudflare 提供的值
        ↓
4. 等待 DNS 生效（通常 10 分鐘 ~ 48 小時）
        ↓
5. 在 Cloudflare 管理所有 DNS 記錄
```

## 步驟一：在 GoDaddy 購買域名

1. 前往 [GoDaddy](https://www.godaddy.com/)
2. 搜尋想要的域名（如 `example.com`）
3. 加入購物車並完成付款
4. 購買完成後可在 **我的產品 → 域名** 看到新域名

## 步驟二：在 Cloudflare 新增網站

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 點擊右上角 **「Add a Site」（新增網站）**
3. 輸入在 GoDaddy 購買的域名（如 `example.com`）
4. 選擇方案（免費方案 Free 即可）
5. Cloudflare 會自動掃描現有 DNS 記錄（GoDaddy 預設的），確認後點擊 **Continue**

### 取得 Cloudflare Nameserver

完成上述步驟後，Cloudflare 會提供兩組 Nameserver，例如：

```
ns1.cloudflare.com    ← 範例，實際值每個帳號不同
ns2.cloudflare.com    ← 範例，實際值每個帳號不同
```

> **重要**：這兩組 Nameserver 是你的帳號專屬，請複製並保存，下一步需要填入 GoDaddy。

## 步驟三：在 GoDaddy 修改 Nameserver

1. 登入 [GoDaddy](https://account.godaddy.com/products/)
2. 前往 **我的產品 → 域名**
3. 點擊欲修改的域名旁的 **「...」→「管理 DNS」**

   或直接進入 **域名設定 → Nameserver**

4. 點擊 **「變更」**，選擇 **「輸入我自己的 Nameserver（進階）」**
5. 將 Cloudflare 提供的兩組 Nameserver 填入：

```
ns1.cloudflare.com
ns2.cloudflare.com
```

6. 儲存設定

> **DNS 生效時間**：更改 Nameserver 後通常需要 **10 分鐘 ~ 48 小時** 才會全球生效。Cloudflare Dashboard 會寄送 Email 通知確認完成。

## 步驟四：在 Cloudflare 設定 DNS 記錄

Nameserver 生效後，在 Cloudflare Dashboard → 選擇你的域名 → **DNS → Records** 管理所有記錄。

### 常見 DNS 記錄類型

| 類型 | 說明 | 範例用途 |
|------|------|---------|
| `A` | 域名對應 IPv4 位址 | 網站主機 IP |
| `AAAA` | 域名對應 IPv6 位址 | 網站主機 IPv6 |
| `CNAME` | 域名對應另一個域名 | 子域名、CDN |
| `MX` | 郵件伺服器 | Email 收信設定 |
| `TXT` | 文字記錄 | 驗證、SPF、DKIM |
| `NS` | Nameserver 記錄 | 子域名委派 |

### 範例：網站指向主機 IP

```
類型：A
名稱：@          ← 代表根域名 example.com
內容：1.2.3.4    ← 你的主機 IP
TTL：Auto
Proxy：橘色雲 (Proxied)  ← 啟用 Cloudflare CDN/防護
```

```
類型：A
名稱：www
內容：1.2.3.4
TTL：Auto
Proxy：橘色雲 (Proxied)
```

### 範例：子域名 CNAME

```
類型：CNAME
名稱：blog
內容：your-blog.github.io
TTL：Auto
Proxy：灰色雲 (DNS Only)  ← 依需求決定
```

### 範例：MX 郵件記錄

```
類型：MX
名稱：@
內容：mail.example.com
Priority：10
TTL：Auto
Proxy：灰色雲 (DNS Only)  ← MX 記錄不可啟用 Proxy
```

## 步驟五：確認設定生效

使用以下指令確認 DNS 是否已指向 Cloudflare：

```bash
# 查詢域名的 Nameserver
dig NS example.com

# 查詢 A 記錄
dig A example.com

# 使用指定 DNS 伺服器查詢（確認已生效）
dig @8.8.8.8 A example.com
```

或使用線上工具：

```
https://dnschecker.org/
https://www.whatsmydns.net/
```

Cloudflare Dashboard 中，域名狀態顯示 **「Active」** 表示設定完成。

## Cloudflare Proxy（橘色雲）說明

Cloudflare 的每筆 DNS 記錄可選擇是否啟用 Proxy（代理）：

| 狀態 | 圖示 | 說明 |
|------|------|------|
| Proxied（代理） | 🟠 橘色雲 | 流量經過 Cloudflare，享有 CDN、防護、隱藏真實 IP |
| DNS Only | ⬜ 灰色雲 | 僅 DNS 解析，流量直接到主機，不經 Cloudflare |

**建議啟用 Proxy（橘色雲）的情況：**
- Web 網站（HTTP/HTTPS）
- 需要 DDoS 防護
- 想隱藏主機真實 IP

**建議使用 DNS Only（灰色雲）的情況：**
- MX 記錄（郵件）**必須** DNS Only
- SSH、FTP、非 HTTP 服務
- 特定需要直連的服務

## DNSSEC 設定

DNSSEC（DNS Security Extensions）為 DNS 回應加上數位簽章，防止 DNS 快取污染（Cache Poisoning）與中間人攻擊，確保解析結果未被竄改。

**運作原理：**
- DNS 提供商（如 Cloudflare）負責簽署 Zone，產生 `DNSKEY` 記錄
- 域名商（GoDaddy）負責在上層 TLD 登記 `DS 記錄`（Delegation Signer），串聯信任鏈

### 情況一：使用 Cloudflare DNS（Nameserver 已指向 Cloudflare）

**Step 1 — 在 Cloudflare 啟用 DNSSEC，取得 DS 記錄**

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 選擇域名 → **DNS → Settings → DNSSEC → Enable DNSSEC**
3. 記下以下 4 個欄位的值：

```
Key Tag      : （數字，例如 2371）
Algorithm    : 13（ECDSA Curve P-256 with SHA-256）
Digest Type  : 2（SHA-256）
Digest       : （長串十六進位）
```

**Step 2 — 在 GoDaddy 填入 DS 記錄**

1. 登入 [GoDaddy](https://account.godaddy.com/products/)
2. **我的產品 → 域名** → 點擊域名旁 **「...」→「管理 DNS」**
3. 頁面最下方找到 **「DNS 安全性 (DNSSEC)」**
4. 點擊 **「新增」**，填入 Cloudflare 提供的 4 個值
5. 儲存

> 儲存後通常需要 **10 ~ 30 分鐘** 讓 DS 記錄傳播至 TLD 伺服器。

### 情況二：使用 GoDaddy 自有 DNS

GoDaddy 使用自家 DNS 時，可直接在後台啟用 DNSSEC，無需手動填寫 DS 記錄：

1. **我的產品 → 域名 → 管理 DNS**
2. 頁面最下方 **「DNS 安全性 (DNSSEC)」→「新增」**
3. GoDaddy 介面會引導完成設定

### 驗證 DNSSEC 是否生效

```bash
# 查詢 DS 記錄（需等 TLD 傳播完成）
dig DS example.com

# 查詢 DNSKEY 記錄
dig DNSKEY example.com

# 完整 DNSSEC 鏈驗證（線上工具）
# https://dnssec-analyzer.verisignlabs.com/
# https://dnsviz.net/
```

## 常見問題

**Q：更改 Nameserver 後多久生效？**

通常 10 分鐘 ~ 48 小時，Cloudflare 確認後會寄 Email 通知。

**Q：Cloudflare 免費方案夠用嗎？**

免費方案提供：無限 DNS 記錄、DDoS 防護、免費 SSL、基本 CDN，一般個人或小型網站完全夠用。

**Q：改了 Nameserver 後，原本 GoDaddy 的 DNS 設定還有效嗎？**

**無效**。一旦 Nameserver 指向 Cloudflare，所有 DNS 記錄須在 Cloudflare 管理，GoDaddy 的 DNS 介面不再生效。Cloudflare 在你新增網站時會自動掃描並匯入原有記錄，請確認匯入是否正確。

**Q：可以只用 Cloudflare DNS，不啟用 Proxy 嗎？**

可以。將所有記錄設為 DNS Only（灰色雲），Cloudflare 僅作為 DNS 解析服務，不代理流量。

**Q：SSL 憑證如何設定？**

啟用 Proxy（橘色雲）後，Cloudflare 自動提供免費 SSL。建議在 Cloudflare Dashboard → SSL/TLS → Overview 選擇 **「Full (strict)」** 模式（需要主機也有 SSL 憑證），或至少選擇 **「Flexible」**（主機無需 SSL，但安全性較低）。

# 範例

## Python

```ini
[GODADDY]
API_KEY=key
API_SECRET=sercret
```

```Python
import os
import requests
from configparser import ConfigParser


conf = ConfigParser()
conf.read(f'{os.path.dirname(__file__)}/config.ini', encoding='utf-8')


def get_domain_info(domain, shopper_id, is_ote=False):
    '''
    is_ote: 使用ote環境的api
    '''

    API_KEY = conf.get('GODADDY', 'API_KEY')
    API_SECRET = conf.get('GODADDY', 'API_SECRET')

    if is_ote:
        # ote 環境
        api_url = 'api.ote-godaddy.com'
    else:
        # product 環境
        api_url = 'api.godaddy.com'

    header_content = {
        'X-Shopper-Id': f'{shopper_id}',
        'Authorization': f'sso-key {API_KEY}:{API_SECRET}',
        'accept': 'application/json'
    }

    result = requests.get(
        f'https://{api_url}/v1/domains/{domain}',
        headers=header_content
    ).json()

    return result
```