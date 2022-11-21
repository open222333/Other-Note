# Python 模組 pytube(Youtube)

```
pytube 是一個用於下載 YouTube 視頻的輕量級、Pythonic、無依賴性庫（和命令行實用程序）。
```

## 目錄

- [Python 模組 pytube(Youtube)](#python-模組-pytubeyoutube)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [範例相關](#範例相關)
- [指令](#指令)
- [用法](#用法)
	- [取得字幕檔](#取得字幕檔)
	- [讀取 Youtube 清單資訊](#讀取-youtube-清單資訊)

## 參考資料

[pytube 源代碼 Github](https://github.com/pytube/pytube)

[pytube pypi](https://pypi.org/project/pytube/)

[pytube 文檔](https://pytube.io/en/latest/)

[pytube YouTube Object](https://pytube.io/en/latest/api.html#youtube-object)

### 範例相關

[下載 Youtube 影片 ( mp4、mp3、字幕 )](https://ithelp.ithome.com.tw/m/articles/10298678)

# 指令

```bash
# 安裝
pip install pytube
```

# 用法

```Python
from pytube import YouTube

yt = YouTube('http://youtube.com/watch?v=2lAe1cqCOXo')

# 影片標題
yt.title

# 影片長度 ( 秒 )
yt.length

# 影片作者
yt.author

# 影片作者頻道網址
yt.channel_url

# 影片縮圖網址
yt.thumbnail_url

# 影片觀看數
yt.views

# 取得所有語系
yt.captions
# 取得英文語系
caption = yt.captions.get_by_language_code('en')
# 將語系轉換成 xml
xml = caption.xml_captions

# 查看影片支援哪些畫質
yt.streams.all()

# 下載最高畫質影片，如果沒有設定 filename，則以原本影片的 title 作為檔名
yt.streams.filter().get_highest_resolution().download(filename='baby_shart.mp4')

# 指定下載影片的畫質
yt.streams.filter().get_by_resolution('360p').download(filename='oxxostudio_360p.mp4')

# 單獨取出 Youtube 的音軌儲存為 mp3 檔案 ( 預設為 mp4，存檔時改檔名為 mp3 就會變成 mp3 )
yt.streams.filter().get_audio_only().download(filename='oxxostudio.mp3')
```

## 取得字幕檔

```Python
import os
os.chdir('/content/drive/MyDrive/Colab Notebooks')  # 使用 Colab 要換路徑使用

from pytube import YouTube
from bs4 import BeautifulSoup

yt = YouTube('https://www.youtube.com/watch?v=R93ce4FZGbc')
print(yt.captions)                                 # 取得所有語系
caption = yt.captions.get_by_language_code('en')   # 取得英文語系
xml = caption.xml_captions                         # 將語系轉換成 xml
#print(xml)

def xml2srt(text):
    soup = BeautifulSoup(text)                     # 使用 BeautifulSoup 轉換 xml
    ps = soup.findAll('p')                         # 取出所有 p tag 內容

    output = ''                                    # 輸出的內容
    num = 0                                        # 每段字幕編號
    for i, p in enumerate(ps):
        try:
            a = p['a']                             # 如果是自動字幕，濾掉有 a 屬性的 p tag
        except:
            try:
                num = num + 1                      # 每段字幕編號加 1
                text = p.text                      # 取出每段文字
                t = int(p['t'])                    # 開始時間
                d = int(p['d'])                    # 持續時間

                h, tm = divmod(t,(60*60*1000))     # 轉換取得小時、剩下的毫秒數
                m, ts = divmod(tm,(60*1000))       # 轉換取得分鐘、剩下的毫秒數
                s, ms = divmod(ts,1000)            # 轉換取得秒數、毫秒

                t2 = t+d                           # 根據持續時間，計算結束時間
                if t2 > int(ps[i+1]['t']): t2 = int(ps[i+1]['t'])  # 如果時間算出來比下一段長，採用下一段的時間
                h2, tm = divmod(t2,(60*60*1000))   # 轉換取得小時、剩下的毫秒數
                m2, ts = divmod(tm,(60*1000))      # 轉換取得分鐘、剩下的毫秒數
                s2, ms2 = divmod(ts,1000)          # 轉換取得秒數、毫秒


                output = output + str(num) + '\n'  # 產生輸出的檔案，\n 表示換行
                output = output + f'{h:02d}:{m:02d}:{s:02d},{ms:03d} --> {h2:02d}:{m2:02d}:{s2:02d},{ms2:03d}' + '\n'
                output = output + text + '\n'
                output = output + '\n'
            except:
                pass

    return output

#print(xml2srt(xml))
with open('oxxostudio.srt','w') as f1:
    f1.write(xml2srt(xml))    # 儲存為 srt

print('ok!')
```

## 讀取 Youtube 清單資訊

```Python
# 使用 pytube 讀取 Youtube 清單網址後，就能將該影片清單的所有影片網址，輸出成為串列。
from pytube import Playlist


playlist = Playlist('https://www.youtube.com/watch?v=mOPRaLPh-YU&list=PL9ACDjBMkp9wViVmgpYweGkNqh62pHspF')

# 印出清單結果
playlist.video_urls
'''
['https://www.youtube.com/watch?v=mOPRaLPh-YU',
 'https://www.youtube.com/watch?v=wARhTJH1fJI',
 'https://www.youtube.com/watch?v=WLjePGUCRqc']
'''
```