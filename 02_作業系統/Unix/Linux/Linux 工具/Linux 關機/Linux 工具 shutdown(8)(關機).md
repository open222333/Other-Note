# Linux 工具 shutdown(8)(關機)

```
shutdown 命令用於安排系統關機或重新啟動，在 Linux 系統中是一個常用的管理工具。

shutdown 在關機時會把系統的服務都關閉之後，才關閉電腦
```

## 目錄

- [Linux 工具 shutdown(8)(關機)](#linux-工具-shutdown8關機)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[shutdown(8) — Linux man page](https://linux.die.net/man/8/shutdown)

[shutdown(8) — Linux manual page](https://www.man7.org/linux/man-pages/man1/shutdown.8.html)

[Linux 關機指令（shutdown、halt 與 poweroff）教學與範例](https://blog.gtwang.org/linux/how-to-shutdown-linux/)

# 指令

```bash
shutdown [options] [time] [message]

TIME 是指要關機的時間，其格式可分為好幾種：

now：指定為目前的時間，有就是立即關機的意思，這個應該是最常會被用到。
+m：指定多少分鐘之後關機，例如 +30 就是指 30 分鐘之後關機。
hh:mm：指定某個時間點關機，時間的格式是使用 24 小時制的，例如 18:30 就是下午六點三十分關機。
當 TIME 所指定的時間到了之後，shutdown 指令就會送出一個通知給 init 這個 daemon，讓系統進入適當的 runlevel，準備關機。

在選項（OPTION）的部分，可用的選項有：

-r：讓系統重新開機（reboot）。
-h：讓系統停止運作（halt）或關閉電源（power off），至於會選擇哪一種則取決於系統（有時候可以在 BIOS 中更改）。
-H：讓系統停止運作。
-P：讓系統關閉電源。
-c：取消之前所下達的關機指令。
-k：模擬關機，只有對使用者發出警告，並禁止新使用者登入，但不關機。
```

`立即關機`

```bash
shutdown -h now
```

```bash
shutdown now
```

`指定時間後關機`

10 分鐘後關閉系統

```bash
shutdown -h +10
```

`重新啟動系統`

```bash
shutdown -r now
```

```bash
shutdown -r
```

```bash
reboot
```

`安排關機並附帶訊息`

```bash
shutdown -h +30 "系統將在 30 分鐘後關機，請儲存工作並退出所有應用程式。"
```

`取消安排的關機或重新啟動`

```bash
shutdown -c
```
