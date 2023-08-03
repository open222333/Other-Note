# Linux 工具 wget(自動檔案下載工具)

```
```

## 目錄

- [Linux 工具 wget(自動檔案下載工具)](#linux-工具-wget自動檔案下載工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[使用 wget 完成批量下載](https://www.itread01.com/p/188595.html)

[manpage](https://linux.die.net/man/1/wget)

# 安裝

```bash
# 在 Ubuntu 或 Debian 上：
apt-get update && apt-get install wget -y

# 在 CentOS 或 Red Hat 上：
yum install wget -y

# 在 Fedora 上：
dnf install wget -y

# 在 openSUSE 上：
zypper install wget -y

```

# 指令

```bash
# 將filename.txt檔案內的地址 逐行下載
wget -i filename.txt
```
