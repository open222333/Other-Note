# Linux 工具 rsync(同步鏡像備份)

```
```

## 目錄

- [Linux 工具 rsync(同步鏡像備份)](#linux-工具-rsync同步鏡像備份)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[rsync(1) - Linux man page](https://linux.die.net/man/1/rsync)

[以 rsync 進行同步鏡像備份 - 鳥哥](https://linux.vbird.org/linux_server/centos6/0310telnetssh.php#rsync)

# 指令

```bash
rsync [-avrlptgoD] [-e ssh] [user@host:/dir] [/local/path]
	選項與參數：
	-v ：觀察模式，可以列出更多的資訊，包括鏡像時的檔案檔名等；
	-q ：與 -v  相反，安靜模式，略過正常資訊，僅顯示錯誤訊息；
	-r ：遞迴複製！可以針對『目錄』來處理！很重要！
	-u ：僅更新 (update)，若目標檔案較新，則保留新檔案不會覆蓋；
	-l ：複製連結檔的屬性，而非連結的目標原始檔案內容；
	-p ：複製時，連同屬性 (permission) 也保存不變！
	-g ：保存原始檔案的擁有群組；
	-o ：保存原始檔案的擁有人；
	-D ：保存原始檔案的裝置屬性 (device)
	-t ：保存原始檔案的時間參數；
	-I ：忽略更新時間 (mtime) 的屬性，檔案比對上會比較快速；
	-z ：在資料傳輸時，加上壓縮的參數！
	-e ：使用的通道協定，例如使用 ssh 通道，則 -e ssh
	-a ：相當於 -rlptgoD ，所以這個 -a 是最常用的參數了！


# 利用 student 的身份登入 clientlinux.centos.vbird 將家目錄複製到本機 /tmp
rsync -av -e ssh student@192.168.100.10:~ /tmp

# 排除特定文件/目錄的複制
# 使用rysnc命令複製文件或文件夾，請使用–exclude標誌
rsync -av --progress --exclude="dir2" dir* /sahilsending incremental file listdir1/dir3/dir4/dir5/
sent 82 bytes received 28 bytes 220.00 bytes/sectotal size is 0 speedup is 0.00[root@linuxnix tmp]

# 遠程複製文件時使用–exclude標誌
rsync -av --progress --exclude="dir2" dir* 192.168.19.142:/sahilsending incremental file listdir1/dir3/dir4/dir5/
```
