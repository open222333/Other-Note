# Python 模組 FastAPI(Web框架)

```
FastAPI 是一個用於構建 API 的現代、快速（高性能）的 web 框架
```

## 目錄

- [Python 模組 FastAPI(Web框架)](#python-模組-fastapiweb框架)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[FastAPI pypi](https://pypi.org/project/FastAPI/)

[官方文檔](https://fastapi.tiangolo.com/)

# 指令

```bash
# 安裝
pip install fastapi
```

# 用法

```Python
from fastapi import FastAPI
app = FastAPI()

@app.get("/")
def read_root():
   return {"Hello": "World"}

@app.get("/items/{item_id}")
def read_item(item_id: int, q: str = None):
   return {"item_id": item_id, "q": q}
```
