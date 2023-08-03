# Linux 工具 ls(顯示目錄的內容)

```
顯示指定工作目錄下之內容（列出目前工作目錄所含之文件及子目錄)。
```

## 目錄

- [Linux 工具 ls(顯示目錄的內容)](#linux-工具-ls顯示目錄的內容)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[ls(1) — Linux manual page](https://man7.org/linux/man-pages/man1/ls.1.html)

[顯示目錄內容（ls 指令）](https://www.ibm.com/docs/zh-tw/aix/7.1?topic=directories-displaying-contents-directory-ls-command)

[Linux ls 命令 - 菜鳥教程](https://www.runoob.com/linux/linux-comm-ls.html)

# 指令

```bash
ls [- alrtAFR ] [名稱...]
	-a 顯示所有文件及目錄 ( .開頭的隱藏文件也會列出)
	-l 除文件名稱外，亦將文件類型狀態、權限、擁有者、文件大小等信息詳細列表
	-r 將文件以相對次序顯示(確定依英文母次序)
	-t 將文件依建立時間之先後次序列出
	-A 同 -a ，但不列出 "." (目錄前目錄) 及 ".." (父目錄)
	-F 在列表中的文件名後加一個號；如可執行文件則加"*",目錄則加"/"
	-R 若目錄下有文件，則以下之文件亦皆依序列表出
	-i 列出每個檔案的 inode

# ls 版本資訊
ls --version

# -d 參數 只列出目錄
ls -ld

# -n 參數 顯示使用者的 UID 與群組的 GID 值
ls -n

# 若要讓檔案依照時間排序，讓最新的檔案排在最後
ls -ltr

# 依照檔案的大小來排序
ls -lS
```
