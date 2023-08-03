# Linux 工具 strace(跟蹤系統調用和接收的信號)

```
跟蹤進程執行時的系統調用和所接收的信號。

strace是跟蹤進程執行時的系統調用和所接收的信號（即它跟蹤到一個進程產生的系統調用，包括參數、返回值、執行消耗的時間）。
```

## 目錄

- [Linux 工具 strace(跟蹤系統調用和接收的信號)](#linux-工具-strace跟蹤系統調用和接收的信號)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[strace(1) — Linux manual page](https://man7.org/linux/man-pages/man1/strace.1.html)

# 指令

```bash
strace  [  -dffhiqrtttTvxx  ] [ -acolumn ] [ -eexpr ] ...
    [ -ofile ] [-ppid ] ...  [ -sstrsize ] [ -uusername ]
    [ -Evar=val ] ...  [ -Evar  ]...
     [command [ arg ...  ] ]

strace  -c  [ -eexpr ] ...  [ -Ooverhead ] [ -Ssortby ]
    [ command [ arg...  ] ]
```
