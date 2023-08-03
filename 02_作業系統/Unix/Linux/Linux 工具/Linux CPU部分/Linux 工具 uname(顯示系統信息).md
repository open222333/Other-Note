# Linux 工具 uname(顯示系統信息)

```
uname（unix name的簡寫）是一個Unix和類Unix作業系統上的程式，可以列印當前電腦和作業系統的名稱、版本及其他細節。
uname系統呼叫和命令第一次出現在PWB/UNIX上。
這兩個由POSIX指定。

一些Unix變種，如AT&T System V3.0版，包含了相關的setname程式，用來改變uname報告的值。

GNU版本的uname包含在「sh-utils」或「coreutils」包中。uname本身不是一個獨立的程式。
```

## 目錄

- [Linux 工具 uname(顯示系統信息)](#linux-工具-uname顯示系統信息)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[uname - WIKI](https://zh.wikipedia.org/zh-tw/Uname)

[Linux uname 命令](https://www.runoob.com/linux/linux-comm-uname.html)

[uname：查閱系統與核心相關資訊 - 鳥哥](https://linux.vbird.org/linux_basic/centos7/0440processcontrol.php#uname)

# 指令

```bash
uname [-asrmpi]
	# 選項與參數：
	# -a  ：所有系統相關的資訊，包括底下的資料都會被列出來；
	# -s  ：系統核心名稱
	# -r  ：核心的版本
	# -m  ：本系統的硬體名稱，例如 i686 或 x86_64 等；
	# -p  ：CPU 的類型，與 -m 類似，只是顯示的是 CPU 的類型！
	# -i  ：硬體的平台 (ix86)

# 輸出系統的基本資訊
uname -a
#Linux study.centos.vbird 3.10.0-229.el7.x86_64 #1 SMP Fri Mar 6 11:36:42 UTC 2015 x86_64 x86_64 x86_64 GNU/Linux
```
