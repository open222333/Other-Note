# Linux 工具 ifconfig(查詢和設定網路介面卡)

```
ifconfig 指令是Interface Configuration 的縮寫，為Linux/Unix 系統中用來查詢與控制網路介面卡的指令。
```

## 目錄

- [Linux 工具 ifconfig(查詢和設定網路介面卡)](#linux-工具-ifconfig查詢和設定網路介面卡)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[ifconfig(8) — Linux manual page](https://man7.org/linux/man-pages/man8/ifconfig.8.html)

[[Linux] ifconfig 查詢和設定網路介面卡](https://pjchender.dev/app/app-linux-ifconfig/)

[Linux 網路設定 ifconfig 指令使用方法教學與範例](https://officeguide.cc/linux-configure-network-interface-ifconfig-command-tutorial-examples/)

# 安裝

```bash
# 安裝 ifconfig 工具（Ubuntu/Debian）
apt install net-tools

# 安裝 ifconfig 工具（CentOS/RHEL）
dnf install net-tools
```

# 指令

```bash
# 查看目前啟用的網路介面
ifconfig

# 修改網卡設定
# /etc/network/interfaces：在 Debian 和 Ubuntu 等基於 Debian 的發行版中使用。
# /etc/sysconfig/network-scripts/ifcfg-<interface>：在 CentOS 和 Fedora 等基於 Red Hat 的發行版中使用。
# 重啟後 使用新設定
systemctl restart network
```
