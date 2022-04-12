# Linux 基本 筆記

## 參考資料

[指令查詢](https://ss64.com/bash/)

[指令查詢](https://helpmanual.io/)

[指令查詢](https://www.commandlinux.com/)

[Linux 命令大全](https://www.runoob.com/linux/linux-command-manual.html)

# linux 設定檔位置

hosts設定檔：手動設定主機名稱與IP位址
`/etc/hosts`
(https://blog.gtwang.org/windows/windows-linux-hosts-file-configuration/)


# linux 基本 軟體組合 LAMP

https://zh.wikipedia.org/wiki/LAMP

```
LAMP是指一組通常一起使用來執行動態網站或者伺服器的自由軟體名稱首字母縮寫：

	Linux，作業系統
	Apache，網頁伺服器
	MariaDB或MySQL，資料庫管理系統（或者資料庫伺服器）
	PHP、Perl或Python，手稿語言
```

```bash
# 安裝必要軟體
yum install httpd mysql mysql-server php php-mysql

/etc/hosts $hostname

#網頁存放位置
/var/www/html

# php 設定檔
/etc/php.ini

# 啟動
/etc/init.d/httpd start

# 測試設定檔語法
/etc/init.d/httpd configtest

# 開機啟動
chkconfig httpd on

#啟動apache
/usr/sbin/apachectl start

# 查看port
netstat -tulnp | grep 'http'

# 列出所有的區域
firewall-cmd --get-zones

# 開啟 tcp 的 8080 連接埠
firewall-cmd --zone=public --add-port=8080/tcp

# 永久開啟 tcp 的 8080 連接埠
firewall-cmd --zone=public --permanent --add-port=8080/tcp
```