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
  - [yt-dlp-youtube-oauth2 使用](#yt-dlp-youtube-oauth2-使用)
  - [Get cookies.txt LOCALLY 擴展程式導出 cookies](#get-cookiestxt-locally-擴展程式導出-cookies)
  - [不使用 cookies](#不使用-cookies)
- [測試](#測試)
  - [取得 cookies](#取得-cookies)

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

[coletdjnz/yt-dlp-youtube-oauth2 (可能失效)](https://github.com/coletdjnz/yt-dlp-youtube-oauth2?tab=readme-ov-file#logging-in-)

[【已解决】yt-dlp Sign in to confirm you’re not a bot. 问题](https://d.cellmean.com/p/639ac02d9ab2)

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

## yt-dlp-youtube-oauth2 使用

```
這段描述的是在使用 yt-dlp 時，如何進行 YouTube OAuth2 驗證以授權 yt-dlp 存取你的 YouTube 帳戶。

步驟詳解：
首次運行授權：

當你首次運行 yt-dlp 嘗試下載需要授權的 YouTube 影片時，yt-dlp 會提示你需要進行 OAuth2 驗證。
提供驗證碼：

yt-dlp 會顯示一個授權網址（如 https://www.google.com/device）以及一個驗證碼（如 XXX-YYY-ZZZ）。這是標準的「設備授權流程」，通常用於沒有完整鍵盤輸入的設備（如 YouTube on TV）。
輸入驗證碼授權：

你需要在瀏覽器中打開這個網址，並輸入 yt-dlp 提供的驗證碼，完成授權。這個過程授權 yt-dlp 存取你的 YouTube 帳戶。
授權成功：

授權成功後，yt-dlp 會保存你的 OAuth2 token 資料到其緩存中，這樣下次使用時就不需要再次授權。
處理可能的問題：

如果授權有問題，yt-dlp 會顯示錯誤信息。你可以使用 -v 參數啟用詳細日誌記錄來進行調試，這會顯示詳細的過程。如果你看到日誌中顯示「Loading youtube-oauth2.token_data from cache」，這表示 yt-dlp 嘗試從緩存中加載之前保存的 OAuth2 token。
避免使用 YouTube cookies：

注意：建議不要同時使用 YouTube cookies 和 OAuth2 token，這可能會引發授權問題，因為這兩種方法是不同的授權方式，可能會產生衝突。
使用 yt-dlp 進行 OAuth 授權的流程：
yt-dlp 使用的是 YouTube 的「設備授權流程」，這是一個適合於無鍵盤設備（如電視）的授權方法。這就是為什麼它會顯示「這是 YouTube on TV 客戶端的授權流程」的提示。

在完成這一流程後，OAuth2 token 會被保存，你就可以下載需要授權的影片或進行其他操作了。
如果遇到問題，可以使用 yt-dlp -v 來查看更詳細的錯誤信息並進行排查。
```

## Get cookies.txt LOCALLY 擴展程式導出 cookies

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

# 測試

## 取得 cookies

```sh
curl -I -H "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36"  https://www.youtube.com/watch?v=Owke6Quk7T0
```

cookies

```
GPS=1; Domain=.youtube.com; Expires=Thu, 19-Sep-2024 02:37:26 GMT; Path=/; Secure; HttpOnly;YSC=XVtDBsMQfHE; Domain=.youtube.com; Path=/; Secure; HttpOnly; SameSite=none; Partitioned;VISITOR_INFO1_LIVE=vQ-beCaqavw; Domain=.youtube.com; Expires=Tue, 18-Mar-2025 02:07:26 GMT; Path=/; Secure; HttpOnly; SameSite=none; Partitioned;VISITOR_PRIVACY_METADATA=CgJUVxIEGgAgNw%3D%3D; Domain=.youtube.com; Expires=Tue, 18-Mar-2025 02:07:26 GMT; Path=/; Secure; HttpOnly; SameSite=none; Partitioned
```

```
# Netscape HTTP Cookie File
# http://curl.haxx.se/rfc/cookie_spec.html
# This is a generated file!  Do not edit.

.youtube.com	TRUE	/	TRUE	1760968434	PREF	f7=4100&tz=Asia.Taipei&f6=40000000
.youtube.com	TRUE	/	TRUE	1758272060	LOGIN_INFO	AFmmF2swRAIgHjunQuF7FF1A9LqJX_FilYMrirtStw1hxUeDd6ng5X0CIG1Atvr6_PpUBFwSlUTy8eMKNKtLhnHB5b1OvhmFtQbX:QUQ3MjNmeHZ6QkdLc2xKV2RKYkc2TFBieTJndkFFVm56cWU5UzRPSzViS3NLVjloSDZpSzBfYjlLSklPUnp1aEpRanBDOUVRbHR5T1A1TnNnU2VMcWZjR3cxVzNrZl9FTXdzTmhRRTMxX0Faeld5Zk5GdHdlY3UtQ214aDZzNGd5T3ZScUpBbHpyaVVXamRFZENucWtUTXdjT3lkN0o5ZXFn
.youtube.com	TRUE	/	FALSE	1759888876	SID	g.a000nghEikIkg18u0mVCUu2aPo5pwp9BQFi5CYCeuTu9QcEzLREHi15r-WMdJwBkUNuWDVpZDgACgYKATwSARcSFQHGX2MiMNBwoWE65j-emcHUslyxkxoVAUF8yKq5_Jq-bQ695lFD3nDQKLS80076
.youtube.com	TRUE	/	TRUE	1756864876	__Secure-1PSIDTS	sidts-CjEBUFGohzY-6jKuvM-p094m7e8ICbPkmILk4R276MGo_rt6k5irqq5XihC6CtDwqeu7EAA
.youtube.com	TRUE	/	TRUE	1756864876	__Secure-3PSIDTS	sidts-CjEBUFGohzY-6jKuvM-p094m7e8ICbPkmILk4R276MGo_rt6k5irqq5XihC6CtDwqeu7EAA
.youtube.com	TRUE	/	TRUE	1759888876	__Secure-1PSID	g.a000nghEikIkg18u0mVCUu2aPo5pwp9BQFi5CYCeuTu9QcEzLREH0c-ux6B1DmqaJqvYMJXNMgACgYKAeISARcSFQHGX2Mi0nLRCuH1LA9zrkpDjeSX_BoVAUF8yKoIwl1YiOKvt-B3MeFcY9Hh0076
.youtube.com	TRUE	/	TRUE	1759888876	__Secure-3PSID	g.a000nghEikIkg18u0mVCUu2aPo5pwp9BQFi5CYCeuTu9QcEzLREHTIlugvi96ofV2Gv7iHoa6wACgYKAeISARcSFQHGX2MimPrKtx7IA4w-UiGelWh6bxoVAUF8yKovFU6ZHPv7VaFT-r8Y8NKH0076
.youtube.com	TRUE	/	FALSE	1759888876	HSID	AH_qJk2EvFUIapxJd
.youtube.com	TRUE	/	TRUE	1759888876	SSID	AqnRnoSp4RWxDWflV
.youtube.com	TRUE	/	FALSE	1759888876	APISID	tOzZzsdgUSk6mc3e/A5Z3Yck0TE7-W4FOs
.youtube.com	TRUE	/	TRUE	1759888876	SAPISID	SbodpptYobyBbZ5b/APLZkQ5F9hV7f9Khx
.youtube.com	TRUE	/	TRUE	1759888876	__Secure-1PAPISID	SbodpptYobyBbZ5b/APLZkQ5F9hV7f9Khx
.youtube.com	TRUE	/	TRUE	1759888876	__Secure-3PAPISID	SbodpptYobyBbZ5b/APLZkQ5F9hV7f9Khx
.youtube.com	TRUE	/	FALSE	1726408440	ST-tladcw	session_logininfo=AFmmF2swRAIgHjunQuF7FF1A9LqJX_FilYMrirtStw1hxUeDd6ng5X0CIG1Atvr6_PpUBFwSlUTy8eMKNKtLhnHB5b1OvhmFtQbX%3AQUQ3MjNmeHZ6QkdLc2xKV2RKYkc2TFBieTJndkFFVm56cWU5UzRPSzViS3NLVjloSDZpSzBfYjlLSklPUnp1aEpRanBDOUVRbHR5T1A1TnNnU2VMcWZjR3cxVzNrZl9FTXdzTmhRRTMxX0Faeld5Zk5GdHdlY3UtQ214aDZzNGd5T3ZScUpBbHpyaVVXamRFZENucWtUTXdjT3lkN0o5ZXFn
.youtube.com	TRUE	/	FALSE	1757944438	SIDCC	AKEyXzUgrhsTAbJX1UzVL7smQp-T_YwCll0AFQk1pV_Tw-9-rgXlwGspPr5h9ImL7FOQ6a93
.youtube.com	TRUE	/	TRUE	1757944438	__Secure-1PSIDCC	AKEyXzXiYXUgisaD1tmRVc22nMGoyHlEb5E1vGi5zvT98Y64yboqcVIthXYFMQruwLbsAaaApFw
.youtube.com	TRUE	/	TRUE	1757944438	__Secure-3PSIDCC	AKEyXzUIVtckuuuIxr9zPW7Hqxqr0qQQhKSAWwUgF9TI2pb9y77rU9oS6e0u37rhDqWSGPp3CBc
```
