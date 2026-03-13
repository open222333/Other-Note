# Linux 基本 網路相關

## 目錄

- [Linux 基本 網路相關](#linux-基本-網路相關)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [ssh遠端設定](#ssh遠端設定)
- [要確定對方主機某一個埠 TCP port 是否有開啟](#要確定對方主機某一個埠-tcp-port-是否有開啟)
- [IPv6](#ipv6)
- [使用 curl 測試連接](#使用-curl-測試連接)
- [檢測端口](#檢測端口)
- [檢查防火牆和安全組設置](#檢查防火牆和安全組設置)
- [檢查網絡路徑](#檢查網絡路徑)
- [檢查 DNS 設置](#檢查-dns-設置)

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

# IPv6

`確保操作系統的網絡設置正確支持 IPv6`

如果該文件的值為 0，表示 IPv6 被啟用。如果是 1，你可能需要啟用 IPv6

```bash
cat /proc/sys/net/ipv6/conf/all/disable_ipv6
```

# 使用 curl 測試連接

顯示 HTTP 請求的詳細過程，包括請求標頭、響應標頭及任何錯誤訊息。

```sh
curl -v https://example.com/path
```

# 檢測端口

```sh
telnet domain.com 443
```

# 檢查防火牆和安全組設置

如果系統有防火牆或安全組設置（如 AWS 或其他雲服務），請確保這些設置允許連接

```sh
iptables -L -n
```

# 檢查網絡路徑

顯示每一跳的延遲和主機，可以檢查是否有任何跳的延遲或超時

```sh
traceroute example.com
```

# 檢查 DNS 設置

DNS 解析

使用 dig 命令檢查 DNS 是否正確 確認返回的 IP 地址是否正確。

```sh
dig example.com
```