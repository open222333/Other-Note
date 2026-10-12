# Linode 筆記

## 目錄

- [Linode 筆記](#linode-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [Linodes](#linodes)
- [Firewall](#firewall)
- [VPC](#vpc)
	- [私有網路選項比較（VPC / VLAN / Private IP）](#私有網路選項比較vpc--vlan--private-ip)
	- [規格與限制](#規格與限制)
	- [建立 VPC 與子網路](#建立-vpc-與子網路)
	- [把 Linode 加入 VPC](#把-linode-加入-vpc)
	- [linode-cli 指令](#linode-cli-指令)

## 參考資料

[官方文檔](https://www.linode.com/docs/)

[VPC 總覽（可用區域、規格限制）](https://techdocs.akamai.com/cloud-computing/docs/vpc)

[Create a VPC](https://techdocs.akamai.com/cloud-computing/docs/create-a-vpc)

[Assign a Linode to a VPC](https://techdocs.akamai.com/cloud-computing/docs/assign-a-compute-instance-to-a-vpc)

[Configure a forward proxy to enable internet access within a VPC](https://techdocs.akamai.com/cloud-computing/docs/forward-proxy-for-vpc)

[Linode API - VPCs](https://techdocs.akamai.com/linode-api/reference/post-vpc)

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

##################################
            測試連通性 網路
##################################
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

##################################
        Linode 添加私有IP
##################################
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

##################################
        Linode 換ip
##################################
創建 Create一個最小的方案主機 5美金
更換 與要更換ip的主機進行更換 Linodes -> Network -> IP Transfer
重啟 更換後的主機reboot
刪除 最小方案主機Delete

# VPC

```
VPC（Virtual Private Cloud）是同一個資料中心內的私有網路（OSI 第 3 層），
VPC 內的封包與公網、以及同資料中心內不屬於這個 VPC 的其他服務隔離。
可切多個子網路（subnet），子網路之間自動互通。
VPC 本身免費，VPC 內部流量不計入每月網路傳輸量。
```

## 私有網路選項比較（VPC / VLAN / Private IP）

| 項目 | VPC | VLAN | Private IP |
|---|---|---|---|
| OSI 層級 | 第 3 層（內建 IP 定址與路由） | 第 2 層（可跑任何第 3 層協定，但路由功能受限） | 第 3 層 |
| 與其他租戶隔離 | 是 | 是 | 否：同區域內有 Private IP 的 Linode 都能互連（共用 `192.168.128.0/17`） |
| 多個子網路 | 可（每個 VPC 最多 10 個） | 否（只能用一個指定網段） | 否 |
| 費用 | 免費 | 免費 | 免費 |

需要真正隔離內網時用 VPC，不要再用 Private IP（上面「Linode 添加私有IP」是舊做法）。

## 規格與限制

- 一個 VPC 最多 500 台 Linode；每個資料中心預設最多 10 個 VPC；每個 VPC 最多 10 個子網路。
- VPC 只存在於單一資料中心，不同資料中心的 Linode 不能在同一個 VPC。
- 不支援 VPC peering（同資料中心或跨資料中心都不行）。
- VPC 流量與其他租戶隔離，但**沒有加密**。
- VPC 介面不能用 IPv6，VPC IP 不能用 IP sharing / IP transfer（上面的「換 ip」做法不適用）。
- 一台 Linode 只能加入一個 VPC 的一個子網路，不能有多個 VPC 介面；但可以與同 VPC 其他子網路的 Linode 互通。
- 只有部分核心區域支援，例如 Tokyo 3（`jp-tyo-3`）、Osaka（`jp-osa`）、Singapore 2（`sg-sin-2`）、Los Angeles（`us-lax`）等；2026-04-22 起 distributed regions 不再支援 VPC。最新清單見參考資料中的官方文件。

## 建立 VPC 與子網路

```
Cloud Manager → Create → VPC

Region：VPC 所在資料中心（只有同資料中心的服務能加入）
Label：英數字與連字號（不能連續兩個 --）
Subnets：至少 1 個，最多 10 個
  Subnet Label：同一 VPC 內不可重複
  Subnet IP Address Range：
    - 必須是 RFC1918 私有網段（10.0.0.0/8、172.16.0.0/12、192.168.0.0/16）
    - 前綴長度 /1 ~ /29
    - 不能與 192.168.128.0/17 重疊（保留給 Linode Private IP）
    - 不能與同 VPC 其他子網路重疊
```

子網路建立後仍可新增、修改、刪除。

## 把 Linode 加入 VPC

三種方式：

```
1. 建立新 Linode 時，在 VPC 區塊直接指定 VPC 與子網路（VPC 自動設在 eth0）
2. VPC 詳細頁 → Subnets → 該子網路 ⋯ → Assign Linodes
3. 直接編輯 Linode 的網路介面（既有 Linode 建議用這個）
   - Linode Interfaces：Network 分頁 → Add Network Interface → VPC（需先關機）
   - Configuration Profile Interfaces（舊版）：Configurations 分頁 → Edit → Networking
     → eth0 選 VPC，其他介面刪掉；並確認 Network Helper（auto-configure networking）開啟
```

設定項目：

| 項目 | 說明 |
|---|---|
| VPC / Subnet | 每台 Linode 只能選一個 VPC 的一個子網路 |
| VPC IPv4 address | 預設自動分配；手動指定時不能用子網路的**前兩個與最後兩個** IP（保留給路由等用途） |
| Assign a public IPv4 address | 預設 VPC 內的 Linode **無法連外網**；勾選後在 VPC 介面上做 1:1 NAT，透過該 Linode 的公網 IP 對外 |
| Additional IPv4 ranges | 額外配給這台 Linode 的網段，例如讓 Docker 容器直接在 VPC 中可被存取 |

注意：

- VPC 要設在 `eth0`（主要介面），設在其他介面會導致無法與同 VPC 其他子網路通訊。
- 需要連外網時，用 1:1 NAT 或架一台 forward proxy；**不建議**另外再加一個公網介面（用「Assign Linodes」方式加入時會自動多出 `eth1` 公網介面，建議到設定檔刪掉）。
- 既有 Linode 加入或移出 VPC 後，要重開機（Configuration Profile）或關機後再開機（Linode Interfaces）才會生效。
- 有套用 Cloud Firewall 的 Linode，規則同樣會套用在 VPC 介面上——VPC 內部互連也會被防火牆過濾，記得放行內網流量。
- NodeBalancer 可以透過 VPC 連到後端 Linode；Managed Database 也能部署在 VPC 內（Beta），兩者都必須與 VPC 在同一個資料中心。

## linode-cli 指令

```bash
# 建立 VPC（同時建立第一個子網路）
linode-cli vpcs create \
  --label my-vpc \
  --region jp-tyo-3 \
  --description "app 內網" \
  --subnets.label app-subnet \
  --subnets.ipv4 10.0.1.0/24

# 列出 / 查看 VPC
linode-cli vpcs list
linode-cli vpcs view $VPC_ID

# 在既有 VPC 新增子網路
linode-cli vpcs subnet-create $VPC_ID \
  --label db-subnet \
  --ipv4 10.0.2.0/24

# 列出子網路、刪除子網路、刪除 VPC
linode-cli vpcs subnets-list $VPC_ID
linode-cli vpcs subnet-delete $VPC_ID $SUBNET_ID
linode-cli vpcs delete $VPC_ID

# 查看所有可用的 vpcs 子指令（指令名稱以實際版本為準）
linode-cli vpcs --help
```

進入 Linode 後確認 VPC 介面：

```bash
ip addr show eth0
# 測試同 VPC 另一台的內網 IP
ping -c 3 10.0.1.3
```
