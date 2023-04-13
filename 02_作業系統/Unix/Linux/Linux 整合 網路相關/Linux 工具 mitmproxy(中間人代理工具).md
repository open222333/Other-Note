# Linux 工具 mitmproxy(中間人代理工具)

```
mitmproxy 是 man-in-the-middle proxy 的簡稱，譯為中間人代理工具
mitmproxy是一個交互式的、支持 SSL/TLS 的攔截代理，具有用於 HTTP/1、HTTP/2 和 WebSockets 的控制台界面。
mitmweb是 mitmproxy 的基於 Web 的界面。
mitmdump是 mitmproxy 的命令行版本。想想 HTTP 的 tcpdump。

mitmproxy 工具有以下三部分組成

mitmproxy -> 命令列工具（win不支援）
mitmdump -> 載入 python 指令碼
mitmweb -> web 介面工具
	常用引數
	-h 幫助資訊
	-p 修改監聽埠
	-s 載入 python 指令碼
	預設監聽埠8080
```

## 目錄

- [Linux 工具 mitmproxy(中間人代理工具)](#linux-工具-mitmproxy中間人代理工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝步驟](#安裝步驟)
	- [MacOS](#macos)
- [手動安裝 CA 證書](#手動安裝-ca-證書)
- [指令](#指令)

## 參考資料

[官方網站](https://mitmproxy.org/)

[官方文檔](https://docs.mitmproxy.org/stable/)

[簡中文檔](https://ptorch.com/docs/10/mitmproxy_introduction)

[debian manpages](https://manpages.debian.org/jessie/mitmproxy/mitmproxy.1)

[python 使用範例](https://docs.mitmproxy.org/stable/addons-examples/)

# 安裝步驟

## MacOS

```bash
# 安裝
brew install mitmproxy
```

# 手動安裝 CA 證書

```
安裝後啟動 mitmproxy
連線mitmproxy(一般使用proxy的方式)
可使用 http://mitm.it/ 安裝證書
```

[About Certificates](https://docs.mitmproxy.org/stable/concepts-certificates/)

[关于证书](https://ptorch.com/docs/10/mitmproxy-concepts-certificates)

```bash
# 手動安裝證書
```

# 指令

```bash
# curl 通用平台的手動證書安裝文檔
curl --proxy 127.0.0.1:8080 --cacert ~/.mitmproxy/mitmproxy-ca-cert.pem https://example.com/
# wget 通用平台的手動證書安裝文檔
wget -e https_proxy=127.0.0.1:8080 --ca-certificate ~/.mitmproxy/mitmproxy-ca-cert.pem https://example.com/

# 命令列工具（win不支援）
mitmproxy -p 8999

# 載入 python 指令碼
# 錄製：
mitmdump -w 檔名
# 過濾：
mitmdump -nr 檔名 -w 檔名2 "~s hogwarts"
# 回放：
mitmdump -nC 檔名
	-s "script.py --bar" # 執行指令碼，通過雙引號來新增引數
	-n 不啟動代理
	-r 讀取檔案內容
	-w 寫入檔案
	~s 過濾響應資料
	~q 過濾請求資料
	https://docs.mitmproxy.org/stable/concepts-filters/

# web 介面工具
mitmweb -p 8999
	-h 幫助資訊
	-p 修改監聽埠 預設監聽埠8080
	-s 載入 python 指令碼
```