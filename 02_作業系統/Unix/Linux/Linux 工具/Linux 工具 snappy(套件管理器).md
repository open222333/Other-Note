# Linux 工具 snappy(套件管理器)

```
```

## 目錄

- [Linux 工具 snappy(套件管理器)](#linux-工具-snappy套件管理器)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[Installing snapd](https://snapcraft.io/docs/installing-snapd)

# 指令

```bash
# 將EPEL存儲庫添加到CentOS 8系統：
dnf install epel-release
dnf upgrade

# 安裝snapd包
yum install snapd -y


# 啟用管理主snap通信套接字的systemd單元
systemctl enable --now snapd.socket


# error: cannot install "certbot": classic confinement requires snaps under /snap or symlink from /snap to /var/lib/snapd/snap
# 要啟用經典snap 支持，請輸入以下內容以在/var/lib/snapd/snap和之間創建符號鏈接/snap：
ln -s /var/lib/snapd/snap /snap
```