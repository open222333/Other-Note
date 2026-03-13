# Linux 工具 fallocate(分配空間-建立測試)

```
fallocate 命令在 Linux 系統中是內建的，而不是一個單獨的應用程序。

fallocate 是一個用於在 Linux 系統上分配空間的命令行工具。
它可以快速地為指定大小的文件預留磁盤空間，而無需實際寫入文件內容。
```

## 目錄

- [Linux 工具 fallocate(分配空間-建立測試)](#linux-工具-fallocate分配空間-建立測試)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[fallocate(1) — Linux manual page](https://man7.org/linux/man-pages/man1/fallocate.1.html)

# 指令

```bash
# fallocate [OPTIONS] FILENAME
# FILENAME 是要分配空間的目標文件的路徑。
# 常用的選項包括：
# 	-l, --length SIZE: 指定要分配的空間大小。SIZE 可以是具體的字節數，也可以使用後綴符號，如 K（KB）、M（MB）、G（GB）等。
# 	-o, --offset OFFSET: 可選地指定要在文件中分配空間的偏移量（字節數）。
# 	-z, --zero: 在分配空間後，將文件內容填充為零。
# 	-c, --collapse-range: 將分配的範圍合併為一個連續的區域。

# 示例用法
# 分配一個大小為 1 GB 的文件
fallocate -l 1G myfile.bin

# 在文件的偏移量為 1 MB 的位置，分配一個大小為 512 MB 的空間
fallocate -o 1M -l 512M myfile.bin

# 分配一個大小為 100 MB 的空間並填充為零
fallocate -z -l 100M myfile.bin
```
