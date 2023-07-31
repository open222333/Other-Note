# Python 模組-內建 concurrent(管理並發任務)

```
concurrent.futures -- 啟動平行任務
```

## 目錄

- [Python 模組-內建 concurrent(管理並發任務)](#python-模組-內建-concurrent管理並發任務)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[concurrent 官方文檔](https://docs.python.org/zh-tw/3/library/concurrent.html)

# 用法

```Python
import time
from concurrent.futures import ThreadPoolExecutor, wait


class Threading:

    def __init__(self, size: int = None) -> None:
        self.threads = []
        self.size = size

    def set_size(self, size: int):
        self.size = size

    def add_thread(self, func, **args):
        self.threads.append((func, args))

    def run(self):
        with ThreadPoolExecutor(max_workers=self.size) as executor:
            futures = []
            # 提交多個任務給執行緒池
            for thread in self.threads:
                futures.append(executor.submit(thread[0], name=thread[1]['name']))
            wait(futures)


# 定義一個任務函式，用於在多線程中執行

def task(name=None):
    print(f"開始執行任務 {name}")
    time.sleep(2)
    print(f"任務 {name} 執行完成")


def main():
    # 創建 Threading 實例，設置線程池大小為 3
    threading = Threading(size=3)

    # 添加多個任務到線程池
    threading.add_thread(task, name=1)
    threading.add_thread(task, name=2)
    threading.add_thread(task, name=3)
    threading.add_thread(task, name=4)
    threading.add_thread(task, name=5)
    threading.add_thread(task, name=6)
    threading.add_thread(task, name=7)
    threading.add_thread(task, name=8)

    # 執行所有任務並等待完成
    threading.run()

    print("所有任務已完成")


if __name__ == "__main__":
    main()
```
