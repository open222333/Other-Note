# Linux 工具 halt(8)(關機)

```
建議使用 shutdown
shutdown 在關機時會把系統的服務都關閉之後，才關閉電腦，而 halt 指令則允許不管系統的狀態為何，直接停止電腦的運作
```

## 目錄

- [Linux 工具 halt(8)(關機)](#linux-工具-halt8關機)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[halt(8) — Linux man page](https://linux.die.net/man/8/halt)

[halt(8) — Linux manual page](https://www.man7.org/linux/man-pages/man1/halt.8.html)

# 指令

確保在執行此命令之前已經儲存並關閉所有需要的檔案和應用程式

```bash
halt
```

強制終止正在運行的程序並立即停止系統。

請注意，使用 -f 選項可能會造成正在運行的應用程式或系統進程無法正常關閉，可能會導致數據損失或系統不穩定。

```bash
halt -f
```
