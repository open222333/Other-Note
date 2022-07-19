# L2TP(第二層隧道協定)

```
第二層隧道協定（英語：Layer Two Tunneling Protocol，縮寫為L2TP）是一種虛擬隧道協定，通常用於虛擬私人網路。
L2TP協定自身不提供加密與可靠性驗證的功能，可以和安全協定搭配使用，從而實現資料的加密傳輸。
經常與L2TP協定搭配的加密協定是IPsec，當這兩個協定搭配使用時，通常合稱L2TP/IPsec。
```

## 目錄

- [L2TP(第二層隧道協定)](#l2tp第二層隧道協定)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝步驟 `CentOS`](#安裝步驟-centos)
- [strongSwan(IPsec功能)](#strongswanipsec功能)
- [Libreswan(IPsec功能)](#libreswanipsec功能)

## 參考資料

[第二層隧道協定](https://zh.wikipedia.org/zh-tw/%E7%AC%AC%E4%BA%8C%E5%B1%82%E9%9A%A7%E9%81%93%E5%8D%8F%E8%AE%AE)

# 安裝步驟 `CentOS`

```bash
# 確認主機是否支持pptd 通過回傳yes
modprobe ppp-compress-18 && echo yes

# 有的虛擬機主機需要開啟，返回結果為
# cat: /dev/net/tun: File descriptor in bad state。就表示通過
cat /dev/net/tun

# 安裝EPEL源
yum install -y epel-release

# 安裝必要套件
yum install strongswan xl2tpd -y

# 安裝IPsec 擇一
yum install strongswan -y

# 防火牆的設定 範例
firewall-cmd --permanent --add-service=ipsec
firewall-cmd --permanent --direct --add-rule ipv4 filter FORWARD 0 -s 10.10.3.0/24 -m policy --dir in --pol ipsec -j ACCEPT
firewall-cmd --permanent --direct --add-rule ipv4 filter FORWARD 0 -d 10.10.3.0/24 -m policy --dir out --pol ipsec -j ACCEPT
firewall-cmd --permanent --direct --add-rule ipv4 nat POSTROUTING 0 -s 10.10.3.0/24 -o ens3 -j MASQUERADE
firewall-cmd --permanent --direct --add-rule ipv6 filter FORWARD 0 -s fd12:3456:789a:1::/64 -m policy --dir in --pol ipsec -j ACCEPT
firewall-cmd --permanent --direct --add-rule ipv6 filter FORWARD 0 -d fd12:3456:789a:1::/64 -m policy --dir out --pol ipsec -j ACCEPT
firewall-cmd --permanent --direct --add-rule ipv6 nat POSTROUTING 0 -s fd12:3456:789a:1::/64 -o ens3 -j MASQUERADE
firewall-cmd --reload

```

# strongSwan(IPsec功能)

```
strongSwan，一個跨平台開放原始碼項目，在Linux平台上實作了IPsec功能。
```

設定檔 `/etc/strongswan/ipsec.conf`

```conf
config setup

conn %default
    keyexchange=ikev2
    ike=aes256-sha256-modp2048,aes256-sha1-modp2048,3des-sha1-modp2048! # 可自行更換加密套件，但要注意用戶端是否支援
    esp=aes256-sha256,aes256-sha1,3des-sha1!
    dpdaction=clear
    dpddelay=300s
    # win7 setting
    rekey=no

conn vpn
    left=%any
    leftsubnet=0.0.0.0/0,::/0
    leftauth=pubkey
    leftcert=server.crt.pem
    leftsendcert=always
    leftid=@vpn.example.com  #連線的vpn url / ip, 必須完全符合.
    right=%any
    rightsourceip=10.10.3.0/24,fd12:3456:789a:1::/64
    rightauth=eap-mschapv2
    eap_identity=%any
    auto=add
```

設定檔 `/etc/strongswan/strongswan.conf`

```conf
charon {
        load_modular = yes
        plugins {
                attr {
                        # 在這邊加DNS
                        dns = 8.8.8.8, 8.8.4.4, 2001:4860:4860::8888, 2001:4860:4860::8844
                }
                include strongswan.d/charon/*.conf
        }
}
include strongswan.d/*.conf

# /etc/strongswan/ipsec.secrets
# /etc/ipsec.secrets - strongSwan IPsec secrets file

# 這個跟ipsec.conf裏頭的server.crt.pem是成對的keypair
: RSA  server.key.pem
# 這是用來登入的密碼
username : EAP "password"
```

```
產生憑證，
這邊會用strongswan內建的pki來處理，
這邊要注意的是產生server憑證時，
一定要有ikeIntermediate的EKU，
並且將主機的CN name加進SAN名單裏頭，這樣跨平台才不容易有問題
```

```bash
# 產生CA
strongswan pki --gen --type rsa --size 2048 --outform pem > vpnca.key.pem
strongswan pki --self --flag serverAuth --in vpnca.key.pem --type rsa --digest sha256 --dn "C=TW, O=Organization, CN=128.199.112.215" --ca > vpnca.crt.der

# 產生server憑證
strongswan pki --gen --type rsa --size 2048 --outform pem > do-basic.key.pem
strongswan pki --pub --in do-basic.key.pem --type rsa > do-basic.csr
strongswan pki --issue --cacert vpnca.crt.der --cakey vpnca.key.pem --digest sha256 --dn "C=TW, O=Organization, CN=128.199.112.215" --san "128.199.112.215" --flag serverAuth --flag ikeIntermediate --outform pem < do-basic.csr > do-basic.crt.pem

# 複製過去
cp vpnca.crt.der /etc/strongswan/ipsec.d/cacerts/
cp do-basic.crt.pem /etc/strongswan/ipsec.d/certs/server.crt.pem
cp do-basic.key.pem /etc/strongswan/ipsec.d/private/server.key.pem

# 保護private key
chmod 600 /etc/strongswan/ipsec.d/private/server.key.pem

# 開機啟動
systemctl enable strongswan

# 現在啟動
systemctl start strongswan
```

# Libreswan(IPsec功能)

```
Libreswan，一個開放原始碼軟體專案，在Linux平台上實作IPsec功能。
```

[Run your own VPN with Libreswan](https://www.redhat.com/sysadmin/run-your-own-vpn-libreswan)