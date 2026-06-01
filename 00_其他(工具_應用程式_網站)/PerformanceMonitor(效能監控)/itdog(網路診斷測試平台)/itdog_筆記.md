# itdog（網路診斷測試平台）

```
https://www.itdog.cn
```

## 目錄

- [itdog（網路診斷測試平台）](#itdog網路診斷測試平台)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [說明](#說明)
- [功能](#功能)
  - [Ping 測試](#ping-測試)
  - [TCPing 測試](#tcping-測試)
  - [HTTP 速度測試](#http-速度測試)
  - [Traceroute 路由追蹤](#traceroute-路由追蹤)
  - [MTR 測試](#mtr-測試)
  - [DNS 查詢](#dns-查詢)
  - [批次測試](#批次測試)
- [使用情境](#使用情境)

## 參考資料

[itdog — 官方網站](https://www.itdog.cn)

[itdog HTTP Speed Test — Firefox 擴充套件](https://addons.mozilla.org/en-US/firefox/addon/itdog-http-speed-test/)

---

# 說明

itdog 是中國常用的線上網路診斷平台，無需安裝任何軟體，直接透過瀏覽器使用。

提供從中國各地區、各運營商（電信、聯通、移動）節點發起的多點測試，可快速定位延遲高、丟包、路由繞路等網路問題。

支援 IPv4 / IPv6 雙協議。

---

# 功能

## Ping 測試

從中國多個地區與運營商節點同時發起 ICMP Ping，顯示：

- 各節點延遲（ms）
- 丟包率（%）
- 地區 / 運營商分布圖

```
https://www.itdog.cn/ping/
```

## TCPing 測試

基於 TCP 協議的連通性測試，適用於目標主機**封鎖 ICMP** 的情況（如部分 CDN、防火牆後端）。

```
https://www.itdog.cn/tcping/
```

> 常用於確認特定 Port（如 80、443、22）是否可達。

## HTTP 速度測試

模擬多節點發出 HTTP/HTTPS 請求，深度分析：

| 指標 | 說明 |
|---|---|
| DNS 解析時間 | 域名解析耗時 |
| TCP 連線時間 | 三次握手耗時 |
| SSL 握手時間 | TLS 協商耗時 |
| 首位元組時間（TTFB） | 伺服器回應速度 |
| 總下載時間 | 完整回應耗時 |
| HTTP 狀態碼 | 200 / 301 / 403 / 502 … |
| Response Header | 回應標頭資訊 |

```
https://www.itdog.cn/http/
```

## Traceroute 路由追蹤

從多節點追蹤封包路由路徑，可視覺化顯示每一跳（hop）的延遲與地理位置，幫助分析繞路問題。

```
https://www.itdog.cn/traceroute/
```

## MTR 測試

結合 Ping 與 Traceroute，持續監測路由路徑上每個節點的延遲與丟包，比單次 Traceroute 更精確。

```
https://www.itdog.cn/mtr/
```

## DNS 查詢

支援 8 種 DNS 記錄類型的查詢：

| 記錄類型 | 說明 |
|---|---|
| A | IPv4 地址 |
| AAAA | IPv6 地址 |
| CNAME | 別名記錄 |
| MX | 郵件伺服器 |
| TXT | 文字記錄（SPF、驗證等） |
| NS | 名稱伺服器 |
| PTR | 反向解析 |
| SRV | 服務記錄 |

```
https://www.itdog.cn/dns/
```

## 批次測試

支援一次輸入多個域名 / IP，批次執行 Ping、TCPing、HTTP 測試，適合快速篩查多台主機狀況。

```
https://www.itdog.cn/batch_ping/
https://www.itdog.cn/batch_tcping/
https://www.itdog.cn/batch_http/
```

---

# 使用情境

| 情境 | 建議功能 |
|---|---|
| 確認網站在中國各地是否可達 | Ping / TCPing |
| 排查網站速度慢的原因 | HTTP 速度測試（觀察 TTFB） |
| 確認 CDN 是否正常分流 | HTTP 速度測試 + DNS 查詢 |
| 定位封包路由繞路問題 | Traceroute / MTR |
| 確認防火牆是否封鎖特定 Port | TCPing（指定 Port） |
| 檢查 DNS 記錄是否正確生效 | DNS 查詢 |
| 批次確認多台伺服器存活狀態 | 批次 Ping / 批次 TCPing |
