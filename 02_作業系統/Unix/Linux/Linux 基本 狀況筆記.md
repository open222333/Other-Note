# Linux 基本 狀況筆記

## 目錄

- [Linux 基本 狀況筆記](#linux-基本-狀況筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [網站連線數問題](#網站連線數問題)
- [root 密碼忘記](#root-密碼忘記)
- [查看port佔用](#查看port佔用)
- [linux shell nologin 不可登入的系統帳號](#linux-shell-nologin-不可登入的系統帳號)
- [linux PID查看以及刪除](#linux-pid查看以及刪除)
- [檢查服務狀態](#檢查服務狀態)

## 參考資料

[linux 命令搜尋](https://wangchujiang.com/linux-command/)


# 網站連線數問題

[/etc/security/limits.conf 詳解與配置](https://www.itread01.com/content/1575187502.html)

# root 密碼忘記

```
重新開機進入單人維護模式 系統會主動的給予 root 權限的 bash 介面
```

# 查看port佔用

[CentOS7 查看 Port 占用情況](http://weng-weiling.blogspot.com/2017/05/centos7-port.html)

```
如果提示端口已經被佔用:Address already in use
```

1. netstat 指令

```bash
# 查看目前使用的port
netstat -tulpn
	-t 代表找走 TCP 協定的
	-u 代表找走 UDP 協定的
	-l 代表找 LISTEN 的 socket
	-n 代表顯示硬體名稱，-p 代表顯示 PID。

netstat -tln # 可以看到有哪些 IP:Port 開啟
netstat -tulpn | grep LISTEN  # 查詢 port 80 的語法:
netstat -tulpn | grep :80
```

2. lsof 指令

```bash
# RHEL 及 CentOS 預設沒有安裝 lsof, 執行以下指令用 yum 安裝
yum install lsof -y

# 要查看某一個 port 是否被佔用, 在 grep 後面加上 port 即可:
lsof -i -P -n | grep LISTEN
lsof -i -P -n | grep :80
```

3. nmap 指令

```bash
# RHEL 及 CentOS 預設沒有安裝 nmap, 執行以下指令用 yum 安裝:
yum install nmap -y

nmap -sT -O localhost
# 輸出類似這樣的結果:

	# Nmap scan report for localhost (127.0.0.1)
	# Host is up (0.00012s latency).
	# Other addresses for localhost (not scanned): 127.0.0.1
	# Not shown: 999 closed ports
	# PORT STATE SERVICE
	# 3325/tcp   open  smtp
	# 3380/tcp   open  http
	# 1111/tcp  open  rpcbind
	# 3389/tcp  open  ldap
	# 3443/tcp  open  https
	# 3631/tcp  open  ipp
	# 33306/tcp open  mysql
	# 33389/tcp open  ms-wbt-server
```

# linux shell nologin 不可登入的系統帳號

```
/sbin/nologin 特殊的 shell
```

# linux PID查看以及刪除

```bash
# 查看PID
ps U username -f

# 刪除PID
kill PID
```

# 檢查服務狀態

```
ps aux | grep smb

netstat -tunlp
```