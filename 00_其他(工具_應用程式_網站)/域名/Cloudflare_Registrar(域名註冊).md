# Cloudflare Registrar(域名註冊)

```
```

## 目錄

- [Cloudflare Registrar(域名註冊)](#cloudflare-registrar域名註冊)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [說明](#說明)
- [在 Cloudflare 購買域名](#在-cloudflare-購買域名)
  - [步驟一：搜尋並購買域名](#步驟一搜尋並購買域名)
  - [步驟二：確認 DNS 已自動託管](#步驟二確認-dns-已自動託管)
  - [步驟三：設定 DNS 記錄](#步驟三設定-dns-記錄)
    - [常見 DNS 記錄設定](#常見-dns-記錄設定)
- [從其他域名商轉移至 Cloudflare Registrar](#從其他域名商轉移至-cloudflare-registrar)
  - [轉移前準備](#轉移前準備)
  - [轉移步驟](#轉移步驟)
- [Cloudflare Proxy（橘色雲）說明](#cloudflare-proxy橘色雲說明)
- [確認設定生效](#確認設定生效)
- [DNSSEC 設定](#dnssec-設定)
- [常見問題](#常見問題)

## 參考資料

[Cloudflare Registrar 官網](https://www.cloudflare.com/products/registrar/)

[Cloudflare Dashboard](https://dash.cloudflare.com/)

[Cloudflare Registrar 說明文檔](https://developers.cloudflare.com/registrar/)

[Cloudflare DNS 記錄說明](https://developers.cloudflare.com/dns/manage-dns-records/how-to/create-dns-records/)

[域名轉移至 Cloudflare](https://developers.cloudflare.com/registrar/get-started/transfer-domain-to-cloudflare/)

# 說明

Cloudflare Registrar 是 Cloudflare 提供的域名註冊服務，主要優勢是**以成本價（at-cost）銷售域名**，不收取任何加價。

**主要特點：**
- 域名以批發成本價銷售，無額外加價（無首年優惠陷阱）
- 購買後 DNS 自動由 Cloudflare 管理，無需另外設定 Nameserver
- 免費 WHOIS 隱私保護
- 整合 Cloudflare CDN、DDoS 防護、SSL
- 支援 DNSSEC

**限制：**
- 不支援部分 ccTLD（如 `.tw`、`.io` 等，支援清單持續更新）
- 不提供域名首次購買優惠（長期持有比 GoDaddy 首年優惠後便宜）
- 需先有 Cloudflare 帳號

**網站：** https://www.cloudflare.com/products/registrar/

# 在 Cloudflare 購買域名

> 在 Cloudflare 直接購買域名後，DNS 會**自動託管在 Cloudflare**，不需要額外設定 Nameserver，這是與其他域名商最大的不同。

## 步驟一：搜尋並購買域名

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 左側選單點擊 **「Domain Registration」→「Register Domains」**
3. 搜尋想要的域名（如 `example.com`）
4. 選擇可用域名並點擊 **「Purchase」**
5. 填寫帳單資訊並完成付款

## 步驟二：確認 DNS 已自動託管

購買完成後，域名會自動出現在 Cloudflare Dashboard：

**Websites → 選擇域名 → DNS → Records**

無需設定 Nameserver，Cloudflare 已自動接管。

## 步驟三：設定 DNS 記錄

在 **DNS → Records** 新增所需記錄：

### 常見 DNS 記錄設定

```
類型：A
名稱：@          ← 代表根域名 example.com
內容：1.2.3.4    ← 你的主機 IP
TTL：Auto
Proxy：🟠 橘色雲 (Proxied)
```

```
類型：A
名稱：www
內容：1.2.3.4
TTL：Auto
Proxy：🟠 橘色雲 (Proxied)
```

```
類型：MX
名稱：@
內容：mail.example.com
Priority：10
TTL：Auto
Proxy：⬜ DNS Only（MX 必須）
```

# 從其他域名商轉移至 Cloudflare Registrar

## 轉移前準備

1. **確認域名已滿 60 天**（新購買的域名無法立即轉移，ICANN 規定）
2. **解除 Transfer Lock（Domain Lock）**：在原域名商後台關閉「鎖定轉移」功能
3. **關閉 WHOIS 隱私保護**（部分域名商需要，轉移完成後可重新開啟）
4. **取得 Auth Code（EPP Code / Transfer Code）**：在原域名商後台申請

## 轉移步驟

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 左側選單點擊 **「Domain Registration」→「Transfer Domains」**
3. 輸入要轉移的域名
4. 輸入從原域名商取得的 **Auth Code**
5. 確認 DNS 記錄（Cloudflare 會自動掃描）
6. 完成付款（轉移需支付一年續費費用，以成本價計算）
7. **至原域名商信箱確認轉移申請**（通常需要點擊確認信件）

> **轉移時間**：通常需要 **5 ~ 7 個工作天**。轉移期間域名正常運作，不影響網站。

# Cloudflare Proxy（橘色雲）說明

| 狀態 | 圖示 | 說明 |
|------|------|------|
| Proxied（代理） | 🟠 橘色雲 | 流量經過 Cloudflare，享有 CDN、防護、隱藏真實 IP |
| DNS Only | ⬜ 灰色雲 | 僅 DNS 解析，流量直接到主機 |

**建議啟用 Proxy（橘色雲）的情況：**
- Web 網站（HTTP/HTTPS）
- 需要 DDoS 防護或 CDN 加速

**必須使用 DNS Only（灰色雲）的情況：**
- MX 記錄（郵件）
- SSH、FTP、非 HTTP 服務

# 確認設定生效

```bash
# 查詢域名 A 記錄
dig A example.com

# 查詢 Nameserver（應顯示 Cloudflare NS）
dig NS example.com

# 指定 DNS 查詢
dig @8.8.8.8 A example.com
```

# DNSSEC 設定

DNSSEC（DNS Security Extensions）為 DNS 回應加上數位簽章，防止 DNS 快取污染與中間人攻擊，確保解析結果未被竄改。

Cloudflare Registrar 的 DNSSEC 設定是所有域名商中**最簡單的**：因為域名商與 DNS 提供商都是 Cloudflare，無需跨平台複製 DS 記錄，一鍵即可完成完整鏈設定。

## 啟用 DNSSEC（一鍵完成）

1. 登入 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 選擇域名 → **DNS → Settings**
3. 找到 **「DNSSEC」** 區塊 → 點擊 **「Enable DNSSEC」**
4. 完成

Cloudflare 會自動：
- 產生 DNSKEY 記錄（Zone 簽署）
- 將 DS 記錄上傳至 TLD（因為 Cloudflare 同時是 Registrar）
- 維護後續金鑰輪換（Key Rollover）

> **對比其他域名商**：使用 GoDaddy / Namecheap 等搭配 Cloudflare DNS 時，需手動複製 DS 記錄並貼到另一個平台。Cloudflare Registrar 完全自動化，這是其最大優勢之一。

## 驗證 DNSSEC 是否生效

```bash
# 查詢 DS 記錄（啟用後約 10 ~ 30 分鐘可查到）
dig DS example.com

# 查詢 DNSKEY 記錄
dig DNSKEY example.com

# 驗證完整信任鏈
dig +dnssec A example.com
```

線上驗證工具：
```
https://dnssec-analyzer.verisignlabs.com/
https://dnsviz.net/
```

# 常見問題

**Q：Cloudflare Registrar 的價格比 GoDaddy 便宜嗎？**

首年通常比 GoDaddy 促銷價貴，但續費比 GoDaddy 便宜很多。GoDaddy 首年優惠後，續費價格通常大幅提高。Cloudflare 每年以相同的成本價計費。

**Q：Cloudflare Registrar 支援哪些 TLD？**

主要支援 `.com`、`.net`、`.org`、`.io`、`.co` 等常見 TLD。部分 ccTLD（如 `.tw`）可能不支援，購買前請在 Cloudflare 搜尋確認。

**Q：在 Cloudflare 購買後還需要設定 Nameserver 嗎？**

不需要。在 Cloudflare Registrar 購買的域名，DNS 自動由 Cloudflare 管理，可直接在 Dashboard 新增 DNS 記錄。

**Q：可以把域名從 Cloudflare 轉出去嗎？**

可以。在 Cloudflare Dashboard → Domain Registration → 選擇域名 → Transfer Out，取得 Auth Code 後即可轉移到其他域名商。

**Q：DNSSEC 如何啟用？**

Cloudflare Dashboard → 選擇域名 → DNS → Settings → DNSSEC → 開啟。Cloudflare Registrar 購買的域名可一鍵啟用，自動設定所有必要記錄。
