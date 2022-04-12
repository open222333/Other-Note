# Linux 工具 ab(網站伺服器壓力測試工具)

```
ApacheBench是一個極為輕量級的壓力測試工具，通常只要電腦有安裝Apache伺服器就會有這個工具。
ApacheBench僅能測試一個網頁所能承受的負載能力，並無法模擬真正使用者操作網站的複雜動作。
```

## 參考資料

`完整指令說明` : [ab - Apache HTTP server benchmarking tool](https://httpd.apache.org/docs/2.4/programs/ab.html)

[【軟體測試】使用 ApacheBench 進行網站伺服器壓力測試](https://blog.markgdi.com/article/stress-test-using-apache-bench/)

## 指令

```bash
ab http://127.0.0.1/
	-c concurrency
		併發
		同時向服務器端發送的請求數目，默認狀態下是一次 只執行一個http請求.
	-n requests
		總數
        執行一次測試會話的時候所發出的請求數目,默認是執行一個單一的請求
```