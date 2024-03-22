# Linux 工具 poweroff(8)(關機)

```
建議使用 shutdown

poweroff 指令也是類似的狀況，它也允許你不管系統的狀況，直接把電腦的電源切斷

要關閉 Linux 系統，你可以使用 poweroff 命令。這個命令會立即關閉系統，類似於 shutdown -h now 命令。
```

## 目錄

- [Linux 工具 poweroff(8)(關機)](#linux-工具-poweroff8關機)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[poweroff(8) — Linux man page](https://linux.die.net/man/8/poweroff)

[poweroff(8) — Linux manual page](https://www.man7.org/linux/man-pages/man1/poweroff.8.html)

# 指令

確保在執行此命令之前已經儲存並關閉所有需要的檔案和應用程式，因為系統會立即關閉而不會提示保存工作

```bash
poweroff
```


強制終止正在運行的程序並立即停止系統。

請注意，使用 -f 選項可能會造成正在運行的應用程式或系統進程無法正常關閉，可能會導致數據損失或系統不穩定。

```bash
poweroff -f
```
