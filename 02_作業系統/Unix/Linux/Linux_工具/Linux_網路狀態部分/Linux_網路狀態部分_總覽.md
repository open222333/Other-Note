# Linux 網路狀態部分 總覽

```
本目錄收錄 Linux 網路診斷與監控工具：連線測試、DNS 查詢、網卡與路由、連線狀態、流量監控。
```

## 目錄

- [Linux 網路狀態部分 總覽](#linux-網路狀態部分-總覽)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [工具清單](#工具清單)
- [類型比較](#類型比較)
  - [舊工具與新工具對照](#舊工具與新工具對照)
- [各工具說明](#各工具說明)
  - [連線測試](#連線測試)
  - [DNS 查詢](#dns-查詢)
  - [網卡與路由](#網卡與路由)
  - [連線狀態與流量](#連線狀態與流量)
- [需求選擇建議](#需求選擇建議)

## 參考資料

- [curl](./Linux_工具_curl(對指定URL進行網路傳輸).md)
- [dig](./Linux_工具_dig(DNS偵錯工具).md)
- [ethtool](./Linux_工具_ethtool(網路卡狀態及設定).md)
- [ifconfig](./Linux_工具_ifconfig(查詢和設定網路介面卡).md)
- [iftop](./Linux_工具_iftop(網路流量監控工具).md)
- [ip route](./Linux_工具_ip-route(顯示和管理_Linux_系統的_IP_路由表).md)
- [iptraf](./Linux_工具_iptraf(網路流量監測).md)
- [mtr](./Linux_工具_mtr(網絡診斷工具).md)
- [netstat](./Linux_工具_netstat(顯示當前的網路狀態).md)
- [nmap](./Linux_工具_nmap(網路檢測工具).md)
- [nslookup](./Linux_工具_nslookup(DNS偵錯工具).md)
- [ping](./Linux_工具_ping(測試連線).md)
- [sar](./Linux_工具_sar(監控Linux).md)
- [tcpdump](./Linux_工具_tcpdump(擷取通過網路介面的封包).md)
- [telnet](./Linux_工具_telnet(檢測連線-端口).md)
- [watch](./Linux_工具_watch(觀看執行結果).md)

---

# 工具清單

| 工具 | 類型 | 主要用途 | 筆記 |
|---|---|---|---|
| **ping** | 連線測試 | 測試主機是否可達（ICMP） | [ping](./Linux_工具_ping(測試連線).md) |
| **telnet** | 連線測試 | 測試指定 port 是否有服務 | [telnet](./Linux_工具_telnet(檢測連線-端口).md) |
| **curl** | 連線測試 / HTTP | 發 HTTP 請求、量測回應時間、查對外 IP | [curl](./Linux_工具_curl(對指定URL進行網路傳輸).md) |
| **mtr** | 路徑診斷 | ping + traceroute，找出哪一跳掉包或延遲 | [mtr](./Linux_工具_mtr(網絡診斷工具).md) |
| **nmap** | 掃描 | 掃描主機或網段開放的 port | [nmap](./Linux_工具_nmap(網路檢測工具).md) |
| **dig** | DNS | 查詢 A / CNAME / TXT / NS 等記錄 | [dig](./Linux_工具_dig(DNS偵錯工具).md) |
| **nslookup** | DNS | 互動式 DNS 正反向查詢 | [nslookup](./Linux_工具_nslookup(DNS偵錯工具).md) |
| **ifconfig** | 網卡 | 查詢與設定網路介面 | [ifconfig](./Linux_工具_ifconfig(查詢和設定網路介面卡).md) |
| **ip route** | 路由 | 查看與修改路由表 | [ip route](./Linux_工具_ip-route(顯示和管理_Linux_系統的_IP_路由表).md) |
| **ethtool** | 網卡 | 網卡速率、雙工、驅動參數 | [ethtool](./Linux_工具_ethtool(網路卡狀態及設定).md) |
| **netstat** | 連線狀態 | 列出監聽 port、連線數、對應程式 | [netstat](./Linux_工具_netstat(顯示當前的網路狀態).md) |
| **iftop** | 流量監控 | 即時看各連線的頻寬用量 | [iftop](./Linux_工具_iftop(網路流量監控工具).md) |
| **iptraf** | 流量監控 | 各連線的協定與封包統計（TUI） | [iptraf](./Linux_工具_iptraf(網路流量監測).md) |
| **tcpdump** | 封包擷取 | 擷取、過濾、存檔封包 | [tcpdump](./Linux_工具_tcpdump(擷取通過網路介面的封包).md) |
| **sar** | 系統監控 | 歷史的 CPU / 記憶體 / 磁碟 / 網路統計 | [sar](./Linux_工具_sar(監控Linux).md) |
| **watch** | 輔助 | 定時重跑指令觀察變化（常搭配 netstat） | [watch](./Linux_工具_watch(觀看執行結果).md) |

---

# 類型比較

| 想看的層次 | 工具 | 備註 |
|---|---|---|
| 主機可不可達（L3） | ping、mtr | 對方擋 ICMP 時 ping 不通不代表服務掛了 |
| port 有沒有開（L4） | telnet、nmap、curl | telnet / curl 測單一 port，nmap 掃多個 |
| 應用層回應（L7） | curl | 可看 HTTP 狀態碼、各階段耗時 |
| 名稱解析 | dig、nslookup | 排查 DNS 時優先用 dig（輸出完整） |
| 本機網卡 / 路由 | ifconfig、ip route、ethtool | |
| 本機連線 / 頻寬 | netstat、iftop、iptraf、tcpdump、sar | 由粗到細：netstat → iftop → tcpdump |

## 舊工具與新工具對照

`ifconfig`、`netstat`、`route` 屬於 net-tools 套件，新版發行版多半預設不裝，改用 iproute2：

| net-tools（舊） | iproute2（新） |
|---|---|
| `ifconfig` | `ip addr` / `ip link` |
| `route` | `ip route` |
| `netstat -tlnp` | `ss -tlnp` |

---

# 各工具說明

## 連線測試

- **ping**：最先做的檢查；不通時再用 mtr 看斷在哪一跳。
- **mtr**：持續送封包統計每一跳的掉包率與延遲，適合回報給機房或雲端商。
- **telnet / curl**：確認服務 port 有沒有在聽；curl 還能看 HTTP 回應與耗時。
- **nmap**：一次掃多個 port 或整個網段；只掃自己管理的主機。

## DNS 查詢

- **dig**：腳本與排錯首選，可指定 DNS 伺服器（`dig @8.8.8.8 example.com`）。
- **nslookup**：互動式、Windows 也有，適合快速查一筆。

## 網卡與路由

- **ifconfig / ip**：看 IP、網卡狀態。
- **ip route**：多網卡、VPN、內網路由問題。
- **ethtool**：懷疑網卡協商成 100M / 半雙工時檢查。

## 連線狀態與流量

- **netstat**：哪個程式佔了 port、某 port 有多少連線。
- **iftop / iptraf**：頻寬被誰吃掉。
- **tcpdump**：需要看封包內容時，可存成 pcap 再用 Wireshark 開。
- **sar**：看過去一段時間的資源趨勢（需 sysstat 收集資料）。
- **watch**：搭配上述指令即時觀察變化。

---

# 需求選擇建議

| 情境 | 推薦工具 | 理由 |
|---|---|---|
| 網站連不上 | ping → dig → curl | 依序確認主機、DNS、HTTP |
| 連得上但很慢 | mtr、curl `-w` | 找出延遲在網路路徑還是伺服器回應 |
| 確認服務 port 有開 | telnet、curl、nmap | 單一 port 用 telnet / curl，多個用 nmap |
| DNS 設定後確認生效 | dig | 可指定公共 DNS 比對結果 |
| 查哪個程式佔了 port | netstat、ss | `-tlnp` 顯示 PID |
| 頻寬異常 | iftop → tcpdump | 先找連線，再看封包內容 |
| 事後回顧資源用量 | sar | 有歷史資料 |
