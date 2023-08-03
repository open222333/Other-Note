# Linux 工具 ps(檢查處理程序狀態)

```
任何時候，系統在執行時，處理程序也會執行。

可以使用 ps 指令來尋找正在執行中的處理程序，並顯示這些處理程序的相關資訊。

ps 指令具有數個旗標，可指定列出哪些處理程序，以及顯示每一個處理程序的哪些資訊。
```

## 目錄

- [Linux 工具 ps(檢查處理程序狀態)](#linux-工具-ps檢查處理程序狀態)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[ps(1) — Linux manual page](https://man7.org/linux/man-pages/man1/ps.1.html)

[檢查處理程序狀態的指令（ps 指令）](https://www.ibm.com/docs/zh-tw/aix/7.1?topic=processes-command-check-process-status-ps-command)

# 指令

```bash
ps [options] [--help]
	-A 列出所有的進程
	-w 顯示加寬可以顯示較多的資訊
	-au 顯示較詳細的資訊
	-aux 顯示所有包含其他使用者的進程
	au(x) 輸出格式 :
		USER PID %CPU %MEM VSZ RSS TTY STAT START TIME COMMAND


# 查看PID
ps U username -f

# 看進程編號
ps U {user} -f

# 觀察系統所有的程序資料
ps -aux

# 也是能夠觀察所有系統的資料
ps -lA

# 連同部分程序樹狀態
ps -axjf
	# 選項與參數：
	# 	-A ：所有的 process 均顯示出來，與 -e 具有同樣的效用；
	# 	-a ：不與 terminal 有關的所有 process ；
	# 	-u ：有效使用者 (effective user) 相關的 process ；
	# 	x ：通常與 a 這個參數一起使用，可列出較完整資訊。
	# 輸出格式規劃：
	# 	l ：較長、較詳細的將該 PID 的的資訊列出；
	# 	j ：工作的格式 (jobs format)
	# 	-f ：做一個更為完整的輸出。
```
