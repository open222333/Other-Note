# Linux 工具 tar(副檔名tar 壓縮工具)

```
tar是Unix和類Unix系統上的歸檔打包工具，可以將多個檔案合併為一個檔案，打包後的檔名亦為「tar」。
目前，tar檔案格式已經成為POSIX標準，最初是POSIX.1-1988，目前是POSIX.1-2001。

tar代表未壓縮的tar檔案。
已壓縮的tar檔案則附加資料壓縮格式的副檔名，如經過gzip壓縮後的tar檔案，副檔名為「.tar.gz」。
由於受到DOS8.3檔名格式的限制，常使用下列縮寫：

.tgz等價於.tar.gz
.tbz與tb2等價於.tar.bz2
.taz等價於.tar.Z
.tlz等價於.tar.lzma
.txz等價於.tar.xz
```

## 目錄

- [Linux 工具 tar(副檔名tar 壓縮工具)](#linux-工具-tar副檔名tar-壓縮工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[tar(1) - Linux man page](https://linux.die.net/man/1/tar)

[tar wiki](https://zh.wikipedia.org/zh-tw/Tar)

[GNU Tar 官方網站](https://www.gnu.org/software/tar/)

# 指令

```bash
tar 功能 選項 檔案
	# 功能
	# -c，--create 建立新的tar檔案
	# -x，--extract，--get 解開tar檔案
	# -t，--list 列出tar檔案中包含的檔案的資訊
	# -r，--append 附加新的檔案到tar檔案中
	# -u，--update 用已打包的檔案的較新版本更新tar檔案
	# -A，--catenate，--concatenate 將tar檔案作為一個整體追加到另一個tar檔案中
	# -d，--diff，--compare 將檔案系統里的檔案和tar檔案里的檔案進行比較
	# --delete 刪除tar檔案里的檔案。注意，這個功能不能用於已儲存在磁帶上的tar檔案！

	# 常用選項
	# -v，--verbose 列出每一步處理涉及的檔案的資訊，只用一個「v」時，僅列出檔名，使用兩個「v」時，列出權限、所有者、大小、時間、檔名等資訊。
	# -k，--keep-old-files 不覆蓋檔案系統上已有的檔案
	# -f，--file [主機名:]檔名 指定要處理的檔名。可以用「-」代表標準輸出或標準輸入。
	# -P，--absolute-names 使用絕對路徑
	# -j，--bzip2 呼叫bzip2執行壓縮或解壓縮。注意，由於部分老版本的tar使用-I實現本功能，因此，編寫指令碼時，最好使用--bzip2。
	# -J，--xz，--lzma 呼叫XZ Utils執行壓縮或解壓縮。依賴XZ Utils。
	# -z，--gzip，--gunzip，--ungzip 呼叫gzip執行壓縮或解壓縮
	# -Z，--compress，--uncompress 呼叫compress執行壓縮或解壓縮

# 將/home目錄下的所有檔案打包入home_backup.tar檔案中
tar -cvf home_backup.tar /home

# 列出home_backup.tar檔案里已被打包的檔案
tar -tf home_backup.tar

# 在當前目錄下解壓home_back.tar
tar -xvf home_backup.tar

# 指定解壓出test.c這個檔案。
# 解壓過程中會自動建立home這個子目錄。
tar -xvf home_backup.tar home/test.c

```
