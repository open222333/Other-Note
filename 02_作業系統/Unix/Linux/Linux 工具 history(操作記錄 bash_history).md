# Linux 工具 history(操作記錄 bash_history)

## 參考資料

[Linux Bash 刪除 history 指令操作歷史紀錄](https://xyz.cinc.biz/2017/08/linux-bash-history-clear.html)

# 指令

```bash
# echo $HISTFILE
/root/.bash_history

# 刪除全部的操作紀錄：
history -c
history -w

# 說明：「history -c」會刪除「下 history 指令時，列出的操作紀錄」， 但不會刪除「.bash_history」(HISTFILE)的檔案內容，為避免重新登入後，又讀取「.bash_history」(HISTFILE)的檔案內容， 所以須再用「history -w」寫入目前已清空的操作紀錄。
```

# history操作紀錄相關的幾個環境變數

```bash
# 操作歷史紀錄，儲存的檔案位置。(操作歷史紀錄檔)(.bash_history)
echo $HISTFILE
# /root/.bash_history

# 操作歷史紀錄檔，最多儲存幾筆。
echo $HISTFILESIZE
# 1000

# history 最多列出幾筆(在記憶體中存放的筆數)
echo $HISTSIZE
# 1000
```