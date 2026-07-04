# Namecheap(域名註冊和網絡託管服務)

```
```

## 目錄

- [Namecheap(域名註冊和網絡託管服務)](#namecheap域名註冊和網絡託管服務)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [說明](#說明)
- [購買域名並設定至 Cloudflare](#購買域名並設定至-cloudflare)
  - [流程概覽](#流程概覽)
  - [步驟一：在 Namecheap 購買域名](#步驟一在-namecheap-購買域名)
  - [步驟二：在 Cloudflare 新增網站](#步驟二在-cloudflare-新增網站)
    - [取得 Cloudflare Nameserver](#取得-cloudflare-nameserver)
  - [步驟三：在 Namecheap 修改 Nameserver](#步驟三在-namecheap-修改-nameserver)
  - [步驟四：在 Cloudflare 設定 DNS 記錄](#步驟四在-cloudflare-設定-dns-記錄)
  - [步驟五：確認設定生效](#步驟五確認設定生效)
- [Namecheap DNS 管理（不用 Cloudflare）](#namecheap-dns-管理不用-cloudflare)
- [DNSSEC 設定](#dnssec-設定)
- [常見問題](#常見問題)

## 參考資料

[Namecheap 官網](https://www.namecheap.com/)

[Namecheap API 文檔](https://www.namecheap.com/support/api/methods/)

[Namecheap - 如何更換 Nameserver](https://www.namecheap.com/support/knowledgebase/article.aspx/767/10/how-to-change-dns-for-a-domain/)

[Cloudflare - 更換 Nameserver 官方說明](https://developers.cloudflare.com/dns/zone-setups/full-setup/setup/)

# 說明

Namecheap 是知名的域名註冊商，以低廉的價格和免費 WhoisGuard（隱私保護）著稱。

**主要特點：**
- 域名價格競爭力強，常有折扣活動
- 免費 WhoisGuard 隱私保護（終身免費）
- 提供 DNS 管理、Email Forwarding、URL Redirect
- 支援 2FA 帳號安全驗證
- 提供免費靜態網站 hosting（Namecheap Hosting）

**網站：** https://www.namecheap.com/

# 購買域名並設定至 Cloudflare

## 流程概覽

```
1. Namecheap 購買域名
        ↓
2. Cloudflare 新增網站，取得兩組 Nameserver
        ↓
3. Namecheap 後台將 Nameserver 替換為 Cloudflare 提供的值
        ↓
4. 等待 DNS 生效（通常 10 分鐘 ~ 48 小時）
        ↓
5. 在 Cloudflare 管理所有 DNS 記錄
```

## 步驟一：在 Namecheap 購買域名

1. 前往 [Namecheap](https://www.namecheap.com/)
2. 搜尋想要的域名（如 `example.com`）
3. 加入購物車並完成付款
4. 購買完成後可在 **Dashboard → Domain List** 看到新域名

## 步驟二：在 Cloudflare 新增網站

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 點擊 **「Add a Site」（新增網站）**
3. 輸入在 Namecheap 購買的域名（如 `example.com`）
4. 選擇方案（免費方案 Free 即可）
5. Cloudflare 自動掃描現有 DNS 記錄，確認後點擊 **Continue**

### 取得 Cloudflare Nameserver

完成後 Cloudflare 會提供兩組 Nameserver，例如：

```
ns1.cloudflare.com    ← 範例，實際值每個帳號不同
ns2.cloudflare.com    ← 範例，實際值每個帳號不同
```

> **重要**：這兩組 Nameserver 是帳號專屬，請複製保存，下一步需填入 Namecheap。

## 步驟三：在 Namecheap 修改 Nameserver

1. 登入 [Namecheap](https://ap.www.namecheap.com/domains/list/)
2. 在 **Domain List** 找到你的域名，點擊 **「Manage」**
3. 在 **Nameservers** 區塊，下拉選單選擇 **「Custom DNS」**
4. 填入 Cloudflare 提供的兩組 Nameserver：

```
ns1.cloudflare.com
ns2.cloudflare.com
```

5. 點擊右側 ✓（綠色勾勾）儲存

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

# Namecheap DNS 管理（不用 Cloudflare）

若不使用 Cloudflare，可直接在 Namecheap 管理 DNS：

**Domain List → Manage → Advanced DNS**

常用記錄設定：

```
類型：A Record
Host：@
Value：1.2.3.4（主機 IP）
TTL：Automatic
```

```
類型：CNAME Record
Host：www
Value：example.com
TTL：Automatic
```

# DNSSEC 設定

DNSSEC（DNS Security Extensions）為 DNS 回應加上數位簽章，防止 DNS 快取污染與中間人攻擊，確保解析結果未被竄改。

**運作原理：**
- DNS 提供商負責簽署 Zone，產生 `DNSKEY` 記錄
- 域名商（Namecheap）負責在上層 TLD 登記 `DS 記錄`（Delegation Signer），串聯信任鏈

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

**Step 2 — 在 Namecheap 填入 DS 記錄**

1. 登入 [Namecheap](https://ap.www.namecheap.com/domains/list/)
2. 選擇域名 → **Manage → Advanced DNS**
3. 頁面下方找到 **「DNSSEC」** 區塊，展開後點擊 **「Add new record」**
4. 填入 Cloudflare 提供的 4 個值：

```
Key Tag   : （Cloudflare 提供）
Algorithm : 13
Digest    : （Cloudflare 提供）
Digest Type: 2
```

5. 點擊 ✓ 儲存

> 儲存後通常需要 **10 ~ 30 分鐘** 讓 DS 記錄傳播至 TLD 伺服器。

## 情況二：使用 Namecheap 自有 DNS（BasicDNS / PremiumDNS）

Namecheap PremiumDNS 方案支援 DNSSEC，BasicDNS 不支援。

1. **Domain List → Manage → Advanced DNS**
2. **「DNSSEC」** 區塊 → 切換開關為 **On**
3. Namecheap 會自動產生並上傳 DS 記錄至 TLD

> **注意**：需升級為 PremiumDNS（付費方案）才能使用自動 DNSSEC。

## 驗證 DNSSEC 是否生效

```bash
# 查詢 DS 記錄
dig DS example.com

# 查詢 DNSKEY 記錄
dig DNSKEY example.com
```

線上驗證工具：
```
https://dnssec-analyzer.verisignlabs.com/
https://dnsviz.net/
```

# 常見問題

**Q：Namecheap 免費的 WhoisGuard 是什麼？**

隱藏域名持有人的個人資訊（姓名、Email、地址），避免 WHOIS 查詢洩漏個資。Namecheap 對所有域名終身免費提供。

**Q：Namecheap 的 Nameserver 改成 Custom DNS 後，原本的設定還在嗎？**

Namecheap 的 Advanced DNS 設定不再生效，所有 DNS 由 Cloudflare 管理。若日後切換回 Namecheap BasicDNS，原設定仍保留。

**Q：更換 Nameserver 後多久生效？**

通常 10 分鐘 ~ 48 小時，Cloudflare Dashboard 會以 Email 通知完成。
