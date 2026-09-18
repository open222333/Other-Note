# Chrome(Google瀏覽器)

```
```

## 目錄

- [Chrome(Google瀏覽器)](#chromegoogle瀏覽器)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [開法者工具相關](#開法者工具相關)
- [開發者工具](#開發者工具)
	- [查看 SSL證書 資訊](#查看-ssl證書-資訊)
	- [追蹤實際呼叫的 API (Network 分頁)](#追蹤實際呼叫的-api-network-分頁)

## 參考資料

### 開法者工具相關

[如何在谷歌浏览器Chrome查看SSL证书信息？](https://www.ovssl.cn/20181031-1.html)

[Android app 版做法（Android Studio Network Profiler / HTTP Toolkit）](../../行動應用程式(Mobile_App)/Android_網路抓包(HTTP攔截除錯).md)

# 開發者工具

## 查看 SSL證書 資訊

1. Chrome瀏覽器在右上角點擊`⋮`，然後點擊`更多工具`，進入`開發者工具`。

2. 進入開發者工具後選擇`Security`。

3. 點擊`View Certificate`查看證書。

## 追蹤實際呼叫的 API (Network 分頁)

用途：網頁畫面觸發某功能時，實際打了哪支 API、打到哪個網域。

1. `F12`（Mac 是 `Cmd+Option+I`）叫出 DevTools，切到 `Network` 分頁，勾選 `Fetch/XHR` 篩選器，先按左上角 🚫 清空舊紀錄
2. 實際去操作會觸發該 API 的功能，讓請求即時出現在清單裡（比用猜的準）
3. 在篩選欄輸入關鍵字（例如 API path 的一部分）篩掉不相關的資源請求
4. 點開單筆請求看 `Headers` 分頁的 `Request URL`，要看**完整網域**，不是只看 path
5. 往下看 `Response Headers` 找後端線索：`Server`、`X-Powered-By`、`Via`、`X-Request-Id` 等欄位常會洩漏內部服務名稱或版本
6. 切到 `Initiator` 欄位（或展開 call stack），往回追是哪支前端 JS 檔案、哪一行呼叫了這支 API，bundle 檔名/路徑常對應到專案名稱
7. 需要更確定網域身份時，用終端機驗證：

```bash
curl -v https://<網域>/xxx
# 或看 TLS 憑證的 CN/SAN
openssl s_client -connect <網域>:443 -servername <網域>
```
