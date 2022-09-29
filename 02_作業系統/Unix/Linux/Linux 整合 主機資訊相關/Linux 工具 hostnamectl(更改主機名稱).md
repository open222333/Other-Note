# Linux 工具 hostnamectl(更改主機名稱)

```
```

## 目錄

- [Linux 工具 hostnamectl(更改主機名稱)](#linux-工具-hostnamectl更改主機名稱)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [基本指令](#基本指令)

## 參考資料

[Red Hat / CentOS Linux 7 查詢、更改主機名稱設定教學](https://blog.gtwang.org/linux/redhat-centos-7-change-hostname-tutorial/)

# 基本指令

```bash
# 顯示主機名稱
hostname -s

# 顯示主機的 FQDN
hostname -f

# 顯示主機所有的 FQDN
hostname -A

# 顯示主機的 DNS 網域名稱
hostname -d


# 更改主機名稱：
# 顯示目前主機名稱設定
hostnamectl

# 更改主機名稱設定
hostnamectl set-hostname myhost.gtwang.org

# 若需要設定主機的別名（alias），可以參考 hosts 的線上手冊，直接更改 /etc/hosts 的設定。

# 閱讀 hosts 的線上手冊
man hosts
```