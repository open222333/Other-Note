# Linux 工具 tail(顯示檔案最後幾行)

```
tail 顯示檔案最後幾行；加 -f 會持續等待並即時印出新寫入的內容。
最常用在「長時間作業（備份、還原、部署）把 log 寫到檔案，另開視窗 tail -f 看進度」。
```

## 目錄

- [Linux 工具 tail(顯示檔案最後幾行)](#linux-工具-tail顯示檔案最後幾行)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
	- [即時監看 log（tail -f / tail -F）](#即時監看-logtail--f--tail--f)
	- [長時間作業的監看技巧](#長時間作業的監看技巧)

## 參考資料

[顯示檔案的最後幾行（tail 指令）](https://www.ibm.com/docs/zh-tw/aix/7.1?topic=files-displaying-last-lines-tail-command)

[tail(1) - Linux manual page](https://man7.org/linux/man-pages/man1/tail.1.html)

[tmux 筆記（另開 window / pane 監看）](../Linux_工具_tmux(終端機管理工具).md)

[mydumper 筆記（長時間備份還原的實際流程）](<../../../../../03_伺服器服務/DatabaseServer(資料庫伺服器)/MySQL/MySQL_工具_mydumper_myloader(平行備份還原工具).md>)

# 指令

```bash
tail $log_file              # 最後 10 行
tail -n 100 $log_file       # 最後 100 行
tail -n +50 $log_file       # 從第 50 行印到結尾
```

## 即時監看 log（tail -f / tail -F）

```bash
# 持續顯示新內容，Ctrl+C 離開（只停 tail，不影響寫 log 的程式）
tail -f $log_file

# 先印最後 200 行再持續監看
tail -n 200 -f $log_file

# 檔案被輪替（logrotate）、刪掉重建、或還沒產生時，會自動重新開啟 → 長時間監看建議用 -F
tail -F $log_file

# 同時看多個 log（會標出檔名）
tail -f $log_a $log_b

# 只看關鍵字：grep 要加 --line-buffered，否則輸出會被緩衝、延遲出現
tail -f $log_file | grep --line-buffered -E 'ERROR|CRITICAL'

# 程式結束時 tail 跟著結束（$PID 為寫 log 的程式）
tail -f --pid=$PID $log_file
```

| 選項 | 說明 |
|---|---|
| `-f` | follow，追蹤**同一個檔案**（inode）的新內容 |
| `-F` | 等於 `--follow=name --retry`，依檔名追蹤，檔案被換掉或晚點才出現也能接上 |
| `-n N` | 顯示最後 N 行；`-n +N` 從第 N 行開始 |
| `--pid=PID` | 該程序結束後 tail 自動結束 |

想邊看邊往回翻：`less +F $log_file`（`Ctrl+C` 暫停追蹤可上下捲動、`/` 搜尋，按 `F` 恢復追蹤）。

## 長時間作業的監看技巧

流程：

```
1. 重跑前把舊 log 移開（避免新內容接在舊內容後面，事後 grep 統計被污染）
2. tmux 裡執行作業，log 寫到檔案
3. 另開 tmux window / pane：tail -f 看進度
4. 完成後 grep -c 檢查錯誤數
```

```bash
# 1. 舊 log 移開（留作紀錄，用不到再刪）
mkdir -p /data/old_run && mv /data/job.log /data/old_run/

# 2. tmux 裡執行
tmux new -s job
time long_running_command > /data/job.log 2>&1

# 3. Ctrl+b c 開新 window（或 Ctrl+b % 分割 pane）後
tail -F /data/job.log

# 4. 完成後
grep -c -E 'ERROR|CRITICAL' /data/job.log
```

- 監看視窗的 `Ctrl+C` 只停 tail；作業視窗不要按。
- 要離開 SSH：`Ctrl+b d` detach，作業繼續跑；回來用 `tmux a -t job`。
- 作業本身就有 log 參數時（例 mydumper `-L`），直接用參數，不用 `>` 導向。
