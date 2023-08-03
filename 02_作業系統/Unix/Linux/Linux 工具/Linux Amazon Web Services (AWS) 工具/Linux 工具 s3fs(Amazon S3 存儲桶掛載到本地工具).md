# Linux 工具 s3fs(Amazon S3 存儲桶掛載到本地工具)

```
S3fs:Amazon S3或Amazon Simple Storage Service

s3fs 工具是用於將 Amazon S3 存儲桶掛載到本地文件系統的工具
```

## 目錄

- [Linux 工具 s3fs(Amazon S3 存儲桶掛載到本地工具)](#linux-工具-s3fsamazon-s3-存儲桶掛載到本地工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[how-do-i-use-s3fs-on-linode-object-storage](https://www.linode.com/community/questions/18817/)

[Install s3fs on Centos 7](https://pikedom.com/install-s3fs-on-centos-7/)

[s3fs 的command](https://github.com/s3fs-fuse/s3fs-fuse/wiki/Fuse-Over-Amazon#use_cache-default-which-means-disabled)

# 安裝

```bash
# 在 Ubuntu 或 Debian 上：
apt-get update && apt-get install s3fs -y

# 在 CentOS 或 Red Hat 上：
# 安裝相關套件
yum install fuse -y
# 安裝 EPEL 存儲庫
yum install epel-release -y
# 然後安裝s3fs
yum install s3fs-fuse -y
# 將公鑰和私鑰複製到~/.passwd-s3fs
echo 公鑰:私鑰 > ~/.passwd-s3fs
# 更改檔案權限
chmod -v 600 ~/.passwd-s3fs

# 在 Fedora 上：
dnf install s3fs -y

# 在 openSUSE 上：
zypper install s3fs -y
```

# 指令

```bash
# 使用 s3fs 工具掛載 S3 存儲桶到本地文件系統
# 請將 {mybucket} 替換為您的 S3 存儲桶名稱，{/path/to/mountpoint} 替換為希望的本地掛載點路徑
# passwd_file 選項指定憑證文件的路徑，請將 {$HOME}/.passwd-s3fs 替換為憑證文件位置
s3fs {mybucket} {/path/to/mountpoint} -o passwd_file={$HOME}/.passwd-s3fs

# 編輯 /etc/fstab 添加掛載設定
# 將 {mybucket} 替換為 S3 存儲桶名稱
# 將 {/path/to/mountpoint} 替換為希望的本地掛載點路徑，請確保該路徑事先已建立
# 將 {region} 替換為希望使用的 AWS 區域
# 將 {userid} 和 {groupid} 替換為適當的使用者 ID 和群組 ID
s3fs#{mybucket} {/path/to/mountpoint} fuse _netdev,allow_other,use_path_request_style,url=https://{region}.linodeobjects.com/,uid={userid},gid={groupid} 0 0

# 可選：如果需要指定 tmpdir 和 retries 選項，也可以添加以下設定
# retries 選項用於指定掛載重試的次數。當掛載 S3 存儲桶時，如果出現連接或其他錯誤，s3fs 將嘗試重新連接並重新掛載存儲桶。retries 選項允許設定重試的次數，以便在連接問題時嘗試多次重試。
# tmpdir 選項用於指定臨時目錄的位置。在 s3fs 工具掛載 S3 存儲桶時，它會使用臨時目錄來緩存下載的數據，這有助於提高性能並減少對 S3 的請求次數。如果不指定 tmpdir，s3fs 將使用默認的臨時目錄。
s3fs#{mybucket} {/path/to/mountpoint} tmpdir={/path/to/tmpdir},retries=3,fuse _netdev,allow_other,use_path_request_style,url=https://{region}.linodeobjects.com/,uid={userid},gid={groupid} 0 0

# 使用 mount -a 指令掛載所有未掛載的磁碟
# 此指令將根據 /etc/fstab 中的設定，掛載所有未掛載的磁碟，包括在 fstab 中添加的 S3 存儲桶掛載點
mount -a

# s3fs 卸載
# 將 /path 替換為掛載點路徑
fusermount -u /path

# 使用 df -h 指令查看掛載的硬碟
# 此指令將顯示當前掛載的硬碟，包括 S3 存儲桶掛載點
df -h

```