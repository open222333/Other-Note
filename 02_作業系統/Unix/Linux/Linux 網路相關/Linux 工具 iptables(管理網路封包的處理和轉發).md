# Linux 工具 iptables(管理網路封包的處理和轉發)

```
iptables是運行在使用者空間的應用軟體，通過控制Linux核心netfilter模組，來管理網路封包的處理和轉發。
```

## 參考資料

[iptables(8) - Linux man page](https://linux.die.net/man/8/iptables)

# 指令

```bash
# 基本
iptables
	-A, --append chain rule-specification
		將一個或多個規則附加到選定鏈的末尾。
		當源和/或目標名稱解析為多個地址時，將為每個可能的地址組合添加一條規則。
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
	-n, --numeric
		數字輸出。
		IP 地址和端口號將以數字格式打印。
		默認情況下，程序會嘗試將它們顯示為主機名、網絡名或服務。
	-v, --verbose
		詳細輸出。
		此選項使 list 命令顯示接口名稱、規則選項(如果有)和 TOS 掩碼。
		還列出了數據包和字節計數器，後綴“K”、“M”或“G”分別表示 1000、1,000,000 和 1,000,000,000 乘數(但請參閱-x標誌以更改此值)。
		對於追加、插入、刪除和替換，這會導致打印規則的詳細信息。
	-p, --protocol [!] protocol
		封包的協定
		tcp ：封包為 TCP 協定的封包；
		upd ：封包為 UDP 協定的封包；
		icmp：封包為 ICMP 協定、
		all ：表示為所有的封包
	-j, --jump target



###-----------------------------------------------------###
# 清除先前的設定
###-----------------------------------------------------###
# 清除預設表 filter 中，所有規則鏈中的規則
iptables -F
# 清除預設表 filter 中，使用者自訂鏈中的規則
iptables -X
# 清除mangle表中，所有規則鏈中的規則
iptables -F -t mangle
# 清除mangle表中，使用者自訂鏈中的規則
iptables -t mangle -X
# 清除nat表中，所有規則鏈中的規則
iptables -F -t nat
# 清除nat表中，使用者自訂鏈中的規則
iptables -t nat -X
```
