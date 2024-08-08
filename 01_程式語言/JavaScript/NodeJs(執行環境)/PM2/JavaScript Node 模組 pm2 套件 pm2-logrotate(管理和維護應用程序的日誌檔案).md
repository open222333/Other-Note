# JavaScript Node 模組 pm2 套件 pm2-logrotate(管理和維護應用程序的日誌檔案)

```
處理 PM2 启動的應用程序的日誌檔案的輪轉

它可以幫助管理和維護應用程序的日誌檔案，以防止它們變得過大，同時保留過去的日誌歷史。
```

## 目錄

- [JavaScript Node 模組 pm2 套件 pm2-logrotate(管理和維護應用程序的日誌檔案)](#javascript-node-模組-pm2-套件-pm2-logrotate管理和維護應用程序的日誌檔案)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [配置](#配置)
- [用法](#用法)

## 參考資料

[pm2-logrotate npm 頁面](https://www.npmjs.com/package/pm2-logrotate)

[pm2-logrotate Github](https://github.com/keymetrics/pm2-logrotate)

# 指令

```bash
# 安裝
pm2 install pm2-logrotate
```

# 配置

`config 檔 通常位置`

~/.pm2/module_conf.json

修改 .pm2/module_conf.json 文件後 重啟 PM2 進程以應用新的配置

module_conf.json設定檔參數

```json
// pm2-logrotate 模組本身的配置，包括模組的執行模式、命令行參數、日誌文件路徑等。
// 這些設置影響的是 pm2-logrotate 模組自身的行為和特性。

// "module_name"：模組的名稱，這裡是 pm2-logrotate。
// "exec_mode"：執行模式，這裡是 fork。
// "args"：pm2-logrotate 的命令行參數，指定了日誌輪轉的相關設置。
// -d 30：保留 30 天的日誌。
// -s 10M：當日誌大小達到 10MB 時進行輪轉。
// -n 5：保留 5 個歷史日誌文件。
// --compress：壓縮歷史日誌文件。
// "error_file"：pm2-logrotate 的錯誤日誌文件路徑。
// "out_file"：pm2-logrotate 的標準輸出日誌文件路徑。
// "log_date_format"：日誌時間格式，這裡設置為 "YYYY-MM-DD HH:mm Z"。
// "merge_logs"：是否合併日誌文件，這裡設置為 true。
// "watch"：是否監聽文件變化並自動重啟，這裡設置為 false。
{
  "module_name": "pm2-logrotate",
  "exec_mode": "fork",
  "args": ["-d", "30", "-s", "10M", "-n", "5", "--compress"],
  "error_file": "/path/to/pm2-logrotate-error.log",
  "out_file": "/path/to/pm2-logrotate-out.log",
  "log_date_format": "YYYY-MM-DD HH:mm Z",
  "merge_logs": true,
  "watch": false
}
```

```json
// pm2-logrotate 模組管理的日誌文件的配置，包括日誌大小、保留歷史日誌數量、輪轉間隔等。
// 這些設置影響的是 pm2-logrotate 模組管理的日誌文件的行為，例如何時進行日誌輪轉、保留多少歷史日誌文件等。

// "max_size"：最大日誌文件大小為 50MB。
// "retain"：保留 30 個歷史日誌文件。
// "compress"：不壓縮歷史日誌文件。
// "dateFormat"：日誌文件的時間格式為 "YYYY-MM-DD_HH-mm-ss"。
// "workerInterval"：工作週期為 30 秒。
// "rotateInterval"：每天的午夜 0 點觸發日誌輪轉。
// "rotateModule"：啟用模組日誌輪轉。
{
    "pm2-logrotate": {
        "max_size": "50M",
        "retain": "30",
        "compress": false,
        "dateFormat": "YYYY-MM-DD_HH-mm-ss",
        "workerInterval": "30",
        "rotateInterval": "0 0 * * *",
        "rotateModule": true
    }
}
```

# 用法

`查看 pm2-logrotate 的目前設置`

```bash
pm2 conf | grep pm2-logrotate
```

`在設定完成後，可能需要重新啟動 PM2 以確保設定生效`

```bash
pm2 reload all
```

`設定日誌檔案的最大大小，當日誌檔案達到此大小時，將會建立新的日誌檔案。`

```bash
pm2 set pm2-logrotate:max_size 10M
```

`設定保留的歷史日誌檔案的數量`

```bash
pm2 set pm2-logrotate:retain 7
```

`啟用日誌壓縮功能，以節省儲存空間。`

```bash
pm2 set pm2-logrotate:compress true
```

`設定日誌輪換的時間間隔`

```bash
pm2 set pm2-logrotate:rotateInterval "0 0 * * *"
```

`設定輪轉規則`

```bash
pm2 set pm2-logrotate:compress true      # 启用/禁用壓縮
pm2 set pm2-logrotate:retain 7           # 保留最近的7個日誌檔案
pm2 set pm2-logrotate:dateFormat YYYY-MM-DD_HH-mm-ss # 指定日期格式
```

`手動觸發輪轉`

```bash
pm2 trigger pm2-logrotate rotate
```

`查看輪轉日誌的計劃 顯示有關 pm2-logrotate 的配置和狀態信息`

```bash
pm2 show pm2-logrotate
```

詳細說明

```
Process Information:

    status: 進程的狀態，這裡是 "online"。
    name: 進程的名稱，這是 "pm2-logrotate"。
    namespace: 進程的命名空間，這裡是 "default"。
    version: pm2-logrotate 模組的版本。
    restarts: 進程重新啟動的次數。
    uptime: 進程的運行時間。
    script path: pm2-logrotate 的腳本路徑。
    interpreter: 使用的解釋器，這裡是 "node"。
    node.js version: 使用的 Node.js 版本。
    created at: 進程創建的時間。

Process Configuration:

    有關 pm2-logrotate 配置的信息，如輪轉間隔、日期格式、保留日誌文件數量等。
    Actions Available:

    提供的操作列表，包括 heapdump、CPU profiling 等。

Code Metrics Value:

    堆"（Heap）在計算機科學中通常指的是記憶體中的一個區域，用於動態分配和釋放內存。
    在程式執行過程中，當需要在運行時動態創建對象或數據結構時，這些對象通常被放置在堆中。

    堆是用來存儲動態分配的內存，這些內存的大小在程式運行時可以動態地增長或減少。
    與之相對的是 "棧"（Stack），棧用於存儲局部變數和函數調用信息。

    Used Heap Size:
    已使用的堆大小，即進程當前使用的內存空間。
    Heap Usage:
    堆使用率，表示已使用堆大小與總堆大小的百分比。
    Heap Size:
    總堆大小，即分配給進程的內存總量。
    Files Count:
    檔案數量，表示進程當前打開的文件數量。
    Global Logs Size:
    全局日誌大小，表示已記錄的全局日誌文件的總大小。
    Event Loop Latency p95:
    事件循環延遲的百分位數（p95），表示事件處理的時間百分之95的數據。
    Event Loop Latency:
    事件循環延遲，表示事件處理的平均時間。
    Active Handles:
    活動的處理程序數量，表示當前進程中處於活動狀態的事件處理程序數量。
    Active Requests:
    活動的請求數量，表示當前進程中處於活動狀態的請求數量。

Divergent Env Variables from Local Env:

    與本地環境變數不同的環境變數列表。
```
