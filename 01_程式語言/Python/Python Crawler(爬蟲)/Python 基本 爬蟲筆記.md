# Python 基本 爬蟲筆記

## 參考資料

[cURL轉成個別語言範本](https://curlconverter.com/#python)

[Python學習資源整理 - 爬蟲](https://www.learncodewithmike.com/2020/02/python3-learning.html)

[All User Agent Strings(所有 user-agent 資訊)](http://useragentstring.com/pages/useragentstring.php?name=All)

# XPath

[XPath教程](https://www.runoob.com/xpath/xpath-tutorial.html)


# CSS

[CSS 基本](https://developer.mozilla.org/zh-TW/docs/Learn/Getting_started_with_the_web/CSS_basics)


# cURL

[cURL 方法](https://curl.se/docs/manpage.html)

# blob video 下載

[How do we download a blob url video](https://stackoverflow.com/questions/42901942/how-do-we-download-a-blob-url-video)

```Python
# 下載 Blob 視頻，您必須從頁面檢查中獲取主段 url，就像給定的圖像一樣，通過代碼中提到它的 url
import requests
import m3u8
import subprocess

master_url ='master_url_from_inspect_network'
# past your page inspect request header

r = requests.get(master_url)
m3u8_master = m3u8.loads(r.text)
print(m3u8_master)
playlist_url =m3u8_master.data['playlists'][0]['uri']
play_r = requests.get(playlist_url)
m3u8_master_play = m3u8.loads(play_r.text)
m3_data=(m3u8_master_play.data)

m3_datas = m3_data['segments'][0]['uri']

with open('video.ts','wb') as fs:
    for segments in m3_data['segments']:
       uri = segments['uri']
       print(uri)
       r = requests.get(uri)
       fs.write(r.content)
```