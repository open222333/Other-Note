# Linux 工具 iftop(網路流量監控工具)

```
```

## 參考資料

[iftop：Linux 即時監控網路流量工具](https://blog.gtwang.org/linux/iftop-linux-network-traffic-monitor/)

[iftop manpage](https://helpmanual.io/man8/iftop/)

# 指令

```bash
# 監控連線 流量
iftop
```

```bash
# Ubuntu Linux 安裝
apt-get install iftop

# CentOS 安裝 epel-release擴充資源庫
yum install epel-release -y

# 安裝iftop
yum install iftop -y

iftop
	# -h：顯示簡短的使用說明。
	# -n：不執行 DNS 反解，直接顯示 IP 位址。
	# -N：直接顯示埠號（port），不轉換為服務名稱。
	# -P：以 promiscuous 模式執行 iftop。
	# -i：指定網路卡（interface）。
	# -B：流量以 Bytes 為單位。
	# -F：指定要監看的 IPv4 網路區段。
	# -G：指定要監看的 IPv6 網路區段。
	# -f：指定 filter code。
```

執行 iftop 之後 快捷鍵
```
Host display：
	n：開啓或關閉 DNS 反解。
	s：顯示或隱藏來源（source）IP 位址。
	d：顯示或隱藏目的（destination）IP 位址。
	t：切換輸入輸出的流量顯示模式。

Port display：
	N：啓動或關閉埠號（port）轉換為服務名稱功能。
	S：顯示或隱藏來源埠號。
	D：顯示或隱藏目的埠號。
	p：顯示或隱藏所有埠號。

Sorting：
	1/2/3：選擇排序欄位。
	<：以流量來源 hostname 排序。
	>：以流量目的 hostname 排序。
	o：凍結目前排列狀況。

General：
	P：暫停更新資料，讓使用者慢慢看目前的圖表。
	j/k：上下捲動圖表，用於凍結排列的狀況。
	h：顯示快速鍵使用說明。
	b：顯示或隱藏流量圖。
	B：切換流量圖的顯示資料。
	T：顯示或隱藏累計流量。
	f：編輯 filter code。
	l：設定 screen filter。
	L：切換 linear 或 log 轉換。
	!：執行 shell command。
	q：離開 iftop。
```
