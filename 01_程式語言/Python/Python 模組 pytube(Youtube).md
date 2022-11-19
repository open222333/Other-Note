# Python 模組 pytube(Youtube)

```
pytube 是一個用於下載 YouTube 視頻的輕量級、Pythonic、無依賴性庫（和命令行實用程序）。
```

## 目錄

- [Python 模組 pytube(Youtube)](#python-模組-pytubeyoutube)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[pytube 源代碼 Github](https://github.com/pytube/pytube)

[pytube pypi](https://pypi.org/project/pytube/)

[pytube 文檔](https://pytube.io/en/latest/)

[pytube YouTube Object](https://pytube.io/en/latest/api.html#youtube-object)

# 指令

```bash
# 安裝
pip install pytube
```

# 用法

```Python
from pytube import YouTube

yt = YouTube('http://youtube.com/watch?v=2lAe1cqCOXo')

# 獲取視頻標題
yt.title

# 獲取縮略圖網址
yt.thumbnail_url
```
