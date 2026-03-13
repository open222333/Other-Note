# Linux 工具 iconv(字符集轉換)

```
一個用於字符集轉換的命令行工具。
它允許你在不同的字符集之間進行轉換。
```

## 目錄

- [Linux 工具 iconv(字符集轉換)](#linux-工具-iconv字符集轉換)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
	- [Debian (Ubuntu)](#debian-ubuntu)
	- [RedHat (CentOS)](#redhat-centos)
- [指令](#指令)

## 參考資料

[iconv(1) — Linux man page](https://linux.die.net/man/1/iconv)

[iconv(1) — Linux manual page](https://man7.org/linux/man-pages/man1/iconv.1.html)

# 安裝

## Debian (Ubuntu)

```bash
apt-get install -y libc-bin
```

## RedHat (CentOS)

```bash
yum install -y glibc-common
```

# 指令

`顯示版本信息`

```bash
iconv --version
```

`使用 iconv 命令轉換文件編碼`

```bash
iconv -f old_encoding -t utf-8 your_file.txt > new_file.txt
```