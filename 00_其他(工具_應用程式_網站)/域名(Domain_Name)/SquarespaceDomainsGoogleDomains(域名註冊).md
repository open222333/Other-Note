# Squarespace Domains / Google Domains(域名註冊)

```
Google Domains 已於 2023 年將業務出售給 Squarespace，現為 Squarespace Domains
```

## 目錄

- [Squarespace Domains / Google Domains(域名註冊)](#squarespace-domains--google-domains域名註冊)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [說明](#說明)
- [購買域名並設定至 Cloudflare](#購買域名並設定至-cloudflare)
  - [流程概覽](#流程概覽)
  - [步驟一：在 Squarespace Domains 購買域名](#步驟一在-squarespace-domains-購買域名)
  - [步驟二：在 Cloudflare 新增網站](#步驟二在-cloudflare-新增網站)
    - [取得 Cloudflare Nameserver](#取得-cloudflare-nameserver)
  - [步驟三：在 Squarespace Domains 修改 Nameserver](#步驟三在-squarespace-domains-修改-nameserver)
  - [步驟四：在 Cloudflare 設定 DNS 記錄](#步驟四在-cloudflare-設定-dns-記錄)
  - [步驟五：確認設定生效](#步驟五確認設定生效)
- [從 Google Domains 遷移說明](#從-google-domains-遷移說明)
- [DNSSEC 設定](#dnssec-設定)
- [常見問題](#常見問題)

## 參考資料

[Squarespace Domains 官網](https://domains.squarespace.com/)

[Google Domains 遷移說明](https://support.google.com/domains/answer/13689670)

[Squarespace Domains - 更換 Nameserver](https://support.squarespace.com/hc/en-us/articles/213469948)

[Cloudflare - 更換 Nameserver 官方說明](https://developers.cloudflare.com/dns/zone-setups/full-setup/setup/)

# 說明

**Google Domains** 曾是 Google 旗下的域名註冊服務，以簡潔介面和免費 WHOIS 隱私保護著稱。2023 年 Google 宣布將 Google Domains 業務出售給 Squarespace，所有域名已陸續遷移至 **Squarespace Domains**。

> 若你原本使用 Google Domains，你的域名已自動遷移至 Squarespace Domains，帳號登入方式與管理介面已改變。

**主要特點（Squarespace Domains）：**
- 承接 Google Domains 原有域名
- 免費 WHOIS 隱私保護
- 支援 Google Workspace 整合（Email）
- 介面簡潔易用
- 價格透明，無隱藏費用

**網站：** https://domains.squarespace.com/

# 購買域名並設定至 Cloudflare

## 流程概覽

```
1. Squarespace Domains 購買域名
        ↓
2. Cloudflare 新增網站，取得兩組 Nameserver
        ↓
3. Squarespace Domains 後台將 Nameserver 替換為 Cloudflare 提供的值
        ↓
4. 等待 DNS 生效（通常 10 分鐘 ~ 48 小時）
        ↓
5. 在 Cloudflare 管理所有 DNS 記錄
```

## 步驟一：在 Squarespace Domains 購買域名

1. 前往 [Squarespace Domains](https://domains.squarespace.com/)
2. 搜尋想要的域名（如 `example.com`）
3. 加入購物車並完成付款（需 Squarespace 帳號）
4. 購買完成後可在帳戶 **Domains** 頁面看到新域名

## 步驟二：在 Cloudflare 新增網站

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 點擊 **「Add a Site」（新增網站）**
3. 輸入在 Squarespace Domains 購買的域名（如 `example.com`）
4. 選擇方案（免費方案 Free 即可）
5. Cloudflare 自動掃描現有 DNS 記錄，確認後點擊 **Continue**

### 取得 Cloudflare Nameserver

完成後 Cloudflare 會提供兩組 Nameserver，例如：

```
ns1.cloudflare.com    ← 範例，實際值每個帳號不同
ns2.cloudflare.com    ← 範例，實際值每個帳號不同
```

## 步驟三：在 Squarespace Domains 修改 Nameserver

1. 登入 [Squarespace Domains](https://account.squarespace.com/domains)
2. 選擇你的域名
3. 進入 **「DNS」** 設定
4. 找到 **「Nameservers」** 區塊
5. 選擇 **「Use custom nameservers」**
6. 填入 Cloudflare 提供的兩組 Nameserver：

```
ns1.cloudflare.com
ns2.cloudflare.com
```

7. 儲存設定

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

# 從 Google Domains 遷移說明

若你的域名原本在 Google Domains，2023 年後已**自動遷移至 Squarespace Domains**。

**重要事項：**
- 原本在 Google Domains 設定的 DNS 記錄會保留
- 原本使用的 Google Workspace Email 整合仍可繼續使用
- 登入方式由 Google 帳號改為 Squarespace 帳號（需重新設定密碼）
- 自動續費設定可能需要重新確認

**登入新帳號：**
1. 前往 https://account.squarespace.com/
2. 以收到的遷移邀請 Email 設定 Squarespace 密碼
3. 即可看到原本的 Google Domains 域名

# DNSSEC 設定

DNSSEC（DNS Security Extensions）為 DNS 回應加上數位簽章，防止 DNS 快取污染與中間人攻擊，確保解析結果未被竄改。

**運作原理：**
- DNS 提供商負責簽署 Zone，產生 `DNSKEY` 記錄
- 域名商（Squarespace Domains）負責在上層 TLD 登記 `DS 記錄`（Delegation Signer），串聯信任鏈

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

**Step 2 — 在 Squarespace Domains 填入 DS 記錄**

1. 登入 [Squarespace Domains](https://account.squarespace.com/domains)
2. 選擇域名 → **「DNS」** 頁籤
3. 找到 **「DNSSEC」** 區塊
4. 點擊 **「Add DS Record」**，填入 Cloudflare 提供的值：

```
Key Tag      : （Cloudflare 提供）
Algorithm    : 13
Digest Type  : 2
Digest       : （Cloudflare 提供）
```

5. 儲存設定

> 儲存後通常需要 **10 ~ 30 分鐘** 讓 DS 記錄傳播至 TLD 伺服器。

## 情況二：使用 Squarespace Domains 自有 DNS

1. **Domains → 選擇域名 → DNS**
2. 找到 **「DNSSEC」** 區塊 → 點擊啟用
3. Squarespace Domains 自動簽署並上傳 DS 記錄至 TLD

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

**Q：我的 Google Domains 域名去哪了？**

Google Domains 已出售給 Squarespace，你的域名已遷移至 Squarespace Domains。前往 https://account.squarespace.com/domains 登入查看。

**Q：遷移到 Squarespace 後，我的網站和 Email 還正常嗎？**

是的，DNS 設定和 Email 設定都會保留，服務不中斷。

**Q：Squarespace Domains 的價格和 Google Domains 一樣嗎？**

遷移後首個續費週期維持原價，之後可能有所調整。建議在續費前確認最新定價。

**Q：想離開 Squarespace Domains 怎麼辦？**

可以將域名轉移到其他域名商（如 Cloudflare Registrar、Namecheap 等）。在 Squarespace Domains 後台取得 Auth Code，然後在目標域名商發起轉移。

**Q：更換 Nameserver 後多久生效？**

通常 10 分鐘 ~ 48 小時。
