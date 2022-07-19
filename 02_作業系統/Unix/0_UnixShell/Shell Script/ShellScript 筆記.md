# ShellScript 筆記

```
副檔名 .sh
```

```bash
# 將該檔案設定為可執行
chmod +x demo.sh
```

## 目錄

- [ShellScript 筆記](#shellscript-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)

## 參考資料

[Linux Shell Script 入門教學](https://blog.techbridge.cc/2019/11/15/linux-shell-script-tutorial/)

[shell中讀取ini配置。](https://www.firbug.com/a/202108/677572.html)

[What are the special dollar sign shell variables?(特殊變數)](https://stackoverflow.com/questions/5163144/what-are-the-special-dollar-sign-shell-variables)

```
撰寫 shell script 的良好習慣
    script 的功能
    script 的版本資訊
    script 的作者與聯絡方式
    script 的版權宣告方式
    script 的 History (歷史紀錄)
    script 內較特殊的指令，使用『絕對路徑』的方式來下達
    script 運作時需要的環境變數預先宣告與設定。
```

特殊變數

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

常用的環境變數之意義，說明如下：

```
※ HOME：當前使用者之家目錄的路徑。
※ SHELL：當前所使用的shell之名稱。
※ PATH：搜尋命令執行檔時，所要搜尋的目錄。
※ MAIL：和收發e-mail有關。
※ HISTSIZE：與歷史命令的數量有關。
※ SAVEHIST：與歷史命令有關。
※ LANG：目前系統所使用的語系。如：sh_TW.UTF-8，是繁體中文萬國碼。
※ RANDOM：此環境變數較特別，它是「亂數」的意思，在呼叫它時會產生一個正整數亂數（介於0到32767之間）。
```

```bash
# 範例
# 將目前執行 process 的 PID 依照數字大小排序

# 宣告使用 /bin/bash
#!/bin/bash

echo "=== 將目前執行 process 的 PID 依照數字大小排序，取出前 10 名 === "

# ps 為列出 process 相關資訊，透過 | pipe 管線傳遞資料。awk 可以根據 pattern 進行資料處理（這邊印出第一欄 PID）而 sort 是進行排序，其排序時，預設都是把資料當作字串來排序，若想讓資料根據實際數值的大小來排序，可以加上 -n 參數。-r 則是由大到小排序，預設是由小到大
ps | awk '{print $1}' | sort -rn | head -10
```

ShellScript 特殊 set -e exec

[What does set -e and exec "$@" do for docker entrypoint scripts?](https://stackoverflow.com/questions/39082768/what-does-set-e-and-exec-do-for-docker-entrypoint-scripts)

```bash
#!/bin/bash
set -e

... code ...
exec "$@"
```