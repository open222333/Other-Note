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
	- [下載影片](#下載影片)

## 參考資料

[yt-dlp pypi](https://pypi.org/project/yt-dlp/)

[yt-dlp Github - 文檔位置](https://github.com/yt-dlp/yt-dlp)

[yt-dlp YoutubeDL 選項](https://github.com/yt-dlp/yt-dlp/blob/master/yt_dlp/YoutubeDL.py#L180)

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

## 下載影片

```Python
from yt_dlp import YoutubeDL


def download_youtube(urls, start_time:int=None, end_time:int=None):
    '''下載 youtube影片 以及 圖片

    YoutubeDL選項
    https://github.com/yt-dlp/yt-dlp/blob/master/yt_dlp/YoutubeDL.py
    '''
    params = {
        'username': YT_USERNAME,
        'password': YT_PASSWORD,
        'writethumbnail': True,
        # home 輸出路徑 temp 暫存路徑
        'paths': {'home': YT_DIR, 'temp': YT_DIR}
    }

    with YoutubeDL(params=params) as ydl:
        ydl.download(urls)
```
