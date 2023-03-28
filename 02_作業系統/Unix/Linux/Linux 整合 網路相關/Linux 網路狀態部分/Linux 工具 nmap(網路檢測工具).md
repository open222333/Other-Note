# Linux 工具 nmap(網路檢測工具)

```
Nmap 是一個開放原始碼的網路掃描與探測工具，可以讓網路管理者掃描整個子網域或主機的連接埠等
```

## 目錄

- [Linux 工具 nmap(網路檢測工具)](#linux-工具-nmap網路檢測工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[nmap(1) - Linux man page](https://linux.die.net/man/1/nmap)

[觀察 port： netstat, nmap](https://linux.vbird.org/linux_server/centos4/0210port_limit.php#nmap)

[Nmap 網路診斷工具基本使用技巧與教學](https://blog.gtwang.org/linux/nmap-command-examples-tutorials/)

# 安裝

```bash
```

# 指令

```bash
nmap [掃瞄類型] [掃瞄參數] [hosts 位址與範圍]
	參數：
	[掃瞄類型]：主要的掃瞄類型有底下幾種：
		-sT：掃瞄 TCP 封包已建立的連線 connect()
		-sS：掃瞄 TCP 封包帶有 SYN 標籤的資料
		-sP：以 ping 的方式進行掃瞄
		-sU：以 UDP 的封包格式進行掃瞄
		-sO：以 IP 的協定 ( protocol ) 進行主機的掃瞄
	[掃瞄參數]：主要的掃瞄參數有幾種：
		-PT：使用 TCP 裡頭的 ping 的方式來進行掃瞄，可以獲知目前有幾部電腦存活(較常用)
		-PI：使用實際的 ping (帶有 ICMP 封包的) 來進行掃瞄
		-p ：這個是 port range ，例如 1024-, 80-1023, 30000-60000 等等的使用方式
	[Hosts 位址與範圍]：
		192.168.0.100  ：直接寫入 HOST IP 而已，僅檢查一部；
		192.168.0.0/24 ：為 C Class 的型態，
		192.168.*.*　　：變為 B Class 的型態了 掃瞄的範圍變廣了
		192.168.0.0-50,60-100,103,200 ：這種是變形的主機範圍
```
