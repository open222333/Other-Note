# Python 模組 yt-dlp(Youtube下載)

```
```

## 目錄

- [Python 模組 yt-dlp(Youtube下載)](#python-模組-yt-dlpyoutube下載)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
	- [提取資訊](#提取資訊)

## 參考資料

[yt-dlp pypi](https://pypi.org/project/yt-dlp/)

[yt-dlp Github - 文檔位置](https://github.com/yt-dlp/yt-dlp)

# 指令

```bash
# 安裝
pip install yt-dlp
```

# 用法

## 提取資訊

```Python
import json
import yt_dlp

'''取得 youtube影片資訊'''

URL = 'https://www.youtube.com/watch?v=BaW_jenozKc'

ydl_opts = {}
with yt_dlp.YoutubeDL(ydl_opts) as ydl:
    info = ydl.extract_info(URL, download=False)
    print(json.dumps(ydl.sanitize_info(info)))
```
