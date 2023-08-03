# Linux 工具 mtr(網絡診斷工具)

```
MTR 是一個跨平台、簡單易用的命令列網路檢測工具，其結合了 ping 與 traceroute 兩個指令的功能，並提供了更詳細的資訊

以 traceroute 找出中間的每一個網路節點（閘道器、路由器、橋接器等），然後使用 ping 去檢查每一個節點的網路連線狀況
```

## 目錄

- [Linux 工具 mtr(網絡診斷工具)](#linux-工具-mtr網絡診斷工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[mtr(8) - Linux man page](https://linux.die.net/man/8/mtr)

# 安裝

```bash
# 使用 apt 安裝
apt install mtr

# 使用 yum 安裝
yum install mtr -y

# 使用 dnf 安裝
dnf install mtr -y
```

# 指令

```bash
# 若要離開 MTR 的畫面，可以按下 q 鍵，或是按下 Ctrl + c
mtr [domain | ip]

# -n 以 IP 位址表示節點
mtr -n www.google.com.tw

# -b 同時顯示網域名稱與 IP 位址
mtr -b www.google.com.tw
```
