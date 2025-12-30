# 技術標準-M3U8(HLS 播放清單)

```
m3u8 = HLS 播放清單（playlist）

不是影片本體, 是一個文字檔

告訴播放器：

    影片切成哪些片段
    每個片段在哪裡
    有沒有多畫質 / 字幕

類似「影片目錄」
```

HLS 播放整體流程

```
播放器
  ↓ 讀
master.m3u8   ←（畫質 / 字幕選單）
  ↓ 選
video/1080p.m3u8
  ↓ 逐段下載
0001.ts → 0002.ts → 0003.ts ...
```

## 目錄

- [技術標準-M3U8(HLS 播放清單)](#技術標準-m3u8hls-播放清單)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [字幕與m3u8相關](#字幕與m3u8相關)
    - [測試相關](#測試相關)
- [範例](#範例)
  - [最簡單的 m3u8（單畫質）](#最簡單的-m3u8單畫質)
  - [實務常見：主播放清單（master.m3u8）](#實務常見主播放清單masterm3u8)
  - [HLS 正確加入 VTT 的方式（官方標準）](#hls-正確加入-vtt-的方式官方標準)

## 參考資料

[IETF RFC 8216 (HLS 協議標準)](https://datatracker.ietf.org/doc/html/rfc8216)

[m3u8格式详解](https://blog.csdn.net/weixin_39399492/article/details/131687865)

[m3u8格式详解](https://blog.csdn.net/weixin_39399492/article/details/131687865?utm_source=chatgpt.com)

[HLS 教學 (上) — 從閱讀Spec 開始](https://yuanchieh.page/posts/2018/2018-09-24-hls-%E6%95%99%E5%AD%B8-%E4%B8%8A-%E5%BE%9E%E9%96%B1%E8%AE%80spec-%E9%96%8B%E5%A7%8B/?utm_source=chatgpt.com)

### 字幕與m3u8相關

[多字幕转码打包最佳实践](https://www.alibabacloud.com/help/zh/ims/use-cases/multi-caption-transcoding-and-packaging-best-practices)

[我怎樣才能把 VTT 字幕加到已經弄好的 HLS 影片裡？](https://www.reddit.com/r/ffmpeg/comments/120seti/how_can_i_add_vtt_subtitles_to_an_already_formed/?tl=zh-hant)

[Integrate captions into your HLS manifest - 將字幕整合到 HLS 清單中](http://support.captionhub.com/timbra/anHrwmAnQCHCsf4DjG5uGX/integrate-captions-into-your-hls-manifest/38A8ShVEARoBJqhfcLUBjf?utm_source=chatgpt.com)

[How to add external WebVTT subtitles into HTTP Live Stream on iOS client - 如何在 iOS 用戶端上為 HTTP 直播串流新增外部 WebVTT 字幕](https://stackoverflow.com/questions/43161124/how-to-add-external-webvtt-subtitles-into-http-live-stream-on-ios-client)

### 測試相關

[hls 播放測試](https://hlsjs.video-dev.org/demo/)

# 範例

## 最簡單的 m3u8（單畫質）

```m3u8
#EXTM3U
#EXT-X-VERSION:3
#EXT-X-TARGETDURATION:10

#EXTINF:10,
0001.ts
#EXTINF:10,
0002.ts
#EXTINF:10,
0003.ts

#EXT-X-ENDLIST
```

| 行                       | 說明         |
| ----------------------- | ---------- |
| `#EXTM3U`               | 宣告這是 m3u8  |
| `#EXT-X-VERSION`        | HLS 版本     |
| `#EXT-X-TARGETDURATION` | 最大片段長度     |
| `#EXTINF`               | 下一個片段的秒數   |
| `0001.ts`               | 影片實際檔案     |
| `#EXT-X-ENDLIST`        | 點播影片（不是直播） |

## 實務常見：主播放清單（master.m3u8）

```m3u8
#EXTM3U
#EXT-X-VERSION:6

#EXT-X-STREAM-INF:BANDWIDTH=800000,RESOLUTION=1280x720
video/720p.m3u8

#EXT-X-STREAM-INF:BANDWIDTH=5000000,RESOLUTION=1920x1080
video/1080p.m3u8
```

意思是

「這部影片有 720p 跟 1080p，播放器自己選適合的」

## HLS 正確加入 VTT 的方式（官方標準）

主播放清單（master.m3u8）

字幕一定要寫在 master playlist，用 #EXT-X-MEDIA。

```m3u8
#EXTM3U
#EXT-X-VERSION:6

# 字幕定義
#EXT-X-MEDIA:TYPE=SUBTITLES,GROUP-ID="subs",NAME="Chinese",LANGUAGE="zh",DEFAULT=YES,AUTOSELECT=YES,URI="subtitles/zh.vtt"
#EXT-X-MEDIA:TYPE=SUBTITLES,GROUP-ID="subs",NAME="English",LANGUAGE="en",DEFAULT=NO,AUTOSELECT=YES,URI="subtitles/en.vtt"

# 視訊串流
#EXT-X-STREAM-INF:BANDWIDTH=5000000,RESOLUTION=1920x1080,CODECS="avc1.640028,mp4a.40.2",SUBTITLES="subs"
video/1080p.m3u8
```

VTT 檔（zh.vtt）

```vtt
WEBVTT

00:00:01.000 --> 00:00:04.000
你好，世界

00:00:05.000 --> 00:00:08.000
這是 HLS 字幕範例
```