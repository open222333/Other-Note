# AWS S3 s3fs掛載(Amazon S3)

```
S3fs:Amazon S3或Amazon Simple Storage Service
```

## 目錄

- [AWS S3 s3fs掛載(Amazon S3)](#aws-s3-s3fs掛載amazon-s3)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝步驟 CentOS7](#安裝步驟-centos7)

## 參考資料

[how-do-i-use-s3fs-on-linode-object-storage](https://www.linode.com/community/questions/18817/)

[Install s3fs on Centos 7](https://pikedom.com/install-s3fs-on-centos-7/)

[s3fs 的command](https://github.com/s3fs-fuse/s3fs-fuse/wiki/Fuse-Over-Amazon#use_cache-default-which-means-disabled)


# 安裝步驟 CentOS7

```bash
# 安裝相關套件
yum install fuse
# 安裝 EPEL 存儲庫
yum install epel-release
# 然後安裝s3fs
yum install s3fs-fuse

# 將公鑰和私鑰複製到~/.passwd-s3fs
echo 公鑰:私鑰 > ~/.passwd-s3fs
# 更改檔案權限
chmod -v 600 ~/.passwd-s3fs

# 掛載
s3fs downloads-repository {/path/to/mountpoint} -o passwd_file=${HOME}/.passwd-s3fs


# 編輯/etc/fstab 添加下列內容({mybucket}, {/path/to/mountpoint}:路徑要先建立, {region})：
s3fs#{mybucket} {/path/to/mountpoint} fuse _netdev,allow_other,use_path_request_style,url=https://{region}.linodeobjects.com/,uid=userid,gid=groupid 0 0

s3fs#{mybucket} {/path/to/mountpoint} tmpdir={},retries=3,fuse _netdev,allow_other,use_path_request_style,url=https://{region}.linodeobjects.com/,uid=userid,gid=groupid 0 0


# 掛載 -a：依照設定檔 /etc/fstab 的資料將所有未掛載的磁碟都掛載上來
mount -a

# s3fs卸載
fusermount -u /path

# 查看掛載硬碟
df -h
```