# Linux 工具 nfs-utils nfs-kernel-server(Network File System(NFS)遠端掛載)

```
在遠端主機上設定 NFS 伺服器並共享目錄，讓 NFS 客戶端可以訪問
```

## 目錄

- [Linux 工具 nfs-utils nfs-kernel-server(Network File System(NFS)遠端掛載)](#linux-工具-nfs-utils-nfs-kernel-servernetwork-file-systemnfs遠端掛載)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [fstab相關](#fstab相關)
- [安裝](#安裝)
- [指令](#指令)
	- [自動掛載](#自動掛載)

## 參考資料

[NFS-NetworkFileShare(NFS伺服器-檔案分享)](../../../../../03_伺服器服務/FileServer(檔案伺服器)/NFS-NetworkFileShare(NFS伺服器-檔案分享).md)

### fstab相關

[Fstab](https://help.ubuntu.com/community/Fstab)

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
# rw 表示允許讀寫訪問, ro 表示唯讀模式
# sync 表示同步寫入
# no_root_squash 表示允許 root 使用者以其真實身份訪問共享目錄

# 重新載入 NFS 配置
exportfs -r

# /etc/fstab 檔案，然後執行以下指令來重新掛載
mount -a

# 手動掛載
# 將 {remote_host_ip} 替換為遠端主機的 IP 地址
# 將 {nfs_export_path} 替換為遠端目錄在遠端主機上的路徑
mount -t nfs {remote_host_ip}:{nfs_export_path} /mnt/nfs-remote-dir
```

## 自動掛載

```bash
vim /etc/fstab
{remote_host_ip}:{nfs_export_path} /mnt/nfs-remote-dir     nfs    ro    0 0
{remote_host_ip}:{nfs_export_path} /mnt/nfs-remote-dir     nfs auto,nofail,noatime,nolock,intr,tcp,actimeo=1800 0 0
# nfs：掛載的類型，表示這是一個 NFS 掛載。
# auto：指示系統在開機時自動掛載該 NFS 資源。
# nofail：表示即使無法成功掛載，也不阻礙開機進行。
# noatime：取消記錄檔案的訪問時間，避免寫入記錄檔案的操作，減少對伺服器的 I/O 負擔。
# nolock：禁用使用 NLM（Network Lock Manager）。
# intr：允許中斷掛載，可以在掛載期間進行中斷動作。
# tcp：使用 TCP 協議來進行通信。
# actimeo=1800：設定 NFS 文件屬性的快取時間為 1800 秒（30 分鐘）。
# 0 0：其他掛載選項，通常設定為 0 0。

```