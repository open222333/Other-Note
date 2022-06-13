# squid(ProxyServer)

```
Proxy 提供快取網頁的功能，能提高網頁瀏覽速度。
VPN 屬於作業系統層級，連上VPN 之後，整台設備都會受影響，包含瀏覽器及其他所有連接到網際網路的應用程式，所有流量都會重新路由。
而Proxy 的作用是局部的，只適用於指定的應用程式。
```

## 參考資料

[設定文檔 參數說明](http://www.squid-cache.org/Doc/config/)

[How to set up a squid Proxy with basic username and password authentication? [closed]](https://stackoverflow.com/questions/3297196/how-to-set-up-a-squid-proxy-with-basic-username-and-password-authentication)

LOG紀錄位置 `/var/log/squid/`

```
access.log # 連線紀錄
cache.log
```

# 安裝步驟 `CentOS 7`

[CentOS 7 安裝 squid proxy server 及基本設定](https://blog.pmail.idv.tw/?p=13663)

```bash
# 安裝 EPEL 存儲庫
yum -y install epel-release
yum -y update
# 安裝
yum install squid -y

# squid命令查看可用選項
squid -h

# 查看 Squid 版本和配置選項
squid -v

# 檢查 Squid 的錯誤日誌
tail -f /var/log/squid/access.log

# 啟動服務
systemctl start squid
# 開機自動啟動
systemctl enable squid
# 查詢啟動狀態
systemctl status squid
# 重啟
systemctl restart squid
# 停止
systemctl stop squid
```

設定檔案位置`/etc/squid/squid.conf`

```conf
; 預設port : 3128

; 拒絕全部 除非在名單內 白名單
http_access deny all

; 接受全部 除非在名單內 黑名單
http_access allow all

; 允許 IP 地址通過您的代理服務器使用 Internet
acl localnet src 110.220.330.0/24

; 允許 HTTP 連接的特定端口
acl Safe_ports port 80          # http
acl Safe_ports port 21          # ftp
acl Safe_ports port 443         # https
acl Safe_ports port 70          # gopher
acl Safe_ports port 210         # wais
acl Safe_ports port 1025-65535  # unregistered ports
acl Safe_ports port 280         # http-mgmt
acl Safe_ports port 488         # gss-http
acl Safe_ports port 591         # filemaker
acl Safe_ports port 777         # multiling http
```

# 安裝步驟 `MacOS`

```bash
# 安裝
brew install squid

# 啟動
brew services start squid

# 生成用戶
htpasswd -c /usr/local/etc/squid_passwords username_you_like

# 重啟
brew services restart squid
```
配置文件 `/usr/local/etc/squid.conf`


```conf
; 驗證使用者

; 刪除
http_access allow localnet
; 添加
auth_param basic program /usr/local/Cellar/squid/4.8/libexec/basic_ncsa_auth /usr/local/etc/squid_passwords
auth_param basic realm proxy
acl authenticated proxy_auth REQUIRED
http_access allow authenticated
```

# 阻止訪問單個或一系列網站

在文檔 `/etc/squid/blocked_sites`

添加網址 網址範例:

```
liptanbiswas.com
liptan.com
```

輸入指令:

```bash
acl blocked_sites dstdomain "/etc/squid/blocked_sites"
http_access deny blocked_sites
```


```bash
# 對外開放 3128 port
firewall-cmd --zone=public --add-port=3128/tcp --permanent

# 重新讀取 firewall 設定
firewall-cmd --reload

# 測試port 是否正常 squid 是否正常使用
curl -x host_ip:port google.com

curl -x 'http://{USERNAME}:{PASSWORD}@{IP}:{PORT}' -v "http://www.google.com/"
```

# 建立使用者 對 Squid 使用基本身份驗證 `CentOS`

```bash
# 安裝httpd-tools使用htpasswd來創建加密的密碼文件。
yum -y install httpd-tools

    # htdigest 以純文本形式存儲密碼。
    # htpasswd 存儲密碼散列（各種散列算法可用）

# 創建一個新文件並為squid提供所有權訪問它
touch /etc/squid/passwd && chown squid /etc/squid/passwd
    # 默認情況下，htpasswd 使用 MD5 加密密碼


# 建立使用者帳號(執行後輸入密碼兩次)
htpasswd /etc/squid/passwd pxuser
```

設定檔案位置 `/etc/squid/squid.conf`

[How to Install and Configure Squid Proxy on CentOS 7](https://hostpresto.com/community/tutorials/how-to-install-and-configure-squid-proxy-on-centos-7/)

```conf
auth_param basic program /usr/lib64/squid/basic_ncsa_auth /etc/squid/passwd
auth_param basic children 5
auth_param basic realm Squid Basic Authentication
auth_param basic credentialsttl 2 hours
acl auth_users proxy_auth REQUIRED
http_access allow auth_users
```

```bash
# 重啟
systemctl restart squid
```

# 在 Squid 3.0 中刪除 X-Forwarded-For 標頭

```
X-Forwarded-For是用來辨識通過HTTP代理或負載均衡方式連接到Web伺服器的客戶端最原始的IP位址的HTTP頭欄位。
```

[Remove X-Forwarded-For header in Squid 3.0](https://serverfault.com/questions/102668/remove-x-forwarded-for-header-in-squid-3-0)

設定檔案位置 `/etc/squid/squid.conf`

```conf
# Hide client ip #
forwarded_for delete

# Turn off via header #
via off

# Deny request for original source of a request
follow_x_forwarded_for deny all

# See below
request_header_access X-Forwarded-For deny all
```