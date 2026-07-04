# Porkbun(域名註冊)

```
```

## 目錄

- [Porkbun(域名註冊)](#porkbun域名註冊)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [說明](#說明)
- [購買域名並設定至 Cloudflare](#購買域名並設定至-cloudflare)
  - [流程概覽](#流程概覽)
  - [步驟一：在 Porkbun 購買域名](#步驟一在-porkbun-購買域名)
  - [步驟二：在 Cloudflare 新增網站](#步驟二在-cloudflare-新增網站)
    - [取得 Cloudflare Nameserver](#取得-cloudflare-nameserver)
  - [步驟三：在 Porkbun 修改 Nameserver](#步驟三在-porkbun-修改-nameserver)
  - [步驟四：在 Cloudflare 設定 DNS 記錄](#步驟四在-cloudflare-設定-dns-記錄)
  - [步驟五：確認設定生效](#步驟五確認設定生效)
- [Porkbun DNS 管理（不用 Cloudflare）](#porkbun-dns-管理不用-cloudflare)
- [DNSSEC 設定](#dnssec-設定)
- [常見問題](#常見問題)

## 參考資料

[Porkbun 官網](https://porkbun.com/)

[Porkbun API 文檔](https://porkbun.com/api/json/v3/documentation)

[Porkbun - 如何更換 Nameserver](https://kb.porkbun.com/article/22-how-to-change-your-nameservers)

[Cloudflare - 更換 Nameserver 官方說明](https://developers.cloudflare.com/dns/zone-setups/full-setup/setup/)

# 說明

Porkbun 是美國知名域名註冊商，以價格低廉、介面清爽和免費附加服務著稱，深受開發者喜愛。

**主要特點：**
- 域名價格低廉，常為市場最便宜選項之一
- 免費 WHOIS 隱私保護
- 免費 SSL 憑證（Let's Encrypt 整合）
- 免費 URL 轉向（URL Forwarding）
- 免費 Email Forwarding
- 支援 API 管理域名
- 介面簡潔現代

**網站：** https://porkbun.com/

# 購買域名並設定至 Cloudflare

## 流程概覽

```
1. Porkbun 購買域名
        ↓
2. Cloudflare 新增網站，取得兩組 Nameserver
        ↓
3. Porkbun 後台將 Nameserver 替換為 Cloudflare 提供的值
        ↓
4. 等待 DNS 生效（通常 10 分鐘 ~ 48 小時）
        ↓
5. 在 Cloudflare 管理所有 DNS 記錄
```

## 步驟一：在 Porkbun 購買域名

1. 前往 [Porkbun](https://porkbun.com/)
2. 搜尋想要的域名（如 `example.com`）
3. 加入購物車並完成付款
4. 購買完成後可在 **Account → Domain Management** 看到新域名

## 步驟二：在 Cloudflare 新增網站

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 點擊 **「Add a Site」（新增網站）**
3. 輸入在 Porkbun 購買的域名（如 `example.com`）
4. 選擇方案（免費方案 Free 即可）
5. Cloudflare 自動掃描現有 DNS 記錄，確認後點擊 **Continue**

### 取得 Cloudflare Nameserver

完成後 Cloudflare 會提供兩組 Nameserver，例如：

```
ns1.cloudflare.com    ← 範例，實際值每個帳號不同
ns2.cloudflare.com    ← 範例，實際值每個帳號不同
```

> **重要**：這兩組 Nameserver 是帳號專屬，請複製保存。

## 步驟三：在 Porkbun 修改 Nameserver

1. 登入 [Porkbun](https://porkbun.com/account/domainsSpeedy)
2. 在域名列表找到你的域名，點擊 **「Details」**（展開管理面板）
3. 找到 **「Authoritative Nameservers」** 區塊
4. 點擊 **「Edit」**，刪除現有 Nameserver
5. 填入 Cloudflare 提供的兩組 Nameserver：

```
ns1.cloudflare.com
ns2.cloudflare.com
```

6. 點擊 **「Add」** 後儲存

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

或使用線上工具：`https://dnschecker.org/`

# Porkbun DNS 管理（不用 Cloudflare）

若不使用 Cloudflare，可在 Porkbun 的 DNS 管理介面直接設定：

**Domain Management → 選擇域名 → DNS Records**

常用記錄設定：

```
類型：A
Host：留空（代表 @）
Answer：1.2.3.4（主機 IP）
TTL：600
```

```
類型：CNAME
Host：www
Answer：example.com
TTL：600
```

# DNSSEC 設定

DNSSEC（DNS Security Extensions）為 DNS 回應加上數位簽章，防止 DNS 快取污染與中間人攻擊，確保解析結果未被竄改。

**運作原理：**
- DNS 提供商負責簽署 Zone，產生 `DNSKEY` 記錄
- 域名商（Porkbun）負責在上層 TLD 登記 `DS 記錄`（Delegation Signer），串聯信任鏈

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

**Step 2 — 在 Porkbun 填入 DS 記錄**

1. 登入 [Porkbun](https://porkbun.com/account/domainsSpeedy)
2. 找到域名，點擊展開管理面板（Domain Details）
3. 找到 **「DNSSEC」** 列，點擊 **「Manage」**
4. 點擊 **「Add DS Record」**，填入 Cloudflare 提供的值：

```
Key Tag      : （Cloudflare 提供）
Algorithm    : 13
Digest Type  : 2
Digest       : （Cloudflare 提供）
```

5. 點擊 **「Add」** 儲存

> 儲存後通常需要 **10 ~ 30 分鐘** 讓 DS 記錄傳播至 TLD 伺服器。

## 情況二：使用 Porkbun 自有 DNS

Porkbun 自有 DNS 支援 DNSSEC，可直接啟用：

1. 展開域名管理面板 → **「DNSSEC」→「Manage」**
2. 點擊 **「Enable DNSSEC」**（或對應的切換開關）
3. Porkbun 自動產生並上傳 DS 記錄至 TLD

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

**Q：Porkbun 的 SSL 憑證是什麼？**

Porkbun 提供免費 Let's Encrypt SSL 憑證，可在域名管理頁面下載。若搭配 Cloudflare，則使用 Cloudflare 的免費 SSL。

**Q：Porkbun 支援哪些 TLD？**

支援非常廣泛，包含 `.com`、`.net`、`.org`、`.io`、`.app`、`.dev`、`.ai`、`.xyz` 等數百種。部分新 TLD 的首年價格極低。

**Q：Porkbun 有 API 嗎？**

有。Porkbun 提供 REST API，可用於查詢域名、管理 DNS 記錄等。需在帳號設定中啟用 API 並取得 API Key 和 Secret。

**Q：更換 Nameserver 後多久生效？**

通常 10 分鐘 ~ 48 小時，Cloudflare Dashboard 中域名狀態會由 Pending 變為 Active。
