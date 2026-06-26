# NetWork 筆記()

```
```

## 目錄

- [NetWork 筆記()](#network-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [Internet Protocol(IP 網際網路協定)](#internet-protocolip-網際網路協定)
- [Internet Protocol(IP 網際網路協定)](#internet-protocolip-網際網路協定-1)
  - [網路 IPv4](#網路-ipv4)
  - [網路 IPv6](#網路-ipv6)
- [狀況](#狀況)
  - [CDN 出現 50x 錯誤（如 500、502、503、504 等）](#cdn-出現-50x-錯誤如-500502503504-等)
  - [ERR\_CONNECTION\_RESET](#err_connection_reset)
  - [DNS\_PROBE\_FINISHED\_NXDOMAIN](#dns_probe_finished_nxdomain)
  - [網路檢測程序](#網路檢測程序)

## 參考資料

### Internet Protocol(IP 網際網路協定)

[保留IP位址](https://zh.wikipedia.org/zh-tw/%E4%BF%9D%E7%95%99IP%E5%9C%B0%E5%9D%80)

# Internet Protocol(IP 網際網路協定)

## 網路 IPv4

## 網路 IPv6

[Wiki](https://zh.wikipedia.org/wiki/IPv6#IPv6%E6%A0%BC%E5%BC%8F)

RFC 4193
```
::1/128
	是一種單播繞回位址。如果一個應用程式將封包送到此位址，IPv6堆疊會轉送這些封包繞回到同樣的虛擬介面（相當於IPv4中的127.0.0.1/8）。
fe80::/10
	這些鏈路本地位址指明，這些位址只在區域連線中是合法的，這有點類似於IPv4中的169.254.0.0/16。

fc00::/7
	唯一區域位址（ULA，unique local address）只可用於本地通訊，類似於IPv4的專用網路位址10.0.0.0/8、172.16.0.0/12和192.168.0.0/16。這定義在RFC 4193
```

# 狀況

## CDN 出現 50x 錯誤（如 500、502、503、504 等）

CDN 出現 50x 錯誤（如 500、502、503、504 等）通常表示伺服器在處理請求時發生了錯誤，可能涉及後端伺服器、CDN 設定或網絡連接問題。以下是常見錯誤及解決方法：

常見 50x 錯誤與解釋

500 Internal Server Error

原因：後端伺服器發生內部錯誤，未能處理請求。

解決：
檢查後端伺服器日誌（如 Nginx、Apache、應用程式日誌）。
確保應用程式程式碼無錯誤（如未捕捉的例外）。
檢查伺服器資源（CPU、RAM）是否耗盡。

502 Bad Gateway

原因：CDN 無法從後端伺服器獲取有效響應，可能是網關配置或後端服務器問題。
解決：
確認後端伺服器運行正常（如應用程式是否啟動、伺服器健康檢查）。
檢查 CDN 與後端伺服器的連接是否穩定（如防火牆、網絡超時）。
檢查代理伺服器（如 Nginx）的配置。
503 Service Unavailable

原因：後端伺服器無法處理請求，可能是過載或維護中。
解決：
確保伺服器負載在合理範圍內（檢查 CPU、內存、連接數）。
增加伺服器數量或擴展資源（如使用負載均衡）。
檢查後端應用程式是否處於維護模式。
504 Gateway Timeout

原因：CDN 請求後端伺服器時超時。
解決：
檢查後端伺服器響應時間是否過長，優化應用程式性能。
調整 CDN 的超時配置（如 Cloudflare 的 Timeout 設定）。
確保後端伺服器的網絡連接穩定。

## ERR_CONNECTION_RESET

瀏覽器與伺服器之間的 TCP 連線被強制中斷，通常是本機網路堆疊或 DNS 問題。

**Windows**

```powershell
# 重置 Winsock（網路堆疊）
netsh winsock reset

# 重置 TCP/IP 堆疊
netsh int ip reset

# 清除 DNS 快取
ipconfig /flushdns
```

> 執行後需重新開機。

**macOS**

```bash
# 清除 DNS 快取
sudo dscacheutil -flushcache; sudo killall -HUP mDNSResponder
```

其他排查步驟：
- **優先檢查瀏覽器 Proxy / VPN 擴充功能**（如 SwitchyOmega）是否攔截流量
- 停用瀏覽器擴充功能或以無痕模式測試
- 關閉 VPN / Proxy
- 換用其他 DNS（如 8.8.8.8 或 1.1.1.1）
- 確認防火牆未封鎖連線

診斷指令：

```bash
# 確認目標服務是否可達（能回傳任何 HTTP 狀態碼即代表網路正常）
curl -s -o /dev/null -w "%{http_code}\n" https://claude.ai

# 確認路由是否完整
traceroute -m 20 api.anthropic.com   # macOS / Linux
tracert api.anthropic.com            # Windows
```

## DNS_PROBE_FINISHED_NXDOMAIN

DNS 查詢回傳「此域名不存在」，瀏覽器無法解析域名為 IP。

**常見原因**

| 原因 | 說明 |
|---|---|
| DNS 快取過期 / 汙染 | 本機快取的舊紀錄已失效 |
| DNS 伺服器異常 | ISP 提供的 DNS 服務故障 |
| 域名確實不存在 | 打錯網址或域名已過期 |
| hosts 檔案設定錯誤 | 本機 hosts 覆蓋了正確解析 |

**Windows**

```powershell
# 清除 DNS 快取
ipconfig /flushdns

# 更換 DNS 伺服器（控制台 > 網路介面卡 > IPv4 內容）
# 慣用 DNS：8.8.8.8（Google）或 1.1.1.1（Cloudflare）

# 手動測試 DNS 解析
nslookup claude.ai
```

**macOS**

```bash
# 清除 DNS 快取
sudo dscacheutil -flushcache; sudo killall -HUP mDNSResponder

# 手動測試 DNS 解析
nslookup claude.ai

# 檢查 hosts 檔案是否有異常設定
cat /etc/hosts
```

**Linux**

```bash
# systemd-resolved
sudo systemd-resolve --flush-caches

# 或 nscd
sudo systemctl restart nscd

# 測試解析
dig claude.ai
```

## 網路檢測程序

需要檢查的通常有三個部份﹕

* 使用者
* 硬體/網線
* 軟體

參考的步驟﹕

1. 首先檢查的是和使用者之間的溝通﹐看看有沒有彼此誤會的情況出現。
2. 建立一份問題清單﹐例如﹕
	* 在出現問題之前﹐使用者做了什麼﹖
	* 當使用嘗試連接網路的時候﹐發生什麼樣的狀況﹖
	* 使用者有否正確登錄網路﹖
3. 請使用者
   * 在 Novell 的 DOS 下面用“whoami”試試﹔
   * 在 Microsoft 網路可以試試“net diag /s”﹔
   * 在 TCP/IP 網路則可以試試“ping”這些指令﹐然後詢問他們看到的結果。
4. 檢查硬體
5. 檢查問題是否機器本身的問題﹕
	* 問題的狀況是全網路的還是僅本機如此﹖
	* 找一台確定工作良好的機器來連接試試。
	* 將機器搬到確定沒問題的連線試試。
6. 如果問題確定是來自網路後﹕
   * 用SNMP(Simple Network Management Protocol)等手段去找到哪一個區段有問題。
7. 網路硬體的指示燈是否工作正常
8. 伺服器的問題﹐哪台伺服器
9. 工作站的問題﹕
	* 檢查 hub
	* 檢查網線
	* 檢查接頭
	* 檢查工作站記憶體
	* 檢查 IRQ﹑DMA﹑I/O﹑memory 等資源狀況
	* 檢查網卡之媒體類別
	* 運用跟網路卡一起來的工具程式
10. 檢查軟體
	* 先將一些駐留程式(TSR)設備驅動程式卸載(unload)﹐然後重新開機試試看。
	* 將一些非必要的設備暫時關閉(disable)再重新開機。
	* 如果程式依然有問題﹐將網路設備也給關閉看看。

---

# 例外狀況

## ERR_CONNECTION_RESET：瀏覽器 Proxy 擴充功能攔截

**現象：** 網路其他頁面正常，但特定網站（如 claude.ai）出現 ERR_CONNECTION_RESET。curl 測試可正常連線，traceroute 路由完整，清除 DNS 快取無效。

**根本原因：** 瀏覽器安裝了 Proxy / VPN 擴充功能（如 SwitchyOmega），在應用層攔截特定域名流量，導致 TCP 連線被強制中斷。網路層完全正常，問題發生在瀏覽器擴充功能層。

**診斷方式：**

```bash
# 確認網路層正常（能回傳任何 HTTP 狀態碼即可）
curl -s -o /dev/null -w "%{http_code}\n" https://claude.ai

# 確認路由完整
traceroute -m 20 api.anthropic.com   # macOS / Linux
tracert api.anthropic.com            # Windows

# 若 curl 正常但瀏覽器仍 ERR_CONNECTION_RESET → 懷疑擴充功能
```

**解決：** 開啟 `chrome://extensions/` → 停用 Proxy / VPN 類擴充功能 → 重試。

**排錯優先順序（避免走彎路）：**

1. curl 確認基本連線
2. **優先停用瀏覽器 Proxy / VPN 擴充功能**
3. 無痕模式測試（擴充功能預設停用）
4. 清除 DNS 快取
5. 最後才考慮 Winsock reset / 路由器重啟
