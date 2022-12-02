# Python 模組-內建 threading(多執行緒)

```
基於線程的並行

這個模塊在較低級的模塊 _thread 基礎上建立較高級的線程接口。
參見：queue 模塊。
```

## 目錄

- [Python 模組-內建 threading(多執行緒)](#python-模組-內建-threading多執行緒)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

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
