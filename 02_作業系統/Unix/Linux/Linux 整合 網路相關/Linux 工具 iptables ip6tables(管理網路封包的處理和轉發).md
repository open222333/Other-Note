# Linux 工具 iptables(管理網路封包的處理和轉發)

```
iptables是運行在使用者空間的應用軟體，通過控制Linux核心netfilter模組，來管理網路封包的處理和轉發。

Version 2.0：使用 ipfwadm 這個防火牆機制；
Version 2.2：使用 ipchains 這個防火牆機制；
Version 2.4 與 2.6：使用 iptables 這個防火牆機制

用 uname -r 追蹤一下核心版本
```

## 目錄

- [Linux 工具 iptables(管理網路封包的處理和轉發)](#linux-工具-iptables管理網路封包的處理和轉發)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝部分](#安裝部分)
	- [安裝步驟 CentOS7](#安裝步驟-centos7)
- [指令](#指令)
- [防火牆規則](#防火牆規則)
- [配置文檔](#配置文檔)
	- [CentOS](#centos)
	- [IPv6部分](#ipv6部分)
- [範例](#範例)
- [例外狀況](#例外狀況)
	- [Failed to start IPv4 firewall with iptables](#failed-to-start-ipv4-firewall-with-iptables)

## 參考資料

[iptables(8) - Linux man page](https://linux.die.net/man/8/iptables)

[ip6tables(8) - Linux man page](https://linux.die.net/man/8/ip6tables)

[第十一章、Linux 防火牆與 NAT 伺服器](https://linux.vbird.org/linux_server/centos4/0250simple_firewall-centos4.php#reference)

[iptables 的安裝與設定](http://120.105.184.250/cswang/thit/Linux/iptables.htm)

[IPTables - 防火牆](https://wiki.centos.org/zh-tw/HowTos/Network/IPTables)

# 安裝部分

## 安裝步驟 CentOS7

```bash
# 在CentOS 7或RHEL 7或Fedora中防火牆由firewalld來管理
# 安裝iptables-services
yum install iptables-services -y

# 停用 firewalld
systemctl stop firewalld
systemctl mask firewalld
```

# 指令

```bash
# [停止 啟動 重啟]
service iptables [stop|start|restart]

# 啟動服務
systemctl start iptables
# 開機自動啟動
systemctl enable iptables
# 開機不會啟動
systemctl disable iptables
# 查詢啟動狀態
systemctl status iptables
# 重啟
systemctl restart iptables
# 停止
systemctl stop iptables
```

# 防火牆規則

```bash
###-----------------------------------------------------###
# 防火牆 規則
###-----------------------------------------------------###
# 定義預設政策 (policy)
# 當封包不在設定的規則之內時，則該封包的通過與否，以 Policy 的設定為準
# -P ：定義政策( Policy )
# ACCEPT ：該封包可接受
# DROP   ：該封包直接丟棄，不會讓 client 端知道為何被丟棄。
iptables [-AI 鏈] [-io 網路介面] [-p 協定] [-s 來源IP/網域] [--sport 埠口範圍] [-d 目標IP/網域] [--dport 埠口範圍] -j [ACCEPT|DROP]
	參數：
	-AI 鏈
		針對某的鏈進行規則的 "插入" 或 "累加"
		-A
			新增加一條規則，該規則增加在原本規則的最後面。
			例如原本已經有四條規則，
			使用 -A 就可以加上第五條規則！
		-A, --append chain rule-specification
			將一個或多個規則附加到選定鏈的末尾。
			當源和/或目標名稱解析為多個地址時，將為每個可能的地址組合添加一條規則。
		-I
			插入一條規則。如果沒有指定此規則的順序，預設是插入變成第一條規則。
			例如原本有四條規則，使用 -I 則該規則變成第一條，而原本四條變成 2~5 號
		鏈 ：有 INPUT, OUTPUT, FORWARD 等，此鏈名稱又與 -io 有關，請看底下。
	-io 網路介面
		設定封包進出的介面規範
		-i
			封包所進入的那個網路介面，例如 eth0, lo 等介面。需與 INPUT 鏈配合；
		-o
			封包所傳出的那個網路介面，需與 OUTPUT 鏈配合；
	-p, --protocol [!] protocol
		封包的協定
		tcp ：封包為 TCP 協定的封包；
		upd ：封包為 UDP 協定的封包；
		icmp：封包為 ICMP 協定、
		all ：表示為所有的封包
	-s 來源 IP/網域
		設定此規則之封包的來源項目，可指定單純的 IP 或包括網域，例如：
		IP：192.168.0.100
		網域：192.168.0.0/24, 192.168.0.0/255.255.255.0 均可。
		若規範為『不許』時，則加上 ! 即可，例如：
		-s ! 192.168.100.0/24 表示不許 192.168.100.0/24 之封包來源；
	-d
		目標 IP/網域：同 -s ，只不過這裡指的是目標的 IP 或網域。
	-j, --jump 動作
		後面接動作，主要的動作有接受 (ACCEPT)、丟棄 (DROP) 及記錄 (LOG)
	--sport 埠口範圍
		限制來源的埠口號碼，埠口號碼可以是連續的，例如 1024:65535
		port 是 TCP,UDP 特有的 需加上 -p tcp 或 -p udp
	--dport 埠口範圍
		限制目標的埠口號碼。
		port 是 TCP,UDP 特有的 需加上 -p tcp 或 -p udp
	-t
		後面接 table ，例如 nat 或 filter ，若省略此項目，則使用預設的 filter
	-L
		列出目前的 table 的規則
	-n, --numeric
		不進行 IP 與 HOSTNAME 的反查，顯示訊息的速度會快很多
		數字輸出。
		IP 地址和端口號將以數字格式打印。
		默認情況下，程序會嘗試將它們顯示為主機名、網絡名或服務。
	-v, --verbose
		詳細輸出。
		此選項使 list 命令顯示接口名稱、規則選項(如果有)和 TOS 掩碼。
		還列出了數據包和字節計數器，後綴“K”、“M”或“G”分別表示 1000、1,000,000 和 1,000,000,000 乘數(但請參閱-x標誌以更改此值)。
		對於追加、插入、刪除和替換，這會導致打印規則的詳細信息。
	-D, --delete chain rule-specification
	-D, --delete chain rulenum
		從選定的鏈中刪除一個或多個規則。
		此命令有兩個版本：可以將規則指定為鏈中的數字（從 1 開始表示第一條規則）或要匹配的規則。
	-I, --insert chain [rulenum] rule-specification
		在選定的鏈中插入一個或多個規則作為給定的規則編號。
		因此，如果規則編號為 1，則將一條或多條規則插入到鏈的頭部。
		如果未指定規則編號，這也是默認值。
	-R, --replace chain rulenum rule-specification
		替換選定鏈中的規則。
		如果源和/或目標名稱解析為多個地址，則該命令將失敗。
		規則從 1 開始編號。
	-L, --list
		列出選定鏈中的所有規則。
	-F, --flush [chain]
		刷新選定的鏈（如果沒有給出表中的所有鏈）。
		這相當於將所有規則一一刪除。
	-Z, --zero [chain]
		將所有鏈中的數據包和字節計數器歸零。
		指定-L, --list (list) 選項也是合法的，以便在計數器被清除之前立即查看它們。（看上面。）
	-N, --new-chain chain
		按給定名稱創建一個新的用戶定義鏈。
		必須沒有該名稱的目標。
	-X, --delete-chain [chain]
		刪除指定的可選用戶定義鏈。
		不能有對鏈的引用。如果有，則必須先刪除或替換引用規則，然後才能刪除鏈。
		鏈必須為空，即不包含任何規則。
		如果沒有給出參數，它將嘗試刪除表中的每個非內置鏈。
	-P, --policy chain target
		將鏈的策略設置為給定的目標。
		有關合法目標，請參閱目標部分。
		只有內置（非用戶定義的）鏈可以有策略，內置和用戶定義的鏈都不能成為策略目標。
			INPUT　：封包為輸入主機的方向；
			OUTPUT ：封包為輸出主機的方向；
			FORWARD：封包為不進入主機而向外再傳輸出去的方向；
			PREROUTING ：在進入路由之前進行的工作；
			OUTPUT　 　：封包為輸出主機的方向；
			POSTROUTING：在進入路由之後進行的工作。
	-E, --rename-chain old-chain new-chain
		將用戶指定的鏈重命名為用戶提供的名稱。
		這是裝飾性的，對錶的結構沒有影響。
	-m
		一些 iptables 的模組，主要常見的有：
		state ：狀態模組
		mac   ：網路卡硬體位址 (hardware address)
	--state
		一些封包的狀態，主要有：
		INVALID    ：無效的封包，例如資料破損的封包狀態
		ESTABLISHED：已經連線成功的連線狀態；
		NEW        ：想要新建立連線的封包狀態；
		RELATED    ：這個最常用！表示這個封包是與我們主機發送出去的封包有關

# 範例一：所有的來自 lo 這個介面的封包，都予以接受
iptables -A INPUT -i eth0 -s 192.168.0.1 -j ACCEPT

# 範例二：目標來自 192.168.0.1 這個 IP 的封包都予以接受
iptables -A INPUT -i eth0 -s 192.168.0.1 -j ACCEPT

# 範例三：目標來自 192.168.1.0/24 可接受，但 192.168.1.10 丟棄
# 要先丟棄 192.168.1.10 才能接受該網域
iptables -A INPUT -i eth0 -s 192.168.1.10 -j DROP
iptables -A INPUT -i eth0 -s 192.168.1.0/24 -j ACCEPT

# 記錄某個規則的紀錄 只要有封包來自 192.168.2.200 這個 IP 時
# 那麼該封包的相關資訊就會被寫入到核心訊息 亦即是 /var/log/messages
iptables -A INPUT -s 192.168.2.200 -j LOG

# 想要連線進入本機 port 21 的封包都抵擋掉：
iptables -A INPUT -i eth0 -p tcp --dport 21 -j DROP

# 想連到這部主機的 (upd port 137,138 tcp port 139,445) 就放行
iptables -A INPUT -i eth0 -p udp --dport 137:138 -j ACCEPT
iptables -A INPUT -i eth0 -p tcp --dport 139 -j ACCEPT
iptables -A INPUT -i eth0 -p tcp --dport 445 -j ACCEPT

# 只要已建立或相關封包就予以通過，只要是不合法封包就丟棄
iptables -A INPUT -m state --state RELATED,ESTABLISHED -j ACCEPT
iptables -A INPUT -m state --state INVALID -j DROP

# 列出 filter table 三條鏈的規則
iptables -L -n
iptables -L -nv

# 保存輸入規則(需重啟)
service iptables save

# 重啟
service iptables restart

# 防火牆的記錄
iptables-save > filename

# 防火牆的回復
iptables-restore < filename

###-----------------------------------------------------###
# 清除設定
###-----------------------------------------------------###
# 清除預設表 filter 中，所有規則鏈中的規則
iptables -F
# 清除預設表 filter 中，使用者自訂鏈中的規則
iptables -X
# 清除{table}表中，所有規則鏈中的規則
iptables -F -t {table}
# 清除{table}表中，使用者自訂鏈中的規則
iptables -t {table} -X
# 清除nat表中，所有規則鏈中的規則
iptables -F -t nat
# 清除nat表中，使用者自訂鏈中的規則
iptables -t nat -X

# 將chain INPUT iptables以序號標記顯示
iptables -L INPUT -n --line-number

# 刪除行號3
iptables -D INPUT 3
```

# 配置文檔

## CentOS

直接修改設定檔後重啟即可，且永久生效。

IPv4
`/etc/sysconfig/iptables`
IPv6
`/etc/sysconfig/ip6tables`

```bash
# 重啟後生效
service iptables restart

# 沒裝 iptables.services 有可能要用另一種指令 reload
reload iptables
```

## IPv6部分

`/etc/sysconfig/network`

NETWORKING_IPV6 是否開啟ipv6

```
NETWORKING="yes"
NETWORKING_IPV6="no"
GATEWAYDEV="eth0"
```

# 範例

```
# 代表是註解
* 代表預設的 table
: 代表各條鏈的預設政策
後續的動作則是各個規則
```

```conf
# Generated by iptables-save v1.4.21 on Mon Mar 28 10:19:30 2022
*filter
:INPUT DROP [0:0]
:FORWARD ACCEPT [0:0]
:OUTPUT ACCEPT [40:5262]
-A INPUT -m state --state RELATED,ESTABLISHED -j ACCEPT
-A INPUT -s xxx.xxx.xxx.xxx/32 -p tcp -m tcp --dport 22 -j ACCEPT
-A INPUT -p tcp -m tcp --dport 80 -j ACCEPT
-A INPUT -p tcp -m tcp --dport 443 -j ACCEPT
-A INPUT -i lo -j ACCEPT
COMMIT
# Completed on Mon Mar 28 10:19:30 2022
```

# 例外狀況

## Failed to start IPv4 firewall with iptables

[CentOS 7.2:Failed to start IPv4 firewall with iptables](https://developer.aliyun.com/article/788144)

```bash
# 關閉firewalld
systemctl stop firewalld
systemctl mask firewalld

# 開 443 端口（HTTPS）
iptables -A INPUT -p tcp --dport 443 -j ACCEPT

# 保存上述規則
service iptables save

# 開啟服務
systemctl restart iptables.service
```

