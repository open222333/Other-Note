# Linux 工具 traceroute(追蹤封包傳遞路徑)

```
追蹤「網際網路通訊協定」(IP) 封包傳遞到目的地所經的路徑。
```

## 目錄

- [Linux 工具 traceroute(追蹤封包傳遞路徑)](#linux-工具-traceroute追蹤封包傳遞路徑)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[traceroute(8) - Linux man page](https://linux.die.net/man/8/traceroute)

# 安裝

```bash
yum install traceroute -y
```

# 指令

```bash
traceroute[參數][主機]
	-d 使用Socket層級的排錯功能。
	-f 設置第一個檢測數據包的存活數值TTL的大小。
	-F 設置勿離斷位。
	-g 設置來源路由網關，最多可設置8個。
	-i 使用指定的網絡界面送出數據包。
	-I 使用ICMP迴應取代UDP資料信息。
	-m 設置檢測數據包的最大存活數值TTL的大小。
	-n直接使用IP地址而非主機名稱。
	-p 設置UDP傳輸協議的通信端口。
	-r 忽略普通的Routing Table，直接將數據包送到遠端主機上。
	-s 設置本地主機送出數據包的IP地址。
	-t 設置檢測數據包的TOS數值。
	-v 詳細顯示指令的執行過程。
	-w 設置等待遠端主機回報的時間。
	-x 開啓或關閉數據包的正確性檢驗。
```
