# Python 模組-內建 threading(線程的並行)

```
基於線程的並行
```

## 參考資料

[threading 官方文檔](https://docs.python.org/zh-tw/3/library/threading.html)

[Python 睡眠可繼續執行的 Thread Timer()](https://blog.longwin.com.tw/2021/09/python-sleep-time-thread-timer-2021/)

# 用法

```Python
from threading import Timer
import time

def hello():
    print("Hello World!")

t = Timer(5, hello) # 在5秒後，自動執行 hello()
t.start()

for i in [1, 2, 3, 4, 5, 6]:
    print(i)
    time.sleep(1)
```
