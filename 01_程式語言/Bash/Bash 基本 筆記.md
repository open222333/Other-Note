# Bash 基本 筆記

```
副檔名 .sh
```

```bash
# 將該檔案設定為可執行
chmod +x demo.sh
```

## 撰寫 shell script 的良好習慣

```
script 的功能
script 的版本資訊
script 的作者與聯絡方式
script 的版權宣告方式
script 的 History (歷史紀錄)
script 內較特殊的指令，使用『絕對路徑』的方式來下達
script 運作時需要的環境變數預先宣告與設定。
```

## 目錄

- [Bash 基本 筆記](#bash-基本-筆記)
		- [撰寫 shell script 的良好習慣](#撰寫-shell-script-的良好習慣)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [特殊說明相關](#特殊說明相關)
- [特殊變數](#特殊變數)
- [常用的環境變數](#常用的環境變數)
- [範例](#範例)
	- [將目前執行 process 的 PID 依照數字大小排序](#將目前執行-process-的-pid-依照數字大小排序)
	- [特殊 set -e exec](#特殊-set--e-exec)

## 參考資料

[Linux Shell Script 入門教學](https://blog.techbridge.cc/2019/11/15/linux-shell-script-tutorial/)

[shell中讀取ini配置。](https://www.firbug.com/a/202108/677572.html)

### 特殊說明相關

[What are the special dollar sign shell variables?(特殊變數)](https://stackoverflow.com/questions/5163144/what-are-the-special-dollar-sign-shell-variables)

[What does set -e and exec "$@" do for docker entrypoint scripts?](https://stackoverflow.com/questions/39082768/what-does-set-e-and-exec-do-for-docker-entrypoint-scripts)

[Shell重定向 ＆>file、2>&1、1>&2 、/dev/null的区别](https://blog.csdn.net/u011630575/article/details/52151995)

# 特殊變數

```bash
# 位置參數
$1, $2, $3
# 所有位置參數的類似數組的構造，{$1, $2, $3 ...}。
$@
# 所有位置參數的 IFS 展開，$1 $2 $3 ...。
$*
# 位置參數的數量。
$#
# shell 設置的當前選項。
$-
# 當前shell（不是子shell）的pid。
$$
# 最近的參數（或啟動後立即啟動當前 shell 的命令的 abs 路徑）。
$_
# (輸入）字段分隔符。
$IFS
# 最近的前台管道退出狀態。
$?
# 最近的後台命令的PID。
$!
# shell 或 shell 腳本的名稱
$0
```
3
# 常用的環境變數

```
HOME：當前使用者之家目錄的路徑。
SHELL：當前所使用的shell之名稱。
PATH：搜尋命令執行檔時，所要搜尋的目錄。
MAIL：和收發e-mail有關。
HISTSIZE：與歷史命令的數量有關。
SAVEHIST：與歷史命令有關。
LANG：目前系統所使用的語系。如：sh_TW.UTF-8，是繁體中文萬國碼。
RANDOM：此環境變數較特別，它是「亂數」的意思，在呼叫它時會產生一個正整數亂數（介於0到32767之間）。
```

# Shell重定向 ＆>file、2>&1、1>&2 、/dev/null的區別

```
在shell腳本中，默認情況下，總是有三個文件處於打開狀態，標準輸入(鍵盤輸入)、標準輸出（輸出到屏幕）、標準錯誤（也是輸出到屏幕），它們分別對應的文件描述符是0，1，2 。

默認為標準輸出重定向，
與 1> 相同 2>&1  意思是把 標準錯誤輸出 重定向到 標準輸出.
&>file  意思是把標準輸出 和 標準錯誤輸出 都重定向到文件file中
/dev/null是一個文件，這個文件比較特殊，所有傳給它的東西它都丟棄掉
```

# 範例

## 將目前執行 process 的 PID 依照數字大小排序

```bash
# 宣告使用 /bin/bash
#!/bin/bash

echo "=== 將目前執行 process 的 PID 依照數字大小排序，取出前 10 名 === "

# ps 為列出 process 相關資訊，透過 | pipe 管線傳遞資料。awk 可以根據 pattern 進行資料處理（這邊印出第一欄 PID）而 sort 是進行排序，其排序時，預設都是把資料當作字串來排序，若想讓資料根據實際數值的大小來排序，可以加上 -n 參數。-r 則是由大到小排序，預設是由小到大
ps | awk '{print $1}' | sort -rn | head -10
```

## 特殊 set -e exec

```bash
#!/bin/bash
set -e

... code ...
exec "$@"
```