# linux 工具 certbot(https憑證)

```
```

## 目錄

- [linux 工具 certbot(https憑證)](#linux-工具-certbothttps憑證)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [建立憑證 使用 certbot 作業系統 CentOS](#建立憑證-使用-certbot-作業系統-centos)
- [指令](#指令)
- [配置文件](#配置文件)

## 參考資料

[certbot官方網站](https://certbot.eff.org/)

[certbot參考文檔](https://eff-certbot.readthedocs.io/en/stable/)

[certbot指令](https://eff-certbot.readthedocs.io/en/stable/using.html#certbot-command-line-options)

[解析 Certbot（Let's encrypt） 使用方式](https://andyyou.github.io/2019/04/13/how-to-use-certbot/)

# 建立憑證 使用 certbot 作業系統 CentOS

[安裝snapd](https://snapcraft.io/docs/installing-snap-on-centos)

```bash
# 安裝EPEL
	# CentOS 8
	dnf install epel-release
	dnf upgrade

	# CentOS 7
	yum install epel-release

# 安裝 snapd(軟體部署和軟體套件管理系統)
yum install snapd

# 啟用
systemctl enable --now snapd.socket

# 建立連結
ln -s /var/lib/snapd/snap /snap

# 確認snapd版本為最新
snap install core
snap refresh core

# 刪除certbot(若有在之前使用其他軟體套件管理系統安裝過certbot)
apt-get remove certbot
dnf remove certbot
yum remove certbot

###################################
# 安裝 letsencrypt 的 certbot 套件 (for nginx)
# certbot 為 epel-release 套件庫所提供
yum install epel-release -y
yum install certbot python2-certbot-nginx -y
yum install python2-certbot-dns-cloudflare -y
```

# 指令

```bash
# 可以註冊沒有電子郵件地址的帳戶 信箱(用於寄信通知用)
certbot --register-unsafely-without-email

# 生成憑證
certbot -c cli.ini certonly --dns-cloudflare --no-autorenew -d test.com -d *.test.com
	certonly
		單純取得憑證 獲取證書但不安裝
	-c, --config CONFIG_FILE
		path to config file (default: /etc/letsencrypt/cli.ini and ~/.config/letsencrypt/cli.ini)
	-d DOMAINS
		Comma-separated list of domains to obtain a certificate for
	--dns-cloudflare
		Obtain certificates using a DNS TXT record (if you are using Cloudflare for DNS). (default: False)
	--no-autorenew
		Disable auto renewal of certificates. (default: True)

# 測試單次執行刷新憑證
certbot renew -c cli-setting.ini --dns-cloudflare --cert-name %domain% --dry-run
	-c CONFIG_FILE, --config CONFIG_FILE
		配置文件的路徑（默認：/etc/letsencrypt/cli.ini 和 ~/.config/letsencrypt/cli.ini)
	--dry-run
		讓 certbot 不要真的替換憑證
	--dns-cloudflare
		使用 DNS TXT 記錄獲取證書（如果您是 使用 Cloudflare 進行 DNS）
	--force-renew

	--no-autorenew

	--cert-name {domain}

# 測試檢查當前機器的憑證
certbot certificates
	'''
	這將返回以下格式的信息：
	Found the following certificates:
	Certificate Name: example.com
		Domains: example.com, www.example.com
		Expiry Date: 2017-02-19 19:53:00+00:00 (VALID: 30 days)
		Certificate Path: /etc/letsencrypt/live/example.com/fullchain.pem
		Key Type: RSA
		Private Key Path: /etc/letsencrypt/live/example.com/privkey.pem
	'''
```

# 配置文件

```ini
# This is an example of the kind of things you can do in a configuration file.
# All flags used by the client can be configured here. Run Certbot with
# "--help" to learn more about the available options.
#
# Note that these options apply automatically to all use of Certbot for
# obtaining or renewing certificates, so options specific to a single
# certificate on a system with several certificates should not be placed
# here.

# Use ECC for the private key
key-type = ecdsa
elliptic-curve = secp384r1

# Use a 4096 bit RSA key instead of 2048
rsa-key-size = 4096

# Uncomment and update to register with the specified e-mail address
# email = foo@example.com

# Uncomment to use the standalone authenticator on port 443
# authenticator = standalone

# Uncomment to use the webroot authenticator. Replace webroot-path with the
# path to the public_html / webroot folder being served by your web server.
# authenticator = webroot
# webroot-path = /usr/share/nginx/html

# Uncomment to automatically agree to the terms of service of the ACME server
# agree-tos = true

# An example of using an alternate ACME server that uses EAB credentials
# server = https://acme.sectigo.com/v2/InCommonRSAOV
# eab-kid = somestringofstuffwithoutquotes
# eab-hmac-key = yaddayaddahexhexnotquoted
```