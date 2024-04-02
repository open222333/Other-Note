# Linux 工具 watch(觀看執行結果)

```
```

## 目錄

- [Linux 工具 watch(觀看執行結果)](#linux-工具-watch觀看執行結果)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[watch(1) — Linux manual page](https://man7.org/linux/man-pages/man1/watch.1.html)

[Linux 以 watch 指令重複執行程式並監看結果教學](https://blog.gtwang.org/linux/linux-watch-run-command-repeatedly/)

# 指令

```bash

watch -n 1 'echo $date' -n 每秒執行

# 監看錯誤訊息紀錄檔
# 輸出最新的網頁伺服器錯誤訊息
tail /var/log/httpd/error_log

# 自動監看最新的網頁伺服器錯誤訊息
watch tail /var/log/httpd/error_log

# 每0.5秒更新一次: -n 參數指定執行指令的間隔時間
watch -n 0.5 tail /var/log/httpd/error_log

# 標示輸出差異之處: -d 參數讓watch將差異之處反白
watch -d ls -l

# 解析ANSI顏色與樣式: -c --color 參數 把檔案依照類型上色
watch -c ls --color

# 列出最近有更改過的檔案
watch -n 1 "ls -ltr | tail -n20 | tac | nl"

# 關閉標頭行 -t 參數
watch -t ls
```