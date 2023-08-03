# Linux 基本 網路相關

## 目錄

- [Linux 基本 網路相關](#linux-基本-網路相關)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [ssh遠端設定](#ssh遠端設定)
- [要確定對方主機某一個埠 TCP port 是否有開啟](#要確定對方主機某一個埠-tcp-port-是否有開啟)

## 參考資料

[RHEL / CentOS 8 重新啟動網路](https://www.opencli.com/linux/rhel-centos8-restart-network)

[CentOS 官方](https://wiki.centos.org/FrontPage)

[CentOS SELinux](https://wiki.centos.org/HowTos/SELinux)

## ssh遠端設定

[How to Enable SSH on CentOS 8](https://linuxhint.com/enable_ssh_centos8/)

[Linux：修改ssh設定、root登入、更改port、密碼登入](https://www.ewdna.com/2012/05/linuxsshrootport.html)

[CentOS 7 文字模式下更改為固定 IP](https://ithelp.ithome.com.tw/articles/10214435)

# 要確定對方主機某一個埠 TCP port 是否有開啟

[Linux – Ping TCP port?](http://benjr.tw/91897)

```bash
# PaPing:http://code.google.com/p/paping
paping www.google.com -p 80 -c 4

# nc (或是 netcat)
nc -vz google.com 80

hping google.com -S -V -p 80

nmap -p 80 google.com
# 其他參數使用請參考 http://benjr.tw/28087

# Telnet
telnet www.google.com 23
```