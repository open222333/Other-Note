# MySQL 工具 phpMyAdmin(MySQL資料庫管理工具)

```
phpMyAdmin 是一個以PHP為基礎，以Web-Base方式架構在網站主機上的MySQL的資料庫管理工具
```

## 目錄

- [MySQL 工具 phpMyAdmin(MySQL資料庫管理工具)](#mysql-工具-phpmyadminmysql資料庫管理工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [安裝相關](#安裝相關)
- [安裝](#安裝)
	- [CentOS7 phpMyAdmin安裝(Apache)](#centos7-phpmyadmin安裝apache)
	- [CentOS7 phpMyAdmin安裝(Apache)](#centos7-phpmyadmin安裝apache-1)
- [設定檔](#設定檔)
	- [單一 phpadmin 多個 mysql](#單一-phpadmin-多個-mysql)

## 參考資料

[官方網站](https://www.phpmyadmin.net/)

[官方文檔](https://docs.phpmyadmin.net/zh_CN/latest/)

### 安裝相關

[How to Install phpMyAdmin on CentOS 7](https://phoenixnap.com/kb/how-to-install-secure-phpmyadmin-on-centos-7)

[How To Install phpMyAdmin with Nginx on CentOS 7 / RHEL 7](https://www.itzgeek.com/how-tos/linux/centos-how-tos/phpmyadmin-with-nginx-on-centos-7-rhel-7.html)

# 安裝

## CentOS7 phpMyAdmin安裝(Apache)

```bash
# Step 1: Install EPEL Repository
yum install -y epel-release
yum –y update

# Step 2: Install Apache Web Server
yum install httpd -y
systemctl status httpd

# Step 3: Installing phpMyAdmin on CentOS 7
yum -y install phpmyadmin

# Step 4: Configuring and Securing phpMyAdmin
vim /etc/phpMyAdmin/config.inc.php
```


設定檔位置 `/etc/httpd/conf.d/phpMyAdmin.conf`

```conf
# Restrict IP Addresses(限制IP地址) 更改“Require IP” or “Allow IP.”

# Change Alias(別名) 默認要有
Alias /phpMyAdmin /usr/share/phpMyAdmin
Alias /phpmyadmin /usr/share/phpMyAdmin

# 默認容易成為目標 可考慮換名 可自行添加
Alias /MySecretLogin /usr/share/phpMyAdmin
```

## CentOS7 phpMyAdmin安裝(Apache)

```bash
# 需要啟用 EPEL 存儲庫來下載和安裝 phpMyAdmin
rpm -Uvh https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm複製

# temporarily enable Remi repository and install PHP support packages required for phpMyAdmin
yum install --enablerepo=remi-php73 phpmyadmin
yum -y install phpmyadmin
```

# 設定檔

## 單一 phpadmin 多個 mysql

`config.user.inc.php`

```php
<?php
$cfg['Servers'][1]['host'] = 'master';
$cfg['Servers'][1]['port'] = '3306';

$cfg['Servers'][2]['host'] = 'slave1';
$cfg['Servers'][2]['port'] = '3306';

// You can add more server configurations as needed
// $cfg['Servers'][3]...

// Set the default server to use (in this case, server 1)
$cfg['DefaultServer'] = 1;
?>
```
