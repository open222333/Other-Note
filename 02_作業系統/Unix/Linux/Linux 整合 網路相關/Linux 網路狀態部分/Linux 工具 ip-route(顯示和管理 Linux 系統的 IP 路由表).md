# Linux 工具 ip-route(顯示和管理 Linux 系統的 IP 路由表)

```
Linux 網絡配置工具集合中的一部分，用於檢視和操作系統的路由表，並指定數據包的路由
```

## 目錄

- [Linux 工具 ip-route(顯示和管理 Linux 系統的 IP 路由表)](#linux-工具-ip-route顯示和管理-linux-系統的-ip-路由表)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

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
