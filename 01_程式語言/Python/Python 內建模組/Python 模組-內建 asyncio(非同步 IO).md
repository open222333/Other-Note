# Python 模組-內建 asyncio(非同步 I/O)

```
```

## 目錄

- [Python 模組-內建 asyncio(非同步 I/O)](#python-模組-內建-asyncio非同步-io)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [用法](#用法)

## 參考資料

[asyncio 官方文檔](https://docs.python.org/zh-tw/3/library/asyncio.html)

[Python asyncio 從不會到上路](https://myapollo.com.tw/blog/begin-to-asyncio/)

# 用法

```Python
import aiohttp
import asyncio

"""
import requests


def do_requests():
    resp = requests.get('https://example.com')
    print('example.com =>', resp.status_code)


def main():
    for _ in range(0, 10):
        do_requests()


if __name__ == '__main__':
    main()
"""


def do_requests(session):
    return session.get('https://example.com')


async def main():
    async with aiohttp.ClientSession() as session:
        tasks = []
        for _ in range(0, 10):
            tasks.append(do_requests(session))

        results = await asyncio.gather(*tasks)
        for r in results:
            print('example.com =>', r.status)


if __name__ == '__main__':
    asyncio.run(main())
```

```Python
import asyncio

async def main():
    asyncio.sleep(1)
    print('hello')

asyncio.run(main())
```