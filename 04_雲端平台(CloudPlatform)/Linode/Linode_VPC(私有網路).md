# Linode VPC(私有網路)

```
VPC（Virtual Private Cloud）是同一個資料中心內的私有網路（OSI 第 3 層），
VPC 內的封包與公網、以及同資料中心內不屬於這個 VPC 的其他服務隔離。
可切多個子網路（subnet），子網路之間自動互通。
VPC 本身免費，VPC 內部流量不計入每月網路傳輸量。
```

## 目錄

- [Linode VPC(私有網路)](#linode-vpc私有網路)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [私有網路選項比較（VPC / VLAN / Private IP）](#私有網路選項比較vpc--vlan--private-ip)
  - [規格與限制](#規格與限制)
  - [建立 VPC 與子網路](#建立-vpc-與子網路)
  - [把 Linode 加入 VPC](#把-linode-加入-vpc)
  - [每台 Linode 可個別設定的內網項目](#每台-linode-可個別設定的內網項目)
  - [VPC 與防火牆怎麼搭配](#vpc-與防火牆怎麼搭配)
  - [設定步驟（Cloud Manager）](#設定步驟cloud-manager)
  - [linode-cli 指令](#linode-cli-指令)

## 參考資料

[VPC 總覽（可用區域、規格限制）](https://techdocs.akamai.com/cloud-computing/docs/vpc)

[Create a VPC](https://techdocs.akamai.com/cloud-computing/docs/create-a-vpc)

[Assign a Linode to a VPC](https://techdocs.akamai.com/cloud-computing/docs/assign-a-compute-instance-to-a-vpc)

[Manage Linode interfaces（編輯 VPC 介面：IP、1:1 NAT、額外網段、防火牆）](https://techdocs.akamai.com/cloud-computing/docs/managing-linode-interfaces)

[Configure a forward proxy to enable internet access within a VPC](https://techdocs.akamai.com/cloud-computing/docs/forward-proxy-for-vpc)

[Linode Cloud Firewalls（VPC 範本）](https://techdocs.akamai.com/cloud-computing/docs/cloud-firewall)

[Linode API - VPCs](https://techdocs.akamai.com/linode-api/reference/post-vpc)

[Linode 筆記（網路介面、Cloud Firewall）](Linode_筆記.md)

[Linode 工具 linode-cli 筆記](Linode_工具_linode-cli(Linode（Akamai_Cloud）官方命令列工具).md)

## 私有網路選項比較（VPC / VLAN / Private IP）

| 項目 | VPC | VLAN | Private IP |
|---|---|---|---|
| OSI 層級 | 第 3 層（內建 IP 定址與路由） | 第 2 層（可跑任何第 3 層協定，但路由功能受限） | 第 3 層 |
| 與其他租戶隔離 | 是 | 是 | 否：同區域內有 Private IP 的 Linode 都能互連（共用 `192.168.128.0/17`） |
| 多個子網路 | 可（每個 VPC 最多 10 個） | 否（只能用一個指定網段） | 否 |
| 費用 | 免費 | 免費 | 免費 |

需要真正隔離內網時用 VPC，不要再用 Private IP（[Linode 筆記](Linode_筆記.md)裡「Linode 添加私有IP」是舊做法）。

## 規格與限制

- 一個 VPC 最多 500 台 Linode；每個資料中心預設最多 10 個 VPC；每個 VPC 最多 10 個子網路。
- VPC 只存在於單一資料中心，不同資料中心的 Linode 不能在同一個 VPC。
- 不支援 VPC peering（同資料中心或跨資料中心都不行）。
- VPC 流量與其他租戶隔離，但**沒有加密**。
- VPC 介面不能用 IPv6，VPC IP 不能用 IP sharing / IP transfer（[Linode 筆記](Linode_筆記.md)裡「換 ip」的做法不適用於 VPC IP）。
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
| VPC IPv4 address | 預設自動分配；**可以手動指定**：取消勾選「Auto-assign a VPC IPv4」後填入。IP 必須在子網路網段內，且子網路的前兩個與最後兩個位址保留給路由等用途不能用（例：`10.0.1.0/24` 可用 `10.0.1.2` ~ `10.0.1.253`） |
| Assign a public IPv4 address | 預設 VPC 內的 Linode **無法連外網**；勾選後在 VPC 介面上做 1:1 NAT，透過該 Linode 的公網 IP 對外 |
| Additional IPv4 ranges | 額外配給這台 Linode 的網段，例如讓 Docker 容器直接在 VPC 中可被存取 |

注意：

- VPC 要設在 `eth0`（主要介面），設在其他介面會導致無法與同 VPC 其他子網路通訊。
- 需要連外網時，用 1:1 NAT 或架一台 forward proxy；**不建議**另外再加一個公網介面（用「Assign Linodes」方式加入時會自動多出 `eth1` 公網介面，建議到設定檔刪掉）。
- 既有 Linode 加入或移出 VPC 後，要重開機（Configuration Profile）或關機後再開機（Linode Interfaces）才會生效。
- 有套用 Cloud Firewall 的 Linode，規則同樣會套用在 VPC 介面上——VPC 內部互連也會被防火牆過濾，記得放行內網流量（搭配方式見下方「VPC 與防火牆怎麼搭配」）。
- NodeBalancer 可以透過 VPC 連到後端 Linode；Managed Database 也能部署在 VPC 內（Beta），兩者都必須與 VPC 在同一個資料中心。

## 每台 Linode 可個別設定的內網項目

可以。VPC 的設定單位是「每台 Linode 的 VPC 介面」，每台都能不同：

| 項目 | 個別設定 | 說明 |
|---|---|---|
| 加入哪個 VPC / 子網路 | 可 | 每台只能一個 VPC 的一個子網路 |
| VPC IPv4 位址 | 可 | 自動分配，或取消勾選 Auto-assign 後手動填入；只要避開子網路頭尾各兩個保留位址（例：`10.0.1.0/24` 可用 `.2` ~ `.253`） |
| 對外上網（1:1 NAT） | 可 | 每台各自勾「Allow public IPv4 access」，沒勾就完全不能連外網 |
| 額外 IPv4 網段 | 可 | 例如讓該台的 Docker 容器網段直接在 VPC 中被存取 |
| 防火牆 | Linode Interfaces 才可 | VPC 介面可以掛和 public 介面**不同**的防火牆；舊式 Configuration Profile 是整台共用一個 |
| 預設路由 | Linode Interfaces 才可 | IPv4 預設路由走 VPC 或 Public 介面，每台自己選 |

做不到的：

- 子網路層級的存取控制（像 AWS 的 NACL / Security Group 綁子網路）：同 VPC 子網路一律互通，隔離只能靠每台 VPC 介面的防火牆。
- 一台掛多個 VPC、VPC peering、跨資料中心的 VPC。
- VPC 介面用 IPv6。

修改時機：Linode Interfaces 改 VPC 介面（IP、NAT、額外網段、防火牆）前，若有開 Network Helper 要先關機，改完開機生效；Configuration Profile 改完重開機生效。

## VPC 與防火牆怎麼搭配

兩種網路介面（Linode Interfaces / Configuration Profile）的差異，以及 Cloud Firewall 的通用規則與限制，見 [Linode 筆記](Linode_筆記.md)。

Linode 沒有獨立的「VPC 防火牆」產品，VPC 用的也是 Cloud Firewall，差別只在**掛在哪裡**：

```
Linode Interfaces（可分開設定）
  Public 介面  ← 掛「公網防火牆」：只開 80/443、限 IP 的 22
  VPC 介面     ← 掛「內網防火牆」：只放行 VPC 網段需要的 port

Configuration Profile（只能一組）
  整台 Linode  ← 一個防火牆同時管 public + VPC
               規則要同時寫公網規則 + 內網放行（例如 source 10.0.0.0/16）
```

重點：

- **VPC 內部流量會被防火牆過濾**。Inbound 設 Drop 卻沒放行 VPC 網段，內網就會互相連不到——這是最常見的坑。
- 官方範本：VPC 範本放行 SSH、必要的網路控制流量、**整個 VPC 網段的 inbound**；Public 範本只放行 SSH 和網路控制流量。範本的 IP 範圍偏寬，建議收窄。
- **子網路不是安全邊界**：同一 VPC 的不同子網路預設互通，要隔離（例如 DB 子網只讓 Web 子網連）必須靠各 Linode VPC 介面上的防火牆規則。

設計範例（Linode Interfaces，同一 VPC 分兩個子網路 web `10.0.1.0/24`、db `10.0.2.0/24`）：

**做法 A（官方建議）：web 只掛 VPC 介面，對外用 1:1 NAT**

官方不建議同時掛 VPC 介面和 public 介面。web 只掛一個 VPC 介面並開 1:1 NAT，從公網 IP 進來的流量也走這個介面，所以**同一個防火牆要同時寫公網規則與內網規則**。

| 主機 | 介面 | 防火牆 | Inbound（Policy: Drop） |
|---|---|---|---|
| web | VPC + 1:1 NAT | fw-web | TCP 80, 443 from `0.0.0.0/0`、`::/0`；TCP 22 from 管理 IP/32 |
| db | VPC（不開 NAT） | fw-db | TCP 3306 from `10.0.1.0/24`（只讓 web 子網）；TCP 22 from 跳板 IP/32 |

**做法 B：web 同時掛 public 介面與 VPC 介面，兩個防火牆分開**

可以做，但官方不建議（路由與預設路由要自己留意）。好處是公網規則和內網規則分在兩個防火牆。

| 主機 | 介面 | 防火牆 | Inbound（Policy: Drop） |
|---|---|---|---|
| web | Public | fw-web-public | TCP 80, 443 from 0.0.0.0/0；TCP 22 from 管理 IP/32 |
| web | VPC | fw-web-vpc | 監控 port from 監控主機；TCP 22 from 跳板 IP/32 |
| db | VPC | fw-db | 同做法 A |

兩種做法的 db 都不需要公網：要對外更新套件時，暫時開 VPC 介面的 1:1 NAT，或走 forward proxy。

## 設定步驟（Cloud Manager）

以做法 A 為例。

**1. 確認網路介面類型**

```
Administration → Account Settings → 新 Linode 使用的網路介面：選 Linode Interfaces
（之後建立的 Linode 才能每個介面各掛一個防火牆）

既有 Linode 若是 Configuration Profile：
  - 可以升級成 Linode Interfaces，但不可逆，且升級後不能用 Private IP
  - 不升級的話，防火牆只能整台掛一個，規則要同時包含公網與內網
```

**2. 建立 VPC 與子網路**

```
Create → VPC
  Region：與 Linode 相同的資料中心
  Subnets：web-subnet 10.0.1.0/24、db-subnet 10.0.2.0/24
```

**3. 建立防火牆**

```
Firewalls → Create Firewall（可先選 VPC 或 Public 範本，再修改）
  Label：fw-web / fw-db
  Rules 頁：
    Inbound Policy：Drop
    Outbound Policy：Accept
    Add an Inbound Rule：依上表逐條新增（Sources 選 IP/Netmask，填 CIDR）
  最後按 Save Changes（沒按不會生效）
```

可在 Account Settings → **Default Firewalls** 把 VPC 介面、Public 介面的預設防火牆設好，之後新建的介面會自動套用。

**4. 每台 Linode 設定 VPC 介面**

```
Linodes → 選主機 → Power Off（開了 Network Helper 時必須先關機）
Network 分頁
  還沒有 VPC 介面：Add Network Interface → VPC
  已有 VPC 介面：該列 ⋯ → Edit
設定：
  VPC / Subnet：web 選 web-subnet；db 選 db-subnet
  Auto-assign a VPC IPv4：取消勾選並填固定 IP，例如 web 10.0.1.10、db 10.0.2.10
  Allow public IPv4 access (1:1 NAT)：web 勾；db 不勾
  Firewall：web 選 fw-web；db 選 fw-db
Save → Power On
```

Configuration Profile 的舊式 Linode 改法：Configurations 分頁 → Edit → eth0 選 VPC 並設定 IP；防火牆到 Firewalls → 該防火牆 → Linodes 分頁 → Add Linodes to Firewall；最後重開機。

**5. 進主機驗證**

```bash
ip addr show                 # 確認 VPC IP（例如 10.0.2.10）已套用
ping -c 3 10.0.2.10          # web → db 內網互通（ICMP 需在 fw-db 放行，否則 ping 不通屬正常）
nc -zv 10.0.2.10 3306        # web → db 3306 應通
curl -I http://<web 公網 IP>  # 公網 → web 80 應通
```

從其他子網或外部連 db 3306 應該不通，才代表隔離生效。連不到時兩層都要查：Cloud Firewall 規則，以及主機內的 ufw / iptables（見 [Linode 筆記 → Firewall](Linode_筆記.md#firewall)）。

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
