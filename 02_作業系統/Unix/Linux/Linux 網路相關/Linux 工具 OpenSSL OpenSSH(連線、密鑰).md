# Linux 工具 OpenSSL OpenSSH(連線、密鑰)

```
```

## 目錄

- [Linux 工具 OpenSSL OpenSSH(連線、密鑰)](#linux-工具-openssl-openssh連線密鑰)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [指令相關](#指令相關)
- [指令](#指令)
	- [ssh指令](#ssh指令)

## 參考資料

[Using OpenSSL s_client commands to test SSL connectivity](https://docs.pingidentity.com/bundle/solution-guides/page/iqs1569423823079.html)

[OpenSSL commands](https://www.openssl.org/docs/man3.0/man1/)

[OpenSSH Manual Pages](https://www.openssh.com/manual.html)

[openssl-rand, rand - generate pseudo-random bytes](https://www.openssl.org/docs/man1.0.2/man1/openssl-rand.html)

## 指令相關

[ssh(1) - Linux man page](https://linux.die.net/man/1/ssh)

# 指令

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

## ssh指令

```bash
# -L 指定將本地（客戶端）主機上的給定端口轉發到遠程端的給定主機和端口
ssh username@host_ip:port -L [bind_address:]port:host:hostport
```
