# Python 模組 yt-dlp(Youtube下載)

```
```

## 目錄

- [Python 模組 yt-dlp(Youtube下載)](#python-模組-yt-dlpyoutube下載)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [範例相關](#範例相關)
    - [狀況相關](#狀況相關)
      - [\[youtube\] Sign in to confirm you’re not a bot. This helps protect our community](#youtube-sign-in-to-confirm-youre-not-a-bot-this-helps-protect-our-community)
- [指令](#指令)
- [用法](#用法)
  - [提取資訊](#提取資訊)
  - [下載影片](#下載影片)
- [例外狀況](#例外狀況)
  - [不使用 cookies](#不使用-cookies)

## 參考資料

[yt-dlp pypi](https://pypi.org/project/yt-dlp/)

[yt-dlp Github - 文檔位置](https://github.com/yt-dlp/yt-dlp)

[yt-dlp YoutubeDL 選項](https://github.com/yt-dlp/yt-dlp/blob/master/yt_dlp/YoutubeDL.py#L180)

### 範例相關

[YT-DLP download range not working for youtube links](https://stackoverflow.com/questions/73921240/yt-dlp-download-range-not-working-for-youtube-links)

[YT-DLP Error: "Sign in to confirm you’re not a bot. This helps protect our community". Proxy recommendation](https://www.reddit.com/r/youtubedl/comments/1e6bzu4/ytdlp_error_sign_in_to_confirm_youre_not_a_bot/?rdt=46892)

### 狀況相關

#### [youtube] Sign in to confirm you’re not a bot. This helps protect our community

[[youtube] Sign in to confirm you’re not a bot. This helps protect our community](https://github.com/yt-dlp/yt-dlp/issues/10128)

[Extractors - Passing Visitor Data without cookies](https://github.com/yt-dlp/yt-dlp/wiki/Extractors#passing-visitor-data-without-cookies)

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


def download_youtube(urls, dir_path, temp_path=None, username=None, password=None, start_time:int = 0, end_time:int = None):
    '''下載 youtube影片 以及 圖片

    YoutubeDL選項
    https://github.com/yt-dlp/yt-dlp/blob/master/yt_dlp/YoutubeDL.py

	dir_path: 輸出資料夾
	temp_path: 下載時，暫存資料夾
	start_time: 開始時間
	end_time: 結束時間
	username: 登入帳號
	password: 登入密碼
    '''
	if not temp_path:
		temp_path = dir_path

    params = {
        'username': username,
        'password': password,
        'writethumbnail': True,
        # home 輸出路徑 temp 暫存路徑
        'paths': {'home': dir_path, 'temp': temp_path},
		'outtmpl': '%(id)s.%(ext)s'
    }

	# 只抓片段
	if end_time:
		params['download_ranges'] = download_range_func(None, [(start_time, end_time)])

    with YoutubeDL(params=params) as ydl:
        ydl.download(urls)
```

# 例外狀況

```
這是一個很好的方法來處理YouTube的cookie，特別是當你需要通過yt_dlp來下載私人或受限的內容時。以下是你所描述的步驟的繁體中文說明：

使用 Get cookies.txt LOCALLY 擴展程式導出 cookies：
在瀏覽器中安裝並啟用 Get cookies.txt LOCALLY 擴展程式。這個擴展程式可以幫助你導出當前網站的所有 cookies。

登錄你的 YouTube 帳號並運行擴展程式：
確保你已經登錄到 YouTube 帳號，然後在擴展程式中選擇“Export all cookies”來導出 cookies。

點擊“Export all cookies”以保存 cookies 到 .txt 文件：
導出完成後，你會得到一個包含所有 cookies 的 .txt 文件。

將此 .txt 文件保存到與你的程式碼相同的文件夾中：
確保將導出的 cookies 文件（例如 cookies.txt）放在與你執行程式碼的目錄相同的位置。

更新你的 yt_dlp 設定以包含 cookies：
在你的程式碼中設置 yt_dlp 的選項，將 cookiefile 選項指向你保存的 cookies 文件。以下是一個範例設定：
```

```python
ydl_opts = {
    'format': 'bestaudio/best',
    'quiet': True,
    'postprocessors': [{
        'key': 'FFmpegExtractAudio',
        'preferredcodec': 'mp3',
        'preferredquality': '192',
    }],
    'cookiefile': 'cookies.txt'
}
```

```Python
import yt_dlp

# 設定 yt_dlp 的選項
ydl_opts = {
    'format': 'bestaudio/best',
    'quiet': True,
    'postprocessors': [{
        'key': 'FFmpegExtractAudio',
        'preferredcodec': 'mp3',
        'preferredquality': '192',
    }],
    'cookiefile': 'cookies.txt'  # 指定 cookies.txt 檔案的位置
}

# 要下載的 YouTube 影片 URL
url = 'https://www.youtube.com/watch?v=YOUR_VIDEO_ID'

# 使用 yt_dlp 進行下載
with yt_dlp.YoutubeDL(ydl_opts) as ydl:
    ydl.download([url])

print("下載完成！")
```

## 不使用 cookies

```Python
def download_youtube(urls, dir_path, temp_path=None, username=None, password=None, start_time:int = 0, end_time:int = None, po_token=None, visitor_data=None):
    '''下載 youtube影片 以及 圖片

    YoutubeDL選項
    https://github.com/yt-dlp/yt-dlp/blob/master/yt_dlp/YoutubeDL.py

    dir_path: 輸出資料夾
    temp_path: 下載時，暫存資料夾
    start_time: 開始時間
    end_time: 結束時間
    username: 登入帳號
    password: 登入密碼
    po_token: PO_TOKEN值
    visitor_data: VISITOR_DATA值
    '''
    if not temp_path:
        temp_path = dir_path

    params = {
        'username': username,
        'password': password,
        'writethumbnail': True,
        # home 輸出路徑 temp 暫存路徑
        'paths': {'home': dir_path, 'temp': temp_path},
        'outtmpl': '%(id)s.%(ext)s',
        'extractor_args': {
            'youtube': {
                'player-client': 'web,default',
                'player-skip': 'webpage,configs',
                'po_token': f'web+{po_token}',
                'visitor_data': visitor_data
            }
        }
    }

    # 只抓片段
    if end_time:
        params['download_ranges'] = download_range_func(None, [(start_time, end_time)])

    with YoutubeDL(params=params) as ydl:
        ydl.download(urls)
```
