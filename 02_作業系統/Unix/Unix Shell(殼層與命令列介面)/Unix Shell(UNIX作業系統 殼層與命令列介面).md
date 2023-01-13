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