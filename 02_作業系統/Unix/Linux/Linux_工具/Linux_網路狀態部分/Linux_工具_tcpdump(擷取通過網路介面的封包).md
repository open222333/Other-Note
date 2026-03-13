# Linux 工具 tcpdump(擷取通過網路介面的封包)

```
```

## 目錄

- [Linux 工具 tcpdump(擷取通過網路介面的封包)](#linux-工具-tcpdump擷取通過網路介面的封包)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)

## 參考資料

[tcpdump(8) - Linux man page](https://linux.die.net/man/8/tcpdump)

[文字介面封包擷取器： tcpdump](https://linux.vbird.org/linux_server/centos6/0140networkcommand.php#tcpdump)

# 安裝

```bash
```

# 指令

```bash
tcpdump [-AennqX] [-i 介面] [-w 儲存檔名] [-c 次數] [-r 檔案] [所欲擷取的封包資料格式]
	選項與參數：
	-A ：封包的內容以 ASCII 顯示，通常用來捉取 WWW 的網頁封包資料。
	-e ：使用資料連接層 (OSI 第二層) 的 MAC 封包資料來顯示；
	-nn：直接以 IP 及 port number 顯示，而非主機名與服務名稱
	-q ：僅列出較為簡短的封包資訊，每一行的內容比較精簡
	-X ：可以列出十六進位 (hex) 以及 ASCII 的封包內容，對於監聽封包內容很有用
	-i ：後面接要『監聽』的網路介面，例如 eth0, lo, ppp0 等等的介面；
	-w ：如果你要將監聽所得的封包資料儲存下來，用這個參數就對了！後面接檔名
	-r ：從後面接的檔案將封包資料讀出來。那個『檔案』是已經存在的檔案，
		並且這個『檔案』是由 -w 所製作出來的。
	-c ：監聽的封包數，如果沒有這個參數， tcpdump 會持續不斷的監聽，
		直到使用者輸入 [ctrl]-c 為止。
```
