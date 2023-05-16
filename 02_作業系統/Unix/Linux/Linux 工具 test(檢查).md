# Linux 工具 test(檢查)

```
檢測系統上面某些檔案或者是相關的屬性
```

## 目錄

- [Linux 工具 test(檢查)](#linux-工具-test檢查)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[test(1) — Linux manual page](https://man7.org/linux/man-pages/man1/test.1.html)

[Shell test 命令](https://www.runoob.com/linux/linux-shell-test.html)

[12.3.1 利用 test 指令的測試功能 - 鳥哥](https://linux.vbird.org/linux_basic/centos7/0340bashshell-scripts.php#test)

# 指令

```bash
test -e filename
	-e	該『檔名』是否存在？(常用)
	-f	該『檔名』是否存在且為檔案(file)？(常用)
	-d	該『檔名』是否存在且為目錄(directory)？(常用)
	-b	該『檔名』是否存在且為一個 block device 裝置？
	-c	該『檔名』是否存在且為一個 character device 裝置？
	-S	該『檔名』是否存在且為一個 Socket 檔案？
	-p	該『檔名』是否存在且為一個 FIFO (pipe) 檔案？
	-L	該『檔名』是否存在且為一個連結檔？
	-r	偵測該檔名是否存在且具有『可讀』的權限？
	-w	偵測該檔名是否存在且具有『可寫』的權限？
	-x	偵測該檔名是否存在且具有『可執行』的權限？
	-u	偵測該檔名是否存在且具有『SUID』的屬性？
	-g	偵測該檔名是否存在且具有『SGID』的屬性？
	-k	偵測該檔名是否存在且具有『Sticky bit』的屬性？
	-s	偵測該檔名是否存在且為『非空白檔案』？

test file1 -nt file2
	-nt	(newer than)判斷 file1 是否比 file2 新
	-ot	(older than)判斷 file1 是否比 file2 舊
	-ef	判斷 file1 與 file2 是否為同一檔案，可用在判斷 hard link 的判定上。 主要意義在判定，兩個檔案是否均指向同一個 inode

test n1 -eq n2
	-eq	兩數值相等 (equal)
	-ne	兩數值不等 (not equal)
	-gt	n1 大於 n2 (greater than)
	-lt	n1 小於 n2 (less than)
	-ge	n1 大於等於 n2 (greater than or equal)
	-le	n1 小於等於 n2 (less than or equal)
```
