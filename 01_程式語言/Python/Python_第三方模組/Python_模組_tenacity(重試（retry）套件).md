# Python 模組 tenacity(重試（retry）套件)

```
tenacity 是 Python 中最靈活好用的 重試（retry）套件，比 retrying 更新、維護積極，而且語法更乾淨。
```

## 目錄

- [Python 模組 tenacity(重試（retry）套件)](#python-模組-tenacity重試retry套件)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
  - [設定重試次數、等待時間、捕捉例外](#設定重試次數等待時間捕捉例外)
  - [捕捉特定例外](#捕捉特定例外)
  - [指數回退（exponential backoff）(適合 API 呼叫、網路不穩定等情況)](#指數回退exponential-backoff適合-api-呼叫網路不穩定等情況)
  - [重試前後的 callback](#重試前後的-callback)
  - [傳回最後一次例外，不再丟出（return instead of raise）](#傳回最後一次例外不再丟出return-instead-of-raise)
  - [使用參數判斷是否重試（retry\_if\_result）](#使用參數判斷是否重試retry_if_result)
  - [搭配 async / await（常用 Tenacity 完全支援 async）](#搭配-async--await常用-tenacity-完全支援-async)
  - [API 重試 + 伺服器錯誤判斷 + 指數退避](#api-重試--伺服器錯誤判斷--指數退避)

## 參考資料

[tenacity pypi](https://pypi.org/project/tenacity/)

# 指令

```bash
# 安裝
pip install tenacity
```

# 用法

```Python
from tenacity import retry

@retry
def my_function():
    print("執行中…")
    raise Exception("失敗示範")

my_function()
```

```
無限重試

間隔 0 秒

捕捉所有 Exception
```

## 設定重試次數、等待時間、捕捉例外

```Python
from tenacity import retry, stop_after_attempt, wait_fixed

@retry(
    stop=stop_after_attempt(3),     # 最多重試 3 次
    wait=wait_fixed(2),             # 每次間隔 2 秒
)
def fetch_data():
    raise ValueError("錯誤示範")

fetch_data()
```

## 捕捉特定例外

避免重試無意義的錯誤（例如語法錯誤、參數錯誤）

```Python
from tenacity import retry, retry_if_exception_type, stop_after_attempt

@retry(
    retry=retry_if_exception_type(ConnectionError),
    stop=stop_after_attempt(5)
)
def connect():
    raise ConnectionError("連線失敗")

connect()
```

## 指數回退（exponential backoff）(適合 API 呼叫、網路不穩定等情況)

```Python
from tenacity import retry, wait_exponential, stop_after_attempt

@retry(
    wait=wait_exponential(multiplier=1, min=1, max=10),  # 1, 2, 4, 8, 10 秒
    stop=stop_after_attempt(5)
)
def api_call():
    raise Exception("API Error")

api_call()
```

## 重試前後的 callback

```Python
from tenacity import retry, before_log, after_log, stop_after_attempt, wait_fixed
import logging

logger = logging.getLogger(__name__)

@retry(
    before=before_log(logger, logging.INFO),
    after=after_log(logger, logging.ERROR),
    stop=stop_after_attempt(3),
    wait=wait_fixed(1)
)
def run_task():
    raise Exception("錯誤")

run_task()
```

## 傳回最後一次例外，不再丟出（return instead of raise）

```Python
from tenacity import retry, stop_after_attempt, retry_error_callback

def final_result(retry_state):
    return f"失敗：{retry_state.outcome.exception()}"

@retry(
    stop=stop_after_attempt(3),
    retry_error_callback=final_result
)
def test():
    raise Exception("Oops")

print(test())
# 結果：失敗：Oops
```

## 使用參數判斷是否重試（retry_if_result）

```Python
from tenacity import retry, retry_if_result, stop_after_attempt

def is_bad(result):
    return result is None

@retry(
    retry=retry_if_result(is_bad),
    stop=stop_after_attempt(3)
)
def api():
    return None   # 模擬 API 回傳錯誤

print(api())
```

## 搭配 async / await（常用 Tenacity 完全支援 async）

```Python
import asyncio
from tenacity import retry, stop_after_attempt, wait_fixed

@retry(stop=stop_after_attempt(3), wait=wait_fixed(1))
async def fetch_async():
    print("Try…")
    raise RuntimeError("Async error")

asyncio.run(fetch_async())
```

## API 重試 + 伺服器錯誤判斷 + 指數退避

```Python
import requests
from tenacity import (
    retry,
    wait_exponential,
    stop_after_attempt,
    retry_if_exception_type
)

@retry(
    wait=wait_exponential(min=1, max=8),
    stop=stop_after_attempt(5),
    retry=retry_if_exception_type((requests.ConnectionError, requests.Timeout))
)
def fetch_url(url):
    resp = requests.get(url, timeout=3)
    if resp.status_code >= 500:
        raise requests.ConnectionError("Server error")
    return resp.text
```
