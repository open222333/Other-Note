# Linux 工具 nfs-utils nfs-kernel-server(Network File System(NFS)遠端掛載)

```
在遠端主機上設定 NFS 伺服器並共享目錄，讓 NFS 客戶端可以訪問
```

## 目錄

- [Linux 工具 nfs-utils nfs-kernel-server(Network File System(NFS)遠端掛載)](#linux-工具-nfs-utils-nfs-kernel-servernetwork-file-systemnfs遠端掛載)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[NFS-NetworkFileShare(NFS伺服器-檔案分享)](../../../../../03_伺服器服務/FileServer(檔案伺服器)/NFS-NetworkFileShare(NFS伺服器-檔案分享).md)

# 安裝

```bash
# 在 CentOS 或 Red Hat 上安裝 NFS
yum install -y nfs-utils

# 在 Ubuntu 或 Debian 上安裝 NFS
apt-get update
apt-get install -y nfs-kernel-server

# 在 Fedora 上安裝 NFS
dnf install -y nfs-utils

# 在 openSUSE 上安裝 NFS
zypper install -y nfs-client
```

# 指令

```bash
# 對於 CentOS、Red Hat、Fedora
systemctl start nfs-server
systemctl enable nfs-server

# 對於 Ubuntu、Debian
systemctl start nfs-kernel-server
systemctl enable nfs-kernel-server

# 配置 NFS 伺服器：
# 編輯 /etc/exports 檔案，將要共享的目錄添加到該檔案中，並指定允許訪問的客戶端 IP 或網段。例如：
vim /etc/exports
/export/shared    192.168.1.0/24(rw,sync,no_root_squash)
# 在此範例中，/export/shared 是要共享的目錄，192.168.1.0/24 是允許訪問的客戶端 IP 範圍
# rw 表示允許讀寫訪問
# sync 表示同步寫入
# no_root_squash 表示允許 root 使用者以其真實身份訪問共享目錄

# 重新載入 NFS 配置
exportfs -r

# 將 {remote_host_ip} 替換為遠端主機的 IP 地址
# 將 {nfs_export_path} 替換為遠端目錄在遠端主機上的路徑
mount -t nfs {remote_host_ip}:{nfs_export_path} /mnt/nfs-remote-dir
```
