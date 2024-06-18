# Linux 工具 mkdir(建立目錄)

```
mkdir(1)：

mkdir 命令在終端中執行時使用的命令。
用於在 Linux 和其他 Unix-like 系統中創建目錄。
終端用戶直接使用的工具，可以在終端中直接執行。

mkdir(2)：

mkdir 是 Linux 中的一個系統調用（system call），屬於標準 C 函數庫的一部分。
在 C 語言中，可以使用 mkdir(2) 函數來在程序中創建目錄。
通常由程序員在開發時使用，用於寫入 C 語言的程式碼中。

mkdir(3)：

mkdir 是 POSIX 標準中的一個函數，也是 Linux 中的一個 C 函數庫中的函數。
在 C 語言中，可以使用 mkdir(3) 函數來在程序中創建目錄。
通常由程序員在開發時使用，用於寫入 C 語言的程式碼中，具有跨平台的特性。
```

## 目錄

- [Linux 工具 mkdir(建立目錄)](#linux-工具-mkdir建立目錄)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[mkdir(1) — Linux man page](https://linux.die.net/man/1/mkdir)

[mkdir(2) — Linux man page](https://linux.die.net/man/2/mkdir)

[mkdir(3) — Linux man page](https://linux.die.net/man/3/mkdir)

[mkdir(1) — Linux manual page](https://man7.org/linux/man-pages/man1/mkdir.1.html)

[mkdir(2) — Linux manual page](https://man7.org/linux/man-pages/man2/mkdir.2.html)

[mkdir(3) — Linux manual page](https://man7.org/linux/man-pages/man3/mkdir.3p.html)

# 指令

基本用法 在當前工作目錄下創建一個名為 directory_name 的目錄。

```bash
mkdir directory_name
```

創建多個目錄 同時創建多個目錄，用空格分隔各個目錄名稱。

```bash
mkdir dir1 dir2 dir3
```

創建嵌套目錄 使用 -p 選項可以創建嵌套目錄，即如果某些父目錄不存在，也會一併創建。

```bash
mkdir -p path/to/nested/directory
```

設置目錄權限 使用 -m 選項可以設置創建的目錄的權限模式，mode 可以是八進制表示的權限值，例如 mkdir -m 755 mydir。

```bash
mkdir -m mode directory_name
```

創建多個嵌套目錄 一次性創建多個嵌套目錄結構。

```bash
mkdir -p dir1/{subdir1,subdir2}/{subsubdir1,subsubdir2}
```

使用絕對路徑來創建目錄，可以指定創建目錄的具體位置。

```bash
mkdir /path/to/directory
```

使用相對路徑來創建目錄，例如在當前目錄的父目錄中創建一個新目錄。

```bash
mkdir ../new_directory
```

使用 -v 選項可以顯示詳細的創建信息，包括創建的目錄名稱。

```bash
mkdir -v directory_name
```
