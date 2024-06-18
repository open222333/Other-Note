# Linux 工具 man(1)(手冊頁面)

```
手冊頁面（manual pages）
```

## 目錄

- [Linux 工具 man(1)(手冊頁面)](#linux-工具-man1手冊頁面)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [分節（sections）](#分節sections)
- [指令](#指令)

## 參考資料

[man(1) — Linux man page](https://linux.die.net/man/1/man)

[man(1) — Linux manual page](https://man7.org/linux/man-pages/man1/man.1.html)

## 分節（sections）

在 Linux 的 man 頁面中，常見的分節（sections）及其對應的意思如下：

User Commands: 用戶命令，包括終端中可以直接執行的命令，如 mkdir、ls、cp 等。

System Calls: 系統調用，用於程序中通過函數調用操作系統內核的功能，如 mkdir(2)、read(2)、write(2) 等。

Library Functions: 函數庫函數，通常是 C 語言庫中的函數，用於開發程序時使用，如 printf(3)、malloc(3)、memcpy(3) 等。

Special Files: 特殊文件，包括設備文件、設置文件等，如 /dev/null、/etc/passwd 等。

File Formats and Conventions: 文件格式和慣例，介紹文件格式、配置文件格式等，如 passwd(5)、fstab(5) 等。

Games: 遊戲，包括一些 Linux 系統中的遊戲或遊戲相關信息。

Miscellaneous: 雜項，包括各種雜項信息，如 ascii(7)、regex(7) 等。

System Administration tools and Deamons: 系統管理工具和守護進程，介紹系統管理相關的工具和守護進程，如 cron(8)、systemd(8) 等。

# 指令

查看 ls 命令的用法和說明

```bash
man ls
```

查看 C 語言中 printf 函數的用法和說明

```bash
man printf
```

查看 passwd 文件的格式和慣例說明

```bash
man 5 passwd
```
