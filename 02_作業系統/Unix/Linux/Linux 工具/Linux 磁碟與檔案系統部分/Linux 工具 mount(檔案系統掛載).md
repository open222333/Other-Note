# Linux 工具 mount(檔案系統掛載)

```
NFS (Network File System)
是一種可以讓多台電腦「共享同一個資料夾」的網路檔案系統。
```

| 比較項目 | NFS           | S3（s3fs）      |
| ---- | ------------- | ------------- |
| 協議   | 傳統檔案系統        | 物件儲存          |
| 寫入速度 | 快（本地網路）       | 慢（HTTP）       |
| 延遲   | 低             | 高             |
| 共享   | 支援            | 支援但表現較差       |
| 擴展性  | 中等            | 非常高           |
| 用途   | 企業內部 NAS、共享目錄 | 大規模儲存、備份、影片來源 |

## 目錄

- [Linux 工具 mount(檔案系統掛載)](#linux-工具-mount檔案系統掛載)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
- [指令](#指令)
  - [查看目前所有掛載](#查看目前所有掛載)
  - [掛載（mount）一般磁碟裝置](#掛載mount一般磁碟裝置)
  - [掛載 NFS](#掛載-nfs)
    - [掛載 /etc/fstab](#掛載-etcfstab)
  - [掛載 S3FS（S3 bucket）](#掛載-s3fss3-bucket)
  - [解除 mount](#解除-mount)
- [/etc/fstab (系統開機自動掛載檔案系統)](#etcfstab-系統開機自動掛載檔案系統)
- [例外](#例外)
  - [掛載點必須存在](#掛載點必須存在)

## 參考資料

[mount(8) - Linux man page](https://linux.die.net/man/8/mount)

[磁碟掛載與卸載](https://dywang.csie.cyut.edu.tw/dywang/linuxSystem/node43.html)

# 安裝

## Debian (Ubuntu)

```sh
apt update
apt install nfs-common
```

# 指令

```bash
mount [-tonL]  裝置名稱代號  掛載點
	參數：
	-a  ：依照 /etc/fstab 的內容將所有相關的磁碟都掛載
	-n  ：掛載而不記錄到 /etc/mtab
	-L  ：後接掛載 partition 的表頭名稱( Label )
	-t  ：指定掛載裝置的檔案格式
	-o  ：後面可接額外參數：
		ro, rw:       此 partition 為唯讀(ro) 或可讀寫(rw)
		async, sync:  此 partition 為同步寫入 (sync) 或非同步 (async)
		auto, noauto: 允許此 partition 被以 mount -a 自動掛載(auto)
		dev, nodev:   是否允許此 partition 上，可建立裝置檔案？ dev 為可允許
		suid, nosuid: 是否允許此 partition 含有 suid/sgid 的檔案格式？
		exec, noexec: 是否允許此 partition 上擁有可執行 binary 檔案？
		user, nouser: 是否允許此 partition 讓 user 執行 mount ？
						一般 user 也能夠對此 partition 進行 mount 。
		defaults:     預設值為：rw, suid, dev, exec, auto, nouser, and async
		remount:      重新掛載，這在系統出錯，或重新更新參數時，
		iocharset=    設定字元編碼，如 big5,utf8 等
```

## 查看目前所有掛載

```sh
mount | column -t
```

只看特定掛載點

```sh
mount | grep /var/app/dir
```

只看 s3fs

```sh
mount | grep s3fs
```

只看 nfs

```sh
mount | grep nfs
```

查看目前掛載的檔案類型

```sh
df -T
```

## 掛載（mount）一般磁碟裝置

```sh
mount /dev/sda1 /mnt
```

## 掛載 NFS

```sh
# mkdir：建立資料夾
# -p（parents）：如果上層資料夾不存在，自動順便建立，不會報錯
mkdir -p /var/app/dir
mount -t nfs {IP}:/var/app/dir/nfs /var/app/dir
```

### 掛載 /etc/fstab

會掛載 /etc/fstab 中所有尚未掛載的檔案系統

有錯誤會顯示，方便檢查設定是否正確

```sh
mount -a
```

## 掛載 S3FS（S3 bucket）

```sh
s3fs mybucket /mnt/s3 -o allow_other -o url=https://s3.amazonaws.com
```

## 解除 mount

用掛載點（推薦） 或 掛載來源 來卸載

```sh
umount /var/app/dir
```

```sh
umount 192.168.1.1:/var/app/dir/nfs
```

```
umount: target is busy

目錄正在被使用（有人在裡面，或正在執行程式）。
```

找出誰在使用

```sh
sudo lsof +f -- /var/app/dir
```

# /etc/fstab (系統開機自動掛載檔案系統)

```
<設備/UUID>  <掛載點>  <檔案系統>  <掛載選項>  <dump>  <fsck>
```

```
UUID=1234-5678  /mnt/data  ext4  defaults  0  2
```

```
UUID=1234-5678 → 裝置
/mnt/data → 掛載目錄
ext4 → 檔案系統類型
defaults → 掛載選項
0 → dump（備份）
2 → fsck（開機檢查順序）
```

# 例外

## 掛載點必須存在

如果 /mnt/data 不存在，開機會失敗 可先建立

```sh
mkdir -p /mnt/data
```