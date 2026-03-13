# Linux 工具 sshfs(掛載遠端主機的目錄)

```
透過 SSH 協定來掛載遠端主機的目錄到本地文件系統，使得遠端目錄就像本地目錄一樣可以進行訪問和操作。
```

## 目錄

- [Linux 工具 sshfs(掛載遠端主機的目錄)](#linux-工具-sshfs掛載遠端主機的目錄)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[sshfs(1) — Linux manual page](https://man7.org/linux/man-pages/man1/sshfs.1.html)

# 安裝

```bash
# 在 CentOS 7 上安裝 sshfs
yum install -y sshfs

# 在 Ubuntu 或 Debian 上安裝 sshfs
apt-get update
apt-get install -y sshfs

# 在 Fedora 上安裝 sshfs
dnf install -y sshfs

# 在 openSUSE 上安裝 sshfs
zypper install -y sshfs
```

# 指令

```bash
# 創建本地目錄作為掛載點
mkdir /mnt/remote-dir

# 使用 sshfs 掛載遠端主機目錄到本地掛載點
# 請將 {username} 替換為遠端主機上的有效用戶名
# 請將 {remote_host_ip} 替換為遠端主機的 IP 地址
# 請將 {/path/to/remote/directory} 替換為遠端目錄的路徑
sshfs {username}@{remote_host_ip}:/path/to/remote/directory /mnt/remote-dir

# 訪問遠端目錄
ls /mnt/remote-dir

# 卸載遠端目錄
# 總結來說，umount 是 Linux 的內建指令，用於卸載檔案系統，而 fusermount -u 則是針對 FUSE 檔案系統的卸載指令。
# 如果使用 sshfs 等 FUSE 檔案系統進行掛載，則使用 fusermount -u 來卸載；如果是普通的檔案系統，則使用 umount 來卸載。
umount /mnt/remote-dir
fusermount -u /mnt/remote-dir
```
