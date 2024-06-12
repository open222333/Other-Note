# Linux 工具 telnet(檢測連線-端口)

```
檢測某服務器的端口是否正常對外服務

診斷各種伺服器與網路連線問題。
```

## 目錄

- [Linux 工具 telnet(檢測連線-端口)](#linux-工具-telnet檢測連線-端口)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [RedHat (CentOS)](#redhat-centos)
  - [Homebrew (MacOS)](#homebrew-macos)
- [指令](#指令)

## 參考資料

[telnet(1) - Linux man page](https://linux.die.net/man/1/telnet)

[Linux telnet命令](https://www.runoob.com/linux/linux-comm-telnet.html)

# 安裝

## Debian (Ubuntu)

```bash
apt update
apt install telnet
```

## RedHat (CentOS)

```bash
yum install telnet -y
```

## Homebrew (MacOS)

```bash
brew install telnet
```

# 指令

```bash
telnet ip port
telnet 192.168.x.x 1111

telnet [- 8acdEfFKLrx ][- b <主機別名>][- e <脫離字符>][- k <域名>][- l <用戶名稱>][- n <記錄文件>][- S <服務類型>][- X <認證形態>][主機名稱或IP地址<通信端口>]

	-8 允許使用8位字符資料，包括輸入與輸出。
	-a 嘗試自動登入遠端系統。
	-b<主機別名> 使用別名指定遠端主機名稱。
	-c 不讀取用戶專屬目錄裡的.telnetrc文件。
	-d 啟動排錯模式。
	-e<脫離字符> 設置脫離字符。
	-E 濾除脫離字符。
	-f 此參數的效果和指定"-F"參數相同。
	-F 使用Kerberos V5認證時，加上此參數可把本地主機的認證數據上傳到遠端主機。
	-k<域名> 使用Kerberos認證時，加上此參數讓遠端主機採用指定的領域名，而非該主機的域名。
	-K 不自動登入遠端主機。
	-l<用戶名稱> 指定要登入遠端主機的用戶名稱。
	-L 允許輸出8位字符資料。
	-n<記錄文件> 指定文件記錄相關信息。
	-r 使用類似rlogin指令的用戶界面。
	-S<服務類型> 設置telnet連線所需的IP TOS信息。
	-x 假設主機有支持數據加密的功能，就使用它。
	-X<認證形態> 關閉指定的認證形態。
```
