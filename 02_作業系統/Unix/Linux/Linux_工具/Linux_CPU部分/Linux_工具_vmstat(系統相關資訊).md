# Linux 工具 vmstat(系統相關資訊)

```
vmstat提供了非常多的系統相關資訊，包括process、CPU、記憶體、swap空間與I/O狀況，是系統管理與效能檢視不可或缺的。
```

## 目錄

- [Linux 工具 vmstat(系統相關資訊)](#linux-工具-vmstat系統相關資訊)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[vmstat(8) — Linux manual page](https://man7.org/linux/man-pages/man8/vmstat.8.html)

# 指令

```bash
vmstat [-V] [-n] [delay [count]]

	-V 表示打印出版本信息；
	-n 表示在週期性循環輸出時，輸出的頭部信息僅顯示一次；
	delay 是兩次輸出之間的延遲時間；
	count 是指按照這個時間間隔統計的次數。
```
