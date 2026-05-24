# Dynadot(域名註冊)

```
```

## 目錄

- [Dynadot(域名註冊)](#dynadot域名註冊)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [說明](#說明)
- [購買域名並設定至 Cloudflare](#購買域名並設定至-cloudflare)
  - [流程概覽](#流程概覽)
  - [步驟一：在 Dynadot 購買域名](#步驟一在-dynadot-購買域名)
  - [步驟二：在 Cloudflare 新增網站](#步驟二在-cloudflare-新增網站)
    - [取得 Cloudflare Nameserver](#取得-cloudflare-nameserver)
  - [步驟三：在 Dynadot 修改 Nameserver](#步驟三在-dynadot-修改-nameserver)
  - [步驟四：在 Cloudflare 設定 DNS 記錄](#步驟四在-cloudflare-設定-dns-記錄)
  - [步驟五：確認設定生效](#步驟五確認設定生效)
- [Dynadot DNS 管理（不用 Cloudflare）](#dynadot-dns-管理不用-cloudflare)
- [DNSSEC 設定](#dnssec-設定)
- [常見問題](#常見問題)

## 參考資料

[Dynadot 官網](https://www.dynadot.com/)

[Dynadot API 文檔](https://www.dynadot.com/community/help/question/api-setting)

[Dynadot - 如何設定 Nameserver](https://www.dynadot.com/community/help/question/set-nameserver)

[Cloudflare - 更換 Nameserver 官方說明](https://developers.cloudflare.com/dns/zone-setups/full-setup/setup/)

# 說明

Dynadot 是美國域名註冊商，以價格合理、介面易用和良好的中文支援著稱，在亞洲市場有一定用戶基礎。

**主要特點：**
- 價格具競爭力，常有促銷折扣
- 提供繁體中文介面
- 免費 WHOIS 隱私保護
- 支援多種付款方式（包含支付寶）
- 提供 Website Builder 和 Email Hosting
- 支援 API 管理
- 域名市場（二手域名買賣）

**網站：** https://www.dynadot.com/

# 購買域名並設定至 Cloudflare

## 流程概覽

```
1. Dynadot 購買域名
        ↓
2. Cloudflare 新增網站，取得兩組 Nameserver
        ↓
3. Dynadot 後台將 Nameserver 替換為 Cloudflare 提供的值
        ↓
4. 等待 DNS 生效（通常 10 分鐘 ~ 48 小時）
        ↓
5. 在 Cloudflare 管理所有 DNS 記錄
```

## 步驟一：在 Dynadot 購買域名

1. 前往 [Dynadot](https://www.dynadot.com/)
2. 搜尋想要的域名（如 `example.com`）
3. 加入購物車並完成付款
4. 購買完成後可在 **我的帳戶 → 域名** 看到新域名

## 步驟二：在 Cloudflare 新增網站

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 點擊 **「Add a Site」（新增網站）**
3. 輸入在 Dynadot 購買的域名（如 `example.com`）
4. 選擇方案（免費方案 Free 即可）
5. Cloudflare 自動掃描現有 DNS 記錄，確認後點擊 **Continue**

### 取得 Cloudflare Nameserver

完成後 Cloudflare 會提供兩組 Nameserver，例如：

```
ns1.cloudflare.com    ← 範例，實際值每個帳號不同
ns2.cloudflare.com    ← 範例，實際值每個帳號不同
```

## 步驟三：在 Dynadot 修改 Nameserver

1. 登入 [Dynadot](https://www.dynadot.com/account/domain/list.html)
2. 點擊域名名稱進入管理頁面
3. 點擊左側 **「域名設定」→「域名記錄」**（或 **Name Servers**）
4. 選擇 **「自訂域名伺服器」（Custom Nameservers）**
5. 填入 Cloudflare 提供的兩組 Nameserver：

```
ns1.cloudflare.com
ns2.cloudflare.com
```

6. 點擊 **「儲存域名伺服器」** 儲存

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

# Dynadot DNS 管理（不用 Cloudflare）

若使用 Dynadot 自有 DNS 管理：

**帳戶 → 域名 → 選擇域名 → DNS 記錄**

常用記錄設定：

```
類型：A
主機：留空（代表 @）
IP 地址：1.2.3.4（主機 IP）
TTL：600
```

```
類型：CNAME
主機：www
目標主機：example.com
TTL：600
```

# DNSSEC 設定

DNSSEC（DNS Security Extensions）為 DNS 回應加上數位簽章，防止 DNS 快取污染與中間人攻擊，確保解析結果未被竄改。

**運作原理：**
- DNS 提供商負責簽署 Zone，產生 `DNSKEY` 記錄
- 域名商（Dynadot）負責在上層 TLD 登記 `DS 記錄`（Delegation Signer），串聯信任鏈

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

**Step 2 — 在 Dynadot 填入 DS 記錄**

1. 登入 [Dynadot](https://www.dynadot.com/account/domain/list.html)
2. 選擇域名 → 點擊 **「域名設定」**（Domain Settings）
3. 左側找到 **「DS 記錄」**（DS Records）
4. 點擊 **「新增 DS 記錄」**，填入 Cloudflare 提供的值：

```
Key Tag      : （Cloudflare 提供）
Algorithm    : 13
Digest Type  : 2
Digest       : （Cloudflare 提供）
```

5. 點擊 **「儲存」**

> 儲存後通常需要 **10 ~ 30 分鐘** 讓 DS 記錄傳播至 TLD 伺服器。

## 情況二：使用 Dynadot 自有 DNS

1. **帳戶 → 域名 → 選擇域名 → DNS 設定**
2. 切換至 **「DNSSEC」** 區塊
3. 點擊啟用 / Enable DNSSEC
4. Dynadot 自動簽署並上傳 DS 記錄至 TLD

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

**Q：Dynadot 有中文介面嗎？**

有，Dynadot 提供繁體中文與簡體中文介面，對中文用戶較為友善。

**Q：Dynadot 支援哪些付款方式？**

支援信用卡、PayPal、支付寶、銀行轉帳等多種方式。

**Q：Dynadot 支援哪些 TLD？**

支援 `.com`、`.net`、`.org`、`.io`、`.co`、`.app`、`.xyz` 等數百種，也提供部分亞洲 ccTLD（如 `.cn`、`.hk`）。

**Q：更換 Nameserver 後多久生效？**

通常 10 分鐘 ~ 48 小時，Cloudflare Dashboard 中域名狀態顯示 Active 表示完成。
