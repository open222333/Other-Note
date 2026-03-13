# Linux 工具 mpstat(查看CPU信息)

```
mpstat 不但能查看所有CPU的平均信息，還能查看指定CPU的信息。
```

## 目錄

- [Linux 工具 mpstat(查看CPU信息)](#linux-工具-mpstat查看cpu信息)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[mpstat(1) — Linux manual page](https://man7.org/linux/man-pages/man1/mpstat.1.html)

# 指令

```bash
mpstat [-P {|ALL}] [internal [count]]
	-P {|ALL} 表示監控哪個CPU， cpu在[0,cpu個數-1]中取值
	internal 相鄰的兩次採樣的間隔時間
	count 採樣的次數，count只能和delay一起使用
```
