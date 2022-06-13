# PPTP(點對點隧道協定)

```
點對點隧道協定（英語：Point to Point Tunneling Protocol，縮寫為PPTP）是實現虛擬私人網路（VPN）的方式之一。
```

## 參考資料

[點對點隧道協定](https://zh.wikipedia.org/zh-tw/%E9%BB%9E%E5%B0%8D%E9%BB%9E%E9%9A%A7%E9%81%93%E5%8D%94%E8%AD%B0)

[CentOS 7 安裝VPN Server 和 Client](https://www.796t.com/content/1548169205.html)

# 安裝步驟 `CentOS 7 `

## 安裝VPN Server

```bash
# 安裝ppp和pptpd
yum -y install ppp pptpd
```

配置文檔 `/etc/pptpd.conf`

```conf
localip 192.168.9.1
remoteip 172.168.9.101-200
#Localip這行是給vpn伺服器設定一個隧道ip
#Remoteip是自動分配給客戶端的ip地址範圍。
```

配置文檔 `/etc/ppp/options.pptpd`

```
#先去掉debug前面的#，開啟日誌記錄
ms-dns  202.96.209.5
ms-dns  8.8.8.8
logfile /var/log/pptpd.log
```

配置帳密 `/etc/ppp/chap-secrets`

```
#client為帳號，server是pptpd服務，secret是密碼，*表示是分配任意的ip
#Secrets for authentication using CHAP
#client     server     secret          IP addresses
 vpn        pptpd       vpn             *
```

配置文檔 `/etc/sysctl.conf`

```
#新增一行    net.ipv4.ip_forward = 1
#到末尾即可，然後儲存,這個很重要,系統路由模式功能。
sysctl -p
#執行這個命令會輸出上面新增的那一行資訊，意思是使核心修改生效
```

```bash
# 設置轉發規則
firewall-cmd --permanent --direct --passthrough ipv4 -t nat -I POSTROUTING -o eth1 -j MASQUERADE -s 192.168.9.0/24

# 在centos 7 中，用firewalld 來取代了iptables
firewall-cmd --zone=public --add-port=1723/tcp --permanent
firewall-cmd --zone=public --add-port=47/tcp --permanent
firewall-cmd --permanent --direct --add-rule ipv4 filter INPUT 0 -p gre -j ACCEPT
firewall-cmd --permanent --direct --add-rule ipv6 filter INPUT 0 -p gre -j ACCEPT
firewall-cmd --reload

# 系統自動啟動pptp的vpn server
systemctl start pptpd
systemctl enable pptpd
```

## 安裝VPN Client

```bash
# 安裝ppp pptp pptp-setup
yum install ppp pptp pptp-setup

# 建立VPN連線
pptpsetup --create vpn --server 10.46.89.192 --username vpn --password vpn --start

# 載入模組
modprobe ppp_mppe

# 連線VPN連線
pppd call vpn  //這裡的vpn是上面建立vpn連線
```