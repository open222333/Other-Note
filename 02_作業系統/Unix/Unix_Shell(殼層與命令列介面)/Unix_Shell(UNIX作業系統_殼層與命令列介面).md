# Unix Shell(UNIX作業系統 殼層與命令列介面)

```
Unix shell，一種殼層與命令列介面，是UNIX作業系統下傳統的使用者和電腦的互動介面。
第一個使用者直接輸入命令來執行各種各樣的任務。
```

## 目錄

- [Unix Shell(UNIX作業系統 殼層與命令列介面)](#unix-shellunix作業系統-殼層與命令列介面)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [Bourne shell相容](#bourne-shell相容)
		- [C shell相容](#c-shell相容)
		- [操作教學範例](#操作教學範例)
- [Bourne shell 系列（sh / ash / dash）](#bourne-shell-系列sh--ash--dash)
- [指令](#指令)

## 參考資料

[Unix shell](https://zh.wikipedia.org/zh-tw/Unix_shell)

### Bourne shell相容

[Bourne shell（sh）](https://zh.wikipedia.org/zh-tw/Bourne_shell)

[Almquist shell（ash）](https://zh.wikipedia.org/zh-tw/Almquist_shell)

[Bourne-Again shell（bash）](https://zh.wikipedia.org/zh-tw/Bash)

[Debian Almquist shell（dash）](https://zh.wikipedia.org/zh-tw/Debian_Almquist_shell)

[Korn shell（ksh）](https://zh.wikipedia.org/zh-tw/KornShell)

[Z shell（zsh）](https://zh.wikipedia.org/zh-tw/Z_shell)

### C shell相容

[C shell（csh）](https://zh.wikipedia.org/zh-tw/C_Shell)

[TENEX C shell（tcsh）](https://zh.wikipedia.org/zh-tw/Tcsh)

### 操作教學範例

[Linux 查詢與更改登入 shell 設定，chsh 指令用法教學與範例](https://blog.gtwang.org/linux/linux-chsh-command-change-login-shell-tutorial/)

# Bourne shell 系列（sh / ash / dash）

| Shell | 說明 | 常見位置 |
|---|---|---|
| **sh**（Bourne shell） | Stephen Bourne 在貝爾實驗室開發，隨 Version 7 Unix 發布，是後續相容 shell 的基礎；現在的 `/bin/sh` 多半是連到其他 POSIX 相容 shell | 各 Unix / Linux 的 `/bin/sh` |
| **ash**（Almquist shell） | Kenneth Almquist 開發的輕量 sh 相容實作 | BusyBox、Alpine Linux（Docker 映像常見） |
| **dash**（Debian Almquist shell） | Debian 由 ash 移植改良，啟動快、記憶體小、符合 POSIX | Debian / Ubuntu 的 `/bin/sh` |
| **bash**（Bourne-Again shell） | GNU 開發，相容 sh 並加上大量擴充 | 多數發行版的預設登入 shell，見 [bash 筆記](./Bourne-Again_shell(bash).md) |

```sh
# 確認 /bin/sh 實際指向哪個 shell
ls -l /bin/sh
```

> 腳本開頭寫 `#!/bin/sh` 時，在 Debian / Ubuntu 會用 dash 執行，bash 專屬語法（如 `[[ ]]`、陣列）會失敗；需要時改寫 `#!/bin/bash`。

[Debian Almquist shell 官方網站](http://gondor.apana.org.au/~herbert/dash/)

# 指令

```sh
# 查看登入預設的 shell 設定值
grep `whoami` /etc/passwd | cut -d: -f7

# 查看目前的 shell 名稱
# (這個用法並不是所有shell都支持)
echo $0
# 環境變量中shell的匹配查找
env | grep SHELL

# 列出系統上所有可用的 shell
cat /etc/shells
chsh -l

# 更改登入預設的 shell 設定
chsh -s /bin/bash
```