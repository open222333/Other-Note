# Linux 工具 curl(對指定URL進行網路傳輸)

```
基於網路協定，對指定URL進行網路傳輸。
cURL涉及是任何網路協定傳輸，不涉及對具體資料的具體處理。
```

## 目錄

- [Linux 工具 curl(對指定URL進行網路傳輸)](#linux-工具-curl對指定url進行網路傳輸)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [測試 回傳速度](#測試-回傳速度)
	- [參考資料](#參考資料-1)

## 參考資料

[官方文檔 man page](https://curl.se/docs/manpage.html)

[proxy範例](http://username:password@host_ip:port)

# 指令

```bash
# 查看目前對外ip
curl ipinfo.io

# 使用proxy 開啟 url
curl -x (proxy) (url)
curl -v ip:port

# 測試port是否正常
curl -x host_ip:port google.com
	# -I
	# --head
	# 	僅獲取HTTP標頭 (HTTP/FTP/文件)
	# -v
	# --verbose
	# 	取得細節 主要用於調試
```

# 測試 回傳速度

## 參考資料

[How do I measure request and response times at once using cURL?](https://stackoverflow.com/questions/18215389/how-do-i-measure-request-and-response-times-at-once-using-curl)

```bash
curl -w "@curl-format.txt" -o /dev/null -s "http://wordpress.com/"
# -w "@curl-format.txt" 告訴 cURL 使用我們的格式文件
# -o /dev/null 將請求的輸出重定向到 /dev/null
# -s 告訴 cURL 不顯示進度表
# “http://wordpress.com/”是我們請求的 URL。如果您的 URL 有“&”查詢字符串參數，請使用引號

curl [url] -d 'json' -H 'Content-Type: application/json' -v -w "@curl-format.txt"
# -H 標頭
# -d json內容
```

`curl-format.txt`

```
time_namelookup:  %{time_namelookup}s\n
        time_connect:  %{time_connect}s\n
     time_appconnect:  %{time_appconnect}s\n
    time_pretransfer:  %{time_pretransfer}s\n
       time_redirect:  %{time_redirect}s\n
  time_starttransfer:  %{time_starttransfer}s\n
                     ----------\n
          time_total:  %{time_total}s\n
```
