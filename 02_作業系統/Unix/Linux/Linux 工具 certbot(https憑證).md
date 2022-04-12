# linux 工具 certbot(https憑證)

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
		單純取得憑證
	-c, --config CONFIG_FILE
		path to config file (default: /etc/letsencrypt/cli.ini and ~/.config/letsencrypt/cli.ini)
	-d DOMAINS
		Comma-separated list of domains to obtain a certificate for
	--dns-cloudflare
		Obtain certificates using a DNS TXT record (if you are using Cloudflare for DNS). (default: False)
	--no-autorenew
		Disable auto renewal of certificates. (default: True)
```