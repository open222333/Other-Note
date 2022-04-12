# Linux 基本 網路相關

## `參考資料:`

[RHEL / CentOS 8 重新啟動網路](https://www.opencli.com/linux/rhel-centos8-restart-network)

[CentOS 官方](https://wiki.centos.org/FrontPage)

[CentOS SELinux](https://wiki.centos.org/HowTos/SELinux)

## `ssh遠端設定:`

[How to Enable SSH on CentOS 8](https://linuxhint.com/enable_ssh_centos8/)

[Linux：修改ssh設定、root登入、更改port、密碼登入](https://www.ewdna.com/2012/05/linuxsshrootport.html)

[CentOS 7 文字模式下更改為固定 IP](https://ithelp.ithome.com.tw/articles/10214435)

# CentOS

[CentOS 官方](https://wiki.centos.org/HowTos/SELinux)

```bash
### semanage(SELinux工具程式 CentOS 防火牆 功能(預設開啟)) ###

# 通過將-a選項替換-m為修改，添加tcp port 5000到http_port_t
semanage port -m -t http_port_t -p tcp 5000

# 啟動的端口加入到如上端口列表中
semanage port -a -t http_port_t -p tcp 8090

# 查看http允許訪問的端口：
semanage port -l | grep http_port_t

# SELinux 設置為寬容模式，方便調試：
sudo setenforce 0

vim /etc/selinux/config

### === 關閉 SELinux === ###
vim /etc/selinux/config

# 關閉 SELinux：
#     SELINUX=enforce
#     改成:
#     SELINUX=disabled

#	enforcing：強制模式，SELinux 執行中，依 SELinux 設定限制存取
#	permissive：寬容模式：SELinux 執行中，但僅顯示警告，而不限制存取
#	disabled：關閉，SELinux 未執行

# 將 SELINUXTYPE=targeted 註釋

# 儲存後離開編輯器, 需要重新開機設定才會生效。

# 檢查SELinux的狀態
sestatus

### CentOS 7 ###
# 重新啟動網路
systemctl restart network

# 查看網卡資訊
ifconfig

# 啟動網卡介面
ifup [ifcsg]

# 關閉網卡介面
ifdown [ifcsg]

### CentOS 8 ###
# 重新啟動 NetworkManager, 這時便會一同重新啟動網路
systemctl restart NetworkManager.

# nmcli是NetworkManager的指令工具
# 停止及開啟網路的指令:
nmcli networking off
nmcli networking on
```

# cURL curl(對指定URL進行網路傳輸)

[官方文檔 man page](https://curl.se/docs/manpage.html)

[proxy範例](http://username:password@host_ip:port)

```
基於網路協定，對指定URL進行網路傳輸。
cURL涉及是任何網路協定傳輸，不涉及對具體資料的具體處理。
```

```bash
# 查看目前對外ip
curl ipinfo.io

# 使用proxy 開啟 url
curl -x (proxy) (url)
curl -v ip:port

# 測試port是否正常
curl -x host_ip:port google.com
	# -I
	# --head
	# 	僅獲取HTTP標頭 (HTTP/FTP/文件)
	# -v
	# --verbose
	# 	取得細節 主要用於調試
```

# netstat(顯示當前的網路狀態)

[netstat manpage](https://man7.org/linux/man-pages/man8/netstat.8.html)

[netstat 基本用法](https://blog.gtwang.org/linux/linux-netstat-command-examples/)

```
netstat是一個基於命令列介面的網路實用工具，可顯示當前的網路狀態，包括傳輸控制協定層的連線狀況、路由表、網路介面狀態和網路協定的統計資訊等。
```

```bash
# 安裝
yum install -y net-tools
apt install -y net-tools

# 基本查看 All + Numeric
netstat -antp

# 過濾出80 Port
netstat -an | grep :80

# 計算80 Port總連線數
netstat -na | grep 80 | wc -l

# 計算每一個 ip 在主機上建立的連線數量:
netstat -ntu | awk '{print $5}' | cut -d: -f1 | sort | uniq -c | sort -n

# 列出每個 ip 建立的 ESTABLISHED 連線數量:
netstat -ntu | grep ESTAB | awk '{print $5}' | cut -d: -f1 | sort | uniq -c | sort -nr

# 列出每個 ip 建立的 port 80 連線數量:
netstat -plan|grep :80|awk {'print $5'}|cut -d: -f 1|sort|uniq -c|sort -nk 1

# 列出所有連接埠，包含 listening 與 non listening
netstat -a

# 僅列出 TCP 的連接埠
netstat -at

# 僅列出 UDP 的連接埠
netstat -au

# 列出所有 listening 狀態的連接埠
netstat -l

# 列出所有 listening 狀態的 TCP 連接埠
netstat -lt

# 列出所有 listening 狀態的 UDP 連接埠
netstat -lu

# 列出各種協定的統計數據
netstat -s

# 列出 TCP 的統計數據
netstat -st

# 列出 UDP 的統計數據
netstat -su

# 如果要顯示應詳細的統計數據，可以再加上 -w 參數
netstat -sw

# 檢視哪些IP連線本機
netstat -an

# 檢視tcp連線數狀態
netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'

# 統計8080埠上有多少個TCP連線，命令：
netstat -ant |grep 80|wc -l

# TCP連線中有多少個連線狀態是ESTABLISHED，命令：
netstat -ant |grep 80|grep ESTABLISHED|wc -l

# TCP連線中有多少個連線狀態是CLOSE_WAIT
netstat -ant |grep 80|grep CLOSE_WAIT|wc -l

# TCP連線中有多少個連線狀態是TIME_WAIT
netstat -ant |grep 80|grep TIME_WAIT|wc -l

# 使用awk來完成統計資訊，命令如下
netstat -ant |grep 80|awk '{++S[$NF]} END {for (a in S) print a, S[a]}'

	# TCP連線狀態詳解：
	# 	LISTEN：      偵聽來自遠方的TCP埠的連線請求
	# 	SYN-SENT：    再傳送連線請求後等待匹配的連線請求
	# 	SYN-RECEIVED：再收到和傳送一個連線請求後等待對方對連線請求的確認
	# 	ESTABLISHED： 代表一個開啟的連線
	# 	FIN-WAIT-1：  等待遠端TCP連線中斷請求，或先前的連線中斷請求的確認
	# 	FIN-WAIT-2：  從遠端TCP等待連線中斷請求
	# 	CLOSE-WAIT：  等待從本地使用者發來的連線中斷請求
	# 	CLOSING：     等待遠端TCP對連線中斷的確認
	# 	LAST-ACK：    等待原來的發向遠端TCP的連線中斷請求的確認
	# 	TIME-WAIT：   等待足夠的時間以確保遠端TCP接收到連線中斷請求的確認
	# 	CLOSED：      沒有任何連線狀態
	# 	SYN_RECV：    表示正在等待處理的請求數
	# 	ESTABLISHED： 表示正常資料傳輸狀態
	# 	TIME_WAIT：   表示處理完畢，等待超時結束的請求數。
```

# nslookup(DNS偵錯工具)

[nslookup](https://ss64.com/bash/nslookup.html)

```
輸入 nslookup 命令後，會看到 > 提示符號，之後就可輸入查詢指令。
一般輸入IP address或是domain name來做反向及正向的解析。
```

```bash
nslookup [IP/domain]

# 檢測 DNS TXT 配置結果是否正確
nslookup -type=txt subdomain.domain.com
```

# dig(DNS偵錯工具)

[dig (domain information groper)](https://ss64.com/bash/dig.html)

```
dig是一個網絡管理命令行工具，用於查詢域名系統。
dig是域名伺服器軟體套件BIND的組成部分。
```

```bash
dig [@server]

### 回應說明 ###
# 指向
;; ANSWER SECTION:

# 檢測 DNS TXT 配置結果是否正確
dig @223.5.5.5 subdomain.domain.com txt +short
```

# wget

[使用 wget 完成批量下載](https://www.itread01.com/p/188595.html)

[manpage](https://linux.die.net/man/1/wget)

```bash
# 安裝
yum install wget -y

# 將filename.txt檔案內的地址 逐行下載
wget -i filename.txt
```

# iftop(網路流量監控工具)

```bash
# 監控連線 流量
iftop
```
[iftop：Linux 即時監控網路流量工具](https://blog.gtwang.org/linux/iftop-linux-network-traffic-monitor/)

[iftop manpage](https://helpmanual.io/man8/iftop/)

```bash
# Ubuntu Linux 安裝
apt-get install iftop

# CentOS 安裝 epel-release擴充資源庫
yum install epel-release -y

# 安裝iftop
yum install iftop -y

iftop
	# -h：顯示簡短的使用說明。
	# -n：不執行 DNS 反解，直接顯示 IP 位址。
	# -N：直接顯示埠號（port），不轉換為服務名稱。
	# -P：以 promiscuous 模式執行 iftop。
	# -i：指定網路卡（interface）。
	# -B：流量以 Bytes 為單位。
	# -F：指定要監看的 IPv4 網路區段。
	# -G：指定要監看的 IPv6 網路區段。
	# -f：指定 filter code。
```

執行 iftop 之後 快捷鍵
```
Host display：
	n：開啓或關閉 DNS 反解。
	s：顯示或隱藏來源（source）IP 位址。
	d：顯示或隱藏目的（destination）IP 位址。
	t：切換輸入輸出的流量顯示模式。

Port display：
	N：啓動或關閉埠號（port）轉換為服務名稱功能。
	S：顯示或隱藏來源埠號。
	D：顯示或隱藏目的埠號。
	p：顯示或隱藏所有埠號。

Sorting：
	1/2/3：選擇排序欄位。
	<：以流量來源 hostname 排序。
	>：以流量目的 hostname 排序。
	o：凍結目前排列狀況。

General：
	P：暫停更新資料，讓使用者慢慢看目前的圖表。
	j/k：上下捲動圖表，用於凍結排列的狀況。
	h：顯示快速鍵使用說明。
	b：顯示或隱藏流量圖。
	B：切換流量圖的顯示資料。
	T：顯示或隱藏累計流量。
	f：編輯 filter code。
	l：設定 screen filter。
	L：切換 linear 或 log 轉換。
	!：執行 shell command。
	q：離開 iftop。
```

# OpenSSL OpenSSH(連線、密鑰)

[Using OpenSSL s_client commands to test SSL connectivity](https://docs.pingidentity.com/bundle/solution-guides/page/iqs1569423823079.html)

[OpenSSL commands](https://www.openssl.org/docs/man3.0/man1/)

[OpenSSH Manual Pages](https://www.openssh.com/manual.html)

[openssl-rand, rand - generate pseudo-random bytes](https://www.openssl.org/docs/man1.0.2/man1/openssl-rand.html)

```bash
### 生成密鑰 ###
# 用openssl產生一個16位元的binary key

openssl rand 16 > {key_dir}/{key_name}.key

openssl rand [-out file] [-rand file(s)] [-base64] [-hex] num

ssh-keygen
	# -f  指定密鑰文件名。
	# -N  提供一個新的密語。
    # -P  提供(舊)密語。
    # -p  要求改變某私鑰文件的密語而不重建私鑰。程序將提示輸入私鑰文件名、原來的密語、以及兩次輸入新密語。
    # -b  指定密鑰長度。對於RSA密鑰，最小要求768位，默認是2048位。DSA密鑰必須恰好是1024位(FIPS 186-2 標準的要求)。
	# -R  從known_hosts 文件中刪除所有屬於hostname 的密鑰，這個選項主要用於刪除經過散列的主機(參見-H 選項)的密鑰。
	# -t  指定要創建的密鑰類型。可以使用："rsa1"(SSH-1) "rsa"(SSH-2) "dsa"(SSH-2)

    # -C  提供一個新註釋
    # -F  在known_hosts文件中搜索指定的hostname ，並列出所有的匹配項。這個選項主要用於查找散列過的主機名/ip地址，還可以和-H 選項聯用打印找到的公鑰的散列值。
    # -H  對known_hosts 文件進行散列計算。這將把文件中的所有主機名/ip地址替換為相應的散列值。原來文件的內容將會添加一個".old"後綴後保存。這些散列值只能被ssh 和sshd 使用。這個選項不會修改已經經過散列的主機名/ip地址，因此可以在部分公鑰已經散列過的文件上安全使用。
    # -i  讀取未加密的SSH-2兼容的私鑰/公鑰文件，然後在stdout 顯示OpenSSH兼容的私鑰/公鑰。該選項主要用於從多種商業版本的SSH中導入密鑰。
    # -l  顯示公鑰文件的指紋數據。它也支持RSA1 的私鑰。對於RSA和DSA密鑰，將會尋找對應的公鑰文件，然後顯示其指紋數據。
    # -r  打印名為hostname 的公鑰文件的SSHFP 指紋資源記錄。
    # -U  把現存的RSA私鑰上傳到智能卡reader
    # -v 詳細模式。輸出處理過程的詳細調試信息。常用於調試模數的產生過程。重複使用多個-v 選項將會增加信息的詳細程度(最大3次)。

### 使用ssh key連線設定 ###

# Linux 伺服器的部份，如果要提高安全性
vim /etc/ssh/sshd_config

# 修改以下的設定
# 停用密碼認證的登入方式
# PasswordAuthentication no
# 只允許金鑰認證
# PubkeyAuthentication yes

# 重啟
/etc/init.d/sshd restart

### 上傳金鑰的方法 ###

# 複製公鑰 到Linux伺服器上的 ~/.ssh/authorized_keys
ssh user@host 'mkdir -p ~/.ssh; cat >> ~/.ssh/authorized_keys' <  /home/username/.ssh/key.pub

# 複製公鑰
ssh-copy-id -i ~/.ssh/key.pub user@host

# chmod u=rw,go= file
# 對file的所有者設定讀寫權限，清空該使用者群組和其他使用者對file的所有權限（空格代表無權限）
chmod -R go= ~/.ssh

### 設定ssh-agent 改善每次登入都要輸入Passphrase ###
# 第一種
# 首先啟動ssh-agent
eval ssh-agent -s

# 將私鑰給ssh-agent來保管 使用ssh-add來指定金鑰。再輸入產生金鑰時所設定的Passphrase。
ssh-add
# Enter passphrase for /root/.ssh/id_rsa:  #輸入Passphrase密碼

# 第二種
vim /root/.bash_profile

SSH_ENV=$HOME/.ssh/environment
function start_agent {
     echo "Initialising new SSH agent..."
     /usr/bin/ssh-agent | sed 's/^echo/#echo/' > ${SSH_ENV}
     echo succeeded
     chmod 600 ${SSH_ENV}
     . ${SSH_ENV} > /dev/null
     /usr/bin/ssh-add;
}
# Source SSH settings, if applicable
if [ -f "${SSH_ENV}" ]; then
     . ${SSH_ENV} > /dev/null
     #ps ${SSH_AGENT_PID} doesn't work under cywgin
     ps -ef | grep ${SSH_AGENT_PID} | grep ssh-agent$ > /dev/null || {
         start_agent;
     }
else
     start_agent;
fi

### 指令 scp 傳遞檔案 ###

scp [帳號@來源主機]:來源檔案 [帳號@目的主機]:目的檔案

# 複製目錄 -r 參數
scp -r /path/folder1 myuser@192.168.0.1:/path/folder2

# 保留檔案時間與權限 -p 參數
scp -p /path/file1 myuser@192.168.0.1:/path/file2

# 將資料壓縮之後再傳送，減少網路頻寬的使用量 -C 參數
scp -C /path/file1 myuser@192.168.0.1:/path/file2

# 限制傳輸速度 -l 指定可用的網路頻寬上限值（單位為 Kbit/s）：
scp -l 400 /path/file1 myuser@192.168.0.1:/path/file2

# 使用 2222 連接埠 -P
scp -P 2222 /path/file1 myuser@192.168.0.1:/path/file2

# 使用 IPv4 -4
scp -4 /path/file1 myuser@192.168.0.1:/path/file2

# 使用 IPv6 -6
scp -6 /path/file1 myuser@192.168.0.1:/path/file2

### ssh 遠端設定 ###
vim /etc/sshd/sshd_config
	# shh 使用port
	# Port 22
	# 限制root使用ssh登入
	# PermitRootLogin no
	# 使用密碼登入
	# PasswordAuthentication

# 設定固定ip 設定檔位置
vim /etc/sysconfig/network-scripts/ifcfg-eth0
	# DEVICE=網卡的代號
	# BOOTPROTO=是否使用 dhcp
	# HWADDR=是否加入網卡卡號(MAC)
	# IPADDR=IP位址
	# NETMASK=子網路遮罩
	# ONBOOT=要不要預設啟動此介面
	# GATEWAY=通訊閘

# 主機名稱 設定檔位置
vim /etc/sysconfig/network
	# NETWORKING=要不要有網路
	# NETWORKING_IPV6=支援IPv6否？
	# HOSTNAME=你的主機名

# DNS IP 設定檔位置
vim /etc/resolv.conf
    # nameserver DNS的IP
	# 私有 IP 對應的主機名稱 設定檔位置 /etc/hosts
	# 私有IP 主機名稱 別名

# 重新啟動整個網路的參數 很快的恢復系統預設的參數值。
/etc/init.d/network restart

# 啟動或者是關閉某張網路介面。
ifup eth0 (ifdown eth0)

# 啟動ssh服務
/etc/init.d/sshd restart
netstat -tlnp | grep ssh

# 防火牆設定
# 查看
firewall-cmd --zone=public --list-all
# 對外開放 6379 port
firewall-cmd --zone=public --add-port=6379/tcp --permanent
	# --permanent 指定為永久設定，否則在 firewalld 重啟或是重新讀取設定，就會失效
# 重新讀取 firewall 設定
firewall-cmd --reload
```

# firewall-cmd SELinux(防火牆)

```bash
# 查看
firewall-cmd --zone=public --list-all

# 對外開放 6379 port
firewall-cmd --zone=public --add-port=30000/tcp --permanent

# 重新讀取 firewall 設定
firewall-cmd --reload
	# --permanent 指定為永久設定，否則在 firewalld重啟或是重新讀取設定，就會失效

### FirewallD is not running ###

# 取消服務的鎖定
systemctl unmask firewalld

# 鎖定該服務時執行
systemctl mask firewalld

# 關閉。
systemctl stop firewalld

# 啟動服務
systemctl start firewalld
# 開機自動啟動
systemctl enable firewalld
# 開機不會啟動
systemctl disable firewalld
# 查詢啟動狀態
systemctl status firewalld
# 重啟
systemctl restart firewalld
# 停止
systemctl stop firewalld
```

# 要確定對方主機某一個埠 TCP port 是否有開啟

[Linux – Ping TCP port?](http://benjr.tw/91897)

```bash
# PaPing:http://code.google.com/p/paping
paping www.google.com -p 80 -c 4

# nc (或是 netcat)
nc -vz google.com 80

hping google.com -S -V -p 80

nmap -p 80 google.com
# 其他參數使用請參考 http://benjr.tw/28087

# Telnet
telnet www.google.com 23
```
