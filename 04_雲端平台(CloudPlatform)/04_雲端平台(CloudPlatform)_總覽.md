# 04_雲端平台(CloudPlatform)_總覽

```
本文彙整主流雲端主機（VPS / IaaS）平台的月費、維護影響與適用對象，協助依需求快速選擇。
各平台詳細操作請見個別筆記。
```

## 目錄

- [04\_雲端平台(CloudPlatform)\_總覽](#04_雲端平台cloudplatform_總覽)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [平台清單](#平台清單)
- [價位與維護影響比較](#價位與維護影響比較)
  - [維護影響評分說明](#維護影響評分說明)
- [需求選擇建議](#需求選擇建議)

## 參考資料

- [Tencent(騰訊) 筆記](./Tencent(騰訊)/Tencent(騰訊)_筆記.md)
- [Linode 筆記](./Linode/Linode_筆記.md)
- [AWS(Amazon_Web_Services) 筆記](./AWS(Amazon_Web_Services)/AWS_筆記.md)
- [GCP(Google_Cloud_Platform) 筆記](./GCP(Google_Cloud_Platform)/GCP(Google_Cloud_Platform)_筆記.md)
- [Baidu(百度) 筆記](./Baidu(百度)/Baidu_筆記.md)

# 平台清單

| 平台 | 類型 | 主要用途 | 筆記 |
|---|---|---|---|
| Tencent（騰訊雲） | 綜合雲端平台 | 中國大陸／東南亞服務 | [連結](./Tencent(騰訊)/Tencent(騰訊)_筆記.md) |
| Linode（Akamai Cloud） | VPS | 新手、穩定運行 | [連結](./Linode/Linode_筆記.md) |
| AWS（含 Lightsail） | 綜合雲端平台 | 小型網站、API、企業服務 | [連結](./AWS(Amazon_Web_Services)/AWS_筆記.md) |
| GCP（Google Compute Engine） | 綜合雲端平台 | 24 小時服務、企業、Live Migration | [連結](./GCP(Google_Cloud_Platform)/GCP(Google_Cloud_Platform)_筆記.md) |
| Baidu（百度雲） | 綜合雲端平台 | 中國大陸服務 | [連結](./Baidu(百度)/Baidu_筆記.md) |
| Hetzner Cloud | VPS | 高 CP 值、自架服務 | 尚無獨立筆記 |
| Vultr Cloud Compute | VPS | 網站、Docker、遊戲 | 尚無獨立筆記 |
| DigitalOcean | VPS | 開發、測試環境 | 尚無獨立筆記 |
| Azure Virtual Machine | 綜合雲端平台 | Microsoft 生態 | 尚無獨立筆記 |

# 價位與維護影響比較

> 價格為概略區間，實際以各平台當下報價為準。

| 雲端主機 | 最低月費 | 約新台幣 | 維護影響 | 適合對象 |
|---|---:|---:|---|---|
| Hetzner Cloud | €4.5–6 | NT$170–230 | ★★★☆☆（偶爾需重開機） | 高 CP 值、自架服務 |
| Vultr Cloud Compute | US$5 | NT$160 | ★★★☆☆ | 網站、Docker、遊戲 |
| Linode | US$5 | NT$160 | ★★★☆☆ | 新手、穩定運行 |
| DigitalOcean | US$4 | NT$130 | ★★★☆☆ | 開發、測試環境 |
| AWS Lightsail | US$5 起 | NT$160 起 | ★★★★☆ | 小型網站、API |
| Google Compute Engine | US$6–8 起 | NT$190–260 起 | ★★★★★（支援 Live Migration） | 24 小時服務、企業 |
| Azure Virtual Machine | US$6–10 起 | NT$190–320 起 | ★★★★☆ | Microsoft 生態 |

## 維護影響評分說明

| 評分 | 說明 |
|---|---|
| ★★★★★ | 大部分維護無須停機（Live Migration） |
| ★★★★☆ | 極少停機，維護影響低 |
| ★★★☆☆ | 偶爾需要重新開機 |
| ★★☆☆☆ | 維護停機較頻繁 |

# 需求選擇建議

| 需求 | 推薦平台 |
|---|---|
| 最低成本、自架服務 | Hetzner Cloud |
| 新手、穩定入門 VPS | Linode / DigitalOcean |
| Docker / 遊戲主機 | Vultr Cloud Compute |
| 小型網站、簡易 API | AWS Lightsail |
| 24 小時關鍵服務、企業級、不能停機 | Google Compute Engine |
| 既有 Microsoft 生態整合 | Azure Virtual Machine |
| 中國大陸／東南亞在地服務 | Tencent 騰訊雲 / Baidu 百度雲 |
