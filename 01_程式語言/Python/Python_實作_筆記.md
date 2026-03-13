# Python 實作 筆記

## 目錄

- [Python 實作 筆記](#python-實作-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [斷點續傳 原理](#斷點續傳-原理)
- [Python 技巧 Mac打包在其他作業系統環境](#python-技巧-mac打包在其他作業系統環境)
- [Python 技巧 進度條](#python-技巧-進度條)

## 參考資料

[python 下載檔案的多種方法彙總](https://www.it145.com/9/69039.html)

[Python實現斷點續傳下載檔案 requests模組](https://www.itread01.com/content/1546369412.html)

[headers Range 說明](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Range)


## 斷點續傳 原理

從文件已經下載的地方開始繼續下載

HTTP 請求頭：

```
Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-
excel, application/msword, application/vnd.ms-powerpoint, */*
Accept-Language: zh-cn
Accept-Encoding: gzip, deflate
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)
Connection: Keep-Alive
```

伺服器收到請求後

提取客戶端請求目標的信息返回給客戶端

返回`HTTP Header`如下：

```
200
Content-Length=106786027
Accept-Ranges=bytes
Date=Mon, 30 Apr 2001 12:56:11 GMT
ETag=W/"02ca57e173c11:95b"
Content-Type=application/octet-stream
Server=Microsoft-IIS/5.0
Last-Modified=Tue, 15 Oct 2019 08:40:36 GMT
```

客戶端瀏覽器傳給伺服器

HTTP請求頭中的`Range: bytes`

意思是告訴伺服器文件下載的位置從哪裡開始

此例子要求從2000070字節開始

`HTTP Header`:
```
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)
Range: bytes=2000070-
Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2
```

伺服器收到這個請求以後

返回的`HTTP Header`如下：

```
206
Content-Length=106786027
Content-Range=bytes 2000070-106786027/106786028
Date=Mon, 30 Apr 2001 12:55:20 GMT
ETag=W/"02ca57e173c11:95b"
Content-Type=application/octet-stream
Server=Microsoft-IIS/5.0
Last-Modified=Tue, 15 Oct 2019 08:48:08 GMT
```

HTTP請求頭有`Range: bytes`屬性

返回的信息增加`Content-Range`：

```
Content-Range=bytes 2000070-106786027/106786028
```

返回的狀態碼改爲`206`不再是`200`

# Python 技巧 Mac打包在其他作業系統環境

[Python 程式打包為執行檔.exe ( Mac OS )](https://medium.com/%E6%88%91%E5%B0%B1%E5%95%8F%E4%B8%80%E5%8F%A5-%E6%80%8E%E9%BA%BC%E5%AF%AB/python-%E5%B0%87%E7%A8%8B%E5%BC%8F%E6%89%93%E5%8C%85%E7%82%BA%E5%9F%B7%E8%A1%8C%E6%AA%94-exe-mac-os-e9521bc87e24)

# Python 技巧 進度條

[給Python程式碼加上酷炫進度條的幾種姿勢](https://www.gushiciku.cn/pl/pB2F/zh-tw)

[Python progress bar and downloads](https://stackoverflow.com/questions/15644964/python-progress-bar-and-downloads)

[python 下載檔案的多種方法彙總](https://www.it145.com/9/69039.html)