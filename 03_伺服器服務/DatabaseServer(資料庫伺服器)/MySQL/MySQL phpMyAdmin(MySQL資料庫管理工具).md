# MySQL phpMyAdmin(MySQL資料庫管理工具)

```
phpMyAdmin 是一個以PHP為基礎，以Web-Base方式架構在網站主機上的MySQL的資料庫管理工具
```

## 參考資料

[官方網站](https://www.phpmyadmin.net/)

[官方文檔](https://docs.phpmyadmin.net/zh_CN/latest/)

# 安裝

## `CentOS 7`

```bash
# 安裝需要先加入 EPEL Repository
yum install epel-release -y

# 安裝 phpMyAdmin
yum -y install phpmyadmin
```

設定檔位置 `/etc/httpd/conf.d/phpMyAdmin.conf`
