# DMARC(基於域名的郵件認證協議)

```
Domain-based Message Authentication, Reporting & Conformance

基於域名的郵件認證協議，用於幫助郵件接收者確定如何處理未通過 SPF（Sender Policy Framework）或 DKIM（DomainKeys Identified Mail）認證的郵件。

DMARC 的目的是防止電子郵件詐騙、網絡釣魚和電子郵件欺騙。

認證：

    DMARC 依賴於 SPF 和 DKIM 這兩個郵件認證技術來驗證郵件的真實性。
    如果郵件同時通過 SPF 和 DKIM 的認證，則認為該郵件來自合法發件人。

報告：

    DMARC 提供報告功能，發件人域名的所有者可以接收到有關未通過 SPF 或 DKIM 認證的郵件的信息。
    報告分為兩類：
    Aggregate reports（綜合報告）：提供有關郵件流量的整體視圖。
    Forensic reports（詳細報告）：提供具體未通過認證郵件的詳細信息。

一致性：

    DMARC 規定了接收郵件伺服器應該如何處理未通過認證的郵件，可以選擇拒絕、隔離（quarantine）或無特別處理（none）。
```

## 目錄

- [DMARC(基於域名的郵件認證協議)](#dmarc基於域名的郵件認證協議)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [設置](#設置)

## 參考資料

[什麼是 DNS DMARC 記錄？](https://www.cloudflare.com/zh-tw/learning/dns/dns-records/dns-dmarc-record/)

[什麼是 DNS DKIM 記錄？](https://www.cloudflare.com/zh-tw/learning/dns/dns-records/dns-dkim-record/)

[什麼是 DNS SPF 記錄？](https://www.cloudflare.com/zh-tw/learning/dns/dns-records/dns-spf-record/)

# 設置

`配置 SPF 記錄`：

確保域名已有 SPF 記錄，用於定義哪些伺服器有權代表該域名發送郵件。

```
example.com.  IN  TXT  "v=spf1 include:_spf.google.com ~all"
```

`配置 DKIM`：

確保郵件伺服器配置了 DKIM 簽名，以便接收伺服器可以驗證郵件的完整性和發件人。

`配置 DMARC 記錄`：

在域名的 DNS 中添加 DMARC 記錄，配置策略和報告接收地址。

```
_dmarc.example.com.  IN  TXT  "v=DMARC1; p=reject; rua=mailto:dmarc-reports@example.com; ruf=mailto:dmarc-reports@example.com; fo=1"
```

DMARC 記錄解析

v=DMARC1：指定版本為 DMARC1。

p=reject：指定策略為拒絕未通過認證的郵件。可以設置為 none（不處理）、quarantine（隔離）或 reject（拒絕）。

rua=mailto:dmarc-reports@example.com：指定接收綜合報告的電子郵件地址。

ruf=mailto:dmarc-reports@example.com：指定接收詳細報告的電子郵件地址。

fo=1：指定詳細報告的選項，1 代表所有失敗郵件都要報告。
