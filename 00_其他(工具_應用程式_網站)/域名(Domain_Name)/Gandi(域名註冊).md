# Gandi(域名註冊)

```
```

## 目錄

- [Gandi(域名註冊)](#gandi域名註冊)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [說明](#說明)
- [購買域名並設定至 Cloudflare](#購買域名並設定至-cloudflare)
  - [流程概覽](#流程概覽)
  - [步驟一：在 Gandi 購買域名](#步驟一在-gandi-購買域名)
  - [步驟二：在 Cloudflare 新增網站](#步驟二在-cloudflare-新增網站)
    - [取得 Cloudflare Nameserver](#取得-cloudflare-nameserver)
  - [步驟三：在 Gandi 修改 Nameserver](#步驟三在-gandi-修改-nameserver)
  - [步驟四：在 Cloudflare 設定 DNS 記錄](#步驟四在-cloudflare-設定-dns-記錄)
  - [步驟五：確認設定生效](#步驟五確認設定生效)
- [Gandi LiveDNS 管理（不用 Cloudflare）](#gandi-livedns-管理不用-cloudflare)
- [DNSSEC 設定](#dnssec-設定)
- [常見問題](#常見問題)

## 參考資料

[Gandi 官網](https://www.gandi.net/)

[Gandi API 文檔](https://api.gandi.net/docs/)

[Gandi LiveDNS 說明](https://api.gandi.net/docs/livedns/)

[Gandi - 如何更換 Nameserver](https://docs.gandi.net/en/domain_names/common_operations/changing_nameservers.html)

[Cloudflare - 更換 Nameserver 官方說明](https://developers.cloudflare.com/dns/zone-setups/full-setup/setup/)

# 說明

Gandi 是法國老牌域名註冊商，以「No Bullshit」（無廢話）理念著稱，不推銷不需要的服務，深受技術用戶信賴。

**主要特點：**
- 透明定價，無隱藏費用
- 免費 WHOIS 隱私保護
- 每個域名附贈 2 個免費 Email 信箱
- 自有 LiveDNS 系統，支援 Anycast DNS
- 支援 DNSSEC
- 提供 REST API
- 支援 `.fr`、`.eu` 等歐洲 ccTLD
- 總部位於法國，符合 GDPR

**網站：** https://www.gandi.net/

# 購買域名並設定至 Cloudflare

## 流程概覽

```
1. Gandi 購買域名
        ↓
2. Cloudflare 新增網站，取得兩組 Nameserver
        ↓
3. Gandi 後台將 Nameserver 替換為 Cloudflare 提供的值
        ↓
4. 等待 DNS 生效（通常 10 分鐘 ~ 48 小時）
        ↓
5. 在 Cloudflare 管理所有 DNS 記錄
```

## 步驟一：在 Gandi 購買域名

1. 前往 [Gandi](https://www.gandi.net/)
2. 搜尋想要的域名（如 `example.com`）
3. 加入購物車並完成付款
4. 購買完成後可在 **Dashboard → Domain** 看到新域名

## 步驟二：在 Cloudflare 新增網站

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 點擊 **「Add a Site」（新增網站）**
3. 輸入在 Gandi 購買的域名（如 `example.com`）
4. 選擇方案（免費方案 Free 即可）
5. Cloudflare 自動掃描現有 DNS 記錄，確認後點擊 **Continue**

### 取得 Cloudflare Nameserver

完成後 Cloudflare 會提供兩組 Nameserver，例如：

```
ns1.cloudflare.com    ← 範例，實際值每個帳號不同
ns2.cloudflare.com    ← 範例，實際值每個帳號不同
```

## 步驟三：在 Gandi 修改 Nameserver

1. 登入 [Gandi Dashboard](https://admin.gandi.net/)
2. 左側選單點擊 **「Domain」**，選擇你的域名
3. 點擊 **「Nameservers」** 頁籤
4. 預設為 Gandi LiveDNS，選擇 **「External」（外部）**
5. 刪除現有 Nameserver，填入 Cloudflare 提供的兩組：

```
ns1.cloudflare.com
ns2.cloudflare.com
```

6. 點擊 **「Save」** 儲存

> **DNS 生效時間**：通常需要 **10 分鐘 ~ 48 小時** 全球生效。

## 步驟四：在 Cloudflare 設定 DNS 記錄

Nameserver 生效後，在 Cloudflare Dashboard → 選擇域名 → **DNS → Records** 管理記錄。

| 類型 | 名稱 | 內容 | Proxy |
|------|------|------|-------|
| `A` | `@` | 主機 IP | 🟠 Proxied |
| `A` | `www` | 主機 IP | 🟠 Proxied |
| `MX` | `@` | 郵件伺服器 | ⬜ DNS Only |
| `TXT` | `@` | SPF/DKIM 驗證 | ⬜ DNS Only |

## 步驟五：確認設定生效

```bash
# 查詢 Nameserver 是否已更新
dig NS example.com

# 查詢 A 記錄
dig A example.com

# 使用 Google DNS 查詢
dig @8.8.8.8 A example.com
```

# Gandi LiveDNS 管理（不用 Cloudflare）

若使用 Gandi 自有的 LiveDNS 管理 DNS：

**Dashboard → Domain → 選擇域名 → DNS Records**

常用記錄設定：

```
類型：A
名稱：@
TTL：10800
值：1.2.3.4（主機 IP）
```

```
類型：CNAME
名稱：www
TTL：10800
值：example.com.
```

Gandi LiveDNS 也提供 API 操作：

```bash
# 使用 Gandi API 查詢 DNS 記錄
curl -X GET \
  https://api.gandi.net/v5/livedns/domains/example.com/records \
  -H "Authorization: Apikey YOUR_API_KEY"
```

# DNSSEC 設定

DNSSEC（DNS Security Extensions）為 DNS 回應加上數位簽章，防止 DNS 快取污染與中間人攻擊，確保解析結果未被竄改。

**運作原理：**
- DNS 提供商負責簽署 Zone，產生 `DNSKEY` 記錄
- 域名商（Gandi）負責在上層 TLD 登記 `DS 記錄`（Delegation Signer），串聯信任鏈

## 情況一：使用 Cloudflare DNS（Nameserver 已指向 Cloudflare）

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

**Step 2 — 在 Gandi 填入 DS 記錄**

1. 登入 [Gandi Dashboard](https://admin.gandi.net/)
2. 左側選單 → **「Domain」** → 選擇域名
3. 點擊 **「DNSSEC」** 頁籤
4. 點擊 **「Add a key」**
5. 選擇類型為 **「DS」**，填入 Cloudflare 提供的值：

```
Key Tag（Flag）  : （Cloudflare 提供）
Algorithm        : 13
Digest Type      : 2
Digest           : （Cloudflare 提供）
```

6. 點擊 **「Submit」** 儲存

> 儲存後通常需要 **10 ~ 30 分鐘** 讓 DS 記錄傳播至 TLD 伺服器。

## 情況二：使用 Gandi LiveDNS（自有 DNS）

Gandi LiveDNS 支援自動 DNSSEC，無需手動設定：

1. **Dashboard → Domain → 選擇域名 → DNSSEC** 頁籤
2. 確認 Nameserver 設定為 Gandi LiveDNS（預設即是）
3. 點擊 **「Enable DNSSEC」** 或確認狀態為 Active
4. Gandi 自動簽署並上傳 DS 記錄至 TLD

> Gandi LiveDNS 本身已支援 DNSSEC，是選擇 Gandi 的優勢之一。

## 驗證 DNSSEC 是否生效

```bash
# 查詢 DS 記錄
dig DS example.com

# 查詢 DNSKEY 記錄
dig DNSKEY example.com

# 驗證含 DNSSEC 的 A 記錄回應
dig +dnssec A example.com
```

線上驗證工具：
```
https://dnssec-analyzer.verisignlabs.com/
https://dnsviz.net/
```

# 常見問題

**Q：Gandi 附贈的 Email 信箱如何使用？**

每個域名附贈 2 個 Gandi Mail 信箱。若將 Nameserver 指向 Cloudflare，則 Gandi Mail 的 MX 記錄需手動在 Cloudflare 新增，Gandi 後台信箱功能仍可正常使用。

**Q：Gandi 比 GoDaddy 貴嗎？**

Gandi 無首年折扣，定價透明，通常比 GoDaddy 首年便宜但比續費便宜。整體而言屬中等價位，勝在誠實定價。

**Q：更換 Nameserver 後多久生效？**

通常 10 分鐘 ~ 48 小時。

**Q：Gandi 支援哪些 TLD？**

支援超過 700 種 TLD，包含大量歐洲 ccTLD（.fr、.de、.eu）以及新 TLD（.io、.app、.xyz）。
