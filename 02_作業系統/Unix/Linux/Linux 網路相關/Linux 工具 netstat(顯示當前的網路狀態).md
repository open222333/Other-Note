# Linux 工具 netstat(顯示當前的網路狀態)

```
netstat是一個基於命令列介面的網路實用工具，可顯示當前的網路狀態，包括傳輸控制協定層的連線狀況、路由表、網路介面狀態和網路協定的統計資訊等。
```

## 目錄

- [Linux 工具 netstat(顯示當前的網路狀態)](#linux-工具-netstat顯示當前的網路狀態)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[netstat manpage](https://man7.org/linux/man-pages/man8/netstat.8.html)

[netstat 基本用法](https://blog.gtwang.org/linux/linux-netstat-command-examples/)

# 指令

```bash
# 安裝
yum install -y net-tools
apt install -y net-tools

# 查看目前使用中的port 以及使用的程式
netstat -tulpn
	-t 代表找走 TCP 協定的
	-u 代表找走 UDP 協定的
	-l 代表找 LISTEN 的 socket
	-n 代表顯示硬體名稱，-p 代表顯示 PID。

# 基本查看 All + Numeric
netstat -antp

# 過濾出80 Port
netstat -an | grep :80

# 計算80 Port總連線數
netstat -na | grep 80 | wc -l

# 計算每一個 ip 在主機上建立的連線數量:
netstat -ntu | awk '{print $5}' | cut -d: -f1 | sort | uniq -c | sort -n

# 列出每個 ip 建立的 ESTABLISHED 連線數量:
netstat -ntu | grep ESTAB | awk '{print $5}' | cut -d: -f1 | sort | uniq -c | sort -nr

# 列出每個 ip 建立的 port 80 連線數量:
netstat -plan|grep :80|awk {'print $5'}|cut -d: -f 1|sort|uniq -c|sort -nk 1

# 列出所有連接埠，包含 listening 與 non listening
netstat -a

# 僅列出 TCP 的連接埠
netstat -at

# 僅列出 UDP 的連接埠
netstat -au

# 列出所有 listening 狀態的連接埠
netstat -l

# 列出所有 listening 狀態的 TCP 連接埠
netstat -lt

# 列出所有 listening 狀態的 UDP 連接埠
netstat -lu

# 列出各種協定的統計數據
netstat -s

# 列出 TCP 的統計數據
netstat -st

# 列出 UDP 的統計數據
netstat -su

# 如果要顯示應詳細的統計數據，可以再加上 -w 參數
netstat -sw

# 檢視哪些IP連線本機
netstat -an

# 檢視tcp連線數狀態
netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'

# 統計8080埠上有多少個TCP連線，命令：
netstat -ant |grep 80|wc -l

# TCP連線中有多少個連線狀態是ESTABLISHED，命令：
netstat -ant |grep 80|grep ESTABLISHED|wc -l

# TCP連線中有多少個連線狀態是CLOSE_WAIT
netstat -ant |grep 80|grep CLOSE_WAIT|wc -l

# TCP連線中有多少個連線狀態是TIME_WAIT
netstat -ant |grep 80|grep TIME_WAIT|wc -l

# 使用awk來完成統計資訊，命令如下
netstat -ant |grep 80|awk '{++S[$NF]} END {for (a in S) print a, S[a]}'

	# TCP連線狀態詳解：
	# 	LISTEN：      偵聽來自遠方的TCP埠的連線請求
	# 	SYN-SENT：    再傳送連線請求後等待匹配的連線請求
	# 	SYN-RECEIVED：再收到和傳送一個連線請求後等待對方對連線請求的確認
	# 	ESTABLISHED： 代表一個開啟的連線
	# 	FIN-WAIT-1：  等待遠端TCP連線中斷請求，或先前的連線中斷請求的確認
	# 	FIN-WAIT-2：  從遠端TCP等待連線中斷請求
	# 	CLOSE-WAIT：  等待從本地使用者發來的連線中斷請求
	# 	CLOSING：     等待遠端TCP對連線中斷的確認
	# 	LAST-ACK：    等待原來的發向遠端TCP的連線中斷請求的確認
	# 	TIME-WAIT：   等待足夠的時間以確保遠端TCP接收到連線中斷請求的確認
	# 	CLOSED：      沒有任何連線狀態
	# 	SYN_RECV：    表示正在等待處理的請求數
	# 	ESTABLISHED： 表示正常資料傳輸狀態
	# 	TIME_WAIT：   表示處理完畢，等待超時結束的請求數。
```

