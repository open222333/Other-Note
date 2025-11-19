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
- [範例](#範例)
  - [class：CronCommandScheduler](#classcroncommandscheduler)
    - [從檔案讀取內容 整合到排程系統](#從檔案讀取內容-整合到排程系統)

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

# 範例

## class：CronCommandScheduler

```
完整 class：CronCommandScheduler

✔ 可以解析整行
✔ 把 cron 表達式與指令拆開
✔ schedule 執行 subprocess
✔ 支援多行 crontab
```

```
* * * * * python main.py
*/5 * * * * python worker.py --task sync
0 3 * * * /usr/bin/python3 jobs/backup.py
```

CronCommandScheduler.py

```Python
import schedule
import threading
import time
import subprocess
from datetime import datetime
from croniter import croniter


class CronCommandScheduler:

    def __init__(self):
        self.jobs = []

    def add_cron_line(self, line: str):
        """
        範例: "* * * * * python main.py"
        """
        parts = line.strip().split()
        cron_expr = " ".join(parts[0:5])  # 5 格式 (* * * * *)
        command = " ".join(parts[5:])

        if not command:
            raise ValueError("Cron line must include a command, ex: * * * * * python main.py")

        job = {
            "cron": cron_expr,
            "command": command,
            "iter": croniter(cron_expr, datetime.now())
        }

        # 建立下一次排程
        self._schedule_next(job)

        self.jobs.append(job)

    def _schedule_next(self, job):
        now = datetime.now()
        next_time = job["iter"].get_next(datetime)
        diff_seconds = (next_time - now).total_seconds()

        print(f"[Cron] {job['cron']} 下一次時間: {next_time} → Command: {job['command']}")

        schedule.every(diff_seconds).seconds.do(
            self._execute_job, job
        ).tag(job["cron"])

    def _execute_job(self, job):
        schedule.clear(job["cron"])

        print(f"[執行] {datetime.now()} → {job['command']}")
        try:
            subprocess.run(job["command"], shell=True)
        except Exception as e:
            print("執行錯誤:", e)

        # 設定下一次
        self._schedule_next(job)

        return schedule.CancelJob

    def start(self):
        t = threading.Thread(target=self._loop, daemon=True)
        t.start()

    @staticmethod
    def _loop():
        while True:
            schedule.run_pending()
            time.sleep(1)
```

使用範例（main.py）

```Python
from CronCommandScheduler import CronCommandScheduler
import time

cron = CronCommandScheduler()

# 每分鐘執行 python main.py
cron.add_cron_line("* * * * * python main.py")

# 每 5 分鐘執行另一個 job
cron.add_cron_line("*/5 * * * * python worker.py")

cron.start()

while True:
    time.sleep(10)
```

### 從檔案讀取內容 整合到排程系統

```Python
def load_cron_file(path="crontab.txt"):
    commands = []
    with open(path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            commands.append(line)
    return commands
```

```Python
from CronCommandScheduler import CronCommandScheduler
import time

cron = CronCommandScheduler()

# 讀取 crontab.txt
lines = load_cron_file("crontab.txt")

# 加進排程
for line in lines:
    cron.add_cron_line(line)

cron.start()

# 主程式保持運作
while True:
    time.sleep(10)
```
