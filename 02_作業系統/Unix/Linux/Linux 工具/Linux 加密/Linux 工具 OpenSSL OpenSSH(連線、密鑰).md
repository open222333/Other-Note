# Linux 工具 OpenSSL OpenSSH(連線、密鑰)

```
```

## 目錄

- [Linux 工具 OpenSSL OpenSSH(連線、密鑰)](#linux-工具-openssl-openssh連線密鑰)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [指令相關](#指令相關)
    - [憑證相關](#憑證相關)
    - [設定檔相關](#設定檔相關)
    - [深入學習相關](#深入學習相關)
    - [範例相關](#範例相關)
    - [例外相關](#例外相關)
- [安裝](#安裝)
  - [CentOS7](#centos7)
- [指令](#指令)
  - [ssh指令](#ssh指令)
  - [scp指令 (傳遞檔案)](#scp指令-傳遞檔案)
    - [rsync 與 scp 區別](#rsync-與-scp-區別)
  - [sshpass(傳送密碼)](#sshpass傳送密碼)
- [常用](#常用)
  - [ssh\_config(紀錄主機連線資訊)](#ssh_config紀錄主機連線資訊)
    - [常用建立連線設定步驟](#常用建立連線設定步驟)
  - [公鑰(.pub 文件)傳送到另一個電腦使用](#公鑰pub-文件傳送到另一個電腦使用)
  - [連線設定1](#連線設定1)

## 參考資料

[Using OpenSSL s_client commands to test SSL connectivity](https://docs.pingidentity.com/bundle/solution-guides/page/iqs1569423823079.html)

[OpenSSL commands](https://www.openssl.org/docs/man3.0/man1/)

[OpenSSH Manual Pages](https://www.openssh.com/manual.html)

[openssl-rand, rand - generate pseudo-random bytes](https://www.openssl.org/docs/man1.0.2/man1/openssl-rand.html)

### 指令相關

[ssh(1) - Linux man page](https://linux.die.net/man/1/ssh)

[scp(1) - Linux man page](https://linux.die.net/man/1/scp)

[sshpass(1) - Linux man page](https://linux.die.net/man/1/sshpass)

[ssh-keygen(1) — Linux manual page](https://www.man7.org/linux/man-pages/man1/ssh-keygen.1.html)

[ssh-copy-id - Man Page](https://www.mankier.com/1/ssh-copy-id)

### 憑證相關

[Certificate Decoder - 查看憑證資訊(日期)](https://www.sslshopper.com/certificate-decoder.html)

### 設定檔相關

[ssh ssh_config — OpenSSH 客戶端配置文件](https://man.openbsd.org/OpenBSD-current/man5/ssh_config.5)

[ssh ssh_config — OpenSSH 客戶端配置文件](https://linux.die.net/man/5/ssh_config)

[增進 SSH 使用效率 - ssh_config](https://chusiang.gitbooks.io/working-on-gnu-linux/content/20.ssh_config.html)

### 深入學習相關

[How does `scp` differ from `rsync`? - `scp` 與 `rsync` 有何不同？](https://stackoverflow.com/questions/20244585/how-does-scp-differ-from-rsync)

### 範例相關

[Automated ssh-keygen without passphrase - 自動生成不要passphrase,相同檔名處理](https://unix.stackexchange.com/questions/69314/automated-ssh-keygen-without-passphrase-how)

[ssh 跳過第一次指紋確認](https://stackoverflow.com/questions/21383806/how-can-i-force-ssh-to-accept-a-new-host-fingerprint-from-the-command-line)

### 例外相關

[SSH Key: “Permissions 0644 for 'id_rsa.pub' are too open.” on mac - SSH 金鑰：id_rsa.pub 的權限 0644 過於開放。 蘋果電腦](https://stackoverflow.com/questions/29933918/ssh-key-permissions-0644-for-id-rsa-pub-are-too-open-on-mac)

# 安裝

## CentOS7

```bash
yum install -y sshpass
```

# 指令

## ssh指令

```bash
# -L 指定將本地（客戶端）主機上的給定端口轉發到遠程端的給定主機和端口
ssh username@host_ip:port -fN -L [bind_address:]port:host:hostport
	-1：強制使用ssh協議版本1
	-2：強制使用ssh協議版本2
	-4：強制使用IPv4地址
	-6：強制使用IPv6地址
	-A：開啟認證代理連接轉發功能
	-a：關閉認證代理連接轉發功能
	-b：使用本機指定地址作為對應連接的源ip地址
	-C：請求壓縮所有數據
	-c：選擇所加密的密碼型式（blowfish|3des 預設是3des）
	-e：設定跳脫字符
	-F：指定ssh指令的配置文件
	-f：後台執行ssh指令
	-g：允許遠程主機連接主機的轉發端口
	-i：指定身份文件（預設是在使用者的家目錄中的.ssh/identity）
	-l：指定連接遠程服務器登錄用戶名
	-N：不執行遠程指令
	-n：重定向stdin 到/dev/null
	-o：指定配置選項
	-p：指定遠程服務器上的端口（默認22）
	-P：使用非特定的port 去對外聯機（注意這個選項會關掉RhostsAuthentication 和RhostsRSAAuthentication）
	-q：靜默模式
	-T：禁止分配偽終端
	-t：強製配置pseudo-tty
	-v：打印更詳細信息
	-X：開啟X11轉發功能
	-x：關閉X11轉發功能
	-y：開啟信任X11轉發功能
	-L listen-port:host:port 指派本地的port 到達端機器地址上的port

ssh-keygen
	-m PEM 將金鑰格式設定為 PEM
	-t rsa 要建立的金鑰類型，在此案例中採用 RSA 格式
	-b 4096 金鑰中的位元數，在此案例中為 4096
	-C "azureuser@myserver" 附加至公開金鑰檔案結尾以便輕鬆識別的註解。 通常會以電子郵件地址作為註解，但您可以使用任何最適合您的基礎結構的項目。
	-f ~/.ssh/mykeys/myprivatekey 私密金鑰檔案的檔案名稱 (如果選擇不使用預設名稱)。 加上 .pub 的對應公開金鑰檔案會產生在相同的目錄中。 此目錄必須已存在。
	-N mypassphrase = 用來存取私密金鑰檔案的其他複雜密碼。

# Remove old key
ssh-keygen -R $target_host

# Add the new key
ssh-keyscan $target_host >> ~/.ssh/known_hosts
```

## scp指令 (傳遞檔案)

```bash
### 指令 scp  ###
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
```

### rsync 與 scp 區別

```
主要區別在於它們複製文件的方式。

scp基本上讀取源文件並將其寫入目標。它在本地或通過網絡執行簡單的線性複制。

rsync還可以在本地或通過網絡複製文件。但它採用特殊的增量傳輸算法和一些優化來使操作更快。考慮通話。

rsync有大量的命令行選項，允許用戶微調其行為。它支持複雜的過濾規則，以批處理模式、守護模式等方式運行，scp只有幾個開關。
```

## sshpass(傳送密碼)

```bash
# 用 sshpass 傳送密碼 123qwe，ssh 登入 kvm7.deyu.wang 在目錄 /root 下產生一個檔案 abc 內容為 aaaa。 遠端連線時目錄 .ssh 中有一個檔案 known_hosts 會記錄曾經連線過的信任主機，若連線主機重新安裝或更換 ip，其 host key 不同，ssh 會產生警告訊息，要求確認並刪除在 known_hosts 中的記錄，才能連線。選項 -o StrictHostKeyChecking=no 就是不進行 host key 的檢查，以避免此中斷程式的動作
sshpass -p123qwe ssh -o StrictHostKeyChecking=no kvm7.deyu.wang "echo aaaa > /root/abc"

# 遠程創建文件file
sshpass -p 123456 ssh admin@1.1.1.1 "touch file"
# 把本地文件file1傳入遠程機器1.1.1.2上的用戶目錄下
sshpass -p 123456 scp file1 admin@1.1.1.2:~

rsync -av -e "sshpass -p123qwe ssh" b.txt kvm7.deyu.wang:
```

```Ruby
=begin
sshpass.rb 是一個 Ruby 語言編寫的工具，用於通過 SSH 密碼自動登錄遠程服務器。
它的作用是簡化在腳本或命令行中通過 SSH 登錄的過程，特別是當需要自動化執行需要 SSH 登錄的任務時。

這個工具的原理是使用 sshpass 命令來將密碼傳遞給 SSH 客戶端，從而實現自動登錄遠程服務器。
通過 sshpass.rb，可以在 Ruby 程式中方便地使用 SSH 登錄功能，而不需要手動輸入密碼。
=end
require 'sshpass'

host = 'example.com'
user = 'username'
password = 'your_password'
command = 'ls -l'

# 使用 sshpass.rb 登錄遠程主機並執行命令
output = SSHPass.run(host, user, password, command)

puts "Command output:"
puts output
```

# 常用

## ssh_config(紀錄主機連線資訊)

```bash
# 編輯設定
vim ~/.ssh/config

# - master
Host            master                # 代號
Hostname        192.168.11.24        # IP or Domain name
Port            2222                # 指定埠口
User            jonny                # 使用者名稱
identityfile    ~/.ssh/id_rsa_24    # 指定金鑰

# - slave
Host            slave                # 代號
Hostname        192.168.11.25        # IP or Domain name
Port            2223                # 指定埠口
User            jonny                # 使用者名稱
identityfile    ~/.ssh/id_rsa_25    # 指定金鑰

# 使用代號進行連線
ssh slave
sftp


# 另一種格式
Host ssh-jp-manager
    User root
    HostName 192.168.11.25
    Port 22
    ServerAliveInterval 60
    IdentityFile ~/.ssh/id_rsa

Host test1
	User            root
	HostName        192.168.11.1
	ServerAliveInterval     60
	IdentityFile	 ~/.ssh/test1
Host test2
	User            root
	HostName        192.168.11.2
	ServerAliveInterval     60
	IdentityFile     ~/.ssh/test2
Host test3
	User            user
	HostName        192.168.11.3
	ServerAliveInterval     60
	IdentityFile     ~/.ssh/test3
```

`背景建立 連線通道`

建立 SSH 隧道，將來自本地端口的流量通過 SSH 連接轉發到遠程主機

user@host: 這是用於指定 SSH 連接的目標主機的用戶名和主機地址。
user 是目標主機上的用戶名，host 是目標主機的 IP 地址或主機名。

-fN: 這是兩個選項的組合。
-f 表示在背景運行 SSH 連接，並且不會打開 shell。
-N 表示不執行任何命令，僅用於轉發端口。

-L 127.0.0.1:port:host_ip:port:
這是用於定義本地端口轉發的選項。
這告訴 SSH 在本地主機的 127.0.0.1 地址的 port 端口上監聽，並將來自該端口的流量轉發到遠程主機的 host_ip 地址的 port 端口上。

```bash
ssh user@host -fN -L 127.0.0.1:port:host_ip:port
```

### 常用建立連線設定步驟

```bash
ssh-keygen -f ~/.ssh/$hostname

# 複製公鑰
ssh-copy-id -i ~/.ssh/$hostname.pub $user@$host

# 編輯 填入資料
vim ~/.ssh/config

Host $hostname
		User            $user
		HostName        $host
		ServerAliveInterval     60
		IdentityFile     ~/.ssh/$hostname
```

## 公鑰(.pub 文件)傳送到另一個電腦使用

將 SSH 公鑰文件（即 .pub 文件）添加到目標服務器或系統的授權文件中

若還沒有訪問目標服務器，請使用 SSH 登錄到該服務器。

```bash
ssh username@hostname
```

創建 SSH 授權文件：

在目標服務器上，創建 .ssh 目錄（如果不存在），然後創建 authorized_keys 文件用於存儲授權的公鑰。

```bash
mkdir -p ~/.ssh
touch ~/.ssh/authorized_keys
```

將公鑰添加到授權文件中：

打開 authorized_keys 文件，將公鑰（即 .pub 文件的內容）添加到這個文件中。

可以使用文本編輯器（如 nano 或 vim）來編輯文件：

```bash
nano ~/.ssh/authorized_keys
```

設置授權文件的權限：

確保 authorized_keys 文件的權限設置正確，只有擁有者可以讀取和寫入，其他用戶無權限。

```bash
chmod 600 ~/.ssh/authorized_keys
```

## 連線設定1

```bash
### 生成密鑰 ###
# 用openssl產生一個16位元的binary key

openssl rand 16 > {key_dir}/{key_name}.key

openssl rand [-out file] [-rand file(s)] [-base64] [-hex] num

ssh-keygen -t rsa -b 2048 -f /path/to/.ssh/key_name

ssh-keygen
	# -f  指定密鑰文件名。 ssh-keygen -f /path/to/your_key
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

# 測試遠程檔案是否存在
ssh $ssh_host test -e $file
ssh root@172.104.86.251 test -e /var/jiaobaba/uploads/UP-4ca87e5a296de3b1f812ae248ac8cae9/UP-4ca87e5a296de3b1f812ae248ac8cae9.mp4

# chmod u=rw,go= file
# 對file的所有者設定讀寫權限，清空該使用者群組和其他使用者對file的所有權限（空格代表無權限）
chmod -R go= ~/.ssh

### 設定ssh-agent 改善每次登入都要輸入Passphrase ###
# 第一種
# 首先啟動ssh-agent
eval ssh-agent -s

# 將私鑰給ssh-agent來保管 使用ssh-add來指定金鑰。再輸入產生金鑰時所設定的Passphrase。
eval `ssh-agent -s`
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


# If you want to decode certificates on your own computer, run this OpenSSL command:
# 解碼證書
openssl x509 -in certificate.crt -text -noout
```

