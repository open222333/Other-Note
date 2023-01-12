# Linux 工具 ln(鏈結檔案)

```
對於使用位於不同地點的相同資料來說，使用 ln 指令來鏈結檔案是很方便的方式。

建立鏈結的方式是提供替代名稱給原來的檔案。
對於大型的檔案，例如資料庫或郵寄清單，經由使用鏈結，不需複製即可讓許多使用者共用該檔。
鏈結不僅省節磁碟空間，對於檔案的變更，亦會自動反應在所有的鏈結檔案中。

軟鏈接：
	1.軟鏈接，以路徑的形式存在。類似於Windows操作系統中的快捷方式
	2.軟鏈接可以跨文件系統，硬鏈接不可以
	3.軟鏈接可以對一個不存在的文件名進行鏈接
	4.軟鏈接可以對目錄進行鏈接
硬鏈接：
	1.硬鏈接，以文件副本的形式存在。但不佔用實際空間。
	2.不允許給目錄創建硬鏈接
	3.硬鏈接只有在同一個文件系統中才能創建
```

## 目錄

- [Linux 工具 ln(鏈結檔案)](#linux-工具-ln鏈結檔案)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[ln(1) — Linux manual page](https://man7.org/linux/man-pages/man1/ln.1.html)

[鏈結檔案（ln 指令）](https://www.ibm.com/docs/zh-tw/aix/7.1?topic=links-linking-files-ln-command)

[Linux ln 命令 - 菜鳥教程](https://www.runoob.com/linux/linux-comm-ln.html)

[ln — 建立連結指令](https://www.ltsplus.com/linux/ln-create-link-command)

# 指令

```bash
ln [參數][源文件或目錄][目標文件或目錄]其中參數的格式為
[- bdfinsvF ] [- S backup - suffix ] [- V { numbered , existing , simple }]
[-- help ] [-- version ] [--]

命令參數
必要參數：
	--backup[=CONTROL] 備份已存在的目標文件
	-b 類似--backup，但不接受參數
	-d 允許超級用戶製作目錄的硬鏈接
	-f 強制執行
	-i 交互模式，文件存在則提示用戶是否覆蓋
	-n 把符號鏈接視為一般目錄
	-s 軟鏈接(符號鏈接)
	-v 顯示詳細的處理過程
選擇參數：
	-S "-S<字尾備份字符串> "或"--suffix=<字尾備份字符串>"
	-V "-V<備份方式>"或"--version-control=<備份方式>"
	--help 顯示幫助信息
	--version 顯示版本信息


# 建立 myfolder 目錄的軟連結
ln -s myfolder folder_link

# 列出檔案的 inode 資訊
ls -li

ls -alh

# -i 參數 以互動式方式確認覆蓋檔案
ln -si file.txt soft_link.txt

# -f 參數 強制覆蓋檔案
ln -sf file.txt soft_link.txt

# 顯示原始檔案 串連的軟連結
ls -l /etc/vtrgb

# readlink 指令 顯示串連軟連結最後的原始檔案
readlink -f /etc/vtrgb
```
