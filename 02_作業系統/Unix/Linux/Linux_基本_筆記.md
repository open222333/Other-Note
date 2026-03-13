# Linux 基本 筆記

```
在 Linux 系統中，命令的名稱後面的數字通常是代表該命令的說明文件的章節。shutdown(8) 中的數字 8 是代表該命令所屬的章節。

在 Linux 系統中，說明文件被分為幾個章節，每個章節專門描述特定類型的內容。以下是一些常見的章節以及它們的內容類型：

第 1 章：一般的使用者命令和工具。
第 2 章：系統調用（System calls）和函式庫函數。
第 3 章：C 程序庫函數。
第 4 章：設備和設備驅動程式。
第 5 章：檔案格式和協議。
第 6 章：遊戲和應用程式。
第 7 章：雜項信息，例如文件、檔案格式等。
第 8 章：系統管理命令和工具。
```

## 目錄

- [Linux 基本 筆記](#linux-基本-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [各檔案夾的結構說明及用途介紹](#各檔案夾的結構說明及用途介紹)
- [手冊頁面（manual pages）](#手冊頁面manual-pages)
  - [分節（sections）](#分節sections)
- [指令](#指令)
  - [常用指令 用於取得系統資訊和狀態](#常用指令-用於取得系統資訊和狀態)
- [軟體組合 LAMP](#軟體組合-lamp)
- [dep 和 rpm 是兩種主要的軟體套件管理系統](#dep-和-rpm-是兩種主要的軟體套件管理系統)

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

# 手冊頁面（manual pages）

## 分節（sections）

在 Linux 的 man 頁面中，常見的分節（sections）及其對應的意思如下：

User Commands: 用戶命令，包括終端中可以直接執行的命令，如 mkdir、ls、cp 等。

System Calls: 系統調用，用於程序中通過函數調用操作系統內核的功能，如 mkdir(2)、read(2)、write(2) 等。

Library Functions: 函數庫函數，通常是 C 語言庫中的函數，用於開發程序時使用，如 printf(3)、malloc(3)、memcpy(3) 等。

Special Files: 特殊文件，包括設備文件、設置文件等，如 /dev/null、/etc/passwd 等。

File Formats and Conventions: 文件格式和慣例，介紹文件格式、配置文件格式等，如 passwd(5)、fstab(5) 等。

Games: 遊戲，包括一些 Linux 系統中的遊戲或遊戲相關信息。

Miscellaneous: 雜項，包括各種雜項信息，如 ascii(7)、regex(7) 等。

System Administration tools and Deamons: 系統管理工具和守護進程，介紹系統管理相關的工具和守護進程，如 cron(8)、systemd(8) 等。

這些分節對於查找和理解不同類型的信息非常有幫助。例如，如果想查找特定的系統調用的用法和參數，就可以查看 man 2 mkdir；如果想查找特定的函數庫函數的用法，就可以查看 man 3 printf 等。

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

# dep 和 rpm 是兩種主要的軟體套件管理系統

```
dep: .deb 是 Debian 系統專用的套件格式。這種格式的包主要適用於基於 Debian 的系統（例如 Ubuntu、Linux Mint）。
rpm: .rpm 是 Red Hat 系統專用的套件格式，適用於基於 Red Hat 的系統（例如 CentOS、Fedora、RHEL）。
```
