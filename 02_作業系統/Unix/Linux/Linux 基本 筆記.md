# Linux 基本 筆記

## 目錄

- [Linux 基本 筆記](#linux-基本-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [各檔案夾的結構說明及用途介紹](#各檔案夾的結構說明及用途介紹)
- [linux 設定檔位置](#linux-設定檔位置)
- [linux 基本 軟體組合 LAMP](#linux-基本-軟體組合-lamp)

## 參考資料

[指令查詢](https://ss64.com/bash/)

[指令查詢](https://helpmanual.io/)

[指令查詢](https://www.commandlinux.com/)

[Linux 命令大全](https://www.runoob.com/linux/linux-command-manual.html)

# 各檔案夾的結構說明及用途介紹

```
/bin：二進位制可執行命令。
/dev：裝置特殊檔案。
/etc：系統管理和配置檔案。
/etc/rc.d：啟動的配 置檔案和指令碼。
/home：使用者主目錄的基點，比如使用者user的主目錄就是/home/user，可以用~user表示。
/lib：標準程式設計庫，又 叫動態連結共享庫，作用類似windows裡的.dll檔案。
/sbin：系統管理命令，這 裡存放的是系統管理員使用的管理程式。
/tmp：公用的臨時檔案儲存 點。
/root：系統管理員的主目 錄。
/mnt：系統提供這個目錄是 讓使用者臨時掛載其他的檔案系統。
/lost+found：這個 目錄平時是空的，系統非正常關機而留下“無家可歸”的檔案就在這裡。
/proc：虛擬的目錄，是系 統記憶體的對映。可直接訪問這個目錄來獲取系統資訊。
/var：某些大檔案的溢位 區，比方說各種服務的日誌檔案。
/usr：最龐大的目錄，要用 到的應用程式和檔案幾乎都在這個目錄。其中包含：
/usr/x11r6：存放x window的目錄。
/usr/bin：眾多的應用程式。
/usr/sbin：超級使用者的一些管理程式。
/usr/doc：linux文件。
/usr/include：linux下開發和編譯應用程式所需要的標頭檔案。
/usr/lib：常用的動態連結庫和軟體包的配置檔案。
/usr/man：幫助文件。
/usr/src：原始碼，linux核心的原始碼就放在/usr/src/linux 裡。
/usr/local/bin：本地增加的命令。
/usr/local/lib：本地增加的庫根檔案系統。
```

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