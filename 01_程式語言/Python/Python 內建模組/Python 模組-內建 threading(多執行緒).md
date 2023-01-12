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
		- [教學範例](#教學範例)
- [用法](#用法)
	- [物件導向](#物件導向)

## 參考資料

[threading 官方文檔](https://docs.python.org/zh-tw/3/library/threading.html)

[Python 睡眠可繼續執行的 Thread Timer()](https://blog.longwin.com.tw/2021/09/python-sleep-time-thread-timer-2021/)

### 教學範例

[Python 多執行緒 threading 模組平行化程式設計教學](https://blog.gtwang.org/programming/python-threading-multithreaded-programming-tutorial/)

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

## 物件導向

```Python
import threading
import time

# 子執行緒類別
class MyThread(threading.Thread):
  def __init__(self, num):
    threading.Thread.__init__(self)
    self.num = num

  def run(self):
    print("Thread", self.num)
    time.sleep(1)

# 建立 5 個子執行緒
threads = []
for i in range(5):
  threads.append(MyThread(i))
  threads[i].start()

# 主執行緒繼續執行自己的工作
# ...

# 等待所有子執行緒結束
for i in range(5):
  threads[i].join()

print("Done.")
```
