# Linux 工具 df du(硬碟空間資訊)

```
磁碟的整體資料是在 superblock 區塊中，但是每個各別檔案的容量則在 inode 當中記載的。
那在文字介面底下該如何叫出這幾個資料

df：列出檔案系統的整體磁碟使用量；
du：評估檔案系統的磁碟使用量(常用在推估目錄所佔容量)
```

## 目錄

- [Linux 工具 df du(硬碟空間資訊)](#linux-工具-df-du硬碟空間資訊)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[df(1) - Linux man page](https://linux.die.net/man/1/df)

[du(1) — Linux manual page](https://man7.org/linux/man-pages/man1/du.1.html)

[7.2.1 磁碟與目錄的容量： df, du - 鳥哥](https://linux.vbird.org/linux_basic/centos7/0230filesystem.php#df)

# 指令

```bash
# 列出檔案系統的整體磁碟使用量
df [-ahikHTm] [目錄或檔名]
	-a  ：列出所有的檔案系統，包括系統特有的 /proc 等檔案系統；
	-k  ：以 KBytes 的容量顯示各檔案系統；
	-m  ：以 MBytes 的容量顯示各檔案系統；
	-h  ：以人們較易閱讀的 GBytes, MBytes, KBytes 等格式自行顯示；
	-H  ：以 M=1000K 取代 M=1024K 的進位方式；
	-T  ：連同該 partition 的 filesystem 名稱 (例如 xfs) 也列出；
	-i  ：不用磁碟容量，而以 inode 的數量來顯示

# 輸出容易閱讀和理解的
df -h
Filesystem      Size  Used   Avail Use% Mounted on
/dev/sda6       29G   4.2G   23G   16%     /
udev            1.5G  4.0K   1.5G   1%     /dev
tmpfs           604M  892K   603M   1%     /run
none            5.0M     0   5.0M   0%     /run/lock
none            1.5G  156K   1.5G   1%     /run/shm

# 顯示所有的信息
df --total
Filesystem     1K-blocks    Used    Available Use% Mounted on
/dev/sda6       29640780 4320720    23814372  16%     /
udev             1536756       4    1536752   1%      /dev
tmpfs             617620     892    616728    1%      /run
none                5120       0    5120      0%      /run/lock
none             1544044     156    1543888   1%      /run/shm
total           33344320 4321772    27516860  14%

# 評估檔案系統的磁碟使用量(常用在推估目錄所佔容量)
du [-ahskm] 檔案或目錄名稱
	-a  ：列出所有的檔案與目錄容量，因為預設僅統計目錄底下的檔案量而已。
	-h  ：以人們較易讀的容量格式 (G/M) 顯示；
	-s  ：列出總量而已，而不列出每個各別的目錄佔用容量；
	-S  ：不包括子目錄下的總計，與 -s 有點差別。
	-k  ：以 KBytes 列出容量顯示；
	-m  ：以 MBytes 列出容量顯示；
```
