# Linux 工具 iostat(監視磁盤活動)

```
iostat - 報告中央處理器 (CPU) 統計信息和 設備和分區的輸入/輸出統計信息。

通過iostat方便查看CPU、網卡、tty設備、磁盤、CD-ROM 等等設備的活動情況, 負載信息。
```

## 目錄

- [Linux 工具 iostat(監視磁盤活動)](#linux-工具-iostat監視磁盤活動)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[iostat(1) — Linux manual page](https://man7.org/linux/man-pages/man1/iostat.1.html)

# 指令

```bash
iostat[參數][時間][次數]
	-C 顯示CPU使用情況
	-d 顯示磁盤使用情況
	-k 以 KB 為單位顯示
	-m 以 M 為單位顯示
	-N 顯示磁盤陣列(LVM) 信息
	-n 顯示NFS 使用情況
	-p[磁盤] 顯示磁盤和分區的情況
	-t 顯示終端和CPU的信息
	-x 顯示詳細信息
	-V 顯示版本信息
```
