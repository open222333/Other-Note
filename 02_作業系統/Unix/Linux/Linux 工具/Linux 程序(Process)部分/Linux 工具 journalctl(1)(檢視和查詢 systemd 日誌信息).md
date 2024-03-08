# Linux 工具 journalctl(1)(檢視和查詢 systemd 日誌信息)

```
用於檢視和查詢 systemd 日誌信息。
如果你的系統正在使用 systemd，journalctl 應該已經可用。
```

## 目錄

- [Linux 工具 journalctl(1)(檢視和查詢 systemd 日誌信息)](#linux-工具-journalctl1檢視和查詢-systemd-日誌信息)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[journalctl(1) — Linux manual page](https://www.man7.org/linux/man-pages/man1/journalctl.1.html)

# 指令

`詳細的選項和使用方法`

```bash
man journalctl
```

`查看所有日誌條目`

```bash
journalctl
```

`按時間範圍查看日誌條目`

```bash
journalctl --since "2024-02-01 00:00:00" --until "2024-02-29 23:59:59"
```

`顯示最新的日誌條目`

```bash
journalctl -n
```

`按優先級過濾日誌條目`

```bash
journalctl -p err  # 只顯示錯誤和更嚴重的條目
```

`按單元過濾日誌條目`

```bash
journalctl -u nginx  # 顯示與 Nginx 單元相關的日誌條目
```

`即時追蹤日誌變化`

```bash
journalctl -f
```

`以 JSON 格式顯示日誌條目`

```bash
journalctl -o json
```
