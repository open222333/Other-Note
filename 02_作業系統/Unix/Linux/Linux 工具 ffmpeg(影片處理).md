# Linux 工具 ffmpeg(影片處理)

```
```

## 目錄

- [Linux 工具 ffmpeg(影片處理)](#linux-工具-ffmpeg影片處理)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [安裝相關](#安裝相關)
		- [HLS參數相關](#hls參數相關)
		- [ffprobe相關](#ffprobe相關)
		- [應用相關](#應用相關)
- [安裝](#安裝)
- [指令 ffmpeg](#指令-ffmpeg)
- [指令 ffprobe](#指令-ffprobe)
- [HLS（HTTP Live Stream）](#hlshttp-live-stream)

## 參考資料

[官方文檔](https://ffmpeg.org/ffmpeg.html)

[官方下載](http://ffmpeg.org/download.html)

[【CPU】关于x86、x86_64/x64、amd64和arm64/aarch64](https://www.jianshu.com/p/2753c45af9bf)

```
x86=i386=IA32
amd64=x86_64=x64!=IA64
```

### 安裝相關

[快速安装 FFmpeg 静态构建版本](https://anlan.cc/191)

### HLS參數相關

[文檔 有關ffmpeg的HLS 參數說明：](https://ffmpeg.org/ffmpeg-formats.html)

[Options (HLS 參數說明)](http://underpop.online.fr/f/ffmpeg/help/options-51.htm.gz)

[如何利用ffmpeg將影片轉換成HLS切片](https://jerry.thesolarsystems.net/?p=969)

### ffprobe相關

[ffprobe](https://ffmpeg.org/ffprobe.html)

[FFmpeg之ffprobe](https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/746195/)

### 應用相關

[ffmpeg常用指令介紹](https://wilsbur.pixnet.net/blog/post/146836324)

[影音剪輯 / 使用 ffmpeg 分割影片 (指定開始及結束時間或固定時間長度分割)](https://note.charlestw.com/ffmpeg-trim-chunk/)

[FFmpeg转码太慢的解决方案](https://blog.51cto.com/xiaohaiwa/5380303)

# 安裝

```bash
# Centos7 ffmpeg version 3.4
yum install epel-release
rpm -v --import http://li.nux.ro/download/nux/RPM-GPG-KEY-nux.ro
rpm -Uvh http://li.nux.ro/download/nux/dextop/el7/x86_64/nux-dextop-release-0-5.el7.nux.noarch.rpm
yum install ffmpeg ffmpeg-devel
# 解除安裝
yum remove ffmpeg -y

# Centos7 ffmpeg version 4.4 Linux Static Builds
# https://johnvansickle.com/ffmpeg/
# [ARM架構](https://zh.wikipedia.org/zh-tw/ARM%E6%9E%B6%E6%A7%8B)
# [x86-64](https://zh.wikipedia.org/zh-tw/X86-64)
wget https://www.johnvansickle.com/ffmpeg/old-releases/ffmpeg-4.4-amd64-static.tar.xz
# 解壓縮
tar xvf ffmpeg-4.4-amd64-static.tar.xz
# 刪除 壓縮包
rm -f ffmpeg-4.4-amd64-static.tar.xz
# 移動可執行文件到/usr/bin/方便系統調用
mv ffmpeg-4.4-amd64-static/ffmpeg  ffmpeg-4.4-amd64-static/ffprobe /usr/bin/
# 刪除 資料夾
rm -rf ffmpeg-4.4-amd64-static
# 檢查版本
ffmpeg -version

# 查看linux kernel
uname -r

uname -a
```

# 指令 ffmpeg

```bash
ffmpeg -formats：查看ffmpeg支援的格式
D=解碼，E=編碼

ffmpeg -i (檔案名稱)：查看檔案資訊，如解析度、bitrate

mp4轉檔範例(windows)：
ffmpeg -i will.MTS -s 640x480 -b:v 500k -vcodec libx264 -r 29.97 -acodec libvo_aacenc -b:a 48k -ac 2 -ar 44100 -profile:v baseline -level 3.0 -f mp4 -y will.mp4

mp4轉檔範例(linux)：
ffmpeg -i (檔案) -s 640x480 -b 500k -vcodec libx264 -r 29.97 -acodec libfaac -ab 48k -ac 2 -ar 44100 -profile baseline -level 3.0 -f mp4 -y (新檔名).mp4

-i : 指輸入的檔案名稱
-f : 強迫輸出的檔案格式。
-s : 畫面的解析度，格式是 wxh
-b：指定影像的bitrate
-vcodec : 指定影像的編碼格式
-r : 設定fps。
-acodec : 指定聲音的編碼格式
-ab : 指定聲音的bitrate
-ac : 設定聲音的聲道數。1指是的單聲道
-ar : 指定聲音的取樣頻率，一般預設是44100
-profile：指定profile
-level：指定level
-f：強迫輸出格式
-y：若檔名重覆即不詢問直接覆蓋
# 多線程
-threads 5 -preset ultrafast

wmv轉檔設定
-vcodec wmv2
-acodec wmav2

flv轉檔設定
-vcodec flv
-acodec libmp3lame

ffmpeg 影片切割
-ss 01:00:00 指定從01:00:00開始切割
-t 00:00:30 切割00:00:30秒
```

# 指令 ffprobe

```bash
# 查看音視頻文件的封裝格式
ffprobe -show_format inputFile
```

# HLS（HTTP Live Stream）

```
HLS（HTTP Live Stream）是蘋果推出的影音串流的標準，目前可支援大多數的行動裝置與電視盒，如何利用ffmpeg將直播或影片進行切片（chunk）轉成HLS格式。
```

```bash
ffmpeg \
	-i {video_path} \
	-c copy \
	-hls_segment_type mpegts \
	-hls_time 10 \
	-start_number 1 \
	-hls_key_info_file {keyinfo_path} \
	-hls_segment_filename {output_video_dir}/{output_video_name}_%05d.ts \
	-hls_list_size 0 \
	-hls_playlist_type vod \
	-hls_flags delete_segments+split_by_time {output_video_dir}/{output_video_name}.m3u8 -y


-start_number：從指定的數字開始序列。默認值為 1。

-hls_key_info_file key_info_file：

	Key info file format:
		key URI
		key file path
		IV (optional)

		Example key URIs:
			http://server/file.key
			file.key
			/path/to/file.key

		Example key file paths:
			file.key
			/path/to/file.key

		Example IV:
			0123456789ABCDEF0123456789ABCDEF

	Key info file example:
		http://server/file.key
		/path/to/file.key
		0123456789ABCDEF0123456789ABCDEF

	Example shell script:
		#!/bin/sh
		BASE_URL=${1:-'.'}
		openssl rand 16 > file.key
		echo $BASE_URL/file.key > file.keyinfo
		echo file.key >> file.keyinfo
		echo $(openssl rand -hex 16) >> file.keyinfo
		ffmpeg -f lavfi -re -i testsrc -c:v h264 -hls_flags delete_segments \
		  -hls_key_info_file file.keyinfo out.m3u8

-hls_segment_filename：
	格式："segment_%Y%m%d%H%M%S_%%04d_%%08s_%%013t.ts"
	顯示："segment_20170102194334_0003_00122200_0000003000000.ts",
		 "segment_20170102194334_0004_00120072_0000003000000.ts"

-hls_time：設定每個切片（chunk）的長度（秒），這裡設定為6秒一個切片。

-hls_segment_type: fmp4 or mpegts，切片的格式為mp4或者為mpeg-2 ts格式

-hls_list_size：
	設定playlist播放清單最多的內容，如果是0則無限制。
	因此為直播內容，可預先保留最大的值，這裡設定為10，就是會預先切個10個切片的播放清單，前面hls_time設定為6秒，切十個，就是預留60秒的內容進行播放。
	相對的，就有會將近60秒的延遲。

-hls_flags：有很多參數可用
	delete_segments: 在segment的持續時間加上播放列表(playlist)的持續時間之後的一段時間之後刪除從播放列表中刪除的段文件(segment)。

	append_list: 將新segment添加到舊segment列表的末尾，並從舊段列表中刪除＃EXT-X-ENDLIST。

	split_by_time: 允許segment在關鍵幀(key frame)以外的幀上啟動。 當關鍵幀之間的時間不一致時，這會改善某些玩家的行為，但可能會使其他播放器的情況變得更糟，並且在搜索過程中可能會導致一些奇怪的現象。 此標誌應與hls_time選項一起使用。

-hls_playlist_type:
	event，＃EXT-X-PLAYLIST-TYPE：m3u8標頭的EVENT。
	vod，#EXT-X-PLAYLIST-TYPE:VODm3u8：標頭中發出。
```
