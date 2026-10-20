# Linode 筆記

## 目錄

- [Linode 筆記](#linode-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [Linodes](#linodes)
- [網路介面（Linode Interfaces / Configuration Profile）](#網路介面linode-interfaces--configuration-profile)
- [Firewall](#firewall)
	- [Cloud Firewall 規則與限制](#cloud-firewall-規則與限制)
	- [VPC 與防火牆怎麼搭配](#vpc-與防火牆怎麼搭配)
	- [與主機內防火牆（ufw / iptables / firewalld）搭配](#與主機內防火牆ufw--iptables--firewalld搭配)
- [網路操作（舊筆記）](#網路操作舊筆記)
	- [測試連通性 網路](#測試連通性-網路)
	- [Linode 添加私有IP（舊做法，需隔離請改用 VPC）](#linode-添加私有ip舊做法需隔離請改用-vpc)
	- [Linode 換ip](#linode-換ip)
- [VPC](#vpc)

## 參考資料

[官方文檔](https://www.linode.com/docs/)

[Networking interfaces（Linode Interfaces 與 Configuration Profile 差異）](https://techdocs.akamai.com/cloud-computing/docs/networking-interface)

[Manage Linode interfaces（編輯 VPC / Public 介面、掛防火牆）](https://techdocs.akamai.com/cloud-computing/docs/managing-linode-interfaces)

[Linode Cloud Firewalls（限制、範本）](https://techdocs.akamai.com/cloud-computing/docs/cloud-firewall)

[Manage firewall rules（預設政策、規則順序）](https://techdocs.akamai.com/cloud-computing/docs/manage-firewall-rules)

[Select default Cloud Firewalls for new Linodes](https://techdocs.akamai.com/cloud-computing/docs/select-default-firewalls-for-new-linodes)

[Comparing Cloud Firewalls to Linux firewall software](https://techdocs.akamai.com/cloud-computing/docs/comparing-cloud-firewalls-to-linux-firewall-software)

[Linode VPC(私有網路) 筆記](Linode_VPC(私有網路).md)

[Linode 工具 linode-cli 筆記](Linode_工具_linode-cli(Linode（Akamai_Cloud）官方命令列工具).md)

# Linodes

```
Distributions:佈局

Images:選擇系統
Region:選擇伺服器地區

Linode Plan
	Dedicated CPU
	專用 CPU 實例適用於需要一致性能的全負載工作負載。

	Shared CPU
	實例適用於中型工作負載，並且是性能、資源和價格的良好組合。

	High Memory
	高內存實例比其他資源更喜歡 RAM，並且可能有利於內存消耗量大的用例，如緩存和內存數據庫。所有 High Memory 計劃都使用專用 CPU 內核。

	Linode GPU
	Linode GPU 計劃的可用性有限，在您提出請求時可能不可用。訪問這些服務可能需要一些額外的驗證。


Private IP: 開啟內網IP功能，讓內網的伺服器可以溝通。
```

# 網路介面（Linode Interfaces / Configuration Profile）

每台 Linode 的網路介面有兩種管理方式，**決定了防火牆能不能分開設定**。2026-04-09 起 Linode Interfaces 正式 GA。

| 項目 | Linode Interfaces（新） | Configuration Profile Interfaces（舊） |
|---|---|---|
| 管理位置 | Linode → **Network** 分頁 | Linode → **Configurations** 分頁 |
| 修改條件 | 新增 / 刪除 / 改位址要**先關機** | 開機中可改，**重開機**後生效 |
| Cloud Firewall | **每個介面各自掛**（public 一個、VPC 一個） | 掛在整台 Linode，public 與 VPC 介面**共用同一組規則** |
| 防火牆範本 | 有 VPC / Public 範本 | 無 |
| Private IP（`192.168.128.0/17`） | **不支援** | 支援 |
| 預設路由 | 可分別指定 IPv4 / IPv6 走哪個介面 | 自動決定 |
| LKE 叢集節點 | 不支援 | 支援 |

- 介面數量：public 最多 1 個、VPC 最多 1 個、VLAN 可多個（不同 VLAN）。
- 舊 Linode 可以升級成 Linode Interfaces，但**不可逆**；還在用 Private IP 的 Linode 不要升級。
- 帳號預設值：Administration → Account Settings → 新 Linode 用哪種介面。
- 官方不建議同一台同時掛 VPC 介面和 public 介面；要連外網用 VPC 介面上的 1:1 NAT（Public access）。

# Firewall

[防火牆](https://www.linode.com/docs/guides/getting-started-with-cloud-firewall/)

```
選擇自建的規則

Rules:
	Inbound Rules:入站規則 Accept(允許) Drop(禁止)

	Outbound Rules:出站規則


Linodes:
伺服器加入規則
```

## Cloud Firewall 規則與限制

- **有狀態（stateful）**：允許進來的連線，回應封包自動放行，不用另外開 outbound。
- **預設政策**：Inbound / Outbound 各自選 Accept（規則以外全放行）或 Drop（規則以外全擋）。Inbound 建議 **Drop**，只開需要的；Outbound 通常 Accept。
- **規則順序**：由上往下比對，可拖曳排序；改完要按 **Save Changes** 才生效。
- **上限**：每個防火牆最多 25 條規則（inbound + outbound 合計）、每條最多 255 個 IP/網段、15 個 port（範圍算 2 個）。
- 單一 IP 用 `/32`（IPv6 `/128`），不寫遮罩時自動補。
- 一個防火牆可以掛在多台 Linode / 多個介面 / NodeBalancer；但**一個介面（或一台舊式 Linode）同時只能掛一個防火牆**。
- 套用範圍：public 與 VPC 流量都會過濾，**VLAN 流量不過濾**。
- NodeBalancer 只套用 inbound 規則，而且只保護 NodeBalancer 的公網 IP，後端 Linode 要自己另外掛防火牆。
- 帳號預設防火牆：Administration → Account Settings → **Default Firewalls**，可分別指定「Linode（舊式）/ VPC 介面 / Public 介面」的預設防火牆，建立時自動套用。

## VPC 與防火牆怎麼搭配

已移到 [Linode VPC(私有網路)](Linode_VPC(私有網路).md#vpc-與防火牆怎麼搭配)。

## 與主機內防火牆（ufw / iptables / firewalld）搭配

兩者可以同時用，官方也建議搭配：

| | Cloud Firewall | 主機防火牆（nftables / iptables / ufw / firewalld） |
|---|---|---|
| 位置 | 封包到達主機**之前** | 主機 kernel 內 |
| 設定方式 | Cloud Manager / CLI / API，一組規則套多台 | 每台主機各自用指令設定 |
| 適合 | 粗粒度擋外：開哪些 port、哪些來源 | 細規則、與 fail2ban 等軟體整合 |
| 處理順序 | **Inbound 先過 Cloud Firewall** | **Outbound 先過主機防火牆** |

排查連不到時兩層都要看：Cloud Firewall 放行了，主機的 ufw/iptables 還可能擋；Docker 發布的 port 會繞過 ufw 直接寫 iptables，對外的保護要靠 Cloud Firewall。

# 網路操作（舊筆記）

## 測試連通性 網路

測試連通性
通過 SSH 登錄您的 Linode。

使用該ip工具確保應用了您在上面設置的地址：

root@localhost:~# ip addr | grep inet
inet 127.0.0.1/8 scope host lo
inet6 ::1/128 scope host
inet 198.51.100.5/24 brd 198.51.100.255 scope global eth0
inet6 2600:3c02::f03c:91ff:fe24:3a2f/64 scope global
inet6 fe80::f03c:91ff:fe24:3a2f/64 scope link
確認您的/etc/resolv.conf存在且正確。其內容將根據 Linux 發行版而有所不同。

root@localhost:~# cat /etc/resolv.conf
nameserver 8.8.8.8
nameserver 2001:4860:4860::8888
domain members.linode.com
options rotate
嘗試 ping 某些內容以確認您已通過 IPv4 和 IPv6 實現完全連接。

ping -c 3 google.com
ping6 -c 3 ipv6.google.com

## Linode 添加私有IP（舊做法，需隔離請改用 VPC）

配置設定
https://www.linode.com/docs/guides/linux-static-ip-configuration/
添加IP
https://www.linode.com/docs/guides/managing-ip-addresses/#adding-private-ip-addresses


配置設定
    vim /etc/sysconfig/network-scripts/ifcfg-eth0

    # Add a private IPv4 address.
    IPADDR1=192.168.143.152
    PREFIX1=17

應用網路設定
    重開機
    或
    centos 7
        systemctl restart network.service
        systemctl restart network

檢測是否有通
    curl -v ip:port

## Linode 換ip

創建 Create一個最小的方案主機 5美金
更換 與要更換ip的主機進行更換 Linodes -> Network -> IP Transfer
重啟 更換後的主機reboot
刪除 最小方案主機Delete

# VPC

VPC 內容（私有網路比較、規格限制、建立與加入、個別設定、防火牆搭配、linode-cli）已獨立成 [Linode VPC(私有網路)](Linode_VPC(私有網路).md)。
