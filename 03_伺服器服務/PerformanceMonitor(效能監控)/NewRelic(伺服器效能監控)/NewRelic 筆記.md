# NewRelic 筆記

```
提供企業加速雲端創新必備的APM監控工具。
New Relic的可觀測性分析機制及APM解決方案使應用程式及系統效能監控變得更容易。
```

## 參考資料

[官網](https://newrelic.com/)

[官方文檔](https://docs.newrelic.com/)


# PHP代理安裝：Ubuntu和Debian

[PHP agent installation: Ubuntu and Debian](https://docs.newrelic.com/docs/apm/agents/php-agent/installation/php-agent-installation-ubuntu-debian/)

```bash
# 安裝 newrelic-repo-5-3.noarch 擴展庫
sudo rpm -Uvh http://yum.newrelic.com/pub/newrelic/el5/x86_64/newrelic-repo-5-3.noarch.rpm
	# -h或--hash 套件安裝時列出標記。
	# -U<套件檔>或--upgrade<套件檔> 升級指定的套件檔。
	# -v 顯示指令執行過程。

# New Relic 的 PHP 代理的包名是newrelic-php5. 儘管包名引用了 PHP 5，但該包適用於所有受支持的 PHP 版本。
sudo yum install newrelic-php5 -y

# 上方指令出現
# Please run newrelic-install as root to complete installation.
# 非標準安裝：手動完成安裝。(需輸入 New Relic license key)
sudo newrelic-install install

# 找出 設定檔位置
php -i | grep newrelic.ini

# 編輯
vim /etc/opt/remi/php74/php.d/newrelic.ini
# newrelic.license = license_key
# app_name

# 起動
systemctl enable newrelic-daemon
```