# Linux 工具 man(1)(手冊頁面)

```
手冊頁面（manual pages）
```

## 目錄

- [Linux 工具 man(1)(手冊頁面)](#linux-工具-man1手冊頁面)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [Linux manpage 查詢網站清單（含用途說明）](#linux-manpage-查詢網站清單含用途說明)
      - [官方文件來源](#官方文件來源)
      - [社群與第三方工具](#社群與第三方工具)
      - [建議用法](#建議用法)
- [常用 man page 線上查詢網站清單](#常用-man-page-線上查詢網站清單)
- [建議用途對照](#建議用途對照)
  - [分節（sections）](#分節sections)
- [指令](#指令)

## 參考資料

[man(1) — Linux man page](https://linux.die.net/man/1/man)

[man(1) — Linux manual page](https://man7.org/linux/man-pages/man1/man.1.html)

### Linux manpage 查詢網站清單（含用途說明）

#### 官方文件來源

[**Debian Manpages**](https://manpages.debian.org/)

適用於 Debian 系統，提供各版本（stable、testing、unstable）的 man page 搜尋與瀏覽。

[**Ubuntu Manpages**](https://manpages.ubuntu.com/)

適用於 Ubuntu 發行版，可選擇特定版本（如 20.04、22.04）查看對應的 man page。

[**Arch Linux Man Pages**](https://man.archlinux.org/)

提供 Arch Linux 套件的最新 man page，支援英文與多語言搜尋。

[**Fedora Man Pages**](https://manpages.fedoraproject.org/)

Fedora 官方線上 man page 文件，對 Red Hat / CentOS 用戶也有參考價值。

[**man7.org**](https://man7.org/linux/man-pages/)

Linux man-pages 官方專案網站，內容完整涵蓋核心系統呼叫與 C 標準函式庫。

[**OpenBSD Man Pages**](https://man.openbsd.org/)

OpenBSD 官方 man page 查詢網站，支援多版本與章節搜尋。

[**FreeBSD Man Pages**](https://man.freebsd.org/)

FreeBSD 官方 man page，提供針對各版本（如 13.x、14.x）的完整文件。

#### 社群與第三方工具

[**die.net Linux Man Pages**](https://linux.die.net/man/)

經典的線上 man page 平台，支援指令快速搜尋與章節導覽。

[**Mankier**](https://www.mankier.com/)

現代化 man page 搜尋網站，支援命令範例、自動補完與語法高亮。

[**CommandLineFu**](https://www.commandlinefu.com/)

收錄大量 Linux 指令範例與使用技巧，可作為 man page 的補充學習資源。

#### 建議用法

若需查找系統 API 或核心呼叫，建議使用：
[man7.org](https://man7.org/linux/man-pages/)

若想查看自己發行版對應版本的 man page，建議使用：
[Debian](https://manpages.debian.org/)、[Ubuntu](https://manpages.ubuntu.com/)、[Arch](https://man.archlinux.org/)

若想快速查指令範例與語法高亮，建議使用：
[Mankier](https://www.mankier.com/)


# 常用 man page 線上查詢網站清單

| 名稱                              | 說明                                                                          | 網址                                                                                                             |
| ------------------------------- | --------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------- |
| **Debian Manpages**             | 收錄 Debian 各版本（stable、testing、unstable）的手冊頁，支援搜尋指令與庫函式                       |  [https://manpages.debian.org/](https://manpages.debian.org/)                                                |
| **man7.org**                    | 由 Michael Kerrisk 維護的官方 Linux man-pages 專案（glibc、syscalls、system calls、API） |  [https://man7.org/linux/man-pages/](https://man7.org/linux/man-pages/)                                      |
| **die.net**                     | 收錄常見 Linux 指令 man page，排版簡潔、搜尋快，但內容偏舊                                       |  [https://linux.die.net/man/](https://linux.die.net/man/)                                                    |
| **Ubuntu Manpages**             | Ubuntu 官方線上手冊頁，支援多版本（14.04～24.04）                                           |  [https://manpages.ubuntu.com/](https://manpages.ubuntu.com/)                                                |
| **Arch Linux Man Pages**        | Arch 官方文件庫，最新軟體版本的手冊頁，對於 systemd 與新工具最完整                                    |  [https://man.archlinux.org/](https://man.archlinux.org/)                                                    |
| **Fedora Man Pages**            | Red Hat/Fedora 官方 manpage，涵蓋 RHEL 生態系工具與系統管理指令                              |  [https://manpages.fedoraproject.org/](https://manpages.fedoraproject.org/)                                  |
| **OpenBSD / FreeBSD Manpages**  | 若查 POSIX 或跨平台通用指令（如 `sed`, `awk`）可參考 BSD 版本                                 |  [https://man.openbsd.org/](https://man.openbsd.org/) / [https://man.freebsd.org/](https://man.freebsd.org/) |
| **ManKier**                     | 整合 Debian、Ubuntu、CentOS 等多發行版 man page，可全文搜尋                                |  [https://www.mankier.com/](https://www.mankier.com/)                                                        |
| **CommandLineFu Manpages (社群)** | 提供指令使用範例與 man page 連結，偏實戰導向                                                 |  [https://www.commandlinefu.com/](https://www.commandlinefu.com/)                                            |

# 建議用途對照

| 用途                            | 建議網站                                                                           |
| ----------------------------- | ------------------------------------------------------------------------------ |
| 查 **最新 Linux API / 系統呼叫**     | [man7.org](https://man7.org/linux/man-pages/)                                  |
| 查 **Debian / Ubuntu 指令行工具**   | [Debian](https://manpages.debian.org/), [Ubuntu](https://manpages.ubuntu.com/) |
| 查 **RedHat / CentOS / Rocky** | [Fedora Manpages](https://manpages.fedoraproject.org/)                         |
| 查 **滾動更新系統（Arch）**            | [Arch Manpages](https://man.archlinux.org/)                                    |
| 查 **BSD 類系統**                 | [OpenBSD](https://man.openbsd.org/), [FreeBSD](https://man.freebsd.org/)       |
| 查 **多發行版搜尋**                  | [Mankier](https://www.mankier.com/)                                            |
| 查 **舊版 Linux 指令**             | [die.net](https://linux.die.net/man/)                                          |

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
