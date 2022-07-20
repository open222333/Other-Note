# Python 模組 gunicorn(WSGI Server)

```
Web伺服器閘道器介面
```

## 目錄

- [Python 模組 gunicorn(WSGI Server)](#python-模組-gunicornwsgi-server)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[官方文檔](https://docs.gunicorn.org/en/stable/run.html)

[配置文件](https://docs.gunicorn.org/en/stable/settings.html)

# 指令

```bash
# 安裝
pip install gunicorn

# 啟動
gunicorn -w 4 -b 0.0.0.0:8000 檔名:Flask()實例

	-c CONFIG
		配置文件的路徑，通過配置文件啟動生產環境使用

	-b ADDRESS
		ip加端口，綁定運行的主機

	-w INT,  --workers INT
		用於處理工作進程的數量，為正整數，默認為1

	-k STRTING, --worker-class STRTING
		要使用的工作模式，默認為sync異步，可以下載eventlet和gevent並指定

	--threads INT
		處理請求的工作線程數，使用指定數量的線程運行每個worker。為正整數，默認為1。

	--worker-connections INT
		最大客戶端併發數量，默認情況下這個值為1000。

	--backlog INT
		未決連接的最大數量，即等待服務的客戶的數量。默認2048個，一般不修改

	-p FILE, --pid FILE
		設置pid文件的文件名，如果不設置將不會創建pid文件

	--access-logfile FILE
		要寫入的訪問日誌目錄

	--access-logformat STRING
		要寫入的訪問日誌格式

	--error-logfile FILE, --log-file FILE
		要寫入錯誤日誌的文件目錄。

	--log-level LEVEL
		錯誤日誌輸出等級。

	--limit-request-line INT
		HTTP請求頭的行數的最大大小，此參數用於限制HTTP請求行的允許大小，默認情況下，這個值為4094。值是0~8190的數字。

	--limit-request-fields INT
		限制HTTP請求中請求頭字段的數量。此字段用於限制請求頭字段的數量以防止DDOS攻擊，默認情況下，這個值為100，這個值不能超過32768

	--limit-request-field-size INT
		限制HTTP請求中請求頭的大小，默認情況下這個值為8190字節。值是一個整數或者0，當該值為0時，表示將對請求頭大小不做限制

	-t INT, --timeout INT
		超過這麼多秒後工作將被殺掉，並重新啟動。一般設定為30秒

	--daemon
		是否以守護進程啟動，默認false

	--chdir
		在加載應用程序之前切換目錄

	--graceful-timeout INT
		默認情況下，這個值為30，在超時(從接收到重啟信號開始)之後仍然活著的工作將被強行殺死一般使用默認

	--keep-alive INT
		在keep-alive連接上等待請求的秒數，默認情況下值為2。一般設定在1~5秒之間。

	--reload
		默認為False。此設置用於開發，每當應用程序發生更改時，都會導致工作重新啟動。

	--spew
		打印服務器執行過的每一條語句，默認False。此選擇為原子性的，即要麼全部打印，要麼全部不打印

	--check-config
		顯示現在的配置，默認值為False，即顯示。

	-e ENV,  --env ENV
		設置環境變量
```