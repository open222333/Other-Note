# Linux 工具 ip-route(顯示和管理 Linux 系統的 IP 路由表)

```
Linux 網絡配置工具集合中的一部分，用於檢視和操作系統的路由表，並指定數據包的路由
```

## 目錄

- [Linux 工具 ip-route(顯示和管理 Linux 系統的 IP 路由表)](#linux-工具-ip-route顯示和管理-linux-系統的-ip-路由表)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
	- [IPv6 路由表（ip -6 route）](#ipv6-路由表ip--6-route)

## 參考資料

[ip-route(8) — Linux manual page](https://man7.org/linux/man-pages/man8/ip-route.8.html)

# 指令

```bash
# 顯示系統的 IP 路由表
ip route show

# 添加一個新的路由
# <network>/<mask> 指定目的網絡和網絡掩碼。
# via <gateway> 指定下一跳的 IP 地址。
# dev <interface> 指定出口接口。
ip route add <network>/<mask> via <gateway> dev <interface>

# 刪除現有的路由
ip route del <network>/<mask>

# 獲取特定目的地的路由信息
ip route get <destination>

# 替換現有的路由
ip route replace <network>/<mask> via <gateway> dev <interface>

# 清除路由緩存
ip route flush cache
```

## IPv6 路由表（ip -6 route）

`ip -6 route` **本來就看不到本機的 IPv6 位址**，因為 IPv4 和 IPv6 路由表記錄的東西不一樣。

IPv4 會看到本機位址：設定位址時系統自動加一條「本機所在網段」的路由，並附上 `src <本機位址>`：

```text
203.0.113.0/24 dev eth0 proto kernel scope link src 203.0.113.5
```

IPv6 看不到的原因：

- IPv6 路由一般**不帶 `src`**：來源位址是連線當下依規則（RFC 6724）挑的，不寫在路由表。
- 位址若標著 `noprefixroute`，系統**不會**為該網段（例 `2001:db8::/64`）加路由，路由表裡連這個網段都沒有：

```text
$ ip -6 addr
    inet6 2001:db8::5/64 scope global dynamic mngtmpaddr noprefixroute

$ ip -6 route
default via fe80::1 dev eth0 proto ra metric 1024    ← 預設閘道（link-local），IPv6 流量都從這出去
fe80::/64 dev eth0 proto kernel metric 256           ← 每張網卡各自的 link-local
fe80::/64 dev docker0 proto kernel metric 256        ← docker0、br-*、veth* 是 Docker 建的虛擬網卡
```

查本機 IPv6 與實際使用的來源位址：

```bash
ip -6 addr show scope global          # 本機對外的 IPv6
ip -6 route get 2001:db8::20          # 連某個 IPv6 時走哪張網卡、用哪個來源（看 src）
```

| 想知道 | IPv4 | IPv6 |
|---|---|---|
| 本機位址 | `ip route` 的 `src`，或 `ip -4 addr` | 只能用 `ip -6 addr show scope global` |
| 連某個位址用哪個來源 | `ip route get <IPv4>` | `ip -6 route get <IPv6>` |
