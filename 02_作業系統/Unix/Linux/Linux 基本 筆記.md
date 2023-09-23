# Linux 基本 筆記

## 目錄

- [Linux 基本 筆記](#linux-基本-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [各檔案夾的結構說明及用途介紹](#各檔案夾的結構說明及用途介紹)
- [指令](#指令)
  - [常用指令 用於取得系統資訊和狀態](#常用指令-用於取得系統資訊和狀態)
- [軟體組合 LAMP](#軟體組合-lamp)

## 參考資料

[指令查詢](https://ss64.com/bash/)

[指令查詢](https://helpmanual.io/)

[指令查詢](https://www.commandlinux.com/)

[Linux 命令大全](https://www.runoob.com/linux/linux-command-manual.html)

[LAMP - wiki](https://zh.wikipedia.org/wiki/LAMP)

[手動設定網址與 IP 對應的 hosts 檔教學，適用 Windows、Mac OS X 與 Linux 系統](https://blog.gtwang.org/windows/windows-linux-hosts-file-configuration/)

```
LAMP是指一組通常一起使用來執行動態網站或者伺服器的自由軟體名稱首字母縮寫：

	Linux，作業系統
	Apache，網頁伺服器
	MariaDB或MySQL，資料庫管理系統（或者資料庫伺服器）
	PHP、Perl或Python，手稿語言
```

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
/etc/hosts: hosts設定檔：手動設定主機名稱與IP位址
```

# 指令

## 常用指令 用於取得系統資訊和狀態

```bash
# 查看系统信息
# uname -a：顯示系統的核心版本和其他基本信息。
uname -a

# lsb_release -a：顯示Linux發行版的信息。
lsb_release -a

# 查看系統負載和資源使用情況
# top：即時顯示系統的資源使用情況，包括CPU、記憶體、進程等。
top

# htop：類似於top，但提供更多互動式功能。
htop

# free -h：顯示系統記憶體使用情況。
free -h

# df -h：顯示磁碟空間使用情況。
df -h

# 查看網絡信息
# ifconfig或ip addr：顯示網絡接口的配置信息。
ifconfig

# netstat -tuln：顯示當前正在監聽的網絡端口。
netstat -tuln

# ss：另一個用於查看網絡連接和套接字的工具。
ss

# 查看系統日誌
# /var/log 目錄下包含了各種系統日誌文件，例如/var/log/syslog、/var/log/auth.log等。
# 可以使用cat或less命令來查看這些文件的內容。
cat /var/log/syslog
cat /var/log/auth.log

# 查看硬件信息
# lscpu：顯示CPU信息。
lscpu

# lshw：顯示硬件信息，包括CPU、記憶體、磁碟、網絡適配器等。
lshw

# 查看進程信息
# ps aux：顯示當前運行的所有進程。
ps aux

# pgrep process_name：查找特定進程的PID。
pgrep process_name

# 查看用戶信息
# who：顯示當前登錄系統的用戶。
who

# w：顯示當前登錄用戶的詳細信息。
w

# 查看系統時間
# date：顯示當前系統時間和日期。
date

# 查看啟動信息
# systemctl list-units --type=service：列出所有系統服務的狀態。
systemctl list-units --type=service
```

# 軟體組合 LAMP

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
