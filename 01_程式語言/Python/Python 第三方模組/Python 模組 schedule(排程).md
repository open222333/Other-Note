# Python 模組 schedule(排程)

```
schedule 是最簡單的 Python 排程套件，適合輕量排程腳本。
```

## 目錄

- [Python 模組 schedule(排程)](#python-模組-schedule排程)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [schedule 支援的排程方式](#schedule-支援的排程方式)

## 參考資料

[schedule pypi](https://pypi.org/project/schedule/)

[schedule 文檔](https://schedule.readthedocs.io/en/stable/)

# 指令

```bash
# 安裝
pip install schedule
```

# 用法

```Python
import schedule
import time

def job():
    print("執行任務")

# 每 10 秒執行一次
schedule.every(10).seconds.do(job)

# 每天指定時間
schedule.every().day.at("14:30").do(job)

while True:
    schedule.run_pending()
    time.sleep(1)
```

## schedule 支援的排程方式

```Python
schedule.every().second
schedule.every().minute
schedule.every().hour
schedule.every().day.at("10:30")
schedule.every().monday
schedule.every(5).minutes
schedule.every(3).hours
```

