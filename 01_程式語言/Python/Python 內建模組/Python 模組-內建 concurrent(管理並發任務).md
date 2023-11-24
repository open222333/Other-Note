# Python 模組-內建 concurrent(管理並發任務)

```
concurrent.futures -- 啟動平行任務
```

## 目錄

- [Python 模組-內建 concurrent(管理並發任務)](#python-模組-內建-concurrent管理並發任務)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [用法](#用法)
  - [多線程執行時，要取得每個線程的返回值](#多線程執行時要取得每個線程的返回值)
  - [強制結束目前執行緒池的所有執行緒](#強制結束目前執行緒池的所有執行緒)
    - [使用 concurrent.futures.as\_completed 或 concurrent.futures.wait 來實現在等待任務完成的同時繼續進行其他操作。](#使用-concurrentfuturesas_completed-或-concurrentfutureswait-來實現在等待任務完成的同時繼續進行其他操作)
  - [處理大量 MongoDB 數據時 範例](#處理大量-mongodb-數據時-範例)

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

## 多線程執行時，要取得每個線程的返回值

```Python
import concurrent.futures

def worker_function(thread_id):
    # 在這裡進行耗時的操作
    result = f"Hello from Thread {thread_id}"
    return result

# 創建 ThreadPoolExecutor
with concurrent.futures.ThreadPoolExecutor() as executor:
    # 提交任務給線程池，每個線程的ID作為參數
    results = executor.map(worker_function, range(5))

# 獲取所有線程的返回值
for result in results:
    print(result)
```

```Python
import concurrent.futures

def worker_function(arg1, arg2):
    # 模擬一些耗時的工作
    result = arg1 + arg2
    return result

def main():
    # 設置最大執行緒數量為 3
    max_workers=3

    # 使用 ThreadPoolExecutor 創建一個執行緒池
    with concurrent.futures.ThreadPoolExecutor(max_workers=max_workers) as executor:
        # 提交任務到執行緒池，得到一個 Future 物件
        future = executor.submit(worker_function, 10, 20)

        # 可以繼續執行其他工作

        # 等待 Future 完成並取得結果，timeout 是最多等待的秒數
        # 當前執行緒會被阻塞
        result = future.result(timeout=2)

        if future.done():
            # 如果 Future 完成，處理結果
            print(f"結果為: {result}")
        else:
            # 如果 Future 沒有完成，可能是因為超時
            print("工作尚未完成")

if __name__ == "__main__":
    main()
```

## 強制結束目前執行緒池的所有執行緒

```Python
import concurrent.futures
import time

def my_task(index):
    print(f"Task {index} started")
    time.sleep(5)  # 模擬執行時間
    print(f"Task {index} completed")

def main():
    # 建立執行緒池
    with concurrent.futures.ThreadPoolExecutor() as executor:
        # 提交一些任務
        futures = [executor.submit(my_task, i) for i in range(5)]

        # 等待所有任務完成，或者直到執行緒池被 shutdown
        concurrent.futures.wait(futures)

        # 強制結束執行緒池的所有執行緒
        executor.shutdown(wait=False)  # wait=False 表示不等待執行緒完成

    print("All tasks completed")

if __name__ == "__main__":
    main()
```

### 使用 concurrent.futures.as_completed 或 concurrent.futures.wait 來實現在等待任務完成的同時繼續進行其他操作。

```Python
import concurrent.futures

def download_image(img_url, comic_path):
    # 下載圖片的邏輯

with concurrent.futures.ThreadPoolExecutor() as executor:
    futures = [executor.submit(download_image, img_url, comic_path) for img_url in data['img_download_urls'].values()]

    for future in concurrent.futures.as_completed(futures):
        try:
            result = future.result(timeout=2)
            # 在這裡處理已完成的任務的結果(result)
        except concurrent.futures.TimeoutError:
            # 在這裡處理超時的情況
            print("Task timed out")

# 這裡的程式碼會在所有任務完成之後執行
```

```Python
import concurrent.futures

def download_image(img_url, comic_path):
    # 下載圖片的邏輯

with concurrent.futures.ThreadPoolExecutor() as executor:
    futures = [executor.submit(download_image, img_url, comic_path) for img_url in data['img_download_urls'].values()]

    done, not_done = concurrent.futures.wait(futures, timeout=2)

    # 在這裡處理已完成的任務的結果
    for future in done:
        result = future.result()

    # 在這裡處理未完成的任務
    for future in not_done:
        future.cancel()

# 這裡的程式碼會在所有任務完成之後執行
```

## 處理大量 MongoDB 數據時 範例

```
可以使用 concurrent.futures.ThreadPoolExecutor 或 concurrent.futures.ProcessPoolExecutor 來啟動多個線程或進程。

由於 Python 中的全局解釋器鎖（GIL）的存在，ThreadPoolExecutor 主要用於 I/O 密集型任務。
如果你的處理邏輯主要是 CPU 密集型的，你可能需要考慮使用 ProcessPoolExecutor 來利用多個進程。
```

```Python
from concurrent.futures import ThreadPoolExecutor
import pymongo

class YourClass:
    def __init__(self, mongo_client):
        self.mongo_client = mongo_client

    def process_data(self, data):
        # Actual logic to process the data
        # 實際處理數據的邏輯
        print(data)

    def process_mongo_data_parallel(self, collection_name, query=None, num_threads=2):
        col = self.mongo_client.your_database[collection_name]
        total = col.count_documents(query) if query else col.count_documents({})

        with ThreadPoolExecutor(max_workers=num_threads) as executor:
            # Calculate the data chunk each thread will process
            # 計算每個線程處理的數據量
            chunk_size = total // num_threads

            # Use map function to apply the processing function to different data chunks
            # 使用 map 函數將處理函數應用到不同的數據塊
            futures = []
            for i in range(num_threads):
                start = i * chunk_size
                end = (i + 1) * chunk_size if i != num_threads - 1 else total
                chunk_query = query if query else {}
                future = executor.submit(self.__process_data_chunk, col, chunk_query, start, end)
                futures.append(future)

            # Wait for all threads to complete
            # 等待所有線程完成
            for future in futures:
                future.result()

    def __process_mongo_data_chunk(self, col, query, start, end):
        data_chunk = col.find(query).skip(start).limit(end - start)
        for data in data_chunk:
            self.process_data(data)

    def __process_mysql_data_chunk(self, cursor, query, start, end):
        sql = f"SELECT * FROM your_table WHERE your_condition LIMIT {start}, {end - start}"

        cursor.execute(sql)
        data_chunk = cursor.fetchall()

        for data in data_chunk:
            self.process_data(data)

# Example usage
mongo_client = pymongo.MongoClient('your_mongo_uri')
your_instance = YourClass(mongo_client)
your_instance.process_mongo_data_parallel(collection_name='your_collection', num_threads=4)
```